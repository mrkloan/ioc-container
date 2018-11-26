# ioc-container [![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container) [![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
> Lightweight IoC Container for the Java programming language.

## Usage

Complete usage example:

```java
// Dependency graph:
// B -> A -> C
//       \-> D -> E
final Container container = Container.empty()
        .register(Id.of(A.class), A.class, asList(Id.of(C.class), Id.of(D.class)))
        .register(Id.of(B.class), B.class, singletonList(Id.of(A.class)))
        .register(Id.of(C.class), C.class, emptyList())
        .register(Id.of(D.class), D.class, singletonList(Id.of(E.class)))
        .register(Id.of(E.class), E::new)
        .instantiate();

final B b = container.provide(Id.of(B.class));
```

An `Id` can be created using any type. This way, you are in control of your identifiers and can register the same type 
multiple times if needed:

```java
final Id typeId = Id.of(Object.class);
final Id integerId = Id.of(1);
final Id stringId = Id.of("myDependency");
```

First, you will need a `RegistrationContainer` in order to register your dependency graph. The dependencies registration 
can be done in any order; a topological sort will be executed before their instantiation.

```java
final RegistrationContainer registrationContainer = Container.empty();

// Register a class without any dependencies.
registrationContainer.register(Id.of(E.class), E.class, emptyList());

// Register a class with any number of dependencies.
registrationContainer.register(Id.of(D.class), D.class, singletonList(Id.of(E.class)));

// Register a supplied instance if you do not want the container to create it for you.
registrationContainer.register(Id.of(C.class), C::new);
registrationContainer.register(Id.of("my.string.value"), () -> "Some value");
```

Once your registration process is over, you can start the instantiation process and get the resulting `Container`.
From now on, you can ask it to provide any instance that was previously registered in the `RegistrationContainer`:

```java
final Container container = registrationContainer.instantiate();

final String value = container.provide(Id.of("my.string.value"));
final D d = container.provide(Id.of(D.class));
```

The `RegistrationContainer` uses an `Instantiator` instance in order to... instantiate the registered objects.
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