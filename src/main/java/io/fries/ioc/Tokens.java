package io.fries.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

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

    Dependencies instantiate() {
        throw new UnsupportedOperationException();
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
