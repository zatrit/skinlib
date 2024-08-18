package zatrit.skins.lib.api;

@FunctionalInterface
public interface Layer<T> {
    T apply(T input);
}
