import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Stateless randomisation logic for all scenario categories.
 *
 * <p>The generator holds the immutable map list and a single {@link Random}
 * instance.  All roll-methods are pure functions of the RNG – they carry no
 * side effects and never mutate caller state.
 */
public final class ScenarioGenerator {

    /** Number of distinct game modes (Normal / Crusade / Skirmish). */
    public static final int MODE_COUNT = 3;

    /** Minimum and maximum balance values (inclusive). */
    public static final int BALANCE_MIN = 1;
    public static final int BALANCE_MAX = 5;

    private final List<GameMap> availableMaps;
    private final Random        random;

    /**
     * @param maps non-null, non-empty list of maps to draw from
     * @throws IllegalArgumentException if the list is empty
     */
    public ScenarioGenerator(List<GameMap> maps) {
        Objects.requireNonNull(maps, "maps must not be null");
        if (maps.isEmpty()) throw new IllegalArgumentException("At least one map is required");
        // defensive copy so callers cannot mutate the list
        this.availableMaps = Collections.unmodifiableList(new ArrayList<>(maps));
        this.random        = new Random();
    }

    /** Returns a uniformly random map from the pool. */
    public GameMap rollMap() {
        return availableMaps.get(random.nextInt(availableMaps.size()));
    }

    /**
     * Returns a random player count in [2, map.maxPlayers].
     * Always returns at least 2 regardless of maxPlayers.
     */
    public int rollPlayers(GameMap map) {
        Objects.requireNonNull(map, "map must not be null");
        int max = map.getMaxPlayers();
        // nextInt(n) gives [0, n-1]; +2 shifts to [2, max]
        return max <= 2 ? 2 : 2 + random.nextInt(max - 1);
    }

    /**
     * Returns a random mode index: 0 = Normal, 1 = Crusade, 2 = Skirmish.
     */
    public int rollMode() {
        return random.nextInt(MODE_COUNT);
    }

    /**
     * Returns a random balance value in [{@value #BALANCE_MIN}, {@value #BALANCE_MAX}].
     */
    public int rollBalance() {
        return BALANCE_MIN + random.nextInt(BALANCE_MAX - BALANCE_MIN + 1);
    }

    /**
     * Assigns each player to a team randomly.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Draw a team count {@code k} in [2, totalPlayers].
     *   <li>Shuffle player list and assign sequentially so every team gets at
     *       least one member, then distribute the rest randomly.
     *   <li>If the result produces only one team (FFA special case that the
     *       caller handles) the loop retries until at least two teams form.
     * </ol>
     *
     * @param totalPlayers number of players; must be &ge; 2
     * @return list of teams (each team is a sorted list of 1-based player numbers)
     * @throws IllegalArgumentException if totalPlayers &lt; 2
     */
    public List<List<Integer>> rollTeams(int totalPlayers) {
        if (totalPlayers < 2) throw new IllegalArgumentException("Need at least 2 players, got: " + totalPlayers);

        // Build a shuffled player list: [1 .. totalPlayers]
        List<Integer> players = new ArrayList<>(totalPlayers);
        for (int i = 1; i <= totalPlayers; i++) players.add(i);

        List<List<Integer>> teams;
        do {
            Collections.shuffle(players, random);
            // k teams, 2 <= k <= totalPlayers
            int k = 2 + random.nextInt(totalPlayers - 1);

            Map<Integer, List<Integer>> bucket = new HashMap<>();
            // Guarantee every team has at least one player
            for (int t = 0; t < k; t++) {
                bucket.computeIfAbsent(t, id -> new ArrayList<>()).add(players.get(t));
            }
            // Distribute remaining players randomly
            for (int p = k; p < totalPlayers; p++) {
                int t = random.nextInt(k);
                bucket.get(t).add(players.get(p));
            }
            teams = new ArrayList<>(bucket.values());
            for (List<Integer> team : teams) Collections.sort(team);
        } while (teams.size() < 2);  // safety: should always pass, but guards edge cases

        return teams;
    }
}
