package io.fries.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

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

    <T> T getInstance(final Id id) {
        return get(id).getInstance();
    }

    Dependencies merge(final Dependencies dependencies) {
        final Map<Id, Dependency> merged = Stream.of(this.dependencies, dependencies.dependencies)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Dependencies.of(merged);
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
