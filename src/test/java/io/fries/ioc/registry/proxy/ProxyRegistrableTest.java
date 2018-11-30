package io.fries.ioc.registry.proxy;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Supplier;

import static io.fries.ioc.registry.proxy.ProxyRegistrable.NO_DEPENDENCIES;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Component proxy should")
class ProxyRegistrableTest {

    @Test
    @DisplayName("throw when providing a null identifier")
    void should_throw_when_providing_a_null_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyRegistrable.of(null, Supplier.class, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when providing a null interface type")
    void should_throw_when_providing_a_null_interface_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyRegistrable.of(mock(Id.class), null, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when providing a class type instead of an interface type")
    void should_throw_when_providing_a_with_a_class_type_instead_of_an_interface_type() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ProxyRegistrable.of(mock(Id.class), Object.class, Object.class, emptyList()))
                .withMessage("Proxied type must be an interface");
    }

    @Test
    @DisplayName("throw when providing a null type")
    void should_throw_when_providing_a_null_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyRegistrable.of(mock(Id.class), Supplier.class, null, emptyList()));
    }

    @Test
    @DisplayName("throw when providing null components")
    void should_throw_when_providing_null_dependencies() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyRegistrable.of(mock(Id.class), Supplier.class, Object.class, null));
    }
    
    @Test
    @DisplayName("always count zero components as they are not needed to create a proxy")
    void should_count_zero_dependencies() {
        final ProxyRegistrable proxy = ProxyRegistrable.of(mock(Id.class), Supplier.class, Object.class, singletonList(mock(Id.class)));

        final int dependenciesCount = proxy.countDependencies(mock(Registry.class));

        assertThat(dependenciesCount).isEqualTo(NO_DEPENDENCIES);
    }

    @Test
    @DisplayName("create a proxy of the provided interface type without actually instantiating the object")
    void should_create_a_proxy_of_the_provided_interface_type() {
        final Id id = mock(Id.class);
        final Class<Supplier> interfaceType = Supplier.class;
        final Class<Object> type = Object.class;
        final List<Id> proxyDependencies = emptyList();
        final ProxyRegistrable proxy = ProxyRegistrable.of(id, interfaceType, type, proxyDependencies);

        final Instantiator instantiator = mock(Instantiator.class);
        final Components components = mock(Components.class);

        final Component component = proxy.instantiate(instantiator, components);
        final Supplier<Object> instance = component.getInstance();

        verify(components, never()).findAllById(proxyDependencies);
        verify(instantiator, never()).createInstance(type, emptyList());

        assertThat(component.getId()).isEqualTo(id);
        assertThat(instance).isInstanceOf(Proxy.class);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final ProxyRegistrable firstProxy = ProxyRegistrable.of(id, Supplier.class, Object.class, emptyList());
        final ProxyRegistrable secondProxy = ProxyRegistrable.of(id, Supplier.class, Object.class, emptyList());

        assertThat(firstProxy).isEqualTo(secondProxy);
        assertThat(firstProxy.hashCode()).isEqualTo(secondProxy.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final ProxyRegistrable firstProxy = ProxyRegistrable.of(mock(Id.class), Supplier.class, Object.class, emptyList());
        final ProxyRegistrable secondProxy = ProxyRegistrable.of(mock(Id.class), Supplier.class, Object.class, emptyList());

        assertThat(firstProxy).isNotEqualTo(secondProxy);
        assertThat(firstProxy.hashCode()).isNotEqualTo(secondProxy.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final ProxyRegistrable proxy = ProxyRegistrable.of(id, Supplier.class, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = proxy.toString();

        assertThat(result).isEqualTo("ProxyRegistrable{id=Id, interfaceType=interface java.util.function.Supplier, type=class java.lang.Object, components=[]}");
    }
}