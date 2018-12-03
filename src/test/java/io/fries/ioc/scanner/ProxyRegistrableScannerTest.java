package io.fries.ioc.scanner;

import io.fries.ioc.annotations.Proxy;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Proxy registrable should")
class ProxyRegistrableScannerTest {

    @Mock
    private TypeScanner typeScanner;

    private ProxyRegistrableScanner proxyRegistrableScanner;

    @BeforeEach
    void setUp() {
        this.proxyRegistrableScanner = new ProxyRegistrableScanner(typeScanner);
    }

    @Test
    @DisplayName("find all proxy registrables")
    void should_find_all_proxy_registrables() {
        final Set<Class<?>> scannedTypes = new HashSet<>();
        scannedTypes.add(FriendlyProtagonist.class);

        when(typeScanner.findAnnotatedBy(Proxy.class)).thenReturn(scannedTypes);
        final List<Registrable> result = proxyRegistrableScanner.findAll();

        final List<Registrable> expected = singletonList(
                ProxyRegistrable.of(Id.of("knights.perceval"), Protagonist.class, FriendlyProtagonist.class, singletonList(Id.of("knights.karadoc")))
        );
        assertThat(result).isEqualTo(expected);
    }
}