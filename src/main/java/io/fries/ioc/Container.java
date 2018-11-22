package io.fries.ioc;

import java.util.Optional;

class Container {

    static RegistrationContainer using(final Instantiator instantiator) {
        return null;
    }

    <T> Optional<T> provide(final Id id) {
        return Optional.empty();
    }
}
