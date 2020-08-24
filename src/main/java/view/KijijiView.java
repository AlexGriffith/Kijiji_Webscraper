
package view;

import common.FileUtility;
import entity.Category;
import entity.Image;
import entity.Item;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import scraper.kijiji.Kijiji;
import scraper.kijiji.KijijiItem;

/**
 *Servlet that displays all the items that exist in the database.
 * doGet also handles the transfer of items from KijijiItem's to Items that can be stored in the database. 
 * Calls all the methods in the Kijiji class.
 * @author griff
 */
@WebServlet(name = "Kijiji", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * Processes the printwriter printing the HTML text. 
     * Uses a loop to display details about each item in itemList, including calling ImageDelivery.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ItemLogic iLogic = new ItemLogic();
        List<Item> itemList = iLogic.getAll();
        //Item item = itemList.get(1);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
                out.println("<head>");
                    out.println("<title>Servlet KijijiView</title>");  
                    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"\\WebScraper\\KijijiStyle.css\">");
                out.println("</head>");
                out.println("<body>");
                    out.println("<h1>Servlet KijijiView at " + request.getContextPath() + "</h1>");
                    out.println("<div class=\"center-column\">");

                    for(int i = 0; i < itemList.size(); i++){
                            out.println("<div class=\"center-column\"> <br><br>");
                                out.println("<div class=\"image\">"); 
                                    //out.println("<img src=\"ImageDelivery?id="+itemList.get(i).getImageId().getName()+"\" style=\"max-width: 250px; max-height: 200px;\" />");
                                    String imageName = itemList.get(i).getImageId().getName();
                                    //String hardcode = "C:\\Users\\griff\\KijijiImages\\1143386917.jpg";
                                    out.println("<img src=\"ImageDelivery/"+imageName+"\" style=\"max-width: 250px; max-height: 200px;\" />");
                                out.println("</div>");
                                out.println("<div class=\"details\">"); 
                                    out.println("<div class=\"title\">");
                                        out.println("<a href=\""+itemList.get(i).getUrl()+"\" target=\"_blank\">"+itemList.get(i).getTitle()+"</a>");
                                    out.println("</div>");
                                    out.println("<div class=\"price\">");
                                        out.println("$"+itemList.get(i).getPrice()+".00");    
                                    out.println("</div>");    
                                    out.println("<div class=\"date\">");     
                                        out.println(itemList.get(i).getDate());    
                                    out.println("</div>");    
                                    out.println("<div class=\"location\">");     
                                        out.println(itemList.get(i).getLocation());
                                    out.println("</div>");
                                    out.println("<div class=\"description\">");
                                        out.println(itemList.get(i).getDescription());
                                    out.println("</div>");
                                out.println("</div>");
                            out.println("</div>");
                        }
                    out.println("</div>"); 
                out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * doGet handles the direct request for this servlet.
     * the 'Kijiji' downloads the whole page from the category url given. 
     * then takes all the attributes off the page. 
     * One by one each item is then filled into a KijijiItem. 
     * Each KijijiItem is then run through process items, and the Consumer to make them into Items and add them to the database. 
     * process request then displays all the html, and pulls the information out of the database. 
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String imageLocation = System.getProperty("user.home")+"\\KijijiImages\\";
        new File(imageLocation).mkdir();
        
        CategoryLogic cLogic = new CategoryLogic();
        
        Category pageCategory = cLogic.getWithId(1);
        
        ImageLogic imLogic = new ImageLogic();
        
        
        Kijiji kijiji = new Kijiji();
        kijiji.downloadPage(pageCategory.getUrl());
        kijiji.findAllItems();
        
        Consumer<KijijiItem> saveItems = (KijijiItem item) -> {
            
            ItemLogic iLogic = new ItemLogic();
            Item newItem = new Item();
            
            
            String imageName = item.getId()+".jpg";
            String imagePath = imageLocation+imageName;
            FileUtility.downloadAndSaveFile(item.getImageUrl(), imageLocation, imageName);
            Map<String, String[]> imMap = new HashMap<>();
            imMap.put(ImageLogic.NAME, new String[]{imageName});
            imMap.put(ImageLogic.URL, new String[]{item.getImageUrl()});
            imMap.put(ImageLogic.PATH, new String[]{imagePath});
            Image image = imLogic.createEntity(imMap);
            //image.setId(Integer.parseInt(item.getId()));
            if(imLogic.getWithPath(imagePath)==null)
                imLogic.add(image);
            
            
            newItem.setCategoryId(pageCategory);
            newItem.setImageId(imLogic.getWithPath(imagePath));
            newItem.setDescription(item.getDescription());
            newItem.setLocation(item.getLocation());
            newItem.setDate(new java.util.Date());
            try{
                NumberFormat format = NumberFormat.getCurrencyInstance();
                Number price = format.parse(item.getPrice());
                if(item.getPrice().length() >= 5)
                    newItem.setPrice(Long.parseLong(price.toString()));
                else
                    newItem.setPrice(0);
            }catch(NumberFormatException | ParseException ex){
                newItem.setPrice(0);
            } 
            newItem.setTitle(item.getTitle());
            newItem.setUrl(item.getUrl());
            newItem.setId(Integer.parseInt(item.getId()));
            
            if(iLogic.getWithId(newItem.getId())==null){
                iLogic.add(newItem);
            }
            
        };
            
        kijiji.proccessItems(saveItems);
                    
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
