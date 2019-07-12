package ru.skillbranch.devintensive.models

class Plural(
    val zero: String,
    val one: String,
    val few: String,
    val many: String
) {

    fun getString(value: Int): String =
        if (value in 11 .. 14) many
        else when(value%10) {
            0 -> zero
            1 -> one
            in 2 .. 4 -> few
            else -> many
        }
}