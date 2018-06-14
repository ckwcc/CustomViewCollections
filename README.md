# CustomViewCollections
这是一个基于Kotlin的自定义View集合

## 1.RulerView
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

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/app/src/main/res/drawable/rulerview.gif)

## 2.CircleLevelView和CircleVerticalView

### 2.1 CircleLevelView

```java
  app:externalRadius="60dp"//外圆半径
  app:innerRadius="50dp"//内圆半径
  app:endAngle="250"//开始角度
  app:startAngle="150"//结束角度
  app:centerTextSize="12sp"//中间显示的文字大小
  app:isPercent="true"//是否显示百分号
  app:centerTextColor="@color/colorRed"
  app:externalColor="@color/colorYellow"
  app:percentColor="@color/colorRed"
  app:innerColor="@color/colorWhite"
```

```java
  //以下两个方法的设置需要在setAngle或者setValue之前
  circleLevelView.setAnimationDuration(2500)
  circleLevelView.setIsPercent(true)
  //下面两个方法选一种就好了
  //方法1：通过设置初始和结束的角度
  circleLevelView.setAngle(0f,100f)
  //方法2：通过设置百分比
  circleLevelView.setValue(55.746f)
```
### 2.2 CircleVerticalView

 ```java
  app:circleVerticalViewRadius="50dp"//圆的半径
  app:verticalPercent="59.56"//设置的百分比
  app:waveHeight="4dp"//波浪高度
  app:waveWidth="30dp"//一份波浪的宽度
  app:circleVerticalTextSize="10sp"
  app:circleVerticalTextColor="@color/black"
  app:waveColor="@color/colorYellow"//波浪颜色
  app:horizontalDuration="2500"
  app:verticalDuration="8000"
 ```
 
 ```java
  circleVerticalView.setVerticalProgress(50f)//如果xml中已经设置过了verticalPercent，这里可以不再设置
  circleVerticalView.start()//开始
 ```
 
 效果图：
 
![image](https://github.com/ckwcc/CustomViewCollections/blob/master/app/src/main/res/drawable/levelview.gif)

## 3.TagViewLayout和TagView

### TagViewLayout

   TagViewLayout是TagView的容器，首先看一下TagViewLayout
   对外提供一个方法,用于添加TagView
```java
   tagViewLayout.addTagView(tagView)
```
   或者可以在xml中的TagViewLayout内直接添加TagView

### TagView

   xml属性：
```java
   app:tagViewText="这是为你好"
   app:tagViewTextColor="@color/black"
   app:tagViewTextSize="10sp"
   app:tagViewCorner="16dp"
   app:tagViewBgColor="@color/colorYellow"
```
  或者也可以在java中动态生成TagView:
  
```java
   val tagView = TagView(this)
   tagView.setMargins(4,4,4,4)//单位是dp
   tagView.setTagViewCorner(20)//单位是dp
   tagView.setTagViewBackground(Color.YELLOW)
   tagView.setTagViewText("世界那么大,看看")
   tagView.setTagViewTextSize(10)//单位是sp
   tagView.setTagViewTextColor(Color.RED)
```
 效果图：

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/app/src/main/res/drawable/tagview.jpg)
