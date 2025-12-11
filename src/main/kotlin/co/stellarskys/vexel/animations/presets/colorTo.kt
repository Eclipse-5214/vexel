package co.stellarskys.vexel.animations.presets

import co.stellarskys.vexel.animations.extensions.animateColor
import co.stellarskys.vexel.animations.types.EasingType
import co.stellarskys.vexel.components.core.Rectangle
import co.stellarskys.vexel.components.core.SvgImage
import co.stellarskys.vexel.components.core.Text
import co.stellarskys.vexel.components.base.VexelElement
import java.awt.Color

fun <T : VexelElement<T>> T.colorTo(
    color: Int,
    duration: Long = 300,
    type: EasingType = EasingType.LINEAR,
    onComplete: (() -> Unit)? = null
): T {
    when (this) {
        is Rectangle -> animateColor({ backgroundColor }, { backgroundColor = it }, color, duration, type, onComplete)
        is Text -> animateColor({ textColor }, { textColor = it }, color, duration, type, onComplete)
        is SvgImage -> animateColor({ this.color.rgb }, { setSvgColor(Color(it, true)) }, color, duration, type, onComplete)
    }
    return this
}