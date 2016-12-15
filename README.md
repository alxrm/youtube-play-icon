# Play
[![](https://jitpack.io/v/alxrm/youtube-play-icon.svg)](https://jitpack.io/#alxrm/youtube-play-icon)
[![License](http://img.shields.io/badge/license-MIT-green.svg?style=flat)]()

There is no need to explain what this thing does, just take a look at the gif below.

![](https://github.com/alxrm/youtube-play-icon/blob/master/art/play.gif?raw=true)

## Including in your project

Add to your root build.gradle:
```Groovy
allprojects {
	repositories {
	    // ...
	    maven { url "https://jitpack.io" }
	}
}
```

Add the dependency:
```Groovy
dependencies {
    compile 'com.github.alxrm:youtube-play-icon:0.9'
}
```

Usage
-----

### PlayIconDrawable

You can use it as a `Drawable`, and insert into any `ImageView`,
with beautiful fluent API like this:

```java
final ImageView iconView = (ImageView) findViewById(R.id.icon_play);
final PlayIconDrawable play = PlayIconDrawable.builder()
    .withColor(Color.WHITE)
    .withInterpolator(new FastOutSlowInInterpolator())
    .withDuration(300)
    .withInitialState(PlayIconDrawable.IconState.PAUSE)
    .withAnimatorListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        Log.d("Animation", "animationFinished");
      }
    })
    .withStateListener(new PlayIconDrawable.StateListener() {
      @Override public void onStateChanged(PlayIconDrawable.IconState state) {
        Log.d("IconState", "onStateChanged: " + state);
      }
    })
    .into(iconView);
```

All of these methods can be ignored, so the drawable will be created with it's default state

_There is an `PlayIconView`, which extends `ImageView` and simply draws the icon and provides an API to work with it.
You can use it in any layout._

## API

To change icon state w/o animation, you can use:

```java
PlayIcon.setIconState(@NonNull PlayIconDrawable.IconState state)
```

and to do the same with beautiful morphing animation:

```java
PlayIcon.animateToState(@NonNull PlayIconDrawable.IconState state)
```

you also can use a convenient `toggle` method:

```java
PlayIcon.toggle(boolean animated);
```

if you need to know whether the state is changed, there is a handy `StateListener`, you can set one with:

```java
PlayIcon.setStateListener(@Nullable PlayIconDrawable.StateListener listener);
```

for more info about API, take a look at [this](https://github.com/alxrm/youtube-play-icon/blob/master/lib/src/main/java/rm/com/youtubeplayicon/PlayIcon.java)

## Contribution

You are free to send me PRs and issues, I'd like to help you and improve this.

## License

    MIT License

    Copyright (c) 2016 Alexey Derbyshev

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
