import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TrackDesignPanel extends JPanel {

    private RacingGUI mainGUI;

    // GUI components
    private JSlider sliderForTrackLength;
    private JLabel labelForTrackLength;
    private JSpinner spinnerForLaneCount;
    private JComboBox<String>comboBoxForTrackShape;
    private JComboBox<String>comboBoxForTrackCondition;
    private JPanel previewPanelForTrack;


    public TrackDesignPanel(RacingGUI mainGUI) {

        this.mainGUI = mainGUI;
        setLayout(new BorderLayout());

        JPanel panelForm = createPanelForm();
        add(panelForm, BorderLayout.NORTH);

        previewPanelForTrack = createPreviewPanel();
        add(new JScrollPane(previewPanelForTrack), BorderLayout.CENTER);


    }

    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics ghc) {
                super.paintComponent(ghc);
                drawPreviewTrack(ghc);
            }
        };

        panel.setPreferredSize(new Dimension(600, 400));
        panel.setBackground(Color.MAGENTA);
        return panel;

    }

    private void setEventListeners(JButton buttonApply){
        sliderForTrackLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = sliderForTrackLength.getValue();
                labelForTrackLength.setText(val + " units");


                if (!sliderForTrackLength.getValueIsAdjusting()) {
                    previewPanelForTrack.repaint();
                }
            }
        });

        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ap) {
                applyTheChanges();
            }
        });

        comboBoxForTrackShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanelForTrack.repaint();
            }
        });

        comboBoxForTrackCondition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAppearanceOfTrack();
            }
        });
    }

    private void applyTheChanges() {
        int length_of_track = sliderForTrackLength.getValue();
        String shape_of_track = (String) comboBoxForTrackShape.getSelectedItem();
        String condition_of_track = (String) comboBoxForTrackCondition.getSelectedItem();

        mainGUI.setLengthOfTrack(length_of_track);
        mainGUI.setShapeOfTrack(shape_of_track);
        mainGUI.setCondition_OfTrack(condition_of_track);

        JOptionPane.showMessageDialog(this, "Changes to track applied successfully", "Alright changes applied", JOptionPane.INFORMATION_MESSAGE);

    }

    private void updateAppearanceOfTrack() {
        String condition = (String) comboBoxForTrackCondition.getSelectedItem();

        previewPanelForTrack.repaint();
    }


    private void drawPreviewTrack(Graphics ghc) {
        Graphics2D ghc2d = (Graphics2D) ghc;
        ghc2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int LANES = (Integer) spinnerForLaneCount.getValue();
        int width = previewPanelForTrack.getWidth();
        int height = previewPanelForTrack.getHeight();
        int trackLength = sliderForTrackLength.getValue();
        String trackShape = (String) comboBoxForTrackShape.getSelectedItem();
        String trackCondition = (String) comboBoxForTrackCondition.getSelectedItem();

        Color ColorOfTrack;
        if(trackCondition.equals("Muddy")){
            ColorOfTrack = new Color(112,84,62);
        } else if (trackCondition.equals("Icy")) {
            ColorOfTrack = new Color(32,195,208);
        }else {
            ColorOfTrack = new Color(210,180,180);
        }

        ghc2d.setColor(ColorOfTrack);


        if(trackShape.equals("Oval")){

            int pad=50;
            int widthOval = width - (2*pad);
            int heightOval = height - (2*pad);

            double scaleFac= trackLength/30.0;
            widthOval = (int) (widthOval*scaleFac);
            if(widthOval>width-(2*pad)){
                widthOval = width-(2*pad);
            }
            ghc2d.fillOval(pad,pad,widthOval,heightOval);

            //draw inside the oval i.e. empty space
            ghc2d.setColor(Color.WHITE);


            int laneWidth = 10;  // Same as your lane line spacing
            int totalLaneWidth = LANES * laneWidth;


            ghc2d.fillOval(pad + totalLaneWidth, pad + totalLaneWidth, widthOval - 2 * totalLaneWidth, heightOval - 2 * totalLaneWidth);

            //draw the lanes of the track
            ghc2d.setColor(Color.BLACK);
            for(int i=0; i<LANES; i++){
                int widthOfLane = 10;

                ghc2d.drawOval(pad+(i*widthOfLane),pad+(i*widthOfLane),widthOval-(i*widthOfLane*2), heightOval-(i*widthOfLane*2));
            }
        } else if (trackShape.equals("Figure-Eight")) {
            int pad = 50;
            int circlesSize = width <= height ? width - (2 * pad) : height - (2 * pad);

            double scaleFac = trackLength / 30.0;
            circlesSize = (int) (circlesSize * scaleFac);
            if(circlesSize*2+pad>width){
                circlesSize =(width-pad)/2;
            }
            ghc2d.fillOval(pad, pad, circlesSize, circlesSize);
            ghc2d.fillOval(width-pad-circlesSize,pad, circlesSize, circlesSize);

            ghc2d.setColor(Color.BLACK);

            for(int i=0; i<LANES; i++){
                int widthOfLane = 5;
                ghc2d.drawOval(pad+(i*widthOfLane),pad+(i*widthOfLane),circlesSize-(i*widthOfLane*2), circlesSize-(i*widthOfLane*2));
                ghc2d.drawOval(width-pad-circlesSize+(i*widthOfLane),pad+(i*widthOfLane),circlesSize-(i*widthOfLane*2), circlesSize-(i*widthOfLane*2));
            }
        } else if (trackShape.equals("Zigzag")) {
            int pad = 50;
            int widthOfTrack = 40;
            int widthZigzag = width - (2 * pad);
            int heightZigzag = height - (2 * pad);

            double scaleFac = trackLength / 30.0;
            widthZigzag = (int) (widthZigzag * scaleFac);

            if(widthZigzag>width-(2*pad)){
                widthZigzag = width-(2*pad);
            }
            int[] pointsX ={pad,pad+widthZigzag/4,pad+widthZigzag/2,pad+(3*widthZigzag/4),pad+widthZigzag};
            int[] pointsY = {pad+heightZigzag/2,pad,pad+heightZigzag,pad,pad+heightZigzag/2};

            //draw path for zigzag
            ghc2d.setStroke(new BasicStroke(widthOfTrack));
            ghc2d.drawPolyline(pointsX, pointsY, 5);

            //now the lanes here:
            ghc2d.setColor(Color.BLACK);
            ghc2d.setStroke(new BasicStroke(1));
            ghc2d.drawPolyline(pointsX, pointsY, 5);
        }
    }



    private JPanel createPanelForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.insets = new Insets(5, 5, 5, 5);

        //tracks length
        cc.gridx=0;
        cc.gridy=0;
        cc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Track Length:"), cc);

        cc.gridx=1;
        sliderForTrackLength = new JSlider(JSlider.HORIZONTAL,10,50,mainGUI.getLengthOfTrack());
        sliderForTrackLength.setMajorTickSpacing(10);
        sliderForTrackLength.setMinorTickSpacing(5);
        sliderForTrackLength.setPaintTicks(true);
        sliderForTrackLength.setPaintLabels(true);
        panel.add(sliderForTrackLength,cc);

        cc.gridx =2;
        labelForTrackLength = new JLabel(mainGUI.getLengthOfTrack()+" units");
        panel.add(labelForTrackLength,cc);


        cc.gridx =0;
        cc.gridy=1;
        panel.add(new JLabel("Number of Lanes:"),cc);

        cc.gridx=1;
        SpinnerNumberModel model = new SpinnerNumberModel(3,1,10,1);
        spinnerForLaneCount = new JSpinner(model);
        panel.add(spinnerForLaneCount,cc);


        cc.gridx=0;
        cc.gridy=2;
        panel.add(new JLabel("Shape of Track:"),cc);

        cc.gridx=1;
        String[] optionsForShapes = {"Oval","Figure-Eight","Zigzag"};
        comboBoxForTrackShape = new JComboBox<>(optionsForShapes);
        panel.add(comboBoxForTrackShape,cc);

        //conditions of track
        cc.gridx=0;
        cc.gridy=3;
        panel.add(new JLabel("Condition of Track:"),cc);

        cc.gridx=1;
        String[] optionsForConditions ={"Dry","Muddy","Icy"};
        comboBoxForTrackCondition = new JComboBox<>(optionsForConditions);
        panel.add(comboBoxForTrackCondition,cc);

        //application of button
        cc.gridx=1;
        cc.gridy=4;
        cc.anchor = GridBagConstraints.CENTER;
        JButton buttonApply = new JButton("Apply");
        panel.add(buttonApply,cc);

        setEventListeners(buttonApply);
        return panel;

    }
}