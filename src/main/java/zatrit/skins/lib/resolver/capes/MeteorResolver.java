package zatrit.skins.lib.resolver.capes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lombok.Cleanup;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.resolver.CapesListResolver;

public final class MeteorResolver extends CapesListResolver {
  private static final String BASE_URL = "https://meteorclient.com/api";
  private static final String OWNERS_URL = BASE_URL + "/capeowners";
  private static final String CAPES_URL = BASE_URL + "/capes";
  private Map<String, String> capes;

  public MeteorResolver(Config config) {
    super(config);
  }

  private static @NotNull Map<String, String> parseTable(Reader reader) {
    @Cleanup val scanner = new Scanner(reader);
    val map = new HashMap<String, String>();

    while (scanner.hasNextLine()) {
      val pair = scanner.nextLine().split(" ");
      map.put(pair[0], pair[1]);
    }

    return map;
  }

  @Override
  protected @NotNull Map<String, String> fetchList() throws IOException {
    capes = parseTable(new InputStreamReader(URI.create(CAPES_URL).toURL().openStream()));

    val owners = parseTable(new InputStreamReader(URI.create(OWNERS_URL).toURL().openStream()));
    val uuidOwners = new HashMap<String, String>();

    for (val pair : owners.entrySet()) {
      uuidOwners.put(pair.getKey().replace("-", ""), pair.getValue());
    }

    return uuidOwners;
  }

  @Override
  protected String getUrl(String capeName) {
    return capes.get(capeName);
  }
}
