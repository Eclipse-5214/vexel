package xyz.meowing.vexel.events

import xyz.meowing.vexel.events.api.Event

abstract class GuiEvent : Event() {
    class Render : GuiEvent()
}