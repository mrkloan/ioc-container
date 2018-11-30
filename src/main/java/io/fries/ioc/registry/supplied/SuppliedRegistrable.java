package io.fries.ioc.registry.supplied;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.Registry;

import java.util.Objects;
import java.util.function.Supplier;

public class SuppliedRegistrable implements Registrable {

    static final int NO_DEPENDENCIES = 0;

    private final Id id;
    private final Supplier<?> instanceSupplier;

    private SuppliedRegistrable(final Id id, final Supplier<?> instanceSupplier) {
        this.id = id;
        this.instanceSupplier = instanceSupplier;
    }

    public static SuppliedRegistrable of(final Id id, final Supplier<?> instanceSupplier) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(instanceSupplier);

        return new SuppliedRegistrable(id, instanceSupplier);
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public int countDependencies(final Registry registry) {
        return NO_DEPENDENCIES;
    }

    @Override
    public Component instantiate(final Instantiator instantiator, final Components components) {
        return Component.of(id, instanceSupplier.get());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SuppliedRegistrable that = (SuppliedRegistrable) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instanceSupplier, that.instanceSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instanceSupplier);
    }

    @Override
    public String toString() {
        return "SuppliedRegistrable{" +
                "id=" + id +
                ", instanceSupplier=" + instanceSupplier +
                '}';
    }
}
