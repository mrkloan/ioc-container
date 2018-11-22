package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Tokens should")
class TokensTest {

    @Test
    @DisplayName("add a new dependency token")
    void should_create_a_new_tokens_instance_containing_the_added_token() {
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens tokens = Tokens.empty();

        final Tokens result = tokens.add(token);

        assertThat(tokens).isEqualTo(Tokens.empty());
        assertThat(result).isEqualTo(Tokens.of(singletonList(token)));
    }

    @Test
    @DisplayName("instantiate a dependency from its token")
    void should_instantiate_a_dependency_from_its_token() {
        final Instantiator instantiator = mock(Instantiator.class);

        final DependencyToken firstToken = mock(DependencyToken.class);
        final DependencyToken secondToken = mock(DependencyToken.class);
        final Tokens tokens = Tokens.of(asList(firstToken, secondToken));

        final Dependency firstDependency = mock(Dependency.class);
        final Dependency secondDependency = mock(Dependency.class);
        final Dependencies dependencies = Dependencies.of(asList(firstDependency, secondDependency));

        when(firstToken.instantiate(instantiator)).thenReturn(firstDependency);
        when(secondToken.instantiate(instantiator)).thenReturn(secondDependency);
        final Dependencies result = tokens.instantiate(instantiator);

        verify(firstToken).instantiate(instantiator);
        verify(secondToken).instantiate(instantiator);
        assertThat(result).isEqualTo(dependencies);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens firstTokens = Tokens.of(singletonList(token));
        final Tokens secondTokens = Tokens.of(singletonList(token));

        assertThat(firstTokens).isEqualTo(secondTokens);
        assertThat(firstTokens.hashCode()).isEqualTo(secondTokens.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Tokens firstTokens = Tokens.of(singletonList(mock(DependencyToken.class)));
        final Tokens secondTokens = Tokens.of(singletonList(mock(DependencyToken.class)));

        assertThat(firstTokens).isNotEqualTo(secondTokens);
        assertThat(firstTokens.hashCode()).isNotEqualTo(secondTokens.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Tokens tokens = Tokens.empty();

        final String result = tokens.toString();

        assertThat(result).isEqualTo("Tokens{tokens=[]}");
    }
}