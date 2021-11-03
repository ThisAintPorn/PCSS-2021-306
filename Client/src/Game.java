
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game extends Canvas implements Runnable {

    private static final String gameTitle= "Tilted Towers";
    private static final int width=1920,height=1080;
    private static int playerLives=3;
    private static ArrayList<Block> stackedBlocks;
    private static Block swingBlock;
    private static BufferedImage background;

    private boolean running;
    private Thread thread;
    private BufferStrategy bs;
    private Graphics g;
    private static long lastFPSCheck=0;

    private KeyManager keyManager;

    public Game(){
        new Window(width,height,gameTitle,this);
    }


    public static void main(String[] args){
        Game game = new Game();
                game.initialize();



    }

    @Override
    public void run() {
        int frames = 0;

        while(running){

            tick();
            render();

            frames++;


                //FPS counter
            if(System.nanoTime() > lastFPSCheck + 1000000000 ){
                lastFPSCheck =System.nanoTime();
                long currentFPS = frames;
                frames=0;
                System.out.println("FPS: "+currentFPS);
                System.out.println("block posX: "+swingBlock.getPosX()+", posY: "+ swingBlock.getPosY());
            }
        }
        stop();
    }

    public void initialize(){
        //put image and sound loading here
        try {
            background = ImageIO.read(new File("res/gameBond.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyManager =new KeyManager();
        keyManager.setGame(this);
        this.addKeyListener(keyManager);
        swingBlock= new Block();
        this.start();
        this.requestFocusInWindow();
    }

    public void start(){
        if(!running) {
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    public void tick(){
        swingBlock.swing();
        swingBlock.fall();

    }

    public void render(){
        bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        //Clear
        g.clearRect(0,0,width,height);
        //Draw here
        g.setColor(Color.white);
        g.fillRect(0,0,1920,1080);

        g.drawImage(background,640,-3240,null);

        g.drawImage(swingBlock.getBlockImg(), swingBlock.getPosX(), swingBlock.getPosY(),null);


        bs.show();
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

    public static Block getSwingBlock() {
        return swingBlock;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}
