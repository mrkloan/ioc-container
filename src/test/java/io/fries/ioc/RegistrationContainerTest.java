package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Registration container should")
class RegistrationContainerTest {

    @Mock
    private Instantiator instantiator;

    @Mock
    private Tokens tokens;

    @Test
    @DisplayName("register a dependency token without dependencies")
    void should_register_a_dependency_token_without_dependencies() {
        final Id id = mock(Id.class);
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();

        final DependencyToken token = DependencyToken.of(id, type, dependencies);
        final RegistrationContainer registrationContainer = RegistrationContainer.of(instantiator, tokens);

        registrationContainer.register(id, type, dependencies);

        verify(tokens).add(token);
    }
}