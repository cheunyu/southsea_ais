package com.scyb.aisbroadcast.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.scyb.aisbroadcast.common.bo.SystemCache;
import com.scyb.aisbroadcast.common.bo.SystemConfig;
import com.scyb.aisbroadcast.common.util.AisSerialController;
import com.scyb.aisbroadcast.common.util.BdSerialController;
import com.scyb.aisbroadcast.common.util.ComInterfaceUtil;
import com.scyb.aisbroadcast.common.util.IOManagerUtil;
import com.scyb.aisbroadcast.common.util.SocketAisManager;
import com.scyb.aisbroadcast.common.util.SystemConfigUtil;

public class SetDialog extends JDialog {
	private JLabel lblPort;
	private ComInterfaceUtil ciu = new ComInterfaceUtil();
	private JPanel panel;
	private JTextField textField_Ip1;
	private JTextField textField_Ip2;
	private JTextField textField_Ip3;
	private JTextField textField_Ip4;
	private JTextField textField_Port;
	private JRadioButton radioButton = new JRadioButton("Socket");
	private JRadioButton rdbtnNewRadioButton = new JRadioButton("串口");
	private JRadioButton radioButton_AisTemp = new JRadioButton();
	private ButtonGroup bgroup = new ButtonGroup();
	private RadioButtonListener radioButtonListener = new RadioButtonListener();
	private JComboBox comboBox_AisComRate = new JComboBox();
	private JComboBox comboBox_AisCom = new JComboBox();
	private JComboBox comboBox_BdCom = new JComboBox();
	private JComboBox comboBox_BdComRate = new JComboBox();
	private String comRate[] = { "115200", "57600", "38400", "19200", "9600", "4800", "2400", "1200" };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SetDialog dialog = new SetDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SetDialog() {
		String[] ip = SystemConfig.getAisSocketIp().split("\\.");
		setBounds(100, 100, 760, 500);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 0.0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.columnWidths = new int[] { 500 };
		gridBagLayout.rowHeights = new int[] { 300, 300, 33 };
		getContentPane().setLayout(gridBagLayout);
		{
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "Ais", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.insets = new Insets(0, 0, 5, 0);
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 0;
			gbc_panel_1.gridy = 0;
			getContentPane().add(panel_1, gbc_panel_1);
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			gbl_panel_1.columnWidths = new int[] { 200, 200 };
			gbl_panel_1.rowHeights = new int[] { 200, 100 };
			gbl_panel_1.columnWeights = new double[] { 1.0, 1.0 };
			gbl_panel_1.rowWeights = new double[] { 1.0, 1.0 };
			panel_1.setLayout(gbl_panel_1);
			{
				JPanel panel_2 = new JPanel();
				panel_2.setBorder(new TitledBorder(null, "Socket", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.insets = new Insets(0, 0, 5, 5);
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 0;
				gbc_panel_2.gridy = 0;
				panel_1.add(panel_2, gbc_panel_2);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				gbl_panel_2.rowHeights = new int[] { 0, 0 };
				gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, .0,
						Double.MIN_VALUE };
				gbl_panel_2.rowWeights = new double[] { 1, 1 };
				panel_2.setLayout(gbl_panel_2);
				{
					JLabel lblIp = new JLabel("IP");
					GridBagConstraints gbc_lblIp = new GridBagConstraints();
					gbc_lblIp.insets = new Insets(0, 0, 5, 5);
					gbc_lblIp.anchor = GridBagConstraints.WEST;
					gbc_lblIp.gridx = 1;
					gbc_lblIp.gridy = 0;
					panel_2.add(lblIp, gbc_lblIp);
				}
				{
					JLabel lblPort = new JLabel("Port");
					GridBagConstraints gbc_lblIp = new GridBagConstraints();
					gbc_lblIp.insets = new Insets(0, 0, 0, 5);
					gbc_lblIp.anchor = GridBagConstraints.EAST;
					gbc_lblIp.gridx = 1;
					gbc_lblIp.gridy = 1;
					panel_2.add(lblPort, gbc_lblIp);
				}
				{
					textField_Ip1 = new JTextField();
					GridBagConstraints gbc_textField = new GridBagConstraints();
					gbc_textField.insets = new Insets(0, 0, 5, 5);
					gbc_textField.anchor = GridBagConstraints.WEST;
					gbc_textField.gridx = 2;
					gbc_textField.gridy = 0;
					panel_2.add(textField_Ip1, gbc_textField);
					textField_Ip1.setColumns(4);
					textField_Ip1.setText(ip[0]);
				}
				{
					JLabel lblNewLabel = new JLabel(".");
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
					gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
					gbc_lblNewLabel.gridx = 3;
					gbc_lblNewLabel.gridy = 0;
					panel_2.add(lblNewLabel, gbc_lblNewLabel);
				}
				{
					textField_Ip2 = new JTextField();
					textField_Ip2.setColumns(4);
					GridBagConstraints gbc_textField_1 = new GridBagConstraints();
					gbc_textField_1.insets = new Insets(0, 0, 5, 5);
					gbc_textField_1.anchor = GridBagConstraints.WEST;
					gbc_textField_1.gridx = 4;
					gbc_textField_1.gridy = 0;
					panel_2.add(textField_Ip2, gbc_textField_1);
					textField_Ip2.setText(ip[1]);
				}
				{
					JLabel label = new JLabel(".");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.insets = new Insets(0, 0, 5, 5);
					gbc_label.anchor = GridBagConstraints.EAST;
					gbc_label.gridx = 5;
					gbc_label.gridy = 0;
					panel_2.add(label, gbc_label);
				}
				{
					textField_Ip3 = new JTextField();
					textField_Ip3.setColumns(4);
					GridBagConstraints gbc_textField_2 = new GridBagConstraints();
					gbc_textField_2.insets = new Insets(0, 0, 5, 5);
					gbc_textField_2.anchor = GridBagConstraints.WEST;
					gbc_textField_2.gridx = 6;
					gbc_textField_2.gridy = 0;
					panel_2.add(textField_Ip3, gbc_textField_2);
					textField_Ip3.setText(ip[2]);
				}
				{
					JLabel label = new JLabel(".");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.insets = new Insets(0, 0, 5, 5);
					gbc_label.anchor = GridBagConstraints.EAST;
					gbc_label.gridx = 7;
					gbc_label.gridy = 0;
					panel_2.add(label, gbc_label);
				}
				{
					textField_Ip4 = new JTextField();
					textField_Ip4.setColumns(4);
					GridBagConstraints gbc_textField_3 = new GridBagConstraints();
					gbc_textField_3.insets = new Insets(0, 0, 5, 0);
					gbc_textField_3.anchor = GridBagConstraints.WEST;
					gbc_textField_3.gridx = 8;
					gbc_textField_3.gridy = 0;
					panel_2.add(textField_Ip4, gbc_textField_3);
					textField_Ip4.setText(ip[3]);
				}
				{
					textField_Port = new JTextField();
					textField_Port.setColumns(6);
					GridBagConstraints gbc_textField_4 = new GridBagConstraints();
					gbc_textField_4.gridwidth = 2;
					gbc_textField_4.anchor = GridBagConstraints.WEST;
					gbc_textField_4.insets = new Insets(0, 0, 0, 5);
					gbc_textField_4.gridx = 2;
					gbc_textField_4.gridy = 1;
					panel_2.add(textField_Port, gbc_textField_4);
					textField_Port.setText(String.valueOf(SystemConfig.getAisSocketPort()));
				}
			}
			{
				JPanel panel_2 = new JPanel();
				panel_2.setBorder(
						new TitledBorder(null, "\u4E32\u53E3", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.insets = new Insets(0, 0, 5, 0);
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 1;
				gbc_panel_2.gridy = 0;
				panel_1.add(panel_2, gbc_panel_2);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[] { 0, 0, 0, 0, 0 };
				gbl_panel_2.rowHeights = new int[] { 0, 0 };
				gbl_panel_2.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
				gbl_panel_2.rowWeights = new double[] { 1, 1 };
				panel_2.setLayout(gbl_panel_2);
				{
					JLabel label = new JLabel("端口");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.insets = new Insets(0, 0, 5, 5);
					gbc_label.anchor = GridBagConstraints.WEST;
					gbc_label.gridx = 2;
					gbc_label.gridy = 0;
					panel_2.add(label, gbc_label);
				}
				{
					Object comArray[] = ciu.commPortCheck().toArray();
					comboBox_AisCom.setModel(new DefaultComboBoxModel(comArray));
					GridBagConstraints gbc_comboBox = new GridBagConstraints();
					gbc_comboBox.anchor = GridBagConstraints.WEST;
					gbc_comboBox.insets = new Insets(0, 0, 5, 0);
					gbc_comboBox.gridx = 3;
					gbc_comboBox.gridy = 0;
					panel_2.add(comboBox_AisCom, gbc_comboBox);
					for (int i = 0; i < comArray.length; i++) {
						if (SystemConfig.getAisCom().equals(comArray[i])) {
							comboBox_AisCom.setSelectedIndex(i);
						}
					}
				}
				{
					JLabel label = new JLabel("波特率");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.anchor = GridBagConstraints.EAST;
					gbc_label.insets = new Insets(0, 0, 0, 5);
					gbc_label.gridx = 2;
					gbc_label.gridy = 1;
					panel_2.add(label, gbc_label);
				}
				{
					comboBox_AisComRate.setModel(new DefaultComboBoxModel(comRate));
					GridBagConstraints gbc_comboBox = new GridBagConstraints();
					gbc_comboBox.anchor = GridBagConstraints.WEST;
					gbc_comboBox.gridx = 3;
					gbc_comboBox.gridy = 1;
					panel_2.add(comboBox_AisComRate, gbc_comboBox);
					for (int i = 0; i < comRate.length; i++) {
						if (SystemConfig.getAisComRate().equals(comRate[i])) {
							comboBox_AisComRate.setSelectedIndex(i);
						}
					}
				}
			}
			{
				JPanel panel_2 = new JPanel();
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.gridwidth = 2;
				gbc_panel_2.insets = new Insets(0, 0, 0, 5);
				gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_2.gridx = 0;
				gbc_panel_2.gridy = 1;
				panel_1.add(panel_2, gbc_panel_2);
				panel_2.setLayout(new FlowLayout(FlowLayout.RIGHT));
				radioButton.addActionListener(radioButtonListener);
				panel_2.add(radioButton);
				bgroup.add(radioButton);
				rdbtnNewRadioButton.addActionListener(radioButtonListener);
				panel_2.add(rdbtnNewRadioButton);
				bgroup.add(rdbtnNewRadioButton);
			}
		}
		{
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u5317\u6597",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.insets = new Insets(0, 0, 5, 0);
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 0;
			gbc_panel_1.gridy = 1;
			getContentPane().add(panel_1, gbc_panel_1);
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			gbl_panel_1.columnWidths = new int[] { 0, 0 };
			gbl_panel_1.rowHeights = new int[] { 0, 0 };
			gbl_panel_1.columnWeights = new double[] { 0, 0 };
			gbl_panel_1.rowWeights = new double[] { 0 };
			panel_1.setLayout(gbl_panel_1);
			{
				JLabel label = new JLabel("端口");
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.anchor = GridBagConstraints.EAST;
				gbc_label.gridx = 2;
				gbc_label.gridy = 1;
				panel_1.add(label, gbc_label);
			}
			{
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				Object comArray[] = ciu.commPortCheck().toArray();
				gbc_comboBox.insets = new Insets(0, 0, 5, 0);
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 3;
				gbc_comboBox.gridy = 1;
				panel_1.add(comboBox_BdCom, gbc_comboBox);
				comboBox_BdCom.setModel(new DefaultComboBoxModel(comArray));
				for (int i = 0; i < comArray.length; i++) {
					if (SystemConfig.getBdCom().equals(comArray[i])) {
						comboBox_BdCom.setSelectedIndex(i);
					}
				}
			}
			{
				JLabel label = new JLabel("波特率");
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.EAST;
				gbc_label.insets = new Insets(0, 0, 0, 5);
				gbc_label.gridx = 2;
				gbc_label.gridy = 2;
				panel_1.add(label, gbc_label);
			}
			{
				comboBox_BdComRate.setModel(new DefaultComboBoxModel(comRate));
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 3;
				gbc_comboBox.gridy = 2;
				panel_1.add(comboBox_BdComRate, gbc_comboBox);
				for (int i = 0; i < comRate.length; i++) {
					if (SystemConfig.getBdComRate().equals(comRate[i])) {
						comboBox_BdComRate.setSelectedIndex(i);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 2;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("\u4FDD\u5B58");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ComInterfaceUtil cifu = new ComInterfaceUtil();
								// 北斗配置写入缓存
								SystemConfig.setBdCom(comboBox_BdCom.getSelectedItem().toString());
								SystemConfig.setBdComRate(comboBox_BdComRate.getSelectedItem().toString());
								// AIS配置写入缓存
								StringBuffer ip = new StringBuffer();
								ip.append(textField_Ip1.getText()).append(".").append(textField_Ip2.getText())
										.append(".").append(textField_Ip3.getText()).append(".")
										.append(textField_Ip4.getText());
								SystemConfig.setAisSocketIp(ip.toString());
								SystemConfig.setAisSocketPort(Integer.parseInt(textField_Port.getText()));
								SystemConfig.setAisCom(comboBox_AisCom.getSelectedItem().toString());
								SystemConfig.setAisComRate(comboBox_AisComRate.getSelectedItem().toString());
								// 开启北斗串口监听
								try {
									BdSerialController bdSc = new BdSerialController();
									bdSc.openPort(SystemConfig.getBdCom(), SystemConfig.getBdComRate());
									SystemCache.setBdSc(bdSc);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// AIS连接方式判断并写入缓存
								if ("Socket".equals(radioButton_AisTemp.getText())) {
									IOManagerUtil.setAisCom(false);
									IOManagerUtil.setAisSocket(true);
									SystemConfig.setDefaultConnection(0);
									SocketAisManager.initialized();
								} else {
									/*
									 * 
									 * 
									 * 关闭AIS socket接口
									 * 
									 * 
									 */
									IOManagerUtil.setAisCom(true);
									IOManagerUtil.setAisSocket(false);
									SystemConfig.setDefaultConnection(1);
									// 开启AIS串口监听
									try {
										AisSerialController aisSc = new AisSerialController();
										aisSc.openPort(SystemConfig.getAisCom(), SystemConfig.getAisComRate());
										SystemCache.setAisSc(aisSc);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								// 保存当前配置到本地配置文件中
								SystemConfigUtil.saveProperties();

								/**
								 * 测试
								 * 
								 * GeneralForecast gf = new GeneralForecast();
								 * gf.setMmsi("987654321");
								 * gf.setTideHigh("14.2");
								 * gf.setTideHighTime("14:00");
								 * gf.setTideLow("10.5");
								 * gf.setTideLowTime("11:21");
								 * gf.setWaterTemperature("12");
								 * gf.setWaveHigh("3.2");
								 * IGeneralForecastService gfService = new
								 * GeneralForecastServiceImpl(); for(int
								 * i=0;i<100;i++) { try { Thread.sleep(5000);
								 * gfService.sendGeneralForcastService(gf); }
								 * catch (InterruptedException e) { // TODO
								 * Auto-generated catch block
								 * e.printStackTrace(); } }
								 */
							}
						}).start();
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("\u53D6\u6D88");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			getContentPane().add(panel, gbc_panel);
		}
		/* 判断AIS默认连接方式 */
		if (SystemConfig.getDefaultConnection() == 0) {
			radioButton.setSelected(true);
			comboBox_AisCom.setEnabled(false);
			comboBox_AisComRate.setEnabled(false);
		} else {
			rdbtnNewRadioButton.setSelected(true);
			textField_Ip1.setEnabled(false);
			textField_Ip2.setEnabled(false);
			textField_Ip3.setEnabled(false);
			textField_Ip4.setEnabled(false);
			textField_Port.setEnabled(false);
		}
	}

	public class RadioButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			radioButton_AisTemp = (JRadioButton) arg0.getSource();
			if (radioButton_AisTemp.isSelected()) {
				if ("Socket".equals(radioButton_AisTemp.getText())) {
					comboBox_AisCom.setEnabled(false);
					comboBox_AisComRate.setEnabled(false);
					textField_Ip1.setEnabled(true);
					textField_Ip2.setEnabled(true);
					textField_Ip3.setEnabled(true);
					textField_Ip4.setEnabled(true);
					textField_Port.setEnabled(true);
				} else {
					comboBox_AisCom.setEnabled(true);
					comboBox_AisComRate.setEnabled(true);
					textField_Ip1.setEnabled(false);
					textField_Ip2.setEnabled(false);
					textField_Ip3.setEnabled(false);
					textField_Ip4.setEnabled(false);
					textField_Port.setEnabled(false);
				}
			}
		}

	}

}
