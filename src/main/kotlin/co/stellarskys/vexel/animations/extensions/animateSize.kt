package co.stellarskys.vexel.animations.extensions

import co.stellarskys.vexel.animations.impl.VectorAnimation
import co.stellarskys.vexel.animations.types.AnimationTarget
import co.stellarskys.vexel.animations.types.AnimationType
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.base.VexelElement

fun <T : VexelElement<T>> T.animateSize(
    endWidth: Float,
    endHeight: Float,
    duration: Long,
    type: EasingType = EasingType.LINEAR,
    onComplete: (() -> Unit)? = null
): VectorAnimation {
    val target = AnimationTarget(
        width to height,
        endWidth to endHeight
    ) { (w, h) ->
        width = w
        height = h
    }
    val animation = VectorAnimation(target, duration, type, AnimationType.SIZE, "${hashCode()}:size", onComplete)
    animation.start()
    return animation
}