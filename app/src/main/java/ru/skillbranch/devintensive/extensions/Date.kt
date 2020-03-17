package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time

    return this
}

/*
0с - 1с "только что"
1с - 45с "несколько секунд назад"
45с - 75с "минуту назад"
75с - 45мин "N минут назад"
45мин - 75мин "час назад"
75мин 22ч "N часов назад"
22ч - 26ч "день назад"
26ч - 360д "N дней назад"
>360д "более года назад"
*/
fun Date.humanizeDiff(date: Date = Date()): String {
    val curTime = Date().time
    val dif: Long
    val res: String

    if (curTime >= this.time) {
        dif = curTime - this.time
        when {
            dif / SECOND <= 1 -> {
                res = "только что"
            }
            dif / SECOND <= 45 -> {
                res = "несколько секунд назад"
            }
            dif / MINUTE <= 45 -> {
                res = TimeUnits.MINUTE.plural((dif / MINUTE).toInt()) + " назад"
            }
            dif / MINUTE <= 75 -> {
                res = "час назад"
            }
            dif / HOUR <= 22 -> {
                res = TimeUnits.HOUR.plural((dif / HOUR).toInt()) + " назад"
            }
            dif / HOUR <= 25 -> {
                res = "день назад"
            }
            dif / DAY <= 360 -> {
                res = TimeUnits.DAY.plural((dif / DAY).toInt()) + " назад"
            }
            else -> {
                res = "более года назад"
            }
        }
    } else {
        dif = this.time - curTime
        when {
            dif / SECOND <= 1 -> {
                res = "только что"
            }
            dif / SECOND <= 45 -> {
                res = "через несколько секунд"
            }
            dif / MINUTE <= 45 -> {
                res = "через " + TimeUnits.MINUTE.plural((dif / MINUTE).toInt())
            }
            dif / MINUTE <= 75 -> {
                res = "через час"
            }
            dif / HOUR <= 22 -> {
                res = "через " + TimeUnits.HOUR.plural((dif / HOUR).toInt())
            }
            dif / HOUR <= 25 -> {
                res = "через назад"
            }
            dif / DAY <= 360 -> {
                res = "через " + TimeUnits.DAY.plural((dif / DAY).toInt())
            }
            else -> {
                res = "более чем через год"
            }
        }
    }

    return res
}

enum class TimeUnits {
    SECOND {
        override fun plural(value: Int) = when (value % 10) {
            0, 5, 6, 7, 8, 9 -> "$value секунд"
            1 -> "$value секунду"
            2, 3, 4 -> "$value секунды"
            else -> "$value с"
        }
    },
    MINUTE {
        override fun plural(value: Int) = when (value % 10) {
            0, 5, 6, 7, 8, 9 -> "$value минут"
            1 -> "$value минуту"
            2, 3, 4 -> "$value минуты"
            else -> "$value м"
        }
    },
    HOUR {
        override fun plural(value: Int) = when (value % 10) {
            0, 5, 6, 7, 8, 9 -> "$value часов"
            1 -> "$value час"
            2, 3, 4 -> "$value часа"
            else -> "$value ч"
        }
    },
    DAY {
        override fun plural(value: Int) = when (value % 10) {
            0, 5, 6, 7, 8, 9 -> "$value дней"
            1 -> "$value день"
            2, 3, 4 -> "$value дня"
            else -> "$value д"
        }
    };

    abstract fun plural(value: Int): String
}
