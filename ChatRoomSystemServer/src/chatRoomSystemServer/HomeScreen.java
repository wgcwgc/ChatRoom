package chatRoomSystemServer;

/**
 * @author Administrator
 * @copyright wgcwgc
 * @date 2016年4月26日
 * @time 上午10:30:27
 * @project_name ChatRoomSystemClient
 * @package_name
 * @file_name HomeScreen.java
 * @type_name HomeScreen
 * @enclosing_type
 * @tags
 * @todo
 * @others
 * 
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class HomeScreen extends JFrame implements ActionListener
{
	public JButton loginJButton;
	public JTextField userJTestField;
	public JPasswordField passwordJPasswordField;
	public double number = 0;
	
	@SuppressWarnings("deprecation")
	/**
	 * 主界面设计
	 */
	public HomeScreen()
	{
		Dimension frameSize = new Dimension(250 , 350);
		setSize(frameSize);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) ( screenSize.width - frameSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - frameSize.getHeight() ) / 2);
		setTitle("局域网聊天室系统");
		setResizable(false);
		setLayout(new GridLayout(12 , 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(new JLabel("              聊天室系统欢迎您！！！"));
		
		JLabel userNameJLabel = new JLabel("账号：");
		add(userNameJLabel);
		userJTestField = new JTextField(15);
		add(userJTestField);
		
		JLabel passwordJLabel = new JLabel("密码：");
		add(passwordJLabel);
		passwordJPasswordField = new JPasswordField(15);
		add(passwordJPasswordField);
		
		JButton loginJButton = new JButton("登录");
		add(loginJButton);
		loginJButton.addActionListener(new ActionLogin());
		
		JButton registerJButton = new JButton("注册");
		add(registerJButton);
		registerJButton.addActionListener(new ActionRegister());
		
		JButton modifyPasswordJButton = new JButton("修改密码");
		add(modifyPasswordJButton);
		modifyPasswordJButton.addActionListener(new ActionModifyPassword());
		
		JButton forgetPasswordJButton = new JButton("忘记密码");
		add(forgetPasswordJButton);
		forgetPasswordJButton.addActionListener(new ActionForgetPassword());
		
		JButton forgetUserNameJButton = new JButton("忘记账号");
		add(forgetUserNameJButton);
		forgetUserNameJButton.addActionListener(new ActionForgetUserName());
		
		JButton visitorLoginJButton = new JButton("游客登录");
		add(visitorLoginJButton);
		visitorLoginJButton.addActionListener(new ActionVisitorLogin());
		
		JButton aboutJButton = new JButton("关于");
		add(aboutJButton);
		aboutJButton.addActionListener(new ActionAbout());
		
		Image icon;
		icon = getImage("icon.gif");
		this.setIconImage(icon);
		show();
		setVisible(true);
	}
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @see 验证账号和密码正确
	 * @return  true
	 * @throws SQLException
	 */
	public boolean judge(String username , String password) throws SQLException
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(2).equals(username)
					&& rs.getString(3).equals(password))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param username
	 * @see 数据库中已经有此账号
	 * @return  true
	 * @throws SQLException
	 */
	public boolean judgeUserExist(String username) throws SQLException
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(2).equals(username))
				return true;
		}
		
		return false;
	}
	
	/**
	 * @param number
	 * @see 数据库中已经有此手机号
	 * @return true
	 */
	public boolean judgeNumberExist(String number) throws Exception
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(4).equals(number))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param username
	 * @param number
	 * @see 判断账号在数据库中 
	 * @return id
	 * @throws SQLException
	 */
	public String findUserName(String username , String number)
			throws SQLException
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(4).equals(number)
					&& rs.getString(2).equals(username))
				return rs.getString(1);
		}
		return "";
	}
	
	/**
	 * 
	 * @param username
	 * @see 根据账号找到id
	 * @return id
	 */
	public String findUserName(String username) throws Exception
	{
		
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(2).equals(username))
				return rs.getString(1);
		}
		return "";
	}
	
	/**
	 * 
	 * @param username
	 * @param number
	 * @see 判断某账号是否在数据库中
	 * @return password
	 * @throws SQLException
	 */
	public String findPassword(String username , String number)
			throws SQLException
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(2).equals(username)
					&& rs.getString(4).equals(number))
				return rs.getString(3);
		}
		return "";
	}
	
	/**
	 * 
	 * @param number
	 * @param realname
	 * @see 判断账号是否在数据库中 
	 * @return name
	 * @throws SQLException
	 */
	public String findUsername(String number , String realname)
			throws SQLException
	{
		ResultSet rs = new ConnectDatabase().getResultSet();
		while(rs.next())
		{
			if(rs.getString(4).equals(number)
					&& rs.getString(5).equals(realname))
				return rs.getString(2);
		}
		return "";
	}
	
	/**
	 *  登录
	 */
	class ActionLogin implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String userName = null;
			userName = userJTestField.getText();
			@SuppressWarnings("deprecation")
			String password = passwordJPasswordField.getText();
			try
			{
				if(judge(userName , password))
				{
					dispose();
					JOptionPane.showMessageDialog(null , "欢迎 " + userName
							+ " 来到服务器端.\n" + "祝您玩得愉快！！！");
					new ChatServer(userName);
				}
				else if(judgeUserExist(userName))
				{
					number ++ ;
					if(number == 3)
					{
						JOptionPane
								.showMessageDialog(
										userJTestField ,
										"账号密码输入错误3次，密码已被系统冻结，请24小时内修改密码！！！\n"
												+ "                                              "
												+ "^-^  ^-^  ^-^  ^-^");
						Statement statement = new ConnectDatabase()
								.getStatement();
						String string = null;
						try
						{
							string = findUserName(userName);
						}
						catch(Exception e1)
						{
							String str = "\n ActionLogin类：查找数据库异常！！！\n";
							str += e1.getMessage();
							new Print(str);
//							System.out.println(str);
						}
						password = new Random().toString();
						int a = statement
								.executeUpdate("update admin set password = '"
										+ password + "' where id = '"
										+ Integer.parseInt(string) + " '");
						if(a == 1)
						{
							number = 0;
						}
						System.exit(0);
					}
					else
					{
						JOptionPane.showMessageDialog(userJTestField ,
								"账号或者密码错误！！！\n请重新输入！！！");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(userJTestField ,
							"账号或者密码错误！！！\n请重新输入！！！");
				}
			}
			catch(Exception e1)
			{
				String str = "\n ActionLogin类：查找数据库异常1！！！\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		
	}
	
	/**
	 * 账号注册
	 */
	class ActionRegister implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("请输入您的账号：\n");
			try
			{
				if( ! username.trim().isEmpty())
				{
					if(judgeUserExist(username))
						JOptionPane.showMessageDialog(null ,
								"您的账号已经存在，可以直接登录。\n如果忘记密码，可以点击忘记密码找回，，，");
					else
					{
						String password = JOptionPane
								.showInputDialog("请输入您的密码\n");
						if(password.trim().isEmpty())
						{
							JOptionPane.showMessageDialog(null ,
									"密码不能为空，请重新注册！！！");
							dispose();
							return;
						}
						String passwordConfirm = JOptionPane
								.showInputDialog("请再次确认您的密码\n");
						if(password.equals(passwordConfirm))
						{
							String number = JOptionPane
									.showInputDialog("请输入您的手机号\n");
							if(judgeNumberExist(number))
							{
								JOptionPane.showMessageDialog(null ,
										"该手机号已经注册，可以通过找回账号找到您的账号！！！");
								return;
							}
							if(number.trim().isEmpty())
							{
								JOptionPane.showMessageDialog(null ,
										"手机号不能为空，请重新注册！！！");
								dispose();
								return;
							}
							String realname = JOptionPane
									.showInputDialog("请输入您的真实姓名\n");
							if(realname.trim().isEmpty())
							{
								JOptionPane.showMessageDialog(null ,
										"真实姓名不能为空，请重新注册！！！");
								dispose();
								return;
							}
//							int a = st.executeUpdate("insert into user_information(user_name,user_password,user_income) values('xiaoming','mima',0)");//插入
//							int a = st.executeUpdate("delete from user_information where user_id = 1");//删除
//							int a = st.executeUpdate("update user_information set user_name = 'xiaoming' , user_income = 100 where user_id = 1");//更改
//							if(statement.executeUpdate("insert into test(name , password , number)" + "values('" + username + "','" + passwordConfirm + "',0)") == 1)
							Statement statement = new ConnectDatabase()
									.getStatement();
							if(statement
									.executeUpdate("insert into admin(name , password , number , realname)"
											+ "values('"
											+ username
											+ "','"
											+ passwordConfirm
											+ "','"
											+ number
											+ "','" + realname + "')") == 1)
							{
								JOptionPane.showMessageDialog(null ,
										"恭喜您的账号注册成功！！！\n");
								JOptionPane.showMessageDialog(null ,
										"\n请牢记您的信息：\n" + "账号：" + username
												+ "\n密码：" + password + "\n手机号："
												+ number + "\n真实姓名：" + realname
												+ "\n");
							}
						}
						else
							JOptionPane.showMessageDialog(null ,
									"密码不匹配，请重新注册！！！");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null , "账号不能为空，请重新注册！！！");
					dispose();
				}
			}
			catch(Exception e1)
			{
				JOptionPane.showMessageDialog(null ,
						"账号可以是汉字、但密码不能为汉字，也不能为空，注册失败！！！请重新注册！！！");
				String str = "\n ActionRegister类：查找数据库异常！！！\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
				dispose();
			}
			
		}
		
	}
	
	/**
	 * 修改密码 
	 */
	class ActionModifyPassword implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("请输入您的账号：\n");
			try
			{
				if(username.trim().isEmpty())
				{
					JOptionPane.showMessageDialog(null , "账号不能为空，请重新输入！！！");
					return;
				}
			}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog(null , "账号不能为空，请重新输入！！！");
			}
			String number = JOptionPane.showInputDialog("请输入您的手机号：\n");
			try
			{
				if( ! findUserName(username , number).trim().isEmpty())
				{
					String password = JOptionPane
							.showInputDialog("请输入您新的密码：\n");
					if(password.trim().isEmpty())
					{
						JOptionPane.showMessageDialog(null ,
								"密码不能为空，请重新修改密码！！！");
						return;
					}
					String passwordConfirm = JOptionPane
							.showInputDialog("请确认您的新密码：\n");
					if(password.equals(passwordConfirm))
					{
						Statement statement = new ConnectDatabase()
								.getStatement();
						int a = statement
								.executeUpdate("update admin set password = '"
										+ password
										+ "' where id = '"
										+ Integer.parseInt(findUserName(
												username , number)) + " '");
						if(a == 1)
						{
							JOptionPane.showMessageDialog(null ,
									"您的新密码已修改，请牢记！！！");
							JOptionPane.showMessageDialog(null , "您的新密码是："
									+ password);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null , "密码不一致，请重新修改！！！");
					}
				}
				else
					JOptionPane.showMessageDialog(null ,
							"您的账号或者手机号输入有误或者账号不存在！！！");
			}
			catch(Exception e1)
			{
				String str = "\n ActionModifyPassword类：查找数据库异常！！！\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * 忘记密码
	 */
	class ActionForgetPassword implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("请输入您的账号：\n");
			try
			{
				if(username.trim().isEmpty())
				{
					JOptionPane.showMessageDialog(null , "账号不能为空，请重新输入！！！");
					return;
				}
			}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog(null , "账号不能为空，请重新输入！！！");
			}
			String number = JOptionPane.showInputDialog("请输入您的手机号：\n");
			try
			{
				if(findPassword(username , number) != "")
					JOptionPane.showMessageDialog(null , "您的密码是："
							+ findPassword(username , number));
				else
					JOptionPane.showMessageDialog(null ,
							"您的账号或者手机号输入有误或者账号不存在！！！");
			}
			catch(Exception e1)
			{
				String str = "\n ActionForgetPassword类：查找数据库异常！！！\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * 忘记用户名 
	 */
	class ActionForgetUserName implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String number = JOptionPane.showInputDialog("请输入您的手机号：\n");
			String realname = JOptionPane.showInputDialog("请输入您注册账户时的真实姓名：\n");
			try
			{
				if(findUsername(number , realname) != "")
					JOptionPane.showMessageDialog(null , "您的账号是："
							+ findUsername(number , realname));
				else
					JOptionPane.showMessageDialog(null ,
							"您的姓名或者手机号输入有误或者账号不存在！！！");
			}
			catch(Exception e1)
			{
				String str = "\n ActionForgetUserName类：查找数据库异常！！！\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * 关于 
	 */
	class ActionAbout implements ActionListener
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{
			Help helpDialog = new Help("homeScreenHelp");
			helpDialog.show();
		}
	}
	
	/**
	 * 游客登录
	 */
	class ActionVisitorLogin implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
//			dispose();
//			new ChatServer("游客");
			JOptionPane.showMessageDialog(null , "想得美，回去注册吧^..^");
		}
	}
	
	/**
	 * 通过给定的文件名获得图像
	 */
	Image getImage(String filename)
	{
		URLClassLoader urlLoader = (URLClassLoader) this.getClass()
				.getClassLoader();
		URL url = null;
		Image image = null;
		url = urlLoader.findResource(filename);
		image = Toolkit.getDefaultToolkit().getImage(url);
		MediaTracker mediatracker = new MediaTracker(this);
		try
		{
			mediatracker.addImage(image , 0);
			mediatracker.waitForID(0);
		}
		catch(InterruptedException _ex)
		{
			image = null;
			String str = "\n getImage类：线程异常！！！\n";
			str += _ex.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		if(mediatracker.isErrorID(0))
		{
			image = null;
		}
		
		return image;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	public static void main(String [] args)
	{
		new HomeScreen();
	}
}
