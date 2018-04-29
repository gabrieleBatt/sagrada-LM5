package it.polimi.ingsw.model;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class DashBoard {
   public static void main (String args[]){
        JSONObject obj = new JSONObject();

        try (FileWriter file = new FileWriter("resources\\dashboards\\test.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
