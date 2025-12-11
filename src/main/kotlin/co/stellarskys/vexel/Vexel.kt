package co.stellarskys.vexel

import dev.deftu.omnicore.api.client.client
import net.minecraft.util.Identifier
import co.stellarskys.vexel.api.RenderAPI
import co.stellarskys.vexel.api.nvg.NVGRenderer
import co.stellarskys.vexel.api.style.Font
import co.stellarskys.vexel.events.api.EventBus

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
