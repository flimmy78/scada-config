package com.ht.scada.config.window;

import java.awt.Point;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;

import bsh.This;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class StorageDetailInfor {

	private static String faultStorageName = "fault";			// 故障存储器常量
	private static String yxStorageName = "yx";					// 变位存储器常量
	private static String offlimitsStorageName = "offlimits";	// 越限存储器常量
	private static String storageStr;							//用于记录‘存储规则’的字符串
	private static String [] pushScreenArray = new String[] {"true", "false"};	//存储是否推送状态
	private static String [] yxAlarmArray = {"1", "0", "-1"};					//变位存储器警告状态
	private static String [] faultAlarmArray = {"1", "0"};						//故障存储器警告状态		
	private static String [] comboOffLTypeArray = {"true", "false"};			//越限类型 				
	private static int locationX=500;
	private static int locationY=500;
	
	public static int getLocationX() {
		return locationX;
	}

	public static void setLocationX(int locationX) {
		StorageDetailInfor.locationX = locationX;
	}

	public static int getLocationY() {
		return locationY;
	}

	public static void setLocationY(int locationY) {
		StorageDetailInfor.locationY = locationY;
	}

	protected Shell shell;
	private Text textSTypeFault;
	private Text textFaultCombine;
	private Text textFaultDevide;
	private Text textYXSType;
	private Text textYXCombine;
	private Text textYXDevide;
	private Text textOffLSType;
	private Text textOffLimitInfor;
	private Text textLimitValue;
	private CTabFolder tabFolderStorage;
	private Composite composite ;
	private Composite composite_1;
	private Composite composite_2;
	private Combo comboYXPushScreen ;
	private Combo comboYXAlarm ;
	private Combo comboFaultAlarm;
	private Combo comboFaultPushScreen;
	private Combo comboOffLType ;
	private Combo comboOffLPushScreen;

	public String getStorageStr() {
		return storageStr;
	}

	public void setStorageStr(String storageStr) {
		StorageDetailInfor.storageStr = storageStr;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StorageDetailInfor window = new StorageDetailInfor();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();	
		createContents();	
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.TITLE);
		shell.setLocation(locationX, locationY);
		shell.setSize(370, 190);
		shell.setText("存储规则详细信息");
		shell.setLayout(new GridLayout(1, false));
		
		tabFolderStorage = new CTabFolder(shell, SWT.BORDER);
		GridData gd_tabFolderStorage = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_tabFolderStorage.widthHint = 347;
		tabFolderStorage.setLayoutData(gd_tabFolderStorage);
		tabFolderStorage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectIndex1 = tabFolderStorage.getSelectionIndex();
				if (selectIndex1 ==0 ) {
					textSTypeFault.setText(faultStorageName);
				} else if (selectIndex1 ==1 ) {
					textYXSType.setText(yxStorageName);
				} else {
					textOffLSType.setText(offlimitsStorageName);
				}
			}
		});
		tabFolderStorage.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tabItemFault = new CTabItem(tabFolderStorage, SWT.NONE);
		tabItemFault.setText("故障存储器");
		
		composite = new Composite(tabFolderStorage, SWT.NONE);
		tabItemFault.setControl(composite);
		composite.setLayout(new GridLayout(5, false));
		
		Label labelStorageTypeFault = new Label(composite, SWT.NONE);
		labelStorageTypeFault.setText("存储器类型：");
		
		textSTypeFault = new Text(composite, SWT.BORDER);
		textSTypeFault.setEditable(false);
		textSTypeFault.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Label lblAlarmFault = new Label(composite, SWT.NONE);
		lblAlarmFault.setText("报警标志：");
		
		comboFaultAlarm = new Combo(composite, SWT.NONE);
		comboFaultAlarm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboFaultAlarm.setItems(faultAlarmArray);
		comboFaultAlarm.select(0);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("合消息      ：");
		
		textFaultCombine = new Text(composite, SWT.BORDER);
		GridData gd_textFaultCombine = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textFaultCombine.widthHint = 59;
		textFaultCombine.setLayoutData(gd_textFaultCombine);
		new Label(composite, SWT.NONE);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("分消息   ：");
		
		textFaultDevide = new Text(composite, SWT.BORDER);
		textFaultDevide.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("是否推画面：");
		
		comboFaultPushScreen = new Combo(composite, SWT.NONE);
		comboFaultPushScreen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboFaultPushScreen.setItems(pushScreenArray);
		comboFaultPushScreen.select(0);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		CTabItem tabItemYX = new CTabItem(tabFolderStorage, SWT.NONE);
		tabItemYX.setText("变为存储器");
		
		composite_1 = new Composite(tabFolderStorage, SWT.NONE);
		tabItemYX.setControl(composite_1);
		composite_1.setLayout(new GridLayout(5, false));
		
		Label label_3 = new Label(composite_1, SWT.NONE);
		label_3.setText("存储器类型：");
		
		textYXSType = new Text(composite_1, SWT.BORDER);
		textYXSType.setEditable(false);
		GridData gd_textYXSType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textYXSType.widthHint = 80;
		textYXSType.setLayoutData(gd_textYXSType);
		new Label(composite_1, SWT.NONE);
		
		Label label_4 = new Label(composite_1, SWT.NONE);
		label_4.setText("报警类型：");
		
		comboYXAlarm = new Combo(composite_1, SWT.NONE);
		GridData gd_comboYXAlarm = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboYXAlarm.widthHint = 63;
		comboYXAlarm.setLayoutData(gd_comboYXAlarm);
		comboYXAlarm.setItems(yxAlarmArray);
		comboYXAlarm.select(0);
		
		Label label_5 = new Label(composite_1, SWT.NONE);
		label_5.setText("合消息      ：");
		
		textYXCombine = new Text(composite_1, SWT.BORDER);
		textYXCombine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite_1, SWT.NONE);
		
		Label label_6 = new Label(composite_1, SWT.NONE);
		label_6.setText("分消息   ：");
		
		textYXDevide = new Text(composite_1, SWT.BORDER);
		textYXDevide.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_7 = new Label(composite_1, SWT.NONE);
		label_7.setText("是否推画面：");
		
		comboYXPushScreen = new Combo(composite_1, SWT.NONE);
		comboYXPushScreen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboYXPushScreen.setItems(pushScreenArray);
		comboYXPushScreen.select(0);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		CTabItem tbtmItemOffLimited = new CTabItem(tabFolderStorage, SWT.NONE);
		tbtmItemOffLimited.setText("越限存储器");
		
		composite_2 = new Composite(tabFolderStorage, SWT.NONE);
		tbtmItemOffLimited.setControl(composite_2);
		composite_2.setLayout(new GridLayout(5, false));
		
		Label label_8 = new Label(composite_2, SWT.NONE);
		label_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label_8.setText("存储器类型：");
		
		textOffLSType = new Text(composite_2, SWT.BORDER);
		textOffLSType.setEditable(false);
		textOffLSType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite_2, SWT.NONE);
		
		Label label_9 = new Label(composite_2, SWT.NONE);
		label_9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label_9.setText("限值      ：");
		
		textLimitValue = new Text(composite_2, SWT.BORDER);
		textLimitValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label label_10 = new Label(composite_2, SWT.NONE);
		label_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label_10.setText("越限信息   ：");
		
		textOffLimitInfor = new Text(composite_2, SWT.BORDER);
		textOffLimitInfor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite_2, SWT.NONE);
		
		Label label_11 = new Label(composite_2, SWT.NONE);
		label_11.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		label_11.setText("越限类型：");
		
		comboOffLType = new Combo(composite_2, SWT.NONE);
		comboOffLType.setItems(comboOffLTypeArray);
		comboOffLType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboOffLType.select(0);
		
		Label label_12 = new Label(composite_2, SWT.NONE);
		label_12.setText("是否推画面：");
		label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		comboOffLPushScreen = new Combo(composite_2, SWT.NONE);
		comboOffLPushScreen.setItems(pushScreenArray);
		comboOffLPushScreen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOffLPushScreen.select(0);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectIndex = tabFolderStorage.getSelectionIndex();	//获得当前选中的选项卡
				String storageRuleStr = "";
				if (selectIndex == 0 ) {
					//System.out.println("Fault");
					storageRuleStr = textSTypeFault.getText().trim() + "|" +
							comboFaultAlarm.getText().trim() + "|" +
							textFaultCombine.getText().trim() + "|" +
							textFaultDevide.getText().trim() + "|" +
							comboFaultPushScreen.getText().trim();
				} else if (selectIndex == 1 ) {
					//System.out.println("YX");
					storageRuleStr = textYXSType.getText().trim() + "|" +
							comboYXAlarm.getText().trim() + "|" + 
							textYXCombine.getText().trim() + "|" +
							textYXDevide.getText().trim() + "|" +
							comboYXPushScreen.getText().trim();
					
				} else {
					//	System.out.println("offlimits");
					storageRuleStr = textOffLSType.getText().trim() + "|" +
							textLimitValue.getText().trim() +"|" +
							textOffLimitInfor.getText().trim() + "|" +
							comboOffLType.getText().trim() + "|" +
							comboOffLPushScreen.getText().trim();
				}	
				storageStr = storageRuleStr;
				shell.close();
			}
		});
		btnSave.setText("保    存");
		

	
		
		/*
		 * 持久化属性
		 * 多个存储器用逗号隔开 
		 * (0) 故障存储器： 		fault|1|合闸|分闸|true 
		 * 					【存储器类型】|【报警标志1/0】|【合消息】|【分消息】|【是否推画面】
		 * (1) 遥信变位存储器： 	yx|-1|合闸|分闸|true 
		 * 					【存储器类型】|【报警类型1/0/-1】|【合消息】|【分消息】|【是否推画面】
		 * (2) 遥测越限存储器：	offlimits|500|电流越过上限|true|true 
		 * 					【存储器类型】|【限值】|【越限信息】|【越限类型（true:上限，false:下限）】|【是否推画面】
		 **/
		String temp1=storageStr.split("\\|")[0];
		
		if (temp1.equals(faultStorageName)) {				// 故障存储器
			tabFolderStorage.setSelection(0);
			
			textSTypeFault.setText(storageStr.split("\\|")[0]);		//存储器类型			
			//faultAlarmArray
			String faultAlarmStr =  storageStr.split("\\|")[1];		//报警类型
			if (faultAlarmStr!=null) {
				for (int i=0; i<this.faultAlarmArray.length; i++) {
					if ( faultAlarmStr.equals(this.faultAlarmArray[i])) {
						this.comboFaultAlarm.select(i);
					}
				}
			}
			textFaultCombine.setText(storageStr.split("\\|")[2]);	//合消息
			textFaultDevide.setText(storageStr.split("\\|")[3]);	//分消息
			String pushScreen = storageStr.split("\\|")[4];		//是否推画面
			if ( pushScreen != null ) {
				for ( int i=0; i< this.pushScreenArray.length; i++ ) {
					if ( pushScreen.equals(this.pushScreenArray[i])) {
						this.comboFaultPushScreen.select(i);
					}
				}
			}
			
		} else if (temp1.equals(yxStorageName)) {			// 变位存储器
			tabFolderStorage.setSelection(1);
			
			// 初始化设置
			textYXSType.setText(storageStr.split("\\|")[0]);	//存储器类型
			//comboYXAlarm										//报警类型
			String yxAlarmStr = storageStr.split("\\|")[1];
			if (yxAlarmStr!=null) {
				for (int j=0; j< this.yxAlarmArray.length; j++ ) {
					if (yxAlarmStr.equals(this.yxAlarmArray[j])) {
						this.comboYXAlarm.select(j);
					}
				}
			}
			textYXCombine.setText(storageStr.split("\\|")[2]);	//合消息
			textYXDevide.setText(storageStr.split("\\|")[3]);	//分消息
			String pushScreen = storageStr.split("\\|")[4];		//是否推画面
			if ( pushScreen != null ) {
				for ( int i=0; i< this.pushScreenArray.length; i++ ) {
					if ( pushScreen.equals(this.pushScreenArray[i])) {
						this.comboYXPushScreen.select(i);
					}
				}
			}
			
		} else if (temp1.equals(offlimitsStorageName)) {		// 越限存储器
			tabFolderStorage.setSelection(2);
			
			textOffLSType.setText(storageStr.split("\\|")[0]);		//存储器类型
			textLimitValue.setText(storageStr.split("\\|")[1]);		//限值
			textOffLimitInfor.setText(storageStr.split("\\|")[2]);	//越限信息 
			String offLTypeStr = storageStr.split("\\|")[3];		//越限类型
			if (offLTypeStr!=null) {
				for (int i=0; i<this.comboOffLTypeArray.length; i++) {
					if (offLTypeStr.equals(this.comboOffLTypeArray[i])) {
						this.comboOffLType.select(i);
					}
				}
			}
			String pushScreen = storageStr.split("\\|")[4];			//是否推画面
			if ( pushScreen != null ) {
				for ( int i=0; i< this.pushScreenArray.length; i++ ) {
					if ( pushScreen.equals(this.pushScreenArray[i])) {
						this.comboOffLPushScreen.select(i);
					}
				}
			}
			
		} else {									// 默认选择的
			tabFolderStorage.setSelection(0);
		}

		
		
		
		
		

	}
}
