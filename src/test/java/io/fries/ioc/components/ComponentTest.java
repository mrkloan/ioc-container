package io.fries.ioc.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Component should")
class ComponentTest {

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Object instance = mock(Object.class);
        final Component firstComponent = Component.of(id, instance);
        final Component secondComponent = Component.of(id, instance);

        assertThat(firstComponent).isEqualTo(secondComponent);
        assertThat(firstComponent.hashCode()).isEqualTo(secondComponent.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Component firstComponent = Component.of(mock(Id.class), mock(Object.class));
        final Component secondComponent = Component.of(mock(Id.class), mock(Object.class));

        assertThat(firstComponent).isNotEqualTo(secondComponent);
        assertThat(firstComponent.hashCode()).isNotEqualTo(secondComponent.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final Object instance = mock(Object.class);
        final Component component = Component.of(id, instance);

        when(id.toString()).thenReturn("Id");
        when(instance.toString()).thenReturn("Instance");
        final String result = component.toString();

        assertThat(result).isEqualTo("Component{id=Id, instance=Instance}");
    }
}