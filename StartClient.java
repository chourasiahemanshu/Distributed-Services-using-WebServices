
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;

public class StartClient {
	static HashMap<String, HashSet<String>> hashMap = new HashMap<>();
	static boolean idTaken = false;
	static Scanner obj;
	static String id = "";
static MeraInterface interFace;
	public static void main(String[] args) {
		
		
/*		try {
			MultiThread();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/
		
		
		
		URL addURL = null;
		try {
			addURL = new URL("http://localhost:8080/addition?wsdl");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		QName addQName = new QName("http://impl.service.web.com/", "ImplementationService");
		
		Service addition = Service.create(addURL, addQName);
		interFace = addition.getPort(MeraInterface.class);

		while (true) {
			try {

				System.out.println("Enter 1 to enter id or 2 to exit");
				String opt = obj.nextLine();
				if (opt.equals("1") || opt == "1") {

					System.out.println("Enter ID");
					id = obj.nextLine().toUpperCase();
					String[] vals = split(id);
					interFace = gettype(vals[0]);

					if (vals[1] == "M" || vals[1].equals("M")) {
						idTaken = false;
						System.out.println(
								"SELECT 1 to 6\n1. Add Event\n2. Remove Event\n3. List Event Availability \n4. Book Event\n5.Cancel Event \n6.Get Booking Schedule\n7.Swap Event");
						int ans = obj.nextInt();
						options(ans);
					} else {
						idTaken = true;
						System.out.println(
								"SELECT 1 to 3\n1. Book Event\n2.Cancel Event\n3.Get Booking Schedule\n4.Swap Event");
						int ans = obj.nextInt();
						options(ans + 3);
					}
				} else if (opt.equals("2") || opt == "2") {
					System.exit(0);
				} else {
					continue;
				}
			} catch (Exception e) {
				System.out.println("Error While Entering ... Try Again");

			}
		}

	}

	public static void options(int ans) throws Exception {
		String type = "", uniqueid = "";
		int booking;
		try {
			switch (ans) {
			case 1:

				type = getType();
				uniqueid = getEventID();
				booking = getBooking();
				LogData("Manager trying to add event", id);
				String reply = interFace.addEvent(uniqueid, type, booking).trim();
				System.out.println(reply);
				LogData("GOT REPLY", id);
				break;

			case 2:
				type = getType();
				uniqueid = getEventID();
				reply = interFace.removeEvent(uniqueid, type).trim();
				if (reply.trim().equals("EVENT ID REMOVED SUCCESSFULLY")) {
					for (String i : hashMap.keySet()) {

						HashSet<String> tempHash = hashMap.get(i);
						if (tempHash.contains(type + "||" + uniqueid)) {
							tempHash.remove(type + "||" + uniqueid);
						}
					}

				}
				System.out.println(reply);
				break;

			case 3:
				type = getType();
				System.out.println(interFace.listEventAvailability(type).trim());
				break;
			case 4:
				int temp = 0;
				if (!idTaken)
					id = getCustomerID();
				type = getType();
				uniqueid = getEventID();
				LogData("ForCustomer" + id + "CUSTUMER TRYING TO BOOK EVENT", id);

				if (id.substring(0, 3).equals(uniqueid.substring(0, 3))) {
					temp = 0;
				} else {

					if (hashMap.containsKey(id)) {
						HashSet<String> hashSet = hashMap.get(id);
						ArrayList<String> abc = new ArrayList(hashSet);
						for (int i = 0; i < hashSet.size(); i++) {
							String eid = abc.get(i);
							String sub = eid.split("\\|\\|")[1].substring(0, 3);
							if (!id.substring(0, 3).equals(sub)) {
								String usub = uniqueid.substring(6, 8);
								String eidsub = eid.split("\\|\\|")[1].substring(6, 8);
								if (usub.equals(eidsub))
									temp++;
							}
						}
					} else {
						temp = 0;
					}
				}

				if (temp < 3) {
					reply = interFace.bookEvent(id, uniqueid, type).trim();
					if (reply.equals("Successfully Booked")) {
						LogData("Successfull Event BOOKED", id);
						if (hashMap.containsKey(id)) {
							hashMap.get(id).add(type + "||" + uniqueid);
						} else {
							HashSet<String> hashSet = new HashSet<>();
							hashSet.add(type + "||" + uniqueid);
							hashMap.put(id, hashSet);
						}
					}
					System.out.println(reply);
				} else {
					LogData("CANNOT BOOK EVENT AS ALREADY HAVE MORE THEN # EVENTS PER MONTH", id);
					System.out.println("CUSTOMER HAVE MORE THEN THREE BOOKINGS FOR THAT MONTH");
				}
				break;
			case 5:
				if (!idTaken)
					id = getCustomerID();
				type = getType();
				uniqueid = getEventID();
				reply = interFace.cancelEvent(id, uniqueid, type).trim();
				if (reply.equals("Event Canceled Successfully")) {
					LogData("SUCCESS", id);

					hashMap.get(id).remove(type + "||" + uniqueid);
				}
				System.out.println(reply);
				LogData("GOT REPLY" + reply, id);

				break;
			case 6:
				if (!idTaken)
					id = getCustomerID();

				reply = interFace.getBookingSchedule(id).trim();
				LogData(reply, id);
				reply.replaceAll("SEMINAR\\|\\|", " ").replaceAll("TRADE SHOW\\|\\|", " ").replaceAll("CONFRENCE\\|\\|",
						" ");
				System.out.println(reply);
				break;
			case 7:
				if (!idTaken)
					id = getCustomerID();
				type = getType();
				uniqueid = getEventID();
				String type2 = getOLDType();
				String uniqueid2 = getOLDEventID();

				if (hashMap.containsKey(id)) {
					String[] customs = new String[hashMap.get(id).size()];
					hashMap.get(id).toArray(customs);

					reply = interFace.swapEvent(id, uniqueid, type, uniqueid2, type2, customs);

					if (reply.equals("SUCCESS")) {
						LogData("SUCCESS", id);
						if (hashMap.containsKey(id)) {
							hashMap.get(id).add(type + "||" + uniqueid);
						} else {
							HashSet<String> hashSet = new HashSet<>();
							hashSet.add(type + "||" + uniqueid);
							hashMap.put(id, hashSet);
						}
						hashMap.get(id).remove(type2 + "||" + uniqueid2);
					}
				} else {
					reply = "CUSTOMER DOESNT EXIST";

				}
				System.out.println(reply);
				break;
			default:
				break;

			}
		} catch (Exception e) {
			System.out.println("Error yaaaaaaaa hooooooooooooo");
			e.printStackTrace();
			LogData("ERROR", id);
		}
	}

	public static MeraInterface gettype(String abc) {
		return interFace;

	}

	public static String getType() {
		String type = "";
		System.out.println("Select Event type :- \n1. Seminar\n2.Trade Show\n3.Confrence");
		int typ = obj.nextInt();
		if (typ == 1)
			type = "SEMINAR";
		else if (typ == 2)
			type = "Trade Show";
		else if (typ == 3)
			type = "Conference";
		else {
			System.out.println("Invalid Option...Try Again");
			getType();
		}
		return type.toUpperCase();
	}

	public static String getEventID() {
		String uniqueid = "";
		System.out.println("ENTER EVENT ID e.g MTLA100919 :- ");
		obj.nextLine();
		uniqueid = obj.nextLine();
		if (eventIdCheck(uniqueid))
			return uniqueid.toUpperCase();
		else {
			System.out.println("INVALID EVENT ID TRY AGAIN");
			getEventID();
		}
		return "";

	}

	public static String getOLDType() {
		String type = "";
		System.out.println("Select OLD Event type :- \n1. Seminar\n2.Trade Show\n3.Confrence");
		int typ = obj.nextInt();
		if (typ == 1)
			type = "SEMINAR";
		else if (typ == 2)
			type = "Trade Show";
		else if (typ == 3)
			type = "Conference";
		else {
			System.out.println("Invalid Option...Try Again");
			getType();
		}
		return type.toUpperCase();
	}

	public static String getOLDEventID() {
		String uniqueid = "";
		System.out.println("ENTER OLD EVENT ID e.g MTLA100919 :- ");
		obj.nextLine();
		uniqueid = obj.nextLine();
		if (eventIdCheck(uniqueid))
			return uniqueid.toUpperCase();
		else {
			System.out.println("INVALID EVENT ID TRY AGAIN");
			getEventID();
		}
		return "";

	}

	public static String getCustomerID() {
		String uniqueid = "";
		System.out.println("ENTER CUSTOMER ID e.g MTLC1234 :- ");
		obj.nextLine();
		uniqueid = obj.nextLine();
		if (idCheck(uniqueid))
			return uniqueid.toUpperCase();
		else {
			System.out.println("INVALID ID TRY AGAIN");
			obj.nextLine();
			getCustomerID();
		}
		return "";
	}

	public static Integer getBooking() {
		int booking = 0;
		System.out.println("Enter Capacity");
		booking = obj.nextInt();

		return booking;
	}

	public static String[] split(String id) {
		String vals[] = new String[3];

		if (idCheck(id)) {
			vals[0] = id.substring(0, 3).toUpperCase();
			vals[1] = id.substring(3, 4).toUpperCase();
			vals[2] = id.substring(4, 8);
		} else {
			System.out.println("Invalid Id");
		}
		return vals;

	}

	public static boolean idCheck(String id) {
		boolean ans = false;
		try {
			String city = id.substring(0, 3).toUpperCase().trim();
			String mc = id.substring(3, 4).trim().toUpperCase();
			String uid = id.substring(4).trim();
			if (city.equals("TOR") || city.equals("MTL") || city.equals("OTW"))
				ans = true;
			else {
				ans = false;
				return ans;
			}
			if (mc.equals("M") || mc.equals("C"))
				ans = true;
			else {
				return false;
			}
			if (uid.length() > 4 || uid.length() < 4)
				return false;
			else
				ans = true;
			int a = Integer.parseInt(uid);

		} catch (Exception e) {
			ans = false;
		}
		return ans;
	}

	public static boolean eventIdCheck(String id) {
		boolean ans = false;
		try {
			String city = id.substring(0, 3).toUpperCase().trim();
			String mc = id.substring(3, 4).trim().toUpperCase();
			String uid = id.substring(4).trim();
			if (city.equals("TOR") || city.equals("MTL") || city.equals("OTW"))
				ans = true;
			else {
				ans = false;
				return ans;
			}
			if (mc.equals("M") || mc.equals("E") || mc.equals("A"))
				ans = true;
			else {
				return false;
			}
			if (uid.length() > 6 || uid.length() < 6)
				return false;
			else
				ans = true;
			int a = Integer.parseInt(uid);

		} catch (Exception e) {
			ans = false;
		}
		return ans;
	}

	public static void LogData(String value, String name) {
		Date date = new Date(); // this object contains the current date value
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		File log = new File(name + ".txt");
		try {
			if (!log.exists()) {
				log.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(log, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(date + " : " + value + "\n");
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}

/*	public static void MultiThread() throws Exception {
		try {

			Eventmanagement TorInterface = (Eventmanagement) EventmanagementHelper.narrow(ncRef.resolve_str("TOR"));
			Eventmanagement MtlInterface = (Eventmanagement) EventmanagementHelper.narrow(ncRef.resolve_str("MTL"));
			Eventmanagement OtwInterface = (Eventmanagement) EventmanagementHelper.narrow(ncRef.resolve_str("OTW"));
			
			TorInterface.addEvent("TORE090909", "SEMINAR", 12);
			TorInterface.addEvent("TORE010101", "Trade Show", 12);
			TorInterface.addEvent("TORE020202", "Conference", 2);
//			TorInterface.addEvent("TORE030303", "SEMINAR", 12);
//			TorInterface.addEvent("TORE040404", "SEMINAR", 12);
			
			TorInterface.bookEvent("TORC1000", "TORE090909", "SEMINAR");
			TorInterface.bookEvent("TORC1001", "TORE010101", "Trade Show");
		//	TorInterface.bookEvent("TORC1002", "TORE020202", "Conference");
		//	TorInterface.bookEvent("TORC1003", "TORE030303", "SEMINAR");
		//	TorInterface.bookEvent("TORC1004", "TORE040404", "SEMINAR");
			if (hashMap.containsKey("TORC1000")) {
				hashMap.get("TORC1000").add("SEMINAR||TORE090909");
			} else {
				HashSet<String> hashSet = new HashSet<>();
				hashSet.add("SEMINAR||TORE090909");
				hashMap.put("TORC1000", hashSet);
			}
			if (hashMap.containsKey("TORC1001")) {
				hashMap.get("TORC1001").add("Trade Show||TORE010101");
			} else {
				HashSet<String> hashSet = new HashSet<>();
				hashSet.add("Trade Show||TORE010101");
				hashMap.put("TORC1001", hashSet);
			}

			
			
			
			
//			MtlInterface.addEvent("MTLE090909", "SEMINAR", 12);
//			MtlInterface.addEvent("MTLE010101", "Trade Show", 12);
	//		MtlInterface.addEvent("MTLE020202", "Conference", 12);
	//		MtlInterface.addEvent("MTLE030303", "SEMINAR", 12);
	//		MtlInterface.addEvent("MTLE040404", "SEMINAR", 12);
			
			MtlInterface.bookEvent("MTLC1000", "TORE090909", "SEMINAR");
			MtlInterface.bookEvent("MTLC1001", "TORE010101", "Trade Show");
		//	MtlInterface.bookEvent("MTLC1002", "MTLE020202", "Conference");
		//	MtlInterface.bookEvent("MTLC1003", "MTLE030303", "SEMINAR");
		//	MtlInterface.bookEvent("MTLC1004", "MTLE040404", "SEMINAR");
			if (hashMap.containsKey("MTLC1000")) {
				hashMap.get("MTLC1000").add("SEMINAR||TORE090909");
			} else {
				HashSet<String> hashSet = new HashSet<>();
				hashSet.add("SEMINAR||TORE090909");
				hashMap.put("MTLC1000", hashSet);
			}
			if (hashMap.containsKey("MTLC1001")) {
				hashMap.get("TORC1001").add("Trade Show||TORE010101");
			} else {
				HashSet<String> hashSet = new HashSet<>();
				hashSet.add("Trade Show||TORE010101");
				hashMap.put("MTLC1001", hashSet);
			}
//			OtwInterface.addEvent("OTWE090909", "SEMINAR", 12);
		//	OtwInterface.addEvent("OTWE010101", "Trade Show", 12);
		//	OtwInterface.addEvent("OTWE020202", "Conference", 12);
		//	OtwInterface.addEvent("OTWE030303", "SEMINAR", 12);
		//	OtwInterface.addEvent("OTWE040404", "SEMINAR", 12);
			
			OtwInterface.bookEvent("OTWC1000", "TORE090909", "SEMINAR");
		//	OtwInterface.bookEvent("OTWC1001", "OTWE010101", "Trade Show");
		//	OtwInterface.bookEvent("OTWC1002", "OTWE020202", "Conference");
		//	OtwInterface.bookEvent("OTWC1003", "OTWE030303", "SEMINAR");
		//	OtwInterface.bookEvent("OTWC1004", "OTWE040404", "SEMINAR");
			if (hashMap.containsKey("OTWC1000")) {
				hashMap.get("OTWC1000").add("SEMINAR||TORE090909");
			} else {
				HashSet<String> hashSet = new HashSet<>();
				hashSet.add("SEMINAR||TORE090909");
				hashMap.put("OTWC1000", hashSet);
			}
			
			
			
			String[] custom1 = new String[hashMap.get("TORC1000").size()];
			hashMap.get("OTWC1000").toArray(custom1);
			String[] custom2 = new String[hashMap.get("TORC1001").size()];
			hashMap.get("OTWC1000").toArray(custom2);
			String[] custom3 = new String[hashMap.get("MTLC1000").size()];
			hashMap.get("OTWC1000").toArray(custom3);
			String[] custom4 = new String[hashMap.get("MTLC1001").size()];
			hashMap.get("OTWC1000").toArray(custom4);
			String[] custom5 = new String[hashMap.get("OTWC1000").size()];
			hashMap.get("OTWC1000").toArray(custom5);

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						LogData("Thread 1", "threadLog");
						System.out.println("Thread 1 "+TorInterface.swapEvent("TORC1000","TORE020202", "Conference","TORE090909", "SEMINAR", custom1));
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Thread thread2 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						LogData("Thread 2", "threadLog");
						System.out.println("Thread 2 "+TorInterface.swapEvent("TORC1001","TORE020202", "Conference","TORE010101", "Trade Show", custom2));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Thread thread3 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						LogData("Thread 3", "threadLog");
						System.out.println("Thread 3 "+TorInterface.swapEvent("MTLC1000","TORE020202", "Conference","TORE090909", "SEMINAR", custom3));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Thread thread4 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						LogData("Thread 4", "threadLog");
						System.out.println("Thread 4 "+TorInterface.swapEvent("MTLC1001","TORE020202", "Conference","TORE010101", "Trade Show", custom4));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Thread thread5 = new Thread(new Runnable() {
				@Override
				public void run() {
					try {

						LogData("Thread 5", "threadLog");
						System.out.println("Thread 5 "+TorInterface.swapEvent("OTWC1000","TORE020202", "Conference","TORE090909", "SEMINAR", custom5));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			thread.start();
			thread2.start();
			thread3.start();
			thread4.start();
			thread5.start();

		} catch (Exception e) {
		}
	}
*/
}