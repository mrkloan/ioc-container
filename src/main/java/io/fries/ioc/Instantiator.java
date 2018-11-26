package io.fries.ioc;

import java.util.List;

public interface Instantiator {
    <T> T createInstance(Class<T> type, List<Dependency> dependencies);
}
