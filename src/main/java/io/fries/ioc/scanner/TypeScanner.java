package io.fries.ioc.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface TypeScanner {
    Set<Class<?>> findAnnotatedBy(final Class<? extends Annotation> annotationType);
}
