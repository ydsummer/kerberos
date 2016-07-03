package public_class;

import java.sql.Timestamp;
import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;


public class AS_C {
	byte Type;//1Bytes的类型代码
	byte num;
	byte[] Kctgs;
	String IDc;//ClientID
	int IDtgs;//4Bytes的tgsServerID
	long TS2;//8Bytes的TS(所占空间等于long所占空间，8bytes)
	long Lifetime2;//8Bytes的lifetime(所占空间等于long所占空间，8bytes)
	Ticket Tickettgs;//32
	byte[] TickettgsAtClient;
	
	//根据传入信息生成AS_C数据包
	public AS_C(String _IDc,int _IDtgs,Ticket _tickettgs,byte[] _Kctgs){
		Type = 2;
		IDc = _IDc;
		IDtgs = _IDtgs;
		Tickettgs = _tickettgs;
		TickettgsAtClient = Tickettgs.encryptData();
		num = (byte)TickettgsAtClient.length;
		TS2 = System.currentTimeMillis();
		Lifetime2 = 2 * 1000 * 60 * 60;
		Kctgs = _Kctgs;
	}
	//不含type的数据
	public byte[] getData(){
		byte[] iidtgs=ourMath.intToByteArray(IDtgs, 4);
		byte[] its=ourMath.long2bytes(TS2);
		byte[] ilifetime=ourMath.long2bytes(Lifetime2);
		byte[] iticket=Tickettgs.encryptData();
		byte[] data = new byte[29 + iticket.length];
		int i=0;
		for(byte a:iidtgs){data[i++]=a;}
		for(byte a:its){data[i++]=a;}
		for(byte a:ilifetime){data[i++]=a;}
		for(byte a:Kctgs){data[i++]=a;}
		data[i++]=num;
		//System.out.println(i + "&&&" + num);
		for(byte a:iticket){data[i++]=a;}
		return data;
	}
	//加密data,再加上Type
	public byte[] encrytData(byte[] Kc){
		/*改成用Kc加密Data*/
		byte[] data = getData();
		byte[] result = DES.encrypt(data,Kc);
     	byte[] out = new byte[result.length+1];
     	out[0] = Type;
     	int i=1;
     	for(byte a:result){out[i++]=a;}
		return out;
	}		
	//拆包
	public AS_C(byte[] data,byte[] Kc) {//此处的Kc就是client的Kc
		try {
			byte[] enData = new byte[data.length-1];
			enData = Arrays.copyOfRange(data,1,data.length);
			byte[] decryResult= DES.decrypt(enData,Kc);
			//int length =decryResult.length;
			
			Type = data[0];
			
			byte[] idtgs = Arrays.copyOfRange(decryResult, 0, 4);
			byte[] ts2 = Arrays.copyOfRange(decryResult, 4, 12);
			byte[] lifetime = Arrays.copyOfRange(decryResult, 12, 20);
			Kctgs = Arrays.copyOfRange(decryResult, 20, 28);
			num = decryResult[28];
			
			byte[] ticket = Arrays.copyOfRange(decryResult, 29, 29+num);
			IDtgs = ourMath.byteArrayToint(idtgs);
			TS2 = ourMath.bytes2long(ts2);
			Lifetime2 = ourMath.bytes2long(lifetime);
			TickettgsAtClient = ticket;
			//Tickettgs = new Ticket(ticket,ourMath.long2bytes((long)123));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//返回type
	public byte getType(){
		return Type;
	}

	//返回IDtgs
	public int getIDtgs(){
		return IDtgs;
	}
	//返回TS
	public long getTS(){
		return TS2;
	}
	//返回lifetime
	public long getLifetime(){
		return Lifetime2;
	}
	//返回Ticket的结构体
	public Ticket getTicket(){
		return Tickettgs;
	}
	//返回Ticket的字符串
	public byte[] getTicketBytes(){
		return TickettgsAtClient;
	}
	//返回Ticket的字符串
		public byte[] getKctgs(){
			return Kctgs;
		}
	//测试用输出
	public void show(){
		System.out.println("    AS_C show ");
		System.out.println("IDtgs " + this.getIDtgs());
		System.out.println("TS " + new Timestamp(this.getTS()));
		System.out.println("Lifetime " + this.getLifetime());
	}
	public static void main(String args[]){
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		String UserID = "wym";
		int IDtgs = 123;
		Ticket t = new Ticket(ourMath.long2bytes((long)2345),ourMath.long2bytes((long)123),UserID,ADc,IDtgs);
		AS_C as = new AS_C("wym",123,t,ourMath.long2bytes((long)2345));//Kctgs--ourMath.long2bytes((long)2345)
		as.show();
		byte[] as_c_bytes = as.encrytData(ourMath.long2bytes((long)123));//Kc--ourMath.long2bytes((long)123)
		AS_C as2 = new AS_C(as_c_bytes,ourMath.long2bytes((long)123));//Kc
		as2.show();
		as2.getTicket().show();
		
	}
}
