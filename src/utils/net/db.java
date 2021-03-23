package utils.net;

import java.sql.*;
import java.util.ArrayList;
import utils.index;
import utils.thread.serverThread;


/**
 * <h1>MySQL DataBase Class </h1>
 * @version 1.0 from 14/03/2021
 * @author Matteo Lambertucci
 * @see index
 * @since 1.0
 */

public final class db extends Object {

   private Connection connection = null;
   private Statement statement = null;
   private ResultSet resultSet = null;
   private ArrayList<String> messages = null;

   public db() {
      try {
         this.connection();
         this.statement = this.connection.createStatement();
         this.messages = new ArrayList<String>();
      } catch(final SQLException e) {
         index.handleException(e);
      }
   }

   public db(final db db) {
      if(db != null) {
         this.setConnection(db.connection);
         this.setStatement(db.statement);
         this.setResultSet(db.resultSet);
         this.setMessages(db.messages);
      } else {
         final String className = this.getClass().toString();
         index.handleCopy_Construct_ERR(className);
      }
   }
   
   @Override
   public final String toString() {
      return "";
   }

   public final Connection getConnection() { return this.connection; }

   public final ResultSet getResultSet() { return this.resultSet; }

   public final Statement getStatement() { return this.statement; }

   public final ArrayList<String> getMessages() { return this.messages; }

   public final String getTable() { return index.table; }

   public final String getAttr() { return index.attr; }

   public final void setConnection(final Connection connection) { this.connection = connection; }

   public final void setResultSet(final ResultSet resultSet) { this.resultSet = resultSet; }

   public final void setStatement(final Statement statement) { this.statement = statement; }

   public final void setMessages(final ArrayList<String> messages) { this.messages = messages; }

   public final void connection() {
      final String driver = "jdbc";
      final String type = "mysql";
      final String host = "127.0.0.1"; // InetAddress
      final int port = 3306;
      final String user = "root";
      final String password = "";
      final String database = "JavaChat";
      final String driverClassName = String.format("com.%s.cj.%s.Driver", type, driver );
      final String uri = String.format("%s:%s://%s:%d/%s", driver, type, host, port, database);
      try {
         Class.forName(driverClassName);
         this.connection = DriverManager.getConnection(uri, user, password);
         index.printQuery("Connection established with: " + uri);
      } catch(final ClassNotFoundException | SQLException e) {
         index.handleException(e);
      }
   }

   public final void readSelectResult() {
      try {
         this.messages.clear();
         while(this.resultSet.next()) {
            final String txt = this.resultSet.getString(index.attr);
            this.messages.add(txt);
         }
      } catch(final SQLException e) {
         index.handleException(e);
      }
   }

   public final void insert(final String txt) {
      final String query = String.format(
              "INSERT INTO %s ( %s ) VALUES ( '%s' );",
              index.table,
              index.attr,
              txt
      );
      try {
         final int result = this.statement.executeUpdate(query);
         if(result != 1)
            throw new SQLException();
         index.printQuery(query);
      } catch(final SQLException e) {
         index.handleException(e);
      }
   }

   public final void select() {
      final String query = String.format(
              "SELECT %s FROM %s;",
              index.attr,
              index.table
      );
      try {
         this.resultSet = this.statement.executeQuery(query);
         this.readSelectResult();
         index.printQuery(query);
      } catch(final SQLException e) {
         index.handleException(e);
      }
   }

   public final synchronized void close() {
      try {
         System.out.println("Database Connection shutting down...");
         this.connection.close();
         final boolean isClosed = this.connection.isClosed();
         if (isClosed) {
            index.printQuery("Database Connection Closed.");
            System.exit(1);
         } else
            System.out.println("Database Connection still running...");
      } catch (final SQLException e) {
         index.handleException(e);
      }
   }
}