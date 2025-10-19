package zatrit.skins.lib.layer.awt;

import java.awt.*;
import java.awt.image.BufferedImage;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.data.TypedTexture;

/** Layer for legacy (64x32) skins to work correctly. */
public class LegacySkinLayer extends ImageLayer {
  @Override
  public BufferedImage apply(@NotNull BufferedImage input) {
    val size = input.getWidth();
    if (input.getHeight() != 32 || input.getHeight() == input.getWidth()) {
      return input;
    }

    val dest = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    val graphics = dest.createGraphics();

    graphics.drawImage(input, 0, 0, null);

    /* I'd like to apologize to anyone who understands how
    AffineTransform works. This code was created by trial
    and error, so it may be imperfect. If you know how to
    improve it, feel free to make a pull request. */
    int[][] regions = {
      {0, 20, 16, 52, 12, 12},
      {12, 20, 28, 52, 4, 12},
      {4, 16, 20, 48, 4, 4},
      {8, 16, 24, 48, 4, 4},
      {40, 20, 32, 52, 12, 12},
      {52, 20, 44, 52, 4, 12},
      {44, 16, 36, 48, 4, 4},
      {48, 16, 40, 48, 4, 4}
    };

    for (int[] r : regions) {
      drawMirrored(input, graphics, r[0], r[1], r[2], r[3], r[4], r[5]);
    }

    graphics.dispose();

    return dest;
  }

  private void drawMirrored(
      @NotNull BufferedImage src,
      @NotNull Graphics2D g,
      int sx,
      int sy,
      int dx,
      int dy,
      int w,
      int h) {
    g.drawImage(src, dx, dy, dx + w, dy + h, sx + w, sy, sx, sy + h, null);
  }

  @Override
  protected boolean accepts(@NotNull TypedTexture input) {
    return input.getType() == TextureType.SKIN;
  }
}
