package com.gank.android.app.controller;

import android.content.Context;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.gank.android.app.aouth.User;
import com.gank.android.app.entity.UserEntity;
import com.gank.android.app.even.BaseEven;
import com.gank.android.app.even.EvenBusTag;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.model.UserModel;
import com.gank.android.app.model.UserModelImpl;
import com.gank.android.app.net.UrlKit;
import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.NoticeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.gank.androidlibs.event.BusFactory;
import cn.gank.androidlibs.httphelper.BaseResponse;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.httphelper.JsonCallback;
import cn.gank.androidlibs.httphelper.Result;
import cn.gank.androidlibs.httphelper.ResultCallBack;
import cn.gank.androidlibs.kit.Kits;
import cn.gank.androidlibs.log.XLog;
import okhttp3.Call;
import okhttp3.Response;


/**
 * @author shijunxing
 */

public class UserController extends BaseController<UserModelImpl, UserModel> {

    public static final int REQUEST_CODE_CAMERA = 1000;
    public static final int REQUEST_CODE_GALLERY = 1001;

    public UserController(String receiverIDCard) {
        super(receiverIDCard);
    }

    /**
     * 用户信息
     *
     * @param context
     * @param phone
     */
    public void getInfo(Context context, String phone) {
        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        getUser(context, UrlKit.getUserUrl(UrlKit.API_INFO), RequestEventID.USER_INFO, params);
    }

    /**
     * 登录
     *
     * @param context
     * @param phone
     * @param pwd
     */
    public void login(Context context, String phone, String pwd) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(pwd)) {
            dispatchEvent(DataEvent.get(RequestEventID.USER_LOGIN, new Result(false, "账户或密码不能为空")));
            return;
        }

        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("password", pwd);

        getUser(context, UrlKit.getUserUrl(UrlKit.API_LOGIN), RequestEventID.USER_LOGIN, params);

    }


    /**
     * 注册、登录、个人信息
     */
    private void getUser(Context context, String url, final String RequestID, Map<String, String> params) {

        HttpFactory.getHelper().invokePost(context, url, params, new JsonCallback<BaseResponse<UserEntity>>() {

            @Override
            public void onSuccess(BaseResponse<UserEntity> response, Call call, Response response2) {
                if (response != null && response.getResults() != null) {
                    User.setInfo(response.getResults());
                    BusFactory.getBus().post(new BaseEven("", EvenBusTag.USER_INFO));
                    dispatchEvent(new NoticeEvent(RequestID, response));
                } else if (response != null) {
                    if (RequestID.equals(RequestEventID.USER_LOGOUT) && response.getStatus() == 0) {
                        User.loginOut();
                    }
                    dispatchEvent(new NoticeEvent(RequestID, response));
                } else {
                    dispatchEvent(new NoticeEvent(RequestID, -2, "未知错误"));
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(RequestID, -1, e.getMessage()));
            }
        });

    }


    /**
     * 注册
     *
     * @param context
     * @param phone
     * @param name
     * @param pwd
     */
    public void register(Context context, String phone, String name, String pwd) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
            dispatchEvent(DataEvent.get(RequestEventID.USER_REGISTER, new Result(false, "账户或密码不能为空")));
            return;
        }

        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("name", name);
        params.put("password", pwd);
        params.put("device", "厂商:" + DeviceUtils.getManufacturer() + ",型号:" + DeviceUtils.getModel() + ",系统:" + DeviceUtils.getSDKVersion());
        params.put("version", Kits.Package.getVersionName(context));

        getUser(context, UrlKit.getUserUrl(UrlKit.API_REGISTER), RequestEventID.USER_REGISTER, params);

    }


    /**
     * 退出
     *
     * @param context
     */
    public void logout(Context context) {
        Map<String, String> params = new HashMap<>(16);
        getUser(context, UrlKit.getUserUrl(UrlKit.API_LOGOUT), RequestEventID.USER_LOGOUT, params);
    }


    /**
     * 获取验证码
     *
     * @param phone
     */
    public void getVerificationCode(String phone) {
        BmobSMS.requestSMSCode(phone, "gank", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                //验证码发送成功
                if (e == null) {
                    dispatchEvent(new NoticeEvent(RequestEventID.USER_CODE, 0, "验证码发送成功"));
                    //用于查询本次短信发送详情
                    XLog.i("bmob", "短信id：" + smsId);
                } else {
                    dispatchEvent(new NoticeEvent(RequestEventID.USER_CODE, -2, "验证码发送失败"));
                }
            }
        });
    }

    /**
     * 验证验证码是否正确
     */
    public void verifyCode(String phone, String code) {
        BmobSMS.verifySmsCode(phone, code, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                //短信验证码已验证成功
                if (ex == null) {
                    dispatchEvent(new NoticeEvent(RequestEventID.USER_VERIFY_CODE, 0, "短信验证码已验证成功"));
                } else {
                    dispatchEvent(new NoticeEvent(RequestEventID.USER_VERIFY_CODE, -2, "短信验证码验证失败"));
                    XLog.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                }
            }
        });
    }


    /**
     * 从相册/相机获取图片
     */
    public void getPhoto(final Context context, int type) {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setEnableEdit(true);
        functionConfigBuilder.setCropSquare(true);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setForceCrop(true);
        functionConfigBuilder.setEnablePreview(true);

        GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() != 0) {
                    try {
                        upload(context, resultList.get(0).getPhotoPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        };

        if (type == REQUEST_CODE_GALLERY) {
            GalleryFinal.openGallerySingle(type, functionConfigBuilder.build(), mOnHanlderResultCallback);
        } else {
            GalleryFinal.openCamera(type, functionConfigBuilder.build(), mOnHanlderResultCallback);
        }

    }


    /**
     * 修改密码
     *
     * @param pwd 密码
     */
    public void updatePwd(Context context, String phone, String pwd) {

        Map<String, String> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("password", pwd);
        HttpFactory.getHelper().invokePost(context, UrlKit.getUserUrl(UrlKit.API_UPDATE_PWD), params,new JsonCallback<BaseResponse<UserEntity>>() {

            @Override
            public void onSuccess(BaseResponse<UserEntity> response, Call call, Response response2) {
                dispatchEvent(new NoticeEvent(RequestEventID.USER_UPDATE_PWD, response));
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(RequestEventID.USER_UPDATE_PWD, -1, e.getMessage()));
            }
        });

    }

    /**
     * 将图片上传
     *
     * @param imgPath
     */
    private void upload(Context context, String imgPath) {

        Map<String, String> params = new HashMap<>(16);
        params.put("phone", User.getPhone());
        HttpFactory.getHelper().invokePost(context, UrlKit.getUserUrl(UrlKit.API_UPLOAD), params, "file", imgPath, new JsonCallback<BaseResponse<UserEntity>>() {

            @Override
            public void onSuccess(BaseResponse<UserEntity> response, Call call, Response response2) {
                if (response != null && response.getResults() != null) {
                    User.setHead(response.getResults().getUserhead());
                }
                dispatchEvent(new NoticeEvent(RequestEventID.USER_CHANGE_HEAD, response));
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(RequestEventID.USER_CHANGE_HEAD, -1, e.getMessage()));
            }
        });

    }


}
