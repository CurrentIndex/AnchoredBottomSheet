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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//internal abstract class AnchoredBottomSheetStateSize(val value: Float)
sealed class AnchoredBottomSheetStateSize {
    data class Fixed(val size: Dp) : AnchoredBottomSheetStateSize()
    data class Weight(val size: Float) : AnchoredBottomSheetStateSize()
}

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
    initialValue: AnchoredBottomSheetStateSize,
    confirmValueChange: (AnchoredBottomSheetStateSize) -> Boolean = { true },
    animationSpec: AnimationSpec<Float>,
    internal val anchors: List<AnchoredBottomSheetStateSize>,
) {

    @ExperimentalMaterial3Api
    @Suppress("Deprecation")
    constructor(
        density: Density,
        initialValue: AnchoredBottomSheetStateSize,
        confirmValueChange: (AnchoredBottomSheetStateSize) -> Boolean = { true },
        animationSpec: AnimationSpec<Float>,
        anchors: List<AnchoredBottomSheetStateSize>,
    ) : this(initialValue, confirmValueChange, animationSpec, anchors) {
        this.density = density
    }

    val currentValue: AnchoredBottomSheetStateSize get() = anchoredDraggableState.currentValue

    val targetValue: AnchoredBottomSheetStateSize get() = anchoredDraggableState.targetValue
    fun positionOf(value: AnchoredBottomSheetStateSize) = anchoredDraggableState.anchors.positionOf(value)
    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    suspend fun animateTo(
        targetValue: AnchoredBottomSheetStateSize,
        velocity: Float = anchoredDraggableState.lastVelocity,
    ) {
        anchoredDraggableState.animateTo(targetValue, velocity)
    }

    internal suspend fun snapTo(targetValue: AnchoredBottomSheetStateSize) {
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

    companion object {
        fun Saver(
            confirmValueChange: (AnchoredBottomSheetStateSize) -> Boolean,
            density: Density,
            animationSpec: AnimationSpec<Float>,
            anchors: List<AnchoredBottomSheetStateSize>,
        ) = Saver<AnchoredSheetState, Float>(
            save = {
                when (val currentValue = it.currentValue) {
                    is AnchoredBottomSheetStateSize.Weight -> currentValue.size
                    is AnchoredBottomSheetStateSize.Fixed -> with(density) { currentValue.size.toPx() }
                }
            },
            restore = { value ->
                val restoreAnchor = anchors.first {
                    when (val currentValue = it) {
                        is AnchoredBottomSheetStateSize.Weight -> currentValue.size == value
                        is AnchoredBottomSheetStateSize.Fixed -> with(density) { currentValue.size.toPx() } == value
                    }
                }
                AnchoredSheetState(density, restoreAnchor, confirmValueChange, animationSpec, anchors)
            }
        )
    }
}
