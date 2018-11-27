package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static io.fries.ioc.DependencyProxy.NO_DEPENDENCIES;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dependency proxy should")
class DependencyProxyTest {

    @Test
    @DisplayName("always count zero dependencies as they are not needed to create a proxy")
    void should_count_zero_dependencies() {
        final DependencyProxy proxy = DependencyProxy.of(mock(Id.class), Supplier.class, Object.class, singletonList(mock(Id.class)));

        final int dependenciesCount = proxy.countDependencies(mock(Registry.class));

        assertThat(dependenciesCount).isEqualTo(NO_DEPENDENCIES);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final DependencyProxy firstProxy = DependencyProxy.of(id, Supplier.class, Object.class, emptyList());
        final DependencyProxy secondProxy = DependencyProxy.of(id, Supplier.class, Object.class, emptyList());

        assertThat(firstProxy).isEqualTo(secondProxy);
        assertThat(firstProxy.hashCode()).isEqualTo(secondProxy.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final DependencyProxy firstProxy = DependencyProxy.of(mock(Id.class), Supplier.class, Object.class, emptyList());
        final DependencyProxy secondProxy = DependencyProxy.of(mock(Id.class), Supplier.class, Object.class, emptyList());

        assertThat(firstProxy).isNotEqualTo(secondProxy);
        assertThat(firstProxy.hashCode()).isNotEqualTo(secondProxy.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final DependencyProxy proxy = DependencyProxy.of(id, Supplier.class, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = proxy.toString();

        assertThat(result).isEqualTo("DependencyProxy{id=Id, interfaceType=interface java.util.function.Supplier, type=class java.lang.Object, dependencies=[]}");
    }
}