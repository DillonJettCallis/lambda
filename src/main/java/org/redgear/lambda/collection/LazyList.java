package org.redgear.lambda.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by dcallis on 7/21/2015.
 *
 */
public class LazyList<T> implements List<T> {

	public static final Logger log = LoggerFactory.getLogger(LazyList.class);

	private Iterator<T> source;
	private final ArrayList<T> list;

	public LazyList(Iterator<T> source){
		this.source = source;
		this.list = new ArrayList<>();
	}

	public LazyList(Stream<T> source){
		this(source.iterator());
	}

	public LazyList(Iterable<T> source){
		this(source.iterator());
	}

	public static <T> LazyList<T> from(Iterator<T> source){
		return new LazyList<>(source);
	}

	public static <T> LazyList<T> from(Stream<T> source){
		return new LazyList<>(source);
	}

	public static <T> LazyList<T> from(Iterable<T> source){
		if(source instanceof LazyList)
			return (LazyList<T>) source;
		else
			return new LazyList<>(source);
	}

	public int pullAll(){
		if(source == null)
			return 0;

		int pulled = 0;
		while(source.hasNext()) {
			list.add(source.next());
			pulled++;
		}
		source = null;
		list.trimToSize();
		return pulled;
	}

	public int pull(int amount){
		if(source == null)
			return 0;

		int pulled = 0;
		for(int i = 0; i < amount && source.hasNext(); i++){
			list.add(source.next());
			pulled++;
		}

		if(!source.hasNext()) {
			source = null;
			list.trimToSize();
		}
		return pulled;
	}

	public int lazyIndex(){
		return list.size() - 1;
	}

	public LazyList<T> concat(LazyList<T> other) {
		return from(new JoiningIterator<>(iterator(), other.iterator()));
	}


	@Override
	public int size() {
		pullAll();
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty() && pull(1) == 0;
	}

	@Override
	public boolean contains(Object o) {

		for(T item : this){
			if(item != null && item.equals(o))
				return true;
		}

		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public Object[] toArray() {
		pullAll();
		return list.toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] t1s) {
		pullAll();
		return list.toArray(t1s);
	}

	@Override
	public boolean add(T t) {
		pullAll();
		return list.add(t);
	}

	@Override
	public boolean remove(Object o) {
		for(T item : this){
			if(item != null && item.equals(o)) {
				return list.remove(o);
			}
		}

		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		for(Object obj : collection){
			if(!contains(obj))
				return false;
		}

		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		if(collection.isEmpty())
			return false;

		pullAll();

		list.addAll(collection);

		return true;
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> collection) {
		if(collection.isEmpty())
			return false;

		int pullUntil = i - lazyIndex();

		if(pullUntil > 0)
			pull(pullUntil);

		list.addAll(i, collection);

		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		if(collection.isEmpty())
			return false;

		boolean changed = false;

		for(Object obj : collection)
			changed |= remove(obj);


		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		if(collection.isEmpty())
			return false;

		pullAll();

		return list.retainAll(collection);
	}

	@Override
	public void clear() {
		list.clear();
		source = null;
	}

	@Override
	public T get(int i) {
		if(lazyIndex() > i)
			return list.get(i);
		else {
			pull(i - lazyIndex());
			return list.get(i);
		}
	}

	@Override
	public T set(int i, T t) {
		if(lazyIndex() > i)
			return list.set(i, t);
		else {
			pull(i - lazyIndex());
			return list.set(i, t);
		}
	}

	@Override
	public void add(int i, T t) {
		if(lazyIndex() > i)
			list.add(i, t);
		else {
			pull(i - lazyIndex());
			list.add(i, t);
		}
	}

	@Override
	public T remove(int i) {
		if(lazyIndex() > i)
			return list.remove(i);
		else {
			pull(i - lazyIndex());
			return list.remove(i);
		}
	}

	@Override
	public int indexOf(Object o) {
		for(T t : this){
			if(t != null && t.equals(o)){
				return list.indexOf(o);
			}
		}

		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		pullAll();
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		return new ListIterator<T>(){

			int index = i;

			@Override
			public boolean hasNext() {
				return LazyList.this.lazyIndex() >= index || LazyList.this.pull(1) == 1;
			}

			@Override
			public T next() {
				return LazyList.this.get(index++);
			}

			@Override
			public boolean hasPrevious() {
				return index != 0;
			}

			@Override
			public T previous() {
				return LazyList.this.get(--index);
			}

			@Override
			public int nextIndex() {
				return index + 1;
			}

			@Override
			public int previousIndex() {
				return index - 1;
			}

			@Override
			public void remove() {
				LazyList.this.remove(index);
			}

			@Override
			public void set(T t) {
				LazyList.this.set(index, t);
			}

			@Override
			public void add(T t) {
				LazyList.this.add(index, t);
			}
		};
	}

	@Override
	public List<T> subList(int start, int end) {
		return from(new Iterator<T>(){

			int index = start;

			@Override
			public boolean hasNext() {
				return index < end && (lazyIndex() >= index || pull(index - lazyIndex()) == index - lazyIndex());
			}

			@Override
			public T next() {
				return get(index++);
			}
		});

//		if(lazyIndex() > end)
//			return list.subList(start, end);
//		else {
//			pull(end - lazyIndex());
//			return list.subList(start, end);
//		}
	}


	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;

		if(obj instanceof List){
			List<?> other = (List) obj;
			pullAll();
			return list.equals(other);
		}
		else
			return false;
	}

	@Override
	public int hashCode(){
		pullAll();
		return list.hashCode();
	}

	@Override
	public String toString(){
		pullAll();
		return list.toString();
	}
}
