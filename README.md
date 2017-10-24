# Android Circle SeekBar

**Circle SeekBar** - An Android custom circular SeekBar that supports max/min range and step
settings.

![android-circle-seekbar](https://raw.githubusercontent.com/vhnguyen1001/android-circle-seekbar/master/art/art_demo.png)

## Gradle

```java
dependencies {
	...
	compile 'me.hiennguyen.circleseekbar:circleseekbar:1.0.0'
}
```

## Usage

* In XML layout:

```xml
<hiennguyen.me.circleseekbar.CircleSeekBar
        android:id="@+id/circular"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:padding="64dp"
        app:csb_arcColor="@color/color_arc"
        app:csb_arcWidth="6dp"
        app:csb_indicatorIcon="@drawable/ic_circle_seekbar"
        app:csb_max="1000"
        app:csb_min="10"
        app:csb_points="300"
        app:csb_progressColor="#00aad4"
        app:csb_progressWidth="12dp"
        app:csb_step="5"
        app:csb_textColor="@color/color_text"
        app:csb_textSize="36sp"
        app:csb_thumbSize="36dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        app:layout_constraintBottom_toBottomOf="parent"/>
```
**Remember** to add `layout_padding` to make sure that there is enough space to display the whole widget and indicator drawable.

* All customizable attributes:

```xml
<declare-styleable name="CircleSeekBar">
        <attr name="csb_points" format="integer"/>
        <attr name="csb_min" format="integer"/>
        <attr name="csb_max" format="integer"/>
        <attr name="csb_step" format="integer"/>

        <attr name="csb_indicatorIcon" format="reference"/>

        <attr name="csb_progressColor" format="color"/>
        <attr name="csb_progressWidth" format="dimension"/>

        <attr name="csb_arcColor" format="color"/>
        <attr name="csb_arcWidth" format="dimension"/>

        <attr name="csb_textSize" format="dimension"/>
        <attr name="csb_textColor" format="color"/>
        <attr name="csb_thumbSize" format="dimension" />

    </declare-styleable>
```

## Licence
Copyright 2017 Hien Nguyen - Junior Android Developer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
