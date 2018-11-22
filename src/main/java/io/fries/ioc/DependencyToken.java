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

    int countDeepDependencies(final Tokens tokens) {
        final int firstLevelDependencies = dependencies.size();
        final int deepDependencies = dependencies
                .stream()
                .map(tokens::get)
                .mapToInt(token -> token.countDeepDependencies(tokens))
                .sum();

        return firstLevelDependencies + deepDependencies;
    }

    Dependency instantiate(final Instantiator instantiator) {
        throw new UnsupportedOperationException();
    }

    boolean isIdentifiedBy(final Id id) {
        return this.id.equals(id);
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

    @Override
    public String toString() {
        return "DependencyToken{" +
                "id=" + id +
                ", type=" + type +
                ", dependencies=" + dependencies +
                '}';
    }
}
