import java.util.ArrayList;
import java.util.List;

public class Line {

    String lineNumber;
    String lineName;
    List<Station> stationsOnLine;
    static ArrayList<Line> lines = new ArrayList<>();

    public Line(String lineNumber, String lineName){
        this.lineNumber = lineNumber;
        this.stationsOnLine = new ArrayList<>();
        this.lineName = lineName;
    }
}