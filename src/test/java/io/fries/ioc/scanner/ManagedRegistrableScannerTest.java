package io.fries.ioc.scanner;

import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.NovelBook;
import testable.stories.ScienceFictionStory;

import static io.fries.ioc.scanner.ManagedRegistrableScanner.INFERRED_IDENTIFIER;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Managed registrable scanner should")
class ManagedRegistrableScannerTest {

    private ManagedRegistrableScanner managedRegistrableScanner;

    @BeforeEach
    void setUp() {
        this.managedRegistrableScanner = new ManagedRegistrableScanner();
    }

    @Test
    @DisplayName("create a ManagedRegistrable with an identified dependency")
    void should_create_a_managed_registrable_with_an_identified_dependency() {
        final Class<?> type = NovelBook.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn(INFERRED_IDENTIFIER);
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("NovelBook"), NovelBook.class, singletonList(Id.of("FantasyStory")));
        assertThat(registrable).isEqualTo(expected);
    }

    @Test
    @DisplayName("create a ManagedRegistrable with a non identified dependency")
    void should_create_a_managed_registrable_with_a_non_identified_dependency() {
        final Class<?> type = ScienceFictionStory.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn(INFERRED_IDENTIFIER);
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("ScienceFictionStory"), ScienceFictionStory.class, singletonList(Id.of("Plot")));
        assertThat(registrable).isEqualTo(expected);
    }

    @Test
    @DisplayName("create a ManagedRegistrable with a custom identifier")
    void should_create_a_managed_registrable_with_a_custom_identifier() {
        final Class<?> type = ScienceFictionStory.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn("story.sci-fi");
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("story.sci-fi"), ScienceFictionStory.class, singletonList(Id.of("Plot")));
        assertThat(registrable).isEqualTo(expected);
    }
}