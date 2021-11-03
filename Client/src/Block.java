import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    private BufferedImage blockImg;
    private int posX, posY, blockSpawnX=747,blockSpawnY=0,bottomBoundY=512,
            leftOpponentBound= 640, rightOpponentBound = 1067; //1280-213
    private double fallAcceleration = 1.0982;
    private boolean falling, swinging,swingLeft;



    public Block(){
        this.posX = blockSpawnX;
        this.posY = blockSpawnY;
        this.swinging=true;
        try {
            blockImg = ImageIO.read(new File("res/midtower.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fall(){
        if(falling) {
            if (posY < bottomBoundY) {
                posY ++;
            }
            else {
                falling=false;
            }
        }
    }

    public void swing(){
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

    public void setSwinging(boolean b){
        this.swinging=b;
    }
    public boolean isSwinging(){
        return swinging;
    }
    public void setfalling(boolean b){
        this.falling=b;
    }
    public boolean isFalling(){
        return falling;
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

