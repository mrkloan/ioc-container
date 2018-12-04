package io.fries.ioc.scanner;

import io.fries.ioc.Container;
import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.scanner.registrable.RegistrableScanner;

import java.util.ArrayList;
import java.util.List;

class ComponentsScanner {

    private final RegistrationContainer registrationContainer;
    private final List<RegistrableScanner> scanners;

    private ComponentsScanner(final RegistrationContainer registrationContainer) {
        this.registrationContainer = registrationContainer;
        this.scanners = new ArrayList<>();
    }

    public static ComponentsScanner of(final RegistrationContainer registrationContainer) {
        return new ComponentsScanner(registrationContainer);
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
