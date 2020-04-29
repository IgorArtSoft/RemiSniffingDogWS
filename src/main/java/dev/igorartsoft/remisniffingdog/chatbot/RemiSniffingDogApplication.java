package dev.igorartsoft.remisniffingdog.chatbot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.igorartsoft.remisniffingdog.chatbot.models.ChatBotResponse;
import dev.igorartsoft.remisniffingdog.chatbot.models.ChatNode;


interface NextLevelId{
	String nextLevelId( String parentId, String currentNodeId );
}

/**
 * @author Igor Artimenko
 *
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SpringBootApplication
@RestController
public class RemiSniffingDogApplication {

	public static final String GENERIC_NODE_LABEL = "Chat text ";

	public static final NextLevelId CHILD_ID = ( parentId, currentNodeId ) -> ( parentId + "_" + currentNodeId );
	
	public static void main(String[] args) {
		SpringApplication.run(RemiSniffingDogApplication.class, args);
	}
	
	/**
	 * Search for a list of "root" nodes to start drilling down. Partial search by substring. Current version is from top level. 
	 * If you don't know initial node name then get a list of real root nodes to start user interractions 
	 * 
	 * @param searchStr For now it's ignored
	 * @return Returns always the same for this demo
	 * 
	 * Example calls:
	 * http://localhost:8080/chatnodes
	 * http://localhost:8080/chatnodes?name=Chat text 3
	 * 
	 */
	@GetMapping("/chatnodes")
	public ChatBotResponse searchNodesByName( @RequestParam(value = "name", defaultValue = "" ) String searchStr ) {
		
		List<ChatNode> searchedChatNodes = this.initChatNodes();
		
		searchedChatNodes.stream().forEach( node -> System.out.println( node.getName() ) );
		
		// Filtering 1st level of nodes by name
		if( searchStr != null && !"".equals( searchStr ) ) {
			searchedChatNodes = searchedChatNodes.stream()
				.filter( node-> node.getName().toUpperCase().contains( searchStr.toUpperCase() ) )
				.collect( Collectors.toList() );
		}
		truncateGrandChildren( searchedChatNodes );
		
		return new ChatBotResponse( searchedChatNodes );
	}
	
	/**
	 * Search for specific nodeId. Exact search 
	 * 
	 * @param id must be supplied
	 * @return node with specified id and it's children
	 * 
	 * Example calls:
	 * http://localhost:8080/node/3
	 * 
	 */
	@GetMapping("/node/{id}")
	public ChatBotResponse searchNodeById( @PathVariable String id ) {
		
		List<ChatNode> searchedChatNodes = null;
		
		// Filtering 1st level of nodes by id
		if( id != null && !"".equals( id ) ) {
			searchedChatNodes = this.initChatNodes();
			searchedChatNodes = findFirstNodeById( searchedChatNodes, id );

			// Truncate grand children
			truncateGrandChildren( searchedChatNodes );
			
		}
				
		return new ChatBotResponse( searchedChatNodes );
	}
	
	private void truncateGrandChildren( List<ChatNode> searchedChatNodes ) {
		
		for (Iterator<ChatNode> iterator = searchedChatNodes.iterator(); iterator.hasNext();) {
			ChatNode parentNode = iterator.next();
			
			List<ChatNode> currentChildren = parentNode.getChildren();
			for (Iterator<ChatNode> iteratorChildren = currentChildren.iterator(); iteratorChildren.hasNext();) {
				ChatNode currentNode = iteratorChildren.next();
				currentNode.setChildren( null );
			}
			
		}
		
	}
	
	/**
	 * Allows to drill down to find first node is specific id
	 * @param searchedChatNodes
	 * @param id
	 * @return entire subtree below specific element including pra-pra-...-pra children
	 * 
	 */
	private List<ChatNode> findFirstNodeById( List<ChatNode> searchedChatNodes, String id ) {
		List<ChatNode> firstById = null; 
	
		// Try to find All elements among current level
		firstById = searchedChatNodes.stream().filter( node-> node.getId().equals( id ) ).collect( Collectors.toList() );
		
		// If not try to find among direct children
		if( firstById == null || firstById.isEmpty() ) {
			
			for (Iterator<ChatNode> iterator = searchedChatNodes.iterator(); iterator.hasNext();) {
				ChatNode currentNode = iterator.next();
				List<ChatNode> currentChildren = currentNode.getChildren();
				if( currentChildren!= null && !currentChildren.isEmpty() ) {
					firstById = currentChildren.stream().filter( node-> node.getId().equals( id ) ).collect( Collectors.toList() );
				}					
				// At least one found then stop search and return it
				if( firstById != null && !firstById.isEmpty() ) {
					break;
				}					

			}
			// If it was not found among direct children we have to drill down deeper
			if( firstById == null || firstById.isEmpty() ) {
				for (Iterator<ChatNode> iterator = searchedChatNodes.iterator(); iterator.hasNext();) {
					ChatNode currentNode = iterator.next();
					List<ChatNode> currentChildren = currentNode.getChildren();
					if( currentChildren!= null && !currentChildren.isEmpty() ) {
						firstById = findFirstNodeById( currentChildren, id );
					}
					if( firstById != null && !firstById.isEmpty() ) {
						break;
					}
				}
			}
		}
		
		return firstById;
	}
	
	/**
	 * Mock-up of real data.
	 * @return Entire nodes hierarchy. 
	 */
	private List<ChatNode> initChatNodes() {
		
		int noteTreeSize = 5;
		
		List<ChatNode> searchedChatNodes = new ArrayList<ChatNode>();
		
		for (int i = 0; i < noteTreeSize; i++) {
			
			String parentNodeId = String.valueOf( i );
			List<ChatNode> children = new ArrayList<ChatNode>();
			for (int j = 0; j < noteTreeSize - 1; j++) {
				
				String id = CHILD_ID.nextLevelId( parentNodeId, String.valueOf( j ) );
				ChatNode secondLevelNode = new ChatNode( id, GENERIC_NODE_LABEL + id, parentNodeId );
				secondLevelNode.setChildren( new ArrayList<ChatNode>() );
				
				// Introduce 3rd level
				for (int k = 0; k < noteTreeSize - 2; k++) {
					String currentId = CHILD_ID.nextLevelId( secondLevelNode.getId(), String.valueOf( k ) );
					ChatNode thirdLevelNode = new ChatNode( currentId, GENERIC_NODE_LABEL + currentId, secondLevelNode.getId() );
					secondLevelNode.getChildren().add( thirdLevelNode );
					
					thirdLevelNode.setChildren( new ArrayList<ChatNode>() );
					// 4th level
					for (int l = 0; l < noteTreeSize - 3; l++) {
						String currentId4 = CHILD_ID.nextLevelId( thirdLevelNode.getId(), String.valueOf( l ) );
						ChatNode fourthLevelNode = new ChatNode( currentId4, GENERIC_NODE_LABEL + currentId4, thirdLevelNode.getId() );
						thirdLevelNode.getChildren().add( fourthLevelNode );
					}
					
				}
				
				children.add( secondLevelNode );				
				
			}
			
			searchedChatNodes.add( new ChatNode( parentNodeId, GENERIC_NODE_LABEL + parentNodeId, parentNodeId, children ) );
			
		}
		
		return searchedChatNodes;
	}
	
}
