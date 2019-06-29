package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

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

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstNameInitial = getInitial(firstName)
        val lastNameInitial = getInitial(lastName)
        val initials = StringBuilder()
        firstNameInitial?.run { initials.append(firstNameInitial) }
        lastNameInitial?.run { initials.append(lastNameInitial) }

        return if (initials.toString().isNotEmpty()) initials.toString() else null
    }

    private fun getInitial(someString: String?) =
        someString?.replace(" ", "")?.toUpperCase()?.firstOrNull()

}