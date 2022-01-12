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
        } else if (subJsonObject instanceof JSONObject) {
            // type2: JSONObject
            jsonResultString = ((JSONObject) subJsonObject).toString(INDENT_SPACE);
        } else {
            // type3: String or value(int, float, bigdecimal...)
            jsonResultString = subJsonObject.toString();
        }

        // write back to disk
//        System.out.println("query result class: " + subJsonObject.getClass());
//        System.out.println("query result: \n" + jsonResultString);
        writeJsonStringToLocalDisk(jsonResultString, targetJsonFilePath);
    }

    /* Task 4
     read an XML file into a JSON object, and add the prefix "swe262_" to all of its keys.
      */
    private static void getJsonObjectWithPrefixFromXMLFile(String XMLFilePath, String jsonFilePath) {
        JSONObject jsonObject = getJsonObjectFromXMLFile(XMLFilePath);
        Map<String, Object> map = jsonObject.toMap();
        Map<String, Object> newMap = addPrefixToKeys(map);
        JSONObject newJsonObject = new JSONObject(newMap);
        writeJsonObjectToLocalDisk(newJsonObject, jsonFilePath);
    }

    private static Map<String, Object> addPrefixToKeys(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof HashMap) {
                addPrefixToKeys((HashMap) val);
            } else if (val instanceof ArrayList) {
                for (int i = 0; i < ((ArrayList<?>) val).size(); i++) {
                    if (((ArrayList<?>) val).get(i) instanceof HashMap) {
                        addPrefixToKeys((HashMap) ((ArrayList<?>) val).get(i));
                    }
                }
            }
        }
        modifyKey(map);
        return map;
    }

    private static void modifyKey(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        Set<String> keysToRemove = new HashSet<>();
        for (String key : map.keySet()) {
            String newKey = "swe262_" + key;
            Object val = map.get(key);
            keysToRemove.add(key);
            newMap.put(newKey, val);
        }
        map.keySet().removeAll(keysToRemove);
        // mapping newMap into map
        map.putAll(newMap);
    }

    /* Task5 */
    private static void replaceSubJsonWithAnotherJsonObject(String XMLFilePath, String queryString,
                                                            String targetJsonFilePath) {
        // check if query is valid
        JSONObject oldJson = getJsonObjectFromXMLFile(XMLFilePath);
        try {
            JSONPointer ptr = new JSONPointer(queryString);
            Object result = ptr.queryFrom(oldJson);
            if (result == null) {
                System.out.println("Query Did Not Match Anything. Return Early. ");
                return;
            }
        } catch (JSONPointerException e) {
            e.printStackTrace();
        }

        // new json object
        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("author", "Carl Wang");
        newJsonObject.put("price", 114514);
        newJsonObject.put("genre", "Computer");
        newJsonObject.put("description", "Replaced Old Json Object Test. ");
        newJsonObject.put("id", "bk999");
        newJsonObject.put("title", "Leetcode Book");
        newJsonObject.put("publish_date", "2022-1-11");

        JSONObject newJson = updateJsonObjectWithKeyString(oldJson, queryString, "", newJsonObject);
        writeJsonStringToLocalDisk(newJson.toString(INDENT_SPACE), targetJsonFilePath);
    }


    private static JSONObject updateJsonObjectWithKeyString(JSONObject currentJson, String key,
                                                            String pathPrefix, JSONObject newObj) {
        // get current object's keys
        Iterator iter = currentJson.keys();
        String currentKey;
        String globalKeyPath;
        while (iter.hasNext()) {
            currentKey = (String) iter.next();
            globalKeyPath = pathPrefix + '/' + currentKey;

            // check if match the key.
            if (globalKeyPath.equals(key)) {
                currentJson.put(currentKey, newObj);
                // System.out.println("Match Hit." + globalKeyPath + "  " + currentKey + "  \n" + newObj);
                return currentJson;
            }

            // expanding the nested structure.
            if (currentJson.optJSONObject(currentKey) != null) {
                updateJsonObjectWithKeyString(currentJson.getJSONObject(currentKey), key, globalKeyPath, newObj);
            }

            if (currentJson.optJSONArray(currentKey) != null) {
                JSONArray arr = currentJson.getJSONArray(currentKey);
                String[] paths = key.split("/");

                // find the selected index
                String[] pres = globalKeyPath.split("/");
                for (int i = 1; i < paths.length; ++i) {
                    if (Objects.equals(paths[i - 1], pres[pres.length - 1])) {
                        // if has reached the end
                        if (i == paths.length - 1) {
                            arr.put(Integer.parseInt(paths[paths.length - 1]), newObj);
                            // System.out.println("Match Hit at line 150.  " + globalKeyPath + "  " + currentKey + "  " + newObj);
                            return currentJson;
                        } else {
                            if (paths[i].matches("[0-9]+")) {
                                // update key path with array index
                                int selectedIndex = Integer.parseInt(paths[i]);
                                globalKeyPath = globalKeyPath + "/" + paths[i];
                                // System.out.println("** updated global key path: " + globalKeyPath);
                                updateJsonObjectWithKeyString(arr.getJSONObject(selectedIndex),
                                        key, globalKeyPath, newObj);
                            } else {
                                throw new Error("unexpected reach");
                            }
                        }
                    }
                }
            }

        }


        return currentJson;
    }

    public static void main(String[] args) {
        String xmlFilePath;
        String queryString;

        // parse arguments
        if (args.length > 2) {
            throw new IllegalArgumentException("Only accept zero, one, or two arguments as: <xmlFilePath> <query>");
        }
        else if (args.length == 0) {
            xmlFilePath = "./testcase/books.xml";
            queryString = "/catalog/book/1";
        }
        else if (args.length == 1) {
            xmlFilePath = args[0];
            queryString = "/catalog/book/1";
        }
        else {
            xmlFilePath = args[0];
            queryString = args[1];
        }

        // Task 1
        JSONObject jsonObject;
        jsonObject = getJsonObjectFromXMLFile(xmlFilePath);
        writeJsonObjectToLocalDisk(jsonObject, "./testcase/output/books.json");
        System.out.println("*** Task 1 Done. See output at ./testcase/output/books.json");

        // Task 2 & Task 3
        querySubJsonFromXMLFile(xmlFilePath,
                queryString, "./testcase/output/books_query_result.json");
        System.out.println("*** Task 2, 3 Done. See output at ./testcase/output/books_query_result.json");

        // Task 4
        getJsonObjectWithPrefixFromXMLFile("./testcase/books.xml", "./testcase/output/prefix.json");
        getJsonObjectWithPrefixFromXMLFile("./testcase/small3.xml", "./testcase/output/prefixSmall3.json");
        getJsonObjectWithPrefixFromXMLFile("./testcase/medium1.xml", "./testcase/output/prefixMedium1.json");
        System.out.println("*** Task 4 Done. See output at ./testcase/output/prefix*.json");

        // Task 5
        replaceSubJsonWithAnotherJsonObject(xmlFilePath,
                queryString, "./testcase/output/books_replaced.json");
        System.out.println("*** Task 5 Done. See output at ./testcase/output/books_replaced.json");
    }
}
