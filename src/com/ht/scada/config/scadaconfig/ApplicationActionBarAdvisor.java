package com.ht.scada.config.scadaconfig;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.ht.scada.config.action.OpenViewAction;
import com.ht.scada.config.action.OpenWindowAction;
import com.ht.scada.config.action.PerspectiveChangeAction;
import com.ht.scada.config.perspective.RTURemotePerspective;
import com.ht.scada.config.perspective.ScadaDevicePerspective;
import com.ht.scada.config.perspective.ScadaObjectPerspective;
import com.ht.scada.config.perspective.VariableConfigPerspective;
import com.ht.scada.config.util.ImagePath;
import com.ht.scada.config.view.AreaTreeView;
import com.ht.scada.config.view.EnergyTreeView;
import com.ht.scada.config.view.ScadaObjectTreeView;
import com.ht.scada.config.view.VariableGroupConfigView;
import com.ht.scada.config.view.VariableTemplateConfigView;
import com.ht.scada.config.window.DatabaseInitWindow;
import com.ht.scada.config.window.ProjectInitWindow;

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
    
    private Action projectInitAction;	//工程初始化
    private Action dataBaseInitAction;	//数据库初始化
    
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        
        scadaObjectChangeAction = new OpenViewAction(window, ScadaObjectPerspective.ID, ScadaObjectTreeView.ID, "监控对象配置");
        scadaObjectChangeAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.MAJOR_TAG_INDEX_IMAGE));
        register(scadaObjectChangeAction);
        
        areaViewShowAction = new OpenViewAction(window, ScadaObjectPerspective.ID, AreaTreeView.ID, "常规分类配置");
        areaViewShowAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.AREA_INDEX_IMAGE));
        register(areaViewShowAction);
        
        energyViewShowAction = new OpenViewAction(window, ScadaObjectPerspective.ID, EnergyTreeView.ID, "能耗分类分项");
        energyViewShowAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.ENERGY_INDEX_IMAGE));
        register(energyViewShowAction);
        
        varGroupShowAction = new OpenViewAction(window, VariableConfigPerspective.ID, VariableGroupConfigView.ID, "变量分组配置");
        varGroupShowAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.VARIABLE_GROUP_IMAGE));
        register(varGroupShowAction);
        
        varTemplateShowAction = new OpenViewAction(window, VariableConfigPerspective.ID, VariableTemplateConfigView.ID, "变量模板配置");
        varTemplateShowAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.VARIABLE_TEMPLATE_IMAGE));
        register(varTemplateShowAction);
        
        scadaDeviceChangeAction = new PerspectiveChangeAction("监控设备配置",ScadaDevicePerspective.ID, window);
        scadaDeviceChangeAction.setImageDescriptor(Activator.getDefault().getImageDescriptor(ImagePath.ACQUISITION_CHANNEL_INDEX_IMAGE));
        register(scadaDeviceChangeAction);
        
        rtuRemoteChangeAction = new PerspectiveChangeAction("RTU远程配置",RTURemotePerspective.ID, window);
        register(rtuRemoteChangeAction);
        
        ApplicationWindow projectInitWindow = new ProjectInitWindow(null);
        projectInitAction = new OpenWindowAction(window, "工程初始化", projectInitWindow);
        register(projectInitAction);
        
        ApplicationWindow databaseInitWindow = new DatabaseInitWindow(null);
        dataBaseInitAction = new OpenWindowAction(window, "数据源初始化", databaseInitWindow);
        register(dataBaseInitAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        MenuManager projectMenu = new MenuManager("工程");
        MenuManager variableConfigMenu = new MenuManager("变量配置");
        MenuManager scadaObjectMenu = new MenuManager("监控对象配置");
        MenuManager scadaDeviceMenu = new MenuManager("监控设备配置");
        MenuManager rtuRemoteMenu = new MenuManager("RTU远程配置");
        
//        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//        menuBar.add(helpMenu);
        
        menuBar.add(projectMenu);
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
        
        //工程
        projectMenu.add(dataBaseInitAction);
        projectMenu.add(projectInitAction);
        
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
