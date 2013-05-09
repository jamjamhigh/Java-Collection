import java.net.MalformedURLException;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



public class Crawler {

     static Set<String> ignorewords = new HashSet<String>();
     static {
        ignorewords.add("the");
        ignorewords.add("of");
        ignorewords.add("to");
        ignorewords.add("and");
        ignorewords.add("a");
        ignorewords.add("in");
        ignorewords.add("is");
        ignorewords.add("it");
     }


     public void crawl(Set<String> pages, int depth) {
        for (int i = 0; i < depth; i++) {
            Set<String> newpages = new HashSet<String>();
            for (String page : pages) {
                NodeList soup;
                try {
                    Parser parser = new Parser();
                    parser.setResource(page);
                    soup = parser.parse(null);
                    addtoindex(page, soup);
                    } catch (ParserException e) {
                         System.out.println("Could not open " + page);
                            e.printStackTrace();
                            continue;
                    }
                NodeList links = soup.extractAllNodesThatMatch(
                        new TagNameFilter("a"), true);
                for (int j = 0; j < links.size(); j++) {
                    TagNode link = (TagNode) links.elementAt(j);
                    if (link.getAttribute("href") != null) {
                        String url;
                        try {
                            url = new URL(new URL(page), link
                                    .getAttribute("href")).toString();
                            } catch (MalformedURLException e) {
                            continue;
                            
                            if (url.indexOf("'") != -1) {
                                continue;
                            }
                            }
                        url = url.split("#")[0];
                        if (url.startsWith("http") && !isindexed(url)) {
                             newpages.add(url);
                             }

                        String linktext = gettextonly(link);
                        addlinkref(page, url, linktext);
                        }
                    }

                dbcommit();
                }
            pages = newpages;

		}

	}


	private void addtoindex(String page, NodeList soup) {
		// TODO 自動生成されたメソッド・スタブ
		
	}


	private boolean isindexed(String url) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


	private void addlinkref(String page, String url, String linktext) {
		// TODO 自動生成されたメソッド・スタブ
		
	}


	private String gettextonly(TagNode link) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}



}