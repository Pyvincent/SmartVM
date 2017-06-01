package com.inhand.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.inhand.handle.Myhandler;
import com.inhand.open.aidl.InterfaceConstant;
import com.inhand.open.aidl.OpenChannelBean;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private final static String TAG=MyBroadCastReceiver.class.getSimpleName();
    Myhandler myhandler;

    public MyBroadCastReceiver(Myhandler mh) {
        myhandler = mh;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(InterfaceConstant.ACTION_VCS_NETWORK_ONLINE)) {
            Boolean mNetOnline = intent.getBooleanExtra(InterfaceConstant.EXTRAC_NETWORK_ONLINE,
                    false);
            if (mNetOnline) {
                Log.i(TAG, "售货机平台链接成功");
            } else {
                Log.i(TAG, "售货机平台断开了");
            }

        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_VMC_ONLINE)) {
            Log.i(TAG, "VMC断了");
        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_INBOXCORE_HEARTBEAT_REQ)) {
            Log.i(TAG, "收到了inboxcore发过来的一个心跳");
            Intent respIntent = new Intent(InterfaceConstant.ACTION_INBOXCORE_HEARTBEAT_RESP);
            respIntent.putExtra(InterfaceConstant.EXTRAC_SERVICE_ID,
                    InterfaceConstant.SERVICE_SMARTVM_ID);
            respIntent.putExtra(InterfaceConstant.EXTRAC_SERVICE_PID,
                    android.os.Process.myPid());
            context.sendBroadcast(respIntent);

        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_VCS_CABINET_INFO_CHANGED)) {
            Log.i(TAG, "货道变了，要更新了");
            Message message = new Message();
            message.what = 2;
            myhandler.sendMessage(message);
        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_OPEN_QR_INDENT_RESP)) {
            Log.i(TAG, "获取二维码的广播");
            OpenChannelBean openChannelBean = intent.getParcelableExtra(InterfaceConstant.EXTRAC_CHANNEL_BEAN);
            Log.i("二维码获取到了--", String.valueOf(openChannelBean));
            if (openChannelBean != null) {
                String wechatUrl = openChannelBean.getWechatUrl();
                Log.i("二维码链接--", wechatUrl);
                Message message = new Message();
                Bundle bd = new Bundle();
                bd.putString("wechaturl", wechatUrl);
                message.setData(bd);
                message.what = 1;
                myhandler.sendMessage(message);

            } else {
                Log.i(TAG, "二维码获取失败");
            }


        }

    }
}
