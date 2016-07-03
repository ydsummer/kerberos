package gui;
import java.awt.*;
import javax.swing.*;
import java.lang.String;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;

public class Log {
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Load log=new Load();
	}
}
@SuppressWarnings("serial")
class Load extends JFrame implements ActionListener{
	private JPanel pan;
	private JLabel labelName,labelPassword,label;
	@SuppressWarnings("unused")
	private JButton button,b;
	private JTextField text;
	private JPasswordField password;
	private JLabel labelImage;
	private ImageIcon imageBackground,image;
	private String str,str1;
	boolean x=false;
	File file1;    //定义记录用户注册的用户名和密码的文件夹
	Writer writer;
//	String Usersname; //定义记录用户名的数组
//	String Userspasswordxs;//定义记录密码的数组
//   Db3Conn logindb= new Db3Conn();


	public Load(){
		setTitle("登陆界面");
		this.setResizable(false);
		pan=new JPanel();//创建面板对象pan
		str="";   //image/qq.PNG"kechengsheji/my/bin/qq.jpg";//"F:\\dongman\\qq.jpg"
		str1="E:/java程序/image/tuu.jpg";//		
		imageBackground=new ImageIcon(str1);
		imageBackground.setImage(imageBackground.getImage().getScaledInstance(376,283,Image.SCALE_DEFAULT));
		image=new ImageIcon(str);
		image.setImage(image.getImage().getScaledInstance(87,92,Image.SCALE_DEFAULT));
		labelName=new JLabel("用户名");
		labelPassword=new JLabel("密   码");
		text=new JTextField(16);
		password=new JPasswordField(16);
		button=new JButton("登陆");
//		b=new JButton("注册");
		label=new JLabel(imageBackground);
		labelImage=new JLabel(image);
		labelImage.setBounds(18,80,87,86);
		labelName.setBounds(20,70,50,30);
		text.setBounds(70,80,140,24);
		labelPassword.setBounds(20,120,50,30);
		password.setBounds(70,120,140,24);
		button.setBounds(100,150,60,30);
//		b.setBounds(200,150,60,30);
		button.addActionListener(this);
//		b.addActionListener(this);
		//button.addActionListener(this);
		pan.add(labelImage);
		pan.add(labelName);
		pan.add(text);
		pan.add(labelPassword);
		pan.add(password);
		pan.add(button);
//		pan.add(b);
		pan.setLayout(null);
		label.setBounds(0, 0,imageBackground.getIconWidth(), imageBackground.getIconHeight());
		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		this.setContentPane(pan);
		pan.setOpaque(false);
		setBounds(400,200,276,282);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		validate();
	}
	public void actionPerformed(ActionEvent e){
		
		@SuppressWarnings("deprecation")
		String pp =password.getText();//密码
		String nn = text.getText();//账号
	if(e.getSource() == button)//监听登陆
	{
		@SuppressWarnings("unused")
		boolean flag = true;

		if(new String(text.getText()).equals("")){
			JOptionPane.showMessageDialog(null, "用户名不能为空!");
		}
		else if(new String(password.getPassword()).equals("")){
			JOptionPane.showMessageDialog(null, "密码不能为空!");
		}
		else
		{
			this.setVisible(false);
		new  mainTable(nn,pp);

		}
	}

//	if (e.getSource() == b) {//监听注册
//
//		if (pp.equals("")||nn.equals("")) {
//			JOptionPane.showMessageDialog(null,"用户名或密码不能为空");
//		}
//
//		else {
//			String sqlString ="select 账号 from  人员信息 where 账号 ='"+nn+"'";
//			ResultSet newrs = null; 
//			newrs=logindb.executeQuery(sqlString);
//			try {
//				if (newrs.next()) {
//					JOptionPane.showMessageDialog(null,"账号已经已被注册");
//					text.setText("");
//				}
//				else {
//					sqlString="insert into 人员信息 (账号,密码) values ('"+nn+"','"+pp+"') ";
//					logindb.execute(sqlString);
//					text.setText(nn);
//					password.setText(pp);
//					JOptionPane.showMessageDialog(null,"注册成功");
//				}
//				
//			} catch (Exception e2) {
//				// TODO: handle exception
//			}
//
//		}

//		}
  
	}
/*	public static void main(String[] args)
	{
	new	Load();
	}*/
}
