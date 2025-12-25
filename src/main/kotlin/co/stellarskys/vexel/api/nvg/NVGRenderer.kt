@file:Suppress("UNUSED")

package co.stellarskys.vexel.api.nvg

import co.stellarskys.vexel.api.RenderAPI
import co.stellarskys.vexel.api.style.Font
import co.stellarskys.vexel.api.style.Gradient
import co.stellarskys.vexel.api.style.Image
import co.stellarskys.vexel.api.style.Color.Companion.red
import co.stellarskys.vexel.api.style.Color.Companion.green
import co.stellarskys.vexel.api.style.Color.Companion.blue
import co.stellarskys.vexel.api.style.Color.Companion.alpha
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoSVG.*
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryUtil.memAlloc
import org.lwjgl.system.MemoryUtil.memFree
import java.awt.Color
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

//#if MC >= 1.21.9
//$$ import org.lwjgl.opengl.GL13
//#endif

/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2023-2025, odtheking
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Portions of this file are derived from OdinFabric
 * Copyright (c) odtheking
 * Licensed under BSD-3-Clause
 *
 * Modifications and additions:
 * Licensed under GPL-3.0
 */
object NVGRenderer : RenderAPI {
    private val nvgPaint = NVGPaint.malloc()
    private val nvgColor = NVGColor.malloc()
    private val nvgColor2 = NVGColor.malloc()

    private val fontMap = HashMap<Font, NVGFont>()
    private val fontBounds = FloatArray(4)

    private val images = HashMap<Image, NVGImage>()

    private var scissor: Scissor? = null
    private var drawing: Boolean = false
    private var vg = -1L

