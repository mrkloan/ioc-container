package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Proxy registrable builder")
class ProxyRegistrableBuilderTest {

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final ProxyRegistrableBuilder firstBuilder = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, emptyList());
        final ProxyRegistrableBuilder secondBuilder = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, emptyList());

        assertThat(firstBuilder).isEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isEqualTo(secondBuilder.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final ProxyRegistrableBuilder firstBuilder = new ProxyRegistrableBuilder(mock(Id.class), Supplier.class, Object.class, emptyList());
        final ProxyRegistrableBuilder secondBuilder = new ProxyRegistrableBuilder(mock(Id.class), Supplier.class, Object.class, emptyList());

        assertThat(firstBuilder).isNotEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isNotEqualTo(secondBuilder.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final ProxyRegistrableBuilder builder = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = builder.toString();

        assertThat(result).isEqualTo("ProxyRegistrableBuilder{id=Id, interfaceType=interface java.util.function.Supplier, type=class java.lang.Object, dependencies=[]}");
    }
}