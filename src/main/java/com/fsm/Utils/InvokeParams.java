package com.fsm.Utils;

public class InvokeParams {

    Class<?> parameterTypes;
    Object[] parameters;

    public InvokeParams() { }

    public InvokeParams(Class<?> parameterTypes,
                        Object[] parameters) {
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
    }

}
