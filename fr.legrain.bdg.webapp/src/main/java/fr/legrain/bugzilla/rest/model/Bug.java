
package fr.legrain.bugzilla.rest.model;

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
    "alias",
    "assigned_to",
    "assigned_to_detail",
    "blocks",
    "cc",
    "cc_detail",
    "classification",
    "component",
    "creation_time",
    "creator",
    "creator_detail",
    "deadline",
    "depends_on",
    "dupe_of",
    "flags",
    "groups",
    "id",
    "is_cc_accessible",
    "is_confirmed",
    "is_creator_accessible",
    "is_open",
    "keywords",
    "last_change_time",
    "op_sys",
    "platform",
    "priority",
    "product",
    "qa_contact",
    "resolution",
    "see_also",
    "severity",
    "status",
    "summary",
    "target_milestone",
    "url",
    "version",
    "whiteboard"
})
public class Bug {

    @JsonProperty("alias")
    private List<Object> alias = null;
    @JsonProperty("assigned_to")
    private String assignedTo;
    @JsonProperty("assigned_to_detail")
    private AssignedToDetail assignedToDetail;
    @JsonProperty("blocks")
    private List<Object> blocks = null;
    @JsonProperty("cc")
    private List<Object> cc = null;
    @JsonProperty("cc_detail")
    private List<Object> ccDetail = null;
    @JsonProperty("classification")
    private String classification;
    @JsonProperty("component")
    private String component;
    @JsonProperty("creation_time")
    private String creationTime;
    @JsonProperty("creator")
    private String creator;
    @JsonProperty("creator_detail")
    private CreatorDetail creatorDetail;
    @JsonProperty("deadline")
    private Object deadline;
    @JsonProperty("depends_on")
    private List<Object> dependsOn = null;
    @JsonProperty("description")
    private String description;
    @JsonProperty("dupe_of")
    private Object dupeOf;
    @JsonProperty("flags")
    private List<Object> flags = null;
    @JsonProperty("groups")
    private List<Object> groups = null;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("is_cc_accessible")
    private Boolean isCcAccessible;
    @JsonProperty("is_confirmed")
    private Boolean isConfirmed;
    @JsonProperty("is_creator_accessible")
    private Boolean isCreatorAccessible;
    @JsonProperty("is_open")
    private Boolean isOpen;
    @JsonProperty("keywords")
    private List<Object> keywords = null;
    @JsonProperty("last_change_time")
    private String lastChangeTime;
    @JsonProperty("op_sys")
    private String opSys;
    @JsonProperty("platform")
    private String platform;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("product")
    private String product;
    @JsonProperty("qa_contact")
    private String qaContact;
    @JsonProperty("resolution")
    private String resolution;
    @JsonProperty("see_also")
    private List<Object> seeAlso = null;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("status")
    private String status;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("target_milestone")
    private String targetMilestone;
    @JsonProperty("url")
    private String url;
    @JsonProperty("version")
    private String version;
    @JsonProperty("whiteboard")
    private String whiteboard;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The alias
     */
    @JsonProperty("alias")
    public List<Object> getAlias() {
        return alias;
    }

    /**
     * 
     * @param alias
     *     The alias
     */
    @JsonProperty("alias")
    public void setAlias(List<Object> alias) {
        this.alias = alias;
    }

    /**
     * 
     * @return
     *     The assignedTo
     */
    @JsonProperty("assigned_to")
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * 
     * @param assignedTo
     *     The assigned_to
     */
    @JsonProperty("assigned_to")
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * 
     * @return
     *     The assignedToDetail
     */
    @JsonProperty("assigned_to_detail")
    public AssignedToDetail getAssignedToDetail() {
        return assignedToDetail;
    }

    /**
     * 
     * @param assignedToDetail
     *     The assigned_to_detail
     */
    @JsonProperty("assigned_to_detail")
    public void setAssignedToDetail(AssignedToDetail assignedToDetail) {
        this.assignedToDetail = assignedToDetail;
    }

    /**
     * 
     * @return
     *     The blocks
     */
    @JsonProperty("blocks")
    public List<Object> getBlocks() {
        return blocks;
    }

    /**
     * 
     * @param blocks
     *     The blocks
     */
    @JsonProperty("blocks")
    public void setBlocks(List<Object> blocks) {
        this.blocks = blocks;
    }

