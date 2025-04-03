import java.util.ArrayList;

/**
 * Write a description of class Horse here.
 *
 * @author (Jason Owusu Peprah)
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse //change 1 made private
    private String horseName;
    private char horseSymbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double horseConfidence;
    private static ArrayList<Horse> madeHorses = new ArrayList<>();



    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {

        for(Horse horse: madeHorses){

            if(horseName.equalsIgnoreCase(horse.getName())){
                throw new IllegalArgumentException("Horse name has been entered already");
            }

        }

        if(horseName.isEmpty()){

            throw new IllegalArgumentException("Horse name cannot be empty");
        }

        this.horseName = horseName;
        this.horseSymbol = horseSymbol;
        this.distanceTravelled = 0;
        this.hasFallen= false;
        this.setConfidence(horseConfidence);
        madeHorses.add(this);

    }



    //Other methods of class Horse
    public void fall()
    {
        this.hasFallen=true;

        //reduce confidence by a percentage when the horse falls and goes back to the start
        this.setConfidence(this.horseConfidence*0.9);
    }

    public double getConfidence()
    {
        return horseConfidence;
    }

    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }

    public String getName()
    {
        return horseName;
    }

    public char getSymbol()
    {
        return horseSymbol;
    }

    public void goBackToStart() {
        this.distanceTravelled = 0;
        this.hasFallen= false;
    }

    public boolean hasFallen() {
        return hasFallen;
    }

    public void moveForward() {
        if(!hasFallen){
            this.distanceTravelled++;
        }
    }


    public void setConfidence(double newConfidence) {

        if(newConfidence>=0.0 && newConfidence<=1.0){
           this.horseConfidence = newConfidence;
        }else{

            throw new IllegalArgumentException("Confidence out of range");
        }

    }

    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }

}
