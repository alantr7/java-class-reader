package com.alant7_.classreader;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class TestingClass {

    private final String name = "test";

    enum Vehicle {
        CAR, TRUCK, AIRPLANE
    }

    @Getter
    private final Vehicle vehicle = Vehicle.CAR;

    @Deprecated
    @TestAnnotation(value = RetentionPolicy.RUNTIME, a = "Hello", randomClass = TestingClass.class, annotationRef = @TestAnnotation2, classes = { Object.class, Double.class, TestAnnotation2.class })
    private final float f = 5f;

    @TestAnnotation(value = RetentionPolicy.RUNTIME, a = "Hello", randomClass = TestingClass.class, annotationRef = @TestAnnotation2, classes = {})
    private final double d = 5d;

    @Deprecated
    public void test(@NotNull String a) {

    }

    public TestingClass() {
    }

    private String vehicleName;

    public TestingClass(String car) {
        this.vehicleName = car;
    }

    @Deprecated
    static class B {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
        RetentionPolicy value();

        int integer() default 10;

        String a();

        Class<?> randomClass();

        TestAnnotation2 annotationRef();

        Class<?>[] classes();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2 {

    }

}
