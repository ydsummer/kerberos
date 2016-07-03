package asserver;

import java.net.ServerSocket;

public class ASServer {
	public static int ThreadMaxNum = 3;
	public static void main(String args[]){
		ServerThread[] ServerThreadList = new ServerThread[ThreadMaxNum];
		for(int i=0;i<ThreadMaxNum;i++){
			ServerThreadList[i] = new ServerThread(i);
		}
		try{
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(4700);
			int begin = 0;
			int now = begin;
			int prev = 0;
			while(true){
				if(!ServerThreadList[now].getIsBusy()){
					ServerThreadList[now] = new ServerThread(now);
					ServerThreadList[now].setSocket(server.accept());
					ServerThreadList[now].start();
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