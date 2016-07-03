package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;

public class GroupReceiveThread extends Thread{
	BufferedReader reader;
	public GroupReceiveThread(BufferedReader _reader) throws UnknownHostException, IOException{
		reader = _reader;
	}
	public void run(){
		String input;
		try {
			input = reader.readLine();
			while(true){
				if(input != null)
				System.out.println(input);
				input = reader.readLine();
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

}
