package net.zatrit.skins.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.zatrit.skins.lib.api.Layer;
import net.zatrit.skins.lib.api.cache.CacheProvider;
import net.zatrit.skins.lib.data.TypedTexture;
import net.zatrit.skins.lib.util.AnyCaseEnumDeserializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
@Setter
@NoArgsConstructor
public final class Config {
    private @Nullable CacheProvider cacheProvider;
    private @NotNull Collection<Layer<TypedTexture>> layers = Collections.emptyList();
    // Very cool Gson that parses TextureType ignoring case
    private @NotNull Gson gson = new GsonBuilder().registerTypeAdapter(
        TextureType.class,
        new AnyCaseEnumDeserializer<>(TextureType.values())
    ).create();
    private @NotNull Executor executor = Executors.newCachedThreadPool();
}
