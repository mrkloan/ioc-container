package io.fries.ioc.instantiator;

@SuppressWarnings("WeakerAccess")
public class ComponentInstantiationException extends RuntimeException {

    ComponentInstantiationException(final Throwable cause) {
        super(cause);
    }
}
