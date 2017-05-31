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
                Log.i("BBBBBBBBBBBBBBBBBB", "HTTP链接了");
            } else {
                Log.i("DDDDDDDDDDDDDDDDD", "HTTP断了");
            }

        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_VMC_ONLINE)) {
            Log.i("AAAAAAAAAAAAAAAAA", "VMC断了");
        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_INBOXCORE_HEARTBEAT_REQ)) {
            Log.i("TTTTTTTTTT", "收到了inboxcore发过来的一个心跳");
            Intent respIntent = new Intent(InterfaceConstant.ACTION_INBOXCORE_HEARTBEAT_RESP);
            respIntent.putExtra(InterfaceConstant.EXTRAC_SERVICE_ID,
                    InterfaceConstant.SERVICE_SMARTVM_ID);
            respIntent.putExtra(InterfaceConstant.EXTRAC_SERVICE_PID,
                    android.os.Process.myPid());
            context.sendBroadcast(respIntent);

        }
        if (intent.getAction().equals(InterfaceConstant.ACTION_OPEN_QR_INDENT_RESP)) {
            Log.i("EEEEEEEEEEEEEE", "获取二维码的广播");
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
                Log.i("QQQQQQQQQQQQQ", "二维码获取失败");
            }


        }

    }
}
