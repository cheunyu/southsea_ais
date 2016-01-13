package com.scyb.aisbroadcast.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextArea;

import com.scyb.aisbroadcast.common.bo.SystemConfig;
import com.scyb.aisbroadcast.common.util.ComInterfaceUtil;
import com.scyb.aisbroadcast.common.util.SocketAisManager;
import com.scyb.aisbroadcast.common.util.SystemConfigUtil;
import com.scyb.aisbroadcast.common.util.TimerGeneralBroadcast;
import com.scyb.aisbroadcast.common.util.TimerNumericalBroadcast;

import javax.swing.JToolBar;


public class MainFrame extends JFrame {
	
	public static JTextArea textArea = new JTextArea();
	public static JTextArea textAreaAis = new JTextArea();
	
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public static JTextArea getTextAreaAis() {
		return textAreaAis;
	}

	public static void setTextAreaAis(JTextArea textAreaAis) {
		MainFrame.textAreaAis = textAreaAis;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		SystemConfigUtil.loadConfigFile();
//		new TimerGeneralBroadcast();
		new TimerNumericalBroadcast();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("\u4E32\u53E3\u8BBE\u7F6E");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetDialog dialog = new SetDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		menu.add(menuItem);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000, 270));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(1000, 300));
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		textAreaAis.setLineWrap(true);
		textAreaAis.setEditable(false);
		panel_2.add(new JScrollPane(textAreaAis), BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("");
		toolBar.add(label);
//		ComInterfaceUtil cifu = new ComInterfaceUtil();
//		System.out.println(SystemConfig.getAisCom());
//		System.out.println(SystemConfig.getAisComRate());
//		System.out.println(SystemConfig.getAisSocketIp());
//		System.out.println(SystemConfig.getAisSocketPort());
//		System.out.println(SystemConfig.getBdCom());
//		System.out.println(SystemConfig.getBdComRate());
//		cifu.connectionCom(SystemConfig.getBdCom(), SystemConfig.getBdComRate());
	}

}
