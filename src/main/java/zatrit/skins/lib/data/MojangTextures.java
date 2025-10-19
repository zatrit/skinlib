package zatrit.skins.lib.data;

import java.util.EnumMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.texture.URLTexture;

/** Container for textures provided by Mojang-like APIs. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiStatus.Internal
public class MojangTextures {
  private Map<TextureType, URLTexture> textures = new EnumMap<>(TextureType.class);
}
