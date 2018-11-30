package io.fries.ioc.registry.proxy;

import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

public class ProxyRegistrableBuilder implements RegistrableBuilder {

    public static ProxyRegistrableBuilder proxy(final Class<?> type) {
        throw new UnsupportedOperationException();
    }

    public ProxyRegistrableBuilder of(final Class<?> interfaceType) {
        throw new UnsupportedOperationException();
    }

    public ProxyRegistrableBuilder with(final Object... dependencies) {
        throw new UnsupportedOperationException();
    }

    public <ID> ProxyRegistrableBuilder as(final ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Registrable build() {
        throw new UnsupportedOperationException();
    }
}
