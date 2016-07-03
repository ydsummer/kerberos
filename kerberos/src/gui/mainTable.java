package gui;

import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.SOUTH;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class mainTable extends JFrame {
	String Password;
	String Name;
	
	private JTextArea showM;//消息显示区域
	private JTextArea writeM;//消息输入区域

	private JButton send;
	
	public mainTable(String password,String name) {
		// TODO Auto-generated constructor stub
//		super();			
//		setBounds(450, 100, 350, 500);
//		setTitle("消息显示");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		SpringLayout springLayout = new SpringLayout();
//		Container contentPane = getContentPane();// 获得窗体容器对象
////		this.add(contentPane);
//		contentPane.setLayout(springLayout);
//		contentPane.setVisible(true);
//		
//		JScrollPane contentScrollPane = new JScrollPane();
//		showM = new JTextArea(3,29);
//		showM.setLineWrap(true);
//		contentScrollPane.setViewportView(showM);
//		contentPane.add(contentScrollPane);
//		springLayout.putConstraint(SpringLayout.NORTH, contentScrollPane, 5, SpringLayout.NORTH,contentPane);
//		springLayout.putConstraint(SpringLayout.WEST, contentScrollPane, 5, SpringLayout.WEST,contentPane);
		
		this.setBounds(500,100,400,500);//设置窗口的位置，大小
		this.setTitle("消息显示");
		this.setVisible(true);//设置窗口是否可见
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		Container contentPane = getContentPane();// 获得窗体容器对象
		contentPane.setLayout(springLayout);
		//消息显示
		JScrollPane contentScrollPane = new JScrollPane();
		showM = new JTextArea(18,34);
		showM.setLineWrap(true);
		contentScrollPane.setViewportView(showM);
		contentPane.add(contentScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, contentScrollPane, 5, SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST, contentScrollPane, 5, SpringLayout.WEST,contentPane);
		//消息发送
		JScrollPane contentScrollPane2 = new JScrollPane();
//		writeM = new JTextField(20);
		writeM = new JTextArea(5,34);
//		writeM.getDocument().addDocumentListener((DocumentListener) this);
		writeM .setLineWrap(true);
		contentScrollPane2.setViewportView(writeM);
		contentPane.add(contentScrollPane2);
		springLayout.putConstraint(SpringLayout.NORTH, contentScrollPane2, 8, SpringLayout.SOUTH,showM);
		springLayout.putConstraint(SpringLayout.WEST, contentScrollPane2, 5, SpringLayout.WEST,contentPane);
		//发送按钮
		send = new JButton("发送");
		contentPane.add(send);
		springLayout.putConstraint(NORTH, send, 5, SOUTH,contentScrollPane2);
		springLayout.putConstraint(EAST, send, -5, EAST,contentPane);
		
		
		send.addActionListener(new Send());

		
		writeM.addKeyListener(new KeyListener() { //添加键盘事件  
            public void keyPressed(KeyEvent e) {  
            	if(e.getKeyCode()==KeyEvent.VK_ENTER){
    			showM.append("Send: \n"+writeM.getText()+"\n");//显示在文本区中
    			writeM.setText("");//清空文本框以便下次输入		
            	}
            }  
  
            public void keyReleased(KeyEvent e) {  
              
            }  
  
            public void keyTyped(KeyEvent e) {                
  
            }  
        });

	}

	private class Send implements ActionListener {///重置
		public void actionPerformed(final ActionEvent e){
			
			showM.append("Send: \n"+writeM.getText()+"\n\n");//显示在文本区中
			writeM.setText("");//清空文本框以便下次输入						
		}
	}
	
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//		showM.append(writeM.getText()+"\n");//显示在文本区中
//		writeM.setText("");//清空文本框以便下次输入
//	}
}
