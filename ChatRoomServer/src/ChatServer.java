import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

/**
 * 
 * @author           Administrator
 * @copyright        wgcwgc
 * @date             2016��4��19��
 * @time             ����4:34:35
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
 * �������˵��������
 */
@SuppressWarnings("serial")
public class ChatServer extends JFrame implements ActionListener
{
	public static int port = 8888;// ����˵������˿�
	ServerSocket serverSocket;// �����Socket
	Image icon;// ����ͼ��
	String userName = null;
	
	@SuppressWarnings("rawtypes")
	JComboBox serverReceiverComboBox;// ѡ������Ϣ�Ľ�����
	JTextArea serverMessageShow;// ����˵���Ϣ��ʾ
	JScrollPane serverMessageScrollPane;// ��Ϣ��ʾ�Ĺ�����
	JTextField showUserStatus;// ��ʾ�û�����״̬
	JLabel sendToLabel , sendMessageLabel;
	JTextField systemMessage;// �������Ϣ�ķ���
	JButton systemMessageButton;// �������Ϣ�ķ��Ͱ�ť
	UserLinkList userLinkList;// �û�����
	
	// �����˵���
	JMenuBar jMenuBar = new JMenuBar();
	// �����˵���
	JMenu serviceMenu = new JMenu("����(V)");
	// �����˵���
	JMenuItem portConfigItem = new JMenuItem("�˿�����(P)");
	JMenuItem startServerItem = new JMenuItem("��������(S)");
	JMenuItem stopServerItem = new JMenuItem("ֹͣ����(T)");
	JMenuItem exitItem = new JMenuItem("�˳�(X)");
	
	JMenu helpMenu = new JMenu("����(H)");
	JMenuItem helpItem = new JMenuItem("����(H)");
	
	// ����������
	JToolBar toolBar = new JToolBar();
	
	// �����������еİ�ť���
	JButton portSetButton;// �������������
	JButton startServerButton;// �������������
	JButton stopServerButton;// �رշ��������
	JButton exitButton;// �˳���ť
	
	// ��ܵĴ�С
	Dimension faceSize = new Dimension(400 , 647);
	
	ServerListen listenThread;
	
	JPanel downPanel;
	GridBagLayout girdBag;
	GridBagConstraints girdBagCon;
	
