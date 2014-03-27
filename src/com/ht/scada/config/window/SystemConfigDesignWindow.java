package com.ht.scada.config.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.entity.PrecinctSystemConfig;
import com.ht.scada.common.tag.entity.PrecinctSystemEndTagList;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.PrecinctSystemConfigService;
import com.ht.scada.common.tag.service.PrecinctSystemEndTagListService;
import com.ht.scada.config.scadaconfig.Activator;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * 相关主索引的系统组态设计页面
 * @author 王蓬
 * @time 2013.3.24
 */
public class SystemConfigDesignWindow extends ApplicationWindow {
	
	public static String [] sysNameArray = { "系统总图", "集输系统", "注水系统" };	// 系统名称数组（后期可提取成枚举变量）
	
	private int screenWidth;		// 屏幕最大宽度
	private int screenHeight;		//　屏幕最大高度
	
	private int originalImageWidthObject ;	// 获得原图宽
	private int originalImageHeightObject;	// 获得原图长
	private int containerWidthObject ;		// 容器宽度
	private int containerHeightObject ;		// 容器高度
	
	private MajorTag majorTag;		// 主系统索引对象
	private List<EndTag> endtags;	// 系统下的所有监控对象

	private EndTagService endTagService = (EndTagService) Activator
			.getDefault().getApplicationContext().getBean("endTagService");					// 节点服务对象
	
	private PrecinctSystemConfigService precinctSystemConfigService = (PrecinctSystemConfigService) Activator
			.getDefault().getApplicationContext().getBean("precinctSystemConfigService");	// 系统组态服务对象
	private PrecinctSystemConfig precinctSystemConfig;				// 系统组态对象
	
	private PrecinctSystemEndTagListService precinctSystemEndTagListService = (PrecinctSystemEndTagListService) Activator
			.getDefault().getApplicationContext().getBean("precinctSystemEndTagListService");// 系统相关节点服务对象
	private List<PrecinctSystemEndTagList> precinctSystemEndTagListSets = new ArrayList<PrecinctSystemEndTagList>();	// 系统相关节点集合
	
	private ArrayList<Label> labelList = new ArrayList<Label> ();							// 用于存储添加的label控件
	private ArrayList<PrecinctSystemEndTagList> precinctSystemEndTagListDelete = new ArrayList<PrecinctSystemEndTagList>();		// 移出的 系统节点集合
	
	private Composite compositeImage;			// 图片容器
	private Combo comboSysname;					// 系统选择控件
	private GridData gd_compositeTop;			// 上部容器高度
	private Combo comboEndtag;					// 要添加的监控对象名称		
	private Composite composite_1 ;
	private GridData gd_composite_1 ;
	
