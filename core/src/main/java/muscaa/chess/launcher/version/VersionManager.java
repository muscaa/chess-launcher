package muscaa.chess.launcher.version;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fluff.core.utils.StringUtils;
import fluff.http.HTTP;
import fluff.http.HTTPException;
import fluff.http.body.HTTPBodyParser;
import fluff.http.head.HTTPHead;
import fluff.http.head.HTTPHeader;
import fluff.json.JSONObject;
import muscaa.chess.launcher.utils.FileUtils;
import muscaa.chess.launcher.version.setups.SetupV1;

public class VersionManager {
	
	public static final String USER = "muscaa";
	public static final String REPOSITORY = "chess";
	public static final String URL = "https://jitpack.io/com/github/" + USER + "/" + REPOSITORY;
	public static final String API_URL = "https://jitpack.io/api/builds/com.github." + USER + "/" + REPOSITORY;
	public static final HTTPHead HEAD = HTTPHead.builder()
			.add(HTTPHeader.USER_AGENT, "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.200 Mobile Safari/537.36")
			.build();
	
	private final HTTP http = new HTTP();
	private final Map<String, AbstractSetup> setups = new HashMap<>();
	private final LinkedList<Version> versions = new LinkedList<>();
	
	public VersionManager() {
		addSetup(new SetupV1());
		
		try {
			loadAvailableVersions();
		} catch (HTTPException e) {
			loadInstalledVersions();
		}
	}
	
	private void addSetup(AbstractSetup setup) {
		setups.put(setup.getID(), setup);
	}
	
	private void loadAvailableVersions() throws HTTPException {
		JSONObject groupsObject = http.GET(API_URL)
				.setTimeout(Duration.ofSeconds(3))
				.send()
				.getBody()
				.get(HTTPBodyParser.JSON_LINKED_OBJECT);
		JSONObject artifactsObject = groupsObject.getObject("com.github." + USER);
		JSONObject versionsObject = artifactsObject.getObject(REPOSITORY);
		
		Version latestStable = null;
		for (Map.Entry<String, String> e : versionsObject.iterate(JSONObject::getString)) {
			if (!e.getValue().equalsIgnoreCase("ok")) continue;
			
			String setupID = http.GET(StringUtils.format("${}/client/${}/setup.war", URL, e.getKey()))
					.setTimeout(Duration.ofSeconds(3))
					.setHead(HEAD)
					.send()
					.getBody()
					.get(HTTPBodyParser.STRING);
			
			Version version = new Version(e.getKey(), e.getKey(), false, setups.get(setupID));
			if (!version.isSnapshot()) latestStable = version;
			
			versions.addFirst(version);
		}
		
		if (latestStable != null) {
			versions.addFirst(new Version("latest", latestStable.getString(), true, latestStable.getSetup()));
		}
	}
	
	private void loadInstalledVersions() {
		for (File dir : FileUtils.versions.listFiles()) {
			if (!dir.isDirectory()) continue;
			try {
				versions.addFirst(new Version(dir, setups));
			} catch (VersionException e) {}
		}
	}
	
	public List<Version> getVersions() {
		return versions;
	}
}
