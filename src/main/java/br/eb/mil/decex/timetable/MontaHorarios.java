package br.eb.mil.decex.timetable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MontaHorarios {

	@NonNull
	Set<Turma> turmas;
	// TODO mover disciplinas para o professor
	@NonNull
	Set<Professor> professores;
	@NonNull
	int diasPorSemana;
	@NonNull
	int temposAluaPorDia;
	private Quadro horarios;

	private Disciplina jaAllocado;

	public Quadro montaHorarios() {
		this.jaAllocado = new Disciplina();
		jaAllocado.setCodigo("XXXX");

		horarios = new Quadro(professores.size(), turmas.size(), diasPorSemana, temposAluaPorDia);

		Stack<Integer>[] allocTable = initAllocTable();

		for (Turma turma : turmas) {
			for (Professor professor : professores) {
				Set<Disciplina> disciplinas = professor.getDisciplinas();
				// List<Integer> ordemHorarios =
				// genOrdemHorarios(horarios.getNumHorarios());
				for (Disciplina disciplina : disciplinas) {
					int quantHorariosParaAlloc = getQuantHorarios(disciplina);
					// Adicionar randomicamente
					while (disciplina.getCargaHoraria() != 0) {
						boolean alloc = false;
						Stack<Integer> tries = new Stack<Integer>();
						tries.addAll(genOrdemHorarios(temposAluaPorDia));
						Integer diaSemana=tries.pop();
						while (!tries.isEmpty() && !alloc) {
							if (allocTable[diaSemana].size() < quantHorariosParaAlloc)
								continue;
							//Integer horarioInicial = tries.pop();
							Integer horarioInicial = allocTable[diaSemana].pop();
							alloc = allocaHorarios(turma.getId(), professor, horarioInicial,
									quantHorariosParaAlloc, disciplina);

							horarios.alloc(turma.getId(), professor.getId(), horarioInicial.intValue(),
									quantHorariosParaAlloc, disciplina);
							if (alloc)
								updateListaHorario(allocTable, diaSemana, horarioInicial,
										quantHorariosParaAlloc);
						}
						if (!alloc) {
							quantHorariosParaAlloc--;
						} else {
							disciplina.addCargaHorariaAlocada(quantHorariosParaAlloc);
							quantHorariosParaAlloc = getQuantHorarios(disciplina);
						}
					}
				}

			}
		}

		return horarios;
	}

	private Stack<Integer>[] initAllocTable() {
		//ArrayList<Individual>[] group = (ArrayList<Individual>[])new ArrayList[4];
		Stack<Integer>[] allocTable = (Stack<Integer>[])new Stack[diasPorSemana];
		
		for (int d = 0; d < diasPorSemana; d++) {
			for (int t = 0; t <temposAluaPorDia; t++) {
				allocTable[d].add(temposAluaPorDia * d + t);
			}
		}
		return allocTable;
	}

	private boolean allocaHorarios(int turma, Professor professor, int horarioInicial, int quantHorariosParaAlloc,
			Disciplina disciplina) {
		boolean alloc = horarios.alloc(turma, professor.getId(), horarioInicial, quantHorariosParaAlloc, disciplina);
		// if (alloc) {
		// for (int t = turma; t < turmas.size(); t++) {
		// alloc = horarios.alloc(turma, professor.getId(), horarioInicial,
		// quantHorariosParaAlloc, jaAllocado);
		// }
		// professor.addCargaHoraria(quantHorariosParaAlloc);
		// }

		return alloc;
	}

	private void updateListaHorario(Stack<Integer>[] allocTable,int diaSemana, int horarioInicial,
			int quantHorariosParaAlloc) {
		for (int i = horarioInicial; i < horarioInicial + quantHorariosParaAlloc; i++) {
			Integer target = new Integer(i);
			if (allocTable[diaSemana].contains(target))
				allocTable[diaSemana].remove(target);
		}
	}

	private int getQuantHorarios(Disciplina disciplina) {
		int MAX_TEMPO_AULA = 3;
		int MIN_TEMPO_AULA_DESEJADO = 2;
		int quantTempo = disciplina.getCargaHoraria();
		if (quantTempo > MAX_TEMPO_AULA) {
			if (quantTempo - MIN_TEMPO_AULA_DESEJADO >= MAX_TEMPO_AULA)
				return MAX_TEMPO_AULA;
			else
				return MIN_TEMPO_AULA_DESEJADO;
		}

		return quantTempo;
	}

	private List<Integer> genOrdemHorarios(int numHorarios) {
		List<Integer> tries = IntStream.range(0, numHorarios).sorted().boxed().collect(Collectors.toList());
		Collections.shuffle(tries);
		return tries;
	}
}