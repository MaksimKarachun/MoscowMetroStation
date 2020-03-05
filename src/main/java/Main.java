import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {

    public static void main(String[] args) {

        try{
        Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0").maxBodySize(0).get();
        File jsonFile = new File("result.json");
            FileWriter fw = new FileWriter(jsonFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            getAllStation(document);
            Collections.sort(Station.allStation);
            parseLine(Station.allStation);
            getAllConnections(document);


            Map<String, Object> result = new LinkedHashMap<>();

            //preparation to JSON stations
            Map<String, List<String>> stations = new LinkedHashMap<>();
            for(Line line : Line.lines){
                List<String> stationsNames = new ArrayList<>();
                for(Station station : line.stationsOnLine){
                    stationsNames.add(station.stationName);
                }
                stations.put(line.lineNumber, stationsNames);
            }
            result.put("Stations", stations);

            //preparation to JSON connections
            List<List<Station>> connections = new ArrayList<>();
            for (Connection connect : Connection.allConnect){
                connections.add(connect.connectStations);
            }
            result.put("Connections", connections);

            //preparation to JSON lines
            List<Map<String, String>> lines = new ArrayList<>();
            for (Line line : Line.lines){
                Map<String, String> map = new LinkedHashMap<>();
                map.put("lineName:", line.lineName);
                map.put("lineNumber", line.lineNumber);
                lines.add(map);
            }
            result.put("Lines", lines);


            String str = gson.toJson(result);
            fw.write(str);
            fw.flush();
            fw.close();
            ReadResult.read();
        }
        catch (IOException e){
            e.getMessage();
        }
    }


    public static void getAllStation (Document document){
        //get Station from table 3
        parseStationInTable(document, 3, 1);
        //get Station from table 4
        parseStationInTable(document, 4, 2);
        //get Station from table 5
        parseStationInTable(document, 5, 2);
    }

    public static void getAllConnections (Document document){
        //get Connections from table 3
        parseConnectionsInTable(document, 3, 1);
        //get Connections from table 4
        parseConnectionsInTable(document, 4, 2);
        //get Connections from table 5
        parseConnectionsInTable(document, 5, 2);
    }


    //method creates lines and add station to them
    public static void parseLine(ArrayList<Station> stationList){
        ArrayList<String> currentLineNumber = new ArrayList<>();
        Line line = null;
        for (Station station : stationList){
            if (line == null) {
                line = new Line(station.stationLineNumber, station.lineName);
                Line.lines.add(line);
                currentLineNumber.add(line.lineNumber);
            }
            if (currentLineNumber.contains(station.stationLineNumber))
                line.stationsOnLine.add(station);
            else {
                line = new Line(station.stationLineNumber, station.lineName);
                Line.lines.add(line);
                line.stationsOnLine.add(station);
                currentLineNumber.add(line.lineNumber);
            }
        }
    }


    public static void parseConnectionsInTable(Document document, int tableNum, int rowBegin){
        Element table = document.select("table").get(tableNum);
        Elements rows = table.select("tr");
        for (int i = rowBegin; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            //check connection
            String connectionIndex = cols.get(3).getElementsByTag("td").attr("data-sort-value");
            if (connectionIndex.equals("Infinity"))
                continue;

            //get Line number
            String currentLineNumber = cols.get(0).getElementsByTag("span").get(0).text();

            //delete 0 from line number
            if (currentLineNumber.charAt(0) == '0')
                currentLineNumber = currentLineNumber.substring(1);

            //get Station name
            String str = cols.get(1).getElementsByTag("a").attr("title");
            String currentStationName;

            //delete text in (...) from currentStationName
            int ind = str.indexOf('(');
            if (ind != -1)
                currentStationName = str.substring(0, ind - 1);
            else
                currentStationName = str;

            //check connection with station
            if(Connection.findConnect(currentStationName, currentLineNumber))
                continue;

                ArrayList<Station> stationInConnect = new ArrayList<>();
                stationInConnect.add(Station.findStation(currentStationName, currentLineNumber));
                Elements connections = cols.get(3).select("span");

                for(int k = 0; k < connections.size();) {
                    String line = connections.get(k).getElementsByTag("span").text();

                    //delete 0 from line number
                    if (line.charAt(0) == '0')
                        line = line.substring(1);

                    str = connections.get(k + 1).getElementsByTag("span").attr("title");
                    String station = str.substring(str.lastIndexOf("станцию") + 8);
                    stationInConnect.add(Station.findStation(station, line));
                    k = k + 2;
                }
                Connection.allConnect.add(new Connection(stationInConnect));
        }
    }


    //method find Station in table and add to List
    public static void parseStationInTable(Document document, int tableNum, int rowBegin) {

        Element table = document.select("table").get(tableNum);
        Elements rows = table.select("tr");

        for (int i = rowBegin; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            //get Line number
            String currentLineNumber = cols.get(0).getElementsByTag("span").get(0).text();
            String currentLineName = cols.get(0).getElementsByTag("span").attr("title");
            //delete 0 from line number
            if (currentLineNumber.charAt(0) == '0')
                currentLineNumber = currentLineNumber.substring(1);

            //get Station name
            String str = cols.get(1).getElementsByTag("a").attr("title");
            String currentStationName;

            //delete text in (...) from currentStationName
            int ind = str.indexOf('(');
            if (ind != -1)
                currentStationName = str.substring(0, ind - 1);
            else
                currentStationName = str;

            //create station and add to List
            Station.allStation.add(new Station(currentStationName, currentLineNumber, currentLineName));
            if (cols.get(0).getElementsByTag("td").attr("data-sort-value").equals("8.9"))
                Station.allStation.add(new Station(currentStationName, "11", "Большая кольцевая линия"));
        }
    }
}
