package io.fries.ioc.scanner.registrable;

import io.fries.ioc.registry.Registrable;

import java.util.List;

public interface RegistrableScanner {
    List<Registrable> findAll();
}
