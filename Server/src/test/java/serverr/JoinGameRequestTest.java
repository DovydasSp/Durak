package serverr;

import Server.Application;
import org.javatuples.Pair;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Parameterized.class)
public class JoinGameRequestTest {
    @Parameters
    public static Collection<Pair<Boolean, String>> data() throws IOException, JSONException {
        ArrayList<Pair<Boolean, String>> list = new ArrayList<>();
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
                if(response.has("gameID") && response.has("playerID")){
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
                    list.add(new Pair<>(true, String.valueOf(content)));
                } else {
                    list.add(new Pair<>(true, ""));
                }
            }
            SpringApplication.exit(context);
        }

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
                if(response.has("gameID") && response.has("playerID")){
                    String gameID = response.getString("gameID");
                    String playerID = response.getString("playerID");
                    con.disconnect();

                    url = new URL("http://localhost:8080/joinGame");
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    JSONObject joinGameJson = new JSONObject();
                    joinGameJson.put("gameID", gameID+"xxxx");
                    joinGameJson.put("playerName", "test");
                    con.setDoOutput(true);
                    out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(joinGameJson.toString());
                    out.flush();
                    out.close();

                    if(con.getResponseCode() >= 400){
                        in = new BufferedReader(
                                new InputStreamReader(con.getErrorStream()));
                        content = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();
                        list.add(new Pair<>(false, String.valueOf(content)));
                    }
                    else
                        list.add(new Pair<>(true, ""));
                }
                else
                    list.add(new Pair<>(true, ""));
            }
            SpringApplication.exit(context);
        }
        return list;
    }


    public String data;
    public Boolean expectedResult;

    public JoinGameRequestTest(org.javatuples.Pair<Boolean, String> data){
        this.data = data.getValue1();
        expectedResult = data.getValue0();
    }

    //@Test
    public void joinGameTest() throws IOException, JSONException {
        JSONObject testData = new JSONObject(data);
        assertEquals(expectedResult, testData.has("playerID"));
    }
}
