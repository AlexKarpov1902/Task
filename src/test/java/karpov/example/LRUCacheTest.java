package karpov.example;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class LRUCacheTest {
    @Test
    public void when10AddNew() {
        LRUCache<Integer, String> c = new LRUCache<>(4);
        for (int i = 0; i < 10; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        assertThat(c.getValue(6), is("стр-6"));
        assertThat(c.getValue(7), is("стр-7"));
        assertThat(c.getValue(8), is("стр-8"));
        assertThat(c.getValue(9), is("стр-9"));
    }

    @Test
    public void when4AddNew2GetThen2AddNew() {
        LRUCache<Integer, String> c = new LRUCache<>(4);
        for (int i = 0; i < 4; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        c.getValue(0);
        c.getValue(1);
        c.putValue(4, "стр-4");
        c.putValue(5, "стр-5");
        assertThat(c.getValue(0), is("стр-0"));
        assertThat(c.getValue(1), is("стр-1"));
    }
}