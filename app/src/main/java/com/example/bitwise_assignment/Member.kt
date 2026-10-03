package com.example.bitwise_assignment

data class Member(
    val id: Int,
    val fullName: String,
    val nickname: String,
    val nim: String,
    val hobby: List<String>,
    val dream: String,
    val motto: String,
    val photoResId: Int
)

object MemberData {
    val members = listOf(
        Member(
            id = 0,
            fullName = "Athiyyah Dzatil Izzah",
            nickname = "Tiya",
            nim = "0102524008",
            hobby = listOf("Menggambar", "Membaca"),
            dream = "Berkarier di bidang teknologi & membawa manfaat",
            motto = "Carpe diem! seize the day, make your lives extraordinary.",
            photoResId = R.drawable.tiya
        ),
        Member(
            id = 1,
            fullName = "Devi Rahmawati",
            nickname = "Devi",
            nim = "0102524011",
            hobby = listOf("Musik", "Menggambar", "Kerajinan"),
            dream = "UI/UX Designer",
            motto = "Don’t be afraid to fail, be afraid to never try.",
            photoResId = R.drawable.devi
        ),
        Member(
            id = 2,
            fullName = "Nava Amanda",
            nickname = "Nava",
            nim = "0102524033",
            hobby = listOf("Traveling", "Makeup"),
            dream = "Punya pekerjaan yang sesuai dengan minat dan bisa sukses di masa depan",
            motto = "Terus belajar, terus berkembang, dan jangan takut mencoba hal baru.",
            photoResId = R.drawable.nava
        ),
        Member(
            id = 3,
            fullName = "Vidaufa Savanisa Akmal",
            nickname = "Sava",
            nim = "0102524037",
            hobby = listOf("Baking", "Musik"),
            dream = "UI/UX Designer",
            motto = "It’s okay to fall just don't fall apart .",
            photoResId = R.drawable.sava
        )
    )

    fun findById(id: Int): Member? = members.find { it.id == id }
}
