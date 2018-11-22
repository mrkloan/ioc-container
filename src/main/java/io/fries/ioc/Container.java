package io.fries.ioc;

import java.util.Optional;

class Container {

    static RegistrationContainer using(final Instantiator instantiator) {
        return RegistrationContainer.of(instantiator, Tokens.empty());
    }

    <T> Optional<T> provide(final Id id) {
        throw new UnsupportedOperationException();
    }
}
