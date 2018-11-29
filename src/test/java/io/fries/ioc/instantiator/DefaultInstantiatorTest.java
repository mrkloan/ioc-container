package io.fries.ioc.instantiator;

import io.fries.ioc.dependencies.Dependency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.Book;
import testable.NovelBook;
import testable.stories.Story;
import testable.stories.plots.IncrediblePlot;
import testable.stories.protagonists.FriendlyProtagonist;
import testable.stories.protagonists.HeroicProtagonist;
import testable.stories.protagonists.Protagonist;

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

        final HeroicProtagonist instance = assertDoesNotThrow(() -> instantiator.createInstance(HeroicProtagonist.class, emptyList()));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_an_empty_constructor() {
        final Instantiator instantiator = new DefaultInstantiator();

        final IncrediblePlot instance = assertDoesNotThrow(() -> instantiator.createInstance(IncrediblePlot.class, emptyList()));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_a_constructor_with_a_parameter() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getInstance()).thenReturn(new HeroicProtagonist());
        final Protagonist instance = assertDoesNotThrow(() -> instantiator.createInstance(FriendlyProtagonist.class, singletonList(dependency)));

        assertThat(instance).isNotNull();
    }

    @Test
    void should_create_an_object_instance_using_a_private_constructor_with_a_parameter() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getInstance()).thenReturn(mock(Story.class));
        final Book instance = assertDoesNotThrow(() -> instantiator.createInstance(NovelBook.class, singletonList(dependency)));

        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("throw when trying to create an object instance without the required dependencies")
    void should_throw_when_trying_to_instantiate_an_object_without_the_required_dependencies() {
        final Instantiator instantiator = new DefaultInstantiator();

        assertThatExceptionOfType(DependencyInstantiationException.class)
                .isThrownBy(() -> instantiator.createInstance(NovelBook.class, emptyList()))
                .withCauseInstanceOf(IllegalArgumentException.class)
                .withMessageContaining("wrong number of arguments");
    }

    @Test
    @DisplayName("throw when trying to create an object instance without the required dependencies")
    void should_throw_when_trying_to_instantiate_an_object_with_too_many_dependencies() {
        final Instantiator instantiator = new DefaultInstantiator();
        final Dependency dependency = mock(Dependency.class);

        when(dependency.getInstance()).thenReturn(mock(Protagonist.class));

        assertThatExceptionOfType(DependencyInstantiationException.class)
                .isThrownBy(() -> instantiator.createInstance(HeroicProtagonist.class, singletonList(dependency)))
                .withCauseInstanceOf(IllegalArgumentException.class)
                .withMessageContaining("wrong number of arguments");
    }
}