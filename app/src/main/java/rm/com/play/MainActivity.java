package rm.com.play;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import rm.com.youtubeplayicon.PlayIconDrawable;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final ImageView icon = (ImageView) findViewById(R.id.icon_play);
    final PlayIconDrawable play = new PlayIconDrawable();

    icon.setImageDrawable(play);
    icon.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        play.toggle(true);
      }
    });
  }
}
