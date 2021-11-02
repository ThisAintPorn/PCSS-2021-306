import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private static final String gameTitle= "Tilted Towers";
    private static final int width=1920,height=1080;
    private static int playerLives=3;

    private boolean running;
    private Thread thread;

    public Game(){
        new Window(width,height,gameTitle,this);
    }

    public static void main(String[] args){
        new Game();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running){
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime=now;

            while(delta>=1) {
            tick();
            delta--;
            }
            if(running){
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: "+frames/now);
            }
        }
        stop();
    }

    public void start(){
        if(!running) {
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    public void tick(){
    }

    public void render(){
        BufferStrategy buffStrat = this.getBufferStrategy();
        if (buffStrat == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffStrat.getDrawGraphics();

        g.setColor(Color.blue);
        g.fillRect(640,0,640,1080);

        g.dispose();;
        buffStrat.show();
    }
    public void stop(){
        try{
            thread.join();
            running = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
