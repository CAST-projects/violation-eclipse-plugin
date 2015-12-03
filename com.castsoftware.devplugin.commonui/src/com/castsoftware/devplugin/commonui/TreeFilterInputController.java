package com.castsoftware.devplugin.commonui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class TreeFilterInputController implements ModifyListener{

	private static final long FILTERWAITTIME=500;
	private long itsCurrentMilli;
	private String itsLastFilter="";

	private Text itsFilterText;
	private AbstractTreeViewer itsViewer; 


	public TreeFilterInputController(Text aFilterText, AbstractTreeViewer aViewer) {
		super();
		itsFilterText = aFilterText;
		itsViewer = aViewer;
		itsFilterText.addModifyListener(this);
	}
	public void modifyText(ModifyEvent aE) {
		itsCurrentMilli=System.currentTimeMillis();
		Thread r=new Thread(){
			public void run()
			{
				exec(); exec();
			}
			private void exec() {
				boolean b=false;
				do{
					try {
						Thread.sleep(FILTERWAITTIME);
						b=true;
					} catch (InterruptedException e) {
						// nothing to do here except to relaunch the wait
//						e.printStackTrace();
					}}
				while(!b);
				Display.getDefault().syncExec(new Runnable(){

					public void run() {
						changeDiagFilter();
					}});
			}};
			r.setDaemon(true);
			r.start();
	}
	protected void changeDiagFilter() {
		if(System.currentTimeMillis() < itsCurrentMilli+FILTERWAITTIME*3/4)
			return;
		String t=itsFilterText.getText();

		if(itsLastFilter.equals(t))
			return;
		itsLastFilter=t;
		itsViewer.resetFilters();
//		ViewerFilter vf=new ViewerFilter()
//		{

//		@Override
//		public boolean select(Viewer aViewer, Object aParentElement,
//		Object aElement) {
//		ITreeContentProvider cp=(ITreeContentProvider) ((ContentViewer)aViewer).getContentProvider();
//		ILabelProvider lp=(ILabelProvider) ((ContentViewer)aViewer).getLabelProvider();


//		return cp.hasChildren(aElement) || lp.getText(aElement).toLowerCase(). contains(itsFilterText.getText().toLowerCase());
//		}};
		try{
			itsViewer.getControl().setRedraw(false);
			itsViewer.addFilter(new MyViewFilter());
			itsViewer.expandAll();
		}finally
		{
			itsViewer.getControl().setRedraw(true);			
		}
	}
	class MyViewFilter extends ViewerFilter
	{
		protected Set<Object> itsSelectedNodes=new HashSet<Object>(); 

		public MyViewFilter()
		{
			ITreeContentProvider cp=(ITreeContentProvider) itsViewer.getContentProvider();
			ILabelProvider lp=(ILabelProvider) itsViewer.getLabelProvider();
			Object[] roots=cp.getElements(itsViewer.getInput());

			if(roots != null){
				for(Object r:roots)
					evaluate(cp,lp,itsFilterText.getText().toLowerCase(),r);
			}
		}


		private boolean evaluate(ITreeContentProvider aCp, ILabelProvider aLp,String aFilter,Object aR) {
			boolean add=false;
			if(aCp.hasChildren(aR))
			{
				Object[] children=aCp.getChildren(aR);
				for(Object o:children)
					add |= evaluate(aCp,aLp,aFilter,o);
			}
			else
			{
				add=aLp.getText(aR).toLowerCase(). contains(aFilter);
			}
			if(add)
				itsSelectedNodes.add(aR);
			return add;
		}


		@Override
		public boolean select(Viewer aViewer, Object aParentElement,
				Object aElement) {
			return itsSelectedNodes.contains(aElement);
		}

	}
}
