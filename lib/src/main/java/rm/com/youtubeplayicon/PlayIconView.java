package rm.com.youtubeplayicon;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by alex
 */
public final class PlayIconView extends ImageView implements PlayIcon {

  private PlayIconDrawable playIconDrawable = new PlayIconDrawable();

  public PlayIconView(Context context) {
    this(context, null);
  }

  public PlayIconView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PlayIconView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setImageDrawable(playIconDrawable);
  }

  @Override public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);

    if (drawable instanceof PlayIconDrawable) {
      playIconDrawable = (PlayIconDrawable) drawable;
    }
  }

  @Override public void setIconState(@NonNull PlayIconDrawable.IconState state) {
    playIconDrawable.setIconState(state);
  }

  @NonNull @Override public PlayIconDrawable.IconState getIconState() {
    return playIconDrawable.getIconState();
  }

  @Override public void animateToState(@NonNull PlayIconDrawable.IconState nextState) {
    playIconDrawable.animateToState(nextState);
  }

  @Override public void toggle(boolean animated) {
    playIconDrawable.toggle(animated);
  }

  @Override public void setStateListener(@Nullable PlayIconDrawable.StateListener listener) {
    playIconDrawable.setStateListener(listener);
  }

  @Override public void setColor(int color) {
    playIconDrawable.setColor(color);
  }

  @Override public void setVisible(boolean visible) {
    playIconDrawable.setVisible(visible);
  }

  @Override public void setAnimationDuration(int duration) {
    playIconDrawable.setAnimationDuration(duration);
  }

  @Override public void setInterpolator(@NonNull TimeInterpolator interpolator) {
    playIconDrawable.setInterpolator(interpolator);
  }

  @Override public void setAnimationListener(@Nullable Animator.AnimatorListener listener) {
    playIconDrawable.setAnimationListener(listener);
  }

  @Override public void setCurrentFraction(float fraction) {
    playIconDrawable.setCurrentFraction(fraction);
  }
}
