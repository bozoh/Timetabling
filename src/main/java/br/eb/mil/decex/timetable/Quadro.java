package br.eb.mil.decex.timetable;

import lombok.Getter;

public class Quadro {
	@Getter
	private final int numTurmas;
	@Getter
	private final int numProfessores;
	@Getter
	private final int diasPorSemana;
	@Getter
	private final int temposAluaPorDia;
	@Getter
	private final int numHorarios;

	private final Disciplina[][][] horarios;

	public Quadro(int numProfessores, int numTurmas, int diasPorSemana, int temposAluaPorDia) {
		// -1 pois começão no zero e não no 1
		this.numProfessores = numProfessores;
		this.numTurmas = numTurmas;
		this.diasPorSemana = diasPorSemana;
		this.temposAluaPorDia = temposAluaPorDia;
		this.numHorarios = diasPorSemana * temposAluaPorDia;
		this.horarios = new Disciplina[this.numTurmas][this.numProfessores][this.numHorarios];
	}

	public String printQuadroHorariosTurmas() {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < numTurmas; i++) {
			ret.append(printQuadroHorarioTurma(i));
			ret.append("\n\n###########################################\n\n");
		}
		return ret.toString();
	}

	private String printQuadroHorarioTurma(int turma) {
		StringBuilder ret = new StringBuilder();
		Disciplina[][] quadro = flattenTurma(turma);
		for (int t = 0; t < temposAluaPorDia; t++) {
			for (int d = 0; d < diasPorSemana; d++) {
				if (quadro[t][d] != null)
					ret.append(quadro[t][d].getCodigo());
				else
					ret.append("XXXX");

				ret.append(" | ");
			}
			ret.append("\n--------------------------------------------------------\n");
		}
		return ret.toString();
	}

	private Disciplina[][] flattenTurma(int turma) {
		Disciplina[][] horarios = new Disciplina[numProfessores][numHorarios];
		for (int p = 0; p < horarios.length; p++) {
			horarios[p] = this.horarios[turma][p].clone();
		}
		// Disciplina[][] quadro = new
		// Disciplina[temposAluaPorDia][diasPorSemana];
		// int diaSemana = 0;
		// int tempoAula = 0;
		//
		// for (int h = 0; h < numHorarios; h++) {
		// tempoAula = h % temposAluaPorDia;
		// diaSemana = h / temposAluaPorDia;
		// for (int p = 0; p < numProfessores; p++) {
		// if (horarios[turma][p][h] != null) {
		// quadro[tempoAula][diaSemana] = horarios[turma][p][h];
		// break;
		// }
		// }
		// }

		return flatten(horarios);
	}

	private Disciplina[][] flattenProfessor(int professor) {
		Disciplina[][] horarios = new Disciplina[numTurmas][numHorarios];
		for (int t = 0; t < horarios.length; t++) {
			horarios[t] = this.horarios[t][professor].clone();
		}
		return flatten(horarios);
	}

	private Disciplina[][] flatten(Disciplina[][] horarios) {
		Disciplina[][] quadro = new Disciplina[temposAluaPorDia][diasPorSemana];
		int diaSemana = 0;
		int tempoAula = 0;
		for (int i = 0; i < horarios.length; i++) {
			for (int h = 0; h < horarios[i].length; h++) {
				if (horarios[i][h] != null) {
					tempoAula = h % temposAluaPorDia;
					diaSemana = h / temposAluaPorDia;
					quadro[tempoAula][diaSemana] = horarios[i][h];
				}
			}
		}

		return quadro;
	}

	public boolean canAlloc(int turma, int professor, int horarionInicial, int quantidaHorario) {
		// Verificando se o espaço de horário requirido (quantidaHorario) não
		// vai ficar quebrado em dias diferentes
		if ((horarionInicial % temposAluaPorDia) + quantidaHorario > temposAluaPorDia)
			// A quantidade requerida para o alocamento extrapola a quantidades
			// de horario do dia
			// ou seja ficaria parte do horaio em um dia, e parte em outro
			return false;

		for (int i = horarionInicial; i < horarionInicial + quantidaHorario; i++) {
			if (horarios[turma][professor][i] != null)
				// Já alocado
				return false;
		}

		return true;
	}

	private void doAlloc(int turma, int professor, int horarionInicial, int quantidaHorario, Disciplina d) {
		for (int i = horarionInicial; i < horarionInicial + quantidaHorario; i++) {
			horarios[turma][professor][i] = d;
		}
	}

	public boolean alloc(int turma, int professor, int horarionInicial, int quantidaHorario, Disciplina d) {
		boolean ret = canAlloc(turma, professor, horarionInicial, quantidaHorario);
		if (ret) {
			doAlloc(turma, professor, horarionInicial, quantidaHorario, d);
		}
		return ret;
	}

	// public Disciplina[][][] getHorarios() {
	// return horarios;
	// }

}
