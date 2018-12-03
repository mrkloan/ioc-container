package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.components.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import testable.NovelBook;
import testable.stories.Story;

import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Identified dependencies scanner should")
class IdentifiedDependenciesScannerTest {

    @Spy
    private IdentifiedDependenciesScanner identifiedDependenciesScanner;

    @Test
    @DisplayName("find the identified dependencies using the first declared constructor")
    void should_find_the_identified_dependencies_of_a_type() {
        final Class<?> type = NovelBook.class;

        final List<Id> result = identifiedDependenciesScanner.findByConstructor(type);

        assertThat(result).isEqualTo(singletonList(Id.of("FantasyStory")));
    }

    @Test
    @DisplayName("extract the type parameter when the parameter is not identified")
    void should_extract_the_type_parameter_when_the_parameter_is_not_identified() throws NoSuchMethodException {
        final Parameter parameter = Object.class.getDeclaredMethod("equals", Object.class).getParameters()[0];

        final Id result = identifiedDependenciesScanner.extractParameterId(parameter);

        assertThat(result).isEqualTo(Id.of("Object"));
    }

    @Test
    @DisplayName("extract the annotation parameter when the parameter is identified")
    void should_extract_the_annotation_parameter_when_the_parameter_is_identified() throws NoSuchMethodException {
        final Parameter parameter = NovelBook.class.getDeclaredConstructor(Story.class).getParameters()[0];

        final Id result = identifiedDependenciesScanner.extractParameterId(parameter);

        assertThat(result).isEqualTo(Id.of("FantasyStory"));
    }

    @Test
    @DisplayName("create an identifier using the parameter annotation")
    void should_create_an_identifier_using_the_parameter_annotation() throws NoSuchMethodException {
        final Parameter parameter = NovelBook.class.getDeclaredConstructor(Story.class).getParameters()[0];

        final Id result = identifiedDependenciesScanner.parameterAnnotationToId(parameter);

        assertThat(result).isEqualTo(Id.of("FantasyStory"));
    }

    @Test
    @DisplayName("create an identifier using the parameter name")
    void should_create_an_identifier_using_the_parameter_name() throws NoSuchMethodException {
        final Parameter parameter = NovelBook.class.getDeclaredConstructor(Story.class).getParameters()[0];

        final Id result = identifiedDependenciesScanner.parameterTypeToId(parameter);

        assertThat(result).isEqualTo(Id.of("Story"));
    }
}