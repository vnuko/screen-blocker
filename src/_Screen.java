import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;

class _Screen implements KeyListener {

    static JPasswordField field;

    static JDialog frame;

    static JLabel label;
    static JLabel message;

    static JLabel background;

    static GraphicsEnvironment graphics;
    static GraphicsDevice device;

    static Properties properties;

    static String timeStr;

    static Logger logger;
    static FileHandler fh;

    static long lockedTimestamp;

    // main class
    public static void mainww(String[] args) throws IOException {
//        System.setProperty("apple.awt.UIElement", "true");

        logger = Logger.getLogger(_Screen.class.getName());
        fh = null;

        try {
            fh = new FileHandler("screen-blocker.log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);

//        properties = _Screen.getProperties();

        if (!properties.getProperty("debug", "false").equals("true")) {
            LogManager.getLogManager().reset(); // disable logging
        }

//        try {
//            UIManager.setLookAndFeel(
//                    UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        _Screen scr = new _Screen();

//        graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        device = graphics.getDefaultScreenDevice();

        // Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

//        // Main Window Component
//        frame = new JDialog();
//        frame.setUndecorated(true);
//        frame.setResizable(false);
//        frame.setBackground(new Color(0,0,0));

//        // Background Component
//        background = new JLabel();
//        background.setIcon(
//                new ImageIcon(
//                        new ImageIcon(
//                                ImageIO.read(
//                                        new File("screen-blocker.jpg")
//                                )
//                        ).getImage().getScaledInstance((int) size.getWidth(), (int) size.getHeight(), Image.SCALE_DEFAULT)
//                )
//        );
//        background.setLayout(null);

        //frame.setContentPane(background);

//        // Field Component
//        field = new JPasswordField(8);
//        field.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        field.setBackground(new Color(255,255, 255, 180));
//        field.setBounds((int) ((size.getWidth() / 2) - (200 / 2)), (int) ((size.getHeight() / 2) - (26 / 2)), 200, 36);
//        field.setFont(
//                new Font("SansSerif", Font.BOLD, 20)
//        );
//        field.addKeyListener(scr);
//
//        background.add(field);

        // Field Label Component a.k.a Error Message
//        label = new JLabel("");
//        label.setBounds(field.getX(), (field.getY() + field.getHeight() + 10), 200, 20);
//        label.setForeground(new Color(175, 0, 29));
//
//        background.add(label);

//        // Message Component
//        message = new JLabel(properties.getProperty("message", ""));
//        message.setFont(
//                new Font("SansSerif", Font.PLAIN, 100)
//        );
//        message.setForeground(new Color(255, 255, 255));
//        message.setBounds(
//            (int) ((size.getWidth() / 2) - (message.getPreferredSize().getWidth() / 2)),
//            (int) ((size.getHeight() / 2) - message.getPreferredSize().getHeight() - 60),
//            (int) message.getPreferredSize().getWidth(),
//            (int) message.getPreferredSize().getHeight()
//        );
//
//        background.add(message);

        logger.info("Starting timer...");

//        Timer timer = new Timer();
//        TimerTask tt = new TimerTask() {
//            public void run() {
//                Calendar cal = Calendar.getInstance(); //this is the method you should use, not the Date(), because it is desperated.
//
//                switch (cal.get(Calendar.DAY_OF_WEEK)) {
//                    case Calendar.SUNDAY -> timeStr = properties.getProperty("sun", "");
//                    case Calendar.MONDAY -> timeStr = properties.getProperty("mon", "");
//                    case Calendar.TUESDAY -> timeStr = properties.getProperty("tue", "");
//                    case Calendar.WEDNESDAY -> timeStr = properties.getProperty("wed", "");
//                    case Calendar.THURSDAY -> timeStr = properties.getProperty("thu", "");
//                    case Calendar.FRIDAY -> timeStr = properties.getProperty("fri", "");
//                    case Calendar.SATURDAY -> timeStr = properties.getProperty("sat", "");
//                    default -> timeStr = ""; // should never happen
//                }
//
//                int hour = cal.get(Calendar.HOUR_OF_DAY);
//                int min = cal.get(Calendar.MINUTE);
//
//                logger.info("Current time: " + hour  + ":" + min);
//
//                if (!timeStr.equals("")) {
//                    logger.info("Time configurations for today: " + timeStr);
//
//                    StringTokenizer tokens = new StringTokenizer(timeStr, ";");
//                    while (tokens.hasMoreElements()) {
//                        String hhmmStr = tokens.nextElement().toString();
//                        String[] hhmmArr = hhmmStr.split(":");
//                        logger.info("Checking configuration time against current time...");
//                        if (hour == Integer.parseInt(hhmmArr[0]) && min == Integer.parseInt(hhmmArr[1])) {
//                            logger.info("FOUND match, current time " + hour  + ":" + min + " matches config's: " + Integer.parseInt(hhmmArr[0]) + ":" + Integer.parseInt(hhmmArr[1]));
//                            logger.info("Showing Screen Blocker window");
//                            device.setFullScreenWindow(frame);
//                            lockedTimestamp = System.currentTimeMillis();
//                            break;
//                        } else {
//                            logger.info("NO match, will try again in 60 sec. Current time: " + hour  + ":" + min + " didn't match config's: " + Integer.parseInt(hhmmArr[0]) + ":" + Integer.parseInt(hhmmArr[1]));
//                        }
//                    }
//                }
//
//                if (lockedTimestamp != 0) {
//                    long ago = lockedTimestamp < lockedTimestamp - (10 * 60 * 1000);
//                }
//
//            }
//        };
//        timer.schedule(tt, 1000, 1000*60);//	delay the task 1 second, and then run task every 60 seconds

        device.setFullScreenWindow(frame); //DEBUG
    }

//    private static lockScreen() {
//        device.setFullScreenWindow(frame);
//        lockedScreen = true;
//    }

    public static Properties getProperties() throws IOException {
        Properties p = new Properties();

        InputStream inputstream = new FileInputStream("screen-blocker.cfg");
        p.load(inputstream);

        return p;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        String secret = properties.getProperty("secret");

        label.setText("");

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if (Arrays.equals(secret.toCharArray(), field.getPassword())) {
                field.setText("");
                device.setFullScreenWindow(null);

                logger.info("Correct secret key");
            } else {
                field.setText("");
                label.setText("Wrong Secret Key");
                logger.info("Incorrect secret key");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}