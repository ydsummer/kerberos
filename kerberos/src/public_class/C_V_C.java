package public_class;

import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;

public class C_V_C {//9
	byte Type;//1
	long TS;//8
	int num;
	byte[] data;
	byte[] Kcv;
	//���ع��캯��
	public C_V_C(long TS5,byte[] K,byte[] _data){
		Type = 6;
		TS = TS5 + 1;
		Kcv = K;
		data = _data;
		num = data.length;
	}
	//�����ȡ��type�󣬽�ʣ�ಿ�ֽ��ܣ���Ϊtypeû�м���
	public C_V_C(byte[] buffer,byte[] _K){
		try {
			Type = buffer[0];
			Kcv = _K;
			/****ȡ��type������ݽ��н���***///�ĳ���Kcv����
			byte[] data2 = DES.decrypt(Arrays.copyOfRange(buffer, 1, buffer.length),Kcv);
			TS = ourMath.bytes2long(Arrays.copyOfRange(data2, 0, 8));
			num = ourMath.byteArrayToint(Arrays.copyOfRange(data2, 8, 12));
			data = Arrays.copyOfRange(data2, 12, 12 + num);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//���,,TGS->c����,������type
	public byte[] datatoSend(){
		byte[] endata = new byte[12 + num];

		byte[] its = ourMath.long2bytes(TS);
		byte[] inum = ourMath.intToByteArray(num,4);
		int i=0;
		for(byte a:its){endata[i++]=a;}
		for(byte a:inum){endata[i++]=a;}
		for(byte a:data){endata[i++]=a;}
		
		byte[] eneddata = DES.encrypt(endata,Kcv);
		byte[] outdata = new byte[1+eneddata.length];
		outdata[0] = Type;
		i=1;
		for(byte a:eneddata){outdata[i++]=a;}

		return outdata;
	}	
	//����Ticket
	public byte getType(){
		return Type;
	}
	//��ȡTS
	public long getTS(){
		return TS;
	}
	//��ȡTS
	public byte[] getData(){
		return data;
	}
	//���������
	public void show(){
		System.out.println("    V_C show");
		System.out.println("Type " + getType());
		System.out.println("Ts " + getTS());
		System.out.println("Data " + new String(getData()));
	}
	public static void main(String args[]){
		byte[] Kcv = ourMath.long2bytes(3456);
		C_V_C v = new C_V_C((long)123456,Kcv,"hello".getBytes());
		C_V_C v2 = new C_V_C(v.datatoSend(),Kcv);
		v2.show();
	}
}
