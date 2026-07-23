package com.mkdev.cultofcardsword.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlin.math.sin

private const val ZOOM_MIN = 0.4f
private const val ZOOM_MAX = 2.8f

/**
 * Renders the Dual Swordsman 3D model with:
 * - Animated "Loading character…" overlay until the GLB is ready.
 * - Pinch-to-zoom (clamped between [ZOOM_MIN]x and [ZOOM_MAX]x model scale).
 * - Horizontal-only (yaw) rotation — pitch is locked.
 * - SceneView's own orbit/zoom manipulator is blocked; we drive everything ourselves.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DualSwordsmanViewer(modifier: Modifier = Modifier) {
    val engine            = rememberEngine()
    val modelLoader       = rememberModelLoader(engine)
    val materialLoader    = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val childNodes        = rememberNodes()

    var rotationY    by remember { mutableFloatStateOf(0f) }
    var zoomLevel    by remember { mutableFloatStateOf(1f) }
    var modelNode    by remember { mutableStateOf<ModelNode?>(null) }
    var baseScale    by remember { mutableFloatStateOf(1f) }
    var isLoading    by remember { mutableStateOf(true) }

    // Load GLB once
    LaunchedEffect(Unit) {
        val instance = modelLoader.createModelInstance("models/dual_swordsman.glb")
        val node = ModelNode(
            modelInstance = instance,
            // scaleToUnits auto-fits the model; 0.75 keeps it well inside the frame
            scaleToUnits  = 0.75f,
            centerOrigin  = Position(x = 0f, y = 0f, z = 0f)
        )
        childNodes += node
        // Capture the scale that scaleToUnits computed so we can multiply zooms on top
        baseScale = node.scale.x
        modelNode = node
        isLoading = false
    }

    // Loading overlay animations
    val infiniteTransition = rememberInfiniteTransition(label = "loader")
    val spinnerAngle by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label         = "spinner"
    )
    val dotStep by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 3f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label         = "dot_step"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF080810))
            // Single gesture detector handles both 1-finger drag (yaw) and pinch (zoom).
            // We feed results directly to the model node so SceneView's manipulator is bypassed.
            .pointerInput(Unit) {
                detectTransformGestures(panZoomLock = false) { _, pan, zoom, _ ->
                    // Horizontal pan → yaw rotation; vertical pan is ignored (pitch locked)
                    rotationY += pan.x * 0.45f

                    // Pinch zoom — multiply current zoom, clamp to [ZOOM_MIN, ZOOM_MAX]
                    zoomLevel = (zoomLevel * zoom).coerceIn(ZOOM_MIN, ZOOM_MAX)

                    val mn = modelNode ?: return@detectTransformGestures
                    mn.rotation = Rotation(x = 0f, y = rotationY, z = 0f)
                    val s = baseScale * zoomLevel
                    mn.scale = Scale(s, s, s)
                }
            }
    ) {
        Scene(
            modifier = Modifier
                .fillMaxSize()
                // Block ALL raw touch from reaching SceneView's orbit/zoom manipulator.
                // Our outer pointerInput already handles everything we need.
                .pointerInteropFilter { true },
            engine            = engine,
            modelLoader       = modelLoader,
            materialLoader    = materialLoader,
            environmentLoader = environmentLoader,
            childNodes        = childNodes,
            // Camera is fixed — SceneView can't move it because we block touch above.
            // z = 3.5 is the built-in default; the model appears smaller because
            // scaleToUnits = 0.75 rather than 1.8.
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition = Position(x = 0f, y = 0f, z = 3.5f),
                targetPosition    = Position(x = 0f, y = 0f, z = 0f),
            ),
        )

        // Loading overlay — covers the dark background until the model is ready
        if (isLoading) {
            Box(
                modifier         = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF080810)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Pulsing sword icon
                    Text(
                        text     = "⚔",
                        fontSize = 34.sp,
                        modifier = Modifier.alpha(
                            (0.6f + 0.4f * sin(Math.toRadians(spinnerAngle.toDouble())).toFloat())
                                .coerceIn(0f, 1f)
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text       = "Loading character...",
                        color      = Color(0xFFD4A017),
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign  = TextAlign.Center
                    )

                    Spacer(Modifier.height(10.dp))

                    // Bouncing gold dots
                    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        repeat(3) { idx ->
                            val isActive = dotStep.toInt() == idx
                            val dotAlpha by animateFloatAsState(
                                targetValue   = if (isActive) 1f else 0.28f,
                                animationSpec = tween(200),
                                label         = "dot_$idx"
                            )
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .alpha(dotAlpha)
                                    .background(Color(0xFFD4A017), shape = RoundedCornerShape(50))
                            )
                        }
                    }
                }
            }
        }
    }
}
