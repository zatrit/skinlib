package zatrit.skins.lib.data;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import zatrit.skins.lib.TextureType;

/** An API response from the Minecraft Capes server containing player-related fields. */
@Getter
@AllArgsConstructor
@ApiStatus.Internal
public class MCCapesResponse {
  private boolean animatedCape;
  private Map<TextureType, String> textures;
}
