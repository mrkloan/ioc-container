package io.fries.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static java.util.stream.Collectors.toList;

class DefaultInstantiator implements Instantiator {

    @Override
    public <T> T createInstance(final Class<T> type, final List<Dependency> dependencies) {
        try {
            final List<Object> parameterInstances = mapParameterInstances(dependencies);
            final Class<?>[] parameterTypes = mapParameterTypes(dependencies);

            final Constructor<T> constructor = type.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);

            return constructor.newInstance(parameterInstances.toArray());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new DependencyInstantiationException(e);
        }
    }

    private List<Object> mapParameterInstances(final List<Dependency> dependencies) {
        return dependencies
                .stream()
                .map(Dependency::getInstance)
                .collect(toList());
    }

    private Class[] mapParameterTypes(final List<Dependency> dependencies) {
        return dependencies
                .stream()
                .map(Dependency::getType)
                .toArray(Class[]::new);
    }
}
