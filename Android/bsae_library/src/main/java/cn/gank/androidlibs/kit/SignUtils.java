package cn.gank.androidlibs.kit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static cn.gank.androidlibs.XConf.SIGN_KEY;

/**
 *
 * @author shijunxing
 * @date 2017/10/30
 */

public class SignUtils {

    public static String createSign(Map<String, String> params, boolean encode)
            throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }

        temp.append(SIGN_KEY);
        return temp.toString().toUpperCase();

    }


}

