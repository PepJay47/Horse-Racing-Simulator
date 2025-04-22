import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This is the main GUI for the simulation.
 * @author (Jason Owusu Peprah)
 * @version 1.1
 */

public class RacingGUI extends JFrame {
    //panels for the interface
    private JPanel main_Panel;
    private JTabbedPane tabPane;
    private TrackDesignPanel trackDesignPanel;
    private CustomizeHorsePanel customizeHorsePanel;
    private ViewRacePanel racePanel;
    private PanelFor_Statistics panelStats;
    private PanelForGamble bettingPanel;

    //controlling the race;
    private Race activeRace;
    private ArrayList<Horse> horses;

    //configuration of the track
    private int lengthOfTrack =20;
    private String shapeOfTrack = "Oval";
    private String condition_OfTrack = "Dry";


    //constructor for class
    public RacingGUI() {
        super("Horse Racing Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//to stop the program when we hit close;
        setSize(800,650);
        setLocationRelativeTo(null);// bring to the centre of my screen
        initComponents();

        setVisible(true);//to make the GUI visible
    }

    private void initComponents() {
        //horse collection initialization here:

        horses = new ArrayList<>();

        //main panel border layout
        main_Panel = new JPanel();
        main_Panel.setLayout(new BorderLayout());

        tabPane = new JTabbedPane();

        //make the panels for sections
        trackDesignPanel = new TrackDesignPanel(this);
        customizeHorsePanel = new CustomizeHorsePanel(this);
        racePanel = new ViewRacePanel(this);
        panelStats = new PanelFor_Statistics(this);
        bettingPanel = new PanelForGamble(this);

        tabPane.addTab("Design of Track", trackDesignPanel);
        tabPane.addTab("Customize the Horse", customizeHorsePanel);
        tabPane.addTab("Rave View", racePanel);
        tabPane.addTab("Statistics", panelStats);
        tabPane.addTab("Betting", bettingPanel);

        main_Panel.add(tabPane, BorderLayout.CENTER);

        //adding a button to the bottom
        JPanel buttonpanil= createButtonPanel();
        main_Panel.add(buttonpanil, BorderLayout.SOUTH);

        setContentPane(main_Panel);

    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton startBTN = new JButton("Start");
        startBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRace();
            }
        });

        JButton resetBTN = new JButton("Reset");
        resetBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetRaceSim();
            }
        });

        panel.add(startBTN);
        panel.add(resetBTN);

        return panel;
    }

    public void startRace() {
        activeRace = new Race(lengthOfTrack);

         for (Horse horse : horses) {
             activeRace.addHorse(horse);
         }

         tabPane.setSelectedComponent(racePanel);

         racePanel.setRace(activeRace);

         SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {
             protected Void doInBackground() throws Exception {
                 activeRace.startRace();
                 return null;
             }

             protected void done() {
                 panelStats.updateStats(horses);

                 bettingPanel.updateBetting(horses);
             }

         };

         worker.execute();

    }


    public void resetRaceSim() {
        for (Horse horse : horses) {
            horse.goBackToStart();
        }

        racePanel.refresh();
        panelStats.refresh();
        bettingPanel.resetBets();
    }

    public void addHorse(Horse horse) {
        horses.add(horse);
        racePanel.refresh();
    }

    public void removeHorse(Horse horse) {
        horses.remove(horse);
        racePanel.refresh();
    }

    public ArrayList<Horse> getHorses() {
        return horses;
    }

    public int getLengthOfTrack() {
        return lengthOfTrack;
    }

    public void setLengthOfTrack(int lengthOfTrack) {
        this.lengthOfTrack = lengthOfTrack;
    }

    public void setShapeOfTrack(String shape){
        this.shapeOfTrack=shape;
    }

    public String getShapeOfTrack() {
        return shapeOfTrack;
    }

    public void setCondition_OfTrack(String condition){
        this.condition_OfTrack=condition;
    }

    public String getCondition_OfTrack() {
        return condition_OfTrack;
    }

    public static void startGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RacingGUI();
            }
        });
    }




}
