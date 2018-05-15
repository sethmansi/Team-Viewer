
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class RemoteFrame extends javax.swing.JFrame {

    String ip;

    //boolean flag=false;
    boolean status = true;

    public RemoteFrame(String ip) {

        this.ip = ip;
        initComponents();
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
        jScrollPane1.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                status = false;
            }
        });

        new Thread(new Runnable() {
            public void run() {
                while (status) {
                    ImageClient ic = new ImageClient();
                    try {
                        ic.t.join();
                        // Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    //public static void main(String[] args) {
    //     new RemoteFrame("127.0.0.1");
    //}
    class ImageClient implements Runnable {

        DataInputStream dis;
        DataOutputStream dos;
        Thread t;

        public ImageClient() {
            try {
                Socket sock = new Socket(ip, 9051);
                dis = new DataInputStream(sock.getInputStream());
                dos = new DataOutputStream(sock.getOutputStream());
                t = new Thread(this);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                dos.writeBytes("send photo\r\n");
                String s = dis.readLine();
                if (s.equals("sending file")) {
                    //if(flag==false)
                    // {
                    int w = dis.readInt();
                    int h = dis.readInt();
                    jLabel1.setSize(w, h);
                  //  flag=true;
                    //  }
                    String filename = dis.readLine();
                    long filesize = dis.readLong();
                    FileOutputStream fos = new FileOutputStream("d:\\received\\" + filename);
                    byte b[] = new byte[100000];
                    long count = 0;
                    while (true) {
                        int r = dis.read(b, 0, 100000);
                        fos.write(b, 0, r);
                        count = count + r;
                        if (count == filesize) {
                            break;
                        }
                    }
                    fos.close();
                    dos.writeBytes("File received\r\n");
                    BufferedImage bm = ImageIO.read(new File("d:\\received\\" + filename));
                    jLabel1.setIcon(new ImageIcon(bm));
                    System.out.println(new File("d:\\received\\" + filename).delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public class CommandClient {

        BufferedReader br;
        PrintWriter pw;

        public CommandClient() {
            try {
                Socket sock = new Socket(ip, 8005);
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                pw = new PrintWriter(sock.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel1MouseMoved(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jLabel1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 10, 70, 70);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        int x = evt.getX();
        int y = evt.getY();
        int bt = evt.getButton();
        CommandClient cc = new CommandClient();
        cc.pw.println("Mouse Clicked");
        cc.pw.println(x);
        cc.pw.println(y);
        cc.pw.println(bt);
        cc.pw.flush();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseMoved
        int x = evt.getX();
        int y = evt.getY();
         CommandClient cc = new CommandClient();
          cc.pw.println("Mouse Moved");
          cc.pw.println(x);
          cc.pw.println(y);
          cc.pw.flush();
    }//GEN-LAST:event_jLabel1MouseMoved

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
       CommandClient cc = new CommandClient();
        cc.pw.println("Key Pressed");
        cc.pw.println(evt.getKeyCode());
        cc.pw.flush();
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        CommandClient cc = new CommandClient();
        cc.pw.println("Key Released");
        cc.pw.println(evt.getKeyCode());
        cc.pw.flush();
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
      //  <editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
        //try {
    //    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
    //        if ("Nimbus".equals(info.getName())) {
    //            javax.swing.UIManager.setLookAndFeel(info.getClassName());
    //            break;
    //        }
    //    }
    // } catch (ClassNotFoundException ex) {
    //     java.util.logging.Logger.getLogger(RemoteFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    // } catch (InstantiationException ex) {
    //     java.util.logging.Logger.getLogger(RemoteFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    // } catch (IllegalAccessException ex) {
    //     java.util.logging.Logger.getLogger(RemoteFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //  } catch (javax.swing.UnsupportedLookAndFeelException ex) {
    //     java.util.logging.Logger.getLogger(RemoteFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //  }
    //</editor-fold>

    /* Create and display the form */
       // java.awt.EventQueue.invokeLater(new Runnable() {
    //    public void run() {
    //       new RemoteFrame().setVisible(true);
    //     }
    //   });
    // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
