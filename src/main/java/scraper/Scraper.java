package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scraper.html.HtmlDocument;
import scraper.html.HtmlElement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Vetle Jahr
 * @version 1.0
 */
public class Scraper {
    private enum SourceType {URL, STRING}
    private Document _jsoupInternalDocument;
    private HtmlDocument content;

    private Scraper() {
    }

    /**
     * Private constructor to instantiate a new Scraper object with a URL or a String. Method is private as Scraper
     * objects are intended to be instantiated with a factory pattern using
     * {@link #buildScraperWithURL(String) buildScraperWithUrl} and
     * {@link #buildScraperWithString(String) buildScraperWithString} methods.
     *
     * @param source Source for the Scraper to use, either a String to Scraper or a URL for a website to scrape
     * @param sourceType Enum type of source for the scraper to differantiate between String and URL
     */
    private Scraper(String source, SourceType sourceType) {
        if (sourceType == SourceType.STRING) {
            this._jsoupInternalDocument = StringToHtmlPage(source);
            setContent();
        } else if (sourceType == SourceType.URL) {
            try (InputStream in = new URL(source).openStream()) {
                this._jsoupInternalDocument = StringToHtmlPage(new String(in.readAllBytes(), StandardCharsets.UTF_8));
                setContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Private constructor to instantiate a new Scraper object with a File. Method is private as Scraper objects are
     * intended to be instatiated with a factory pattern using
     * {@link #buildScraperWithHtmlFile(File) BuildScraperWithFile} method.
     *
     * @param file file for the Scraper to use as Source for the content
     */
    private Scraper(File file) {
        this._jsoupInternalDocument = FileToHtmlPage(file);
        setContent();
        // TODO: Create exception if file is not found or if file is not HTML
    }

    /**
     * Sets the content of the Scraper object.
     */
    private void setContent() {
        this.content =  new HtmlDocument(this._jsoupInternalDocument);
    }

    /**
     * Reads a file and parses the content in a Document object.
     *
     * @param file File to read into Document object.
     * @return Document object with parsed content or null object if error occurs.
     */
    private Document FileToHtmlPage(File file) {
        try {
            return Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A method for creating a Scraper object with a URL using a builder pattern.
     *
     * @param url URL for the website to use the Scraper object on
     * @return Scraper Object built with the sepcified url
     */
    public static Scraper buildScraperWithURL(String url) {
        return new Scraper(url, SourceType.URL);
    }

    /**
     * Method for creating a Scraper object with an HTML string using a builder pattern
     *
     * @param htmlString String in HTML format to use the Scraper object on
     * @return Scraper Object built with a specified String.
     */
    public static Scraper buildScraperWithString(String htmlString) {
        return new Scraper(htmlString, SourceType.STRING);
    }

    /**
     * Creates a Scraper Object with an HTML file using a builder pattern
     *
     * @param file html-file to use the Scraper object on
     * @return Scraper object built with a specified file.
     */
    public static Scraper buildScraperWithHtmlFile(File file) {
        // TODO: Add check that file is .html
        return new Scraper(file);
    }

    /**
     * Returns the conent of the Scraper in raw format.
     *
     * @return Raw output of HTML string, file or url
     */
    public String getRawContent() {
        return this.content.toString();
    }

    public HtmlDocument getContentDocumentObject(){
        return this.content;
    }

    /** Reads a String and parses the content ot Document object.
     *
     * @param source Soruce String to parse
     * @return Document object iwht parsed content
     */
    private Document StringToHtmlPage(String source) {
        return Jsoup.parse(source);
    }

    public ArrayList<HtmlElement> getElementsFromTag(String htmlTag) {
        Elements elements = _jsoupInternalDocument.getElementsByTag(htmlTag);
        ArrayList<HtmlElement> htmlElements = new ArrayList<>();
        for (Element el : elements){
            htmlElements.add(HtmlElement.ElementToHtmlElement(el));
        }
        return htmlElements;
    }

    public ArrayList<HtmlElement> getElementsFromClass(String className) {
        Elements elements = _jsoupInternalDocument.getElementsByClass(className);
        ArrayList<HtmlElement> htmlElements = new ArrayList<>();
        for (Element el : elements){
            htmlElements.add(HtmlElement.ElementToHtmlElement(el));
        }
        return htmlElements;
    }

    public ArrayList<HtmlElement> getElementsByXpath(String xPath) {
        return null;
    }

    public HtmlElement getElementByXpath(String xPath) {
        return null;
    }

    public HtmlElement getElementById(String id) {
        return null;
    }

    public String getTitle() {
        return null;
    }
}