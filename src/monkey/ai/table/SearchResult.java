package monkey.ai.table;

/**
 * A <code>SearchResult</code> stores the aftermath of a brute-force search.
 * That is, the best/refutation move, the computed score, its nature, the depth
 * of the search and the number of nodes visited in the process. The last field
 * is an addition to the data usually found "traditional" transposition tables.
 * It is meant to support the <code>TWOBIG1</code> replacement scheme. See D.M.
 * Breuker, J.W.H.M. Uiterwijk, H.J. van den Herik, <i>Information in
 * Transposition Tables</i>, in H.J. van den Herik, J.W.H.M. Uiterwijk (eds),
 * <i>Advances in Computer Chess 8</i>, Computer Science Department,
 * Universiteit Maastricht. 1997, pp. 2-3, 4.
 *
 * @param <Action>  The type of the moves of the game.
 * @param <Utility> The type used to quantify the payoffs.
 * @author Gaia Clerici
 * @version 1.0
 * @since 1.0
 */
public class SearchResult<Action, Utility extends Comparable<Utility>>
		implements Comparable<SearchResult<Action, Utility>> {

	/**
	 * A <code>ScoreType</code> indicates the nature of the {@link #SCORE}.
	 *
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	public enum ScoreType {
		/** Indicates that the {@link #SCORE} is a true value. */
		TRUEVALUE,
		/** Indicates that the {@link #SCORE} is an upper bound. */
		UPPERBOUND,
		/** Indicates that the {@link #SCORE} is a lower bound. */
		LOWERBOUND;
	}

	/**
	 * Contains either the best move in the position (which obtained the highest
	 * score) or the refutation move (which caused a cutoff).
	 */
	public final Action MOVE;
	/** Contains the value of {@link #MOVE}. */
	public final Utility SCORE;
	/**
	 * Indicates whether the score is a true value, an upper bound or a lower bound.
	 */
	public final ScoreType FLAG;
	/** Contains the actual depth of the search. */
	public final int SEARCHDEPTH;
	/** Contains the number of nodes of the subtree searched. */
	public final int SEARCHEDNODES;

	/**
	 * Constructs a new {@link SearchResult}.
	 *
	 * @param move          Non-<code>null</code> initializer for {@link #MOVE}.
	 * @param score         Non-<code>null</code> initializer for {@link #SCORE}.
	 * @param flag          Non-<code>null</code> initializer for {@link FLAG}.
	 * @param searchDepth   Strictly positive initializer for {@link #SEARCHDEPTH}.
	 * @param searchedNodes Strictly positive initializer for
	 *                      {@link #SEARCHEDNODES}.
	 * @throws NullPointerException     At least one of the arguments is
	 *                                  <code>null</code>.
	 * @throws IllegalArgumentException searchDepth or searchedNodes are negative or
	 *                                  zero.
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	public SearchResult(Action move, Utility score, ScoreType flag, int searchDepth, int searchNodes) {
		if (move == null || score == null || flag == null)
			throw new NullPointerException("move, result and flag can't be null.");
		if (searchDepth < 0 || searchNodes < 0)
			throw new IllegalArgumentException("searchDepth and searchNodes can't be negative values.");
		MOVE = move;
		SCORE = score;
		FLAG = flag;
		SEARCHDEPTH = searchDepth;
		SEARCHEDNODES = searchNodes;

	}

	/**
	 * Compares to another {@link SearchResult} by considering their
	 * {@link #SEARCHEDNODES}. This is coherent with the <code>TWOBIG1</code>
	 * replacement scheme. Note: this class has a natural ordering that is
	 * inconsistent with equals.
	 * 
	 * @throws NullPointerException {@inheritDoc}
	 * @throws ClassCastException   {@inheritDoc}
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	public int compareTo(SearchResult<Action, Utility> s) {
		if (SEARCHEDNODES >= s.SEARCHEDNODES)
			return SEARCHEDNODES;
		return s.SEARCHEDNODES;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	@Override
	public String toString() {
		return MOVE + " " + SCORE + " " + FLAG + " " + SEARCHDEPTH + " " + SEARCHEDNODES;
	}
}
