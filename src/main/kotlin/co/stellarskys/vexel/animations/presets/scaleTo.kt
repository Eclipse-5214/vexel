package co.stellarskys.vexel.animations.presets

import co.stellarskys.vexel.animations.extensions.animateSize
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.base.VexelElement

fun <T : VexelElement<T>> T.scaleTo(
    width: Float,
    height: Float,
    duration: Long = 300,
    type: EasingType = EasingType.EASE_OUT,
    onComplete: (() -> Unit)? = null
): T {
    animateSize(width, height, duration, type, onComplete)
    return this
}