package cs2321;
import java.util.Iterator;

import net.datastructures.Position;
import net.datastructures.PositionalList;


public class DoublyLinkedList<E> implements PositionalList<E> {
	
	private Node<E> head;
	private Node<E> tail;
	private int size;
	
	private class DLLPositionIterator implements Iterator<Position<E>>{ //our iterator for position objects
		private Position<E> current = first();
		private Position<E> recent = null;

		
		@Override
		public boolean hasNext() {
			return (current != null); //the node we're on isn't null
		}

		@Override
		public Position<E> next() {
			if(hasNext()) { //if we're not on a null node
				recent = current; //shift it into recent
				current = after(current); //get the next node
				return recent; //return our starting node
			}
			return null;
		}
		
	}
	
	private class DLLPositionIterable implements Iterable<Position<E>>{ //iterable for position objects

		@Override
		public Iterator<Position<E>> iterator() { //return a new position iterator
			return new DLLPositionIterator();
		}
		
	}
	
	private class DLLElementIterator implements Iterator<E>{ //element iterator, wraps the positional one to return elements

		Iterator<Position<E>> positionIterator = new DLLPositionIterator();
		
		
		@Override
		public boolean hasNext() {
			return positionIterator.hasNext(); //same thing as positional hasNext()
		}

		@Override
		public E next() {
			return positionIterator.next().getElement(); //run next, but extract the element
		}
		
	}
	
	public class Node<E> implements Position<E>{ //inner node class for use by the linked list
		public Node<E> next;
		public Node<E> prev;
		public E val;
		
		public Node(E val){
			this.val = val;
		}
		
		public Node(){
			this(null); //make a null node using the main constructor
		}

		@Override
		public E getElement() throws IllegalStateException {
			if(next == null) {	//apparently this means that this node is 'invalid', which sounds kind of dumb if you ask me. Why wouldn't we check prev as well? This is what the book has.
				throw new IllegalStateException();
			}
			return this.val; //return our value
		}
	}
	
	public DoublyLinkedList() { //main constructor
		head = new Node<E>(null); 
		tail = new Node<E>(null);
		
		this.head.next = this.tail;
		this.tail.prev = this.head;
		this.size = 0;
	}

	private boolean inList(Position<E> position) { //finds if a position points to the same location as another in the list
		if(position == head || position == tail) { //provisions for head and tail, since they're not checked by default
			return true;
		} else {
			Node<E> current = head;
			while(current.next != null) { //while we have another node
				if((Position<E>)current.next == position) { //match the two
					return true;
				}
				current = current.next; //move forward if they didn't mesh
			}
			return false; // return if we didn't find anything
		}
	}
	
	@Override
	public int size() { //return the size
		return this.size;
	}

	@Override
	public boolean isEmpty() { //if we have no elements
		return this.size == 0;
	}

	@Override
	public Position<E> first() { 
		if(this.size == 0) { //catch nullpointer
			return null;
		}
		return this.head.next;
	}

	@Override
	public Position<E> last() {
		if(this.size == 0) { //catch nullpointer
			return null;
		}
		return this.tail.prev;
	}

	@Override
	public Position<E> before(Position<E> p) throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the position isn't in the list, return early.
		}
		if(((Node<E>)p).prev == head) { //ignore the head, should be null
			return null;
		} else {
			return ((Node<E>)p).prev;
		}
	}

	@Override
	public Position<E> after(Position<E> p) throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the position isn't in the list, return early.
		}
		if(((Node<E>)p).next == tail) { //ignore the head, should be null
			return null;
		} else {
			return ((Node<E>)p).next;
		}
	}

	@Override
	public Position<E> addFirst(E e) {
		return addAfter(head, e); //adding after the head is equivalent to adding to the first position
	}

	@Override
	public Position<E> addLast(E e) {
		return addBefore(tail, e); //adding before the tail is equivalent to adding to the last position
	}

	@Override
	public Position<E> addBefore(Position<E> p, E e)
			throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the given position isn't in the list, exit early.
		}
		Node<E> temp = new Node<E>(e); //make the new node
		temp.prev = ((Node<E>)p).prev; 
		temp.next = ((Node<E>)p);
		((Node<E>)p).prev.next = temp;
		((Node<E>)p).prev = temp;
		size++; //increase size
		return temp;
	}

	@Override
	public Position<E> addAfter(Position<E> p, E e)
			throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the given position isn't in the list, exit early.
		}
		Node<E> temp = new Node<E>(e); //make the new node
		temp.prev = ((Node<E>)p);
		temp.next = ((Node<E>)p).next;
		((Node<E>)p).next.prev = temp;
		((Node<E>)p).next = temp;
		size++; //increase size
		return temp;
	}

	@Override
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the given position isn't in the list, exit early.
		}
		E temp = p.getElement();
		((Node<E>)p).val = e; //set the value, we have to cast to node first.
		return temp;
	}

	@Override
	public E remove(Position<E> p) throws IllegalArgumentException {
		if(!inList(p)) {
			throw new IllegalArgumentException(); //if the given position isn't in the list, exit early.
		}
		if(size == 0) {
			return null;
		}
		((Node<E>)p).next.prev = ((Node<E>)p).prev; //flip the pointers
		((Node<E>)p).prev.next = ((Node<E>)p).next;
		size--; //decrease size
		return p.getElement();
	}

	@Override
	public Iterator<E> iterator() { //return the default item iterator, used for true item i/o
		return new DLLElementIterator();
	}

	@Override
	public Iterable<Position<E>> positions() { //get the position iterator
		return new DLLPositionIterable();
	}
	
	public E removeFirst() throws IllegalArgumentException {
		if(size == 0) {
			return null;
		}
		return remove(head.next); //can throw IllegalArgumentException
	}
	
	public E removeLast() throws IllegalArgumentException {
		if(size == 0) {
			return null;
		}
		return remove(tail.prev); //can throw IllegalArgumentException
	}

}
