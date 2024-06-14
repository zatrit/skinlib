package net.zatrit.skins.lib;

import lombok.AllArgsConstructor;
import lombok.val;
import net.zatrit.skins.lib.api.PlayerTextures;
import net.zatrit.skins.lib.api.Profile;
import net.zatrit.skins.lib.api.Resolver;
import net.zatrit.skins.lib.data.TypedTexture;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.zatrit.skins.lib.util.SneakyLambda.sneaky;

@AllArgsConstructor
public class TextureDispatcher {
    private final Config config;

    /* There are more comments than the rest of the code,
     * because this is a very complex implementation. */

    /**
     * Asynchronously fetches loaders and numbers them from specific resolvers.
     */
    public Stream<CompletableFuture<PlayerTextures>> resolveAsync(
        @NotNull List<Resolver> resolvers, Profile profile) {
        return resolvers.stream().map(resolver -> CompletableFuture.supplyAsync(
            /* This function may throw an exception,
             * but it's a CompletableFuture, so
             * an exception won't crash the game. */
            sneaky(() -> resolver.resolve(profile)), this.config.getExecutor()));
    }

    /**
     * Asynchronously fetches textures from a list of numbered futures returning loaders.
     * <p>
     * Use {@link #resolveAsync} to obtain futures list.
     */
    public CompletableFuture<TypedTexture[]> fetchTexturesAsync(
        @NotNull Stream<CompletableFuture<PlayerTextures>> futures) {

        return CompletableFuture.supplyAsync(() -> {
            val textures = futures.map(CompletableFuture::join)
                .filter(Objects::nonNull).collect(Collectors.toList());

            val typedTextures = new ArrayList<TypedTexture>(TextureType.values().length);
            for (val type : TextureType.values()) {
                TypedTexture typedTexture = null;

                for (val texture : textures) {
                    if (texture.hasTexture(type)) {
                        typedTexture = texture.getTexture(type);
                        break;
                    }
                }

                if (typedTexture != null) {
                    typedTextures.add(typedTexture);
                }
            }

            return typedTextures.toArray(new TypedTexture[0]);
        }, config.getExecutor());
    }
}
