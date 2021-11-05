import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    Game game;

    public void setGame(Game g){
        this.game=g;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && game.getSwingBlock().isSwinging()) {
            game.getSwingBlock().setSwinging(false);
            game.getSwingBlock().setFalling(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !game.getSwingBlock().isSwinging()) {
            game.getSwingBlock().setSwinging(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
