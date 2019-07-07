package com.miedo.dtodoaqui.utils;

public class CallbackUtils {
    public interface SimpleCallback<T>{
        public void OnResult(T t);
        public void OnFailure(String response);
    }
}
