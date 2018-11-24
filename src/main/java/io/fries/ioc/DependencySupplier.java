package io.fries.ioc;

import java.util.Objects;
import java.util.function.Supplier;

class DependencySupplier implements RegisteredDependency {

    static final int NO_DEPENDENCIES = 0;

    private final Id id;
    private final Supplier<Object> instanceSupplier;

    private DependencySupplier(final Id id, final Supplier<Object> instanceSupplier) {
        this.id = id;
        this.instanceSupplier = instanceSupplier;
    }

    static DependencySupplier of(final Id id, final Supplier<Object> instanceSupplier) {
        return new DependencySupplier(id, instanceSupplier);
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
