import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.Timer;

public class ViewRacePanel extends JPanel {

    private RacingGUI mainGUI;

    private Race activeRace;
    private JPanel trackPanel;
    private JTextArea raceInfo;

    private boolean raceProgress;

    public ViewRacePanel(RacingGUI gui) {
        this.mainGUI = gui;
        setLayout(new BorderLayout());

        trackPanel = createTrackPanel();
        add(new JScrollPane(trackPanel), BorderLayout.CENTER);

        JPanel panelInformation = createInformationPanel();
        add(panelInformation, BorderLayout.SOUTH);

    }

    private JPanel createTrackPanel() {
        JPanel panel = new JPanel(){
          @Override
          protected void paintComponent(Graphics ghc){
              super.paintComponent(ghc);
              drawTrack(ghc);
          }
        };
        panel.setPreferredSize(new Dimension(700,400));
        panel.setBackground(Color.LIGHT_GRAY);
        return panel;
    }

    private JPanel createInformationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Race Info"));

        raceInfo = new JTextArea(5, 40);
        raceInfo.setEditable(false);

        panel.add(new JScrollPane(raceInfo), BorderLayout.CENTER);
        return panel;
    }

    private void drawTrack(Graphics ghc){
        Graphics2D ghc2d = (Graphics2D) ghc;

        ghc2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = trackPanel.getWidth();
        int height = trackPanel.getHeight();

        //background
        String conditionTrack = mainGUI.getCondition_OfTrack();
        Color colourOFTrack = null;

        switch(conditionTrack){
            case "Muddy":
                colourOFTrack = new Color(112,84,62);
                break;
            case "Icy":
                colourOFTrack = new Color(32,195,208);
                break;
            case "Dry":
                colourOFTrack = new Color(210,180,180);
                break;
        }
        ghc2d.setColor(colourOFTrack);
        ghc2d.fillRect(0, 0, width, height);

        if(activeRace ==null){
            ghc2d.setColor(Color.BLACK);
            ghc2d.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
            ghc2d.drawString("No Race at the Moment", width/2-100, height/2);
            return;
        }

        int track_Length = mainGUI.getLengthOfTrack();
        ArrayList<Horse> horses = mainGUI.getHorses();

        int lane_height = height/ Math.max(horses.size(), 1);

        //track markers indication for the distance
        ghc2d.setColor(Color.WHITE);
        for(int i =0; i<=4; i++){
            int a = (width-40)*i/4;
            ghc2d.drawLine(a,0,a,height);
            ghc2d.drawString(Integer.toString(track_Length*i/4),a+5,15);
        }



        for(int i = 0; i < horses.size(); i++){
            Horse horse = horses.get(i);

            if(i%2 ==0){
                ghc2d.setColor(new Color(240, 240, 240,50));
            }else {
                ghc2d.setColor(new Color(200, 200, 200, 50));
            }
            ghc2d.fillRect(0,i*lane_height,width,lane_height);

            //draw borders for lane


            //draw the lane
            ghc2d.setColor(Color.WHITE);
            ghc2d.drawLine(0,i*lane_height,width,i*lane_height);
            ghc2d.drawLine(0,(i+1)*lane_height, width, (i+1)*lane_height);

            //draw the horse
            int X_horse =(int) ((double)horse.getDistanceTravelled()/track_Length * width);
            int Y_horse = i*lane_height + lane_height/2;

            //horse name drawn
            ghc2d.setColor(Color.BLACK);
            ghc2d.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
            ghc2d.drawString(horse.getName(), 10, Y_horse+5);

            //confidence
            ghc2d.drawString("Confidence: "+String.format("%.2f", horse.getConfidence()),width-180, Y_horse-15);

            //draw the horse symbol
            ghc2d.setFont(new Font("JetBrains Mono", Font.BOLD, 25));
            if(horse.hasFallen()){
                ghc2d.setColor(Color.RED);
                ghc2d.drawString("❌", X_horse, Y_horse);

                ghc2d.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
                ghc2d.drawString("Fallen", X_horse+30, Y_horse);
            }else{
                ghc2d.setColor(new Color(0,0,150));
                ghc2d.drawString(String.valueOf(horse.getSymbol()), X_horse, Y_horse);

                ghc2d.setColor(Color.GREEN);
                ghc2d.fillRect(0,Y_horse+10, X_horse,3);
            }
        }

        ghc2d.setColor(Color.BLACK);
        ghc2d.setStroke(new BasicStroke(2));
        ghc2d.drawLine(width -20,0,width -20,height);

        int size_of_checker = 10;
        for(int sc=0; sc<height; sc+=size_of_checker*2){
            for(int j=0; j<2; j++){
                ghc2d.fillRect(width-20,sc+j*size_of_checker,size_of_checker,size_of_checker);
            }
        }
        ghc2d.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        ghc2d.drawString("Finish Line", width - 60, 20);

//
//
    }

    public void setRace(Race race){
        this.activeRace = race;
        this.raceProgress = true;
        infoUpdate();

        Timer timerReload = new Timer(100,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(raceProgress){
                    trackPanel.repaint();
                    infoUpdate();
                }else{
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timerReload.start();
    }

    private void infoUpdate(){
        if(activeRace == null){
            raceInfo.setText("No race is in progress");
            return;
        }

        StringBuffer information = new StringBuffer();
        information.append("Track: ").append(mainGUI.getShapeOfTrack()).append(" (").append(mainGUI.getLengthOfTrack()).append(")\n");
        information.append("Length: ").append(mainGUI.getLengthOfTrack()).append(" units\n\n");

        ArrayList<Horse> horses = mainGUI.getHorses();
        ArrayList<Horse> winners = new ArrayList<>();
        boolean allHaveFallen = true;

        for(Horse horse : horses){
            if(horse.getDistanceTravelled()>= mainGUI.getLengthOfTrack()){
                winners.add(horse);
            }
            if(!horse.hasFallen()){
                allHaveFallen = false;
            }
        }
        if(!winners.isEmpty()){
            information.append("The race is finished\n");

            if(winners.size()==1){
                information.append("Winner:").append(winners.get(0).getName()).append("\n");
            }else {
                information.append("Okay now we have a tie between:\n");
                for(Horse winner : winners){
                    information.append("-- ").append(winner.getName()).append("\n");
                }
            }
            raceProgress = false;

        } else if (allHaveFallen) {
            information.append("The Race is finished\n");
            information.append("All horse have fallen\n");
            raceProgress = false;
        }else{
            information.append("Race is in progress..\n");
        }
        raceInfo.setText(information.toString());
    }

    public void refresh(){
        if(raceProgress){
        trackPanel.repaint();
        infoUpdate();
        }
    }

}
