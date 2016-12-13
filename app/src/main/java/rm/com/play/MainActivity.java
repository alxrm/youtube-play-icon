package rm.com.play;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import rm.com.youtubeplayicon.PlayIconDrawable;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ImageView iconView = (ImageView) findViewById(R.id.icon_play);
    final PlayIconDrawable play = PlayIconDrawable.builder()
        .withColor(Color.WHITE)
        .withInterpolator(new FastOutSlowInInterpolator())
        .withDuration(300)
        .withInitialState(PlayIconDrawable.IconState.PAUSE)
        .withStateListener(new PlayIconDrawable.StateListener() {
          @Override public void onStateChanged(PlayIconDrawable.IconState state) {
            Log.d("MainActivity", "onStateChanged: " + state);
          }
        })
        .into(iconView);

    iconView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        play.toggle(true);
      }
    });
  }
}
