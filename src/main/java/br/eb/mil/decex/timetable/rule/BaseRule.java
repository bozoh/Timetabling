package br.eb.mil.decex.timetable.rule;

import java.util.Collection;
import java.util.List;

import br.eb.mil.decex.timetable.Disciplina;

public class BaseRule implements Rule {
	protected List<Disciplina> content;

	public BaseRule(List<Disciplina> content) {
		this.content = content;
	}

	@Override
	public List<Disciplina> getContent() {
		return this.content;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public int getFitness() {
		return 0;
	}

	
	
}
