package io.fries.ioc.scanner;

import io.fries.reflection.Reflection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Reflection type scanner should")
class ReflectionTypeScannerTest {

    @Mock
    private Reflection reflection;

    private ReflectionTypeScanner reflectionTypeScanner;

    @BeforeEach
    void setUp() {
        this.reflectionTypeScanner = new MockedReflectionTypeScanner(Object.class);
    }

    @Test
    void should_find_types_by_annotation() {
        final Set<Class<?>> annotatedTypes = emptySet();
        final Class<? extends Annotation> annotationType = Annotation.class;

        when(reflection.getAnnotatedTypes(annotationType)).thenReturn(annotatedTypes);
        final Set<Class<?>> result = reflectionTypeScanner.findAnnotatedBy(annotationType);

        verify(reflection).getAnnotatedTypes(annotationType);
        assertThat(result).isEqualTo(annotatedTypes);
    }

    private class MockedReflectionTypeScanner extends ReflectionTypeScanner {
        private MockedReflectionTypeScanner(final Class<?> entryPoint) {
            super(entryPoint);
        }

        @Override
        Reflection createReflectionEngine(final Class<?> entryPoint) {
            return reflection;
        }
    }
}
