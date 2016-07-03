package public_method;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {

	/**
	 * @param args
	 */
	public DES() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ����
	 * @param args
	 */	
	public static byte[] encrypt(byte[] datasource,byte[] password) { 
		//String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
		try{
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password);
		//����һ���ܳ׹�����Ȼ��������DESKeySpecת����
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		//Cipher����ʵ����ɼ��ܲ���
		Cipher cipher = Cipher.getInstance("DES");
		//���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		//���ڣ���ȡ���ݲ�����
		//��ʽִ�м��ܲ���
		return cipher.doFinal(datasource);
		}catch(Throwable e){
		e.printStackTrace();
		}
		return null;
		}
	
	/**
	* ����
	* @param src byte[]
	* @param password String
	* @return byte[]
	* @throws Exception
	*/
	public static byte[] decrypt(byte[] src,byte[] password) throws Exception {
		//String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
	// DES�㷨Ҫ����һ�������ε������Դ
	SecureRandom random = new SecureRandom();
	
	// ����һ��DESKeySpec����
	DESKeySpec desKey = new DESKeySpec(password);
	// ����һ���ܳ׹���
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	// ��DESKeySpec����ת����SecretKey����
	SecretKey securekey = keyFactory.generateSecret(desKey);
	// Cipher����ʵ����ɽ��ܲ���
	Cipher cipher = Cipher.getInstance("DES");
	// ���ܳ׳�ʼ��Cipher����
	cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	// ������ʼ���ܲ���
	return cipher.doFinal(src);
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		while(true){
			System.out.println("��������Ҫ���ܵ�����:");
			@SuppressWarnings("resource")
			Scanner scnner= new Scanner(System.in);
			String str=scnner.next();
			byte[] result = DES.encrypt(str.getBytes("ASCII"),"11223344".getBytes());
		  
			String result2 = new String(result);
			byte[] result3 = result2.getBytes("ASCII");
			System.out.println("���ܺ�"+result);
			System.out.println("���ܺ�"+new String(result));
			
			
			
			
			//ֱ�ӽ��������ݽ���
			try {
			byte[] decryResult = DES.decrypt(result3,"11223344".getBytes());
			//System.out.println("���ܺ�"+decryResult[1]);
			System.out.println("���ܺ�"+new String(decryResult));
			} catch (Exception e1) {
			e1.printStackTrace();
			}
		}
	}

}
