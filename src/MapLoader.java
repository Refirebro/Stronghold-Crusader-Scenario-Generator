import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides the full list of Stronghold Crusader maps as {@link GameMap} instances.
 *
 * <p>Map data is defined as a static array so it stays close to the domain
 * and does not require a file-system resource at runtime.  If map data ever
 * needs to come from an external source (JSON, database, etc.) only this class
 * needs to change.
 */
public final class MapLoader {

    private static final Logger LOG = Logger.getLogger(MapLoader.class.getName());

    /**
     * Raw map data: {maxPlayers, namePL, nameEN}.
     * Sorted by maxPlayers for readability; runtime order does not matter.
     */
    private static final Object[][] MAP_DATA = {
        {2, "Armenia",                "Armenia"},
        {2, "Bród na rzece",          "River Ford"},
        {2, "Edessa",                 "Edessa"},
        {2, "Kilka centymetrów",      "A Few Centimeters"},
        {2, "Kochaj sąsiada",         "Love Thy Neighbor"},
        {2, "Przewaga wysokości",     "Height Advantage"},
        {2, "Skalista grań",          "Rocky Ridge"},
        {2, "Surowce podzielone",     "Divided Resources"},
        {2, "To niesprawiedliwe",     "That's Unfair"},
        {2, "Za ciasno na wygodę",    "Too Tight for Comfort"},
        {2, "Śpiąc z wrogiem",        "Sleeping with the Enemy"},
        {3, "Antiochia",              "Antioch"},
        {3, "Kraj kaktusa",           "Cactus Country"},
        {3, "Strati",                 "Strati"},
        {3, "Trypolis",               "Tripoli"},
        {3, "W cieniu",               "In the Shadow"},
        {4, "Bagna Cezarei",          "Marshes of Caesarea"},
        {4, "Cel",                    "Target"},
        {4, "Centrum oazy",           "Oasis Center"},
        {4, "Cyklady",                "Cyclades"},
        {4, "Głupi Jaś",              "Dumb John"},
        {4, "Patrol graniczny",       "Border Patrol"},
        {4, "Region Koryntu",         "Corinth Region"},
        {4, "Równiny zalewowe",       "Flood Plains"},
        {4, "Rzeka Halys",            "River Halys"},
        {4, "Sojusz przeciwko górze", "Alliance Against the Mountain"},
        {4, "Wzgórza Antiochii",      "Hills of Antioch"},
        {5, "Demo Krzyżowca",         "Crusader Demo"},
        {6, "Kryjówka na wzgórzu",    "Hilltop Hideout"},
        {6, "Lacus Magnus",           "Lacus Magnus"},
        {6, "Leśna oaza",             "Forest Oasis"},
        {6, "Pas zieleni",            "Green Belt"},
        {6, "Prawo dżungli",          "Law of the Jungle"},
        {6, "Przyjaciel w potrzebie", "Friend in Need"},
        {6, "Rozróba nad rzeką",      "River Brawl"},
        {6, "Rów",                    "The Trench"},
        {6, "Rzeka Arnon",            "River Arnon"},
        {6, "W kółeczku",             "Going in Circles"},
        {6, "Walka o oazę",           "Fight for the Oasis"},
        {6, "Wielkie jezioro",        "Great Lake"},
        {6, "Wydmy",                  "Sand Dunes"},
        {6, "Wydmy Nicei",            "Dunes of Nicaea"},
        {6, "Wyspa Trapesac",         "Trapesac Island"},
        {6, "Zachodnie wybrzeże",     "Western Shore"},
        {7, "Dwoje w jednym łóżku",   "Two in One Bed"},
        {7, "Jeziora Konya",          "Lakes of Konya"},
        {7, "Melos",                  "Melos"},
        {7, "Morze traw",             "Sea of Grass"},
        {7, "Prowincja Bodrum",       "Bodrum Province"},
        {7, "Tasos",                  "Thasos"},
        {7, "Zapach kokosów",         "Scent of Coconuts"},
        {7, "Ćwiartowanie",           "Quartering"},
        {8, "Bagniste piekło",        "Swampy Hell"},
        {8, "Bez odwrotu",            "No Retreat"},
        {8, "Bliskie spotkania",      "Close Encounters"},
        {8, "Dolina",                 "The Valley"},
        {8, "Kraina szczęśliwości",   "Land of Bliss"},
        {8, "Kreteński półwysep",     "Cretan Peninsula"},
        {8, "Nieprzyjazne skały",     "Hostile Rocks"},
        {8, "Ostatni posterunek",     "Last Outpost"},
        {8, "Piekło na wzgórzu",      "Hill from Hell"},
        {8, "Podmokłe ziemie",        "Wetlands"},
        {8, "Pustynna wyspa",         "Desert Island"},
        {8, "Północ-południe",        "North–South"},
        {8, "Rzeka",                  "The River"},
        {8, "Strażnicy",              "Guardians"},
        {8, "Tylos",                  "Tylos"},
        {8, "Ukryty krater",          "Hidden Crater"},
        {8, "W dziesiątkę",           "Bull's Eye"},
        {8, "Wielki Eufrat",          "Great Euphrates"},
        {8, "Z pustymi rękami",       "Empty-Handed"},
        {8, "Z wyspy na wyspę",       "Island to Island"},
        {8, "Zabójcze równiny",       "Killing Plains"},
        {8, "Zielony raj",            "Green Paradise"},
        {8, "Ściana żelaza",          "Iron Wall"},
    };

    private MapLoader() {}  // utility class

    /**
     * Builds and returns an unmodifiable list of all available maps.
     * Rows that fail validation are logged and skipped rather than crashing.
     *
     * @return unmodifiable, non-empty list of {@link GameMap}
     */
    public static List<GameMap> loadMaps() {
        List<GameMap> maps = new ArrayList<>(MAP_DATA.length);
        for (Object[] row : MAP_DATA) {
            try {
                maps.add(new GameMap((int) row[0], (String) row[1], (String) row[2]));
            } catch (Exception e) {
                LOG.warning("Skipping invalid map row: " + java.util.Arrays.toString(row) + " – " + e.getMessage());
            }
        }
        if (maps.isEmpty()) {
            throw new IllegalStateException("No valid maps could be loaded.");
        }
        return Collections.unmodifiableList(maps);
    }
}
