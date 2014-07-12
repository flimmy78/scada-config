package com.ht.scada.config.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.AcquisitionChannel;
import com.ht.scada.common.tag.entity.TagCfgTpl;
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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.wb.swt.SWTResourceManager;

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
	private String frameInforStr="";		//标记该通道的帧信息
	
	private CTabFolder tabFolderPortInfor;
	private CTabFolder tabFolderFrame ;
	private GridTableViewer gridTableViewer;
	private GridTableViewer gridTableViewer_1 ;
	private Grid gridModbusFrame ;
	private Grid gridIECFrame;
	private CTabItem tbtmIec;
	
	
	
	List<framesStr> frames =new ArrayList<framesStr> ();		//存储Modbus协议下的通讯帧
	List<IECFrameStr> framesIEC = new ArrayList<IECFrameStr>();	//存储IEC104写一下的通讯帧

	/**
	 * Modbus下通讯帧对象模型
	 * @author hhh
	 *
	 */
	//【设备地址】|【功能码】-【数据地址】-【数据长度】|【优先级】|【帧名称(可省略)
	public class framesStr {
		private String deviceAddress;
		private String functionID;
		private String dataAddress;
		private String dataLength;
		private String priority;
		private String frameName;

		public String getDeviceAddress() {
			return deviceAddress;
		}
		public void setDeviceAddress(String deviceAddress) {
			this.deviceAddress = deviceAddress;
		}
		public String getFunctionID() {
			return functionID;
		}
		public void setFunctionID(String functionID) {
			this.functionID = functionID;
		}
		public String getDataAddress() {
			return dataAddress;
		}
		public void setDataAddress(String dataAddress) {
			this.dataAddress = dataAddress;
		}
		public String getDataLength() {
			return dataLength;
		}
		public void setDataLength(String dataLength) {
			this.dataLength = dataLength;
		}
		public String getPriority() {
			return priority;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
		public String getFrameName() {
			return frameName;
		}
		public void setFrameName(String frameName) {
			this.frameName = frameName;
		}
	}
	
	/*
	 * IEC104下的通讯帧对象模型
	 * 【召唤类型编码】-【执行间隔(s)】|【帧名称(可省略)】
	 */
	public class IECFrameStr {
		public String getCallTypeNum() {
			return callTypeNum;
		}
		public void setCallTypeNum(String callTypeNum) {
			this.callTypeNum = callTypeNum;
		}
		public String getInterval() {
			return interval;
		}
		public void setInterval(String interval) {
			this.interval = interval;
		}
		public String getFrameName() {
			return frameName;
		}
		public void setFrameName(String frameName) {
			this.frameName = frameName;
		}
		private String callTypeNum;
		private String interval;
		private String frameName;
		
	}
	
	
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
	private Table table;

	public void createPartControl(Composite parent) {
		parent.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		parent.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		composite.setLayout(null);

		Group groupBasicInfo = new Group(composite, SWT.NONE);
		groupBasicInfo.setBounds(10, 10, 306, 83);
		groupBasicInfo.setText("基本信息");
		groupBasicInfo.setLayout(new GridLayout(4, false));

		Label labelName = new Label(groupBasicInfo, SWT.NONE);
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		labelName.setText("通道名称：");

		textChannelName = new Text(groupBasicInfo, SWT.BORDER);
		GridData gd_textChannelName = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_textChannelName.widthHint = 108;
		textChannelName.setLayoutData(gd_textChannelName);
				
						Label labelIdx = new Label(groupBasicInfo, SWT.NONE);
						labelIdx.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
								false, 1, 1));
						labelIdx.setText(" 序号：");
								
										textIdx = new Text(groupBasicInfo, SWT.BORDER);
										GridData gd_textIdx = new GridData(SWT.FILL, SWT.CENTER, true, false,
												1, 1);
										gd_textIdx.widthHint = 57;
										textIdx.setLayoutData(gd_textIdx);
						
								labelProtocal = new Label(groupBasicInfo, SWT.NONE);
								labelProtocal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
								labelProtocal.setText("通信规约：");
						
								comboProtocal = new Combo(groupBasicInfo, SWT.READ_ONLY);
								comboProtocal.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										if ( comboProtocal.getText().trim().contains("Modbus")) {
											tabFolderFrame.setSelection(0);
											frameInforStr = getFrameInforStr(0);
										} else if ( comboProtocal.getText().trim().contains("IEC104")) {
											tabFolderFrame.setSelection(1);
											frameInforStr = getFrameInforStr(1);
										} else {
											tabFolderFrame.setSelection(2);
											frameInforStr = getFrameInforStr(2);
										}
									}
								});
								comboProtocal.setItems(protocalTypeStr);
								GridData gd_comboProtocal = new GridData(SWT.FILL, SWT.CENTER, true,
										false, 1, 1);
								gd_comboProtocal.widthHint = 50;
								comboProtocal.setLayoutData(gd_comboProtocal);
								comboProtocal.select(0);
						new Label(groupBasicInfo, SWT.NONE);
						
						final Button button = new Button(groupBasicInfo, SWT.CHECK);
						button.setText("启用");
						button.setSelection(true);
						
						Group group_1 = new Group(composite, SWT.NONE);
						group_1.setBounds(10, 99, 306, 422);
						group_1.setText("通讯信息");
	    group_1.setLayout(new GridLayout(1, false));
	    
	    Composite composite_2 = new Composite(group_1, SWT.NONE);
	    composite_2.setLayout(new GridLayout(4, false));
	    GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
	    gd_composite_2.widthHint = 293;
	    composite_2.setLayoutData(gd_composite_2);
	    
	    		Label labelOffline = new Label(composite_2, SWT.NONE);
	    		labelOffline.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    		labelOffline.setText("通讯离线：");
	    		
	    				textOffline = new Text(composite_2, SWT.BORDER);
	    				GridData gd_textOffline = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
	    				gd_textOffline.widthHint = 58;
	    				textOffline.setLayoutData(gd_textOffline);
	    				
	    						Label labelInterval = new Label(composite_2, SWT.NONE);
	    						labelInterval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    						labelInterval.setText("帧间隔(ms)：");
	    												
	    														textInterval = new Text(composite_2, SWT.BORDER);
	    														GridData gd_textInterval = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
	    														gd_textInterval.widthHint = 60;
	    														textInterval.setLayoutData(gd_textInterval);
	    
	    Label lbll = new Label(group_1, SWT.NONE);
	    lbll.setText(" 端口情况：");
	    
	    tabFolderPortInfor = new CTabFolder(group_1, SWT.BORDER);
	    GridData gd_tabFolderPortInfor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_tabFolderPortInfor.widthHint = 282;
	    tabFolderPortInfor.setLayoutData(gd_tabFolderPortInfor);
	    tabFolderPortInfor.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
	    
	    CTabItem tbtmTcpIp = new CTabItem(tabFolderPortInfor, SWT.NONE);
	    tbtmTcpIp.setText("tcp/ip");
	    
	    Composite composite_3 = new Composite(tabFolderPortInfor, SWT.NONE);
	    tbtmTcpIp.setControl(composite_3);
	    composite_3.setLayout(new GridLayout(2, false));
	    
	    Label labelIP = new Label(composite_3, SWT.NONE);
	    labelIP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelIP.setText("IP   ：");
	    
	    textIP = new Text(composite_3, SWT.BORDER);
	    GridData gd_textIP = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
	    gd_textIP.widthHint = 144;
	    textIP.setLayoutData(gd_textIP);
	    
	    Label labelPort = new Label(composite_3, SWT.NONE);
	    labelPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelPort.setText("端口：");
	    
	    textPort = new Text(composite_3, SWT.BORDER);
	    
	    CTabItem tbtmDtu = new CTabItem(tabFolderPortInfor, SWT.NONE);
	    tbtmDtu.setText("dtu");
	    
	    Composite composite_4 = new Composite(tabFolderPortInfor, SWT.NONE);
	    tbtmDtu.setControl(composite_4);
	    composite_4.setLayout(new GridLayout(4, false));
	    
	    Label labelDTUID = new Label(composite_4, SWT.NONE);
	    labelDTUID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelDTUID.setText("DTU-ID ：");
	    
	    textDTUID = new Text(composite_4, SWT.BORDER);
	    textDTUID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    
	    Label labelDtuPort = new Label(composite_4, SWT.NONE);
	    labelDtuPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelDtuPort.setText("   端口：");
	    
	    textDtuPort = new Text(composite_4, SWT.BORDER);
	    textDtuPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    
	    Label labelHeart = new Label(composite_4, SWT.NONE);
	    labelHeart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelHeart.setText("心跳信号：");
	    
	    textHeart = new Text(composite_4, SWT.BORDER);
	    textHeart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    new Label(composite_4, SWT.NONE);
	    new Label(composite_4, SWT.NONE);
	    
	    Label labelHeartInterval = new Label(composite_4, SWT.NONE);
	    labelHeartInterval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelHeartInterval.setText("信号间隔：");
	    
	    textHeartInteval = new Text(composite_4, SWT.BORDER);
	    textHeartInteval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    new Label(composite_4, SWT.NONE);
	    new Label(composite_4, SWT.NONE);
	    
	    CTabItem tbtmSerial = new CTabItem(tabFolderPortInfor, SWT.NONE);
	    tbtmSerial.setText("serial");
	    
	    Composite composite_5 = new Composite(tabFolderPortInfor, SWT.NONE);
	    tbtmSerial.setControl(composite_5);
	    composite_5.setLayout(new GridLayout(4, false));
	    
	    Label labelSerialPort = new Label(composite_5, SWT.NONE);
	    labelSerialPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelSerialPort.setText("端   口：");
	    
	    textSerialPort = new Text(composite_5, SWT.BORDER);
	    textSerialPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label labelBaud = new Label(composite_5, SWT.NONE);
	    labelBaud.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelBaud.setText("波特率：");
	    
	    comboBaud = new Combo(composite_5, SWT.NONE);
	    comboBaud.setItems(comboBaudArray);
	    comboBaud.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label labelData = new Label(composite_5, SWT.NONE);
	    labelData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelData.setText("数据位：");
	    
	    comboDatabit = new Combo(composite_5, SWT.NONE);
	    comboDatabit.setItems(comboDatabitArray);
	    comboDatabit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label labelCheckbit = new Label(composite_5, SWT.NONE);
	    labelCheckbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelCheckbit.setText("校验位：");
	    
	    comboCheckbit = new Combo(composite_5, SWT.NONE);
	    comboCheckbit.setItems(comboCheckbitArray);
	    comboCheckbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label labelStopbit = new Label(composite_5, SWT.NONE);
	    labelStopbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
	    labelStopbit.setText("停止位：");
	    
	    comboStopbit = new Combo(composite_5, SWT.NONE);
	    comboStopbit.setItems(comboStopbitArray);
	    comboStopbit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    new Label(composite_5, SWT.NONE);
	    new Label(composite_5, SWT.NONE);
	    							    	    
		Label label = new Label(group_1, SWT.NONE);
		label.setText(" 帧情况：");

		tabFolderFrame = new CTabFolder(group_1, SWT.BORDER);
		GridData gd_tabFolderFrame = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_tabFolderFrame.heightHint = 155;
		gd_tabFolderFrame.widthHint = 281;
		tabFolderFrame.setLayoutData(gd_tabFolderFrame);
		tabFolderFrame.setSelectionBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabItemModbus = new CTabItem(tabFolderFrame, SWT.NONE);
		tabItemModbus.setText("modbus");

		Composite composite_6 = new Composite(tabFolderFrame, SWT.NONE);
		tabItemModbus.setControl(composite_6);
		composite_6.setLayout(new GridLayout(1, false));

		gridTableViewer = new GridTableViewer(composite_6, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		gridModbusFrame = gridTableViewer.getGrid();
		gridModbusFrame.setRowHeaderVisible(true);
		gridModbusFrame.setHeaderVisible(true);
		gridModbusFrame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		GridViewerColumn gridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnDeviceName = gridViewerColumn.getColumn();
		gridColumnDeviceName.setWidth(60);
		gridColumnDeviceName.setText("设备地址");
		gridViewerColumn.setEditingSupport(new EditingSupport(gridTableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(gridModbusFrame);
				return ce;
			}

			protected Object getValue(Object element) {
				framesStr tct = (framesStr) element;
				return tct.getDeviceAddress();
			}

			protected void setValue(Object element, Object value) {
				framesStr tct = (framesStr) element;	
				String deviceAddress = (String)value;
				if( deviceAddress.length() ==0 || deviceAddress == null) {
					MessageDialog.openError(gridModbusFrame.getShell(), "错误", "设备地址不能为空！");
					tct.setDeviceAddress(tct.getDeviceAddress());
				} else {
					tct.setDeviceAddress(deviceAddress);
				}
				gridTableViewer.update(tct, null);
		
			}
		});

		GridViewerColumn gridViewerColumn_1 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnFunctionID = gridViewerColumn_1.getColumn();
		gridColumnFunctionID.setWidth(50);
		gridColumnFunctionID.setText("功能码");
		gridViewerColumn_1
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(gridModbusFrame);
				return ce;
			}

			protected Object getValue(Object element) {
				framesStr tct = (framesStr) element;
				return tct.getFunctionID();
			}

			protected void setValue(Object element, Object value) {
				framesStr tct = (framesStr) element;
				String functionID = (String)value;
				if( functionID.length() ==0 || functionID == null) {
					MessageDialog.openError(gridModbusFrame.getShell(), "错误", "功能码不能为空！");
					tct.setFunctionID(tct.getFunctionID());
				} else {
					tct.setFunctionID(functionID);
				}
				gridTableViewer.update(tct, null);
			}
		});	

		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnDataAddress = gridViewerColumn_2.getColumn();
		gridColumnDataAddress.setWidth(60);
		gridColumnDataAddress.setText("数据地址");
		gridViewerColumn_2
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						CellEditor ce = new TextCellEditor(gridModbusFrame);
						return ce;
					}

					protected Object getValue(Object element) {
						framesStr tct = (framesStr) element;
						return tct.getDataAddress();
					}

					protected void setValue(Object element, Object value) {
						framesStr tct = (framesStr) element;
						String dataAddress = (String)value;
						if( dataAddress.length() ==0 || dataAddress == null) {
							MessageDialog.openError(gridModbusFrame.getShell(), "错误", "数据地址不能为空！");
							tct.setDataAddress(tct.getDataAddress());
						} else {
							tct.setDataAddress(dataAddress);
						}		
						gridTableViewer.update(tct, null);

					}
				});

		GridViewerColumn gridViewerColumn_3 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnDataLength = gridViewerColumn_3.getColumn();
		gridColumnDataLength.setWidth(60);
		gridColumnDataLength.setText("数据长度");
		gridViewerColumn_3
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						CellEditor ce = new TextCellEditor(gridModbusFrame);
						return ce;
					}

					protected Object getValue(Object element) {
						framesStr tct = (framesStr) element;
						return tct.getDataLength();
					}

					protected void setValue(Object element, Object value) {
						framesStr tct = (framesStr) element;
						String dataLength = (String)value;
						if( dataLength.length() ==0 || dataLength == null) {
							MessageDialog.openError(gridModbusFrame.getShell(), "错误", "数据长度不能为空！");
							tct.setDataLength(tct.getDataLength());
						} else {
							tct.setDataLength(dataLength);
						}			
						gridTableViewer.update(tct, null);
					}
				});

		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnPriority = gridViewerColumn_4.getColumn();
		gridColumnPriority.setWidth(50);
		gridColumnPriority.setText("优先级");
		gridViewerColumn_4
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						CellEditor ce = new TextCellEditor(gridModbusFrame);
						return ce;
					}

					protected Object getValue(Object element) {
						framesStr tct = (framesStr) element;
						return tct.getPriority();
					}

					protected void setValue(Object element, Object value) {
						framesStr tct = (framesStr) element;	
						String priority = (String)value;
						if( priority.length() ==0 || priority == null) {
							MessageDialog.openError(gridModbusFrame.getShell(), "错误", "优先级不能为空！");
							tct.setPriority(tct.getPriority());
						} else {
							tct.setPriority(priority);
						}	
						gridTableViewer.update(tct, null);

					}
				});

		GridViewerColumn gridViewerColumn_5 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumnFrameName = gridViewerColumn_5.getColumn();
		gridColumnFrameName.setWidth(50);
		gridColumnFrameName.setText("帧名称");
		gridViewerColumn_5.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						CellEditor ce = new TextCellEditor(gridModbusFrame);
						return ce;
					}

					protected Object getValue(Object element) {
						framesStr tct = (framesStr) element;
						return tct.getFrameName();
					}

					protected void setValue(Object element, Object value) {
						framesStr tct = (framesStr) element;
						tct.setFrameName((String) value);
						gridTableViewer.update(tct, null);

					}
				});

		CTabItem tbtmIec = new CTabItem(tabFolderFrame, SWT.NONE);
		tbtmIec.setText("iec104");
		

		Composite composite_7 = new Composite(tabFolderFrame, SWT.NONE);
		tbtmIec.setControl(composite_7);
		composite_7.setLayout(new GridLayout(1, false));

		gridTableViewer_1 = new GridTableViewer(composite_7, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		gridIECFrame = gridTableViewer_1.getGrid();
		gridIECFrame.setRowHeaderVisible(true);
		gridIECFrame.setHeaderVisible(true);
		gridIECFrame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		GridViewerColumn gridViewerColumn_6 = new GridViewerColumn(
				gridTableViewer_1, SWT.NONE);
		GridColumn gridColumnCallType = gridViewerColumn_6.getColumn();
		gridColumnCallType.setWidth(90);
		gridColumnCallType.setText("召唤类型编码");
		gridViewerColumn_6.setEditingSupport(new EditingSupport(gridTableViewer_1) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(gridIECFrame);
				return ce;
			}

			protected Object getValue(Object element) {
				IECFrameStr tct = (IECFrameStr) element;
				return tct.getCallTypeNum();
			}

			protected void setValue(Object element, Object value) {
				IECFrameStr tct = (IECFrameStr) element;
				String callTypeNum=(String)value;
				if( callTypeNum.length() ==0 || callTypeNum == null) {
					MessageDialog.openError(gridIECFrame.getShell(), "错误", "召唤类型不能为空！");
					tct.setCallTypeNum(tct.getCallTypeNum());
				} else {
					tct.setCallTypeNum(callTypeNum);
				}
				gridTableViewer_1.update(tct, null);

			}
		});

		GridViewerColumn gridViewerColumn_7 = new GridViewerColumn(
				gridTableViewer_1, SWT.NONE);
		GridColumn gridColumnInterval = gridViewerColumn_7.getColumn();
		gridColumnInterval.setWidth(70);
		gridColumnInterval.setText("执行间隔");
		gridViewerColumn_7.setEditingSupport(new EditingSupport(gridTableViewer_1) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(gridIECFrame);
				return ce;
			}

			protected Object getValue(Object element) {
				IECFrameStr tct = (IECFrameStr) element;
				return tct.getInterval();
			}

			protected void setValue(Object element, Object value) {
				IECFrameStr tct = (IECFrameStr) element;
				String interval = (String)value;
				if( interval.length() ==0 || interval == null) {
					MessageDialog.openError(gridIECFrame.getShell(), "错误", "执行间隔不能为空！");
					tct.setInterval(tct.getInterval());
				} else {
					tct.setInterval(interval);
				}
				gridTableViewer_1.update(tct, null);

			}
		});

		GridViewerColumn gridViewerColumn_8 = new GridViewerColumn(
				gridTableViewer_1, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_8.getColumn();
		gridColumn_2.setWidth(65);
		gridColumn_2.setText("帧名称");
		gridViewerColumn_8.setEditingSupport(new EditingSupport(gridTableViewer_1) {
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				CellEditor ce = new TextCellEditor(gridIECFrame);
				return ce;
			}

			protected Object getValue(Object element) {
				IECFrameStr tct = (IECFrameStr) element;
				return tct.getFrameName();
			}

			protected void setValue(Object element, Object value) {
				IECFrameStr tct = (IECFrameStr) element;
				tct.setFrameName((String) value);
				gridTableViewer_1.update(tct, null);

			}
		});

		CTabItem tabItemOthers = new CTabItem(tabFolderFrame, SWT.NONE);
		tabItemOthers.setText("others");

		Composite composite_8 = new Composite(tabFolderFrame, SWT.NONE);
		tabItemOthers.setControl(composite_8);
		composite_8.setLayout(new GridLayout(1, false));
		
		Label label_1 = new Label(composite_8, SWT.NONE);
		label_1.setText("其余规约的待扩充！");
		
				Composite composite_1 = new Composite(parent, SWT.NONE);
				composite_1.setLayout(null);
				
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
									}									
									
									//新加入的代码_控制端口信息=======
									int tabIndex = tabFolderPortInfor.getSelectionIndex();	//获得当前的通讯方式
									String portInforStr = getPortInforStr (tabIndex);
									//新加入的代码_控制帧信息=======
									int tabIndex2 = tabFolderFrame.getSelectionIndex();
									frameInforStr = getFrameInforStr (tabIndex2);
									
									acquisitionChannel.setName(textChannelName.getText().trim());
									acquisitionChannel.setProtocal(getCommPotclType(comboProtocal.getText()).getName());
									acquisitionChannel.setIdx(Integer.valueOf(textIdx.getText().trim()));
									acquisitionChannel.setOffline(Integer.valueOf(textOffline.getText()));
									acquisitionChannel.setIntvl(Integer.valueOf(textInterval.getText()));
