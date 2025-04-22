import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelFor_Statistics extends JPanel {
    private RacingGUI mainGUI;

    private JTable statisticsTable;
    private DefaultTableModel TModel;
    private JComboBox<String> horseSelect;
    private JPanel history_Panel;
    private JTextArea history_Area;


    private JTextArea trackStatisticsArea;

    private HashMap<String, ArrayList<RaceResult>> horseHistory;
    private HashMap<String, TrackRecord> track_records;


    public PanelFor_Statistics(RacingGUI gui) {

        this.mainGUI = gui;
        setLayout(new BorderLayout());

        horseHistory = new HashMap<>();
        track_records = new HashMap<>();

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel horseStatisticsPanel = createHorseStatisticsPanel();
        tabbedPane.add("Horse Statistics", horseStatisticsPanel);

        JPanel trackStatisticsPanel = createTrackStatisticsPanel();
        tabbedPane.add("Track Statistics", trackStatisticsPanel);

        JPanel comparePanel = createComparePanel();
        tabbedPane.add("Compare Horses", comparePanel);

        add(tabbedPane, BorderLayout.CENTER);


    }

    private class RaceResult{
        String trackType;
        String trackCondition;
        int position;
        double averageSpeed;
        boolean hasFallen;
        double confidenceChange;
        boolean isWinner;


    }

    private class TrackRecord{
        double bestTime=0;
        String recordHolder="None";
        double totalTime = 0;
        int raceCount = 0;
        double averageTime = 0;



    }

    private JPanel createHorseStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSelector.add(new JLabel("Choose Horse"));

        horseSelect = new JComboBox<>();
        updateSelector();
        panelSelector.add(horseSelect);

        panel.add(panelSelector, BorderLayout.NORTH);

        String[] nameOFColumns ={"Metric", "Value"};
        TModel = new DefaultTableModel(nameOFColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;

            }
        };
        statisticsTable = new JTable(TModel);
        JScrollPane scrollPane = new JScrollPane(statisticsTable);

        history_Panel = new JPanel(new BorderLayout());
        history_Panel.setBorder(BorderFactory.createTitledBorder("History"));

        history_Area = new JTextArea(10, 40);
        history_Area.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(history_Area);
        history_Panel.add(scrollPane1, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane, history_Panel);
        split.setDividerLocation(200);

        panel.add(split, BorderLayout.CENTER);

        horseSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                horseStatsUpdate();
            }
        });
        return panel;
    }

    private JPanel createTrackStatisticsPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        trackStatisticsArea = new JTextArea(20, 40);
        trackStatisticsArea.setEditable(false);

        panel.add(new JScrollPane(trackStatisticsArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createComparePanel(){
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSelector.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panelSelector.add(new JLabel("Horse 1"));
        JComboBox<String> selector_horse1 = new JComboBox<>();
        comboBoxUpdate(selector_horse1);
        panelSelector.add(selector_horse1);

        panelSelector.add(Box.createHorizontalStrut(20));//add space essentially

        panelSelector.add(new JLabel("Horse 2"));
        JComboBox<String> selector_horse2 = new JComboBox<>();
        comboBoxUpdate(selector_horse2);
        panelSelector.add(selector_horse2);

        panelSelector.add(Box.createHorizontalStrut(20));
        JButton compareBTN = new JButton("Compare");
        panelSelector.add(compareBTN);

        panel.add(panelSelector, BorderLayout.NORTH);

        JPanel contentP = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10,10,5,10);

        //adding title
        JLabel tittle_panel = new JLabel("Horse Compare:");
        tittle_panel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        contentP.add(tittle_panel, c);

        //instructions
        c.gridy++;
        c.insets = new Insets(15,10,5,10);
        JLabel instructions = new JLabel("Select two Horses and click compare");
        contentP.add(instructions, c);

        //feature list
        c.gridy++;
        c.insets = new Insets(20,10,5,10);
        JLabel features = new JLabel("Comparison Features:");
        contentP.add(features, c);

        c.gridy++;
        c.insets = new Insets(5,10,5,10);
        contentP.add(new JLabel(". Rate of Win and Race Stats"),c);

        c.gridy++;
        contentP.add(new JLabel(".Speed and Performance"),c);

        c.gridy++;
        contentP.add(new JLabel(".Confidence Trends"),c);

        panel.add(contentP, BorderLayout.CENTER);

        compareBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String horseA = (String) selector_horse1.getSelectedItem();
                String horseB = (String) selector_horse2.getSelectedItem();

                if(horseA == null || horseB == null){
                    JOptionPane.showMessageDialog(panel, "Two horses need to compare", "Selection need", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(horseA.equals(horseB)){
                    JOptionPane.showMessageDialog(panel, "Different Horses need to compare", "Selected same horses", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(panel, "Comnparison will come later in another version","Feature not available", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;

    }

    private void comboBoxUpdate(JComboBox<String> comboBox){
        comboBox.removeAllItems();

        for(Horse horse: mainGUI.getHorses()){
            comboBox.addItem(horse.getName());
        }
    }

    private void updateSelector(){
        horseSelect.removeAllItems();

        for(Horse horse: mainGUI.getHorses()){
            horseSelect.addItem(horse.getName());
        }
    }

    private void horseStatsUpdate(){
        TModel.setRowCount(0);
        history_Area.setText("");

        String selected_Name_Horse = (String) horseSelect.getSelectedItem();
        if(selected_Name_Horse == null){
            return;
        }

        Horse selectedHorseObj = null;
        for(Horse horse: mainGUI.getHorses()){
            if(horse.getName().equals(selected_Name_Horse)){
                selectedHorseObj = horse;
                break;
            }
        }
        if(selectedHorseObj == null){
            return;
        }

        TModel.addRow(new Object[]{"Name", selectedHorseObj.getName()});
        TModel.addRow(new Object[]{"Symbol", String.valueOf(selectedHorseObj.getSymbol())});
        TModel.addRow(new Object[]{"Current Confidence", String.format("%.2f", selectedHorseObj.getConfidence())});

        ArrayList<RaceResult> History = horseHistory.get(selected_Name_Horse);

        if(History != null && !History.isEmpty()){
            int raceCount = History.size();
            int winsCounter=0;
            int fallCounter=0;
            double speedSum = 0;

            StringBuilder txtHistory = new StringBuilder();
            txtHistory.append("History of Race:\n\n");

            for(int i=0; i< History.size(); i++){
                RaceResult result = History.get(i);

                if(result.isWinner){
                    winsCounter++;
                }

                if(result.hasFallen){
                    fallCounter++;
                }
                speedSum += result.averageSpeed;

                txtHistory.append("Race ").append(i+1).append(":\n");
                txtHistory.append(" Track: ").append(result.trackType).append(" (").append(result.trackCondition).append(")\n");
                txtHistory.append("Position: ").append(result.position).append("\n");
                txtHistory.append("Average Speed: ").append(String.format("%.2f", result.averageSpeed)).append(" units/s\n");
                txtHistory.append("Fallen: ").append(result.hasFallen ? "Yes" : "No").append("\n");
                txtHistory.append(" Confidence Changed to: ").append(String.format("%.2f", result.confidenceChange)).append("\n");
            }

            double rateOfWin = (double) winsCounter / raceCount;
            double averageSpeed = speedSum / raceCount;

            TModel.addRow(new Object[]{"Sum of Races", String.valueOf(raceCount)});
            TModel.addRow(new Object[]{"Num of Wins", winsCounter});
            TModel.addRow(new Object[]{"Num of Falls", fallCounter});
            TModel.addRow(new Object[]{"Rate of Win", String.format("%.2f", rateOfWin)});
            TModel.addRow(new Object[]{"Average Speed", String.format("%.2f", averageSpeed)});

            history_Area.setText(txtHistory.toString());

        }else {
            TModel.addRow(new Object[]{"Total Number of Races",0});
            TModel.addRow(new Object[]{"Total Number of Wins",0});
            TModel.addRow(new Object[]{"Win Rate","0.00"});
        }
    }

    public void updateTrackStatistics(){
        StringBuilder builder = new StringBuilder();
        builder.append("Records of Track\n");
        builder.append("--------------\n");

        if(track_records.isEmpty()){
            builder.append("No records found\n");
        }else{
            for(Map.Entry<String, TrackRecord>entry: track_records.entrySet()){
                String trackKey = entry.getKey();
                TrackRecord rcrd = entry.getValue();

                String[] trackParts = trackKey.split(":");
                String trackShape = trackParts[0];
                String trackCondition = trackParts[1];

                builder.append(trackShape).append(" Track (").append(trackCondition).append(")\n");
                builder.append("Best Time: ").append(String.format("%.2f", rcrd.bestTime)).append(" seconds\n");
                builder.append("Record Holder: ").append(rcrd.recordHolder).append("\n");
                builder.append("Average Time: ").append(String.format("%.2f", rcrd.averageTime)).append(" seconds\n");
                builder.append("Total Num of Races: ").append(rcrd.raceCount).append("\n");

            }


        }
        trackStatisticsArea.setText(builder.toString());

    }

    public void updateStats(ArrayList<Horse> horses){
        if(horses == null || horses.isEmpty()){
            return;
        }

        ArrayList<Horse> winners_of_race = new ArrayList<>();
        for(Horse horse: horses){
            if(horse.getDistanceTravelled() >= mainGUI.getLengthOfTrack()){
                winners_of_race.add(horse);
            }
        }

        String trackKey = mainGUI.getShapeOfTrack() + ":" + mainGUI.getCondition_OfTrack();
        TrackRecord nwRecord = track_records.getOrDefault(trackKey, new TrackRecord());

        double raceTime = 10.0 + Math.random()*10.0;

        if(nwRecord.bestTime == 0 || raceTime < nwRecord.bestTime){
            nwRecord.bestTime = raceTime;
            if(!winners_of_race.isEmpty()){
                nwRecord.recordHolder = winners_of_race.get(0).getName();
            }
        }

        nwRecord.totalTime += raceTime;
        nwRecord.raceCount++;
        nwRecord.averageTime = nwRecord.totalTime / nwRecord.raceCount;

        track_records.put(trackKey, nwRecord);


        for(Horse horse: horses){
            String horse_name = horse.getName();
            ArrayList<RaceResult> horseHistoryList = horseHistory.getOrDefault(horse_name, new ArrayList<>());

            RaceResult ResultI = new RaceResult();
            ResultI.trackType = mainGUI.getShapeOfTrack();
            ResultI.trackCondition = mainGUI.getCondition_OfTrack();
            ResultI.isWinner = winners_of_race.contains(horse);
            ResultI.hasFallen = horse.hasFallen();


            int position = 1;
            for(Horse otherHorse: horses){
                if(otherHorse != horse && otherHorse.getDistanceTravelled() >= horse.getDistanceTravelled()){
                    position++;
                }
            }
            ResultI.position = position;

            ResultI.averageSpeed = horse.getDistanceTravelled() / raceTime;

            ResultI.confidenceChange = 0.0;

            horseHistoryList.add(ResultI);
            horseHistory.put(horse_name, horseHistoryList);
        }
        updateSelector();
        horseStatsUpdate();
        updateTrackStatistics();

    }

    public void refresh(){
        updateSelector();
        horseStatsUpdate();
        updateTrackStatistics();
    }



}
