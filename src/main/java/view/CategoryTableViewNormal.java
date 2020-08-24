package view;

import logic.CategoryLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Category;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;

/**
 *outputs a table of all the listed categories and their id's in the database. 
 * These categories will be the ones scraped by the kijiji class and KijijiView servlet to add items to the database. 
 * @author griff
 */
@WebServlet(name = "CategoryTable", urlPatterns = {"/CategoryTable"})
public class CategoryTableViewNormal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>CategoryViewNormal</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<table align=\"center\" border=\"1\">");
            out.println("<caption>Category</caption>");

            CategoryLogic logic = new CategoryLogic();
            List<Category> entity = logic.getAll();
            
            out.println("</tr>");
            List<?> columnNames = logic.getColumnNames();
            for (Object name: columnNames){
                out.println("<th>"+name+"</th>");
            }
            out.println("</tr>");
            
            for (Category e : entity) {
                //for other tables replace the code bellow with
                //extractDataAsList in a loop to fill the data.
                List<?> row = logic.extractDataAsList(e);
                out.printf("<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
                        row.get(0), row.get(1),row.get(2));
            }

            out.println("</tr>");
            for (Object name: columnNames){
                out.println("<th>"+name+"</th>");
            }
            out.println("</tr>");
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Category View Normal";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
