package net.volcano.jdautils.utils



/**
 * Split a string into multiple strings, trying to split at the provided string
 * If content contains any substring longer than maxLength and not containing split,
 * that substring will be capped at maxLength, and the remainder will be tested again
 *
 * @param maxLen The max length of the returning strings
 * @param split     The string to try and split at, default to newline
 * @return A list of strings in order
 */
fun String.splitAt(maxLen: Int, split: String = "\n"): List<String> {
	val strings: MutableList<String> = ArrayList()
	var stringBuilder = StringBuilder()
	if (this.length < maxLen) {
		return listOf(this)
	}
	for (str in this.split(split.toRegex()).toTypedArray()) {
		var s = str
		while (s.length > maxLen) {
			stringBuilder.append(s, 0, maxLen)
			s = s.substring(maxLen)
		}
		if (s.length + stringBuilder.length > maxLen) {
			strings.add(stringBuilder.toString())
			stringBuilder = StringBuilder()
		}
		stringBuilder.append(s)
	}
	if (stringBuilder.isNotEmpty()) {
		strings.add(stringBuilder.toString())
	}
	return strings
}

const val TOKEN_REGEX = "\"[^\"]+\"|'[^']+'|\\S+"

fun String.tokenize(removeQuotes: Boolean = true): List<String> {
	val regex = Regex(TOKEN_REGEX, setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

	val tokens = regex.findAll(this).map { it.value }.toList()

	return if (removeQuotes)
		tokens.map { it.replace(Regex("^['\"](.*)['\"]$"), "$1") }
	else
		tokens
}

fun String.capitalizeFirst(): String {
	return this.replaceFirstChar { it.uppercaseChar() }
}

fun String.capitalize() : String {
	return this.lowercase().capitalizeFirst()
}

fun String.camelCaseToSpaces(): String {
	return this.replace("([A-Z])".toRegex(), " $1").trim { it <= ' ' }
}

fun String.trim(len: Int): String {
	return if (this.length > len) this.substring(0,len - 1) else this
}

fun String.plural(amount: Int) : String {
	return if (this.endsWith("s"))  {
		if (amount == 1) {
			this.substringBeforeLast("s")
		} else {
			this
		}
	} else {
		if ( amount == 1) {
			this
		} else {
			this + "s"
		}
	}
}

fun String.plural(l: Collection<*>): String {
	return this.plural(l.size	)
}