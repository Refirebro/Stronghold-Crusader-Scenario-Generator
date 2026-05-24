import java.util.List;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set appearance / Ustaw wygląd systemu operacyjnego
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        List<GameMap> maps = MapLoader.loadMaps();
        ScenarioGenerator generator = new ScenarioGenerator(maps);

        SwingUtilities.invokeLater(() -> {
            ScenarioGUI gui = new ScenarioGUI(generator);
            gui.setVisible(true);
        });
    }
}
