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
	File file1;    //�����¼�û�ע����û�����������ļ���
	Writer writer;
//	String Usersname; //�����¼�û���������
//	String Userspasswordxs;//�����¼���������
//   Db3Conn logindb= new Db3Conn();


	public Load(){
		setTitle("��½����");
		this.setResizable(false);
		pan=new JPanel();//����������pan
		str="";   //image/qq.PNG"kechengsheji/my/bin/qq.jpg";//"F:\\dongman\\qq.jpg"
		str1="E:/java����/image/tuu.jpg";//		
		imageBackground=new ImageIcon(str1);
		imageBackground.setImage(imageBackground.getImage().getScaledInstance(376,283,Image.SCALE_DEFAULT));
		image=new ImageIcon(str);
		image.setImage(image.getImage().getScaledInstance(87,92,Image.SCALE_DEFAULT));
		labelName=new JLabel("�û���");
		labelPassword=new JLabel("��   ��");
		text=new JTextField(16);
		password=new JPasswordField(16);
		button=new JButton("��½");
//		b=new JButton("ע��");
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
		String pp =password.getText();//����
		String nn = text.getText();//�˺�
	if(e.getSource() == button)//������½
	{
		@SuppressWarnings("unused")
		boolean flag = true;

		if(new String(text.getText()).equals("")){
			JOptionPane.showMessageDialog(null, "�û�������Ϊ��!");
		}
		else if(new String(password.getPassword()).equals("")){
			JOptionPane.showMessageDialog(null, "���벻��Ϊ��!");
		}
		else
		{
			this.setVisible(false);
		new  mainTable(nn,pp);

		}
	}

//	if (e.getSource() == b) {//����ע��
//
//		if (pp.equals("")||nn.equals("")) {
//			JOptionPane.showMessageDialog(null,"�û��������벻��Ϊ��");
//		}
//
//		else {
//			String sqlString ="select �˺� from  ��Ա��Ϣ where �˺� ='"+nn+"'";
//			ResultSet newrs = null; 
//			newrs=logindb.executeQuery(sqlString);
//			try {
//				if (newrs.next()) {
//					JOptionPane.showMessageDialog(null,"�˺��Ѿ��ѱ�ע��");
//					text.setText("");
//				}
//				else {
//					sqlString="insert into ��Ա��Ϣ (�˺�,����) values ('"+nn+"','"+pp+"') ";
//					logindb.execute(sqlString);
//					text.setText(nn);
//					password.setText(pp);
//					JOptionPane.showMessageDialog(null,"ע��ɹ�");
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
