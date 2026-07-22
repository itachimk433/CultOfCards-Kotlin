package com.mkdev.cultofcardsword.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
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

/**
 * Renders the Dual Swordsman 3D model.
 * - Zoom is fully disabled (multi-touch blocked + camera is fixed).
 * - Vertical (pitch) rotation is locked — only horizontal (yaw) drag rotates the model.
 * - Camera is fixed and aimed at the model's centre so it always appears centred.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DualSwordsmanViewer(modifier: Modifier = Modifier) {
    val engine            = rememberEngine()
    val modelLoader       = rememberModelLoader(engine)
    val materialLoader    = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val childNodes        = rememberNodes()

    // Track accumulated horizontal rotation (degrees)
    var rotationY by remember { mutableFloatStateOf(0f) }
    var modelNode by remember { mutableStateOf<ModelNode?>(null) }

    // Load the GLB from assets/models/ once
    LaunchedEffect(Unit) {
        val instance = modelLoader.createModelInstance("models/dual_swordsman.glb")
        val node = ModelNode(
            modelInstance = instance,
            scaleToUnits  = 1.8f,
            centerOrigin  = Position(x = 0f, y = 0f, z = 0f)
        )
        childNodes += node
        modelNode = node
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF080810))
            // Capture drag gestures in the outer Box: only apply horizontal (yaw) rotation
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    // Accumulate only the X delta — Y is intentionally ignored
                    rotationY += dragAmount.x * 0.4f
                    modelNode?.rotation = Rotation(x = 0f, y = rotationY, z = 0f)
                }
            }
    ) {
        Scene(
            modifier = Modifier
                .fillMaxSize()
                // Block ALL touch events from reaching SceneView so:
                //  (a) pinch-to-zoom is impossible, and
                //  (b) the internal Manipulator cannot pitch the camera.
                .pointerInteropFilter { true },
            engine            = engine,
            modelLoader       = modelLoader,
            materialLoader    = materialLoader,
            environmentLoader = environmentLoader,
            childNodes        = childNodes,
            // Fixed camera — centred on the model, cannot be moved by touch
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition = Position(x = 0f, y = 0f, z = 3.5f),
                targetPosition    = Position(x = 0f, y = 0f, z = 0f),
            ),
        )
    }
}
