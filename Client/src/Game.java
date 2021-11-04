
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Game extends Canvas implements Runnable {

    private static final String gameTitle = "Tilted Towers";
    private static final int width = 1920, height = 1080;
    private static int playerLives = 3;
    private static int score = 0;
    private static ArrayList<Block> blockStack;
    private static Block swingBlock;
    private static BufferedImage background;

    private boolean running;
    private Thread thread;
    private BufferStrategy bs;
    private Graphics g;
    private static long lastFPSCheck = 0;
    private static long msPerFrame = 16;
    private static int centerPosX = 960;
    private static int lastBlockCenterX = 960;

    private static DataInputStream dip;
    private static DataOutputStream dop;


    private KeyManager keyManager;

    public Game() {
        new Window(width, height, gameTitle, this);
    }


    public static void main(String[] args) {
        /*
        Scanner input = new Scanner(System.in);
        boolean connect = true;
        System.out.println("Enter ip address, for example 192.168.1.1 ");
        String ipAddress = input.next();
        System.out.println("The ip address is " + ipAddress);
        System.out.println("Enter port as an integer, for example 2345");
        int port = input.nextInt();
        System.out.println("The port is " + port);
        input.close();
        try {
            //A socket to connect to the server
            Socket connectToServer = new Socket(ipAddress, port);

            dip = new DataInputStream(connectToServer.getInputStream());

            dop = new DataOutputStream(connectToServer.getOutputStream());

            connectToServer.close();
        } catch (IOException ex) {
            System.out.println(ex.toString() + '\n');
        }
            */
        Game game = new Game();
        game.initialize();

    }

    @Override
    public void run() {
        int frames = 0;
        while (running) {


            tick();
            render();
            frames++;
            try {
                Thread.sleep(msPerFrame);
            } catch (InterruptedException e) {
                System.out.println(e);
            }


            //FPS counter
            if (System.nanoTime() > lastFPSCheck + 1000000000) {
                lastFPSCheck = System.nanoTime();
                long currentFPS = frames;
                frames = 0;
                //System.out.println("FPS: " + currentFPS);

            }
        }
        stop();
    }

    public void initialize() {
        //put image and sound loading here
        try {
            background = ImageIO.read(new File("res/gameBond.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyManager = new KeyManager();
        keyManager.setGame(this);
        this.addKeyListener(keyManager);
        swingBlock = new Block(this);
        blockStack = new ArrayList<Block>();
        this.start();
        this.requestFocusInWindow();
    }

    public void start() {
        if (!running) {
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    public void tick() {
        swingBlock.swing();
        swingBlock.fall();

    }

    public void render() {
        bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        //Clear
        g.clearRect(0, 0, width, height);
        //Draw here


        g.drawImage(background, 640, -3240, null);

        g.drawImage(swingBlock.getBlockImg(), swingBlock.getPosX(), swingBlock.getPosY(), null);

        //calibration-line
        g.setColor(Color.black);
        g.fillRect(960, 0, 1, 1080);

        bs.show();
        g.dispose();
        ;

    }

    public void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Block getSwingBlock() {
        return swingBlock;
    }
    public static void setSwingBlock(Block b) {
        swingBlock =b;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public int getStackHeight() {
        return blockStack.size();
    }

    public void setBlockCenter(int x) {
        this.lastBlockCenterX = x;
    }

    public void addBlockToStack(Block b) {
        this.blockStack.add(b);
    }

    public int getLastBlockCenterX() {
        return lastBlockCenterX;
    }

    public int getCenterPosX() {
        return centerPosX;
    }

    public void hitMarker() {
        score++;
        System.out.println("Point!, score is: "+score);
        /*
        try {
            dop.writeInt(lastBlockCenterX);
            dop.flush();
        } catch (IOException ex) {
            System.out.println(ex.toString() + '\n');
        }

         */
    }

    public void missMarker() {
        System.out.println("Miss");

        //lose life
        /*
        try {
            dop.writeInt(lastBlockCenterX);
            dop.flush();
        } catch (IOException ex) {
            System.out.println(ex.toString() + '\n');
        }

         */
    }
}
