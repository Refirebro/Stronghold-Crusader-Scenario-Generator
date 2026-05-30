import java.util.List;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Apply the system Look-and-Feel before any Swing component is created.
 *   <li>Load maps, construct the generator, and hand everything off to the GUI.
 *   <li>All GUI work is dispatched on the Event Dispatch Thread (EDT).
 * </ul>
 */
public final class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {}

    public static void main(String[] args) {
        applySystemLaf();

        List<GameMap>       maps      = MapLoader.loadMaps();
        ScenarioGenerator   generator = new ScenarioGenerator(maps);

        SwingUtilities.invokeLater(() -> {
            ScenarioGUI gui = new ScenarioGUI(generator);
            gui.setVisible(true);
        });
    }

    private static void applySystemLaf() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.warning("Could not set system L&F: " + e.getMessage());
        }
    }
}
