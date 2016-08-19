import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * 服务端的侦听类
 */
public class ServerListen extends Thread
{
	ServerSocket server;
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;
	JTextArea textArea;
	JTextField textField;
	UserLinkList userLinkList;// 用户链表
	Node client;
	ServerReceive serverReceiverThread;
	public boolean isStop;
	
	/*
	 * 聊天服务端的用户上线于下线侦听类
	 */
	@SuppressWarnings("rawtypes")
	public ServerListen(ServerSocket server , JComboBox comboBox ,
			JTextArea textArea , JTextField textField ,
			UserLinkList userLinkList)
	{
		this.server = server;
		this.comboBox = comboBox;
		this.textArea = textArea;
		this.textField = textField;
		this.userLinkList = userLinkList;
		isStop = false;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		try
		{
			while( ! isStop && ! server.isClosed())
			{
				try
				{
					client = new Node();
					client.socket = server.accept();
					client.output = new ObjectOutputStream(
							client.socket.getOutputStream());
					client.output.flush();
					client.input = new ObjectInputStream(
							client.socket.getInputStream());
					client.username = (String) client.input.readObject();
					// 显示提示信息
					comboBox.addItem(client.username);
					userLinkList.addUser(client);
					textArea.append("用户 " + client.username + " 上线" + "\n");
					textField.setText("在线用户" + userLinkList.getCount() + "人\n");
					serverReceiverThread = new ServerReceive(textArea , textField ,
							comboBox , client , userLinkList);
					serverReceiverThread.start();
				}
				catch(Exception e)
				{
					String str = "\n ServerListen类：服务器异常！！！\n";
					str += e.getMessage();
					new Print(str);
//				System.out.println(str);
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null , "该端口已经被占用，请选择启动其他端口号 ！！！");
			String str = "\n ServerListen类：服务器异常！！！\n该端口已经被占用，请选择启动其他端口号 ！！！";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
	}
}
