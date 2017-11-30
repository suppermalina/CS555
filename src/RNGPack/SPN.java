package RNGPack;
import java.util.ArrayList;
import java.util.List;

class SPN {
	private List<List<Double>> sequence;
	private String nameOfSequence;
	
	SPN(String nameOfSequence) {
		this.nameOfSequence = nameOfSequence;
		sequence = new ArrayList<List<Double>>();
	}
	
	List<List<Double>> getSequence() {
		return sequence;
	}
	
	String getName() {
		return nameOfSequence;
	}
	
	void setSequence(List<Double> d) {
		sequence.add(d);
	}
	
	int getSeqenceSize() {
		return sequence.size();
	}
}
