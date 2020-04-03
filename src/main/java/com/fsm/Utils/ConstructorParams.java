package com.fsm.Utils;

public class ConstructorParams {

    Class<?> parameterTypes;
    Object[] parameters;

    ConstructorParams() { }

    ConstructorParams(Class<?> parameterTypes, Object[] parameters) {
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

}
