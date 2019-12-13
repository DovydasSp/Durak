package gamee;

import Game.Card;
import Game.Durak;
import Game.cardstatic.Static;
import org.assertj.core.util.Arrays;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Parameterized.class)
public class cardGameTest {
    @Parameters
    public static Collection<Quartet<Card, Card, Boolean, String>> data(){
        ArrayList<Quartet<Card, Card, Boolean, String>> list = new ArrayList<>();
        for(int i = 0; i < Static.suits.length; i++) {
            Durak.TRUMP = Static.suits[i];
            System.out.println(Durak.TRUMP);
            for (int j = 0; j < Static.ranks.length; j++) {
                for (int n = 0; n < Static.suits.length; n++) {
                    Card atacker = new Card(Static.ranks[j], Static.suits[n]);
                    for (int o = 0; o < Static.ranks.length; o++) {
                        for (int k = 0; k < Static.suits.length; k++) {
                            Card defender = new Card(Static.ranks[o], Static.suits[k]);
                            if(Static.ranks[j] != Static.ranks[o] && Static.suits[n] != Static.suits[k]){
                                if(Static.values.get(Static.ranks[j]) < Static.values.get(Static.ranks[o])
                                        && Static.suits[n] == Static.suits[k]
                                        || Static.suits[n] != Static.suits[k] && Static.suits[k] == Durak.TRUMP){
                                    System.out.println("Atacker" + Static.suits[n] + " " + Static.ranks[j]);
                                    System.out.println("Defender" + Static.suits[k] + " " + Static.ranks[o]);
                                    list.add(new Quartet<>(atacker, defender, true, Durak.TRUMP));

                                    //assertEquals(result, true);
                                }else{
                                    list.add(new Quartet<>(atacker, defender, false, Durak.TRUMP));
                                    System.out.println("Atacker" + Static.suits[n] + " " + Static.ranks[j]);
                                    System.out.println("Defender" + Static.suits[k] + " " + Static.ranks[o]);
                                    //assertEquals(result, false);

                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public Card atacker;
    public Card defender;
    public Boolean expected;
    public cardGameTest(Quartet<Card, Card, Boolean, String> data){
        atacker = data.getValue0();
        defender = data.getValue1();
        expected = data.getValue2();
        Durak.TRUMP = data.getValue3();
    }

    //@Test
    public void checkIfDefenderIsValid(){
        Game.Pair pair = new Game.Pair(atacker);
        boolean result = true;
        try{
            pair.response(defender);
            result = true;

        }catch (IllegalArgumentException exception){
            result = false;
        }
        assertEquals(expected, result);
    }

}
