
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewRacePanel extends JPanel {
    private RacingGUI      mainGUI;
    private Race           activeRace;
    private JPanel         trackPanel;
    private JTextArea      raceInfo;
    private boolean        raceInProgress;
    private Runnable       onRaceComplete;

    public ViewRacePanel(RacingGUI gui) {
        this.mainGUI = gui;
        setLayout(new BorderLayout());
        trackPanel = createTrackPanel();
        add(new JScrollPane(trackPanel), BorderLayout.CENTER);
        add(createInformationPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTrackPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTrack((Graphics2D) g);
            }
        };
        p.setPreferredSize(new Dimension(700,400));
        p.setBackground(Color.LIGHT_GRAY);
        return p;
    }

    private JPanel createInformationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Race Info"));
        raceInfo = new JTextArea(5, 40);
        raceInfo.setEditable(false);
        panel.add(new JScrollPane(raceInfo), BorderLayout.CENTER);
        return panel;
    }


    private Color applyHorseCoatColour(String c){

        switch (c){
            case "Sorrel": return new Color(205,92,92);
            case "Gray": return Color.LIGHT_GRAY;
            case "Roan": return new Color(178,34,34);
            case "Palomino": return new Color(218,165,32);
            case "Black": return Color.BLACK;
            default: return new Color(0,0,150);


        }
    }

    private void drawTrack(Graphics2D g) {
        int w = trackPanel.getWidth(), h = trackPanel.getHeight();
        // draw background based on track condition
        switch (mainGUI.getCondition_OfTrack()) {
            case "Muddy": g.setColor(new Color(112,84,62)); break;
            case "Icy":   g.setColor(new Color(32,195,208));break;
            default:      g.setColor(new Color(210,180,180));break;
        }
        g.fillRect(0,0,w,h);

        if (activeRace == null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
            g.drawString("No Race at the Moment", w/2 -100, h/2);
            return;
        }

        int trackLen = mainGUI.getLengthOfTrack();
        ArrayList<Horse> horses = mainGUI.getHorses();
        int laneH = h / Math.max(horses.size(),1);


        g.setColor(Color.WHITE);
        for (int i=0; i<=4; i++){
            int x = (w-40)*i/4;
            g.drawLine(x,0,x,h);
            g.drawString(""+(trackLen*i/4), x+5, 15);
        }


        for (int i=0; i<horses.size(); i++){
            Horse horse = horses.get(i);

            g.setColor(i%2==0 ? new Color(240,240,240,50) : new Color(200,200,200,50));
            g.fillRect(0,i*laneH,w,laneH);


            g.setColor(Color.WHITE);
            g.drawLine(0,i*laneH,w,i*laneH);
            g.drawLine(0,(i+1)*laneH,w,(i+1)*laneH);


            int xPos = (int)((double)horse.getDistanceTravelled()/trackLen * w);
            int yMid = i*laneH + laneH/2;


            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 15));
            g.drawString(horse.getName(), 10, yMid+5);
            g.drawString("Conf: "+String.format("%.2f", horse.getConfidence()), w-180, yMid-15);


            g.setFont(new Font("Monospaced", Font.BOLD, 25));
            if (horse.hasFallen()) {
                g.setColor(Color.RED);
                g.drawString("❌", xPos, yMid);
            } else {
                Color horseCoatColour = applyHorseCoatColour(horse.getColour());
                g.setColor(horseCoatColour);
                g.drawString(String.valueOf(horse.getSymbol()),xPos,yMid);

                g.setColor(Color.GREEN);
                g.fillRect(0,yMid+10,xPos,3);
            }
        }


        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(w-20,0,w-20,h);
        int size = 10;
        for (int y=0; y<h; y+=size*2) {
            g.fillRect(w-20, y, size, size);
            g.fillRect(w-20, y+size*1, size, size);
        }
        g.setFont(new Font("Monospaced", Font.BOLD, 15));
        g.drawString("Finish", w-60, 20);
    }


    public void setRace(Race race) {
        this.activeRace     = race;
        this.raceInProgress = true;
        infoUpdate();


        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (activeRace.isFinished()) {
                    ((Timer)e.getSource()).stop();
                    raceInProgress = false;
                    infoUpdate();
                    if (onRaceComplete != null)
                        onRaceComplete.run();
                } else {
                    activeRace.step();
                    trackPanel.repaint();
                    infoUpdate();
                }
            }
        }).start();
    }


    public void setOnRaceComplete(Runnable r) {
        this.onRaceComplete = r;
    }


    public void refresh() {
        if (raceInProgress) {
            trackPanel.repaint();
            infoUpdate();
        }
    }

    private void infoUpdate() {
        if (activeRace == null) {
            raceInfo.setText("No race is in progress");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Track: ").append(mainGUI.getShapeOfTrack())
                .append(" (").append(mainGUI.getLengthOfTrack()).append(")\n\n");


        ArrayList<Horse> winners = new ArrayList<>();
        boolean allFallen = true;
        for (Horse h : mainGUI.getHorses()) {
            if (h.getDistanceTravelled() >= mainGUI.getLengthOfTrack())
                winners.add(h);
            if (!h.hasFallen()) allFallen = false;
        }

        if (!winners.isEmpty()) {
            sb.append("Race finished!");
            if (winners.size()==1) {
                sb.append("\nWinner: ").append(winners.get(0).getName());
            } else {
                sb.append("\nTie between:");
                for (Horse w : winners) sb.append("\n  - ").append(w.getName());
            }
        } else if (allFallen) {
            sb.append("All horses have fallen!");
        } else {
            sb.append("Race in progress...");
        }

        raceInfo.setText(sb.toString());
    }
}
