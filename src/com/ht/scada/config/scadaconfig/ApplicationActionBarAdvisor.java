package com.ht.scada.config.scadaconfig;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.ht.scada.config.action.OpenViewAction;
import com.ht.scada.config.action.PerspectiveChangeAction;
import com.ht.scada.config.perspective.RTURemotePerspective;
import com.ht.scada.config.perspective.ScadaDevicePerspective;
import com.ht.scada.config.perspective.ScadaObjectPerspective;
import com.ht.scada.config.perspective.VariableConfigPerspective;
import com.ht.scada.config.view.AreaTreeView;
import com.ht.scada.config.view.EnergyTreeView;
import com.ht.scada.config.view.ScadaObjectTreeView;
import com.ht.scada.config.view.VariableGroupConfigView;
import com.ht.scada.config.view.VariableTemplateConfigView;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
//    private IWorkbenchAction exitAction;
//    private IWorkbenchAction aboutAction;
//    private IWorkbenchAction newWindowAction;
//    private OpenViewAction openViewAction;
//    private Action messagePopupAction;
//    private IContributionItem perspectivesMenu;
    
    private Action scadaObjectChangeAction;
    private Action scadaDeviceChangeAction;
    private Action rtuRemoteChangeAction;
    
    private Action areaViewShowAction;
    private Action energyViewShowAction;
    private Action varGroupShowAction;
    private Action varTemplateShowAction;
    
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

//        exitAction = ActionFactory.QUIT.create(window);
//        register(exitAction);
//        
//        aboutAction = ActionFactory.ABOUT.create(window);
//        register(aboutAction);
//        
//        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
//        register(newWindowAction);
        
//        openViewAction = new OpenViewAction(window, "Open Another Message View", MainIndexView.ID);
//        register(openViewAction);
        
        
//        perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        
        scadaObjectChangeAction = new OpenViewAction(window, ScadaObjectPerspective.ID, ScadaObjectTreeView.ID, "监控对象配置");
        register(scadaObjectChangeAction);
        
        areaViewShowAction = new OpenViewAction(window, ScadaObjectPerspective.ID, AreaTreeView.ID, "常规分类配置");
        register(areaViewShowAction);
        
        energyViewShowAction = new OpenViewAction(window, ScadaObjectPerspective.ID, EnergyTreeView.ID, "能耗分类分项");
        register(energyViewShowAction);
        
        varGroupShowAction = new OpenViewAction(window, VariableConfigPerspective.ID, VariableGroupConfigView.ID, "变量分组配置");
        register(varGroupShowAction);
        
        varTemplateShowAction = new OpenViewAction(window, VariableConfigPerspective.ID, VariableTemplateConfigView.ID, "变量模板配置");
        register(varTemplateShowAction);
        
        scadaDeviceChangeAction = new PerspectiveChangeAction("监控设备配置",ScadaDevicePerspective.ID, window);
        register(scadaDeviceChangeAction);
        
        rtuRemoteChangeAction = new PerspectiveChangeAction("RTU远程配置",RTURemotePerspective.ID, window);
        register(rtuRemoteChangeAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        MenuManager variableConfigMenu = new MenuManager("变量配置");
        MenuManager scadaObjectMenu = new MenuManager("监控对象配置");
        MenuManager scadaDeviceMenu = new MenuManager("监控设备配置");
        MenuManager rtuRemoteMenu = new MenuManager("RTU远程配置");
        
//        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//        menuBar.add(helpMenu);
        
        menuBar.add(variableConfigMenu);
        menuBar.add(scadaObjectMenu);
        menuBar.add(scadaDeviceMenu);
        menuBar.add(rtuRemoteMenu);
        
        // File
//        fileMenu.add(newWindowAction);
//        fileMenu.add(new Separator());
//        fileMenu.add(messagePopupAction);
//        fileMenu.add(openViewAction);
//        fileMenu.add(new Separator());
//        fileMenu.add(exitAction);
        
        // Help
//        helpMenu.add(aboutAction);
        
        //变量配置
        variableConfigMenu.add(varGroupShowAction);
        variableConfigMenu.add(varTemplateShowAction);
        
        //监控对象配置
        scadaObjectMenu.add(scadaObjectChangeAction);
        scadaObjectMenu.add(areaViewShowAction);
        scadaObjectMenu.add(energyViewShowAction);
        
        //监控设备配置
        scadaDeviceMenu.add(scadaDeviceChangeAction);
        
        //远程RTU配置
        rtuRemoteMenu.add(rtuRemoteChangeAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
//        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
//        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
//        toolbar.add(openViewAction);
//        toolbar.add(messagePopupAction);
    }
}
