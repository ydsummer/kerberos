package public_class;


import java.util.Arrays;

import public_method.ourMath;


public class C_TGS {//长58
	byte Type;//1Bytes的类型代码
	
	int IDv;//4
	byte num_ticket;//1
	byte[] ticket;//36
	Ticket Tickettgs;//36
	byte num_auth;//1
	byte[] authen;//16
	Authenticator Authen;//16
	
	int totalNum;
	//本地构造函数，Client将参数打包成C_TGS
	public C_TGS(int _IDv,byte[] t,Authenticator auth){
		IDv = _IDv;
		Type = 3;
		ticket = t;
		num_ticket = (byte)ticket.length;
		Authen = auth;
		authen = Authen.datatobytes();
		num_auth = (byte)authen.length;
		totalNum = 7 + num_ticket + num_auth;
		//添加TYPE=1
		//TS1为当前时间
		//random唯一随机数
		//random_hash为随机数与Kc hash后取32-63位生成的验证码		
	}
	/*
	//ASServer将参数打包成C_TGS
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
		//添加TYPE=1
		//TS1为当前时间
		//random唯一随机数
		//random_hash为随机数与Kc hash后取32-63位生成的验证码		
	}*/
	
	//将接收到的负载转换为C_TGS，拆包，没有解密，，，因为C->TGS没有加密
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
	//封包,,c->TGS不加密
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
	//返回IDv
	public int getIDv(){
		return IDv;
	}
	//返回Ticket
	public byte getType(){
		return Type;
	}
	
	//返回Ticket
	public byte[] getTicket_bytes(){
		return ticket;
	}
	//返回Ticket实例
	public Ticket getTicket_instance(){
		return Tickettgs;
	}
	//返回authenticator
	public byte[] getAuthenticator_bytes(){
		return authen;
	}
	//返回authenticator实例
	public Authenticator getAuthenticator_instance(){
		return Authen;
	}
	//测试用输出
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
