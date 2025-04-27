import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class PanelForGamble extends JPanel {

    private RacingGUI mainGUI;

    private JTable table_odds;
    private DefaultTableModel table_OddsModel;
    private JComboBox<String> selector_ForHorse;
    private JTextField textField_GambleAmount;
    private JLabel label_PossibleWinnings;
    private JButton button_MakeBet;


    private JTable historyOfBetsTable;
    private DefaultTableModel historyOfBets_TModel;

    private double inSimulatorBalance =1000.0;
    private JLabel label_balance;

    private HashMap<String, Double> horseOddsMap;
    private ArrayList<BettingRecord> historyOfBets;

    private class BettingRecord{
        String horses_name;
        int race_number;
        double gamble_amount;
        double gamble_odds;
        String  result;
        double gamble_winnings;

        public String getHorses_name() {
            return horses_name;
        }

        public void setHorses_name(String horses_name) {
            this.horses_name = horses_name;
        }

        public int getRace_number() {
            return race_number;
        }

        public void setRace_number(int race_number) {
            this.race_number = race_number;
        }

        public double getGamble_amount() {
            return gamble_amount;
        }

        public void setGamble_amount(double gamble_amount) {
            this.gamble_amount = gamble_amount;
        }

        public double getGamble_odds() {
            return gamble_odds;
        }

        public void setGamble_odds(double gamble_odds) {
            this.gamble_odds = gamble_odds;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public double getGamble_winnings() {
            return gamble_winnings;
        }

        public void setGamble_winnings(double gamble_winnings) {
            this.gamble_winnings = gamble_winnings;
        }
    }

    //constructor for class
    public PanelForGamble(RacingGUI gui){
        this.mainGUI = gui;
        setLayout(new BorderLayout());

        horseOddsMap = new HashMap<>();
        historyOfBets = new ArrayList<>();

        JSplitPane sPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel panel_betting = createBettingPanel();

        sPane.setTopComponent(panel_betting);

        JPanel panel_history = createHistoryPanel();
        sPane.setBottomComponent(panel_history);

        sPane.setDividerLocation(350);
        add(sPane, BorderLayout.CENTER);

        calcOddss();


    }

    private JPanel createBettingPanel(){
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panel_odds = new JPanel(new BorderLayout());
        panel_odds.setBorder(BorderFactory.createTitledBorder("Odds(As of now)"));

        String[] name_of_coloumns = {"Horse Name", "Odds"};
        table_OddsModel = new DefaultTableModel(name_of_coloumns,0){
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
        };

        table_odds = new JTable(table_OddsModel);
        JScrollPane oddsScroll = new JScrollPane(table_odds);
        panel_odds.add(oddsScroll, BorderLayout.CENTER);


        JPanel panel_bettingForm = new JPanel(new GridLayout());
        panel_bettingForm.setBorder(BorderFactory.createTitledBorder("Place Your Bet"));

        GridBagConstraints ct = new GridBagConstraints();
        ct.insets = new Insets(5,5,5,5);
        ct.anchor = GridBagConstraints.WEST;

        //display the balance
        ct.gridx = 0;
        ct.gridy = 0;
        ct.gridwidth = 2;
        label_balance = new JLabel("Simulator Balance: £" + String.format("%.2f", inSimulatorBalance));
        label_balance.setFont(new Font("Times New Roman", Font.BOLD, 15));
        panel_bettingForm.add(label_balance, ct);


        //selector for horse
        ct.gridx = 0;
        ct.gridy = 1;
        ct.gridwidth = 1;
        panel_bettingForm.add(new JLabel("Select Horse"));

        ct.gridx = 1;
        selector_ForHorse = new JComboBox<>();
        selector_HorseUpdate();
        panel_bettingForm.add(selector_ForHorse, ct);

        //gamble amount
        ct.gridx = 0;
        ct.gridy = 2;
        panel_bettingForm.add(new JLabel("Gamble Amount"));

        ct.gridx = 1;
        textField_GambleAmount = new JTextField(10);
        textField_GambleAmount.setText("10.00");
        panel_bettingForm.add(textField_GambleAmount, ct);


        ct.gridx = 0;
        ct.gridy = 3;
        panel_bettingForm.add(new JLabel("Possible Winnings"));

        ct.gridx =1;
        label_PossibleWinnings = new JLabel("£0.00");
        panel_bettingForm.add(label_PossibleWinnings, ct);


        ct.gridx = 0;
        ct.gridy = 4;
        ct.gridwidth = 2;
        ct.anchor = GridBagConstraints.CENTER;
        button_MakeBet = new JButton("Make Bet");
        panel_bettingForm.add(button_MakeBet, ct);

        setEventListeners();

        JPanel panel_Top = new JPanel(new BorderLayout());
        panel_Top.add(panel_odds, BorderLayout.CENTER);
        panel_Top.add(panel_bettingForm, BorderLayout.EAST);
        panel.add(panel_Top, BorderLayout.CENTER);


        JButton button_refresh = new JButton("Odds Refresh");
        button_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcOddss();
            }
        });
        panel.add(button_refresh, BorderLayout.SOUTH);
        return panel;

    }

    private JPanel  createHistoryPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("History of Bets"));


        String[] name_of_columns ={"Race","Betting Horse", "Gamble Amount", "Odds", "Result", "Winnings"};
        historyOfBets_TModel = new DefaultTableModel(name_of_columns,0){
            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
        };
        historyOfBetsTable = new JTable(historyOfBets_TModel);
        JScrollPane historyScroll = new JScrollPane(historyOfBetsTable);
        panel.add(historyScroll, BorderLayout.CENTER);


        JPanel panel_stats = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label_stats = new JLabel("Bets Rate Success: 0 | Total Earnings: £0.00");
        panel_stats.add(label_stats);
        panel.add(panel_stats, BorderLayout.SOUTH);

        return panel;
    }

    public void setEventListeners(){
        selector_ForHorse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWinPotential();
            }
        });

        textField_GambleAmount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateWinPotential();
            }
        });

        textField_GambleAmount.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateWinPotential();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateWinPotential();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateWinPotential();
            }
        });

        button_MakeBet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeBet();
            }
        });
    }

    private void selector_HorseUpdate(){
        selector_ForHorse.removeAllItems();

        for(Horse horses : mainGUI.getHorses()){
            selector_ForHorse.addItem(horses.getName());
        }
    }


    public void calcOddss(){
        horseOddsMap.clear();
        table_OddsModel.setRowCount(0);

        ArrayList<Horse> horses = mainGUI.getHorses();
        if(horses.isEmpty()){
            return;
        }


        String trackCond = mainGUI.getCondition_OfTrack();
        String trackShape = mainGUI.getShapeOfTrack();


        for(Horse horse : horses){
            double confidence = horse.getConfidence();

            double defaultOdds = 1.0 / confidence;


            double adjustedOdds = defaultOdds;

            if(trackCond.equals("Muddy")){

                adjustedOdds *= 1.2;
            }else if(trackCond.equals("Icy")){

                adjustedOdds *= 1.5;
            }

            if(trackShape.equals("Figure-Eight")){

                adjustedOdds *= 1.1;
            }else if(trackShape.equals("Zigzag")){

                adjustedOdds *= 1.3;
            }


            adjustedOdds *= (0.9+ Math.random()*0.2);


            adjustedOdds = Math.round(adjustedOdds * 100.0) / 100.0;

            horseOddsMap.put(horse.getName(), adjustedOdds);


            table_OddsModel.addRow(new Object[]{horse.getName(), String.format("%.2f",adjustedOdds)});
        }
        updateWinPotential();
    }

    private void updateWinPotential(){
        String selected_Horse = (String) selector_ForHorse.getSelectedItem();
        if(selected_Horse== null){
            label_PossibleWinnings.setText("£0.00");
            return;
        }
        double gableAmount = 0.0;

        try{
            gableAmount = Double.parseDouble(textField_GambleAmount.getText());
        } catch (NumberFormatException e) {
            label_PossibleWinnings.setText("Amount Invalid");
            return;
        }

        Double possibility = horseOddsMap.get(selected_Horse);
        if(possibility == null){
            label_PossibleWinnings.setText("£0.00");
            return;
        }
        double winnings = gableAmount * possibility;
        label_PossibleWinnings.setText("£" + String.format("%.2f", winnings));
    }

    private void makeBet(){
        String selected_Horse = (String) selector_ForHorse.getSelectedItem();
        if(selected_Horse == null){
            JOptionPane.showMessageDialog(this, "Please select a horse to bet on");
            return;
        }

        double gambleAmount = 0.0;
        try {
            gambleAmount = Double.parseDouble(textField_GambleAmount.getText());

            if(gambleAmount<=0){
                JOptionPane.showMessageDialog(this, "Gamble amount must be greater than 0");
                return;
            }

            if(gambleAmount>inSimulatorBalance){
                JOptionPane.showMessageDialog(this, "You do not have enough balance to place this bet");
                return;
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid gamble amount");
            return;
        }

        Double possibility = horseOddsMap.get(selected_Horse);
        if(possibility == null){
            JOptionPane.showMessageDialog(this, "Odds for selected horse not available/found");
            return;
        }


        inSimulatorBalance -= gambleAmount;
        label_balance.setText("Simulator Balance: £" + String.format("%.2f", inSimulatorBalance));


        BettingRecord bRecord = new BettingRecord();
        bRecord.setRace_number(historyOfBets.size()+1);
        bRecord.setHorses_name(selected_Horse);
        bRecord.setGamble_amount(gambleAmount);
        bRecord.setGamble_odds(possibility);
        bRecord.setResult("Pending");
        bRecord.setGamble_winnings(0.0);

        historyOfBets.add(bRecord);

        historyOfBets_TModel.addRow(new Object[]{bRecord.getRace_number(), bRecord.getHorses_name(), String.format("£%.2f", bRecord.getGamble_amount()), String.format("%.2f", bRecord.getGamble_odds()), bRecord.getResult(), String.format("£%.2f", bRecord.getGamble_winnings())});

        JOptionPane.showMessageDialog(this, "Your bet has been successfully placed on "+selected_Horse+ " for £"+String.format("%.2f", gambleAmount), "Bet Placed", JOptionPane.INFORMATION_MESSAGE);
    }

    private void betResultsUpdate(ArrayList<Horse> horses){
        if(horses == null || horses.isEmpty()){
            return;
        }


        ArrayList<Horse> winners = new ArrayList<>();
        for(Horse horse : horses){
            if(horse.getDistanceTravelled() >= mainGUI.getLengthOfTrack()){
                winners.add(horse);
            }
        }


        for(int i=0; i<historyOfBets.size(); i++ ){
            BettingRecord bet = historyOfBets.get(i);

            if(bet.result.equals("Pending")){

                boolean hasWon = false;
                for(Horse winner : winners){
                    if(bet.getHorses_name().equals(winner.getName())){
                        hasWon = true;
                        break;
                    }
                }

                if(hasWon){
                    bet.setResult("Won");
                    bet.setGamble_amount(bet.getGamble_amount() * bet.getGamble_odds());
                    inSimulatorBalance +=bet.getGamble_winnings();

                }else{
                    bet.setResult("Lost");
                    bet.setGamble_winnings(0.0);
                }
            }

            historyOfBets_TModel.setValueAt(bet.result, i, 4);
            historyOfBets_TModel.setValueAt(String.format("£%.2f", bet.gamble_winnings), i, 5);


            bettingStatsUpdate();
        }
    }


    public void updateBetting(ArrayList<Horse> horses){

        betResultsUpdate(horses);

        label_balance.setText("In simulator Balance: £" + String.format("%.2f", inSimulatorBalance));

        calcOddss();

        selector_HorseUpdate();

    }

    private void bettingStatsUpdate(){
        int sumBets = 0;
        int sumWonBets = 0;
        double totalEarnings = 0.0;

        for (BettingRecord bet : historyOfBets) {
            if(!bet.getResult().equals("Pending")){
                sumBets++;
                if(bet.getResult().equals("Won")){
                    sumWonBets++;
                    totalEarnings += (bet.getGamble_winnings() + bet.getGamble_amount());
                }
            }
        }

        double rateOFSuccess = (sumBets>0) ? ((double) sumWonBets / sumBets) * 100 : 0.0;


        JPanel panel_stats = (JPanel) historyOfBetsTable.getParent().getParent().getComponent(1);
        JLabel label_stats = (JLabel) panel_stats.getComponent(0);
        label_stats.setText("Betting Rate Success: " + String.format("%.2f", rateOFSuccess) + "% | Total Earnings: £" + String.format("%.2f", totalEarnings));
    }

    public void resetBets(){

        for(int i=0; i<historyOfBets.size(); i++){
            BettingRecord bet = historyOfBets.get(i);

            if(bet.result.equals("Pending")){
                bet.result = "Bet Cancelled";
                inSimulatorBalance += bet.getGamble_amount();

                historyOfBets_TModel.setValueAt(bet.getResult(), i, 4);
            }
        }

        label_balance.setText("In simulator Balance: £" + String.format("%.2f", inSimulatorBalance));


        calcOddss();
    }
}