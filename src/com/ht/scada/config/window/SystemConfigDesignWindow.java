package com.ht.scada.config.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ht.scada.common.tag.entity.EndTag;
import com.ht.scada.common.tag.entity.MajorTag;
import com.ht.scada.common.tag.service.EndTagService;
import com.ht.scada.config.scadaconfig.Activator;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 相关主索引的系统组态设计页面
 * @author 王蓬
 * @time 2013.3.24
 */
public class SystemConfigDesignWindow extends ApplicationWindow {
	
	public static String [] sysNameArray = { "系统总图", "集输系统", "注水系统" };	// 系统名称数组（后期可提取成枚举变量）
	
	private MajorTag majorTag;		// 主系统索引对象
	private List<EndTag> endtags;	// 系统下的所有监控对象

	private EndTagService endTagService = (EndTagService) Activator
			.getDefault().getApplicationContext().getBean("endTagService");					// 节点服务对象
	
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
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
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
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_2.heightHint = 35;
		composite.setLayoutData(gd_composite_2);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("系统选择：");
		
		Combo comboSysname = new Combo(composite, SWT.NONE);
		GridData gd_comboSysname = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboSysname.widthHint = 60;
		comboSysname.setLayoutData(gd_comboSysname);
		comboSysname.setItems(sysNameArray);
		comboSysname.select(0);
		new Label(composite, SWT.NONE);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("监控对象：");
		
		Combo comboEndtag = new Combo(composite, SWT.NONE);
		GridData gd_comboEndtag = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboEndtag.widthHint = 112;
		comboEndtag.setLayoutData(gd_comboEndtag);

		
			
			Button btnAddEndtag = new Button(composite, SWT.NONE);
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
		btnDesignSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnDesignSave.setText("设 计 保 存");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCancel.setText("取  消");
		
		Composite composite_1 = new Composite(container, SWT.BORDER);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// 对comboEndtag进行赋值
		endtags =  endTagService.getEndTagByMajorTagId(majorTag.getId());
		String [] endTagNames = new String[endtags.size()];
		for ( int i=0; i<endtags.size(); i++ ) {
			endTagNames[i] = endtags.get(i).getName();
		}
		comboEndtag.setItems(endTagNames);
		comboEndtag.select(0);
		
		
		return container;
	}
}
