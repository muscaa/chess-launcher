package muscaa.chess.launcher.gui.panels.tabs.play;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.version.Version;
import muscaa.chess.launcher.version.VersionException;
import muscaa.chess.launcher.version.VersionStatus;

public class VersionsComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object> {
	
	private static final long serialVersionUID = 8280553395275331123L;
	
	private final List<Object> elements = new ArrayList<>();
	private Version selected;
	private VersionStatus status;
	
	public VersionsComboBoxModel(boolean snapshots) {
		reload(snapshots);
	}
	
	public void reload(boolean snapshots) {
		elements.clear();
		
		ChessLauncher.INSTANCE.versions.getInstalled()
				.thenAccept(list -> {
					if (list.isEmpty()) return;
					
					synchronized (elements) {
						elements.add("Installed:");
						for (Version v : list) {
							if (!snapshots && v.isSnapshot()) continue;
							
							elements.add(v);
						}
					}
				});
		
		ChessLauncher.INSTANCE.versions.getAvailable()
				.thenAccept(list -> {
					if (list.isEmpty()) return;
					
					LinkedList<Version> installed = ChessLauncher.INSTANCE.versions.getInstalled().join();
					
					synchronized (elements) {
						if (!installed.isEmpty()) elements.add("");
						elements.add("Available:");
						for (Version v : list) {
							if (!snapshots && v.isSnapshot()) continue;
							
							elements.add(v);
						}
					}
				});
		
		/*List<Version> installed = ChessLauncher.INSTANCE.versions.getInstalled();
		if (!installed.isEmpty()) {
			elements.add("Installed:");
			for (Version v : installed) {
				if (!snapshots && v.isSnapshot()) continue;
				
				elements.add(v);
			}
		}
		
		List<Version> available = ChessLauncher.INSTANCE.versions.getAvailable();
		if (!available.isEmpty()) {
			elements.add("");
			elements.add("Available:");
			for (Version v : available) {
				if (!snapshots && v.isSnapshot()) continue;
				
				elements.add(v);
			}
		}*/
	}
	
	@Override
	public int getSize() {
		return elements.size();
	}
	
	@Override
	public Object getElementAt(int index) {
		return elements.get(index);
	}
	
	@Override
	public void setSelectedItem(Object anItem) {
		if (!(anItem instanceof Version v)) return;
		
		try {
			status = v.getStatus();
			selected = v;
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getSelectedItem() {
		return selected == null ? "Select version..." : selected;
	}
	
	public Version getVersion() {
		return selected;
	}
	
	public VersionStatus getStatus() {
		return status;
	}
}
