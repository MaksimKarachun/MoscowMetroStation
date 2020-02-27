import java.util.ArrayList;


public class Connection {

    static ArrayList<Connection> allConnect = new ArrayList<>();

    public ArrayList<Station> connectStations;

    public Connection(ArrayList<Station> stationInConnect) {
        this.connectStations = stationInConnect;
    }


    public static Boolean findConnect(String stationName, String lineNumber){
        boolean findConnect = false;
        for (Connection connect : Connection.allConnect){
            for (Station station : connect.connectStations){
                if(station.stationName.equals(stationName) && station.stationLineNumber.equals(lineNumber)) {
                    findConnect = true;
                    break;
                }
            }
        }
        return findConnect;
    }
}
