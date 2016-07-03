package asserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;

import public_class.AS_C;
import public_class.C_AS;
import public_class.Control;
import public_class.Ticket;
import public_method.SandR;
import public_method.ourMath;


public class ServerThread extends Thread {
	int id;
	private boolean isbusy;//�Ƿ�����æ�ı��
	private Socket s;
	public ServerThread(int _id) {
		super();
		this.id = _id;
		this.isbusy=false;
		this.s = null;
	}
	public void setSocket(Socket s) {
		this.isbusy=true;
		this.s = s;
	}
	
	public void run() {
		try {
			int resend = 0;
			testOutPut();
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//Զ�̷��͵�server�������
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//Զ�̴�server���յ�������
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				System.out.println("id-" + id + " :receive :" + new String(line) + " ,from:" + s.getInetAddress() + ":" + s.getPort() + " at : " + new Timestamp(System.currentTimeMillis()));
				if(line.equals("c-as")){
					System.out.println("��ʼ��������");
					//��������
					byte[] receivedata = SandR.readDes_bytes(fromClient);
					byte _type = 1;
					
					//������֤�����Ƿ����Լ���Ҫ��
					if(receivedata[0] == _type){
						C_AS c2 = new C_AS(receivedata);		
						c2.show();
						//�ж�ʱ��
						if(System.currentTimeMillis()-c2.getTS() > 300){
							System.out.println("���ݰ���ʱ���б��طŹ�����Σ��");
							byte type = 13;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
						
						
						//�������ݿ�
						Connection conn=null;  
		       	    	ResultSet rs=null; 
		       		    Statement stmt=null;
		       		    String input_name = c2.getIDc();
		       		    if(input_name != null)
		       		    {
		       		    	try{
		       		    		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
							    conn = DriverManager.getConnection("jdbc:odbc:con_DB");  
							    stmt=conn.createStatement();
							    rs = stmt.executeQuery("select * from  user where u_name='"+input_name+"'");
							    if(rs.next())
			            	    { 
							    	System.out.println("���û�����!");
							    	System.out.println(input_name);
					
			            	    }
							    else{
							    	System.out.println("�û���������");
									byte type = 8;
									SandR.sendControl(type,c2.getIDc(),toClient);
									resend++;
									line_in = SandR.readDes_bytes(fromClient);
									line = new String(line_in);
									continue;
							    }
							    
							    MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
								byte[] output = messageDigest.digest((input_name + rs.getString("u_psw")).getBytes());//MD5���㣬�õ�random��kc
								byte[] Kc;
								Kc = new byte[8];
								for(int i = 0;i<8;i++){Kc[i] = output[i+4];}
								//System.out.println(new String(Kc));
								byte[] random_hash_byte = messageDigest.digest(ourMath.long2bytes(c2.getRandom() + ourMath.bytes2long(Kc) + c2.getTS()));//MD5���㣬�õ�random��kc
								if(!Arrays.equals(Arrays.copyOfRange(random_hash_byte,4,8),ourMath.intToByteArray(c2.getRandom_hash(),4))){
									System.out.println("�������");
									byte type = 9;
									SandR.sendControl(type,c2.getIDc(),toClient);
									resend++;
									line_in = SandR.readDes_bytes(fromClient);
									line = new String(line_in);
									continue;
								}
								
								//ͨ�����м�飬�ظ�AS_C
								System.out.println("ͨ��У��");
								byte type = 66;
								SandR.sendControl(type,c2.getIDc(),toClient);
								@SuppressWarnings("unused")
								byte[] Kctgs = ourMath.long2bytes((long)2345);
								byte[] addr = s.getInetAddress().getAddress();//Ktgs=ourMath.long2bytes((long)123)
								Ticket t = new Ticket(ourMath.long2bytes((long)2345),ourMath.long2bytes((long)123),input_name,addr,c2.getIDtgs());
								AS_C as = new AS_C("wym",123,t,ourMath.long2bytes((long)2345));//Kctgs--ourMath.long2bytes((long)2345)
								as.show();
								byte[] as_c_bytes = as.encrytData(Kc);
								//System.out.println(as_c_bytes.length);
								SandR.sendDes_bytes(as_c_bytes,toClient);
								Control report = new Control(SandR.readDes_bytes(fromClient));
								switch(report.getType()){
								case 7:System.out.println("Ŀ�겻��ASServer������ϵ����Ա");break;
								case 8:System.out.println("�û���������");break;
								case 9:System.out.println("�������");break;
								case 10:System.out.println("������Ŀ�겻����");break;
								case 12:System.out.println("Ȩ�޲���");break;
								case 13:report.show();break;
								case 67:System.out.println("client����AS-C�ɹ���׼���ж�TCP����.");break;
								default:report.show();break;
								}
		       		    	}catch (Exception ex) { 
						    	 System.err.println("OpenConn:"+ex.getMessage());
						    } 
						}
					}
					else{
						SandR.sendDes_bytes("what?".getBytes(),toClient);
					}
					if(resend >=5){break;}
					toClient.flush();
					line_in = SandR.readDes_bytes(fromClient);
					line = new String(line_in);
				}
				SandR.sendDes_bytes("see you~".getBytes(),toClient);
				toClient.close();
				fromClient.close();
				exit();
			} 
		}catch (Exception e) {
			try {
				exit();
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��					e1.printStackTrace();
			}
			e.printStackTrace();
		}
		       		    	
	}
						
