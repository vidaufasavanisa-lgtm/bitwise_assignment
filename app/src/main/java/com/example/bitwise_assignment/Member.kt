package com.example.bitwise_assignment

data class Member(
    val id: Int,
    val name: String,
    val role: String,
    val hobby: String,
    val dream: String,
    val about: String
)

object MemberData {
    val members = listOf(
        Member(
            id = 0,
            name = "Alya",
            role = "Informatics Student",
            hobby = "Membaca & fotografi",
            dream = "UI/UX Designer",
            about = "Suka mencoba hal-hal baru dan tertarik dengan desain digital."
        ),
        Member(
            id = 1,
            name = "Bima",
            role = "Informatics Student",
            hobby = "Main game & ngoding",
            dream = "Android Developer",
            about = "Senang membangun aplikasi kecil untuk memecahkan masalah sehari-hari."
        ),
        Member(
            id = 2,
            name = "Citra",
            role = "Informatics Student",
            hobby = "Menggambar & menulis",
            dream = "Product Manager",
            about = "Suka mengatur ide dan memastikan tim bergerak ke arah yang sama."
        ),
        Member(
            id = 3,
            name = "Daffa",
            role = "Informatics Student",
            hobby = "Olahraga & musik",
            dream = "Backend Engineer",
            about = "Tertarik dengan sistem server, API, dan keamanan data."
        )
    )

    fun findById(id: Int): Member? = members.find { it.id == id }
}