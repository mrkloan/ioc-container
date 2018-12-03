package io.fries.ioc.scanner;

import io.fries.ioc.registry.Registrable;

import java.util.List;

public interface RegistrableScanner {
    List<Registrable> findAll();
}
