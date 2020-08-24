package scraper.kijiji;

import java.util.Objects;

/**
 * A simple class to hold strings of all items taken from the kijiji page,
 * before they get converted into Items and Images.
 * 
 * getters and setters are the used portion of this class. 
 * 
 * @author griff
 */
public final class KijijiItem {
        
    private String id;
    private String url;
    private String imageUrl;
    private String imageName;
    private String price;
    private String title;
    private String date;
    private String location;
    private String description;

    KijijiItem() {
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setId(String id) {
        this.id = id;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    void setImageName(String imageName) {
        this.imageName = imageName;
    }

    void setPrice(String price) {
        this.price = price;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setDate(String date) {
        this.date = date;
    }

    void setLocation(String location) {
        this.location = location;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KijijiItem other = (KijijiItem) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return String.format("[id:%s, url:%s image_url:%s, image_name:%.10s, price:%s, title:%.10s, date:%s, location:%s, description:%.10s]",
                id, url, imageUrl, imageName, price, title, date, location, description);
    }
}
