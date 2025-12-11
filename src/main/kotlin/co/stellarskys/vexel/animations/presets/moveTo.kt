package co.stellarskys.vexel.animations.presets

import co.stellarskys.vexel.animations.extensions.animatePosition
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.base.VexelElement

fun <T : VexelElement<T>> T.moveTo(
    x: Float,
    y: Float,
    duration: Long = 500,
    type: EasingType = EasingType.EASE_OUT,
    onComplete: (() -> Unit)? = null
): T {
    animatePosition(x, y, duration, type, onComplete)
    return this
}