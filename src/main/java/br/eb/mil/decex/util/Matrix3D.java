package br.eb.mil.decex.util;

import java.util.Vector;

public class Matrix3D<T> {
	private final int P;// Página
	private final int L;// Linha
	private final int C;// Coluna
	private Vector<T> content;
	private T temp;

	public Matrix3D(int pagina, int linha, int coluna) {
		this.P = pagina;
		this.L = linha;
		this.C = coluna;
		content = new Vector<T>();
		content.setSize(pagina * linha * coluna);
	}

	public T getT() {
		return temp;
	}

	public int getPagina() {
		return P;
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

	public T getContent(int i, int j, int k) {
		return content.get(getIndex(i, j, k));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < P; i++) {
			sb.append("\nPágina: ").append(i + 1).append("\n");
			sb.append(get2DPart(i).toString());
		}
		return sb.toString();
	}

	public void setContent(int i, int j, int k, T t) {
		content.set(getIndex(i, j, k), t);
	}

	/**
	 * Converte as coordenadas da matriz em um índice para o vetor de conteúdo
	 * 
	 * @param i-> página
	 * @param j-> linha
	 * @param k-> coluna
	 * @return
	 */
	private int getIndex(int i, int j, int k) {
		// Simplificação de:
		// i*(size(j)*size(k) + j*(size(k)) +k
		// (i*size(j)+j)*size(k) + k
		// Lembrando que i, j, k començão em zero
		return (((i * L) + j) * C) + k;
	}

	public void init(T t) {
		this.temp = t;
		clearContent();
		for (int i = 0; i < P * L * C; i++)
			getContent().add(i, t);
	}

	public Matrix2D<T> get2DPart(int i) {
		Matrix2D<T> matrix2D;
		matrix2D = new Matrix2D<T>(L, C);
		matrix2D.getContent().addAll(0, getContent().
				subList(getIndex(i, 0, 0), getIndex(i, L - 1, C - 1) + 1));
		return matrix2D;
	}

	public void set2DPart(int i, Matrix2D<T> matrix2D) {
		getContent().addAll(getIndex(i, 0, 0), matrix2D.getContent());
	}
}
