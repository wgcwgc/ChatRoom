import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * ����
 */
@SuppressWarnings(
{ "serial" })
public class Help extends JDialog
{
	JPanel titlePanel = new JPanel();
	JPanel contentPanel = new JPanel();
	JPanel closePanel = new JPanel();
	
	JButton close = new JButton();
	JTextArea help = new JTextArea();
	
	Color bg = new Color(255 , 255 , 255);
	
	public Help(String flog)
	{
		super();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int) ( screenSize.width - 400 ) / 2 + 25 ,
				(int) ( screenSize.height - 320 ) / 2);
		this.setResizable(false);
		if(flog.equals("clientMenuHelp"))
		{
			try
			{
				menuHelpInit();
			}
			catch(Exception e)
			{
				String str = "\n Help�ࣺ�˵�չʾ�쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		else if(flog.equals("homeScreenHelp"))
		{
			try
			{
				homeScreenHelp();
			}
			catch(Exception e)
			{
				String str = "\n Help�ࣺ�˵�չʾ�쳣������\n";
				str += e.getMessage();
				new Print(str);
//				System.out.println(str);
			}
		}
		
	}
	
	/**
	 * ���������
	 */
	private void homeScreenHelp()
	{
		this.setSize(new Dimension(500 , 270));
		this.setTitle("����");
		
		titlePanel.setBackground(bg);
		contentPanel.setBackground(bg);
		closePanel.setBackground(bg);
		
		help.setText("1����������˺ţ�������ȷ���˺ź����룬�������¼������ʹ�á�\n"
				+ "2�������û���˺ŵ����ע�ᡱ��  �����û��������롢�ֻ��š���ʵ�����Ϳ�ӵ�����˺š�\n"
				+ "      ����������֤�����ĸ�����Ϣ����й©�������κ��û��͸��ˣ�ֻ�������ά����\n"
				+ "      ���⣺һ���ֻ���ֻ��ע��һ���˺š�\n"
				+ "3�������Ҫ�޸����룬�ɵ�����޸����롱��ͨ������ע��ʱ���˺ź��ֻ��ż����޸ġ�\n"
				+ "4��������������룬���Ե�����������롱������ע��ʱ���˺ź��ֻ��ż����һء�\n"
				+ "5������������˺ţ����Ե���������˺š�������ע��ʱ���ֻ��ź���ʵ�����һء�\n"
				+ "6�����������κ����⣬����ϵ�������䣺1348066599@qq.com\n" + "7����л����ʹ����֧�֣�����\n");
		help.setEditable(false);
		
		titlePanel.add(new Label("              "));
		JLabel title = new JLabel("�����ҿͻ���������");
		titlePanel.add(title);
		titlePanel.add(new Label("              "));
		
		contentPanel.add(help);
		
		closePanel.add(new Label("              "));
		closePanel.add(close);
		closePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel , BorderLayout.NORTH);
		contentPane.add(contentPanel , BorderLayout.CENTER);
		contentPane.add(closePanel , BorderLayout.SOUTH);
		
		close.setText("�ر�");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
	
	/**
	 * �ͻ��˲˵�����
	 * @throws Exception
	 */
	private void menuHelpInit() throws Exception
	{
		this.setSize(new Dimension(350 , 270));
		this.setTitle("����");
		
		titlePanel.setBackground(bg);
		contentPanel.setBackground(bg);
		closePanel.setBackground(bg);
		
		help.setText("1��������������á�����������Ҫ���ӷ���źͷ�������\n"
				+ "      Ĭ������Ϊ��127.0.0.1:8888����\n" + "2��������û����á����������ǳƣ�Ĭ������Ϊ:���ⴺ����\n"
				+ "3���������¼����������ӵ�ָ���ķ��䣻\n" + "      �����ע�������Ժͷ���Ͽ����ӡ�\n"
				+ "4��ѡ����Ҫ������Ϣ���û�������Ϣ����д����Ϣ��\n" + "      ͬʱѡ����飬��������͡���ɷ�����Ϣ��\n");
		help.setEditable(false);
		
		titlePanel.add(new Label("              "));
		JLabel title = new JLabel("�����ҿͻ���");
		titlePanel.add(title);
		titlePanel.add(new Label("              "));
		
		contentPanel.add(help);
		
		closePanel.add(new Label("              "));
		closePanel.add(close);
		closePanel.add(new Label("              "));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(titlePanel , BorderLayout.NORTH);
		contentPane.add(contentPanel , BorderLayout.CENTER);
		contentPane.add(closePanel , BorderLayout.SOUTH);
		
		close.setText("�ر�");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
	}
}
