package io.fries.ioc.scanner.type;

import io.fries.reflection.Reflection;
import io.fries.reflection.filters.PackageFilter;
import io.fries.reflection.scanners.ClassPathScanner;
import io.fries.reflection.scanners.Scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.lang.Thread.currentThread;

public class ReflectionTypeScanner implements TypeScanner {

    private final Reflection reflection;

    public ReflectionTypeScanner(final Class<?> entryPoint) {
        this.reflection = createReflectionEngine(entryPoint);
    }

    Reflection createReflectionEngine(final Class<?> entryPoint) {
        final String rootPackage = entryPoint.getPackage().getName();
        final ClassLoader classLoader = currentThread().getContextClassLoader();
        final Scanner scanner = ClassPathScanner
                .of(classLoader)
                .filter(PackageFilter.withSubpackages(rootPackage));

        return Reflection.of(scanner);
    }

    @Override
    public Set<Class<?>> findAnnotatedBy(final Class<? extends Annotation> annotationType) {
        return reflection.getAnnotatedTypes(annotationType);
    }
}
