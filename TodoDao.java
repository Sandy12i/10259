package com.taskpackage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TodoDao {

    private String url = "jdbc:mysql://localhost:3306/todo";
    private String username = "root";
    private String password = "root";

    private String Insert = "INSERT INTO todo_items (description, dateandtime, status) VALUES (?, ?, ?);";
    private String Select = "SELECT id, description, dateandtime, status FROM todo_items WHERE id = ?;";
    private String ReadAll = "SELECT * FROM todo_items;";
    private String Delete = "DELETE FROM todo_items WHERE id = ?;";
    private String Update = "UPDATE todo_items SET description = ?, dateandtime = ?, status = ? WHERE id = ?;";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }


    public void insertTodo(Todo todo) throws SQLException {
        Connection con = getConnection();
        System.out.println(con);
        PreparedStatement ps = con.prepareStatement(Insert);

        ps.setString(1, todo.getDescription());
        ps.setTimestamp(2, todo.getDateandtime());  
        ps.setString(3, todo.getStatus());

       int i = ps.executeUpdate();
       if(i>0) {
        System.out.println("Insetes done");
       }
       else {
      System.out.println("Not done");
       }
        con.close();
    }


    public Todo selectbyid(int id) throws SQLException {
        Todo todo = null;

        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(Select);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String description = rs.getString("description");
            Timestamp dateandtime = rs.getTimestamp("dateandtime");
            String status = rs.getString("status");
            todo = new Todo(id, description, dateandtime, status);
        }
        con.close();
        return todo;
    }


    public List<Todo> Readall() throws SQLException {
        List<Todo> todolist = new ArrayList<>();

        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(ReadAll);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String description = rs.getString("description");
            Timestamp dateandtime = rs.getTimestamp("dateandtime");
            String status = rs.getString("status");
            todolist.add(new Todo(id, description, dateandtime, status));
        }
        con.close();
        return todolist;
    }


    public boolean deletebyid(int id) throws SQLException {
        boolean rowDeleted = false;

        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(Delete);
        ps.setInt(1, id);

        if (ps.executeUpdate() > 0) {
            rowDeleted = true;
        }
        con.close();
        return rowDeleted;
    }

 
    public boolean updateTodo(Todo todo) throws SQLException {
        boolean rowUpdated = false;

        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(Update);

        ps.setString(1, todo.getDescription());
        ps.setTimestamp(2, todo.getDateandtime());
        ps.setString(3, todo.getStatus());
        ps.setInt(4, todo.getId());

        if (ps.executeUpdate() > 0) {
            rowUpdated = true;
        }
        con.close();
        return rowUpdated;
    }
}

