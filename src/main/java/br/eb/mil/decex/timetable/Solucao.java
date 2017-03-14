package br.eb.mil.decex.timetable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import br.eb.mil.decex.util.Matrix3D;

public class Solucao {

	private List<Quadro> populacao;
	private int objetivo;
	private FitnessCalculator fit;

	public Solucao(List<Quadro> populacao, int objetivo) {
		this.populacao = populacao;
		this.objetivo = objetivo;
		this.fit = new FitnessCalculator();
	}

	public void init() {
		List<Quadro> sorted = new LinkedList<Quadro>(populacao);
		Quadro found = null;
		while (found == null) {
			for (int it = 0; it < 100; it++) {

				sorted.forEach(q -> q.addFitness(fit.orphanFitness(q)));
				try {
					found = sorted.stream().filter(q -> q.getFitness() == objetivo).findAny().get();
					if (found != null) {
						System.out.println(found.getFitness() + "------\n" + found.flattenTurma(0));
						return;
					}
				} catch (NoSuchElementException e) {
					int cut = sorted.stream().mapToInt(q -> q.getFitness()).min().getAsInt();
					sorted = sorted.stream().filter(q -> q.getFitness() > cut - 1)
							.sorted((q1, q2) -> q2.getFitness() - q1.getFitness()).collect(Collectors.toList());

					//sorted.forEach(q -> System.out.println(q.getFitness() + "\n" + q.flattenTurma(0)));
					System.out.println(sorted.get(1).getFitness() + "------\n" + sorted.get(0).flattenTurma(0));
					System.out.println("-----" + it + "-------");

					int gen = populacao.size() - sorted.size();
					for (int i = 0; i < gen; i++) {
						sorted.addAll(genetrate(sorted.get(i), sorted.get(i + 1)));

					}

				}
			}
			System.out.println(sorted.get(0).getFitness() + "------\n" + sorted.get(0).flattenTurma(0));
		}
		System.out.println(found.getFitness() + "------\n" + found.flattenTurma(0));
	}

	private Collection<? extends Quadro> genetrate(Quadro quadro, Quadro quadro2) {
		List<Quadro> quadros = new LinkedList<Quadro>();
		Matrix3D<Disciplina> matrix3d1 = new Matrix3D<>(quadro.getNumTurmas(), quadro.getNumProfessores(),
				quadro.getNumHorarios());
		Matrix3D<Disciplina> matrix3d2 = new Matrix3D<>(quadro.getNumTurmas(), quadro.getNumProfessores(),
				quadro.getNumHorarios());

		int size = quadro.flattenTurma(0).getContent().size();

		matrix3d1.getContent().addAll(0, quadro.flattenTurma(0).getContent().subList(0, size / 2));
		matrix3d1.getContent().addAll(size / 2, quadro2.flattenTurma(0).getContent().subList(size / 2, size));
		quadros.add(new Quadro(matrix3d1));

		matrix3d2.getContent().addAll(0, quadro2.flattenTurma(0).getContent().subList(0, size / 2));
		matrix3d2.getContent().addAll(size / 2, quadro.flattenTurma(0).getContent().subList(size / 2, size));
		quadros.add(new Quadro(matrix3d2));
		return quadros;
	}

}
