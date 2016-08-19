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
 * @date             2016��4��19��
 * @time             ����4:35:08
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
 * ����ͻ��˵��������
 */
@SuppressWarnings("serial")
public class ChatClient extends JFrame implements ActionListener
{
	String ip = "127.0.0.1";// ���ӵ�����˵�ip��ַ
	int serverPort = 8888;// ���ӵ�����˵Ķ˿ں�
	String defaultUserName = "���ⴺ";// �û���
	int connectionStatus = 0;// 0��ʾδ���ӣ�1��ʾ������
	String userName = null;
	Image icon;// ����ͼ��
	@SuppressWarnings("rawtypes")
	JComboBox clientReceiverComboBox;// ѡ������Ϣ�Ľ�����
	JTextArea clientMessageShow;// �ͻ��˵���Ϣ��ʾ
	JScrollPane clientMessageScrollPane;// ��Ϣ��ʾ�Ĺ�����
	
	JLabel sendToLabel , emojiLabel , sendMessageLabel;
	
	JTextField clientMessage;// �ͻ�����Ϣ�ķ���
	JCheckBox checkBox;// ˽��
	@SuppressWarnings("rawtypes")
	JComboBox emojiList;// ����ѡ��
	JButton clientMessageButton;// ������Ϣ
	JTextField showUserStatus;// ��ʾ�û�����״̬
	
	Socket socket;
	ObjectOutputStream output;// �����׽��������
	ObjectInputStream input;// �����׽���������
	
	ClientReceive clientReceiveThread;
	
	// �����˵���
	JMenuBar jMenuBar = new JMenuBar();
	// �����˵���
	JMenu operateMenu = new JMenu("����(O)");
	// �����˵���
	JMenuItem loginItem = new JMenuItem("�û���¼(I)");
	JMenuItem logoutItem = new JMenuItem("�û�ע��(L)");
	JMenuItem exitItem = new JMenuItem("�˳�(X)");
	
	JMenu setMenu = new JMenu("����(C)");
	JMenuItem userItem = new JMenuItem("�û�����(U)");
	JMenuItem connectItem = new JMenuItem("��������(C)");
	
	JMenu helpMenu = new JMenu("����(H)");
	JMenuItem helpItem = new JMenuItem("����(H)");
	
	// ����������
	JToolBar toolBar = new JToolBar();
	// �����������еİ�ť���
	JButton loginButton;// �û���¼
	JButton logoutButton;// �û�ע��
	JButton userSetButton;// �û���Ϣ������
	JButton connectSetButton;// ��������
	JButton exitButton;// �˳���ť
	
	// ��ܵĴ�С
	Dimension frameSize = new Dimension(400 , 647);
	
	JPanel downPanel;
	GridBagLayout gridBag;// ���ֹ�����
	GridBagConstraints gridBagCon;// �������Լ��
	
	public ChatClient()
	{
		
	}
	
	@SuppressWarnings("deprecation")
	public ChatClient(String userName)
	{
		this.userName = userName;
		init();// ��ʼ������
		// ��ӿ�ܵĹر��¼�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		// ���ÿ�ܵĴ�С
		this.setSize(frameSize);
		
		// ��������ʱ���ڵ�λ��
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - frameSize.getWidth() ) / 2 ,
				(int) ( screenSize.height - frameSize.getHeight() ) / 2);
		this.setResizable(false);// �������ڴ�С
		this.setTitle("�����ҿͻ���   " + userName); // ���ñ���
		
		// ����ͼ��
//		String packagePath = this.getClass().getResource("").getPath().toString();
//		System.out.println(packagePath + "\n*******************");
//		packagePath = packagePath.substring(1 , packagePath.indexOf("bin") - 1);
//		System.out.println(packagePath + "\n###################");
//		String image = packagePath + "/image/icon.gif";
//		System.out.println(image + "\n###################");
		icon = getImage("icon.gif");
