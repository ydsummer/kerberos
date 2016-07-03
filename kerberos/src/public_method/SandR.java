package public_method;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import public_class.Control;

public class SandR {
	//����bytes����
	public static byte[] readDes_bytes(DataInputStream read) throws Exception{
		int num = read.readInt();
		byte[] input2 = new byte[num];
		read.read(input2,0,num);
		return DES.decrypt(input2,"87654321".getBytes());
	}
	//����bytes����
	public static boolean sendDes_bytes(byte[] output,DataOutputStream toClient) throws Exception{
		byte[] result = DES.encrypt(output,"87654321".getBytes());
		toClient.writeInt(result.length);
		toClient.flush();
		toClient.write(result,0,result.length);
		toClient.flush();
		return true;
	}
	//����bytes������
	public static byte[] Read_bytes(DataInputStream read) throws Exception{
		int num = read.readInt();
		byte[] input2 = new byte[num];
		read.read(input2,0,num);
		return input2;
	}
	//����bytes������
	public static boolean Send_bytes(byte[] output,DataOutputStream toClient) throws Exception{
		toClient.writeInt(output.length);
		toClient.flush();
		toClient.write(output,0,output.length);
		toClient.flush();
		return true;
	}
	//����Control����
	public static void sendControl(byte type,String IDc,DataOutputStream toClient) throws Exception{
		Control report = new Control(type,IDc);
		sendDes_bytes(report.datatoBytes(),toClient);
	}
}
