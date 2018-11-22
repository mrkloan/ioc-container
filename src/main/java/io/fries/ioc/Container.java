package io.fries.ioc;

import java.util.Optional;

class Container {

    static RegistrationContainer using(final Instantiator instantiator) {
        return new RegistrationContainer(instantiator, Tokens.empty());
    }

    <T> Optional<T> provide(final Id id) {
        return Optional.empty();
    }
}
