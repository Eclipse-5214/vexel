package co.stellarskys.vexel.api.nvg

import co.stellarskys.vexel.api.RenderAPI
import co.stellarskys.vexel.api.style.Font
import co.stellarskys.vexel.api.style.Gradient
import co.stellarskys.vexel.api.style.Image
import java.awt.Color

object DummyRenderer: RenderAPI {
    override fun beginFrame(width: Float, height: Float, devicePixelRatio: Float) {
    }

    override fun endFrame() {
        
    }

    override fun push() {
        
    }

    override fun pop() {
        
    }

    override fun scale(x: Float, y: Float) {
        
    }

    override fun translate(x: Float, y: Float) {
        
    }

    override fun rotate(amount: Float) {
        
    }

    override fun globalAlpha(amount: Float) {
        
    }

    override fun pushScissor(x: Float, y: Float, w: Float, h: Float) {
        
    }

    override fun popScissor() {
        
    }

    override fun line(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        thickness: Float,
        color: Int
    ) {
        
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int) {
        
    }

    override fun rect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color: Int,
        radius: Float
    ) {
        
    }

    override fun rect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color: Int,
        radius: Float,
        roundTop: Boolean
    ) {
        
    }

    override fun rect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color: Int,
        topRight: Float,
        topLeft: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        
    }

    override fun gradientRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color1: Int,
        color2: Int,
        gradient: Gradient,
        radius: Float
    ) {
        
    }

    override fun hollowRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        thickness: Float,
        color: Int,
        radius: Float
    ) {
        
    }

    override fun hollowGradientRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        thickness: Float,
        color1: Int,
        color2: Int,
        gradient: Gradient,
        radius: Float
    ) {
        
    }

    override fun circle(x: Float, y: Float, radius: Float, color: Int) {
        
    }

    override fun dropShadow(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        blur: Float,
        spread: Float,
        shadowColor: Color,
        radius: Float
    ) {
        
    }

    override fun text(
        text: String,
        x: Float,
        y: Float,
        size: Float,
        color: Int,
        font: Font
    ) {
        
    }

    override fun wrappedText(
        text: String,
        x: Float,
        y: Float,
        w: Float,
        size: Float,
        color: Int,
        font: Font,
        lineHeight: Float
    ) {
        
    }

    override fun shadowedText(
        text: String,
        x: Float,
        y: Float,
        size: Float,
        color: Int,
        font: Font,
        shadowColor: Int,
        offsetX: Float,
        offsetY: Float,
        blur: Float
    ) {
        
    }

    override fun textWidth(
        text: String,
        size: Float,
        font: Font
    ): Float {
        return 0f
    }

    override fun textBounds(
        text: String,
        w: Float,
        size: Float,
        font: Font,
        lineHeight: Float
    ): FloatArray {
        return floatArrayOf(0f, 0f, 0f, 0f)
    }

    override fun image(
        image: Int,
        textureWidth: Int,
        textureHeight: Int,
        subX: Int,
        subY: Int,
        subW: Int,
        subH: Int,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        radius: Float
    ) {
        
    }

    override fun image(
        image: Image,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        radius: Float
    ) {
        
    }

    override fun image(
        image: Image,
        x: Float,
        y: Float,
        w: Float,
        h: Float
    ) {
        
    }

    override fun svg(
        id: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        a: Float
    ) {
        
    }

    override fun createImage(
        resourcePath: String,
        width: Int,
        height: Int,
        color: Color,
        id: String
    ): Image {
        return Image("")
    }

    override fun deleteImage(image: Image) {
        
    }

    override fun cleanCache() {
        
    }
}