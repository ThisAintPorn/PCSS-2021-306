import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    private BufferedImage blockImg;
    private int posX, posY, blockSpawnX = 646, blockSpawnY = 0, blockwidth = 213, fallMargin = 107,
            bottomBoundY = 512, leftOpponentBound = 640, rightOpponentBound = 1067; //1280-213


    private double fallSpeed =8, fallAcceleration = 1.0982;
    private boolean falling, swinging, swingLeft;
    private Game game;


    public Block(Game g) {
        this.posX = blockSpawnX;
        this.posY = blockSpawnY;
        this.swinging = true;
        this.game = g;
        try {
            blockImg = ImageIO.read(new File("res/midtower.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fall() {
        if (falling) {
            posY+= fallSpeed;
            fallSpeed *= fallAcceleration;
            if (posX + blockwidth / 2 <= (game.getLastBlockCenterX() + fallMargin) && posX + blockwidth / 2 >= (game.getLastBlockCenterX() - fallMargin)) {

                if (posY >= bottomBoundY) {
                    falling = false;
                    posY=bottomBoundY;
                    game.setBlockCenter(posX);
                    game.addBlockToStack(this);
                    game.hitMarker();
                }
            }
        }
    }

    public void swing() {
        if (swinging) {
            if (posX < rightOpponentBound && !swingLeft) {
                posX += 1 + game.getStackHeight();
            }
            if (posX > leftOpponentBound && swingLeft) {
                posX -= 1 + game.getStackHeight();
            }
            if (posX >= rightOpponentBound && !swingLeft) {
                swingLeft = true;
            }
            if (posX <= leftOpponentBound && swingLeft) {
                swingLeft = false;
            }
        }
    }

    public void setSwinging(boolean b) {
        this.swinging = b;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public void setfalling(boolean b) {
        this.falling = b;
    }

    public boolean isFalling() {
        return falling;
    }

    public BufferedImage getBlockImg() {
        return this.blockImg;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}

