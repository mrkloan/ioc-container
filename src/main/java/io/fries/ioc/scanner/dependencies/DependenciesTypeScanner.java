package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.components.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class DependenciesTypeScanner implements DependenciesScanner {

    @Override
    public List<Id> findByConstructor(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] constructorParameters = constructor.getParameters();

        return stream(constructorParameters)
                .map(Parameter::getType)
                .map(Id::of)
                .collect(Collectors.toList());
    }
}
