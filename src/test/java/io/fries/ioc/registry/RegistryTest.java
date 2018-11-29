package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
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
    @DisplayName("add a new registered dependency")
    void should_create_a_new_registry_instance_containing_the_added_registered_dependency() {
        final Id id = mock(Id.class);
        final RegisteredDependency registeredDependency = mock(RegisteredDependency.class);
        final Registry registry = Registry.empty();

        when(registeredDependency.getId()).thenReturn(id);
        final Registry result = registry.add(registeredDependency);

        assertThat(registry).isEqualTo(Registry.empty());
        assertThat(result).isEqualTo(Registry.of(singletonMap(id, registeredDependency)));
    }

    @Test
    @DisplayName("throw when adding a new registered dependency with an identifier that already exists")
    void should_throw_when_a_registered_dependency_with_the_same_id_already_exists() {
        final Id id = mock(Id.class);
        final RegisteredDependency registeredDependency = mock(RegisteredDependency.class);
        final Registry registry = Registry.of(singletonMap(id, mock(DependencyToken.class)));

        when(registeredDependency.getId()).thenReturn(id);
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> registry.add(registeredDependency))
                .withMessage("Another dependency was already registered with the id: " + id);
    }

    @Test
    @DisplayName("get a registered dependency using its identifier")
    void should_get_a_registered_dependency_by_its_id() {
        final Id id = mock(Id.class);
        final RegisteredDependency registeredDependency = mock(RegisteredDependency.class);
        final Registry registry = Registry.of(singletonMap(id, registeredDependency));

        final RegisteredDependency result = registry.get(id);

        assertThat(result).isEqualTo(registeredDependency);
    }

    @Test
    @DisplayName("throw an exception when the required registered dependency is not present")
    void should_throw_when_the_required_registered_dependency_is_not_present() {
        final RegisteredDependency registeredDependency = mock(RegisteredDependency.class);
        final Registry registry = Registry.of(singletonMap(mock(Id.class), registeredDependency));

        final Id id = mock(Id.class);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> registry.get(id))
                .withMessage("This identifier is not linked to any dependency inside the container: " + id);
    }

    @Test
    @DisplayName("instantiate a dependency from its registration")
    void should_instantiate_a_dependency_from_its_registration() {
        final Instantiator instantiator = mock(Instantiator.class);

        final RegisteredDependency firstRegistered = mock(RegisteredDependency.class);
        final RegisteredDependency secondRegistered = mock(RegisteredDependency.class);
        final Map<Id, RegisteredDependency> registryMap = new HashMap<>();
        registryMap.put(mock(Id.class), firstRegistered);
        registryMap.put(mock(Id.class), secondRegistered);

        final Registry registry = Registry.of(registryMap);
        when(firstRegistered.countDependencies(registry)).thenReturn(1);
        when(secondRegistered.countDependencies(registry)).thenReturn(0);

        final Id firstDependencyId = mock(Id.class);
        final Dependency firstDependency = mock(Dependency.class);
        when(firstDependency.getId()).thenReturn(firstDependencyId);

        final Id secondDependencyId = mock(Id.class);
        final Dependency secondDependency = mock(Dependency.class);
        when(secondDependency.getId()).thenReturn(secondDependencyId);

        final Map<Id, Dependency> dependencyMap = new HashMap<>();
        dependencyMap.put(firstDependencyId, firstDependency);
        dependencyMap.put(secondDependencyId, secondDependency);

        final Dependencies dependencies = Dependencies.of(dependencyMap);
        when(firstRegistered.instantiate(any(), any())).thenReturn(firstDependency);
        when(secondRegistered.instantiate(any(), any())).thenReturn(secondDependency);

        final Dependencies result = registry.instantiate(instantiator);

        assertThat(result).isEqualTo(dependencies);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final RegisteredDependency registeredDependency = mock(RegisteredDependency.class);
        final Registry firstRegistry = Registry.of(singletonMap(id, registeredDependency));
        final Registry secondRegistry = Registry.of(singletonMap(id, registeredDependency));

        assertThat(firstRegistry).isEqualTo(secondRegistry);
        assertThat(firstRegistry.hashCode()).isEqualTo(secondRegistry.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Registry firstRegistry = Registry.of(singletonMap(mock(Id.class), mock(RegisteredDependency.class)));
        final Registry secondRegistry = Registry.of(singletonMap(mock(Id.class), mock(RegisteredDependency.class)));

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