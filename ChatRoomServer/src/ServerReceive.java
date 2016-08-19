import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/*
 * 服务器收发消息的类
 */
public class ServerReceive extends Thread
{
	JTextArea textArea;
	JTextField textField;
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;
	Node client;
	UserLinkList userLinkList;// 用户链表
	
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
		// 向所有人发送用户的列表
		sendUserList();
		
		while( ! isStop && ! client.socket.isClosed())
		{
			try
			{
				String type = (String) client.input.readObject();
				
				if(type.equalsIgnoreCase("聊天信息"))
				{
					String toSomebody = (String) client.input.readObject();
					String status = (String) client.input.readObject();
					String emoji = (String) client.input.readObject();
					String message = (String) client.input.readObject();
					
					String chatMessageContent = client.username + " " + emoji
							+ " 对 " + toSomebody + " 说 : " + message + "\n";
					String chatMessageContentTitle = null;
					if(status.equalsIgnoreCase("陌生人信息"))
					{
						chatMessageContentTitle = " [ 陌生人信息 ]  ";
					}
					else
					{
						chatMessageContentTitle = " [ 正常信息 ]  ";
					}
					textArea.append(chatMessageContentTitle + chatMessageContent);
					
					if(toSomebody.equals(client))
					{
						try
						{
							client.output.writeObject("聊天信息");
							client.output.flush();
							chatMessageContent = chatMessageContentTitle + "你"
									+ emoji + " 自己说 : " + message + "\n";
							client.output.writeObject(chatMessageContent);
							client.output.flush();
						}
						catch(Exception e)
						{
							String str = "\n ServerReceive类：服务器异常0！！！\n";
							str += e.getMessage();
							new Print(str);
//							System.out.println(str);
						}
					}
					else
					{
						if(toSomebody.equalsIgnoreCase("所有人"))
						{
							if(status.equalsIgnoreCase("陌生人信息"))
							{
								try
								{
									client.output.writeObject("聊天信息");
									client.output.flush();
									chatMessageContent = " [ 陌生人信息 ]  " + "你"
											+ emoji + " 对 所有人 说 : " + message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								catch(Exception e)
								{
									String str = "\n ServerReceive类：服务器异常！！！\n";
									str += e.getMessage();
									new Print(str);
//									System.out.println(str);
								}
								
								chatMessageContent = " [ 陌生人信息 ]  " + "陌生人" + emoji
										+ " 对 " + toSomebody + " 说 : " + message
										+ "\n";
								sendToAll(chatMessageContent , client);// 向所有人发送消息
							}
							else
							{
								try
								{
									client.output.writeObject("聊天信息");
									client.output.flush();
									chatMessageContent = " [ 正常信息 ]  " + "你"
											+ emoji + " 对 所有人 说 : " + message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								catch(Exception e)
								{
									String str = "\n ServerReceive类：服务器异常1！！！\n";
									str += e.getMessage();
									new Print(str);
//									System.out.println(str);
								}
								chatMessageContent = " [ 正常信息 ]  "
										+ client.username + " " + emoji
										+ " 对 所有人 说 : " + message + "\n";
								sendToAll(chatMessageContent , client);// 向所有人发送消息
							}
						}
						else
						{
							try
							{
								if(status.equalsIgnoreCase("陌生人信息"))
								{
									client.output.writeObject("聊天信息");
									client.output.flush();
									chatMessageContent = " [ 陌生人信息 ]  " + "你"
											+ emoji + " 对 " + toSomebody + " 说 : "
											+ message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
								else
								{
									client.output.writeObject("聊天信息");
									client.output.flush();
									chatMessageContent = " [ 正常信息 ]  " + "你"
											+ emoji + " 对 " + toSomebody + " 说 : "
											+ message + "\n";
									client.output.writeObject(chatMessageContent);
									client.output.flush();
								}
							}
							catch(Exception e)
							{
								String str = "\n ServerReceive类：服务器异常2！！！\n";
								str += e.getMessage();
								new Print(str);
//								System.out.println(str);
							}
							
							Node node = userLinkList.findUser(toSomebody);
							if(node != null)
							{
								node.output.writeObject("聊天信息");
								node.output.flush();
								String strangerMessageContent = "";
								if(status.equalsIgnoreCase("陌生人信息"))
								{
									strangerMessageContent = " [ 陌生人信息 ]  " + "陌生人"
											+ emoji + " 对你说 : " + message + "\n";
								}
								else
								{
									strangerMessageContent = " [ 正常信息 ]  "
											+ client.username + " " + emoji
											+ " 对你说 : " + message + "\n";
								}
								node.output.writeObject(strangerMessageContent);
								node.output.flush();
							}
							else
							{
								client.output.writeObject("聊天信息");
								client.output.flush();
								client.output.writeObject("\n发送失败！！！没有找到该联系人！！！");
								client.output.flush();
							}
						}
					}
					
				}
				else if(type.equalsIgnoreCase("用户下线"))
				{
					Node node = userLinkList.findUser(client.username);
					userLinkList.deleteUser(node);
					
					String offlineMessageContent = "用户 " + client.username
							+ " 下线\n";
					int count = userLinkList.getCount();
					
					comboBox.removeAllItems();
					comboBox.addItem("所有人");
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
					textField.setText("在线用户" + userLinkList.getCount() + "人\n");
					sendToAll(offlineMessageContent);// 向所有人发送消息
					sendUserList();// 重新发送用户列表,刷新
					break;
				}
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive类：服务器异常3！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
	}
	
	/*
	 * 向所有人（除了自己）发送消息
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
				node.output.writeObject("聊天信息");
				node.output.flush();
				node.output.writeObject(messageContent);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive类：服务器异常4！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
	
	/*
	 * 向所有人发送消息
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
				node.output.writeObject("聊天信息");
				node.output.flush();
				node.output.writeObject(messageContent);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive类：服务器异常5！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
	
	/*
	 * 向所有人发送用户的列表
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
				node.output.writeObject("用户列表");
				node.output.flush();
				node.output.writeObject(userlist);
				node.output.flush();
			}
			catch(Exception e)
			{
				String str = "\n ServerReceive类：服务器异常6！！！\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
			i ++ ;
		}
	}
}
