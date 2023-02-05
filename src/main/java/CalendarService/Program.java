package CalendarService;

public class Program {

	public static void main(String[] args) {
		System.out.println(getHelloWorld("Hello World"));
		System.out.println(getHello("Hello World"));
		System.out.println(getHelloTriple("Hello World"));
	}
	
	private static String getHelloWorld(String message) {
		if (message.equals("Hello World")) {
			return message + " " + message; 
		}
		return "Not Hello" + message;
	}
	
	private static String getHello(String message) {
		if (message.equals("Hello World")) {
			return message + " " + message; 
		}
		return "Not Hello" + message;
	}
	
	private static String getHelloTriple(String message) {
		if (message.equals("Hello World")) {
			return message + " " + message; 
		}
		return "Not Hello" + message;
	}


}
