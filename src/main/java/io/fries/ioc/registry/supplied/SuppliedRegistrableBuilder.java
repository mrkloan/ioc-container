package io.fries.ioc.registry.supplied;

import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

import java.util.function.Supplier;

public class SuppliedRegistrableBuilder implements RegistrableBuilder {

    public static SuppliedRegistrableBuilder supply(final Supplier<?> instanceSupplier) {
        throw new UnsupportedOperationException();
    }

    public <ID> SuppliedRegistrableBuilder as(final ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Registrable build() {
        throw new UnsupportedOperationException();
    }
}
