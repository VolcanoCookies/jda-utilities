package net.volcano.jdautils.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {
	
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
	public static List<String> splitAt(String content, int maxLength, String split) {
		List<String> strings = new ArrayList<>();
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if (content.length() < maxLength) {
			return Collections.singletonList(content);
		}
		
		for (String s : content.split(split)) {
			
			while (s.length() > maxLength) {
				stringBuilder.append(s, 0, maxLength);
				s = s.substring(maxLength);
			}
			
			if (s.length() + stringBuilder.length() > maxLength) {
				strings.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
			
			stringBuilder.append(s);
			
		}
		
		if (stringBuilder.length() > 0) {
			strings.add(stringBuilder.toString());
		}
		
		return strings;
	}
	
	public static String trim(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength - 1) : content;
	}
	
}
