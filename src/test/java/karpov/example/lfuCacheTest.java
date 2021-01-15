package karpov.example;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class lfuCacheTest {
    @Test
    public void whenAdd10() {
        LfuCache<Integer, String> c = new LfuCache<>(4);
        for (int i = 0; i < 10; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        assertThat(c.getValue(7), is("стр-7"));
        assertThat(c.getValue(8), is("стр-8"));
        assertThat(c.getValue(9), is("стр-9"));
    }
    @Test
    public void when5AddAnd10GetAndAdd5New() {
        LfuCache<Integer, String> c = new LfuCache<>(4);

        for (int i = 0; i < 5; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        for (int i = 0; i < 5; i++) {
            c.getValue(2);
        }
        for (int i = 10; i < 15; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        assertThat(c.getValue(2), is("стр-2"));
        assertThat(c.getValue(13), is("стр-13"));
        assertThat(c.getValue(14), is("стр-14"));
    }

    @Test
    public void when3OftenAnd5AddNew() {
        LfuCache<Integer, String> c = new LfuCache<>(4);
        for (int i = 0; i < 4; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        for (int i = 0; i < 5; i++) {
            c.getValue(1);
            c.getValue(2);
            c.getValue(3);
        }
        for (int i = 10; i < 15; i++) {
            c.putValue(i, String.format("стр-%d", i));
        }
        assertThat(c.getValue(1), is("стр-1"));
        assertThat(c.getValue(2), is("стр-2"));
        assertThat(c.getValue(3), is("стр-3"));
    }
}