package io.fries.ioc.dependencies;

import java.util.Objects;

public class Id {

    private final String value;

    private Id(final String value) {
        this.value = value;
    }

    public static <ID> Id of(final ID value) {
        Objects.requireNonNull(value);

        return new Id(value.toString());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Id id = (Id) o;
        return Objects.equals(value, id.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Id{" +
                "value='" + value + '\'' +
                '}';
    }
}
