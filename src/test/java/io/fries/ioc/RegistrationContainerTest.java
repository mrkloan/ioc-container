package io.fries.ioc;

import io.fries.ioc.dependencies.Dependencies;
import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.DependencyProxy;
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

import static java.util.Arrays.asList;
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
        final DependencySupplier supplier = DependencySupplier.of(id, instanceSupplier);

        registrationContainer.register(id, instanceSupplier);

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
    @DisplayName("register a dependency proxy")
    void should_register_a_dependency_proxy() {
        final Id id = mock(Id.class);
        final Class<?> interfaceType = Supplier.class;
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();
        final DependencyProxy proxy = DependencyProxy.of(id, interfaceType, type, dependencies);

        registrationContainer.register(id, interfaceType, type, dependencies);

        verify(registry).add(id, proxy);
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

    @Test
    @DisplayName("infer type dependencies from its constructor parameters")
    void should_infer_dependencies_from_constructor_parameters() {
        final Class<?> type = RegistrationContainer.class;
        final List<Id> dependencies = asList(Id.of(Instantiator.class), Id.of(Registry.class));

        final List<Id> inferredDependencies = registrationContainer.inferDependenciesFrom(type);

        assertThat(inferredDependencies).isEqualTo(dependencies);
    }
}