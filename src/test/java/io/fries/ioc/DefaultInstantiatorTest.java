package io.fries.ioc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.fries.ioc.Tests.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Default instantiator should")
class DefaultInstantiatorTest {

    @Test
    void should_create_an_object_instance_using_the_default_constructor() {
        final Instantiator instantiator = new DefaultInstantiator();

        final E instance = assertDoesNotThrow(() -> instantiator.createInstance(E.class, emptyList()));

        Assertions.assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_an_empty_constructor() {
        final Instantiator instantiator = new DefaultInstantiator();

        final C instance = assertDoesNotThrow(() -> instantiator.createInstance(C.class, emptyList()));

        Assertions.assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_a_constructor_with_a_parameter() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getInstance()).thenReturn(new E());
        final D instance = assertDoesNotThrow(() -> instantiator.createInstance(D.class, singletonList(dependency)));

        Assertions.assertThat(instance).isNotNull();
    }
}