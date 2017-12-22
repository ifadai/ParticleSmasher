> Android 粒子破碎效果，可以使用于任何View。

## 特色：
- 六种效果，包含爆炸效果、坠落效果、四个方向的逐渐飘落效果；
- 链式调用，自定义动画时间、样式、动画幅度等；


## 效果图：

![六种效果演示](https://github.com/ifadai/ParticleSmasher/blob/master/screenshot/screen1.gif)

## 用法：
### 导入

```
dependencies {
 compile 'com.ifadai:particlesmasher:1.0.1'
}
```
### 简单使用：

```
 ParticleSmasher smasher = new ParticleSmasher(this);
 // 默认为爆炸动画
 smasher.with(view).start();
```
### 复杂一点：

```
smasher.with(view)
        .setStyle(SmashAnimator.STYLE_DROP)    // 设置动画样式
        .setDuration(1500)                     // 设置动画时间
        .setStartDelay(300)                    // 设置动画前延时
        .setHorizontalMultiple(2)              // 设置横向运动幅度，默认为3
        .setVerticalMultiple(2)                // 设置竖向运动幅度，默认为4
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
```
### 让View重新显示：

```
smasher.reShowView(view);
```
### 代码解析：
[Android粒子破碎效果（2）——实现多种破碎效果之ParticleSmasher](http://www.jianshu.com/p/fee6b7c1215d)
