import java.util.Random;

public class Ki {
    Field enemyField;
    int [][] shotmap;

    public Ki(Field enemyField){
        this.enemyField = enemyField;
        shotmap = new int[enemyField.getHeight()][enemyField.getLength()];
    }

    public void shootRandom(){
        Random random = new Random();
    }
}
