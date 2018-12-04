package io.fries.ioc.scanner;

import io.fries.ioc.Container;
import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.dependencies.IdentifiedDependenciesScanner;
import io.fries.ioc.scanner.registrable.ManagedRegistrableScanner;
import io.fries.ioc.scanner.registrable.ProxyRegistrableScanner;
import io.fries.ioc.scanner.registrable.RegistrableScanner;
import io.fries.ioc.scanner.registrable.SuppliedRegistrableScanner;
import io.fries.ioc.scanner.type.ReflectionTypeScanner;
import io.fries.ioc.scanner.type.TypeScanner;

import java.util.ArrayList;
import java.util.List;

public class ComponentsScanner {

    private final RegistrationContainer registrationContainer;
    private final List<RegistrableScanner> scanners;

    private ComponentsScanner(final RegistrationContainer registrationContainer) {
        this.registrationContainer = registrationContainer;
        this.scanners = new ArrayList<>();
    }

    public static ComponentsScanner of(final RegistrationContainer registrationContainer) {
        return new ComponentsScanner(registrationContainer);
    }

    public static ComponentsScanner withDefault(final Class<?> entryPoint, final RegistrationContainer registrationContainer) {
        final TypeScanner typeScanner = new ReflectionTypeScanner(entryPoint);
        final DependenciesScanner dependenciesScanner = new IdentifiedDependenciesScanner();

        return of(registrationContainer)
                .use(new ManagedRegistrableScanner(typeScanner, dependenciesScanner))
                .use(new SuppliedRegistrableScanner(typeScanner, new DefaultInstantiator()))
                .use(new ProxyRegistrableScanner(typeScanner, dependenciesScanner));
    }

    public ComponentsScanner use(final RegistrableScanner registrableScanner) {
        scanners.add(registrableScanner);
        return this;
    }

    public Container scan() {
        scanners.stream()
                .flatMap(scanner -> scanner.findAll().stream())
                .forEach(registrationContainer::register);

        return registrationContainer.instantiate();
    }
}
