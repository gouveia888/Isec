package Ex1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import javax.json.Json;
import java.io.*;
import java.net.*;

public class Main {

    //Download package javax e gson
    /*
    public static void getCaracter(String qouteId) throws Exception {
        HttpURLConnection connection;
        String uri = "https://potterapi-fedeperin.vercel.app/en/characters?index=x";
        URL url = new URI(uri).toUrl();
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml, *//*"); //apagar uma /        InputStream in = connection.getInputStream();

        JsonReader jsonReader = Json.createReader(in);
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        connection.disconnect();

        Gson gson = new GsonBuilder().create();
        Caracter q = gson.fromJson(object.toString(), Caracter.class);
    }
    */

    public static void alineaD() throws URISyntaxException, IOException {
        String uri = "https://potterapi-fedeperin.vercel.app/en/characters";
        URL url = new URI(uri).toURL(); //usar try with resources
        HttpURLConnection connection;

        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml, */*");

        InputStream jsonStream = connection.getInputStream();

        JsonReader jsonReader = Json.createReader(jsonStream);
        JsonArray array = jsonReader.readArray();
        jsonReader.close(); connection.disconnect();

        for(int i=0; i<array.size(); i++){
            JsonObject object = array.getJsonObject(i);
            Gson gson = new GsonBuilder().create();
            Character q = gson.fromJson(object.toString(), Character.class);
            System.out.println("Citacao: " + q.getValue().getQuote());

            URL imageURL = new URL(q.getImage());
            String [] names = q.getName().split(" ");
        }

        try{
            InputStream in = imageURL.openStream();
            FileOutputStream out = new FileOutputStream(outputFilePathh)

                    out.write(in.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void SemJSON(){
        try{
            URL url = new URI(API_URI).toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setRequestProperty("Accept", "application/json");

            InputStream in = connection.getInputStream();
            JsonReader jsonReader = Json.createReader(in);
            JsonArray arrayOfCharacters = jsonReader.readArray();

            jsonReader.close();
            connection.disconnect();

            //byte [] buffer = new byte[BUFF_SIZE];

            for(int i=0; i<arrayOfCharacters.size(); i++){
                JsonObject object = arrayOfCharacters.getJsonObject(i);
                Gson gson = new GsonBuilder().create();
                Character character = gson.fromJson(object.toString(), Character.class);

                System.out.printf("(id: %d) %s", character.getIndex(), character.getFullName());
                //System.out.printf("(id: %d) %s", object.getInt("index"), object.getString("fullName"));
                System.out.println();

                URL imageUrl = new URI(character.getImage()).toURL();
                //URL imageUrl = new URI(object.getString("image")).toURL();

                String [] names = character.getFullName().split(" ");
                //String [] names = object.getString("fullName").split(" ");
                String outputFilePath = localDirectory.getCanonicalPath() + File.separator +
                        names[0] + (names.length > 1 ? "-" + names[names.length-1] : "") + ".png";

                try(InputStream inputStreamImage = imageUrl.openStream();
                    FileOutputStream out = new FileOutputStream(outputFilePath)){

                    /*int nbytes;
                    while((nbytes = inputStreamImage.read(buffer))!=-1) {
                        out.write(buffer, 0, nbytes);
                    }*/

                    out.write(inputStreamImage.readAllBytes());

                }catch(IOException ex) {
                    System.out.println(ex);
                }
            }

        } catch (URISyntaxException | IOException ex){
            System.out.println(ex);
        }

    }


    public static void main(String args[]){

        if(args.length != 0){
            System.out.println("Uso: java Ex1.Main");
            System.exit(1);
        }

        File file = new File(args[0]);

        if(!file.exists()){
            System.out.println("Ficheiro nao existe");
            System.exit(1);
        }

        if(!file.isFile() || !file.canRead()){
            System.out.println("Ficheiro invalido");
            System.exit(1);
        }

        try{
            alineaD();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
