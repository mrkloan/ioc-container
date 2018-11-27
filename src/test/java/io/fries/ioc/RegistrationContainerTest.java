package io.fries.ioc;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.DependencySupplier;
import io.fries.ioc.registry.DependencyToken;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Registration container should")
class RegistrationContainerTest {

    @Mock
    private Instantiator instantiator;

    @Mock
    private Registry registry;

    private RegistrationContainer registrationContainer;

    @BeforeEach
    void setUp() {
        this.registrationContainer = RegistrationContainer.of(instantiator, registry);
    }

    @Test
    @DisplayName("register a dependency supplier")
    void should_register_a_dependency_supplier() {
        final Id id = mock(Id.class);
        final Supplier<Object> instanceSupplier = () -> mock(Object.class);
        final DependencySupplier supplier = DependencySupplier.of(id, Object.class, instanceSupplier);

        registrationContainer.register(id, Object.class, instanceSupplier);

        verify(registry).add(id, supplier);
    }

    @Test
    @DisplayName("register a dependency token")
    void should_register_a_dependency_token() {
        final Id id = mock(Id.class);
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();
        final DependencyToken token = DependencyToken.of(id, type, dependencies);

        registrationContainer.register(id, type, dependencies);

        verify(registry).add(id, token);
    }

    @Test
    @DisplayName("create a container containing the token instances")
    void should_create_a_container_containing_the_instances_of_the_registered_tokens() {
        final Dependencies dependencies = mock(Dependencies.class);

        when(registry.instantiate(instantiator)).thenReturn(dependencies);
        final Container container = registrationContainer.instantiate();

        verify(registry).instantiate(instantiator);
        assertThat(container).isEqualTo(Container.of(dependencies));
    }
}