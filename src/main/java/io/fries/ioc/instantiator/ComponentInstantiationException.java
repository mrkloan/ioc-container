package io.fries.ioc.instantiator;

@SuppressWarnings("WeakerAccess")
public class ComponentInstantiationException extends RuntimeException {

    public ComponentInstantiationException(final Throwable cause) {
        super(cause);
    }
}
