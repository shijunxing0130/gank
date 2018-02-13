package cn.gank.androidlibs.httphelper;

import java.io.Serializable;

/**
 * Created by shijunxing on 2017/9/7.
 */

public class Result implements Serializable {
    private boolean success;
    private int status;
    private String msg;

    public Result(BaseResponse baseResponse) {
        this.status = baseResponse.getStatus();
        this.msg = baseResponse.getMsg();
        this.success = baseResponse.getStatus() == 0;
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Result(boolean success, int status, String msg) {
        this.success = success;
        this.status = status;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
