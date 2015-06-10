# MagnifyingLayout
a custom layout with a magnifying class to zoom in content, highlight the touched word in text view.

#ScreenShot
![](https://github.com/FridayLi/MagnifyingLayout/blob/master/screenshots/screenshot1.png)

#Usage
Include `MagnifyingLayout` in your layout XML.
```xml
<com.datayes.magnifying.MagnifyingLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <!--add you content view-->
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <com.datayes.magnifying.TextSelectionView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            />
    </ScrollView>
</com.datayes.magnifying.MagnifyingLayout>
```

In the `onCreate` of your activity or the `onCreateView` of your fragment, configue the
`MagnifyingLayout`(magnify glass width, magnify glass height glass border color, etc.)
```java
MagnifyingLayout container = (MagnifyingLayout)findViewById(R.id.container);
MagnifyingLayoutConfiguration config = new MagnifyingLayoutConfiguration.Builder()
        .setGlassWidth(500)
        .setGlassHeight(250)
        .setGlassBorderWidth(15)
        .setScale(1.3f)
        .build();
container.initConfiguration(config);
```
