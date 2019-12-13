package serverr;
import Game.Card;
import Game.Durak;
import Game.cardstatic.Static;
import Server.Application;
import org.javatuples.Pair;
import org.javatuples.Quartet;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*@RunWith(Parameterized.class)
public class CreateGameRequestTest {
    //@Parameters
    public static Collection<Pair<Boolean, String>> data() throws IOException, JSONException {
        ArrayList<Pair<Boolean, String>> list = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            ApplicationContext context = SpringApplication.run(Application.class);
            for (int i = 0; i < j; i++) {
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
                list.add(new Pair<>(true, String.valueOf(content)));
            }
            SpringApplication.exit(context);
        }

        for (int j = 1; j <= 5; j++) {
            ApplicationContext context = SpringApplication.run(Application.class);
            for (int i = 0; i < j; i++) {
                URL url = new URL("http://localhost:8080/createGame");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                JSONObject creatGameJson = new JSONObject();
                creatGameJson.put("playerNamee", "test");
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(creatGameJson.toString());
                out.flush();
                out.close();

                //System.out.println(con.getResponseMessage());
                if (con.getResponseCode() >= 400) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getErrorStream()));
                    String inputLine;
                    //System.out.println(in.lines());
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    in.close();
                    System.out.println(content);
                    list.add(new Pair<>(false, String.valueOf(content)));
                } else
                    list.add(new Pair<>(false, ""));
            }
            SpringApplication.exit(context);
        }


        return list;
    }

    public String data;
    public Boolean expectedResult;

    public CreateGameRequestTest(org.javatuples.Pair<Boolean, String> data) {
        this.data = data.getValue1();
        expectedResult = data.getValue0();
    }

    //@Test
    public void createGameTest() throws JSONException {
        JSONObject testData = new JSONObject(data);
        assertEquals(expectedResult, testData.has("gameID"));
        assertEquals(expectedResult, testData.has("playerID"));
    }
}*/
