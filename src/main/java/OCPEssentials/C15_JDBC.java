package OCPEssentials;

import java.sql.*;

public class C15_JDBC {
    public static void main(String... args){
        new A_RelationalDatabases().relationalDatabases();
        new B_JDBCInterfaces().jdbcInterfaces();
        new C_Statements().statements();
        new D_ResultSets().retrievingDataWithResultSet();
    }
}

class A_RelationalDatabases{
    public void relationalDatabases(){
        //A relational database is organised into tables, consisting of columns describing properties of the data,
        //and one row for each element of data
        //You can access database data using the Java Database Connectivity API
        //Access to a relational database is done using Structured Query Langage - SQL
        //JDBC works by sending an SQL command to a database and processing the response

        /** Basic SQL Statements **/
        //Basic SQL statements are CRUD operations - CREATE, READ, UPDATE or DELETE
        //read statement -> SELECT * FROM table_name WHERE column_name = column_value
        //create statement -> INSERT INTO table_name VALUES (column1_value, column2_value, columnN_value)
        //update statement -> UPDATE table_name SET column1_name = column1_value WHERE column2_name = column2_value
        //delete statement -> DELETE FROM table_name WHERE column_name = column_value

        //SQL is case insensitive!
    }
}

class B_JDBCInterfaces {
    public void jdbcInterfaces() {
        //JDBC has 5 key interfaces that handle accessing and interacting with databases
        //The concrete classes have to be implemented at runtime, by using a database (driver) jar
        //The 5 key interfaces are:
        //Driver -> Establishes connection to database
        //Connection -> Sends commands to the databases
        //PreparedStatement -> Executes SQL queries
        //CallableStatement -> Executes stored commands/procedures in the database
        //ResultSet -> stores and allows for easy access of the results of a Query

        //These interfaces are located in the java.sql package

        connectingToDatabases();
    }

    private void connectingToDatabases() {
        //Each database is accessed using a JDBC url
        //It consists of 3 parts separated by semicolons (:)
        //    1) The protocol -> jdbc
        //    2) The subprotocol, which is the name of the database -> oracle
        //    3) The subname, which could be anything identifying the database -> //localhost/mydb
        //    In total, the JDBC url of the above would be jdbc:oracle://localhost/mydb

        //The JDK has a DriverManager class which has a static factory method to retrieve the db connection:
        try (Connection connection = DriverManager.getConnection("url")){
            //Use a try-with-resources to make sure the connection is properly closed!
            //    Could cause resource leakage otherwise
            //Method throws a checked SQLException:
        } catch (SQLException e) {
            System.out.println("Connection couldn't be established");
        }

        //The getConnection() method also has an overloaded variant to allow user/password:
        try (Connection connection = DriverManager.getConnection("url", "user", "pass")){
            //Method throws a checked SQLException:
        } catch (SQLException e) {
            System.out.println("Connection couldn't be established #2");
        }
    }
}

class C_Statements {
    public void statements(){
        //PreparedStatement and CallableStatement are both children of the Statement interface
        //In practice you will always use either a PreparedStatement or a CallableStatement implementation

        //You can obtain a PreparedStatement from a connection
        //First open the connection:
        try (Connection connection = DriverManager.getConnection("url", "user", "pass")){
            //Then, the connection offers a prepareStatement(String statementSQL) method which allows you to construct the
            //    PreparedStatement object from an SQL query:
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM table")){
                //Again, it's good practice to close the statement using try-with-resources
                //   Normally, closing a Preparedstatement will also close an encompassing ResultSet
                //             closing a Connection will also close the encompassing PreparedStatement
                //          so closing just the Connection is technically enough
                //You NEED to input a String you can't do an empty prepareStatement() call

                //There are 3 types of statement execution:
                //    1) Executing updates, delete and insert statements, this is done by
                int result = preparedStatement.executeUpdate();
                //       The result of the executeUpdate() method is the number of affected rows!
                //    2) Executing read operations, this is done by
                try(ResultSet resultSet = preparedStatement.executeQuery()){}
                //       The result of the executeQuery() method is the returned database recorded, stored in a ResultSet object
                //       Notice how a try-with-resources is used, since the ResultSet also must be closed
                //    3) Executing any statement, returning a boolean whether a ResultSet or RowCount (int) was returned:
                if (preparedStatement.execute()){
                    ResultSet resultSet = preparedStatement.getResultSet();
                } else {
                    int rowCount = preparedStatement.getUpdateCount();
                }

                //You CANNOT use executeUpdate with SELECT/READ statements, and you CANNOT use executeQuery with DELETE/INSERT/UPDATEs
                //You can, however, always use execute()
            }
        } catch (SQLException e) {
            System.out.println("Connection couldn't be established #3");
        }

