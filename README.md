# ioc-container
> Lightweight IoC Container for the Java programming language.

[![](https://jitpack.io/v/MrKloan/ioc-container.svg)](https://jitpack.io/#MrKloan/ioc-container)
[![Build Status](https://travis-ci.org/MrKloan/ioc-container.svg?branch=master)](https://travis-ci.org/MrKloan/ioc-container)
[![Javadoc](https://img.shields.io/badge/docs-Javadoc-blue.svg)](https://mrkloan.github.io/ioc-container/index.html)

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
	implementation 'com.github.MrKloan:ioc-container:master-SNAPSHOT'
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
		<version>master-SNAPSHOT</version>
	</dependency>
</dependencies>
```