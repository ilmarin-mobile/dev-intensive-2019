package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.Plural
import java.text.SimpleDateFormat
import java.util.*

enum class TimeUnits(
    val value: Long,
    val plural: Plural
) {
    SECOND(1000L, Plural("секунд", "секунду", "секунды", "секунд")),
    MINUTE(60 * SECOND.value, Plural("минут", "минуту", "минуты", "минут")),
    HOUR(60 * MINUTE.value, Plural("часов", "час", "часа", "часов")),
    DAY(24 * HOUR.value, Plural("дней", "день", "дня", "дней"));

    fun getTimeValue(value: Int): Long =
        when(this) {
            SECOND -> value * SECOND.value
            MINUTE -> value * MINUTE.value
            HOUR -> value * HOUR.value
            DAY -> value * DAY.value
        }

    fun plural(value: Int): String =
        "${value} ${this.plural.getString(value)}"

    fun getTimeUnitCount(timeInMiliseconds: Long): Int =
        ((timeInMiliseconds)/this.value).toInt()

    fun pluralFromMiliseconds(timeInMiliseconds: Long): String =
        "${getTimeUnitCount(timeInMiliseconds)} ${this.plural.getString(getTimeUnitCount(timeInMiliseconds))}"

}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    var time = this.time

    time += units.getTimeValue(value)

    this.time = time

    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var timediff = this.time - date.time
    val pastTime = timediff < 0
    if (timediff < 0) {
        timediff *= -1
    }

    return when (timediff) {
        in 0 .. 1 * TimeUnits.SECOND.value  -> "только что"
        in 1 * TimeUnits.SECOND.value .. 45 * TimeUnits.SECOND.value-1 -> "несколько секунд".pastOrFuture(pastTime)
        in 45 * TimeUnits.SECOND.value .. 75 * TimeUnits.SECOND.value-1 -> "минуту".pastOrFuture(pastTime)
        in 75 * TimeUnits.SECOND.value .. 45 * TimeUnits.MINUTE.value-1 -> "${TimeUnits.MINUTE.pluralFromMiliseconds(timediff).pastOrFuture(pastTime)}"
        in 45 * TimeUnits.MINUTE.value .. 75 * TimeUnits.MINUTE.value-1 -> "час".pastOrFuture(pastTime)
        in 75 * TimeUnits.MINUTE.value .. 22 * TimeUnits.HOUR.value-1 -> "${TimeUnits.HOUR.pluralFromMiliseconds(timediff).pastOrFuture(pastTime)}"
        in 22 * TimeUnits.HOUR.value .. 26 * TimeUnits.HOUR.value-1 -> "день".pastOrFuture(pastTime)
        in 26 * TimeUnits.HOUR.value .. 360 * TimeUnits.DAY.value -> "${TimeUnits.DAY.pluralFromMiliseconds(timediff).pastOrFuture(pastTime)}"
        else -> if (pastTime) "более года назад" else "более чем через год"
    }
}

private fun String.pastOrFuture(pastTime: Boolean): String =
    if (pastTime) "${this} назад" else "через ${this}"