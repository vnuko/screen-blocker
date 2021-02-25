import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Screen implements KeyListener {
    protected Properties config;
    protected Logger logger;
    protected GraphicsEnvironment graphics;
    protected GraphicsDevice device;
    protected JDialog frame;
    protected JLabel background;
    protected JPasswordField field;
    protected JLabel label;
    protected JLabel message;
    protected String timeStr;
    protected long lockTimestamp;

    public static void main(String[] args) {
        Screen screenBlocker = new Screen(
                Screen.loadConfig(),
                Screen.getLoger(
                        Screen.class.getName()
                )
        );
        screenBlocker.init();
    }

    public Screen(Properties config, Logger logger) {
        this.config = config;
        this.logger = logger;

        if (!this.config.getProperty("debug", "false").equals("true")) {
            LogManager.getLogManager().reset(); // disable logging
        }
    }

    public void init() {
        System.setProperty("apple.awt.UIElement", "true");

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = graphics.getDefaultScreenDevice();

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        // Main Frame
        frame = new JDialog();
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setBackground(
                new Color(0,0,0)
        );

        // Background
        background = new JLabel();
        try {
            background.setIcon(
                    new ImageIcon(
                            new ImageIcon(
                                    ImageIO.read(
                                            new File("screen-blocker.jpg")
                                    )
                            ).getImage().getScaledInstance(
                                    (int) size.getWidth(),
                                    (int) size.getHeight(),
                                    Image.SCALE_DEFAULT
                            )
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        background.setLayout(null);

        frame.setContentPane(background);

        // Password Field
        field = new JPasswordField(8);
        field.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        field.setBackground(
                new Color(255,255, 255, 180)
        );
        field.setBounds(
                (int) ((size.getWidth() / 2) - (200 / 2)),
                (int) ((size.getHeight() / 2) - (26 / 2)),
                200,
                36
        );
        field.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );
        field.addKeyListener(this);

        background.add(field);

        // Error Message
        label = new JLabel("");
        label.setBounds(
                field.getX(),
                (field.getY() + field.getHeight() + 10),
                200,
                20
        );
        label.setForeground(new Color(175, 0, 29));

        background.add(label);

        // Custom Message
        message = new JLabel(
                config.getProperty("message", "")
        );
        message.setFont(
                new Font("SansSerif", Font.PLAIN, 100)
        );
        message.setForeground(
                new Color(255, 255, 255)
        );
        message.setBounds(
                (int) ((size.getWidth() / 2) - (message.getPreferredSize().getWidth() / 2)),
                (int) ((size.getHeight() / 2) - message.getPreferredSize().getHeight() - 60),
                (int) message.getPreferredSize().getWidth(),
                (int) message.getPreferredSize().getHeight()
        );

        background.add(message);

        this.startTimer();

        // this.lock(); // DEBUG
    }

    protected void startTimer() {
        java.util.Timer timer = new Timer();

        TimerTask tt = new TimerTask() {
            public void run() {
                Calendar cal = Calendar.getInstance();

                switch (cal.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY -> timeStr = config.getProperty("sun", "");
                    case Calendar.MONDAY -> timeStr = config.getProperty("mon", "");
                    case Calendar.TUESDAY -> timeStr = config.getProperty("tue", "");
                    case Calendar.WEDNESDAY -> timeStr = config.getProperty("wed", "");
                    case Calendar.THURSDAY -> timeStr = config.getProperty("thu", "");
                    case Calendar.FRIDAY -> timeStr = config.getProperty("fri", "");
                    case Calendar.SATURDAY -> timeStr = config.getProperty("sat", "");
                    default -> timeStr = ""; // should never happen
                }

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);

                logger.info("Current time: " + hour  + ":" + min);

                if (!timeStr.equals("")) {
                    logger.info("Time configurations for today: " + timeStr);

                    StringTokenizer tokens = new StringTokenizer(timeStr, ";");
                    while (tokens.hasMoreElements()) {
                        String hhmmStr = tokens.nextElement().toString();
                        String[] hhmmArr = hhmmStr.split(":");
                        logger.info("Checking configuration time against current time...");
                        if (hour == Integer.parseInt(hhmmArr[0]) && min == Integer.parseInt(hhmmArr[1])) {
                            logger.info("FOUND match, current time " + hour  + ":" + min + " matches config's: " + Integer.parseInt(hhmmArr[0]) + ":" + Integer.parseInt(hhmmArr[1]));
                            logger.info("Showing Screen Blocker window");
                            Screen.this.lock();
                            break;
                        } else {
                            logger.info("NO match, will try again in 60 sec. Current time: " + hour  + ":" + min + " didn't match config's: " + Integer.parseInt(hhmmArr[0]) + ":" + Integer.parseInt(hhmmArr[1]));
                        }
                    }
                }

                if (
                        Integer.parseInt(config.getProperty("clearAfter", "0")) > 0  &&
                        lockTimestamp != 0 &&
                        (System.currentTimeMillis() - ((long) Integer.parseInt(config.getProperty("clearAfter", "0")) * 60 * 1000)) > lockTimestamp
                ) {
                    Screen.this.unlock();
                }
            }
        };
        timer.schedule(tt, 1000, 1000*60);//	delay the task 1 second, and then run task every 60 seconds
    }

    public static Properties loadConfig() {
        Properties config = new Properties();

        try {
            InputStream inputStream = new FileInputStream("screen-blocker.cfg");
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public static Logger getLoger(String className) {
        Logger logger = Logger.getLogger(className);

        try {
            FileHandler fh = new FileHandler("screen-blocker.log");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logger;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        String secret = config.getProperty("secret", "");
        label.setText("");

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if (Arrays.equals(secret.toCharArray(), field.getPassword())) {
                field.setText("");
                this.unlock();
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

    protected void lock() {
        device.setFullScreenWindow(frame);
        lockTimestamp = System.currentTimeMillis();
    }

    protected void unlock() {
        device.setFullScreenWindow(null);
        lockTimestamp = 0;
    }
}
