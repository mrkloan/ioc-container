package io.fries.ioc;

import io.fries.ioc.dependencies.Id;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.Book;
import testable.NovelBook;
import testable.stories.FantasyStory;
import testable.stories.Story;
import testable.stories.plots.IncrediblePlot;
import testable.stories.plots.Plot;
import testable.stories.plots.PredictablePlot;
import testable.stories.protagonists.FriendlyProtagonist;
import testable.stories.protagonists.HeroicProtagonist;
import testable.stories.protagonists.Protagonist;

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
                .register(Id.of(FantasyStory.class), FantasyStory.class, asList(Id.of(IncrediblePlot.class), Id.of(HeroicProtagonist.class)))
                .register(Id.of(NovelBook.class), NovelBook.class, singletonList(Id.of(FantasyStory.class)))
                .register(Id.of(IncrediblePlot.class), IncrediblePlot.class, emptyList())
                .register(Id.of(HeroicProtagonist.class), HeroicProtagonist.class, emptyList())
                .instantiate();

        final Book providedInstance = container.provide(Id.of(NovelBook.class));

        assertThat(providedInstance.toString()).isEqualTo("NovelBook(FantasyStory(IncrediblePlot, HeroicProtagonist))");
    }

    @Test
    @DisplayName("provide pre-instanced dependencies")
    void should_provide_pre_instanced_dependencies() {
        final Container container = Container.empty()
                .register(Id.of(PredictablePlot.class), PredictablePlot.class, singletonList(Id.of("plot.outcome")))
                .register(Id.of("plot.outcome"), () -> "Outcome")
                .instantiate();

        final Plot providedInstance = container.provide(Id.of(PredictablePlot.class));

        assertThat(providedInstance.toString()).isEqualTo("PredictablePlot('Outcome')");
    }

    @Test
    @DisplayName("provide circular dependencies")
    void should_provide_circular_dependencies() {
        final Container container = Container.empty()
                .register(Id.of("knights.perceval"), FriendlyProtagonist.class, singletonList(Id.of("knights.karadoc")))
                .register(Id.of("knights.karadoc"), Protagonist.class, FriendlyProtagonist.class, singletonList(Id.of("knights.perceval")))
                .instantiate();

        final Protagonist karadoc = container.provide(Id.of("knights.karadoc"));

        assertThat(karadoc.toString()).isEqualTo("FriendlyProtagonist(FriendlyProtagonist)");
    }

    @Test
    @DisplayName("provide dependencies with inferred identifiers and dependencies")
    void should_provide_dependencies_with_inferred_id_and_dependencies() {
        final Container container = Container.empty()
                .register(Id.of(Story.class), FantasyStory.class)
                .register(NovelBook.class)
                .register(Id.of(Plot.class), IncrediblePlot.class)
                .register(Id.of(Protagonist.class), HeroicProtagonist.class)
                .instantiate();

        final Book providedInstance = container.provide(Id.of(NovelBook.class));

        assertThat(providedInstance.toString()).isEqualTo("NovelBook(FantasyStory(IncrediblePlot, HeroicProtagonist))");
    }
}
