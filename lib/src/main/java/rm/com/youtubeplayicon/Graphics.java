package rm.com.youtubeplayicon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by alex
 */
final class Graphics {
  private Graphics() {
  }

  static Paint paintOf(int color) {
    final Paint result = new Paint(Paint.ANTI_ALIAS_FLAG);
    result.setColor(color);
    return result;
  }

  static Paint paintOf() {
    return paintOf(Color.WHITE);
  }
}
