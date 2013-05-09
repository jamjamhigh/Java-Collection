package search;

public class Crawler {public Crawler(String dbname) {
	init();

}

private void init() {

}

public void addtoindex(String url, NodeList soup) {
	System.out.println("Indexing " + url);
}

public String gettextonly(Node soup) {
	return null;
}

public String gettextonly(NodeList children) {
	return null;
}

public Boolean isindexed(String url) {
}

public void addlinkref(String urlFrom, String urlTo, String linkText) {
}

public void crawl(Set<String> pages, int depth) {

}

public void createindextables() {
}
}



