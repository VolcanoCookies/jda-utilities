package net.volcano.jdautils.utils

import org.intellij.lang.annotations.Language
import java.text.SimpleDateFormat
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*

object TimeUtil {

	private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	fun format(instant: Instant?): String {
		return simpleDateFormat.format(Date.from(instant))
	}

	fun format(dateTime: OffsetDateTime): String {
		return format(dateTime.toInstant())
	}

	fun formatEpoch(epoch: Long?): String {
		return simpleDateFormat.format(Date.from(Instant.ofEpochMilli(epoch!!)))
	}

	@Language("regexp")
	const val DATE_TIME_REGEX: String =
		"(\\d{4})(?:-(\\d{1,2})(?:-(\\d{1,2})(?: ?(\\d{1,2})(?::(\\d{1,2})(?::(\\d{1,2}))?)?)?)?)?"

	fun getDateTimeFromString(input: String): OffsetDateTime {

		val dateTimeRegex =
			Regex(DATE_TIME_REGEX)

		val res =
			dateTimeRegex.find(input) ?: throw InvalidDateTimeFormatException(
				0,
				input.length,
				"Invalid format for date time"
			)

		val startOffset = input.indexOf(res.value)

		val year = res.groupValues[1].toInt()

		val month = res.groupValues[2].let {
			when {
				it.isEmpty() -> 1
				it.toInt() > 12 -> throw InvalidDateTimeFormatException(
					startOffset,
					0,
					"Month cannot be greater than 12."
				)
				it.toInt() < 1 -> throw InvalidDateTimeFormatException(startOffset, 0, "Month cannot be less than 1.")
				else -> it.toInt()
			}
		}

		val day = res.groupValues[3].let {
			val maxDay = YearMonth.of(year, month).lengthOfMonth()

			when {
				it.isEmpty() -> 1
				it.toInt() < 1 -> throw InvalidDateTimeFormatException(startOffset, 0, "Day cannot be less than 1.")
				it.toInt() > maxDay -> throw InvalidDateTimeFormatException(
					0,
					startOffset,
					"Day cannot be greater than $maxDay for the provided month."
				)
				else -> it.toInt()
			}
		}

		val hour = res.groupValues[2].let {
			when {
				it.isEmpty() -> 0
				it.toInt() > 24 -> throw InvalidDateTimeFormatException(
					startOffset,
					0,
					"Hour cannot be greater than 24."
				)
				else -> it.toInt()
			}
		}

		val minute = res.groupValues[2].let {
			when {
				it.isEmpty() -> 0
				it.toInt() > 59 -> throw InvalidDateTimeFormatException(
					startOffset,
					0,
					"Hour cannot be greater than 59."
				)
				else -> it.toInt()
			}
		}

		val second = res.groupValues[2].let {
			when {
				it.isEmpty() -> 0
				it.toInt() > 59 -> throw InvalidDateTimeFormatException(
					startOffset,
					0,
					"Second cannot be greater than 59."
				)
				else -> it.toInt()
			}
		}

		return OffsetDateTime.of(year, month, day, hour, minute, second, 0, ZoneOffset.UTC)

	}

	class InvalidDateTimeFormatException(
		val errorStartIndex: Int,
		val errorLength: Int,
		val hint: String
	) : Exception()

	@Language("regexp")
	const val DURATION_TIME_REGEX: String =
		"(?:(\\d{1,32}) ?y(?:ears?)?)? ?(?:(\\d{1,32}) ?mo(?:nths?)?)? ?(?:(\\d{1,32}) ?d(?:ays?)?)? ?(?:(\\d{1,32}) ?h(?:ours?)?)? ?(?:(\\d{1,32}) ?mi(?:nutes?)?)? ?(?:(\\d{1,32}) ?s(?:econds?)?)?"

	fun getTimeDurationFromString(input: String): Duration {

		val res =
			Regex(DATE_TIME_REGEX).matchEntire(input) ?: throw InvalidTimeDurationFormatException()

		val years = res.groups[1]?.value?.toLong()
		val months = res.groups[2]?.value?.toLong()
		val days = res.groups[3]?.value?.toLong()
		val hours = res.groups[4]?.value?.toLong()
		val minutes = res.groups[5]?.value?.toLong()
		val seconds = res.groups[6]?.value?.toLong()

		if (years == null && months == null && days == null && hours == null && minutes == null && seconds == null) {
			throw InvalidTimeDurationFormatException()
		}

		val duration = Duration.ZERO
		years?.let { duration.plus(it, ChronoUnit.YEARS) }
		months?.let { duration.plus(it, ChronoUnit.MONTHS) }
		days?.let { duration.plusDays(it) }
		hours?.let { duration.plusHours(it) }
		minutes?.let { duration.plusMinutes(it) }
		seconds?.let { duration.plusSeconds(it) }

		return duration

	}

	class InvalidTimeDurationFormatException : Exception()

}