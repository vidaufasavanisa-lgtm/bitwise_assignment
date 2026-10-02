package com.example.bitwise_assignment

data class Member(
    val id: Int,
    val fullName: String,
    val nickname: String,
    val nim: String,
    val hobby: List<String>,
    val dream: String,
    val motto: String
)

object MemberData {
    val members = listOf(
        Member(
            id = 0,
            fullName = "Alya Maharani",
            nickname = "Alya",
            nim = "240001",
            hobby = listOf("Membaca", "Fotografi"),
            dream = "UI/UX Designer",
            motto = "Suka mencoba hal-hal baru dan terus belajar."
        ),
        Member(
            id = 1,
            fullName = "Bima Saputra",
            nickname = "Bima",
            nim = "240002",
            hobby = listOf("Main game", "Ngoding"),
            dream = "Android Developer",
            motto = "Pantang menyerah sebelum error hilang."
        ),
        Member(
            id = 2,
            fullName = "Citra Lestari",
            nickname = "Citra",
            nim = "240003",
            hobby = listOf("Menggambar", "Menulis"),
            dream = "Product Manager",
            motto = "Ide yang baik harus dieksekusi dengan baik."
        ),
        Member(
            id = 3,
            fullName = "Daffa Pratama",
            nickname = "Daffa",
            nim = "240004",
            hobby = listOf("Olahraga", "Musik"),
            dream = "Backend Engineer",
            motto = "Stabilitas adalah kunci dari segalanya."
        )
    )

    fun findById(id: Int): Member? = members.find { it.id == id }
}