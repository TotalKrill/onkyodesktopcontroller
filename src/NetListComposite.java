

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;


public class NetListComposite extends Composite {
	protected static final boolean longList = true;
	public static List list;
	
	public NetListComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());		
		list = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fd_list = new FormData();
		fd_list.width = 2;
		fd_list.height = 2;
		fd_list.bottom = new FormAttachment(0, 300);
		fd_list.right = new FormAttachment(0, 240);
		fd_list.top = new FormAttachment(0, 10);
		fd_list.left = new FormAttachment(0, 10);
		list.setLayoutData(fd_list);
		
		Button btnListup = new Button(this, SWT.NONE);
		btnListup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("NTCLEFT");
			}
		});
		FormData fd_btnListup = new FormData();
		fd_btnListup.right = new FormAttachment(0, 98);
		fd_btnListup.top = new FormAttachment(0, 335);
		fd_btnListup.left = new FormAttachment(0, 10);
		btnListup.setLayoutData(fd_btnListup);
		btnListup.setText("listUp");
		
		Button btnListdown = new Button(this, SWT.NONE);
		btnListdown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {Eiscp.sendCommand("NTCRIGHT");
			}
		});
		FormData fd_btnListdown = new FormData();
		fd_btnListdown.right = new FormAttachment(0, 240);
		fd_btnListdown.top = new FormAttachment(0, 335);
		fd_btnListdown.left = new FormAttachment(0, 152);
		btnListdown.setLayoutData(fd_btnListdown);
		btnListdown.setText("listDown");
		
		
		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				//runs at selection
				int[] selectedItems = list.getSelectionIndices();
				String listPos = "";
				int listPosNum=0;
				for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++){
					listPos += selectedItems[loopIndex];
				listPosNum= Integer.parseInt(listPos)+1;
				}
				System.out.println("Clicked Items: " + listPos+" ("+ listPosNum+")");
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				//runs at doubleClick
				int[] selectedItems = list.getSelectionIndices();
				String listPos = "";
				int listPosNum =0;
				for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++){
					listPos = selectedItems[loopIndex]+"";
					listPosNum = Integer.parseInt(listPos);
				}
				System.out.println("Selected Items: " + listPos);
				if(longList){
					Eiscp.sendCommand("NLSI" +(listPosNum+1));
				}
				else{
					Eiscp.sendCommand("NLSL"+listPos);
				}
				mainForm.setTopComposite(0);
			}
		});
	}

	
	@Override
	public void update(){
		list.removeAll();
		String[] tempList;
		if(longList){
			tempList = Status.wholePlayList.SongTitles;			
		}
		else{
			tempList = Status.listData;			
		}
		for(int i=0; i<tempList.length; i++){
			if(tempList[i] ==null)
				tempList[i] = "Pending...";
			list.add(tempList[i]);
		}
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
