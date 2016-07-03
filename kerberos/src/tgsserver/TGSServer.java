package tgsserver;

import java.net.ServerSocket;

public class TGSServer {
	public static int ThreadMaxNum = 20;
	public static void main(String args[]){
		TGSServerThread[] TGSServerThreadList = new TGSServerThread[ThreadMaxNum];
		for(int i=0;i<ThreadMaxNum;i++){
			TGSServerThreadList[i] = new TGSServerThread(i);
		}
		try{
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(4600);
			int begin = 0;
			int now = begin;
			int prev = 0;
			while(true){
				if(!TGSServerThreadList[now].getIsBusy()){
				TGSServerThreadList[now] = new TGSServerThread(now);
				TGSServerThreadList[now].setSocket(server.accept());
				TGSServerThreadList[now].start();
				prev = now;
			}
			if(now < ThreadMaxNum-1)now++;
			else now=0;
			if(now == prev){ 
				System.out.println("no empty thread");
				Thread.sleep(500);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
