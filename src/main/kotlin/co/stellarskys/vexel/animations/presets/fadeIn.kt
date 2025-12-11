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

fun <T : VexelElement<T>> T.fadeIn(
    duration: Long = 300,
    type: EasingType = EasingType.EASE_OUT,
    includeChildren: Boolean = true,
    onComplete: (() -> Unit)? = null
): T {
    visible = true

    if (includeChildren) {
        children.forEach { child ->
            when (child) {
                is Rectangle -> child.fadeIn(duration, type, includeChildren)
                is Text -> child.fadeIn(duration, type, includeChildren)
                is SvgImage -> child.fadeIn(duration, type, includeChildren)
            }
        }
    }

    when (this) {
        is Rectangle -> {
            val targetBg = (backgroundColor and 0x00FFFFFF) or (255 shl 24)
            val targetBorder = (borderColor and 0x00FFFFFF) or (255 shl 24)
            backgroundColor = backgroundColor and 0x00FFFFFF
            borderColor = borderColor and 0x00FFFFFF
            animateColor({ backgroundColor }, { backgroundColor = it }, targetBg, duration, type, onComplete)
            animateColor({ borderColor }, { borderColor = it }, targetBorder, duration, type)
        }
        is Text -> {
            val target = (textColor and 0x00FFFFFF) or (255 shl 24)
            textColor = textColor and 0x00FFFFFF
            animateColor({ textColor }, { textColor = it }, target, duration, type, onComplete)
        }
        is SvgImage -> {
            val target = (color.rgb and 0x00FFFFFF) or (255 shl 24)
            animateColor({ color.rgb }, { setSvgColor(Color(it, true)) }, target, duration, type, onComplete)
        }
        else -> animateFloat({ 0f }, {}, 1f, duration, type, AnimationType.ALPHA, onComplete)
    }

    return this
}
