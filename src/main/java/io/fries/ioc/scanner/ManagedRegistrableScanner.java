package io.fries.ioc.scanner;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.managed.ManagedRegistrable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ManagedRegistrableScanner implements RegistrableScanner {

    static final String INFERRED_IDENTIFIER = "";

    private final TypeScanner typeScanner;

    ManagedRegistrableScanner(final TypeScanner typeScanner) {
        this.typeScanner = typeScanner;
    }

    @Override
    public List<Registrable> findAll() {
        return typeScanner
                .findAnnotatedBy(Register.class)
                .stream()
                .map(type -> createRegistrable(type, type.getAnnotation(Register.class)))
                .collect(toList());
    }

    Registrable createRegistrable(final Class<?> type, final Register register) {
        final Id id = extractComponentId(type, register);
        final List<Id> dependencies = extractDependencies(type);

        return ManagedRegistrable.of(id, type, dependencies);
    }

    private Id extractComponentId(final Class<?> type, final Register register) {
        if (register.value().equals(INFERRED_IDENTIFIER))
            return Id.of(type.getSimpleName());

        return Id.of(register.value());
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
