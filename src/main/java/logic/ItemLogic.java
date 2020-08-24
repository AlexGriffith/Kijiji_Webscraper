package logic;

import dal.ItemDAL;
import entity.Item;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds all the methods that access the ItemDAL to get items from the database.
 * CreateEntity is what is used to instantiate a new item. It handles error checking. 
 * A category and an image must be added to the Item before it can be added to the database, or an exception will be thrown. 
 * 
 * @author griff
 */
public class ItemLogic extends GenericLogic<Item,ItemDAL>{
    
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_ID = "categoryId";
    public static final String IMAGE_ID = "imageId";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String URL = "url";
    public static final String ID = "id";

    
    public ItemLogic() {
        super(new ItemDAL());
    }
    
    @Override
    public List<Item> getAll(){
        return get(()->dal().findAll());
    }
    
    @Override
    public Item getWithId( int id){
        return get(()->dal().findById(id));
    }
    
    public List<Item> getWithPrice( BigDecimal price){
        return get(()->dal().findByPrice(price));
    }
    
    public List<Item> getWithTitle( String title){
        return get(()->dal().findByTitle(title));
    }
    
    public List<Item> getWithDate( String date){
        return get(()->dal().findByDate(date));
    }
    
    public List<Item> getWithLocation( String location){
        return get(()->dal().findByLocation(location));
    }
    
    public List<Item> getWithDescription( String description){
        return get(()->dal().findByDescription(description));
    }
    
    public Item getWithUrl( String url){
        return get(()->dal().findByUrl(url));
    }
    
    public List<Item> getWithCategory( int categoryId){
        return get(()->dal().findByCategory(categoryId));
    }
    
    @Override
    public List<Item> search( String search){
        return get(()->dal().findContaining(search));
    }

    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();
        if(parameterMap.containsKey(ID)){
            item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }        
        if(parameterMap.get(DESCRIPTION)[0].length() < 255)
            {item.setDescription(parameterMap.get(DESCRIPTION)[0]);}
        else if(parameterMap.get(DESCRIPTION)[0].length() == 0){
            item.setDescription("No Description");
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Description. Set to 'No Description'");
        } else{
            item.setDescription(parameterMap.get(DESCRIPTION)[0].substring(0, 254));
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Description. Truncated");
        }
        
        if(parameterMap.get(LOCATION)[0].length() < 45)
            {item.setLocation(parameterMap.get(LOCATION)[0]);}
        else{
            item.setLocation(parameterMap.get(LOCATION)[0].substring(0, 44));
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Location. Truncated");
        }
        
        if(parameterMap.get(PRICE)[0].length() < 18 && Long.parseLong(parameterMap.get(PRICE)[0]) < 999999999999999.99)
            {item.setPrice(Long.parseLong(parameterMap.get(PRICE)[0]));}
        else{
            item.setPrice(0);
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Price. Set to 0");
        }
        
        if(parameterMap.get(TITLE)[0].length() < 255)
            {item.setTitle(parameterMap.get(TITLE)[0]);}
        else if(parameterMap.get(TITLE)[0].length() == 0){
            item.setTitle("Untitled");
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Title. Set to 'Untitled'");
        }else{
            item.setTitle(parameterMap.get(TITLE)[0].substring(0, 254));
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Title. Truncated");
        }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            item.setDate(format.parse(parameterMap.get(DATE)[0]));
        } catch (ParseException ex) {
            Logger.getLogger(ItemLogic.class.getName()).log(Level.SEVERE, "Failed to set Date, default Jan 01, 2001 set.", ex);
            try {
                item.setDate(format.parse("2000/01/01"));
            } catch (ParseException ex1) {
                Logger.getLogger(ItemLogic.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        if(parameterMap.get(URL)[0].length() < 255)
            {item.setTitle(parameterMap.get(URL)[0]);}
        else if(parameterMap.get(URL)[0].length() == 0){
            item.setUrl("Url Not Found");
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Set to 'Url Not Found'");
        }else{
            item.setUrl(parameterMap.get(URL)[0].substring(0, 254));
            Logger.getLogger(ItemLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Truncated");
        }
        return item;
    } 

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Description", "Category Id", "Image Id", "Location", "Price", "Title", "Date", "Url");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, DESCRIPTION, CATEGORY_ID, IMAGE_ID, LOCATION, PRICE, TITLE, DATE, URL);
    }

    @Override
    public List<?> extractDataAsList( Item e) {
        return Arrays.asList(e.getId(), e.getDescription(), e.getCategoryId(), e.getImageId(), e.getLocation(), e.getPrice(), e.getTitle(), e.getDate(), e.getUrl());
    }
}
