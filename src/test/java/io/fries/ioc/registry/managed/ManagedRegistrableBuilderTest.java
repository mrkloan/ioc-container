package io.fries.ioc.registry.managed;

import io.fries.ioc.RegistrationContainer;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testable.NovelBook;

import java.util.List;

import static io.fries.ioc.registry.managed.ManagedRegistrableBuilder.managed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Managed registrable builder should")
class ManagedRegistrableBuilderTest {

    @Test
    @DisplayName("infer the identifier and dependencies of the managed type")
    void should_infer_the_id_of_the_managed_type() {
        final Class<?> type = NovelBook.class;
        final Id id = Id.of(type);
        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(id, type, emptyList());

        final ManagedRegistrableBuilder result = managed(type);

        assertThat(result).isEqualTo(builder);
    }

    @Test
    @DisplayName("update the dependencies identifiers")
    void should_update_the_dependencies_id() {
        final Id id = mock(Id.class);
        final Class<?> type = Object.class;
        final List<Id> dependencies = singletonList(Id.of(Object.class));

        final Object dependency = mock(Object.class);
        final List<Id> expectedDependencies = singletonList(Id.of(dependency));
        final ManagedRegistrableBuilder expected = new ManagedRegistrableBuilder(id, type, expectedDependencies);

        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(id, type, dependencies);
        final ManagedRegistrableBuilder result = builder.with(dependency);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("update its identifier")
    void should_update_the_registrable_id() {
        final Object newId = mock(Object.class);
        final ManagedRegistrableBuilder expected = new ManagedRegistrableBuilder(Id.of(newId), Object.class, emptyList());

        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(mock(Id.class), Object.class, emptyList());
        final ManagedRegistrableBuilder result = builder.as(newId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("build the managed registrable without inferring its dependencies")
    void should_build_the_managed_registrable_without_inferring_its_dependencies() {
        final Id id = mock(Id.class);
        final List<Id> dependencies = singletonList(Id.of(mock(Object.class)));
        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(id, Object.class, dependencies);
        final ManagedRegistrable expected = ManagedRegistrable.of(id, Object.class, dependencies);

        final Registrable result = builder.build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("build the managed registrable with inferred dependencies")
    void should_build_the_managed_registrable_with_inferred_dependencies() {
        final Id id = mock(Id.class);
        final List<Id> dependencies = asList(Id.of(Instantiator.class), Id.of(Registry.class));
        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(id, RegistrationContainer.class, emptyList());
        final ManagedRegistrable expected = ManagedRegistrable.of(id, RegistrationContainer.class, dependencies);

        final Registrable result = builder.build();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Id id = mock(Id.class);
        final ManagedRegistrableBuilder firstBuilder = new ManagedRegistrableBuilder(id, Object.class, emptyList());
        final ManagedRegistrableBuilder secondBuilder = new ManagedRegistrableBuilder(id, Object.class, emptyList());

        assertThat(firstBuilder).isEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isEqualTo(secondBuilder.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final ManagedRegistrableBuilder firstBuilder = new ManagedRegistrableBuilder(mock(Id.class), Object.class, emptyList());
        final ManagedRegistrableBuilder secondBuilder = new ManagedRegistrableBuilder(mock(Id.class), Object.class, emptyList());

        assertThat(firstBuilder).isNotEqualTo(secondBuilder);
        assertThat(firstBuilder.hashCode()).isNotEqualTo(secondBuilder.hashCode());
    }

    @Test
    void should_be_formatted_as_a_string() {
        final Id id = mock(Id.class);
        final ManagedRegistrableBuilder builder = new ManagedRegistrableBuilder(id, Object.class, emptyList());

        when(id.toString()).thenReturn("Id");
        final String result = builder.toString();

        assertThat(result).isEqualTo("ManagedRegistrableBuilder{id=Id, type=class java.lang.Object, dependencies=[]}");
    }
}