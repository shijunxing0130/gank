package com.gank.android.app.net;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.Toast;

import com.gank.android.app.R;
import com.gank.android.app.aouth.User;
import com.gank.android.app.entity.Notice;
import com.gank.android.app.utils.NotificationUtils;

import cn.bmob.push.PushConstants;
import cn.gank.androidlibs.httphelper.Convert;
import cn.gank.androidlibs.kit.Kits;


/**
 *  集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
    @author shijunxing
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Notice notice = Convert.fromJson(msg,Notice.class);

            Uri uri = Uri.parse("gank://uri.gank.main?action=1");
            Intent targetIntent = new Intent("android.gank.main.activity");
            targetIntent.setData(uri);
            PendingIntent pendingIntent;

            if (!Kits.Package.isAppIsInBackground(context)) {
                pendingIntent = PendingIntent.getActivity(context,
                        (int) SystemClock.uptimeMillis(), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            }else {
                pendingIntent = PendingIntent.getActivity(context,
                        (int) SystemClock.uptimeMillis(), targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationUtils utils=new NotificationUtils(context, 1000);
            utils.sendSingleLineNotification("有干货更新啦~~~", "有干货更新啦~~~", notice.getAlert(), R.drawable.icon_gank, pendingIntent, true, true, true);
            User.setHasGankHuo(true);
        }
    }

}