	/**
	 * ����˹��캯��
	 */
	@SuppressWarnings("deprecation")
	public ChatServer(String userName)
	{
		this.userName = userName;
		init();// ��ʼ������
		// ��ӿ�ܵĹر��¼�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		// ���ÿ�ܵĴ�С
		this.setSize(faceSize);
		
		// ��������ʱ���ڵ�λ��
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - faceSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - faceSize.getHeight() ) / 2);
		this.setResizable(false);
		
		this.setTitle("�����ҷ���� " + userName); // ���ñ���
		
		// ����ͼ��
		icon = getImage("icon.gif");
		this.setIconImage(icon); // ���ó���ͼ��
		show();
		
		// Ϊ����˵��������ȼ�'V'
		serviceMenu.setMnemonic('V');
		
		// Ϊ�˿����ÿ�ݼ�Ϊctrl+p
		portConfigItem.setMnemonic('P');
		portConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P ,
				InputEvent.CTRL_MASK));
		
		// Ϊ���������ݼ�Ϊctrl+s
		startServerItem.setMnemonic('S');
		startServerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�˿����ÿ�ݼ�Ϊctrl+T
		stopServerItem.setMnemonic('T');
		stopServerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�˳����ÿ�ݼ�Ϊctrl+x
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�����˵��������ȼ�'H'
		helpMenu.setMnemonic('H');
		
		// Ϊ�������ÿ�ݼ�Ϊctrl+p
		helpItem.setMnemonic('H');
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H ,
				InputEvent.CTRL_MASK));
	}
	
	/**
	 * �����ʼ������
	 */
	@SuppressWarnings(
	{ "unchecked" , "rawtypes", "deprecation" })
	public void init()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		// ��Ӳ˵���
		serviceMenu.add(portConfigItem);
		serviceMenu.add(startServerItem);
		serviceMenu.add(stopServerItem);
		serviceMenu.add(exitItem);
		jMenuBar.add(serviceMenu);
		helpMenu.add(helpItem);
		jMenuBar.add(helpMenu);
		setJMenuBar(jMenuBar);
		
		// ��ʼ����ť
		portSetButton = new JButton("�˿�����");
		startServerButton = new JButton("��������");
		stopServerButton = new JButton("ֹͣ����");
		exitButton = new JButton("�˳�");
		// ����ť��ӵ�������
		toolBar.add(portSetButton);
		toolBar.addSeparator();// ��ӷָ���
		toolBar.add(startServerButton);
		toolBar.add(stopServerButton);
		toolBar.addSeparator();// ��ӷָ���
		toolBar.add(exitButton);
		contentPane.add(toolBar , BorderLayout.NORTH);
		
		// ��ʼʱ����ֹͣ����ť������
		stopServerButton.setEnabled(false);
		stopServerItem.setEnabled(false);
		
		// Ϊ�˵�������¼�����
		portConfigItem.addActionListener(this);
		startServerItem.addActionListener(this);
		stopServerItem.addActionListener(this);
		exitItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		// ��Ӱ�ť���¼�����
		portSetButton.addActionListener(this);
		startServerButton.addActionListener(this);
		stopServerButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		serverReceiverComboBox = new JComboBox();
		serverReceiverComboBox.insertItemAt("������" , 0);
		serverReceiverComboBox.setSelectedIndex(0);
		
		serverMessageShow = new JTextArea();
		serverMessageShow.setEditable(false);
		// ��ӹ�����
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
		systemMessageButton.setText("����");
		
		// ���ϵͳ��Ϣ���¼�����
		systemMessage.addActionListener(this);
		systemMessageButton.addActionListener(this);
		
		sendToLabel = new JLabel("������:");
		sendMessageLabel = new JLabel("������Ϣ:");
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
		// �رճ���ʱ�Ĳ���
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				stopService();
				String str = "\n ChatServer�ࣺ�������رգ�����\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
				System.exit(0);
			}
		});
		
		PortConf portConf = new PortConf(this);
		portConf.show();
	}
	
	/**
	 * �¼�����
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		if(obj == startServerButton || obj == startServerItem)
		{ // ���������
			startService();
			String str = "\n ChatServer�ࣺ����������������\n";
			new PrintLog("serverLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == stopServerButton || obj == stopServerItem)
		{ // ֹͣ�����
			int j = JOptionPane.showConfirmDialog(this , "���ֹͣ������?" , "ֹͣ����" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				stopService();
				String str = "\n ChatServer�ࣺ�������رգ�����\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
			}
		}
		else if(obj == portSetButton || obj == portConfigItem)
		{ // �˿�����
			// �����˿����õĶԻ���
			PortConf portConf = new PortConf(this);
			portConf.show();
		}
		else if(obj == exitButton || obj == exitItem)
		{ // �˳�����
			int j = JOptionPane.showConfirmDialog(this , "���Ҫ�˳���?" , "�˳�" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				stopService();
				String str = "\n ChatServer�ࣺ�������˳�������\n";
				new PrintLog("serverLog.log" , userName + str);
//				System.out.println(str);
				System.exit(0);
			}
		}
		else if(obj == helpItem)
		{ // �˵����еİ���
			// ���������Ի���
			Help helpDialog = new Help("serverMenuHelp");
			helpDialog.show();
		}
		else if(obj == systemMessage || obj == systemMessageButton)
		{ // ����ϵͳ��Ϣ
			sendSystemMessage();
		}
	}
	
	/**
	 * ���������
	 */
	public void startService()
	{
		try
		{
			serverSocket = new ServerSocket(port , 10);
			serverMessageShow.append("������Ѿ���������" + port + "�˿�����...\n");
			
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
			String str = "\n ChatServer�ࣺ�������쳣������\n";
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
	 * �رշ����
	 */
	@SuppressWarnings("unchecked")
	public void stopService()
	{
		try
		{
			// �������˷��ͷ������رյ���Ϣ
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
			serverMessageShow.append("������Ѿ��ر�\n");
			
			serverReceiverComboBox.removeAllItems();
			serverReceiverComboBox.addItem("������");
		}
		catch(Exception e)
		{
			String str = "\n ChatServer�ࣺ�������쳣1������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
	}
	
	/**
	 * �������˷��ͷ������رյ���Ϣ
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
				node.output.writeObject("����ر�");
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer�ࣺ�������쳣2������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			
			i ++ ;
		}
	}
	
	/**
	 * �������˷�����Ϣ
	 */
	public void sendMsgToAll(String msg)
	{
		int count = userLinkList.getCount();// �û�����
		
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
				node.output.writeObject("ϵͳ��Ϣ");
				node.output.flush();
				node.output.writeObject(msg);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer�ࣺ�������쳣3������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			
			i ++ ;
		}
		
		systemMessage.setText("");
	}
	
	/**
	 * ��ͻ����û�������Ϣ
	 */
	public void sendSystemMessage()
	{
		String toSomebody = serverReceiverComboBox.getSelectedItem().toString();
		String message = systemMessage.getText() + "\n";
		
		serverMessageShow.append(message);
		
		// �������˷�����Ϣ
		if(toSomebody.equalsIgnoreCase("������"))
		{
			sendMsgToAll(message);
		}
		else
		{
			// ��ĳ���û�������Ϣ
			Node node = userLinkList.findUser(toSomebody);
			
			try
			{
				node.output.writeObject("ϵͳ��Ϣ");
				node.output.flush();
				node.output.writeObject(message);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ChatServer�ࣺ�������쳣4������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			systemMessage.setText("");// ��������Ϣ������Ϣ���
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
			String str = "\n ChatServer�ࣺ�������쳣5������\n";
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
