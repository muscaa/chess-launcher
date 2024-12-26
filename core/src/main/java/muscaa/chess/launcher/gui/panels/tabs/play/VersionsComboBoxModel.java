package muscaa.chess.launcher.gui.panels.tabs.play;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import muscaa.chess.launcher.ChessLauncher;
import muscaa.chess.launcher.version.Version;
import muscaa.chess.launcher.version.VersionException;
import muscaa.chess.launcher.version.VersionStatus;

public class VersionsComboBoxModel extends AbstractListModel<Version> implements ComboBoxModel<Version> {
	
	private static final long serialVersionUID = 8280553395275331123L;
	
	private final List<Version> versions = new ArrayList<>();
	private Version selected;
	private VersionStatus status;
	
	public VersionsComboBoxModel(boolean snapshots) {
		for (Version v : ChessLauncher.INSTANCE.versions.getVersions()) {
			if (!snapshots && v.isSnapshot()) continue;
			
			versions.add(v);
		}
		
		setSelectedItem(versions.get(0));
	}
	
	@Override
	public int getSize() {
		return versions.size();
	}
	
	@Override
	public Version getElementAt(int index) {
		return versions.get(index);
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
		return selected;
	}
	
	public Version getVersion() {
		return selected;
	}
	
	public VersionStatus getStatus() {
		return status;
	}
}
