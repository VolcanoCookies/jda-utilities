package net.volcano.jdautils.utils

fun <T> Iterable<T>.asString(separator: String, maxLen: Int, func: (T) -> String): List<String> {
	if (this.count() == 0)
		return emptyList()
	val output = mutableListOf<String>()
	var string = ""
	for (t in this) {
		val s = func.invoke(t)
		if (string.length + s.length + separator.length > maxLen) {
			output += string
			string = ""
		}
		if (string.isNotEmpty())
			string += separator
		string += s
	}
	output += string
	return output
}

fun <T> Iterable<T>.asString(separator: String, func: (T) -> String): String {
	return this.joinToString(separator = separator, transform = func)
}

fun <K, V> Map<K, V>.asString(separator: String, maxLen: Int, func: (Map.Entry<K, V>) -> String): List<String> {
	return this.entries.asString(separator, maxLen, func)
}

fun <K, V> Map<K, V>.asString(separator: String, func: (Map.Entry<K, V>) -> String): String {
	return this.entries.asString(separator, func)
}