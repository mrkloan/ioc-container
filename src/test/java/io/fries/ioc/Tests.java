package io.fries.ioc;

class Tests {

    static class A {
        private final C c;
        private final D d;

        A(final C c, final D d) {
            this.c = c;
            this.d = d;
        }

        @Override
        public String toString() {
            return "A(" + c + "," + d + ")";
        }
    }

    static class B {
        private final A a;

        B(final A a) {
            this.a = a;
        }

        @Override
        public String toString() {
            return "B(" + a + ")";
        }
    }

    static class C {

        C() {
        }

        @Override
        public String toString() {
            return "C";
        }
    }

    static class D {
        private final E e;

        D(final E e) {
            this.e = e;
        }

        @Override
        public String toString() {
            return "D(" + e + ")";
        }
    }

    static class E {
        @Override
        public String toString() {
            return "E";
        }
    }

    static class F {
        private final E e;

        private F(final E e) {
            this.e = e;
        }
    }

    interface Circular {
        String dependsOn();
    }

    static class CircularA implements Circular {

        private final Circular circular;

        CircularA(final Circular circular) {
            this.circular = circular;
        }

        @Override
        public String dependsOn() {
            return circular.toString();
        }

        @Override
        public String toString() {
            return "A";
        }
    }

    static class CircularB implements Circular {

        private final Circular circular;

        CircularB(final Circular circular) {
            this.circular = circular;
        }

        @Override
        public String dependsOn() {
            return circular.toString();
        }

        @Override
        public String toString() {
            return "B";
        }
    }
}
