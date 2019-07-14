package com.furca.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Article {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="title", nullable=false)
	@NotNull
    private String title;
	
	@Column(name="content", nullable=false)
	@Lob
	@NotNull
    private String content;
    
    @Column(name="published_on", nullable=false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date publishedOn;
    
    @Column(name="published_by", nullable=false)
	@NotNull
    private String publishedBy;
    
    @Column(name="source", nullable=false)
	@NotNull
    private String source;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    
    @ManyToMany
	@JoinTable(name = "article_tag",
		joinColumns = { @JoinColumn(name = "article_id") }, 
		inverseJoinColumns = {@JoinColumn(name = "tag_id") })
	Set<Tag> tags;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}