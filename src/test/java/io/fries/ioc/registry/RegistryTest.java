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
    @DisplayName("add a new dependency token")
    void should_create_a_new_tokens_instance_containing_the_added_token() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Registry registry = Registry.empty();

        final Registry result = registry.add(id, token);

        assertThat(registry).isEqualTo(Registry.empty());
        assertThat(result).isEqualTo(Registry.of(singletonMap(id, token)));
    }

    @Test
    @DisplayName("throw when adding a new token with an identifier that already exists")
    void should_throw_when_a_token_with_the_same_id_already_exists() {
        final Id id = mock(Id.class);
        final Registry registry = Registry.of(singletonMap(id, mock(DependencyToken.class)));

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> registry.add(id, mock(DependencyToken.class)))
                .withMessage("Another dependency token was already registered with the id: " + id);
    }

    @Test
    @DisplayName("get a token using its identifier")
    void should_get_a_token_by_its_id() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Registry registry = Registry.of(singletonMap(id, token));

        final RegisteredDependency result = registry.get(id);

        assertThat(result).isEqualTo(token);
    }

    @Test
    @DisplayName("throw an exception when the required token is not present")
    void should_throw_when_the_required_token_is_not_present() {
        final DependencyToken token = mock(DependencyToken.class);
        final Registry registry = Registry.of(singletonMap(mock(Id.class), token));

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> registry.get(mock(Id.class)))
                .withMessage("The specified dependency token is not registered in the registration container");
    }

    @Test
    @DisplayName("instantiate a dependency from its token")
    void should_instantiate_a_dependency_from_its_token() {
        final Instantiator instantiator = mock(Instantiator.class);

        final DependencyToken firstToken = mock(DependencyToken.class);
        final DependencyToken secondToken = mock(DependencyToken.class);
        final Map<Id, RegisteredDependency> tokenMap = new HashMap<>();
        tokenMap.put(mock(Id.class), firstToken);
        tokenMap.put(mock(Id.class), secondToken);

        final Registry registry = Registry.of(tokenMap);
        when(firstToken.countDependencies(registry)).thenReturn(1);
        when(secondToken.countDependencies(registry)).thenReturn(0);

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
        when(firstToken.instantiate(any(), any())).thenReturn(firstDependency);
        when(secondToken.instantiate(any(), any())).thenReturn(secondDependency);

        final Dependencies result = registry.instantiate(instantiator);

        assertThat(result).isEqualTo(dependencies);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Registry firstRegistry = Registry.of(singletonMap(id, token));
        final Registry secondRegistry = Registry.of(singletonMap(id, token));

        assertThat(firstRegistry).isEqualTo(secondRegistry);
        assertThat(firstRegistry.hashCode()).isEqualTo(secondRegistry.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Registry firstRegistry = Registry.of(singletonMap(mock(Id.class), mock(DependencyToken.class)));
        final Registry secondRegistry = Registry.of(singletonMap(mock(Id.class), mock(DependencyToken.class)));

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