package view;

import entity.Category;
import entity.Image;
import entity.Item;
import logic.ItemLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.CategoryLogic;
import logic.ImageLogic;

/**
 * a form to input new items into the database manually. 
 * Used for feature testing, and debugging of other methods. 
 * @author griff
 */
@WebServlet(name = "CreateItem", urlPatterns = {"/CreateItem"})
public class CreateItem extends HttpServlet {

    private String errorMessage = null;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Feed</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");
            
            out.println("Item Id:<br>");
            out.printf("<input type=\"number\" min=\"1\" step=\"1\" name=\"%s\" value=\"\"><br>",ItemLogic.ID);
            out.println("<br>");

            out.println("Title:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.TITLE);
            out.println("<br>");

            out.println("Location:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.LOCATION);
            out.println("<br>");
            
            out.println("Description:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.DESCRIPTION);
            out.println("<br>");

            out.println("Date as 'yyyy/mm/dd':<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.DATE);
            out.println("<br>");

            out.println("Price:<br>");
            out.printf("<input type=\"number\" min=\"1\" step=\"1\" name=\"%s\" value=\"\"><br>",ItemLogic.PRICE);
            out.println("<br>");

            out.println("Url:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>",ItemLogic.URL);
            out.println("<br>");
            
            out.println("Category Id:<br>");
            out.printf("<input type=\"number\" min=\"1\" step=\"1\" name=\"%s\" value=\"\"><br>",ItemLogic.CATEGORY_ID);
            out.println("<br>");
            
            out.println("Image Id:<br>");
            out.printf("<input type=\"number\" min=\"1\" step=\"1\" name=\"%s\" value=\"\"><br>",ItemLogic.IMAGE_ID);
            out.println("<br>");
            
            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if(errorMessage!=null&&!errorMessage.isEmpty()){
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * get method is called first when requesting a URL. since this servlet
     * will create a host this method simple delivers the html code. 
     * creation will be done in doPost method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * this method will handle the creation of entity. as it is called by user
     * submitting data through browser.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        
        ItemLogic iLogic = new ItemLogic();
        CategoryLogic cLogic = new CategoryLogic();
        ImageLogic imLogic = new ImageLogic();
        int itemId = 0;
        if(request.getParameter(ItemLogic.ID)!=null){
            itemId = Integer.parseInt(request.getParameter(ItemLogic.ID));
        }
        String title = request.getParameter( ItemLogic.TITLE);
        String description = request.getParameter( ItemLogic.DESCRIPTION);
        String url = request.getParameter( ItemLogic.URL);
        String categoryId = request.getParameter( ItemLogic.CATEGORY_ID);
        String imageId = request.getParameter( ItemLogic.IMAGE_ID);
        Category category = cLogic.getWithId(Integer.parseInt(categoryId));
        Image image = imLogic.getWithId(Integer.parseInt(imageId));
        List<Item> titleList = iLogic.getWithTitle(title);
        boolean titleMatched = false;
        for(Item titleCheck : titleList){
            if(title.equals(titleCheck.getTitle()))
                titleMatched = true;
        }
        List<Item> descriptionList = iLogic.getWithDescription(description);
        boolean descriptionMatched = false;
        for(Item descriptionCheck : descriptionList){
            if(description.equals(descriptionCheck.getDescription()))
                descriptionMatched = true;
        }
        if(itemId==0){
            itemId = descriptionList.size()+1;
        }
        
        if(iLogic.getWithId(itemId) != null){
            errorMessage = "Item Id "+itemId+" already exists";
        }else
        if(title.equals("") || titleMatched){
            errorMessage = "Title "+title+" already exists, or is blank.";
        }else if(description.equals("") || descriptionMatched){
            errorMessage = "Description "+description+" already exists, or is blank.";
        }else if(url.equals("") || iLogic.getWithUrl(url)!=null){
            errorMessage = "Url "+url+" already exists, or is blank.";
        }else if(categoryId.equals("") || category == null){
            errorMessage = "Category Id is blank or category does not exist.";
        }else if(imageId.equals("") || image == null){
            errorMessage = "Image Id is blank or image does not exist.";
        }else{
            Item item = iLogic.createEntity( request.getParameterMap());
            item.setCategoryId(category);
            item.setImageId(image);
            item.setUrl(url);
            iLogic.add(item);
        }

        if( request.getParameter("add")!=null){
            //if add button is pressed return the same page
            processRequest(request, response);
        }else if (request.getParameter("view")!=null) {
            //if view button is pressed redirect to the appropriate table
            response.sendRedirect("ItemTable");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create an Item Entity";
    }

    private static final boolean DEBUG = true;

    @Override
    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    /**
     *
     * @param msg
     * @param t
     */
    @Override
    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
}