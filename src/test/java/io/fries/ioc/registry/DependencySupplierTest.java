package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static io.fries.ioc.registry.DependencySupplier.NO_DEPENDENCIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Dependency supplier should")
class DependencySupplierTest {

    private Id id;
    private Supplier<Object> instanceSupplier;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        this.id = mock(Id.class);
        this.instanceSupplier = mock(Supplier.class);
    }

    @Test
    @DisplayName("throw when providing a null identifier")
    void should_throw_when_providing_a_null_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DependencySupplier.of(null, () -> mock(Object.class)));
    }

    @Test
    @DisplayName("throw when providing a null instance supplier")
    void should_throw_when_providing_a_null_instance_supplier() {
        final Supplier<Object> instanceSupplier = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DependencySupplier.of(mock(Id.class), instanceSupplier));
    }

    @Test
    @DisplayName("have 0 dependencies as it is already instanced")
    void should_have_zero_dependencies() {
        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);

        final int dependenciesCount = supplier.countDependencies(mock(Registry.class));

        assertThat(dependenciesCount).isEqualTo(NO_DEPENDENCIES);
    }

    @Test
    @DisplayName("create a dependency contained the supplied instance")
    void should_inject_the_supplied_instance_into_a_dependency() {
        final Object instance = mock(Object.class);
        final Dependency dependency = Dependency.of(id, instance);
        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);

        when(instanceSupplier.get()).thenReturn(instance);
        final Dependency instancedDependency = supplier.instantiate(mock(Instantiator.class), mock(Dependencies.class));

        verify(instanceSupplier).get();
        assertThat(instancedDependency).isEqualTo(dependency);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final DependencySupplier firstSupplier = DependencySupplier.of(id, instanceSupplier);
        final DependencySupplier secondSupplier = DependencySupplier.of(id, instanceSupplier);

        assertThat(firstSupplier).isEqualTo(secondSupplier);
        assertThat(firstSupplier.hashCode()).isEqualTo(secondSupplier.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final DependencySupplier firstSupplier = DependencySupplier.of(mock(Id.class), () -> mock(Object.class));
        final DependencySupplier secondSupplier = DependencySupplier.of(mock(Id.class), () -> mock(Object.class));

        assertThat(firstSupplier).isNotEqualTo(secondSupplier);
        assertThat(firstSupplier.hashCode()).isNotEqualTo(secondSupplier.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);

        when(id.toString()).thenReturn("Id");
        when(instanceSupplier.toString()).thenReturn("Instance");
        final String result = supplier.toString();

        assertThat(result).isEqualTo("DependencySupplier{id=Id, instanceSupplier=Instance}");
    }
}