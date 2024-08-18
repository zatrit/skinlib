package zatrit.skins.lib.resolver;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import zatrit.skins.lib.BasePlayerTextures;
import zatrit.skins.lib.Config;
import zatrit.skins.lib.TextureType;
import zatrit.skins.lib.api.PlayerTextures;
import zatrit.skins.lib.api.Profile;
import zatrit.skins.lib.api.Resolver;
import zatrit.skins.lib.api.Texture;
import zatrit.skins.lib.data.Metadata;
import zatrit.skins.lib.texture.URLTexture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;

/**
 * Resolver for the local directory, which has a similar format to the skin
 * server, i.e. loads by name from the {@code textures/} and
 * {@code metadata/} folders.
 */
public final class LocalResolver implements Resolver {
    private final Config config;
    private final Path texturesDir;
    private final Path metadataDir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public LocalResolver(Config config, @NotNull Path directory) {
        this.config = config;
        this.texturesDir = directory.resolve("textures");
        this.metadataDir = directory.resolve("metadata");

        metadataDir.toFile().mkdirs();

        for (val type : TextureType.values()) {
            texturesDir.resolve(type.toString().toLowerCase()).toFile().mkdirs();
        }
    }

    @Override
    public @NotNull PlayerTextures resolve(@NotNull Profile profile)
        throws IOException {
        val name = profile.getName();
        val textures = new EnumMap<TextureType, Texture>(TextureType.class);

        for (val type : TextureType.values()) {
            Metadata metadata = null;
            val typeName = type.toString().toLowerCase();
            val texturesFile = texturesDir.resolve(typeName).resolve(
                name + ".png").toFile();

            if (!texturesFile.isFile()) continue;

            val url = texturesFile.toURI().toURL().toString();
            val metadataFile = metadataDir.resolve(name + ".json");

            if (metadataFile.toFile().isFile()) {
                val reader = Files.newBufferedReader(metadataFile);

                metadata = this.config.getGson().fromJson(
                    reader,
                    Metadata.class
                );
            }

            textures.put(type, new URLTexture(url, metadata));
        }

        return new BasePlayerTextures<>(textures, this.config.getLayers());
    }
}
