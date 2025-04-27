import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Race {
    private final int raceLength;
    private final List<Horse> horses;

    public Race(int distance) {
        this.raceLength = distance;
        this.horses = new ArrayList<>();
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public int getRaceLength() {
        return raceLength;
    }

    public void addHorse(Horse horse) {
        if (horse != null) {
            horses.add(horse);
        }
    }


    public void reset() {
        for (Horse h : horses) {
            h.goBackToStart();
        }
    }


    public void step() {
        for (Horse h : horses) {
            if (h != null && !h.hasFallen() && h.getDistanceTravelled() < raceLength) {
                moveHorse(h);
            }
        }
    }


    public boolean isFinished() {
        if (allHorsesFallen()) return true;
        return horses.stream()
                .anyMatch(h -> h.getDistanceTravelled() >= raceLength);
    }


    public List<Horse> getWinners() {
        return horses.stream()
                .filter(h -> h.getDistanceTravelled() >= raceLength).collect(Collectors.toList());
    }



    private void moveHorse(Horse theHorse) {

        if (Math.random() < theHorse.getConfidence()) {
            theHorse.moveForward();
        }

        if (Math.random() < 0.1 * theHorse.getConfidence() * theHorse.getConfidence()) {
            theHorse.fall();
        }
    }

    private boolean allHorsesFallen() {
        for (Horse h : horses) {
            if (h != null && !h.hasFallen()) {
                return false;
            }
        }
        return true;
    }
}
