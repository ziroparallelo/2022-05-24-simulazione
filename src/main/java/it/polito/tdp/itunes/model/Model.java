package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private List<Genre> generi;
	private ItunesDAO dao;
	private Graph<Track, DefaultWeightedEdge> grafo;
	private Map<Integer, Track> idMap;
	private List<Track> vertici;
	private List<Arco> archi;
	private List<Track> soluzione;
	
	public Model() {
		this.generi = new ArrayList<Genre>();
		this.dao = new ItunesDAO();
		
		
		
	}
	
	public void creaGrafo(Genre genere) {
		
		this.vertici = new ArrayList<Track>();
		this.archi = new ArrayList<Arco>();
		
		this.grafo = new SimpleWeightedGraph<Track, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<Integer, Track>();
		
		//Aggiungo i vertici
		this.vertici = dao.getAllVertici(genere, idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		
		//Aggiungo gli archi
		this.archi = dao.getAllArchi(genere, idMap);
		for(Arco a : archi) {
		Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
		
	}
	
	
	public List<Genre> getAllGeneri(){
		
		this.generi = dao.getAllGenres();
		
		Collections.sort(this.generi);
		return this.generi;
	}

	public int getNvert() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size();
	}

	public int getNArc() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}

	public Arco getArcoMax() {
		
		int pesoMax = 0;
		Arco arcoMax = null;
		for(Arco a : this.archi) {
			if(pesoMax<a.getPeso()) {
				pesoMax = a.getPeso();
				arcoMax = a;
			}
		}
		return arcoMax;
	}

	public List<Track> getVertici() {
		
		Collections.sort(this.vertici);
		return this.vertici;
	}

	public List<Track> listaCanzoni(Track canzone, Long bytes) {
		
		this.soluzione = new ArrayList<Track>();
		
		List<Track> parziale = new ArrayList<Track>();
		List<Track> vConn = new ArrayList<Track>();
		parziale.add(canzone);
		
		ConnectivityInspector<Track, DefaultWeightedEdge> ci = 
		new ConnectivityInspector<Track, DefaultWeightedEdge>(grafo);


		for(Track t : ci.connectedSetOf(canzone)) {
			vConn.add(t);
		}
		
		ricorsione_canzoni(parziale, bytes, vConn);
		

		return soluzione;
	}

	private void ricorsione_canzoni(List<Track> parziale, Long memoria, List<Track> vConn) {
		
		//Condizioni di terminazione
		if(pesoParziale(parziale) > memoria) {
			return;
		} else if(parziale.size()>this.soluzione.size()) {
			this.soluzione = new ArrayList<Track>(parziale);
			System.out.println(this.soluzione);
		}
		
		//Caso normale
		for(Track t : vConn) {
			if(!parziale.contains(t)) {
				parziale.add(t);
				ricorsione_canzoni(parziale, memoria, vConn);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private Integer pesoParziale(List<Track> parziale) {
		
		int peso = 0;
		
		for(Track t : parziale) {
			peso += t.getBytes();
		}
		return peso;
	}
	
}
