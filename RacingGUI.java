// RacingGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class RacingGUI extends JFrame {
    private JPanel main_Panel;
    private JTabbedPane tabPane;
    private TrackDesignPanel  trackDesignPanel;
    private CustomizeHorsePanel customizeHorsePanel;
    private ViewRacePanel racePanel;
    private PanelFor_Statistics panelStats;
    private PanelForGamble bettingPanel;

    private Race activeRace;
    private ArrayList<Horse>  horses;

    private int lengthOfTrack = 20;
    private String shapeOfTrack  = "Oval";
    private String conditionOfTrack = "Dry";

    public RacingGUI() {
        super("Horse Racing Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        horses = new ArrayList<>();

        main_Panel = new JPanel(new BorderLayout());
        tabPane    = new JTabbedPane();

        trackDesignPanel = new TrackDesignPanel(this);
        customizeHorsePanel = new CustomizeHorsePanel(this);
        racePanel = new ViewRacePanel(this);
        panelStats = new PanelFor_Statistics(this);
        bettingPanel = new PanelForGamble(this);

        tabPane.addTab("Track Design", trackDesignPanel);
        tabPane.addTab("Customize the Horse", customizeHorsePanel);
        tabPane.addTab("Race View", racePanel);
        tabPane.addTab("Statistics", panelStats);
        tabPane.addTab("Betting", bettingPanel);

        main_Panel.add(tabPane, BorderLayout.CENTER);
        main_Panel.add(createButtonPanel(), BorderLayout.SOUTH);
        setContentPane(main_Panel);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton startBTN = new JButton("Start");
        startBTN.addActionListener((ActionEvent e) -> startRace());
        panel.add(startBTN);
        return panel;
    }

    public void startRace() {

        horses.forEach(horse -> horse.goBackToStart());
        activeRace = new Race(lengthOfTrack);
        horses.forEach(activeRace::addHorse);
        bettingPanel.resetBets();
        tabPane.setSelectedComponent(racePanel);
        racePanel.setRace(activeRace);
        racePanel.setOnRaceComplete(() -> {
            panelStats.updateStats(horses);
            bettingPanel.updateBetting(horses);
        });
    }


    public void addHorse(Horse horse) {
        horses.add(horse);
        racePanel.refresh();
    }

    public void removeHorse(Horse horse) {
        horses.remove(horse);
        racePanel.refresh();
    }


    public int getLengthOfTrack(){
        return lengthOfTrack;
    }
    public void setLengthOfTrack(int a){
        this.lengthOfTrack = a;
    }
    public String getShapeOfTrack(){
        return shapeOfTrack;
    }
    public void setShapeOfTrack(String shape){
        this.shapeOfTrack = shape;
    }
    public String getCondition_OfTrack(){
        return conditionOfTrack;
    }
    public void setCondition_OfTrack(String condi) {
        this.conditionOfTrack = condi;
    }

    public ArrayList<Horse> getHorses(){
        return horses;
    }

    public static void startGUI() {
        SwingUtilities.invokeLater(RacingGUI::new);
    }
}
