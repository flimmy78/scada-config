package com.ht.scada.config.window;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ht.scada.config.util.LayoutUtil;
import org.eclipse.swt.layout.GridData;

public class DatabaseInitWindow extends ApplicationWindow {
	

	public DatabaseInitWindow(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	private Text username;
	private Text password;
	private Text ip;
	private Text database;
	/**
	 * Create contents of the window.
	 */
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_shell = new GridLayout(2, false);
		gl_shell.marginTop = 20;
		gl_shell.marginLeft = 50;
		container.setLayout(gl_shell);
		
		LayoutUtil.centerShell(Display.getCurrent(), container.getShell());
		
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setBounds(21, 36, 78, 17);
		label.setText("数据库类型：");
		
		Combo combo = new Combo(container, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 85;
		combo.setLayoutData(gd_combo);
		combo.setBounds(105, 33, 153, 25);
		combo.setItems(new String[]{"MySQL","SQL Server","MariaDB","XML"});
		combo.select(0);
		
		Label ip1 = new Label(container, SWT.NONE);
		ip1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		ip1.setBounds(21, 72, 78, 17);
		ip1.setText("服务器地址：");
		
		ip = new Text(container, SWT.BORDER);
		GridData gd_ip = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ip.widthHint = 90;
		ip.setLayoutData(gd_ip);
		ip.setText("127.0.0.1");
		ip.setBounds(105, 69, 153, 23);
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("数据库名：");
		label_4.setBounds(21, 112, 61, 17);
		
		database = new Text(container, SWT.BORDER);
		GridData gd_database = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_database.widthHint = 90;
		database.setLayoutData(gd_database);
		database.setText("scada");
		database.setBounds(105, 109, 153, 23);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setBounds(21, 154, 61, 17);
		label_1.setText("用户名：");
		
		username = new Text(container, SWT.BORDER);
		GridData gd_username = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_username.widthHint = 90;
		username.setLayoutData(gd_username);
		username.setText("root");
		username.setBounds(105, 151, 153, 23);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setBounds(21, 191, 61, 17);
		label_2.setText("密码：");
		
		password = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridData gd_password = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_password.widthHint = 90;
		password.setLayoutData(gd_password);
		password.setText("dltx_212");
		password.setBounds(105, 188, 153, 23);
		new Label(container, SWT.NONE);
		
		Button button = new Button(container, SWT.NONE);
		GridData gd_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button.widthHint = 80;
		button.setLayoutData(gd_button);
		button.addSelectionListener(new SelectionAdapter() {
			
		});
		button.setBounds(178, 228, 80, 27);
		button.setText("确定");
		
		return container;
	}
	@Override
	protected void configureShell(Shell shell) {
		shell.setText("数据库初始化（暂未实现，手工修改）");
		shell.setSize(330, 280);
		super.configureShell(shell);
	}
	
	
}
