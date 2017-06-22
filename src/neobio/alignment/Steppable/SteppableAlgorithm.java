package neobio.alignment.Steppable;

import neobio.alignment.InvalidSequenceException;
import neobio.alignment.ScoringScheme;
import structures.ObservableMatrix;

/**
 * Created by Adam on 10/31/2016.
 */
public interface SteppableAlgorithm {
    /**
     * Does one 'step' forward in the process.
     */
    void stepForward();

    /**
     * Does one 'step' backwards in the process.
     */
    void stepBackward();

    /**
     * Does all 'steps' in the process in one go.
     */
    default void full()
    {
        while (hasMore())
            stepForward();
    }

    /**
     * Used for if something gets updated.
     * The process will repeat steps to get to the same point
     * it was modified at. But only if the changes are compatible.
     */
    void resume();

    /**
     * Returns the row currently being processed.
     * @return the row being processed
     */
    int getCurrentRow();

    /**
     * Returns the row currently being processed.
     * @return the column being processed
     */
    int getCurrentColumn();

    /**
     * Used for loading the sequences into the algorithm.
     * @param s1 First sequence
     * @param s2 Second sequence
     */
    void loadSequences(String s1, String s2) throws InvalidSequenceException;

    /**
     * Sets the scoring scheme used in the algorithm.
     * @param s the specified scoring scheme to use.
     */
    void setScoringScheme(ScoringScheme s);

    /**
     * Get if there are steps remaining in the algorithm.
     */
    boolean hasMore();

    /**
     * Get if there are available before the current step.
     * Used to tell if there are steps available for stepping back.
     */
    boolean hasPrevious();

    /**
     * Gets the observable matrix being used as output for the algorithm.
     * @return the observable matrix.
     */
    ObservableMatrix getObservableMatrix();

    String[] getSequenceAlignment();
}
