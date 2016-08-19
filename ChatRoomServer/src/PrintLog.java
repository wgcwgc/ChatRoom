import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

/**
 * 
 */

/**
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2016��4��28��
 * @time             ����9:10:34
 * @project_name     ChatRoomSystemClient
 * @package_name     
 * @file_name        PrintLog.java
 * @type_name        PrintLog
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

public class PrintLog
{
	public PrintLog()
	{
		
	}
	
	public PrintLog(String str)
	{
		new Print(str);
	}
	
	public PrintLog(String path , String str)
	{
		new Print(path , str);
	}
}

class ReadIniFile
{
	@SuppressWarnings("rawtypes")
	HashMap sections = new HashMap();
	String currentSecion;
	Properties current;
	
	@SuppressWarnings("unchecked")
	public ReadIniFile(String filename)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(filename));
		}
		catch(Exception e)
		{
			String str = "\n ReadIniFile�ࣺ�����ļ������ڻ��߸�ʽ����ȷ������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
			System.exit(0);
		}
		
		try
		{
			String line;
			try
			{
				while( ( line = reader.readLine() ) != null)
				{
					line = line.trim();
					if(line.matches("\\[.*\\]"))
					{
						currentSecion = line.replaceFirst("\\[(.*)\\]" , "$1");
						current = new Properties();
						sections.put(currentSecion , current);
					}
					else if(line.matches(".*=.*"))
					{
						if(current != null)
						{
							int i = line.indexOf('=');
							String name = line.substring(0 , i);
							String value = line.substring(i + 1);
							current.setProperty(name , value);
						}
					}
				}
			}
			catch(Exception e)
			{
				String str = "\n ReadIniFile�ࣺ�����ļ���ȡ�쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			String str = "\n ReadIniFile�ࣺ�����ļ����ݲ��Ϸ�������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
			System.exit(0);
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception e)
			{
				String str = "\n ReadIniFile�ࣺ�����ļ��ر��쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
				System.exit(0);
			}
		}
	}
	
	public String getValue(String section , String name)
	{
		Properties p = (Properties) sections.get(section);
		if(p == null)
		{
			return null;
		}
		String value = p.getProperty(name);
		return value;
	}
	
}

class Log
{
	String filePathName = "";
	String fileList = "";
	boolean flog = false;
	
	public Log(String filePathName , String fileList , boolean flog)
	{
		this.filePathName = filePathName;
		this.fileList = fileList;
		this.flog = flog;
		String packagePath = this.getClass().getResource("").getPath().toString();
		packagePath = packagePath.substring(1 , packagePath.indexOf("bin"));
		ReadIniFile readIniFile = new ReadIniFile(packagePath + "/config/wgc.ini");
		String logSavePath = readIniFile.getValue("LogFiles" , "logSavePath");
		String yearMonthDay = logSavePath;
		yearMonthDay = logSavePath + "\\" + filePathName;
		File filepath = new File(yearMonthDay);
		if( ! filepath.getParentFile().exists() || ! filepath.exists())
		{
			filepath.getParentFile().mkdirs();
			try
			{
				filepath.createNewFile();
			}
			catch(Exception e1)
			{
				String str = "\n Log�ࣺ״̬��־�ļ������쳣������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
				System.exit(0);
			}
		}
		
		BufferedWriter bufferedWriter = null;
		try
		{
			if(flog)
				bufferedWriter = new BufferedWriter(new FileWriter(filepath ,
						true));
			else
				bufferedWriter = new BufferedWriter(new FileWriter(filepath));
			bufferedWriter.write(fileList);
		}
		catch(IOException e)
		{
			String str = "\n Log�ࣺ״̬��־�ļ�д���쳣������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
			System.exit(0);
		}
		finally
		{
			try
			{
				bufferedWriter.flush();
				bufferedWriter.close();
			}
			catch(IOException e)
			{
				String str = "\n Log�ࣺ�ļ����ر��쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
				System.exit(0);
			}
		}
	}
	
}


class Print
{
	String str = "";
	public Print(String str)
	{
		this.str = str;
		Calendar calendar = Calendar.getInstance();
		str = ( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS") )
				.format(calendar.getTime()) + "\r\n" + str + "\r\n";
		String path = "debug.log";
		new Log(path , str , true);
//		System.out.println(str);
	}
	
	public Print(String path , String str)
	{
		this.str = str;
		Calendar calendar = Calendar.getInstance();
		str = ( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS") )
				.format(calendar.getTime()) + "\r\n" + str + "\r\n";
		new Log(path , str , true);
//		System.out.println(str);
	}
	
}


