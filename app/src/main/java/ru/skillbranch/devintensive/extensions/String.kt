package ru.skillbranch.devintensive.extensions

fun String.truncate(lastIndex: Int = 16): String {
    if (length > lastIndex)
        subSequence(0, lastIndex).trimEnd().let { subStr ->
            if (subStr.length == trimEnd().length)
                return subStr.toString()
            else
                return "${subStr}..."
        }

    return this
}