package co.stellarskys.vexel.animations.extensions

import co.stellarskys.vexel.animations.impl.ColorAnimation
import co.stellarskys.vexel.animations.types.AnimationTarget
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.base.VexelElement

fun <T : VexelElement<T>> T.animateColor(
    getter: () -> Int,
    setter: (Int) -> Unit,
    endValue: Int,
    duration: Long,
    type: EasingType = EasingType.LINEAR,
    onComplete: (() -> Unit)? = null
): ColorAnimation {
    val target = AnimationTarget(getter(), endValue, setter)
    val animation = ColorAnimation(target, duration, type, "${hashCode()}+$endValue", onComplete)
    animation.start()
    return animation
}