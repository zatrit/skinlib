package zatrit.skins.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zatrit.skins.lib.api.Cache;
import zatrit.skins.lib.api.Layer;
import zatrit.skins.lib.data.TypedTexture;
import zatrit.skins.lib.util.AnyCaseEnumDeserializer;

@Getter
@Setter
@NoArgsConstructor
public final class Config {
  private @Nullable Cache cache;
  private @NotNull Collection<Layer<TypedTexture>> layers = Collections.emptyList();
  // Very cool Gson that parses TextureType ignoring case
  private @NotNull Gson gson =
      new GsonBuilder()
          .registerTypeAdapter(
              TextureType.class, new AnyCaseEnumDeserializer<>(TextureType.values()))
          .create();
  private @NotNull Executor executor = Executors.newCachedThreadPool();
}
