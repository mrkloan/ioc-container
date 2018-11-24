package io.fries.ioc;

import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.Collections.*;
import static java.util.Objects.isNull;

class Dependencies {

    private final Map<Id, Dependency> dependencies;

    private Dependencies(final Map<Id, Dependency> dependencies) {
        this.dependencies = unmodifiableMap(dependencies);
    }

    static Dependencies of(final Map<Id, Dependency> dependencies) {
        return new Dependencies(dependencies);
    }

    static Dependencies empty() {
        return of(emptyMap());
    }

    static BinaryOperator<Dependencies> combiner() {
        return (first, second) -> first;
    }

    Dependencies add(final Dependency dependency) {
        final Map<Id, Dependency> dependencies = new HashMap<>(this.dependencies);
        dependencies.put(dependency.getId(), dependency);

        return of(dependencies);
    }

    Dependency get(final Id id) {
        final Dependency dependency = dependencies.get(id);

        if (isNull(dependency))
            throw new NoSuchElementException("The specified dependency is not registered in the container");

        return dependency;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Dependencies that = (Dependencies) o;
        return Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }

    @Override
    public String toString() {
        return "Dependencies{" +
                "dependencies=" + dependencies +
                '}';
    }
}
