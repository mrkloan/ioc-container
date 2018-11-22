package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependency token should")
class DependencyTokenTest {

    @Test
    @DisplayName("count its number of deep dependencies")
    void should_count_its_number_of_deep_dependencies() {
        final Id firstId = mock(Id.class);
        final DependencyToken firstDependency = mock(DependencyToken.class);
        final Id secondId = mock(Id.class);
        final DependencyToken secondDependency = mock(DependencyToken.class);
        final Tokens tokens = mock(Tokens.class);

        final DependencyToken token = DependencyToken.of(mock(Id.class), Object.class, asList(firstId, secondId));

        when(tokens.get(firstId)).thenReturn(firstDependency);
        when(tokens.get(secondId)).thenReturn(secondDependency);
        when(firstDependency.countDeepDependencies(tokens)).thenReturn(1);
        when(secondDependency.countDeepDependencies(tokens)).thenReturn(0);
        final int deepDependenciesCount = token.countDeepDependencies(tokens);

        assertThat(deepDependenciesCount).isEqualTo(3);
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