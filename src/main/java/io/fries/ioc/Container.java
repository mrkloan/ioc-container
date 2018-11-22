package io.fries.ioc;

import java.util.Objects;
import java.util.Optional;

class Container {

    private final Dependencies dependencies;

    private Container(final Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    static Container of(final Dependencies dependencies) {
        return new Container(dependencies);
    }

    static RegistrationContainer using(final Instantiator instantiator) {
        return RegistrationContainer.of(instantiator, Tokens.empty());
    }

    <T> Optional<T> provide(final Id id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Container container = (Container) o;
        return Objects.equals(dependencies, container.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }
}
