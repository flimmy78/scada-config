package com.ht.scada.config.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.EndTagConfig;
import com.ht.scada.common.tag.service.EndTagConfigService;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.common.tag.service.TplModelConfigService;
import com.ht.scada.config.scadaconfig.Activator;
import com.ht.scada.config.util.FirePropertyConstants;
import com.ht.scada.config.util.ViewPropertyChange;
import org.eclipse.swt.layout.GridLayout;

/**
 * 组态设计页面
 * @author 王蓬
 * @time 2013.12.20
 */
public class EndTagConfigDesignView extends ViewPart  implements IPropertyChangeListener {

	private int originalImageWidthObject ;	// 获得原图宽
	private int originalImageHeightObject;	// 获得原图长
	private int containerWidthObject ;		// 容器宽度
	private int containerHeightObject ;		// 容器高度
	
	
	private EndTagConfigService endTagConfigService = (EndTagConfigService) Activator
			.getDefault().getApplicationContext().getBean("endTagConfigService");			// 节点组态服务对象
	private EndTagConfig endTagConfig = new EndTagConfig();									// 节点个性组态对象
	
	private EndTagService endTagService = (EndTagService) Activator
			.getDefault().getApplicationContext().getBean("endTagService");					// 节点服务对象
	private EndTag endTag = new EndTag();													// 监控节点对象
	
	public static final String ID = "com.ht.scada.config.view.ConfigDesign"; 				//$NON-NLS-1$

	private Text text;
	private Composite composite;
	private Composite container;
	
	public EndTagConfigDesignView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		{
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setText("监控对象名：");
		}
		{
			text = new Text(container, SWT.BORDER);
			GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_text.widthHint = 121;
			text.setLayoutData(gd_text);
		}
		
		composite = new Composite(container, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		createActions();
		initializeToolBar();
		initializeMenu();
		
		ViewPropertyChange.getInstance().addPropertyChangeListener("endtagConfig",	//"endtagConfig随便起的监听的名字"
				this);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		if (event.getProperty().equals(FirePropertyConstants.CONFIG_DESIGN)) {
			endTag = (EndTag) event.getNewValue();
			
			// 初始化控件值
			text.setText(endTag.getName());
			
			// 查看该endTag对象是否有个性组态配置
			if (endTag.getImagePath() == null ) {	// 个性组态图片为空，调用共性组态设计（仅查看功能）
				System.out.println("共性组态设计中...");
				
				ImageData data = new ImageData("\\\\192.168.0.212\\软件\\csView软件开发\\组态图图片目录\\注水站组态图.jpg"); // 构建原始图片信息对象
			
				
				
				GridData gd_compositeImage1 = new GridData(SWT.FILL, SWT.FILL,false, false, 1, 1);
//				gd_compositeImage1.widthHint = data;
//				gd_compositeImage1.heightHint = containerHeight;
				composite.setLayoutData(gd_compositeImage1);
				//compositeImage.setBounds(0, 0, containerWidth, containerHeight);		// 应用网格后，坐标位置‘0’， ‘0’即没有用，仅设置宽、高即可(拖动时使用)
				System.out.println(composite.getBounds());
				System.out.println(gd_compositeImage1.widthHint);
				System.out.println(gd_compositeImage1.heightHint);
				
				
				ImageData dataFit = data.scaledTo(data.width, data.height); 	// 构造符合容器大小的图片信息对象
				Image image = new Image(container.getDisplay(), dataFit); 			// 构造图片对象
				container.setBackgroundImage(image);
				
				
			} else {								// 个性组态非空，调用个性组态设计
				System.out.println("个性组态设计进行中....");
			}
		} 
	}
	
	
	
	
	
	/**
	 * 图片加载（编辑时调用）
	 */
	public void imageFittingEditor(){
		// 设置图片显示
//		int containerWidth = screenWidth - 40; 									// 容器宽度 (40是自定义的，给边框留出的像素)
//		int containerHeight = screenHeight - gd_composite_1.heightHint- 80; 	// 容器高度（屏幕高度-上部容器高度）
//		int containerWidthOriginal = containerWidth;
//		int containerHeightOriginal = containerHeight;
//		//System.out.println("容器原始大小为(宽、高)： " + containerWidth + ", " + containerHeight);
//		
//		ImageData data = new ImageData(text.getText().trim()); // 构建原始图片信息对象
//		final int originalImageWidth = data.width; // 获得原图宽
//		final int originalImageHeight = data.height; // 获得原图长
//		//System.out.println("图片大小为(宽、高)：" + originalImageWidth + ", " + originalImageHeight );
//
//		// 根据图片比例及容器大小，调整容器
//		if (originalImageWidth > containerWidth|| originalImageHeight > containerHeight) { // 原始图大于容器
//			if (originalImageWidth > originalImageHeight ) {	// 按照图片比例(宽长)，重新绘制容器高度： 容器高度 = （容器宽度*图片高度）/ 图片宽度
//				containerHeight = ( containerWidth * originalImageHeight ) / originalImageWidth ;
//			} else {											// 按照图片比例(高长)，重新绘制容器宽度: 容器宽度 = (容器高度*图片宽度)/ 图片高度
//				containerWidth = ( containerHeight * originalImageWidth ) / originalImageHeight	;
//			}
//			//System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);
//
//		} else { // 原始图小于等于容器大小
//			containerWidth = originalImageWidth;
//			containerHeight = originalImageHeight;
//			//System.out.println("容器适应大小为(宽、高)： " + containerWidth + ", " + containerHeight);
//		}	
//		
//		// 若设置的容器大小超出原始容器大小，再次根据原始容器带下调整容器 
//		if (containerHeight > containerHeightOriginal ) {
//			containerWidth = (containerWidth * containerHeightOriginal ) / containerHeight ;
//			containerHeight = containerHeightOriginal;
//		}
//		if (containerWidth > containerWidthOriginal ) {
//			containerHeight = (containerHeight * containerWidthOriginal) / containerWidth ;
//			containerWidth  = containerWidthOriginal;
//		}
//
//		GridData gd_compositeImage1 = new GridData(SWT.FILL, SWT.FILL,false, false, 1, 1);
//		gd_compositeImage1.widthHint = containerWidth;
//		gd_compositeImage1.heightHint = containerHeight;
//		compositeImage.setLayoutData(gd_compositeImage1);
//		compositeImage.setBounds(0, 0, containerWidth, containerHeight);		// 应用网格后，坐标位置‘0’， ‘0’即没有用，仅设置宽、高即可
//		//System.out.println(containerWidth + ", "  + containerHeight);
//
//		ImageData dataFit = data.scaledTo(containerWidth, containerHeight); 	// 构造符合容器大小的图片信息对象
//		Image image = new Image(compositeImage.getDisplay(), dataFit); 			// 构造图片对象
//		compositeImage.setBackgroundImage(image);
//		
//		// 为全局变量赋值
//		originalImageWidthObject = originalImageWidth;
//		originalImageHeightObject = originalImageHeight;
//		containerWidthObject = containerWidth;
//		containerHeightObject = containerHeight;
//		
//		for (int i=0 ;i < labelList.size(); i++ ){					// 擦出已拖拽变量
//			Label tempLabel = labelList.get(i);						// 获得一个label对象（此时label的坐标是绝对坐标，要进行容器内相对转换）
//			tempLabel.setVisible(false)	;
//		}
//		labelList.clear();	// 清空标签列表
//		tagCfgTplListWithLabel.clear();
//		tagCfgTplListDelete.clear();
	}
	
	
	
	
	
}
