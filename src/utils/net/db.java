package utils.net;

import java.sql.*;
import java.util.ArrayList;


public class db {

   private Statement statement = null;
   private ResultSet resultSet = null;
   private Connection conn = null;
   private String table = "Message";
   private String attr = "txt";

   public db() throws SQLException {
      this.connect();
      this.statement = this.conn.createStatement();
   }

   public void connect() {
      final String driver = "jdbc";
      final String type = "mysql";
      final String host = "127.0.0.1"; // inetAddress
      final int port = 3306;
      final String user = "root";
      final String password = "";
      final String database = "JavaChat";
      final String driverClassName = String.format("com.%s.cj.%s.Driver", type, driver );
      final String uri = String.format("%s:%s://%s:%d/%s", driver, type, host, port, database);
      try {
         Class.forName(driverClassName);
         this.conn = DriverManager.getConnection(uri,user,password);
      } catch(ClassNotFoundException | SQLException e) {
         e.printStackTrace(System.err);
      }
   }

   public final String readSelectResult() {
      final ArrayList<String> result = new ArrayList<String>();
      try {
         while(this.resultSet.next()) {
            final String txt = this.resultSet.getString(this.attr);
            result.add(txt);
         }
      } catch(SQLException e) {
         e.printStackTrace();
      }
      return result.toString();
   }

   public final void insert(final String txt) {
      final String query = String.format("INSERT INTO %s ( %s ) VALUES ( '%s' )", this.table, this.attr, txt);
      try {
         System.out.println(query);
         this.statement.executeUpdate(query);
      } catch(SQLException e) {
         e.printStackTrace();
      }
   }

   public final void select() {
      final String query = String.format("SELECT %s FROM %s", this.attr, this.table);
      try {
         this.resultSet = this.statement.executeQuery(query);
         final String result = this.readSelectResult();
         System.out.println(query);
         System.out.println(result);
      } catch(SQLException e) {
         e.printStackTrace();
      }
   }

   public final synchronized void close() {
      try {
         this.conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) throws ClassNotFoundException, SQLException {
      final db db = new db();
      db.insert("bsbssbs");
      db.select();
      db.close();
   }
}