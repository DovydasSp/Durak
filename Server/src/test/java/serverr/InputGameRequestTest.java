package serverr;

import Server.Application;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputGameRequestTest {
    //@Test
    void inputGameTest() throws IOException, JSONException {
        for(int j = 1; j <= 5; j++){
            ApplicationContext context = SpringApplication.run(Application.class);
            for(int i = 0; i < j; i++){
                URL url = new URL("http://localhost:8080/createGame");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                JSONObject creatGameJson = new JSONObject();
                creatGameJson.put("playerName", "test");
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(creatGameJson.toString());
                out.flush();
                out.close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                JSONObject response = new JSONObject(String.valueOf(content));
                assertEquals(true, response.has("gameID"));
                assertEquals(true, response.has("playerID"));

                String gameID = response.getString("gameID");
                String playerID = response.getString("playerID");
                con.disconnect();

                url = new URL("http://localhost:8080/joinGame");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                JSONObject joinGameJson = new JSONObject();
                joinGameJson.put("gameID", gameID);
                joinGameJson.put("playerName", "test");
                con.setDoOutput(true);
                out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(joinGameJson.toString());
                out.flush();
                out.close();

                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                System.out.println(content);
                response = new JSONObject(String.valueOf(content));
                assertEquals(true, response.has("playerID"));


                String secondPlayerID = response.getString("playerID");
                for(int a = -1; a < 10; a++){
                    url = new URL("http://localhost:8080/input");
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    JSONObject inputGameJson = new JSONObject();
                    inputGameJson.put("gameID", gameID);
                    inputGameJson.put("playerID", playerID);
                    inputGameJson.put("command", a);
                    con.setDoOutput(true);
                    out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(inputGameJson.toString());
                    out.flush();
                    out.close();

                    assertEquals(true, con.getResponseCode() == 201);
                }

                for(int a = -1; a < 10; a++){
                    url = new URL("http://localhost:8080/input");
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    JSONObject inputGameJson = new JSONObject();
                    inputGameJson.put("gameID", gameID);
                    inputGameJson.put("playerID", secondPlayerID);
                    inputGameJson.put("command", a);
                    con.setDoOutput(true);
                    out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(inputGameJson.toString());
                    out.flush();
                    out.close();

                    assertEquals(true, con.getResponseCode() == 201);
                }
            }
            SpringApplication.exit(context);
        }
    }
}
