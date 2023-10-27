import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*
/**
 * 描述:
 *    日期扩展
 * @author shihui
 * @version 2019-08-14 10:52
 */
class DateExt {
    companion object {
        @JvmStatic
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val DATE_DENSE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
        val DATE_HOUR_DENSE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH")
        val CST_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)


        @JvmStatic
        fun toDateTimeString(date: Date): String {
            return asLocalDateTime(date).format(DATE_TIME_FORMATTER)
        }
        @JvmStatic
        fun toDateString(date: Date): String {
            return asLocalDate(date).format(DATE_FORMATTER)
        }
        @JvmStatic
        fun asLocalDateTime(date: Date) : LocalDateTime {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
        @JvmStatic
        fun asLocalDate(date: Date) : LocalDate {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        @JvmStatic
        fun localDateTimeParse(dateTimeStr: String): LocalDateTime {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER)
        }
        @JvmStatic
        fun cstLocalDateTimeParse(dateTimeStr: String): LocalDateTime {
            return CST_FORMATTER.parse(dateTimeStr, LocalDateTime::from)
        }
        @JvmStatic
        fun localDateParse(dateTimeStr: String): LocalDate {
            return LocalDate.parse(dateTimeStr, DATE_FORMATTER)
        }

        @JvmStatic
        fun asDate(localDateTime: LocalDateTime) : Date {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }
        @JvmStatic
        fun asDate(localDate: LocalDate) : Date {
            return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
        }
        @JvmStatic
        fun asMillisLocalDateTime(milliseconds: Long) : LocalDateTime {
            return Instant.ofEpochMilli(milliseconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
        @JvmStatic
        fun asMillisLocalDate(milliseconds: Long) : LocalDate {
            return Instant.ofEpochMilli(milliseconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        /**
         * 获取某季度的第一天
         * 第一季度：1月－3月
         * 第二季度：4月－6月
         * 第三季度：7月－9月
         * 第四季度：10月－12月
         * @param date 当前日期LocalDate
         * @return LocalDate
         */
        @JvmStatic
        fun firstDayOfQuarter(date: LocalDate): LocalDate {
            val month = date.month.value
            return when {
                month in 4..6 -> date.withMonth(4).with(TemporalAdjusters.firstDayOfMonth())
                month in 7..9 -> date.withMonth(7).with(TemporalAdjusters.firstDayOfMonth())
                month >= 10 -> date.withMonth(10).with(TemporalAdjusters.firstDayOfMonth())
                else -> date.withMonth(1).with(TemporalAdjusters.firstDayOfMonth())
            }
        }

        /**
         * 获取某季度的最后一天
         * 第一季度：1月－3月
         * 第二季度：4月－6月
         * 第三季度：7月－9月
         * 第四季度：10月－12月
         *
         * @param date 当前日期LocalDate
         * @return LocalDate
         */
        @JvmStatic
        fun lastDayOfQuarter(date: LocalDate): LocalDate {
            val month = date.month.value
            return when {
                month in 4..6 -> date.withMonth(6).with(TemporalAdjusters.lastDayOfMonth())
                month in 7..9 -> date.withMonth(9).with(TemporalAdjusters.lastDayOfMonth())
                month >= 10 -> date.withMonth(12).with(TemporalAdjusters.lastDayOfMonth())
                else -> date.withMonth(3).with(TemporalAdjusters.lastDayOfMonth())
            }
        }
    }
}

/**
 * yyyy-MM-dd HH:mm:ss
 */
fun Date?.toDateTimeString(): String? {
    return this?.let { DateExt.toDateTimeString(it) }
}

/**
 * yyyy-MM-dd
 */
fun Date?.toDateString(): String? {
    return this?.let{ DateExt.toDateString(it) }
}
fun Date?.asLocalDateTime(): LocalDateTime? {
    return this?.let{ DateExt.asLocalDateTime(it) }
}
fun Date?.asLocalDate(): LocalDate? {
    return this?.let{ DateExt.asLocalDate(it) }
}

/**
 * 字符串 yyyy-MM-dd HH:mm:ss 格式成日期
 */
fun String?.asLocalDateTime(): LocalDateTime? {
    return this?.let { DateExt.localDateTimeParse(it) }
}
fun String?.asCstLocalDateTime(): LocalDateTime? {
    return this?.let { DateExt.cstLocalDateTimeParse(it) }
}

/**
 * 字符串 yyyy-MM-dd 格式成日期
 */
fun String?.asLocalDate(): LocalDate? {
    return this?.let{ DateExt.localDateParse(it) }
}

/**
 * LocalDate 转 Date
 */
fun LocalDate?.asDate() : Date? {
    return this?.let{ DateExt.asDate(it) }
}

/**
 * LocalDateTime 转 Date
 */
fun LocalDateTime?.asDate(): Date? {
    return this?.let { DateExt.asDate(it) }
}

/**
 * yyyy-MM-dd HH:mm:ss
 */
fun LocalDateTime?.toDateTimeString(): String? {
    return this?.format(DateExt.DATE_TIME_FORMATTER)
}
/**
 * yyyyMMdd
 */
fun LocalDateTime?.toDateDenseString(): String? {
    return this?.format(DateExt.DATE_DENSE_FORMATTER)
}
/**
 * yyyyMMdd
 */
fun LocalDate?.toDateDenseString(): String? {
    return this?.format(DateExt.DATE_DENSE_FORMATTER)
}
/**
 * yyyy-MM-dd
 */
fun LocalDate?.toDateString(): String? {
    return this?.format(DateExt.DATE_FORMATTER)
}


/** Date 日期是否在闭区间内 **/
fun Date.isClosed(start: Date, end: Date): Boolean {
    return this.time.isClosed(start, end)
}
fun Long.isClosed(start: Date, end: Date): Boolean {
    return (start.time <= this && this <= end.time)
}
fun Long.isClosed(start: Long, end: Long): Boolean {
    return (start <= this && this <= end)
}

/** LocalTime 日期是否在闭区间内 **/
fun LocalTime.isClosed(start: LocalTime, end: LocalTime): Boolean {
    return this.toNanoOfDay().isClosed(start, end)
}
fun LocalTime.isClosed(start: LocalDateTime, end: LocalDateTime): Boolean {
    return this.toNanoOfDay().isClosed(start.toLocalTime(), end.toLocalTime())
}
fun Long.isClosed(start: LocalTime, end: LocalTime): Boolean {
    return (start.toNanoOfDay() <= this && this <= end.toNanoOfDay())
}

/** LocalDate 日期是否在闭区间内 **/
fun LocalDate.isClosed(start: LocalDate, end: LocalDate): Boolean {
    return this.toEpochDay().isClosed(start, end)
}
fun LocalDate.isClosed(start: LocalDateTime, end: LocalDateTime): Boolean {
    return this.toEpochDay().isClosed(start.toLocalDate(), end.toLocalDate())
}
fun Long.isClosed(start: LocalDate, end: LocalDate): Boolean {
    return (start.toEpochDay() <= this && this <= end.toEpochDay())
}

/** LocalDateTime 日期是否在闭区间内 **/
fun LocalDateTime.isClosed(start: LocalDateTime, end: LocalDateTime): Boolean {
    return this.toLocalDate().isClosed(start, end) && this.toLocalTime().isClosed(start, end)
}
/** 毫秒转 日期 **/
fun Long?.asMillisDate(): Date? {
    return this?.let { Date(it) }
}
fun Long?.asMillisLocalDateTime(): LocalDateTime? {
    return this?.let { DateExt.asMillisLocalDateTime(it) }
}
fun Long?.asMillisLocalDate(): LocalDate? {
    return this?.let { DateExt.asMillisLocalDate(it) }
}

/** 月份最后一天 **/
fun LocalDateTime.lastDayOfMonth(): LocalDateTime {
    return with(TemporalAdjusters.lastDayOfMonth())
}
/** 月份第一天 **/
fun LocalDateTime.firstDayOfMonth(): LocalDateTime {
    return with(TemporalAdjusters.firstDayOfMonth())
}

/** 年份最后一天 **/
fun LocalDateTime.lastDayOfYear(): LocalDateTime {
    return with(TemporalAdjusters.lastDayOfYear())
}
/** 年份第一天 **/
fun LocalDateTime.firstDayOfYear(): LocalDateTime {
    return with(TemporalAdjusters.firstDayOfYear())
}

/**
 * 季度最后一天
 * 第一季度：1月－3月
 * 第二季度：4月－6月
 * 第三季度：7月－9月
 * 第四季度：10月－12月
 **/
fun LocalDate.lastDayOfQuarter(): LocalDate {
    return DateExt.lastDayOfQuarter(this)
}
fun LocalDateTime.lastDayOfQuarter(): LocalDate {
    return DateExt.lastDayOfQuarter(this.toLocalDate())
}
/**
 * 季度第一天
 * 第一季度：1月－3月
 * 第二季度：4月－6月
 * 第三季度：7月－9月
 * 第四季度：10月－12月
 **/
fun LocalDate.firstDayOfQuarter(): LocalDate {
    return DateExt.firstDayOfQuarter(this)
}
fun LocalDateTime.firstDayOfQuarter(): LocalDate {
    return DateExt.firstDayOfQuarter(this.toLocalDate())
}