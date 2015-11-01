package com.tlorrain.futile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.fest.assertions.api.Assertions.assertThat;

public class FutileTest {

    @Test
    public void static_each() throws Exception {
        final List<Integer> results = new ArrayList<>();
        Futile.each(Arrays.asList(42, 73, 28), new Closure<Integer>() {
            @Override
            public void apply(Integer it) {
                results.add(it);
            }
        });
        assertThat(results).containsExactly(42, 73, 28);
    }

    @Test(expected = NullPointerException.class)
    public void static_each_nullIterable() throws Exception {
        Futile.each(null, new Closure<Integer>() {
            @Override
            public void apply(Integer it) {

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_each_nullClosure() throws Exception {
        Futile.each(Arrays.asList(1, 2), null);
    }

    @Test
    public void static_filter() throws Exception {
        assertThat(Futile.filter(Arrays.asList("foo", "bar", "far", "baz"), new Predicate<String>() {
            @Override
            public boolean apply(String it) {
                return it.contains("r");
            }
        })).containsExactly("bar", "far");
    }

    @Test(expected = NullPointerException.class)
    public void static_filter_nullIterable() throws Exception {
        Futile.filter(null, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer it) {
                return true;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_filter_nullClosure() throws Exception {
        Futile.filter(Arrays.asList(1, 2), null);
    }

    @Test
    public void static_map() throws Exception {
        assertThat(Futile.map(Arrays.asList("foo", "bar", "baz"), new Function1<String, String>() {
            @Override
            public String apply(String it) {
                return "a" + it;
            }
        })).containsExactly("afoo", "abar", "abaz");
    }

    @Test(expected = NullPointerException.class)
    public void static_map_nullIterable() throws Exception {
        Futile.map(null, new Function1<Object, Object>() {
            @Override
            public Object apply(Object it) {
                return it;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_map_nullClosure() throws Exception {
        Futile.map(Arrays.asList(1, 2), null);
    }

    @Test
    public void static_flatMap() throws Exception {
        assertThat(Futile.flatMap(Arrays.asList("foo", "bar"), new Function1<String, List<Character>>() {
            @Override
            public List<Character> apply(String it) {
                List<Character> result = new ArrayList<>();
                for (char c : it.toCharArray()) {
                    result.add(c);
                }
                return result;
            }
        })).containsExactly('f', 'o', 'o', 'b', 'a', 'r');
    }

    @Test(expected = NullPointerException.class)
    public void static_flatMap_nullIterable() throws Exception {
        Futile.flatMap(null, new Function1<Object, Iterable<Object>>() {
            @Override
            public Iterable<Object> apply(Object it) {
                return Collections.singleton(it);
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_flatMap_nullClosure() throws Exception {
        Futile.flatMap(Arrays.asList(1, 2), null);
    }

    @Test(expected = NullPointerException.class)
    public void static_flatMap_closureReturnsNull() throws Exception {
        Futile.flatMap(Arrays.asList(1, 2), new Function1<Object, Iterable<Object>>() {
            @Override
            public Iterable<Object> apply(Object it) {
                return null;
            }
        });
    }

    @Test
    public void static_fold() {
        assertThat(Futile.fold(Arrays.asList(1, 2, 3), 0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        })).isEqualTo(6);
    }

    @Test(expected = NullPointerException.class)
    public void static_fold_nullIterable() {
        Futile.fold(null, 0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_fold_nullFunction() {
        Futile.fold(Arrays.asList(1, 2, 3), 0, null);
    }

    @Test
    public void static_zip() {
        assertThat(Futile.zip(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        })).containsExactly(5, 7, 9);
    }

    @Test(expected = NoSuchElementException.class)
    public void static_zip_firstShorter() {
        Futile.zip(Arrays.asList(1, 2), Arrays.asList(4, 5, 6), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NoSuchElementException.class)
    public void static_zip_secondShorter() {
        Futile.zip(Arrays.asList(1, 2, 3), Arrays.asList(4, 5), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_zip_firstNull() {
        Futile.zip(null, Arrays.asList(4, 5), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_zip_secondNull() {
        Futile.zip(Arrays.asList(4, 5), null, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void static_zip_functionNull() {
        Futile.zip(Arrays.asList(4, 5),Arrays.asList(4, 5), null);
    }

    @Test
    public void static_getOnlyElement() throws Exception {
        assertThat(Futile.getOnlyElement(Collections.singleton(42))).isEqualTo(42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void static_getOnlyElement_empty() throws Exception {
        Futile.getOnlyElement(Collections.emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void static_getOnlyElement_multiple() throws Exception {
        Futile.getOnlyElement(Arrays.asList(1, 2));
    }

    @Test
    public void iterator() throws Exception {
        Iterator<Integer> iterator = Futile.from(Arrays.asList(42, 73)).iterator();
        assertThat(iterator.next()).isEqualTo(42);
        assertThat(iterator.next()).isEqualTo(73);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void toSingle() throws Exception {
        assertThat(Futile.from(Collections.singleton(42)).toSingle()).isEqualTo(42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingle_empty() throws Exception {
        Futile.from(Collections.emptySet()).toSingle();
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingle_multiple() throws Exception {
        Futile.from(Arrays.asList(1, 2)).toSingle();
    }

    @Test
    public void toArrayList() throws Exception {
        assertThat(Futile.from(Arrays.asList(1, 2)).toArrayList())
                .isOfAnyClassIn(ArrayList.class)
                .containsExactly(1, 2);
    }

    @Test
    public void toLinkedList() throws Exception {
        assertThat(Futile.from(Arrays.asList(1, 2)).toLinkedList())
                .isOfAnyClassIn(LinkedList.class)
                .containsExactly(1, 2);
    }

    @Test(expected = NullPointerException.class)
    public void from_null() throws Exception {
        Futile.from(null);
    }


    @Test
    public void each() throws Exception {
        final List<Integer> results = new ArrayList<>();
        Futile.from(Arrays.asList(42, 73, 28)).each(new Closure<Integer>() {
            @Override
            public void apply(Integer it) {
                results.add(it);
            }
        });
        assertThat(results).containsExactly(42, 73, 28);

    }

    @Test(expected = NullPointerException.class)
    public void each_nullClosure() throws Exception {
        Futile.from(Arrays.asList(42, 73, 28)).each(null);
    }

    @Test
    public void filter() throws Exception {
        assertThat(Futile.from(Arrays.asList("foo", "bar", "far", "baz")).filter(new Predicate<String>() {
            @Override
            public boolean apply(String it) {
                return it.contains("r");
            }
        })).containsExactly("bar", "far");
    }

    @Test(expected = NullPointerException.class)
    public void filter_nullClosure() throws Exception {
        Futile.from(Arrays.asList("foo", "bar", "far", "baz")).filter(null);
    }

    @Test
    public void map() throws Exception {
        assertThat(Futile.from(Arrays.asList("foo", "bar", "baz")).map(new Function1<String, String>() {
            @Override
            public String apply(String it) {
                return "a" + it;
            }
        })).containsExactly("afoo", "abar", "abaz");
    }

    @Test(expected = NullPointerException.class)
    public void map_nullFunction() throws Exception {
        Futile.from(Arrays.asList("foo", "bar", "baz")).map(null);
    }

    @Test
    public void flatMap() throws Exception {
        assertThat(Futile.from(Arrays.asList("foo", "bar")).flatMap(new Function1<String, List<Character>>() {
            @Override
            public List<Character> apply(String it) {
                List<Character> result = new ArrayList<>();
                for (char c : it.toCharArray()) {
                    result.add(c);
                }
                return result;
            }
        })).containsExactly('f', 'o', 'o', 'b', 'a', 'r');
    }

    @Test(expected = NullPointerException.class)
    public void flatMap_nullFunction() throws Exception {
        Futile.from(Arrays.asList("foo", "bar")).flatMap(null);
    }

    @Test
    public void fold() {
        assertThat(Futile.from(Arrays.asList(1, 2, 3)).fold(0, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        })).containsExactly(6);
    }

    @Test(expected = NullPointerException.class)
    public void fold_nullFunction() {
        Futile.from(Arrays.asList(1, 2, 3)).fold(0, null);
    }

    @Test
    public void zip() {
        assertThat(Futile.from(Arrays.asList(1, 2, 3)).zip(Arrays.asList(4, 5, 6), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        })).containsExactly(5, 7, 9);
    }

    @Test(expected = NoSuchElementException.class)
    public void zip_firstShorter() {
        Futile.from(Arrays.asList(1, 2)).zip(Arrays.asList(4, 5, 6), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NoSuchElementException.class)
    public void zip_secondShorter() {
        Futile.from(Arrays.asList(1, 2, 3)).zip(Arrays.asList(4, 5), new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void zip_iterableNull() {
        Futile.from(Arrays.asList(4, 5)).zip(null, new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer arg1, Integer arg2) {
                return arg1 + arg2;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void zip_functionNull() {
        Futile.from(Arrays.asList(4, 5)).zip(Arrays.asList(4, 5), null);
    }

}