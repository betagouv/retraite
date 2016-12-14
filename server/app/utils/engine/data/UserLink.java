package utils.engine.data;

import utils.engine.data.enums.LinkType;

public class UserLink {

	private String label;
	
	private String url;
	
	private LinkType type;

	public UserLink(String label, String url, String type) {
		super();
		this.label = label;
		this.url = url;
		if (type != null) {
			this.type = LinkType.valueOf(type);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LinkType getType() {
		return type;
	}

	public void setType(LinkType type) {
		this.type = type;
	}	
}
