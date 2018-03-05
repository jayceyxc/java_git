package com.linus.test;

interface Supplier<T> {
    T get();
}

public class ClosureTest {
    public static Supplier<Integer> testClosure() {
        int i = 1;
        /**
         * 在lambda表达式以及匿名类内部，如果引用某局部变量，则直接将其视为final
         */
//        i++;
        return new Supplier<Integer> () {
            @Override
            public Integer get () {
                return i;
            }
        };
    }

    public static Supplier<Integer> testClosure2() {
        int i = 2;
        /**
         * 在lambda表达式以及匿名类内部，如果引用某局部变量，则直接将其视为final
         */
//        i++;
        return () -> {
            return i;
        };
    }

    public static void main (String[] args) {
        Supplier<Integer> supplier = testClosure ();
        System.out.println (supplier.get ());

        supplier = testClosure2 ();
        System.out.println (supplier.get ());
    }
}
