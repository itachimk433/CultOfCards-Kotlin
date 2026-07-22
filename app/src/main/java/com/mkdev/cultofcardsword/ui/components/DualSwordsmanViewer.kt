package com.mkdev.cultofcardsword.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlin.math.sin

/**
 * Renders the Dual Swordsman 3D model.
 * - Shows an animated "Loading character…" overlay until the GLB is ready.
 * - Zoom fully disabled (multi-touch blocked + camera fixed).
 * - Only horizontal (yaw) drag rotates the model — pitch is locked.
 * - Camera pulled back so the full figure fits in view.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DualSwordsmanViewer(modifier: Modifier = Modifier) {
    val engine            = rememberEngine()
    val modelLoader       = rememberModelLoader(engine)
    val materialLoader    = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val childNodes        = rememberNodes()

    var rotationY by remember { mutableFloatStateOf(0f) }
    var modelNode by remember { mutableStateOf<ModelNode?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Load GLB once; flip isLoading off as soon as the node is ready
    LaunchedEffect(Unit) {
        val instance = modelLoader.createModelInstance("models/dual_swordsman.glb")
        val node = ModelNode(
            modelInstance = instance,
            scaleToUnits  = 1.8f,
            centerOrigin  = Position(x = 0f, y = 0f, z = 0f)
        )
        childNodes += node
        modelNode = node
        isLoading = false
    }

    // Animations for the loading overlay
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
            // Capture only horizontal drag; rotate model node directly
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    rotationY += dragAmount.x * 0.4f
                    modelNode?.rotation = Rotation(x = 0f, y = rotationY, z = 0f)
                }
            }
    ) {
        Scene(
            modifier = Modifier
                .fillMaxSize()
                // Block ALL touch from SceneView: no zoom, no pitch from Manipulator
                .pointerInteropFilter { true },
            engine            = engine,
            modelLoader       = modelLoader,
            materialLoader    = materialLoader,
            environmentLoader = environmentLoader,
            childNodes        = childNodes,
            // Camera pulled back to show the full figure; fixed — cannot be moved by touch
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition = Position(x = 0f, y = 0f, z = 6.5f),
                targetPosition    = Position(x = 0f, y = 0f, z = 0f),
            ),
        )

        // Loading overlay — visible until model node is ready, then gone instantly
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
