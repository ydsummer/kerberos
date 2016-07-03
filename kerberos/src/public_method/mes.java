package public_method;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class mes {
	public static void main(String args[]) throws NoSuchAlgorithmException{
		MessageDigest messageDigest1 = MessageDigest.getInstance("MD5"); 
		MessageDigest messageDigest2 = MessageDigest.getInstance("MD5");
		byte[] output1 = messageDigest1.digest(intToByteArray(123,4));
		byte[] output2 = messageDigest2.digest(intToByteArray(234,4));
		messageDigest1.update(intToByteArray(123+234,4));
		byte[] output3 = messageDigest1.digest();

		//byte[] output3 = messageDigest1.digest(intToByteArray(234,4));
		System.out.println(messageDigest1.toString());
		System.out.println(messageDigest2.toString());
		System.out.println(new String(output1));
		System.out.println(new String(output2));
		System.out.println(new String(output3));
		System.out.println(output3.length);
		byte[] output4 = new byte[4];
		output4[0] = output1[0];
		output4[1] = output1[1];
		output4[2] = output1[2];
		output4[3] = output1[3];
		
		System.out.println(byteArrayToint(output4,0));
		
	}


	//int×ª»»ÎªBYTE[]
	public static byte[] intToByteArray(int iSource, int iArrayLen) {   
	    byte[] bLocalArr = new byte[iArrayLen];
	    for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
	        bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
	    }
	    return bLocalArr;
	}
	public static int byteArrayToint(byte[] bRefArr,int offset ) {
	    int iOutcome = 0;
	    byte bLoop;

	    for (int i = 0; i < bRefArr.length; i++) {
	        bLoop = bRefArr[i];
	        iOutcome += (bLoop & 0xFF) << (8 * i);
	    }
	    return iOutcome;
	}
}
