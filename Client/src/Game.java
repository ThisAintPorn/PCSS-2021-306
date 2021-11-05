
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable {

    private static final String gameTitle = "Tilted Towers";
    private static final int width = 1920, height = 1080, blockWidth = 213, blockHeight = 219, fallMargin = 107, leftOpponentBound = 640, rightOpponentBound = 1067; //1280-213
    private static int playerLives = 3, score = 0, bottomBoundY = 512;
    private static ArrayList<Block> blockStack;
    private static Block swingBlock;


    private static BufferedImage background;
    private static int backgroundPosY = -3240;

    private boolean running;
    private Thread thread;
    private BufferStrategy bs;
    private Graphics g;
    private static long lastFPSCheck = 0;
    private static long msPerFrame = 16;
    private static int centerPosX = 960;
    private static int lastBlockCenterX = 960;
    private static String p1Score, p2Score, p3Score;

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
            background = ImageIO.read(new File("res/background.png"));
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
        g.drawImage(background, 640, backgroundPosY, null);

        g.drawImage(swingBlock.getBlockImg(), swingBlock.getPosX(), swingBlock.getPosY(), null);

        for (int i = 0; i < blockStack.size(); i++) {
            g.drawImage(blockStack.get(i).getBlockImg(), blockStack.get(i).getPosX(), blockStack.get(i).getPosY(), null);
        }
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.drawString(Integer.toString(score),640,25);
        g.drawString(Integer.toString(playerLives),1265,25);

        bs.show();
        g.dispose();
    }

    public void moveUp() {
        //backgroundInitPosY+=1+getStackHeight();
        //bottomBoundY+=1+getStackHeight();
        backgroundPosY++;
        bottomBoundY++;
        for (int i = 0; i < blockStack.size(); i++) {
            blockStack.get(i).moveOneUp();
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
}
