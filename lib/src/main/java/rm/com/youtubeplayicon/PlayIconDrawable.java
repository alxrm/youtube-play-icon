package rm.com.youtubeplayicon;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

import static rm.com.youtubeplayicon.PlayIconDrawable.IconState.PAUSE;
import static rm.com.youtubeplayicon.PlayIconDrawable.IconState.PLAY;

/**
 * Created by alex
 */
public class PlayIconDrawable extends Drawable implements PlayIcon {
  public static final long DEFAULT_ANIMATION_DURATION = 400;
  public static final int DEFAULT_COLOR = Color.WHITE;
  public static final boolean DEFAULT_VISIBLE = true;

  public enum IconState {
    PLAY, PAUSE
  }

  private static final float FRACTION_PLAY = 0F;
  private static final float FRACTION_PAUSE = 1F;
  private static final float[] TEMP_PATH_DATA = new float[8];

  private final ValueAnimator iconAnimator = ValueAnimator.ofFloat(FRACTION_PLAY, FRACTION_PAUSE);
  private final Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Path pathRight = new Path();
  private final Path pathLeft = new Path();

  private IconState currentIconState = PLAY;
  private boolean visible = DEFAULT_VISIBLE;
  private float currentFraction = FRACTION_PLAY;

  private float[] pathLeftPlay;
  private float[] pathLeftPause;
  private float[] pathRightPlay;
  private float[] pathRightPause;

  public PlayIconDrawable() {
    iconPaint.setColor(DEFAULT_COLOR);

    iconAnimator.setInterpolator(new DecelerateInterpolator(3));
    iconAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
    iconAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setCurrentFraction(valueAnimator.getAnimatedFraction());
      }
    });
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (!visible) return;

    drawPart(canvas, currentFraction, pathRightPlay, pathRightPause, TEMP_PATH_DATA, pathRight,
        iconPaint);

    drawPart(canvas, currentFraction, pathLeftPlay, pathLeftPause, TEMP_PATH_DATA, pathLeft,
        iconPaint);
  }

  @Override protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);
    initPathParts();
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
    if (isRunning()) iconAnimator.cancel();

    currentIconState = state;
    currentFraction = (state == PAUSE) ? FRACTION_PAUSE : FRACTION_PLAY;
    invalidateSelf();
  }

  @NonNull @Override public IconState getIconState() {
    return currentIconState;
  }

  @Override public void animateToState(@NonNull IconState nextState) {
    if (isRunning()) iconAnimator.cancel();

    if (nextState == PAUSE) {
      iconAnimator.start();
    } else {
      iconAnimator.reverse();
    }
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

  @Override public void setCurrentFraction(float fraction) {
    if (fraction < 0F || fraction > 1F) {
      throw new IllegalStateException("Fraction should be in a range of 0F..1F");
    }

    currentFraction = fraction;
    currentIconState = (currentFraction < 0.5F) ? PLAY : PAUSE;
    invalidateSelf();
  }

  public boolean isRunning() {
    return iconAnimator.isRunning();
  }

  public void toggle(boolean animated) {
    final IconState next = (currentIconState == PAUSE) ? PLAY : PAUSE;

    if (animated) {
      animateToState(next);
    } else {
      setIconState(next);
    }
  }

  private void drawPart(@NonNull Canvas canvas, float fraction, float[] pathPlay, float pathPause[],
      float[] animatedPath, Path pathToDraw, Paint with) {
    Graphics.animatePath(animatedPath, pathPlay, pathPause, fraction);
    Graphics.inRect(pathToDraw, animatedPath);

    canvas.drawPath(pathToDraw, with);
  }

  private void initPathParts() {
    pathLeftPlay = new float[] {            // makes trapeze
        0, 0,                               // top left [x, y]
        getCenterX(), getHeight() * 0.25F,  // top right
        getCenterX(), getHeight() * 0.75F,  // bottom right
        0, getHeight()                      // bottom left
    };

    pathLeftPause = new float[] {           // makes rectangle
        0, 0,                               // top left [x, y]
        getWidth() / 3, 0,                  // top right
        getWidth() / 3, getHeight(),        // bottom right
        0, getHeight()                      // bottom left
    };

    pathRightPlay = new float[] {           // makes triangle
        getCenterX(), getHeight() * 0.25F,  // top left [x, y]
        getWidth(), getCenterY(),           // top right
        getWidth(), getCenterY(),           // bottom right
        getCenterX(), getHeight() * 0.75F   // bottom left
    };

    pathRightPause = new float[] {          // makes rectangle
        getWidth() * 2 / 3, 0,              // top left [x, y]
        getWidth(), 0,                      // top right
        getWidth(), getHeight(),            // bottom right
        getWidth() * 2 / 3, getHeight()     // bottom left
    };
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
