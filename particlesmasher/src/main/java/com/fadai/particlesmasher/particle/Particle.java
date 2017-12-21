package com.fadai.particlesmasher.particle;

/**
 * <pre>
 *     author : FaDai
 *     e-mail : i_fadai@163.com
 *     time   : 2017/12/20
 *     desc   : xxxx描述
 *     version: 1.0
 * </pre>
 */

public abstract class Particle {

    public int color;                // 颜色
    public float radius;             // 半径
    public float alpha;              // 透明度（0~1）
    public float cx;                 // 圆心 x
    public float cy;                 // 圆心 y


    public float horizontalElement;  // 水平变化参数
    public float verticalElement;    // 垂直变化参数

    public float baseRadius;         // 初始半径，同时负责半径大小变化
    public float baseCx;             // 初始圆心 x
    public float baseCy;             // 初始圆心 y

    public float font;               // 决定了粒子在动画开始多久之后，开始显示
    public float later;              // 决定了粒子动画结束前多少时间开始隐藏

    public void advance(float factor, float endValue) {
    }

    ;

}
