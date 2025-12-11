package co.stellarskys.vexel.animations.extensions

import co.stellarskys.vexel.animations.impl.FloatAnimation
import co.stellarskys.vexel.animations.types.AnimationTarget
import co.stellarskys.vexel.animations.types.AnimationType
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.base.VexelElement

fun <T : VexelElement<T>> T.animateFloat(
    getter: () -> Float,
    setter: (Float) -> Unit,
    endValue: Float,
    duration: Long,
    type: EasingType = EasingType.LINEAR,
    animationType: AnimationType = AnimationType.CUSTOM,
    onComplete: (() -> Unit)? = null
): FloatAnimation {
    val target = AnimationTarget(getter(), endValue, setter)
    val animation = FloatAnimation(target, duration, type, animationType, "${hashCode()}+$endValue", onComplete)
    animation.start()
    return animation
}