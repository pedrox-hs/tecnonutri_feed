package com.pedrenrique.tecnonutri.domain

data class Profile (
    val id: Int = 0,
    val imageUrl: String?,
    val name: String,
    val generalGoal: String
)