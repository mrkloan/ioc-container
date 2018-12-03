package io.fries.ioc;

import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.Book;
import testable.NovelBook;
import testable.TestableApplication;
import testable.stories.FantasyStory;
import testable.stories.plots.IncrediblePlot;
import testable.stories.plots.Plot;
import testable.stories.plots.PredictablePlot;
import testable.stories.protagonists.FriendlyProtagonist;
import testable.stories.protagonists.HeroicProtagonist;
import testable.stories.protagonists.Protagonist;

import static io.fries.ioc.registry.managed.ManagedRegistrableBuilder.managed;
import static io.fries.ioc.registry.proxy.ProxyRegistrableBuilder.proxy;
import static io.fries.ioc.registry.supplied.SuppliedRegistrableBuilder.supplied;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IoC container should")
class ContainerAcceptanceTest {

    @Test
    @DisplayName("provide the root instance of a dependency graph")
    void should_provide_the_instances_of_registered_classes() {
        final Instantiator instantiator = new DefaultInstantiator();
        final RegistrationContainer registrationContainer = Container.using(instantiator);
        final Container container = registrationContainer
                .register(managed(FantasyStory.class).with(IncrediblePlot.class, HeroicProtagonist.class))
                .register(managed(NovelBook.class).with(FantasyStory.class))
                .register(managed(IncrediblePlot.class))
                .register(managed(HeroicProtagonist.class))
                .instantiate();

        final Book providedInstance = container.provide(NovelBook.class);

        assertThat(providedInstance.toString()).isEqualTo("NovelBook(FantasyStory(IncrediblePlot, HeroicProtagonist))");
    }

    @Test
    @DisplayName("provide pre-instanced components")
    void should_provide_pre_instanced_dependencies() {
        final Container container = Container.empty()
                .register(managed(PredictablePlot.class).with("plot.outcome"))
                .register(supplied(() -> "Outcome").as("plot.outcome"))
                .instantiate();

        final Plot providedInstance = container.provide(PredictablePlot.class);

        assertThat(providedInstance.toString()).isEqualTo("PredictablePlot('Outcome')");
    }

    @Test
    @DisplayName("provide circular components")
    void should_provide_circular_dependencies() {
        final Container container = Container.empty()
                .register(managed(FriendlyProtagonist.class).with("knights.karadoc").as("knights.perceval"))
                .register(proxy(FriendlyProtagonist.class).of(Protagonist.class).with("knights.perceval").as("knights.karadoc"))
                .instantiate();

        final Protagonist karadoc = container.provide("knights.karadoc");

        assertThat(karadoc.toString()).isEqualTo("FriendlyProtagonist(FriendlyProtagonist)");
    }

    @Test
    @DisplayName("provide register components using a fluent builder API")
    void should_register_components_using_a_fluent_builder_api() {
        final Container container = Container.empty()
                .register(managed(FantasyStory.class).with(PredictablePlot.class, "knights.perceval").as(FantasyStory.class))
                .register(managed(NovelBook.class).with(FantasyStory.class).as(NovelBook.class))
                .register(managed(PredictablePlot.class).with("plot.outcome").as(PredictablePlot.class))
                .register(supplied(() -> "Outcome").as("plot.outcome"))
                .register(proxy(FriendlyProtagonist.class).of(Protagonist.class).with("knights.karadoc").as("knights.perceval"))
                .register(managed(FriendlyProtagonist.class).with("knights.perceval").as("knights.karadoc"))
                .instantiate();

        final Book book = container.provide(NovelBook.class);

        assertThat(book.toString()).isEqualTo("NovelBook(FantasyStory(PredictablePlot('Outcome'), FriendlyProtagonist(FriendlyProtagonist)))");
    }

    @Test
    void should_scan_annotated_components() {
        final Container container = Container.scan(TestableApplication.class);

        final Book book = container.provide("novelBook");

        assertThat(book.toString()).isEqualTo("NovelBook(FantasyStory(PredictablePlot('Outcome'), FriendlyProtagonist(HeroicProtagonist)))");
    }
}
