package wikipedia.event.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;

//import javax.swing.text.Document;

import java.time.MonthDay;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wikipedia.event.model.Event;

/**
 * Class for querying events that occurred on a specific day of month. Events
 * are extracted from the English Wikipedia page about a specific day of month.
 */
public class EventExtractor {

	private static Logger	logger = LoggerFactory.getLogger(EventExtractor.class);
	private static DateTimeFormatter format = DateTimeFormatter.ofPattern("[G' ']y[' 'G]", Locale.ENGLISH);
	/**
	 * Returns the web page address of the Wikipedia page about the day of
	 * month specified.
	 *
	 * @param monthDay an object that wraps the month of year and the day of
	 *                 month
	 * @return the web page address of the Wikipedia page about the day of
	 *         month specified, as a string
	 */
	private String getWikipediaURL(MonthDay monthDay) {
		return String.format("https://en.wikipedia.org/wiki/%s_%d", monthDay.getMonth().toString().toLowerCase(), monthDay.getDayOfMonth()); 
	}

	/**
	 * Returns events that occurred on the day of month specified.
	 *
	 * @param monthDay an object that wraps the month of year and the day of
	 *                 month
	 * @return the list of objects that represent events that occurred on the
	 *         day of month specified
	 * @throws IOException if any I/O error occurs during the execution of the query
	 */
	public List<Event> getEvents(MonthDay monthDay) throws IOException {
		List<Event> events = new ArrayList<Event>();
		
		String url = getWikipediaURL(monthDay);
		logger.info("Esemenyek letoltese a {url} cimrol");
		Document document = Jsoup.connect(url).timeout(10000).get();
		
		Elements elements = document.select("h2:has(#Events)+ul>li");
		
		for (Element element : elements) {
			String[] elementParts = element.text().split("–"); //\u2013
			if (elementParts.length !=2) {
				logger.warn("Nem megfeleo esemeny!");
				continue;
			}
			
			elementParts[0] = elementParts[0].trim();
			elementParts[1] = elementParts[1].trim();
			int year = Integer.parseInt(elementParts[0]);
			//int year = Year.parse(elementParts[0])
			LocalDate date = LocalDate.of(year, monthDay.getMonth(), monthDay.getDayOfMonth());
			Event event = new Event(date, elementParts[1]);
			
			events.add(event);
		}
		
		return events;
	}

	/**
	 * Returns events that occurred on the day of month specified.
	 *
	 * @param monthOfYear the month of the year (1&ndash;12)
	 * @param dayOfMonth the day of the month (1&ndash;31)
	 * @return the list of objects that represent events that occurred on the
	 *         day of month specified
	 * @throws IOException if any I/O error occurs during the execution of the query
	 */
	public List<Event> query(int monthOfYear, int dayOfMonth) throws IOException {
		return getEvents(MonthDay.of(monthOfYear, dayOfMonth));
	}

}
