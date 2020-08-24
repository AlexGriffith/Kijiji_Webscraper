package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Account;
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
class AccountLogicTest {

    private static Tomcat tomcat;
    private AccountLogic logic;
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
        logic = new AccountLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        sampleMap.put(AccountLogic.DISPLAY_NAME, new String[]{"Junit 5 Test"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(AccountLogic.USER, new String[]{"junit"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(AccountLogic.PASSWORD, new String[]{"junit5"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Account> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testAccount);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }
    @Test
    final void testGetWithId(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        Account returnedAc = logic.getWithId(testAc.getId());
        assertEquals(testAc.getId(), returnedAc.getId());
        assertEquals(testAc.getUser(), returnedAc.getUser());
        assertEquals(testAc.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAc.getPassword(), returnedAc.getPassword());
    }
    @Test
    final void testGetWithName(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        Account returnedAc = logic.getAccountWithDisplayName(testAc.getDisplayName());
        assertEquals(testAc.getId(), returnedAc.getId());
        assertEquals(testAc.getUser(), returnedAc.getUser());
        assertEquals(testAc.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAc.getPassword(), returnedAc.getPassword());
    }
    @Test
    final void testGetWithUser(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        Account returnedAc = logic.getAccountWIthUser(testAc.getUser());
        assertEquals(testAc.getId(), returnedAc.getId());
        assertEquals(testAc.getUser(), returnedAc.getUser());
        assertEquals(testAc.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAc.getPassword(), returnedAc.getPassword());
    }
    @Test
    final void testGetWithPassword(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        List <Account> returnedAcs = logic.getAccountsWithPassword(testAc.getPassword());
        for(Account account : returnedAcs){
            assertEquals(testAc.getPassword(), account.getPassword());
        }
    }
    @Test
    final void testGetWith(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        Account returnedAc = logic.getAccountWith(testAc.getUser(), testAc.getPassword());
        assertEquals(testAc.getId(), returnedAc.getId());
        assertEquals(testAc.getUser(), returnedAc.getUser());
        assertEquals(testAc.getDisplayName(), returnedAc.getDisplayName());
        assertEquals(testAc.getPassword(), returnedAc.getPassword());
    }
    @Test
    final void testSearch(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        List <Account> returnedAcs = logic.search(testAc.getUser());
        for(Account returnedAc : returnedAcs)
            assertEquals(testAc, returnedAc);
    }
    @Test
    final void testGetColumnNames(){
        String[] columnNames = (String[]) logic.getColumnNames().toArray();
        String[] testColumnNames = {"ID", "Display Name", "User", "Password"};
        for(int i =0; i < testColumnNames.length; i++){
            assertEquals(testColumnNames[i], columnNames[i]);
        }
    }
    @Test
    final void testGetColumnCodes(){
        String[] columnCodes = (String[]) logic.getColumnCodes().toArray();
        String[] testColumnCodes = {"id", "displayName", "user", "password"};
        for(int i =0; i < testColumnCodes.length; i++){
            assertEquals(testColumnCodes[i], columnCodes[i]);
        }
    }
    @Test
    final void testExtractDataAsList(){
        List <Account> accounts = logic.getAll();
        Account testAc = accounts.get(0);
        List <?> returnedAcList = logic.extractDataAsList(testAc); //Returns a list of the accounts id, displayName, user, and password
        assertEquals(testAc.getId(), returnedAcList.get(0));
        assertEquals(testAc.getDisplayName(), returnedAcList.get(1));
        assertEquals(testAc.getUser(), returnedAcList.get(2));
        assertEquals(testAc.getPassword(), returnedAcList.get(3));
    }
}
