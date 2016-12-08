package rm.com.youtubeplayicon;

import android.graphics.Path;

/**
 * Created by alex
 */
final class PathBuilder {
  private final Path result;

  static PathBuilder builder() {
    return new PathBuilder();
  }

  PathBuilder() {
    this.result = new Path();
  }

  final PathBuilder lineTo(float x, float y) {
    result.lineTo(x, y);
    return this;
  }

  final PathBuilder moveTo(float x, float y) {
    result.moveTo(x, y);
    return this;
  }

  final Path build() {
    return result;
  }
}