package io.fries.ioc.registry.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

class ProxyInvocationHandler implements InvocationHandler {

    private final Supplier<?> instanceSupplier;
    private volatile Object instance;

    private ProxyInvocationHandler(final Supplier<?> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    static ProxyInvocationHandler of(final Supplier<?> instanceSupplier) {
        Objects.requireNonNull(instanceSupplier);
        return new ProxyInvocationHandler(instanceSupplier);
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        method.setAccessible(true);
        return method.invoke(this.getInstance(), args);
    }

    private Object getInstance() {
        if (isNull(instance)) {
            lockAndSupplyInstance();
        }

        return instance;
    }

    private synchronized void lockAndSupplyInstance() {
        if (isNull(instance)) {
            instance = instanceSupplier.get();
        }
    }
}
