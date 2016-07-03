package public_class;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

import public_method.ourMath;

public class C_AS {
	private byte type;//1Bytes�����ʹ���
	private byte num;//IDc�ĳ���
	private String IDc;//4Bytes��ClientID
	private int IDtgs;//4Bytes��tgsServerID
	private long TS1;//��long��ʱ���
	private int random;//4bytes�������
	private int random_hash;//ʹ���Ϸ��������Kc hash��ȡ32-63λ���ɵ���֤�룬����ASserver��֤client����ȷ��
	
	private byte[] Kc;//8Byte��Kc
	
	
	//�����������C_AS
	public C_AS(String _IDc,int _IDtgs,byte[] _Kc) throws NoSuchAlgorithmException{
		//���TYPE=1
		//TS1Ϊ��ǰʱ��
		//randomΨһ�����
		//random_hashΪ�������Kc hash��ȡ32-63λ���ɵ���֤��
		type = 1;
		IDc = _IDc;
		num = (byte)IDc.getBytes().length;
		IDtgs = _IDtgs;
		TS1 = System.currentTimeMillis();
		Kc  = _Kc;
		random = new Random().nextInt(124);
		MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
		byte[] output = messageDigest.digest(ourMath.long2bytes((long)random + ourMath.bytes2long(Kc) + TS1));//MD5���㣬�õ�random��kc
		byte[] random_hash_byte = new byte[4];
		random_hash_byte[0] = output[4];
		random_hash_byte[1] = output[5];
		random_hash_byte[2] = output[6];
		random_hash_byte[3] = output[7];
		random_hash = ourMath.byteArrayToint(random_hash_byte);
	}
	//����buffer���ָ�
	public C_AS(byte[] buffer){
		try {
			type = buffer[0];
			num = buffer[21];
			IDc = new String(Arrays.copyOfRange(buffer, 22, 22+num));
			IDtgs = ourMath.byteArrayToint(Arrays.copyOfRange(buffer, 1, 5));
			TS1 = ourMath.bytes2long(Arrays.copyOfRange(buffer, 5, 13));
			random = ourMath.byteArrayToint(Arrays.copyOfRange(buffer, 13, 17));
			random_hash = ourMath.byteArrayToint(Arrays.copyOfRange(buffer, 17, 21));
			//Kc = _Kc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//���ݴ��
	public byte[] datatoBytes(){
		byte[] data = new byte[22 + IDc.length()];
		byte[] idc = IDc.getBytes();
		byte[] idtgs = ourMath.intToByteArray(IDtgs,4);
		byte[] ts1 = ourMath.long2bytes(TS1);
		byte[] _random = ourMath.intToByteArray(random,4);
		byte[] _random_hash = ourMath.intToByteArray(random_hash,4);
		data[0] = type;
		int i=1;
		for(byte a:idtgs){data[i++]=a;}
		for(byte a:ts1){data[i++]=a;}
		for(byte a:_random){data[i++]=a;}
		for(byte a:_random_hash){data[i++]=a;}
		data[i++]=num;
		for(byte a:idc){data[i++]=a;}
		//byte[] result = DES.encrypt(data,Kc);//��C_AS�ļ������㣬ֻ��kc
		//String out = new String(result);
		//System.out.println("������ܣ�"+result);
		//System.out.println("������ܣ�"+out);
		return data;
	}
	//����type
	public byte getType(){
		return type;
	}
	//����IDc
	public String getIDc(){
		return IDc;
	}
	//����IDtgs
	public int getIDtgs(){
		return IDtgs;
	}
	//����TS
	public long getTS(){
		return TS1;
	}
	//����random
	public int getRandom(){
		return random;
	}
	//����random_hash
	public int getRandom_hash(){
		return random_hash;
	}
	//����Kc
	public byte[] getKc(){
		return Kc;
	}
	//���������
	public void show(){
		System.out.println("C-AS show");
		System.out.println("type " + this.getType());
		System.out.println("IDc " + this.getIDc());
		System.out.println("IDtgs "  + this.getIDtgs());
		System.out.println("TS " + new Timestamp( this.getTS()));
		System.out.println("Random " + this.getRandom());
		System.out.println("Random_hash " + this.getRandom_hash());
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException{
		C_AS c = new C_AS("wym",123,ourMath.long2bytes((long)123));
		byte[] des = c.datatoBytes();
		System.out.println("length : " + des.length);
		C_AS c2 = new C_AS(des);
		System.out.println(c.getType());
		System.out.println(c2.getType());
		System.out.println(c.getIDc());
		System.out.println(c2.getIDc());
		System.out.println(c.getIDtgs());
		System.out.println(c2.getIDtgs());
		System.out.println(new Timestamp( c.getTS()));
		System.out.println(new Timestamp( c2.getTS()));
		System.out.println(c.getRandom());
		System.out.println(c2.getRandom());
		System.out.println(c.getRandom_hash());
		System.out.println(c2.getRandom_hash());
		System.out.println(c.getKc());
		System.out.println(c2.getKc());
	}
	
}