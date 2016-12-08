package rm.com.youtubeplayicon;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static rm.com.youtubeplayicon.Graphics.paintOf;
import static rm.com.youtubeplayicon.IconState.PAUSE;
import static rm.com.youtubeplayicon.IconState.PLAY;

/**
 * Created by alex
 */

public final class PlayIconDrawable extends Drawable implements PlayIcon {
  private static final boolean DEFAULT_VISIBLE = true;
  private static final float FRACTION_PLAY = 0F;
  private static final float FRACTION_PAUSE = 1F;

  private final Paint iconPaint = paintOf();
  private final ValueAnimator iconAnimator = ValueAnimator.ofFloat(FRACTION_PLAY, FRACTION_PAUSE);

  private IconState currentIconState = IconState.PLAY;
  private boolean visible = DEFAULT_VISIBLE;
  private float currentFraction = FRACTION_PLAY;

  public PlayIconDrawable() {
    iconAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        final float fraction = valueAnimator.getAnimatedFraction();
        currentFraction = (currentIconState == PAUSE) ? fraction : (1F - fraction);

        invalidateSelf();
      }
    });
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (!visible) return;

    drawLeftHalf(canvas, currentFraction);
    drawRightHalf(canvas, currentFraction);
  }

  @Override public void setAlpha(int alpha) {
    iconPaint.setAlpha(alpha);
    invalidateSelf();
  }

  @Override public void setColorFilter(ColorFilter colorFilter) {
    iconPaint.setColorFilter(colorFilter);
    invalidateSelf();
  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSPARENT;
  }

  @Override public void setIconState(@NonNull IconState state) {
    currentIconState = state;
    currentFraction = state == PAUSE ? FRACTION_PAUSE : FRACTION_PLAY;
    invalidateSelf();
  }

  @NonNull @Override public IconState getIconState() {
    return currentIconState;
  }

  @Override public void animateToState(@NonNull IconState nextState) {
    iconAnimator.cancel();
    currentIconState = nextState;
    iconAnimator.start();
  }

  @Override public void setColor(int color) {
    this.iconPaint.setColor(color);
    invalidateSelf();
  }

  @Override public void setVisible(boolean visible) {
    this.visible = visible;
    invalidateSelf();
  }

  @Override public void setAnimationDuration(int duration) {
    iconAnimator.setDuration(duration);
  }

  @Override public void setInterpolator(@NonNull TimeInterpolator interpolator) {
    iconAnimator.setInterpolator(interpolator);
  }

  @Override public void setAnimationListener(@Nullable Animator.AnimatorListener listener) {
    if (listener == null) {
      iconAnimator.removeAllListeners();
    } else {
      iconAnimator.addListener(listener);
    }
  }

  public final void toggle(boolean animated) {
    final IconState next = currentIconState == PAUSE ? PLAY : PAUSE;

    if (animated) animateToState(next);
    else setIconState(next);
  }

  private void drawLeftHalf(@NonNull Canvas canvas, float fraction) {
    final float cx = getCenterX();
    final float h = getHeight();
    final float w = getWidth();

    final float[] playPath = {
        cx, h / 4,      // second pair [x, y], first is always [0, 0]
        cx, h * 3 / 4,  // third pair
        0, h            // fourth pair -> makes trapeze
    };

    final float[] pausePath = {
        w / 3, 0,       // second pair [x, y], first is always [0, 0]
        w / 3, h,       // third pair
        0, h            // fourth pair
    };

    final Path res = PathBuilder.builder()
        .lineTo(playPath[0] + (pausePath[0] - playPath[0]) * fraction,
            playPath[1] + (pausePath[1] - playPath[1]) * fraction)
        .lineTo(playPath[2] + (pausePath[2] - playPath[2]) * fraction,
            playPath[3] + (pausePath[3] - playPath[3]) * fraction)
        .lineTo(playPath[4] + (pausePath[4] - playPath[4]) * fraction,
            playPath[5] + (pausePath[5] - playPath[5]) * fraction)
        .build();

    canvas.drawPath(res, iconPaint);
  }

  private void drawRightHalf(@NonNull Canvas canvas, float fraction) {
    final float cx = getCenterX();
    final float cy = getCenterY();
    final float w = getWidth();
    final float h = getHeight();

    final float[] playPath = {
        cx, h / 4,      // first pair [x, y]
        w, cy,          // second pair
        w, cy,          // third pair
        cx, h * 3 / 4   // fourth pair -> makes trapeze
    };

    final float[] pausePath = {
        w * 2 / 3, 0,   // first pair [x, y]
        w, 0,           // second pair
        w, h,           // third pair
        w * 2 / 3, h    // fourth pair
    };

    final Path res = PathBuilder.builder()
        .moveTo(playPath[0] + (pausePath[0] - playPath[0]) * fraction,
            playPath[1] + (pausePath[1] - playPath[1]) * fraction)
        .lineTo(playPath[2] + (pausePath[2] - playPath[2]) * fraction,
            playPath[3] + (pausePath[3] - playPath[3]) * fraction)
        .lineTo(playPath[4] + (pausePath[4] - playPath[4]) * fraction,
            playPath[5] + (pausePath[5] - playPath[5]) * fraction)
        .lineTo(playPath[6] + (pausePath[6] - playPath[6]) * fraction,
            playPath[7] + (pausePath[7] - playPath[7]) * fraction)
        .build();

    canvas.drawPath(res, iconPaint);
  }

  private float getCenterX() {
    return getBounds().exactCenterX();
  }

  private float getCenterY() {
    return getBounds().exactCenterY();
  }

  private int getHeight() {
    return getBounds().height();
  }

  private int getWidth() {
    return getBounds().width();
  }
}
