package dev.igorartsoft.remisniffingdog.chatbot.models;

import java.util.List;

/**
 * Contains meta information about the response.
 * It's possible that not all entries were retrieved. 
 * So further search is needed. This functionality likely to be changed. 
 * 
 * @author Igor Artimenko
 *
 */
public class ChatBotResponse {

	/**
	 * List of possibly unrelated ChatNodes 
	 */
	private List<ChatNode> chatNodes;
	
	// Meta information. It's an initial draft. Likely will be added
	private boolean allInformationRetrieved = true;
	
	public ChatBotResponse() {
		
	}
	
	public ChatBotResponse( List<ChatNode> chatNodes ) {
		this.chatNodes = chatNodes;
	}
	
	public List<ChatNode> getChatNodes() {
		return chatNodes;
	}

	public void setChatNodes(List<ChatNode> chatNodes) {
		this.chatNodes = chatNodes;
	}

	public boolean isAllInformationRetrieved() {
		return allInformationRetrieved;
	}

	public void setAllInformationRetrieved(boolean allInformationRetrieved) {
		this.allInformationRetrieved = allInformationRetrieved;
	}
	
}
