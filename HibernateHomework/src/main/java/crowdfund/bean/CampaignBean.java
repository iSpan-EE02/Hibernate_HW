package crowdfund.bean;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "campaigns")
public class CampaignBean implements Serializable{
	
	
	@Id
	@Column(name = "campaignid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer campaignID;
	
	@Column(name = "campaignname")
	private String campaignName;
	
	@Column(name = "campaigncategory")
	private String campaignCategory;
	
	@Column(name = "goalamount")
	private Double goalAmount;
	
	@Column(name = "currentamount")
	private Double currentAmount;
	
	@Column(name = "startdate")
	private Date startDate;
	
	@Column(name = "enddate")
	private Date endDate;
	
	@Column(name = "coverimage")
	private String coverImage;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "creatorid")
	private Integer creatorID;
	
	
	@Column(name = "description")
	private String description;
	
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public Integer getCampaignID() {
		return campaignID;
	}
	public void setCampaignID(Integer campaignID) {
		this.campaignID = campaignID;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getCampaignCategory() {
		return campaignCategory;
	}
	public void setCampaignCategory(String campaignCategory) {
		this.campaignCategory = campaignCategory;
	}
	public Double getGoalAmount() {
		return goalAmount;
	}
	public void setGoalAmount(Double goalAmount) {
		this.goalAmount = goalAmount;
	}
	public Double getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(Double currentAmount) {
		this.currentAmount = currentAmount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(Integer creatorID) {
		this.creatorID = creatorID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
