package com.tlorrain.futile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Futile<T> implements Iterable<T> {

    private Iterable<T> iterable;

    private Futile(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    /**
     * Applies a closure to every element in this Futile
     *
     * @param closure the closure to apply
     * @throws NullPointerException if closure is null
     */
    public void each(Closure<? super T> closure) {
        each(this, closure);
    }

    /**
     * Filters elements of this Futile based on a predicate, returning a Futile of the results
     *
     * @param predicate the predicate to use for filtering
     * @return a Futile containing the filtered elements
     * @throws NullPointerException if predicate is null
     */
    public Futile<T> filter(Predicate<? super T> predicate) {
        return from(filter(this, predicate));
    }

    /**
     * Applies a function to every element of this Futile, returning a Futile of the results
     *
     * @param mapFunction the function to apply on every element of the Futile
     * @return a Futile containing the result of mapFunction for every element of this Futile
     * @throws NullPointerException if mapFunction is null
     */
    public <U> Futile<U> map(Function1<? super T, ? extends U> mapFunction) {
        return from(map(this, mapFunction));
    }

    /**
     * Applies a function to every element of this Futile, returning a flattened Futile of the results
     *
     * @param mapFunction the function to apply on every element of the Futile
     * @return a flattened Futile containing the results of mapFunction for every element of this Futile
     * @throws NullPointerException if mapFunction is null
     */
    public <U> Futile<U> flatMap(Function1<? super T, ? extends Iterable<? extends U>> mapFunction) {
        return from(flatMap(this, mapFunction));
    }

    /**
     * Applies a function across an this Futile, accumulating a value and returning a Futile of it.
     *
     * @param initialValue the initial value to use for the fold
     * @param foldFunction the function to use for the fold
     * @return a Futile containg a single element : the result of the fold
     * @throws NullPointerException if the foldFunction is null
     */
    public <U> Futile<U> fold(U initialValue, Function2<? super U, ? super T, ? extends U> foldFunction) {
        return from(Collections.singleton(fold(this, initialValue, foldFunction)));
    }

    /**
     * Iterate over this Futile and an iterable, applying a function to elements taken in pairs and returning a Futile
     * of the results
     *
     * @param iterable    the iterable to zip with this Futile
     * @param zipFunction the function to use to zip
     * @return A futile containing the result of the zip
     * @throws NoSuchElementException if the two iterables do not have the same size
     * @throws NullPointerException   if one of the arguments is null
     */
    public <U, V> Futile<V> zip(Iterable<U> iterable, final Function2<? super T, ? super U, ? extends V> zipFunction) {
        return from(zip(this, iterable, zipFunction));
    }

    /**
     * Converts this Futile to an Iterator
     *
     * @return an iterator over the elements of this Futile
     */
    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    /**
     * Convert this Futile to a single element
     *
     * @return the single element of this Futile
     * @throws IllegalArgumentException if this Futile doesn't contain a single element
     */
    public T toSingle() {
        return getOnlyElement(this);
    }

    /**
     * Convert this futile to an ArrayList
     *
     * @return an ArrayList containing the elements of this Futile
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> toArrayList() {
        return to(ArrayList.class, new Function1<Void, ArrayList>() {
            @Override
            public ArrayList apply(Void it) {
                return new ArrayList<>();
            }
        });
    }

    /**
     * Convert this futile to a LinkedList
     *
     * @return a LinkedList containing the elements of this Futile
     */
    @SuppressWarnings("unchecked")
    public LinkedList<T> toLinkedList() {
        return to(LinkedList.class, new Function1<Void, LinkedList>() {
            @Override
            public LinkedList apply(Void it) {
                return new LinkedList<>();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <C extends List> C to(Class<C> clazz, Function1<Void, C> construct) {
        if (clazz.isInstance(iterable)) {
            return (C) iterable;
        } else {
            C list = construct.apply(null);
            each(new AddClosure<T>(list));
            return list;
        }
    }

    @Override
    public String toString() {
        return "Futile{ " + iterable + " }";
    }

    /**
     * Creates a new Futile pipeline stating with an iterate
     *
     * @param iterable the iterable on witch we should operate
     * @return a Futile of the iterable
     * @throws NullPointerException if the iterable is null
     */
    public static <T> Futile<T> from(Iterable<T> iterable) {
        if (iterable == null) {
            throw new NullPointerException("Cannot create a Futile from a null iterable");
        }
        return new Futile<>(iterable);
    }

    /**
     * Applies a closure to every element of an iterable
     *
     * @param iterable the iterable on witch we should operate
     * @param closure  the closure to apply
     * @throws NullPointerException if one of the arguments is null
     */
    public static <T> void each(Iterable<T> iterable, Closure<? super T> closure) {
        for (T t : iterable) {
            closure.apply(t);
        }
    }

    /**
     * Filters an iterable based on a predicate
     *
     * @param iterable  the iterable on witch we should operate
     * @param predicate the predicate to use for filtering
     * @return the filtered iterable, as a list
     * @throws NullPointerException if one of the arguments is null
     */
    public static <T> List<T> filter(Iterable<T> iterable, final Predicate<? super T> predicate) {
        final List<T> result = new LinkedList<>();
        each(iterable, new Closure<T>() {
            @Override
            public void apply(T it) {
                if (predicate.apply(it)) {
                    result.add(it);
                }
            }
        });
        return result;
    }

    /**
     * Applies a function to every element of an iterable, returning a list of the results
     *
     * @param iterable    the iterable on witch we should operate
     * @param mapFunction the function to apply on every element of the iterable
     * @return a list containing the result of mapFunction for every element of iterable
     * @throws NullPointerException if one of the arguments is null
     */
    public static <T, U> List<U> map(Iterable<T> iterable, final Function1<? super T, ? extends U> mapFunction) {
        final List<U> result = new LinkedList<>();
        each(iterable, new Closure<T>() {
            @Override
            public void apply(T it) {
                result.add(mapFunction.apply(it));
            }
        });
        return result;
    }

    /**
     * Applies a function to every element of an iterable, returning a flattened list of the results
     *
     * @param iterable    the iterable on witch we should operate
     * @param mapFunction the function to apply on every element of the iterable
     * @return a flattened list containing the result of mapFunction for every element of iterable
     * @throws NullPointerException if one of the arguments is null, or if the mapFunction returns null
     */
    public static <T, U> List<U> flatMap(Iterable<T> iterable, final Function1<? super T, ? extends Iterable<? extends U>> mapFunction) {
        final List<U> result = new LinkedList<>();
        each(iterable, new Closure<T>() {
            @Override
            public void apply(T it) {
                each(mapFunction.apply(it), new AddClosure<>(result));
            }
        });
        return result;
    }

    /**
     * Applies a function across an iterable, accumulating a value and returning it
     *
     * @param iterable     the iterable to fold over
     * @param initialValue the initial value to use for the fold
     * @param foldFunction the function to use for the fold
     * @return the result of the fold
     * @throws NullPointerException if the iterable is null, or if the foldFunction is null
     */
    public static <T, U> U fold(Iterable<T> iterable, U initialValue, final Function2<? super U, ? super T, ? extends U> foldFunction) {
        class ChangingRef {
            U ref;

            public ChangingRef(U ref) {
                this.ref = ref;
            }
        }
        final ChangingRef accumulator = new ChangingRef(initialValue);
        each(iterable, new Closure<T>() {
            @Override
            public void apply(T it) {
                accumulator.ref = foldFunction.apply(accumulator.ref, it);
            }
        });
        return accumulator.ref;
    }

    /**
     * Iterate over two iterables, applying a function to elements taken in pairs and returning a list containing the
     * results
     *
     * @param firstIterable  the first iterable to use
     * @param secondIterable the second iterable to use
     * @param zipFunction    the function to use to zip
     * @return the zipped iterable
     * @throws NoSuchElementException if the two iterables do not have the same size
     * @throws NullPointerException   if one of the arguments is null
     */
    public static <T, U, V> List<V> zip(Iterable<T> firstIterable, Iterable<U> secondIterable, final Function2<? super T, ? super U, ? extends V> zipFunction) {
        final Iterator<U> secondIterator = secondIterable.iterator();
        final List<V> result = new LinkedList<>();
        each(firstIterable, new Closure<T>() {
            @Override
            public void apply(T it) {
                result.add(zipFunction.apply(it, secondIterator.next()));
            }
        });
        if (secondIterator.hasNext()) {
            throw new NoSuchElementException("Not enough elements in first iterable to perform the zip");
        }
        return result;
    }

    /**
     * Get only element of an iterable
     *
     * @param iterable the iterable on witch we should operate
     * @return the only element of the iterable
     * @throws NullPointerException     if the iterable is null
     * @throws IllegalArgumentException if the iterable doesn't contain a single element
     */
    public static <T> T getOnlyElement(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            T next = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalArgumentException(String.format("Expected to have only one element in iterable but there were more : %s", iterable));
            }
            return next;
        }
        throw new IllegalArgumentException(String.format("Expected to have one element in iterable but there wasn't any : %s", iterable));
    }

    private static class AddClosure<U> implements Closure<U> {
        private final List<U> result;

        public AddClosure(List<U> result) {
            this.result = result;
        }

        @Override
        public void apply(U it) {
            result.add(it);
        }
    }
}
