package ru.skillbranch.devintensive.extensions

fun String.truncate(lastIndex: Int = 16): String {
    trim().let {
        if (it.length > lastIndex)
            it.subSequence(0, lastIndex).let { subStr ->
                if (subStr.length == it.length)
                    return subStr.toString()
                else
                    return "${subStr}..."
            }
        return it
    }
    return this
}

fun String.stripHtml(): String =
    replace(Regex("<.*?>|&.+;"), "").replace(Regex("\\s+"), " ")