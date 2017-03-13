package br.eb.mil.decex.util;

import java.util.Vector;

public class Matrix2D<T> {
	private final int L; // Linha
	private final int C; // Coluna
	private final Vector<T> content;

	public Matrix2D(int L, int C) {
		this.L = L;
		this.C = C;
		content = new Vector<T>();
		content.setSize(L * C);
	}

	public int getLinha() {
		return L;
	}

	public int getColuna() {
		return C;
	}

	public void clearContent() {
		content.clear();
	}

	public Vector<T> getContent() {
		return content;
	}

	public T getContent(int i, int j) {
		return content.get(getIndex(i, j));
	}

	/**
	 * Converte as coordenadas da matriz em um índice para o vetor de conteúdo
	 * 
	 * @param i-->Linha
	 * @param j-->Coluna
	 * @return
	 */
	private int getIndex(int i, int j) {
		// A conversão é
		// i*size(j)+j
		return (i * C) + j;
	}

	public void setContent(int i, int j, T t) {
		content.set(getIndex(i, j), t);
	}

	public void init(T t) {
		clearContent();
		for (int i = 0; i < L * C; i++)
			getContent().add(i, t);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < C; j++) {
			for (int i = 0; i < L; i++)
				sb.append(content.get(getIndex(i, j))).append("\t");
			sb.append("\n");
		}
		return sb.toString();
	}
}
