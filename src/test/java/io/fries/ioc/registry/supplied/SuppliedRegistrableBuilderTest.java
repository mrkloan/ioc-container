package io.fries.ioc.registry.supplied;

import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static io.fries.ioc.registry.supplied.SuppliedRegistrableBuilder.supplied;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Supplied registrable builder should")
class SuppliedRegistrableBuilderTest {

    @Test
    @DisplayName("infer the identifier of the instance supplier")
    void should_infer_the_identifier_of_the_instance_supplier() {
        final Id id = Id.of(Supplier.class);
        final Supplier instanceSupplier = mock(Supplier.class);
        final SuppliedRegistrableBuilder expected = new SuppliedRegistrableBuilder(id, instanceSupplier);

        final SuppliedRegistrableBuilder builder = supplied(instanceSupplier);

        assertThat(builder).isEqualTo(expected);
    }

    @Test
    @DisplayName("update its identifier")
    void should_update_the_registrable_id() {
        final Object newId = mock(Object.class);
        final Supplier instanceSupplier = mock(Supplier.class);
        final SuppliedRegistrableBuilder expected = new SuppliedRegistrableBuilder(Id.of(newId), instanceSupplier);

        final SuppliedRegistrableBuilder builder = new SuppliedRegistrableBuilder(mock(Id.class), instanceSupplier);
        final SuppliedRegistrableBuilder result = builder.as(newId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("build the managed registrable")
    void should_build_the_managed_registrable() {
        final Id id = mock(Id.class);
        final Supplier instanceSupplier = mock(Supplier.class);
        final SuppliedRegistrableBuilder builder = new SuppliedRegistrableBuilder(id, instanceSupplier);
        final SuppliedRegistrable expected = SuppliedRegistrable.of(id, instanceSupplier);

        final Registrable result = builder.build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Supplier instanceSupplier = mock(Supplier.class);
        final SuppliedRegistrableBuilder firstBuilder = new SuppliedRegistrableBuilder(id, instanceSupplier);
        final SuppliedRegistrableBuilder secondBuilder = new SuppliedRegistrableBuilder(id, instanceSupplier);

        assertThat(firstBuilder).isEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isEqualTo(secondBuilder.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final SuppliedRegistrableBuilder firstBuilder = new SuppliedRegistrableBuilder(mock(Id.class), mock(Supplier.class));
        final SuppliedRegistrableBuilder secondBuilder = new SuppliedRegistrableBuilder(mock(Id.class), mock(Supplier.class));

        assertThat(firstBuilder).isNotEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isNotEqualTo(secondBuilder.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final Supplier instanceSupplier = mock(Supplier.class);
        final SuppliedRegistrableBuilder builder = new SuppliedRegistrableBuilder(id, instanceSupplier);

        when(id.toString()).thenReturn("Id");
        when(instanceSupplier.toString()).thenReturn("Supplier");
        final String result = builder.toString();

        assertThat(result).isEqualTo("SuppliedRegistrableBuilder{id=Id, instanceSupplier=Supplier}");
    }
}