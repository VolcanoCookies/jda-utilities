package net.volcano.jdautils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<FIRST, SECOND> {
	
	private FIRST first;
	private SECOND second;
	
	public static <FIRST, SECOND> Pair<FIRST, SECOND> with(FIRST first, SECOND second) {
		return new Pair<>(first, second);
	}
	
}
