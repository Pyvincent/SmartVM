package com.inhand.smartvm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.inhand.adapter.MyAdapter;
import com.inhand.bean.Goods;
import com.inhand.broadcast.MyBroadCastReceiver;
import com.inhand.handle.Myhandler;
import com.inhand.open.aidl.IOpenService;
import com.inhand.open.aidl.InterfaceConstant;
import com.inhand.open.aidl.OpenCabinetBean;
import com.inhand.open.aidl.OpenChannelBean;
import com.inhand.open.aidl.OpenGoodsBean;
import com.inhand.open.aidl.OpenMachineConfig;
import com.inhand.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SmartVM extends AppCompatActivity {

    private MyBroadCastReceiver myBroadCastReceiver;
    private IOpenService iOpenService = null;
    private List<OpenCabinetBean> mCabinetBeanList = null;
    private OpenMachineConfig mMachineConfig = null;
    private SmartVM mSmartvm = null;
    private Context mContext;

    private GridView grid_goods;
    private BaseAdapter mAdapter = null;
    private ArrayList<Goods> mData = null;

    private ImageView wechattImg;

    ServiceConnection mServiceConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iOpenService = IOpenService.Stub.asInterface(service);
            showMainPage();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iOpenService = null;

        }
    };

    Myhandler myhandler = new Myhandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String wechaturl = msg.getData().getString("wechaturl");
                    Log.i("put QR--------", wechaturl);
                    Bitmap qrBitmap = Utils.generateBitmap(wechaturl, 400, 400);
                    wechattImg.setImageBitmap(qrBitmap);
                    break;
                case 2:
                    grid_goods.invalidate();
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSmartvm = this;
        mContext = SmartVM.this;
        setContentView(R.layout.activity_main);
        grid_goods = (GridView) findViewById(R.id.grid_goods);
        bindVendingCloudService();
        registerBroadcastReceiver();

        mData = new ArrayList<Goods>();
        mAdapter = new MyAdapter<Goods>(mData, R.layout.item_grid_icon) {

            @Override
            public void bindView(ViewHolder holder, Goods obj) {
                holder.setImagebyBitmap(R.id.img_icon, obj.getGoodsBitmap());
                holder.setText(R.id.txt_icon, obj.getGoodsName());
            }
        };
        grid_goods.setAdapter(mAdapter);
        grid_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "你点击了~" + position + "~项", Toast.LENGTH_SHORT).show();
                int ChannelId = 0;

                try {
                    mMachineConfig = iOpenService.getOpenMachineConfig();
                    List<OpenChannelBean> two = mCabinetBeanList.get(0).getChannelList();
                    ChannelId = Integer.parseInt(two.get(position).getStrChannelId());
                    Log.i("KKKKKKKKK", String.valueOf(ChannelId));

                    OpenChannelBean openChannelBean = mCabinetBeanList.get(0).getChannelList().get(position);
                    iOpenService.obtainOpenIndentCode(openChannelBean, 2);     //获取二维码
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                initPopWindow(view);
            }
        });


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    List<OpenCabinetBean> mCabinetBeanList;
//                    mCabinetBeanList = iOpenService.getOpenCabinetList();
//                    OpenChannelBean openChannelBean = mCabinetBeanList.get(0).getChannelList().get(0);
//                    iOpenService.obtainOpenIndentCode(openChannelBean, 2);     //获取二维码
//
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//                initPopWindow(v);
//
//                Intent intent = new Intent(mContext, MainUI.class);
////                Bundle bd = new Bundle();
////                bd.putString("name", name);
////                bd.putString("imgurl", imgurl);
////                intent.putExtras(bd);
//                startActivity(intent);

//            }
//        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    mCabinetBeanList = iOpenService.getOpenCabinetList();
//                    List<OpenGoodsBean> one = mCabinetBeanList.get(0).getGoodsList();
//
////                    for (int i = 0; i < one.size(); i++) {
////                        String imgurl = one.get(i).getImgUrl();
////                        mid.append(imgurl + "\n");
////                    }
//                    String imgurl = one.get(0).getImgUrl();
//                    mid.append(imgurl + "\n");
//
//                    Bitmap bitmap = getLoacalBitmap("/sdcard/inbox/data/picture/"+imgurl+".png"); //从本地取图片
//                    imageView.setImageBitmap(bitmap);	//设置Bitmap
//
////                    for(int i=0;i<mCabinetBeanList.size();i++){
////                        OpenGoodsBean one= (OpenGoodsBean) mCabinetBeanList.get(i).getGoodsList();
////                        String imgUrl=one.getImgUrl();
////                        mid.append(imgUrl+"\n");
////                    }
////                    mconfig = iOpenService.getOpenGlobalConfig();
////                    String server_addr = mconfig.getServerAddr();
////                    mid.setText(server_addr);
//
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
////                Intent in = new Intent(mContext, MainUI.class);
////                startActivity(in);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wechat, null, false);
        wechattImg = (ImageView) view.findViewById(R.id.wechatqr);

