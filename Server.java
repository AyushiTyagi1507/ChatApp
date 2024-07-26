import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.ActiveEvent;
import javax.swing.border.*;
import java.awt.*;


public class Server extends JFrame implements ActionListener{
    ServerSocket server;
    Socket socket;
    
    BufferedReader br; //for reading
    PrintWriter out;  //for writting

    public JLabel heading = new JLabel("Server Area");
public JTextArea messagearea = new JTextArea();
 public JTextField messageinput = new JTextField();
public Font font=new Font( "Roboto" , Font.PLAIN,20);
    
    //Constructor......
    public Server() throws IOException {
        server = new ServerSocket(3308);
        System.out.println("Server is ready to accept connection");
        System.out.println("Waiting.....");
        socket=server.accept();
        
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());
        createGUI();
        startReading();
        // startWriting();
        }
    
        public void createGUI(){
            //GUI code......
            this.setTitle("Server Messager[END]");
            this.setSize(600,700);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //coding for component....
            heading.setFont(font);
            messagearea.setFont(font);
            messageinput.setFont(font);
            
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
            messagearea.setEditable(false);
            messageinput.setHorizontalAlignment(SwingConstants.CENTER);
            messageinput.addActionListener(this);
            
            
            // frame ka layout set krege
            this.setLayout(new BorderLayout());
            
            //adding the component to frame
            
            this.add(heading,BorderLayout.NORTH);
            JScrollPane jScrollPane = new JScrollPane(messagearea);
            this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageinput,BorderLayout.SOUTH);
            
            this.setVisible(true);
            
            }

// start reading [method]

    public void startReading(){
     /// thread - data read krke deta rhega   
     Runnable r1;
        r1 = ()->{
            System.out.println("Reader Started.....");
            try {
            while(true)
            {
                
                String msg = br.readLine();
                
                if(msg.equals("exit"))
                {
                    System.out.println("client terminated  the chat");
                    JOptionPane.showMessageDialog(this,"Client Terminated the chat");
                    messageinput.setEnabled(false);
                    socket.close();
                    break;
                }
                 messagearea.append("Client : " + msg+"\n");
                //System.out.println("client :" +msg);
                }
            }
                catch (Exception e){
                    //e.printStackTrace();
                    System.out.println("Connection Closed...");
                }
                 
                };
        new Thread(r1).start();
                  }


    //start writing [method]
    public void startWriting(){
        
        // thread - data user lega and send krega client tak
        Runnable r2= ()->{
            System.out.println("writer Started.....");
            try{
          while(!socket.isClosed()){
             
                 
                  BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                  String content = br1.readLine();
                  out.println(content);
                  out.flush();

                  if(content.equals("exit")){
                    socket.close();
                    break;
                  }
                  
                 }
                 System.out.println("Connection Closed...");
                
            }
              catch(Exception e){
                  // TODO: handle exception
             e.printStackTrace();
              }
              
             
         };
        
        new Thread(r2).start();
        
     }
    
    
    public static void main(String args[]) throws IOException
    {
        System.out.println("This is server....");
        new Server();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getKeyCode());
        //String out = messageinput.getText();
        String contentToSend = messageinput.getText();
        messagearea.append("Me :"+contentToSend+"\n");
        out.println(contentToSend);
        out.flush();
        messageinput.setText("");
        messageinput.requestFocus();
        

    }

}