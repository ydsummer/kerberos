package public_class;

import java.util.Arrays;

public class Control {
	private byte type;//1Bytes的类型代码
	private String IDc;//ClientID
	//构造函数
	public Control(byte _type,String _IDc){
		type = _type;
		IDc = _IDc;
	}
	//构造函数
	public Control(String input){
		byte[] data = input.getBytes();
		type = data[0];
		IDc = new String(Arrays.copyOfRange(data, 1, data.length));
	}
	//构造函数
	public Control(byte[] input){
		type = input[0];
		IDc = new String(Arrays.copyOfRange(input, 1, input.length));
	}
	//转换为byte的函数
	public byte[] datatoBytes(){
		byte[] data = new byte[1 + IDc.length()];
		byte[] idc = IDc.getBytes();
		data[0] = type;
		int i=1;
		for(byte a:idc){data[i++]=a;}
		return data;
	}
	//返回type
	public byte getType(){
		return type;
	}
	//返回IDc
	public String getIDc(){
		return IDc;
	}
	//报告用输出
	public void show(){
		switch(type){
		case 7:System.out.println("7.	我不需要这个包");break;
		case 8:System.out.println("8.	用户名不存在");break;
		case 9:System.out.println("9.	密码错误");break;
		case 10:System.out.println("10.	所请求TGSServer不存在");break;
		case 11:System.out.println("11.	所请求VServer不存在");break;
		case 12:System.out.println("12.	所请求权限不足");break;
		case 13:System.out.println("13.	数据包超时请重传");break;
		case 14:System.out.println("14.	Ticket过期");break;
		case 15:System.out.println("15. 地址变更");break;
		case 16:System.out.println("");break;
		case 17:System.out.println("");break;
		case 66:System.out.println("66. ASServer接收成功，准备回复报文");break;
		case 67:System.out.println("67. Client接收成功，不回复其他报文");break;
		case 68:System.out.println("68. TGSServer接收成功，准备回复报文");break;
		default:System.out.println("未知控制报文 + " + type + " + 请联系管理员。");
		
		}
	}
	
}
