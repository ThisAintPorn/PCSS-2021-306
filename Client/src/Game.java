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
    private static final int width = 1920, height = 1080, blockWidth = 213, blockHeight = 219, fallMargin = 107,
            backgroundHeight = 4320,laneWidth=640, leftOpponentBound = 640, rightOpponentBound = 1067; //1280-213
    private static int playerLives = 3, score = 0, bottomBoundY = 512, enemyBottomBounds = 512;
    private static ArrayList<Block> blockStack, leftBlockStack, rightBlockStack;
    private static Block swingBlock;
    private static String gameState;


    private static BufferedImage background, blockImg, loopbackground, waitingScreen;

    //initial positions for backgrounds
    private static int backgroundPosY = -3240, leftBackgroundY = -3240, rightBackgroundY = -3240,
            loopBackground1PosY = -7560, loopBackground2PosY = -3240 - (2 * backgroundHeight),
            leftloopBackground1PosY = -7560, leftloopBackground2PosY = -3240 - (2 * backgroundHeight),
            rightloopBackground1PosY = -7560, rightloopBackground2PosY = -3240 - (2 * backgroundHeight);

    private boolean running;
    private Thread thread;
    private BufferStrategy bs;
    private Graphics g;
    private static long lastFPSCheck = 0;
    private static long msPerFrame = 16;
    private static int centerPosX = 960; //not used right now...
    private static int lastBlockCenterX = 960, leftLastBlockcenterX, rightLastBlockcenterX;
    private static Window window;

    boolean connect = true;
    boolean firstTimeId = true;
    int playerId = 0;
    private int p1score, p2score, p3score, p1lives, p2lives, p3lives, leftScore, rightScore, leftLives, rightLives;
    private String ipAddress;
    private int port;
    private boolean sendBool, startGame;
    boolean waitForStart = true;

    private KeyManager keyManager;

    public Game() {
        //new Window(width, height, gameTitle, this);
    }


    public static void main(String[] args) {
        Game game = new Game();


        // Connecting
        Scanner input = new Scanner(System.in);
        System.out.println("Enter ip address for example 192.168.1.1");
        game.ipAddress = input.next();
        System.out.println("The ip address is " + game.ipAddress);
        System.out.println("Enter port as an integer, for example 2345");
        game.port = input.nextInt();
        System.out.println("The port is " + game.port);
        input.close();


        window = new Window(width, height, gameTitle, game);


        game.initialize();//initializes game data and assets
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
            /*
            //FPS counter
            if (System.nanoTime() > lastFPSCheck + 1000000000) {
                lastFPSCheck = System.nanoTime();
                long currentFPS = frames;
                frames = 0;
                System.out.println("FPS: " + currentFPS);
            }
            */
        }
        stop();
    }

    public void initialize() {
        //put image and sound loading here
        try {
            background = ImageIO.read(new File("res/background.png"));
            blockImg = ImageIO.read(new File("res/block.png"));
            loopbackground = ImageIO.read(new File("res/loopbackground.png"));
            waitingScreen = ImageIO.read(new File("res/waitingscreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyManager = new KeyManager();
        keyManager.setGame(this);
        this.addKeyListener(keyManager);
        swingBlock = new Block(this);
        blockStack = new ArrayList<Block>();
        leftBlockStack = new ArrayList<Block>();
        rightBlockStack = new ArrayList<Block>();
        this.start();
        //window.toTop();
        gameState = "waitingForPlayers";
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
        switch (gameState) {
            case "waitingForPlayers":
                // waiting screen and game title
                break;
            case "playing":
                swingBlock.swing();
                swingBlock.fall();
                moveUp();
                break;
        }
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
        switch (gameState) {
            case "waitingForPlayers":
                // draw waiting screen
                g.drawImage(waitingScreen, 0, 0, null);

                break;
            case "playing":
                //background for center player
                g.drawImage(background, laneWidth, backgroundPosY, null);
                //background for left opponent
                g.drawImage(background, 0, leftBackgroundY, null);
                //background for right opponent
                g.drawImage(background, 2*laneWidth, rightBackgroundY, null);

                //looping backgrounds for center player
                g.drawImage(loopbackground, 640, loopBackground1PosY, null);
                if (loopBackground1PosY > 1080) {
                    loopBackground1PosY -= 2 * backgroundHeight;
                }
                g.drawImage(loopbackground, 640, loopBackground2PosY, null);
                if (loopBackground2PosY > 1080) {
                    loopBackground2PosY -= 2 * backgroundHeight;
                }

                //looping backgrounds for left player
                g.drawImage(loopbackground, 0, leftloopBackground1PosY, null);
                if (leftloopBackground1PosY > 1080) {
                    leftloopBackground1PosY -= 2 * backgroundHeight;
                }
                g.drawImage(loopbackground, 0, leftloopBackground2PosY, null);
                if (leftloopBackground2PosY > 1080) {
                    leftloopBackground2PosY -= 2 * backgroundHeight;
                }

                //looping backgrounds for right player
                g.drawImage(loopbackground, 1280, rightloopBackground1PosY, null);
                if (rightloopBackground1PosY > 1080) {
                    rightloopBackground1PosY -= 2 * backgroundHeight;
                }
                g.drawImage(loopbackground, 1280, rightloopBackground2PosY, null);
                if (rightloopBackground2PosY > 1080) {
                    rightloopBackground2PosY -= 2 * backgroundHeight;
                }

                //Drawing center players blockstack
                for (int i = 0; i < blockStack.size(); i++) {
                    g.drawImage(blockImg, blockStack.get(i).getPosX(), blockStack.get(i).getPosY(), null);
                }

                //Drawing left players blockstack
                for (int i = 0; i < leftBlockStack.size(); i++) {
                    g.drawImage(blockImg, leftBlockStack.get(i).getPosX(), leftBlockStack.get(i).getPosY(), null);
                }

                //Drawing right players blockstack
                for (int i = 0; i < rightBlockStack.size(); i++) {
                    g.drawImage(blockImg, rightBlockStack.get(i).getPosX(), rightBlockStack.get(i).getPosY(), null);
                }


                //Drawing swinging block
                g.drawImage(blockImg, swingBlock.getPosX(), swingBlock.getPosY(), null);


                //Drawing center players scores and lives
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                if (playerLives > 0) {
                    g.drawString("Lives: " + Integer.toString(playerLives), 1175, 25);
                    g.drawString("Score: " + Integer.toString(score), 640, 25);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(640, 0, 640, height);
                    g.setColor(Color.white);
                    g.drawString("YOU LOST", 640 + blockWidth, (height / 2));
                    g.drawString("      Your final score was: " + score, 640 + fallMargin, (height / 2) + 30);
                }

                //drawing left players scores and lives
                if (leftLives > 0) {
                    g.drawString("Lives: " + Integer.toString(leftLives), 535, 25);
                    g.drawString("Score: " + Integer.toString(leftScore), 0, 25);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 640, height);
                    g.setColor(Color.white);
                    g.drawString("YOU LOST", 0 + blockWidth, (height / 2));
                    g.drawString("      Your final score was: " + leftScore, 0 + fallMargin, (height / 2) + 30);
                }
                //drawing right players scores and lives
                if (rightLives > 0) {
                    g.drawString("Lives: " + Integer.toString(rightLives), 1815, 25);
                    g.drawString("Score: " + Integer.toString(rightScore), 1280, 25);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(1280, 0, 640, height);
                    g.setColor(Color.white);
                    g.drawString("YOU LOST", 1280 + blockWidth, (height / 2));
                    g.drawString("      Your final score was: " + rightScore, 1280 + fallMargin, (height / 2) + 30);
                }
                break;

        }
        bs.show();
        g.dispose();

    }

    //moves up blocks and background continuously
    public void moveUp() {
        backgroundPosY++;
        bottomBoundY++;
        loopBackground1PosY++;
        loopBackground2PosY++;
        for (int i = 0; i < blockStack.size(); i++) {
            blockStack.get(i).moveOneUp();
        }
    }

    //moves up left players blocks and backgrounds 1 block at a time
    public void moveUpLeft() {
        leftBackgroundY += blockHeight;
        leftloopBackground1PosY += blockHeight;
        leftloopBackground2PosY += blockHeight;
        for (int i = 0; i < leftBlockStack.size(); i++) {
            leftBlockStack.get(i).setPosY(leftBlockStack.get(i).getPosY() + blockHeight);
        }
    }

    //moves up right players blocks and backgrounds 1 block at a time
    public void moveUpRight() {
        rightBackgroundY += blockHeight;
        rightloopBackground1PosY += blockHeight;
        rightloopBackground2PosY += blockHeight;
        for (int i = 0; i < rightBlockStack.size(); i++) {
            rightBlockStack.get(i).setPosY(rightBlockStack.get(i).getPosY() + blockHeight);
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
    }

    public void missMarker() {
        playerLives--;
        System.out.println("Miss");
    }

    public void traffic() {
        new Thread(() -> {
            try {//A socket to connect to the server
                Socket connectToServer = new Socket(ipAddress, port);

                DataInputStream dip = new DataInputStream(connectToServer.getInputStream());

                DataOutputStream dop = new DataOutputStream(connectToServer.getOutputStream());

                while (connect) {

                    //The first time the connection happens, the client receives the player ID
                    if (firstTimeId) {
                        playerId = dip.readInt();
                        System.out.println("Player id is: " + playerId);
                        firstTimeId = false;
                        //dip.readBoolean();
                    }


                    if (waitForStart) {
                        startGame = dip.readBoolean();
                        System.out.println("Game started: " + startGame);
                        waitForStart = dip.readBoolean();
                    }

                    if (startGame) {
                    	gameState = "playing";
                    }

                    //Send score to the server
                    dop.writeInt(score);

                    //Send lives to the server
                    dop.writeInt(playerLives);


                    //Send block position
                    if (sendBool) {
                        dop.writeBoolean(true);
                        dop.writeInt(lastBlockCenterX);
                        sendBool = false;
                    } else {
                        dop.writeBoolean(false);
                    }

                    dop.flush();

                    //Receive block position
                    int blockSendId = 0;
                    blockSendId = dip.readInt();
                    if (playerId == 1 && blockSendId == 2) {
                        leftLastBlockcenterX = dip.readInt();
                        addToLeftBlockStack(leftLastBlockcenterX);
                        moveUpLeft();
                    } else if (playerId == 1 && blockSendId == 3) {
                        rightLastBlockcenterX = dip.readInt();
                        addToRightBlockStack(rightLastBlockcenterX);
                        moveUpRight();
                    } else if (playerId == 2 && blockSendId == 1) {
                        leftLastBlockcenterX = dip.readInt();
                        addToLeftBlockStack(leftLastBlockcenterX);
                        moveUpLeft();
                    } else if (playerId == 2 && blockSendId == 3) {
                        rightLastBlockcenterX = dip.readInt();
                        addToRightBlockStack(rightLastBlockcenterX);
                        moveUpRight();
                    } else if (playerId == 3 && blockSendId == 1) {
                        leftLastBlockcenterX = dip.readInt();
                        addToLeftBlockStack(leftLastBlockcenterX);
                        moveUpLeft();
                    } else if (playerId == 3 && blockSendId == 2) {
                        rightLastBlockcenterX = dip.readInt();
                        addToRightBlockStack(rightLastBlockcenterX);
                        moveUpRight();
                    } else {

                    }

                    //Receive other players' data from server
                    if (playerId == 1) {
                        p2score = dip.readInt();
                        p2lives = dip.readInt();
                        p3score = dip.readInt();
                        p3lives = dip.readInt();
                        //write scores and lives to either left or right depending on ID:
                        leftScore = p2score;
                        leftLives = p2lives;
                        rightLives = p3lives;
                        rightScore = p3score;

                    } else if (playerId == 2) {
                        p1score = dip.readInt();
                        p1lives = dip.readInt();
                        p3score = dip.readInt();
                        p3lives = dip.readInt();

                        leftScore = p1score;
                        leftLives = p1lives;
                        rightLives = p3lives;
                        rightScore = p3score;
                    } else if (playerId == 3) {
                        p1score = dip.readInt();
                        p1lives = dip.readInt();
                        p2score = dip.readInt();
                        p2lives = dip.readInt();

                        leftScore = p1score;
                        leftLives = p1lives;
                        rightLives = p2lives;
                        rightScore = p2score;
                    }
                }
                connectToServer.close();
            } catch (IOException ex) {
                System.out.println(ex.toString() + '\n');
            }
        }).start();
    }

    public void addToLeftBlockStack(int x) {
        //x should be leftLastBlockcenterX
        leftBlockStack.add(new Block(this, x - 640 - 107));
    }

    public void addToRightBlockStack(int x) {
        //x should be rightLastBlockcenterX
        rightBlockStack.add(new Block(this, x + 640 - 107));
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

    public int getLoopBackground1PosY() {
        return loopBackground1PosY;
    }

    public void setLoopBackground1PosY(int y) {
        loopBackground1PosY = y;
    }

    public int getLoopBackground2PosY() {
        return loopBackground2PosY;
    }

    public void setLoopBackground2PosY(int y) {
        loopBackground2PosY = y;
    }

    public void setSendBool(boolean b) {
        sendBool = b;
    }

    public int getEnemyBottomBounds() {
        return enemyBottomBounds;
    }

    public static String getGameState() {
        return gameState;
    }

    public void setGameState(String s) {
        gameState = s;
    }
}
