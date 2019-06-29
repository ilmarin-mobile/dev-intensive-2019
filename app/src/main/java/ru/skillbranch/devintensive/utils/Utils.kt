package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.let {
            if (fullName.replace("\\s+".toRegex(), "").isNotEmpty())
                fullName.split(" ")
            else null
        }

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

}