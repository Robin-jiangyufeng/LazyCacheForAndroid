package com.robin.lazy.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private PtrFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= View.inflate(this,R.layout.activity_main,null);
        setContentView(view);
        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonLoad).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        textView=(TextView)findViewById(R.id.textView);
        mPtrFrame=(PtrFrameLayout)view;
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        // header
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);

/**
 * using a string, support: A-Z 0-9 - .
 * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
 */
        header.initWithString("Alibaba");
        mPtrFrame.setHeaderView(header);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

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
                            CacheLoaderManager.getInstance().saveString("area_strs", area_strs, 5);
                            textView.setText("保存数据用时:"+(System.currentTimeMillis() - lastTime) + "毫秒");
                        }
                    });
                }
            }).start();
        } else if (id == R.id.buttonLoad) {
            lastTime = System.currentTimeMillis();
            String area_strs=CacheLoaderManager.getInstance().loadString("area_strs");
            Toast.makeText(MainActivity.this, "加载数据用时:" + (System.currentTimeMillis() - lastTime) + "毫秒", Toast.LENGTH_SHORT).show();
            textView.setText(area_strs);
        }else if(id==R.id.buttonClear){
            CacheLoaderManager.getInstance().clear();
        }
    }
}
