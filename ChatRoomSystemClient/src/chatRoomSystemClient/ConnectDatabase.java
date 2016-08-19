package chatRoomSystemClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2016年4月26日
 * @time             上午11:27:36
 * @project_name     ChatRoomSystemClient
 * @package_name     
 * @file_name        ConnectDatabase.java
 * @type_name        ConnectDatabase
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

public class ConnectDatabase
{
	public ConnectDatabase()
	{
		
	}
	
	String sqlDriverName = "com.mysql.jdbc.Driver";
	String sqlName = "chatroom";
	String url = "jdbc:mysql://localhost:3306/" + sqlName;
	String user = "root";
	String password = "root";
	ResultSet resultSet = null;
	Statement statement = null;
	
	public ResultSet getResultSet()
	{
		try
		{
			Class.forName(sqlDriverName);
			Connection connection = (Connection) DriverManager.getConnection(
					url , user , password);
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from user");
		}
		catch(Exception e)
		{
			String str = "\n ConnectDatabase类：查找数据库异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		
		return resultSet;
	}
	
	public Statement getStatement()
	{
		try
		{
			Class.forName(sqlDriverName);
			Connection connection = (Connection) DriverManager.getConnection(
					url , user , password);
			statement = connection.createStatement();
		}
		catch(Exception e)
		{
			String str = "\n ConnectDatabase类：查找数据库异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		
		return statement;
	}
	
}
