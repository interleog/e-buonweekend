package it.uiip.digitalgarage.ebuonweekend.entity;

import java.util.List;

public class GenericReturn<T> {
    private T returnObject;
    private List<String> errors;

    public GenericReturn(T returnObject) {
        this.returnObject = returnObject;
        this.errors = null;
    }

    public GenericReturn(T returnObject, List<String> errors) {
        this.returnObject = returnObject;
        this.errors = errors;
    }

    public GenericReturn() {
    }

    public T getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(T returnObject) {
        this.returnObject = returnObject;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
