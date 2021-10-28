public class Block {
    //Image img = 'block.png'
    private int posX,posY, blockSpawnX=960,blockSpawnY=0,bottomY=110,
            leftOpponentBound= 640, rightOpponentBound = 1280;
    private double fallAcceleration = 1.0982;
    private boolean falling = false,swingLeft;

    public Block(){
        posX = blockSpawnX;
        posX = blockSpawnY;
    }

    public Block(int x, int y){
        posX =x;
        posY =y;
        falling =true;
    }

    public void fall(){
        if(posY>bottomY) {
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

