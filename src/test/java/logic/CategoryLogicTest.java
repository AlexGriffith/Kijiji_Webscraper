package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Category;
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
class CategoryLogicTest {

    private static Tomcat tomcat;
    private CategoryLogic logic;
    private Map<String, String[]> sampleMap = new HashMap<>();

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
        logic = new CategoryLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(CategoryLogic.TITLE, new String[]{"junit"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(CategoryLogic.URL, new String[]{"junit5"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Category> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Category testCategory = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testCategory);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testCategory);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }
    @Test
    final void testGetWithId(){
        List <Category> categories = logic.getAll();
        Category testCat = categories.get(0);
        Category returnedCat = logic.getWithId(testCat.getId());
        assertEquals(testCat.getId(), returnedCat.getId());
        assertEquals(testCat.getTitle(), returnedCat.getTitle());
        assertEquals(testCat.getUrl(), returnedCat.getUrl());
    }
    @Test
    final void testGetWithTitle(){
        List <Category> categories = logic.getAll();
        Category testCat = categories.get(0);
        Category returnedCat = logic.getWithTitle(testCat.getTitle());
        assertEquals(testCat.getId(), returnedCat.getId());
        assertEquals(testCat.getTitle(), returnedCat.getTitle());
        assertEquals(testCat.getUrl(), returnedCat.getUrl());
    }
    @Test
    final void testGetWithUrl(){
        List <Category> categories = logic.getAll();
        Category testCat = categories.get(0);
        Category returnedCat = logic.getWithUrl(testCat.getUrl());
        assertEquals(testCat.getId(), returnedCat.getId());
        assertEquals(testCat.getTitle(), returnedCat.getTitle());
        assertEquals(testCat.getUrl(), returnedCat.getUrl());
    }
    @Test
    final void testSearch(){
        List <Category> categories = logic.getAll();
        Category testCat = categories.get(0);
        List <Category> returnedCats = logic.search(testCat.getTitle());
        for(Category returnedCat : returnedCats)
            assertEquals(testCat, returnedCat);
    }
    @Test
    final void testGetColumnNames(){
        String[] columnNames = (String[]) logic.getColumnNames().toArray();
        String[] testColumnNames = {"ID", "Title", "URL"};
        for(int i =0; i < testColumnNames.length; i++){
            assertEquals(testColumnNames[i], columnNames[i]);
        }
    }
    @Test
    final void testGetColumnCodes(){
        String[] columnCodes = (String[]) logic.getColumnCodes().toArray();
        String[] testColumnCodes = {"id", "title", "url"};
        for(int i =0; i < testColumnCodes.length; i++){
            assertEquals(testColumnCodes[i], columnCodes[i]);
        }
    }
    @Test
    final void testExtractDataAsList(){
        List <Category> categories = logic.getAll();
        Category testCat = categories.get(0);
        List <?> returnedCatList = logic.extractDataAsList(testCat); //Returns a list of the accounts id, displayName, user, and password
        assertEquals(testCat.getId(), returnedCatList.get(0));
        assertEquals(testCat.getTitle(), returnedCatList.get(1));
        assertEquals(testCat.getUrl(), returnedCatList.get(2));
    }
}
