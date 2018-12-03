package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dependencies type scanner should")
class DependenciesTypeScannerTest {

    @Spy
    private DependenciesTypeScanner dependenciesTypeScanner;

    @Test
    @DisplayName("find types dependencies from the first declared constructor")
    void should_find_typed_dependencies_from_the_first_declared_constructor() {
        final Class<?> type = RegistrationContainer.class;
        final List<Id> dependencies = asList(Id.of(Instantiator.class), Id.of(Registry.class));

        final List<Id> inferredDependencies = dependenciesTypeScanner.findByConstructor(type);

        assertThat(inferredDependencies).isEqualTo(dependencies);
    }
}