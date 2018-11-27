package io.fries.ioc.dependencies;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Id should")
class IdTest {

    @Test
    @DisplayName("throw when providing a null value")
    void should_throw_when_providing_a_null_value() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> Id.of(null));
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id firstId = Id.of(0);
        final Id secondId = Id.of(0);

        assertThat(firstId).isEqualTo(secondId);
        assertThat(firstId.hashCode()).isEqualTo(secondId.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Id firstId = Id.of("value");
        final Id secondId = Id.of(0);

        assertThat(firstId).isNotEqualTo(secondId);
        assertThat(firstId.hashCode()).isNotEqualTo(secondId.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Id id = Id.of("id");

        final String result = id.toString();

        assertThat(result).isEqualTo("Id{value='id'}");
    }
}