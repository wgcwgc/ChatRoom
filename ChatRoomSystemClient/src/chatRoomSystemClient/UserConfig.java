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
 * 生成用户信息输入对话框的类
 * 让用户输入自己的用户名
 */
@SuppressWarnings("serial")
public class UserConfig extends JDialog
{
	JPanel userConfigPanel = new JPanel();
	JButton save = new JButton();// 保存按钮
	JButton cancel = new JButton();// 取消按钮
	JLabel defaultUserInformationLabel = new JLabel("              默认昵称为 ： 王光春");// 用户信息显示
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
			userConfigInit();// 初始化
		}
		catch(Exception e)
		{
			String str = "\n UserConfig类：用户初始化异常！！！\n";
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
	
	private void userConfigInit() throws Exception
	{
		this.setSize(new Dimension(300 , 120));
		this.setTitle("用户设置");
		userInputNameLabel.setText("          昵称 ：");
		userNameField = new JTextField(10);
		userNameField.setText(userInputName);
		save.setText("保存");
		cancel.setText("取消");
		
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
		
		// 保存按钮的事件处理
		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				if(userNameField.getText().trim().equals(""))
				{
					defaultUserInformationLabel
							.setText("                                 昵称不能为空！");
					userNameField.setText(userInputName);
					return;
				}
				else if(userNameField.getText().trim().length() > 15)
				{
					defaultUserInformationLabel
							.setText("                    昵称长度不能大于15个字符！");
					userNameField.setText(userInputName);
					return;
				}
				else if(userNameField.getText().equals("王光春"))
				{
					userNameField.setText("王光春" + random);
				}
				userInputName = userNameField.getText();
				dispose();
			}
		});
		
		// 关闭对话框时的操作
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				defaultUserInformationLabel
						.setText("                         默认昵称为：王光春" + random);
			}
		});
		
		// 取消按钮的事件处理
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				defaultUserInformationLabel
						.setText("                         默认昵称为：王光春" + random);
				dispose();
			}
		});
	}
}
