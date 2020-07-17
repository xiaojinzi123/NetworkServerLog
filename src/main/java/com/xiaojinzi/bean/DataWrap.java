package com.xiaojinzi.bean;

public class DataWrap<T> {

    private T result;

    public DataWrap(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
