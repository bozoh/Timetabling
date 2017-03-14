package br.eb.mil.decex.timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FitnessCalculator {

	public int orphanFitness(Quadro quadro) {
		Vector<Disciplina> horarios = quadro.flattenTurma(0).getContent();
		// Disciplina[][] horario = quadro.flattenTurma(0);
		// Disciplina[][] horarioTranspose = new
		// Disciplina[horario[0].length][horario.length];
		// for (int i = 0; i < horario.length; i++) {
		// for (int j = 0; j < horario[i].length; j++) {
		// horarioTranspose[j][i] = horario[i][j];
		// }
		// }
		// return calculate(horarioTranspose);
		return calculate(horarios);
	}

	private int calculate(Vector<Disciplina> horarios) {
		int fit = 0;
		int diasPorSemana = 5;
		int temposPorDia = 6;
		for (int i = 0; i < diasPorSemana; i++) {
			Map<String, Integer> map = frequency(horarios.subList(i * temposPorDia, i * temposPorDia + temposPorDia));
			fit += map.values().stream().mapToInt(Number::intValue).sum();
			// for (Iterator iterator = map.keySet().iterator();
			// iterator.hasNext();) {
			// String key = (String) iterator.next();
			// fit += map.get(key);
			// }
		}
		return fit;
	}

	private Map<String, Integer> frequency(List<Disciplina> list) {
		Map<String, Integer> numberAndItsOcuurenceMap = new HashMap<String, Integer>();
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
				// MAX DISCIPLINA
				if (freq > 3)
					numberAndItsOcuurenceMap.put(anterior.getCodigo(), -40);
				// MIN DISCILINA
				else if (freq < 2)
					numberAndItsOcuurenceMap.put(anterior.getCodigo(), 0);
				else {
					if (numberAndItsOcuurenceMap.get(anterior.getCodigo()) != null)
						freq += numberAndItsOcuurenceMap.get(anterior.getCodigo());

					numberAndItsOcuurenceMap.put(anterior.getCodigo(), freq);
				}
				anterior = d;
				freq = 1;
			}

		}
		// MAX DISCIPLINA
		if (freq > 3)
			numberAndItsOcuurenceMap.put(anterior.getCodigo(), -40);
		// MIN DISCILINA
		else if (freq < 2)
			numberAndItsOcuurenceMap.put(anterior.getCodigo(), 0);
		else {
			if (numberAndItsOcuurenceMap.get(anterior.getCodigo()) != null)
				freq += numberAndItsOcuurenceMap.get(anterior.getCodigo());

			numberAndItsOcuurenceMap.put(anterior.getCodigo(), freq);
		}
		return numberAndItsOcuurenceMap;
	}

}
