import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Ipv4Client {
	static int MINSIZE = 20;
	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("codebank.xyz", 38003)) {
			OutputStream out = socket.getOutputStream();
			System.out.println("Connected to server...");
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
			int data = 1;
			for (int i = 0; i < 12; i++) {
				data *= 2;
				int size = MINSIZE + data;
				byte [] packet = new byte [size];
				//Version and HLen
				packet [0] = 0x45;
				//TOS
				packet[1] = 0;
				//Length
				packet[2] = (byte) (size >>> 8);
				packet[3] = (byte) size;
				//Identification
				packet[4] = 0;
				packet[5] = 0;
				//Flags and Offset
				packet[6] = 0x40;
				packet[7] = 0;
				//Time to Live
				packet[8] = 50;
				//Protocol
				packet[9] = 6;
				//Source Address
				packet[12] = (byte) 0x6a;
				packet[13] = (byte) 0x64;
				packet[14] = (byte) 0xf5;
				packet[15] = (byte) 0x0d;
				//Destination Address
				packet[16] = (byte) 0x34;
				packet[17] = (byte) 0x25;
				packet[18] = (byte) 0x58;
				packet[19] = (byte) 0x9a;
				//Checksum
				short check = checksum(packet);
				packet[10] = (byte) (check>>>8);
				packet[11] = (byte) check;
				//Assume data is 0
				out.write(packet);
				System.out.println("Packet: " + (i + 1));
				System.out.println("Data Length: " + data);
				System.out.println(br.readLine());
				
				
			}
			
			
			
		}
		
	}
	public static short checksum(byte[] b) {
		int checkSum = 0;
		int value, length;
		if (b.length % 2 == 1)
			length = b.length / 2 + 1;
		else
			length = b.length / 2;
		for (int i = 0; i < length; i++) {
				
			try  {
				value = (((b[i * 2] << 8) & 0xFF00) | ((b[i * 2 + 1]) & 0xFF));
				checkSum += value;
			}
			catch (Exception IndexOutOfBoundsException) {
				checkSum += (b[i * 2] << 8 & 0xFF00);
				
			}
			if ((checkSum & 0xFFFF0000) > 0) {
				//checkSum += (b[i] << 8 & 0xFF00);
				checkSum = checkSum & 0xFFFF;
				checkSum++;
			}
					
		}
		return (short)(~checkSum & 0xFFFF);
		
	}
	
}

