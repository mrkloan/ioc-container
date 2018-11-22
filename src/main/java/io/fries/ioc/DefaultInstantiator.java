package io.fries.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static java.util.stream.Collectors.toList;

class DefaultInstantiator implements Instantiator {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(final Class<T> type, final List<Dependency> dependencies) {
        try {
            if (dependencies.isEmpty())
                return type.newInstance();

            final List<Object> parameterInstances = dependencies
                    .stream()
                    .map(Dependency::getInstance)
                    .collect(toList());

            final Class<?>[] parameterTypes = parameterInstances
                    .stream()
                    .map(Object::getClass)
                    .toArray(Class[]::new);

            final Constructor<T> constructor = type.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);

            return constructor.newInstance(parameterInstances.toArray());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new DependencyInstantiationException(e);
        }
    }

    private static class DependencyInstantiationException extends RuntimeException {
        private DependencyInstantiationException(final Exception cause) {
            super(cause);
        }
    }
}
