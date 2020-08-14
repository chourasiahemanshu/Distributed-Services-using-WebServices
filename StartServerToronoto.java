

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.xml.ws.Endpoint;

public class StartServerToronoto {
    static ImplimentationToronto exportedObj;

 
  public static void main(String args[]) {
    try{
    	
    	exportedObj = new ImplimentationToronto();
		Endpoint endpoint = Endpoint.publish("http://localhost:8080/addition", exportedObj);
	
    	
    	
    	
    	
      startUDPServer(8086);

    
    } 
 
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
 
      System.out.println("HelloServer Exiting ...");
 
  }
  
  private static void startUDPServer(int portNumber) {
      DatagramSocket aSocket = null;
      String val = "";
      try {
          aSocket = new DatagramSocket(portNumber);
          byte[] buffer = new byte[100000];// to stored the received data from
          // the client.
          System.out.println("Toronto UDP Server Started on 8086............");
          while (true) {
              buffer=new byte[100000];
              DatagramPacket request = new DatagramPacket(buffer, buffer.length);

              aSocket.receive(request);

              System.out.println("Request received from client: " + new String(request.getData()));
              String valuePassed = new String(request.getData());
              String[] parameterToBePassed = valuePassed.split(":");
              if (parameterToBePassed[0].equals("bookEvent")) {
                  val = exportedObj.bookEvent(parameterToBePassed[1].trim(), parameterToBePassed[2].trim(), parameterToBePassed[3].trim());
              } else if (parameterToBePassed[0].equals("listEventAvailability")) {
                  val = exportedObj.listEventAvailabilityServerCall(parameterToBePassed[1].trim());
              } else if (parameterToBePassed[0].equals("getBookingSchedule")) {
                  val = exportedObj.getBookingScheduleServerCall(parameterToBePassed[1].trim());
              } else if (parameterToBePassed[0].equals("cancelEvent")) {
                  val = exportedObj.cancelEvent(parameterToBePassed[1].trim(), parameterToBePassed[2].trim(), parameterToBePassed[3].trim());
              }

              DatagramPacket reply = new DatagramPacket(val.getBytes(), val.length(), request.getAddress(),
                      request.getPort());// reply packet ready

              aSocket.send(reply);// reply sent
          }
      } catch (SocketException e) {
          System.out.println("Socket: " + e.getMessage());
      } catch (IOException e) {
          System.out.println("IO: " + e.getMessage());
      } finally {
          if (aSocket != null)
              aSocket.close();
      }
  }

}