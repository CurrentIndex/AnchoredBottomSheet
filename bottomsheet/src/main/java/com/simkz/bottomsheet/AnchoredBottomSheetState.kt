package com.simkz.bottomsheet


import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

abstract class AnchoredBottomSheetStateValue(val value: Float)


@Stable
@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
class AnchoredSheetState @Deprecated(
    message = "This constructor is deprecated. " +
            "Please use the constructor that provides a [Density]",
    replaceWith = ReplaceWith(
        "SheetState(" +
                "skipPartiallyExpanded, LocalDensity.current, initialValue, " +
                "confirmValueChange, skipHiddenState)"
    )
) constructor(
    initialValue: AnchoredBottomSheetStateValue,
    confirmValueChange: (AnchoredBottomSheetStateValue) -> Boolean = { true },
    animationSpec: AnimationSpec<Float>,
    internal val anchors: List<AnchoredBottomSheetStateValue>,
) {

    @ExperimentalMaterial3Api
    @Suppress("Deprecation")
    constructor(
        density: Density,
        initialValue: AnchoredBottomSheetStateValue,
        confirmValueChange: (AnchoredBottomSheetStateValue) -> Boolean = { true },
        animationSpec: AnimationSpec<Float>,
        anchors: List<AnchoredBottomSheetStateValue>,
    ) : this(initialValue, confirmValueChange, animationSpec, anchors) {
        this.density = density
    }

    val currentValue: AnchoredBottomSheetStateValue get() = anchoredDraggableState.currentValue

    val targetValue: AnchoredBottomSheetStateValue get() = anchoredDraggableState.targetValue
    fun positionOf(value: AnchoredBottomSheetStateValue) = anchoredDraggableState.anchors.positionOf(value)
    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    suspend fun animateTo(
        targetValue: AnchoredBottomSheetStateValue,
        velocity: Float = anchoredDraggableState.lastVelocity,
    ) {
        anchoredDraggableState.animateTo(targetValue, velocity)
    }

    internal suspend fun snapTo(targetValue: AnchoredBottomSheetStateValue) {
        anchoredDraggableState.snapTo(targetValue)
    }

    internal suspend fun settle(velocity: Float) {
        anchoredDraggableState.settle(velocity)
    }

    internal var anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = animationSpec,
        confirmValueChange = confirmValueChange,
        positionalThreshold = { with(requireDensity()) { 56.dp.toPx() } },
        velocityThreshold = { with(requireDensity()) { 125.dp.toPx() } }
    )

    internal var density: Density? = null

    private fun requireDensity() = requireNotNull(density) {
        "SheetState did not have a density attached. Are you using SheetState with " +
                "BottomSheetScaffold or ModalBottomSheet component?"
    }

    suspend fun maximum() {
        val anchor = anchors.reduce { acc, anchor -> if (acc.value > anchor.value) acc else anchor }
        animateTo(anchor)
    }

    suspend fun minimum() {
        val anchor = anchors.reduce { acc, anchor -> if (acc.value > anchor.value) anchor else acc }
        animateTo(anchor)
    }

    companion object {
        fun Saver(
            confirmValueChange: (AnchoredBottomSheetStateValue) -> Boolean,
            density: Density,
            animationSpec: AnimationSpec<Float>,
            anchors: List<AnchoredBottomSheetStateValue>,
        ) = Saver<AnchoredSheetState, Float>(
            save = { it.currentValue.value },
            restore = { savedValue ->
                val restoreAnchor = anchors.first { it.value == savedValue }
                AnchoredSheetState(density, restoreAnchor, confirmValueChange, animationSpec, anchors)
            }
        )
    }
}
