package com.ht.scada.config.view;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
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

import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.service.AcquisitionChannelService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ScadaChannelIndexView extends ViewPart implements IPropertyChangeListener {

	public ScadaChannelIndexView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaChannelIndexView";
	private AcquisitionDevice acquisitionDevice;

	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionChannelService");
	private Text textChannelName;
	private Text textIdx;
	private Text textOffline;
	private Text textInterval;
	private Text textSchedule;
	private Text textPortInfo;
	private Text textFrames;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
		groupBasicInfo.setText("采集时间");
		groupBasicInfo.setLayout(new GridLayout(2, false));
		
		Label labelName = new Label(groupBasicInfo, SWT.NONE);
		labelName.setText("通道名称：");
		
		textChannelName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textChannelName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textChannelName.widthHint = 120;
		textChannelName.setLayoutData(gd_textChannelName);
		textChannelName.setText("<dynamic>");
		
		Label labelProtocal = new Label(groupBasicInfo, SWT.NONE);
		labelProtocal.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelProtocal.setText("通信规约：");
		
		Combo comboProtocal = new Combo(groupBasicInfo, SWT.READ_ONLY);
		comboProtocal.setItems(new String[] {"IEC104", "ModbusTCP", "ModbusRTU", "DL645"});
		GridData gd_comboProtocal = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboProtocal.widthHint = 120;
		comboProtocal.setLayoutData(gd_comboProtocal);
		comboProtocal.select(0);
		
		Label labelIdx = new Label(groupBasicInfo, SWT.NONE);
		labelIdx.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelIdx.setText("序      号：");
		
		textIdx = new Text(groupBasicInfo, SWT.BORDER);
		textIdx.setText("<dynamic>");
		GridData gd_textIdx = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textIdx.widthHint = 120;
		textIdx.setLayoutData(gd_textIdx);
		
		Group groupCommuInfo = new Group(composite, SWT.NONE);
		groupCommuInfo.setText("通讯信息");
		groupCommuInfo.setLayout(new GridLayout(2, false));
		FormData fd_groupCommuInfo = new FormData();
		fd_groupCommuInfo.bottom = new FormAttachment(groupBasicInfo, 209, SWT.BOTTOM);
		fd_groupCommuInfo.top = new FormAttachment(groupBasicInfo, 3);
		fd_groupCommuInfo.right = new FormAttachment(groupBasicInfo, 0, SWT.RIGHT);
		fd_groupCommuInfo.left = new FormAttachment(0, 10);
		groupCommuInfo.setLayoutData(fd_groupCommuInfo);
		
		Label labelOffline = new Label(groupCommuInfo, SWT.NONE);
		labelOffline.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelOffline.setText("通讯离线：");
		
		textOffline = new Text(groupCommuInfo, SWT.BORDER);
		textOffline.setText("<dynamic>");
		GridData gd_textOffline = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textOffline.widthHint = 120;
		textOffline.setLayoutData(gd_textOffline);
		
		Label labelInterval = new Label(groupCommuInfo, SWT.NONE);
		labelInterval.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelInterval.setText("采样间隔：");
		
		textInterval = new Text(groupCommuInfo, SWT.BORDER);
		textInterval.setText("<dynamic>");
		GridData gd_textInterval = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textInterval.widthHint = 120;
		textInterval.setLayoutData(gd_textInterval);
		
		Label labelSchedule = new Label(groupCommuInfo, SWT.NONE);
		labelSchedule.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelSchedule.setText("任务调度：");
		
		textSchedule = new Text(groupCommuInfo, SWT.BORDER);
		textSchedule.setText("<dynamic>");
		GridData gd_textSchedule = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textSchedule.widthHint = 120;
		textSchedule.setLayoutData(gd_textSchedule);
		
		Label labelPortInfo = new Label(groupCommuInfo, SWT.NONE);
		labelPortInfo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPortInfo.setText("端口信息：");
		
		textPortInfo = new Text(groupCommuInfo, SWT.BORDER);
		textPortInfo.setText("<dynamic>");
		GridData gd_textPortInfo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textPortInfo.widthHint = 120;
		textPortInfo.setLayoutData(gd_textPortInfo);
		
		Label labelFrames = new Label(groupCommuInfo, SWT.NONE);
		labelFrames.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelFrames.setText("通 讯 帧：");
		
		textFrames = new Text(groupCommuInfo, SWT.BORDER);
		textFrames.setText("<dynamic>");
		GridData gd_textFrames = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textFrames.widthHint = 120;
		textFrames.setLayoutData(gd_textFrames);
		
		Label label = new Label(groupCommuInfo, SWT.NONE);
		label.setText("上次更新：");
		
		DateTime dateTime = new DateTime(groupCommuInfo, SWT.BORDER);
		GridData gd_dateTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dateTime.widthHint = 120;
		dateTime.setLayoutData(gd_dateTime);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(groupBasicInfo, 0, SWT.RIGHT);
		fd_composite_1.top = new FormAttachment(groupCommuInfo, 8);
		fd_composite_1.left = new FormAttachment(groupBasicInfo, 0, SWT.LEFT);
		composite_1.setLayoutData(fd_composite_1);
		
		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.setBounds(40, 10, 61, 27);
		btnSave.setText("保存(S)");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCancel.setText("取消(C)");
		btnCancel.setBounds(151, 10, 61, 27);

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("area", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.ACQUISITIONCHANNEL_ADD)) {
			acquisitionDevice = new AcquisitionDevice();
			Object object = event.getNewValue();
			if (object instanceof AcquisitionDevice) {
				//acquisitionDevice.setChannel((AcquisitionDevice) object);
			} else {
				acquisitionDevice.setChannel(null);
			}
//			// 初始化控件值
//			textIndex.setText("");
//			textType.setText("");
			
		} else if (event.getProperty().equals(
				FirePropertyConstants.AREAMINOR_EDIT)) {
			//acquisitionDevice = (AreaMinorTag) event.getNewValue();

//			// 初始化控件值
//			textIndex.setText(areaMinorTag.getName());
//			
//			String typeStr = areaMinorTag.getType();
//			if(typeStr == null){
//				typeStr = "";
//			} else {
//			    textType.setText(areaMinorTag.getType());
//			}
			

		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("area");
		super.dispose();
	}
}
