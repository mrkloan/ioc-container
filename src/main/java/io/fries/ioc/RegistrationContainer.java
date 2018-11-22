package io.fries.ioc;

import java.util.List;

class RegistrationContainer {

    RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        throw new UnsupportedOperationException();
    }

    Container createInstances() {
        throw new UnsupportedOperationException();
    }
}
