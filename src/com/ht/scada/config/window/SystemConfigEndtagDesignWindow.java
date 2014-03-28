package com.ht.scada.config.window;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.PrecinctSystemEndTagList;
import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.service.PrecinctSystemEndTagListService;
import com.ht.scada.common.tag.service.TagCfgTplService;
import com.ht.scada.config.scadaconfig.Activator;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SystemConfigEndtagDesignWindow extends ApplicationWindow {

	private PrecinctSystemEndTagList precinctSystemEndTagListObject;			// 传入的节点组态对象'
	private Label shownlabel;													// 传入的展示标签
	private PrecinctSystemEndTagListService precinctSystemEndTagListService = (PrecinctSystemEndTagListService) Activator
			.getDefault().getApplicationContext().getBean("precinctSystemEndTagListService");// 系统相关节点服务对象
	
	//private ArrayList<String> shownItemsStrArray = new ArrayList<String>(); 	// 要展示的项集合
	private ArrayList<TagCfgTpl> shownItemsStrArray = new ArrayList<>(); 		// 要展示的所有变量
	
	public static String splitOfItems = ":";		// 项之间的分割符
	
	private TagCfgTplService tagCfgTplService = (TagCfgTplService) Activator
			.getDefault().getApplicationContext().getBean("tagCfgTplService");	// 服务对象
	private ArrayList<TagCfgTpl> tagCfgTplList = new ArrayList<>(); 			// 当前模板所有变量
	
	private List listAll;		// 所有变量列表
	private List listShown;		// 要展示变量列表
	private Text txtRangeX;
	private Text txtRangeY;
	private Text txtRangeWidth;
	private Text txtRangeHeight;
	private Label lblEndtag;
	private Label lbllocateX;
	private Label lbllocateY;
	private Label label_5 ;
	
	public SystemConfigEndtagDesignWindow(Shell parentShell, PrecinctSystemEndTagList temp, Label tempLabel) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);	// 模态且仅包含关闭按钮
		
		this.precinctSystemEndTagListObject = temp;
		this.shownlabel = tempLabel;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("监控节点详细设置");
		//newShell.setSize(500, 500);
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     // 得到屏幕的尺寸
//		screenWidth = screenSize.width;
//		screenHeight = screenSize.height;
//		newShell.setSize(screenWidth, screenHeight);							// 初始大小
//		newShell.setLocation(0, 0);												// 设置初始weizhi
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SystemConfigEndtagDesignWindow window = new SystemConfigEndtagDesignWindow(null, null, null);
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Create contents of the window.
	 */
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group.setText("基本信息");
		
		Group group_2 = new Group(container, SWT.NONE);
		group_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 2));
		group_2.setText("显示变量设置");
		group_2.setLayout(null);
		
		Label label_3 = new Label(group_2, SWT.NONE);
		label_3.setBounds(20, 22, 60, 17);
		label_3.setText("全部变量：");
		
		Label label_4 = new Label(group_2, SWT.NONE);
		label_4.setBounds(157, 22, 60, 17);
		label_4.setText("展示变量：");
		
		listAll = new List(group_2, SWT.BORDER | SWT.V_SCROLL);
		listAll.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
		listAll.setBounds(20, 45, 111, 163);
		listAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				int selectIndex = listAll.getSelectionIndex();	// 获得选择的索引
				if ( selectIndex != -1 ) {		// 存在变量并进行了选择
					
					if ( shownItemsStrArray.contains(tagCfgTplList.get(selectIndex)) ) {			// 判断是否重复
						MessageDialog.openInformation(getParentShell(), "异常提示", "请勿重复添加变量");
						return;
					}
					
					shownItemsStrArray.add(tagCfgTplList.get(selectIndex));
					String [] nowShowItems = new String [shownItemsStrArray.size()];
					for ( int i=0; i<nowShowItems.length; i++ ) {
						nowShowItems[i] = shownItemsStrArray.get(i).getTagName();
					}
					
				    listShown.setItems(nowShowItems);
				} 
				
			}
		});
		listAll.setItems(new String[] {});
		
		listShown = new List(group_2, SWT.BORDER | SWT.V_SCROLL);
		listShown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				int selectIndex = listShown.getSelectionIndex();	// 获得选择的索引
			
				if ( selectIndex != -1 ) {		// 存在变量并进行了选择
					
					// 确定要删除么
					int j = JOptionPane.showConfirmDialog(null,"确定要删除么？","信息提示",JOptionPane.YES_NO_OPTION);          
					if(j==0){   
						
						shownItemsStrArray.remove(selectIndex);
						String [] nowShowItems = new String [shownItemsStrArray.size()];
						for ( int i=0; i<nowShowItems.length; i++ ) {
							nowShowItems[i] = shownItemsStrArray.get(i).getTagName();
						}
					    listShown.setItems(nowShowItems);
					    
					} else {
						System.out.println("取消删除");
					}
				} 
				
			}
		});
		listShown.setForeground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		listShown.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		listShown.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
		listShown.setBounds(157, 45, 110, 163);
		
		label_5 = new Label(group_2, SWT.NONE);
		label_5.setBounds(20, 212, 94, 16);
		label_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		label_5.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
		label_5.setText("说明: 双击向右添加");
		
		Label label_6 = new Label(group_2, SWT.NONE);
		label_6.setBounds(157, 214, 94, 16);
		label_6.setText("说明: 双击删除展示");
		label_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		label_6.setFont(SWTResourceManager.getFont("微软雅黑", 8, SWT.NORMAL));
		
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		group_1.setText("响应区设置");
		group.setLayout(null);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(37, 22, 60, 17);
		lblNewLabel.setText("监控对象：");
		
		lblEndtag = new Label(group, SWT.NONE);
		lblEndtag.setBounds(102, 22, 92, 17);
		lblEndtag.setText("New Label");
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(37, 44, 60, 17);
		label.setText("展示坐标：");
		
		lbllocateX = new Label(group, SWT.NONE);
		lbllocateX.setBounds(102, 44, 92, 17);
		lbllocateX.setText("New Label");
		
		lbllocateY = new Label(group, SWT.NONE);
		lbllocateY.setBounds(102, 66, 92, 17);
		lbllocateY.setText("New Label");
		group_1.setLayout(null);
		
		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setBounds(37, 25, 56, 17);
		lblNewLabel_4.setText("起点X：   ");
		
		txtRangeX = new Text(group_1, SWT.BORDER);
		txtRangeX.setBounds(102, 22, 71, 23);
		
		Label lbly = new Label(group_1, SWT.NONE);
		lbly.setBounds(37, 53, 55, 17);
		lbly.setText("起点Y：   ");
		
		txtRangeY = new Text(group_1, SWT.BORDER);
		txtRangeY.setBounds(102, 50, 71, 23);
		
		Label label_1 = new Label(group_1, SWT.NONE);
		label_1.setBounds(37, 81, 60, 17);
		label_1.setText("右延长度：");
		
		txtRangeWidth = new Text(group_1, SWT.BORDER);
		txtRangeWidth.setBounds(102, 78, 71, 23);
		
		Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setBounds(37, 109, 60, 17);
		label_2.setText("下延长度：");
		
		txtRangeHeight = new Text(group_1, SWT.BORDER);
		txtRangeHeight.setBounds(102, 106, 71, 23);
		
		Label lblExplanation = new Label(container, SWT.NONE);
		lblExplanation.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblExplanation.setText(" 说明: 响应区域为操作时图片的点击事件范围！");
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite.heightHint = 28;
		composite.setLayoutData(gd_composite);
		
		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				precinctSystemEndTagListObject.setRangeLocateX(Integer.parseInt(txtRangeX.getText().trim()));	// 设置范围
				precinctSystemEndTagListObject.setRangeLocateY(Integer.parseInt(txtRangeY.getText().trim()));
				precinctSystemEndTagListObject.setRangeWidth(Integer.parseInt(txtRangeWidth.getText().trim()));
				precinctSystemEndTagListObject.setRangeHeight(Integer.parseInt(txtRangeHeight.getText().trim()));
				
				String itemsSet = "";	// 设置的项的集合
				// shownItemsStrArray
				for ( int i=0; i<shownItemsStrArray.size(); i++ ) {
					if ( i != (shownItemsStrArray.size()-1) ) {
						itemsSet = itemsSet + shownItemsStrArray.get(i).getVarName() + splitOfItems;
					} else {
						itemsSet = itemsSet + shownItemsStrArray.get(i).getVarName() ;
					}
						
				}
				precinctSystemEndTagListObject.setItemsSet(itemsSet);					// 设置展示变量集合
				precinctSystemEndTagListService.update(precinctSystemEndTagListObject);	// 更新对象	
				
				// 更新label
				if ( shownItemsStrArray.size() == 0 ) {
					
				} else {
					shownlabel.setText(precinctSystemEndTagListObject.getEndTag().getName() + " : " + shownItemsStrArray.size() + "个 ");
				}
				
				MessageDialog.openInformation(getParentShell(), "信息提示", "保存成功");
				
			}
		});
		GridData gd_btnSave = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnSave.widthHint = 52;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("保 存");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 54;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("取 消");
		
		
		pageInit();	// 页面初始化
		
		return container;
	}
	
	/**
	 * 页面参数初始化
	 */
	public void pageInit () {
		
		lblEndtag.setText(precinctSystemEndTagListObject.getEndTag().getName());
		lbllocateX.setText("X坐标： " + precinctSystemEndTagListObject.getItemsLocateX() + "");
		lbllocateY.setText("Y坐标： " + precinctSystemEndTagListObject.getItemsLocateY() + "");
		txtRangeX.setText(precinctSystemEndTagListObject.getRangeLocateX() + "");
		txtRangeY.setText(precinctSystemEndTagListObject.getRangeLocateY() + "");
		txtRangeWidth.setText(precinctSystemEndTagListObject.getRangeWidth() + "");
		txtRangeHeight.setText(precinctSystemEndTagListObject.getRangeHeight() + "");
		 
		// 初始化所有变量
		EndTag endTag = precinctSystemEndTagListObject.getEndTag();	// 获得节点对象
		if ( endTag.getTplName() == null ||endTag.getTplName().equals( "" ) ) {
			label_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			label_5.setText("未关联模板");
			return;
		} else {
			tagCfgTplList = (ArrayList<TagCfgTpl>) tagCfgTplService.findVariablesByTplName(endTag.getTplName());	// 获得该节点关联模板的所有变量
			String [] tplNameArray = new String [tagCfgTplList.size()];
			for (int i= 0 ; i<tplNameArray.length; i++) {
				tplNameArray[i] = tagCfgTplList.get(i).getTagName();
			}
			listAll.setItems(tplNameArray);							// 给listAll赋值
		}
		
		
		
		shownItemsStrArray.clear();		// 先清空一下
		// 初始化已经关联变量
		if ( precinctSystemEndTagListObject.getItemsSet() == null || precinctSystemEndTagListObject.getItemsSet().equals("") ) {
			return ;
		} else {
			String [] showArray = precinctSystemEndTagListObject.getItemsSet().split(splitOfItems);	// 这是varName数组
			// 构造tagName数组用于显示
			
			String [] showTagnameArray = new String[showArray.length];
			for ( int i=0; i< showArray.length; i++ ) {
				for ( int j=0; j<tagCfgTplList.size(); j++ ) {
					
					if ( showArray[i].equals(tagCfgTplList.get(j).getVarName()) ) {
						showTagnameArray[i] = tagCfgTplList.get(j).getTagName();
						shownItemsStrArray.add(tagCfgTplList.get(j));				// 添加入展示集合
						break;
					}
					
				}
			}
			
			listShown.setItems(showTagnameArray);
		}
		
		
	}
}
