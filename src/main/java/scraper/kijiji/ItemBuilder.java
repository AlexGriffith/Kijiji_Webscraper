package scraper.kijiji;

import java.util.Objects;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A class to build KijijiItems from the attributes taken from the kijiji page. 
 * element holds the kijiji page. 
 * Uses the builder design pattern. 
 * @author griff
 */
public final class ItemBuilder {
    
    private static final String URL_BASE = "https://www.kijiji.ca";

    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private Element element;
    private KijijiItem item = new KijijiItem();

    ItemBuilder() {
    }
    
    public ItemBuilder setElement(Element element){
        this.element = element;
        return this;
    }
    
    private String getImageUrl() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).attr("src").trim();
            if (image.isEmpty()) {
                image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
            }
        }
        return image;
    }

    private String getImageName() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim();

        }
        return image;
    }

    private String getUrl() {
        return URL_BASE+element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim();
    }

    private String getId() {
        return element.attr(ATTRIBUTE_ID).trim();
    }

    private String getPrice() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if(elements.isEmpty())
            return "$1.00";
        return elements.get(0).text().trim();
    }

    private String getTitle() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).child(0).text().trim();
    }

    private String getDate() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }

    private String getLocation() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).childNode(0).outerHtml().trim();
    }

    private String getDescription() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if(elements.isEmpty())
            return "";
        return elements.get(0).text().trim();
    }
    
    public KijijiItem build() {
        
        item.setId(getId());
        item.setUrl(getUrl());
        item.setImageUrl(getImageUrl());
        item.setImageName(getImageName());
        item.setPrice(getPrice());
        item.setTitle(getTitle());
        item.setDate(getDate());
        item.setLocation(getLocation());
        item.setDescription(getDescription());
        
        
        return item;
    }

}
