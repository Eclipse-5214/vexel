package co.stellarskys.vexel.components.core

import co.stellarskys.vexel.Vexel
import co.stellarskys.vexel.Vexel.renderer
import co.stellarskys.vexel.components.base.enums.Pos
import co.stellarskys.vexel.components.base.enums.Size
import co.stellarskys.vexel.components.base.VexelElement
import co.stellarskys.vexel.api.style.Font

class Text(
    var text: String = "",
    var textColor: Int = 0xFFFFFFFF.toInt(),
    var fontSize: Float = 12f,
    var shadowEnabled: Boolean = false,
    var font: Font = Vexel.defaultFont
) : VexelElement<Text>() {

    init {
        setSizing(Size.Auto, Size.Auto)
        setPositioning(Pos.ParentPixels, Pos.ParentPixels)
        ignoreMouseEvents()
    }

    override fun onRender(mouseX: Float, mouseY: Float) {
        if (text.isEmpty()) return

        if (shadowEnabled) renderer.shadowedText(text, x, y, fontSize, textColor, font)
        else renderer.text(text, x, y, fontSize, textColor, font)
    }

    override fun getAutoWidth(): Float = renderer.textWidth(text, fontSize, font)

    override fun getAutoHeight(): Float = fontSize

    fun text(newText: String): Text = apply {
        text = newText
    }

    fun color(color: Int): Text = apply {
        textColor = color
    }

    fun fontSize(size: Float): Text = apply {
        fontSize = size
    }

    fun font(newFont: Font): Text = apply {
        font = newFont
    }

    fun shadow(enabled: Boolean = true): Text = apply {
        shadowEnabled = enabled
    }
}