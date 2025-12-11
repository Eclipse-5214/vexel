package co.stellarskys.vexel.events.internal

import co.stellarskys.vexel.components.base.VexelElement

sealed class KeyEvent {
    class Type(
        val keyCode: Int,
        val scanCode: Int,
        val char: Char,
        val element: VexelElement<*>
    )
}