//					acquisitionChannel.setSchedule(textSchedule.getText().trim());
									acquisitionChannel.setPortInfo(portInforStr);
									acquisitionChannel.setFrames(frameInforStr);
									acquisitionChannel.setUsed(button.getSelection());
									
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
									}								

									
//					int year = dateTimeUpdateTime.getYear();
//					int month = dateTimeUpdateTime.getMonth();
//					int day = dateTimeUpdateTime.getDay();
//					Date updateTime = new Date(year-1900,month,day);

//					acquisitionChannel.setUpdateTime(updateTime);
									
					//新加入的代码_控制端口信息=======
					int tabIndex = tabFolderPortInfor.getSelectionIndex();	//获得当前的通讯方式
					String portInforStr = getPortInforStr (tabIndex);
					//新加入的代码_控制帧信息=======
					int tabIndex2 = tabFolderFrame.getSelectionIndex();
					frameInforStr = getFrameInforStr (tabIndex2);
					//System.out.println("帧的信息为： " + frameInforStr);
									
									
									
									acquisitionChannel.setName(textChannelName.getText().trim());
									acquisitionChannel.setProtocal(getCommPotclType(comboProtocal.getText()).getName());
									acquisitionChannel.setIdx(Integer.valueOf(textIdx.getText().trim()));
									acquisitionChannel.setOffline(Integer.valueOf(textOffline.getText()));
									acquisitionChannel.setIntvl(Integer.valueOf(textInterval.getText()));
