package br.eb.mil.decex.timetable;

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

	public Vector<T> getAllContent() {
		return content;
	}

	public T getContent(int i, int j, int k) {
		return content.get(getIndex(i,j,k));
	}

	public void print() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < P; i++) {
			sb.append("\n Página: ").append(i + 1);
			for (int j = 0; j < L; j++) {
				for (int k = 0; k < C; k++)
					sb.append(content.get(getIndex(i,j,k)) + "\t");
				sb.append("\n");
			}
		}
		System.out.println(sb.toString());
	}

	public void setContent(int i, int j, int k, T t) {
		content.set(getIndex(i,j,k), t);
	}

	/**
	 * Converte as coordenadas da matriz em 
	 * um índice para o vetor de conteúdo
	 * 
	 * @param i -> página
	 * @param j -> linha
	 * @param k -> coluna
	 * @return
	 */
	private int getIndex(int i, int j, int k) {
		// Simplificação de:
		// i*(size(j)*size(k) + j*(size(k)) +k
		// (i*size(j)+j)*size(k) + k
		// Lembrando que i, j, k començão em zero
		return (((i * L) + j) * C) + k;
	}

	public void Init(T t) {
		this.temp = t;
		clearContent();
		for (int i = 0; i < P * L * C; i++)
			getAllContent().add(i, t);
	}

	public Matrix2D<T> get2DPart(int i) {
		Matrix2D<T> M2DForm;
		M2DForm = new Matrix2D<T>(L, C);
		M2DForm.Init(getT());
		for (int j = 0; j < L; j++) {
			for (int k = 0; k < C; k++)
				M2DForm.setContent(j, k, getContent(i, j, k));
		}
		return M2DForm;
	}

	public void set2DPart(int i, Matrix2D<T> M2DForm) {
		for (int j = 0; j < M2DForm.getR(); j++)
			for (int k = 0; k < M2DForm.getC(); k++)
				setContent(i, j, k, M2DForm.getContent(j, k));
	}
}