        usingParameters();
        callableStatements();
    }

    private void usingParameters() {
        //Suppose you have a record RECORD with 3 columns: int1, string2 and int2
        //An insert statement would look like this: INSERT INTO record VALUES(1,' String', 2)
        //However, you do not want to hardcode statements like this every time, therefore you can use parameters:
        //A parameterized SQL query would be: INSERT INTO record VALUES(?,?,?)

        try (Connection connection = DriverManager.getConnection("url", "user", "pass")){
            //Use parameterized statement with preparedstatement as such:
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO record VALUES(?,?,?)")) {
                //Now you can use the statement object to pass the variables:
                preparedStatement.setInt(1, 1); //The first parameter is the index, this is the Nth ? in the query
                //                                                The count starts from 1!! So int1 is parameter 1!!

                //Pass a string as the second parameter:
                preparedStatement.setString(2, "String");
                //Pass the final int:
                preparedStatement.setInt(3, 2);
                int rowCount = preparedStatement.executeUpdate();

                //Beware that you use a set method for every parameterized value you defined in your query.
                //If you have 3 ?, you need to call setX for parameter 1, 2 and 3, otherwise you get an exception
                //Similarly, if you call setX for a parameter index that doesn't exist, you also get an exception
                //You can use setObject, and you can pass about anything using it and Java will (try) to convert it for you

                //After doing
                preparedStatement.executeUpdate();
                //The previously set parameters are still active! So if you call the same statement again, it will still
                //execute with those parameters you have previously set.
                //Note that calling an execute() method will close any currently open resultsets.
            }
        } catch (SQLException e){
            System.out.println("Connection couldn't be established #4");
        }
    }

    private void callableStatements() {
        //A callable statements can be used to execute a stored piece of SQL code in a database, a stored procedure
        //The syntax for a simple callable statement is:
        String sql = "{CALL stored_procedure_name()}"; //Notice the {} and the call keyword
        try (Connection connection = DriverManager.getConnection("url");
             CallableStatement callableStatement = connection.prepareCall(sql)) { //Use CallableStatement and prepareCall(String sql)
            ResultSet resultSet = callableStatement.executeQuery(); //Simply do executeQuery in the same way as before
            //Do something with it
        } catch (SQLException e) {
            System.out.println("Oops");
        }

        //The stored procedure could have a parameter required for input
        //This is called an IN Parameter
        //The syntax for registering IN parameters is as follows:
        String sqlIn = "{CALL stored_procedure_name(?)}"; //Notice the ? between the ()
        try (Connection connection = DriverManager.getConnection("url");
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            //Before executing the statement, register the parameter by doing:
            callableStatement.setString("name","input"); //Register by parameter name
            callableStatement.setString(1,"input"); //Register by parameter index number (starting 1)
            ResultSet resultSet = callableStatement.executeQuery(); //Simply do executeQuery in the same way as before
            //Do something with it
        } catch (SQLException e) {
            System.out.println("Oops");
        }

        //Furthermore, the stored procedure could have output, this is called an OUT parameter
        //The syntax for this is:
        String sqlOut = "{?= CALL stored_procedure_name(?)}"; //Notice the ?= before CALL
        try (Connection connection = DriverManager.getConnection("url");
        CallableStatement callableStatement = connection.prepareCall(sqlOut)){
            //Before executing the statement, register the out parameter:
            callableStatement.registerOutParameter(1,Types.VARCHAR); //Register via index
            callableStatement.registerOutParameter("Name",Types.VARCHAR); //Register via name
                                                                                       //Notice how the type is important to register
            //Execute the statement doing:
            callableStatement.execute(); //Execute instead of query, because you do not get a resultset, you get a string:
            //Get the result by doing:
            String result = callableStatement.getString("Name"); //or
            String resultAlso = callableStatement.getString(1); //via parameter index

            //prepareCall and prepareStatement has another overloaded variant which takes 3 parameters:
            connection.prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            //The TYPES are integers, the first is the Type and the second is the concurrency mode
            //Available resultset types:
            int a = ResultSet.TYPE_FORWARD_ONLY; int b = ResultSet.TYPE_SCROLL_INSENSITIVE; int c = ResultSet.TYPE_SCROLL_SENSITIVE;
            //Available concurrency modes:
            int d = ResultSet.CONCUR_UPDATABLE; int e = ResultSet.CONCUR_READ_ONLY;
        } catch (SQLException e) {
            System.out.println("Oops 2");
        }

        //A parameter can also be an INOUT parameter, this means that you register the same parameter as in and as out
        //This has the same syntax as an IN parameter, but you do setInt(1,1) as well as registerOutParameter(1, Types.INTEGER)

    }
}