    init {
        vg = nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)
        require(vg != -1L) { "Failed to initialize NanoVG" }
    }

    override fun beginFrame(width: Float, height: Float) {
        if (drawing) throw IllegalStateException("[NVGRenderer] Already drawing, but called beginFrame")

        nvgBeginFrame(vg, width, height, 1f)
        nvgTextAlign(vg, NVG_ALIGN_LEFT or NVG_ALIGN_TOP)
        drawing = true
    }

    override fun endFrame() {
        if (!drawing) throw IllegalStateException("[NVGRenderer] Not drawing, but called endFrame")
        nvgEndFrame(vg)

        drawing = false
    }

    override fun push() = nvgSave(vg)

    override fun pop() = nvgRestore(vg)

    override fun scale(x: Float, y: Float) = nvgScale(vg, x, y)

    override fun translate(x: Float, y: Float) = nvgTranslate(vg, x, y)

    override fun rotate(amount: Float) = nvgRotate(vg, amount)

    override fun globalAlpha(amount: Float) = nvgGlobalAlpha(vg, amount.coerceIn(0f, 1f))

    override fun pushScissor(x: Float, y: Float, w: Float, h: Float) {
        scissor = Scissor(scissor, x, y, w + x, h + y)
        scissor?.applyScissor()
    }

    override fun popScissor() {
        nvgResetScissor(vg)
        scissor = scissor?.previous
        scissor?.applyScissor()
    }

    override fun line(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Int) {
        nvgBeginPath(vg)
        nvgMoveTo(vg, x1, y1)
        nvgLineTo(vg, x2, y2)
        nvgStrokeWidth(vg, thickness)
        color(color)
        nvgStrokeColor(vg, nvgColor)
        nvgStroke(vg)
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, radius: Float, roundTop: Boolean) {
        nvgBeginPath(vg)

        if (roundTop) {
            nvgMoveTo(vg, x, y + h)
            nvgLineTo(vg, x + w, y + h)
            nvgLineTo(vg, x + w, y + radius)
            nvgArcTo(vg, x + w, y, x + w - radius, y, radius)
            nvgLineTo(vg, x + radius, y)
            nvgArcTo(vg, x, y, x, y + radius, radius)
            nvgLineTo(vg, x, y + h)
        } else {
            nvgMoveTo(vg, x, y)
            nvgLineTo(vg, x + w, y)
            nvgLineTo(vg, x + w, y + h - radius)
            nvgArcTo(vg, x + w, y + h, x + w - radius, y + h, radius)
            nvgLineTo(vg, x + radius, y + h)
            nvgArcTo(vg, x, y + h, x, y + h - radius, radius)
            nvgLineTo(vg, x, y)
        }

        nvgClosePath(vg)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgFill(vg)
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, radius: Float) {
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h + .5f, radius)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgFill(vg)
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int) {
        nvgBeginPath(vg)
        nvgRect(vg, x, y, w, h + .5f)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgFill(vg)
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, topRight: Float, topLeft: Float, bottomRight: Float, bottomLeft: Float) {
        nvgBeginPath(vg)
        nvgRoundedRectVarying(vg, round(x), round(y), round(w), round(h), topRight, topLeft, bottomRight, bottomLeft)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgFill(vg)
    }

    override fun hollowRect(x: Float, y: Float, w: Float, h: Float, thickness: Float, color: Int, radius: Float) {
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h, radius)
        nvgStrokeWidth(vg, thickness)
        nvgPathWinding(vg, NVG_HOLE)
        color(color)
        nvgStrokeColor(vg, nvgColor)
        nvgStroke(vg)
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
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h, radius)
        nvgStrokeWidth(vg, thickness)

        // Gradient stroke
        gradient(color1, color2, x, y, w, h, gradient)
        nvgStrokePaint(vg, nvgPaint)
        nvgStroke(vg)
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
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h, radius)
        gradient(color1, color2, x, y, w, h, gradient)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun dropShadow(x: Float, y: Float, width: Float, height: Float, blur: Float, spread: Float, shadowColor: Color, radius: Float) {
        val r = shadowColor.red.toByte()
        val g = shadowColor.green.toByte()
        val b = shadowColor.blue.toByte()

        nvgRGBA(r, g, b, 125, nvgColor)
        nvgRGBA(r, g, b, 0, nvgColor2)

        nvgBoxGradient(
            vg,
            x - spread,
            y - spread,
            width + 2 * spread,
            height + 2 * spread,
            radius + spread,
            blur,
            nvgColor,
            nvgColor2,
            nvgPaint
        )
        nvgBeginPath(vg)
        nvgRoundedRect(
            vg,
            x - spread - blur,
            y - spread - blur,
            width + 2 * spread + 2 * blur,
            height + 2 * spread + 2 * blur,
            radius + spread
        )
        nvgRoundedRect(vg, x, y, width, height, radius)
        nvgPathWinding(vg, NVG_HOLE)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun circle(x: Float, y: Float, radius: Float, color: Int) {
        nvgBeginPath(vg)
        nvgCircle(vg, x, y, radius)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgFill(vg)
    }

    override fun text(text: String, x: Float, y: Float, size: Float, color: Int, font: Font) {
        nvgFontSize(vg, size)
        nvgFontFaceId(vg, getFontID(font))
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgText(vg, x, y + .5f, text)
    }

    override fun shadowedText(text: String, x: Float, y: Float, size: Float, color: Int, font: Font, shadowColor: Int, offsetX: Float, offsetY: Float, blur: Float) {
        nvgFontFaceId(vg, getFontID(font))
        nvgFontSize(vg, size)

        nvgFontBlur(vg, blur)
        color(shadowColor)
        nvgFillColor(vg, nvgColor)
        nvgText(vg, x + offsetX, y + offsetY, text)

        nvgFontBlur(vg, 0f)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgText(vg, x, y + .5f, text)
    }

    override fun textWidth(text: String, size: Float, font: Font): Float {
        nvgFontSize(vg, size)
        nvgFontFaceId(vg, getFontID(font))
        return nvgTextBounds(vg, 0f, 0f, text, fontBounds)
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
        nvgFontSize(vg, size)
        nvgFontFaceId(vg, getFontID(font))
        nvgTextLineHeight(vg, lineHeight)
        color(color)
        nvgFillColor(vg, nvgColor)
        nvgTextBox(vg, x, y, w, text)
    }

    override fun textBounds(
        text: String,
        w: Float,
        size: Float,
        font: Font,
        lineHeight: Float
    ): FloatArray {
        val bounds = FloatArray(4)
        nvgFontSize(vg, size)
        nvgFontFaceId(vg, getFontID(font))
        nvgTextLineHeight(vg, lineHeight)
        nvgTextBoxBounds(vg, 0f, 0f, w, text, bounds)
        return bounds // [minX, minY, maxX, maxY]
    }

    override fun image(image: Int, textureWidth: Int, textureHeight: Int, subX: Int, subY: Int, subW: Int, subH: Int, x: Float, y: Float, w: Float, h: Float, radius: Float) {
        if (image == -1) return

        val sx = subX.toFloat() / textureWidth
        val sy = subY.toFloat() / textureHeight
        val sw = subW.toFloat() / textureWidth
        val sh = subH.toFloat() / textureHeight

        val iw = w / sw
        val ih = h / sh
        val ix = x - iw * sx
        val iy = y - ih * sy

        nvgImagePattern(vg, ix, iy, iw, ih, 0f, image, 1f, nvgPaint)
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h + .5f, radius)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun image(image: Image, x: Float, y: Float, w: Float, h: Float, radius: Float) {
        nvgImagePattern(vg, x, y, w, h, 0f, getImage(image), 1f, nvgPaint)
        nvgBeginPath(vg)
        nvgRoundedRect(vg, x, y, w, h + .5f, radius)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun image(image: Image, x: Float, y: Float, w: Float, h: Float) {
        nvgImagePattern(vg, x, y, w, h, 0f, getImage(image), 1f, nvgPaint)
        nvgBeginPath(vg)
        nvgRect(vg, x, y, w, h + .5f)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun svg(id: String, x: Float, y: Float, w: Float, h: Float, a: Float) {
        val nvg = getImage(Image(id))

        nvgImagePattern(vg, x, y, w, h, 0f, nvg, a, nvgPaint)
        nvgBeginPath(vg)
        nvgRect(vg, x, y, w, h + .5f)
        nvgFillPaint(vg, nvgPaint)
        nvgFill(vg)
    }

    override fun createImage(resourcePath: String, width: Int, height: Int, color: Color, id: String): Image {
        val image = Image(resourcePath)

        if (image.isSVG) {
            images.getOrPut(image) { NVGImage(0, loadSVG(image, width, height, color)) }.count++
        } else {
            images.getOrPut(image) { NVGImage(0, loadImage(image)) }.count++
        }
        return image
    }

    override fun cleanCache() {
        val iter = images.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            nvgDeleteImage(vg, entry.value.nvg)
            iter.remove()
        }
    }

    // lowers reference count by 1, if it reaches 0 it gets deleted from mem
    override fun deleteImage(image: Image) {
        val nvgImage = images[image] ?: return
        nvgImage.count--
        if (nvgImage.count == 0) {
            nvgDeleteImage(vg, nvgImage.nvg)
            images.remove(image)
        }
    }

    private fun getImage(image: Image): Int {
        return images[image]?.nvg ?: throw IllegalStateException("Image (${image.identifier}) doesn't exist")
    }

    private fun loadImage(image: Image): Int {
        val w = IntArray(1)
        val h = IntArray(1)
        val channels = IntArray(1)
        val buffer = STBImage.stbi_load_from_memory(
            image.buffer(),
            w,
            h,
            channels,
            4
        ) ?: throw NullPointerException("Failed to load image: ${image.identifier}")
        return nvgCreateImageRGBA(vg, w[0], h[0], 0, buffer)
    }

    private fun loadSVG(image: Image, svgWidth: Int, svgHeight: Int, color: Color): Int {
        val vec = image.stream.use { it.bufferedReader().readText() }
        val svg = nsvgParse(vec, "px", 96f) ?: throw IllegalStateException("Failed to parse ${image.identifier}")

        val width = svg.width().toInt()
        val height = svg.height().toInt()
        val buffer = memAlloc(width * height * 4)

        try {
            val rasterizer = nsvgCreateRasterizer()
            nsvgRasterize(rasterizer, svg, 0f, 0f, 1f, buffer, width, height, width * 4)
            val nvgImage = nvgCreateImageRGBA(vg, width, height, 0, buffer)
            nsvgDeleteRasterizer(rasterizer)
            return nvgImage
        } finally {
            nsvgDelete(svg)
            memFree(buffer)
        }
    }

    private fun color(color: Int) {
        nvgRGBA(color.red.toByte(), color.green.toByte(), color.blue.toByte(), color.alpha.toByte(), nvgColor)
    }

    private fun color(color1: Int, color2: Int) {
        nvgRGBA(
            color1.red.toByte(),
            color1.green.toByte(),
            color1.blue.toByte(),
            color1.alpha.toByte(),
            nvgColor
        )
        nvgRGBA(
            color2.red.toByte(),
            color2.green.toByte(),
            color2.blue.toByte(),
            color2.alpha.toByte(),
            nvgColor2
        )
    }

    private fun gradient(color1: Int, color2: Int, x: Float, y: Float, w: Float, h: Float, direction: Gradient) {
        color(color1, color2)
        when (direction) {
            Gradient.LeftToRight -> nvgLinearGradient(vg, x, y, x + w, y, nvgColor, nvgColor2, nvgPaint)
            Gradient.TopToBottom -> nvgLinearGradient(vg, x, y, x, y + h, nvgColor, nvgColor2, nvgPaint)
            Gradient.TopLeftToBottomRight -> nvgLinearGradient(vg, x, y, x + w, y + h, nvgColor, nvgColor2, nvgPaint)
        }
    }

    private fun getFontID(font: Font): Int {
        return fontMap.getOrPut(font) {
            val buffer = font.buffer()
            NVGFont(nvgCreateFontMem(
                vg,
                font.name,
                buffer,
                false
            ), buffer)
        }.id
    }

    private class Scissor(val previous: Scissor?, val x: Float, val y: Float, val maxX: Float, val maxY: Float) {
        fun applyScissor() {
            if (previous == null) nvgScissor(vg, x, y, maxX - x, maxY - y)
            else {
                val x = max(x, previous.x)
                val y = max(y, previous.y)
                val width = max(0f, (min(maxX, previous.maxX) - x))
                val height = max(0f, (min(maxY, previous.maxY) - y))
                nvgScissor(vg, x, y, width, height)
            }
        }
    }

    private data class NVGImage(var count: Int, val nvg: Int)
    private data class NVGFont(val id: Int, val buffer: ByteBuffer)
}