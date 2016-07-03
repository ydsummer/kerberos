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
	private boolean isbusy;//是否正在忙的标记
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
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//远程发送到server的输出流
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//远程从server接收的输入流
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				System.out.println("id-" + id + " :receive :" + new String(line) + " ,from:" + s.getInetAddress() + ":" + s.getPort() + " at : " + new Timestamp(System.currentTimeMillis()));
				if(line.equals("c-as")){
					System.out.println("开始接收数据");
					//接收数据
					byte[] receivedata = SandR.readDes_bytes(fromClient);
					byte _type = 1;
					
					//首先验证类型是否是自己需要的
					if(receivedata[0] == _type){
						C_AS c2 = new C_AS(receivedata);		
						c2.show();
						//判断时间
						if(System.currentTimeMillis()-c2.getTS() > 300){
							System.out.println("数据包超时，有被重放攻击的危险");
							byte type = 13;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
						
						
						//连接数据库
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
							    	System.out.println("该用户存在!");
							    	System.out.println(input_name);
					
			            	    }
							    else{
							    	System.out.println("用户名不存在");
									byte type = 8;
									SandR.sendControl(type,c2.getIDc(),toClient);
									resend++;
									line_in = SandR.readDes_bytes(fromClient);
									line = new String(line_in);
									continue;
							    }
							    
							    MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
								byte[] output = messageDigest.digest((input_name + rs.getString("u_psw")).getBytes());//MD5运算，用到random与kc
								byte[] Kc;
								Kc = new byte[8];
								for(int i = 0;i<8;i++){Kc[i] = output[i+4];}
								//System.out.println(new String(Kc));
								byte[] random_hash_byte = messageDigest.digest(ourMath.long2bytes(c2.getRandom() + ourMath.bytes2long(Kc) + c2.getTS()));//MD5运算，用到random与kc
								if(!Arrays.equals(Arrays.copyOfRange(random_hash_byte,4,8),ourMath.intToByteArray(c2.getRandom_hash(),4))){
									System.out.println("密码错误");
									byte type = 9;
									SandR.sendControl(type,c2.getIDc(),toClient);
									resend++;
									line_in = SandR.readDes_bytes(fromClient);
									line = new String(line_in);
									continue;
								}
								
								//通过所有检查，回复AS_C
								System.out.println("通过校验");
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
								case 7:System.out.println("目标不是ASServer，请联系管理员");break;
								case 8:System.out.println("用户名不存在");break;
								case 9:System.out.println("密码错误");break;
								case 10:System.out.println("所请求目标不存在");break;
								case 12:System.out.println("权限不足");break;
								case 13:report.show();break;
								case 67:System.out.println("client接收AS-C成功，准备切断TCP连接.");break;
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
				// TODO 自动生成的 catch 块					e1.printStackTrace();
			}
			e.printStackTrace();
		}
		       		    	
	}
						
	/*public void run() {
		try {
			int resend = 0;
			testOutPut();
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//远程发送到server的输出流
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//远程从server接收的输入流
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				System.out.println("id-" + id + " :receive :" + new String(line) + " ,from:" + s.getInetAddress() + ":" + s.getPort() + " at : " + new Timestamp(System.currentTimeMillis()));
				if(line.equals("c-as")){
					System.out.println("开始接收数据");
					//接收数据
					byte[] receivedata = SandR.readDes_bytes(fromClient);
					byte _type = 1;
					
					//首先验证类型是否是自己需要的
					if(receivedata[0] == _type){
						C_AS c2 = new C_AS(receivedata);		
						c2.show();
						//判断时间
						if(System.currentTimeMillis()-c2.getTS() > 300){
							System.out.println("数据包超时，有被重放攻击的危险");
							byte type = 13;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}					
						/*
						//从数据库中取数据
						//获取用户名、密码，此处应由数据库获取
						String[] UserID = {"ydyd","lyly","jrjr","lxlx"};
						String[] UserPass = {"ydyd2016","lyly2016","jrjr2016","lxlx2016"};
						String input_name = c2.getIDc();
						int User_IDnum=0;
						for(;User_IDnum<UserID.length;User_IDnum++){
							if(UserID[User_IDnum].equals(input_name)){
								System.out.println(User_IDnum + "该用户存在!");
								break;
							}
						}
						System.out.println(UserID[User_IDnum]);
						//System.out.println(UserID[User_IDnum] + UserPass[User_IDnum]);
						if(User_IDnum == UserID.length){
							System.out.println("用户名不存在");
							byte type = 8;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
		
						//判断Kc
						//计算Kc,有用户名与密码字符串相加转换为byte[]进行hash之后取32-95位
						MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
						byte[] output = messageDigest.digest((UserID[User_IDnum] + UserPass[User_IDnum]).getBytes());//MD5运算，用到random与kc
						byte[] Kc;
						Kc = new byte[8];
						for(int i = 0;i<8;i++){Kc[i] = output[i+4];}
						//System.out.println(new String(Kc));
						byte[] random_hash_byte = messageDigest.digest(ourMath.long2bytes(c2.getRandom() + ourMath.bytes2long(Kc) + c2.getTS()));//MD5运算，用到random与kc
						if(!Arrays.equals(Arrays.copyOfRange(random_hash_byte,4,8),ourMath.intToByteArray(c2.getRandom_hash(),4))){
							System.out.println("密码错误");
							byte type = 9;
							SandR.sendControl(type,c2.getIDc(),toClient);
							resend++;
							line_in = SandR.readDes_bytes(fromClient);
							line = new String(line_in);
							continue;
						}
						//判断TGSServer是否存在
						/*
						if(false){
							//数据库操作
							System.out.println("所请求TGSServer不存在");
							byte type = 10;
							sendControl(type,c2.getIDc(),toClient);
							resend++;
							line = readDes_String(fromClient);
							break;
						}
						//检查权限
						//数据库操作
						if(false){
							System.out.println("权限不足");
							byte type = 12;
							sendControl(type,c2.getIDc(),toClient);
							resend++;
							line = readDes_String(fromClient);
							break;
						}
						*/
						//通过所有检查，回复AS_C
						/*System.out.println("通过校验");
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
						case 7:System.out.println("目标不是ASServer，请联系管理员");break;
						case 8:System.out.println("用户名不存在");break;
						case 9:System.out.println("密码错误");break;
						case 10:System.out.println("所请求目标不存在");break;
						case 12:System.out.println("权限不足");break;
						case 13:report.show();break;
						case 67:System.out.println("client接收AS-C成功，准备切断TCP连接.");break;
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
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}*/
	//退出
	private void exit() throws IOException{
		System.out.println("id:" + id + " is free,socket " + s.getInetAddress() + ":" + s.getPort() + " has left");
		s.close();
		isbusy = false;
	}
	//返回是否正忙标记
	public boolean getIsBusy(){return isbusy;}
	//测试用输出
	public void testOutPut(){
		Integer socketAdd = new Integer(s.getPort());
		System.out.println("new Client! id-" + this.id + "   " + s.getInetAddress() + ":" + socketAdd.toString());
	}
}