package co.stellarskys.vexel.animations.types

data class AnimationTarget<T>(
    val startValue: T,
    val endValue: T,
    val setter: (T) -> Unit
)