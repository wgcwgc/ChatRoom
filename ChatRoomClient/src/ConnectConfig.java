import java.awt.*;
import java.net.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * 生成连接信息输入的对话框
 * 让用户输入连接服务器的IP和端口
 */
@SuppressWarnings("serial")
public class ConnectConfig extends JDialog
{
	JPanel serverInformationPanel = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	JLabel defaultServerSetLabel = new JLabel(
			"          默认房间号为：  127.0.0.1        密码为：8888");
	JPanel downSavePanel = new JPanel();
	JLabel ipAddressLabel = new JLabel();
	
	String userInputIp;
	int userInputPort;
	
	JTextField inputIp;
	JTextField inputPort;
	
	public ConnectConfig()
	{
		
	}
	
	public ConnectConfig(JFrame frame , String ip , int port)
	{
		super(frame , true);
		this.userInputIp = ip;
		this.userInputPort = port;
		try
		{
			connectConfInit();
		}
		catch(Exception e)
		{
			String str = "\n ConnectConfig类：连接初始化异常！！！\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		// 设置运行位置，使对话框居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - 400 ) / 2 + 50 ,
				(int) ( screenSize.height - 600 ) / 2 + 150);
		this.setResizable(false);
	}
	
	private void connectConfInit() throws Exception
	{
		this.setSize(new Dimension(300 , 130));
		this.setTitle("连接设置");
		ipAddressLabel.setText("                         房间号:");
		inputIp = new JTextField(10);
		inputIp.setText(userInputIp);
		inputPort = new JTextField(4);
		inputPort.setText("" + userInputPort);
		save.setText("保存");
		cancel.setText("取消");
		
		serverInformationPanel.setLayout(new GridLayout(2 , 2 , 1 , 1));
		serverInformationPanel.add(ipAddressLabel);
		serverInformationPanel.add(inputIp);
		serverInformationPanel.add(new JLabel("                    房间密码:"));
		serverInformationPanel.add(inputPort);
		
		downSavePanel.add(new Label("              "));
		downSavePanel.add(save);
		downSavePanel.add(cancel);
		downSavePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(serverInformationPanel , BorderLayout.NORTH);
		contentPane.add(defaultServerSetLabel , BorderLayout.CENTER);
		contentPane.add(downSavePanel , BorderLayout.SOUTH);
		// 保存按钮的事件处理
		save.addActionListener(new ActionListener()
		{
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent a)
			{
				int savePort;
				String inputIP;
				// 判断端口号是否合法
				try
				{
					userInputIp = "" + InetAddress.getByName(inputIp.getText());
					userInputIp = userInputIp.substring(1);// userInputIp:
															// /127.0.0.1
				}
				catch(UnknownHostException e)
				{
					defaultServerSetLabel
							.setText("                                    错误的房间号！");
					String str = "\n ConnectConfig类：服务器连接异常！！！\n";
					str += e.getMessage();
					new Print(str);
//					System.out.println(str);
					return;
				}
				// userInputIp = inputIP;
				// 判断端口号是否合法
				try
				{
					savePort = Integer.parseInt(inputPort.getText());
					
					if(savePort < 0 || savePort > 0xFFFF)
					{
						defaultServerSetLabel
								.setText("               房间密码必须是0-65535之间的整数!");
						inputPort.setText("8888");
						return;
					}
					userInputPort = savePort;
					dispose();
				}
				catch(NumberFormatException e)
				{
					defaultServerSetLabel
							.setText("    错误的房间密码,房间密码请填写0-65535之间的整数!");
					inputPort.setText("");
					String str = "\n ConnectConfig类：端口号填写异常！！！\n";
					str += e.getMessage();
					new Print(str);
//					System.out.println(str);
					return;
				}
			}
		});
		// 关闭对话框时的操作
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				defaultServerSetLabel
						.setText("                  默认房间号为：  127.0.0.1  密码为：8888");
			}
		});
		// 取消按钮的事件处理
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defaultServerSetLabel
						.setText("                  默认房间号为：  127.0.0.1  密码为：8888");
				dispose();
			}
		});
	}
}
