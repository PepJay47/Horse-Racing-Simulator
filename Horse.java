import java.util.ArrayList;

/**
 * Models a horse with breed and gear effects on speed and confidence.
 */
public class Horse {
    private String horseName;
    private char horseSymbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double horseConfidence;

    private final String breed, gear, colour;
    private final double speedMultiplier;

    private static final ArrayList<Horse> madeHorses = new ArrayList<>();

    public Horse(char horseSymbol,String horseName, double horseConfidence, String breed, String gear,String colour) {
        for (Horse h : madeHorses) {
            if (h.horseName.equalsIgnoreCase(horseName))
                throw new IllegalArgumentException("Horse name already is used");
        }
        if (horseName.isEmpty())
            throw new IllegalArgumentException("Horse name cannot be empty");

        this.horseName = horseName;
        this.horseSymbol = horseSymbol;
        this.distanceTravelled = 0;
        this.hasFallen = false;
        setConfidence(horseConfidence);

        this.breed = breed;
        this.gear = gear;
        this.colour = colour;

        double bMul = computeBreedMultiplier(breed);
        double gMul = computeGearMultiplier(gear);
        this.speedMultiplier = bMul * gMul;

        madeHorses.add(this);
    }



    public String getName(){
        return horseName;
    }
    public char getSymbol() {
        return horseSymbol;
    }
    public int getDistanceTravelled(){
        return distanceTravelled;
    }
    public boolean hasFallen(){
        return hasFallen;
    }
    public double getConfidence(){
        return horseConfidence;
    }
    public String getBreed(){
        return breed;
    }
    public String getGear(){
        return gear; }
    public String getColour(){
        return colour;
    }
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void goBackToStart() {
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }

    //when the horse falls we cut off some of its confidence
    public void fall() {
        if (!hasFallen) {
            hasFallen = true;
            setConfidence(horseConfidence * 0.9);
        }
    }


    public void moveForward() {
        if (hasFallen) return;


        int base = (int) Math.floor(speedMultiplier);
        double frac = speedMultiplier - base;


        int step = base + (Math.random() < frac ? 1 : 0);
        if (step < 1) step = 1;

        distanceTravelled += step;
    }


    public void setConfidence(double newConfidence) {
        if (newConfidence < 0.0 || newConfidence > 1.0)
            throw new IllegalArgumentException("Confidence is out of range");
        this.horseConfidence = newConfidence;
    }

    public void setSymbol(char newSymbol) {
        this.horseSymbol = newSymbol;
    }



    //breed effect
    private double computeBreedMultiplier(String breed) {
        return switch (breed) {
            case "Quarter Horse"   -> 1.2;
            case "Arabian"         -> 1.15;
            case "Thoroughbred"    -> 1.1;
            case "Standardbred"    -> 1.05;
            case "Appaloosa"       -> 1.0;
            default                -> 1.0;
        };
    }

    //gear effect
    private double computeGearMultiplier(String gear) {
        return switch (gear) {
            case "Horse Shoes(racing)"    -> 1.2;
            case "SaddleL(Light Weight)"  -> 1.1;
            case "SaddleS(Standard)"      -> 1.0;
            case "Horse Shoes(Standard)"  -> 1.0;
            default                       -> 1.0;
        };
    }
}
