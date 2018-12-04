package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import io.fries.ioc.scanner.ComponentsScanner;
import io.fries.ioc.scanner.registrable.RegistrableScanner;

import java.util.Objects;

import static java.util.Arrays.stream;

public class Container {

    private final Components components;

    private Container(final Components components) {
        this.components = components;
    }

    static Container of(final Components components) {
        return new Container(components);
    }

    @SuppressWarnings("WeakerAccess")
    public static RegistrationContainer using(final Instantiator instantiator) {
        Objects.requireNonNull(instantiator);
        return RegistrationContainer.of(instantiator, Registry.empty());
    }

    @SuppressWarnings("WeakerAccess")
    public static RegistrationContainer empty() {
        return RegistrationContainer.of(new DefaultInstantiator(), Registry.empty());
    }

    @SuppressWarnings("WeakerAccess")
    public static Container scan(final Class<?> entryPoint, final RegistrableScanner... scanners) {
        return scan(entryPoint, empty(), scanners);
    }

    @SuppressWarnings("WeakerAccess")
    public static Container scan(final Class<?> entryPoint, final RegistrationContainer registrationContainer, final RegistrableScanner... scanners) {
        final ComponentsScanner componentsScanner = ComponentsScanner.withDefault(entryPoint, registrationContainer);
        stream(scanners).forEach(componentsScanner::use);

        return componentsScanner.scan();
    }

    @SuppressWarnings("WeakerAccess")
    public <T, ID> T provide(final ID id) {
        final Id componentId = Id.of(id);
        return components.getInstance(componentId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Container container = (Container) o;
        return Objects.equals(components, container.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    @Override
    public String toString() {
        return "Container{" +
                "components=" + components +
                '}';
    }
}
