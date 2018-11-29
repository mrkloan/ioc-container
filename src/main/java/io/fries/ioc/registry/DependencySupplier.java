package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;

import java.util.Objects;
import java.util.function.Supplier;

public class DependencySupplier implements RegisteredDependency {

    static final int NO_DEPENDENCIES = 0;

    private final Id id;
    private final Supplier<?> instanceSupplier;

    private DependencySupplier(final Id id, final Supplier<?> instanceSupplier) {
        this.id = id;
        this.instanceSupplier = instanceSupplier;
    }

    public static DependencySupplier of(final Id id, final Supplier<?> instanceSupplier) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(instanceSupplier);

        return new DependencySupplier(id, instanceSupplier);
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
    public Dependency instantiate(final Instantiator instantiator, final Dependencies dependencies) {
        return Dependency.of(id, instanceSupplier.get());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DependencySupplier that = (DependencySupplier) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instanceSupplier, that.instanceSupplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instanceSupplier);
    }

    @Override
    public String toString() {
        return "DependencySupplier{" +
                "id=" + id +
                ", instanceSupplier=" + instanceSupplier +
                '}';
    }
}
