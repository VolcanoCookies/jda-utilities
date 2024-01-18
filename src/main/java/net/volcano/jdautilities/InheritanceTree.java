package net.volcano.jdautilities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InheritanceTree<T> {
	
	public static final char SEPARATOR = '.';
	
	Node<T> root;
	
	public InheritanceTree() {
		root = new Node<>("", null);
	}
	
	public T get(String[] path) {
		var node = root.get(path);
		if (node == null) {
			return null;
		} else {
			return node.value;
		}
	}
	/*
	public T put(String[] path, T value) {
		var node = root;
		for (String s : path) {
			var next = node.getNode(s);
			if (next == null) {
				node.put();
			}
		}
	}*/
	
	static class Node<T> implements Comparable<String> {
		
		String name;
		
		T value;
		
		List<Node<T>> children;
		
		public Node(String name, T value) {
			this.name = name;
			children = new ArrayList<>();
		}
		
		public Node<T> put(String name, T value) {
			var node = new Node<>(name, value);
			var index = indexOf(name);
			if (index >= 0) {
				children.remove(index);
				children.add(index, node);
			} else {
				children.add(-(index + 1), node);
			}
			return node;
		}
		
		public void putIfAbsent(String name, T value) {
			var node = new Node<>(name, value);
			var index = indexOf(name);
			if (index < 0) {
				children.add(-(index + 1), node);
			}
		}
		
		public Node<T> get(String[] path) {
			if (path.length == 0) {
				return null;
			} else if (path.length == 1) {
				return getNode(path[0]);
			} else {
				var next = getNode(path[0]);
				if (next == null) {
					return null;
				} else {
					return next.get(Arrays.copyOfRange(path, 1, path.length - 1));
				}
			}
		}
		
		public Node<T> getNode(String name) {
			var index = indexOf(name);
			if (index >= 0) {
				return children.get(index);
			} else {
				return null;
			}
		}
		
		public int indexOf(String value) {
			return Collections.binarySearch(children, value);
		}
		
		@Override
		public int compareTo(@NotNull String o) {
			return name.compareTo(o);
		}
	}
	
}
