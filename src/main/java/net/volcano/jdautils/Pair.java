package net.volcano.jdautils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<T, E> {
	
	private T first;
	private E second;
	
	public Pair(T first, E second) {
		this.first = first;
		this.second = second;
	}
	
	public static <F, S> Pair<F, S> with(F first, S second) {
		return new Pair<>(first, second);
	}
	
}
