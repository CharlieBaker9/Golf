import java.util.*;

/**
 * Class which helps pick a club depending on wind, temperature, elevation change, and firmness of greens
 *
 * @author Charlie Baker, Dartmouth Spring 2021
 */

public class SimulatorRoomClubChoice {
    private List<String> clubs;
    private ArrayList<Integer> yardageArray;
    private Map<Integer, Map<Integer, Integer>> allWindYardageAffects;
    private String firmness = "normal";
    private String tempType = "Fahrenheit";

    public SimulatorRoomClubChoice(List<String> clubs) {
        this.clubs = clubs;
        this.allWindYardageAffects = allWindYardageAffects();
    }

    public Map allWindYardageAffects() {
        Map<Integer, Map<Integer, Integer>> windAndYardage = new TreeMap<>();
        for (int allYardages = 1; allYardages <= 350; allYardages++) {
            Map<Integer, Integer> yardageMap = new TreeMap<>();
            for (int allWinds = -50; allWinds <= 50; allWinds++) {
                int windAffect;
                if (allYardages < 150) {
                    windAffect = (int) (allWinds*0.3025+0.25833);
                } else {
                    windAffect = (int) (allWinds*0.4275+0.30833);
                }
                yardageMap.put(allWinds, windAffect);
            }
            windAndYardage.put(allYardages, yardageMap);
        }
        return windAndYardage;
    }

    public Integer wind(int yardage, int wind) {
        return yardage -= allWindYardageAffects.get(yardage).get(wind);
    }

    public Integer elevationChange(int yardage, int elevationChange) {
        if (yardage < 100) {
            yardage += (elevationChange / 15) * 10;
        } else if (yardage < 150) {
            yardage += (elevationChange / 15) * 13;
        } else if (yardage < 200) {
            yardage += (elevationChange / 15) * 15;
        }
        return yardage;
    }

    public Integer temperature(int yardage, int temperature) {
        if (tempType.toLowerCase() == "celsius") {
            temperature = (temperature * (9 / 5)) + 32;
        }
        yardage = yardage + (int) ((temperature - 70) * 2) * (yardage / 250);
        return yardage;
    }

    public Integer roll(int yardage) throws Exception {
        double coefficient;
        if (firmness == "normal") {
            coefficient = 0.1;
        } else if (firmness == "soft") {
            coefficient = 0.01;
        } else if (firmness == "firm") {
            coefficient = 0.2;
        } else {
            throw new Exception("Inputted firmness does not exist please input: soft, normal, or firm");
        }
        return (int) (yardage - yardage * coefficient);
    }

    public void setFirmness(String firmness) {
        this.firmness = firmness;
    }

    public String clubChoice(int totalYardage, int wind, int elevationChange, int temperature) throws Exception {
        totalYardage = wind(totalYardage, wind);
        totalYardage = elevationChange(totalYardage, elevationChange);
        totalYardage = temperature(totalYardage, temperature);

        int idealCarryYardage = roll(totalYardage);
        String club = null;
        int difference = -1;
        String typeOfShot;

        for (int i=0; i < clubs.size(); i++){
            if (yardageArray.get(i) > idealCarryYardage && difference==-1){
                club = clubs.get(i);
                difference = yardageArray.get(i) - idealCarryYardage;
                }
            }
        if (club == null) { throw new Exception("Check to make sure yardage is correct"); }

        if (difference < 3) { typeOfShot = "Full "; }
        else if (difference < 6) { typeOfShot = "Stock "; }
        else if (difference < 9) { typeOfShot = "3/4"; }
        else if (difference < 12) { typeOfShot = "Choke Down "; }
        else if (difference < 15) { typeOfShot = "Knockdown "; }
        else { typeOfShot = "Dead armed 1/2 swing "; }

        return club + " " + typeOfShot;
    }


    public void inputCarryYardages(List<Integer> distances) throws Exception {
        yardageArray = new ArrayList<>();
        if (clubs.size() != distances.size()) {
            throw new Exception("Need to input a distance for each club");
        }
        for (int i = 0; i < clubs.size(); i++) {
            yardageArray.add(distances.get(i));
        }
    }

    public static void main(String[] args) throws Exception {
        String[] clubs = {"58 degree", "54 degree", "50 degree", "P wedge", "9 iron", "8 iron", "7 iron", "6 iron", "5 iron", "4 iron", "4 hybrid", "3 wood", "driver"};
        List allClubs = Arrays.asList(clubs);
        Integer[] yardages = {80, 98, 116, 131, 142, 155, 169, 182, 196, 210, 225, 245, 275};
        List allYardages = Arrays.asList(yardages);
        System.out.println(allYardages);
        SimulatorRoomClubChoice Charlie = new SimulatorRoomClubChoice(allClubs);
        Charlie.inputCarryYardages(allYardages);
        System.out.println(Charlie.clubChoice(260, -20, 5, 85));
        System.out.println(Charlie.clubChoice(220, 10, 20, 85));
        System.out.println(Charlie.clubChoice(150, 40, 30, 85));
    }
}
