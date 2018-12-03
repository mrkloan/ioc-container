package io.fries.ioc.scanner;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.managed.ManagedRegistrable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ManagedRegistrableScanner implements RegistrableScanner {

    @Override
    public List<Registrable> findAll() {
        return null;
    }

    Registrable createRegistrable(final Class<?> type) {
        final Id id = Id.of(type.getSimpleName());
        final List<Id> dependencies = extractDependencies(type);

        return ManagedRegistrable.of(id, type, dependencies);
    }

    private List<Id> extractDependencies(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] parameters = constructor.getParameters();
        return stream(parameters)
                .map(this::extractParameterId)
                .collect(toList());
    }

    private Id extractParameterId(final Parameter parameter) {
        if (parameter.isAnnotationPresent(Identified.class))
            return parameterAnnotationToId(parameter);

        return parameterTypeToId(parameter);
    }

    private Id parameterAnnotationToId(final Parameter parameter) {
        final Identified identified = parameter.getAnnotation(Identified.class);
        return Id.of(identified.value());
    }

    private Id parameterTypeToId(final Parameter parameter) {
        final String parameterType = parameter.getType().getSimpleName();
        return Id.of(parameterType);
    }
}
