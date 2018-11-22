package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
    void should_be_formatted_as_a_string() {
        final Tokens tokens = Tokens.empty();

        final String result = tokens.toString();

        assertThat(result).isEqualTo("Tokens{tokens=[]}");
    }
}