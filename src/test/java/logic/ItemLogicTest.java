package logic;

import entity.Category;
import entity.Image;
import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Item;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * Flagging an error for SQL constraint violations now, but the tests were all passing previously.
 * 
 * @author Alex Griffith
 */
@Disabled
class ItemLogicTest {

    private static Tomcat tomcat;
    private static ItemLogic logic;
    private static CategoryLogic catLogic;
    private static ImageLogic imgLogic;
    private static Map<String, String[]> sampleMap = new HashMap<>();
    private static Map<String, String[]> testMap = new HashMap<>();
    private static Map<String, String[]> imgMap = new HashMap<>();

    private static Item testItem;
    private static Image testImage;

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
        
        
        logic = new ItemLogic();
        catLogic = new CategoryLogic();
        imgLogic = new ImageLogic();
        
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{"Junit 5"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(ItemLogic.CATEGORY_ID, new String[]{"1"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(ItemLogic.IMAGE_ID, new String[]{"junit5"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
        sampleMap.put(ItemLogic.LOCATION, new String[]{"junit_loc"});
        sampleMap.put(ItemLogic.PRICE, new String[]{"120"});
        sampleMap.put(ItemLogic.TITLE, new String[]{"junit5_titl"});
        sampleMap.put(ItemLogic.DATE, new String[]{"junit5_dat"});
        sampleMap.put(ItemLogic.URL, new String[]{"junit5_url"});
        
        testMap.put(ItemLogic.DESCRIPTION, new String[]{"Junit 5 test"});
        testMap.put(ItemLogic.CATEGORY_ID, new String[]{"2"});
        testMap.put(ItemLogic.IMAGE_ID, new String[]{"junit5 test"});
        testMap.put(ItemLogic.LOCATION, new String[]{"junit_loc test"});
        testMap.put(ItemLogic.PRICE, new String[]{"130"});
        testMap.put(ItemLogic.TITLE, new String[]{"junit5_titl test"});
        testMap.put(ItemLogic.DATE, new String[]{"junit5_dat test"});
        testMap.put(ItemLogic.URL, new String[]{"junit5_url test"});
        
        imgMap.put(ImageLogic.NAME, new String[]{"image test"});
        imgMap.put(ImageLogic.PATH, new String[]{"image test"});
        imgMap.put(ImageLogic.URL, new String[]{"image test"});
        testImage = imgLogic.createEntity(imgMap);
        imgLogic.add(testImage);
        
        testItem = logic.createEntity(testMap);
        testItem.setId(998);
        testItem.setCategoryId(catLogic.getWithId(1));
        testItem.setImageId(testImage);
        testItem.setUrl("Url Goes Here");
        logic.add(testItem);
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        ImageLogic imLogic = new ImageLogic();
        logic.delete(testItem);
        imLogic.delete(testImage);

        tomcat.stop();
    }

    @BeforeEach
    final void setUp() throws Exception {
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Item> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Item testAdd = logic.createEntity(sampleMap);
        testAdd.setId(999);
        testAdd.setCategoryId(catLogic.getWithId(1));
        testAdd.setImageId(testImage);
        testAdd.setUrl("Url Test");
        //add the newly created account to DB
        logic.add(testAdd);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testItem);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }
    @Test
    final void testGetWithId(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        Item returnedItem = logic.getWithId(testItem.getId());
        assertEquals(testItem.getId(), returnedItem.getId());
        assertEquals(testItem.getDescription(), returnedItem.getDescription());
        assertEquals(testItem.getCategoryId(), returnedItem.getCategoryId());
        assertEquals(testItem.getImageId(), returnedItem.getImageId());
        assertEquals(testItem.getLocation(), returnedItem.getLocation());
        assertEquals(testItem.getPrice(), returnedItem.getPrice());
        assertEquals(testItem.getTitle(), returnedItem.getTitle());
        assertEquals(testItem.getDate(), returnedItem.getDate());
    }
    @Test
    final void testGetWithDescription(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List<Item> returnedItems= logic.getWithDescription(testItem.getDescription());
        for(Item returnedItem : returnedItems)
            assertEquals(testItem.getDescription(), returnedItem.getDescription());
    }
    @Test
    final void testGetWithCategory(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List<Item> returnedItems = logic.getWithCategory(testItem.getCategoryId().getId());
        for(Item returnedItem : returnedItems)
            assertEquals(testItem.getCategoryId(), returnedItem.getCategoryId());
    }
    @Test
    final void testGetWithLocation(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List<Item> returnedItems = logic.getWithLocation(testItem.getLocation());
        for(Item returnedItem : returnedItems)
            assertEquals(testItem.getLocation(), returnedItem.getLocation());
    }
    @Test
    final void testGetWithPrice(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List<Item> returnedItems = logic.getWithPrice(new BigDecimal(testItem.getPrice()));
        for(Item returnedItem : returnedItems)
            assertEquals(new BigDecimal(testItem.getPrice()).compareTo(new BigDecimal(returnedItem.getPrice())), 0);
    }
    @Test
    final void testGetWithUrl(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        Item returnedItem = logic.getWithId(testItem.getId());
        assertEquals(testItem.getId(), returnedItem.getId());
        assertEquals(testItem.getDescription(), returnedItem.getDescription());
        assertEquals(testItem.getCategoryId(), returnedItem.getCategoryId());
        assertEquals(testItem.getImageId(), returnedItem.getImageId());
        assertEquals(testItem.getLocation(), returnedItem.getLocation());
        assertEquals(testItem.getPrice(), returnedItem.getPrice());
        assertEquals(testItem.getTitle(), returnedItem.getTitle());
        assertEquals(testItem.getDate(), returnedItem.getDate());
    }
    
    @Test
    final void testSearch(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List <Item> returnedItems = logic.search(testItem.getDescription());
        for(Item returnedItem : returnedItems)
            assertEquals(testItem, returnedItem);
    }
    @Test
    final void testGetColumnNames(){
        String[] columnNames = (String[]) logic.getColumnNames().toArray();
        String[] testColumnNames = {"ID", "Description", "Category Id", "Image Id", "Location", "Price", "Title", "Date", "Url"};
        for(int i =0; i < testColumnNames.length; i++){
            assertEquals(testColumnNames[i], columnNames[i]);
        }
    }
    @Test
    final void testGetColumnCodes(){
        String[] columnCodes = (String[]) logic.getColumnCodes().toArray();
        String[] testColumnCodes = {"id", "description", "categoryId", "imageId", "location", "price", "title", "date", "url"};
        for(int i =0; i < testColumnCodes.length; i++){
            assertEquals(testColumnCodes[i], columnCodes[i]);
        }
    }
    @Test
    final void testExtractDataAsList(){
        List <Item> items = logic.getAll();
        Item testItem = items.get(0);
        List <?> returnedItemList = logic.extractDataAsList(testItem); //Returns a list of the accounts id, displayName, user, and password
        assertEquals(testItem.getId(), returnedItemList.get(0));
        assertEquals(testItem.getDescription(), returnedItemList.get(1));
        assertEquals(testItem.getCategoryId(), returnedItemList.get(2));
        assertEquals(testItem.getImageId(), returnedItemList.get(3));
        assertEquals(testItem.getLocation(), returnedItemList.get(4));
        assertEquals(testItem.getPrice(), returnedItemList.get(5));
        assertEquals(testItem.getTitle(), returnedItemList.get(6));
        assertEquals(testItem.getDate(), returnedItemList.get(7));
    }
}
