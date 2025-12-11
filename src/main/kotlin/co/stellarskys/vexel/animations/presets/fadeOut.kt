package co.stellarskys.vexel.animations.presets

import co.stellarskys.vexel.animations.extensions.animateColor
import co.stellarskys.vexel.animations.extensions.animateFloat
import co.stellarskys.vexel.animations.types.AnimationType
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.core.Rectangle
import co.stellarskys.vexel.components.core.SvgImage
import co.stellarskys.vexel.components.core.Text
import co.stellarskys.vexel.components.base.VexelElement
import java.awt.Color

fun <T : VexelElement<T>> T.fadeOut(
    duration: Long = 300,
    type: EasingType = EasingType.EASE_IN,
    includeChildren: Boolean = true,
    onComplete: (() -> Unit)? = null
): T {
    if (includeChildren) {
        children.forEach { child ->
            when (child) {
                is Rectangle -> child.fadeOut(duration, type, includeChildren)
                is Text -> child.fadeOut(duration, type, includeChildren)
                is SvgImage -> child.fadeOut(duration, type, includeChildren)
            }
        }
    }

    when (this) {
        is Rectangle -> {
            val targetBg = backgroundColor and 0x00FFFFFF
            val targetBorder = borderColor and 0x00FFFFFF
            animateColor({ backgroundColor }, { backgroundColor = it }, targetBg, duration, type) {
                visible = false
                onComplete?.invoke()
            }
            animateColor({ borderColor }, { borderColor = it }, targetBorder, duration, type)
        }
        is Text -> {
            val target = textColor and 0x00FFFFFF
            animateColor({ textColor }, { textColor = it }, target, duration, type) {
                visible = false
                onComplete?.invoke()
            }
        }
        is SvgImage -> {
            val target = color.rgb and 0x00FFFFFF
            animateColor({ color.rgb }, { setSvgColor(Color(it, true)) }, target, duration, type) {
                visible = false
                onComplete?.invoke()
            }
        }
        else -> animateFloat({ 1f }, {}, 0f, duration, type, AnimationType.ALPHA) {
            visible = false
            onComplete?.invoke()
        }
    }

    return this
}