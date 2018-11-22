package io.fries.ioc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Registration container should")
class RegistrationContainerTest {

    @Mock
    private Instantiator instantiator;

    @Mock
    private Tokens tokens;

    private RegistrationContainer registrationContainer;

    @BeforeEach
    void setUp() {
        this.registrationContainer = RegistrationContainer.of(instantiator, tokens);
    }

    @Test
    @DisplayName("register a dependency token without dependencies")
    void should_register_a_dependency_token_without_dependencies() {
        final Id id = mock(Id.class);
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();
        final DependencyToken token = DependencyToken.of(id, type, dependencies);

        registrationContainer.register(id, type, dependencies);

        verify(tokens).add(token);
    }

    @Test
    @DisplayName("create a container containing the token instances")
    void should_create_a_container_containing_the_instances_of_the_registered_tokens() {
        final Dependencies dependencies = mock(Dependencies.class);

        when(tokens.instantiate()).thenReturn(dependencies);
        final Container container = registrationContainer.instantiate();

        verify(tokens).instantiate();
        assertThat(container).isEqualTo(Container.of(dependencies));
    }
}