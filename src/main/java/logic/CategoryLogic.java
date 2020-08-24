package logic;

import dal.CategoryDAL;
import entity.Category;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

/**
 * Holds all the methods that access the CategoryDAL to get categories from the database.
 * CreateEntity is what is used to instantiate a new category. It handles error checking. 
 * @author griff
 */
public class CategoryLogic extends GenericLogic<Category,CategoryDAL>{
    
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public CategoryLogic() {
        super(new CategoryDAL());
    }
    
    @Override
    public List<Category> getAll(){
        return get(()->dal().findAll());
    }
    
    @Override
    public Category getWithId( int id){
        return get(()->dal().findById(id));
    }
    
    public Category getWithUrl( String url){
        return get(()->dal().findByUrl(url));
    }
    
    public Category getWithTitle( String title){
        return get(()->dal().findByTitle(title));
    }
    
    @Override
    public List<Category> search( String search){
        return get(()->dal().findContaining(search));
    }

    @Override
    public Category createEntity(Map<String, String[]> parameterMap) {
        Category category = new Category();
        if(parameterMap.containsKey(ID)){
            category.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        if(parameterMap.get(TITLE)[0].length() < 255)
            {category.setTitle(parameterMap.get(TITLE)[0]);}
        else if(parameterMap.get(TITLE)[0].length() == 0){
            category.setTitle("Untitled");
            Logger.getLogger(CategoryLogic.class.getName()).log(Level.WARNING, "Failed to set Title. Set to 'Untitled'");
        } else
            {category.setTitle(parameterMap.get(TITLE)[0].substring(0, 254));
            Logger.getLogger(CategoryLogic.class.getName()).log(Level.WARNING, "Failed to set Title. Truncated");
        }
        if(parameterMap.get(URL)[0].length() < 255)
            {category.setUrl(parameterMap.get(URL)[0]);}
        else if(!parameterMap.containsKey(URL)){
            category.setTitle("Link Not Found");
            Logger.getLogger(CategoryLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Set to 'Link Not Found'");
        } else
            {category.setUrl(parameterMap.get(URL)[0].substring(0, 254));
            Logger.getLogger(CategoryLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Truncated");
        }
        return category;
    } 

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Title", "URL");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, TITLE, URL);
    }

    public List<?> extractDataAsList( Category e) {
        return Arrays.asList(e.getId(), e.getTitle(), e.getUrl());
    }
}
