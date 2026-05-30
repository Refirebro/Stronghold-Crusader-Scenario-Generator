import java.util.Objects;

/**
 * Immutable value object representing a playable map.
 * Holds player capacity and localised names.
 */
public final class GameMap {

    private final int    maxPlayers;
    private final String namePL;
    private final String nameEN;

    /**
     * @param maxPlayers maximum number of players supported (must be >= 2)
     * @param namePL     Polish display name (non-null, non-blank)
     * @param nameEN     English display name (non-null, non-blank)
     */
    public GameMap(int maxPlayers, String namePL, String nameEN) {
        if (maxPlayers < 2) {
            throw new IllegalArgumentException("maxPlayers must be >= 2, got: " + maxPlayers);
        }
        this.maxPlayers = maxPlayers;
        this.namePL     = Objects.requireNonNull(namePL, "namePL").strip();
        this.nameEN     = Objects.requireNonNull(nameEN, "nameEN").strip();
        if (this.namePL.isEmpty()) throw new IllegalArgumentException("namePL must not be blank");
        if (this.nameEN.isEmpty()) throw new IllegalArgumentException("nameEN must not be blank");
    }

    public int    getMaxPlayers() { return maxPlayers; }
    public String getNamePL()     { return namePL; }
    public String getNameEN()     { return nameEN; }

    /** Returns the localised name for the given language code. */
    public String getName(String lang) {
        return Lang.EN.equals(lang) ? nameEN : namePL;
    }

    @Override
    public String toString() {
        return "GameMap{maxPlayers=" + maxPlayers + ", namePL='" + namePL + "', nameEN='" + nameEN + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameMap gm)) return false;
        return maxPlayers == gm.maxPlayers && namePL.equals(gm.namePL) && nameEN.equals(gm.nameEN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxPlayers, namePL, nameEN);
    }
}
