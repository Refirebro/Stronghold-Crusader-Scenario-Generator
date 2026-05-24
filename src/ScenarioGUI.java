import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class ScenarioGUI extends JFrame {

    // ── Paleta kolorów: pergamin + ciemne drewno ──────────────────────────
    private static final Color WOOD_DARK   = new Color(0x2C, 0x1A, 0x08);
    private static final Color WOOD_MID    = new Color(0x3E, 0x26, 0x0E);
    private static final Color WOOD_LIGHT  = new Color(0x5A, 0x38, 0x18);
    private static final Color PARCH_DARK  = new Color(0xC8, 0xAA, 0x72);
    private static final Color PARCH_MID   = new Color(0xD8, 0xBE, 0x8A);
    private static final Color PARCH_LIGHT = new Color(0xEC, 0xD8, 0xA8);
    private static final Color INK_DARK    = new Color(0x2A, 0x18, 0x06);
    private static final Color INK_MID     = new Color(0x4A, 0x30, 0x10);
    private static final Color INK_LIGHT   = new Color(0x6A, 0x48, 0x20);
    private static final Color GOLD_BRIGHT = new Color(0xD4, 0xA0, 0x30);
    private static final Color GOLD_DIM    = new Color(0x8A, 0x64, 0x20);
    private static final Color RED_ACCENT  = new Color(0x8B, 0x1A, 0x1A);
    private static final Color RIVET       = new Color(0x55, 0x42, 0x28);

    private static final Color[] TEAM_COLORS = {
        new Color(0xC0, 0x70, 0x20), new Color(0x20, 0x70, 0xA0),
        new Color(0x30, 0x8A, 0x30), new Color(0xA0, 0x28, 0x28),
        new Color(0x70, 0x30, 0x90), new Color(0x20, 0x7A, 0x6A),
        new Color(0x8A, 0x7A, 0x20), new Color(0x7A, 0x40, 0x20),
    };

    // ── Stan ──────────────────────────────────────────────────────────────
    private String lang = Lang.PL;
    private GameMap selectedMap   = null;
    private int totalPlayers      = 0;
    private int lastModeIdx       = -1;
    private int lastBalance       = -1;
    private List<List<Integer>> lastTeams = null;

    private final ScenarioGenerator generator;

    // ── Referencje UI ────────────────────────────────────────────────────
    private JLabel  lblTitleSub, lblSelectLang, lblFooter;
    private JButton btnFlagPL, btnFlagUK;
    private RowPanel rowMap, rowPlayers, rowMode, rowBalance, rowTeams;

    // ═════════════════════════════════════════════════════════════════════
    public ScenarioGUI(ScenarioGenerator generator) {
        this.generator = generator;
        setTitle("Stronghold Crusader – Scenario Randomizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        try {
            InputStream is = getClass().getResourceAsStream("/twierdza.ico");
            if (is != null) { BufferedImage img = ImageIO.read(is); if (img != null) setIconImage(img); }
        } catch (Exception ignored) {}
        buildUI();
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    // ── Główny layout ────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                paintWood((Graphics2D) g, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);
        root.setBorder(new EmptyBorder(10, 12, 10, 12));
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildRows(),   BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    // ── Malowanie drewnianego tła ─────────────────────────────────────────
    private void paintWood(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, WOOD_MID, w, h, WOOD_DARK);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        // słoje drewna
        g2.setColor(new Color(0xFF, 0xD0, 0x80, 12));
        for (int i = 0; i < h; i += 6) {
            g2.drawLine(0, i, w, i + 3);
        }
        // ramka wewnętrzna
        g2.setColor(new Color(0xFF, 0xC0, 0x60, 60));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(4, 4, w - 9, h - 9);
        g2.setColor(new Color(0x00, 0x00, 0x00, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(6, 6, w - 13, h - 13);
    }

    // ── HEADER ────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        // Pergaminowe tło headera
        JPanel header = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                paintParchment((Graphics2D) g, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(new CompoundBorder(
            new MatteBorder(3, 3, 3, 3, GOLD_DIM),
            new EmptyBorder(12, 16, 12, 16)
        ));

        GridBagConstraints gc = new GridBagConstraints();

        // lewy spacer
        JPanel spacer = new JPanel(); spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(130, 10));
        gc.gridx=0; gc.gridy=0; gc.weightx=0; gc.anchor=GridBagConstraints.WEST;
        header.add(spacer, gc);

        // tytuł (środek)
        JPanel titlePanel = new JPanel(); titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("✦  Stronghold Crusader  ✦", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 26));
        lblTitle.setForeground(INK_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTitleSub = new JLabel(Lang.titleSub(lang), SwingConstants.CENTER);
        lblTitleSub.setFont(new Font("Serif", Font.ITALIC, 15));
        lblTitleSub.setForeground(INK_MID);
        lblTitleSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(lblTitleSub);

        gc.gridx=1; gc.weightx=1; gc.fill=GridBagConstraints.HORIZONTAL; gc.anchor=GridBagConstraints.CENTER;
        header.add(titlePanel, gc);

        // prawy panel: język
        JPanel langPanel = buildLangPanel();
        langPanel.setPreferredSize(new Dimension(130, 60));
        gc.gridx=2; gc.weightx=0; gc.fill=GridBagConstraints.NONE; gc.anchor=GridBagConstraints.EAST;
        header.add(langPanel, gc);

        updateFlagBorders();
        return header;
    }

    private JPanel buildLangPanel() {
        // Drewniany panel z flagami
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WOOD_DARK);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(GOLD_DIM);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(6, 8, 6, 8));

        lblSelectLang = new JLabel(Lang.selectLang(lang), SwingConstants.CENTER);
        lblSelectLang.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblSelectLang.setForeground(PARCH_MID);
        lblSelectLang.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnFlagPL = createFlagButton("/pl.png", "Polski",  () -> setLang(Lang.PL));
        btnFlagUK = createFlagButton("/uk.png", "English", () -> setLang(Lang.EN));

        JPanel flagRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        flagRow.setOpaque(false);
        flagRow.add(btnFlagPL);
        flagRow.add(btnFlagUK);
        flagRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblSelectLang);
        panel.add(Box.createVerticalStrut(5));
        panel.add(flagRow);
        return panel;
    }

    private JButton createFlagButton(String path, String tip, Runnable action) {
        JButton btn = new JButton();
        btn.setToolTipText(tip);
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(GOLD_DIM, 2, true));
        btn.setPreferredSize(new Dimension(44, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) btn.setIcon(new ImageIcon(ImageIO.read(is).getScaledInstance(40, 26, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {}
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ── WIERSZE ───────────────────────────────────────────────────────────
    private JPanel buildRows() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 0, 6, 0));

        rowMap     = new RowPanel("1", Lang.lblMap(lang),     Lang.phMap(lang),     Lang.btnMap(lang));
        rowPlayers = new RowPanel("2", Lang.lblPlayers(lang), Lang.phPlayers(lang), Lang.btnPlayers(lang));
        rowMode    = new RowPanel("3", Lang.lblMode(lang),    Lang.phPending(lang), Lang.btnMode(lang));
        rowBalance = new RowPanel("4", Lang.lblBalance(lang), Lang.phPending(lang), Lang.btnBalance(lang));
        rowTeams   = new RowPanel("5", Lang.lblTeams(lang),   Lang.phTeams(lang),   Lang.btnTeams(lang));

        rowPlayers.setButtonEnabled(false);
        rowTeams.setButtonEnabled(false);

        rowMap.setAction(this::doRollMap);
        rowPlayers.setAction(this::doRollPlayers);
        rowMode.setAction(this::doRollMode);
        rowBalance.setAction(this::doRollBalance);
        rowTeams.setAction(this::doRollTeams);

        panel.add(rowMap);     panel.add(Box.createVerticalStrut(6));
        panel.add(rowPlayers); panel.add(Box.createVerticalStrut(10));
        panel.add(makeDivider());
        panel.add(Box.createVerticalStrut(10));
        panel.add(rowMode);    panel.add(Box.createVerticalStrut(6));
        panel.add(rowBalance); panel.add(Box.createVerticalStrut(10));
        panel.add(makeDivider());
        panel.add(Box.createVerticalStrut(10));
        panel.add(rowTeams);
        return panel;
    }

    private JPanel makeDivider() {
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                int y = getHeight() / 2;
                GradientPaint gp = new GradientPaint(0, y, WOOD_MID, getWidth()/2, y, GOLD_DIM);
                GradientPaint gp2 = new GradientPaint(getWidth()/2, y, GOLD_DIM, getWidth(), y, WOOD_MID);
                g2.setPaint(gp);  g2.fillRect(0, y-1, getWidth()/2, 2);
                g2.setPaint(gp2); g2.fillRect(getWidth()/2, y-1, getWidth()/2, 2);
            }
        };
        d.setOpaque(false);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        d.setPreferredSize(new Dimension(100, 8));
        return d;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                paintParchment(g2, getWidth(), getHeight());
                g2.setColor(GOLD_DIM);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 10, 6, 10));
        lblFooter = new JLabel(Lang.footer(lang), SwingConstants.CENTER);
        lblFooter.setFont(new Font("Serif", Font.ITALIC, 13));
        lblFooter.setForeground(INK_MID);
        p.add(lblFooter);
        return p;
    }

    // ── Malowanie pergaminu ───────────────────────────────────────────────
    private void paintParchment(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, PARCH_LIGHT, w, h, PARCH_DARK);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        // faktura
        g2.setColor(new Color(0x60, 0x40, 0x10, 18));
        for (int i = 0; i < h; i += 4) g2.drawLine(0, i, w, i);
        // cień brzegów
        for (int i = 0; i < 8; i++) {
            int alpha = (8 - i) * 10;
            g2.setColor(new Color(0x30, 0x18, 0x00, alpha));
            g2.drawRect(i, i, w - 2*i - 1, h - 2*i - 1);
        }
    }

    // ── AKCJE ─────────────────────────────────────────────────────────────
    private void doRollMap() {
        selectedMap = generator.rollMap(); totalPlayers = 0; lastTeams = null;
        rowMap.setValue(selectedMap.getName(lang) + "  (" + Lang.maxPlayers(lang) + ": " + selectedMap.getMaxPlayers() + ")", false);
        rowPlayers.setValue("◀  " + Lang.btnPlayers(lang), true);
        rowPlayers.setButtonEnabled(true);
        rowTeams.setValue(Lang.phTeams(lang), true);
        rowTeams.setButtonEnabled(false);
    }

    private void doRollPlayers() {
        if (selectedMap == null) return;
        totalPlayers = generator.rollPlayers(selectedMap); lastTeams = null;
        rowPlayers.setValue(totalPlayers + "  (" + Lang.youAre(lang) + ")", false);
        rowTeams.setValue("◀  " + Lang.btnTeams(lang), true);
        rowTeams.setButtonEnabled(true);
    }

    private void doRollMode() {
        lastModeIdx = generator.rollMode();
        rowMode.setValue(Lang.modeName(lang, lastModeIdx).toUpperCase(), false);
    }

    private void doRollBalance() {
        lastBalance = generator.rollBalance();
        String dots = "●".repeat(lastBalance) + "○".repeat(5 - lastBalance);
        rowBalance.setValue(dots + "  " + lastBalance + Lang.balanceDesc(lang, lastBalance), false);
    }

    private void doRollTeams() {
        if (totalPlayers < 2) return;
        lastTeams = generator.rollTeams(totalPlayers);
        renderTeams();
    }

    private void renderTeams() {
        if (lastTeams == null) return;
        if (lastTeams.size() >= totalPlayers) { rowTeams.setValue(Lang.ffa(lang), false); return; }
        StringBuilder sb = new StringBuilder("<html><body style='font-size:14pt'>");
        for (int i = 0; i < lastTeams.size(); i++) {
            if (i > 0) sb.append("<font color='#6A4820'>  |  </font>");
            Color c = TEAM_COLORS[i % TEAM_COLORS.length];
            String hex = String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
            String players = lastTeams.get(i).stream().map(String::valueOf).collect(Collectors.joining(","));
            sb.append("<font color='").append(hex).append("'><b>").append(players).append("</b></font>");
        }
        sb.append("</body></html>");
        rowTeams.setValueHTML(sb.toString());
    }

    // ── JĘZYK ─────────────────────────────────────────────────────────────
    private void setLang(String newLang) {
        this.lang = newLang; updateFlagBorders();
        lblTitleSub.setText(Lang.titleSub(lang));
        lblSelectLang.setText(Lang.selectLang(lang));
        lblFooter.setText(Lang.footer(lang));
        rowMap.setLabel(Lang.lblMap(lang));         rowMap.setButtonText(Lang.btnMap(lang));
        rowPlayers.setLabel(Lang.lblPlayers(lang)); rowPlayers.setButtonText(Lang.btnPlayers(lang));
        rowMode.setLabel(Lang.lblMode(lang));       rowMode.setButtonText(Lang.btnMode(lang));
        rowBalance.setLabel(Lang.lblBalance(lang)); rowBalance.setButtonText(Lang.btnBalance(lang));
        rowTeams.setLabel(Lang.lblTeams(lang));     rowTeams.setButtonText(Lang.btnTeams(lang));

        if (selectedMap != null)
            rowMap.setValue(selectedMap.getName(lang) + "  (" + Lang.maxPlayers(lang) + ": " + selectedMap.getMaxPlayers() + ")", false);
        else rowMap.setValue(Lang.phMap(lang), true);

        if (totalPlayers > 0) rowPlayers.setValue(totalPlayers + "  (" + Lang.youAre(lang) + ")", false);
        else if (selectedMap != null) rowPlayers.setValue("◀  " + Lang.btnPlayers(lang), true);
        else rowPlayers.setValue(Lang.phPlayers(lang), true);

        if (lastModeIdx >= 0) rowMode.setValue(Lang.modeName(lang, lastModeIdx).toUpperCase(), false);
        else rowMode.setValue(Lang.phPending(lang), true);

        if (lastBalance > 0) {
            String dots = "●".repeat(lastBalance) + "○".repeat(5 - lastBalance);
            rowBalance.setValue(dots + "  " + lastBalance + Lang.balanceDesc(lang, lastBalance), false);
        } else rowBalance.setValue(Lang.phPending(lang), true);

        if (lastTeams != null) renderTeams();
        else if (totalPlayers > 0) rowTeams.setValue("◀  " + Lang.btnTeams(lang), true);
        else rowTeams.setValue(Lang.phTeams(lang), true);
    }

    private void updateFlagBorders() {
        btnFlagPL.setBorder(new LineBorder(Lang.PL.equals(lang) ? GOLD_BRIGHT : GOLD_DIM, 2, true));
        btnFlagUK.setBorder(new LineBorder(Lang.EN.equals(lang) ? GOLD_BRIGHT : GOLD_DIM, 2, true));
    }

    // ═════════════════════════════════════════════════════════════════════
    // Wiersz: pergamin z nitami w rogach + metalowy przycisk
    // ═════════════════════════════════════════════════════════════════════
    private class RowPanel extends JPanel {
        private final JLabel  lblLabel;
        private final JLabel  lblValue;
        private final JButton btnRoll;

        RowPanel(String num, String label, String placeholder, String btnText) {
            setLayout(new BorderLayout(14, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
            setMinimumSize(new Dimension(200, 68));
            setPreferredSize(new Dimension(620, 68));
            setBorder(new EmptyBorder(0, 0, 0, 0));

            // ─ pergaminowy panel wewnętrzny ─
            JPanel parch = new JPanel(new BorderLayout(14, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    paintParchment((Graphics2D) g, getWidth(), getHeight());
                    // nity w rogach
                    paintRivet((Graphics2D) g, 8, 8);
                    paintRivet((Graphics2D) g, getWidth()-14, 8);
                    paintRivet((Graphics2D) g, 8, getHeight()-14);
                    paintRivet((Graphics2D) g, getWidth()-14, getHeight()-14);
                }
            };
            parch.setOpaque(false);
            parch.setBorder(new CompoundBorder(
                new LineBorder(GOLD_DIM, 2),
                new EmptyBorder(8, 20, 8, 12)
            ));

            // lewa kolumna: numer + etykieta
            JPanel left = new JPanel(new GridLayout(2, 1, 0, 0));
            left.setOpaque(false);
            left.setPreferredSize(new Dimension(115, 1));

            JLabel lblNum = new JLabel(num + ".");
            lblNum.setFont(new Font("Serif", Font.BOLD, 12));
            lblNum.setForeground(INK_LIGHT);
            lblNum.setOpaque(false);

            lblLabel = new JLabel(label);
            lblLabel.setFont(new Font("Serif", Font.BOLD, 16));
            lblLabel.setForeground(INK_DARK);
            lblLabel.setOpaque(false);

            left.add(lblNum); left.add(lblLabel);

            // środek: wartość
            lblValue = new JLabel(placeholder);
            lblValue.setFont(new Font("Serif", Font.ITALIC, 16));
            lblValue.setForeground(INK_LIGHT);
            lblValue.setOpaque(false);

            // przycisk metalowy
            btnRoll = buildMetalButton(btnText);

            parch.add(left,     BorderLayout.WEST);
            parch.add(lblValue, BorderLayout.CENTER);
            parch.add(btnRoll,  BorderLayout.EAST);
            add(parch, BorderLayout.CENTER);
        }

        private void paintRivet(Graphics2D g2, int x, int y) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(RIVET);
            g2.fillOval(x, y, 8, 8);
            g2.setColor(new Color(0xFF, 0xE0, 0x80, 120));
            g2.fillOval(x+1, y+1, 3, 3);
            g2.setColor(INK_DARK);
            g2.setStroke(new BasicStroke(1f));
            g2.drawOval(x, y, 8, 8);
        }

        private JButton buildMetalButton(String text) {
            JButton btn = new JButton(text) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // metalowe tło
                    Color top = isEnabled()
                        ? (getModel().isPressed() ? new Color(0x2A,0x1A,0x08) : new Color(0x50,0x38,0x18))
                        : new Color(0x30,0x22,0x10);
                    Color bot = isEnabled()
                        ? (getModel().isPressed() ? new Color(0x50,0x38,0x18) : new Color(0x28,0x18,0x08))
                        : new Color(0x22,0x16,0x08);
                    GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bot);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    // highlight
                    g2.setColor(new Color(0xFF, 0xD0, 0x60, isEnabled() ? 60 : 20));
                    g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/2, 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
                @Override protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isEnabled() ? GOLD_DIM : INK_LIGHT);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 8, 8);
                    g2.dispose();
                }
            };
            btn.setFont(new Font("Serif", Font.BOLD, 14));
            btn.setForeground(isEnabled() ? PARCH_LIGHT : INK_LIGHT);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(150, 38));
            btn.setBorder(new EmptyBorder(6, 16, 6, 16));

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (btn.isEnabled()) { btn.setForeground(Color.WHITE); btn.repaint(); }
                }
                public void mouseExited(MouseEvent e) {
                    btn.setForeground(btn.isEnabled() ? PARCH_LIGHT : INK_LIGHT); btn.repaint();
                }
            });
            return btn;
        }

        void setAction(Runnable r)       { btnRoll.addActionListener(e -> r.run()); }
        void setButtonEnabled(boolean b) {
            btnRoll.setEnabled(b);
            btnRoll.setForeground(b ? PARCH_LIGHT : INK_LIGHT);
        }
        void setLabel(String t)      { lblLabel.setText(t); }
        void setButtonText(String t) { btnRoll.setText(t); }

        void setValue(String text, boolean ph) {
            lblValue.setText(text);
            lblValue.setFont(ph ? new Font("Serif", Font.ITALIC, 15) : new Font("Serif", Font.BOLD | Font.ITALIC, 16));
            lblValue.setForeground(ph ? INK_LIGHT : INK_DARK);
        }
        void setValueHTML(String html) {
            lblValue.setText(html);
            lblValue.setFont(new Font("Serif", Font.BOLD, 16));
        }
    }
}
