# CustomViewCollections
这是一个基于Kotlin的自定义View集合

## 1.PlayPauseView

效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/playpauseview.gif)

PlayPauseView是一个播放暂停按钮，具体请看:
[一步步实现自定义View之播放暂停控件](https://blog.csdn.net/ckwccc/article/details/80761974)

## 2.RadarView
RadarView（雷达图）

效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/radarview.gif)

具体请看:
[一步步实现自定义View之雷达图](https://blog.csdn.net/ckwccc/article/details/80832825)


## 3.各种进度条

### 3.1 CircleLevelView
### 3.2 CircleVerticalView
### 3.3 CirclePointView

 效果图：
 
![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/levelview.gif)

[一步步实现自定义View之圆形进度条](https://blog.csdn.net/ckwccc/article/details/80774948)

## 4.TagViewLayout和TagView

### TagViewLayout

   TagViewLayout是TagView的容器,对外提供一个方法,用于添加TagView
   或者可以在xml中的TagViewLayout内直接添加TagView

### TagView : TagViewLayout中的一个item

 效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/tagview.jpg)

具体请看：[一步步实现自定义View之流式布局](https://blog.csdn.net/ckwccc/article/details/80782174)

## 5.StarViewLayout

StarViewLayout是类似于淘宝评价时评分的星星控件
```java
    app:starNumber="5"//星星（图形）的数量
    app:defaultStar="@mipmap/star_default"//默认的图形
    app:lightStar="@mipmap/star_selected"//被选中的图形
    app:starMargin="4dp"//图形间的间距
```

代码中的点击事件
```java
        //控件的点击事件
       starViewLayout.setStarClickListener(this)

       override fun setOnStarClick(position: Int) {
               Toast.makeText(this,"点亮了"+position+"颗星星",Toast.LENGTH_SHORT).show()
       }

       //控件回复默认状态
       starViewLayout.revertToDefaultState()
```

效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/starview.gif)

## 6.RulerView
```java
  app:alphaEnable="true"
  app:lineColor="@color/gray"
  app:lineMaxHeight="40dp"
  app:lineMidHeight="30dp"
  app:lineMinHeight="20dp"
  app:lineSpaceWidth="10dp"
  app:lineWidth="1dp"
  app:textColor="@color/black"
  app:minValue="80.0"//尺子最小值
  app:maxValue="250.0"//尺子最大值
  app:perValue="1"//值的单位
  app:selectorValue="165.0" //默认被选中的值
```
```java
  //设置参数
  rulerView.setRulerViewParams(155f,100f,200f,0.1f)
```

效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/images/rulerview.gif)

