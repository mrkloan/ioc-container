package io.fries.ioc.dependencies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependency should")
class DependencyTest {

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Object instance = mock(Object.class);
        final Dependency firstDependency = Dependency.of(id, Object.class, instance);
        final Dependency secondDependency = Dependency.of(id, Object.class, instance);

        assertThat(firstDependency).isEqualTo(secondDependency);
        assertThat(firstDependency.hashCode()).isEqualTo(secondDependency.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Dependency firstDependency = Dependency.of(mock(Id.class), Object.class, mock(Object.class));
        final Dependency secondDependency = Dependency.of(mock(Id.class), Object.class, mock(Object.class));

        assertThat(firstDependency).isNotEqualTo(secondDependency);
        assertThat(firstDependency.hashCode()).isNotEqualTo(secondDependency.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final Object instance = mock(Object.class);
        final Dependency dependency = Dependency.of(id, Object.class, instance);

        when(id.toString()).thenReturn("Id");
        when(instance.toString()).thenReturn("Instance");
        final String result = dependency.toString();

        assertThat(result).isEqualTo("Dependency{id=Id, instance=Instance}");
    }
}