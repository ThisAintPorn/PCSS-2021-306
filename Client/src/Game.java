import java.awt.Canvas;
public class Game extends Canvas implements Runnable {

    public static final String gameTitle= "Tilted Tower";
    public static final int width=1920,height=640;
    public static int playerLives=3;

    @Override
    public void run() {
    }

    public Game(){
        new Window(width,height,gameTitle,this);
    }

    public void start(){

    }

    public static void main(String[] args){
        new Game();
    }

}
