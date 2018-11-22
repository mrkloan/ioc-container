package io.fries.ioc;

import java.util.List;

class DefaultInstantiator implements Instantiator {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(final Class<T> type, final List<Dependency> dependencies) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DependencyInstantiationException(e);
        }
    }

    private static class DependencyInstantiationException extends RuntimeException {
        private DependencyInstantiationException(final Exception cause) {
            super(cause);
        }
    }
}
