package com.furca.model;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class News {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="content", nullable=false)
	@Lob
	@NotNull
    private String content;
    
    @Column(name="published_on", nullable=false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date publishedOn;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}
}