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

    private val сyrillicToLatin = mapOf(
        "а" to "a",
        "б" to "b",
        "в" to "v",
        "г" to "g",
        "д" to "d",
        "е" to "e",
        "ё" to "e",
        "ж" to "zh",
        "з" to "z",
        "и" to "i",
        "й" to "i",
        "к" to "k",
        "л" to "l",
        "м" to "m",
        "н" to "n",
        "о" to "o",
        "п" to "p",
        "р" to "r",
        "с" to "s",
        "т" to "t",
        "у" to "u",
        "ф" to "f",
        "х" to "h",
        "ц" to "c",
        "ч" to "ch",
        "ш" to "sh",
        "щ" to "sh'",
        "ъ" to "",
        "ы" to "i",
        "ь" to "",
        "э" to "e",
        "ю" to "yu",
        "я" to "ya")

    fun transliteration(payload: String, divider: String = " "): String {
        val trasliterated = StringBuilder()
        payload.forEach {
            if (it.isWhitespace()) trasliterated.append(divider)
            else trasliterated.append(it.toString().toLatin())
        }
        return trasliterated.toString()
    }

    private fun String.toLatin(): String {
        return if (isCyrillic()) {
            if (!isFirstCharUpperCase())
                сyrillicToLatin[this]!!
            else
                сyrillicToLatin[toLowerCase()]!!.let { cyrillicResult ->
                    if (cyrillicResult.length == 1)
                        cyrillicResult.toUpperCase()
                    else
                        cyrillicResult.first().toUpperCase() + cyrillicResult.substring(1)
                }
        } else
            this.toString()
    }

    private fun String.isCyrillic(): Boolean = сyrillicToLatin.keys.any {
            char -> char == this || char.toUpperCase() == this
    }

    private fun String.isFirstCharUpperCase(): Boolean {
        return this[0].isUpperCase()
    }

}