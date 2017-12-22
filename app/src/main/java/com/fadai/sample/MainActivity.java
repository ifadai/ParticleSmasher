package com.fadai.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fadai.particlesmasher.ParticleSmasher;
import com.fadai.particlesmasher.SmashAnimator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ParticleSmasher mSmasher;
    private ImageView mIv1, mIv2, mIv3, mIv4, mIv5, mIv6;
    private Button mBtnReset;
    private TextView mTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSmasher = new ParticleSmasher(this);
        init();
    }

    private void init() {
        mIv1 = (ImageView) findViewById(R.id.iv_main_1);
        mIv2 = (ImageView) findViewById(R.id.iv_main_2);
        mIv3 = (ImageView) findViewById(R.id.iv_main_3);
        mIv4 = (ImageView) findViewById(R.id.iv_main_4);
        mIv5 = (ImageView) findViewById(R.id.iv_main_5);
        mIv6 = (ImageView) findViewById(R.id.iv_main_6);
        mBtnReset = (Button) findViewById(R.id.btn_main_reset);
        mTv1 = (TextView) findViewById(R.id.tv_main_1);

        mIv1.setOnClickListener(this);
        mIv2.setOnClickListener(this);
        mIv3.setOnClickListener(this);
        mIv4.setOnClickListener(this);
        mIv5.setOnClickListener(this);
        mIv6.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        mTv1.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_main_reset:
                reset();
                break;
            case R.id.iv_main_1:
                mSmasher.with(view).start();
                break;
            case R.id.iv_main_2:
                mSmasher.with(view)
                        .setStyle(SmashAnimator.STYLE_DROP)    // 设置动画样式
                        .setDuration(1500)                     // 设置动画时间
                        .setStartDelay(300)                    // 设置动画前延时
                        .setHorizontalMultiple(2)              // 设置横向运动幅度
                        .setVerticalMultiple(2)                // 设置竖向运动幅度
                        .addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                            @Override
                            public void onAnimatorStart() {
                                super.onAnimatorStart();
                                // 回调，动画开始
                            }

                            @Override
                            public void onAnimatorEnd() {
                                super.onAnimatorEnd();
                                // 回调，动画结束
                            }
                        })
                        .start();
                break;
            case R.id.iv_main_3:
                mSmasher.with(view)
                        .setStyle(SmashAnimator.STYLE_FLOAT_TOP)
                        .setHorizontalMultiple(2)
                        .setVerticalMultiple(3)
                        .setDuration(2500)
                        .start();
                break;
            case R.id.iv_main_4:
                mSmasher.with(view)
                        .setStyle(SmashAnimator.STYLE_FLOAT_BOTTOM)
                        .setHorizontalMultiple(2)
                        .setVerticalMultiple(4)
                        .setDuration(3000)
                        .setStartDelay(500)
                        .start();
                break;
            case R.id.iv_main_5:
                mSmasher.with(view)
                        .setStyle(SmashAnimator.STYLE_FLOAT_LEFT)
                        .setHorizontalMultiple(2)
                        .setVerticalMultiple(2)
                        .setDuration(1500)
                        .start();
                break;
            case R.id.iv_main_6:
                mSmasher.with(view)
                        .setStyle(SmashAnimator.STYLE_FLOAT_RIGHT)
                        .setHorizontalMultiple(2)
                        .setVerticalMultiple(2)
                        .setDuration(1500)
                        .start();
                break;

            case R.id.tv_main_1:
                mSmasher.with(view)
                        .setVerticalMultiple(9)
                        .setHorizontalMultiple(3)
                        .addAnimatorListener(new SmashAnimator.OnAnimatorListener() {
                            @Override
                            public void onAnimatorEnd() {
                                super.onAnimatorEnd();
                                mSmasher.reShowView(view);
                                Toast.makeText(MainActivity.this, "动画结束", Toast.LENGTH_LONG).show();
                            }
                        }).start();
                break;


        }
    }

    private void reset() {
        // 让View重新显示
        mSmasher.reShowView(mIv1);
        mSmasher.reShowView(mIv2);
        mSmasher.reShowView(mIv3);
        mSmasher.reShowView(mIv4);
        mSmasher.reShowView(mIv5);
        mSmasher.reShowView(mIv6);
        mSmasher.reShowView(mTv1);
        mTv1.setVisibility(View.VISIBLE);

    }
}