package public_class;

import java.util.Arrays;

public class Control {
	private byte type;//1Bytes�����ʹ���
	private String IDc;//ClientID
	//���캯��
	public Control(byte _type,String _IDc){
		type = _type;
		IDc = _IDc;
	}
	//���캯��
	public Control(String input){
		byte[] data = input.getBytes();
		type = data[0];
		IDc = new String(Arrays.copyOfRange(data, 1, data.length));
	}
	//���캯��
	public Control(byte[] input){
		type = input[0];
		IDc = new String(Arrays.copyOfRange(input, 1, input.length));
	}
	//ת��Ϊbyte�ĺ���
	public byte[] datatoBytes(){
		byte[] data = new byte[1 + IDc.length()];
		byte[] idc = IDc.getBytes();
		data[0] = type;
		int i=1;
		for(byte a:idc){data[i++]=a;}
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
	//���������
	public void show(){
		switch(type){
		case 7:System.out.println("7.	�Ҳ���Ҫ�����");break;
		case 8:System.out.println("8.	�û���������");break;
		case 9:System.out.println("9.	�������");break;
		case 10:System.out.println("10.	������TGSServer������");break;
		case 11:System.out.println("11.	������VServer������");break;
		case 12:System.out.println("12.	������Ȩ�޲���");break;
		case 13:System.out.println("13.	���ݰ���ʱ���ش�");break;
		case 14:System.out.println("14.	Ticket����");break;
		case 15:System.out.println("15. ��ַ���");break;
		case 16:System.out.println("");break;
		case 17:System.out.println("");break;
		case 66:System.out.println("66. ASServer���ճɹ���׼���ظ�����");break;
		case 67:System.out.println("67. Client���ճɹ������ظ���������");break;
		case 68:System.out.println("68. TGSServer���ճɹ���׼���ظ�����");break;
		default:System.out.println("δ֪���Ʊ��� + " + type + " + ����ϵ����Ա��");
		
		}
	}
	
}
