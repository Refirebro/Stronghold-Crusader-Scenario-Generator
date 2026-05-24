public class GameMap {
    private final int maxPlayers;
    private final String namePL;
    private final String nameEN;

    public GameMap(int maxPlayers, String namePL, String nameEN) {
        this.maxPlayers = maxPlayers;
        this.namePL = namePL;
        this.nameEN = nameEN;
    }

    public int getMaxPlayers() { return maxPlayers; }
    public String getNamePL()  { return namePL; }
    public String getNameEN()  { return nameEN; }

    public String getName(String lang) {
        return "en".equals(lang) ? nameEN : namePL;
    }
}
