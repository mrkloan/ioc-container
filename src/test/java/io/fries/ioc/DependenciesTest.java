package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("Dependencies should")
class DependenciesTest {

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
}