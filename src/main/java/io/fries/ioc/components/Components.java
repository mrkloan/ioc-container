package io.fries.ioc.components;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Components {

    private final Map<Id, Component> dependencies;

    private Components(final Map<Id, Component> dependencies) {
        this.dependencies = dependencies;
    }

    public static Components of(final Map<Id, Component> dependencies) {
        return new Components(dependencies);
    }

    public static Components empty() {
        return of(new HashMap<>());
    }

    public Components add(final Component component) {
        dependencies.put(component.getId(), component);

        return this;
    }

    public List<Component> findAllById(final List<Id> identifiers) {
        return identifiers
                .stream()
                .map(this::get)
                .collect(toList());
    }

    Component get(final Id id) {
        final Component component = dependencies.get(id);

        if (isNull(component))
            throw new NoSuchElementException("No component registered with id: " + id);

        return component;
    }

    public <T> T getInstance(final Id id) {
        return get(id).getInstance();
    }

    public Components merge(final Components components) {
        final Map<Id, Component> merged = Stream.of(this.dependencies, components.dependencies)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Entry::getKey, Entry::getValue));

        return Components.of(merged);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Components that = (Components) o;
        return Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }

    @Override
    public String toString() {
        return "Components{" +
                "components=" + dependencies +
                '}';
    }
}
