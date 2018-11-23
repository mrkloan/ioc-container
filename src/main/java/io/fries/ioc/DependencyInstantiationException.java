package io.fries.ioc;

class DependencyInstantiationException extends RuntimeException {
    DependencyInstantiationException(final Exception cause) {
        super(cause);
    }
}
