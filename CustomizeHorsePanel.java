import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class CustomizeHorsePanel extends JPanel {
    private RacingGUI mainGUI;

    private boolean inEdit = false;
    private Horse theHorseBeingEdited = null;

    private JTextField textField_name;
    private JTextField textField_symbol;
    private JSlider sliderConfidence;
    private JLabel  confidenceLabel;
    private JComboBox<String> comboBoxBreed;
    private JComboBox<String> comboBoxColour;
    private JComboBox<String> comboBoxGear;

    private JTable horseTable;
    private DefaultTableModel tModel;
    private JButton add_a_horse;
    private JButton remove_a_horse;
    private JButton edit_a_horse;

    private HashMap<String,Double> confidenceOFBreed;


    public CustomizeHorsePanel(RacingGUI mainGUI) {
        this.mainGUI = mainGUI;
        setLayout(new BorderLayout());

        configure_Breed_Confidence();

        JPanel confidencePanel = createPanelForConfidence();
        add(confidencePanel, BorderLayout.NORTH);

        JPanel horseDetails = createPanelForHorseDetails();
        add(horseDetails, BorderLayout.CENTER);
    }

    private JPanel createPanelForHorseDetails() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Horses in The Race"));

        String[] nameOfColumns = {"Horse Name","Horse Symbol", "Horse Breed", "Horse Colour", "Horse Gear", "Horse Confidence"};
        tModel = new DefaultTableModel(nameOfColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make cells non-editable
            }
        };

        horseTable = new JTable(tModel);
        horseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(horseTable);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        edit_a_horse = new JButton("Edit Horse");
        remove_a_horse = new JButton("Remove Horse");

        btnPanel.add(edit_a_horse);
        btnPanel.add(remove_a_horse);

        panel.add(btnPanel, BorderLayout.SOUTH);

        remove_a_horse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeHorse();
            }
        });

        edit_a_horse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    editHorse();
            }
        });

        return panel;

    }

    private JPanel createPanelForConfidence() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Name of Horse:"), c);

        c.gridx =1;
        c.fill = GridBagConstraints.HORIZONTAL;
        textField_name = new JTextField(20);
        panel.add(textField_name, c);

        //symbol of horse
        c.gridx = 0;
        c.gridy = 1;
        c.weightx =0.0;
        panel.add(new JLabel("Symbol for Horse:"), c);

        c.gridx =1;


        textField_symbol = new JTextField(2);
        textField_symbol.setFont(new Font("Segoe UI Emohi", Font.PLAIN, 20));
        JButton emojiBTN = new JButton("Emoji");
        emojiBTN.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        emojiBTN.setToolTipText("Select from Popular Horse Symbols");

        JPopupMenu popupMenuEmoji = new JPopupMenu();
        String[] emojis = {"🐴","🐎","🐴‍❄️","🐴‍☠️","🐴‍🌈","🐴‍🚀","🐴‍🚒","🐴‍🦺","🐴‍🦼","🐴‍🦯"};

        for (String emoji : emojis) {
            JMenuItem itemEmoji = new JMenuItem(emoji);
            itemEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            itemEmoji.addActionListener(e-> textField_symbol.setText(emoji));
            popupMenuEmoji.add(itemEmoji);
        }
        emojiBTN.addActionListener(e -> popupMenuEmoji.show(emojiBTN, 0, emojiBTN.getHeight()));
        JPanel panel_Symbol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_Symbol.add(textField_symbol);
        panel_Symbol.add(emojiBTN);
        panel.add(panel_Symbol, c);


        //select breed
        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Horse Breed:"), c);

        c.gridx = 1;
        String[] optionsBreeds = {"Appaloosa","Standardbred","Arabian","Thoroughbred","Quarter Horse"};
        comboBoxBreed = new JComboBox<>(optionsBreeds);
        panel.add(comboBoxBreed, c);

        //horse coat colour
        c.gridx=0;
        c.gridy=3;
        panel.add(new JLabel("Colour of Coat:"), c);

        c.gridx=1;
        String[] options_Colours = {"Sorrel","Gray","Black","Roan","Palomino",};
        comboBoxColour = new JComboBox<>(options_Colours);
        panel.add(comboBoxColour, c);

        //gear
        c.gridx=0;
        c.gridy=4;
        panel.add(new JLabel("Gear:"), c);

        c.gridx=1;
        String[] optionsForGear = {"Horse Shoes(Standard)","Horse Shoes(racing)","SaddleL(Light Weight)","SaddleS(Standard)"};
        comboBoxGear = new JComboBox<>(optionsForGear);
        panel.add(comboBoxGear, c);

        //confidence slider
        c.gridx=0;
        c.gridy=5;
        panel.add(new JLabel("Confidence:"), c);

        c.gridx=1;
        sliderConfidence = new JSlider(JSlider.HORIZONTAL,0,100,70);
        sliderConfidence.setMajorTickSpacing(20);
        sliderConfidence.setMinorTickSpacing(20);
        sliderConfidence.setPaintTicks(true);
        sliderConfidence.setPaintLabels(true);
        panel.add(sliderConfidence, c);

        c.gridx=2;
        confidenceLabel = new JLabel("0.70");
        panel.add(confidenceLabel, c);

        //button to add horse
        c.gridx=1;
        c.gridy=6;
        c.anchor = GridBagConstraints.CENTER;
        add_a_horse = new JButton("Add a Horse");
        panel.add(add_a_horse, c);

        setEventListeners();
        return panel;

    }

    private void setEventListeners() {
        sliderConfidence.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double conf = sliderConfidence.getValue()/100.0;
                confidenceLabel.setText(String.format("%.2f", conf));
            }
        });

        comboBoxBreed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String breedSelected = (String) comboBoxBreed.getSelectedItem();
                Double bsConfidence = confidenceOFBreed.get(breedSelected);
                if(bsConfidence !=null){
                    sliderConfidence.setValue((int)(bsConfidence*100));
                }
            }
        });

        add_a_horse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_Horse();
            }
        });
    }


    private void editHorse(){
        int selectRow = horseTable.getSelectedRow();
        if(selectRow>=0){
            String hName = (String) tModel.getValueAt(selectRow,0);

            for(Horse horse: mainGUI.getHorses()){
                if(horse.getName().equals(hName)){

                    theHorseBeingEdited = horse;

                    textField_name.setText(horse.getName());
                    textField_symbol.setText(String.valueOf(horse.getSymbol()));
                    sliderConfidence.setValue((int)(horse.getConfidence()*100));

                    comboBoxBreed.setSelectedItem(tModel.getValueAt(selectRow,2));
                    comboBoxColour.setSelectedItem(tModel.getValueAt(selectRow,3));
                    comboBoxGear.setSelectedItem(tModel.getValueAt(selectRow,4));

                   tModel.removeRow(selectRow);
                   add_a_horse.setText("Save Changes");
                   inEdit = true;

                    JOptionPane.showMessageDialog(this,"Edit the horse and click 'Save Changes' to Update","Edit horse",JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Select horse to edit","Selection needed", JOptionPane.ERROR_MESSAGE);
        }
    }







    private void  removeHorse(){
        int rowSelect = horseTable.getSelectedRow();
        if(rowSelect>=0){
            String hName = (String) tModel.getValueAt(rowSelect,0);

            //confirmation dialog
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this horse?","Confirmation",JOptionPane.YES_NO_OPTION);

            if(confirmation==JOptionPane.YES_OPTION){
                for(Horse horse: mainGUI.getHorses()){
                    if(horse.getName().equals(hName)){
                        mainGUI.removeHorse(horse);
                        tModel.removeRow(rowSelect);
                        break;
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select a horse to remove","You need to select", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void add_Horse(){
        //validate input
        String name = textField_name.getText();
        String txtSymbol = textField_symbol.getText();


        if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter horse name plase","Error(input)",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(txtSymbol.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter horse symbol plase","Error(input)",JOptionPane.ERROR_MESSAGE);
            return;
        }



        char Symbol;
        if(!txtSymbol.isEmpty()){
            Symbol = txtSymbol.charAt(0);
        }else {
            Symbol = 'H';
        }


        String horseBreed = (String) comboBoxBreed.getSelectedItem();
        String horseColour = (String) comboBoxColour.getSelectedItem();
        String gear = (String) comboBoxGear.getSelectedItem();
        double confidence = sliderConfidence.getValue()/100.0;

        try{
            if(inEdit && theHorseBeingEdited !=null){
                theHorseBeingEdited.setSymbol(Symbol);
                theHorseBeingEdited.setConfidence(confidence);

                Object[] rData ={theHorseBeingEdited.getName(), String.valueOf(Symbol), horseBreed, horseColour,gear, String.format("%.2f",confidence)};

                tModel.addRow(rData);

                //resets the edit mode
                inEdit = false;
                theHorseBeingEdited = null;
                add_a_horse.setText("Add a Horse Please");
                JOptionPane.showMessageDialog(this,"Changes have been saved","Horse Edited",JOptionPane.INFORMATION_MESSAGE);
            }else{
                //make/create a new horse
                Horse newHorse = new Horse(Symbol, name, confidence);
                mainGUI.addHorse(newHorse);

                Object[] rData = {name, String.valueOf(Symbol), horseBreed, horseColour, gear, String.format("%.2f", confidence)};

                tModel.addRow(rData);
            }
            //clear the text fields
            textField_name.setText("");
            textField_symbol.setText("");
        }catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configure_Breed_Confidence() {
        confidenceOFBreed = new HashMap<>();
        confidenceOFBreed.put("Appaloosa",0.8);
        confidenceOFBreed.put("Standardbred",0.75);
        confidenceOFBreed.put("Arabian ",0.7);
        confidenceOFBreed.put("Thoroughbred",0.65);
        confidenceOFBreed.put("Quarter Horse",0.6);

    }

    public void refreshTable(){
        tModel.setRowCount(0);

        for(Horse horse :mainGUI.getHorses()){

            String hBreed = "Unknown";
            String hColour = "Unknown";
            String hGear = "Standard";

            Object[] rData ={horse.getName(),String.valueOf(horse.getSymbol()),hBreed,hColour,hGear,String.format("%.2f",horse.getConfidence())};
            tModel.addRow(rData);
        }
    }
}
