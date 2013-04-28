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

public class ScadaDeviceIndexView extends ViewPart implements IPropertyChangeListener {

	public ScadaDeviceIndexView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaDeviceIndexView";
	private AcquisitionDevice acquisitionDevice;

	private AcquisitionChannelService acquisitionChannelService = (AcquisitionChannelService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionChannelService");
	private Text textDeviceName;
	private Text textType;
	private Text textAddress;
	private Text textTimeout;
	private Text textRetry;
	private Text textManufacture;
	private Text textFixPositin;
	private Text textRemark;

	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 20;
		gl_parent.marginTop = 25;
		gl_parent.marginLeft = 40;
		parent.setLayout(gl_parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 515;
		gd_composite.widthHint = 300;
		composite.setLayoutData(gd_composite);
		
		Group groupBasicInfo = new Group(composite, SWT.NONE);
		GridData gd_groupBasicInfo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_groupBasicInfo.widthHint = 253;
		groupBasicInfo.setLayoutData(gd_groupBasicInfo);
		groupBasicInfo.setText("基本信息");
		groupBasicInfo.setLayout(new GridLayout(2, false));
		
		Label labelDeviceName = new Label(groupBasicInfo, SWT.NONE);
		labelDeviceName.setText("设备名称：");
		
		textDeviceName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textDeviceName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDeviceName.widthHint = 120;
		textDeviceName.setLayoutData(gd_textDeviceName);
		textDeviceName.setText("<dynamic>");
		
		Label labelManufacture = new Label(groupBasicInfo, SWT.NONE);
		labelManufacture.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelManufacture.setText("生产厂家：");
		
		textManufacture = new Text(groupBasicInfo, SWT.BORDER);
		textManufacture.setText("<dynamic>");
		GridData gd_textManufacture = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textManufacture.widthHint = 120;
		textManufacture.setLayoutData(gd_textManufacture);
		
		Label labelType = new Label(groupBasicInfo, SWT.NONE);
		labelType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelType.setText("型      号：");
		
		textType = new Text(groupBasicInfo, SWT.BORDER);
		textType.setText("<dynamic>");
		GridData gd_textType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textType.widthHint = 120;
		textType.setLayoutData(gd_textType);
		
		Group groupCommuInfo = new Group(composite, SWT.NONE);
		GridData gd_groupCommuInfo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_groupCommuInfo.widthHint = 253;
		groupCommuInfo.setLayoutData(gd_groupCommuInfo);
		groupCommuInfo.setText("通讯信息");
		
		Label labelFixTime = new Label(groupBasicInfo, SWT.NONE);
		labelFixTime.setText("安装日期：");
		
		DateTime dateTimeFixTime = new DateTime(groupBasicInfo, SWT.BORDER);
		GridData gd_dateTimeFixTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dateTimeFixTime.widthHint = 120;
		dateTimeFixTime.setLayoutData(gd_dateTimeFixTime);
		
		Label labelFixPositin = new Label(groupBasicInfo, SWT.NONE);
		labelFixPositin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelFixPositin.setText("安装位置：");
		
		textFixPositin = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textFixPositin = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textFixPositin.widthHint = 120;
		textFixPositin.setLayoutData(gd_textFixPositin);
		
		Label labelRemark = new Label(groupBasicInfo, SWT.NONE);
		labelRemark.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelRemark.setText("备      注：");
		
		textRemark = new Text(groupBasicInfo, SWT.BORDER);
		textRemark.setText("");
		GridData gd_textRemark = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textRemark.widthHint = 120;
		textRemark.setLayoutData(gd_textRemark);
		groupCommuInfo.setLayout(new GridLayout(3, false));
		
		Label labelAddress = new Label(groupCommuInfo, SWT.NONE);
		labelAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelAddress.setText("设备地址：");
		
		textAddress = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textAddress = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textAddress.widthHint = 120;
		textAddress.setLayoutData(gd_textAddress);
		textAddress.setText("<dynamic>");
		new Label(groupCommuInfo, SWT.NONE);
		
		Label labelTimeout = new Label(groupCommuInfo, SWT.NONE);
		labelTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelTimeout.setText("通讯超时：");
		
		textTimeout = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textTimeout = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textTimeout.widthHint = 120;
		textTimeout.setLayoutData(gd_textTimeout);
		textTimeout.setText("<dynamic>");
		
		Label lblMs = new Label(groupCommuInfo, SWT.NONE);
		lblMs.setText("ms");
		
		Label labelRetry = new Label(groupCommuInfo, SWT.NONE);
		labelRetry.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelRetry.setText("重发次数：");
		
		textRetry = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textRetry = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textRetry.widthHint = 120;
		textRetry.setLayoutData(gd_textRetry);
		textRetry.setText("<dynamic>");
		new Label(groupCommuInfo, SWT.NONE);
		
		Button button = new Button(groupCommuInfo, SWT.CHECK);
		button.setSelection(true);
		button.setText("启用");
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 48;
		gd_composite_1.widthHint = 253;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(null);
		
		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.setBounds(42, 10, 60, 27);
		btnSave.setText("保存(S)");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCancel.setText("取消(C)");
		btnCancel.setBounds(143, 10, 60, 27);

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
