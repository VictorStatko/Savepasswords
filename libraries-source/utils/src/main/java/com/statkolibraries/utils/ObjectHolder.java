package com.statkolibraries.utils;

public class ObjectHolder<T> {
    private T object;

    public ObjectHolder(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
