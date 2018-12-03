package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.managed.ManagedRegistrable;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.type.TypeScanner;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ManagedRegistrableScanner implements RegistrableScanner {

    private final TypeScanner typeScanner;
    private final DependenciesScanner dependenciesScanner;

    ManagedRegistrableScanner(final TypeScanner typeScanner, final DependenciesScanner dependenciesScanner) {
        this.typeScanner = typeScanner;
        this.dependenciesScanner = dependenciesScanner;
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
        final List<Id> dependencies = dependenciesScanner.findByConstructor(type);

        return ManagedRegistrable.of(id, type, dependencies);
    }

    private Id extractComponentId(final Class<?> type, final Register register) {
        if (register.value().isEmpty())
            return Id.of(type.getSimpleName());

        return Id.of(register.value());
    }
}
