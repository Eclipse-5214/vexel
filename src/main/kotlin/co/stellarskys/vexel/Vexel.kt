package co.stellarskys.vexel

import dev.deftu.omnicore.api.client.client
import co.stellarskys.vexel.api.RenderAPI
import co.stellarskys.vexel.api.nvg.NVGRenderer
import co.stellarskys.vexel.api.nvg.NVGSpecialRenderer
import co.stellarskys.vexel.api.style.Font
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry
import net.minecraft.resources.ResourceLocation

object Vexel: ClientModInitializer {
    private var _renderer: RenderAPI? = null

    @JvmStatic
    val defaultFont by lazy {
        val resource = client.resourceManager
            .getResource(ResourceLocation.fromNamespaceAndPath("vexel", "font.ttf"))
            .orElseThrow { IllegalStateException("Could not find vexel:font.ttf") }

        Font("Default", resource.open())
    }

    @JvmStatic
    val renderer: RenderAPI
        get() = _renderer ?: NVGRenderer

    fun init(renderer: RenderAPI) {
        _renderer = renderer
    }

    override fun onInitializeClient() {
        SpecialGuiElementRegistry.register { NVGSpecialRenderer(it.vertexConsumers()) }
    }
}
