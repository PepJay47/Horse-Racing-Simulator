import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @author McRaceface
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses;
    private String shapeOfTrack;
    private String tracksCondition;



    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        this.horses = new ArrayList<>();

    }


    //making constructor to receive information about the track from the GUI()
    public Race(int distance,String shape, String condition){
        this.shapeOfTrack= shape;
        this.tracksCondition = condition;
        this.raceLength = distance;
        this.horses = new ArrayList<>();
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
//     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse){
        horses.add(theHorse);

    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */
    public void startRace() {

        //declare a local variable to tell us when the race is finished
        boolean finished = false;


        for(Horse horse : horses){

            if(horse != null){
                horse.goBackToStart();
            }
        }

        //making changes here for the GUI integration
        //adjusting the speed in the sense that the horses speed is affected by the track condition/factors

        while (!finished) {
            //move each horse

            for(Horse horse : horses){

               if(horse != null && !horse.hasFallen()){
                   horse.speedForTrackAdjustment(shapeOfTrack,tracksCondition, horse.getDistanceTravelled(), raceLength);
                   moveHorse(horse);
               }

            }

            //print the race positions
            printRace();

            for(Horse horse: horses){
                if(horse.hasFallen()){
                    horse.goBackToStart();
                }
            }


            //if any of the three horses has won the race is finished
            for(Horse horse : horses) {
                if (raceWonBy(horse)) {
                    finished = true;
                    break;// end race
                }
            }

            if(allHorsesFallen()){
                System.out.println("All horses have fallen");
                finished = true;
//                break;

            }

            //wait for 100 milliseconds
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){

            }


        }


        //announce winner of the race:
        ArrayList<Horse> raceWinners = new ArrayList<>();
        for(Horse horse : horses) {
            if(raceWonBy(horse)){
                raceWinners.add(horse);
            }

        }

        //print race winner(s)
        if(!raceWinners.isEmpty()){
            if(raceWinners.size() == 1){
                System.out.println(raceWinners.get(0).getName()+" has won the race!");
            }else{
                System.out.println("It is a tie between: ");
                for(int i= 0; i<raceWinners.size(); i++){

                    System.out.println(raceWinners.get(i).getName());
                    if(i<raceWinners.size()-1){
                        System.out.println(", ");
                    }
                }
            }
            System.out.println("!");
        }

    }

   //check if all horses have fallen and return true or false if otherwise
    private boolean allHorsesFallen(){
        for(Horse horse : horses) {
            if(horse!=null && !horse.hasFallen()){
                return false;
            }
        }
        return true;

    }


    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     *
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse) {
        //if the horse has fallen it cannot move,
        //so only run if it has not fallen#

        if(theHorse == null){
            return;
        }

        if  (!theHorse.hasFallen()) {
            double movementChance = theHorse.getConfidence();
            double chanceOfFalling = 0.1*theHorse.getConfidence()*theHorse.getConfidence();


            //movement and chance of falling based off of the shape of the track
            if(shapeOfTrack.equals("Figure-Eight")){
            //critical points of the figure 8 track during tight turns and crossings
                int position = theHorse.getDistanceTravelled();

                boolean isAtCentreCrossing = (position > raceLength/2-2 && position < raceLength/2+2);

                boolean duringTightTurn = (position % 8 == 0);

                if(isAtCentreCrossing){
                    //at the centre the horse has a higher chance of falling and lower chance of moving
                    movementChance *=0.7;
                    chanceOfFalling *= 1.8;

                    System.out.println(theHorse.getName()+" has fallen at the centre crossing");
                }else if(duringTightTurn){
                    //moderate impact
                    movementChance *=0.85;
                    chanceOfFalling *= 1.4;
                    System.out.println(theHorse.getName()+" is currently at a tight turn");
                }
            }else if(shapeOfTrack.equals("Zigzag")){
                int position = theHorse.getDistanceTravelled();

                boolean atTurn = (position % 5 == 0);

                if(atTurn){
                    movementChance *=0.75;
                    chanceOfFalling *= 1.5;

                    System.out.println(theHorse.getName()+" is at a turn on the zigzag track");
                }
            }
            if(tracksCondition.equals("Muddy")){
                movementChance *= 0.8;
                chanceOfFalling *= 1.2;
            }else if(tracksCondition.equals("icy")){
                movementChance *= 0.7;
                chanceOfFalling *= 1.6;
            }

            //make decisions about the horse's movement
            if(Math.random() < movementChance){
                theHorse.moveForward();
            }

            if(Math.random() < chanceOfFalling){
                theHorse.fall();
                System.out.println(theHorse.getName()+" has fallen");
            }


        }

    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse) {

        if(theHorse == null){
            return false;
        }
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
//        System.out.print('\u000C');  //clear the terminal window

        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        for(Horse horse : horses) {
            printLane(horse);
            System.out.println();

        }


        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        //print a | for the beginning of the lane
        System.out.print('|');

        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);

        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('❌');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }

        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);

        //print the | for the end of the track
        System.out.print('|');
        System.out.printf("%-15s(Current confidence: %.2f)",theHorse.getName(), theHorse.getConfidence());

    }


    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     *
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
