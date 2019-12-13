package gamedataclasses;

public class Chat {
    private String enemyName;
    private String message;
    
    public Chat(){
        enemyName = "";
        message = "";        
    }
    
    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
