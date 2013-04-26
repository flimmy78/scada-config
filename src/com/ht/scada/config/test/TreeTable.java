package com.ht.scada.config.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.nebula.widgets.tablecombo.example.TableComboExampleTab;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;

public class TreeTable {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TreeTable window = new TreeTable();
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
		shell = new Shell();
		shell.setSize(524, 379);
		shell.setText("SWT Application");
		
		TableTreeViewer tableTreeViewer = new TableTreeViewer(shell, SWT.BORDER | SWT.FULL_SELECTION);
		TableTree tableTree = tableTreeViewer.getTableTree();
		tableTree.setBounds(27, 10, 181, 176);
		
		TableComboViewer tableComboViewer = new TableComboViewer(shell, SWT.NONE);
		TableCombo tableCombo = tableComboViewer.getTableCombo();
		tableCombo.setBounds(42, 228, 81, 17);
		

	}
}
