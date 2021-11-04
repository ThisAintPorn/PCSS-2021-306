import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    private BufferedImage blockImg;
    private int posX, posY, blockSpawnX = 646, blockSpawnY = 0;
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
            System.out.println("falling Speed: "+fallSpeed+"Acceleration: "+fallAcceleration);
            System.out.println("block posX: " + game.getSwingBlock().getPosX() + ", posY: " + game.getSwingBlock().getPosY());
            posY+= (int) fallSpeed;
            fallSpeed *= fallAcceleration;
           // if (posX + blockwidth / 2 <= (game.getLastBlockCenterX() + fallMargin) && posX + blockwidth / 2 >= (game.getLastBlockCenterX() - fallMargin)) {
                //temporary
            if (posX + (game.getBlockWidth()/2) <= (game.getCenterPosX() + game.getFallMargin()) && posX + (game.getBlockWidth()/2) >= (game.getCenterPosX() - game.getFallMargin())) {

                if (posY >= game.getBottomBoundY()) {
                    falling = false;
                    posY=game.getBottomBoundY();
                    game.setBottomBoundY(game.getBottomBoundY()-219);
                    game.setBlockCenter(posX);
                    game.addBlockToStack(this);
                    game.hitMarker();
                    fallSpeed=8;
                    game.setSwingBlock( new Block(game));
                    game.moveUp();
                }

            }
            else {
                if (posY>=1080) {
                    falling=false;
                    game.missMarker();
                    posY=0;
                    fallSpeed=8;
                    swinging=true;

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
                posX -= 2+ game.getStackHeight();
            }
            if (posX >= game.getRightOpponentBound() && !swingLeft) {
                swingLeft = true;
            }
            if (posX <= game.getLeftOpponentBound() && swingLeft) {
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

    public void setFalling(boolean b) {
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

    public void moveOneUp(){
        posY++;
    }

}

