package com.pixos.guide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GuideActivity extends Activity {

    private int currentScreen = 0;
    private final View[] screens = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 初始化所有界面
        screens[0] = getLayoutInflater().inflate(R.layout.screen_welcome, null);
        screens[1] = getLayoutInflater().inflate(R.layout.screen_terms, null);
        screens[2] = getLayoutInflater().inflate(R.layout.screen_features, null);
        screens[3] = getLayoutInflater().inflate(R.layout.screen_finish, null);

        FrameLayout container = findViewById(R.id.container);
        for (View screen : screens) {
            container.addView(screen);
            screen.setVisibility(View.GONE);
        }

        showScreen(0);
        setupButtons();
    }

    private void showScreen(int index) {
        if (index < 0 || index >= screens.length) return;

        // 淡出当前界面
        if (screens[currentScreen].getVisibility() == View.VISIBLE) {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            screens[currentScreen].startAnimation(fadeOut);
            screens[currentScreen].setVisibility(View.GONE);
        }

        // 淡入新界面
        currentScreen = index;
        screens[currentScreen].setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        screens[currentScreen].startAnimation(fadeIn);
    }

    private void setupButtons() {
        // 欢迎界面
        Button welcomeNext = screens[0].findViewById(R.id.btn_next);
        welcomeNext.setOnClickListener(v -> showScreen(1));

        // 协议界面
        Button termsBack = screens[1].findViewById(R.id.btn_back);
        Button termsNext = screens[1].findViewById(R.id.btn_next);
        CheckBox checkAgree = screens[1].findViewById(R.id.check_agree);

        termsBack.setOnClickListener(v -> showScreen(0));
        termsNext.setOnClickListener(v -> {
            if (checkAgree.isChecked()) {
                showScreen(2);
            } else {
                Toast.makeText(GuideActivity.this, 
							   "请先同意用户协议", Toast.LENGTH_SHORT).show();
            }
        });

        // 功能界面
        Button featuresBack = screens[2].findViewById(R.id.btn_back);
        Button featuresNext = screens[2].findViewById(R.id.btn_next);

        featuresBack.setOnClickListener(v -> showScreen(1));
        featuresNext.setOnClickListener(v -> showScreen(3));

        // 完成界面
        Button uninstall = screens[3].findViewById(R.id.btn_uninstall);
        uninstall.setOnClickListener(v -> uninstallSelf());
    }

    private void uninstallSelf() {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // 禁止返回键和系统导航栏
    @Override
    public void onBackPressed() {
        // 完全禁用返回键
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // 隐藏系统导航栏
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
