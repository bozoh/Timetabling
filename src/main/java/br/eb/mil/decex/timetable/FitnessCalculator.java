package br.eb.mil.decex.timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class FitnessCalculator {
	boolean invalido = false;

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
		this.invalido = false;
		int fit = 0;
		int diasPorSemana = 5;
		int temposPorDia = 6;
		for (int i = 0; i < diasPorSemana; i++) {
			// Map<String, Integer> map = ;
			fit += frequency(horarios.subList(i * temposPorDia, i * temposPorDia + temposPorDia)).values().stream()
					.mapToInt(Number::intValue).sum();
			// for (Iterator iterator = map.keySet().iterator();
			// iterator.hasNext();) {
			// String key = (String) iterator.next();
			// fit += map.get(key);
			// }
		}
		Map<Disciplina, Long> counts = horarios.stream().collect(Collectors.groupingBy(d -> d, Collectors.counting()));

		if (counts.size() < 8)
			invalido = true;
		else
			counts.forEach((d, v) -> checkCarga(d, v));

		// Map<Disciplina, Integer> map = frequency(horarios.subList(0,
		// horarios.size()));

		return (this.invalido ? -1 * fit : fit);
	}

	private Disciplina checkCarga(Disciplina d, Long cargaAlocada) {
		if (d.getCargaHoraria() != cargaAlocada) {
//			System.out.println(d + "\n\tCarga horaria: " + d.getCargaHoraria() + "\n\tAlocada: " + cargaAlocada);
			this.invalido = true;
		}
		return d;
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
				// MAX DISCIPLINA
				if (freq > 3)
					this.invalido = true;
				// numberAndItsOcuurenceMap.put(anterior, -40);
				// MIN DISCILINA
				else if (freq < 2)
					numberAndItsOcuurenceMap.put(anterior, 0);
				else {
					if (numberAndItsOcuurenceMap.get(anterior) != null)
						freq += numberAndItsOcuurenceMap.get(anterior);

					numberAndItsOcuurenceMap.put(anterior, freq);
				}
				anterior = d;
				freq = 1;
			}

		}
		// MAX DISCIPLINA
		if (freq > 3)
			this.invalido = true;
		// numberAndItsOcuurenceMap.put(anterior.getCodigo(), -40);
		// MIN DISCILINA
		else if (freq < 2)
			numberAndItsOcuurenceMap.put(anterior, 0);
		else {
			if (numberAndItsOcuurenceMap.get(anterior) != null)
				freq += numberAndItsOcuurenceMap.get(anterior);

			numberAndItsOcuurenceMap.put(anterior, freq);
		}
		return numberAndItsOcuurenceMap;
	}

}
