package io.fries.ioc;

import java.util.*;
import java.util.function.BiFunction;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

class Tokens {

    private final Map<Id, DependencyToken> tokens;

    private Tokens(final Map<Id, DependencyToken> tokens) {
        this.tokens = unmodifiableMap(tokens);
    }

    static Tokens of(final Map<Id, DependencyToken> tokens) {
        return new Tokens(tokens);
    }

    static Tokens empty() {
        return of(emptyMap());
    }

    Tokens add(final Id id, final DependencyToken token) {
        if(tokens.containsKey(id))
            throw new IllegalStateException("Another dependency token was already registered with the id: " + id);

        final Map<Id, DependencyToken> tokens = new HashMap<>(this.tokens);
        tokens.put(id, token);

        return of(tokens);
    }

    DependencyToken get(final Id id) {
        final DependencyToken token = tokens.get(id);

        if (isNull(token))
            throw new NoSuchElementException("The specified dependency token is not registered in the registration container");

        return token;
    }

    Dependencies instantiate(final Instantiator instantiator) {
        final List<DependencyToken> sortedTokens = topologicalSort(tokens.values());

        return sortedTokens
                .stream()
                .reduce(
                        Dependencies.empty(),
                        reduceDependencies(instantiator),
                        Dependencies.combiner()
                );

    }

    private BiFunction<Dependencies, DependencyToken, Dependencies> reduceDependencies(final Instantiator instantiator) {
        return (dependencies, token) -> {
            final Dependency dependency = token.instantiate(instantiator, dependencies);
            return dependencies.add(dependency);
        };
    }

    private List<DependencyToken> topologicalSort(final Collection<DependencyToken> tokens) {
        return tokens
                .stream()
                .sorted(this::compareTokens)
                .collect(toList());
    }

    private int compareTokens(final DependencyToken firstToken, final DependencyToken secondToken) {
        return firstToken.countDependencies(this) - secondToken.countDependencies(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tokens tokens1 = (Tokens) o;
        return Objects.equals(tokens, tokens1.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokens);
    }

    @Override
    public String toString() {
        return "Tokens{" +
                "tokens=" + tokens +
                '}';
    }
}
