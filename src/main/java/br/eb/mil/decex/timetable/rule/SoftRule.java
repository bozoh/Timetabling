package br.eb.mil.decex.timetable.rule;

import java.util.Collection;
import java.util.List;

import br.eb.mil.decex.timetable.Disciplina;

public abstract class SoftRule implements Rule {

	protected final Rule r;
	protected final List<Disciplina> content;

	public SoftRule(Rule r) {
		this.r = r;
		this.content = r.getContent();
	}

	@Override
	public List<Disciplina> getContent() {
		return this.content;
	}

	@Override
	public boolean isValid() {
		return r.isValid();
	}

	@Override
	public int getFitness() {
		if (!this.isValid())
			return -1;
		return r.getFitness() + calculaFitness();

	}

	protected abstract int calculaFitness();
}
