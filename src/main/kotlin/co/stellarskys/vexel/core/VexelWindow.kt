package co.stellarskys.vexel.core

import dev.deftu.omnicore.api.client.input.OmniMouse
import co.stellarskys.vexel.animations.AnimationManager
import co.stellarskys.vexel.components.base.VexelElement

class VexelWindow {
    val children: MutableList<VexelElement<*>> = mutableListOf()

    fun addChild(element: VexelElement<*>) {
        element.parent = this
        children.add(element)
    }

    fun removeChild(element: VexelElement<*>) {
        element.parent = null
        children.remove(element)
    }

    fun draw() {
        children.forEach { it.render(OmniMouse.rawX.toFloat(), OmniMouse.rawY.toFloat()) }
        AnimationManager.update()
    }

    fun mouseClick(button: Int): Boolean {
        return children.reversed().any { it.handleMouseClick(OmniMouse.rawX.toFloat(), OmniMouse.rawY.toFloat(), button) }
    }

    fun mouseRelease(button: Int): Boolean {
        return children.reversed().any { it.handleMouseRelease(OmniMouse.rawX.toFloat(), OmniMouse.rawY.toFloat(), button) }
    }

    fun mouseMove() {
        children.reversed().forEach { it.handleMouseMove(OmniMouse.rawX.toFloat(), OmniMouse.rawY.toFloat()) }
    }

    fun mouseScroll(horizontalDelta: Double, verticalDelta: Double) {
        children.reversed().forEach { it.handleMouseScroll(OmniMouse.rawX.toFloat(), OmniMouse.rawY.toFloat(), horizontalDelta, verticalDelta) }
    }

    fun charType(keyCode: Int, scanCode: Int, charTyped: Char): Boolean {
        return children.reversed().any { it.handleCharType(keyCode, scanCode, charTyped) }
    }

    fun onWindowResize() {
        children.forEach { it.onWindowResize() }
    }

    fun cleanup() {
        children.toList().forEach { it.destroy() }
        children.clear()
        AnimationManager.clear()
    }
}