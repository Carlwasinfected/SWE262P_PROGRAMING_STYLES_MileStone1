package MileStone1;

import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MileStone1 {

    /* Task 1 read XML file to json object, then save the json file in local disk */
    private static JSONObject getJsonObjectFromXMLFile(String xmlFilePath) {
        JSONObject jsonObject = null;
        try {
            FileReader fr = new FileReader(xmlFilePath);
            jsonObject = XML.toJSONObject(fr);  // Reader
            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("XML File Not Found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("XML File IO Error.");
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static void writeJsonObjectToLocalDisk(JSONObject jsonObject, String jsonFilePath) {
        try {
            FileWriter fw = new FileWriter(jsonFilePath);
            fw.write(jsonObject.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
    }



    public static void main(String[] args) {

        // Task 1
        JSONObject jsonObject = getJsonObjectFromXMLFile("./testcase/small3.xml");
        writeJsonObjectToLocalDisk(jsonObject, "./testcase/output/small3.json");


        //

    }
}
