package chatRoomSystemServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 帮助
 */
@SuppressWarnings(
{ "serial" })
public class Help extends JDialog
{
	JPanel titlePanel = new JPanel();
	JPanel contentPanel = new JPanel();
	JPanel closePanel = new JPanel();
	
	JButton close = new JButton();
	JTextArea help = new JTextArea();
	
	Color bg = new Color(255 , 255 , 255);
	
	public Help(String flog)
	{
		super();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - 400 ) / 2 + 25 ,
				(int) ( screenSize.height - 320 ) / 2);
		this.setResizable(false);
		if(flog.equals("serverMenuHelp"))
		{
			try
			{
				menuHelpInit();
			}
			catch(Exception e)
			{
				String str = "\n Help类：菜单展示异常！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		else if(flog.equals("homeScreenHelp"))
		{
			try
			{
				homeScreenHelp();
			}
			catch(Exception e)
			{
				String str = "\n Help类：菜单展示异常！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		
	}
	
	/**
	 * 主界面关于
	 */
	private void homeScreenHelp()
	{
		this.setSize(new Dimension(500 , 270));
		this.setTitle("关于");
		
		titlePanel.setBackground(bg);
		contentPanel.setBackground(bg);
		closePanel.setBackground(bg);
		
		help.setText("1、如果您有账号，输入正确的账号和密码，点击“登录”即可使用。\n"
				+ "2、如果您没有账号点击“注册”，  输入用户名、密码、手机号、真实姓名就可拥有新账号。\n"
				+ "      这里向您保证：您的个人信息不会泄漏给其他任何用户和个人，只用于软件维护。\n"
				+ "      另外：一个手机号只能注册一个账号。\n"
				+ "3、如果您要修改密码，可点击“修改密码”，通过输入注册时的账号和手机号即可修改。\n"
				+ "4、如果您忘记密码，可以点击“忘记密码”，输入注册时的账号和手机号即可找回。\n"
				+ "5、如果您忘记账号，可以点击“忘记账号”，输入注册时的手机号和真实姓名找回。\n"
				+ "6、如有其他任何问题，请联系本人邮箱：1348066599@qq.com\n" + "7、感谢您的使用与支持！！！\n");
		help.setEditable(false);
		
		titlePanel.add(new Label("              "));
		JLabel title = new JLabel("聊天室服务器端主界面");
		titlePanel.add(title);
		titlePanel.add(new Label("              "));
		
		contentPanel.add(help);
		
		closePanel.add(new Label("              "));
		closePanel.add(close);
		closePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel , BorderLayout.NORTH);
		contentPane.add(contentPanel , BorderLayout.CENTER);
		contentPane.add(closePanel , BorderLayout.SOUTH);
		
		close.setText("关闭");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
	
	/**
	 * 客户端菜单帮助
	 * @throws Exception
	 */
	private void menuHelpInit() throws Exception
	{
		this.setSize(new Dimension(300 , 220));
		this.setTitle("帮助");
		
		titlePanel.setBackground(bg);
		contentPanel.setBackground(bg);
		closePanel.setBackground(bg);
		help.setText("1、设置房间的密码（默认密码为8888）。\n" + "2、点击 启动服务 按钮便可在指定的房间号启动服务。\n"
				+ "3、选择需要接受消息的用户，在消息栏中写入消息，\n      点击“发送”即可发送消息。\n"
				+ "4、信息状态栏中显示房间当前的启动与停止状态、\n" + "      用户发送的消息和系统消息。");
		help.setEditable(false);
		
		titlePanel.add(new Label("              "));
		JLabel title = new JLabel("聊天室服务器端");
		titlePanel.add(title);
		titlePanel.add(new Label("              "));
		
		contentPanel.add(help);
		
		closePanel.add(new Label("              "));
		closePanel.add(close);
		closePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel , BorderLayout.NORTH);
		contentPane.add(contentPanel , BorderLayout.CENTER);
		contentPane.add(closePanel , BorderLayout.SOUTH);
		
		close.setText("关闭");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
}
