package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependency supplier should")
class DependencySupplierTest {

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Supplier<Object> instance = () -> mock(Object.class);

        final DependencySupplier firstSupplier = DependencySupplier.of(id, instance);
        final DependencySupplier secondSupplier = DependencySupplier.of(id, instance);

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
    @SuppressWarnings("unchecked")
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final Supplier<Object> instance = (Supplier<Object>) mock(Supplier.class);
        final DependencySupplier supplier = DependencySupplier.of(id, instance);

        when(id.toString()).thenReturn("Id");
        when(instance.toString()).thenReturn("Instance");
        final String result = supplier.toString();

        assertThat(result).isEqualTo("DependencySupplier{id=Id, instanceSupplier=Instance}");
    }
}