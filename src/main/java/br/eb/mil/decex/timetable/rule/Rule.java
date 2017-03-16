package br.eb.mil.decex.timetable.rule;

import java.util.List;

import br.eb.mil.decex.timetable.Disciplina;

public interface Rule {

	List<Disciplina> getContent();
	boolean isValid();
	int getFitness();
	
	

	

}
