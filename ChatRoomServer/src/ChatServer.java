import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

/**
 * 
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2016年4月19日
 * @time             下午4:34:35
 * @project_name     ChatRoomSystemServer
 * @package_name     
 * @file_name        ChatServer.java
 * @type_name        ChatServer
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

/*
 * 聊天服务端的主框架类
 */
@SuppressWarnings("serial")
public class ChatServer extends JFrame implements ActionListener
{
	public static int port = 8888;// 服务端的侦听端口
	ServerSocket serverSocket;// 服务端Socket
	Image icon;// 程序图标
	String userName = null;
	
	@SuppressWarnings("rawtypes")
	JComboBox serverReceiverComboBox;// 选择发送消息的接受者
	JTextArea serverMessageShow;// 服务端的信息显示
	JScrollPane serverMessageScrollPane;// 信息显示的滚动条
	JTextField showUserStatus;// 显示用户连接状态
	JLabel sendToLabel , sendMessageLabel;
	JTextField systemMessage;// 服务端消息的发送
	JButton systemMessageButton;// 服务端消息的发送按钮
	UserLinkList userLinkList;// 用户链表
	
	// 建立菜单栏
	JMenuBar jMenuBar = new JMenuBar();
	// 建立菜单组
	JMenu serviceMenu = new JMenu("服务(V)");
	// 建立菜单项
	JMenuItem portConfigItem = new JMenuItem("端口设置(P)");
	JMenuItem startServerItem = new JMenuItem("启动服务(S)");
	JMenuItem stopServerItem = new JMenuItem("停止服务(T)");
	JMenuItem exitItem = new JMenuItem("退出(X)");
	
	JMenu helpMenu = new JMenu("帮助(H)");
	JMenuItem helpItem = new JMenuItem("帮助(H)");
	
	// 建立工具栏
	JToolBar toolBar = new JToolBar();
	
	// 建立工具栏中的按钮组件
	JButton portSetButton;// 启动服务端侦听
	JButton startServerButton;// 启动服务端侦听
	JButton stopServerButton;// 关闭服务端侦听
	JButton exitButton;// 退出按钮
	
	// 框架的大小
	Dimension faceSize = new Dimension(400 , 647);
	
	ServerListen listenThread;
	
	JPanel downPanel;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
	
