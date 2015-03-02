package com.ht.scada.config.view;

import java.util.List;

import javax.swing.JFileChooser;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.entity.PrecinctSystemConfig;
import com.ht.scada.common.tag.service.PrecinctSystemConfigService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.LayoutUtil;
import com.ht.scada.config.util.ViewPropertyChange;

/**
 * 相关主索引的系统组态图片关联
 * @author 王蓬
 * @time 2013.3.24
 * @time 2014.6.26 日增加联合站 处理方法
 */
public class SystemConfigPictureRelated extends ViewPart implements IPropertyChangeListener {
	
	public static String [] sysNameArray = { "系统总图", "集输系统", "注水系统" , "联合站", "注水站" };	// 系统名称数组（后期可提取成枚举变量）
	public int sysNameID = 0;					// 系统名ID，默认为总系统
	
	public static MajorTag majorTag;		// 该操作页面对应的主索引
	private List<PrecinctSystemConfig> precinctSystemConfigs;		// 存储某个管理区下已关联组态的系统
	
	private PrecinctSystemConfigService precinctSystemConfigService = (PrecinctSystemConfigService) Activator
			.getDefault().getApplicationContext().getBean("precinctSystemConfigService");					// 节点服务对象
	
	public static final String ID = "com.ht.scada.config.view.SystemConfigPictureRelated";
	private static final Logger log = LoggerFactory.getLogger(SystemConfigPictureRelated.class);
	private Text txtPictureFilePath;
	
	private Label lblManagementDistric ;
	private Combo comboSysnames ;
	private Label lblInfor ;
	
	public SystemConfigPictureRelated() {
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
		System.out.println("sdfdfdsfd1111");
		
		
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(4, false));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("管理区名称：");
		
		lblManagementDistric = new Label(parent, SWT.NONE);
		GridData gd_lblManagementDistric = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblManagementDistric.widthHint = 98;
		lblManagementDistric.setLayoutData(gd_lblManagementDistric);
		lblManagementDistric.setText("New Label");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setText("系统选择：");
		
		comboSysnames = new Combo(parent, SWT.READ_ONLY);
		comboSysnames.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				PrecinctSystemConfig psc = precinctSystemConfigService.findBySysNameAndMajorTagId(comboSysnames.getText().trim(), majorTag.getId());
				if ( psc == null ) {
					txtPictureFilePath.setText("暂未设计组态");		// 设置路径显示标签
				} else {
					txtPictureFilePath.setText(psc.getImagePath());	// 设置路径显示标签
				}
				
			}
		});
		comboSysnames.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Label label_2 = new Label(parent, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("组态图片路径：");
		
		txtPictureFilePath = new Text(parent, SWT.BORDER);
		txtPictureFilePath.setText(" ");
		GridData gd_txtPictureFilePath = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtPictureFilePath.widthHint = 136;
		txtPictureFilePath.setLayoutData(gd_txtPictureFilePath);
		txtPictureFilePath.setEditable(false);
		
		Button button = new Button(parent, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser j1 = new JFileChooser();
				// j1.setCurrentDirectory(new File("//192.168.0.212/软件/csView软件开发/组态图图片目录"));
				int n = j1.showOpenDialog(null);
				if(j1.getSelectedFile() != null ){
					String fileName = j1.getSelectedFile().toString();
					txtPictureFilePath.setText(fileName);			
				}
			}
		});
		button.setText("打 开");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 36;
		composite.setLayoutData(gd_composite);
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PrecinctSystemConfig psc = null ;
			
				boolean hasExist = false;
				for ( int i=0; i<precinctSystemConfigs.size(); i++ ) {
					if ( comboSysnames.getText().trim().equals( precinctSystemConfigs.get(i).getSysname() ) ) {
						hasExist = true;
						psc = precinctSystemConfigs.get(i);
						break;
					}
				}
				 
				if ( hasExist == false ) {		// 未存在，添加
					
					psc = new PrecinctSystemConfig();
					psc.setImagePath(txtPictureFilePath.getText().trim());
					psc.setMajorTag(majorTag);
					psc.setSysname(comboSysnames.getText().trim());
					
					if (psc.getSysname().equals(sysNameArray[0])) {
						sysNameID = 0; 	// 获得系统号
					} else if (psc.getSysname().equals(sysNameArray[1])){
						sysNameID = 1; 	// 获得系统号
					} else if (psc.getSysname().equals(sysNameArray[2])){ 
						sysNameID = 2; 	// 获得系统号
					} else if (psc.getSysname().equals(sysNameArray[3])){
						sysNameID = 3;
					} else if (psc.getSysname().equals(sysNameArray[4])){
						sysNameID = 4;
					}
					psc.setSysnameID(sysNameID);
					
					
					precinctSystemConfigService.create(psc);	// 创建新对象
					precinctSystemConfigs.add(psc);				// 新创建的加入已存在集合
					
				} else {						// 存在，修改
					
					psc.setImagePath(txtPictureFilePath.getText().trim());
					precinctSystemConfigService.update(psc);	// 更新对象
					
				}
				
				LayoutUtil.hideViewPart();		// 创建(修改)完即关闭此页面
			}
		});
		btnSave.setText("保 存");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LayoutUtil.hideViewPart();
			}
		});
		btnCancel.setText("取 消");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		
		lblInfor = new Label(parent, SWT.NONE);
		lblInfor.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		new Label(parent, SWT.NONE);
		
		ViewPropertyChange.getInstance().addPropertyChangeListener("systemConfigPictureRelated", this);
		
	
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		pageInit();	// 页面初始化
		
		ViewPropertyChange.getInstance().removePropertyChangeListener("systemConfigPictureRelated");
		super.dispose();
	}
	
	/**
	 * 页面初始化
	 */
	public void pageInit () {
		if ( majorTag == null ) {			// 主索引对象是空的
			return;
		}
		
		lblManagementDistric.setText(majorTag.getName());
		comboSysnames.setItems(sysNameArray);
		comboSysnames.select(0);
		
		precinctSystemConfigs = precinctSystemConfigService.findByMajorTagId(majorTag.getId()); // 获得该管理区已经设计的组态系统
		if ( precinctSystemConfigs.size() == 0 ) {
			lblInfor.setText("管理区暂未进行组态开发");
		} else {
			// 获得当前系统的组态信息对象
			PrecinctSystemConfig psc = precinctSystemConfigService.findBySysNameAndMajorTagId(comboSysnames.getText().trim(), majorTag.getId());
			
			if (psc==null) {
				txtPictureFilePath.setText("暂未设计组态");
			} else {
				txtPictureFilePath.setText(psc.getImagePath());
			}
		}
	}
	
}
