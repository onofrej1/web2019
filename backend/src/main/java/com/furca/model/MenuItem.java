package com.furca.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "menu_item")
public class MenuItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title", nullable = false)
	@NotNull
	private String title;
	
	@Column(name = "link")
	private String link;
	
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="menu_id")
    private Menu menu;
	
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="page_id")
    private Page page;
	
	@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="parent_id")
    private MenuItem parent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public MenuItem getParent() {
		return parent;
	}

	public void setParent(MenuItem parent) {
		this.parent = parent;
	}
	
}
