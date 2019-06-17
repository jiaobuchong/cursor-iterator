package com.github.phantomthieft.test.abstraceiterator;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

/**
 * Created on 2019-06-17
 */
public class AbstractIteratorTest {
    @Test
    public void testIterator() {
        List<String> source = Lists.newArrayList(null, "1", "2", "3", null, "4", null);
        Iterator<String> iterator = new Iterator<String>() {
            private Iterator<String> sourceIterator = source.iterator();
            private String next;

            public boolean hasNext() {
                while (sourceIterator.hasNext()) {
                    next = sourceIterator.next();
                    if (next != null) {
                        return true;
                    }
                }
                next = null;
                return false;
            }

            public String next() {
                while (next == null) {
                    next = sourceIterator.next();
                }
                String temp = next;
                next = null;
                return temp;
            }

            public void remove() {
            }
        };

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void testAbstractIterator() {
        List<String> source = Lists.newArrayList(null, "1", "2", "3", null, "4", null);
        Iterator<String> iterator = new AbstractIterator<String>() {
            private Iterator<String> sourceIterator = source.iterator();

            @Override
            protected String computeNext() {
                while (sourceIterator.hasNext()) {
                    String next = sourceIterator.next();
                    if (next != null) {
                        return next;
                    }
                }
                return endOfData();
            }
        };

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
