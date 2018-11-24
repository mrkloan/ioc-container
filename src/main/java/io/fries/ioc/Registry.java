package io.fries.ioc;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

class Registry {

    private final Map<Id, RegisteredDependency> registeredDependency;

    private Registry(final Map<Id, RegisteredDependency> registeredDependency) {
        this.registeredDependency = unmodifiableMap(registeredDependency);
    }

    static Registry of(final Map<Id, RegisteredDependency> tokens) {
        return new Registry(tokens);
    }

    static Registry empty() {
        return of(emptyMap());
    }

    Registry add(final Id id, final RegisteredDependency token) {
        if(registeredDependency.containsKey(id))
            throw new IllegalStateException("Another dependency token was already registered with the id: " + id);

        final Map<Id, RegisteredDependency> tokens = new HashMap<>(this.registeredDependency);
        tokens.put(id, token);

        return of(tokens);
    }

    RegisteredDependency get(final Id id) {
        final RegisteredDependency token = registeredDependency.get(id);

        if (isNull(token))
            throw new NoSuchElementException("The specified dependency token is not registered in the registration container");

        return token;
    }

    Dependencies instantiate(final Instantiator instantiator) {
        final List<RegisteredDependency> sortedTokens = topologicalSort(registeredDependency.values());

        return sortedTokens
                .stream()
                .reduce(
                        Dependencies.empty(),
                        reduceDependencies(instantiator),
                        Dependencies.combiner()
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
