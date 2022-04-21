package fr.legrain.bugzilla.rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"ids",
	"comment",
	"is_private"
})
public class CommentCreate {

	@JsonProperty("ids")
	private List<Integer> ids = new ArrayList<Integer>();
	@JsonProperty("comment")
	private String comment;
	@JsonProperty("is_private")
	private Boolean isPrivate;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The ids
	 */
	@JsonProperty("ids")
	public List<Integer> getIds() {
		return ids;
	}

	/**
	 *
	 * @param ids
	 * The ids
	 */
	@JsonProperty("ids")
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	/**
	 *
	 * @return
	 * The comment
	 */
	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

	/**
	 *
	 * @param comment
	 * The comment
	 */
	@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 *
	 * @return
	 * The isPrivate
	 */
	@JsonProperty("is_private")
	public Boolean getIsPrivate() {
		return isPrivate;
	}

	/**
	 *
	 * @param isPrivate
	 * The is_private
	 */
	@JsonProperty("is_private")
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

