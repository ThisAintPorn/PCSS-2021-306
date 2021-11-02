import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    private BufferedImage blockImg;
    private int posX, posY, blockSpawnX=960,blockSpawnY=0,bottomBoundY=110,
            leftOpponentBound= 640, rightOpponentBound = 1067; //1280-213
    private double fallAcceleration = 1.0982;
    private boolean falling, swinging,swingLeft;



    public Block(){
        this.posX = 960; //blockSpawnX;
        this.posX = 0;//blockSpawnY;
        try {
            blockImg = ImageIO.read(new File("res/midtower.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fall(){
        swinging=false;
        if(falling) {
            if (posY < bottomBoundY) {
                posY = (int) (fallAcceleration * posY);
            }
            else {
                falling=false;
            }
        }
    }

    public void swing(){
        swinging=true;
        if(swinging) {
            if(posX<rightOpponentBound && !swingLeft){
                posX++;
            }
            if(posX>leftOpponentBound && swingLeft){
                posX--;
            }
            if(posX>=rightOpponentBound && !swingLeft){
                swingLeft=true;
            }
            if(posX<=leftOpponentBound && swingLeft){
                swingLeft=false;
            }
        }
    }

    public BufferedImage getBlockImg(){
        return this.blockImg;
    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }

}

