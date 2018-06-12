# CustomViewCollections
这是一个基于Kotlin的自定义View集合

1.RulerView
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

![image](https://github.com/ckwcc/CustomViewCollections/blob/master/app/src/main/res/drawable/rulerview.gif)

2.CircleLevelView和CircleVerticalView
```java
  //CircleLevelView
  app:externalRadius="60dp"
  app:innerRadius="50dp"
  app:endAngle="250"
  app:startAngle="150"
  app:centerTextSize="12sp"
  app:isPercent="true"
  app:centerTextColor="@color/colorRed"
  app:externalColor="@color/colorYellow"
  app:percentColor="@color/colorRed"
  app:innerColor="@color/colorWhite"
```

 ```java
  //CircleVerticalView
  app:circleVerticalViewRadius="50dp"
  app:verticalPercent="59.56"
  app:waveHeight="4dp"
  app:waveWidth="30dp"
  app:circleVerticalTextSize="10sp"
  app:circleVerticalTextColor="@color/black"
  app:waveColor="@color/colorYellow"
 ```
 
![image](https://github.com/ckwcc/CustomViewCollections/blob/master/app/src/main/res/drawable/levelview.gif)
