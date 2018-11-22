package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependency token should")
class DependencyTokenTest {

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