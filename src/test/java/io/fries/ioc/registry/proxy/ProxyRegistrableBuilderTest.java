package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.Book;
import testable.NovelBook;
import testable.stories.Story;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.fries.ioc.registry.proxy.ProxyRegistrableBuilder.proxy;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Proxy registrable builder")
class ProxyRegistrableBuilderTest {

    @Test
    @DisplayName("infer the identifier and dependencies of the managed type")
    void should_infer_the_id_and_dependencies_of_the_managed_type() {
        final Class<?> interfaceType = Book.class;
        final Class<?> type = NovelBook.class;
        final Id id = Id.of(type);
        final List<Id> dependencies = singletonList(Id.of(Story.class));
        final ProxyRegistrableBuilder builder = new ProxyRegistrableBuilder(id, interfaceType, type, dependencies);

        final ProxyRegistrableBuilder result = proxy(type);

        assertThat(result).isEqualTo(builder);
    }

    @Test
    @DisplayName("throw if the type does not implement any interface")
    void should_throw_if_the_type_does_not_implement_any_interface() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> proxy(Object.class))
                .withMessage("The provided type does not implement any interface");
    }

    @Test
    @DisplayName("update the interface type")
    void should_update_the_interface_type() {
        final Id id = mock(Id.class);
        final Class<?> newInterfaceType = Consumer.class;
        final ProxyRegistrableBuilder builder = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, emptyList());
        final ProxyRegistrableBuilder expected = new ProxyRegistrableBuilder(id, newInterfaceType, Object.class, emptyList());

        final ProxyRegistrableBuilder result = builder.of(newInterfaceType);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("update the dependencies identifiers")
    void should_update_the_dependencies_id() {
        final Id id = mock(Id.class);
        final Object dependency = mock(Object.class);
        final ProxyRegistrableBuilder builder = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, singletonList(Id.of(Object.class)));
        final ProxyRegistrableBuilder expected = new ProxyRegistrableBuilder(id, Supplier.class, Object.class, singletonList(Id.of(dependency)));

        final ProxyRegistrableBuilder result = builder.with(dependency);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("update its identifier")
    void should_update_the_registrable_id() {
        final Object newId = Object.class;
        final ProxyRegistrableBuilder builder = new ProxyRegistrableBuilder(mock(Id.class), Supplier.class, Object.class, emptyList());
        final ProxyRegistrableBuilder expected = new ProxyRegistrableBuilder(Id.of(newId), Supplier.class, Object.class, emptyList());

        final ProxyRegistrableBuilder result = builder.as(newId);

        assertThat(result).isEqualTo(expected);
    }

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