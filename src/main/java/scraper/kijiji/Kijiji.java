package scraper.kijiji;

import java.io.IOException;
import java.util.function.Consumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *Holds all the methods that access the kijiji.ca webpage, download the items, and strip the attributes, and process them. 
 * 
 * @author Shariar (Shawn) Emami
 */
public class Kijiji {

    private static final String ATTRIBUTE_ID = "data-listing-id";

    private static final String URL_BASE = "https://www.kijiji.ca/b-computer-accessories/";
    private static final String URL_NEWEST_COMPUTER_ACCESSORIES = "https://www.kijiji.ca/b-computer-accessories/ottawa-gatineau-area/c128l1700184?sort=dateDesc";
    private static final String URL_NEWEST_LAPTOP_ACCESSORIES = "https://www.kijiji.ca/b-laptop-accessories/ottawa-gatineau-area/c780l1700184?sort=dateDesc";

    private Document doc;
    private Elements itemElements;

    public Kijiji downloadPage(String kijijiUrl) throws IOException {
        doc = Jsoup.connect(kijijiUrl).get();
        return this;
    }

    public Kijiji downloadDefaultPage() throws IOException {
        return downloadPage(URL_NEWEST_COMPUTER_ACCESSORIES);
    }

    public Kijiji findAllItems() {
        itemElements = doc.getElementsByAttribute(ATTRIBUTE_ID);
        return this;
    }

    @Deprecated
    public Kijiji proccessItemsNoneBuilder(Consumer<SimpleItem> callback) {
//        itemElements.forEach((Element element) -> {
//            callback.accept( new ItemBuilder().setElement(element).build());
//        });
//        same as above
        for (Element element : itemElements) {
            callback.accept( new SimpleItem(element));
        }
        return this;
    }
    
    /**
     * processes each KijijiItem through the Consumer to convert them into Items and add them to the database. 
     * @param callback
     * @return 
     */
    public Kijiji proccessItems(Consumer<KijijiItem> callback) {
        itemElements.forEach((Element element) -> {
            callback.accept( new ItemBuilder().setElement(element).build());
        });

        return this;
    }
    
    public static void main(String[] args) throws IOException {
        
        Consumer<KijijiItem> saveItemsNoneBuilder = (KijijiItem item) -> {
            System.out.println(item);
        };

        Kijiji kijiji = new Kijiji();
//        kijiji.downloadDefaultPage()
//                .findAllItems()
//                .proccessItems(saveItems);
        kijiji.downloadDefaultPage();
        kijiji.findAllItems();
        kijiji.proccessItems(saveItemsNoneBuilder);
    }
}
