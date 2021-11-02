import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable {

    private static final String gameTitle= "Tilted Towers";
    private static final int width=1920,height=1080;
    private static int playerLives=3;
    private static ArrayList<Block> stackedBlocks;
    private static Block swingBlock;

    private boolean running;
    private Thread thread;
    private static long lastFPSCheck=0;

    public Game(){
        new Window(width,height,gameTitle,this);
    }

    public static void main(String[] args){
        new Game();
    }

    @Override
    public void run() {
        double delta = 0;
        int frames = 0;

        while(running){

            while(delta>=1) {
            tick();
            delta--;
            }
            if(running){
                render();
            }
            frames++;

            if(System.nanoTime() > lastFPSCheck + 1000000000 ){
                lastFPSCheck =System.nanoTime();
                long currentFPS = frames;
                frames=0;
                System.out.println("FPS: "+currentFPS);
            }
        }
        stop();
    }

    public void start(){
        if(!running) {
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    public void tick(){
    }

    public void render(){
        BufferStrategy buffStrat = this.getBufferStrategy();
        if (buffStrat == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffStrat.getDrawGraphics();

        //Draw here
        g.setColor(Color.blue);
        g.fillRect(640,0,640,1080);


        buffStrat.show();
        g.dispose();;

    }
    public void stop(){
        try{
            thread.join();
            running = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setSwingBlock(Block b){
       swingBlock = b;
    }
}
