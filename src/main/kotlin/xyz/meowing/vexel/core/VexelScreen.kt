package xyz.meowing.vexel.core

import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniKeys
import dev.deftu.omnicore.api.client.input.OmniMouse
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.screen.KeyPressEvent
import dev.deftu.omnicore.api.client.screen.OmniScreen
import dev.deftu.omnicore.api.scheduling.TickSchedulers
import dev.deftu.textile.Text
import xyz.meowing.vexel.Vexel.eventBus
import xyz.meowing.vexel.events.GuiEvent
import xyz.meowing.vexel.events.api.EventCall

abstract class VexelScreen(screenName: String = "Vexel-Screen") : OmniScreen(Text.literal(screenName)) {
    private var lastX: Double = -1.0
    private var lastY: Double = -1.0

    var renderEvent: EventCall? = null

    var initialized = false
        private set
    var hasInitialized = false
        private set

    val window = VexelWindow()

    open fun afterInitialization() {}

    final override fun onInitialize(width: Int, height: Int) {
        if (!hasInitialized) {
            hasInitialized = true
            initialized = true

            afterInitialization()

            renderEvent = eventBus.register<GuiEvent.Render> {
                if (client?.currentScreen == this) {
                    window.draw()
                    onRenderGui()
                }
            }

        } else {
            initialized = true
        }

        super.onInitialize(width, height)
    }

    override fun onScreenClose() {
        window.cleanup()
        renderEvent?.unregister()
        renderEvent = null
        hasInitialized = false

        super.onScreenClose()
    }

    override fun onResize(width: Int, height: Int) {
        window.onWindowResize()
        super.onResize(width, height)
    }

    override fun onMouseClick(button: OmniMouseButton, x: Double, y: Double, modifiers: KeyboardModifiers): Boolean {
        return window.mouseClick(button.code)
    }

    override fun onMouseRelease(button: OmniMouseButton, x: Double, y: Double, modifiers: KeyboardModifiers): Boolean {
        return window.mouseRelease(button.code)
    }

    override fun onMouseScroll(x: Double, y: Double, amount: Double, horizontalAmount: Double): Boolean {
        window.mouseScroll(horizontalAmount, amount)
        return super.onMouseScroll(x, y, amount, horizontalAmount)
    }

    override fun onKeyPress(
        key: OmniKey,
        scanCode: Int,
        typedChar: Char,
        modifiers: KeyboardModifiers,
        event: KeyPressEvent
    ): Boolean {
        val handled = window.charType(key.code, scanCode, typedChar)
        if (!handled && key.code == OmniKeys.KEY_ESCAPE.code) close()
        return handled
    }

    /**
     * Called after the elements and animations render.
     */
    open fun onRenderGui() {}

    override fun onRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        val newX = OmniMouse.rawX
        val newY = OmniMouse.rawY
        if (newX != lastX || newY != lastY) {
            window.mouseMove()
            lastX = newX
            lastY = newY
        }

        super.onRender(ctx, mouseX, mouseY, tickDelta)
    }

    fun display() {
        TickSchedulers.client.post {
            client?.setScreen(this@VexelScreen)
        }
    }
}