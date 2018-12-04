package io.fries.ioc.scanner;

import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.scanner.registrable.RegistrableScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

@DisplayName("Components scanner should")
class ComponentsScannerTest {

    @Test
    @DisplayName("instantiate the scanned components")
    void should_create_a_container_holding_the_scanned_components() {
        final RegistrationContainer registrationContainer = mock(RegistrationContainer.class);
        final RegistrableScanner registrableScanner = mock(RegistrableScanner.class);
        final Registrable registrable = mock(Registrable.class);

        when(registrableScanner.findAll()).thenReturn(Collections.singletonList(registrable));
        ComponentsScanner.of(registrationContainer)
                .use(registrableScanner)
                .scan();

        verify(registrableScanner).findAll();
        verify(registrationContainer).register(registrable);
        verify(registrationContainer).instantiate();
    }
}