//		icon = getImage(image);
		this.setIconImage(icon); // ���ó���ͼ��
		show();
		
		// Ϊ�����˵��������ȼ�'O' alt+O
		operateMenu.setMnemonic('O');
		
		// Ϊ�û���¼���ÿ�ݼ�Ϊctrl+i
		loginItem.setMnemonic('I');
		loginItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�û�ע����ݼ�Ϊctrl+l
		logoutItem.setMnemonic('L');
		logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�˳���ݼ�Ϊctrl+x
		exitItem.setMnemonic('X');
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X ,
				InputEvent.CTRL_MASK));
		
		// Ϊ���ò˵��������ȼ�'C' alt+c
		setMenu.setMnemonic('C');
		
		// Ϊ�û��������ÿ�ݼ�Ϊctrl+u
		userItem.setMnemonic('U');
		userItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�����������ÿ�ݼ�Ϊctrl+c
		connectItem.setMnemonic('C');
		connectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C ,
				InputEvent.CTRL_MASK));
		
		// Ϊ�����˵��������ȼ�'H'
		helpMenu.setMnemonic('H');
		
		// Ϊ�������ÿ�ݼ�Ϊctrl+h
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
		Container contentPane = getContentPane();// Container������JFrame�������� ��
													// �˴��ǳ�ʼ��һ������
		contentPane.setLayout(new BorderLayout());
		
		// ��Ӳ˵���
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
		// Ϊ�˵�������¼�����
		loginItem.addActionListener(this);
		logoutItem.addActionListener(this);
		exitItem.addActionListener(this);
		userItem.addActionListener(this);
		connectItem.addActionListener(this);
		helpItem.addActionListener(this);
		
		// ��ʼ����ť
		loginButton = new JButton("��¼");
		logoutButton = new JButton("ע��");
		userSetButton = new JButton("�û�����");
		connectSetButton = new JButton("��������");
		exitButton = new JButton("�˳�");
		// �������ϻ���ʾ��Ϣ
		loginButton.setToolTipText("���ӵ�ָ���ķ�����");
		logoutButton.setToolTipText("��������Ͽ�����");
		userSetButton.setToolTipText("�����û���Ϣ");
		connectSetButton.setToolTipText("������Ҫ���ӵ��ķ�������Ϣ");
		// ����ť��ӵ�������
		toolBar.add(userSetButton);
		toolBar.add(connectSetButton);
		toolBar.addSeparator();// ��ӷָ���
		toolBar.add(loginButton);
		toolBar.add(logoutButton);
		toolBar.addSeparator();// ��ӷָ���
		toolBar.add(exitButton);
		contentPane.add(toolBar , BorderLayout.NORTH);
		
		// ��ʼʱ
		loginButton.setEnabled(true);
		logoutButton.setEnabled(false);
		logoutItem.setEnabled(false);
		// ��Ӱ�ť���¼�����
		loginButton.addActionListener(this);
		logoutButton.addActionListener(this);
		userSetButton.addActionListener(this);
		connectSetButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		clientMessageShow = new JTextArea();
		clientMessageShow.setEditable(false);
		// ��ӹ�����
		clientMessageScrollPane = new JScrollPane(clientMessageShow ,
		/* ��ֱ����������Ҫ��ʱ�����20 */
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,
		/* ˮƽ����������Ҫ��ʱ�����30 */
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		clientMessageScrollPane.setPreferredSize(new Dimension(400 , 400));
		clientMessageScrollPane.revalidate();
		
		sendToLabel = new JLabel("������:");
		emojiLabel = new JLabel("         ����:   ");
		sendMessageLabel = new JLabel("������Ϣ:");
		
		clientReceiverComboBox = new JComboBox();
		clientReceiverComboBox.insertItemAt("������" , 0);
		clientReceiverComboBox.setSelectedIndex(0);
		
		emojiList = new JComboBox();
		emojiList.addItem(" ");
		emojiList.addItem("΢Ц��");
		emojiList.addItem("���˵�");
		emojiList.addItem("���ĵ�");
		emojiList.addItem("������");
		emojiList.addItem("С�ĵ�");
		emojiList.addItem("������");
		emojiList.setSelectedIndex(0);
		
		checkBox = new JCheckBox("İ����ģʽ");
		checkBox.setSelected(false);
		
		clientMessage = new JTextField(22);
		clientMessage.setEnabled(false);
		
		clientMessageButton = new JButton();
		clientMessageButton.setText("����");
		
		// ���ϵͳ��Ϣ���¼�����
		clientMessage.addActionListener(this);
		clientMessageButton.addActionListener(this);
		
		downPanel = new JPanel();
		gridBag = new GridBagLayout();
		downPanel.setLayout(gridBag);
		
//		gridBagCon = new GridBagConstraints();//��Ԫ�񲼾�
//		gridBagCon.gridx = 0;//��Ԫ��ĺ�����
//		gridBagCon.gridy = 0;//��Ԫ���������
//		gridBagCon.gridwidth = 5//��Ԫ������ȸ���
//		gridBagCon.gridheight = 2;//��Ԫ�������ȸ���
//		gridBagCon.ipadx = 5;//����Ԫ���ڵ��������С�ߴ����
//		gridBagCon.ipady = 5;//����������
//		JLabel none = new JLabel("    ");
//		gridBag.setConstraints(none , gridBagCon);
//		downPanel.add(none);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 0;
		gridBagCon.gridy = 2;
		gridBagCon.insets = new Insets(1 , 0 , 0 , 0);// ��Ԫ����
//		gridBagCon.ipadx = 5;
//		gridBagCon.ipady = 5;
		gridBag.setConstraints(sendToLabel , gridBagCon);
		downPanel.add(sendToLabel);
		
		gridBagCon = new GridBagConstraints();
		gridBagCon.gridx = 1;
		gridBagCon.gridy = 2;
		gridBagCon.anchor = GridBagConstraints.LINE_START;// ���뷽ʽ
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
		
		// �رճ���ʱ�Ĳ���
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(connectionStatus == 1)
				{
					DisConnect();
					String str = "\n ChatClient�ࣺ�������رգ�����\n";
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
	 * �¼�����
	 */
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e)
	{
		Object obj = e.getSource();
		
		if(obj == userItem || obj == userSetButton)
		{ // �û���Ϣ����
			// �����û���Ϣ���öԻ���
			UserConfig userConf = new UserConfig(this , defaultUserName);
			userConf.show();
			defaultUserName = userConf.userInputName;
		}
		else if(obj == connectItem || obj == connectSetButton)
		{ // ���ӷ��������
			// �����������öԻ���
			ConnectConfig conConfig = new ConnectConfig(this , ip , serverPort);
			conConfig.show();
			ip = conConfig.userInputIp;
			serverPort = conConfig.userInputPort;
		}
		else if(obj == loginItem || obj == loginButton)
		{ // ��¼
			Connect();
			String str = "\n ChatClient�ࣺ�û���¼���ߣ�����\n";
			new PrintLog("clientLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == logoutItem || obj == logoutButton)
		{ // ע��
			DisConnect();
			showUserStatus.setText("");
			String str = "\n ChatClient�ࣺ�û�ע�����ߣ�����\n";
			new PrintLog("clientLog.log" , userName + str);
//			System.out.println(str);
		}
		else if(obj == clientMessage || obj == clientMessageButton)
		{ // ������Ϣ
			SendMessage();
			clientMessage.setText("");
		}
		else if(obj == exitButton || obj == exitItem)
		{ // �˳�
			int j = JOptionPane.showConfirmDialog(this , "���Ҫ�˳���?" , "�˳�" ,
					JOptionPane.YES_OPTION , JOptionPane.QUESTION_MESSAGE);
			
			if(j == JOptionPane.YES_OPTION)
			{
				if(connectionStatus == 1)
				{
					DisConnect();
					String str = "\n ChatClient�ࣺ�û��˳����ߣ�����\n";
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
			JOptionPane.showConfirmDialog(this , "�������ӵ�ָ���ķ�������\n��ȷ�����������Ƿ���ȷ��" ,
					"��ʾ" , JOptionPane.DEFAULT_OPTION ,
					JOptionPane.WARNING_MESSAGE);
			String str = "\n ChatClient�ࣺ���ӷ������쳣������\n";
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
			clientMessageShow.append("���ӷ���ţ� " + ip + "    ���룺" + serverPort
					+ " �ɹ�... ...\n");
			connectionStatus = 1;// ��־λ��Ϊ������
			
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig�ࣺ���������ݷ����쳣������\n";
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
			output.writeObject("�û�����");
			output.flush();
			
			input.close();
			output.close();
			socket.close();
			clientMessageShow.append("�Ѿ���������Ͽ�����... ...\n");
			connectionStatus = 0;
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig�ࣺ�ͻ����˳��쳣������\n";
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
			status = "İ������Ϣ";
		}
		
		String emoji = emojiList.getSelectedItem().toString();
		String message = clientMessage.getText();
		
		if(socket.isClosed())
		{
			return;
		}
		
		try
		{
			output.writeObject("������Ϣ");
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
			String str = "\n ConnectConfig�ࣺ��Ϣ�����쳣������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
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
			String str = "\n ConnectConfig�ࣺ�����ļ���ȡ�쳣������\n";
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
