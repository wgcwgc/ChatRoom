import java.awt.*;
import java.net.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * ����������Ϣ����ĶԻ���
 * ���û��������ӷ�������IP�Ͷ˿�
 */
@SuppressWarnings("serial")
public class ConnectConfig extends JDialog
{
	JPanel serverInformationPanel = new JPanel();
	JButton save = new JButton();
	JButton cancel = new JButton();
	JLabel defaultServerSetLabel = new JLabel(
			"          Ĭ�Ϸ����Ϊ��  127.0.0.1        ����Ϊ��8888");
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
			String str = "\n ConnectConfig�ࣺ���ӳ�ʼ���쳣������\n";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
		// ��������λ�ã�ʹ�Ի������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - 400 ) / 2 + 50 ,
				(int) ( screenSize.height - 600 ) / 2 + 150);
		this.setResizable(false);
	}
	
	private void connectConfInit() throws Exception
	{
		this.setSize(new Dimension(300 , 130));
		this.setTitle("��������");
		ipAddressLabel.setText("                         �����:");
		inputIp = new JTextField(10);
		inputIp.setText(userInputIp);
		inputPort = new JTextField(4);
		inputPort.setText("" + userInputPort);
		save.setText("����");
		cancel.setText("ȡ��");
		
		serverInformationPanel.setLayout(new GridLayout(2 , 2 , 1 , 1));
		serverInformationPanel.add(ipAddressLabel);
		serverInformationPanel.add(inputIp);
		serverInformationPanel.add(new JLabel("                    ��������:"));
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
		// ���水ť���¼�����
		save.addActionListener(new ActionListener()
		{
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent a)
			{
				int savePort;
				String inputIP;
				// �ж϶˿ں��Ƿ�Ϸ�
				try
				{
					userInputIp = "" + InetAddress.getByName(inputIp.getText());
					userInputIp = userInputIp.substring(1);// userInputIp:
															// /127.0.0.1
				}
				catch(UnknownHostException e)
				{
					defaultServerSetLabel
							.setText("                                    ����ķ���ţ�");
					String str = "\n ConnectConfig�ࣺ�����������쳣������\n";
					str += e.getMessage();
					new Print(str);
//					System.out.println(str);
					return;
				}
				// userInputIp = inputIP;
				// �ж϶˿ں��Ƿ�Ϸ�
				try
				{
					savePort = Integer.parseInt(inputPort.getText());
					
					if(savePort < 0 || savePort > 0xFFFF)
					{
						defaultServerSetLabel
								.setText("               �������������0-65535֮�������!");
						inputPort.setText("8888");
						return;
					}
					userInputPort = savePort;
					dispose();
				}
				catch(NumberFormatException e)
				{
					defaultServerSetLabel
							.setText("    ����ķ�������,������������д0-65535֮�������!");
					inputPort.setText("");
					String str = "\n ConnectConfig�ࣺ�˿ں���д�쳣������\n";
					str += e.getMessage();
					new Print(str);
//					System.out.println(str);
					return;
				}
			}
		});
		// �رնԻ���ʱ�Ĳ���
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				defaultServerSetLabel
						.setText("                  Ĭ�Ϸ����Ϊ��  127.0.0.1  ����Ϊ��8888");
			}
		});
		// ȡ����ť���¼�����
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defaultServerSetLabel
						.setText("                  Ĭ�Ϸ����Ϊ��  127.0.0.1  ����Ϊ��8888");
				dispose();
			}
		});
	}
}
