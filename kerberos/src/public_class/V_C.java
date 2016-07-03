package public_class;

import java.util.Arrays;

import public_method.DES;
import public_method.ourMath;

public class V_C {//9
	byte Type;//1
	long TS;//8
	byte[] Kcv;
	//���ع��캯��
	public V_C(long TS5,byte[] K){
		Type = 6;
		TS = TS5 + 1;
		Kcv = K;	
	}
	//�����ȡ��type�󣬽�ʣ�ಿ�ֽ��ܣ���Ϊtypeû�м���
	public V_C(byte[] buffer,byte[] _K){
		try {
			Type = buffer[0];
			Kcv = _K;
			/****ȡ��type������ݽ��н���***///�ĳ���Kcv����
			byte[] data = DES.decrypt(Arrays.copyOfRange(buffer, 1, buffer.length),Kcv);
			TS = ourMath.bytes2long(data);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	//���,,TGS->c����,������type
	public byte[] datatoSend(){
		byte[] TS_b= ourMath.long2bytes(TS);
		/********�ĳ���Kcv����*********/
		byte[] idata = DES.encrypt(TS_b,Kcv);
		
		byte[] data = new byte[1+idata.length];
		
		data[0] = Type;
		
		//���ݼ��ܺ���λ����type
		int x=1;
		for(byte a:idata){data[x++]=a;}
		return data;
	}	
	//����Ticket
	public byte getType(){
		return Type;
	}
	//��ȡTS
	public long getTS(){
		return TS;
	}
	//���������
	public void show(){
		System.out.println("    V_C show");
		System.out.println("Type " + getType());
		System.out.println("Ts " + getTS());
	}
	public static void main(String args[]){
		byte[] Kcv = ourMath.long2bytes(3456);
		V_C v = new V_C((long)123456,Kcv);
		V_C v2 = new V_C(v.datatoSend(),Kcv);
		v2.show();
	}
}
