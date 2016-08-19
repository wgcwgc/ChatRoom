package chatRoomSystemClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * �����û���Ϣ����Ի������
 * ���û������Լ����û���
 */
@SuppressWarnings("serial")
public class UserConfig extends JDialog
{
	JPanel userConfigPanel = new JPanel();
	JButton save = new JButton();// ���水ť
	JButton cancel = new JButton();// ȡ����ť
	JLabel defaultUserInformationLabel = new JLabel("              Ĭ���ǳ�Ϊ �� ���ⴺ");// �û���Ϣ��ʾ
	JPanel downSavePanel = new JPanel();
	JLabel userInputNameLabel = new JLabel();
	String userInputName;
	JTextField userNameField;
	long random = new Random().nextInt(57);
	
	public UserConfig()
	{
		
	}
	
	public UserConfig(JFrame frame , String username)
	{
		super(frame , true);
		this.userInputName = username;
		try
		{
			userConfigInit();// ��ʼ��
		}
		catch(Exception e)
		{
			String str = "\n UserConfig�ࣺ�û���ʼ���쳣������\n";
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
	
	private void userConfigInit() throws Exception
	{
		this.setSize(new Dimension(300 , 120));
		this.setTitle("�û�����");
		userInputNameLabel.setText("          �ǳ� ��");
		userNameField = new JTextField(10);
		userNameField.setText(userInputName);
		save.setText("����");
		cancel.setText("ȡ��");
		
		userConfigPanel.setLayout(new FlowLayout());
		userConfigPanel.add(userInputNameLabel);
		userConfigPanel.add(userNameField);
		
		downSavePanel.add(new Label("              "));
		downSavePanel.add(save);
		downSavePanel.add(cancel);
		downSavePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(userConfigPanel , BorderLayout.NORTH);
		contentPane.add(defaultUserInformationLabel , BorderLayout.CENTER);
		contentPane.add(downSavePanel , BorderLayout.SOUTH);
		
		// ���水ť���¼�����
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				if(userNameField.getText().trim().equals(""))
				{
					defaultUserInformationLabel
							.setText("                                 �ǳƲ���Ϊ�գ�");
					userNameField.setText(userInputName);
					return;
				}
				else if(userNameField.getText().trim().length() > 15)
				{
					defaultUserInformationLabel
							.setText("                    �ǳƳ��Ȳ��ܴ���15���ַ���");
					userNameField.setText(userInputName);
					return;
				}
				else if(userNameField.getText().equals("���ⴺ"))
				{
					userNameField.setText("���ⴺ" + random);
				}
				userInputName = userNameField.getText();
				dispose();
			}
		});
		
		// �رնԻ���ʱ�Ĳ���
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				defaultUserInformationLabel
						.setText("                         Ĭ���ǳ�Ϊ�����ⴺ" + random);
			}
		});
		
		// ȡ����ť���¼�����
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defaultUserInformationLabel
						.setText("                         Ĭ���ǳ�Ϊ�����ⴺ" + random);
				dispose();
			}
		});
	}
}
