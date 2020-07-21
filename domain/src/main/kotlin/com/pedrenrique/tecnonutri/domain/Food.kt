package com.pedrenrique.tecnonutri.domain

data class Food(
    val description: String? = null,
    val measure: String? = null,
    val amount: Double? = null,
    val weight: Double? = null,
    val energy: Double? = null,
    val carbohydrate: Double? = null,
    val fat: Double? = null,
    val protein: Double? = null
)