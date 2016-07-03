package tgsserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import public_class.Authenticator;
import public_class.C_TGS;
import public_class.TGS_C;
import public_class.Ticket;
import public_method.SandR;
import public_method.ourMath;

public class TGSServerThread extends Thread {
	int id;
	private boolean isbusy;//是否正在忙的标记
	private Socket s;
	public TGSServerThread(int _id) {
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
			testOutPut();
			id = 234;
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//远程发送到server的输出流
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//远程从server接收的输入流
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				if(line.equals("c-tgs")){
					byte [] c_byte = SandR.readDes_bytes(fromClient);
					byte type = 3;
					if(c_byte[0] == type){
						C_TGS c = new C_TGS(c_byte);
						byte[] Kctgs = ourMath.long2bytes(2345);
						byte[] Ktgs = ourMath.long2bytes(123);
						Authenticator au = new Authenticator(c.getAuthenticator_bytes(),Kctgs);
						Ticket t = new Ticket(c.getTicket_bytes(),Ktgs);
						au.show();
						t.show();
						//判断ID
						if(t.getIDtgs() != id){
							type = 7;
							SandR.sendControl(type,t.getIDc(),toClient);
						}
						//判断AD
						if(!t.getADc().equals(au.getADc())){
							type = 15;
							SandR.sendControl(type,t.getIDc(),toClient);
						}
						//判断时间
						if(t.getTS()+t.getLifeTime() < au.getTS()){
							type = 14;
							SandR.sendControl(type,t.getIDc(),toClient);
						}
						//从数据库中读取VServer信息与权限信息
						//如果Vserver不存在
						/*if(t.getTS()+t.getLifeTime() < au.getTS()){
							type = 8;
							sendControl(type,t.getIDc(),toClient);
						}*/
						//判断权限
						/*if(权限){
							type = 12;
							sendControl(type,t.getIDc(),toClient);
						}*/
						System.out.println("通过校验");
						type = 68;
						SandR.sendControl(type,t.getIDc(),toClient);
						
						byte[] Kv = ourMath.long2bytes(234);
						byte[] Kcv = ourMath.long2bytes(3456);
						Kctgs = t.getK();
						String UserID = t.getIDc();
						int IDtgs = id;
						int IDv = c.getIDv();
						byte[] ADc = new byte[4];
						ADc = s.getInetAddress().getAddress();
						t = new Ticket(Kcv,Kv,UserID,ADc,IDtgs);
						//t.show();
						TGS_C tgs_c = new TGS_C(Kcv,Kctgs,IDv,t);
						SandR.sendDes_bytes(tgs_c.datatoSend(),toClient);
					}else{
						SandR.sendDes_bytes("get out".getBytes(),toClient);
					}
				}
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
		
	}
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