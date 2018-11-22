package io.fries.ioc;

import java.util.List;

class DefaultInstantiator implements Instantiator {

    @Override
    public <T> T createInstance(final Class<T> type, final List<Dependency> dependencies) {
        throw new UnsupportedOperationException();
    }
}
