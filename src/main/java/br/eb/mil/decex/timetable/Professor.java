package br.eb.mil.decex.timetable;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Professor {
	@Getter
	@Setter
	String codigo;
	// Em termos de tempos de aula de 50 min / semana
	// se for de 40horas/semana = 48 tempos de aula
	@Getter
	@Setter
	int cargaHoraria;
	@Getter
	@Setter
	Set<Horario> restrincoes;
	@Setter
	Set<Disciplina> disciplinas;
	@Getter
	@Setter
	int Id;

	public Set<Disciplina> getDisciplinas() {
		List<Disciplina> ord = new LinkedList<Disciplina>(this.disciplinas);
		Collections.sort(ord, new Comparator<Disciplina>() {
			@Override
			public int compare(Disciplina d1, Disciplina d2) {
				return d2.getCargaHoraria() - d1.getCargaHoraria();
			}
		});
		return new LinkedHashSet<Disciplina>(ord);
	}

	public void addCargaHoraria(int quantHorariosAlloc) {
		this.cargaHoraria -= quantHorariosAlloc;

	}

}
