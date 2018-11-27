package io.fries.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

class ProxyInvocationHandler implements InvocationHandler {

    private final Supplier<?> instanceSupplier;
    private Object instance;

    private ProxyInvocationHandler(final Supplier<?> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    static ProxyInvocationHandler of(final Supplier<?> instanceSupplier) {
        Objects.requireNonNull(instanceSupplier);
        return new ProxyInvocationHandler(instanceSupplier);
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return method.invoke(this.getInstance(), args);
    }

    private synchronized Object getInstance() {
        if (isNull(instance)) {
            instance = instanceSupplier.get();
        }

        return instance;
    }
}
