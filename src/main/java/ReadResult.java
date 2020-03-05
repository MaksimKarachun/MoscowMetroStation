import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadResult {

    static String filePath = "result.json";

    public static void read() {
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Object stationObj = gson.fromJson(br, Object.class);
            Pattern pattern = Pattern.compile("1=\\[.+?}");
            Matcher mather = pattern.matcher(stationObj.toString());
            String str = null;
            if(mather.find()){
                str = mather.group();
            }
            String[] mass = str.split("],");
            for(int i = 0; i < mass.length; i++){
                String[] mass1 = mass[i].split(",");
                System.out.println("Лния метро: " + Line.lines.get(i).lineNumber + " " + Line.lines.get(i).lineName + ", " + "Количество станций на линии: " + mass1.length);
            }

        }
        catch (Exception e){
            e.getMessage();
        }
    }
}
