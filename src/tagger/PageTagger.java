package tagger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PageTagger {

	private MaxentTagger tagger;
	
	public PageTagger(){
		tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	}
	
	/**
	 * Method that takes a String [text] as input and returns the tagged text as a String
	 * @param text
	 * @return taggedText
	 */
	public String tagText(String text){
		String taggedText = null;
		//TO-DO Use thetagString method of MaxentTagger
		
		return taggedText;
	}
	
	/**
	 * Method that takes a java.net.URL [url] as input and returns a String containing
	 * all the text in the boby of a web page, excluding HTLM tags and JavaScript.
	 * @param url
	 * @return text
	 */
	public String getText(java.net.URL url){
		String text = null;
		
		try(final WebClient webClient = new WebClient();) {
			System.out.println(url.toString() + " Contains this text in body");
			
			final HtmlPage startPage = webClient.getPage(url.toString());
			text = startPage.getBody().asText();
			
			System.out.println(text);
			
			
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (text == null){
				text = "An error ocurred, couldn't load the page body";
			}
		}
		
		return text;
	}
	
	public static void main(String[] args){
		PageTagger pageTagger = new PageTagger();
		//Turn off loging just to optimize.
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		
		System.out.println("-->Requesting text from: ");
		try {
			pageTagger.getText(new URL("http://gumgum.com/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//pageTagger.getText(new URL("http://www.popcrunch.com/jimmy-kimmel-engaged/"));
		
	}
}
