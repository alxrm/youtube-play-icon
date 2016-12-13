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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import static rm.com.youtubeplayicon.PlayIconDrawable.IconState.PAUSE;
import static rm.com.youtubeplayicon.PlayIconDrawable.IconState.PLAY;

/**
 * Created by alex
 */
public class PlayIconDrawable extends Drawable implements PlayIcon {
  public static final int DEFAULT_COLOR = Color.WHITE;
  public static final boolean DEFAULT_VISIBLE = true;
  public static final int DEFAULT_ANIMATION_DURATION = 400;
  public static final TimeInterpolator DEFAULT_ANIMATION_INTERPOLATOR =
      new AccelerateDecelerateInterpolator();

  public interface StateListener {
    void onStateChanged(IconState state);
  }

  public enum IconState {
    PLAY, PAUSE
  }

  private static final float FRACTION_PLAY = 0F;
  private static final float FRACTION_PAUSE = 1F;
  private static final float[] ANIMATED_PATH_CONTAINER = new float[8];

  private final ValueAnimator iconAnimator = ValueAnimator.ofFloat(FRACTION_PLAY, FRACTION_PAUSE);
  private final Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Path pathRight = new Path();
  private final Path pathLeft = new Path();

  private IconState currentIconState = PLAY;
  private StateListener stateListener = null;
  private boolean visible = DEFAULT_VISIBLE;
  private float currentFraction = FRACTION_PLAY;

  private float[] pathLeftPlay;
  private float[] pathLeftPause;
  private float[] pathRightPlay;
  private float[] pathRightPause;

  public PlayIconDrawable() {
    iconPaint.setColor(DEFAULT_COLOR);

    iconAnimator.setInterpolator(DEFAULT_ANIMATION_INTERPOLATOR);
    iconAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
    iconAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setCurrentFraction(valueAnimator.getAnimatedFraction());
      }
    });
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (!visible) return;

    drawPart(canvas, currentFraction, pathRightPlay, pathRightPause, ANIMATED_PATH_CONTAINER,
        pathRight, iconPaint);

    drawPart(canvas, currentFraction, pathLeftPlay, pathLeftPause, ANIMATED_PATH_CONTAINER,
        pathLeft, iconPaint);
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

    currentFraction = (state == PAUSE) ? FRACTION_PAUSE : FRACTION_PLAY;
    updateIconState(state);
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

  @Override public void setColor(@ColorInt int color) {
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
    updateIconState((currentFraction < 0.5F) ? PLAY : PAUSE);
    invalidateSelf();
  }

  public boolean isRunning() {
    return iconAnimator.isRunning();
  }

  @Override public void toggle(boolean animated) {
    final IconState next = (currentIconState == PAUSE) ? PLAY : PAUSE;

    if (animated) {
      animateToState(next);
    } else {
      setIconState(next);
    }
  }

  @Override public void setStateListener(@Nullable StateListener listener) {
    this.stateListener = listener;
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

  private void updateIconState(IconState state) {
    if (stateListener != null && currentIconState != state) {
      stateListener.onStateChanged(state);
    }

    currentIconState = state;
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

  public static PlayIconBuilder builder() {
    return new PlayIconBuilder();
  }

  public static final class PlayIconBuilder {
    private IconState state = IconState.PLAY;
    private int color = PlayIconDrawable.DEFAULT_COLOR;
    private int duration = PlayIconDrawable.DEFAULT_ANIMATION_DURATION;
    private TimeInterpolator interpolator = PlayIconDrawable.DEFAULT_ANIMATION_INTERPOLATOR;
    private Animator.AnimatorListener animatorListener = null;
    private StateListener stateListener = null;

    public PlayIconBuilder withInitialState(@NonNull IconState state) {
      this.state = state;
      return this;
    }

    public PlayIconBuilder withColor(@ColorInt int color) {
      this.color = color;
      return this;
    }

    public PlayIconBuilder withDuration(int duration) {
      this.duration = duration;
      return this;
    }

    public PlayIconBuilder withInterpolator(@NonNull TimeInterpolator interpolator) {
      this.interpolator = interpolator;
      return this;
    }

    public PlayIconBuilder withAnimatorListener(@Nullable Animator.AnimatorListener listener) {
      this.animatorListener = listener;
      return this;
    }

    public PlayIconBuilder withStateListener(@Nullable StateListener listener) {
      this.stateListener = listener;
      return this;
    }

    public PlayIconDrawable into(@NonNull ImageView target) {
      final PlayIconDrawable result = build();
      target.setImageDrawable(result);
      return result;
    }

    public PlayIconDrawable build() {
      final PlayIconDrawable result = new PlayIconDrawable();
      result.setIconState(state);
      result.setAnimationDuration(duration);
      result.setColor(color);
      result.setInterpolator(interpolator);
      result.setAnimationListener(animatorListener);
      result.setStateListener(stateListener);
      return result;
    }
  }
}