	/*public void run() {
		try {
			int resend = 0;
			testOutPut();
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//Զ�̷��͵�server�������
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//Զ�̴�server���յ�������
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				System.out.println("id-" + id + " :receive :" + new String(line) + " ,from:" + s.getInetAddress() + ":" + s.getPort() + " at : " + new Timestamp(System.currentTimeMillis()));
				if(line.equals("c-as")){
					System.out.println("��ʼ��������");
					//��������
					byte[] receivedata = SandR.readDes_bytes(fromClient);
					byte _type = 1;
					
					//������֤�����Ƿ����Լ���Ҫ��
					if(receivedata[0] == _type){
						C_AS c2 = new C_AS(receivedata);		
						c2.show();
						//�ж�ʱ��
						if(System.currentTimeMillis()-c2.getTS() > 300){
							System.out.println("���ݰ���ʱ���б��طŹ�����Σ��");
							byte type = 13;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}					
						/*
						//�����ݿ���ȡ����
						//��ȡ�û��������룬�˴�Ӧ�����ݿ��ȡ
						String[] UserID = {"ydyd","lyly","jrjr","lxlx"};
						String[] UserPass = {"ydyd2016","lyly2016","jrjr2016","lxlx2016"};
						String input_name = c2.getIDc();
						int User_IDnum=0;
						for(;User_IDnum<UserID.length;User_IDnum++){
							if(UserID[User_IDnum].equals(input_name)){
								System.out.println(User_IDnum + "���û�����!");
								break;
							}
						}
						System.out.println(UserID[User_IDnum]);
						//System.out.println(UserID[User_IDnum] + UserPass[User_IDnum]);
						if(User_IDnum == UserID.length){
							System.out.println("�û���������");
							byte type = 8;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
		
						//�ж�Kc
						//����Kc,���û����������ַ������ת��Ϊbyte[]����hash֮��ȡ32-95λ
						MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
						byte[] output = messageDigest.digest((UserID[User_IDnum] + UserPass[User_IDnum]).getBytes());//MD5���㣬�õ�random��kc
						byte[] Kc;
						Kc = new byte[8];
						for(int i = 0;i<8;i++){Kc[i] = output[i+4];}
						//System.out.println(new String(Kc));
						byte[] random_hash_byte = messageDigest.digest(ourMath.long2bytes(c2.getRandom() + ourMath.bytes2long(Kc) + c2.getTS()));//MD5���㣬�õ�random��kc
						if(!Arrays.equals(Arrays.copyOfRange(random_hash_byte,4,8),ourMath.intToByteArray(c2.getRandom_hash(),4))){
							System.out.println("�������");
							byte type = 9;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
						//�ж�TGSServer�Ƿ����
						/*
						if(false){
							//���ݿ����
							System.out.println("������TGSServer������");
							byte type = 10;
							sendControl(type,c2.getIDc(),toClient);
							resend++;
							line = readDes_String(fromClient);
							break;
						}
						//���Ȩ��
						//���ݿ����
						if(false){
							System.out.println("Ȩ�޲���");
							byte type = 12;
							sendControl(type,c2.getIDc(),toClient);
							resend++;
							line = readDes_String(fromClient);
							break;
						}
						*/
						//ͨ�����м�飬�ظ�AS_C
						/*System.out.println("ͨ��У��");
						byte type = 66;
						SandR.sendControl(type,c2.getIDc(),toClient);
						@SuppressWarnings("unused")
						byte[] Kctgs = ourMath.long2bytes((long)2345);
						byte[] addr = s.getInetAddress().getAddress();//Ktgs=ourMath.long2bytes((long)123)
						Ticket t = new Ticket(ourMath.long2bytes((long)2345),ourMath.long2bytes((long)123),UserID[User_IDnum],addr,c2.getIDtgs());
						AS_C as = new AS_C("wym",123,t,ourMath.long2bytes((long)2345));//Kctgs--ourMath.long2bytes((long)2345)
						as.show();
					
						byte[] as_c_bytes = as.encrytData(Kc);
						//System.out.println(as_c_bytes.length);
						SandR.sendDes_bytes(as_c_bytes,toClient);
						Control report = new Control(SandR.readDes_bytes(fromClient));
						switch(report.getType()){
						case 7:System.out.println("Ŀ�겻��ASServer������ϵ����Ա");break;
						case 8:System.out.println("�û���������");break;
						case 9:System.out.println("�������");break;
						case 10:System.out.println("������Ŀ�겻����");break;
						case 12:System.out.println("Ȩ�޲���");break;
						case 13:report.show();break;
						case 67:System.out.println("client����AS-C�ɹ���׼���ж�TCP����.");break;
						default:report.show();break;
						}
					}
				}
				else{
					SandR.sendDes_bytes("what?".getBytes(),toClient);
				}
				if(resend >=5){break;}
				toClient.flush();
				line_in = SandR.readDes_bytes(fromClient);
				line = new String(line_in);
			}
			SandR.sendDes_bytes("see you~".getBytes(),toClient);
			toClient.close();
			fromClient.close();
			exit();
		} catch (Exception e) {
			try {
				exit();
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}*/
	//�˳�
	private void exit() throws IOException{
		System.out.println("id:" + id + " is free,socket " + s.getInetAddress() + ":" + s.getPort() + " has left");
		s.close();
		isbusy = false;
	}
	//�����Ƿ���æ���
	public boolean getIsBusy(){return isbusy;}
	//���������
	public void testOutPut(){
		Integer socketAdd = new Integer(s.getPort());
		System.out.println("new Client! id-" + this.id + "   " + s.getInetAddress() + ":" + socketAdd.toString());
	}
}