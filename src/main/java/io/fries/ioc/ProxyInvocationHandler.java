package io.fries.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

class ProxyInvocationHandler implements InvocationHandler {

    private final Supplier<Object> instanceSupplier;
    private Object instance;

    ProxyInvocationHandler(final Supplier<Object> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return method.invoke(this.getInstance(), args);
    }

    private Object getInstance() {
        if (isNull(instance)) {
            instance = instanceSupplier.get();
        }

        return instance;
    }
}
