# ioc-container [![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container) [![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
> Lightweight IoC Container for the Java programming language.

## Usage

These examples use the following object hierarchy, which you can found under the `testable` package in `src/test/java`:

    +-------------+-------------------------------------------------------+
    | Interface   | Implementation(Dependency...)...                      |
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
the Knight Perceval, depending on his best friend, the Knight Karadoc.

```java
final Container container = Container.empty()
        .register(managed(FantasyStory.class).with(PredictablePlot.class, "knights.perceval").as(FantasyStory.class))
        .register(managed(NovelBook.class).with(FantasyStory.class).as(NovelBook.class))
        .register(managed(PredictablePlot.class).with("plot.outcome").as(PredictablePlot.class))
        .register(supplied(() -> "Outcome").as("plot.outcome"))
        .register(proxy(FriendlyProtagonist.class).of(Protagonist.class).with("knights.karadoc").as("knights.perceval"))
        .register(managed(FriendlyProtagonist.class).with("knights.perceval").as("knights.karadoc"))
        .instantiate();

final Book book = container.provide(NovelBook.class);
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

The `RegistrationContainer` exposes a single `register` method consuming a `RegistrableBuilder` in order to stay extensible.
In the following examples, we assume that every required builder has been statically imported.

If you do not want the container to manage your object instances, or if you want to register hard-coded/configurations
values, use a `SuppliedRegistrableBuilder` in order to register a supplied instance:

```java
registrationContainer.register(supplied(HeroicProtagonist.class).as("hero"));
registrationContainer.register(supplied(() -> "Outcome").as("plot.outcome"));
```

If you want your objects to be managed by the container, use a `ManagedRegistrableBuilder` to perform the registration:

```java
// These 3 examples are equivalent: the identifier and dependencies can be inferred by the container.
registrationContainer.register(managed(NovelBook.class));
registrationContainer.register(managed(NovelBook.class).as(NovelBook.class));
registrationContainer.register(managed(NovelBook.class).as(NovelBook.class).with(Story.class));
```

Finally, use a `ProxyRegistrableBuilder` if you want to register an interface proxy managed by the container: 

```java
// These 4 example are equivalent: the proxied interface, dependencies and identifier can be inferred by the container.
registrationContainer.register(proxy(FriendlyProtagonist.class));
registrationContainer.register(proxy(FriendlyProtagonist.class).of(Protagonist.class));
registrationContainer.register(proxy(FriendlyProtagonist.class).of(Protagonist.class).with(Protagonist.class));
registrationContainer.register(proxy(FriendlyProtagonist.class).of(Protagonist.class).with(Protagonist.class).as(FriendlyProtagnosit.class));
```

### Instantiation and provision

Once your registration process is over, you can start the instantiation process and get the resulting `Container`.
From now on, this `Container` can provide any instance that was previously registered in the `RegistrationContainer`:

```java
final Container container = registrationContainer.instantiate();

final String outcome = container.provide("plot.outcome");
final Book novelBook = container.provide(NovelBook.class);
final Protagonist karadoc = container.provide("knights.karadoc");
```

### Custom instantiators

The `RegistrationContainer` uses an `Instantiator` in order to create instances of the registered classes.
The default implementation uses reflection to find a constructor and tries to call it with the provided dependencies to 
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