	public SystemConfigDesignWindow(MajorTag majorTag) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);	// 模态且仅包含关闭按钮
		
		this.majorTag = majorTag;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("管理区系统组态设计");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     // 得到屏幕的尺寸
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		newShell.setSize(screenWidth, screenHeight);							// 初始大小
		newShell.setLocation(0, 0);												// 设置初始weizhi
	}
	
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new GridLayout(21, false));
		gd_compositeTop = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_compositeTop.heightHint = 35;
		composite.setLayoutData(gd_compositeTop);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("系统选择：");
		
		comboSysname = new Combo(composite, SWT.NONE);
		GridData gd_comboSysname = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboSysname.widthHint = 60;
		comboSysname.setLayoutData(gd_comboSysname);
		comboSysname.setItems(sysNameArray);
		comboSysname.select(0);
		new Label(composite, SWT.NONE);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("监控对象：");
		
		comboEndtag = new Combo(composite, SWT.NONE);
		GridData gd_comboEndtag = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboEndtag.widthHint = 112;
		comboEndtag.setLayoutData(gd_comboEndtag);

		
			
			Button btnAddEndtag = new Button(composite, SWT.NONE);
			btnAddEndtag.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					// 重新获得下系统组态对象（防止操作过程中更改系统）
					EndTag tempEndtag = endtags.get(comboEndtag.getSelectionIndex());			// 监控对象节点
					precinctSystemConfig = precinctSystemConfigService.findBySysNameAndMajorTagId(comboSysname.getText().trim(), majorTag.getId()); // 系统组态节点
					if ( precinctSystemConfig == null ) {		// 若为空，不允许进行其它操作
						JOptionPane.showMessageDialog(null, "该系统暂时未配置组态", "异常提示", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// 判断该监控节点是否已经被添加
					boolean exist = false;								// 要添加的监控节点已经存在
					for( int ww=0; ww< precinctSystemEndTagListSets.size(); ww++ ) {
						if ( tempEndtag.getName().equals( precinctSystemEndTagListSets.get(ww).getEndTag().getName()) ) {		// 同一个变量
							// System.out.println("正在试图添加已经存在的变量...");
							exist = true;
							break;
						}
					}
					
					if (exist == true) {				// 添加重复的变量
						MessageDialog.openInformation( Display.getCurrent().getActiveShell(), "异常提示", "请勿重复添加监控对象");
					} else {
						
						PrecinctSystemEndTagList psetl = new PrecinctSystemEndTagList();	// 管理区系统节点关联对象
						
						psetl.setEndTag(tempEndtag);					// 设置监控对象节点
						psetl.setPrecinctSystemConfig(precinctSystemConfig);
						precinctSystemEndTagListSets.add(psetl);		// 加入已有监控节点集合
						
						
						Label label = new Label(compositeImage, SWT.BORDER);
						label.setBounds(10, 10, 110, 27);				// 注意label的宽度定义需要根据实际待定
						label.setText(comboEndtag.getText().trim());	// 设置label的显示值
						
						labelList.add(label);					// 将新建的label放入集合中
						
						MoveLabelListener listener = new MoveLabelListener();	// 构造一个鼠标监听对象
						listener.setLabel(label);								// 设置关联信息
						listener.setListener(listener);
						listener.setContainer(compositeImage.getBounds());
						label.addMouseListener(listener);						// 为该label添加监听器
					}
					
					
				}
			});
			GridData gd_btnAddEndtag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btnAddEndtag.widthHint = 54;
			btnAddEndtag.setLayoutData(gd_btnAddEndtag);
			btnAddEndtag.setText("添  加");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnDesignSave = new Button(composite, SWT.NONE);
		btnDesignSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(precinctSystemEndTagListSets.size()  + " 个！" );
				
				
				for ( int w = 0 ; w < precinctSystemEndTagListSets.size() ; w++ ) {
					Label tempLabel = labelList.get(w);				// 获得一个label对象（此时label的坐标是绝对坐标，要进行容器内相对转换）
					int originalX = (tempLabel.getBounds().x*originalImageWidthObject) / containerWidthObject;
					int originalY = (tempLabel.getBounds().y*originalImageHeightObject) / containerHeightObject;
					
					PrecinctSystemEndTagList temp1 = precinctSystemEndTagListSets.get(w);	// 获得系统组态对象
					if ( temp1.getId()!=null ) {	// 原先已存在（更新）
						
					} else {						// 原先个性表中不存在（新建）
						precinctSystemEndTagListService.create(temp1);	// 创建
					}	
					
					// 设置其余基本内容
					temp1.setItemsLocateX(originalX);
					temp1.setItemsLocateY(originalY);
					
					temp1.setRangeLocateX(originalX);	// 设计范围
					temp1.setRangeLocateY(originalY);
					temp1.setRangeWidth(100);
					temp1.setRangeHeight(50);
					temp1.setItemsSet("123456789: " + temp1.getId());
					
					precinctSystemEndTagListService.update(temp1);
				}
				
				// 移出变量集合
				PrecinctSystemEndTagList temp11 = new PrecinctSystemEndTagList();	// 获得系统组态节点
				for ( int w= 0 ; w< precinctSystemEndTagListDelete.size(); w++ ) {
					temp11 = precinctSystemEndTagListDelete.get(w);
					// System.out.println(temp11.getTagCfgTpl().getTagName( ) + ", " + temp11.getId() );
					if ( temp11.getId()!=null ) {	// 删除了模板中已有的对象
						precinctSystemEndTagListService.deleteById(temp11.getId());
					}		
				}
								
				// 信息提示
				MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "信息提示", "保存成功");
				
				
			}
		});
		btnDesignSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnDesignSave.setText("设 计 保 存");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCancel.setText("取  消");
		
		composite_1 = new Composite(container, SWT.BORDER);
		composite_1.setLayout(new GridLayout(1, false));
		gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_1.heightHint = 64;						// 这个参数很重要，需要根据屏幕可以做自适应调整，以后再实现（防止变形）
		composite_1.setLayoutData(gd_composite_1);
		
		compositeImage = new Composite(composite_1, SWT.NONE);
		compositeImage.setBackgroundMode(SWT.INHERIT_DEFAULT);						// 设置透明
		
		// 对comboEndtag进行赋值
		endtags =  endTagService.getEndTagByMajorTagId(majorTag.getId());
		String [] endTagNames = new String[endtags.size()];
		for ( int i=0; i<endtags.size(); i++ ) {
			endTagNames[i] = endtags.get(i).getName();
		}
		comboEndtag.setItems(endTagNames);
		comboEndtag.select(0);
		
		pageInit();		// 页面初始化
		
		return container;
	}
	
	/**
	 * 进行页面初始化操作
	 */
	public void pageInit() {
		System.out.println("页面参数及元素初始化：");
		
		// 获得系统组态图片关联对象
		precinctSystemConfig = precinctSystemConfigService.findBySysNameAndMajorTagId(comboSysname.getText().trim(), majorTag.getId());
		if ( precinctSystemConfig == null || (precinctSystemConfig.getImagePath()==null || precinctSystemConfig.getImagePath().equals("")) ) {
			
			Label lblNewLabel = new Label(compositeImage, SWT.NONE);
			lblNewLabel.setBounds(10, 20, 300, 17);
			lblNewLabel.setText("该系统暂没进行组态配置，请先进行图片关联");
			
			return;
		} 
		
		// 主要部分初始化（1）图片 （2）内容
		imageFittingEditor(precinctSystemConfig.getImagePath()); // 进行图片加载（1）
		
		// 控件初始化放置位置， 加载已经配置的监控对象信息（2）
		alreadyConfigEndtagShow( );

	}
	
	/**
	 * 图片加载（编辑时调用）
	 */
	public void imageFittingEditor(String imagePath){
		// 设置图片显示
		
		int containerWidth = screenWidth - 40; 								// 容器宽度 (40是自定义的，给边框留出的像素)
		int containerHeight = screenHeight - gd_compositeTop.heightHint ; 	// 容器高度（屏幕高度-上部容器高度）
		int containerWidthOriginal = containerWidth;
		int containerHeightOriginal = containerHeight;
		//System.out.println("容器原始大小为(宽、高)： " + containerWidth + ", " + containerHeight);
		
		ImageData data = new ImageData(imagePath); 		// 构建原始图片信息对象
		final int originalImageWidth = data.width; 		// 获得原图宽
		final int originalImageHeight = data.height; 	// 获得原图长
		// System.out.println("图片大小为(宽、高)：" + originalImageWidth + ", " + originalImageHeight );

		// 根据图片比例及容器大小，调整容器
		if (originalImageWidth > containerWidth|| originalImageHeight > containerHeight) { // 原始图大于容器
			if (originalImageWidth > originalImageHeight ) {	// 按照图片比例(宽长)，重新绘制容器高度： 容器高度 = （容器宽度*图片高度）/ 图片宽度
				containerHeight = ( containerWidth * originalImageHeight ) / originalImageWidth ;
			} else {											// 按照图片比例(高长)，重新绘制容器宽度: 容器宽度 = (容器高度*图片宽度)/ 图片高度
				containerWidth = ( containerHeight * originalImageWidth ) / originalImageHeight	;
			}
			//System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);

		} else { // 原始图小于等于容器大小
			containerWidth = originalImageWidth;
			containerHeight = originalImageHeight;
			//System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);
		}	
		
		// 若设置的容器大小超出原始容器大小，再次根据原始容器带下调整容器 
		if (containerHeight > containerHeightOriginal ) {
			containerWidth = (containerWidth * containerHeightOriginal ) / containerHeight ;
			containerHeight = containerHeightOriginal;
		}
		if (containerWidth > containerWidthOriginal ) {
			containerHeight = (containerHeight * containerWidthOriginal) / containerWidth ;
			containerWidth  = containerWidthOriginal;
		}

		GridData gd_compositeImage1 = new GridData(SWT.FILL, SWT.FILL,false, false, 1, 1);
		gd_compositeImage1.widthHint = containerWidth;
		gd_compositeImage1.heightHint = containerHeight;
		compositeImage.setLayoutData(gd_compositeImage1);
		compositeImage.setBounds(0, 0, containerWidth, containerHeight);		// 应用网格后，坐标位置‘0’， ‘0’即没有用，仅设置宽、高即可(拖动时使用)
		// System.out.println(containerWidth + ", "  + containerHeight);

		ImageData dataFit = data.scaledTo(containerWidth, containerHeight); 	// 构造符合容器大小的图片信息对象
		Image image = new Image(compositeImage.getDisplay(), dataFit); 			// 构造图片对象
		compositeImage.setBackgroundImage(image);
		
		// 为全局变量赋值
		originalImageWidthObject = originalImageWidth;
		originalImageHeightObject = originalImageHeight;
		containerWidthObject = containerWidth;
		containerHeightObject = containerHeight;
		
		
		labelList.clear();	// 清空标签列表
		precinctSystemEndTagListSets.clear();
		precinctSystemEndTagListDelete.clear();
		
	}
	
	/**
	 * 加载已经配置的监控对象信息
	 * @param precinctSystemEndTagListSets
	 */
	public void alreadyConfigEndtagShow(  ) {
		precinctSystemEndTagListSets = precinctSystemEndTagListService.findByPrecinctSystemConfigId(precinctSystemConfig.getId());
		
		if (precinctSystemEndTagListSets.size()!=0) {						// 存在这少一个监控对象
			PrecinctSystemEndTagList temp =  new PrecinctSystemEndTagList();	
			for (int i = 0; i< precinctSystemEndTagListSets.size(); i++ ) {
				temp = precinctSystemEndTagListSets.get(i);
				if (temp.getItemsLocateX()!=null && temp.getItemsLocateY()!=null) {				// 该变量已经被设置组态坐标
					System.out.println(temp.getEndTag().getName());
					
					Label label = new Label(compositeImage, SWT.BORDER);
					int showX = ( temp.getItemsLocateX() * containerWidthObject) / originalImageWidthObject; 		// 相对容器的原图X坐标
					int showY = ( temp.getItemsLocateY() * containerHeightObject) / originalImageHeightObject;		// 相对容器的原图Y坐标
					// System.out.println(temp.getX() + ", " + containerWidthObject + ", " + originalImageWidthObject);
					// System.out.println(temp.getY() + ", " + containerHeightObject + ", " + originalImageHeightObject);
					label.setBounds(showX, showY, 110, 27);
					label.setText(temp.getEndTag().getName());				// 设置label的显示值

					MoveLabelListener listener = new MoveLabelListener();	// 构造一个鼠标监听对象
					listener.setLabel(label);								// 设置关联信息
					listener.setListener(listener);
					listener.setContainer(compositeImage.getBounds());
					label.addMouseListener(listener);						// 为该label添加监听器

					labelList.add(label);									// 将 该标签加入集合
				}		
			}
		}
	}
	
	/**
	 * 监听器类，实现对鼠标事件的监听
	 * @author 王蓬
	 * @time   2013.12.5
	 */
	class MoveLabelListener implements MouseMoveListener, MouseListener   {

		private Label label;						// 监听的label
		private MoveLabelListener listener;			// 监听对象
		private Rectangle container;				// label所在的容器信息
		
		public Rectangle getContainer() {
			return container;
		}
		public void setContainer(Rectangle container) {
			this.container = container;
		}

		public MoveLabelListener getListener() {
			return listener;
		}
		public void setListener(MoveLabelListener listener) {
			this.listener = listener;
		}

		public Label getLabel() {
			return label;
		}
		public void setLabel(Label label) {
			this.label = label;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			 int j = JOptionPane.showConfirmDialog(null,"确定要删除此变量么？","信息提示",JOptionPane.YES_NO_OPTION);          
			 
			 if(j==0){            
				 // System.out.println("确定删除");
				 Label tempLabel = (Label) e.getSource();		// 获得时间操作的标签
				 tempLabel.setVisible(false);					// 隐藏按钮
				 
				 endTagDeleteProcess(tempLabel);// 调用删除变量操作	 
				 
			 } else {
				 System.out.println("取消删除");
			 }
		}

		// 当鼠标按下时，注册鼠标移动监听器
		@Override
		public void mouseDown(MouseEvent e) {
			// TODO Auto-generated method stub
			label.addMouseMoveListener(listener);
						
			if (e.button ==3 ) {									// 捕捉到了鼠标右击事件		
				final JPopupMenu popup = new JPopupMenu("Popup");  
				final JMenuItem item1  = new JMenuItem("删除变量");
				final Label tempLabel = (Label) e.getSource();		// 获得时间操作的标签
				
				item1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						 int j = JOptionPane.showConfirmDialog(null,"确定要删除此变量么？","信息提示",JOptionPane.YES_NO_OPTION);     
						 if(j==0){            
							 //System.out.println("确定删除");
							 Display.getDefault().syncExec(new Runnable() {
									public void run() {
										tempLabel.setVisible(false);					// 隐藏按钮
									}
								});
							 endTagDeleteProcess(tempLabel);		// 调用删除变量操作	 
						 } else {
							// System.out.println("取消删除");
						 }
						
						 popup.setVisible(false);
					}
				});
				popup.add(item1);

				//e.x是label框内的相对坐标
				popup.show(null, tempLabel.getBounds().x + e.x , tempLabel.getBounds().y + e.y + gd_composite_1.heightHint);
				popup.setInvoker(null);
			}
			
		}
		

		// 当鼠标抬起时，停止拖放，移除鼠标移动监听器
		@Override
		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			label.removeMouseMoveListener(listener);			
		}

		// 当鼠标移动时
		@Override
		public void mouseMove(MouseEvent e) {		 
			// TODO Auto-generated method stub
			// 将当前鼠标位置转化为窗口的坐标位置
			Point convertPoint = Display.getCurrent().map(label, Display.getCurrent().getActiveShell(), e.x, e.y);
			Rectangle current = label.getBounds();
			
			int labelNormalX = (convertPoint.x - (container.x + composite_1.getBounds().x + 5));
			int labelNormalY = convertPoint.y - gd_composite_1.heightHint;
			
			// 移除容器判断（使新添加的label只能出现在容器中）
			if ( (convertPoint.x - (container.x + composite_1.getBounds().x + 5)) < 0 ) { 	// 移出左边界
				labelNormalX = 0;
			} 
			if ( (convertPoint.y - gd_composite_1.heightHint) < 0 ) {						// 移出上边界	
				labelNormalY = 0;
			} 	
			if ( (convertPoint.x + current.width) > (container.x + composite_1.getBounds().x + 5 + container.width)) {// 移出右边界
				labelNormalX = container.width - current.width;
			}
			if ( (convertPoint.y + current.height) > (gd_composite_1.heightHint + container.height)) {				// 移出下边界
				labelNormalY = container.height - current.height;
			}
		
			// 重新设置按钮的位置，使之跟随鼠标移动 (相对坐标)
			label.setBounds( labelNormalX, labelNormalY , current.width, current.height);
		}
	}
	
	/**
	 * 删除节点的一些操作
	 * @param tempLabel
	 */
	public void endTagDeleteProcess ( Label tempLabel ) {
		 int endTagIndexInList = 0; 
		 for(int i=0 ; i<labelList.size();i++){
			 if(labelList.get(i) == tempLabel ){
				 endTagIndexInList = i;							// 获得选中的索引
				 break;
			 }
		 }
		 
		 PrecinctSystemEndTagList temp = precinctSystemEndTagListSets.get(endTagIndexInList);	// 移出的对象
		 precinctSystemEndTagListDelete.add(temp);
		 
		 labelList.remove(endTagIndexInList);					// 从集合中移出
		 precinctSystemEndTagListSets.remove(endTagIndexInList);			 
	}
	
	
}
