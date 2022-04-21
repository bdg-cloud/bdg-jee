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
	"time",
	"text",
	"bug_id",
	"count",
	"attachment_id",
	"is_private",
	"tags",
	"creator",
	"creation_time",
	"id"
})
public class Comment {

	@JsonProperty("time")
	private String time;
	@JsonProperty("text")
	private String text;
	@JsonProperty("bug_id")
	private Integer bugId;
	@JsonProperty("count")
	private Integer count;
	@JsonProperty("attachment_id")
	private Object attachmentId;
	@JsonProperty("is_private")
	private Boolean isPrivate;
	@JsonProperty("tags")
	private List<Object> tags = new ArrayList<Object>();
	@JsonProperty("creator")
	private String creator;
	@JsonProperty("creation_time")
	private String creationTime;
	@JsonProperty("id")
	private Integer id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The time
	 */
	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	/**
	 *
	 * @param time
	 * The time
	 */
	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 *
	 * @return
	 * The text
	 */
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	/**
	 *
	 * @param text
	 * The text
	 */
	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	/**
	 *
	 * @return
	 * The bugId
	 */
	@JsonProperty("bug_id")
	public Integer getBugId() {
		return bugId;
	}

	/**
	 *
	 * @param bugId
	 * The bug_id
	 */
	@JsonProperty("bug_id")
	public void setBugId(Integer bugId) {
		this.bugId = bugId;
	}

	/**
	 *
	 * @return
	 * The count
	 */
	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	/**
	 *
	 * @param count
	 * The count
	 */
	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 *
	 * @return
	 * The attachmentId
	 */
	@JsonProperty("attachment_id")
	public Object getAttachmentId() {
		return attachmentId;
	}

	/**
	 *
	 * @param attachmentId
	 * The attachment_id
	 */
	@JsonProperty("attachment_id")
	public void setAttachmentId(Object attachmentId) {
		this.attachmentId = attachmentId;
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

	/**
	 *
	 * @return
	 * The tags
	 */
	@JsonProperty("tags")
	public List<Object> getTags() {
		return tags;
	}

	/**
	 *
	 * @param tags
	 * The tags
	 */
	@JsonProperty("tags")
	public void setTags(List<Object> tags) {
		this.tags = tags;
	}

	/**
	 *
	 * @return
	 * The creator
	 */
	@JsonProperty("creator")
	public String getCreator() {
		return creator;
	}

	/**
	 *
	 * @param creator
	 * The creator
	 */
	@JsonProperty("creator")
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 *
	 * @return
	 * The creationTime
	 */
	@JsonProperty("creation_time")
	public String getCreationTime() {
		return creationTime;
	}

	/**
	 *
	 * @param creationTime
	 * The creation_time
	 */
	@JsonProperty("creation_time")
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 *
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
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
