# ioc-container [![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container) [![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
> Lightweight IoC Container for the Java programming language.

## Usage

These examples use the following object hierarchy, which you can found under the `testable` package in `src/test/java`:

    +-------------+-------------------------------------------------------+
    | Interface   | Implementation(Dependency, ...), ...                  |
    +-------------+-------------------------------------------------------+
    | Book        | NovelBook(Story)                                      |
    | Story       | FantasyStory(Plot, Protagonist)                       |
    | Plot        | IncrediblePlot(), PredictablePlot(String)             |
    | Protagonist | HeroicProtagonist(), FriendlyProtagonist(Protagonist) |
    +-------------+-------------------------------------------------------+

You may notice that `FriendlyProtagonist` depends on the `Protagonist` interface. This design could create a circular
dependency if two `FriendlyProtagonist` instances are mutually dependent. In order to solve this problem, we could
inject an interface proxy instead of a concrete implementation, whose instantiation would be deferred until the first 
method call.

### Complete usage example

We want to create a `NovelBook` of a `FantasyStory` with a `PredictablePlot` of "Outcome" and a `FriendlyProtagonist`,
the Knight Perceval, depending on its best friend, the Knight Karadoc.

```java
final Container container = Container.empty()
        .register(Id.of(FantasyStory.class), FantasyStory.class, asList(Id.of(PredictablePlot.class), Id.of("knights.perceval")))
        .register(Id.of(NovelBook.class), NovelBook.class, singletonList(Id.of(FantasyStory.class)))
        .register(Id.of(PredictablePlot.class), PredictablePlot.class, singletonList(Id.of("plot.outcome")))
        .register(Id.of("plot.outcome"), () -> "Outcome")
        .register(Id.of("knights.perceval"), Protagonist.class, FriendlyProtagonist.class, singletonList(Id.of("knights.karadoc")))
        .register(Id.of("knights.karadoc"), FriendlyProtagonist.class, singletonList(Id.of("knights.perceval")))
        .instantiate();

final Book book = container.provide(Id.of(NovelBook.class));
```

### Identifiers

Each component uses a unique identifier for registration and provision. 

An `Id` can be created using any type. This way, you are in control of your identifiers and can register the same type 
multiple times if needed:

```java
final Id typeId = Id.of(Book.class);
final Id integerId = Id.of(1);
final Id stringId = Id.of("knights.arthur");
// ...
```

### Registering components

The first step is to use a `RegistrationContainer` in order to register your component graph. The components registration 
can be done in any order; a topological sort will be executed before their instantiation to reorder the graph.

You can create an empty `RegistrationContainer` using the `Container.empty()` factory method:

```java
final RegistrationContainer registrationContainer = Container.empty();
```

The `RegistrationContainer` exposes a set of `register` factory methods for each type of `Registrable` component.

Register a supplied instance if you do not want the container to create it for you:

```java
registrationContainer.register(Id.of(HeroicProtagonist.class), HeroicProtagonist::new);
registrationContainer.register(Id.of("plot.outcome"), () -> "Outcome");
```

Register a class whose instance will be managed by the container:

```java
// These 3 examples are equivalent: the identifier and dependencies can be inferred by the container.
registrationContainer.register(NovelBook.class);
registrationContainer.register(Id.of(NovelBook.class), NovelBook.class);
registrationContainer.register(Id.of(NovelBook.class), NovelBook.class, singletonList(Id.of(Story.class)));
```

Register an interface proxy managed by the container: 

```java
// Register a FriendlyProtagonist as a proxy for the Protagonist interface.
registrationContainer.register(Id.of("knights.karadoc"), Protagonist.class, FriendlyProtagonist.class, singletonList(Id.of("knights.perceval")))

// Depends on the proxy, not the concrete implementation.
registrationContainer.register(Id.of("knights.perceval"), FriendlyProtagonist.class, singletonList(Id.of("knights.karadoc")))
```

### Instantiation and provision

Once your registration process is over, you can start the instantiation process and get the resulting `Container`.
From now on, this `Container` can provide any instance that was previously registered in the `RegistrationContainer`:

```java
final Container container = registrationContainer.instantiate();

final String outcome = container.provide(Id.of("plot.outcome"));
final Book novelBook = container.provide(Id.of(NovelBook.class));
final Protagonist karadoc = container.provide(Id.of("knights.karadoc"));
```

### Custom instantiators

The `RegistrationContainer` uses an `Instantiator` in order to create instances of the registered classes.
The default implementation uses reflection to find a constructor matching the provided components and calls it to 
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