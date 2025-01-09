package muscaa.chess.launcher.version;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import fluff.core.utils.StringUtils;
import fluff.http.HTTP;
import fluff.http.HTTPException;
import fluff.http.body.HTTPBodyParser;
import fluff.http.head.HTTPHead;
import fluff.http.head.HTTPHeader;
import fluff.http.response.HTTPResponse;
import fluff.http.response.HTTPResponseStatus;
import fluff.json.JSONObject;
import muscaa.chess.launcher.utils.FileUtils;
import muscaa.chess.launcher.version.setups.SetupV1;

public class VersionManager {
	
	public static final String USER = "muscaa";
	public static final String REPOSITORY = "chess";
	public static final String URL = "https://jitpack.io/com/github/" + USER + "/" + REPOSITORY;
	public static final String API_URL = "https://jitpack.io/api/builds/com.github." + USER + "/" + REPOSITORY;
	public static final String NEWS_URL = "https://raw.githubusercontent.com/" + USER + "/" + REPOSITORY + "/refs/heads/main/NEWS.md";
	public static final HTTPHead HEAD = HTTPHead.builder()
			.add(HTTPHeader.USER_AGENT, "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.200 Mobile Safari/537.36")
			.build();
	
	private final HTTP http = new HTTP();
	private final Map<String, AbstractSetup> setups = new HashMap<>();
	
	private CompletableFuture<LinkedList<Version>> available;
	private CompletableFuture<LinkedList<Version>> installed;
	private CompletableFuture<Version> latestStable;
	private CompletableFuture<String> news;
	
	public VersionManager() {
		addSetup(new SetupV1());
		
		fetchAvailableVersions();
		fetchInstalledVersions();
		fetchNews();
	}
	
	private void addSetup(AbstractSetup setup) {
		setups.put(setup.getID(), setup);
	}
	
	private void fetchAvailableVersions() {
		latestStable = new CompletableFuture<>();
		available = CompletableFuture.supplyAsync(() -> {
			LinkedList<Version> list = new LinkedList<>();
			
			try {
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
					
					list.addFirst(version);
				}
				
				if (latestStable != null) {
					list.addFirst(new Version("latest", latestStable.getString(), true, latestStable.getSetup()));
				}
				
				this.latestStable.complete(latestStable);
			} catch (HTTPException e) {}
			
			return list;
		});
	}
	
	private void fetchInstalledVersions() {
		installed = CompletableFuture.supplyAsync(() -> {
			LinkedList<Version> list = new LinkedList<>();
			
			for (File dir : FileUtils.versions.listFiles()) {
				if (!dir.isDirectory()) continue;
				try {
					list.add(new Version(dir, setups));
				} catch (VersionException e) {}
			}
			
			return list;
		});
	}
	
	private void fetchNews() {
		news = CompletableFuture.supplyAsync(() -> {
			try {
		        HTTPResponse r = http.GET(NEWS_URL)
		                .setTimeout(Duration.ofSeconds(3))
		                .send();
		        String body = r.getBody().get(HTTPBodyParser.STRING);
		        if (r.getStatus() != HTTPResponseStatus.OK) throw new HTTPException(body);
		        
		        return body;
			} catch (HTTPException e) {
				return "Could not fetch news. " + e.getMessage();
			}
		});
	}
	
	public CompletableFuture<LinkedList<Version>> getAvailable() {
		return available;
	}
	
	public CompletableFuture<LinkedList<Version>> getInstalled() {
		return installed;
	}
	
	public CompletableFuture<Version> getLatestStable() {
		return latestStable;
	}
	
	public CompletableFuture<String> getNews() {
		return news;
	}
}
