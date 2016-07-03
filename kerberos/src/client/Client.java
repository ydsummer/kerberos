package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.MessageDigest;
import public_class.AS_C;
import public_class.Authenticator;
import public_class.C_AS;
import public_class.C_TGS;
import public_class.C_V;
import public_class.C_V_C;
import public_class.Control;
import public_class.TGS_C;
import public_class.V_C;
import public_method.SandR;

public class Client {
	static String ASServer_addr; //�洢ASServer��IP��ַ
	static int ASServer_port;//�洢ASServer��port	
	static String TGSServer_addr; //�洢TGSServer��IP��ַ
	static int TGSServer_port;//�洢TGSServer��port	
	static String VServer_addr; //�洢VServer��IP��ַ
	static int VServer_port;//�洢VServer��port	
	static String GroupServer_addr;
	static int GroupServer_port;
	
	static String UserID;//�û�ID
	static String UserPass;//�û�����
	static byte[] Kc;//�洢Client��ASserver�����ڼ��ܵ�Keyc�����û�ID�û�����hash��ȡ32-63λ����
	static byte[] Kctgs;
	static byte[] Kcv;
	static int IDtgs = 234;
	static int IDv = 345;
	
	static C_AS c2as;
	static AS_C as2c;
	static C_TGS c2tgs;
	static TGS_C tgs2c;
	static C_V c2v;
	static V_C v2c;
	
	static Socket socket;
	static Socket socket_tgs;
	static Socket socket_v;
	
	static BufferedReader fromClient;
	static DataOutputStream toServer;
	static DataInputStream fromServer;
	
