package io.quarkus.qe.hibernate.resources;

import java.lang.reflect.Field;

import io.quarkus.test.h2.H2DatabaseTestResource;

public class CustomH2DatabaseTestResource extends H2DatabaseTestResource {

    @Override
    public void inject(Object testInstance) {
        super.inject(testInstance);

        Class<?> c = testInstance.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if (f.getType().isAssignableFrom(CustomH2DatabaseTestResource.class)) {
                    setFieldValue(f, testInstance, this);
                }
            }

            c = c.getSuperclass();
        }
    }

    private void setFieldValue(Field f, Object testInstance, Object value) {
        try {
            f.setAccessible(true);
            f.set(testInstance, value);
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
