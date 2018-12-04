package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.scanner.type.TypeScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testable.TestableApplication;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Supplied registrable should")
class SuppliedRegistrableScannerTest {

    @Mock
    private TypeScanner typeScanner;
    @Mock
    private Instantiator instantiator;

    private SuppliedRegistrableScanner suppliedRegistrableScanner;

    @BeforeEach
    void setUp() {
        this.suppliedRegistrableScanner = new SuppliedRegistrableScanner(typeScanner, instantiator);
    }

    @Test
    @DisplayName("find all supplied registrables")
    void should_find_all_supplied_registrables() {
        final Set<Class<?>> scannedTypes = new HashSet<>();
        scannedTypes.add(TestableApplication.class);

        when(typeScanner.findAnnotatedBy(Configuration.class)).thenReturn(scannedTypes);
        final List<Registrable> result = suppliedRegistrableScanner.findAll();

        assertThat(result).hasSize(1);
        assertThat(result).first().hasFieldOrPropertyWithValue("id", Id.of("plot.outcome"));
    }

    @Test
    @DisplayName("throw when the configuration type does not declare an empty constructor")
    void should_throw_when_the_configuration_type_does_not_declare_an_empty_constructor() {
        final Class<?> type = SuppliedRegistrableScanner.class;

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> suppliedRegistrableScanner.fromConfigurationInstance(type))
                .withMessage("Invalid configuration class: " + type.getName() + ". An empty constructor is required");
    }

    @Test
    @DisplayName("create a supplier for the registered method call")
    void should_create_a_supplier_for_the_registered_method_call() throws NoSuchMethodException {
        final String expected = "Expected";
        final Object configuration = mock(Object.class);
        final Method method = configuration.getClass().getDeclaredMethod("toString");

        when(configuration.toString()).thenReturn(expected);
        final Supplier<?> supplier = suppliedRegistrableScanner.createSupplier(configuration, method);

        assertThat(supplier.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("throw when creating a supplier for a method with parameters")
    void should_throw_when_creating_a_supplier_for_a_method_with_parameters() throws NoSuchMethodException {
        final Object configuration = mock(Object.class);
        final Method method = configuration.getClass().getDeclaredMethod("equals", Object.class);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> suppliedRegistrableScanner.createSupplier(configuration, method))
                .withMessage("No parameters allowed on a supplied method");
    }

    @Test
    @DisplayName("extract the method name when the register value is empty")
    void should_extract_the_method_name_when_the_register_value_is_empty() throws NoSuchMethodException {
        final Method method = Object.class.getDeclaredMethod("toString");
        final Register register = mock(Register.class);

        when(register.value()).thenReturn("");
        final Id result = suppliedRegistrableScanner.extractSupplierId(method, register);

        final Id expected = Id.of("toString");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("extract the registered identifier")
    void should_extract_the_registered_identifier() throws NoSuchMethodException {
        final Method method = Object.class.getDeclaredMethod("toString");
        final Register register = mock(Register.class);

        when(register.value()).thenReturn("string.value");
        final Id result = suppliedRegistrableScanner.extractSupplierId(method, register);

        final Id expected = Id.of("string.value");
        assertThat(result).isEqualTo(expected);
    }
}