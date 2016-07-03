package vserver;

import java.net.ServerSocket;

public class VServer {
	public static int ThreadMaxNum = 20;
	static VtherThread[] VtherThreadList = new VtherThread[ThreadMaxNum];
	public static void main(String args[]){
		//VtherThread[] VtherThreadList = new VtherThread[ThreadMaxNum];
		for(int i=0;i<ThreadMaxNum;i++){
			VtherThreadList[i] = new VtherThread(i);
		}
		try{
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(4500);
			int begin = 0;
			int now = begin;
			int prev = 0;
			while(true){
				if(!VtherThreadList[now].getIsBusy()){
				VtherThreadList[now] = new VtherThread(now);
				VtherThreadList[now].setSocket(server.accept());
				VtherThreadList[now].start();
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
