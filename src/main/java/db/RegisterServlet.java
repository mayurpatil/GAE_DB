package db;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet
{
   @SuppressWarnings("deprecation")
   public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
   {
      resp.setContentType("text/html");

      // UserService userService = UserServiceFactory.getUserService();
      // User user = userService.getCurrentUser();
      String usn = req.getParameter("USN");
      String name = req.getParameter("Name");
      String btn = req.getParameter("btn");

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      if (btn.equals("Register") && (usn != null) && (name != null))
      {
         Key dbKey = KeyFactory.createKey("Student_DB", usn);
         Entity STudentDB = new Entity("Student_DB", dbKey);

         STudentDB.setProperty("USN", usn);
         STudentDB.setProperty("Name", name);

         datastore.put(STudentDB);
         resp.getWriter().println("<center><H1> Registered: USN:" + usn + " Hello, " + name + "</H1></center>");
      }

      if (btn.equals("List-All"))
      {
         Query q = new Query("Student_DB");
         PreparedQuery pq = datastore.prepare(q);
         resp.getWriter().println("<center><table>");
         resp.getWriter().println("<th>USN</th>");
         resp.getWriter().println("<th>Name</th>");
         for (Entity result : pq.asIterable())
         {
            usn = result.getProperty("USN").toString();
            name = result.getProperty("Name").toString();
            resp.getWriter().println("<tr><H1>");
            resp.getWriter().println("<td>" + usn + "</td> <td>" + name + "</td");
            resp.getWriter().println("</H1></tr>");
         }
         resp.getWriter().println("</table></center>");
      }

      if (btn.equals("Search") && (usn != null))
      {
         Key dbKey = KeyFactory.createKey("Student_DB", usn);
         Query q = new Query("Student_DB", dbKey);
         PreparedQuery pq = datastore.prepare(q);

         if (pq != null)
         {
            Entity result = pq.asSingleEntity();
            usn = result.getProperty("USN").toString();
            name = result.getProperty("Name").toString();
            resp.getWriter().println("<center><H1> Record Found - USN:" + usn + " Name: " + name + "</H1></center>");
         }
         else
         {
            resp.getWriter().println("<center><H1> Record NOT Found - USN:" + usn + "</H1></center>");
         }
      }

      if (btn.equals("Delete") && (usn != null))
      {
         Key dbKey = KeyFactory.createKey("Student_DB", usn);

         Query q = new Query("Student_DB", dbKey);
         PreparedQuery pq = datastore.prepare(q);

         if (pq != null)
         {
            Entity result = pq.asSingleEntity();
            usn = result.getProperty("USN").toString();
            name = result.getProperty("Name").toString();
            datastore.delete(dbKey);

            resp.getWriter().println("<center><h1>Deleted Record - USN:" + usn + " Name: " + name + "</h1></center>");
         }
         else
         {
            resp.getWriter().println("<center><H1> Record NOT Found - USN:" + usn + "</H1></center>");
         }
      }

      resp.getWriter().print("<html><head> <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>");
      resp.getWriter().print("<title>DB on GAE</title>");
      resp
         .getWriter()
         .print(
            "</head><center><body><h6>Simple Database Application on Google App Engine using Cloud IDE Codenvy.com and Datastore for CRUD operations.</h6>");
      resp.getWriter().print("<form action=\"/register\" method=\"post\">");
      resp.getWriter().print("Enter USN: <input name=\"USN\" type=\"text\"/>");
      resp.getWriter().print("Enter Name: <input name=\"Name\" type=\"text\"/>");
      resp
         .getWriter()
         .print(
            "<input name=\"btn\" type=\"Submit\" value=\"Register\" /> <input name=\"btn\" type=\"Submit\" value=\"List-All\" /> <input name=\"btn\" type=\"Submit\" value=\"Search\" /> <input name=\"btn\" type=\"Submit\" value=\"Delete\" /></form>");
      resp.getWriter().print("</body></center></html>");

   }
}
