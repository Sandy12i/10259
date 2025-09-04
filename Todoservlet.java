import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.taskpackage.Todo;

import com.taskpackage.TodoDao;


@WebServlet("/todoservlet")

public class todoservlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	

	String todoHtml = "<!DOCTYPE html>\r\n"

			+ "<html lang=\"en\">\r\n"

			+ "<head>\r\n"

			+ "    <meta charset=\"UTF-8\">\r\n"

			+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"

			+ "    <title>ToDo TaskManager</title>\r\n"

			+ "    <link rel=\"stylesheet\" href=\"style.css\">\r\n"

			+ "</head>\r\n"

			+ "<body>\r\n"

			+ "    <h2>ToDO TaskManager</h2>\r\n"

			+ "\r\n"

			+ "    <form action=\"todoservlet\" method=\"post\">\r\n"

			+ "    <div class=\"form-group\">\r\n"

			+ "        <label>Date</label>\r\n"

			+ "        <input type=\"date\" id=\"date\">\r\n"

			+ "    </div>\r\n"

			+ "\r\n"

			+ "    <div class=\"form-group\">\r\n"

			+ "        <label>ToDo Item Description</label>\r\n"

			+ "        <input type=\"text\" id=\"description\" name=\"desc\" placeholder=\"Enet Description\">\r\n"

			+ "    </div>\r\n"

			+ "\r\n"

			+ "    <div class=\"form-group\">\r\n"

			+ "        <label>Target Date & Time</label>\r\n"

			+ "        <input type=\"datetime-local\" name=\"targetdt\" id=\"dateandtime\">\r\n"

			+ "    </div>\r\n"

			+ "\r\n"

			+ "    <div class=\"form-group\">\r\n"

			+ "        <label>Status</label>\r\n"

			+ "        <select id=\"status\" name=\"stat\">\r\n"

			+ "            <option value=\"Pending\">Pending</option>\r\n"

			+ "            <option value=\"In Progress\">In Progress</option>\r\n"

			+ "            <option value=\"Completed\">Completed</option>\r\n"

			+ "        </select>\r\n"

			+ "    </div>\r\n"

			+ "\r\n"

			+ "    <div class=\"form-group\">\r\n"

			+ "        <button  class=\"submit\" type=\"submit\" id=\"toadd\">Add Task</button>\r\n"

			+ "    </div>\r\n"

			+ "</form>\r\n"

			+ "</body>\r\n"

			+ "</html>\r\n"

			+ "";

    

    public todoservlet() {

        super();

       

    }



	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	

		response.setContentType("text/html");

		response.getWriter().append(todoHtml);

		response.getWriter().append("Served at: ").print("hrll");

	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {





	    String a = request.getParameter("desc");

	    String dateandtime = request.getParameter("targetdt");

	    String stat = request.getParameter("stat");

        response.getWriter().print(a);

	    

	    LocalDateTime dateTime = null;

	    try {

	        dateTime = LocalDateTime.parse(dateandtime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

	    } catch (Exception e) {

	        e.printStackTrace();

	        response.getWriter().println("Invalid date and time format.");

	        return;

	    }



	   PrintWriter pw = response.getWriter();
			   

	    Timestamp timestamp = Timestamp.valueOf(dateTime);



	    

	    Todo newtodo = new Todo(a, timestamp, stat);

	    TodoDao tdao = new TodoDao();

	    try {

			tdao.insertTodo(newtodo);

			System.out.println("done");

		} catch (SQLException e) {

			e.printStackTrace();

		}

	    

	    try {

	    	Todo todo = tdao.selectbyid(1);

	    	if (todo != null) {

	    	    response.getWriter().println("<h3>Task Details</h3>");

	    	    response.getWriter().println("<p>Description: " + todo.getDescription() + "</p>");

	    	    response.getWriter().println("<p>Date & Time: " + todo.getDateandtime() + "</p>");

	    	    response.getWriter().println("<p>Status: " + todo.getStatus() + "</p>");

	    	} else {

	    	    response.getWriter().println("No task found with that ID.");

	    	}

		} catch (SQLException e) {

		

			e.printStackTrace();

		}

	    try {
	        List<Todo> itemslist = tdao.Readall();
	       
	        if(itemslist.isEmpty()) {
	        pw.print("<h2> No Tasks</h2>");
	        }
	        else {
	        pw.println("<table>");
	        pw.println("<tr>");
	        pw.println("<th>ID</th>");
	        pw.println("<th>Task Description</th>");
	        pw.println("<th>Target DateandTime</th>");
	        pw.println("<th>status</th>");
	        pw.println("</tr>");
	       
	       
	        for(Todo t : itemslist) {
	        pw.println("<tr>");
	        pw.println("<td>"+ t.getId() +"</td>");
	        pw.println("<td>"+ t.getDescription()+"</td>");
	        pw.println("<td>"+ t.getDateandtime() +"</td>");
	        pw.println("<td>"+ t.getStatus() +"</td>");
	        pw.println("</tr>");
	        }
	        pw.println("</table>");
	       
	        }
	        }catch(Exception e) {
	        e.printStackTrace();
	        pw.print("<h2> Not Found</h2>");
	        }

	

	    

	    response.setContentType("text/html");

        response.getWriter().println("<h3>Task Created Successfully</h3>");

        response.getWriter().println("<a href='/todo_project/todoservlet'>Add Another Task</a>");

	}



}
