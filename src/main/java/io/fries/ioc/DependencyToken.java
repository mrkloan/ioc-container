package io.fries.ioc;

import java.util.List;
import java.util.Objects;

class DependencyToken {

    private final Id id;
    private final Class<?> type;
    private final List<Id> dependencies;

    private DependencyToken(final Id id, final Class<?> type, final List<Id> dependencies) {
        this.id = id;
        this.type = type;
        this.dependencies = dependencies;
    }

    static DependencyToken of(final Id id, final Class<?> type, final List<Id> dependencies) {
        return new DependencyToken(id, type, dependencies);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DependencyToken that = (DependencyToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, dependencies);
    }
}
