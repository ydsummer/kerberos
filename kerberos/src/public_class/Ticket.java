package public_class;

import java.sql.Timestamp;
import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;

public class Ticket {//36
	private byte[] Kdes;//对Ticket加密时使用的K,8byte	
	
	private byte[] K;//Ticket中包含的K,8byte
	private byte num;//
	private String IDc;//UserID
	private byte[] ADc;//Client的初始ip1
	
	private int IDtgs;//4
	private long TS;//8
	private long Lifetime;//8
	//Ticket的构造函数，本地创造类实例时使用
	public Ticket(byte[] _K,byte[] _Kdes,String _IDc,byte[] _ADc,int _IDtgs){
		K = _K;
		Kdes = _Kdes;
		IDc = _IDc;
		ADc = _ADc;//遇到nat会出问题
		num = (byte)IDc.getBytes().length;
		IDtgs = _IDtgs;
		TS = System.currentTimeMillis();
		Lifetime = 2 * 1000 * 60 * 60;
	}
	//生成未加密的byte数组
	public byte[] getData(){
		byte[] data = new byte[33 + IDc.length()];
		byte[] ik =this.K;
		byte[] iidc=this.IDc.getBytes();
		byte[] iadc=this.ADc;
		byte[] iidtgs=ourMath.intToByteArray(this.IDtgs,4);
		byte[] its=ourMath.long2bytes(this.TS);
		byte[] ilifetime=ourMath.long2bytes(this.Lifetime);
		int i=0;

		for(byte a:ik){data[i++]=a;}
		for(byte a:iadc){data[i++]=a;}
		for(byte a:iidtgs){data[i++]=a;}
		for(byte a:its){data[i++]=a;}
		for(byte a:ilifetime){data[i++]=a;}
		data[i++] = num;
		for(byte a:iidc){data[i++]=a;}
		
		return data;
	}
	
	//Ticket的des算法，返回byte[],使用kdes加密data
	public byte[] encryptData(){
		//使用Kdes加密data
		byte[] result = DES.encrypt(this.getData(),Kdes);	
		return result;
	}
	//Ticket的解包，先解des，然后分拆
	public Ticket(byte[] _data,byte[] _Kdes) throws Exception{
		Kdes = _Kdes;
		//System.out.println("1234 " + _data.length);
		byte[] data = DES.decrypt(_data, Kdes);
		//System.out.println(new String(data));
		K = Arrays.copyOfRange(data, 0, 8);//Ticket中包含的K,8byte
		ADc = Arrays.copyOfRange(data, 8, 12);//Client的初始ip1
		IDtgs = ourMath.byteArrayToint(Arrays.copyOfRange(data, 12, 16));//4
		TS = ourMath.bytes2long(Arrays.copyOfRange(data, 16, 24));//8
		Lifetime = ourMath.bytes2long(Arrays.copyOfRange(data, 24, 32));//8
		num = data[32];
		IDc = new String(Arrays.copyOfRange(data, 33, 33+(int)num));//UserID
	}
	
	
	//获取IDc
	public String getIDc(){
		return IDc;
	}
	//获取IDtgs
	public int getIDtgs(){
		return IDtgs;
	}
	//获取ADc
	public String getADc(){
		return (ADc[0] & 0xff) + "." + (ADc[1] & 0xff) + "." + (ADc[2] & 0xff) + "." + (ADc[3] & 0xff);
	}
	//获取TS
	public long getTS(){
		return TS;
	}
	//获取LifeTimeS
	public long getLifeTime(){
		return Lifetime;
	}
	//获取K
	public byte[] getK(){
		return K;
	}
	//测试输出
	public void show(){
		System.out.println("    Ticket show ");
		System.out.println("ADc " + this.getADc());
		System.out.println("IDc " + this.getIDc());
		System.out.println("IDtgs " + this.getIDtgs());
		System.out.println("TS " + new Timestamp(this.getTS()));
		System.out.println("Lifetime " + this.getLifeTime());
	}
	public static void main(String args[]) throws Exception{
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		String UserID = "wym";
		int IDtgs = 123;
		Ticket t = new Ticket(ourMath.long2bytes((long)2345),ourMath.long2bytes((long)123),UserID,ADc,IDtgs);
		//System.out.println(t.getData());
		t.show();
		Ticket t2 = new Ticket(t.encryptData(),ourMath.long2bytes((long)123));
		t2.show();
	}
}

