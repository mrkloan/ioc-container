package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class Registry {

    private final Map<Id, RegisteredDependency> registeredDependency;

    private Registry(final Map<Id, RegisteredDependency> registeredDependency) {
        this.registeredDependency = unmodifiableMap(registeredDependency);
    }

    public static Registry of(final Map<Id, RegisteredDependency> tokens) {
        return new Registry(tokens);
    }

    public static Registry empty() {
        return of(emptyMap());
    }

    public Registry add(final RegisteredDependency token) {
        if (registeredDependency.containsKey(token.getId()))
            throw new IllegalStateException("Another dependency was already registered with the id: " + token.getId());

        final Map<Id, RegisteredDependency> tokens = new HashMap<>(this.registeredDependency);
        tokens.put(token.getId(), token);

        return of(tokens);
    }

    RegisteredDependency get(final Id id) {
        final RegisteredDependency token = registeredDependency.get(id);

        if (isNull(token))
            throw new NoSuchElementException("This identifier is not linked to any dependency inside the container: " + id);

        return token;
    }

    public Dependencies instantiate(final Instantiator instantiator) {
        final List<RegisteredDependency> sortedTokens = topologicalSort(registeredDependency.values());

        return sortedTokens
                .stream()
                .reduce(
                        Dependencies.empty(),
                        reduceDependencies(instantiator),
                        Dependencies::merge
                );
    }

    private BiFunction<Dependencies, RegisteredDependency, Dependencies> reduceDependencies(final Instantiator instantiator) {
        return (dependencies, token) -> {
            final Dependency dependency = token.instantiate(instantiator, dependencies);
            return dependencies.add(dependency);
        };
    }

    private List<RegisteredDependency> topologicalSort(final Collection<RegisteredDependency> tokens) {
        return tokens
                .stream()
                .sorted(this::compareTokens)
                .collect(toList());
    }

    private int compareTokens(final RegisteredDependency firstToken, final RegisteredDependency secondToken) {
        return firstToken.countDependencies(this) - secondToken.countDependencies(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Registry registry1 = (Registry) o;
        return Objects.equals(registeredDependency, registry1.registeredDependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredDependency);
    }

    @Override
    public String toString() {
        return "Registry{" +
                "registeredDependencies=" + registeredDependency +
                '}';
    }
}
