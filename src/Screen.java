import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;

class Screen implements KeyListener {
    // JTextField
    static JPasswordField field;

    // JFrame
    static JFrame frame;

    static JLabel background;

    static GraphicsEnvironment graphics;
    static GraphicsDevice device;

    static Properties properties;

    static String timeStr;

    // main class
    public static void main(String[] args) throws IOException {
        //System.setProperty("apple.awt.UIElement", "true");

        properties = Screen.getProperties();

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Screen scr = new Screen();

        graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = graphics.getDefaultScreenDevice();

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        // Frame Component
        frame = new JFrame();

        // Background Component
        background = new JLabel();
        background.setIcon(
                new ImageIcon(
                        new ImageIcon(
                                ImageIO.read(
                                        new File("resources/images/glass.jpg")
                                )
                        ).getImage().getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_SMOOTH)
                )
        );
        background.setLayout(null);

        // Field Component
        field = new JPasswordField(8);
        field.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setBackground(new Color(255,255, 255, 100));
        field.setBounds((int) ((size.getWidth() / 2) - (200 / 2)), (int) ((size.getHeight() / 2) - (26 / 2)), 200, 36);
        field.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );
        field.addKeyListener(scr);

        background.add(field);
        frame.setContentPane(background);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setBackground(new Color(0,0,0));

        Timer timer = new Timer();
        TimerTask tt = new TimerTask(){
            public void run(){
                Calendar cal = Calendar.getInstance(); //this is the method you should use, not the Date(), because it is desperated.

                switch (cal.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY -> timeStr = properties.getProperty("sun", "");
                    case Calendar.MONDAY -> timeStr = properties.getProperty("mon", "");
                    case Calendar.TUESDAY -> timeStr = properties.getProperty("tue", "");
                    case Calendar.WEDNESDAY -> timeStr = properties.getProperty("wed", "");
                    case Calendar.THURSDAY -> timeStr = properties.getProperty("thu", "");
                    case Calendar.FRIDAY -> timeStr = properties.getProperty("fri", "");
                    case Calendar.SATURDAY -> timeStr = properties.getProperty("sat", "");
                    default -> timeStr = ""; // should never happen
                }

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                if(!timeStr.equals("")) {
                    StringTokenizer tokens = new StringTokenizer(timeStr, ";");
                    while (tokens.hasMoreElements()) {
                        String hhmmStr = tokens.nextElement().toString();
                        String[] hhmmArr = hhmmStr.split(":");
                        if (hour == Integer.parseInt(hhmmArr[0]) && min == Integer.parseInt(hhmmArr[1])) {
                            device.setFullScreenWindow(frame);
                        }
                    }
                }
            }
        };
        timer.schedule(tt, 1000, 1000*60);//	delay the task 1 second, and then run task every five seconds

        device.setFullScreenWindow(frame); //DEBUG
    }
    
    public static Properties getProperties() throws IOException {
        Properties p = new Properties();

        InputStream inputstream = new FileInputStream("resources/config/properties.config");
        p.load(inputstream);

        return p;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        String secret = properties.getProperty("secret");

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if (Arrays.equals(secret.toCharArray(), field.getPassword())) {
                device.setFullScreenWindow(null);
            } else {
                field.setText("");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}