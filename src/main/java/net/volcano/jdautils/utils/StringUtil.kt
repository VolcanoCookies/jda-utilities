package net.volcano.jdautils.utils

object StringUtil {

	/**
	 * Split a string into multiple strings, trying to split at the provided string
	 * If content contains any substring longer than maxLength and not containing split,
	 * that substring will be capped at maxLength, and the remainder will be tested again
	 *
	 * @param content   The content to split
	 * @param maxLength The max length of the returning strings
	 * @param split     The string to try and split at
	 * @return A list of strings in order
	 */
	fun splitAt(content: String, maxLength: Int, split: String): List<String> {
		val strings: MutableList<String> = ArrayList()
		var stringBuilder = StringBuilder()
		if (content.length < maxLength) {
			return listOf(content)
		}
		for (str in content.split(split.toRegex()).toTypedArray()) {
			var s = str
			while (s.length > maxLength) {
				stringBuilder.append(s, 0, maxLength)
				s = s.substring(maxLength)
			}
			if (s.length + stringBuilder.length > maxLength) {
				strings.add(stringBuilder.toString())
				stringBuilder = StringBuilder()
			}
			stringBuilder.append(s)
		}
		if (stringBuilder.length > 0) {
			strings.add(stringBuilder.toString())
		}
		return strings
	}

	fun trim(content: String, maxLength: Int): String {
		return if (content.length > maxLength) content.substring(0, maxLength - 1) else content
	}

	fun capitalize(content: String): String {
		return when {
			content.isEmpty() -> {
				content
			}
			content.length == 1 -> {
				content.toUpperCase()
			}
			else -> {
				content.substring(0, 1).toUpperCase() + content.substring(1)
			}
		}
	}

	fun cameCaseToSpaces(content: String): String {
		return content.replace("([A-Z])".toRegex(), " $1").trim { it <= ' ' }
	}

	val TOKEN_REGEX = "\"[^\"]+\"|'[^']+'|\\S+"

	fun tokenize(content: String, removeQuotes: Boolean = true): List<String> {

		val regex =
			Regex(TOKEN_REGEX, setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

		val tokens = regex.findAll(content).map { it.value }.toList()

		return if (removeQuotes)
			tokens.map { it.replace(Regex("^['\"](.*)['\"]$"), "$1") }
		else
			tokens

	}

}