public class Main {
    public static void main(String[] args) {


        //create objects
//        Horse firstHorse = new Horse('♘',"Jose",0.5);
//        Horse secondHorse = new Horse('♞',"Jodie",0.7);
//        Horse thirdHorse = new Horse('Ω',"Dylan",0.7);
//
//        //passthrough to add horse




        Race newRace = new Race(10);

        newRace.addHorse(new Horse('0',"davud",4));
        newRace.addHorse(new Horse('♞',"man",0.3));
        newRace.addHorse(new Horse('Ω',"davud",0.4));
        newRace.addHorse(new Horse('$',"dan",0.6));
        newRace.addHorse(new Horse('£',"nesta",0.6));
        newRace.addHorse(new Horse('K',"frank",0.7));
        newRace.addHorse(new Horse('A',"shamima",0.7));
        newRace.addHorse(new Horse('B',"samira",0.8));


        newRace.startRace();
//        testMoveForwardMethod();
//        testConstructor();
//        testGetters();
//        setConfidenceTest();

//        testGoBackToStart();
//        testSetConfidence();
//        testSetSymbolInHorseClass();
//        testConfidenceBoundaries();


    }
//
//    private static void testConstructor() {
//    }

    //    private static void testConfidenceBoundaries() {
//    }
//
//    private static void testSetSymbolInHorseClass() {
//        System.out.println("Testing initializing.......");
//        System.out.println();
//        Horse beta = new Horse('B',"Beta",0.5);
//        char initialSym= beta.getSymbol();
//        System.out.println("The initial symbol of beta is: "+initialSym);
//        System.out.println();
//        beta.setSymbol('X');
//        System.out.println("The symbol of beta is after B is : "+beta.getSymbol());
//        System.out.println();
//        beta.setSymbol('C');
//        System.out.println("The symbol of beta is after X is : "+beta.getSymbol());
//        System.out.println();
//        beta.setSymbol('V');
//        System.out.println("The symbol of beta is after C is : "+beta.getSymbol());
//        System.out.println();
//        beta.setSymbol('$');
//        System.out.println("The symbol of beta is after V is : "+beta.getSymbol());
//        System.out.println();
//        beta.setSymbol('£');
//        System.out.println("The symbol of beta is after $ is : "+beta.getSymbol());
//        System.out.println();
//        beta.setSymbol('#');
//        System.out.println("The symbol of beta is after £ is : "+beta.getSymbol());
//        System.out.println();
//
//        System.out.println();
//
//
//
//        System.out.println("Now the new Symbol of beta is : "+beta.getSymbol());
//        System.out.println();
//        System.out.println("Test done");
//
//
//    }
//
//
//    private static void testGoBackToStart() {
//    }
//
//    private static void testMoveForwardMethod() {
//
//
//        System.out.println("Testing initializing");
//        System.out.println();
//        Horse alpha = new Horse('A', "Alpha", 0.7);
//
//        int alphaInitialDistance = alpha.getDistanceTravelled();
//
//        System.out.println("Initial distance of alpha is: " + alphaInitialDistance);
//        assert alpha.getDistanceTravelled() == 0 : "Alpha should not have moved because distance is 0";
//        System.out.println();
//
//        alpha.moveForward();
//        assert alpha.getDistanceTravelled() == 1 : "Counter is now 1 so that means alpha has moved";
//        System.out.println();
//
//        alpha.moveForward();
//        alpha.moveForward();
//        alpha.moveForward();
//        alpha.moveForward();
//        alpha.moveForward();
//
//        System.out.println("Alpha has moved "+alpha.getDistanceTravelled()+" times, so the distance travelled is now: "+alpha.getDistanceTravelled());
//        System.out.println();
//
//        alpha.fall();
//        System.out.println("Alpha has now fallen at this point");
//
//        alpha.moveForward();
//        System.out.println("After alpha has fallen, the distance travelled is now: "+alpha.getDistanceTravelled());
//        assert alpha.getDistanceTravelled() == 6 : "Counter should not increase because alpha has fallen";
//
//    }

//    private static void testFallMethodInHorseClass() {
//
//        System.out.println("Testing initializing....");
//
//        Horse alpha = new Horse('A', "Alpha", 0.7);
//
//        boolean alphaInitialFallen = alpha.hasFallen();
//
//        System.out.println("Initial fallen state of alpha is: "+alphaInitialFallen);
//        assert !alpha.hasFallen() : "Alpha should not have fallen";
//
//        alpha.fall();
//
//        boolean alphaAfterFallen = alpha.hasFallen();
//
//        System.out.println("After falling,the fallen state of alpha is now: "+alphaAfterFallen);
//        assert alpha.hasFallen() : "Alpha should have fallen now.";
//
//        System.out.println("Test finished");
//
//    }

//    private static void testGetters() {
//    }
//
//    private static void testConstructor() {
//
//    }
}