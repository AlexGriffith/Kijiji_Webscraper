package dal;

import entity.Item;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author griff
 */
public class ItemDAL extends GenericDAL<Item>{

    public ItemDAL() {
        super( Item.class);
    }
    
    public List<Item> findAll(){
        return findResults( "Item.findAll", null);
    }
    
    public Item findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult( "Item.findById", map);
    }
    
    public List<Item> findByPrice( BigDecimal price){
        Map<String, Object> map = new HashMap<>();
        map.put("Price", price);
        return findResults( "Item.findByPrice", map);
    }
    
    public List<Item> findByTitle( String title){
        Map<String, Object> map = new HashMap<>();
        map.put("Title", title);
        return findResults( "Item.findByTitle", map);
    }
    
    public List<Item> findByDate( String date){
        Map<String, Object> map = new HashMap<>();
        map.put("Date", date);
        return findResults( "Item.findByDate", map);
    }
    
    public List<Item> findByLocation( String location){
        Map<String, Object> map = new HashMap<>();
        map.put("Location", location);
        return findResults( "Item.findByLocation", map);
    }
    
    public List<Item> findByDescription( String description){
        Map<String, Object> map = new HashMap<>();
        map.put("Description", description);
        return findResults( "Item.findByDescription", map);
    }
    
    public Item findByUrl( String url){
        Map<String, Object> map = new HashMap<>();
        map.put("Url", url);
        return findResult( "Item.findByUrl", map);
    }
    
    public List<Item> findByCategory( int category){
        Map<String, Object> map = new HashMap<>();
        map.put("Category", category);
        return findResults( "Item.findByCategory", map);
    }
    
    public List<Item> findContaining(String search){
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults( "Item.findContaining", map);
    }
}
