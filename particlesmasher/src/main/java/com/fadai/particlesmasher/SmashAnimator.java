package com.fadai.particlesmasher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.fadai.particlesmasher.particle.DropParticle;
import com.fadai.particlesmasher.particle.ExplosionParticle;
import com.fadai.particlesmasher.particle.FloatParticle;
import com.fadai.particlesmasher.particle.Particle;

import java.util.Random;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/12/14
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public class SmashAnimator {

    public static final int STYLE_EXPLOSION=1,       // 爆炸
            STYLE_DROP=2,                            // 下落
            STYLE_FLOAT_LEFT=3,                      // 飘落——>自左往右，逐列飘落
            STYLE_FLOAT_RIGHT=4,                     // 飘落——>自右往左，逐列飘落
            STYLE_FLOAT_TOP=5,                       // 飘落——>自上往下，逐行飘落
            STYLE_FLOAT_BOTTOM=6;                    // 飘落——>自下往上，逐行飘落

    private int mStyle=STYLE_EXPLOSION;             // 动画样式

    private ValueAnimator mValueAnimator;

    private ParticleSmasher mContainer;                  // 绘制动画效果的View
    private View mAnimatorView;                        // 要进行爆炸动画的View
    
    private Bitmap mBitmap;
    private Rect mRect;                                // 要进行动画的View在坐标系中的矩形
    
    private Paint mPaint;                              // 绘制粒子的画笔
    private Particle[][] mParticles;                   // 粒子数组
    
    private float mEndValue = 1.5f;

    private long mDuration = 1000L;
    private long mStartDelay = 150L;
    private float mHorizontalMultiple = 3;             // 粒子水平变化幅度
    private float mVerticalMultiple = 4;               // 粒子垂直变化幅度
    private int mRadius=Utils.dp2Px(2);                // 粒子基础半径

    // 加速度插值器
    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.6f);
    private OnExplosionListener mOnExplosionListener;

    public SmashAnimator(ParticleSmasher view, View animatorView) {
        this.mContainer = view;
        init(animatorView);
    }

    private void init(View animatorView) {
        this.mAnimatorView = animatorView;
        mBitmap = mContainer.createBitmapFromView(animatorView);
        mRect = mContainer.getViewRect(animatorView);
        initValueAnimator();
        initPaint();
    }

    private void initValueAnimator() {
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setFloatValues(0F, mEndValue);
        mValueAnimator.setInterpolator(DEFAULT_INTERPOLATOR);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    /**
     *   爆炸动画回调事件
     */
    public static abstract class OnExplosionListener {

        /**
         * 动画开始时回调
         */
        public void onExplosionStart() {
        }

        /**
         * 动画结束后回调
         */
        public void onExplosionEnd() {
        }

    }

    /**
     *   设置动画样式
     *   @param style {@link #STYLE_EXPLOSION},{@link #STYLE_DROP},{@link #STYLE_FLOAT_TOP},{@link #STYLE_FLOAT_BOTTOM},{@link #STYLE_FLOAT_LEFT},{@link #STYLE_FLOAT_RIGHT};
     *
     *   @return      链式调用，因此返回本身
     */
    public SmashAnimator setStyle(int style){
        this.mStyle=style;
        return this;
    }

    /**
     *   设置爆炸动画时间
     *   @param duration    时间，单位为毫秒
     *   @return      链式调用，因此返回本身
     */
    public SmashAnimator setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     *   设置爆炸动画前延时
     *   @param startDelay    动画开始前的延时，单位为毫秒
     *   @return      链式调用，因此返回本身
     *
     */
    public SmashAnimator setStartDelay(long startDelay) {
        mStartDelay = startDelay;
        return this;
    }

    /**
     *   设置水平变化参数
     *   @param horizontalMultiple          水平变化幅度，默认为3。为0则不产生变化。
     *   @return      链式调用，因此返回本身
     */
    public SmashAnimator setHorizontalMultiple(float horizontalMultiple) {
        this.mHorizontalMultiple = horizontalMultiple;
        return this;
    }

    /**
     *   设置垂直变化参数
     *   @param verticalMultiple  垂直变化参数，默认为4，为0则不产生变化
     *   @return      链式调用，因此返回本身
     *
     */
    public SmashAnimator setVerticalMultiple(float verticalMultiple) {
        this.mVerticalMultiple = verticalMultiple;
        return this;
    }

    /**
     *   设置粒子基础半径
     *   @param radius  半径，单位为px
     *   @return      链式调用，因此返回本身
     */
    public SmashAnimator setParticleRadius(int radius){
        this.mRadius=radius;
        return this;
    }

    /**
     *   添加回调
     *   @param listener   回调事件，包含开始回调、结束回调。
     *   @return      链式调用，因此返回本身
     */
    public SmashAnimator addExplosionListener(final OnExplosionListener listener) {
        this.mOnExplosionListener = listener;
        return this;
    }

    /**
     *   开始动画
     */
    public void start() {
        setValueAnimator();
        calculateParticles(mBitmap);
        hideView(mAnimatorView, mStartDelay);
        mValueAnimator.start();
        mContainer.invalidate();
    }

    /**
     *   设置动画参数
     */
    private void setValueAnimator() {
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setStartDelay(mStartDelay);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnExplosionListener != null) {
                    mOnExplosionListener.onExplosionEnd();
                }
                mContainer.removeAnimator(SmashAnimator.this);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnExplosionListener != null) {
                    mOnExplosionListener.onExplosionStart();
                }

            }
        });
    }

    /**
     * 根据图片计算粒子
     * @param bitmap      需要计算的图片
     */
    private void calculateParticles(Bitmap bitmap) {

        int col = bitmap.getWidth() /(mRadius*2);
        int row = bitmap.getHeight() / (mRadius*2);

        Random random = new Random(System.currentTimeMillis());
        mParticles = new Particle[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int x=j * mRadius*2 + mRadius;
                int y=i * mRadius*2 + mRadius;
                int color = bitmap.getPixel(x, y);
                Point point=new Point(mRect.left+x,mRect.top+y);

                switch (mStyle){
                    case STYLE_EXPLOSION:
                        mParticles[i][j] = new ExplosionParticle(color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_DROP:
                        mParticles[i][j] = new DropParticle(point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_LEFT:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_LEFT,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_RIGHT:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_RIGHT,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_TOP:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_TOP,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                    case STYLE_FLOAT_BOTTOM:
                        mParticles[i][j] = new FloatParticle(FloatParticle.ORIENTATION_BOTTOM,point,color, mRadius, mRect, mEndValue, random, mHorizontalMultiple, mVerticalMultiple);
                        break;
                }

            }
        }
        mBitmap.recycle();
        mBitmap = null;
    }


    /**
     *  View执行颤抖动画，之后再执行和透明动画，达到隐藏View的效果
     *  @param view 执行效果的View
     *  @param startDelay 爆炸动画的开始前延时时间
     */
    public void hideView(final View view, long startDelay) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(startDelay + 50).setFloatValues(0f, 1f);
        // 使View颤抖
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            Random random = new Random();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setTranslationX((random.nextFloat() - 0.5F) * view.getWidth() * 0.05F);
                view.setTranslationY((random.nextFloat() - 0.5f) * view.getHeight() * 0.05f);
            }
        });
        valueAnimator.start();
        // 将View 缩放至0、透明至0
        view.animate().setDuration(260).setStartDelay(startDelay).scaleX(0).scaleY(0).alpha(0).start();
    }


    /**
     *   开始逐个绘制粒子
     *   @param canvas  绘制的画板
     *   @return 是否成功
     */
    public boolean draw(Canvas canvas) {
        if (!mValueAnimator.isStarted()) {
            return false;
        }
        for (Particle[] particle : mParticles) {
            for (Particle p : particle) {
                // 根据动画进程，修改粒子的参数
                p.advance((float) (mValueAnimator.getAnimatedValue()), mEndValue);
                if (p.alpha > 0) {
                    mPaint.setColor(p.color);
                    mPaint.setAlpha((int) (Color.alpha(p.color) * p.alpha));
                    canvas.drawCircle(p.cx, p.cy, p.radius, mPaint);
                }
            }
        }
        mContainer.invalidate();
        return true;
    }


}
