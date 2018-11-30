package io.fries.ioc.registry.managed;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Managed registrable should")
class ManagedRegistrableTest {

    @Test
    @DisplayName("throw when providing a null identifier")
    void should_throw_when_providing_a_null_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ManagedRegistrable.of(null, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when providing a null type")
    void should_throw_when_providing_a_null_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ManagedRegistrable.of(mock(Id.class), null, emptyList()));
    }

    @Test
    @DisplayName("throw when providing null components")
    void should_throw_when_providing_null_dependencies() {
        final List<Id> dependencies = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ManagedRegistrable.of(mock(Id.class), Object.class, dependencies));
    }

    @Test
    @DisplayName("count its number of components")
    void should_count_its_number_of_dependencies() {
        final Id firstId = mock(Id.class);
        final ManagedRegistrable firstRegistrable = mock(ManagedRegistrable.class);
        final Id secondId = mock(Id.class);
        final ManagedRegistrable secondRegistrable = mock(ManagedRegistrable.class);
        final Registry registry = mock(Registry.class);

        final ManagedRegistrable registrable = ManagedRegistrable.of(mock(Id.class), Object.class, asList(firstId, secondId));

        when(registry.get(firstId)).thenReturn(firstRegistrable);
        when(registry.get(secondId)).thenReturn(secondRegistrable);
        when(firstRegistrable.countDependencies(registry)).thenReturn(1);
        when(secondRegistrable.countDependencies(registry)).thenReturn(0);
        final int deepDependenciesCount = registrable.countDependencies(registry);

        assertThat(deepDependenciesCount).isEqualTo(3);
    }

    @Test
    @DisplayName("create a component instance")
    void should_create_a_component_instance() {
        final Instantiator instantiator = mock(Instantiator.class);
        final Components components = mock(Components.class);

        final Id componentId = mock(Id.class);
        final Component component = mock(Component.class);

        final Id id = mock(Id.class);
        final Class<Object> type = Object.class;
        final ManagedRegistrable registrable = ManagedRegistrable.of(id, type, singletonList(componentId));

        final Object instance = mock(type);
        final Component expectedComponent = Component.of(id, instance);

        when(components.findAllById(singletonList(componentId))).thenReturn(singletonList(component));
        when(instantiator.createInstance(type, singletonList(component))).thenReturn(instance);
        final Component result = registrable.instantiate(instantiator, components);

        assertThat(result).isEqualTo(expectedComponent);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final ManagedRegistrable firstRegistrable = ManagedRegistrable.of(id, Object.class, emptyList());
        final ManagedRegistrable secondRegistrable = ManagedRegistrable.of(id, Object.class, emptyList());

        assertThat(firstRegistrable).isEqualTo(secondRegistrable);
        assertThat(firstRegistrable.hashCode()).isEqualTo(secondRegistrable.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final ManagedRegistrable firstRegistrable = ManagedRegistrable.of(mock(Id.class), Object.class, emptyList());
        final ManagedRegistrable secondRegistrable = ManagedRegistrable.of(mock(Id.class), Object.class, emptyList());

        assertThat(firstRegistrable).isNotEqualTo(secondRegistrable);
        assertThat(firstRegistrable.hashCode()).isNotEqualTo(secondRegistrable.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final ManagedRegistrable registrable = ManagedRegistrable.of(id, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = registrable.toString();

        assertThat(result).isEqualTo("ManagedRegistrable{id=Id, type=class java.lang.Object, components=[]}");
    }
}