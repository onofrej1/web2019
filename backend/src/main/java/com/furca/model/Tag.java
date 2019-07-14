
package com.furca.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Tag {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long Id;
	
	@Column(name="tag", nullable=false)
	@NotNull
    private String tag;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
