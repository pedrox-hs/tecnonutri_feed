package br.com.pedrosilva.tecnonutri.domain.entities

data class Profile (
    val id: Int = 0,
    val imageUrl: String?,
    val name: String,
    val generalGoal: String
)