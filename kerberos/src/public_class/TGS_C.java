package public_class;

import java.sql.Timestamp;
import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;


public class TGS_C {//长58
	byte Type;//1Bytes的类型代码
	byte[] Kcv;//8
	int IDv;//4
	long TS4;//8Bytes
	byte num_ticket;//1	
	byte[] ticketv;//36
	Ticket Ticketv;
	
	byte[] Kctgs;
	//本地构造函数
	public TGS_C(byte[] _Kcv,byte[] _Kctgs,int _IDv,Ticket _Ticketv){
		Type = 4;
		Kcv = _Kcv;
		IDv = _IDv;
		TS4 = System.currentTimeMillis();
		Ticketv = _Ticketv;
		ticketv = Ticketv.encryptData();
		num_ticket = (byte) ticketv.length;
		
		Kctgs = _Kctgs;
		
	}
	//封包,,TGS->c加密,不加密type
	public byte[] datatoSend(){
		
		byte[] endata = new byte[21 + num_ticket];
		byte[] idv = ourMath.intToByteArray(IDv, 4);
		byte[] ts4 = ourMath.long2bytes(TS4);
		int i=0;
		for(byte a:Kcv){endata[i]=a;i++;}
		for(byte a:idv){endata[i]=a;i++;}
		for(byte a:ts4){endata[i]=a;i++;}
		endata[i]= num_ticket;i++;
		for(byte a:ticketv){endata[i]=a;i++;}
		/********改成用Kctgs加密*********/
		byte[] iendata = DES.encrypt(endata,Kctgs);
		//数据加密后，首位加入type
		byte[] data = new byte[1 + iendata.length];
		data[0] = Type;
		i=1;
		for(byte a:iendata){data[i]=a;i++;}
		
		return data;
	}	
	//拆包，取出type后，将剩余部分解密，因为type没有加密
	public TGS_C(byte[] buffer,byte[] _Kctgs){
		try {
			
			Kctgs = _Kctgs;
			Type = buffer[0]; 
			/****取出type后的数据进行解密***///改成用Kctgs解密
			byte[] deData = Arrays.copyOfRange(buffer, 1, buffer.length);
			byte[] data = DES.decrypt(deData,Kctgs);
			Kcv = Arrays.copyOfRange(data, 0, 8);			
			IDv =ourMath.byteArrayToint(Arrays.copyOfRange(data, 8, 12));	
			TS4 =ourMath.bytes2long(Arrays.copyOfRange(data, 12, 20));
			num_ticket = data[20];
			ticketv = Arrays.copyOfRange(data, 21, 21 + num_ticket);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
		
	
	//获取IDv
	public int getIDv(){
		return IDv;
	}
	//获取Kcv
	public byte[] getK(){
		return Kcv;
	}	
	//获取TS
	public long getTS(){
		return TS4;
	}
	//返回Ticket的结构体
	public Ticket getTicket(){
		return Ticketv;
	}
	//返回Ticket的字符串
	public byte[] getTicketBytes(){
		return ticketv;
	}
	//测试输出
	public void show(){
		System.out.println("    TGS_C 输出测试");
		System.out.println("IDv  " + this.getIDv());
		System.out.println("TS " + new Timestamp(this.getTS()));
	}
	public static void main(String args[]){
		//Ticketv = Kv(Kcv,IDc,ADc,IDv,TS4,Lifetime4)
		//TGS_C = Kctgs(Kcv,IDv,TS4,Ticket4)
		byte[] Kv = ourMath.long2bytes(234);
		byte[] Kcv = ourMath.long2bytes(3456);
		byte[] Kctgs = ourMath.long2bytes(2345);
		String UserID = "wym";
		int IDtgs = 234;
		int IDv = 345;
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		Ticket t = new Ticket(Kcv,Kv,UserID,ADc,IDtgs);
		//t.show();
		
		TGS_C tgs_c = new TGS_C(Kcv,Kctgs,IDv,t);
		tgs_c.show();
		tgs_c.getTicket().show();
		
		TGS_C tgs_c2 = new TGS_C(tgs_c.datatoSend(),Kctgs);
		tgs_c2.show();
	}	
}
