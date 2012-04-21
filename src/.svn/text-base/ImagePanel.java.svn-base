import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private Image image;

    public ImagePanel(Image image) {
       this.image = image;
    }

    
    @Override
    public void paintComponent(Graphics g) {
        
        //g.drawImage(image, 300, 200, 60, 120, this);
        
        g.drawImage(image, 0, 0, this);
        

    }

}