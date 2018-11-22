package io.fries.ioc;

import java.util.List;

interface Instantiator {
    <T> T createInstance(Class<T> type, List<Dependency> dependencies);
}
