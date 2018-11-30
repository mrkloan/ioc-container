package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;
import io.fries.ioc.registry.Registry;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
import io.fries.ioc.registry.supplied.SuppliedRegistrable;
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
    @DisplayName("register a component supplier")
    void should_register_a_component_supplier() {
        final Id id = mock(Id.class);
        final Supplier<Object> instanceSupplier = () -> mock(Object.class);
        final SuppliedRegistrable supplier = SuppliedRegistrable.of(id, instanceSupplier);

        registrationContainer.register(id, instanceSupplier);

        verify(registry).add(supplier);
    }

    @Test
    @DisplayName("register a managed registrable")
    void should_register_a_managed_registrable() {
        final Id id = mock(Id.class);
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();
        final ManagedRegistrable registrable = ManagedRegistrable.of(id, type, dependencies);

        registrationContainer.register(id, type, dependencies);

        verify(registry).add(registrable);
    }

    @Test
    @DisplayName("register a component proxy")
    void should_register_a_component_proxy() {
        final Id id = mock(Id.class);
        final Class<?> interfaceType = Supplier.class;
        final Class<?> type = Object.class;
        final List<Id> dependencies = emptyList();
        final ProxyRegistrable proxy = ProxyRegistrable.of(id, interfaceType, type, dependencies);

        registrationContainer.register(id, interfaceType, type, dependencies);

        verify(registry).add(proxy);
    }

    @Test
    @DisplayName("infer type components from its constructor parameters")
    void should_infer_dependencies_from_constructor_parameters() {
        final Class<?> type = RegistrationContainer.class;
        final List<Id> dependencies = asList(Id.of(Instantiator.class), Id.of(Registry.class));

        final List<Id> inferredDependencies = registrationContainer.inferDependenciesFrom(type);

        assertThat(inferredDependencies).isEqualTo(dependencies);
    }

    @Test
    @DisplayName("register from a builder")
    void should_register_from_a_builder() {
        final RegistrableBuilder builder = mock(RegistrableBuilder.class);
        final Registrable registrable = mock(Registrable.class);

        when(builder.build()).thenReturn(registrable);
        registrationContainer.register(builder);

        verify(builder).build();
        verify(registry).add(registrable);
    }

    @Test
    @DisplayName("create a container containing the instanced component of each registrable")
    void should_create_a_container_containing_the_instanced_component_of_each_registrable() {
        final Components components = mock(Components.class);

        when(registry.instantiate(instantiator)).thenReturn(components);
        final Container container = registrationContainer.instantiate();

        verify(registry).instantiate(instantiator);
        assertThat(container).isEqualTo(Container.of(components));
    }
}