    /**
     * 
     * @return
     *     The cc
     */
    @JsonProperty("cc")
    public List<Object> getCc() {
        return cc;
    }

    /**
     * 
     * @param cc
     *     The cc
     */
    @JsonProperty("cc")
    public void setCc(List<Object> cc) {
        this.cc = cc;
    }

    /**
     * 
     * @return
     *     The ccDetail
     */
    @JsonProperty("cc_detail")
    public List<Object> getCcDetail() {
        return ccDetail;
    }

    /**
     * 
     * @param ccDetail
     *     The cc_detail
     */
    @JsonProperty("cc_detail")
    public void setCcDetail(List<Object> ccDetail) {
        this.ccDetail = ccDetail;
    }

    /**
     * 
     * @return
     *     The classification
     */
    @JsonProperty("classification")
    public String getClassification() {
        return classification;
    }

    /**
     * 
     * @param classification
     *     The classification
     */
    @JsonProperty("classification")
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * 
     * @return
     *     The component
     */
    @JsonProperty("component")
    public String getComponent() {
        return component;
    }

    /**
     * 
     * @param component
     *     The component
     */
    @JsonProperty("component")
    public void setComponent(String component) {
        this.component = component;
    }
    
    /**
     * 
     * @return
     *     The component
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param component
     *     The component
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The creationTime
     */
    @JsonProperty("creation_time")
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * 
     * @param creationTime
     *     The creation_time
     */
    @JsonProperty("creation_time")
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * 
     * @return
     *     The creator
     */
    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * @param creator
     *     The creator
     */
    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * @return
     *     The creatorDetail
     */
    @JsonProperty("creator_detail")
    public CreatorDetail getCreatorDetail() {
        return creatorDetail;
    }

    /**
     * 
     * @param creatorDetail
     *     The creator_detail
     */
    @JsonProperty("creator_detail")
    public void setCreatorDetail(CreatorDetail creatorDetail) {
        this.creatorDetail = creatorDetail;
    }

    /**
     * 
     * @return
     *     The deadline
     */
    @JsonProperty("deadline")
    public Object getDeadline() {
        return deadline;
    }

    /**
     * 
     * @param deadline
     *     The deadline
     */
    @JsonProperty("deadline")
    public void setDeadline(Object deadline) {
        this.deadline = deadline;
    }

    /**
     * 
     * @return
     *     The dependsOn
     */
    @JsonProperty("depends_on")
    public List<Object> getDependsOn() {
        return dependsOn;
    }

    /**
     * 
     * @param dependsOn
     *     The depends_on
     */
    @JsonProperty("depends_on")
    public void setDependsOn(List<Object> dependsOn) {
        this.dependsOn = dependsOn;
    }

    /**
     * 
     * @return
     *     The dupeOf
     */
    @JsonProperty("dupe_of")
    public Object getDupeOf() {
        return dupeOf;
    }

    /**
     * 
     * @param dupeOf
     *     The dupe_of
     */
    @JsonProperty("dupe_of")
    public void setDupeOf(Object dupeOf) {
        this.dupeOf = dupeOf;
    }

    /**
     * 
     * @return
     *     The flags
     */
    @JsonProperty("flags")
    public List<Object> getFlags() {
        return flags;
    }

    /**
     * 
     * @param flags
     *     The flags
     */
    @JsonProperty("flags")
    public void setFlags(List<Object> flags) {
        this.flags = flags;
    }

    /**
     * 
     * @return
     *     The groups
     */
    @JsonProperty("groups")
    public List<Object> getGroups() {
        return groups;
    }

    /**
     * 
     * @param groups
     *     The groups
     */
    @JsonProperty("groups")
    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The isCcAccessible
     */
    @JsonProperty("is_cc_accessible")
    public Boolean getIsCcAccessible() {
        return isCcAccessible;
    }

    /**
     * 
     * @param isCcAccessible
     *     The is_cc_accessible
     */
    @JsonProperty("is_cc_accessible")
    public void setIsCcAccessible(Boolean isCcAccessible) {
        this.isCcAccessible = isCcAccessible;
    }

