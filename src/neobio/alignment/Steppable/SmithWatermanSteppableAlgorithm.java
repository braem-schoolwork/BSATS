package neobio.alignment.Steppable;

import neobio.alignment.*;
import structures.ObservableMatrix;

/**
 * Created by Adam on 10/31/2016.
 */
@SuppressWarnings("all")
public class SmithWatermanSteppableAlgorithm extends neobio.alignment.SmithWaterman implements SteppableAlgorithm {

    private static final int STARTING_ROW = 1, STARTING_COLUMN = 1;
    private static int current_row = STARTING_ROW, current_column = STARTING_COLUMN;
    private static int rows_matrix, cols_matrix, max_score_matrix;
    boolean performed_atleast_one_step = false;

    public SmithWatermanSteppableAlgorithm(String sequence1, String sequence2, ScoringScheme score, ObservableMatrix observed_matrix)
            throws InvalidScoringMatrixException, InvalidSequenceException
    {
        super(observed_matrix);
        this.initializeDynamicMatrix();
        if(!(sequence1 == null || sequence2 == null || score == null || sequence1.equals("") || sequence2.equals("")))
        {
            this.loadSequences(sequence1, sequence2);
            this.setScoringScheme(score);
        }
    }

    public SmithWatermanSteppableAlgorithm(String sequence1, String sequence2, ScoringScheme score, ObservableMatrix observed_matrix, int row_to_resume_to, int column_to_resume_to)
            throws InvalidScoringMatrixException, InvalidSequenceException
    {
        this(sequence1, sequence2, score, observed_matrix);
        this.current_row = row_to_resume_to;
        this.current_column = column_to_resume_to;
    }

    public void resume()
    {
        if(this.scoring == null || !this.sequences_loaded)
            return;

        int old_r = current_row, old_c = current_column;
        int old_rows_length = rows_matrix, old_columns_length = cols_matrix;

        initializeDynamicMatrix();

        //If the resumable position exists in the matrix, resume to it.
        if(old_r < this.matrix.length() && old_c < this.matrix.length(0))
        {
            while(!(current_row == old_r && current_column == old_c))
                this.stepForward();
        }
    }

    private void initializeDynamicMatrix()
    {
        current_row = STARTING_ROW;
        current_column = STARTING_COLUMN;
        rows_matrix = (seq1 == null) ? 0 : seq1.length() + 1;
        cols_matrix = (seq2 == null) ? 0 : seq2.length() + 1;

        this.matrix.initializeNewInternalMatrix(rows_matrix, cols_matrix);

        //initialize first row
        for (int i = 0; i < cols_matrix; i++)
            this.matrix.set(0, i, 0);

        //initialize first column
        for(int j = 0; j < rows_matrix; j++)
            this.matrix.set(j, 0, 0);
    }

    /**
     * Steps forward in computing the matrix row-wise.
     */
    public void stepForward()
    {
        if(!hasMore())
            return;
        if(!performed_atleast_one_step)
        {
            this.initializeDynamicMatrix();
            performed_atleast_one_step = true;
        }

        int ins, sub, del;

        try
        {
            ins = (Integer)this.matrix.get(current_row, current_column -1) + super.scoreInsertion(seq2.charAt(current_column));
            sub = (Integer)this.matrix.get(current_row -1, current_column -1) + super.scoreSubstitution(seq1.charAt(current_row),seq2.charAt(current_column));
            del = (Integer)this.matrix.get(current_row -1, current_column) + super.scoreDeletion(seq1.charAt(current_row));

            // choose the greatest
            this.matrix.set(current_row, current_column, max (ins, sub, del, 0));

            if ((Integer)matrix.get(current_row, current_column) > max_score_matrix)
            {
                // keep track of the maximum score
                max_score_matrix = (Integer)matrix.get(current_row, current_column);
                this.max_row = current_row; this.max_col = current_column;
            }
        }
        catch(IncompatibleScoringSchemeException e) {
            System.err.print("An incompatible scoring scheme was used: " + e);
        }

        incrementMatrixPosition();
        printDynamicMatrix();

        if(!hasMore()){
        	matrix.markFinished();
        }
    }

    public void stepBackward()
    {
        if(hasPrevious())
        {
            decrementMatrixPosition();
            //Clear the previous entry.
            this.matrix.set(current_row, current_column, null);
        }
        printDynamicMatrix();
    }

    private void incrementMatrixPosition()
    {
        this.current_column = (this.current_column + 1) % this.matrix.length(this.current_row);
        if(this.current_column == 0)// the 0 row and column are already filled so we want to start from the 1 row and column onward.
        {
            this.current_column = 1;
            this.current_row++;
        }
    }

    private void decrementMatrixPosition()
    {
        current_column--;
        if(current_column < STARTING_COLUMN && this.matrix != null)
        {
            if(current_row >= matrix.length())
                current_row = matrix.length()-1;
            else if(current_row > STARTING_ROW)
                current_row--;
            current_column = matrix.length(current_row) - 1;
        }
    }

    public boolean hasMore()
    {
        if(this.matrix == null)
            return true;
        else
            return (current_row < this.matrix.length()) && (current_column < this.matrix.length(0));
    }

    public boolean hasPrevious()
    {
        if(current_row > STARTING_ROW)
            if(current_column >= STARTING_COLUMN)
                return true;
            else
                return false;
        else
            if(current_column > STARTING_COLUMN)
                return true;
            else
                return false;
    }

    @Override
    public void loadSequences(String s1, String s2) throws InvalidSequenceException
    {
        super.loadSequences(s1, s2);
    }

    public void setScoringScheme(ScoringScheme s)
    {
        super.setScoringScheme(s);
    }

    public ObservableMatrix getObservableMatrix()
    {
        return this.matrix;
    }

    /**
     * Returns an array containing the aligned sequences including gaps.
     * @return an array containg the two aligned sequences ([0])([1])
     */
    public String[] getSequenceAlignment()
    {
        //Check to make sure the provided strings are substrings of the specified sequences.
        if(!hasMore())
        {
            try
            {
                PairwiseAlignment aligned_sequences = this.buildOptimalAlignment();
                String[] gaped_sequences = {aligned_sequences.getGappedSequence1(), aligned_sequences.getGappedSequence2()};
                return gaped_sequences;
            }
            catch(IncompatibleScoringSchemeException e){System.err.println(e); return null;}
        }
        else
            return null;
    }

    public int getCurrentRow()
    {
        return current_row;
    }

    public int getCurrentColumn()
    {
        return current_column;
    }

    private void printDynamicMatrix()
    {
        if(this.matrix == null)
            return;
        System.out.println("\n\n\n\n\n\n\n\n");
        for(int i = 0; i < matrix.length(); i++)
        {
            for(int j = 0; j < matrix.length(i); j++)
            {
                System.out.print("[");
                if((Integer)matrix.get(i, j) == null)
                    System.out.print(" ");
                else
                    System.out.print((Integer)matrix.get(i, j));
                System.out.print("]");
            }
            System.out.println();
        }
    }
}
