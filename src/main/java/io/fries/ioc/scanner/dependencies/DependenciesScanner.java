package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.components.Id;

import java.util.List;

public interface DependenciesScanner {
    List<Id> findByConstructor(final Class<?> type);
}
