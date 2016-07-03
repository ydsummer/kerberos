package public_class;

import java.sql.Timestamp;
import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;

public class Authenticator {
	
	private byte[] ADc;//4
	private long TS;//8
	private byte num;
	private String IDc;//4
	private byte[] K;
	//���캯��
	public Authenticator(String _IDc,byte[] _ADc,byte[] _K){
		IDc = _IDc;
		num = (byte)IDc.getBytes().length;
		ADc = _ADc;
		TS = System.currentTimeMillis();
		K = _K;
		//����ʱ��
	}
	//���ܺ�������Authenticator���н���
	public Authenticator(byte[] buffer,byte[] _K){
		//ʹ��Kctgs��buffer���н���֮���ٲ���
		//����ʱ��
		/*****�ĳ���Kctgs����******/
		try {
			K = _K;
			byte[] result = DES.decrypt(buffer,K);
			ADc = Arrays.copyOfRange(result, 0, 4);
			TS = ourMath.bytes2long(Arrays.copyOfRange(result, 4, 12));
			num = result[13];
			IDc = new String(Arrays.copyOfRange(result, 13, 13 + num));	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��ȡ
	public String getIDc(){
		return IDc;
	}
	//��ȡ
	//��ȡADc
	public String getADc(){
		return (ADc[0] & 0xff) + "." + (ADc[1] & 0xff) + "." + (ADc[2] & 0xff) + "." + (ADc[3] & 0xff);
	}
	//��ȡ
	public long getTS(){
		return TS;
	}
	
	
	
	
	//����,�õ�Authenticator
	public byte[] datatobytes(){
		//ʹ��Kctgs�����ݽ��м���֮���ٲ���
		byte[] data = new byte[13+num];
		byte[] idc = this.IDc.getBytes();
		byte[] adc = this.ADc;
		byte[] ts3 = ourMath.long2bytes(this.TS);
		int i = 0;
		for(byte a:adc){data[i]=a;i++;}
		for(byte a:ts3){data[i]=a;i++;}
		data[i++] = num;
		for(byte a:idc){data[i]=a;i++;}	
		/**�ĳ���Kctgs����***/
		byte[] out = DES.encrypt(data,K);
		return out;
	}
	//���������
	public void show(){
		System.out.println("    Authentication show ");
		System.out.println(this.getADc());
		System.out.println(IDc);
		System.out.println(new Timestamp(this.getTS()));
	}
	public static void main(String args[]){
		byte[] ADc = new byte[4];
		ADc[0]=(byte) 192;
		ADc[1]=(byte) 168;
		ADc[2]=(byte) 1;
		ADc[3]=(byte) 1;
		Authenticator au = new Authenticator("wym",ADc,ourMath.long2bytes(2345));//Kctgs
		au.show();
		Authenticator au2 = new Authenticator(au.datatobytes(),ourMath.long2bytes(2345));//Kctgs
		au2.show();
	}
}
