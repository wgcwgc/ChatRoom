package chatRoomSystemServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 用户链表的结点类
 */
public class Node
{
	String username = null;
	Socket socket = null;
	ObjectOutputStream output = null;
	ObjectInputStream input = null;
	
	Node next = null;
}
