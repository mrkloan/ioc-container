package io.fries.ioc;

import java.util.List;
import java.util.Optional;

class Container {

    static RegistrationContainer using(final Instantiator instantiator) {
        throw new UnsupportedOperationException();
    }

    <T> Optional<T> provide(final Id id) {
        throw new UnsupportedOperationException();
    }
}
