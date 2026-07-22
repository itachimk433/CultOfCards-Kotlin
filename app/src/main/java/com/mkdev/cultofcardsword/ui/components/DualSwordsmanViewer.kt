package com.mkdev.cultofcardsword.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes

/**
 * Renders the Dual Swordsman 3D model with free orbit rotation.
 * Pinch-to-zoom is intentionally disabled — fixed camera distance only.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DualSwordsmanViewer(modifier: Modifier = Modifier) {
    val engine            = rememberEngine()
    val modelLoader       = rememberModelLoader(engine)
    val materialLoader    = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val nodes             = rememberNodes()

    // Load the GLB from assets/models/ once
    LaunchedEffect(Unit) {
        val instance = modelLoader.createModelInstance("models/dual_swordsman.glb")
        nodes += ModelNode(
            modelInstance = instance,
            scaleToUnits  = 1.8f,
            centerOrigin  = Position(y = -0.5f)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF080810))
    ) {
        Scene(
            modifier = Modifier
                .fillMaxSize()
                // Consume all multi-touch events so pinch-to-zoom is disabled.
                // Single-finger drag still reaches SceneView for orbit rotation.
                .pointerInteropFilter { event -> event.pointerCount > 1 },
            engine            = engine,
            modelLoader       = modelLoader,
            materialLoader    = materialLoader,
            environmentLoader = environmentLoader,
            nodes             = nodes,
            // Fixed camera position — users orbit around the model but cannot zoom
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition   = Position(x = 0f, y = 0.5f, z = 3.5f),
                orbitTargetPosition = Position(x = 0f, y = 0f,   z = 0f),
            ),
        )
    }
}
