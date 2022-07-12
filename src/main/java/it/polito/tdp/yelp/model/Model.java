package it.polito.tdp.yelp.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<Review, DefaultWeightedEdge> grafo;
	
	private YelpDao dao;
	
	private Map<String, Business> idMapBusiness;
	
	private Map<String, Review> idMapReview;
	
	private List<Review> vertici;
	
	
	
	public Model() {
		this.dao = new YelpDao();
		this.idMapBusiness = new HashMap<String, Business>();
		this.dao.getAllBusiness(idMapBusiness);
		this.idMapReview = new HashMap<String, Review>();
		this.dao.getAllReviews(idMapReview);
	}
	


	public String creaGrafo(Business b) {
		
		this.grafo = new SimpleDirectedWeightedGraph<Review, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici = this.dao.getAllVertici(b, this.idMapReview);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		for(Review r1 : this.vertici) {
			for(Review r2 : this.vertici) {
				if(!r1.equals(r2) && r1.getReviewId().compareTo(r2.getReviewId())<0) {
					double peso = Math.abs(ChronoUnit.DAYS.between(r1.getDate(), r2.getDate()));
					if(peso != 0) {
						if(r2.getDate().isBefore(r1.getDate())) {
							//r2 è piu recente di r1 li collego da r2 a r1
							Graphs.addEdgeWithVertices(this.grafo, r2, r1, peso);
						}else {
							Graphs.addEdgeWithVertices(this.grafo, r1, r2, peso);
						}
					}
					
				}
			}
		}

		
		String result = "GRAFO CREATO!\n" + "#VERTICI: " + this.grafo.vertexSet().size() + "\n" + "#ARCHI: " + this.grafo.edgeSet().size()+"\n";
		return result;
	}
	
	public List<ReviewCount> getMaxArchi(){
		List<ReviewCount> result = new ArrayList<ReviewCount>();
		int max = 0;
		for(Review r : this.vertici) {
			int count = this.grafo.outDegreeOf(r);
			if(count > max) {
				result.clear();
				max = count;
				result.add(new ReviewCount(r, count));
			}else if(count == max) {
				result.add(new ReviewCount(r, count));
			}
			
		}
		return result;
		
	}
	
	public List<String> getAllCitta(){
		return this.dao.getAllCitta();
	}

	public List<Business> getAllBusinessByCity(String citta) {
		return this.dao.getBusinessByCity(citta, idMapBusiness);
	}
	
}
