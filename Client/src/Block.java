import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Block {
    private int posX, posY, blockSpawnY = 0;
    private double fallSpeed = 8, fallAcceleration = 1.0982;
    private boolean falling, swinging, swingLeft;
    private Game game;


    public Block(Game g) {

        this.posX = ThreadLocalRandom.current().nextInt(640, game.getRightOpponentBound() + 1);
        this.posY = blockSpawnY;
        this.swinging = true;
        this.game = g;
    }

    public Block(Game g,int x) {
        this.game = g;
        this.posX = x;
        this.posY = g.getEnemyBottomBounds();
    }

    public void fall() {
        if (falling) {
            System.out.println("falling Speed: " + fallSpeed + "Acceleration: " + fallAcceleration);
            System.out.println("posX: "+ game.getSwingBlock().getPosX() + ",posY: " + game.getSwingBlock().getPosY());
            posY += (int) fallSpeed;
            fallSpeed *= fallAcceleration;
            if (posX + (game.getBlockWidth() / 2) <= (game.getLastBlockCenterX() + game.getFallMargin()) &&
                    posX + (game.getBlockWidth() / 2) >= (game.getLastBlockCenterX() - game.getFallMargin())) {

                if (posY >= game.getBottomBoundY()) { //actually hitting wiht block
                    falling = false;
                    posY = game.getBottomBoundY();
                    game.setBottomBoundY(game.getBottomBoundY() - 219);
                    game.setBlockCenter(posX + (game.getBlockWidth() / 2));
                    game.addBlockToStack(this);
                    game.hitMarker();
                    fallSpeed = 8 + game.getStackHeight();
                    game.setSwingBlock(new Block(game));
                    game.moveUp();
                    if (posY < game.getBlockHeight()) {
                        game.setBackgroundPosY(game.getBackgroundPosY() + game.getBlockHeight());
                        game.setLoopBackground1PosY(game.getLoopBackground1PosY() + game.getBlockHeight());
                        game.setLoopBackground2PosY(game.getLoopBackground2PosY() + game.getBlockHeight());
                        game.setBottomBoundY(game.getBottomBoundY() + game.getBlockHeight());
                        for (int i = 0; i < game.getBlockStack().size(); i++) {
                            game.getBlockStack().get(i).setPosY(game.getBlockStack().get(i).getPosY() + game.getBlockHeight());
                        }
                    }
                }

            } else {
                if (posY >= game.getHeight()) { //missing with block
                    falling = false;
                    game.missMarker();
                    posY = 0;
                    fallSpeed = 8 + game.getStackHeight();
                    swinging = true;

                }
            }
        }
    }

    public void swing() {
        if (swinging) {
            if (posX < game.getRightOpponentBound() && !swingLeft) {
                posX += 2 + game.getStackHeight();
            }
            if (posX > game.getLeftOpponentBound() && swingLeft) {
                posX -= 2 + game.getStackHeight();
            }
            if (posX >= game.getRightOpponentBound() && !swingLeft) {
                swingLeft = true;
            }
            if (posX <= game.getLeftOpponentBound() && swingLeft) {
                swingLeft = false;
            }
        }
    }

    public void moveOneUp() {
        posY++;
    }

    public void setSwinging(boolean b) {
        this.swinging = b;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public void setFalling(boolean b) {
        this.falling = b;
    }

    public boolean isFalling() {
        return falling;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int x) {
        posX = x;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int y) {
        posY = y;
    }


}

