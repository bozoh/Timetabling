package br.eb.mil.decex.timetable;

import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Disciplina  {
	String codigo;
	int cargaHoraria;
	@Getter

	Quadro horarios;
	Set<Horario> restrincoes;

	
@Override
public String toString() {
	return this.getCodigo();
}
	

	

}
