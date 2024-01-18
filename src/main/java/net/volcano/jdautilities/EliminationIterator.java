package net.volcano.jdautilities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class EliminationIterator<T> {
	
	private final Set<T> set;
	
	private Iterator<T> iterator;
	
	public EliminationIterator(Iterable<T> i) {
		set = (Set<T>) i;
		iterator = set.iterator();
	}
	
	public EliminationIterator(T... t) {
		this(Arrays.asList(t));
	}
	
	public T next() {
		return iterator.next();
	}
	
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	public void remove() {
		iterator.remove();
	}
	
	public void reset() {
		iterator = set.iterator();
	}
	
	public Set<T> getElements() {
		return set;
	}
	
}
