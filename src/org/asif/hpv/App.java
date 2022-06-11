package org.asif.hpv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class App {
    public static void main(String[] args) throws Exception {
        String characterName = args[0];
        JSONParser jsonParser = new JSONParser();
        String characterData = loadJson(new File("/home/asif/myprojects/java/hpv/src/org/asif/hpv/characters.json"));
        Object object = jsonParser.parse(characterData);
        JSONArray jsonArray = (JSONArray) object;
        JSONObject wizardObject = findWizardWithName(jsonArray, characterName);
        if (wizardObject != null) {
            display(wizardObject);
        } else {
            System.out.println("Sorry\nWizard not found!!");
        }

    }

    private static JSONObject findWizardWithName(JSONArray jsonArray, String characterName) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject wizObject = (JSONObject) jsonArray.get(i);
            if (wizObject.get("name").toString().equalsIgnoreCase(characterName)) {
                return wizObject;
            }
        }
        return null;
    }

    public static String loadJson(File dataFile) {
        if (!dataFile.exists()) 
           downloadFile(); 
        try {
            return readWholeInputStream(new FileInputStream(dataFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }
    
    private static void downloadFile() {
        String json;
        URL url;
        HttpURLConnection connection;
        File file;
        FileWriter fileWriter;
        try {
            url = new URL("http://hp-api.herokuapp.com/api/characters");
            connection = (HttpURLConnection) url.openConnection();
            file = new File("/home/asif/myprojects/java/hpv/src/org/asif/hpv/characters.json");
            fileWriter = new FileWriter(file);
            if(connection.getResponseCode() == 200) {
                // connection.setRequestMethod("GET");
                json = readWholeInputStream(connection.getInputStream());
                System.out.println("Response Code: " + connection.getResponseCode());
                fileWriter.write(json);
                fileWriter.flush();
            }
            connection.disconnect();
            fileWriter.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public static String readWholeInputStream(InputStream in) {
        String line = "";
        String characterData = "";
        try (InputStreamReader fileReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while((line = bufferedReader.readLine()) != null) {
                characterData += line;
            }
        } catch (IOException e) {
            // System.out.println("Data file not found\nExiting the program...");
            e.printStackTrace();
        }
            return characterData;
    }

    public static void display(JSONObject wizObject) {
        var iterator =  wizObject.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = wizObject.get(key).toString();
            System.out.println(key + " - " + value);
        }
    }
}
