package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.fries.ioc.Tests.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IoC container should")
class ContainerAcceptanceTest {

    @Test
    @DisplayName("provide the root instance of a dependency tree")
    void should_provide_the_instances_of_registered_classes() {
        final Instantiator instantiator = new DefaultInstantiator();
        final RegistrationContainer registrationContainer = Container.using(instantiator);
        final Container container = registrationContainer
                .register(Id.of(A.class), A.class, asList(Id.of(C.class), Id.of(D.class)))
                .register(Id.of(B.class), B.class, singletonList(Id.of(A.class)))
                .register(Id.of(C.class), C.class, emptyList())
                .register(Id.of(D.class), D.class, singletonList(Id.of(E.class)))
                .register(Id.of(E.class), E.class, emptyList())
                .instantiate();

        final B providedInstance = container.provide(Id.of(B.class));

        assertThat(providedInstance.toString()).isEqualTo("B(A(C,D(E)))");
    }

    @Test
    @DisplayName("provide pre-instanced dependencies")
    void should_provide_pre_instanced_dependencies() {
        final Container container = Container.empty()
                .register(Id.of(D.class), D.class, singletonList(Id.of(E.class)))
                .register(Id.of(E.class), E::new)
                .instantiate();

        final D providedInstance = container.provide(Id.of(D.class));

        assertThat(providedInstance.toString()).isEqualTo("D(E)");
    }
}
