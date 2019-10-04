package durak;

import durak.GameDataClasses.Field;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

public class FieldObserver implements Observer{
    Field field = new Field();
    GameUI game;
    JPanel handCardPanel;
    JPanel tablePanel;

    FieldObserver(GameUI game_, JPanel handCardPanel_, JPanel tablePanel_) {
        game = game_;
        handCardPanel = handCardPanel_;
        tablePanel = tablePanel_;
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        field = (Field)arg;
        game.refreshTable(handCardPanel, tablePanel, field);
    }
    
}
