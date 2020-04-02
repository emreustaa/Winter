package com.fsm.Utlis;

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
