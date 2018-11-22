package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Dependencies should")
class DependenciesTest {

    @Test
    @DisplayName("add a new dependency")
    void should_create_a_new_dependencies_instance_containing_the_added_dependency() {
        final Dependency dependency = mock(Dependency.class);
        final Dependencies dependencies = Dependencies.empty();

        final Dependencies result = dependencies.add(dependency);

        assertThat(dependencies).isEqualTo(Dependencies.empty());
        assertThat(result).isEqualTo(Dependencies.of(singletonList(dependency)));
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Dependency dependency = mock(Dependency.class);
        final Dependencies firstDependencies = Dependencies.of(singletonList(dependency));
        final Dependencies secondDependencies = Dependencies.of(singletonList(dependency));

        assertThat(firstDependencies).isEqualTo(secondDependencies);
        assertThat(firstDependencies.hashCode()).isEqualTo(secondDependencies.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Dependencies firstDependencies = Dependencies.of(singletonList(mock(Dependency.class)));
        final Dependencies secondDependencies = Dependencies.of(singletonList(mock(Dependency.class)));

        assertThat(firstDependencies).isNotEqualTo(secondDependencies);
        assertThat(firstDependencies.hashCode()).isNotEqualTo(secondDependencies.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Dependencies dependencies = Dependencies.empty();

        final String result = dependencies.toString();

        assertThat(result).isEqualTo("Dependencies{dependencies=[]}");
    }
}