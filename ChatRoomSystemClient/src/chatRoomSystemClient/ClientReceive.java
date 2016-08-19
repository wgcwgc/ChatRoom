package chatRoomSystemClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * 聊天客户端消息收发类
 */
public class ClientReceive extends Thread
{
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox;
	private JTextArea textArea;
	Socket socket;
	ObjectOutputStream output;
	ObjectInputStream input;
	JTextField showUserStatus;
	String userNames[];
	
	public ClientReceive()
	{
		
	}
	
	@SuppressWarnings("rawtypes")
	public ClientReceive(Socket socket , ObjectOutputStream output ,
			ObjectInputStream input , JComboBox comboBox , JTextArea textArea ,
			JTextField showUserStatus)
	{
		this.socket = socket;
		this.output = output;
		this.input = input;
		this.comboBox = comboBox;
		this.textArea = textArea;
		this.showUserStatus = showUserStatus;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		while( ! socket.isClosed())
		{
			try
			{
				String type = (String) input.readObject();
				if(type.equalsIgnoreCase("系统信息"))
				{
					String systemMessage = (String) input.readObject();
					textArea.append("系统信息: " + systemMessage);
				}
				else if(type.equalsIgnoreCase("服务关闭"))
				{
					output.close();
					input.close();
					socket.close();
					textArea.append("服务器已关闭！\n");
					showUserStatus.setText("在线用户 " + "0人");
					break;
				}
				else if(type.equalsIgnoreCase("聊天信息"))
				{
					String message = (String) input.readObject();
					textArea.append("聊天信息 :" + message);
				}
				else if(type.equalsIgnoreCase("用户列表"))
				{
					String userList = (String) input.readObject();
					userNames = userList.split("\n");
					comboBox.removeAllItems();
					
					int i = 0;
					comboBox.addItem("所有人");
					while(i < userNames.length)
					{
						comboBox.addItem(userNames[i]);
						i ++ ;
					}
					comboBox.setSelectedIndex(0);
					showUserStatus.setText("在线用户 " + userNames.length + " 人");
				}
			}
			catch(Exception e)
			{
				String str = "\n ClientReceive类：服务器监听异常！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
}
