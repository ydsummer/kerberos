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
	 * 加密
	 * @param args
	 */	
	public static byte[] encrypt(byte[] datasource,byte[] password) { 
		//String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
		try{
		SecureRandom random = new SecureRandom();
		DESKeySpec desKey = new DESKeySpec(password);
		//创建一个密匙工厂，然后用它把DESKeySpec转换成
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		//Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		//用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		//现在，获取数据并加密
		//正式执行加密操作
		return cipher.doFinal(datasource);
		}catch(Throwable e){
		e.printStackTrace();
		}
		return null;
		}
	
	/**
	* 解密
	* @param src byte[]
	* @param password String
	* @return byte[]
	* @throws Exception
	*/
	public static byte[] decrypt(byte[] src,byte[] password) throws Exception {
		//String password = "9588028820109132570743325311898426347857298773549468758875018579537757772163084478873699447306034466200616411960574122434059469100235892702736860872901247123456";
	// DES算法要求有一个可信任的随机数源
	SecureRandom random = new SecureRandom();
	
	// 创建一个DESKeySpec对象
	DESKeySpec desKey = new DESKeySpec(password);
	// 创建一个密匙工厂
	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	// 将DESKeySpec对象转换成SecretKey对象
	SecretKey securekey = keyFactory.generateSecret(desKey);
	// Cipher对象实际完成解密操作
	Cipher cipher = Cipher.getInstance("DES");
	// 用密匙初始化Cipher对象
	cipher.init(Cipher.DECRYPT_MODE, securekey, random);
	// 真正开始解密操作
	return cipher.doFinal(src);
	}
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		while(true){
			System.out.println("请输入需要加密的数据:");
			@SuppressWarnings("resource")
			Scanner scnner= new Scanner(System.in);
			String str=scnner.next();
			byte[] result = DES.encrypt(str.getBytes("ASCII"),"11223344".getBytes());
		  
			String result2 = new String(result);
			byte[] result3 = result2.getBytes("ASCII");
			System.out.println("加密后："+result);
			System.out.println("加密后："+new String(result));
			
			
			
			
			//直接将如上内容解密
			try {
			byte[] decryResult = DES.decrypt(result3,"11223344".getBytes());
			//System.out.println("解密后："+decryResult[1]);
			System.out.println("解密后："+new String(decryResult));
			} catch (Exception e1) {
			e1.printStackTrace();
			}
		}
	}

}
