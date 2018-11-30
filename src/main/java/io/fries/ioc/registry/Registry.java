package io.fries.ioc.registry;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class Registry {

    private final Map<Id, Registrable> registrables;

    private Registry(final Map<Id, Registrable> registrables) {
        this.registrables = unmodifiableMap(registrables);
    }

    public static Registry of(final Map<Id, Registrable> registrables) {
        return new Registry(registrables);
    }

    public static Registry empty() {
        return of(emptyMap());
    }

    public Registry add(final Registrable registrable) {
        if (registrables.containsKey(registrable.getId()))
            throw new IllegalStateException("Another component was already registered with the id: " + registrable.getId());

        final Map<Id, Registrable> registrables = new HashMap<>(this.registrables);
        registrables.put(registrable.getId(), registrable);

        return of(registrables);
    }

    public Registrable get(final Id id) {
        final Registrable registrable = registrables.get(id);

        if (isNull(registrable))
            throw new NoSuchElementException("This identifier is not linked to any component inside the container: " + id);

        return registrable;
    }

    public Components instantiate(final Instantiator instantiator) {
        final List<Registrable> sortedRegistrables = topologicalSort(registrables.values());

        return sortedRegistrables
                .stream()
                .reduce(
                        Components.empty(),
                        reduceDependencies(instantiator),
                        Components::merge
                );
    }

    private BiFunction<Components, Registrable, Components> reduceDependencies(final Instantiator instantiator) {
        return (components, registrable) -> {
            final Component component = registrable.instantiate(instantiator, components);
            return components.add(component);
        };
    }

    private List<Registrable> topologicalSort(final Collection<Registrable> registrables) {
        return registrables
                .stream()
                .sorted(this::compareRegistrables)
                .collect(toList());
    }

    private int compareRegistrables(final Registrable firstRegistrable, final Registrable secondRegistrable) {
        return firstRegistrable.countDependencies(this) - secondRegistrable.countDependencies(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Registry registry1 = (Registry) o;
        return Objects.equals(registrables, registry1.registrables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrables);
    }

    @Override
    public String toString() {
        return "Registry{" +
                "registeredDependencies=" + registrables +
                '}';
    }
}
