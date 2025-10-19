package zatrit.skins.lib.resolver.capes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.resolver.CapesListResolver;

public final class LiquidBounceResolver extends CapesListResolver {
  private static final String BASE_URL = "http://capes.liquidbounce.net/api/v1/cape";
  private static final String CARRIERS_URL = BASE_URL + "/carriers";
  private static final String NAME_URL = BASE_URL + "/name/";

  public LiquidBounceResolver(Config config) {
    super(config);
  }

  @Override
  protected @NotNull Map<String, String> fetchList() throws IOException {
    val owners =
        this.config
            .getGson()
            .fromJson(
                new InputStreamReader(URI.create(CARRIERS_URL).toURL().openStream()),
                String[][].class);
    val uuidOwners = new HashMap<String, String>();

    for (val pair : owners) {
      uuidOwners.put(pair[0], pair[1]);
    }

    return uuidOwners;
  }

  @Override
  @Contract(pure = true)
  protected @NotNull String getUrl(String capeName) {
    return NAME_URL + capeName;
  }
}
