package br.eb.mil.decex.timetable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;

import br.eb.mil.decex.util.Matrix3D;

public class Solver {

	private List<Quadro> populacao;
	private double objetivo;
	private FitnessCalculator fit;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public Solver(List<Quadro> populacao, int objetivo) {
		this.populacao = populacao;
		this.objetivo = objetivo;
		this.fit = new FitnessCalculator();
	}

	public void init() throws InterruptedException, FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		List<Quadro> sorted = new LinkedList<Quadro>(populacao);
		Quadro found = null;
		int count = 0;
		int oldcont = 0;
		int intervalo = 0;
		int oldmax = 0;
		int regenLimit = 1500;
		while (found == null) {
			count++;

			sorted.forEach(q -> q.addFitness(fit.orphanFitness(q)));
			try {
				found = sorted.stream().filter(q -> q.getFitness() == objetivo).findAny().get();
				if (found != null) {
					continue;
				}
			} catch (NoSuchElementException e) {
				int min = (int) sorted.stream().mapToInt(q -> q.getFitness()).min().getAsInt();
				sorted = sorted.stream().filter(q -> q.getFitness() >= 0)
						.sorted((q1, q2) -> q2.getFitness() - q1.getFitness()).collect(Collectors.toList());
				int cut = (int) sorted.stream().mapToInt(q -> q.getFitness()).average().getAsDouble();
				int max = (int) sorted.stream().mapToInt(q -> q.getFitness()).max().getAsInt();
				int min2 = (int) sorted.stream().mapToInt(q -> q.getFitness()).min().getAsInt();
				if (cut >= max) {
					// found = sorted.get(0);
					sorted = sorted.stream().filter(q -> q.getFitness() > cut - 1)
							.sorted((q1, q2) -> q2.getFitness() - q1.getFitness()).collect(Collectors.toList());

				} else {
					sorted = sorted.stream().filter(q -> q.getFitness() > cut)
							.sorted((q1, q2) -> q2.getFitness() - q1.getFitness()).collect(Collectors.toList());
				}
				Quadro best = sorted.get(0);
				sorted = sorted.stream().filter(q -> !q.equals(best)).collect(Collectors.toList());
				sorted.add(best);
				// sorted.forEach(q -> System.out.println(q.getFitness() +
				// "\n" + q.flattenTurma(0)));

				int gen = populacao.size() - sorted.size();
				// sb.append("\n-----\nto gen: " + gen + " cut: " + cut + "
				// best: " + max + " min1: " + min + " min2: "
				// + min2);
				System.out.println("\n-----\nto gen: " + gen + " cut: " + cut + " best: " + max + " min1: " + min
						+ " min2: " + min2);
				System.out.println("--W---\n" + sorted.get(sorted.size() - 1).getFitness() + "\n"
						+ sorted.get(sorted.size() - 1).flattenTurma(0));
				System.out.println("\n--B---\n" + sorted.get(0).getFitness() + "\n" + sorted.get(0).flattenTurma(0));
				System.out.println(count);
				//
				if (cut / objetivo >= 0.9 && cut == min2 && cut == min && (gen <= 0.1 * populacao.size())) {
					if (count - oldcont >= regenLimit) {
						sb.append("\n-----\nREGEN " + intervalo + "\n gen: " + gen + " cut: " + cut + " best: " + max
								+ " min1: " + min + " min2: " + min2);
						sb.append("\n").append(count - oldcont);
						
						System.out.println(ANSI_BLUE + "\n-----\nREGEN\n gen: " + gen + " cut: " + cut + " best: " + max
								+ " min1: " + min + " min2: " + min2 + ANSI_RESET);
						sorted = initiateBest(best, populacao.size());
						oldcont = count;
						continue;
					}
				}

				if (intervalo >= regenLimit && max / objetivo >= 0.9) {
					sb.append("\n-----\nREGEN " + intervalo + "\n gen: " + gen + " cut: " + cut + " best: " + max
							+ " min1: " + min + " min2: " + min2);
					sb.append("\n").append(count - oldcont);
					sorted = initiateBest(best, populacao.size());
					intervalo = 0;
					oldcont = count;
					continue;

				} else if (oldmax == max) {
					intervalo++;
				} else {
					intervalo = 0;
				}
				oldmax = max;

				// Quadro best = sorted.get(0);
				genetrate(best, sorted, gen);
				// Random rnd = new Random();
				// for (int i = 0; i < gen; i++) {
				// int idx = rnd.nextInt(sorted.size() - 2) + 1;
				// if (rnd.nextBoolean())
				// sorted.add(genetrate(best, sorted.get(idx), gen));
				// else
				// sorted.add(genetrate(sorted.get(idx), best, gen));
				//
				// }
				// if (count % 10000 == 0) {
				// initiateBest(sorted);
				// }

			}
		}
		sb.append("\n\n\t\t\t------Solução-------\n\n");
		sb.append(found.flattenTurma(0));
		File f = new File("result.txt");
		PrintWriter p = new PrintWriter(f);
		p.write(sb.toString());
		p.flush();
		p.close();
		System.out.println(sb.toString());
		
	}

	private List<Quadro> initiateBest(Quadro best, int size) {
		List<Quadro> retVal = new LinkedList<Quadro>();
		for (int i = 0; i < size; i++) {
			Vector<Disciplina> v = best.flattenTurma(0).getContent();
			Collections.shuffle(v);
			Matrix3D<Disciplina> matrix3d1 = new Matrix3D<Disciplina>(best.getNumTurmas(), best.getNumProfessores(),
					best.getNumHorarios());
			matrix3d1.getContent().addAll(0, v);
			matrix3d1.getContent().removeIf(d -> d == null);
			retVal.add(new Quadro(matrix3d1));

		}
		return retVal;
	}

	private void initiateBest(List<Quadro> sorted) {
		int idx = 0;
		for (Quadro quadro : sorted) {
			Vector<Disciplina> v = quadro.flattenTurma(0).getContent();
			Collections.shuffle(v);
			Matrix3D<Disciplina> matrix3d1 = new Matrix3D<Disciplina>(quadro.getNumTurmas(), quadro.getNumProfessores(),
					quadro.getNumHorarios());
			matrix3d1.getContent().addAll(0, v);
			matrix3d1.getContent().removeIf(d -> d == null);
			Quadro qnew = new Quadro(matrix3d1);
			sorted.set(idx, qnew);
			idx++;
		}

	}

	private void genetrate(Quadro best, List<Quadro> sorted, int gen) {
		Random rnd = new Random();
		for (int i = 0; i < gen; i++) {
			if (rnd.nextBoolean()) {
				Vector<Disciplina> newContet = new Vector<Disciplina>();
				newContet.addAll(0, best.flattenTurma(0).getContent());
				newContet.removeIf(d -> d == null);
				int moves = best.getFitness();
				for (int m = 0; m < moves; m++) {
					int idxSource = rnd.nextInt(newContet.size() - 1);
					int idxTarget = rnd.nextInt(newContet.size() - 1);
					Disciplina aux = newContet.get(idxSource);
					newContet.set(idxSource, newContet.get(idxTarget));
					newContet.set(idxSource, aux);
				}
				Matrix3D<Disciplina> matrix3d1 = new Matrix3D<Disciplina>(best.getNumTurmas(), best.getNumProfessores(),
						best.getNumHorarios());
				matrix3d1.getContent().addAll(0, newContet);
				matrix3d1.getContent().removeIf(d -> d == null);
				Quadro qnew = new Quadro(matrix3d1);
				sorted.add(qnew);
				if (this.fit.orphanFitness(qnew) == best.getFitness()) {
					best = qnew;
				}
			} else {
				if (sorted.size() > 1) {
					int idx1 = rnd.nextInt(sorted.size() - 1);
					int idx2 = rnd.nextInt(sorted.size() - 1);
					if (rnd.nextBoolean())
						sorted.add(cross(best, sorted.get(idx1)));
					else
						sorted.add(cross(sorted.get(idx1), sorted.get(idx2)));
				}
			}
		}
		// for (int i = 0; i < gen; i++) {
		// int idx1 = rnd.nextInt(sorted.size() - 2) + 1;
		// int idx2 = rnd.nextInt(sorted.size() - 2) + 1;
		// if (rnd.nextBoolean())
		// sorted.add(cross(best, sorted.get(idx1)));
		// else
		// sorted.add(cross(sorted.get(idx1), sorted.get(idx2)));
		//
		// }
		//
		// // Mutating
		if (gen == 0 || rnd.nextInt(gen) <= gen * 5 / 100) {
			int qidx = rnd.nextInt(sorted.size() - 1) + 1;
			Quadro q = sorted.get(qidx);
			Vector<Disciplina> content = q.flattenTurma(0).getContent();
			int inicial = rnd.nextInt(content.size());
			int end = rnd.nextInt(content.size());

			Disciplina dinicial = content.get(inicial);
			Disciplina dfinal = content.get(end);
			content.set(inicial, dfinal);
			content.set(end, dinicial);
			Matrix3D<Disciplina> matrix3d1 = new Matrix3D<Disciplina>(q.getNumTurmas(), q.getNumProfessores(),
					q.getNumHorarios());
			matrix3d1.getContent().addAll(0, content);
			matrix3d1.getContent().removeIf(d -> d == null);
			Quadro qnew = new Quadro(matrix3d1);
			sorted.set(qidx, qnew);
		}
	}

	private Quadro cross(Quadro q1, Quadro q2) {

		int size = q1.flattenTurma(0).getContent().size();

		List<Disciplina> horarios = new LinkedList<Disciplina>(q1.flattenTurma(0).getContent().subList(0, size / 2));
		horarios.addAll(q2.flattenTurma(0).getContent().subList(size / 2, size));

		Random rnd = new Random();
		if (rnd.nextInt(100) <= 5) {
			int inicial = rnd.nextInt(horarios.size());
			int end = rnd.nextInt(horarios.size());

			Disciplina dinicial = horarios.get(inicial);
			Disciplina dfinal = horarios.get(end);
			horarios.set(inicial, dfinal);
			horarios.set(end, dinicial);
		}

		Matrix3D<Disciplina> retVal = new Matrix3D<Disciplina>(q1.getNumTurmas(), q1.getNumProfessores(),
				q1.getNumHorarios());

		retVal.getContent().addAll(0, horarios);
		retVal.getContent().removeIf(d -> d == null);
		return new Quadro(retVal);
	}

}
