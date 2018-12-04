package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import io.fries.ioc.scanner.ComponentsScanner;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.dependencies.IdentifiedDependenciesScanner;
import io.fries.ioc.scanner.registrable.ManagedRegistrableScanner;
import io.fries.ioc.scanner.registrable.ProxyRegistrableScanner;
import io.fries.ioc.scanner.registrable.SuppliedRegistrableScanner;
import io.fries.ioc.scanner.type.ReflectionTypeScanner;
import io.fries.ioc.scanner.type.TypeScanner;

import java.util.Objects;

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
    public static Container scan(final Class<?> entryPoint) {
        final Instantiator instantiator = new DefaultInstantiator();
        final RegistrationContainer registrationContainer = using(instantiator);

        final TypeScanner typeScanner = new ReflectionTypeScanner(entryPoint);
        final DependenciesScanner dependenciesScanner = new IdentifiedDependenciesScanner();

        return ComponentsScanner.of(registrationContainer)
                .use(new ManagedRegistrableScanner(typeScanner, dependenciesScanner))
                .use(new SuppliedRegistrableScanner(typeScanner, instantiator))
                .use(new ProxyRegistrableScanner(typeScanner, dependenciesScanner))
                .scan();
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
