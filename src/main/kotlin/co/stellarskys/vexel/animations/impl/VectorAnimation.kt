package co.stellarskys.vexel.animations.impl

import co.stellarskys.vexel.animations.Animation
import co.stellarskys.vexel.animations.types.AnimationTarget
import co.stellarskys.vexel.animations.types.AnimationType
import co.stellarskys.vexel.animations.types.EasingType

class VectorAnimation(
    target: AnimationTarget<Pair<Float, Float>>,
    duration: Long,
    type: EasingType,
    animationType: AnimationType,
    elementId: String,
    onComplete: (() -> Unit)? = null
) : Animation<Pair<Float, Float>>(target, duration, type, animationType, elementId, onComplete) {
    override fun interpolate(start: Pair<Float, Float>, end: Pair<Float, Float>, progress: Float): Pair<Float, Float> {
        return (start.first + (end.first - start.first) * progress) to (start.second + (end.second - start.second) * progress)
    }
}