//					acquisitionChannel.setSchedule(textSchedule.getText().trim());
									acquisitionChannel.setPortInfo(portInforStr);
									acquisitionChannel.setFrames(frameInforStr);
									acquisitionChannel.setUsed(button.getSelection());
									
									
									// 更新数据库
									acquisitionChannelService.update(acquisitionChannel);
									ScadaDeviceTreeView.treeViewer.update(acquisitionChannel,null);
								}

								LayoutUtil.hideViewPart();
							}
						});
						btnSave.setBounds(59, 0, 61, 27);
						btnSave.setText("保存(S)");
						
								Button btnCancel = new Button(composite_1, SWT.NONE);
								btnCancel.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(SelectionEvent e) {
										LayoutUtil.hideViewPart();
									}
								});
								
									btnCancel.setText("取消(C)");
									btnCancel.setBounds(191, 0, 61, 27);
						    	    
		// 控制增加和删除提示(Modbus表的)
		Menu menu = new Menu(gridModbusFrame);
		gridModbusFrame.setMenu(menu);

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				framesStr tempFrame = new framesStr();
				tempFrame.setDeviceAddress("请输入");
				tempFrame.setDataAddress("请输入");
				tempFrame.setDataLength("请输入");
				tempFrame.setFrameName("可为空");
				tempFrame.setFunctionID("请输入");
				tempFrame.setPriority("请输入");

				frames.add(tempFrame);

				gridTableViewer.setInput(frames);
				gridTableViewer.refresh();
			}
		});
		menuItem.setText("添加通讯帧");
		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) gridTableViewer
						.getSelection();
				if (selection.isEmpty()) {
					MessageDialog.openWarning(gridModbusFrame.getShell(), "提示",
							"未选择变量！");
					return;
				}
				GridItem gridItems[] = gridModbusFrame.getSelection();
				for (GridItem gi : gridItems) {
					framesStr temp1 = (framesStr) gi.getData();
					frames.remove(temp1);
				}
				gridTableViewer.refresh();

			}
		});
		menuItem_1.setText("删除通讯帧");

		// 控制增加和删除提示(IEC104表的)
		Menu menu1 = new Menu(gridIECFrame);
		gridIECFrame.setMenu(menu1);

		MenuItem menuItem1 = new MenuItem(menu1, SWT.NONE);
		menuItem1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IECFrameStr tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("new");
				tempFrame.setFrameName("可为空");
				tempFrame.setInterval("请输入");

				framesIEC.add(tempFrame);

				gridTableViewer_1.setInput(framesIEC);
				gridTableViewer_1.refresh();
			}
		});
		menuItem1.setText("添加通讯帧");
		MenuItem menuItem_11 = new MenuItem(menu1, SWT.NONE);
		menuItem_11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) gridTableViewer_1
						.getSelection();
				if (selection.isEmpty()) {
					MessageDialog.openWarning(gridIECFrame.getShell(), "提示",
							"未选择变量！");
					return;
				}
				GridItem gridItems[] = gridIECFrame.getSelection();
				for (GridItem gi : gridItems) {
					IECFrameStr temp1 = (IECFrameStr) gi.getData();
					framesIEC.remove(temp1);
				}
				gridTableViewer_1.refresh();

			}
		});
		menuItem_11.setText("删除通讯帧");
									
		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setLabelProvider(new ViewerLabelProvider_1());
		gridTableViewer.setInput(frames);
		//
		gridTableViewer_1.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer_1.setLabelProvider(new ViewerLabelProvider_2());
		gridTableViewer_1.setInput(framesIEC);

		ViewPropertyChange.getInstance()
				.addPropertyChangeListener("channel", this);
	}

	/*
	 * 控制显示内容
	 */
	class ViewerLabelProvider_1 extends LabelProvider implements
	ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			framesStr frameStr = (framesStr) element;
			//1|3-100-10|0|soe,2|3-100-10|0|soe
			switch (columnIndex) {
				case 0:				// 设备地址
					return frameStr.getDeviceAddress();
				case 1:				// 功能码
					return frameStr.getFunctionID();
				case 2:				// 数据地址
					return frameStr.getDataAddress();
				case 3:				// 数据长度
					return frameStr.getDataLength();
				case 4:				// 优先级
					return frameStr.getPriority();
				case 5:				// 帧名称（可空）
					return frameStr.getFrameName();
				default:
					break;
			}
			return null;
		}
	}

	/*
	 * 控制显示内容IEC
	 */
	class ViewerLabelProvider_2 extends LabelProvider implements
	ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			IECFrameStr frameStr = (IECFrameStr) element;
			//【召唤类型编码】-【执行间隔(s)】|【帧名称(可省略)】
			switch (columnIndex) {
				case 0:				// 设备地址
					return frameStr.getCallTypeNum();
				case 1:				// 功能码
					return frameStr.getInterval();
				case 2:				// 数据地址
					return frameStr.getFrameName();
				
				default:
					break;
			}
			return null;
		}
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
			textOffline.setText("3");
			textInterval.setText("500");
