package cs2321;

import net.datastructures.Stack;

public class DLLStack<E> implements Stack<E> {
	
	private DoublyLinkedList<E> internalContents = new DoublyLinkedList<E>();
	
	@Override
	public int size() {
		return internalContents.size();
	}

	@Override
	public boolean isEmpty() {
		return internalContents.isEmpty();
	}

	@Override
	public void push(E e) {
		internalContents.addFirst(e);
	}

	@Override
	public E top() {
		return internalContents.first().getElement();
	}

	@Override
	public E pop() {
		return internalContents.removeFirst();
	}

}
