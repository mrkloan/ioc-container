package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Tokens should")
class TokensTest {

    @Test
    @DisplayName("add a new dependency token")
    void should_create_a_new_tokens_instance_containing_the_added_token() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens tokens = Tokens.empty();

        final Tokens result = tokens.add(id, token);

        assertThat(tokens).isEqualTo(Tokens.empty());
        assertThat(result).isEqualTo(Tokens.of(singletonMap(id, token)));
    }

    @Test
    @DisplayName("throw when adding a new token with an identifier that already exists")
    void should_throw_when_a_token_with_the_same_id_already_exists() {
        final Id id = mock(Id.class);
        final Tokens tokens = Tokens.of(singletonMap(id, mock(DependencyToken.class)));

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> tokens.add(id, mock(DependencyToken.class)))
                .withMessage("Another dependency token was already registered with the id: " + id);
    }

    @Test
    @DisplayName("get a token using its identifier")
    void should_get_a_token_by_its_id() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens tokens = Tokens.of(singletonMap(id, token));

        final DependencyToken result = tokens.get(id);

        assertThat(result).isEqualTo(token);
    }

    @Test
    @DisplayName("throw an exception when the required token is not present")
    void should_throw_when_the_required_token_is_not_present() {
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens tokens = Tokens.of(singletonMap(mock(Id.class), token));

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tokens.get(mock(Id.class)))
                .withMessage("The specified dependency token is not registered in the registration container");
    }

    @Test
    @DisplayName("instantiate a dependency from its token")
    void should_instantiate_a_dependency_from_its_token() {
        final Instantiator instantiator = mock(Instantiator.class);

        final DependencyToken firstToken = mock(DependencyToken.class);
        final DependencyToken secondToken = mock(DependencyToken.class);
        final Map<Id, DependencyToken> tokenMap = new HashMap<>();
        tokenMap.put(mock(Id.class), firstToken);
        tokenMap.put(mock(Id.class), secondToken);

        final Tokens tokens = Tokens.of(tokenMap);
        when(firstToken.countDependencies(tokens)).thenReturn(1);
        when(secondToken.countDependencies(tokens)).thenReturn(0);

        final Id firstDependencyId = mock(Id.class);
        final Dependency firstDependency = mock(Dependency.class);
        when(firstDependency.getId()).thenReturn(firstDependencyId);

        final Id secondDependencyId = mock(Id.class);
        final Dependency secondDependency = mock(Dependency.class);
        when(secondDependency.getId()).thenReturn(secondDependencyId);

        final Map<Id, Dependency> dependencyMap = new HashMap<>();
        dependencyMap.put(firstDependencyId, firstDependency);
        dependencyMap.put(secondDependencyId, secondDependency);

        final Dependencies dependencies = Dependencies.of(dependencyMap);
        when(firstToken.instantiate(any(), any())).thenReturn(firstDependency);
        when(secondToken.instantiate(any(), any())).thenReturn(secondDependency);

        final Dependencies result = tokens.instantiate(instantiator);

        assertThat(result).isEqualTo(dependencies);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final DependencyToken token = mock(DependencyToken.class);
        final Tokens firstTokens = Tokens.of(singletonMap(id, token));
        final Tokens secondTokens = Tokens.of(singletonMap(id, token));

        assertThat(firstTokens).isEqualTo(secondTokens);
        assertThat(firstTokens.hashCode()).isEqualTo(secondTokens.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Tokens firstTokens = Tokens.of(singletonMap(mock(Id.class), mock(DependencyToken.class)));
        final Tokens secondTokens = Tokens.of(singletonMap(mock(Id.class), mock(DependencyToken.class)));

        assertThat(firstTokens).isNotEqualTo(secondTokens);
        assertThat(firstTokens.hashCode()).isNotEqualTo(secondTokens.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Tokens tokens = Tokens.empty();

        final String result = tokens.toString();

        assertThat(result).isEqualTo("Tokens{tokens={}}");
    }
}