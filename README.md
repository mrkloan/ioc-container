# ioc-container [![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container) [![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
> Lightweight IoC Container for the Java programming language, featuring [manual](#manual-usage) and [annotations-based](#annotations-usage) registration.

## Manual usage

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
registrationContainer.register(supplied(HeroicProtagonist::new).as("hero"));
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

### Custom `Instantiator`

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

### Custom `Registrable`

The examples provided above use the three default `Registrable` implementations in order  to create a `Component`. 
If you encounter a use case that is not fulfilled by one of these three solutions, feel free to create your own implementation
of the `Registrable` interface:

```java
class CustomRegistrable implements Registrable { /* Implement the required methods */ }

// ...
final Registrable customRegistrable = new CustomRegistrable(...);
final Container container = Container.empty()
    .register(customRegistrable)
    .instantiate();
```  

## Annotations usage

### Complete usage example

Instead of dealing with your components registration manually, you can use a simple set of four annotations to manage 
their interactions with the IoC container. The following code snippets (one per class) highlight this usage.

```java
// Registers an instance using its class name as the default identifier: "NovelBook".
@Register
class NovelBook implements Book {
    private final Story story;
    
    // Depends on a Story instance, identified by "story.fantasy".
    NovelBook(@Identified("story.fantasy") final Story story) {
        this.story = story;
    }
}
```

```java
// Registers an instance using a custom identifier: "story.fantasy".
@Register(id = "story.fantasy")
class FantasyStory implements Story {
    private final Plot plot;
    private final Protagonist protagonist;
    
    // As these constructor parameters are not annotated with @Identified,
    // their class name will be used as identifiers: "Plot" and "Protagonist".
    FantasyStory(final Plot plot, final Protagonist protagonist) {
        this.plot = plot;
        this.protagonist = protagonist;
    }
}
```

```java
@Register(id = "Plot")
class ComplicatedPlot implements Plot {
    private final String complication;
    
    // This component depends on a primitive type, which can only be registered 
    // using a supplied registrable.
    ComplicatedPlot(@Identified("plotComplication") final String complication) {
        this.complication = complication;
    }
}
```

```java
// A configuration class will not be registered as a component itself.
// Rather, it exposes its methods annotated with @Register as supplied
// components inside the container.
@Configuration
class Library {
    
    // As no identifier is specified with the annotation, 
    // the method name will be used instead: "plotComplication".
    @Register
    String plotComplication() {
        return "Some cliffhanger";
    }
}
```

```java
// You may notice that this Protagonist depends on the same Story instance that depends on itself...
// This is a nasty case of circular dependency. 
//
// Hence, you can use a @Proxy annotation in order not to register the instance of the component
// itself, but a proxy to the implemented interface holding the actual instance, safely injectable.
// 
// In this example, the "type" parameter can be omitted: the proxied type would automatically be 
// inferred as the first implemented interface of the registered type.
@Proxy(id = "Protagonist", type = Protagonist.class)
class StoryDependentProtagonist implements Protagonist {
    private final Story story;
    
    StoryDependentProtagonist(@Identified("story.fantasy") final Story story) {
        this.story = story;
    }
}
```

Once your components are properly annotated, you simply need to provide an entry point to the container.
This entry point is a class whose package will be used as the root for reflection scanning, and do not require to be
annotated/registered inside the container whatsoever. The package content and any of its subpackages will be included.

```java
final Container container = Container.scan(Library.class);
final Book book = container.provide("NovelBook");
```

### Custom `RegistrableScanner`

A `RegistrableScanner` is responsible for identifying and creating `Registrable` instances. Note that a custom scanner 
is only necessary if you created a custom `Registrable` and want the container to be able to manage them automatically
(using your own annotation, for example).

The `Container.scan` method takes a vararg of `RegistrableScanner` as its final parameter, allowing you to pass along as
many custom scanners as you want:

```java
class CustomRegistrableScanner implements RegistrableScanner {
    @Override
    public List<Registrable> findAll() {
        // ...
    }
}

// ...
final RegistrableScanner customScanner = new CustomRegistrableScanner();
final Container container = Container.scan(Library.class, customScanner);
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