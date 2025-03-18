
package web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "StudentServlet", urlPatterns = {"/student"})
public class StudentServlet extends HttpServlet {

    private final Map<String, Student> students = new HashMap<>();
    
    @Override
    public void init() throws ServletException {
        students.put("1", new Student("1", "Student 1", "ІМ-41мп", 
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sodales rutrum ipsum quis ultrices. Phasellus porta nisi magna, "
                        + "at accumsan felis dapibus sit amet."));
        students.put("2", new Student("2", "Student 2", "ІМ-41мп", 
                "Cras feugiat odio in lectus imperdiet, eu scelerisque nulla interdum. Sed eu lacinia augue. Vivamus nisi nibh, blandit nec nisi non, "
                        + "fringilla iaculis felis."));
        students.put("3", new Student("3", "Student 3", "ІМ-41мп", 
                "Mauris venenatis commodo arcu eget ornare. Duis dictum eleifend ipsum non malesuada. Cras dapibus pellentesque bibendum."));
        students.put("4", new Student("4", "Student 4", "ІС-41мп", 
                "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut vel congue erat, et auctor nibh. "
                        + "Maecenas non dolor enim. Nam vel ultrices sem, et sagittis ante."
                        + "Aenean tellus lectus, ullamcorper at lacinia a, pretium at quam."));
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Student student = students.get(id);
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Student`s info</title>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println(".header { background-color: #b9dfb6; padding: 10px; margin-bottom: 20px; }");
            out.println(".profile { margin: 20px 0; }");
            out.println(".footer { margin-top: 30px; padding-top: 10px; border-top: 1px solid #ddd; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            if (student != null) {
                out.println("<div class=\"header\">");
                out.println("<h1>Student`s info</h1>");
                out.println("</div>");
                
                out.println("<div class=\"profile\">");
                out.println("<h2>" + student.getName() + "</h2>");
                out.println("<img src=\"img/student.png\" alt=\"Student\" width=\"200\">");
     
                out.println("<p><strong>Group:</strong> " + student.getGroup() + "</p>");
                out.println("<p><strong>About:</strong> " + student.getAbout() + "</p>");
                out.println("<p>Contact info: <a href=\"https://web.telegram.org/a/\" target=\"_blank\">Telegram</a></p>");
                out.println("</div>");
            } else {
                out.println("<div class=\"header\">");
                out.println("<h1>Not found</h1>");
                out.println("</div>");
            }
            
            out.println("<div class=\"footer\">");
            out.println("<p><a href=\"index.html\">To main page</a></p>");
            out.println("</div>");
            
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

class Student {
    private final String id;
    private final String name;
    private final String group;
    private final String about;
    
    public Student(String id, String name, String group, String about) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.about = about;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getGroup() { return group; }
    public String getAbout() { return about; }
}