//			textSchedule.setText("");
//			Date nowDate = new Date();
//			Calendar nowCalendar = Utils.date2CalendarUtil(nowDate);
//			dateTimeUpdateTime.setDate(nowCalendar.get(Calendar.YEAR),
//					nowCalendar.get(Calendar.MONTH),
//					nowCalendar.get(Calendar.DAY_OF_MONTH));
		
			setPortInfro(event);		//设置控件的值	
			
			tabFolderFrame.setTabHeight(0);
			comboProtocal.select(0);
			tabFolderPortInfor.setSelection(0);
			tabFolderFrame.setSelection(1);			//默认到IEC处
			frames.clear();
			framesIEC.clear();
			gridTableViewer.refresh();
			// gridTableViewer_1.refresh();
			
			
			// 进行IEC104的一些默认帧配置 , 2014.6.2 日增加，便于添加新的通道
			if (comboProtocal.getText().equals(protocalTypeStr[0])) {	// IEC协议时候的初始设置
				IECFrameStr tempFrame = new IECFrameStr();
				
				tempFrame.setCallTypeNum("0x8B");
				tempFrame.setFrameName("");
				tempFrame.setInterval("600");
				framesIEC.add(tempFrame);
				
				tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("0x88");
				tempFrame.setFrameName("");
				tempFrame.setInterval("30");
				framesIEC.add(tempFrame);
				
				tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("0x67");
				tempFrame.setFrameName("");
				tempFrame.setInterval("600");
				framesIEC.add(tempFrame);
				
				tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("0x65");
				tempFrame.setFrameName("");
				tempFrame.setInterval("600");
				framesIEC.add(tempFrame);
				
				tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("0x8E");
				tempFrame.setFrameName("");
				tempFrame.setInterval("600");
				framesIEC.add(tempFrame);

				tempFrame = new IECFrameStr();
				tempFrame.setCallTypeNum("0x91");
				tempFrame.setFrameName("sz");
				tempFrame.setInterval("600");
				framesIEC.add(tempFrame);

				gridTableViewer_1.setInput(framesIEC);
			}
			
			gridTableViewer_1.refresh();
			
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

