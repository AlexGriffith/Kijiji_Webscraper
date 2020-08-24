package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Image;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Alex Griffith
 */
class ImageLogicTest {

    private static Tomcat tomcat;
    private ImageLogic logic;
    private final Map<String, String[]> sampleMap = new HashMap<>();
    private final Map<String, String[]> testMap = new HashMap<>();
    private static Image sampleImage;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        System.out.println(new File("src\\main\\webapp\\").getAbsolutePath());
        tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(8080);
        Context context = tomcat.addWebapp("/WebScraper", new File("src\\main\\webapp").getAbsolutePath());
        context.addApplicationListener("dal.EMFactory");
        tomcat.init();
        tomcat.start();
        
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        tomcat.stop();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new ImageLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(ImageLogic.PATH, new String[]{"junit"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(ImageLogic.URL, new String[]{"junit5"});
        sampleMap.put(ImageLogic.NAME, new String[]{"JUNIT5"});
        
        testMap.put(ImageLogic.PATH, new String[]{"junitTest"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        testMap.put(ImageLogic.URL, new String[]{"junit5Test"});
        testMap.put(ImageLogic.NAME, new String[]{"JUNIT5Test"});
        
        //create a new Account and save it so we can delete later
        sampleImage = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(sampleImage);
    }

    @AfterEach
    final void tearDown() throws Exception {
        logic.delete(sampleImage);
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Image> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Image testImage = logic.createEntity(testMap);
        //add the newly created account to DB
        logic.add(testImage);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testImage);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }
    @Test
    final void testGetWithId(){    
        
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        Image returnedImg = logic.getWithId(testImg.getId());
        assertEquals(testImg.getId(), returnedImg.getId());
        assertEquals(testImg.getName(), returnedImg.getName());
        assertEquals(testImg.getPath(), returnedImg.getPath());
        assertEquals(testImg.getUrl(), returnedImg.getUrl());
        
    }
    @Test
    final void testGetWithName(){        
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        List<Image> returnedImgs = logic.getWithName(testImg.getName());
        for(Image returnedImg : returnedImgs)
            assertEquals(testImg.getName(), returnedImg.getName());
    }
    @Test
    final void testGetWithPath(){
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        Image returnedImg = logic.getWithPath(testImg.getPath());
        assertEquals(testImg.getId(), returnedImg.getId());
        assertEquals(testImg.getName(), returnedImg.getName());
        assertEquals(testImg.getPath(), returnedImg.getPath());
        assertEquals(testImg.getUrl(), returnedImg.getUrl());
    }
    @Test
    final void testGetWithUrl(){
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        List<Image> returnedImgs = logic.getWithUrl(testImg.getUrl());
        for(Image returnedImg : returnedImgs)
            assertEquals(testImg.getUrl(), returnedImg.getUrl());
        
    }
    @Test
    final void testSearch(){
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        List <Image> returnedImgs = logic.search(testImg.getName());
        for(Image returnedImg : returnedImgs)
            assertEquals(testImg, returnedImg);
    }
    @Test
    final void testGetColumnNames(){
        String[] columnNames = (String[]) logic.getColumnNames().toArray();
        String[] testColumnNames = {"ID", "Name", "Path", "Url"};
        for(int i =0; i < testColumnNames.length; i++){
            assertEquals(testColumnNames[i], columnNames[i]);
        }
    }
    @Test
    final void testGetColumnCodes(){
        String[] columnCodes = (String[]) logic.getColumnCodes().toArray();
        String[] testColumnCodes = {"id", "name", "path", "url"};
        for(int i =0; i < testColumnCodes.length; i++){
            assertEquals(testColumnCodes[i], columnCodes[i]);
        }
    }
    @Test
    final void testExtractDataAsList(){
        List <Image> images = logic.getAll();
        Image testImg = images.get(0);
        List <?> returnedImgList = logic.extractDataAsList(testImg); //Returns a list of the accounts id, displayName, user, and password
        assertEquals(testImg.getId(), returnedImgList.get(0));
        assertEquals(testImg.getName(), returnedImgList.get(1));
        assertEquals(testImg.getPath(), returnedImgList.get(2));
        assertEquals(testImg.getUrl(), returnedImgList.get(3));
    }
}
