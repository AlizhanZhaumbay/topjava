package ru.javawebinar.topjava;



import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertMatcher<T> {
    private final String[] fields;

    private AssertMatcher(String... fields) {
        this.fields = fields;
    }

    public static <T> AssertMatcher<T> of(String... fields) {
        return new AssertMatcher<>(fields);
    }

    public void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(fields).isEqualTo(expected);
    }

    public void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fields).isEqualTo(expected);
    }

}
