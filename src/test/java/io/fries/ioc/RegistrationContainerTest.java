package io.fries.ioc;

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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
    @DisplayName("throw when registering a null supplier identifier")
    void should_throw_when_registering_a_null_supplier_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(null, Object.class, () -> mock(Object.class)));
    }

    @Test
    @DisplayName("throw when registering a null supplier type")
    void should_throw_when_registering_a_null_supplier_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), null, () -> mock(Object.class)));
    }

    @Test
    @DisplayName("throw when registering a null supplier instance")
    void should_throw_when_registering_a_null_supplier_instance() {
        final Supplier<Object> instanceSupplier = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), Object.class, instanceSupplier));
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
    @DisplayName("throw when registering a null token identifier")
    void should_throw_when_registering_a_null_token_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(null, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when registering a null token type")
    void should_throw_when_registering_a_null_token_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), null, emptyList()));
    }

    @Test
    @DisplayName("throw when registering a null token dependencies")
    void should_throw_when_registering_a_null_token_dependencies() {
        final List<Id> dependencies = null;

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), Object.class, dependencies));
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
    @DisplayName("throw when registering a null proxy identifier")
    void should_throw_when_registering_a_null_proxy_id() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(null, Supplier.class, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when registering a null proxy interface type")
    void should_throw_when_registering_a_null_proxy_interface_type() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), null, Object.class, emptyList()));
    }

    @Test
    @DisplayName("throw when registering a null proxy interface type")
    void should_throw_when_registering_a_proxy_with_a_class_type_instead_of_an_interface_type() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registrationContainer.register(mock(Id.class), Object.class, Object.class, emptyList()))
                .withMessage("Proxied type must be an interface");
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