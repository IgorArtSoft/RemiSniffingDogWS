package dev.igorartsoft.remisniffingdog.chatbot.models;

import java.io.Serializable;
import java.util.List;

import javax.validation.ValidationException;

import dev.igorartsoft.remisniffingdog.utils.CompareUtils;

/**
 * @author Igor Artimenko
 * 
 *         id and name must be supplied
 *
 */
public class ChatNode implements Serializable {

	private static final long serialVersionUID = 8777907869372480064L;
	
	// Mandatory instance variables
	private String id;
	private String name;
	
	// Optional instance variables
	private String parentId;
	private List<ChatNode> children;

	public ChatNode(String id, String name) throws ValidationException {
		if( CompareUtils.isEmpty( id ) || CompareUtils.isEmpty( name ) ) {
			throw new ValidationException( "id or name must not be null or empty" );
		}
		else {
			this.id = id;
			this.name = name;			
		}
	}

	public ChatNode(String id, String name, String parentId) throws ValidationException  {		
		this( id, name);
		this.parentId = parentId;
	}
	
	public ChatNode(String id, String name, String parentId, List<ChatNode> children) throws ValidationException  {		
		this( id, name, parentId);
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChatNode> getChildren() {
		return children;
	}

	public void setChildren(List<ChatNode> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
