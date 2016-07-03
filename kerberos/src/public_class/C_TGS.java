package public_class;


import java.util.Arrays;

import public_method.ourMath;


public class C_TGS {//��58
	byte Type;//1Bytes�����ʹ���
	
	int IDv;//4
	byte num_ticket;//1
	byte[] ticket;//36
	Ticket Tickettgs;//36
	byte num_auth;//1
	byte[] authen;//16
	Authenticator Authen;//16
	
	int totalNum;
	//���ع��캯����Client�����������C_TGS
	public C_TGS(int _IDv,byte[] t,Authenticator auth){
		IDv = _IDv;
		Type = 3;
		ticket = t;
		num_ticket = (byte)ticket.length;
		Authen = auth;
		authen = Authen.datatobytes();
		num_auth = (byte)authen.length;
		totalNum = 7 + num_ticket + num_auth;
		//���TYPE=1
		//TS1Ϊ��ǰʱ��
		//randomΨһ�����
		//random_hashΪ�������Kc hash��ȡ32-63λ���ɵ���֤��		
	}
	/*
	//ASServer�����������C_TGS
	public C_TGS(int _IDv,Ticket t,Authenticator auth){
		IDv = _IDv;
		Type = 3;
		Tickettgs = t;
		ticket = t.encryptData();
		num_ticket = (byte)ticket.length;
		Authen = auth;
		authen = (Authen.datatobytes());
		num_auth = (byte)authen.length;
		totalNum = 7 + num_ticket + num_auth;
		//���TYPE=1
		//TS1Ϊ��ǰʱ��
		//randomΨһ�����
		//random_hashΪ�������Kc hash��ȡ32-63λ���ɵ���֤��		
	}*/
	
	//�����յ��ĸ���ת��ΪC_TGS�������û�н��ܣ�������ΪC->TGSû�м���
	public C_TGS(byte[] buffer){
		try {
			Type = buffer[0]; 
			IDv = ourMath.byteArrayToint(Arrays.copyOfRange(buffer, 1, 5));
			num_ticket = buffer[5];
			ticket = Arrays.copyOfRange(buffer, 6,6 + num_ticket);
			Tickettgs = new Ticket(ticket,ourMath.long2bytes((long)123));
			num_auth = buffer[6 + num_ticket];
			authen = Arrays.copyOfRange(buffer,7 + num_ticket, 7 + num_ticket +num_auth);
			Authen = new Authenticator(authen,Tickettgs.getK());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//���,,c->TGS������
	public byte[] datatoSend(){
		byte[] data = new byte[totalNum];
		int i=0;
		byte[] idv = ourMath.intToByteArray(IDv, 4);
		
		data[i++] = Type;
		for(byte a:idv){data[i++]=a;}
		data[i++]=num_ticket;
		for(byte a:ticket){data[i++]=a;}
		data[i++]=num_auth;
		for(byte a:authen){data[i++]=a;}
		
		return data;
	}
	//����IDv
	public int getIDv(){
		return IDv;
	}
	//����Ticket
	public byte getType(){
		return Type;
	}
	
	//����Ticket
	public byte[] getTicket_bytes(){
		return ticket;
	}
	//����Ticketʵ��
	public Ticket getTicket_instance(){
		return Tickettgs;
	}
	//����authenticator
	public byte[] getAuthenticator_bytes(){
		return authen;
	}
	//����authenticatorʵ��
	public Authenticator getAuthenticator_instance(){
		return Authen;
	}
	//���������
	public void show(){
		System.out.println("IDv : " + IDv);
		//Tickettgs.show();
		//Authen.show();
	}
	public static void main(String args[]){
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		int IDtgs = 123;
		int IDv = 234;
		String UserID = "wym";	
		Authenticator au = new Authenticator("wym",ADc,ourMath.long2bytes(2345));
		Ticket t = new Ticket(ourMath.long2bytes((long)2345),ourMath.long2bytes((long)123),UserID,ADc,IDtgs);
		
		C_TGS c = new C_TGS(IDv,t.encryptData(),au);
		c.show();
		byte[] _c = c.datatoSend();
		C_TGS c2 = new C_TGS(_c);
		c2.show();
		c2.getTicket_instance().show();
		c2.getAuthenticator_instance().show();
	}
}
