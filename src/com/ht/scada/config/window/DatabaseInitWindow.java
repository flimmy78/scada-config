package com.ht.scada.config.window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

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
import org.eclipse.swt.events.SelectionEvent;

/**
 * 数据库配置
 * @author 王蓬
 * time 2013.6.19
 *
 */
public class DatabaseInitWindow extends ApplicationWindow {
	

	public DatabaseInitWindow(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	private static String fileName="config/application.properties";		//设置数据库的文件
	
	private Text textUsername;
	private Text textPassword;
	private Text textIp;
	private Text textDatabaseName;
	private String [] comboDatabaseTypeArray={"MySQL", "SQL Server", "Oracle", "h2"};		//数据库名称集合
	
	private Combo comboDatabaseType ;
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
		
		comboDatabaseType = new Combo(container, SWT.NONE);
		GridData gd_comboDatabaseType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboDatabaseType.widthHint = 85;
		comboDatabaseType.setLayoutData(gd_comboDatabaseType);
		comboDatabaseType.setBounds(105, 33, 153, 25);
		comboDatabaseType.setItems(comboDatabaseTypeArray);
		comboDatabaseType.select(0);
		
		Label ip1 = new Label(container, SWT.NONE);
		ip1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		ip1.setBounds(21, 72, 78, 17);
		ip1.setText("服务器地址：");
		
		textIp = new Text(container, SWT.BORDER);
		GridData gd_textIp = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textIp.widthHint = 90;
		textIp.setLayoutData(gd_textIp);
		textIp.setText("127.0.0.1");
		textIp.setBounds(105, 69, 153, 23);
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("数据库名：");
		label_4.setBounds(21, 112, 61, 17);
		
		textDatabaseName = new Text(container, SWT.BORDER);
		GridData gd_textDatabaseName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textDatabaseName.widthHint = 90;
		textDatabaseName.setLayoutData(gd_textDatabaseName);
		textDatabaseName.setText("scada");
		textDatabaseName.setBounds(105, 109, 153, 23);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setBounds(21, 154, 61, 17);
		label_1.setText("用户名：");
		
		textUsername = new Text(container, SWT.BORDER);
		GridData gd_textUsername = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textUsername.widthHint = 90;
		textUsername.setLayoutData(gd_textUsername);
		textUsername.setText("root");
		textUsername.setBounds(105, 151, 153, 23);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setBounds(21, 191, 61, 17);
		label_2.setText("密码：");
		
		textPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridData gd_textPassword = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textPassword.widthHint = 90;
		textPassword.setLayoutData(gd_textPassword);
		textPassword.setText("dltx_212");
		textPassword.setBounds(105, 188, 153, 23);
		new Label(container, SWT.NONE);
		
		controlerInit(fileName);
		
		Button button = new Button(container, SWT.NONE);
		GridData gd_button = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_button.widthHint = 80;
		button.setLayoutData(gd_button);
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String databaseType=comboDatabaseType.getText().trim().toString();	//获得数据库类型
				String Ip=textIp.getText().trim().toString();						//获得Ip
				String databaseName=textDatabaseName.getText().trim().toString();	//获得操作的数据库
				String userName=textUsername.getText().trim().toString();			//获得用户名
				String password=textPassword.getText().trim().toString();			//获得用户密码

				//文件修改，第一遍进行注释修改，第二遍进行变量修改
				tagEdit(fileName,databaseType);													//第一遍修改注释标签
				variableEdit(fileName, databaseType, userName, password, Ip, databaseName);		//第二遍遍历，修改变量
				   
			    JOptionPane.showOptionDialog(null, "已经成功保存，请进行其它操作", "信息提示",     
			    JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION,     
			     null, null, null);   

			}
		});
		button.setBounds(178, 228, 80, 27);
		button.setText("确定");
		
		return container;
	}
	@Override
	protected void configureShell(Shell shell) {
		shell.setText("数据库初始化");
		shell.setSize(330, 280);
		super.configureShell(shell);
	}
	
	/**
	 * 第一遍遍历数据库配置文件，修改其注释标签
	 * @param fileName
	 */
	private static void tagEdit(String fileName, String databaseType){
		
		File file = new File(fileName);			
		BufferedReader reader = null;

		try {
			//System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			StringBuffer temp=new StringBuffer("");
			int line = 1;
			//一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null){
				
				if(tempString.contains(databaseType + " ")){	//该行开始是需要设置的数据库
					temp=temp.append(tempString+"\r\n");
					line++;
					int nextLineNumber=5;						//接下去的几行为数据库配置相关设置
					//if(databaseType.equals("MySQL")){
					//	nextLineNumber=6;
					//}
					for(int i=0;i<nextLineNumber;i++){
						tempString = reader.readLine();			//获得要重新设置的行字符串
						if(tempString.contains("#")){			//如果已经包含“#”，则需要删除
							tempString=tempString.substring(1,tempString.length());
							temp=temp.append(tempString+"\r\n");
							line++;
						}else{									//如果不包含“#”，说明已经是选择数据库，无需更改
							temp=temp.append(tempString+"\r\n");
							line++;
						}
						
					}
					
				}else{											//该行不是需要设置的数据库
					
					if(tempString.length()!=0){					//该行不为空
						//System.out.println(line+"行！");
						if(tempString.charAt(0)!='#' && !tempString.contains("dbcp.maxIdle") && !tempString.contains("dbcp.maxActive")){
							tempString="#"+tempString;
						}
					}
					line++;	
					temp=temp.append(tempString+"\r\n");
				}	
			}
			reader.close();
					
			//写文件，追加方式,打开一个写文件器
			FileWriter writer = new FileWriter(fileName);
			writer.write(temp.toString());
			writer.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		
	}
	
	/**
	 * 第二遍遍历，修改其中的变量信息
	 * @param fileName
	 * @param databaseType
	 */
	private static void variableEdit(String fileName, String databaseType, String userName, String password, String Ip, String databaseName){
		
		File file = new File(fileName);			
		BufferedReader reader = null;
		
		try {
			//System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			StringBuffer temp=new StringBuffer("");
			int line = 1;
			//一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null){
				//显示行号
				//System.out.println("line " + line + ": " + tempString);
				
				if(tempString.contains("jdbc.driver") && !tempString.contains("#")){
					//doNothing
				}else if(tempString.contains("jdbc.url") && !tempString.contains("#")){
					//System.out.println(line + " 行！");
					
					//#Oracle database settings   #jdbc.url=jdbc:oracle:thin:@127.0.0.1:1521:XE
					if(databaseType.equals("Oracle")){
						tempString = "jdbc.url=jdbc:oracle:thin:@" + Ip + ":1521:" + databaseName;
					}
					//#MySQL database settings   #jdbc.url=jdbc:mysql://192.168.0.212/scada?useUnicode=true&characterEncoding=utf-8
					if(databaseType.equals("MySQL")){
						tempString = "jdbc.url=jdbc:mysql://" + Ip + "/" + databaseName +"?useUnicode=true&characterEncoding=utf-8";
					}
					//#SQL Server database settings   #jdbc.url=jdbc:jtds:sqlserver://192.168.0.212/test
					if(databaseType.equals("SQL Server")){
						tempString = "jdbc.url=jdbc:jtds:sqlserver://" + Ip + "/" + databaseName;
					}
					//#h2 database settings   #jdbc.url=jdbc:h2:file:~/192.168.1.1/quickstart;AUTO_SERVER=TRUE
					if(databaseType.equals("h2")){
						tempString = "jdbc.url=jdbc:h2:file:~/" + Ip + "/" + databaseName + ";AUTO_SERVER=TRUE";
					}
				
				}else if(tempString.contains("jdbc.username") && !tempString.contains("#")){
					//System.out.println(line + " 行！");
					tempString = "jdbc.username=" + userName;
					
				}else if(tempString.contains("jdbc.password") && !tempString.contains("#")){
					//System.out.println(line + " 行！");
					tempString = "jdbc.password=" + password;
					
				}else if(tempString.contains("database.dialect") && !tempString.contains("#")){
					//doNothing
				}
				
				temp=temp.append(tempString+"\r\n");
				line++;
			}
			reader.close();
					
			//写文件，追加方式,打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName);
			writer.write(temp.toString());
			writer.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		
	}
	
	/**
	 * 读取数据库配置文件 ，进行页面初始化
	 * @param fileName
	 */
	private  void controlerInit(String fileName){
		File file = new File(fileName);			
		BufferedReader reader = null;
		
		try {
			//System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String beforeTemp=null;		//当出现非注释行前的一行
			StringBuffer temp=new StringBuffer("");
			//一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null){
				if(!tempString.contains("#") && tempString.length()>0		//获得该数据库的相关5行信息
						&& !tempString.contains("dbcp.maxIdle")
						&& !tempString.contains("dbcp.maxActive")){
					String [] infors=new String[5];
					infors[0]=tempString;
					infors[1]=reader.readLine();
					infors[2]=reader.readLine();
					infors[3]=reader.readLine();
					infors[4]=reader.readLine();
					
					String type=null;
					if(infors[4].split("=")[1].equals("MYSQL")){
						//#jdbc.url=jdbc:mysql://192.168.0.212/scada?useUnicode=true&characterEncoding=utf-8
						type="MySQL";
						
						textIp.setText(infors[1].split("/")[2]);
						String nameTemp=infors[1].split("/")[3];
						textDatabaseName.setText(nameTemp.split("\\?")[0]);
						textUsername.setText(infors[2].split("=")[1]);
						textPassword.setText(infors[3].split("=")[1]);
						
					}else if(infors[4].split("=")[1].equals("SQL_SERVER")){
						//#jdbc.url=jdbc:jtds:sqlserver://192.168.0.212/test
						type="SQL Server";
						
						textIp.setText(infors[1].split("/")[2]);
						textDatabaseName.setText(infors[1].split("/")[3]);
						textUsername.setText(infors[2].split("=")[1]);
						textPassword.setText(infors[3].split("=")[1]);
						
					}else if(infors[4].split("=")[1].equals("ORACLE")){
						//jdbc.url=jdbc:oracle:thin:@127.0.0.1:1521:XE
						type="Oracle";
						
						String ipTemp=infors[1].split("@")[1];
						textIp.setText(ipTemp.split(":")[0]);
						textDatabaseName.setText(ipTemp.split(":")[2]);
						textUsername.setText(infors[2].split("=")[1]);
						textPassword.setText(infors[3].split("=")[1]);
						
					}else if(infors[4].split("=")[1].equals("H2")){
						//#jdbc.url=jdbc:h2:file:~/192.168.1.1/quickstart;AUTO_SERVER=TRUE
						type="h2";
						
						textIp.setText(infors[1].split("/")[1]);
						String nameTemp=infors[1].split("/")[2];
						textDatabaseName.setText(nameTemp.split(";")[0]);
						textUsername.setText(infors[2].split("=")[1]);
						textPassword.setText(infors[3].split("=")[1]);
						
					}
					
					// 初始化数据库类型
					for (int i = 0; i < comboDatabaseTypeArray.length; i++) {
						if (type.equals(comboDatabaseTypeArray[i])) {
							comboDatabaseType.select(i);
							break;
						}
					}
					
					
					
					
					System.out.println(tempString);
				}
				
			}
			reader.close();
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
}
