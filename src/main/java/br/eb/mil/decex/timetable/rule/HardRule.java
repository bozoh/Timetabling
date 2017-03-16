package br.eb.mil.decex.timetable.rule;

import java.util.Collection;
import java.util.List;

import br.eb.mil.decex.timetable.Disciplina;

public abstract class HardRule implements Rule {

	protected Rule r;
	protected List<Disciplina> content;

	public HardRule(Rule r) {
		this.r = r;
		this.content = r.getContent();
	}

	@Override
	public List<Disciplina> getContent() {
		return this.content;
	}

	@Override
	public int getFitness() {
		if (!this.isValid() || !r.isValid())
			return -1;
		return r.getFitness();

	}

}
