package gamedataclasses;

public class GameData {
    private Player player;
    private Field field;
    private String whatsChanged = "";
    private String whatsChangedInPlayer = "";

    public GameData() {
        player = new Player();
        field = new Field();
    }

    public void setPlayer(Player player_) {
        player = player_;
        whatsChanged = "player";
    }

    public void setField(Field field_) {
        field = field_;
        whatsChanged = "field";
    }

    public void setWhatsChanged(String w) {
        whatsChanged = w;
    }

    public String getWhatsChanged() {
        return whatsChanged;
    }

    public void setWhatsChangedInPlayer(String w) {
        whatsChangedInPlayer = w;
    }

    public String getWhatsChangedInPlayer() {
        return whatsChangedInPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Field getField() {
        return field;
    }
}
