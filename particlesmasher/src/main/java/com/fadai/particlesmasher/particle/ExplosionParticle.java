package com.fadai.particlesmasher.particle;

import android.graphics.Rect;

import java.util.Random;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/12/14
 *     desc   : 爆炸粒子
 *     version: 1.0
 * </pre>
 */

public class ExplosionParticle extends Particle{

    /**
     * 生成粒子
     *
     * @param color              粒子颜色
     * @param radius             粒子的半径
     * @param rect               View区域的矩形
     * @param endValue           动画的结束值
     * @param random             随机数
     * @param horizontalMultiple 水平变化幅度
     * @param verticalMultiple   垂直变化幅度
     */
    public ExplosionParticle( int color, int radius, Rect rect, float endValue, Random random, float horizontalMultiple, float verticalMultiple){

        this.color = color;
        alpha = 1;

        // 参与横向变化参数和竖直变化参数计算，规则：横向参数相对值越大，竖直参数越小
        float nextFloat = random.nextFloat();

        baseRadius = getBaseRadius(radius, random, nextFloat);
        this.radius =  baseRadius;

        horizontalElement = getHorizontalElement(rect, random, nextFloat, horizontalMultiple);
        verticalElement = getVerticalElement(rect, random, nextFloat, verticalMultiple);

        int offsetX = rect.width() / 4;
        int offsetY = rect.height() / 4;

        // baseCx,baseCy在中心点四周的offset/2的范围内。
        baseCx = rect.centerX() + offsetX * (random.nextFloat() - 0.5f);
        baseCy = rect.centerY() + offsetY * (random.nextFloat() - 0.5f);
        cx = baseCx;
        cy = baseCy;


        font = endValue / 10 * random.nextFloat();
        later = 0.4f * random.nextFloat();
    }

    private static float getBaseRadius(float radius, Random random, float nextFloat) {
        float r = radius + radius * (random.nextFloat() - 0.5f) * 0.5f;
        r = nextFloat < 0.6f ? r :
                nextFloat < 0.8f ? r * 1.4f : r * 0.8f;
        return r;
    }

    private static float getHorizontalElement(Rect rect, Random random, float nextFloat,float horizontalMultiple) {

        // 第一次随机运算：h=width*±(0.01~0.49)
        float horizontal = rect.width() * (random.nextFloat() - 0.5f);

        // 第二次随机运行： h= 1/5概率：h；3/5概率：h*0.6; 1/5概率：h*0.3; nextFloat越大，h越小。
        horizontal = nextFloat < 0.2f ? horizontal :
                nextFloat < 0.8f ? horizontal * 0.6f : horizontal * 0.3f;

        // 上面的计算是为了让横向变化参数有随机性，下面的计算是修改横向变化的幅度。
        return horizontal * horizontalMultiple;
    }

    private static float getVerticalElement(Rect rect, Random random, float nextFloat,float verticalMultiple) {

        // 第一次随机运算： v=height*(0.5~1)
        float vertical = rect.height() * (random.nextFloat() * 0.5f + 0.5f);

        // 第二次随机运行： v= 1/5概率：v；3/5概率：v*1.2; 1/5概率：v*1.4; nextFloat越大，h越大。
        vertical = nextFloat < 0.2f ? vertical :
                nextFloat < 0.8f ? vertical * 1.2f : vertical * 1.4f;

        // 上面的计算是为了让变化参数有随机性，下面的计算是变化的幅度。
        return vertical * verticalMultiple;
    }


    public void advance(float factor, float endValue) {

        // 动画进行到了几分之几
        float normalization = factor / endValue;

        if (normalization < font || normalization > 1f - later) {
            alpha = 0;
            return;
        }
        alpha = 1;

        // 粒子可显示的状态中，动画实际进行到了几分之几
        normalization = (normalization - font) / (1f - font - later);
        // 动画超过7/10，则开始逐渐变透明
        if (normalization >= 0.7f) {
            alpha = 1f - (normalization - 0.7f) / 0.3f;
        }

        float realValue = normalization * endValue;

        // y=j+k*x，j、k都是常数，x为 0~1.4
        cx = baseCx + horizontalElement * realValue;

        // y=j+k*(x*(x-1)，j、k都是常数，x为 0~1.4
        cy = baseCy + verticalElement * (realValue * (realValue - 1));

        radius = baseRadius + baseRadius / 4 * realValue;

    }


}
