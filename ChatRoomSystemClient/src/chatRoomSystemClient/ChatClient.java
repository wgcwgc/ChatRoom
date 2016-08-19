package chatRoomSystemClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * 
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2016年4月19日
 * @time             下午4:35:08
 * @project_name     ChatRoomSystemClient
 * @package_name     
 * @file_name        ChatClient.java
 * @type_name        ChatClient
 * @enclosing_type   
 * @tags             
 * @todo             
 * @others           
 *
 */

/*
 * 聊天客户端的主框架类
 */
@SuppressWarnings("serial")
public class ChatClient extends JFrame implements ActionListener
{
	String ip = "127.0.0.1";// 连接到服务端的ip地址
	int serverPort = 8888;// 连接到服务端的端口号
	String defaultUserName = "王光春";// 用户名
	int connectionStatus = 0;// 0表示未连接，1表示已连接
	String userName = null;
	Image icon;// 程序图标
	@SuppressWarnings("rawtypes")
	JComboBox clientReceiverComboBox;// 选择发送消息的接受者
	JTextArea clientMessageShow;// 客户端的信息显示
	JScrollPane clientMessageScrollPane;// 信息显示的滚动条
	
	JLabel sendToLabel , emojiLabel , sendMessageLabel;
	
	JTextField clientMessage;// 客户端消息的发送
	JCheckBox checkBox;// 私信
	@SuppressWarnings("rawtypes")
	JComboBox emojiList;// 表情选择
	JButton clientMessageButton;// 发送消息
	JTextField showUserStatus;// 显示用户连接状态
	
	Socket socket;
	ObjectOutputStream output;// 网络套接字输出流
	ObjectInputStream input;// 网络套接字输入流
	
	ClientReceive clientReceiveThread;
	
	// 建立菜单栏
	JMenuBar jMenuBar = new JMenuBar();
	// 建立菜单组
	JMenu operateMenu = new JMenu("操作(O)");
	// 建立菜单项
	JMenuItem loginItem = new JMenuItem("用户登录(I)");
	JMenuItem logoutItem = new JMenuItem("用户注销(L)");
	JMenuItem exitItem = new JMenuItem("退出(X)");
	
	JMenu setMenu = new JMenu("设置(C)");
	JMenuItem userItem = new JMenuItem("用户设置(U)");
	JMenuItem connectItem = new JMenuItem("连接设置(C)");
	
	JMenu helpMenu = new JMenu("帮助(H)");
	JMenuItem helpItem = new JMenuItem("帮助(H)");
	
	// 建立工具栏
	JToolBar toolBar = new JToolBar();
	// 建立工具栏中的按钮组件
	JButton loginButton;// 用户登录
	JButton logoutButton;// 用户注销
	JButton userSetButton;// 用户信息的设置
	JButton connectSetButton;// 连接设置
	JButton exitButton;// 退出按钮
	
	// 框架的大小
	Dimension frameSize = new Dimension(400 , 647);
	
	JPanel downPanel;
	GridBagLayout gridBag;// 布局管理器
	GridBagConstraints gridBagCon;// 对组件的约束
	
	public ChatClient()
	{
		
	}
	
