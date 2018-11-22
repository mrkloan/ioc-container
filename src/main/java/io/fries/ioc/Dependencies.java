package io.fries.ioc;

import java.util.List;
import java.util.Objects;

class Dependencies {

    private final List<Dependency> dependencies;

    private Dependencies(final List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    static Dependencies of(final List<Dependency> dependencies) {
        return new Dependencies(dependencies);
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
}
