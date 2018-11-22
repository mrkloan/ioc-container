package io.fries.ioc;

class Tokens {

    static Tokens empty() {
        return new Tokens();
    }

    Tokens add(final DependencyToken token) {
        throw new UnsupportedOperationException();
    }
}
