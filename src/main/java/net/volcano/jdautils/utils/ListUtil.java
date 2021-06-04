package net.volcano.jdautils.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {
	
	// Convert a list of objects to a string, with a function applied to each object, and the provided separator
	public static <T> String asString(String separator, Collection<T> list, Function<T, String> function) {
		if (list.isEmpty()) {
			return "";
		}
		List<String> stringList = list.stream().map(function).collect(Collectors.toList());
		StringBuilder output = new StringBuilder();
		for (String element : stringList) {
			output.append(separator).append(element);
		}
		return output.length() > separator.length() ? output.substring(separator.length()) : output.toString();
	}
	
	public static <T> String asString(String separator, T[] array, Function<T, String> function) {
		return asString(separator, Arrays.asList(array), function);
	}
	
	/**
	 * Convert a list of objects to a string, with a function applied to each object, and the provided separator
	 *
	 * @param separator the separator to put between each element
	 * @param list      A list of objects
	 * @param function  A function
	 * @param maxLength the max length of one string
	 * @return A list of strings
	 */
	public static <T> List<String> asString(String separator, Collection<T> list, int maxLength, Function<T, String> function) {
		List<String> output = new ArrayList<>();
		List<String> stringList = list.stream().map(function).collect(Collectors.toList());
		StringBuilder stringBuilder = new StringBuilder();
		for (String element : stringList) {
			if (separator.length() + element.length() + stringBuilder.length() > maxLength) {
				output.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.append(separator);
			}
			stringBuilder.append(element);
		}
		if (stringBuilder.length() > 0) {
			output.add(stringBuilder.toString());
		}
		return output;
	}
	
}
