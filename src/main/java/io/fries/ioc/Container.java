package io.fries.ioc;

import java.util.Objects;

class Container {

    private final Dependencies dependencies;

    private Container(final Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    static Container of(final Dependencies dependencies) {
        return new Container(dependencies);
    }

    static RegistrationContainer using(final Instantiator instantiator) {
        return RegistrationContainer.of(instantiator, Registry.empty());
    }

    static RegistrationContainer empty() {
        return RegistrationContainer.of(new DefaultInstantiator(), Registry.empty());
    }

    <T> T provide(final Id id) {
        return dependencies.getInstance(id);
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

    @Override
    public String toString() {
        return "Container{" +
                "dependencies=" + dependencies +
                '}';
    }
}
