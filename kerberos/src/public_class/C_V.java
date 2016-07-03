package public_class;

import java.util.Arrays;

import public_method.ourMath;

public class C_V {//54
	byte Type;//1
	byte num_ticket;//1
	byte[] ticketv;//36
	Ticket Ticketv;
	byte num_auth;
	byte[] Authenticator;//16
	Authenticator Auth;
	//本地构造函数,Client端使用
	public C_V(byte[] _ticket,Authenticator _authen){
		Type = 5;
		ticketv = _ticket;
		num_ticket = (byte)ticketv.length;
		Auth = _authen;
		Authenticator = Auth.datatobytes();
		num_auth = (byte)Authenticator.length;
	}
	//将接收到的负载转换为C_TGS，拆包，没有解密，，，因为C->TGS没有加密
	public C_V(byte[] buffer,byte[] Kv){
		try {
			Type = buffer[0];
			num_ticket = buffer[1];
			ticketv = Arrays.copyOfRange(buffer, 2, 2 + num_ticket);
			Ticketv = new Ticket(ticketv,Kv);
			num_auth = buffer[2 + num_ticket];
			Authenticator = Arrays.copyOfRange(buffer, 3 + num_ticket, 3 + num_ticket + num_auth);
			Auth = new Authenticator(Authenticator,Ticketv.getK());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//封包，C->V没有加密
	public byte[] datatoSend(){
		byte[] data = new byte[3 + num_ticket + num_auth];
		data[0]=Type;
		data[1]=num_ticket;
		int i=2;
		for(byte a:ticketv){data[i]=a;i++;}
		data[i++] = num_auth;
		for(byte a:Authenticator){data[i]=a;i++;}
		return data;
	}
	//返回Ticket
	public byte getType(){
		return Type;
	}
	//返回Ticket
	public byte[] getTicket_bytes(){
		return ticketv;
	}
	//返回Ticket实例
	public Ticket getTicket_instance(){
		return Ticketv;
	}
	//返回authenticator
	public byte[] getAuthenticator_bytes(){
		return Authenticator;
	}
	//返回authenticator实例
	public Authenticator getAuthenticator_instance(){
		return Auth;
	}
	public static void main(String args[]){
		int IDtgs = 234;
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		byte[] Kcv = ourMath.long2bytes(3456);
		byte[] Kv = ourMath.long2bytes(234);
		String UserID = "wym";
		Ticket t = new Ticket(Kcv,Kv,UserID,ADc,IDtgs);
		t.show();
		
		Authenticator au = new Authenticator("wym",ADc,Kcv);
		au.show();
		
		C_V c_v = new C_V(t.encryptData(),au);
		C_V c_v2 = new C_V(c_v.datatoSend(),ourMath.long2bytes(234));
		c_v2.getAuthenticator_instance().show();
		c_v2.getTicket_instance().show();
		
	}
	
	
}
