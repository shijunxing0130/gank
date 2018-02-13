package com.gank.service.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.gank.service.config.Config;
import com.gank.service.bean.RestFulBean;
import com.gank.service.dao.TokenDao;
import com.gank.service.util.Md5SaltTool;
import com.gank.service.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求过滤拦截
 * @author shijunxing
 * @date 2017/10/16
 */
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenDao tokenDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        // TODO Auto-generated method stub

        Map<String, String> params = getParams(request);
        //此url拦截只需对获取身份认证的url放行（如登陆url），剩余所有的url都需拦截。
        String uri = request.getRequestURI();

        //登录 注册
        if (uri.endsWith("user/login") || uri.endsWith("user/register") || uri.endsWith("user/logout") || uri.startsWith("/upload/") || uri.startsWith("/updatePwd/")) {
            return true;
        } else {
            //判断是否包含timestamp，token，sign参数，如果不含有返回错误码。
            if (isParameterDeficiency(params)) {
                //判断服务器接到请求的时间和参数中的时间戳是否相差很长一段时间（时间自定义如半个小时），如果超过则说明该请求过期
                if (isRequestExpired(Long.parseLong(params.get("timestamp")))) {
                    //判断token是否有效，根据请求过来的token，查询redis缓存中的uid，如果获取不到这说明该token已过期。

                    if (isTokenExpired(params.get("token"))) {
                        //根据用户请求的url参数，服务器端按照同样的规则生成sign签名，对比签名看是否相等，相等则放行。
                        if (isSignExpired(params)) {
                            return true;
                        } else {
                            onResult(8,"签名异常", response);
                            return false;
                        }
                    } else {
                        onResult(5,"身份认证过期", response);
                        return false;
                    }
                } else {
                    onResult(6,"请求过期", response);
                    return false;
                }

            } else {
                onResult(7,"指定参数不全", response);
                return false;
            }

        }

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        httpServletResponse.getOutputStream();
    }

    /**
     * 获取参数列表
     *
     * @param request 请求
     * @return
     */
    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(16);
        Enumeration<?> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String pName = (String) parameterNames.nextElement();
            String pValue = request.getParameter(pName);
            params.put(pName, pValue);
        }
        return params;
    }

    /**
     * 参数是否缺失
     *
     * @param params 参数列表
     * @return
     */
    private boolean isParameterDeficiency(Map<String, String> params) {
        return isParamValid(params, "token") && isParamValid(params, "timestamp") && isParamValid(params, "sign");
    }

    /**
     * 参数是否有效
     *
     * @param params 参数列表
     * @param param  参数
     * @return
     */
    private boolean isParamValid(Map<String, String> params, String param) {
        return params.containsKey(param) && params.get(param) != null && !"".equals(params.get(param));
    }

    /**
     * 返回结果
     *
     * @param statusCode 状态码
     * @param response
     */
    private void onResult(int statusCode,String statusMsg, HttpServletResponse response) {
        RestFulBean<Object> resuFulBean = new RestFulBean<>(null, statusCode, statusMsg);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream  writer;

        try {
            writer = response.getOutputStream();
            writer.write(JSONObject.toJSONString(resuFulBean, SerializerFeature.WriteMapNullValue).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求是否过期
     *
     * @param requestTime 请求时间戳
     * @return
     */
    private boolean isRequestExpired(long requestTime) {
        return System.currentTimeMillis() - requestTime < Config.REQUEST_TIME_LIMIT;
    }

    /**
     * token是否有效
     *
     * @param token token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return tokenDao.isTokenAvailable(token);
    }

    /**
     * 签名是否有效
     *
     * @param params 参数
     * @return
     */
    private boolean isSignExpired(Map<String, String> params) {
        String sign = params.get("sign");
        params.keySet().removeIf("sign"::equals);
        try {
            return Md5SaltTool.validPassword(SignUtils.createSign(params, true), sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
