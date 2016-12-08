package rm.com.play;

import android.os.Handler;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import rm.com.youtubeplayicon.IconState;
import rm.com.youtubeplayicon.PlayIconDrawable;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ImageView icon = (ImageView) findViewById(R.id.icon_play);
    final PlayIconDrawable play = new PlayIconDrawable();
    play.setInterpolator(new FastOutSlowInInterpolator());
    play.setAnimationDuration(400);

    icon.setImageDrawable(play);
    icon.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        play.toggle(true);
      }
    });


    //new Handler().postDelayed(new Runnable() {
    //  @Override public void run() {
    //    play.animateToState(IconState.PAUSE);
    //  }
    //}, 4000);
  }
}
