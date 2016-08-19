package chatRoomSystemClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * ����ͻ�����Ϣ�շ���
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
				if(type.equalsIgnoreCase("ϵͳ��Ϣ"))
				{
					String systemMessage = (String) input.readObject();
					textArea.append("ϵͳ��Ϣ: " + systemMessage);
				}
				else if(type.equalsIgnoreCase("����ر�"))
				{
					output.close();
					input.close();
					socket.close();
					textArea.append("�������ѹرգ�\n");
					showUserStatus.setText("�����û� " + "0��");
					break;
				}
				else if(type.equalsIgnoreCase("������Ϣ"))
				{
					String message = (String) input.readObject();
					textArea.append("������Ϣ :" + message);
				}
				else if(type.equalsIgnoreCase("�û��б�"))
				{
					String userList = (String) input.readObject();
					userNames = userList.split("\n");
					comboBox.removeAllItems();
					
					int i = 0;
					comboBox.addItem("������");
					while(i < userNames.length)
					{
						comboBox.addItem(userNames[i]);
						i ++ ;
					}
					comboBox.setSelectedIndex(0);
					showUserStatus.setText("�����û� " + userNames.length + " ��");
				}
			}
			catch(Exception e)
			{
				String str = "\n ClientReceive�ࣺ�����������쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
}
