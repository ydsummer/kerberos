package public_method;

import java.sql.Timestamp;
import java.util.Random;

public class ourMath {
		//获取随机byte
		public static Byte getRandombyte() {
			return 	intToByteArray(new Random().nextInt(9),4)[0];
		}
		//byte[]转换为int
		public static int byteArrayToint(byte[] bRefArr) {
		    int iOutcome = 0;
		    byte bLoop;

		    for (int i = 0; i < bRefArr.length; i++) {
		        bLoop = bRefArr[i];
		        iOutcome += (bLoop & 0xFF) << (8 * i);
		    }
		    return iOutcome;
		}
		
		//int转换为BYTE[]
		public static byte[] intToByteArray(int iSource, int iArrayLen) {   
		    byte[] bLocalArr = new byte[iArrayLen];
		    for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
		        bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		    }
		    return bLocalArr;
			}
		
		//BYTE[]转换为Timestamp
		public static Timestamp byteToTimestamp(byte[] in) {
			String ts = new String(in);
//			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
			Timestamp out = Timestamp.valueOf(ts);
			return out;
		}
		
		//Timestamp转换为byte[]
		public static byte[] TimestampTobyte(Timestamp in) {
			String ts =in.toString();
			byte[] out = ts.getBytes();
			return out;
		}
		//byte[]-long
		public static long bytes2long(byte[] b) {  
		    long temp = 0;  
		    long res = 0;  
		    for (int i=0;i<8;i++) {  
		        res <<= 8;  
		        temp = b[i] & 0xff;  
		        res |= temp;  
		    }  
		    return res;  
		}  
		//long-byte[]
		public static byte[] long2bytes(long num) {  
		    byte[] b = new byte[8];  
		    for (int i=0;i<8;i++) {  
		        b[i] = (byte)(num>>>(56-(i*8)));  
		    }  
		    return b;  
		}  

		
}
