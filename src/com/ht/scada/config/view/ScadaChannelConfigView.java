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

public class ScadaChannelConfigView extends ViewPart implements
		IPropertyChangeListener {

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

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
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
