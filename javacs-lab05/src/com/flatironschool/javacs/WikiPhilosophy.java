package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		/*List of visited urls beginning with starting urls*/

		List<String> urls = new ArrayList<String>();
		String current_url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		urls.add(current_url);
	
		boolean success = false;
		/*Looping until reaching Philosophy Page or failure*/

		while(!current_url.equals("")){

			//checks if url is Wiki Philosophy page
			if(current_url.equals("https://en.wikipedia.org/wiki/Philosophy")) {
				System.out.println("Success");
				
				break;
			}

			Elements paragraphs = wf.fetchWikipedia(current_url);
			Element firstPara = paragraphs.get(0);

			int parenthesisCounter = 0;

			Iterable<Node> iter = new WikiNodeIterable(firstPara);

			for (Node node: iter) {
				if(node instanceof TextNode){
					if(node.toString().contains("("))
						parenthesisCounter++;
					else if(node.toString().contains(")"))
						parenthesisCounter--;
				}
				if(!node.attr("href").equals("") && parenthesisCounter == 0){
					if(urls.contains(node.attr("abs:href"))){
						current_url="";
					}
					else {
						urls.add(node.attr("abs:href"));
						current_url = node.attr("abs:href");
						break;
					}
				}
			
			 }
			if(current_url.equals(""))
				System.out.println("Failure");
		}

		for(String s: urls){
			System.out.println(s);
		}

	}
}
