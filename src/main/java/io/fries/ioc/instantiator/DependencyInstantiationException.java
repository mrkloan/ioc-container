package io.fries.ioc.instantiator;

@SuppressWarnings("WeakerAccess")
public class DependencyInstantiationException extends RuntimeException {

    DependencyInstantiationException(final Throwable cause) {
        super(cause);
    }
}
