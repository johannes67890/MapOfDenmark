package Address;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class HelloFX extends Application {

    ArrayList<String> cityNames, streetNames, postCodes;
    TextField input;
    TextArea output;
    BorderPane pane;
    Scene scene;


    /**
     * Reads files used later in {@link HelloFX its class}
     */
    public void readFiles(){


        cityNames = new ArrayList<>();
        streetNames = new ArrayList<>();
        postCodes = new ArrayList<>();
        String current;
        String[] splitCurrent;

        try{

            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/citynames.txt"));
            while (reader.ready()) {
                cityNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/streetnames.txt"));
            while (reader.ready()){
                streetNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/postnumre.txt"));
            while (reader.ready()){
                current = reader.readLine();
                splitCurrent = current.split(" ");

                postCodes.add(splitCurrent[0]);
            }
            reader.close();
        
        } catch(IOException e){
            System.out.println("FILE NOT FOUND");
        }
    }



    @Override
    public void start(Stage stage) {

        readFiles();
       
        input = new TextField();
        output = new TextArea();
        pane = new BorderPane();

        pane.setTop(input);
        pane.setCenter(output);

        //Instantiates Address
        input.setOnAction(e->{

            searchForAdress(input.getText());
                        
        });
        startScene(stage);
    }

    public void startScene(Stage stage){


        scene = new Scene(pane);

        stage.setTitle("Address Parsing");
        stage.setScene(scene);
        stage.show();

    }


    /**
     * 
     * Looks for single resembling components for the address (streets, cities, postcodes)
     * Also creates a new address based on input
     * 
     * @param input String that will be contructed into an address.
     */
    public void searchForAdress(String input){
        Address a = Address.parse(input);
            output.setText(a.toString());

            long time = System.currentTimeMillis();

            if(cityNames.contains(a.city)){
                System.out.println("CITY FOUND: " + a.city);
            } else{

                String topString = findSimilar(cityNames, a.city);

                if (topString != null){

                    System.out.println("Mente du: " + topString + "?");

                } else{System.out.println("This City: " + a.city + " does not exist");}

            }
            if(streetNames.contains(a.street)){
                System.out.println("STREET FOUND: " + a.street);
            } else{

                String topString = findSimilar(streetNames, a.street);

                if (topString != null){
                    System.out.println("Mente du: " + topString + "?");
                } else{System.out.println("This City: " + a.street + " does not exist");}

            } 
            System.out.println("Time: " + (System.currentTimeMillis() - time));
            System.out.println(a.street);


    }


    /**
     * Finds the most similar string in a list with another string
     * 
     * <ul>
     * <li>Note that some strings might be equally similar</li>
     * </ul>
     * @param list the list to compare with the string
     * @param s the string to be compared
     * @return a string in the list that resembles s the most
     */
    public String findSimilar(ArrayList<String> list, String s){
        String topString = null;

        int maxSim = Integer.MAX_VALUE;
        int current;

        for (String cityName : cityNames){
            current = Commons.StringUtility.getLevenshteinDistance(cityName, s);
            if (current < maxSim){
                maxSim = current;
                topString = cityName;
            }
        }
        return topString;
    }

    public ArrayList<String> getCities(){
        return cityNames;
    }
    public ArrayList<String> getStreets(){
        return streetNames;
    }
    public ArrayList<String> getPostCodes(){
        return postCodes;
    }


    public static void main(String[] args) {
        launch();
    }

}