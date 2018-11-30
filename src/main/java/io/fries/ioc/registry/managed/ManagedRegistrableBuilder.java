package io.fries.ioc.registry.managed;

import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

public class ManagedRegistrableBuilder implements RegistrableBuilder {

    public static ManagedRegistrableBuilder managed(final Class<?> type) {
        throw new UnsupportedOperationException();
    }

    public ManagedRegistrableBuilder with(final Object... dependencies) {
        throw new UnsupportedOperationException();
    }

    public <ID> ManagedRegistrableBuilder as(final ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Registrable build() {
        throw new UnsupportedOperationException();
    }
}
