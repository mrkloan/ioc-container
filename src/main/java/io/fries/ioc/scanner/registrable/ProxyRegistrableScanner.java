package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Proxy;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
import io.fries.ioc.scanner.type.TypeScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ProxyRegistrableScanner implements RegistrableScanner {

    private final TypeScanner typeScanner;

    ProxyRegistrableScanner(final TypeScanner typeScanner) {
        this.typeScanner = typeScanner;
    }

    private static Class<?> findFirstImplementedInterface(final Class<?> type) {
        if (type.getInterfaces().length > 0)
            return type.getInterfaces()[0];

        if (type.getSuperclass() != null)
            return findFirstImplementedInterface(type.getSuperclass());

        throw new IllegalArgumentException("The provided type does not implement any interface");
    }

    @Override
    public List<Registrable> findAll() {
        return typeScanner
                .findAnnotatedBy(Proxy.class)
                .stream()
                .map(type -> createRegistrable(type, type.getAnnotation(Proxy.class)))
                .collect(toList());
    }

    private Registrable createRegistrable(final Class<?> type, final Proxy proxy) {
        final Id id = extractComponentId(type, proxy);
        final Class<?> interfaceType = extractInterface(type, proxy);
        final List<Id> dependencies = extractDependencies(type);

        return ProxyRegistrable.of(id, interfaceType, type, dependencies);
    }

    private Class<?> extractInterface(final Class<?> type, final Proxy proxy) {
        if (proxy.type().equals(Proxy.class))
            return findFirstImplementedInterface(type);

        return proxy.type();
    }

    private Id extractComponentId(final Class<?> type, final Proxy proxy) {
        if (proxy.value().isEmpty())
            return Id.of(type.getSimpleName());

        return Id.of(proxy.value());
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
