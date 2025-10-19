package zatrit.skins.lib.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.Texture;

@Getter
@Setter
@AllArgsConstructor
public class TypedTexture {
  private final Texture texture;
  private final TextureType type;
}
