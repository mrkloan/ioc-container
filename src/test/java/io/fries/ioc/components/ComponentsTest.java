package io.fries.ioc.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Components should")
class ComponentsTest {

    @Test
    @DisplayName("add a new component")
    void should_create_a_new_dependencies_instance_containing_the_added_component() {
        final Id id = mock(Id.class);
        final Component component = mock(Component.class);
        final Components components = Components.empty();

        when(component.getId()).thenReturn(id);
        final Components result = components.add(component);

        assertThat(result).isEqualTo(components);
        assertThat(result).isEqualTo(Components.of(singletonMap(id, component)));
    }

    @Test
    @DisplayName("get a component using its identifier")
    void should_get_a_component_by_its_id() {
        final Id id = mock(Id.class);
        final Component firstComponent = mock(Component.class);
        final Components components = Components.of(singletonMap(id, firstComponent));

        final Component result = components.get(id);

        assertThat(result).isEqualTo(firstComponent);
    }

    @Test
    @DisplayName("throw when the required component is not present")
    void should_throw_when_the_required_component_is_not_present() {
        final Component firstComponent = mock(Component.class);
        final Components components = Components.of(singletonMap(mock(Id.class), firstComponent));

        final Id id = mock(Id.class);
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> components.get(id))
                .withMessage("No component registered with id: " + id);
    }

    @Test
    @DisplayName("merge two components content")
    void should_merge_two_dependencies() {
        final Id firstId = mock(Id.class);
        final Component firstComponent = mock(Component.class);
        final Components firstComponents = Components.of(singletonMap(firstId, firstComponent));

        final Id secondId = mock(Id.class);
        final Component secondComponent = mock(Component.class);
        final Components secondComponents = Components.of(singletonMap(secondId, secondComponent));

        final Components merged = firstComponents.merge(secondComponents);

        final Map<Id, Component> mergedMap = new HashMap<>();
        mergedMap.put(firstId, firstComponent);
        mergedMap.put(secondId, secondComponent);

        assertThat(merged).isEqualTo(Components.of(mergedMap));
    }

    @Test
    @DisplayName("throw when merging two components holding the same identifier")
    void should_throw_when_merging_two_dependencies_holding_the_same_id() {
        final Id id = mock(Id.class);

        final Components firstComponents = Components.of(singletonMap(id, mock(Component.class)));
        final Components secondComponents = Components.of(singletonMap(id, mock(Component.class)));

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> firstComponents.merge(secondComponents));
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final Component component = mock(Component.class);
        final Components firstComponents = Components.of(singletonMap(id, component));
        final Components secondComponents = Components.of(singletonMap(id, component));

        assertThat(firstComponents).isEqualTo(secondComponents);
        assertThat(firstComponents.hashCode()).isEqualTo(secondComponents.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Components firstComponents = Components.of(singletonMap(mock(Id.class), mock(Component.class)));
        final Components secondComponents = Components.of(singletonMap(mock(Id.class), mock(Component.class)));

        assertThat(firstComponents).isNotEqualTo(secondComponents);
        assertThat(firstComponents.hashCode()).isNotEqualTo(secondComponents.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Components components = Components.empty();

        final String result = components.toString();

        assertThat(result).isEqualTo("Components{components={}}");
    }
}