package com.zach.biz.view.mode;

public class Page {

	// 开始页
	private int page = 1;

	// 每页数据量
	private int pageSize = 10;

	// 总共多少条数据
	private long total = 0;
	
	// 排序
	private String orderBy = "id desc";
		
	public Page() {}
	
	public Page(int page) {
		this.page = page;
	}

	public Page(long total) {
		this.total = total;
	}
	
	public Page(int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}

	public Page(int page, int pageSize, long total) {
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize > 1000) {
			this.pageSize = 1000;
		} else {
			this.pageSize = pageSize;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public static Page getPageInfo(int page, int pageSize, long total) {
		return new Page(page, pageSize, total);
	}

}
