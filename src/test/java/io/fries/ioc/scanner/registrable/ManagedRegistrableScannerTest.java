package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.type.TypeScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testable.NovelBook;
import testable.stories.ScienceFictionStory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Managed registrable scanner should")
class ManagedRegistrableScannerTest {

    private static final String INFERRED_IDENTIFIER = "";
    private static final List<Id> NO_DEPENDENCIES = emptyList();

    @Mock
    private TypeScanner typeScanner;
    @Mock
    private DependenciesScanner dependenciesScanner;

    private ManagedRegistrableScanner managedRegistrableScanner;

    @BeforeEach
    void setUp() {
        when(dependenciesScanner.findByConstructor(any())).thenReturn(NO_DEPENDENCIES);

        this.managedRegistrableScanner = new ManagedRegistrableScanner(typeScanner, dependenciesScanner);
    }

    @Test
    @DisplayName("find all managed registrables")
    void should_find_all_managed_registrables() {
        final Set<Class<?>> scannedTypes = new HashSet<>();
        scannedTypes.add(NovelBook.class);

        when(typeScanner.findAnnotatedBy(Register.class)).thenReturn(scannedTypes);
        final List<Registrable> result = managedRegistrableScanner.findAll();

        final List<Registrable> expected = singletonList(
                ManagedRegistrable.of(Id.of("NovelBook"), NovelBook.class, NO_DEPENDENCIES)
        );
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("create a ManagedRegistrable with an identified dependency")
    void should_create_a_managed_registrable_with_an_identified_dependency() {
        final Class<?> type = NovelBook.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn(INFERRED_IDENTIFIER);
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("NovelBook"), NovelBook.class, NO_DEPENDENCIES);
        assertThat(registrable).isEqualTo(expected);
    }

    @Test
    @DisplayName("create a ManagedRegistrable with a non identified dependency")
    void should_create_a_managed_registrable_with_a_non_identified_dependency() {
        final Class<?> type = ScienceFictionStory.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn(INFERRED_IDENTIFIER);
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("ScienceFictionStory"), ScienceFictionStory.class, NO_DEPENDENCIES);
        assertThat(registrable).isEqualTo(expected);
    }

    @Test
    @DisplayName("create a ManagedRegistrable with a custom identifier")
    void should_create_a_managed_registrable_with_a_custom_identifier() {
        final Class<?> type = ScienceFictionStory.class;
        final Register register = mock(Register.class);

        when(register.value()).thenReturn("story.sci-fi");
        final Registrable registrable = managedRegistrableScanner.createRegistrable(type, register);

        final ManagedRegistrable expected = ManagedRegistrable.of(Id.of("story.sci-fi"), ScienceFictionStory.class, NO_DEPENDENCIES);
        assertThat(registrable).isEqualTo(expected);
    }
}