	public static void main(String args[]){
		try{
			//��ʼ����������Ϣ
			ASServer_addr = "127.0.0.1";
			ASServer_port = 4700;
			TGSServer_addr = "127.0.0.1";
			TGSServer_port = 4600;
			VServer_addr = "127.0.0.1";
			VServer_port = 4500;
			GroupServer_addr = "127.0.0.1";
			GroupServer_port = 4400;
			
			//System.out.println(new String(Kc));
			
			//Kc������ɣ���ʼ����ASServer
			socket = new Socket(ASServer_addr,ASServer_port);
			System.out.println(socket.toString() + socket.getPort());
			
			//�������������
			fromClient = new BufferedReader(new InputStreamReader(System.in));//���ؼ���������
			toServer = new DataOutputStream(socket.getOutputStream());//Զ�̷��͵�server�������
			fromServer = new DataInputStream(socket.getInputStream());//Զ�̴�server���յ�������
			
			//��ʼ���û��������룬�˴�Ӧ��gui����
			System.out.println("�������û���");
			UserID = fromClient.readLine();
			
			System.out.println("����������");
			UserPass = fromClient.readLine();
			//�����������

			
			//����Kc,���û����������ַ������ת��Ϊbyte[]����hash֮��ȡ32-95λ
			MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
			byte[] MD5_output = messageDigest.digest((UserID + UserPass).getBytes());//MD5���㣬�õ�random��kc
			Kc = new byte[8];
			for(int i = 0;i<8;i++){Kc[i] = MD5_output[i+4];}
			
			
			//step1
			String output;
			boolean willContinue = true;
			output = "c-as";
			while(!output.equals("bye")){
				willContinue = true;
				if(output.equals("c-as")){
					//stepOne
					SandR.sendDes_bytes(output.getBytes(),toServer);
					c2as = new C_AS(UserID,IDtgs,Kc);
					SandR.sendDes_bytes(c2as.datatoBytes(),toServer);
					Control report = new Control(SandR.readDes_bytes(fromServer));
					if(report.getIDc().equals(UserID)){
						switch(report.getType()){
							case 7:System.out.println("Ŀ�겻��ASServer������ϵ����Ա");break;
							case 8:{
								System.out.println("�û���������");
								System.out.println("�����������û���");
								UserID = fromClient.readLine();
								messageDigest = MessageDigest.getInstance("MD5"); 
								MD5_output = messageDigest.digest((UserID + UserPass).getBytes());//MD5���㣬�õ�random��kc
								Kc = new byte[8];
								for(int i = 0;i<8;i++){Kc[i] = MD5_output[i+4];}
								break;
							}
							case 9:{
								System.out.println("�������");
								System.out.println("��������������");
								UserPass = fromClient.readLine();
								messageDigest = MessageDigest.getInstance("MD5"); 
								MD5_output = messageDigest.digest((UserID + UserPass).getBytes());//MD5���㣬�õ�random��kc
								Kc = new byte[8];
								for(int i = 0;i<8;i++){Kc[i] = MD5_output[i+4];}
								break;
							}
							case 10:System.out.println("������Ŀ�겻����");break;
							case 12:System.out.println("Ȩ�޲���");break;
							case 13:report.show();willContinue = false;output = "c-as";break;
							case 66:{
								System.out.println("ASServer ���ճɹ���׼�����ܻظ����ģ�");
								byte[] input = SandR.readDes_bytes(fromServer);
								byte type = 2;
								if(input[0] == type){
									as2c = new AS_C(input,Kc);
									as2c.show();
									
									//as2c.getTicket().show();
									byte report2 = 67;
									SandR.sendControl(report2,UserID,toServer);
									output="bye";
									SandR.sendDes_bytes(output.getBytes(),toServer);
									willContinue = false;
									System.out.println(output);
									/*byte[] ADc = socket.getLocalAddress().getAddress();
									int IDtgs = 123;
									int IDv = 234;
									String UserID = "wym";	
									Authenticator au = new Authenticator("wym",ADc,ourMath.long2bytes(2345));
									Ticket t = new Ticket(ourMath.long2bytes((long)123),ourMath.long2bytes((long)2345),UserID,ADc,IDtgs);
									C_TGS c = new C_TGS(IDv,t,au);*/
								}else{
									System.out.println("�ظ����ݴ���");
								}
								break;
							}
							default:report.show();break;
						}
					}else{
						System.out.println("�Ǳ���Ӧ�ý��յĿ��Ʊ���");
					}
				}else{
					SandR.sendDes_bytes(output.getBytes(),toServer);
					System.out.println("receive from server : " + new String(SandR.readDes_bytes(fromServer)));
				}
				if(willContinue){
					output = "c-as";
				}
			}
			
			System.out.println("receive from server : " + SandR.readDes_bytes(fromServer));
			socket.close();
			fromServer.close();
			toServer.close();
			System.out.println("done");
			
			
			
			
			//step2
			
			
			
			
			
			socket_tgs = new Socket(TGSServer_addr,TGSServer_port);
			toServer = new DataOutputStream(socket_tgs.getOutputStream());//Զ�̷��͵�server�������
			fromServer = new DataInputStream(socket_tgs.getInputStream());//Զ�̴�server���յ�������
			
			byte[] ADc = socket_tgs.getLocalAddress().getAddress();
			Kctgs = as2c.getKctgs();
			Authenticator au = new Authenticator("wym",ADc,Kctgs);
			c2tgs = new C_TGS(IDtgs,as2c.getTicketBytes(),au);
			//c_tgs.getAuthenticator_bytes();
			SandR.sendDes_bytes("c-tgs".getBytes(),toServer);
			SandR.sendDes_bytes(c2tgs.datatoSend(),toServer);
			
			Control report = new Control(SandR.readDes_bytes(fromServer));
			if(report.getIDc().equals(UserID)){
				switch(report.getType()){
					case 7:System.out.println("Ŀ�겻��TGSServer������ϵ����Ա");break;
					case 8:System.out.println("�û���������");break;
					case 9:System.out.println("�������");break;
					case 10:System.out.println("������Ŀ�겻����");break;
					case 12:System.out.println("Ȩ�޲���");break;
					case 13:report.show();willContinue = false;output = "c-as";break;
					case 14:report.show();break;
					case 15:report.show();break;
					case 68:{
						System.out.println("TGSServer��������ɹ���׼�����ջظ�����");
						byte[] input = SandR.readDes_bytes(fromServer);
						byte type = 4;
						if(input[0] == type){
							tgs2c = new TGS_C(input,Kctgs);
							tgs2c.show();	
							Kcv = tgs2c.getK();
							byte report2 = 68;
							SandR.sendControl(report2,UserID,toServer);
							
						}else{
							System.out.println("�ظ����ݴ���");
						}
						break;
					}
					default:report.show();break;
				}
			}
			SandR.sendDes_bytes("bye".getBytes(),toServer);
			System.out.println("receive from server : " + new String(SandR.readDes_bytes(fromServer)));
			socket.close();
			fromServer.close();
			toServer.close();
			System.out.println("done");
			
			//step3
			
			
			
			
			socket_v = new Socket(VServer_addr,VServer_port);
			toServer = new DataOutputStream(socket_v.getOutputStream());//Զ�̷��͵�server�������
			fromServer = new DataInputStream(socket_v.getInputStream());//Զ�̴�server���յ�������
			
			Authenticator authen = new Authenticator(UserID, ADc,Kcv);
			c2v = new C_V(tgs2c.getTicketBytes(),authen);
			//c_tgs.getAuthenticator_bytes();
			SandR.sendDes_bytes("c-v".getBytes(),toServer);
			//report = new Control(SandR.readDes_bytes(fromServer));
			SandR.sendDes_bytes(c2v.datatoSend(),toServer);
			
			report = new Control(SandR.readDes_bytes(fromServer));
			if(report.getIDc().equals(UserID)){
				switch(report.getType()){
					case 7:System.out.println("Ŀ�겻��TGSServer������ϵ����Ա");break;
					case 8:System.out.println("�û���������");break;
					case 9:System.out.println("�������");break;
					case 10:System.out.println("������Ŀ�겻����");break;
					case 12:System.out.println("Ȩ�޲���");break;
					case 13:report.show();willContinue = false;output = "c-as";break;
					case 14:report.show();break;
					case 15:report.show();break;
					case 70:{
						System.out.println("VServer��������ɹ���׼�����ջظ�����");
						byte[] input = SandR.readDes_bytes(fromServer);
						byte type = 6;
						if(input[0] == type){

							
							v2c = new V_C(input,Kcv); 
							v2c.show();
							if(v2c.getTS() - c2v.getAuthenticator_instance().getTS() == 1){
								byte report2 = 71;
								SandR.sendControl(report2,UserID,toServer);
								System.out.println("��֤�ɹ������Կ�ʼͨ��");
								C_V_C send;
								C_V_C receive;
								//fromServer.close();
								//new InputStreamReader(socket_v.getInputStream()));
								//GroupReceiveThread g = new GroupReceiveThread(reader);
								output = fromClient.readLine();
								
								while(!output.equals("bye")){
									
									send = new C_V_C(System.currentTimeMillis(),Kcv,output.getBytes());
									SandR.sendDes_bytes(send.datatoSend(),toServer);
									receive = new C_V_C(SandR.readDes_bytes(fromServer),Kcv);
									output = new String(receive.getData());
									System.out.println(new String(receive.getData()));
									output = fromClient.readLine();
								}
								SandR.sendDes_bytes(output.getBytes(),toServer);
								System.out.println("receive from server : " + new String(SandR.readDes_bytes(fromServer)));
							}else{
								System.out.println("ʱ�������!");
							}
						}else{
							System.out.println("�ظ����ݴ���");
						}
						break;
					}
					default:report.show();break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
	
}