	@SuppressWarnings("deprecation")
	public ChatClient(String userName)
	{
		this.userName = userName;
		init();// 初始化程序
		// 添加框架的关闭事件处理
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		// 设置框架的大小
		this.setSize(frameSize);
		
		// 设置运行时窗口的位置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - frameSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - frameSize.getHeight() ) / 2);
		this.setResizable(false);// 调整窗口大小
		this.setTitle("聊天室客户端   " + userName); // 设置标题
		
		// 程序图标
//		String packagePath = this.getClass().getResource("").getPath().toString();
//		System.out.println(packagePath + "\n*******************");
//		packagePath = packagePath.substring(1 , packagePath.indexOf("bin") - 1);
//		System.out.println(packagePath + "\n###################");
//		String image = packagePath + "/image/icon.gif";
//		System.out.println(image + "\n###################");
		icon = getImage("icon.gif");
//		icon = getImage(image);
		this.setIconImage(icon); // 设置程序图标
		show();
		
		// 为操作菜单栏设置热键'O' alt+O
		operateMenu.setMnemonic('O');
		
		// 为用户登录设置快捷键为ctrl+i
		loginItem.setMnemonic('I');
		loginItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I ,
				InputEvent.CTRL_MASK));
		
		// 为用户注销快捷键为ctrl+l
		logoutItem.setMnemonic('L');
		logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L ,
				InputEvent.CTRL_MASK));
		
		// 为退出快捷键为ctrl+x
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X ,
				InputEvent.CTRL_MASK));
		
		// 为设置菜单栏设置热键'C' alt+c
		setMenu.setMnemonic('C');
		
		// 为用户设置设置快捷键为ctrl+u
		userItem.setMnemonic('U');
		userItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U ,
				InputEvent.CTRL_MASK));
		
		// 为连接设置设置快捷键为ctrl+c
		connectItem.setMnemonic('C');
		connectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C ,
				InputEvent.CTRL_MASK));
		
		// 为帮助菜单栏设置热键'H'
		helpMenu.setMnemonic('H');
		
		// 为帮助设置快捷键为ctrl+h
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
		Container contentPane = getContentPane();// Container容器是JFrame的祖先类 ，
													// 此处是初始化一个容器
		contentPane.setLayout(new BorderLayout());
		
		// 添加菜单栏
		operateMenu.add(loginItem);
		operateMenu.add(logoutItem);
		operateMenu.add(exitItem);
		jMenuBar.add(operateMenu);
		setMenu.add(userItem);
		setMenu.add(connectItem);
		jMenuBar.add(setMenu);
		helpMenu.add(helpItem);
		jMenuBar.add(helpMenu);
		setJMenuBar(jMenuBar);
		// 为菜单栏添加事件监听
		loginItem.addActionListener(this);
		logoutItem.addActionListener(this);
		exitItem.addActionListener(this);
		userItem.addActionListener(this);
		connectItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		// 初始化按钮
		loginButton = new JButton("登录");
		logoutButton = new JButton("注销");
		userSetButton = new JButton("用户设置");
		connectSetButton = new JButton("连接设置");
		exitButton = new JButton("退出");
		// 当鼠标放上会显示信息
		loginButton.setToolTipText("连接到指定的服务器");
		logoutButton.setToolTipText("与服务器断开连接");
		userSetButton.setToolTipText("设置用户信息");
		connectSetButton.setToolTipText("设置所要连接到的服务器信息");
		// 将按钮添加到工具栏
		toolBar.add(userSetButton);
		toolBar.add(connectSetButton);
		toolBar.addSeparator();// 添加分隔栏
		toolBar.add(loginButton);
		toolBar.add(logoutButton);
		toolBar.addSeparator();// 添加分隔栏
		toolBar.add(exitButton);
		contentPane.add(toolBar , BorderLayout.NORTH);
		
		// 初始时
		loginButton.setEnabled(true);
		logoutButton.setEnabled(false);
		logoutItem.setEnabled(false);
		// 添加按钮的事件侦听
		loginButton.addActionListener(this);
		logoutButton.addActionListener(this);
		userSetButton.addActionListener(this);
		connectSetButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		clientMessageShow = new JTextArea();
		clientMessageShow.setEditable(false);
		// 添加滚动条
		clientMessageScrollPane = new JScrollPane(clientMessageShow ,
		/* 垂直滚动条，需要的时候出现20 */
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,
		/* 水平滚动条，需要的时候出现30 */
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		clientMessageScrollPane.setPreferredSize(new Dimension(400 , 400));
		clientMessageScrollPane.revalidate();
		
		sendToLabel = new JLabel("发送至:");
		emojiLabel = new JLabel("         表情:   ");
		sendMessageLabel = new JLabel("发送消息:");
		
		clientReceiverComboBox = new JComboBox();
		clientReceiverComboBox.insertItemAt("所有人" , 0);
		clientReceiverComboBox.setSelectedIndex(0);
		
		emojiList = new JComboBox();
		emojiList.addItem(" ");
		emojiList.addItem("微笑地");
		emojiList.addItem("高兴地");
		emojiList.addItem("无聊地");
		emojiList.addItem("生气地");
		emojiList.addItem("小心地");
		emojiList.addItem("静静地");
		emojiList.setSelectedIndex(0);
		
		checkBox = new JCheckBox("陌生人模式");
		checkBox.setSelected(false);
		
		clientMessage = new JTextField(22);
		clientMessage.setEnabled(false);
		
		clientMessageButton = new JButton();
		clientMessageButton.setText("发送");
		
		// 添加系统消息的事件侦听
		clientMessage.addActionListener(this);
		clientMessageButton.addActionListener(this);
		
		downPanel = new JPanel();
		gridBag = new GridBagLayout();
		downPanel.setLayout(gridBag);
		
//		gridBagCon = new GridBagConstraints();//单元格布局
//		gridBagCon.gridx = 0;//单元格的横坐标
//		gridBagCon.gridy = 0;//单元格的纵坐标
//		gridBagCon.gridwidth = 5//单元格横向跨度个数
//		gridBagCon.gridheight = 2;//单元格纵向跨度个数
//		gridBagCon.ipadx = 5;//将单元格内的组件的最小尺寸横向
//		gridBagCon.ipady = 5;//或纵向扩大
//		JLabel none = new JLabel("    ");
//		gridBag.setConstraints(none , gridBagCon);
//		downPanel.add(none);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 0;
		gridBagCon.gridy = 2;
		gridBagCon.insets = new Insets(1 , 0 , 0 , 0);// 单元格间距
//		gridBagCon.ipadx = 5;
//		gridBagCon.ipady = 5;
		gridBag.setConstraints(sendToLabel , gridBagCon);
		downPanel.add(sendToLabel);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 1;
		gridBagCon.gridy = 2;
		gridBagCon.anchor = GridBagConstraints.LINE_START;// 对齐方式
		gridBag.setConstraints(clientReceiverComboBox , gridBagCon);
		downPanel.add(clientReceiverComboBox);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 2;
		gridBagCon.gridy = 2;
		gridBagCon.anchor = GridBagConstraints.LINE_END;
		gridBag.setConstraints(emojiLabel , gridBagCon);
		downPanel.add(emojiLabel);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 3;
		gridBagCon.gridy = 2;
		gridBagCon.anchor = GridBagConstraints.LINE_START;
		gridBagCon.insets = new Insets(1 , 0 , 0 , 0);
//		gridBagCon.ipadx = 5;
//		gridBagCon.ipady = 5;
		gridBag.setConstraints(emojiList , gridBagCon);
		downPanel.add(emojiList);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 4;
		gridBagCon.gridy = 2;
		gridBagCon.insets = new Insets(1 , 0 , 0 , 0);
		// girdBagCon.ipadx = 5;
		// girdBagCon.ipady = 5;
		gridBag.setConstraints(checkBox , gridBagCon);
		downPanel.add(checkBox);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 0;
		gridBagCon.gridy = 3;
		gridBag.setConstraints(sendMessageLabel , gridBagCon);
		downPanel.add(sendMessageLabel);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 1;
		gridBagCon.gridy = 3;
		gridBagCon.gridwidth = 3;
		gridBagCon.gridheight = 1;
		gridBag.setConstraints(clientMessage , gridBagCon);
		downPanel.add(clientMessage);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 4;
		gridBagCon.gridy = 3;
		gridBag.setConstraints(clientMessageButton , gridBagCon);
		downPanel.add(clientMessageButton);
		
		showUserStatus = new JTextField(35);
		showUserStatus.setEditable(false);
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 0;
		gridBagCon.gridy = 5;
		gridBagCon.gridwidth = 5;
		gridBag.setConstraints(showUserStatus , gridBagCon);
		downPanel.add(showUserStatus);
		
		contentPane.add(clientMessageScrollPane , BorderLayout.CENTER);
		contentPane.add(downPanel , BorderLayout.SOUTH);
		clientMessageButton.setEnabled(false);
		
		// 关闭程序时的操作
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(connectionStatus == 1)
				{
					DisConnect();
					String str = "\n ChatClient类：服务器关闭！！！\n";
					new PrintLog("clientLog.log" , userName + str);
//					System.out.println(str);
				}
				System.exit(0);
			}
		});
		
		UserConfig userConf = new UserConfig(this , defaultUserName);
		userConf.show();
		defaultUserName = userConf.userInputName;
	}
	
	/**
	 * 事件处理
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		
		if(obj == userItem || obj == userSetButton)
		{ // 用户信息设置
			// 调出用户信息设置对话框
			UserConfig userConf = new UserConfig(this , defaultUserName);
			userConf.show();
			defaultUserName = userConf.userInputName;
		}
		else if(obj == connectItem || obj == connectSetButton)
		{ // 连接服务端设置
			// 调出连接设置对话框
			ConnectConfig conConfig = new ConnectConfig(this , ip , serverPort);
			conConfig.show();
			ip = conConfig.userInputIp;
			serverPort = conConfig.userInputPort;
		}
		else if(obj == loginItem || obj == loginButton)
		{ // 登录
			Connect();
			String str = "\n ChatClient类：用户登录上线！！！\n";
			new PrintLog("clientLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == logoutItem || obj == logoutButton)
		{ // 注销
			DisConnect();
			showUserStatus.setText("");
			String str = "\n ChatClient类：用户注销下线！！！\n";
			new PrintLog("clientLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == clientMessage || obj == clientMessageButton)
		{ // 发送消息
			SendMessage();
			clientMessage.setText("");
		}
		else if(obj == exitButton || obj == exitItem)
		{ // 退出
			int j = JOptionPane.showConfirmDialog(this , "真的要退出吗?" , "退出" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				if(connectionStatus == 1)
				{
					DisConnect();
					String str = "\n ChatClient类：用户退出下线！！！\n";
					new PrintLog("clientLog.log" , userName + str);
//					System.out.println(str);
				}
				System.exit(0);
			}
		}
		else if(obj == helpItem)
		{
			Help helpDialog = new Help("clientMenuHelp");
			helpDialog.show();
		}
	}
	
	public void Connect()
	{
		try
		{
			socket = new Socket(ip , serverPort);
		}
		catch(Exception e)
		{
			JOptionPane.showConfirmDialog(this , "不能连接到指定的服务器。\n请确认连接设置是否正确。" ,
					"提示" , JOptionPane.DEFAULT_OPTION ,
					JOptionPane.WARNING_MESSAGE);
			String str = "\n ChatClient类：连接服务器异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
			return;
		}
		try
		{
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());
			
			output.writeObject(defaultUserName);
			output.flush();
			
			clientReceiveThread = new ClientReceive(socket , output , input ,
					clientReceiverComboBox , clientMessageShow , showUserStatus);
			clientReceiveThread.start();
			
			loginButton.setEnabled(false);
			loginItem.setEnabled(false);
			userSetButton.setEnabled(false);
			userItem.setEnabled(false);
			connectSetButton.setEnabled(false);
			connectItem.setEnabled(false);
			logoutButton.setEnabled(true);
			logoutItem.setEnabled(true);
			clientMessage.setEnabled(true);
			clientMessageButton.setEnabled(true);
			clientMessageShow.append("连接房间号： " + ip + "    密码：" + serverPort
					+ " 成功... ...\n");
			connectionStatus = 1;// 标志位设为已连接
			
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig类：服务器数据发送异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
			return;
		}
	}
	
	public void DisConnect()
	{
		loginButton.setEnabled(true);
		loginItem.setEnabled(true);
		userSetButton.setEnabled(true);
		userItem.setEnabled(true);
		connectSetButton.setEnabled(true);
		connectItem.setEnabled(true);
		logoutButton.setEnabled(false);
		logoutItem.setEnabled(false);
		clientMessage.setEnabled(false);
		clientMessageButton.setEnabled(false);
		if(socket.isClosed())
		{
			return;
		}
		
		try
		{
			output.writeObject("用户下线");
			output.flush();
			
			input.close();
			output.close();
			socket.close();
			clientMessageShow.append("已经与服务器断开连接... ...\n");
			connectionStatus = 0;
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig类：客户端退出异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
	}
	
	public void SendMessage()
	{
		String toSomebody = clientReceiverComboBox.getSelectedItem().toString();
		String status = "";
		if(checkBox.isSelected())
		{
			status = "陌生人信息";
		}
		
		String emoji = emojiList.getSelectedItem().toString();
		String message = clientMessage.getText();
		
		if(socket.isClosed())
		{
			return;
		}
		
		try
		{
			output.writeObject("聊天信息");
			output.flush();
			output.writeObject(toSomebody);
			output.flush();
			output.writeObject(status);
			output.flush();
			output.writeObject(emoji);
			output.flush();
			output.writeObject(message);
			output.flush();
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig类：信息发送异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
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
			String str = "\n ConnectConfig类：配置文件读取异常！！！\n";
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
	
//	public static void main(String [] args)
//	{
//		new ChatClient();
//	}
}
