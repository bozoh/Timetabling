package br.eb.mil.decex.timetable;

import java.util.LinkedList;
import java.util.List;

import br.eb.mil.decex.util.Matrix2D;
import br.eb.mil.decex.util.Matrix3D;
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
	@Getter
	private int fitness;

	private final Matrix3D<Disciplina> horarios;

	public Quadro(int numProfessores, int numTurmas, int diasPorSemana, int temposAluaPorDia) {
		// -1 pois começão no zero e não no 1
		this.numProfessores = numProfessores;
		this.numTurmas = numTurmas;
		this.diasPorSemana = diasPorSemana;
		this.temposAluaPorDia = temposAluaPorDia;
		this.numHorarios = diasPorSemana * temposAluaPorDia;
		this.horarios = new Matrix3D<>(this.numTurmas, this.numProfessores, this.numHorarios);
	}

	public String printQuadroHorariosTurmas() {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < numTurmas; i++) {
			ret.append(flattenTurma(i));
			ret.append("\n\n###########################################\n\n");
		}
		return ret.toString();
	}

	public Matrix2D<Disciplina> flattenTurma(int turma) {
		int idxInicial = this.horarios.getIndex(turma, 0, 0);
		int idxFinal = this.horarios.getIndex(turma, numProfessores - 1, numHorarios - 1);
		List<Disciplina> horarios = this.horarios.getContent().subList(idxInicial, idxFinal + 1);
		return flatten(horarios);
	}

	public Matrix2D<Disciplina> flattenProfessor(int professor) {
		List<Disciplina> horarios = new LinkedList<Disciplina>();
		for (int t = 0; t < numTurmas; t++) {
			int idxInicial = this.horarios.getIndex(t, professor, 0);
			int idxFinal = this.horarios.getIndex(t, professor, numHorarios - 1);
			horarios.addAll(this.horarios.getContent().subList(idxInicial, idxFinal + 1));
		}
		return flatten(horarios);
	}

	private Matrix2D<Disciplina> flatten(List<Disciplina> horarios) {
		Matrix2D<Disciplina> retVal = new Matrix2D<>(diasPorSemana, temposAluaPorDia);

		int idx = 0;
		for (Disciplina d : horarios) {
			if (d != null) {
				retVal.getContent().set(idx % numHorarios, d);
			}
			idx++;
		}
		return retVal;
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
			if (horarios.getContent(turma, professor, i) != null)
				// Já alocado
				return false;
		}

		return true;
	}

	private void doAlloc(int turma, int professor, int horarionInicial, int quantidaHorario, Disciplina d) {
		for (int i = horarionInicial; i < horarionInicial + quantidaHorario; i++) {
			horarios.setContent(turma, professor, i, d);
		}
	}

	public boolean alloc(int turma, int professor, int horarionInicial, int quantidaHorario, Disciplina d) {
		boolean ret = canAlloc(turma, professor, horarionInicial, quantidaHorario);
		if (ret) {
			doAlloc(turma, professor, horarionInicial, quantidaHorario, d);
		}
		return ret;
	}

	public void addFitness(int value) {
		this.fitness += value;
	}
	// public Disciplina[][][] getHorarios() {
	// return horarios;
	// }

}
