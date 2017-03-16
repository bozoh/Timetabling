package br.eb.mil.decex.timetable.rule;

import java.util.Map;
import java.util.stream.Collectors;

import br.eb.mil.decex.timetable.Disciplina;

public class CargaHorariaDisciplinaRule extends HardRule {

	private boolean invalido;

	public CargaHorariaDisciplinaRule(Rule r) {
		super(r);
	}

	@Override
	public boolean isValid() {
		if (r.isValid()) {
			Map<Disciplina, Long> counts = content.stream()
					.collect(Collectors.groupingBy(d -> d, Collectors.counting()));

			if (counts.size() < 8)
				this.invalido = true;
			else
				counts.forEach((d, c) -> checkCarga(d, c));

			return !this.invalido;
		}
		return false;
	}

	private Disciplina checkCarga(Disciplina d, Long cargaAlocada) {
		if (d.getCargaHoraria() != cargaAlocada) {
			this.invalido = true;
		}
		return d;
	}

}
