package io.fries.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

class Tokens {

    private final List<DependencyToken> tokens;

    private Tokens(final List<DependencyToken> tokens) {
        this.tokens = unmodifiableList(tokens);
    }

    static Tokens of(final List<DependencyToken> tokens) {
        return new Tokens(tokens);
    }

    static Tokens empty() {
        return of(emptyList());
    }

    Tokens add(final DependencyToken token) {
        final List<DependencyToken> tokens = new ArrayList<>(this.tokens);
        tokens.add(token);

        return of(tokens);
    }

    DependencyToken get(final Id id) {
        return tokens
                .stream()
                .filter(token -> token.isIdentifiedBy(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("The specified dependency token is not registered in the registration container"));
    }

    Dependencies instantiate(final Instantiator instantiator) {
        final List<DependencyToken> sortedTokens = topologicalSort(tokens);

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

    private List<DependencyToken> topologicalSort(final List<DependencyToken> tokens) {
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
