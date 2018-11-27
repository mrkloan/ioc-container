# ioc-container [![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container) [![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
> Lightweight IoC Container for the Java programming language.

## Usage

### Complete usage example

This example uses the following dependency graphs:

    B -> A -> C
          \-> D -> E
               \-> String
               
    F -> G -> F
    
You may notice that `F` and `G` are mutually dependent: that is called a *circular dependency*. In order to resolve this 
problem, `F` implements an `InterfaceF` on which `G` depends, and `G` implements an `InterfaceG` on which `F` depends.
This allows us to inject interface proxies instead of concrete implementations, whose instantiation is deferred until
the first method call.

```java
final Container container = Container.empty()
        .register(Id.of(A.class), A.class, asList(Id.of(C.class), Id.of(D.class)))
        .register(Id.of(B.class), B.class, singletonList(Id.of(A.class)))
        .register(Id.of(C.class), C.class, emptyList())
        .register(Id.of(D.class), D.class, asList(Id.of(E.class), Id.of("some.value")))
        .register(Id.of(E.class), E.class, E::new)
        .register(Id.of("some.value"), String.class, () -> "Raw string")
        .register(Id.of(F.class), InterfaceF.class, F.class, singletonList(G.class))
        .register(Id.of(G.class), InterfaceG.class, G.class, singletonList(F.class))
        .instantiate();

final D d = container.provide(Id.of(D.class)); // Provides any registered instance.
final B b = container.provide(Id.of(B.class)); // Provides the root of a dependency graph.

final InterfaceF f = container.provide(Id.of(F.class)); // Provides a proxy to an interface, solving potential circular dependency problems
```

### Identifiers

Each dependency uses a unique identifier for registration and provision. 

An `Id` can be created using any type. This way, you are in control of your identifiers and can register the same type 
multiple times if needed:

```java
final Id typeId = Id.of(Object.class);
final Id integerId = Id.of(1);
final Id stringId = Id.of("myDependency");
// ...
```

### Registering dependencies

The first step is to use a `RegistrationContainer` in order to register your dependency graph. The dependencies registration 
can be done in any order; a topological sort will be executed before their instantiation to reorder the graph.

You can create an empty `RegistrationContainer` using the `Container.empty()` factory method:

```java
final RegistrationContainer registrationContainer = Container.empty();
```

The `RegistrationContainer` exposes a `register` method for each type of dependency.

Register a supplied instance if you do not want the container to create it for you:

```java
registrationContainer.register(Id.of(C.class), C.class, C::new);
registrationContainer.register(Id.of("some.value"), String.class, () -> "Raw string");
```

Register a class whose instance will be managed by the container:

```java
registrationContainer.register(Id.of(E.class), E.class, emptyList());
registrationContainer.register(Id.of(D.class), D.class, asList(Id.of(E.class), Id.of("some.value")));
```

Register an interface proxy managed by the container:

```java
registrationContainer.register(Id.of(F.class), InterfaceF.class, F.class, singletonList(Id.of(G.class)));
registrationContainer.register(Id.of(G.class), InterfaceG.class, G.class, singletonList(Id.of(F.class)));
```

### Instantiation and provision

Once your registration process is over, you can start the instantiation process and get the resulting `Container`.
From now on, this `Container` can provide any instance that was previously registered in the `RegistrationContainer`:

```java
final Container container = registrationContainer.instantiate();

final String value = container.provide(Id.of("some.value"));
final D d = container.provide(Id.of(D.class));
final InterfaceF f = container.provide(Id.of(F.class));
```

### Custom instantiators

The `RegistrationContainer` uses an `Instantiator` in order to create instances of the registered classes.
The default implementation uses reflection to find a constructor matching the provided dependencies and calls it to 
create a new instance.

You can create your own `Instantiator` implementation and use it like so:

```java
class CustomInstantiator implements Instantiator {
    @Override
    public <T> T createInstance(final Class<T> type, final List<Dependency> dependencies) {
        // ...
    }
}

// ...
final Instantiator instantiator = new CustomInstantiator();
final RegistrationContainer registrationContainer = Container.using(instantiator);
```

## Installation

Gradle:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.MrKloan:ioc-container:1.0.0'
}
```

Maven:
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>com.github.MrKloan</groupId>
		<artifactId>ioc-container</artifactId>
		<version>1.0.0</version>
	</dependency>
</dependencies>
```