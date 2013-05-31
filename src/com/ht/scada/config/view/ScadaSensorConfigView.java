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
import com.ht.scada.common.tag.entity.SensorDevice;
import com.ht.scada.common.tag.service.SensorDeviceService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.Utils;
import com.ht.scada.config.util.ViewPropertyChange;

public class ScadaSensorConfigView extends ViewPart implements IPropertyChangeListener {

	public ScadaSensorConfigView() {
	}

	public static final String ID = "com.ht.scada.config.view.ScadaSensorConfigView";
	private SensorDevice sensorDevice = new SensorDevice();

	private SensorDeviceService sensorDeviceService = (SensorDeviceService) Activator.getDefault()
			.getApplicationContext().getBean("sensorDeviceService");
	
	private Text textDeviceName;
	private Text textType;
	private DateTime dateTimeFixTime;
	private Text textAddress;
	private Text textManufacture;
	private Text textFixPositin;
	private Text textRemark;
	private Text text_number;
	private Text text_checkInterval;
	private Text textNickName;

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
		labelDeviceName.setText("传感器名：");
		
		textDeviceName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textDeviceName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDeviceName.widthHint = 120;
		textDeviceName.setLayoutData(gd_textDeviceName);
		
		Label label_1 = new Label(groupBasicInfo, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("别      名：");
		
		textNickName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textNickName = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_textNickName.widthHint = 120;
		textNickName.setLayoutData(gd_textNickName);
		
		Label labelAddress = new Label(groupBasicInfo, SWT.NONE);
		labelAddress.setText("地      址：");
		
		textAddress = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textAddress.widthHint = 120;
		textAddress.setLayoutData(gd_textAddress);
		
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
				if (sensorDevice.getId() == null) {// 新建
//					if ("".equals(acquisitionDevice.getName().trim())) {
//						MessageDialog.openError(getSite().getShell(), "错误",
//								"设备名不能为空！");
//						return;
//					}

					sensorDevice.setName(textDeviceName.getText().trim());
					sensorDevice.setManufacture(textManufacture.getText().trim());
					sensorDevice.setType(textType.getText().trim());
					sensorDevice.setFixTime(new Date());
					sensorDevice.setFixPositin(textFixPositin.getText().trim());
					sensorDevice.setRemark(textRemark.getText().trim());
					sensorDevice.setAddress(Integer.valueOf(textAddress.getText().trim()));
					sensorDevice.setNumber("".equals(text_number.getText().trim())?null:text_number.getText().trim());
					sensorDevice.setCheckInterval("".equals(text_checkInterval.getText().trim())?null:text_checkInterval.getText().trim());
					sensorDevice.setNickName("".equals(textNickName.getText().trim())?null:textNickName.getText().trim());
					
					// 更新数据库
					sensorDeviceService.create(sensorDevice);
					
					// 更新左边的树状结构
					Object parentObject;
					parentObject = sensorDevice.getRtuDevice();
					ScadaDeviceTreeView.treeViewer.add(parentObject, sensorDevice);
					ScadaDeviceTreeView.treeViewer.setExpandedState(parentObject, true);

				}
				else {// 编辑
					if ("".equals(textDeviceName.getText().trim())) {
						MessageDialog.openError(getSite().getShell(), "错误",
								"设备名不能为空！");
						return;
					}
					sensorDevice.setName(textDeviceName.getText().trim());
					sensorDevice.setManufacture(textManufacture.getText().trim());
					sensorDevice.setType(textType.getText().trim());
					// 
					int year = dateTimeFixTime.getYear();
					int month = dateTimeFixTime.getMonth();
					int day = dateTimeFixTime.getDay();
					Date fixTime = new Date(year-1900,month,day);
					sensorDevice.setFixTime(fixTime);
					
					sensorDevice.setFixPositin(textFixPositin.getText().trim());
					sensorDevice.setRemark(textRemark.getText().trim());
					sensorDevice.setAddress(Integer.valueOf(textAddress.getText().trim()));
					sensorDevice.setNumber("".equals(text_number.getText().trim())?null:text_number.getText().trim());
					sensorDevice.setCheckInterval("".equals(text_checkInterval.getText().trim())?null:text_checkInterval.getText().trim());
					sensorDevice.setNickName("".equals(textNickName.getText().trim())?null:textNickName.getText().trim());
					// 更新数据库
					sensorDeviceService.update(sensorDevice);
					
					// 更新左边的树状结构
					ScadaDeviceTreeView.treeViewer.update(sensorDevice, null);

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
				.addPropertyChangeListener("sensorDeviceConfig", this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FirePropertyConstants.SENSORDEVICE_ADD)) {
			sensorDevice = new SensorDevice();
			Object object = event.getNewValue();
			if (object instanceof AcquisitionDevice) {
				sensorDevice.setRtuDevice((AcquisitionDevice) object);
			} else {
				sensorDevice.setRtuDevice(null);
			}
//			// 初始化控件值
			textDeviceName.setText("");
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
			text_number.setText("");
			text_checkInterval.setText("");
			textNickName.setText("");
			
		} else if (event.getProperty().equals(
				FirePropertyConstants.SENSORDEVICE_EDIT)) {
			sensorDevice = (SensorDevice) event.getNewValue();

			// 初始化控件值
			textDeviceName.setText(sensorDevice.getName());
			textManufacture.setText(sensorDevice.getManufacture());
			textType.setText(sensorDevice.getType());
			Date fixTime = sensorDevice.getFixTime();
			Calendar fixTimeCalendar = Utils.date2CalendarUtil(fixTime);
			int year = fixTimeCalendar.get(Calendar.YEAR);
			int month = fixTimeCalendar.get(Calendar.MONTH);
			int day = fixTimeCalendar.get((Calendar.DAY_OF_MONTH));
			dateTimeFixTime.setDate(year, month, day);
			textFixPositin.setText(sensorDevice.getFixPositin()==null?"":sensorDevice.getFixPositin());
			textRemark.setText(sensorDevice.getRemark()==null?"":sensorDevice.getRemark());
			textAddress.setText(sensorDevice.getAddress()+"");
			
			textNickName.setText(sensorDevice.getNickName()==null?"":sensorDevice.getNickName());
			text_number.setText(sensorDevice.getNumber()==null?"":sensorDevice.getNumber());
			text_checkInterval.setText(sensorDevice.getCheckInterval()==null?"":sensorDevice.getCheckInterval());
		}
	}

	@Override
	public void dispose() {
		ViewPropertyChange.getInstance().removePropertyChangeListener("sensorDeviceConfig");
		super.dispose();
	}
}
