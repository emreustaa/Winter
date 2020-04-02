package com.fsm.Utlis;

public class InvokeResult<T> {
    private Class<?> resultType;
    private Object result;


    public InvokeResult(Class<?> resultType, Object result) {
        this.resultType = resultType;
        this.result = result;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public Object getResult() {
        return result;
    }
}
