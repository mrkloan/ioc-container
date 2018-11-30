package io.fries.ioc.registry;

import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Registrable with dependencies should")
class RegistrableWithDependenciesBuilderTest {

    @Spy
    private RegistrableWithDependenciesBuilder registrableWithDependenciesBuilder;

    @Test
    @DisplayName("infer its dependencies type from its constructor parameters")
    void should_infer_dependencies_from_constructor_parameters() {
        final Class<?> type = RegistrationContainer.class;
        final List<Id> dependencies = asList(Id.of(Instantiator.class), Id.of(Registry.class));

        final List<Id> inferredDependencies = registrableWithDependenciesBuilder.inferDependenciesFrom(type);

        assertThat(inferredDependencies).isEqualTo(dependencies);
    }
}