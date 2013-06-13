package com.ht.scada.config.view;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.AcquisitionDevice;
import com.ht.scada.common.tag.service.AcquisitionDeviceService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.Utils;
import com.ht.scada.config.util.ViewPropertyChange;

public class ScadaDeviceConfigView extends ViewPart implements IPropertyChangeListener {

	public ScadaDeviceConfigView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaDeviceConfigView";
	private AcquisitionDevice acquisitionDevice = new AcquisitionDevice();

	private AcquisitionDeviceService acquisitionDeviceService = (AcquisitionDeviceService) Activator.getDefault()
			.getApplicationContext().getBean("acquisitionDeviceService");
	
	private Text textDeviceName;
	private Text textType;
	private DateTime dateTimeFixTime;
	private Text textAddress;
	private Text textTimeout;
	private Text textRetry;
	private Text textManufacture;
	private Text textFixPositin;
	private Text textRemark;
	private Button btnUsed;
	private Text text_number;
	private Text text_checkInterval;

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
		
		Label labelManufacture = new Label(groupBasicInfo, SWT.NONE);
		labelManufacture.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelManufacture.setText("生产厂家：");
		
		textManufacture = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textManufacture = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textManufacture.widthHint = 120;
		textManufacture.setLayoutData(gd_textManufacture);
		
		Label labelType = new Label(groupBasicInfo, SWT.NONE);
		labelType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelType.setText("型      号：");
		
		textType = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textType.widthHint = 120;
		textType.setLayoutData(gd_textType);
		
		Group groupCommuInfo = new Group(composite, SWT.NONE);
		GridData gd_groupCommuInfo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_groupCommuInfo.widthHint = 253;
		groupCommuInfo.setLayoutData(gd_groupCommuInfo);
		groupCommuInfo.setText("通讯信息");
		