	/**
	 * 服务端构造函数
	 */
	@SuppressWarnings("deprecation")
	public ChatServer(String userName)
	{
		this.userName = userName;
		init();// 初始化程序
		// 添加框架的关闭事件处理
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		// 设置框架的大小
		this.setSize(faceSize);
		
		// 设置运行时窗口的位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - faceSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - faceSize.getHeight() ) / 2);
		this.setResizable(false);
		
		this.setTitle("聊天室服务端 " + userName); // 设置标题
		
		// 程序图标
		icon = getImage("icon.gif");
		this.setIconImage(icon); // 设置程序图标
		show();
		
		// 为服务菜单栏设置热键'V'
		serviceMenu.setMnemonic('V');
		
		// 为端口设置快捷键为ctrl+p
		portConfigItem.setMnemonic('P');
		portConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P ,
				InputEvent.CTRL_MASK));
		
		// 为启动服务快捷键为ctrl+s
		startServerItem.setMnemonic('S');
		startServerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S ,
				InputEvent.CTRL_MASK));
		
		// 为端口设置快捷键为ctrl+T
		stopServerItem.setMnemonic('T');
		stopServerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T ,
				InputEvent.CTRL_MASK));
		
		// 为退出设置快捷键为ctrl+x
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X ,
				InputEvent.CTRL_MASK));
		
		// 为帮助菜单栏设置热键'H'
		helpMenu.setMnemonic('H');
		
		// 为帮助设置快捷键为ctrl+p
		helpItem.setMnemonic('H');
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H ,
				InputEvent.CTRL_MASK));
	}
	
	/**
	 * 程序初始化函数
	 */
	@SuppressWarnings(
	{ "unchecked" , "rawtypes", "deprecation" })
	public void init()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		// 添加菜单栏
		serviceMenu.add(portConfigItem);
		serviceMenu.add(startServerItem);
		serviceMenu.add(stopServerItem);
		serviceMenu.add(exitItem);
		jMenuBar.add(serviceMenu);
		helpMenu.add(helpItem);
		jMenuBar.add(helpMenu);
		setJMenuBar(jMenuBar);
		
		// 初始化按钮
		portSetButton = new JButton("端口设置");
		startServerButton = new JButton("启动服务");
		stopServerButton = new JButton("停止服务");
		exitButton = new JButton("退出");
		// 将按钮添加到工具栏
		toolBar.add(portSetButton);
		toolBar.addSeparator();// 添加分隔栏
		toolBar.add(startServerButton);
		toolBar.add(stopServerButton);
		toolBar.addSeparator();// 添加分隔栏
		toolBar.add(exitButton);
		contentPane.add(toolBar , BorderLayout.NORTH);
		
		// 初始时，令停止服务按钮不可用
		stopServerButton.setEnabled(false);
		stopServerItem.setEnabled(false);
		
		// 为菜单栏添加事件监听
		portConfigItem.addActionListener(this);
		startServerItem.addActionListener(this);
		stopServerItem.addActionListener(this);
		exitItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		// 添加按钮的事件侦听
		portSetButton.addActionListener(this);
		startServerButton.addActionListener(this);
		stopServerButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		serverReceiverComboBox = new JComboBox();
		serverReceiverComboBox.insertItemAt("所有人" , 0);
		serverReceiverComboBox.setSelectedIndex(0);
		
		serverMessageShow = new JTextArea();
		serverMessageShow.setEditable(false);
		// 添加滚动条
		serverMessageScrollPane = new JScrollPane(serverMessageShow ,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		serverMessageScrollPane.setPreferredSize(new Dimension(400 , 400));
		serverMessageScrollPane.revalidate();
		
		showUserStatus = new JTextField(35);
		showUserStatus.setEditable(false);
		
		systemMessage = new JTextField(24);
		systemMessage.setEnabled(false);
		systemMessageButton = new JButton();
		systemMessageButton.setText("发送");
		
		// 添加系统消息的事件侦听
		systemMessage.addActionListener(this);
		systemMessageButton.addActionListener(this);
		
		sendToLabel = new JLabel("发送至:");
		sendMessageLabel = new JLabel("发送消息:");
		downPanel = new JPanel();
		girdBag = new GridBagLayout();
		downPanel.setLayout(girdBag);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 0;
		girdBagCon.gridwidth = 3;
		girdBagCon.gridheight = 2;
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		JLabel none = new JLabel("    ");
		girdBag.setConstraints(none , girdBagCon);
		downPanel.add(none);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 2;
		girdBagCon.insets = new Insets(1 , 0 , 0 , 0);
		girdBagCon.ipadx = 5;
		girdBagCon.ipady = 5;
		girdBag.setConstraints(sendToLabel , girdBagCon);
		downPanel.add(sendToLabel);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 2;
		girdBagCon.anchor = GridBagConstraints.LINE_START;
		girdBag.setConstraints(serverReceiverComboBox , girdBagCon);
		downPanel.add(serverReceiverComboBox);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(sendMessageLabel , girdBagCon);
		downPanel.add(sendMessageLabel);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 1;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(systemMessage , girdBagCon);
		downPanel.add(systemMessage);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 2;
		girdBagCon.gridy = 3;
		girdBag.setConstraints(systemMessageButton , girdBagCon);
		downPanel.add(systemMessageButton);
		
		girdBagCon = new GridBagConstraints();
		girdBagCon.gridx = 0;
		girdBagCon.gridy = 4;
		girdBagCon.gridwidth = 3;
		girdBag.setConstraints(showUserStatus , girdBagCon);
		downPanel.add(showUserStatus);
		
		contentPane.add(serverMessageScrollPane , BorderLayout.CENTER);
		contentPane.add(downPanel , BorderLayout.SOUTH);
		systemMessageButton.setEnabled(false);
		// 关闭程序时的操作
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				stopService();
				String str = "\n ChatServer类：服务器关闭！！！\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
				System.exit(0);
			}
		});
		
		PortConf portConf = new PortConf(this);
		portConf.show();
	}
	
	/**
	 * 事件处理
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		if(obj == startServerButton || obj == startServerItem)
		{ // 启动服务端
			startService();
			String str = "\n ChatServer类：服务器启动！！！\n";
			new PrintLog("serverLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == stopServerButton || obj == stopServerItem)
		{ // 停止服务端
			int j = JOptionPane.showConfirmDialog(this , "真的停止服务吗?" , "停止服务" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				stopService();
				String str = "\n ChatServer类：服务器关闭！！！\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
			}
		}
		else if(obj == portSetButton || obj == portConfigItem)
		{ // 端口设置
			// 调出端口设置的对话框
			PortConf portConf = new PortConf(this);
			portConf.show();
		}
		else if(obj == exitButton || obj == exitItem)
		{ // 退出程序
			int j = JOptionPane.showConfirmDialog(this , "真的要退出吗?" , "退出" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				stopService();
				String str = "\n ChatServer类：服务器退出！！！\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
				System.exit(0);
			}
		}
		else if(obj == helpItem)
		{ // 菜单栏中的帮助
			// 调出帮助对话框
			Help helpDialog = new Help("serverMenuHelp");
			helpDialog.show();
		}
		else if(obj == systemMessage || obj == systemMessageButton)
		{ // 发送系统消息
			sendSystemMessage();
		}
	}
	
	/**
	 * 启动服务端
	 */
	public void startService()
	{
		try
		{
			serverSocket = new ServerSocket(port , 10);
			serverMessageShow.append("服务端已经启动，在" + port + "端口侦听...\n");
			
			startServerButton.setEnabled(false);
			startServerItem.setEnabled(false);
			portSetButton.setEnabled(false);
			portConfigItem.setEnabled(false);
			
			stopServerButton.setEnabled(true);
			stopServerItem.setEnabled(true);
			systemMessage.setEnabled(true);
			systemMessageButton.setEnabled(true);
		}
		catch(Exception e)
		{
			String str = "\n ChatServer类：服务器异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		userLinkList = new UserLinkList();
		
		listenThread = new ServerListen(serverSocket , serverReceiverComboBox ,
				serverMessageShow , showUserStatus , userLinkList);
		listenThread.start();
	}
	
	/**
	 * 关闭服务端
	 */
	@SuppressWarnings("unchecked")
	public void stopService()
	{
		try
		{
			// 向所有人发送服务器关闭的消息
			sendStopToAll();
			listenThread.isStop = true;
			serverSocket.close();
			
			int count = userLinkList.getCount();
			
			int i = 0;
			while(i < count)
			{
				Node node = userLinkList.findUser(i);
				
				node.input.close();
				node.output.close();
				node.socket.close();
				
				i ++ ;
			}
			
			stopServerButton.setEnabled(false);
			stopServerItem.setEnabled(false);
			startServerButton.setEnabled(true);
			startServerItem.setEnabled(true);
			portSetButton.setEnabled(true);
			portConfigItem.setEnabled(true);
			systemMessage.setEnabled(false);
			systemMessageButton.setEnabled(false);
			serverMessageShow.append("服务端已经关闭\n");
			
			serverReceiverComboBox.removeAllItems();
			serverReceiverComboBox.addItem("所有人");
		}
		catch(Exception e)
		{
			String str = "\n ChatServer类：服务器异常1！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
	}
	
	/**
	 * 向所有人发送服务器关闭的消息
	 */
	public void sendStopToAll()
	{
		int count = userLinkList.getCount();
		
		int i = 0;
		while(i < count)
		{
			Node node = userLinkList.findUser(i);
			if(node == null)
			{
				i ++ ;
				continue;
			}
			
			try
			{
				node.output.writeObject("服务关闭");
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer类：服务器异常2！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			
			i ++ ;
		}
	}
	
	/**
	 * 向所有人发送消息
	 */
	public void sendMsgToAll(String msg)
	{
		int count = userLinkList.getCount();// 用户总数
		
		int i = 0;
		while(i < count)
		{
			Node node = userLinkList.findUser(i);
			if(node == null)
			{
				i ++ ;
				continue;
			}
			
			try
			{
				node.output.writeObject("系统信息");
				node.output.flush();
				node.output.writeObject(msg);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer类：服务器异常3！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			
			i ++ ;
		}
		
		systemMessage.setText("");
	}
	
	/**
	 * 向客户端用户发送消息
	 */
	public void sendSystemMessage()
	{
		String toSomebody = serverReceiverComboBox.getSelectedItem().toString();
		String message = systemMessage.getText() + "\n";
		
		serverMessageShow.append(message);
		
		// 向所有人发送消息
		if(toSomebody.equalsIgnoreCase("所有人"))
		{
			sendMsgToAll(message);
		}
		else
		{
			// 向某个用户发送消息
			Node node = userLinkList.findUser(toSomebody);
			
			try
			{
				node.output.writeObject("系统信息");
				node.output.flush();
				node.output.writeObject(message);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer类：服务器异常4！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			systemMessage.setText("");// 将发送消息栏的消息清空
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
			String str = "\n ChatServer类：服务器异常5！！！\n";
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
	
//	@SuppressWarnings("unused")
//	public static void main(String [] args)
//	{
//		ChatServer app = new ChatServer();
//	}
}
