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
}
