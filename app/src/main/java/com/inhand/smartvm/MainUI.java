package com.inhand.smartvm;


import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainUI extends EuclidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainUI.this, "Oh hi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();

        int[] avatars = {
                R.drawable.coffee,
                R.drawable.coffee,
                R.drawable.coffee,
                R.drawable.coffee,
        };

        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, avatars[i]);
            profileMap.put(EuclidListAdapter.KEY_NAME, names[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, getString(R.string.lorem_ipsum_short));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, getString(R.string.lorem_ipsum_long));
            profilesList.add(profileMap);
        }

        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }
}


//public class MainUI extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_ui);
//        TextView name = (TextView) findViewById(R.id.name);
//        ImageView img = (ImageView) findViewById(R.id.img);
//
//        Intent intent=this.getIntent();
//        if(intent!=null){
//
//            Bundle bd=intent.getExtras();
//            String nameV=bd.getString("name");
//            String imgV=bd.getString("imgurl");
//            name.setText(nameV);
//            Bitmap bitmap = Utils.getLoacalBitmap("/sdcard/inbox/data/picture/"+imgV+".png");
//            Matrix matrix=new Matrix();
//            matrix.postScale(2.0f,2.0f);
//            if(bitmap != null) {
//                Bitmap bit=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//                img.setImageBitmap(bit);
//            }
//
//
//        }
//    }
//}
