package com.robin.lazy.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.robin.lazy.cache.CacheLoaderManager;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long lastTime;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= View.inflate(this,R.layout.activity_main,null);
        setContentView(view);
        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonLoad).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        textView=(TextView)findViewById(R.id.textView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonSave) {
            //把数据存入缓存需要操作io流,有对io的操作都是比较耗时的,最好都在子线程完成.
            //android 理想的fps是大于等于60(16s/帧),这种情况应用操作起来才会感觉不到卡顿
            //尽管测出来存储缓存数据使用的时间在6s左右,但是为了性能考虑,最好在子线程中操作
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String area_strs = FileUtil.readAssets(MainActivity.this, "province.json");
                            lastTime = System.currentTimeMillis();
                            CacheLoaderManager.getInstance().saveString("area_strs", area_strs, 0);
                            CacheLoaderManager.getInstance().saveString("area_strs1", area_strs, 0);
                            textView.setText("保存数据用时:"+(System.currentTimeMillis() - lastTime) + "毫秒");
                        }
                    });
                }
            }).start();
        } else if (id == R.id.buttonLoad) {
            lastTime = System.currentTimeMillis();
            String area_strs=CacheLoaderManager.getInstance().loadString("area_strs");
            if(TextUtils.isEmpty(area_strs)){
                area_strs+=CacheLoaderManager.getInstance().loadString("area_strs1");
            }
            Toast.makeText(MainActivity.this, "加载数据用时:" + (System.currentTimeMillis() - lastTime) + "毫秒", Toast.LENGTH_SHORT).show();
            textView.setText(area_strs);
        }else if(id==R.id.buttonClear){
            CacheLoaderManager.getInstance().clear();
        }
    }
}
