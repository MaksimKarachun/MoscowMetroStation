import java.util.ArrayList;

public class Station implements Comparable<Station> {

    static ArrayList<Station> allStation = new ArrayList<>();
    String stationName;
    String stationLineNumber;
    String lineName;

    public Station(String stationName, String stationLineNumber, String lineName){
        this.stationName = stationName;
        this.stationLineNumber = stationLineNumber;
        this.lineName = lineName;
    }

    //method find stationName in string end give Station
    public static Station findStation(String nameInString, String line){
        String[] strMas = nameInString.split(" ");
        Station findStation = null;
        String currentName;
        boolean find = false;
        for (Station station : Station.allStation){
            currentName = strMas[0];
            for (int i = 0; i < strMas.length; i++){
                if (i != 0)
                    currentName = currentName + " " + strMas[i];

                if (station.stationName.equals(currentName) && station.stationLineNumber.equals(line)){
                    findStation = station;
                    find = true;
                    break;
                }
            }
            if (find)
                break;
        }
        return findStation;
    }

    @Override
    public int compareTo(Station station) {
        boolean flag1 = stationLineNumber.contains("А");
        boolean flag2 = station.stationLineNumber.contains("А");
        int num1 = 0;
        int num2 = 0;

        if (flag1 && flag2) {
            num1 = Integer.parseInt(stationLineNumber.substring(0, stationLineNumber.length() - 1));
            num2 = Integer.parseInt(station.stationLineNumber.substring(0, station.stationLineNumber.length() - 1));;
        }

        if (flag1 && !flag2) {
            num1 = Integer.parseInt(stationLineNumber.substring(0, stationLineNumber.length() - 1));
            num2 = Integer.parseInt(station.stationLineNumber);

            if (num1 == num2)
                num1 = num2 + 1;
        }

        if (!flag1 && flag2) {
            num1 = Integer.parseInt(stationLineNumber);
            num2 = Integer.parseInt(station.stationLineNumber.substring(0, station.stationLineNumber.length() - 1));

            if (num1 == num2)
                num2 = num1 + 1;
        }

        if (!flag1 && !flag2) {
            num1 = Integer.parseInt(stationLineNumber);
            num2 = Integer.parseInt(station.stationLineNumber);
        }

        return Integer.compare(num1, num2);
    }

    }
