package MileStone1;

import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MileStone1 {
    private final static int INDENT_SPACE = 4;

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
            fw.write(jsonObject.toString(INDENT_SPACE));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonStringToLocalDisk(String jsonString, String jsonFilePath) {
        try {
            FileWriter fw = new FileWriter(jsonFilePath);
            fw.write(jsonString);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void querySubJsonFromXMLFile(String XMLFilePath, String queryString,
                                                      String targetJsonFilePath) {
        JSONObject jsonObject = getJsonObjectFromXMLFile(XMLFilePath);
        Object subJsonObject = null;
        String jsonResultString;

        try {
            JSONPointer ptr = new JSONPointer(queryString);
            subJsonObject = ptr.queryFrom(jsonObject);

        } catch (JSONPointerException e) {
            e.printStackTrace();
        }

        if (subJsonObject == null) {
            System.out.println("** The Given Key Path Did Not Match, Discard The Query! **");
            return;
        }

        // different cast with given type
        if (subJsonObject instanceof JSONArray) {
            // type1: JSONArray
            jsonResultString = ((JSONArray) subJsonObject).toString(INDENT_SPACE);
        }
        else if (subJsonObject instanceof JSONObject) {
            // type2: JSONObject
            jsonResultString = ((JSONObject) subJsonObject).toString(INDENT_SPACE);
        }
        else {
            // type3: String or value(int, float, bigdecimal...)
            jsonResultString = subJsonObject.toString();
        }

        // write back to disk
        System.out.println("query result class: " + subJsonObject.getClass());
        System.out.println("query result: \n" + jsonResultString);
        writeJsonStringToLocalDisk(jsonResultString, targetJsonFilePath);
    }

    public static void main(String[] args) {

        // Task 1
        JSONObject jsonObject;
        jsonObject = getJsonObjectFromXMLFile("./testcase/books.xml");
        writeJsonObjectToLocalDisk(jsonObject, "./testcase/output/books.json");


        // Task 2 & Task 3
        querySubJsonFromXMLFile("./testcase/books.xml",
                "/catalog/book/4","./testcase/output/books_query_result.json");
        //writeJsonObjectToLocalDisk();


    }
}
