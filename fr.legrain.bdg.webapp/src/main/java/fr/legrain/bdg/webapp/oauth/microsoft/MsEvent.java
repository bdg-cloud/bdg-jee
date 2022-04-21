package fr.legrain.bdg.webapp.oauth.microsoft;

import java.io.Serializable;

public class MsEvent implements Serializable{
		private String id;
		private String subject;
		private String bodyPreview;
		private Boolean isAllDay;
		private String webLink;
		private String bodyContentType;
		private String bodyContent;
		private String startDateTime;
		private String startTimeZone;
		private String endDateTime;
		private String endTimeZone;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getBodyPreview() {
			return bodyPreview;
		}
		public void setBodyPreview(String bodyPreview) {
			this.bodyPreview = bodyPreview;
		}
		public Boolean getIsAllDay() {
			return isAllDay;
		}
		public void setIsAllDay(Boolean isAllDay) {
			this.isAllDay = isAllDay;
		}
		public String getWebLink() {
			return webLink;
		}
		public void setWebLink(String webLink) {
			this.webLink = webLink;
		}
		public String getBodyContentType() {
			return bodyContentType;
		}
		public void setBodyContentType(String bodyContentType) {
			this.bodyContentType = bodyContentType;
		}
		public String getBodyContent() {
			return bodyContent;
		}
		public void setBodyContent(String bodyContent) {
			this.bodyContent = bodyContent;
		}
		public String getStartDateTime() {
			return startDateTime;
		}
		public void setStartDateTime(String startDateTime) {
			this.startDateTime = startDateTime;
		}
		public String getStartTimeZone() {
			return startTimeZone;
		}
		public void setStartTimeZone(String startTimeZone) {
			this.startTimeZone = startTimeZone;
		}
		public String getEndDateTime() {
			return endDateTime;
		}
		public void setEndDateTime(String endDateTime) {
			this.endDateTime = endDateTime;
		}
		public String getEndTimeZone() {
			return endTimeZone;
		}
		public void setEndTimeZone(String endTimeZone) {
			this.endTimeZone = endTimeZone;
		}
		
	}