class D_ResultSets {
    public void retrievingDataWithResultSet(){
        //The results of JDBC queries are stored in and returned as a ResultSet
        //It's a collection of records you can iterate over
        //If you have a connection and a PreparedStatement, you can get a resultset by executing the statement:
        try (Connection connection = DriverManager.getConnection("url", "user", "pass")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM table")) {
                //Get the resultset:
                ResultSet resultSet = preparedStatement.executeQuery();
                //ResultSet.next() gets the next result, you HAVE to call next() at least once to get the first result
                while(resultSet.next()) {//Gets the next result and return whether there was a next result
                    //                      use while to loop over all of them, just use if(resultSet.next) when you just want one
                    resultSet.getInt(1); //get the int at the first index of the resultset
                    //                                AGAIN, index starts at 1!
                    resultSet.getInt("first"); //get the int with column_name "first"
                    //getString, getLong, getDouble, getBoolean, getObject also work

                    //when resultSet.next() returns false, calling a getX() method on it throws SQLException
                    //                                      that's why it's important to always check the result of next()
                    //You also get an SQLException if you specify a column (index/name) that doesn't exist
                    
                }
            }
        } catch (SQLException e){
            System.out.println("Connection couldn't be established #5");
        }
    }
}

class E_Transactions {
    public void controllingTransactions() {
        //Per default data is saved to the database (committed) right away.
        //You can change this default behavior by marking the connection:
        try (Connection connection = DriverManager.getConnection("url")){
            connection.setAutoCommit(false); //No longer commit automatically
            //Now you have to commit manually by doing:
            connection.commit();
            //By doing this, you can rollback to the last commit if something fails by:
            connection.rollback();
            //You can also create a savepoint by doing:
            Savepoint point1 = connection.setSavepoint(); //anonymous savepoint
            Savepoint point2 = connection.setSavepoint("point2"); //Or a named savepoint
            //And then you can specifically rollback to that point using:
            connection.rollback(point1);
            //Notice a few things:
            //   1) it is only possible to use rollback and savepoints if you set autocommit to false
            //   2) rolling back is only possible if the savepoint still exists,
            //      by doing rollback(point1), point2 is voided and rolling back to point2 results in an Exception
            //      also doing rollback() voids all currently existing savepoints
            //   3) setting autocommit back to true automatically triggers an initial commit
        } catch (SQLException e) {

        }
    }
}
