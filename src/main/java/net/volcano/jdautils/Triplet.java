package net.volcano.jdautils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Triplet<FIRST, SECOND, THIRD> {
	
	private FIRST first;
	private SECOND second;
	private THIRD third;
	
	public static <FIRST, SECOND, THIRD> Triplet<FIRST, SECOND, THIRD> with(FIRST first, SECOND second, THIRD third) {
		return new Triplet<>(first, second, third);
	}
	
}
