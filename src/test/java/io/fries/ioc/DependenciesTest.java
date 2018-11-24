package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependencies should")
class DependenciesTest {

    @Test
    @DisplayName("add a new dependency")
    void should_create_a_new_dependencies_instance_containing_the_added_dependency() {
        final Id id = mock(Id.class);
        final Dependency dependency = mock(Dependency.class);
        final Dependencies dependencies = Dependencies.empty();

        when(dependency.getId()).thenReturn(id);
        final Dependencies result = dependencies.add(dependency);

        assertThat(dependencies).isEqualTo(Dependencies.empty());
        assertThat(result).isEqualTo(Dependencies.of(singletonMap(id, dependency)));
    }

    @Test
    @DisplayName("get a dependency using its identifier")
    void should_get_a_dependency_by_its_id() {
        final Id id = mock(Id.class);
        final Dependency firstDependency = mock(Dependency.class);
        final Dependencies dependencies = Dependencies.of(singletonMap(id, firstDependency));

        final Dependency result = dependencies.get(id);

        assertThat(result).isEqualTo(firstDependency);
    }

    @Test
    @DisplayName("throw when the required dependency is not present")
    void should_throw_when_the_required_dependency_is_not_present() {
        final Dependency firstDependency = mock(Dependency.class);
        final Dependencies dependencies = Dependencies.of(singletonMap(mock(Id.class), firstDependency));

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> dependencies.get(mock(Id.class)))
                .withMessage("The specified dependency is not registered in the container");
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Dependency dependency = mock(Dependency.class);
        final Dependencies firstDependencies = Dependencies.of(singletonMap(id, dependency));
        final Dependencies secondDependencies = Dependencies.of(singletonMap(id, dependency));

        assertThat(firstDependencies).isEqualTo(secondDependencies);
        assertThat(firstDependencies.hashCode()).isEqualTo(secondDependencies.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Dependencies firstDependencies = Dependencies.of(singletonMap(mock(Id.class), mock(Dependency.class)));
        final Dependencies secondDependencies = Dependencies.of(singletonMap(mock(Id.class), mock(Dependency.class)));

        assertThat(firstDependencies).isNotEqualTo(secondDependencies);
        assertThat(firstDependencies.hashCode()).isNotEqualTo(secondDependencies.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Dependencies dependencies = Dependencies.empty();

        final String result = dependencies.toString();

        assertThat(result).isEqualTo("Dependencies{dependencies={}}");
    }
}