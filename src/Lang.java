public class Lang {
    public static final String PL = "pl";
    public static final String EN = "en";

    // ---- Labels / Etykiety statyczne ----
    public static String titleSub(String l)      { return EN.equals(l) ? "Scenario Randomizer"       : "Losowanie Scenariusza"; }
    public static String selectLang(String l)    { return EN.equals(l) ? "Select language"            : "Wybierz język"; }

    public static String lblMap(String l)        { return EN.equals(l) ? "Map"                        : "Mapa"; }
    public static String lblPlayers(String l)    { return EN.equals(l) ? "Players"                    : "Gracze"; }
    public static String lblMode(String l)       { return EN.equals(l) ? "Game Mode"                  : "Tryb gry"; }
    public static String lblBalance(String l)    { return EN.equals(l) ? "Balance"                    : "Balans"; }
    public static String lblTeams(String l)      { return EN.equals(l) ? "Alliances"                  : "Sojusze"; }

    public static String btnMap(String l)        { return EN.equals(l) ? "Roll Map"                   : "Losuj Mapę"; }
    public static String btnPlayers(String l)    { return EN.equals(l) ? "Roll Players"               : "Losuj Graczy"; }
    public static String btnMode(String l)       { return EN.equals(l) ? "Roll Mode"                  : "Losuj Tryb"; }
    public static String btnBalance(String l)    { return EN.equals(l) ? "Roll Balance"               : "Losuj Balans"; }
    public static String btnTeams(String l)      { return EN.equals(l) ? "Roll Teams"                 : "Losuj Drużyny"; }

    public static String phMap(String l)         { return EN.equals(l) ? "Click the button →"         : "Kliknij przycisk →"; }
    public static String phPlayers(String l)     { return EN.equals(l) ? "Roll map first"             : "Najpierw wylosuj mapę"; }
    public static String phTeams(String l)       { return EN.equals(l) ? "Determine players first"    : "Najpierw ustal graczy"; }
    public static String phPending(String l)     { return EN.equals(l) ? "???"                        : "???"; }

    public static String maxPlayers(String l)    { return EN.equals(l) ? "Max players"                : "Maks. graczy"; }
    public static String youAre(String l)        { return EN.equals(l) ? "You are player #1"          : "w tym Ty jako nr 1"; }

    public static String modeName(String l, int idx) {
        String[][] names = {
            {"Gra normalna", "Normal Game"},
            {"Krucjata",     "Crusade"},
            {"Potyczka",     "Skirmish"}
        };
        return names[idx][EN.equals(l) ? 1 : 0];
    }

    public static String balanceDesc(String l, int b) {
        if (b <= 2) return EN.equals(l) ? " – Easy for you"  : " – Łatwo dla Ciebie";
        if (b >= 4) return EN.equals(l) ? " – Powerful AI"   : " – Potężne AI";
        return "";
    }

    public static String ffa(String l)          { return EN.equals(l) ? "Free for All (no alliances)" : "Każdy na każdego (Brak sojuszy)"; }
    public static String footer(String l)       { return EN.equals(l) ? "You are player #1  ·  Roll each category independently"
                                                                       : "Gracz nr 1 to Ty  ·  Losuj każdą kategorię niezależnie"; }
}
