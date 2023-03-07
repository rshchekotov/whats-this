package edu.tum.romance.whatsthis.util

val tokenSplitRegex = Regex("[\\s/]+")
val specialCharRegex = Regex("[.,!?:;\"'\\[\\](){}#@$%^&*+=<>`~]")
val yearRegex = Regex("1\\d{3}|2[0-2]\\d{2}")
val numberRegex = Regex("\\d+")
val mathRegex = Regex("\\{\\s*\\\\displaystyle")
val isbnRegex = Regex("([\\dX]{13}|[\\d\\-X]{17}|[\\dX]{10}|[\\d\\-X]{13})").with(specialCharRegex.any())
val dateRegex = Regex("\\d{4}-\\d{2}-\\d{2}").with(specialCharRegex.any())
val mailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}").with(specialCharRegex.any())
val urlRegex =
    Regex("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)").with(
        specialCharRegex.any()
    )

fun Regex.with(other: Regex) = Regex(this.pattern + other.pattern)
fun Regex.any() = Regex("(?:${this.pattern})*")