package tagger;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PageTagger {

	private MaxentTagger tagger;
	
	public PageTagger(){
		tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	}
	
	public static void main(String[] args){
		PageTagger pageTagger = new PageTagger();
	}
}
