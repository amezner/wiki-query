package wikipedia.event.client;

import java.io.IOException;
import java.time.MonthDay;
import java.util.List;

import wikipedia.event.model.Event;
import wikipedia.event.parser.EventExtractor;

/**
 * Command line client for querying events that occurred on a specific day of month.
 */
public class CmdLineClient {

	public static void main(String[] args) throws IOException {
		List<Event> events;
		
		if (args.length == 0) {
			events = new EventExtractor().getEvents(MonthDay.now());
			events.forEach(System.out::println);
		} else if (args.length == 2) {
			int month = Integer.parseInt(args[0]);
			int day = Integer.parseInt(args[1]);
			events = new EventExtractor().query(month, day);			
			events.forEach(System.out::println);
		} else {
			System.err.println("Nem megfelelo parameterszam.");
			System.exit(1);
		}
		
	}

}
