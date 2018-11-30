package io.fries.ioc.registry.supplied;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static io.fries.ioc.registry.supplied.SuppliedRegistrable.NO_DEPENDENCIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Component supplier should")
class SuppliedRegistrableTest {

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
                .isThrownBy(() -> SuppliedRegistrable.of(null, () -> mock(Object.class)));
    }

    @Test
    @DisplayName("throw when providing a null instance supplier")
    void should_throw_when_providing_a_null_instance_supplier() {
        final Supplier<Object> instanceSupplier = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> SuppliedRegistrable.of(mock(Id.class), instanceSupplier));
    }

    @Test
    @DisplayName("have 0 components as it is already instanced")
    void should_have_zero_dependencies() {
        final SuppliedRegistrable supplier = SuppliedRegistrable.of(id, instanceSupplier);

        final int dependenciesCount = supplier.countDependencies(mock(Registry.class));

        assertThat(dependenciesCount).isEqualTo(NO_DEPENDENCIES);
    }

    @Test
    @DisplayName("create a component containing the supply instance")
    void should_inject_the_supplied_instance_into_a_component() {
        final Object instance = mock(Object.class);
        final Component component = Component.of(id, instance);
        final SuppliedRegistrable supplier = SuppliedRegistrable.of(id, instanceSupplier);

        when(instanceSupplier.get()).thenReturn(instance);
        final Component instancedComponent = supplier.instantiate(mock(Instantiator.class), mock(Components.class));

        verify(instanceSupplier).get();
        assertThat(instancedComponent).isEqualTo(component);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final SuppliedRegistrable firstSupplier = SuppliedRegistrable.of(id, instanceSupplier);
        final SuppliedRegistrable secondSupplier = SuppliedRegistrable.of(id, instanceSupplier);

        assertThat(firstSupplier).isEqualTo(secondSupplier);
        assertThat(firstSupplier.hashCode()).isEqualTo(secondSupplier.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final SuppliedRegistrable firstSupplier = SuppliedRegistrable.of(mock(Id.class), () -> mock(Object.class));
        final SuppliedRegistrable secondSupplier = SuppliedRegistrable.of(mock(Id.class), () -> mock(Object.class));

        assertThat(firstSupplier).isNotEqualTo(secondSupplier);
        assertThat(firstSupplier.hashCode()).isNotEqualTo(secondSupplier.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final SuppliedRegistrable supplier = SuppliedRegistrable.of(id, instanceSupplier);

        when(id.toString()).thenReturn("Id");
        when(instanceSupplier.toString()).thenReturn("Instance");
        final String result = supplier.toString();

        assertThat(result).isEqualTo("SuppliedRegistrable{id=Id, instanceSupplier=Instance}");
    }
}