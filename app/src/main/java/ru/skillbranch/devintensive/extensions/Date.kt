package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

enum class TimeUnits(
    val value: Long
) {
    SECOND(1000L),
    MINUTE(60 * SECOND.value),
    HOUR(60 * MINUTE.value),
    DAY(24 * HOUR.value);

    companion object {
        fun getTimeValue(value: Int, units: TimeUnits): Long =
            when(units) {
                SECOND -> value * SECOND.value
                MINUTE -> value * MINUTE.value
                HOUR -> value * HOUR.value
                DAY -> value * DAY.value
            }
    }
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {

    var time = this.time

    time += TimeUnits.getTimeValue(value, units)

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
        in 75 * TimeUnits.SECOND.value .. 45 * TimeUnits.MINUTE.value-1 -> "${getTimeDiffString(timediff, TimeUnits.MINUTE).pastOrFuture(pastTime)}"
        in 45 * TimeUnits.MINUTE.value .. 75 * TimeUnits.MINUTE.value-1 -> "час".pastOrFuture(pastTime)
        in 75 * TimeUnits.MINUTE.value .. 22 * TimeUnits.HOUR.value-1 -> "${getTimeDiffString(timediff, TimeUnits.HOUR).pastOrFuture(pastTime)}"
        in 22 * TimeUnits.HOUR.value .. 26 * TimeUnits.HOUR.value-1 -> "день".pastOrFuture(pastTime)
        in 26 * TimeUnits.HOUR.value .. 360 * TimeUnits.DAY.value -> "${getTimeDiffString(timediff, TimeUnits.DAY).pastOrFuture(pastTime)}"
        else -> if (pastTime) "более года назад" else "более чем через год"
    }
}

private fun String.pastOrFuture(pastTime: Boolean): String =
    if (pastTime) "${this} назад" else "через ${this}"


private fun getTimeDiffString(timediff: Long, timeUnit: TimeUnits): String =
    when (timeUnit) {
        TimeUnits.MINUTE -> "${getTimeUnitCount(timediff, TimeUnits.MINUTE)} ${getMinutesString(getTimeUnitCount(timediff, TimeUnits.MINUTE))}"
        TimeUnits.HOUR -> "${getTimeUnitCount(timediff, TimeUnits.HOUR)} ${getHoursString(getTimeUnitCount(timediff, TimeUnits.HOUR))}"
        TimeUnits.DAY -> "${getTimeUnitCount(timediff, TimeUnits.DAY)} ${getDaysString(getTimeUnitCount(timediff, TimeUnits.DAY))}"
        else -> ""
    }


private fun getTimeUnitCount(timediff: Long, timeUnit: TimeUnits): Int =
    ((timediff)/timeUnit.value).toInt()


private fun getMinutesString(time: Int): String =
    if (time in 11 .. 14) "минут"
    else when(time%10) {
        1 -> "минуту"
        in 2 .. 4 -> "минуты"
        else -> "минут"
    }

private fun getHoursString(time: Int): String =
    if (time in 11 .. 14) "часов"
    else when(time%10) {
        1 -> "час"
        in 2 .. 4 -> "часа"
        else -> "часов"
    }

private fun getDaysString(time: Int): String =
    if (time in 11 .. 14) "дней"
    else when(time%10) {
        1 -> "день"
        in 2 .. 4 -> "дня"
        else -> "дней"
    }

//0с - 1с "только что"
//1с - 45с "несколько секунд назад"
//45с - 75с "минуту назад"
//75с - 45мин "N минут назад"
//45мин - 75мин "час назад"
//75мин 22ч "N часов назад"
//22ч - 26ч "день назад"
//26ч - 360д "N дней назад"
//>360д "более года назад"