		Label label = new Label(groupBasicInfo, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("序      号：");
		
		text_number = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_text_number = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_number.widthHint = 80;
		text_number.setLayoutData(gd_text_number);
		
		Label labelFixTime = new Label(groupBasicInfo, SWT.NONE);
		labelFixTime.setText("安装日期：");
		
		dateTimeFixTime = new DateTime(groupBasicInfo, SWT.BORDER);
		GridData gd_dateTimeFixTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dateTimeFixTime.widthHint = 120;
		dateTimeFixTime.setLayoutData(gd_dateTimeFixTime);
		
		Label lblNewLabel = new Label(groupBasicInfo, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("校准间隔：");
		
		text_checkInterval = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_text_checkInterval = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_checkInterval.widthHint = 80;
		text_checkInterval.setLayoutData(gd_text_checkInterval);
		
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
		new Label(groupCommuInfo, SWT.NONE);
		
		Label labelTimeout = new Label(groupCommuInfo, SWT.NONE);
		labelTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelTimeout.setText("通讯超时：");
		
		textTimeout = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textTimeout = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textTimeout.widthHint = 120;
		textTimeout.setLayoutData(gd_textTimeout);
		textTimeout.setText("5");
		
		Label lblMs = new Label(groupCommuInfo, SWT.NONE);
		lblMs.setText("ms");
		
		Label labelRetry = new Label(groupCommuInfo, SWT.NONE);
		labelRetry.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelRetry.setText("重发次数：");
		
		textRetry = new Text(groupCommuInfo, SWT.BORDER);
		GridData gd_textRetry = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textRetry.widthHint = 120;
		textRetry.setLayoutData(gd_textRetry);
		textRetry.setText("3");
		new Label(groupCommuInfo, SWT.NONE);
		
		btnUsed = new Button(groupCommuInfo, SWT.CHECK);
		btnUsed.setSelection(true);
		btnUsed.setText("启用");
		new Label(groupCommuInfo, SWT.NONE);
		new Label(groupCommuInfo, SWT.NONE);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 48;
		gd_composite_1.widthHint = 253;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(null);
		
		Button btnSave = new Button(composite_1, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (acquisitionDevice.getId() == null) {// 新建
//					if ("".equals(acquisitionDevice.getName().trim())) {
//						MessageDialog.openError(getSite().getShell(), "错误",
//								"设备名不能为空！");
//						return;
//					}

					acquisitionDevice.setName(textDeviceName.getText().trim());
					acquisitionDevice.setManufacture(textManufacture.getText().trim());
					acquisitionDevice.setType(textType.getText().trim());
					acquisitionDevice.setFixTime(new Date());
					acquisitionDevice.setFixPositin(textFixPositin.getText().trim());
					acquisitionDevice.setRemark(textRemark.getText().trim());
					acquisitionDevice.setAddress(Integer.valueOf(textAddress.getText().trim()));
					acquisitionDevice.setTimeout(Integer.valueOf(textTimeout.getText().trim()));
					acquisitionDevice.setRetry(Integer.valueOf(textRetry.getText().trim()));
					acquisitionDevice.setUsed(btnUsed.getSelection());
					acquisitionDevice.setNumber("".equals(text_number.getText().trim())?null:text_number.getText().trim());
					acquisitionDevice.setCheckInterval("".equals(text_checkInterval.getText().trim())?null:text_checkInterval.getText().trim());
					
					// 更新数据库
					acquisitionDeviceService.create(acquisitionDevice);
					
					// 更新左边的树状结构
					Object parentObject;
					parentObject = acquisitionDevice.getChannel();
					ScadaDeviceTreeView.treeViewer.add(parentObject, acquisitionDevice);
					ScadaDeviceTreeView.treeViewer.setExpandedState(parentObject, true);

				}
				else {// 编辑
					if ("".equals(textDeviceName.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"设备名不能为空！");
						return;
					}
					acquisitionDevice.setName(textDeviceName.getText().trim());
					acquisitionDevice.setManufacture(textManufacture.getText().trim());
					acquisitionDevice.setType(textType.getText().trim());
					// 
					int year = dateTimeFixTime.getYear();
					int month = dateTimeFixTime.getMonth();
					int day = dateTimeFixTime.getDay();
					Date fixTime = new Date(year-1900,month,day);
					acquisitionDevice.setFixTime(fixTime);
					
					acquisitionDevice.setFixPositin(textFixPositin.getText().trim());
					acquisitionDevice.setRemark(textRemark.getText().trim());
					acquisitionDevice.setAddress(Integer.valueOf(textAddress.getText().trim()));
					acquisitionDevice.setTimeout(Integer.valueOf(textTimeout.getText().trim()));
					acquisitionDevice.setRetry(Integer.valueOf(textRetry.getText().trim()));
					acquisitionDevice.setUsed(btnUsed.getSelection());
					acquisitionDevice.setNumber("".equals(text_number.getText().trim())?null:text_number.getText().trim());
					acquisitionDevice.setCheckInterval("".equals(text_checkInterval.getText().trim())?null:text_checkInterval.getText().trim());
					
					// 更新数据库
					acquisitionDeviceService.update(acquisitionDevice);
					
					// 更新左边的树状结构
					ScadaDeviceTreeView.treeViewer.update(acquisitionDevice, null);

				}

				LayoutUtil.hideViewPart();
				
			}
		});
		btnSave.setBounds(42, 10, 60, 27);
		btnSave.setText("保存(S)");
		
		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutUtil.hideViewPart();
			}
		});
		btnCancel.setText("取消(C)");
		btnCancel.setBounds(143, 10, 60, 27);

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("scadaDeviceConfig", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.ACQUISITIONDEVICE_ADD)) {
			acquisitionDevice = new AcquisitionDevice();
			Object object = event.getNewValue();
			if (object instanceof AcquisitionChannel) {
				acquisitionDevice.setChannel((AcquisitionChannel) object);
			} else {
				acquisitionDevice.setChannel(null);
			}
//			// 初始化控件值
			textDeviceName.setText("PDM");
			textManufacture.setText("丹东华通测控有限公司");
			textType.setText("");
			Date fixTime = new Date();
			Calendar fixTimeCalendar = Utils.date2CalendarUtil(fixTime);
			int year = fixTimeCalendar.get(Calendar.YEAR);
			int month = fixTimeCalendar.get(Calendar.MONTH);
			int day = fixTimeCalendar.get((Calendar.DAY_OF_MONTH));
			dateTimeFixTime.setDate(year, month, day);
			textFixPositin.setText("");
			textRemark.setText("");
			textAddress.setText("");
			textTimeout.setText("500");
			textRetry.setText("2");
			btnUsed.setSelection(true);
			text_number.setText("");
			text_checkInterval.setText("");
			
		} else if (event.getProperty().equals(
				FirePropertyConstants.ACQUISITIONDEVICE_EDIT)) {
			acquisitionDevice = (AcquisitionDevice) event.getNewValue();

			// 初始化控件值
			textDeviceName.setText(acquisitionDevice.getName());
			textManufacture.setText(acquisitionDevice.getManufacture());
			textType.setText(acquisitionDevice.getType());
			Date fixTime = acquisitionDevice.getFixTime();
			if(fixTime != null) {
				Calendar fixTimeCalendar = Utils.date2CalendarUtil(fixTime);
				int year = fixTimeCalendar.get(Calendar.YEAR);
				int month = fixTimeCalendar.get(Calendar.MONTH);
				int day = fixTimeCalendar.get((Calendar.DAY_OF_MONTH));
				dateTimeFixTime.setDate(year, month, day);
			}
			
			textFixPositin.setText(acquisitionDevice.getFixPositin()==null?"":acquisitionDevice.getFixPositin());
			textRemark.setText(acquisitionDevice.getRemark()==null?"":acquisitionDevice.getRemark());
			textAddress.setText(acquisitionDevice.getAddress()+"");
			textTimeout.setText(acquisitionDevice.getTimeout()+"");
			textRetry.setText(acquisitionDevice.getRetry()+"");
			btnUsed.setSelection(acquisitionDevice.isUsed());
			
			text_number.setText(acquisitionDevice.getNumber()==null?"":acquisitionDevice.getNumber());
			text_checkInterval.setText(acquisitionDevice.getCheckInterval()==null?"":acquisitionDevice.getCheckInterval());
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("scadaDeviceConfig");
		super.dispose();
	}
}
