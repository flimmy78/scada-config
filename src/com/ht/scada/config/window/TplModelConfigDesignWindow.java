package com.ht.scada.config.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ht.scada.common.tag.entity.TagCfgTpl;
import com.ht.scada.common.tag.entity.TplModelConfig;
import com.ht.scada.common.tag.service.TagCfgTplService;
import com.ht.scada.common.tag.service.TplModelConfigService;
import com.ht.scada.config.scadaconfig.Activator;

/**
 * 变量模板的组态图设计类
 * @author 王蓬
 *
 */
public class TplModelConfigDesignWindow extends ApplicationWindow {
		
	private int screenWidth;		// 屏幕最大宽度
	private int screenHeight;		//　屏幕最大高度
	
	private int originalImageWidthObject ;	// 获得原图宽
	private int originalImageHeightObject;	// 获得原图长
	private int containerWidthObject ;		// 容器宽度
	private int containerHeightObject ;		// 容器高度
	
	private String tplModelName;															// 传递进来的变量模板名参数
	private TplModelConfigService tplModelConfigService = (TplModelConfigService) Activator
			.getDefault().getApplicationContext().getBean("tplModelConfigService");			// 服务对象
	TplModelConfig tplModelConfig = new TplModelConfig();									// 模板组态对象
	
	private TagCfgTplService tagCfgTplService = (TagCfgTplService) Activator
			.getDefault().getApplicationContext().getBean("tagCfgTplService");				// 服务对象
	private List<TagCfgTpl> tagCfgTplList = new ArrayList<>(); 								// 当前模板所有变量
	
	private ArrayList<Label> labelList = new ArrayList<Label> ();							// 用于存储添加的label控件
	private ArrayList<TagCfgTpl> tagCfgTplListWithLabel = new ArrayList<TagCfgTpl>();		// 添加标签关联的变量对象
	private ArrayList<TagCfgTpl> tagCfgTplListDelete = new ArrayList<TagCfgTpl>();		// 移出的变量对象
	
	
	private Text text;
	private Label lblNewLabel_1;
	private Combo combo;
	private Composite compositeImage;
	private Composite composite ;
	private GridData gd_composite_1 ;
	private Composite composite_1;
	
