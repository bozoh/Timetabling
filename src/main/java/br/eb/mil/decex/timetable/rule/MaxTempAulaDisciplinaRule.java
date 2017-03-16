package br.eb.mil.decex.timetable.rule;

import java.util.List;

import br.eb.mil.decex.timetable.Disciplina;

/**
 * Verifica se a o horario viola o quantidade máxima de tempos SEGUIDOS de uma
 * disciplina
 * 
 * @author Carlos Alexandre S. da Fonseca
 *
 */
public class MaxTempAulaDisciplinaRule extends HardRule {

	public static final int DIAS_SEMANA = 5;

	public static final int MAX_TEMPO_DISCIPLINA = 3;

	private final int temposPorDia;

	public MaxTempAulaDisciplinaRule(Rule r) {
		super(r);
		temposPorDia = content.size() / DIAS_SEMANA;
	}

	@Override
	public boolean isValid() {
		boolean invalido = false;
		for (int i = 0; i < DIAS_SEMANA; i++) {
			invalido = !checkTempos(content.subList(i * temposPorDia, i * temposPorDia + temposPorDia));
			if (invalido)
				return false;
		}

		return true;
	}

	private boolean checkTempos(List<Disciplina> list) {
		int freq = 0;
		Disciplina anterior = null;
		for (Disciplina d : list) {
			if (anterior == null) {
				freq = 1;
				anterior = d;
				continue;
			} else if (anterior.getCodigo().equals(d.getCodigo())) {
				freq += 1;
				anterior = d;
				continue;
			} else if (!anterior.getCodigo().equals(d.getCodigo())) {
				//
				if (freq > MAX_TEMPO_DISCIPLINA)
					return false;
				anterior = d;
				freq = 1;
			}

		}
		// MAX DISCIPLINA
		if (freq > MAX_TEMPO_DISCIPLINA)
			return false;
		return true;
	}

}
