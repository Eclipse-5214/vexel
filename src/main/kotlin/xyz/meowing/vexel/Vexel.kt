package xyz.meowing.vexel

import dev.deftu.omnicore.api.client.client
import net.minecraft.util.Identifier
import xyz.meowing.vexel.api.RenderAPI
import xyz.meowing.vexel.api.nvg.NVGRenderer
import xyz.meowing.vexel.api.style.Font
import xyz.meowing.vexel.events.api.EventBus

object Vexel {
    private var _renderer: RenderAPI? = null

    @JvmStatic
    val defaultFont = Font("Default", client.resourceManager.getResource(Identifier.of("vexel", "font.ttf")).get().inputStream)

    @JvmStatic
    val eventBus = EventBus()

    @JvmStatic
    val renderer: RenderAPI
        get() = _renderer ?: NVGRenderer

    fun init(renderer: RenderAPI) {
        _renderer = renderer
    }
}
