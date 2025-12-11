package co.stellarskys.vexel.events

import co.stellarskys.vexel.events.api.Event

abstract class GuiEvent : Event() {
    class Render : GuiEvent()
}