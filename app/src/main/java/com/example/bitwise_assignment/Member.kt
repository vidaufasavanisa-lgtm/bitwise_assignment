package com.example.bitwise_assignment

data class Member(
    val id: Int,
    val fullName: String,
    val nickname: String,
    val nim: String,
    val hobby: List<String>,
    val dream: String,
    val motto: String,
    val photoResId: Int,
    val videoResId: Int
)

object MemberData {
    val members = listOf(
        Member(
            id = 0,
            fullName = "Dmitri Prokovich Razumikhin",
            nickname = "Razumikhin",
            nim = "001",
            hobby = listOf("Menggambar", "Membaca"),
            dream = "UI/UX Designer",
            motto = "Suka mencoba hal-hal baru dan terus belajar.",
            photoResId = R.drawable.razumikhin,
            videoResId = R.raw.intro1
        ),
        Member(
            id = 1,
            fullName = "Rodion Raskolnikov",
            nickname = "Rodya",
            nim = "002",
            hobby = listOf("Main game", "Ngoding"),
            dream = "Android Developer",
            motto = "Pantang menyerah sebelum error hilang.",
            photoResId = R.drawable.raskolnikov,
            videoResId = R.raw.intro2
        ),
        Member(
            id = 2,
            fullName = "Rintarou Okabe",
            nickname = "Rintarou",
            nim = "003",
            hobby = listOf("Menggambar", "Menulis"),
            dream = "Product Manager",
            motto = "Ide yang baik harus dieksekusi dengan baik.",
            photoResId = R.drawable.rintarou,
            videoResId = R.raw.intro1
        ),
        Member(
            id = 3,
            fullName = "Dazai Osamu",
            nickname = "Dazai",
            nim = "004",
            hobby = listOf("Olahraga", "Musik"),
            dream = "Backend Engineer",
            motto = "Stabilitas adalah kunci dari segalanya.",
            photoResId = R.drawable.dazai,
            videoResId = R.raw.intro2
        )
    )

    fun findById(id: Int): Member? = members.find { it.id == id }
}