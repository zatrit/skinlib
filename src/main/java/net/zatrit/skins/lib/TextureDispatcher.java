package net.zatrit.skins.lib;

import lombok.AllArgsConstructor;
import lombok.val;
import net.zatrit.skins.lib.api.PlayerTextures;
import net.zatrit.skins.lib.api.Profile;
import net.zatrit.skins.lib.api.Resolver;
import net.zatrit.skins.lib.data.TypedTexture;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
            val pairs = futures.map(CompletableFuture::join).filter(
                Objects::nonNull).sorted().collect(
                Collectors.toList());

            //noinspection DataFlowIssue,ConstantValue,SuspiciousToArrayCall,OptionalGetWithoutIsPresent
            return Arrays.stream(TextureType.values()).parallel()
                .map(type -> pairs.stream().map(t -> t.getTexture(type))
                    .findFirst().get()).map(Objects::nonNull).toArray(
                    TypedTexture[]::new);
        }, config.getExecutor());
    }
}
