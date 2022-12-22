import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Panel1 extends JPanel implements Runnable{
    JLabel road, roadb;
    static JLabel car2, car;
    static JLabel score = new JLabel("Score : 0");
    static Thread th;
    Timer timer;
    static final int roadHeight = 1600;
    Panel1() {
        th = new Thread(this);
        setLayout(null);

        ImageIcon ii = new ImageIcon("road.jpeg");
        Image image = ii.getImage();
        Image newing = image.getScaledInstance(380, roadHeight, Image.SCALE_SMOOTH);
        ii = new ImageIcon(newing);

        ImageIcon i = new ImageIcon("road.jpeg");
        Image imag = i.getImage();
        Image newin = imag.getScaledInstance(380, roadHeight, Image.SCALE_SMOOTH);
        i = new ImageIcon(newin);

        ImageIcon ii1 = new ImageIcon("car1.png");
        Image image1 = ii1.getImage();
        Image newing1 = image1.getScaledInstance(100, 200, Image.SCALE_SMOOTH);
        ii1 = new ImageIcon(newing1);

        ImageIcon ii2 = new ImageIcon("car4.png");
        Image image2 = ii2.getImage();
        Image newing2 = image2.getScaledInstance(100, 200, Image.SCALE_SMOOTH);
        ii2 = new ImageIcon(newing2);

        car = new JLabel(ii1);
        road = new JLabel(ii);
        roadb = new JLabel(i);
        car2 = new JLabel(ii2);

        JLabel time = new JLabel();
        add(score);
        add(time);
        add(car2);
        add(car);
        add(road);
        add(roadb);
        time.setForeground(Color.ORANGE);
        score.setForeground(Color.white);
        time.setBounds(10, 10, 50, 20);
        score.setBounds(310, 10, 80, 20);
        road.setBounds(0, -1 * roadHeight/2, 380, roadHeight);
        roadb.setBounds(0, -1 * (roadHeight + (roadHeight/2)), 380, roadHeight);
        car.setBounds(40, (roadHeight/2) - 300, 100, 200);

        timer = new Timer(time);
        RandomCars ra = new RandomCars(car2);
        th.start();
        timer.start();
        ra.start();
    }
    public void run() {
        while (Main.On){
            road.setBounds(0, road.getY() + 1, 380, roadHeight);
            roadb.setBounds(0, roadb.getY() + 1, 380, roadHeight);
            try{
                Thread.sleep(5);
            }
            catch (Exception e){
                System.out.println(e);
            }
            if(road.getY() == (roadHeight/2)) {
                road.setBounds(0, -1 * (roadHeight + (roadHeight/2)), 380, roadHeight);
            }
            if(roadb.getY() == (roadHeight/2)){
                roadb.setBounds(0, -1 * (roadHeight + (roadHeight/2)), 380, roadHeight);
            }
        }
    }
    public void leftside(){
        new MoveCar(car, 40, -2);
    }
    public void rightside(){
        new MoveCar(car, 240, +2);

    }
}
class MoveCar extends Thread{
    int upto = 0, n = 0;
    JLabel car;
    static Thread th;
    MoveCar(JLabel car, int upto, int n){
        th = new Thread(this);
        this.upto = upto;
        this.car = car;
        this.n = n;
        start();
    }

    @Override
    public void run() {
        while(car.getX() != upto && Main.On) {
            car.setBounds(car.getX() + n, car.getY(), car.getWidth(), car.getHeight());
            try {
                sleep(1);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
class Timer extends Thread{
    static Thread th;
    JLabel time;
    Timer(JLabel time){
        this.time = time;
        th = new Thread(this);
    }
    @Override
    public void run() {
        for (int j = 0; Main.On; j++) {
            String min = ""+j+" : ";
            if(j<=9){
                min = "0"+j+" : ";
            }
            for (int i = 0; i<60 && Main.On; i++) {
                String sec = "" + i;
                if(i<=9){
                    sec = "0"+i;
                }
                try {
                    time.setText(min + sec);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
class RandomCars extends Thread{
    static Thread th;
    Random rand = new Random();
    JLabel car;
    RandomCars(JLabel car){
        this.car = car;
    }
    @Override
    public void run() {
        th = new Thread(this);
        int x = 0, score = 0;
        if(rand.nextInt(2) == 0){
            x = 40;
        }
        else{
            x = 240;
        }
        car.setBounds(x, -200, 100, 200);
        for (int i = 0; i <= 1000; i += 2) {
            try {
                Thread.sleep(5);
                car.setBounds(x, -200 + i, 100, 200);
            }catch (Exception e){
                System.out.println(e);
            }
            if (i==1000) {
                Panel1.score.setText("Score : " + ++score);
                i = 0;
                if(rand.nextInt(2) == 0){
                    x = 40;
                }
                else{
                    x = 240;
                }
            }
            if(isCarMeet()){
                stopAll();
                break;
            }
        }
    }
    static boolean isCarMeet(){
        int x1 = Panel1.car.getX();
        int y1 = Panel1.car.getY();
        int x2 = Panel1.car2.getX();
        int y2 = Panel1.car2.getY();
        if (x1 >= x2 && x1 <= x2 + 100 && y1 >= y2 && y1 <= y2 + 200) {
            System.out.println("Acident A");
            return true;
        }
        else if(x1 + 100 >= x2 && x1 + 100<= x2 + 100 && y1 >= y2 && y1 <= y2 + 200){
            System.out.println("Accident B");
            return true;
        }
        else if(x1 + 100 >= x2 && x1 + 100<= x2 + 100 && y1 + 200>= y2 && y1 + 200 <= y2 + 200){
            System.out.println("Accident C");
            return true;
        }
        else if (x1 >= x2 && x1 <= x2 + 100 && y1 + 200 >= y2 && y1 + 200 <= y2 + 200) {
            System.out.println("Acident D");
            return true;
        }
        return false;
    }
    public void stopAll(){
        Main.On = false;
    }
}
public class Main {
    static JFrame frame = new JFrame("My First Game");
    static boolean On = true;//game switch
    public static void main(String[] args) {

	Panel1 panel = new Panel1();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 37){
                    panel.leftside();//left arrow
                }
                else if(e.getKeyCode() == 39){
                    panel.rightside();//right arrow
                }
            }
        });
        frame.setSize(400, 810);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
