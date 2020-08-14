
import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface MeraInterface {
	@WebMethod
	String bookEvent(String customerID, String eventID, String eventType);

	@WebMethod
	String removeEvent(String eventID, String eventType);

	@WebMethod
	String listEventAvailability(String eventType);

	@WebMethod
	String addEvent(String eventID, String eventType, int bookingCapacity);

	@WebMethod
	String getBookingSchedule(String customerID);

	@WebMethod
	String cancelEvent(String customerID, String eventID, String eventType);

	@WebMethod
	String swapEvent(String customerID, String newEventID, String newEventType, String oldEventID, String oldEventType,
			String[] customers);
} // interface EventmanagementOperations