    /**
     * 
     * @return
     *     The isConfirmed
     */
    @JsonProperty("is_confirmed")
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    /**
     * 
     * @param isConfirmed
     *     The is_confirmed
     */
    @JsonProperty("is_confirmed")
    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    /**
     * 
     * @return
     *     The isCreatorAccessible
     */
    @JsonProperty("is_creator_accessible")
    public Boolean getIsCreatorAccessible() {
        return isCreatorAccessible;
    }

    /**
     * 
     * @param isCreatorAccessible
     *     The is_creator_accessible
     */
    @JsonProperty("is_creator_accessible")
    public void setIsCreatorAccessible(Boolean isCreatorAccessible) {
        this.isCreatorAccessible = isCreatorAccessible;
    }

    /**
     * 
     * @return
     *     The isOpen
     */
    @JsonProperty("is_open")
    public Boolean getIsOpen() {
        return isOpen;
    }

    /**
     * 
     * @param isOpen
     *     The is_open
     */
    @JsonProperty("is_open")
    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 
     * @return
     *     The keywords
     */
    @JsonProperty("keywords")
    public List<Object> getKeywords() {
        return keywords;
    }

    /**
     * 
     * @param keywords
     *     The keywords
     */
    @JsonProperty("keywords")
    public void setKeywords(List<Object> keywords) {
        this.keywords = keywords;
    }

    /**
     * 
     * @return
     *     The lastChangeTime
     */
    @JsonProperty("last_change_time")
    public String getLastChangeTime() {
        return lastChangeTime;
    }

    /**
     * 
     * @param lastChangeTime
     *     The last_change_time
     */
    @JsonProperty("last_change_time")
    public void setLastChangeTime(String lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    /**
     * 
     * @return
     *     The opSys
     */
    @JsonProperty("op_sys")
    public String getOpSys() {
        return opSys;
    }

    /**
     * 
     * @param opSys
     *     The op_sys
     */
    @JsonProperty("op_sys")
    public void setOpSys(String opSys) {
        this.opSys = opSys;
    }

    /**
     * 
     * @return
     *     The platform
     */
    @JsonProperty("platform")
    public String getPlatform() {
        return platform;
    }

    /**
     * 
     * @param platform
     *     The platform
     */
    @JsonProperty("platform")
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The product
     */
    @JsonProperty("product")
    public String getProduct() {
        return product;
    }

    /**
     * 
     * @param product
     *     The product
     */
    @JsonProperty("product")
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * 
     * @return
     *     The qaContact
     */
    @JsonProperty("qa_contact")
    public String getQaContact() {
        return qaContact;
    }

    /**
     * 
     * @param qaContact
     *     The qa_contact
     */
    @JsonProperty("qa_contact")
    public void setQaContact(String qaContact) {
        this.qaContact = qaContact;
    }

    /**
     * 
     * @return
     *     The resolution
     */
    @JsonProperty("resolution")
    public String getResolution() {
        return resolution;
    }

    /**
     * 
     * @param resolution
     *     The resolution
     */
    @JsonProperty("resolution")
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * 
     * @return
     *     The seeAlso
     */
    @JsonProperty("see_also")
    public List<Object> getSeeAlso() {
        return seeAlso;
    }

    /**
     * 
     * @param seeAlso
     *     The see_also
     */
    @JsonProperty("see_also")
    public void setSeeAlso(List<Object> seeAlso) {
        this.seeAlso = seeAlso;
    }

    /**
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    /**
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The summary
     */
    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    /**
     * 
     * @param summary
     *     The summary
     */
    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 
     * @return
     *     The targetMilestone
     */
    @JsonProperty("target_milestone")
    public String getTargetMilestone() {
        return targetMilestone;
    }

    /**
     * 
     * @param targetMilestone
     *     The target_milestone
     */
    @JsonProperty("target_milestone")
    public void setTargetMilestone(String targetMilestone) {
        this.targetMilestone = targetMilestone;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The version
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * @return
     *     The whiteboard
     */
    @JsonProperty("whiteboard")
    public String getWhiteboard() {
        return whiteboard;
    }

    /**
     * 
     * @param whiteboard
     *     The whiteboard
     */
    @JsonProperty("whiteboard")
    public void setWhiteboard(String whiteboard) {
        this.whiteboard = whiteboard;
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
