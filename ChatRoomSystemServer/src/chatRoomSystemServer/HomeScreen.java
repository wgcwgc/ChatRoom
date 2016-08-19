package chatRoomSystemServer;

/**
 * @author Administrator
 * @copyright wgcwgc
 * @date 2016��4��26��
 * @time ����10:30:27
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
	 * ���������
	 */
	public HomeScreen()
	{
		Dimension frameSize = new Dimension(250 , 350);
		setSize(frameSize);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) ( screenSize.width - frameSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - frameSize.getHeight() ) / 2);
		setTitle("������������ϵͳ");
		setResizable(false);
		setLayout(new GridLayout(12 , 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(new JLabel("              ������ϵͳ��ӭ��������"));
		
		JLabel userNameJLabel = new JLabel("�˺ţ�");
		add(userNameJLabel);
		userJTestField = new JTextField(15);
		add(userJTestField);
		
		JLabel passwordJLabel = new JLabel("���룺");
		add(passwordJLabel);
		passwordJPasswordField = new JPasswordField(15);
		add(passwordJPasswordField);
		
		JButton loginJButton = new JButton("��¼");
		add(loginJButton);
		loginJButton.addActionListener(new ActionLogin());
		
		JButton registerJButton = new JButton("ע��");
		add(registerJButton);
		registerJButton.addActionListener(new ActionRegister());
		
		JButton modifyPasswordJButton = new JButton("�޸�����");
		add(modifyPasswordJButton);
		modifyPasswordJButton.addActionListener(new ActionModifyPassword());
		
		JButton forgetPasswordJButton = new JButton("��������");
		add(forgetPasswordJButton);
		forgetPasswordJButton.addActionListener(new ActionForgetPassword());
		
		JButton forgetUserNameJButton = new JButton("�����˺�");
		add(forgetUserNameJButton);
		forgetUserNameJButton.addActionListener(new ActionForgetUserName());
		
		JButton visitorLoginJButton = new JButton("�ο͵�¼");
		add(visitorLoginJButton);
		visitorLoginJButton.addActionListener(new ActionVisitorLogin());
		
		JButton aboutJButton = new JButton("����");
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
	 * @see ��֤�˺ź�������ȷ
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
	 * @see ���ݿ����Ѿ��д��˺�
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
	 * @see ���ݿ����Ѿ��д��ֻ���
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
	 * @see �ж��˺������ݿ��� 
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
	 * @see �����˺��ҵ�id
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
	 * @see �ж�ĳ�˺��Ƿ������ݿ���
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
	 * @see �ж��˺��Ƿ������ݿ��� 
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
	 *  ��¼
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
					JOptionPane.showMessageDialog(null , "��ӭ " + userName
							+ " ������������.\n" + "ף�������죡����");
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
										"�˺������������3�Σ������ѱ�ϵͳ���ᣬ��24Сʱ���޸����룡����\n"
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
							String str = "\n ActionLogin�ࣺ�������ݿ��쳣������\n";
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
								"�˺Ż���������󣡣���\n���������룡����");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(userJTestField ,
							"�˺Ż���������󣡣���\n���������룡����");
				}
			}
			catch(Exception e1)
			{
				String str = "\n ActionLogin�ࣺ�������ݿ��쳣1������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		
	}
	
	/**
	 * �˺�ע��
	 */
	class ActionRegister implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("�����������˺ţ�\n");
			try
			{
				if( ! username.trim().isEmpty())
				{
					if(judgeUserExist(username))
						JOptionPane.showMessageDialog(null ,
								"�����˺��Ѿ����ڣ�����ֱ�ӵ�¼��\n����������룬���Ե�����������һأ�����");
					else
					{
						String password = JOptionPane
								.showInputDialog("��������������\n");
						if(password.trim().isEmpty())
						{
							JOptionPane.showMessageDialog(null ,
									"���벻��Ϊ�գ�������ע�ᣡ����");
							dispose();
							return;
						}
						String passwordConfirm = JOptionPane
								.showInputDialog("���ٴ�ȷ����������\n");
						if(password.equals(passwordConfirm))
						{
							String number = JOptionPane
									.showInputDialog("�����������ֻ���\n");
							if(judgeNumberExist(number))
							{
								JOptionPane.showMessageDialog(null ,
										"���ֻ����Ѿ�ע�ᣬ����ͨ���һ��˺��ҵ������˺ţ�����");
								return;
							}
							if(number.trim().isEmpty())
							{
								JOptionPane.showMessageDialog(null ,
										"�ֻ��Ų���Ϊ�գ�������ע�ᣡ����");
								dispose();
								return;
							}
							String realname = JOptionPane
									.showInputDialog("������������ʵ����\n");
							if(realname.trim().isEmpty())
							{
								JOptionPane.showMessageDialog(null ,
										"��ʵ��������Ϊ�գ�������ע�ᣡ����");
								dispose();
								return;
							}
//							int a = st.executeUpdate("insert into user_information(user_name,user_password,user_income) values('xiaoming','mima',0)");//����
//							int a = st.executeUpdate("delete from user_information where user_id = 1");//ɾ��
//							int a = st.executeUpdate("update user_information set user_name = 'xiaoming' , user_income = 100 where user_id = 1");//����
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
										"��ϲ�����˺�ע��ɹ�������\n");
								JOptionPane.showMessageDialog(null ,
										"\n���μ�������Ϣ��\n" + "�˺ţ�" + username
												+ "\n���룺" + password + "\n�ֻ��ţ�"
												+ number + "\n��ʵ������" + realname
												+ "\n");
							}
						}
						else
							JOptionPane.showMessageDialog(null ,
									"���벻ƥ�䣬������ע�ᣡ����");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null , "�˺Ų���Ϊ�գ�������ע�ᣡ����");
					dispose();
				}
			}
			catch(Exception e1)
			{
				JOptionPane.showMessageDialog(null ,
						"�˺ſ����Ǻ��֡������벻��Ϊ���֣�Ҳ����Ϊ�գ�ע��ʧ�ܣ�����������ע�ᣡ����");
				String str = "\n ActionRegister�ࣺ�������ݿ��쳣������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
				dispose();
			}
			
		}
		
	}
	
	/**
	 * �޸����� 
	 */
	class ActionModifyPassword implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("�����������˺ţ�\n");
			try
			{
				if(username.trim().isEmpty())
				{
					JOptionPane.showMessageDialog(null , "�˺Ų���Ϊ�գ����������룡����");
					return;
				}
			}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog(null , "�˺Ų���Ϊ�գ����������룡����");
			}
			String number = JOptionPane.showInputDialog("�����������ֻ��ţ�\n");
			try
			{
				if( ! findUserName(username , number).trim().isEmpty())
				{
					String password = JOptionPane
							.showInputDialog("���������µ����룺\n");
					if(password.trim().isEmpty())
					{
						JOptionPane.showMessageDialog(null ,
								"���벻��Ϊ�գ��������޸����룡����");
						return;
					}
					String passwordConfirm = JOptionPane
							.showInputDialog("��ȷ�����������룺\n");
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
									"�������������޸ģ����μǣ�����");
							JOptionPane.showMessageDialog(null , "�����������ǣ�"
									+ password);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null , "���벻һ�£��������޸ģ�����");
					}
				}
				else
					JOptionPane.showMessageDialog(null ,
							"�����˺Ż����ֻ���������������˺Ų����ڣ�����");
			}
			catch(Exception e1)
			{
				String str = "\n ActionModifyPassword�ࣺ�������ݿ��쳣������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * ��������
	 */
	class ActionForgetPassword implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String username = JOptionPane.showInputDialog("�����������˺ţ�\n");
			try
			{
				if(username.trim().isEmpty())
				{
					JOptionPane.showMessageDialog(null , "�˺Ų���Ϊ�գ����������룡����");
					return;
				}
			}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog(null , "�˺Ų���Ϊ�գ����������룡����");
			}
			String number = JOptionPane.showInputDialog("�����������ֻ��ţ�\n");
			try
			{
				if(findPassword(username , number) != "")
					JOptionPane.showMessageDialog(null , "���������ǣ�"
							+ findPassword(username , number));
				else
					JOptionPane.showMessageDialog(null ,
							"�����˺Ż����ֻ���������������˺Ų����ڣ�����");
			}
			catch(Exception e1)
			{
				String str = "\n ActionForgetPassword�ࣺ�������ݿ��쳣������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * �����û��� 
	 */
	class ActionForgetUserName implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String number = JOptionPane.showInputDialog("�����������ֻ��ţ�\n");
			String realname = JOptionPane.showInputDialog("��������ע���˻�ʱ����ʵ������\n");
			try
			{
				if(findUsername(number , realname) != "")
					JOptionPane.showMessageDialog(null , "�����˺��ǣ�"
							+ findUsername(number , realname));
				else
					JOptionPane.showMessageDialog(null ,
							"�������������ֻ���������������˺Ų����ڣ�����");
			}
			catch(Exception e1)
			{
				String str = "\n ActionForgetUserName�ࣺ�������ݿ��쳣������\n";
				str += e1.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/**
	 * ���� 
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
	 * �ο͵�¼
	 */
	class ActionVisitorLogin implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
//			dispose();
//			new ChatServer("�ο�");
			JOptionPane.showMessageDialog(null , "���������ȥע���^..^");
		}
	}
	
	/**
	 * ͨ���������ļ������ͼ��
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
			String str = "\n getImage�ࣺ�߳��쳣������\n";
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
