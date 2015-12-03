package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

import com.swtdesigner.ResourceManager;

public class AppModuleTreeViewerGroup extends Group {

	private CheckboxTreeViewer itsViewer;
	private Tree itsTree;
	private Text itsText;
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public AppModuleTreeViewerGroup(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		
		ToolBar toolBar;
		toolBar = new ToolBar(this, SWT.NONE);
		final FormData fd_toolBar = new FormData();
		fd_toolBar.left = new FormAttachment(100, -26);
		fd_toolBar.right = new FormAttachment(100, -2);
		toolBar.setLayoutData(fd_toolBar);

		itsText = new Text(this, SWT.BORDER);
		fd_toolBar.top = new FormAttachment(itsText, -24, SWT.BOTTOM);
		fd_toolBar.bottom = new FormAttachment(itsText, 0, SWT.BOTTOM);

		final ToolItem theRemoveCheckItem = new ToolItem(toolBar, SWT.PUSH);
		theRemoveCheckItem.setToolTipText("Uncheck all items");
		theRemoveCheckItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				itsViewer.setCheckedElements(new Object[0]);
				itsViewer.setGrayedElements(new Object[0]);
				Composite compo = getParent().getParent();
				Control[] childrens = compo.getChildren();
				for(Control children : childrens){
					if(children instanceof Composite){
						Control[] buttons = ((Composite)children).getChildren();
						for(Control button : buttons){
							if(button instanceof Button){
								Integer idButton = (Integer)((Button) button).getData();
								if(idButton != null && idButton.intValue() == IDialogConstants.OK_ID){
									button.setEnabled(false);
								}
							}
						}
					}
				}
			}
		});
		theRemoveCheckItem.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/deselectall.png"));
		final FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(100, -71);
		fd_text.bottom = new FormAttachment(0, 28);
		fd_text.top = new FormAttachment(0, 9);
		itsText.setLayoutData(fd_text);

		Label theFilterLabel;
		theFilterLabel = new Label(this, SWT.NONE);
		fd_text.left = new FormAttachment(theFilterLabel, 0, SWT.RIGHT);
		final FormData fd_theFilterLabel = new FormData();
		fd_theFilterLabel.top = new FormAttachment(0, 11);
		fd_theFilterLabel.bottom = new FormAttachment(itsText, 8, SWT.BOTTOM);
		fd_theFilterLabel.left = new FormAttachment(0, 4);
		fd_theFilterLabel.right = new FormAttachment(0, 35);
		theFilterLabel.setLayoutData(fd_theFilterLabel);
		theFilterLabel.setText("Filter:");

		itsViewer = new CheckboxTreeViewer(this, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		itsViewer.addCheckStateListener(new TreeViewerLeafSelectionManager() );
		itsTree = itsViewer.getTree();
		final FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, -5);
		fd_tree.right = new FormAttachment(100, -5);
		fd_tree.top = new FormAttachment(theFilterLabel, 5, SWT.BOTTOM);
		fd_tree.left = new FormAttachment(theFilterLabel, 0, SWT.LEFT);
		itsTree.setLayoutData(fd_tree);
		//
		
		@SuppressWarnings("unused")
		final TreeFilterInputController theCtrl=new TreeFilterInputController(itsText,itsViewer);
		itsViewer.setSorter(new ViewerSorter());
		itsViewer.setUseHashlookup(true);

	}

	public void setContentProvider(IContentProvider aProvider) {
		itsViewer.setContentProvider(aProvider);
	}

	public void setLabelProvider(IBaseLabelProvider aLabelProvider) {
		itsViewer.setLabelProvider(aLabelProvider);
	}


	public final void setInput(Object aInput) {
		itsViewer.setInput(aInput);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Object[] getCheckedElements() {
		return itsViewer.getCheckedElements();
	}

	public Object[] getGrayedElements() {
		return itsViewer.getGrayedElements();
	}

	public void setCheckedElements(Object[] aElements) {
		itsViewer.setCheckedElements(aElements);
	}

	public void setGrayedElements(Object[] aElements) {
		itsViewer.setGrayedElements(aElements);
	}

	public void addCheckStateListener(ICheckStateListener aListener) {
		itsViewer.addCheckStateListener(aListener);
	}
}
