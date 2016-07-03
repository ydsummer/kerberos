package vserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import public_class.Authenticator;
import public_class.C_V;
import public_class.C_V_C;
import public_class.Control;
import public_class.Ticket;
import public_class.V_C;
import public_method.SandR;
import public_method.ourMath;

public class VtherThread extends Thread {
	int id;
	private boolean isbusy;//�Ƿ�����æ�ı��
	private Socket s;
	public VtherThread(int _id) {
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
			DataOutputStream toClient = new DataOutputStream(s.getOutputStream());//Զ�̷��͵�server�������
			DataInputStream fromClient = new DataInputStream(s.getInputStream());//Զ�̴�server���յ�������
			byte[] line_in;
			line_in = SandR.readDes_bytes(fromClient);
			String line = new String(line_in);
			while(!line.equals("bye")){
				if(line.equals("c-v")){
					byte [] c_byte = SandR.readDes_bytes(fromClient);
					byte type = 5;
					if(c_byte[0] == type){
						byte[] Kv = ourMath.long2bytes(234);
						C_V c_v = new C_V(c_byte,Kv);
						Authenticator au = c_v.getAuthenticator_instance();
						Ticket t = c_v.getTicket_instance();
						
						
						if(t.getIDtgs() != id){
							type = 7;
							SandR.sendControl(type,t.getIDc(),toClient);
							exit();
							return;
						}
						//�ж�AD
						if(!t.getADc().equals(au.getADc())){
							type = 15;
							SandR.sendControl(type,t.getIDc(),toClient);
							exit();
							return;
						}
						//�ж�ʱ��
						if(t.getTS()+t.getLifeTime() < au.getTS()){
							type = 14;
							SandR.sendControl(type,t.getIDc(),toClient);
							exit();
							return;
						}
						//�����ݿ��ж�ȡVServer��Ϣ��Ȩ����Ϣ
						//���Vserver������
						/*if(t.getTS()+t.getLifeTime() < au.getTS()){
							type = 8;
							sendControl(type,t.getIDc(),toClient);
						}*/
						//�ж�Ȩ��
						/*if(Ȩ��){
							type = 12;
							sendControl(type,t.getIDc(),toClient);
						}*/
						
						System.out.println("ͨ��У��");
						type = 70;
						SandR.sendControl(type,t.getIDc(),toClient);
						V_C v_c = new V_C(au.getTS(),t.getK());
						SandR.sendDes_bytes(v_c.datatoSend(),toClient);
						Control report = new Control(SandR.readDes_bytes(fromClient));
						if(report.getIDc().equals(t.getIDc())){
							System.out.println("Client�ѽ��ճɹ������Կ�ʼ�ṩ����");
							byte[] Kcv = t.getK();
							C_V_C send;
							C_V_C receive;
							while(true){
								receive = new C_V_C(SandR.readDes_bytes(fromClient),Kcv);
								String input = new String(receive.getData());
								if(input.equals("bye"))break;
								System.out.println(new String(receive.getData()));
								
								/*send0 = new C_V_C(receive.getTS(),Kcv,input.getBytes());
								for(int i=0;i < VServer.VtherThreadList.length;i++)
								{
									
									SandR.sendDes_bytes(send0.datatoSend(),toClient);
								}*/
								send = new C_V_C(receive.getTS(),Kcv,"continue!".getBytes());
								SandR.sendDes_bytes(send.datatoSend(),toClient);
							}
						}
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
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
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