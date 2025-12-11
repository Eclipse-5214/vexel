package co.stellarskys.vexel.animations.impl

import co.stellarskys.vexel.animations.Animation
import co.stellarskys.vexel.animations.types.AnimationTarget
import co.stellarskys.vexel.animations.types.AnimationType
import co.stellarskys.vexel.animations.types.EasingType

class FloatAnimation(
    target: AnimationTarget<Float>,
    duration: Long,
    type: EasingType,
    animationType: AnimationType,
    elementId: String,
    onComplete: (() -> Unit)? = null
) : Animation<Float>(target, duration, type, animationType, elementId, onComplete) {
    override fun interpolate(start: Float, end: Float, progress: Float): Float = start + (end - start) * progress
}