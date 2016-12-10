package rm.com.youtubeplayicon;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by alex
 */

public interface PlayIcon {

  /**
   * Change icon without animation
   *
   * @param state new icon state
   */
  void setIconState(@NonNull PlayIconDrawable.IconState state);

  /**
   * Return current icon state
   *
   * @return icon state
   */
  @NonNull PlayIconDrawable.IconState getIconState();

  /**
   * Animate icon to given state.
   *
   * @param nextState new icon state
   */
  void animateToState(@NonNull PlayIconDrawable.IconState nextState);

  /**
   * Set color of icon
   *
   * @param color new icon color
   */
  void setColor(int color);

  /**
   * Set visibility of icon
   *
   * @param visible new value for visibility
   */
  void setVisible(boolean visible);

  /**
   * Set duration of transformation animations
   *
   * @param duration new animation duration
   */
  void setAnimationDuration(int duration);

  /**
   * Set interpolator for transformation animations
   *
   * @param interpolator new interpolator
   */
  void setInterpolator(@NonNull TimeInterpolator interpolator);

  /**
   * Set listener for {@code MaterialMenuDrawable} animation events
   *
   * @param listener new listener or null to remove any listener
   */
  void setAnimationListener(@Nullable Animator.AnimatorListener listener);

  /**
   * Allows one to manually control the transformation of the icon
   *
   * @param fraction determinate fraction which should be in range 0F..1F
   */
  void setCurrentFraction(float fraction);
}