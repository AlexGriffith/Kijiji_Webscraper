package logic;

import dal.ImageDAL;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds all the methods that access the ImageDAL to get images from the database.
 * CreateEntity is what is used to instantiate a new image. It handles error checking. 
 * @author griff
 */
public class ImageLogic extends GenericLogic<Image,ImageDAL>{
    
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";
    
    public ImageLogic() {
        super(new ImageDAL());
    }
    
    @Override
    public List<Image> getAll(){
        return get(()->dal().findAll());
    }
    
    @Override
    public Image getWithId( int id){
        return get(()->dal().findById(id));
    }
    
    public List<Image> getWithName( String name){
        return get(()->dal().findByName(name));
    }

    public Image getWithPath( String path){
        return get(()->dal().findByPath(path));
    }

    public List<Image> getWithUrl( String url){
        return get(()->dal().findByUrl(url));
    }
    
    @Override
    public List<Image> search( String search){
        return get(()->dal().findContaining(search));
    }

    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        Image image = new Image();
        if(parameterMap.containsKey(ID)){
            image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        if(parameterMap.get(NAME)[0].length() < 255)
            {image.setName(parameterMap.get(NAME)[0]);}
        else if(parameterMap.get(NAME)[0].length() == 0){
            image.setName("Untitled");
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set Image. Set to 'Untitled'");
        } else
            {image.setName(parameterMap.get(NAME)[0].substring(0, 254));
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set Name. Truncated");
        }
        
        if(parameterMap.get(PATH)[0].length() < 255)
            {image.setPath(parameterMap.get(PATH)[0]);}
        else if(parameterMap.get(PATH)[0].length() == 0){
            image.setPath("Path Not Found");
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set PATH. Set to 'Path Not Found'");
        } else
            {image.setPath(parameterMap.get(PATH)[0].substring(0, 254));
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set Path. Truncated");
        }
        
        if(parameterMap.get(URL)[0].length() < 255)
            {image.setUrl(parameterMap.get(URL)[0]);}
        else if(parameterMap.get(URL)[0].length() == 0){
            image.setUrl("Untitled");
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Set to 'Url Not Found'");
        } else            {image.setUrl(parameterMap.get(URL)[0].substring(0, 254));
            Logger.getLogger(ImageLogic.class.getName()).log(Level.WARNING, "Failed to set Url. Truncated");
        }
        return image;
    } 

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Name", "Path", "Url");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, NAME, PATH, URL);
    }

    public List<?> extractDataAsList( Image e) {
        return Arrays.asList(e.getId(), e.getName(), e.getPath(), e.getUrl());
    }
}
