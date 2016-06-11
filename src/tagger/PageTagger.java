package tagger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;

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
		
		try{
			taggedText = tagger.tagString(text);
		}catch (OutOfMemoryError e){
			System.err.println("OutOfMemory Error: The sentence is too long");
			int textLen = text.split("[/. ,:]").length;
			taggedText = "There is a sentence that is too long, it has "
					+ "approximately " + textLen + " words";
		}
		
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
			//Turns off CSS, Applets and JS
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			final HtmlPage startPage = webClient.getPage(url.toString());
			
			text = startPage.getBody().asText();			
			System.out.println("==> TEXT RETRIEVED");
			
		} catch (Exception e) {
			// Creates a log file for this exception named with the date of the exception
			String date = new Date().toString().replace(":", "_");
			try (PrintWriter writer = new PrintWriter(date + "_error.txt", "UTF-8");){
				e.printStackTrace(writer);
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} finally{
			if (text == null){
				text = "An error ocurred, couldn't load the page body";
			}
		}	
		return text;
	}
	
	public static void main(String[] args){
		String[] urlArray = {
				"http://gumgum.com/",
				"http://www.popcrunch.com/jimmy-kimmel-engaged/",
				"http://gumgum-public.s3.amazonaws.com/numbers.html",
				"http://www.windingroad.com/articles/reviews/quick-drive-2012-bmw-z4-sdrive28i/"
				};
		String text;
		String taggedText;
		PageTagger pageTagger = new PageTagger();
		Charset cs = Charset.forName("utf-8");
		Path file = Paths.get("output.txt");
		
		//Turn off logging on the WebClient
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		
		try(BufferedWriter writer = Files.newBufferedWriter(file, cs);) {
			
			for (String urlText:urlArray){
				System.out.println("==>Requesting text from: ");
				writer.write("==>Requesting text from: ");			
				URL url = new URL(urlText);
				System.out.println(url.toString() + " Contains this text in body");
				text = pageTagger.getText(url);
				
				System.out.println(url.toString() + " Contains this TAGGED text in body");
				writer.newLine();
				writer.write(url.toString() + " Contains this TAGGED text in body");
				writer.newLine();
				taggedText = pageTagger.tagText(text);
				if (taggedText == null) {
					taggedText = "Text couldn't be tagged";
				}
				System.out.println(taggedText);
				writer.write(taggedText);
				writer.newLine();
				writer.newLine();
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
