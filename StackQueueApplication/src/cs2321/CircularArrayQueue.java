/**
 * 
 */
package cs2321;

import net.datastructures.Queue;

/**
 * @author ruihong-adm
 * @param <E>
 *
 */

public class CircularArrayQueue<E> implements Queue<E> {
    int size =0;
    E[] data;
    int capacity;
    
    int f=0;  //if size >0, point the front end, where the beginning of the Queue. 
    int r=0;  //point the end of queue. The next element shall go to.
    
	public CircularArrayQueue(int queueSize) {
		data = (E[]) new Object[queueSize];
		capacity = queueSize;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public void enqueue(E e) throws IllegalStateException {
		if (size == capacity) throw new IllegalStateException("Queue is full") ;
		r = (f + size) % capacity;
		data[r]=e;
		size++;
	}

	@Override
	public E first() {
		if (size==0) return null;
		return data[f];
	}

	@Override
	public E dequeue() {
		if (size==0) return null;
		E tmp = data[f];
		f = (f+1) % capacity;
		size--;
		return tmp;
	}

}
