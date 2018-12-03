package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Proxy;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.type.TypeScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testable.stories.protagonists.FriendlyProtagonist;
import testable.stories.protagonists.Protagonist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Proxy registrable should")
class ProxyRegistrableScannerTest {

    private static final List<Id> NO_DEPENDENCIES = emptyList();

    @Mock
    private TypeScanner typeScanner;
    @Mock
    private DependenciesScanner dependenciesScanner;

    private ProxyRegistrableScanner proxyRegistrableScanner;

    @BeforeEach
    void setUp() {
        when(dependenciesScanner.findByConstructor(any())).thenReturn(NO_DEPENDENCIES);

        this.proxyRegistrableScanner = new ProxyRegistrableScanner(typeScanner, dependenciesScanner);
    }

    @Test
    @DisplayName("find all proxy registrables")
    void should_find_all_proxy_registrables() {
        final Set<Class<?>> scannedTypes = new HashSet<>();
        scannedTypes.add(FriendlyProtagonist.class);

        when(typeScanner.findAnnotatedBy(Proxy.class)).thenReturn(scannedTypes);
        final List<Registrable> result = proxyRegistrableScanner.findAll();

        final List<Registrable> expected = singletonList(
                ProxyRegistrable.of(Id.of("knights.perceval"), Protagonist.class, FriendlyProtagonist.class, NO_DEPENDENCIES)
        );
        assertThat(result).isEqualTo(expected);
    }
}