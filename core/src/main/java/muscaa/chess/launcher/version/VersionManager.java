package muscaa.chess.launcher.version;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fluff.core.utils.StringUtils;
import fluff.http.HTTP;
import fluff.http.body.HTTPBodyParser;
import fluff.http.head.HTTPHead;
import fluff.http.head.HTTPHeader;
import fluff.json.JSONObject;
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
		
		loadInstalledVersions();
		loadAvailableVersions();
	}
	
	private void addSetup(AbstractSetup setup) {
		setups.put(setup.getID(), setup);
	}
	
	private void loadInstalledVersions() {
		
	}
	
	private void loadAvailableVersions() {
		/*JSONObject groupsObject = http.GET(API_URL)
				.setTimeout(Duration.ofSeconds(3))
				.send()
				.getBody()
				.get(HTTPBodyParser.JSON_LINKED_OBJECT);
		JSONObject artifactsObject = groupsObject.getObject("com.github." + USER);
		JSONObject versionsObject = artifactsObject.getObject(REPOSITORY);
		
		for (Map.Entry<String, String> e : versionsObject.iterate(JSONObject::getString)) {
			if (!e.getValue().equalsIgnoreCase("ok")) continue;
			
			// TODO check if snapshot version
			boolean snapshot = false;
			
			String setupID = http.GET(StringUtils.format("${}/client/${}/client-${}-setup.zip", URL, e.getKey(), e.getKey()))
					.setTimeout(Duration.ofSeconds(3))
					.setHead(HEAD)
					.send()
					.getBody()
					.get(HTTPBodyParser.STRING);
			
			versions.addFirst(new Version(e.getKey(), snapshot, false, setups.get(setupID)));
		}*/
		
		// TODO add latest version
	}
	
	public List<Version> getVersions() {
		return versions;
	}
}
