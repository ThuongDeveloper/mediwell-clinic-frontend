/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.util;



import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarkdownProcessor {
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;

    @Autowired
    public MarkdownProcessor(Parser markdownParser, HtmlRenderer htmlRenderer) {
        this.markdownParser = markdownParser;
        this.htmlRenderer = htmlRenderer;
    }

    public String convertToHtml(String markdown) {
        Node document = markdownParser.parse(markdown);
        return htmlRenderer.render(document);
    }
}