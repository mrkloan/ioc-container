package io.fries.ioc.registry;

import io.fries.ioc.components.Component;
import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Registry should")
class RegistryTest {

    @Test
    @DisplayName("add a new registered component")
    void should_create_a_new_registry_instance_containing_the_added_registered_component() {
        final Id id = mock(Id.class);
        final Registrable registrable = mock(Registrable.class);
        final Registry registry = Registry.empty();

        when(registrable.getId()).thenReturn(id);
        final Registry result = registry.add(registrable);

        assertThat(registry).isEqualTo(Registry.empty());
        assertThat(result).isEqualTo(Registry.of(singletonMap(id, registrable)));
    }

    @Test
    @DisplayName("throw when adding a new registered component with an identifier that already exists")
    void should_throw_when_a_registered_component_with_the_same_id_already_exists() {
        final Id id = mock(Id.class);
        final Registrable registrable = mock(Registrable.class);
        final Registry registry = Registry.of(singletonMap(id, mock(ManagedRegistrable.class)));

        when(registrable.getId()).thenReturn(id);
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> registry.add(registrable))
                .withMessage("Another component was already registered with the id: " + id);
    }

    @Test
    @DisplayName("get a registered component using its identifier")
    void should_get_a_registered_component_by_its_id() {
        final Id id = mock(Id.class);
        final Registrable registrable = mock(Registrable.class);
        final Registry registry = Registry.of(singletonMap(id, registrable));

        final Registrable result = registry.get(id);

        assertThat(result).isEqualTo(registrable);
    }

    @Test
    @DisplayName("throw an exception when the required registered component is not present")
    void should_throw_when_the_required_registered_component_is_not_present() {
        final Registrable registrable = mock(Registrable.class);
        final Registry registry = Registry.of(singletonMap(mock(Id.class), registrable));

        final Id id = mock(Id.class);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> registry.get(id))
                .withMessage("This identifier is not linked to any component inside the container: " + id);
    }

    @Test
    @DisplayName("instantiate a component from its registration")
    void should_instantiate_a_component_from_its_registration() {
        final Instantiator instantiator = mock(Instantiator.class);

        final Registrable firstRegistered = mock(Registrable.class);
        final Registrable secondRegistered = mock(Registrable.class);
        final Map<Id, Registrable> registryMap = new HashMap<>();
        registryMap.put(mock(Id.class), firstRegistered);
        registryMap.put(mock(Id.class), secondRegistered);

        final Registry registry = Registry.of(registryMap);
        when(firstRegistered.countDependencies(registry)).thenReturn(1);
        when(secondRegistered.countDependencies(registry)).thenReturn(0);

        final Id firstComponentId = mock(Id.class);
        final Component firstComponent = mock(Component.class);
        when(firstComponent.getId()).thenReturn(firstComponentId);

        final Id secondComponentId = mock(Id.class);
        final Component secondComponent = mock(Component.class);
        when(secondComponent.getId()).thenReturn(secondComponentId);

        final Map<Id, Component> componentMap = new HashMap<>();
        componentMap.put(firstComponentId, firstComponent);
        componentMap.put(secondComponentId, secondComponent);

        final Components components = Components.of(componentMap);
        when(firstRegistered.instantiate(any(), any())).thenReturn(firstComponent);
        when(secondRegistered.instantiate(any(), any())).thenReturn(secondComponent);

        final Components result = registry.instantiate(instantiator);

        assertThat(result).isEqualTo(components);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Registrable registrable = mock(Registrable.class);
        final Registry firstRegistry = Registry.of(singletonMap(id, registrable));
        final Registry secondRegistry = Registry.of(singletonMap(id, registrable));

        assertThat(firstRegistry).isEqualTo(secondRegistry);
        assertThat(firstRegistry.hashCode()).isEqualTo(secondRegistry.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Registry firstRegistry = Registry.of(singletonMap(mock(Id.class), mock(Registrable.class)));
        final Registry secondRegistry = Registry.of(singletonMap(mock(Id.class), mock(Registrable.class)));

        assertThat(firstRegistry).isNotEqualTo(secondRegistry);
        assertThat(firstRegistry.hashCode()).isNotEqualTo(secondRegistry.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Registry registry = Registry.empty();

        final String result = registry.toString();

        assertThat(result).isEqualTo("Registry{registeredDependencies={}}");
    }
}