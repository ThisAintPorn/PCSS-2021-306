
import javax.imageio.ImageIO;
import java.awt.*;
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
    private static final int width = 1920, height = 1080, blockWidth = 213, blockHeight = 219, fallMargin = 107, leftOpponentBound = 640, rightOpponentBound = 1067; //1280-213
    private static int playerLives = 3, score = 0, bottomBoundY = 512;
    private static ArrayList<Block> blockStack, leftBlockStack, rightBlockStack;
    private static Block swingBlock;


    private static BufferedImage background, blockImg;
    private static int backgroundPosY = -3240, leftBackgroundY = -3240, rightBackgroundY = -3240;


    private boolean running;
    private Thread thread;
    private BufferStrategy bs;
    private Graphics g;
    private static long lastFPSCheck = 0;
    private static long msPerFrame = 16;
    private static int centerPosX = 960;
    private static int lastBlockCenterX = 960;
    private static Window window;

    boolean connect = true;
    boolean firstTimeId = true;
    int playerId = 0;
    private int p1score, p2score, p3score, p1lives, p2lives, p3lives;
    private String ipAddress;
    private int port;
    private boolean sendBool;

    private KeyManager keyManager;

    public Game() {
        //new Window(width, height, gameTitle, this);
    }


    public static void main(String[] args) {
        Game game = new Game();

        /*
        //traffic
        Scanner input = new Scanner(System.in);
        System.out.println("Enter ip address for example 192.168.1.1");
        game.ipAddress = input.next();
        System.out.println("The ip address is " + game.ipAddress);
        System.out.println("Enter port as an integer, for example 2345");
        game.port = input.nextInt();
        System.out.println("The port is " + game.port);
        input.close();
        */

        window = new Window(width, height, gameTitle, game);


        game.initialize();
        game.traffic(); //creates new thread for traffic


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
            background = ImageIO.read(new File("res/background.png"));
            blockImg = ImageIO.read(new File("res/block.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyManager = new KeyManager();
        keyManager.setGame(this);
        this.addKeyListener(keyManager);
        swingBlock = new Block(this);
        blockStack = new ArrayList<Block>();
        this.start();
        //window.toTop();
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
        moveUp();
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
        g.drawImage(background, 0, leftBackgroundY, null);
        g.drawImage(background, 640, backgroundPosY, null);
        g.drawImage(background, 1280, rightBackgroundY, null);

        for (int i = 0; i < blockStack.size(); i++) {
            g.drawImage(blockImg, blockStack.get(i).getPosX(), blockStack.get(i).getPosY(), null);
        }

        g.drawImage(blockImg, swingBlock.getPosX(), swingBlock.getPosY(), null);


        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString(Integer.toString(score), 640, 25);
        g.drawString(Integer.toString(playerLives), 1265, 25);

        bs.show();
        g.dispose();
    }

    public void moveUp() {
        backgroundPosY++;
        bottomBoundY++;
        for (int i = 0; i < blockStack.size(); i++) {
            blockStack.get(i).moveOneUp();
        }
       /* for (int i = 0; i < leftBlockStack.size(); i++) {
            leftBlockStack.get(i).moveOneUp();
        }
        for (int i = 0; i < rightBlockStack.size(); i++) {
            rightBlockStack.get(i).moveOneUp();
        }*/

    }

    public void moveUpLeft() {
        leftBackgroundY += 219;
        for (int i = 0; i < blockStack.size(); i++) {
            leftBlockStack.get(i).setPosY(getY() + blockHeight);
        }
    }

    public void moveUpRight() {
        leftBackgroundY += 219;
        for (int i = 0; i < rightBlockStack.size(); i++) {
            rightBlockStack.get(i).setPosY(getY() + blockHeight);
        }
    }

    public void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hitMarker() {
        setSendBool(true);
        score++;
        System.out.println("Point!, score is: " + score);
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
        playerLives--;
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

    public void traffic() {
        new Thread(() -> {
            try {//A socket to connect to the server
                Socket connectToServer = new Socket(ipAddress, port);

                DataInputStream dip = new DataInputStream(connectToServer.getInputStream());

                DataOutputStream dop = new DataOutputStream(connectToServer.getOutputStream());

                while (connect) {

                    //The first time the connection happens, the client receives the player ID
                    if (firstTimeId == true) {
                        playerId = dip.readInt();
                        firstTimeId = dip.readBoolean();
                    }

                    //Send score to the server
                    dop.writeInt(score);

                    //Send lives to the server
                    dop.writeInt(playerLives);

                    //Block position
                    //INSERT HERE YO
                    if(sendBool){
                        dop.writeBoolean(true);
                        dop.writeInt(lastBlockCenterX);
                        sendBool=false;
                    }

                    //Receive other players' data from server
                    if (playerId == 1) {
                        p2score = dip.readInt();
                        p2lives = dip.readInt();
                        p3score = dip.readInt();
                        p3lives = dip.readInt();


                    } else if (playerId == 2) {
                        p1score = dip.readInt();
                        p1lives = dip.readInt();
                        //add a read of players lastBlocCenterX
                        p3score = dip.readInt();
                        p3lives = dip.readInt();
                        //add a read of players lastBlocCenterX

                    } else if (playerId == 3) {
                        p1score = dip.readInt();
                        p1lives = dip.readInt();
                        //add a read of players lastBlocCenterX
                        p2score = dip.readInt();
                        p2lives = dip.readInt();
                        //add a read of players lastBlocCenterX
                    }

                }
                connectToServer.close();
            } catch (IOException ex) {
                System.out.println(ex.toString() + '\n');
            }
        }).start();
    }

    //getters and setters below
    public static Block getSwingBlock() {
        return swingBlock;
    }

    public static void setSwingBlock(Block b) {
        swingBlock = b;
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

    public static int getPlayerLives() {
        return playerLives;
    }

    public static void setPlayerLives(int playerLives) {
        Game.playerLives = playerLives;
    }

    public static ArrayList<Block> getBlockStack() {
        return blockStack;
    }

    public static void setBlockStack(ArrayList<Block> blockStack) {
        Game.blockStack = blockStack;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Game.score = score;
    }

    public static BufferedImage getBackgroundimg() {
        return background;
    }

    public static void setBackground(BufferedImage background) {
        Game.background = background;
    }

    public static int getBlockWidth() {
        return blockWidth;
    }

    public static int getBlockHeight() {
        return blockHeight;
    }

    public static int getFallMargin() {
        return fallMargin;
    }

    public static int getBottomBoundY() {
        return bottomBoundY;
    }

    public static void setBottomBoundY(int y) {
        bottomBoundY = y;
    }

    public static int getLeftOpponentBound() {
        return leftOpponentBound;
    }

    public static int getRightOpponentBound() {
        return rightOpponentBound;
    }

    public int getBackgroundPosY() {
        return backgroundPosY;
    }

    public void setBackgroundPosY(int y) {
        backgroundPosY = y;
    }

    public void setSendBool(boolean b){
        sendBool=b;
    }
}
