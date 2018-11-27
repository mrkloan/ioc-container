package io.fries.ioc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Proxy invocation handler should")
class ProxyInvocationHandlerTest {

    @Test
    @DisplayName("throw when providing a null instance supplier")
    void should_throw_when_providing_a_null_instance_supplier() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ProxyInvocationHandler.of(null));
    }
}