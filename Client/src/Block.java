import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    private BufferedImage blockImg;
    private int posX, posY, blockSpawnX=960,blockSpawnY=0,bottomBoundY=110,
            leftOpponentBound= 640, rightOpponentBound = 1280;
    private double fallAcceleration = 1.0982;
    private boolean falling, swinging,swingLeft;



    public Block(){
        this.posX = blockSpawnX;
        this.posX = blockSpawnY;
        try {
            blockImg = ImageIO.read(new File("res/midtower.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fall(){
        if(falling) {
            if (posY > bottomBoundY) {
                posY = (int) (fallAcceleration * posY);
            }
        }
    }

    public void swing(){
            if(posX<rightOpponentBound && !swingLeft){
                posX++;
            } else if (posX >= rightOpponentBound){
                swingLeft=true;
            } else if (posX <= leftOpponentBound){
                swingLeft=true;
            } else {
                posX--;
            }
    }

    public BufferedImage getBlockImg(){
        return this.blockImg;
    }
    public int getPosX(){
        return this.posX;
    }
    public int getPosY(){
        return this.posY;
    }

}

