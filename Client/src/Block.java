import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    BufferedImage blockImg;
    private int posX,posY, blockSpawnX=960,blockSpawnY=0,bottomBoundY=110,
            leftOpponentBound= 640, rightOpponentBound = 1280;
    private double fallAcceleration = 1.0982;
    private boolean falling = false,swingLeft;



    public Block(){
        posX = blockSpawnX;
        posX = blockSpawnY;
        try {
            blockImg = ImageIO.read(new File("midtower.png)"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game.setSwingBlock(this);
    }

    public Block(int x, int y){
        posX =x;
        posY =y;
        falling =true;
    }

    public void fall(){
        if(posY>bottomBoundY) {
            posY = (int) (fallAcceleration * posY);
        }
    }

    public void swing(){
        if(!falling){
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
    }
}

