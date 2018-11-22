package io.fries.ioc;

import java.util.Objects;

class Dependency {

    private final Id id;
    private final Object instance;

    private Dependency(final Id id, final Object instance) {
        this.id = id;
        this.instance = instance;
    }

    static Dependency of(final Id id, final Object instance) {
        return new Dependency(id, instance);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Dependency that = (Dependency) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instance);
    }
}
