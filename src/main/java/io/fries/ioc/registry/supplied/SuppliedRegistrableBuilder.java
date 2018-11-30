package io.fries.ioc.registry.supplied;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;

import java.util.Objects;
import java.util.function.Supplier;

public class SuppliedRegistrableBuilder implements RegistrableBuilder {

    private final Id id;
    private final Supplier<?> instanceSupplier;

    SuppliedRegistrableBuilder(final Id id, final Supplier<?> instanceSupplier) {
        this.id = id;
        this.instanceSupplier = instanceSupplier;
    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SuppliedRegistrableBuilder that = (SuppliedRegistrableBuilder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instanceSupplier, that.instanceSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instanceSupplier);
    }

    @Override
    public String toString() {
        return "SuppliedRegistrableBuilder{" +
                "id=" + id +
                ", instanceSupplier=" + instanceSupplier +
                '}';
    }
}
