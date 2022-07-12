package it.polito.tdp.yelp.model;

public class ReviewCount {
	
	private Review r;
	private int count;
	
	public ReviewCount(Review r, int count) {
		super();
		this.r = r;
		this.count = count;
	}
	
	public Review getR() {
		return r;
	}
	public void setR(Review r) {
		this.r = r;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return r.getReviewId()+ "        #ARCHI USCENTI: " + count;
	}
	
	
	
	

}
