
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

public class server2 implements Runnable {

    int i = 0;
    boolean flag = false;
    ServerSocket sersock;

    server2() {
        try {
            sersock = new ServerSocket(9051);
            Thread t = new Thread(this);
            t.start();
        } catch (IOException e) {
        }
    }

    public void run() {
        try {
            while (true) 
            {
                Socket sock = sersock.accept();
                new Thread(new server2.imageclientHandler(sock)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class imageclientHandler implements Runnable {

        DataInputStream dis;
        DataOutputStream dos;

        imageclientHandler(Socket sock) {
            try {
                dis = new DataInputStream(sock.getInputStream());
                dos = new DataOutputStream(sock.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                String s = dis.readLine();
                System.out.println(s);
                if (s.equals("send photo")) {

                    i++;
                    Robot robot = new Robot();
                    BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    ImageIO.write(screenShot, "png", new File("d:\\screenshot\\" + i + ".png"));
                    File f;
                    f = new File("d:\\screenshot\\" + i + ".png");
                    if (!f.exists()) {
                        i = 100;
                        f = new File("d:\\screenshot\\" + i + ".png");
                    }
                    FileInputStream fis = new FileInputStream(f);
                    dos.writeBytes("sending file\r\n");
                    //if(flag==false)
                    //  {
                    dos.writeInt((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
                    dos.writeInt((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                    //   flag=true;
                    //  }
                    String s1 = f.getName();
                    dos.writeBytes(s1 + "\r\n");
                    dos.writeLong(f.length());
                    byte b[] = new byte[100000];
                    while (fis.available() > 0) {
                        int r = fis.read(b, 0, 100000);
                        dos.write(b, 0, r);

                    }
                }

                System.out.println(dis.readLine());
                if (i >= 100) {
                    File myfile[] = new File("d:\\screenshot").listFiles();
                    for (int j = 0; j < myfile.length; j++) {
                        myfile[j].delete();
                    }
                    i = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        new server2();
    }

}
