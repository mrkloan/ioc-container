package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.RegistrableBuilder;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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