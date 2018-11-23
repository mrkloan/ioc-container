package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Container should")
class ContainerTest {

    @Test
    @DisplayName("provide a dependency instance using its identifier")
    void should_provide_a_dependency_instance_by_its_id() {
        final Id id = mock(Id.class);
        final Object instance = mock(Object.class);
        final Dependency dependency = mock(Dependency.class);
        final Dependencies dependencies = mock(Dependencies.class);

        final Container container = Container.of(dependencies);

        when(dependency.getInstance()).thenReturn(instance);
        when(dependencies.get(id)).thenReturn(dependency);
        final Object providedInstance = container.provide(id);

        verify(dependencies).get(id);
        assertThat(providedInstance).isEqualTo(instance);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Dependencies dependencies = mock(Dependencies.class);
        final Container firstContainer = Container.of(dependencies);
        final Container secondContainer = Container.of(dependencies);

        assertThat(firstContainer).isEqualTo(secondContainer);
        assertThat(firstContainer.hashCode()).isEqualTo(secondContainer.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Container firstContainer = Container.of(mock(Dependencies.class));
        final Container secondContainer = Container.of(mock(Dependencies.class));

        assertThat(firstContainer).isNotEqualTo(secondContainer);
        assertThat(firstContainer.hashCode()).isNotEqualTo(secondContainer.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Dependencies dependencies = mock(Dependencies.class);
        final Container container = Container.of(dependencies);

        when(dependencies.toString()).thenReturn("Dependencies");
        final String result = container.toString();

        assertThat(result).isEqualTo("Container{dependencies=Dependencies}");
    }
}