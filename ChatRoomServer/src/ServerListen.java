import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * ����˵�������
 */
public class ServerListen extends Thread
{
	ServerSocket server;
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;
	JTextArea textArea;
	JTextField textField;
	UserLinkList userLinkList;// �û�����
	Node client;
	ServerReceive serverReceiverThread;
	public boolean isStop;
	
	/*
	 * �������˵��û�����������������
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
					// ��ʾ��ʾ��Ϣ
					comboBox.addItem(client.username);
					userLinkList.addUser(client);
					textArea.append("�û� " + client.username + " ����" + "\n");
					textField.setText("�����û�" + userLinkList.getCount() + "��\n");
					serverReceiverThread = new ServerReceive(textArea , textField ,
							comboBox , client , userLinkList);
					serverReceiverThread.start();
				}
				catch(Exception e)
				{
					String str = "\n ServerListen�ࣺ�������쳣������\n";
					str += e.getMessage();
					new Print(str);
//				System.out.println(str);
				}
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null , "�ö˿��Ѿ���ռ�ã���ѡ�����������˿ں� ������");
			String str = "\n ServerListen�ࣺ�������쳣������\n�ö˿��Ѿ���ռ�ã���ѡ�����������˿ں� ������";
			str += e.getMessage();
			new Print(str);
//			System.out.println(str);
		}
	}
}
