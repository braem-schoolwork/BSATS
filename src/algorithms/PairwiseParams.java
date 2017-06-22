package algorithms;

public class PairwiseParams
{
	private String sequence1;
	private String sequence2;
	private Algorithm algorithm;
	private int gapPenalty;
	
	public PairwiseParams() {
		setSequence1("");
		setSequence2("");
		setAlgorithm(Algorithm.NEEDLEMAN);
		setGapPenalty(1);
	}
	
	public PairwiseParams(String seq1, String seq2, Algorithm algo, int gap) {
		this.setSequence1(seq1);
		this.setSequence2(seq2);
		this.setAlgorithm(algo);
		this.setGapPenalty(gap);
	}

	public int getGapPenalty() {
		return gapPenalty;
	}

	public void setGapPenalty(int gapPenalty) {
		this.gapPenalty = gapPenalty;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public String getSequence2() {
		return sequence2;
	}

	public void setSequence2(String sequence2) {
		this.sequence2 = sequence2;
	}

	public String getSequence1() {
		return sequence1;
	}

	public void setSequence1(String sequence1) {
		this.sequence1 = sequence1;
	}
	
}
