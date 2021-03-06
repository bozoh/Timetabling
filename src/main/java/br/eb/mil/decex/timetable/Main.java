package br.eb.mil.decex.timetable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("Inicializando");

		// Matrix3D<String> m3 = new Matrix3D<>(3, 2, 10);
		// Matrix2D<String> m2 = null;
		// for (int i = 0; i < 3; i++) {
		// m2 = new Matrix2D<>(2, 10);
		// for (int j = 0; j < 20; j++) {
		// m2.getContent().set(j, String.valueOf(20*i+j));
		//
		// }
		// m3.set2DPart(i, m2);
		// }
		//
		// System.out.println(m3);

		System.out.println("Criando restições de horário");
		Set<Horario> r1 = new HashSet<Horario>();
		// Primeiro tempo de aula durante a semana inteira
		r1.add(Horario.H01);
		r1.add(Horario.H02);
		r1.add(Horario.H03);
		r1.add(Horario.H04);
		r1.add(Horario.H05);

		Set<Horario> r2 = new HashSet<Horario>();
		// Restrição de dia de semana quinta-feira
		r2.add(Horario.H04);
		r2.add(Horario.H09);
		r2.add(Horario.H14);
		r2.add(Horario.H19);
		r2.add(Horario.H24);
		r2.add(Horario.H25);

		// r1+r2
		Set<Horario> r3 = new HashSet<Horario>(r1);
		r3.addAll(r2);

		System.out.println("Criando Professores");
		Professor pport = new Professor();
		// Em termos de tepos de 50min por semana
		// 40horas = (40*60)/50 = 48
		pport.setCargaHoraria(48);
		pport.setCodigo("pport");
		pport.setRestrincoes(r1);
		pport.setId(0);

		Professor pmat = new Professor();
		pmat.setCargaHoraria(48);
		pmat.setCodigo("pmat");
		pmat.setId(1);
		pmat.setRestrincoes(r2);

		// Professor pgeo = new Professor();
		// pgeo.setCargaHoraria(24);
		// pgeo.setCodigo("pgeo");
		// pgeo.setRestrincoes(r3);
		// Professor phist = new Professor();
		// phist.setCargaHoraria(24);
		// phist.setCodigo("phis");
		//
		// Professor part = new Professor();
		// part.setCargaHoraria(24);
		// part.setCodigo("part");
		// part.setRestrincoes(r1);
		//
		// Professor pfilo = new Professor();
		// pfilo.setCargaHoraria(48);
		// pfilo.setCodigo("pfilo");
		// pfilo.setRestrincoes(r3);
		//
		// Professor pef = new Professor();
		// pef.setCargaHoraria(24);
		// pef.setCodigo("pef");
		// pef.setRestrincoes(r1);
		//
		// Professor pling = new Professor();
		// pling.setCargaHoraria(24);
		// pling.setCodigo("pling");

		Disciplina port = new Disciplina();
		port.setCodigo("D-Port");
		port.setCargaHoraria(5);

		Disciplina mat = new Disciplina();
		mat.setCodigo("D-Mat");
		mat.setCargaHoraria(5);

		Disciplina geo = new Disciplina();
		geo.setCodigo("D-Geo");
		geo.setCargaHoraria(4);

		Disciplina hist = new Disciplina();
		hist.setCodigo("D-hist");
		hist.setCargaHoraria(4);

		Disciplina art = new Disciplina();
		art.setCodigo("D-Art");
		art.setCargaHoraria(4);

		Disciplina ling = new Disciplina();
		ling.setCodigo("D-ling");
		ling.setCargaHoraria(4);

		Disciplina ef = new Disciplina();
		ef.setCodigo("D-ef");
		ef.setCargaHoraria(2);

		Disciplina filo = new Disciplina();
		filo.setCodigo("D-filo");
		filo.setCargaHoraria(2);

		Set<Disciplina> d1 = new HashSet<Disciplina>();
		d1.add(port);
		d1.add(mat);
		d1.add(ling);
		d1.add(ef);
		d1.add(filo);

		d1.add(geo);
		d1.add(hist);
		d1.add(art);
		pport.setDisciplinas(d1);
		
//		Set<Disciplina> d2 = new HashSet<Disciplina>();
//		d2.add(geo);
//		d2.add(hist);
//		d2.add(art);
//		
//		pmat.setDisciplinas(d2);

		Turma t1 = new Turma();
		t1.setId(0);

//		Turma t2 = new Turma();
//		t2.setId(1);
		
		Set<Professor> professores = new HashSet<Professor>();
		professores.add(pport);
//		professores.add(pmat);
		Set<Turma> turmas = new HashSet<Turma>();
		turmas.add(t1);
	//	turmas.add(t2);
		
		FitnessCalculator fc = new FitnessCalculator();
		int fitness=0;
		int tries=0;
		
		List<Quadro> populacao = new ArrayList<Quadro>();
		for (int i = 0; i < 60; i++) {
			MontaHorarios mh = new MontaHorarios(turmas, professores, 5, 6);
			populacao.add(mh.montaHorarios());
		}
		Solver s = new Solver(populacao, 30);
		s.init();
		
		
		
//		while(fitness!=30){
//			tries++;
//			
//			Quadro q = mh.montaHorarios();
//			fitness=fc.orphanFitness(q);
//			if (fitness == 30) {
//				System.out.println("------>"+tries);
//				System.out.println(q.printQuadroHorariosTurmas());
//			}
//		}
	}

}