//			Date updateTime = acquisitionChannel.getUpdateTime();
//			Calendar updateTimeCalendar = Utils.date2CalendarUtil(updateTime);
//			int year = updateTimeCalendar.get(Calendar.YEAR);
//			int month = updateTimeCalendar.get(Calendar.MONTH);
//			int day = updateTimeCalendar.get((Calendar.DAY_OF_MONTH));
//			dateTimeUpdateTime.setDate(year, month, day);
			// ====================================
			
			String frameStr = acquisitionChannel.getFrames();
			tabFolderFrame.setTabHeight(0);
			frames.clear();			//集合清空
			framesIEC.clear();
			if (acquisitionChannel.getProtocal().contains("Modbus")) {
				tabFolderFrame.setSelection(0);
				//通讯帧操作
				//String frameStr = "1|3-100-10|0|soe4,2|3-100-10|0,3|3-100-10|0|soe3,4|3-100-10|0|soe2,5|3-100-10|0,6|3-100-10|0|soe1" ;//acquisitionChannel.getFrames();
				String [] frameStrArray = frameStr.split(",");
				for (int i=0; i<frameStrArray.length; i++) {
					framesStr fs = new framesStr();
					//deviceAddress  functionID  dataAddress  dataLength  priority  frameName
					int eachLength = frameStrArray[i].split("\\|").length;
					
					fs.setDeviceAddress(frameStrArray[i].split("\\|")[0]);
					fs.setFunctionID(frameStrArray[i].split("\\|")[1].split("-")[0]);
					fs.setDataAddress(frameStrArray[i].split("\\|")[1].split("-")[1]);
					fs.setDataLength(frameStrArray[i].split("\\|")[1].split("-")[2]);
					fs.setPriority(frameStrArray[i].split("\\|")[2]);
					if ( eachLength == 4) {
						fs.setFrameName(frameStrArray[i].split("\\|")[3]);
					} else {	//帧名称不存在，例如：2|3-100-10|0
						fs.setFrameName("");
					}
					
					frames.add(fs);
				}

			} else if (acquisitionChannel.getProtocal().contains("IEC104")) {
				tabFolderFrame.setSelection(1);
				
				//String frameStr = "0x65-1200|召唤电能,0x64-10,0x8e-600|召唤定时示功图数据,0x34-10|总召唤,0x64-90,0x64-10|总召唤的" ;//acquisitionChannel.getFrames();
				String [] frameStrArray = frameStr.split(",");
				for (int i=0; i<frameStrArray.length; i++) {
					IECFrameStr fs = new IECFrameStr();
					//【召唤类型编码】-【执行间隔(s)】|【帧名称(可省略)】
					int eachLength = frameStrArray[i].split("\\|").length;
					
					fs.setCallTypeNum(frameStrArray[i].split("-")[0]);
					
					if ( eachLength == 2) {
						fs.setInterval(frameStrArray[i].split("-")[1].split("\\|")[0]);
						fs.setFrameName(frameStrArray[i].split("-")[1].split("\\|")[1]);
						
					} else {	
						fs.setInterval(frameStrArray[i].split("-")[1]);
						fs.setFrameName("");
					}
					
					framesIEC.add(fs);
				}
				
			} else {
				tabFolderFrame.setSelection(2);
			}
			
			setPortInfro(event);		//设置控件的值	
			gridTableViewer.refresh();
			gridTableViewer_1.refresh();
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
	
	/**
	 * 根据已有的字符串，设置控件显示信息
	 */
	public void setPortInfro(PropertyChangeEvent event) {
		if ( event.getProperty().equals(
				FirePropertyConstants.ACQUISITIONCHANNEL_ADD) ) {
			
			//清空插件
			textIP.setText("192.168.请补齐");
			textPort.setText("2404");//
			textDTUID.setText("");
			textDtuPort.setText("");
			textHeart.setText("");
			textHeartInteval.setText("");
			textSerialPort.setText("");	
			comboBaud.setText("");
			comboDatabit.setText("");
			comboCheckbit.setText("");
			comboStopbit.setText("");
			
		} else {
			
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
				comboBaud.setText("");
				comboDatabit.setText("");
				comboCheckbit.setText("");
				comboStopbit.setText("");
							
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
				comboBaud.setText("");
				comboDatabit.setText("");
				comboCheckbit.setText("");
				comboStopbit.setText("");
				
				
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
	
	/*
	 * 根据通讯方式的不同，返回相应端口字符串
	 */
	public String getPortInforStr (int tabIndex) {
		String portInforStr="";
		if (tabIndex == 0) {			//当前通讯方式为TCP/IP
			System.out.println("标签栏被选择的索引为： " + "TCP");
			//textIP,textPort,        tcp/ip|192.168.1.110:4660
			portInforStr = "tcp/ip" + "|" + textIP.getText().trim() + ":" + textPort.getText().trim();
			
			
		} else if (tabIndex == 1 ) {	//当前通讯方式为dtu
			System.out.println("标签栏被选择的索引为： " + "DTU");
			//textDTUID,textDtuPort,textHeart,textHeartInteval 		dtu|2000:9815:hello:180
			portInforStr = "dtu" + "|" + textDTUID.getText().trim() + ":" + textDtuPort.getText().trim() + ":" + textHeart.getText().trim() + ":" + textHeartInteval.getText().trim();						
		} else {						//当前通讯方式为串口Serial
			System.out.println("标签栏被选择的索引为： " + "Serial");
			//textSerialPort,comboBaud,comboDatabit,comboCheckbit,comboStopbit		serial|1:9600:8:无:1
			portInforStr = "serial" + "|" + textSerialPort.getText().trim() + ":" + comboBaud.getText().trim() + ":" + comboDatabit.getText().trim() + ":" + comboCheckbit.getText().trim() + ":" + comboStopbit.getText().trim();
		}
		return portInforStr;
	}
	
	/*
	 * 根据协议的不同获得相应的帧信息
	 */
	public String getFrameInforStr (int tabIndex) {
		StringBuffer framesInforStr = new StringBuffer("");
		if (tabIndex ==0) {				// 通讯规约为modbus
			GridItem [] gridItems = gridModbusFrame.getItems();
			framesStr temp1 = new framesStr();
			for ( int i=0; i< gridItems.length; i++) {
				temp1 = (framesStr)gridItems[i].getData();
				//1|3-100-10|0|soe, 1|3-100-10|0
				framesInforStr.append(temp1.getDeviceAddress()+ "|" + temp1.getFunctionID() + "-" +
						temp1.getDataAddress() + "-" + temp1.getDataLength() + "|" + temp1.getPriority());
				if (temp1.getFrameName()==null || temp1.getFrameName().length()==0) {
					//doNothing;
				} else {
					framesInforStr.append("|" + temp1.getFrameName());
				}
				
				if (i != (gridItems.length-1) ) {
					framesInforStr.append(",");
				}
				
			}
	
		} else if (tabIndex == 1) {		// 通信规约为iec104
			GridItem [] gridItems = gridIECFrame.getItems();
			IECFrameStr temp11 = new IECFrameStr();
			for( int j=0; j< gridItems.length; j++) {
				temp11 = (IECFrameStr)gridItems[j].getData();
				//【召唤类型编码】-【执行间隔(s)】|【帧名称(可省略)】
				framesInforStr.append(temp11.getCallTypeNum() + "-" + temp11.getInterval());
				if(temp11.getFrameName() == null || temp11.getFrameName().length() == 0) {
					//doNothing;
				} else {
					framesInforStr.append("|" + temp11.getFrameName());
				}
				
				if (j != (gridItems.length-1) ) {
					framesInforStr.append(",");
				}
			}
			
		} else {						// 暂时保留的其它规约
			framesInforStr.append("待扩展的帧处理方式！");
		}
		return framesInforStr.toString();
	}
}
