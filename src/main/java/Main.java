import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.*;

public class Main {
    public static void main(String [] args) {
       // System.out.println("Jva here");
        String apiUrl = "https://api.github.com/users/kamranahmedse/events";
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(apiUrl)).header("Accept", "application/json").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if(response.statusCode() == HttpURLConnection.HTTP_OK) {
               // System.out.println("Response : "+response.body());
                JsonParser parser = new JsonParser();
                JsonArray array = parser.parse(response.body()).getAsJsonArray();
                displayActivity(array);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }


    }
    private static void displayActivity(JsonArray jsonArray) {

       
        for (JsonElement element : jsonArray) {
            JsonObject obj = element.getAsJsonObject();
            String type = obj.get("type").getAsString();
            String action;
            switch (type) {
                case "PushEvent":
                    int commitCount = obj.get("payload").getAsJsonObject().get("commits").getAsJsonArray().size();
                    action = "Pushed " + commitCount + " commit(s) to " + obj.get("repo").getAsJsonObject().get("name").getAsString();
                    break;
                case "WatchEvent":
                    action = "Starred "+obj.get("repo").getAsJsonObject().get("name").getAsString();
                    break;
                case "IssueCommentEvent":
                    action = obj.get("payload").getAsJsonObject().get("action").getAsString().toUpperCase().charAt(0)+
                            obj.get("payload").getAsJsonObject().get("action").getAsString().substring(1)+" an issue in "+ obj.get("repo").getAsJsonObject().get("name").getAsString();
                    break;
                case "ForkEvent":
                    action="Forked "+obj.get("report").getAsJsonObject().get("name").getAsString();
                    break;
                case "CreateEvent":
                    action = "Created " + obj.get("payload").getAsJsonObject().get("ref_type").getAsString()
                            + " in " + obj.get("repo").getAsJsonObject().get("name").getAsString();
                    break;
                default:
                    action = obj.get("type").getAsString().replace("Event", "")
                            + " in " + obj.get("repo").getAsJsonObject().get("name").getAsString();
                    break;
            }
            System.out.println(action);
        }
       
    }
    }