//        Button btn_xixi = (Button) view.findViewById(R.id.btn_xixi);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x000f0340));    //要为popWindow设置一个背景才有效
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);
    }


    private void showMainPage() {
        try {
            if (!checkServiceIsNull("showImg")) {
                mCabinetBeanList = iOpenService.getOpenCabinetList();
            }
            if (mCabinetBeanList != null) {
                List<OpenGoodsBean> one = mCabinetBeanList.get(0).getGoodsList();
                for (int i = 0; i < one.size(); i++) {
                    mData.add(new Goods(Utils.getLoacalBitmap("/sdcard/inbox/data/picture/" + one.get(i).getImgUrl() + ".png"), one.get(i).getName()));
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReceiver);
        unBindVendingCloudService();
        mSmartvm = null;

    }

    public SmartVM getInstance() {
        return mSmartvm;
    }

    public IOpenService getiOpenService() {
        return iOpenService;
    }

    public boolean checkServiceIsNull(String method) {
        if (null == iOpenService) {
            bindVendingCloudService();
            return true;
        }
        return false;
    }

    private void bindVendingCloudService() {
        if (null != iOpenService) {
            return;
        }
        Intent intent = new Intent(getString(R.string.vcs_serice_name));
        intent.setAction(getString(R.string.open_binder_action));
        bindService(intent, mServiceConnect, Context.BIND_AUTO_CREATE);

    }

    private void unBindVendingCloudService() {
        if (null == iOpenService) {
            return;
        }
        unbindService(mServiceConnect);

    }

    private void registerBroadcastReceiver() {
        myBroadCastReceiver = new MyBroadCastReceiver(myhandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(InterfaceConstant.ACTION_VCS_NETWORK_ONLINE);
        filter.addAction(InterfaceConstant.ACTION_VMC_ONLINE);
        filter.addAction(InterfaceConstant.ACTION_EXTERNAL_CABINET_VMCONLINE);
        filter.addAction(InterfaceConstant.ACTION_VCS_MACHINE_CFG_CHANGED);
        filter.addAction(InterfaceConstant.ACTION_VCS_CABINET_INFO_CHANGED);
        filter.addAction(InterfaceConstant.ACTION_VMC_BUTTON_EVENT);
        filter.addAction(InterfaceConstant.ACTION_VMC_DELIVER_FINISHED);
        filter.addAction(InterfaceConstant.ACTION_VCS_REQ_DELIVER);
        filter.addAction(InterfaceConstant.ACTION_PAYS_REQ_DELIVER);
        filter.addAction(InterfaceConstant.ACTION_VMC_RECV_MONEY);
        filter.addAction(InterfaceConstant.ACTION_PAYSERVICE_RECV_MONEY);
        filter.addAction(InterfaceConstant.ACTION_VMC_SND_MACH_FAULT_INFO);
        filter.addAction(InterfaceConstant.ACTION_SMARTVM_PICKUP_GOODS_RESP);
        filter.addAction(InterfaceConstant.ACTION_INBOXCORE_HEARTBEAT_REQ);
        filter.addAction(InterfaceConstant.ACTION_SMARTVM_GEN_QR_CHK_BEARTBEAT_RESP);
        filter.addAction(InterfaceConstant.ACTION_PAYSERVICE_PAY_STATE);
        filter.addAction(InterfaceConstant.ACTION_VMC_REFUND_EVENT);
        filter.addAction(InterfaceConstant.ACTION_VCS_CONFIG_INFO_CHANGED);
        filter.addAction(InterfaceConstant.ACTION_GAME_CONFIG_UPDATE);
        filter.addAction(InterfaceConstant.ACTION_SW_VDBG_LOG);
        filter.addAction(InterfaceConstant.ACTION_OPEN_QR_INDENT_RESP);
        this.registerReceiver(myBroadCastReceiver, filter);
    }
}

