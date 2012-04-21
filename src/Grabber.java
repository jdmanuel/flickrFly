
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.photos.*;
import com.aetrion.flickr.util.IOUtilities;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jd
 */
public class Grabber extends Component
{
    protected Flickr f;
    protected RequestContext requestContext;
    protected String frob = "";
    protected String token = "";
    protected Properties properties = null;
    protected String[] tagSet;
    //protected BufferedImage img;
    protected PhotoList photoList;
    protected PhotosInterface photosInterface;
    protected int h;
    protected int w;
    protected int photoPointer;
    protected JFrame mainFrame;
    
    public Grabber(String[] tagSet) throws ParserConfigurationException, IOException, SAXException, FlickrException
    {
        this.photoList = null;
        this.tagSet = tagSet;
        this.photoPointer = 0;
        this.mainFrame = new JFrame("Display image");
        
        mainFrame.setSize(1000, 1000);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

            IOUtilities.close(in);

            f = new Flickr(
                    properties.getProperty("apiKey"),
                    properties.getProperty("secret"),
                    new REST());
            Flickr.debugStream = false; // do we need this?
//        requestContext = RequestContext.getRequestContext();

            /*
             * After setting up the Flickr interface, we need to set up the
             * search parameters
             */          
        } catch (Exception ex) {            
            System.out.println(ex.getCause().getMessage());
            
        }        
    }
    
    public void getPhotos()
    {
        SearchParameters searchParams = new SearchParameters();
        searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);

        searchParams.setTags(this.tagSet);

        this.photosInterface = f.getPhotosInterface();        
        
        try {
            this.photoList = this.photosInterface.search(searchParams, 20, 1);
        } catch (IOException ex) {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlickrException ex) {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (this.photoList == null) {
            System.out.println("No photos matching those tags were found.");
        } 
        
    }
    
    private void SearchButtonMouseClicked(java.awt.event.MouseEvent evt)
    {
        System.out.println("GOT TO MOUSE CLICKED");
        
        paintImage();
        

        







    }
    
    
    
    public void paint()
    {
        if (this.photoList != null) {
            
            
                mainFrame.addMouseListener(new java.awt.event.MouseAdapter()
                {

                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt)
                    {
                        SearchButtonMouseClicked(evt);
                    }
                }); 
            
            
            
            
            
//            for (Object p : this.photoList)
//            {
//                mainFrame.addMouseListener(new java.awt.event.MouseAdapter()
//                {
//
//                    @Override
//                    public void mouseClicked(java.awt.event.MouseEvent evt)
//                    {
//                        SearchButtonMouseClicked(evt);
//                    }
//                });
//                
//                
//            }
            
            
            
            
            
            
           
                
            
        }    
    }
    
    public void paintImage()
    {
        
        //JFrame mainFrame = new JFrame("Display image");


        

        Photo photo = (Photo) this.photoList.get(photoPointer);
        photoPointer++;
        String id = photo.getId();       
        
        Collection c = null;
        try
        {
            c = this.photosInterface.getSizes(id);         
            
        } catch (IOException ex)
        {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex)
        {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlickrException ex)
        {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        String u;      
        
        if (c.size() > 7)
        {
            u = photo.getLargeUrl();
            int hack = 0;
            
            for (Object o : c)
            {
                Size s = (Size) o;
                w = s.getWidth();
                h = s.getHeight();
                
                if (++hack == 8)
                {
                    break;  // 8 is the magic number for larges                                            
                }
                
            }
            
            
        } else {
            u = photo.getMediumUrl();
            int hack = 0;

            for (Object o : c)
            {
                Size s = (Size) o;
                w = s.getWidth();
                h = s.getHeight();

                if (++hack == 6)
                {
                    break;  // 6 is the magic number for mediums                                            
                }

            }
        }     
        
        System.out.print("Printing URL:::: ");
        System.out.println(u);

        try
        {
            //BufferedImage img = ImageIO.read(new File(str));

            URL url = new URL(u);
            java.awt.Image img = Toolkit.getDefaultToolkit().createImage(url);

            //JFrame frame = new JFrame("Display image");                    

            ImagePanel imgPanel = new ImagePanel(img);
            
            mainFrame.getContentPane().removeAll();
            
            mainFrame.getContentPane().add(imgPanel);
            
            

            
            //frame.setSize(w, h);
            mainFrame.setSize(w, h);
            mainFrame.setVisible(true);
            mainFrame.setResizable(false);
            mainFrame.repaint();

            //Graphics g2 = img.getGraphics();
            //g2.drawImage(img, 0, 0, null);
        } catch (IOException ex)
        {
            Logger.getLogger(Grabber.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        
    }
        
        
        
        
    
    
    
    
    
    
    
    
    
    
    
}
