package io.fries.ioc.dependencies;

import java.util.Objects;

public class Dependency {

    private final Id id;
    private final Class<?> type;
    private final Object instance;

    private Dependency(final Id id, final Class<?> type, final Object instance) {
        this.id = id;
        this.type = type;
        this.instance = instance;
    }

    public static Dependency of(final Id id, final Class<?> type, final Object instance) {
        return new Dependency(id, type, instance);
    }

    public Id getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance() {
        return (T) instance;
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

    @Override
    public String toString() {
        return "Dependency{" +
                "id=" + id +
                ", instance=" + instance +
                '}';
    }
}
