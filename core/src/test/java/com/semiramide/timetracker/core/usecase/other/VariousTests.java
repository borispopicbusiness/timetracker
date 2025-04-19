package com.semiramide.timetracker.core.usecase.other;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

public class VariousTests {
    @Test
    void shouldBeOnlyOneIntersection() {
        List<String> l1 = Arrays.asList("a", "b", "c", "d");
        List<String> l2 = Arrays.asList("a", "e", "f", "g");
        assertFalse(Sets.intersection(new HashSet<>(l1), new HashSet<>(l2)).isEmpty());
        assertEquals(1, Sets.intersection(new HashSet<>(l1), new HashSet<>(l2)).size());
        assertEquals("a", Sets.intersection(new HashSet<>(l1), new HashSet<>(l2)).stream().iterator().next());
    }
}
