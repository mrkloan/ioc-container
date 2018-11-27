package io.fries.ioc.instantiator;

import io.fries.ioc.dependencies.Dependency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.fries.ioc.Tests.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Default instantiator should")
class DefaultInstantiatorTest {

    @Test
    void should_create_an_object_instance_using_the_default_constructor() {
        final Instantiator instantiator = new DefaultInstantiator();

        final E instance = assertDoesNotThrow(() -> instantiator.createInstance(E.class, emptyList()));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_an_empty_constructor() {
        final Instantiator instantiator = new DefaultInstantiator();

        final C instance = assertDoesNotThrow(() -> instantiator.createInstance(C.class, emptyList()));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_a_constructor_with_a_parameter() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getType()).then(invocation -> E.class);
        when(dependency.getInstance()).thenReturn(new E());
        final D instance = assertDoesNotThrow(() -> instantiator.createInstance(D.class, singletonList(dependency)));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_a_private_constructor_with_a_parameter() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getType()).then(invocation -> E.class);
        when(dependency.getInstance()).thenReturn(new E());
        final F instance = assertDoesNotThrow(() -> instantiator.createInstance(F.class, singletonList(dependency)));

        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("throw when trying to create an object instance without the required dependencies")
    void should_throw_when_trying_to_instantiate_an_object_without_the_required_dependencies() {
        final Instantiator instantiator = new DefaultInstantiator();

        assertThatExceptionOfType(DependencyInstantiationException.class)
                .isThrownBy(() -> instantiator.createInstance(F.class, emptyList()))
                .withCauseInstanceOf(NoSuchMethodException.class);
    }

    @Test
    @DisplayName("throw when trying to create an object instance without the required dependencies")
    void should_throw_when_trying_to_instantiate_an_object_with_too_many_dependencies() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getInstance()).thenReturn(new E());

        assertThatExceptionOfType(DependencyInstantiationException.class)
                .isThrownBy(() -> instantiator.createInstance(E.class, singletonList(dependency)))
                .withCauseInstanceOf(NoSuchMethodException.class);
    }
}