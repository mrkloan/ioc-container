package io.fries.ioc.registry;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Dependency;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
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

@DisplayName("Dependency token should")
class DependencyTokenTest {

    @Test
    @DisplayName("throw when providing a null identifier")
    void should_throw_when_providing_a_null_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DependencyToken.of(null, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when providing a null type")
    void should_throw_when_providing_a_null_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DependencyToken.of(mock(Id.class), null, emptyList()));
    }

    @Test
    @DisplayName("throw when providing null dependencies")
    void should_throw_when_providing_null_dependencies() {
        final List<Id> dependencies = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> DependencyToken.of(mock(Id.class), Object.class, dependencies));
    }

    @Test
    @DisplayName("count its number of dependencies")
    void should_count_its_number_of_dependencies() {
        final Id firstId = mock(Id.class);
        final DependencyToken firstDependency = mock(DependencyToken.class);
        final Id secondId = mock(Id.class);
        final DependencyToken secondDependency = mock(DependencyToken.class);
        final Registry registry = mock(Registry.class);

        final DependencyToken token = DependencyToken.of(mock(Id.class), Object.class, asList(firstId, secondId));

        when(registry.get(firstId)).thenReturn(firstDependency);
        when(registry.get(secondId)).thenReturn(secondDependency);
        when(firstDependency.countDependencies(registry)).thenReturn(1);
        when(secondDependency.countDependencies(registry)).thenReturn(0);
        final int deepDependenciesCount = token.countDependencies(registry);

        assertThat(deepDependenciesCount).isEqualTo(3);
    }

    @Test
    @DisplayName("create a dependency instance")
    void should_create_a_dependency_instance() {
        final Instantiator instantiator = mock(Instantiator.class);
        final Dependencies dependencies = mock(Dependencies.class);

        final Id dependencyId = mock(Id.class);
        final Dependency tokenDependency = mock(Dependency.class);

        final Id id = mock(Id.class);
        final Class<Object> type = Object.class;
        final DependencyToken token = DependencyToken.of(id, type, singletonList(dependencyId));

        final Object instance = mock(type);
        final Dependency expectedDependency = Dependency.of(id, Object.class, instance);

        when(dependencies.findAllById(singletonList(dependencyId))).thenReturn(singletonList(tokenDependency));
        when(instantiator.createInstance(type, singletonList(tokenDependency))).thenReturn(instance);
        final Dependency dependency = token.instantiate(instantiator, dependencies);

        assertThat(dependency).isEqualTo(expectedDependency);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final DependencyToken firstToken = DependencyToken.of(id, Object.class, emptyList());
        final DependencyToken secondToken = DependencyToken.of(id, Object.class, emptyList());

        assertThat(firstToken).isEqualTo(secondToken);
        assertThat(firstToken.hashCode()).isEqualTo(secondToken.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final DependencyToken firstToken = DependencyToken.of(mock(Id.class), Object.class, emptyList());
        final DependencyToken secondToken = DependencyToken.of(mock(Id.class), Object.class, emptyList());

        assertThat(firstToken).isNotEqualTo(secondToken);
        assertThat(firstToken.hashCode()).isNotEqualTo(secondToken.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final DependencyToken token = DependencyToken.of(id, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = token.toString();

        assertThat(result).isEqualTo("DependencyToken{id=Id, type=class java.lang.Object, dependencies=[]}");
    }
}