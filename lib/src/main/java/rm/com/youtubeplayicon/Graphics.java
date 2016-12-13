package rm.com.youtubeplayicon;

import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by alex
 */
final class Graphics {
  private Graphics() {
  }

  static float animateValue(float start, float end, float fraction) {
    return start + (end - start) * fraction;
  }

  static void inRect(@NonNull Path into, @NonNull float[] pathData) {
    if (!into.isEmpty()) into.rewind();

    into.moveTo(pathData[0], pathData[1]); // top left
    into.lineTo(pathData[2], pathData[3]); // top right
    into.lineTo(pathData[4], pathData[5]); // bottom right
    into.lineTo(pathData[6], pathData[7]); // bottom left
  }

  static void animatePath(@NonNull float[] out, @NonNull float[] startPath,
      @NonNull float[] endPath, float fraction) {
    if (startPath.length != endPath.length || out.length != startPath.length) {
      throw new IllegalStateException("Paths should be of the same size");
    }

    final int pathSize = startPath.length;

    for (int i = 0; i < pathSize; i++) {
      out[i] = animateValue(startPath[i], endPath[i], fraction);
    }
  }
}
