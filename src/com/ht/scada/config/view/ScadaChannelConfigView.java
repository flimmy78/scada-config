package com.ht.scada.config.view;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.common.tag.type.entity.CommunicationProtocalType;
import com.ht.scada.common.tag.type.service.TypeService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.ViewPropertyChange;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;

public class ScadaChannelConfigView extends ViewPart implements
		IPropertyChangeListener {

	private Combo comboBaud;
	private Combo comboDatabit;
	private Combo comboCheckbit;
	private Combo comboStopbit;
	
	private String [] comboBaudArray = new String[] {"9600", "19200", "38400", "57600", "115200"};
	private String [] comboDatabitArray = new String[] {"5", "6", "7", "8"};
	private String [] comboCheckbitArray = new String[] {"无", "奇", "偶"};
	private String [] comboStopbitArray = new String[] {"1", "1.5", "2"};
	
	private CTabFolder tabFolderPortInfor;
	
	public ScadaChannelConfigView() {
		protocalTypeList = typeService.getAllCommunicationProtocalType();
		if(protocalTypeList!=null && !protocalTypeList.isEmpty()) {
			protocalTypeStr = new String[protocalTypeList.size()];
			int i = 0;
			for(CommunicationProtocalType type : protocalTypeList) {
				protocalTypeStr[i] = type.getValue();
				i++;
			}
		}
		
	}

	public static final String ID = "com.ht.scada.config.view.ScadaChannelConfigView";
	public static TreeViewer treeViewer;
	private AcquisitionChannel acquisitionChannel;

	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator
			.getDefault().getApplicationContext()
			.getBean("acquisitionChannelService");
	private TypeService typeService = (TypeService) Activator.getDefault()
			.getApplicationContext().getBean("typeService");
	private Text textChannelName;
	private Combo comboProtocal;
	private Text textIdx;
	private Text textOffline;
	private Text textInterval;
	private Text textSchedule;
	private Text textPortInfo;
	private Text textFrames;
	private DateTime dateTimeUpdateTime;
	private Label labelProtocal;
	private List<CommunicationProtocalType> protocalTypeList;
	private String[] protocalTypeStr = new String[]{""};
	private Text textIP;
	private Text textPort;
	private Text textDTUID;
	private Text textDtuPort;
	private Text textHeart;
	private Text textHeartInteval;
	private Text textSerialPort;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.heightHint = 435;
		gd_composite.widthHint = 282;
		composite.setLayoutData(gd_composite);

		Group groupBasicInfo = new Group(composite, SWT.NONE);
		FormData fd_groupBasicInfo = new FormData();
		fd_groupBasicInfo.top = new FormAttachment(0, 10);
		fd_groupBasicInfo.left = new FormAttachment(0, 10);
		fd_groupBasicInfo.bottom = new FormAttachment(0, 127);
		fd_groupBasicInfo.right = new FormAttachment(0, 260);
		groupBasicInfo.setLayoutData(fd_groupBasicInfo);
		groupBasicInfo.setText("基本信息");
		groupBasicInfo.setLayout(new GridLayout(2, false));

		Label labelName = new Label(groupBasicInfo, SWT.NONE);
		labelName.setText("通道名称：");

		textChannelName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textChannelName = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_textChannelName.widthHint = 120;
		textChannelName.setLayoutData(gd_textChannelName);

		labelProtocal = new Label(groupBasicInfo, SWT.NONE);
		labelProtocal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		labelProtocal.setText("通信规约：");

		comboProtocal = new Combo(groupBasicInfo, SWT.READ_ONLY);
		comboProtocal.setItems(protocalTypeStr);
		GridData gd_comboProtocal = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_comboProtocal.widthHint = 100;
		comboProtocal.setLayoutData(gd_comboProtocal);
		comboProtocal.select(0);

		Label labelIdx = new Label(groupBasicInfo, SWT.NONE);
		labelIdx.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		labelIdx.setText("序      号：");

		textIdx = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textIdx = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_textIdx.widthHint = 120;
		textIdx.setLayoutData(gd_textIdx);

		Group groupCommuInfo = new Group(composite, SWT.NONE);
		groupCommuInfo.setText("通讯信息");
		groupCommuInfo.setLayout(new GridLayout(3, false));
		FormData fd_groupCommuInfo = new FormData();
		fd_groupCommuInfo.bottom = new FormAttachment(groupBasicInfo, 209,
				SWT.BOTTOM);
		fd_groupCommuInfo.top = new FormAttachment(groupBasicInfo, 3);
		fd_groupCommuInfo.right = new FormAttachment(groupBasicInfo, 0,
				SWT.RIGHT);
		fd_groupCommuInfo.left = new FormAttachment(0, 10);
		groupCommuInfo.setLayoutData(fd_groupCommuInfo);

		Label labelOffline = new Label(groupCommuInfo, SWT.NONE);
		labelOffline.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		labelOffline.setText("通讯离线：");

		textOffline = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textOffline = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_textOffline.widthHint = 120;
		textOffline.setLayoutData(gd_textOffline);
		new Label(groupCommuInfo, SWT.NONE);

		Label labelInterval = new Label(groupCommuInfo, SWT.NONE);
		labelInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		labelInterval.setText("帧 间 隔：");

		textInterval = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textInterval = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_textInterval.widthHint = 120;
		textInterval.setLayoutData(gd_textInterval);
		
		Label lblMs = new Label(groupCommuInfo, SWT.NONE);
		lblMs.setText("ms");

		textSchedule = new Text(groupCommuInfo, SWT.BORDER);
		textSchedule.setVisible(false);
		GridData gd_textSchedule = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_textSchedule.exclude = true;
		gd_textSchedule.widthHint = 120;
		textSchedule.setLayoutData(gd_textSchedule);
		
				Label labelPortInfo = new Label(groupCommuInfo, SWT.NONE);
				labelPortInfo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
						false, 1, 1));
				labelPortInfo.setText("端口信息：");
		
				textPortInfo = new Text(groupCommuInfo, SWT.BORDER | SWT.WRAP | SWT.MULTI);
				GridData gd_textPortInfo = new GridData(SWT.FILL, SWT.CENTER, true,
						false, 2, 1);
				gd_textPortInfo.heightHint = 44;
				gd_textPortInfo.widthHint = 150;
				textPortInfo.setLayoutData(gd_textPortInfo);
		
				Label labelFrames = new Label(groupCommuInfo, SWT.NONE);
				labelFrames.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
						false, 1, 1));
				labelFrames.setText("通 讯 帧：");
				
						textFrames = new Text(groupCommuInfo, SWT.BORDER | SWT.WRAP);
						GridData gd_textFrames = new GridData(SWT.FILL, SWT.CENTER, true,
								false, 2, 1);
						gd_textFrames.heightHint = 47;
						gd_textFrames.widthHint = 150;
						textFrames.setLayoutData(gd_textFrames);
				new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		
				dateTimeUpdateTime = new DateTime(groupCommuInfo, SWT.BORDER);
				dateTimeUpdateTime.setVisible(false);
				GridData gd_dateTimeUpdateTime = new GridData(SWT.LEFT, SWT.CENTER,
						false, false, 1, 1);
				gd_dateTimeUpdateTime.exclude = true;
				gd_dateTimeUpdateTime.widthHint = 120;
				dateTimeUpdateTime.setLayoutData(gd_dateTimeUpdateTime);
		new Label(groupCommuInfo, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(groupBasicInfo, 0, SWT.RIGHT);
		fd_composite_1.top = new FormAttachment(groupCommuInfo, 8);
				new Label(groupCommuInfo, SWT.NONE);
		
				Label labelUpdateTime = new Label(groupCommuInfo, SWT.NONE);
				labelUpdateTime.setVisible(false);
				labelUpdateTime.setText("上次更新：");
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		fd_composite_1.left = new FormAttachment(groupBasicInfo, 0, SWT.LEFT);
		composite_1.setLayoutData(fd_composite_1);

		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (acquisitionChannel.getId() == null) {// 添加采集通道
					if ("".equals(textChannelName.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通道名称不能为空！");
						return;
					}
					else if ("".equals(textIdx.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"序号不能为空！");
						return;
					}else if ("".equals(textOffline.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通讯离线不能为空！");
						return;
					}else if ("".equals(textInterval.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"帧间隔不能为空！");
						return;
//					}else if ("".equals(textSchedule.getText().trim())) {
//						MessageDialog.openError(getSite().getShell(), "错误",
//								"任务调度不能为空！");
//						return;
					}else if ("".equals(textPortInfo.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"端口信息不能为空！");
						return;
					}else if ("".equals(textFrames.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通讯帧不能为空！");
						return;
					}
					
					acquisitionChannel.setName(textChannelName.getText().trim());
					acquisitionChannel.setProtocal(getCommPotclType(comboProtocal.getText()).getName());
					acquisitionChannel.setIdx(Integer.valueOf(textIdx.getText().trim()));
					acquisitionChannel.setOffline(Integer.valueOf(textOffline.getText()));
					acquisitionChannel.setIntvl(Integer.valueOf(textInterval.getText()));
//					acquisitionChannel.setSchedule(textSchedule.getText().trim());
					acquisitionChannel.setPortInfo(textPortInfo.getText().trim());
					acquisitionChannel.setFrames(textFrames.getText().trim());
					
//					Date nowDate = new Date();
//					acquisitionChannel.setUpdateTime(nowDate);
					
					// 更新数据库
					acquisitionChannelService.create(acquisitionChannel);
					
					ScadaDeviceTreeView.treeViewer.add("采集通道", acquisitionChannel);
					ScadaDeviceTreeView.treeViewer.setExpandedState("采集通道", true);
					
				}
				else {// 编辑
					if ("".equals(textChannelName.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通道名称不能为空！");
						return;
					}
					else if ("".equals(textIdx.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"序号不能为空！");
						return;
					}else if ("".equals(textOffline.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通讯离线不能为空！");
						return;
					}else if ("".equals(textInterval.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"帧间隔不能为空！");
						return;
//					}else if ("".equals(textSchedule.getText().trim())) {
//						MessageDialog.openError(getSite().getShell(), "错误",
//								"任务调度不能为空！");
//						return;
					}else if ("".equals(textPortInfo.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"端口信息不能为空！");
						return;
					}else if ("".equals(textFrames.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"通讯帧不能为空！");
						return;
					}
					
					acquisitionChannel.setName(textChannelName.getText().trim());
					acquisitionChannel.setProtocal(getCommPotclType(comboProtocal.getText()).getName());
					acquisitionChannel.setIdx(Integer.valueOf(textIdx.getText().trim()));
					acquisitionChannel.setOffline(Integer.valueOf(textOffline.getText()));
					acquisitionChannel.setIntvl(Integer.valueOf(textInterval.getText()));
//					acquisitionChannel.setSchedule(textSchedule.getText().trim());
					acquisitionChannel.setPortInfo(textPortInfo.getText().trim());
					acquisitionChannel.setFrames(textFrames.getText().trim());
					
//					int year = dateTimeUpdateTime.getYear();
//					int month = dateTimeUpdateTime.getMonth();
//					int day = dateTimeUpdateTime.getDay();
//					Date updateTime = new Date(year-1900,month,day);

//					acquisitionChannel.setUpdateTime(updateTime);
					
					// 更新数据库
					acquisitionChannelService.update(acquisitionChannel);
					ScadaDeviceTreeView.treeViewer.update(acquisitionChannel,null);
				}

				LayoutUtil.hideViewPart();
			}
		});
		btnSave.setBounds(40, 10, 61, 27);
		btnSave.setText("保存(S)");

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutUtil.hideViewPart();
			}
		});
	
		btnCancel.setText("取消(C)");
		btnCancel.setBounds(151, 10, 61, 27);
		
		Composite composite_2 = new Composite(parent, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_2.heightHint = 273;
		gd_composite_2.widthHint = 310;
		composite_2.setLayoutData(gd_composite_2);
		
		Group group_1 = new Group(composite_2, SWT.NONE);
		group_1.setText("端口信息");
		group_1.setBounds(10, 10, 290, 175);
		
	    tabFolderPortInfor = new CTabFolder(group_1, SWT.BORDER);
		tabFolderPortInfor.setBounds(10, 39, 270, 126);
		tabFolderPortInfor.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmTcpIp = new CTabItem(tabFolderPortInfor, SWT.NONE);
		tbtmTcpIp.setText("tcp/ip");
		
		Composite composite_3 = new Composite(tabFolderPortInfor, SWT.NONE);
		tbtmTcpIp.setControl(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
		
		Label labelIP = new Label(composite_3, SWT.NONE);
		labelIP.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelIP.setText("IP   ：");
		
		textIP = new Text(composite_3, SWT.BORDER);
		GridData gd_textIP = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textIP.widthHint = 144;
		textIP.setLayoutData(gd_textIP);
		
		Label labelPort = new Label(composite_3, SWT.NONE);
		labelPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPort.setText("端口：");
		
		textPort = new Text(composite_3, SWT.BORDER);
		
		CTabItem tbtmDtu = new CTabItem(tabFolderPortInfor, SWT.NONE);
		tbtmDtu.setText("dtu");
		
		Composite composite_4 = new Composite(tabFolderPortInfor, SWT.NONE);
		tbtmDtu.setControl(composite_4);
		composite_4.setLayout(new GridLayout(5, false));
		
		Label labelDTUID = new Label(composite_4, SWT.NONE);
		labelDTUID.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelDTUID.setText("DTU-ID：");
		
		textDTUID = new Text(composite_4, SWT.BORDER);
		GridData gd_textDTUID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textDTUID.widthHint = 62;
		textDTUID.setLayoutData(gd_textDTUID);
		
		Label labelDtuPort = new Label(composite_4, SWT.NONE);
		labelDtuPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelDtuPort.setText("端口：");
		
		textDtuPort = new Text(composite_4, SWT.BORDER);
		
		Label labelHeart = new Label(composite_4, SWT.NONE);
		labelHeart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelHeart.setText("心跳信号：");
		
		textHeart = new Text(composite_4, SWT.BORDER);
		GridData gd_textHeart = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_textHeart.widthHint = 60;
		textHeart.setLayoutData(gd_textHeart);
		new Label(composite_4, SWT.NONE);
		new Label(composite_4, SWT.NONE);
		
		Label labelHeartInterval = new Label(composite_4, SWT.NONE);
		labelHeartInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		labelHeartInterval.setText("心跳信号间隔：");
		
		textHeartInteval = new Text(composite_4, SWT.BORDER);
		GridData gd_textHeartInteval = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textHeartInteval.widthHint = 38;
		textHeartInteval.setLayoutData(gd_textHeartInteval);
		new Label(composite_4, SWT.NONE);
		
		CTabItem tbtmSerial = new CTabItem(tabFolderPortInfor, SWT.NONE);
		tbtmSerial.setText("serial");
		
		Composite composite_5 = new Composite(tabFolderPortInfor, SWT.NONE);
		tbtmSerial.setControl(composite_5);
		composite_5.setLayout(new GridLayout(4, false));
		
		Label labelSerialPort = new Label(composite_5, SWT.NONE);
		labelSerialPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelSerialPort.setText("端口：");
		
		textSerialPort = new Text(composite_5, SWT.BORDER);
		textSerialPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelBaud = new Label(composite_5, SWT.NONE);
		labelBaud.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelBaud.setText("波特率：");
		
		comboBaud = new Combo(composite_5, SWT.NONE);
		comboBaud.setItems(comboBaudArray);
		comboBaud.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelData = new Label(composite_5, SWT.NONE);
		labelData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelData.setText("数据位：");
		
		comboDatabit = new Combo(composite_5, SWT.NONE);
		comboDatabit.setItems(comboDatabitArray);
		comboDatabit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelCheckbit = new Label(composite_5, SWT.NONE);
		labelCheckbit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelCheckbit.setText("校验位：");
		
		comboCheckbit = new Combo(composite_5, SWT.NONE);
		comboCheckbit.setItems(comboCheckbitArray);
		comboCheckbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label labelStopbit = new Label(composite_5, SWT.NONE);
		labelStopbit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelStopbit.setText("停止位：");
		
		comboStopbit = new Combo(composite_5, SWT.NONE);
		comboStopbit.setItems(comboStopbitArray);
		comboStopbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_5, SWT.NONE);
		new Label(composite_5, SWT.NONE);

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("channel", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(
				FirePropertyConstants.ACQUISITIONCHANNEL_ADD)) {
			acquisitionChannel = new AcquisitionChannel();
			Object object = event.getNewValue();
			if (object instanceof AcquisitionChannel) {
				// acquisitionDevice.setChannel((AcquisitionDevice) object);
			} else {
				// acquisitionChannel.(null);
			}
			// ============初始化控件值============
			textChannelName.setText("");
			comboProtocal.setText("");
			textIdx.setText("");
			textOffline.setText("");
			textInterval.setText("");
//			textSchedule.setText("");
			textPortInfo.setText("");
			textFrames.setText("");
//			Date nowDate = new Date();
//			Calendar nowCalendar = Utils.date2CalendarUtil(nowDate);
//			dateTimeUpdateTime.setDate(nowCalendar.get(Calendar.YEAR),
//					nowCalendar.get(Calendar.MONTH),
//					nowCalendar.get(Calendar.DAY_OF_MONTH));
		} else if (event.getProperty().equals(
				FirePropertyConstants.ACQUISITIONCHANNEL_EDIT)) {
			acquisitionChannel = (AcquisitionChannel) event.getNewValue();

			// ============初始化控件值============
			textChannelName.setText(acquisitionChannel.getName());
			comboProtocal.setText(acquisitionChannel.getProtocal()==null?"":getCommPotclType(acquisitionChannel.getProtocal()).getValue());
			textIdx.setText(acquisitionChannel.getIdx().toString());
			textOffline.setText(acquisitionChannel.getOffline() + "");
			textInterval.setText(String.valueOf(acquisitionChannel
					.getIntvl()));
//			textSchedule.setText(acquisitionChannel.getSchedule());
			textPortInfo.setText(acquisitionChannel.getPortInfo());
			textFrames.setText(acquisitionChannel.getFrames());

//			Date updateTime = acquisitionChannel.getUpdateTime();
//			Calendar updateTimeCalendar = Utils.date2CalendarUtil(updateTime);
//			int year = updateTimeCalendar.get(Calendar.YEAR);
//			int month = updateTimeCalendar.get(Calendar.MONTH);
//			int day = updateTimeCalendar.get((Calendar.DAY_OF_MONTH));
//			dateTimeUpdateTime.setDate(year, month, day);
			// ====================================
			
			//20130713,王蓬====
			/**
			 * 物理信息
			 * TCP/IP 通讯方式：	【通讯方式】|【IP】:【端口】
			 * 					tcp/ip|192.168.1.110:4660
			 * DTU 通讯方式：		【通讯方式】|【DTU-ID】:【端口】:【心跳信号】:【心跳信号间隔】
			 * 					dtu|2000:9815:hello:180
			 * 串口通讯方式:		【通讯方式】|【端口】：【波特率】:【数据位(5/6/7/8)】:【校验位(无/奇/偶)】:【停止位(1/1.5/2)】
			 * 					serial|1:9600:8:无:1
			 */
			//==========================控制端口信息=================================================
			String portInfor = acquisitionChannel.getPortInfo();	//获得端口信息字符串
			String tongXinType = portInfor.split("\\|")[0]; 		//获得通讯方式
			tabFolderPortInfor.setSelection(1);
			if(tongXinType.equals("tcp/ip")) {
				tabFolderPortInfor.setSelection(0);
				
				//控件初始化
				String tcpIp = portInfor.split("\\|")[1].split(":")[0];
				String tcpIpPort = portInfor.split("\\|")[1].split(":")[1];
				textIP.setText(tcpIp);
				textPort.setText(tcpIpPort);
				
				//其余输入框清空
				textDTUID.setText("");
				textDtuPort.setText("");
				textHeart.setText("");
				textHeartInteval.setText(""); //
				textSerialPort.setText("");	
				comboBaud.select(-1);
				comboDatabit.select(-1);
				comboCheckbit.select(-1);
				comboStopbit.select(-1);
							
			} else if (tongXinType.equals("dtu")) {
				tabFolderPortInfor.setSelection(1);
				
				//控件初始化
				String dtuID = portInfor.split("\\|")[1].split(":")[0];
				String dtuPort = portInfor.split("\\|")[1].split(":")[1];
				String heart = portInfor.split("\\|")[1].split(":")[2];
				String heartInteval = portInfor.split("\\|")[1].split(":")[3];
				textDTUID.setText(dtuID);
				textDtuPort.setText(dtuPort);
				textHeart.setText(heart);
				textHeartInteval.setText(heartInteval);
				
				//其余输入框清空
				textIP.setText("");
				textPort.setText("");//
				textSerialPort.setText("");	
				comboBaud.select(-1);
				comboDatabit.select(-1);
				comboCheckbit.select(-1);
				comboStopbit.select(-1);
				
				
			} else {
				tabFolderPortInfor.setSelection(2);
				//serial|1:9600:8:无:1
				
				//控件初始化
				String serialPort = portInfor.split("\\|")[1].split(":")[0];
				String baud = portInfor.split("\\|")[1].split(":")[1];
				String databit = portInfor.split("\\|")[1].split(":")[2];
				String checkbit = portInfor.split("\\|")[1].split(":")[3];
				String stopbit =portInfor.split("\\|")[1].split(":")[4];
				textSerialPort.setText(serialPort);
				//初始化波特率combo
				if(baud!=null){
					for ( int i=0; i<this.comboBaudArray.length; i++) {
						if ( baud.equals(this.comboBaudArray[i])) {
							this.comboBaud.select(i);
						}
					}
				}
				//初始化数据位
				if(databit!=null) {
					for( int i=0; i< this.comboDatabitArray.length; i++) {
						if(databit.equals(this.comboDatabitArray[i])) {
							this.comboDatabit.select(i);
						}
					}
				}
				//初始化校验位
				if(checkbit!=null) {
					for (int i=0; i<this.comboCheckbitArray.length; i++) {
						if(checkbit.equals(this.comboCheckbitArray[i])) {
							this.comboCheckbit.select(i);
						}
					}
				}
				//初始化停止位
				if(stopbit!=null) {
					for(int i=0;i<this.comboStopbitArray.length;i++) {
						if(stopbit.equals(this.comboStopbitArray[i])){
							this.comboStopbit.select(i);
						}
					}
				}
				
				//其余输入框清空
				textIP.setText("");
				textPort.setText("");//
				textDTUID.setText("");
				textDtuPort.setText("");
				textHeart.setText("");
				textHeartInteval.setText("");
				
			}
			
					
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("channel");
		super.dispose();
	}
	
	private CommunicationProtocalType getCommPotclType(String key) {
		if(protocalTypeList != null && !protocalTypeList.isEmpty()) {
			for(CommunicationProtocalType type : protocalTypeList) {
				if(type.getName().equals(key) || type.getValue().equals(key)) {
					return type;
				}
			}
		}
		return null;
	}
}
