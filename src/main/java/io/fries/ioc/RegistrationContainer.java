package io.fries.ioc;

import java.util.List;

class RegistrationContainer {

    private final Instantiator instantiator;
    private Tokens tokens;

    private RegistrationContainer(final Instantiator instantiator, final Tokens tokens) {
        this.instantiator = instantiator;
        this.tokens = tokens;
    }

    static RegistrationContainer of(final Instantiator instantiator, final Tokens tokens) {
        return new RegistrationContainer(instantiator, tokens);
    }

    RegistrationContainer register(final Id id, final Class<?> type, final List<Id> dependencies) {
        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        tokens = tokens.add(token);

        return this;
    }

    Container createInstances() {
        throw new UnsupportedOperationException();
    }
}
