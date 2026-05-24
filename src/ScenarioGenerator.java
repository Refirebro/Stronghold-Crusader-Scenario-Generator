import java.util.*;

public class ScenarioGenerator {
    private final List<GameMap> availableMaps;
    private final Random random = new Random();

    public ScenarioGenerator(List<GameMap> maps) {
        this.availableMaps = maps;
    }

    public GameMap rollMap() {
        return availableMaps.get(random.nextInt(availableMaps.size()));
    }

    public int rollPlayers(GameMap map) {
        int max = map.getMaxPlayers();
        return max <= 2 ? 2 : random.nextInt(max - 1) + 2;
    }

    public int rollMode() {
        // 0=normal, 1=crusade, 2=skirmish
        return random.nextInt(3);
    }

    public int rollBalance() {
        return random.nextInt(5) + 1;
    }

    public List<List<Integer>> rollTeams(int totalPlayers) {
        List<List<Integer>> finalTeams;
        do {
            Map<Integer, List<Integer>> temp = new HashMap<>();
            int count = totalPlayers == 2 ? 2 : random.nextInt(totalPlayers - 1) + 2;
            for (int p = 1; p <= totalPlayers; p++) {
                int tid = random.nextInt(count);
                temp.computeIfAbsent(tid, k -> new ArrayList<>()).add(p);
            }
            finalTeams = new ArrayList<>(temp.values());
        } while (finalTeams.size() < 2);
        return finalTeams;
    }
}
