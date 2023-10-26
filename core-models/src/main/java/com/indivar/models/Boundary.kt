package com.indivar.models

data class Boundary (
    val type: BoundaryType,
    val count: Int,
)

enum class BoundaryType {
    Four, Six
}