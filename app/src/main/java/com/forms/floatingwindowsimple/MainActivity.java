package com.forms.floatingwindowsimple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.forms.floatingwindowsimple.service.TrafficServiceUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrafficServiceUtils.startService(MainActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrafficServiceUtils.showWindow(this);
    }

    @Override
    protected void onDestroy() {
        //判断当前栈内是否只有一个activity（多个activity跳转之间，悬浮窗不关闭，此判断应写在baseActivity内）
        if (isTaskRoot()) {
            if (TrafficServiceUtils.isServiceRunning(this, TrafficServiceUtils.ACTION)) {
                TrafficServiceUtils.stopService(this);
            }
        }
        super.onDestroy();
    }

}
