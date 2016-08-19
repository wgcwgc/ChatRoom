import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/*
 * �������շ���Ϣ����
 */
public class ServerReceive extends Thread
{
	JTextArea textArea;
	JTextField textField;
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;
	Node client;
	UserLinkList userLinkList;// �û�����
	
	public boolean isStop;
	
	@SuppressWarnings("rawtypes")
	public ServerReceive(JTextArea textArea , JTextField textField ,
			JComboBox comboBox , Node client , UserLinkList userLinkList)
	{
		this.textArea = textArea;
		this.textField = textField;
		this.client = client;
		this.userLinkList = userLinkList;
		this.comboBox = comboBox;
		isStop = false;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		// �������˷����û����б�
		sendUserList();
		
		while( ! isStop && ! client.socket.isClosed())
		{
			try
			{
				String type = (String) client.input.readObject();
				
				if(type.equalsIgnoreCase("������Ϣ"))
				{
					String toSomebody = (String) client.input.readObject();
					String status = (String) client.input.readObject();
					String emoji = (String) client.input.readObject();
					String message = (String) client.input.readObject();
					
					String chatMessageContent = client.username + " " + emoji
							+ " �� " + toSomebody + " ˵ : " + message + "\n";
					String chatMessageContentTitle = null;
					if(status.equalsIgnoreCase("İ������Ϣ"))
					{
						chatMessageContentTitle = " [ İ������Ϣ ]  ";
					}
					else
					{
						chatMessageContentTitle = " [ ������Ϣ ]  ";
					}
					textArea.append(chatMessageContentTitle + chatMessageContent);
					
					if(toSomebody.equals(client))
					{
						try
						{
							client.output.writeObject("������Ϣ");
							client.output.flush();
							chatMessageContent = chatMessageContentTitle + "��"
									+ emoji + " �Լ�˵ : " + message + "\n";
							client.output.writeObject(chatMessageContent);
							client.output.flush();
						}
						catch(Exception e)
						{
							String str = "\n ServerReceive�ࣺ�������쳣0������\n";
							str += e.getMessage();
							new Print(str);
//							System.out.println(str);
						}
					}
					else
					{
						if(toSomebody.equalsIgnoreCase("������"))
						{
							if(status.equalsIgnoreCase("İ������Ϣ"))
							{
								try
								{
									client.output.writeObject("������Ϣ");
									client.output.flush();
									chatMessageContent = " [ İ������Ϣ ]  " + "��"
											+ emoji + " �� ������ ˵ : " + message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								catch(Exception e)
								{
									String str = "\n ServerReceive�ࣺ�������쳣������\n";
									str += e.getMessage();
									new Print(str);
//									System.out.println(str);
								}
								
								chatMessageContent = " [ İ������Ϣ ]  " + "İ����" + emoji
										+ " �� " + toSomebody + " ˵ : " + message
										+ "\n";
								sendToAll(chatMessageContent , client);// �������˷�����Ϣ
							}
							else
							{
								try
								{
									client.output.writeObject("������Ϣ");
									client.output.flush();
									chatMessageContent = " [ ������Ϣ ]  " + "��"
											+ emoji + " �� ������ ˵ : " + message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								catch(Exception e)
								{
									String str = "\n ServerReceive�ࣺ�������쳣1������\n";
									str += e.getMessage();
									new Print(str);
//									System.out.println(str);
								}
								chatMessageContent = " [ ������Ϣ ]  "
										+ client.username + " " + emoji
										+ " �� ������ ˵ : " + message + "\n";
								sendToAll(chatMessageContent , client);// �������˷�����Ϣ
							}
						}
						else
						{
							try
							{
								if(status.equalsIgnoreCase("İ������Ϣ"))
								{
									client.output.writeObject("������Ϣ");
									client.output.flush();
									chatMessageContent = " [ İ������Ϣ ]  " + "��"
											+ emoji + " �� " + toSomebody + " ˵ : "
											+ message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								else
								{
									client.output.writeObject("������Ϣ");
									client.output.flush();
									chatMessageContent = " [ ������Ϣ ]  " + "��"
											+ emoji + " �� " + toSomebody + " ˵ : "
											+ message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
							}
							catch(Exception e)
							{
								String str = "\n ServerReceive�ࣺ�������쳣2������\n";
								str += e.getMessage();
								new Print(str);
//								System.out.println(str);
							}
							
							Node node = userLinkList.findUser(toSomebody);
							if(node != null)
							{
								node.output.writeObject("������Ϣ");
								node.output.flush();
								String strangerMessageContent = "";
								if(status.equalsIgnoreCase("İ������Ϣ"))
								{
									strangerMessageContent = " [ İ������Ϣ ]  " + "İ����"
											+ emoji + " ����˵ : " + message + "\n";
								}
								else
								{
									strangerMessageContent = " [ ������Ϣ ]  "
											+ client.username + " " + emoji
											+ " ����˵ : " + message + "\n";
								}
								node.output.writeObject(strangerMessageContent);
								node.output.flush();
							}
							else
							{
								client.output.writeObject("������Ϣ");
								client.output.flush();
								client.output.writeObject("\n����ʧ�ܣ�����û���ҵ�����ϵ�ˣ�����");
								client.output.flush();
							}
						}
					}
					
				}
				else if(type.equalsIgnoreCase("�û�����"))
				{
					Node node = userLinkList.findUser(client.username);
					userLinkList.deleteUser(node);
					
					String offlineMessageContent = "�û� " + client.username
							+ " ����\n";
					int count = userLinkList.getCount();
					
					comboBox.removeAllItems();
					comboBox.addItem("������");
					int i = 0;
					while(i < count)
					{
						node = userLinkList.findUser(i);
						if(node == null)
						{
							i ++ ;
							continue;
						}
						
						comboBox.addItem(node.username);
						i ++ ;
					}
					comboBox.setSelectedIndex(0);
					textArea.append(offlineMessageContent);
					textField.setText("�����û�" + userLinkList.getCount() + "��\n");
					sendToAll(offlineMessageContent);// �������˷�����Ϣ
					sendUserList();// ���·����û��б�,ˢ��
					break;
				}
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive�ࣺ�������쳣3������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/*
	 * �������ˣ������Լ���������Ϣ
	 */
	public void sendToAll(String messageContent , Node client)
	{
		int count = userLinkList.getCount();
		int i = 0;
		while(i < count)
		{
			Node node = userLinkList.findUser(i);
			if(node == null || node == client)
			{
				i ++ ;
				continue;
			}
			try
			{
				node.output.writeObject("������Ϣ");
				node.output.flush();
				node.output.writeObject(messageContent);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive�ࣺ�������쳣4������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
	
	/*
	 * �������˷�����Ϣ
	 */
	public void sendToAll(String messageContent)
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
				node.output.writeObject("������Ϣ");
				node.output.flush();
				node.output.writeObject(messageContent);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive�ࣺ�������쳣5������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
	
	/*
	 * �������˷����û����б�
	 */
	public void sendUserList()
	{
		String userlist = "";
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
			userlist += node.username;
			userlist += '\n';
			i ++ ;
		}
		i = 0;
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
				node.output.writeObject("�û��б�");
				node.output.flush();
				node.output.writeObject(userlist);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive�ࣺ�������쳣6������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
}
