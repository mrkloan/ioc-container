package io.fries.ioc.scanner;

import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.ComponentInstantiationException;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.supplied.SuppliedRegistrable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class SuppliedRegistrableScanner implements RegistrableScanner {

    private final TypeScanner typeScanner;
    private final Instantiator instantiator;

    SuppliedRegistrableScanner(final TypeScanner typeScanner, final Instantiator instantiator) {
        this.typeScanner = typeScanner;
        this.instantiator = instantiator;
    }

    @Override
    public List<Registrable> findAll() {
        return typeScanner
                .findAnnotatedBy(Configuration.class)
                .stream()
                .flatMap(type -> fromConfigurationInstance(type).stream())
                .collect(toList());
    }

    private List<Registrable> fromConfigurationInstance(final Class<?> type) {
        final Object configuration = instantiator.createInstance(type, emptyList());

        return stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Register.class))
                .map(method -> createRegistrable(configuration, method, method.getAnnotation(Register.class)))
                .collect(toList());
    }

    private Registrable createRegistrable(final Object configuration, final Method method, final Register register) {
        final Id id = extractSupplierId(method, register);
        final Supplier<?> instanceSupplier = createSupplier(configuration, method);

        return SuppliedRegistrable.of(id, instanceSupplier);
    }

    Supplier<?> createSupplier(final Object configuration, final Method method) {
        method.setAccessible(true);

        return () -> {
            try {
                return method.invoke(configuration);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ComponentInstantiationException(e);
            }
        };
    }

    Id extractSupplierId(final Method method, final Register register) {
        if (register.value().isEmpty())
            return Id.of(method.getName());

        return Id.of(register.value());
    }
}
