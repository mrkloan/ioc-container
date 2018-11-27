package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("Proxy invocation handler should")
class ProxyInvocationHandlerTest {

    @Test
    @DisplayName("throw when providing a null instance supplier")
    void should_throw_when_providing_a_null_instance_supplier() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyInvocationHandler.of(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("supply the instance when invoking a method for the first time")
    void should_supply_the_instance_when_invoking_a_method_for_the_first_time() {
        final Object instance = mock(Object.class);
        final Supplier<Object> supplier = (Supplier<Object>) mock(Supplier.class);
        final ProxyInvocationHandler proxyInvocationHandler = ProxyInvocationHandler.of(supplier);

        assertDoesNotThrow(() -> {
            final Object proxy = mock(Object.class);
            final Method method = Object.class.getMethod("toString");
            final String expectedValue = "value";
            final Object[] args = new Object[]{};

            when(supplier.get()).thenReturn(instance);
            when(instance.toString()).thenReturn(expectedValue);

            verify(supplier, never()).get();
            final Object result = proxyInvocationHandler.invoke(proxy, method, args);
            verify(supplier).get();

            assertThat(result).isEqualTo(expectedValue);
        });
    }
}