package com.castsoftware.devplugin.castbrowser;

//import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class CASTBrowser extends ViewPart
{

	// private Action itsHomeAction;
	private Browser itsBrowser;
	public static final String ID = "com.castsoftware.devplugin.castbrowser.CASTBrowser"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		itsBrowser = new Browser(container, SWT.NONE);
		final FormData fd_browser = new FormData();
		fd_browser.bottom = new FormAttachment(100, -11);
		fd_browser.right = new FormAttachment(100, -9);
		fd_browser.top = new FormAttachment(0, 10);
		fd_browser.left = new FormAttachment(0, 5);
		itsBrowser.setLayoutData(fd_browser);
		//
		createActions();
		initializeToolBar();
		initializeMenu();
		// home();
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(itsBrowser, "com.castsoftware.devplugin.help.CAST_browser");

		 
	}

	// private void home()
	// {
	// String theURLBase=CASTPrefStore.getInstance().getPortalURL();
	// setUrl(theURLBase+"?frame=FRAME_PORTAL_HOME_PAGE");
	// }
	/**
	 * Create the actions
	 */
	private void createActions()
	{

		// itsHomeAction = new Action("Home") {
		// public void run() {
		// home();
		// }
		// };
		// itsHomeAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(),
		// "icons/home.png"));
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar()
	{
		// IToolBarManager toolbarManager = getViewSite().getActionBars()
		// .getToolBarManager();

		// toolbarManager.add(itsHomeAction);
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu()
	{
		// IMenuManager menuManager =
		// getViewSite().getActionBars().getMenuManager();

		// menuManager.add(itsHomeAction);
	}

	@Override
	public void setFocus()
	{
		// Set the focus
	}

	private boolean setUrl(String aUrl)
	{
		return itsBrowser.setUrl(aUrl);
	}

	public static void showViewPartWithURL(String aURL)
	{
		IViewPart thePart;
		try
		{
			thePart = Activator.getWorkbenchPage().showView(ID);
			if (thePart instanceof CASTBrowser)
			{
				CASTBrowser theBrowser = (CASTBrowser) thePart;
				theBrowser.setUrl(aURL);
			}
		} catch (PartInitException e)
		{
			MessageDialog.openError(null, "Exception", e.getMessage());
		}

	}

}
