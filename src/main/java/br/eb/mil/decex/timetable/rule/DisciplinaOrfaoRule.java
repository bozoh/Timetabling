package br.eb.mil.decex.timetable.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.eb.mil.decex.timetable.Disciplina;

public class DisciplinaOrfaoRule extends SoftRule {

	public static final int diasPorSemana = 5;

	private final int temposPorDia;

	public DisciplinaOrfaoRule(Rule r) {
		super(r);
		temposPorDia = content.size() / diasPorSemana;
	}

	@Override
	protected int calculaFitness() {
		int fit = 0;
		for (int i = 0; i < diasPorSemana; i++) {
			fit += frequency(content.subList(i * temposPorDia, i * temposPorDia + temposPorDia)).values().stream()
					.mapToInt(Number::intValue).sum();
		}

		return fit;
	}

	private Map<Disciplina, Integer> frequency(List<Disciplina> list) {
		Map<Disciplina, Integer> numberAndItsOcuurenceMap = new HashMap<Disciplina, Integer>();
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
				numberAndItsOcuurenceMap.put(anterior, freq);
				anterior = d;
				freq = 1;
			}
		}
		numberAndItsOcuurenceMap.put(anterior, freq);

		return numberAndItsOcuurenceMap;
	}

}
