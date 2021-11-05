
import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

    private Game game;
    private JFrame frame;

    public Window(int width, int height, String title, Game game) {

        frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
        frame.setFocusable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void toTop(){
        frame.setAlwaysOnTop(true);
    }

}
