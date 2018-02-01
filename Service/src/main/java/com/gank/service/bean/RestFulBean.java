package com.gank.service.bean;

/**
 *
 * @author ywl
 * @date 2017-10-2
 */
public class RestFulBean<T> {


    private T results;
    private int status;
    private String msg;

    public RestFulBean(T results, int status, String msg) {
        this.results = results;
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
