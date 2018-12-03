package io.fries.ioc.scanner.type;

import io.fries.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ReflectionTypeScanner implements TypeScanner {

    private final Reflection reflection;

    ReflectionTypeScanner(final Class<?> entryPoint) {
        this.reflection = createReflectionEngine(entryPoint);
    }

    Reflection createReflectionEngine(final Class<?> entryPoint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Class<?>> findAnnotatedBy(final Class<? extends Annotation> annotationType) {
        return reflection.getAnnotatedTypes(annotationType);
    }
}