	/**
	 * Create the application window.
	 */
	public TplModelConfigDesignWindow(String tplModelName) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);	// 模态且仅包含关闭按钮
		
		this.tplModelName = tplModelName;
		
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.BORDER);
		container.setLayout(new GridLayout(1, false));
		
		
		container.addControlListener(new ControlListener(){

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				System.out.println("大小发生了变化...");
			}});
		
		composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new GridLayout(33, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite.heightHint = 35;
		composite.setLayoutData(gd_composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("模板名：");
		
		lblNewLabel_1 = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 75;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText("运行补充");
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.widthHint = 90;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setText("监控变量选择：");
		
		combo = new Combo(composite, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 94;
		combo.setLayoutData(gd_combo);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 判断该变量是否已经被添加
				if (tagCfgTplListWithLabel.contains(tagCfgTplList.get(combo.getSelectionIndex()))) {
					JOptionPane.showMessageDialog(null, "请勿添加重复变量！", "错误操作提示", JOptionPane.ERROR_MESSAGE);
				} else {
					tagCfgTplListWithLabel.add(tagCfgTplList.get(combo.getSelectionIndex()));
					
					Label label = new Label(compositeImage, SWT.BORDER);
					label.setBounds(10, 10, 110, 27);		// 注意label的宽度定义需要根据实际待定
					label.setText(combo.getText().trim());	// 设置label的显示值
					
					labelList.add(label);					// 将新建的label放入集合中
					
					MoveLabelListener listener = new MoveLabelListener();	// 构造一个鼠标监听对象
					listener.setLabel(label);								// 设置关联信息
					listener.setListener(listener);
					listener.setContainer(compositeImage.getBounds());
					label.addMouseListener(listener);						// 为该label添加监听器
				}

			}
		});
		btnNewButton.setText("添  加");
		new Label(composite, SWT.NONE);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_1.widthHint = 180;
		label_1.setLayoutData(gd_label_1);
		label_1.setText("说明：左键拖拽 右键或双击删除 ");
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
		new Label(composite, SWT.NONE);
		
		Label label = new Label(composite, SWT.NONE);
		GridData gd_label = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 8, 1);
		gd_label.widthHint = 65;
		label.setLayoutData(gd_label);
		label.setText("组态底图：");
		
		text = new Text(composite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 359;
		text.setLayoutData(gd_text);
		
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser j1 = new JFileChooser();
				j1.setCurrentDirectory(new File("//192.168.0.212/软件/csView软件开发/组态图图片目录")); //\\192.168.0.212\软件\csView软件开发\组态图图片目录
				int n = j1.showOpenDialog(null);
				if(j1.getSelectedFile() != null ){
					String fileName = j1.getSelectedFile().toString();
					text.setText(fileName);			
				}
				
				imageFittingInit();				// 插入图片匹配
				
			}
		});
		btnNewButton_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton_2.setText("选    择");
		
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TagCfgTpl temp  = new TagCfgTpl();			// 存储变量的临时对象
				for (int i =0; i< labelList.size(); i++ ) {
					Label tempLabel = labelList.get(i);						// 获得一个label对象（此时label的坐标是绝对坐标，要进行容器内相对转换）

					// System.out.println("相对坐标: " + tempLabel.getBounds().x + ", " + tempLabel.getBounds().y);				
					int originalX = (tempLabel.getBounds().x*originalImageWidthObject) / containerWidthObject;
					int originalY = (tempLabel.getBounds().y*originalImageHeightObject) / containerHeightObject;
					// System.out.println("原图坐标：" + originalX +", " + originalY );
					
					temp = tagCfgTplListWithLabel.get(i);
					temp.setX(originalX);
					temp.setY(originalY);
					tagCfgTplService.update(temp);		// 更改变量的X、Y坐标值				
				}
				
				// 移出变量集合
				for ( int w= 0 ; w< tagCfgTplListDelete.size(); w++ ) {
					 temp = tagCfgTplListDelete.get(w);
					 temp.setX(null);					// 数据更新  (null, null)
					 temp.setY(null);
					 tagCfgTplService.update(temp);
				}
								
				// 关联模板与组态底图
				TplModelConfig tplModelConfig = tplModelConfigService.findByTplname(tplModelName);
				if (tplModelConfig == null) {								// 没有新建
					tplModelConfig = new TplModelConfig();
					tplModelConfig.setImagePath(text.getText().trim());
					tplModelConfig.setTplName(tplModelName);
					tplModelConfigService.create(tplModelConfig);
						
				} else {													// 有修改已有
					tplModelConfig.setImagePath(text.getText().trim());
					tplModelConfigService.update(tplModelConfig);
				}

				// 信息提示
				JOptionPane.showMessageDialog(null,"保存成功","信息提示",JOptionPane.INFORMATION_MESSAGE);          			
			}
		});
		GridData gd_btnNewButton_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 86;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("设 计 保 存");
		
		composite_1 = new Composite(container, SWT.BORDER);
		composite_1.setLayout(new GridLayout(1, false));
		gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_1.heightHint = 64;						// 这个参数很重要，需要根据屏幕可以做自适应调整，以后再实现（防止变形）
		composite_1.setLayoutData(gd_composite_1);
		
		compositeImage = new Composite(composite_1, SWT.BORDER);		
		compositeImage.setBackgroundMode(SWT.INHERIT_DEFAULT);						// 设置透明
		
		
		// 初始化控件
		initWindow(tplModelName);
		return container;
	}


	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the action
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		/*
		try {
			TplModelConfigDesign window = new TplModelConfigDesign(tplModelName);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("变量模板组态设计");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     // 得到屏幕的尺寸
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		newShell.setSize(screenWidth, screenHeight);							// 初始大小
		newShell.setLocation(0, 0);												// 设置初始weizhi

	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(793, 650);
	}
	
	/**
	 * 初始化控件
	 * @param tplName 变量模板名
	 */
	public void initWindow(String tplName){
		// 初始化控件
		lblNewLabel_1.setText(tplModelName);
		
		tagCfgTplList = tagCfgTplService.findVariablesByTplName(tplModelName);	// 获得该模板下的所有变量
		String [] tplNameArray = new String [tagCfgTplList.size()];
		for (int i= 0 ; i<tplNameArray.length; i++) {
			tplNameArray[i] = tagCfgTplList.get(i).getTagName();
		}
		combo.setItems(tplNameArray);							// 给combo赋值
		combo.select(0);

		
		tplModelConfig = tplModelConfigService.findByTplname(tplName);
		if (tplModelConfig == null ) {							// 还没有为模板配置组态图
			
			
		} else {												// 已经为模板配置组态图
			text.setText(tplModelConfig.getImagePath());		//　获得图片路径

			imageFittingEditor();								// 插入图片匹配

			//
			tagCfgTplList = tagCfgTplService.findVariablesByTplName(tplName);	// 获得该模板下的所有变量
			if (tagCfgTplList.size()!=0) {										// 存在这少一个变量
				TagCfgTpl temp =  new TagCfgTpl();	
				for (int i = 0; i< tagCfgTplList.size(); i++ ) {
					temp = tagCfgTplList.get(i);
					if (temp.getX()!=null && temp.getY()!=null) {				// 该变量已经被设置组态坐标
						
						Label label = new Label(compositeImage, SWT.BORDER);
						int showX = ( temp.getX() * containerWidthObject) / originalImageWidthObject; 		// 相对容器的原图X坐标
						int showY = ( temp.getY() * containerHeightObject) / originalImageHeightObject;		// 相对容器的原图Y坐标
						System.out.println(temp.getX() + ", " + containerWidthObject + ", " + originalImageWidthObject);
						System.out.println(temp.getY() + ", " + containerHeightObject + ", " + originalImageHeightObject);
						label.setBounds(showX, showY, 110, 27);
						label.setText(temp.getTagName());		// 设置label的显示值
						
						MoveLabelListener listener = new MoveLabelListener();	// 构造一个鼠标监听对象
						listener.setLabel(label);								// 设置关联信息
						listener.setListener(listener);
						listener.setContainer(compositeImage.getBounds());
						label.addMouseListener(listener);						// 为该label添加监听器
							
						tagCfgTplListWithLabel.add(temp);						// 将已有坐标信息的变量添加进当前方案
						labelList.add(label);
					}		
				}
			}
			
			
		//	System.out.println(tagCfgTplListAready.size() + " PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
		}
		
	}
	
	/**
	 * 图片加载（编辑时调用）
	 */
	public void imageFittingEditor(){
		// 设置图片显示
		int containerWidth = screenWidth - 40; 									// 容器宽度 (40是自定义的，给边框留出的像素)
		int containerHeight = screenHeight - gd_composite_1.heightHint- 80; 	// 容器高度（屏幕高度-上部容器高度）
		int containerWidthOriginal = containerWidth;
		int containerHeightOriginal = containerHeight;
		//System.out.println("容器原始大小为(宽、高)： " + containerWidth + ", " + containerHeight);
		
		ImageData data = new ImageData(text.getText().trim()); // 构建原始图片信息对象
		final int originalImageWidth = data.width; // 获得原图宽
		final int originalImageHeight = data.height; // 获得原图长
		//System.out.println("图片大小为(宽、高)：" + originalImageWidth + ", " + originalImageHeight );

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
		//System.out.println(containerWidth + ", "  + containerHeight);

		ImageData dataFit = data.scaledTo(containerWidth, containerHeight); 	// 构造符合容器大小的图片信息对象
		Image image = new Image(compositeImage.getDisplay(), dataFit); 			// 构造图片对象
		compositeImage.setBackgroundImage(image);
		
		// 为全局变量赋值
		originalImageWidthObject = originalImageWidth;
		originalImageHeightObject = originalImageHeight;
		containerWidthObject = containerWidth;
		containerHeightObject = containerHeight;
		
		for (int i=0 ;i < labelList.size(); i++ ){					// 擦出已拖拽变量
			Label tempLabel = labelList.get(i);						// 获得一个label对象（此时label的坐标是绝对坐标，要进行容器内相对转换）
			tempLabel.setVisible(false)	;
		}
		labelList.clear();	// 清空标签列表
		tagCfgTplListWithLabel.clear();
		tagCfgTplListDelete.clear();
	}
	
	/**
	 * 图片加载（初始时调用）
	 */
	public void imageFittingInit() {

		// 设置图片显示
		int containerWidth = screenWidth - 40; 								// 容器宽度 (40是自定义的，给边框留出的像素)
		int containerHeight = screenHeight - gd_composite_1.heightHint - 80; // 容器高度（屏幕高度-上部容器高度）
		int containerWidthOriginal = containerWidth;
		int containerHeightOriginal = containerHeight;
		// System.out.println("容器原始大小为(宽、高)： " + containerWidth + ", " +
		// containerHeight);

		ImageData data = new ImageData(text.getText().trim()); 	// 构建原始图片信息对象
		final int originalImageWidth = data.width; 				// 获得原图宽
		final int originalImageHeight = data.height; 			// 获得原图长
		// System.out.println("图片大小为(宽、高)：" + originalImageWidth + ", " + originalImageHeight );

		// 根据图片比例及容器大小，调整容器
		if (originalImageWidth > containerWidth|| originalImageHeight > containerHeight) { 	// 原始图大于容器
			if (originalImageWidth > originalImageHeight) { 								// 按照图片比例(宽长)，重新绘制容器高度：容器高度 =（容器宽度*图片高度）/ 图片宽度
				containerHeight = (containerWidth * originalImageHeight) / originalImageWidth;
			} else { 																		// 按照图片比例(高长)，重新绘制容器宽度: 容器宽度 = (容器高度*图片宽度)/ 图片高度
				containerWidth = (containerHeight * originalImageWidth) / originalImageHeight;
			}
			// System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);

		} else { // 原始图小于等于容器大小
			containerWidth = originalImageWidth;
			containerHeight = originalImageHeight;
			// System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);
		}

		// 若设置的容器大小超出原始容器大小，再次根据原始容器带下调整容器
		if (containerHeight > containerHeightOriginal) {
			containerWidth = (containerWidth * containerHeightOriginal) / containerHeight;
			containerHeight = containerHeightOriginal;
		}
		if (containerWidth > containerWidthOriginal) {
			containerHeight = (containerHeight * containerWidthOriginal) / containerWidth;
			containerWidth = containerWidthOriginal;
		}

		compositeImage.setBounds(5, 5, containerWidth, containerHeight);
		ImageData dataFit = data.scaledTo(containerWidth, containerHeight); 	// 构造符合容器大小的图片信息对象
		Image image = new Image(compositeImage.getDisplay(), dataFit);			// 构造图片对象
		compositeImage.setBackgroundImage(image);
		
		// 为全局变量赋值
		originalImageWidthObject = originalImageWidth;
		originalImageHeightObject = originalImageHeight;
		containerWidthObject = containerWidth;
		containerHeightObject = containerHeight;
		
		
		for (int i=0 ;i < labelList.size(); i++ ){					// 擦出已拖拽变量
			Label tempLabel = labelList.get(i);						// 获得一个label对象（此时label的坐标是绝对坐标，要进行容器内相对转换）
			tempLabel.setVisible(false)	;
		}
		labelList.clear();	// 清空标签列表
		tagCfgTplListWithLabel.clear();
		tagCfgTplListDelete.clear();
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
			// TODO Auto-generated method stub
			 int j = JOptionPane.showConfirmDialog(null,"确定要删除此变量么？","信息提示",JOptionPane.YES_NO_OPTION);          
			 if(j==0){            
				 System.out.println("确定删除");
				 Label tempLabel = (Label) e.getSource();		// 获得时间操作的标签
				 // System.out.println(tempLabel);
				 tempLabel.setVisible(false);					// 隐藏按钮
				 int tplIndexInTplList = 0; 
				 for(int i=0 ; i<labelList.size();i++){
					 if(labelList.get(i) == tempLabel ){
						 tplIndexInTplList = i;	// 获得选中的索引
						 break;
					 }
				 }
				 
				 TagCfgTpl temp = tagCfgTplListWithLabel.get(tplIndexInTplList);	// 获得移出的对象
				 tagCfgTplListDelete.add(temp);							// 加入移出变量集合

				 labelList.remove(tplIndexInTplList);					// 从集合中移出
				 tagCfgTplListWithLabel.remove(tplIndexInTplList);
				 
				 
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
								
								 int tplIndexInTplList = 0; 
								 for(int i=0 ; i<labelList.size();i++){
									 if(labelList.get(i) == tempLabel ){
										 tplIndexInTplList = i;	// 获得选中的索引
										 break;
									 }
								 }
								 
								 TagCfgTpl temp = tagCfgTplListWithLabel.get(tplIndexInTplList);	// 获得移出的对象
								 tagCfgTplListDelete.add(temp);							// 加入移出变量集合

								 labelList.remove(tplIndexInTplList);					// 从集合中移出
								 tagCfgTplListWithLabel.remove(tplIndexInTplList);
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
	

	
	
	


	
	
}
