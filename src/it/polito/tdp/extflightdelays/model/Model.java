package it.polito.tdp.extflightdelays.model;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.extflightdelays.db.Adiacenza;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	ExtFlightDelaysDAO dao;
	Map<Integer, Airport> map;
	List<Airport> vertici;
	private LinkedList<Airport> ottima;
	
	public Model() {
		dao= new ExtFlightDelaysDAO();
		map= new HashMap<Integer, Airport>();
		dao.loadAllAirports(map);
		
	}
	public void creaGrafo(int compagnie) {
		grafo= new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		vertici= new LinkedList<Airport>(dao.trovaVertici(compagnie, map));
		
		for(Airport a: dao.trovaVertici2(compagnie, map)) {
			if(!vertici.contains(a)) {
				vertici.add(a);
			}
		
		}
		Graphs.addAllVertices(grafo, vertici);
		for(Adiacenza a: dao.trovaArchi(map)) {
			if(vertici.contains(a.getA1()) && vertici.contains(a.getA2())) {
				Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
		
		System.out.println("Grafo con "+grafo.vertexSet().size()+" vertici e "+ grafo.edgeSet().size()+" archi");
	}
	public SimpleWeightedGraph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	public String getConnessi(Airport scelto) {
		String string="";
		List<Adiacenza> ordinati= new LinkedList<Adiacenza>(dao.trovaArchi(map));
		Collections.sort(ordinati);
		/*ordinati.sort((a1,a2)->{
			if(a1.getPeso()<a2.getPeso())
				return 1;
			else
				return -1;
			
		});*/
		for(Adiacenza e: ordinati) {
			if(e.getA1().equals(scelto) && grafo.vertexSet().contains(e.getA2())){
			
				string += e.getA2()+" peso: "+ grafo.getEdgeWeight(grafo.getEdge(scelto, e.getA2()))+"\n";
			}
		}
		
		return string;
	}
	public List<Airport> cercaItinerario(Airport partenza, Airport arrivo, int maxTratte) {
		
		this.ottima = new LinkedList<Airport>();
		List<Airport> parziale = new LinkedList<Airport>();
		parziale.add(partenza);
	
		cercaPercorso(0,maxTratte, parziale, partenza, arrivo);
		
		return this.ottima;
	}

	private void cercaPercorso(int step, int maxTratte, List<Airport> parziale, Airport partenza, Airport arrivo ) {
		
		//vedere se la soluzione corrente è migliore della ottima corrente
		if(parziale.get(parziale.size()-1).equals(arrivo) && step+1<maxTratte) {
			if(pesoMax(parziale)>pesoMax(ottima)) {
				this.ottima = new LinkedList<Airport>(parziale);
			}
		}
				
		//ottengo tutti i candidati
		List<Airport> candidati = new LinkedList<Airport>(Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1)));
		
		for(Airport candidato : candidati) {
			if(parziale.isEmpty() || candidato.compareTo(parziale.get(parziale.size()-1))>0) {
				//è un candidato che non ho ancora considerato
				parziale.add(candidato);
				this.cercaPercorso(step+1, maxTratte, parziale, partenza, arrivo);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	public double pesoMax(List<Airport> list) {
		double peso=0;
		for(Airport a:list) {
			if(list.indexOf(a) != (list.size()-1) ) {
			peso+=grafo.getEdgeWeight(grafo.getEdge(a, list.get(list.indexOf(a)+1)));
			}
		}
			
		return peso;
	}
	

}
