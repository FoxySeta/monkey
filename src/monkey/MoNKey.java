package monkey;

import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import mnkgame.MNKCell;
import mnkgame.MNKPlayer;
import monkey.ai.AI;
import monkey.ai.Player;
import monkey.mnk.Board;
import monkey.mnk.Position;

/**
 * A <code>MoNKey</code> offers a possible implementation of
 * <code>MNKPlayer</code> using an instance of {@link monkey.ai.AI}.
 *
 * @author Gaia Clerici
 * @version 1.0
 * @since 1.0
 */
public class MoNKey implements MNKPlayer {

	/**
	 * {@inheritDoc}
	 *
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	@Override
	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		ai = new AI<Board, Position>(first ? Player.P1 : Player.P2, new Board(M, N, K));
		m = M;
		n = N;
		timeout = timeout_in_secs;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	@Override
	public MNKCell selectCell(MNKCell[] FC, MNKCell[] MC) {
		// final long startTime = System.currentTimeMillis();
		if (MC.length > 1)
			ai.update(new Position(m, n, MC[MC.length - 2]));
		if (MC.length > 0)
			ai.update(new Position(m, n, MC[MC.length - 1]));
		Position p;
		if (m * n > BIGGAME)
			p = ai.immediateSearch();
		else {
			final ExecutorService executor = Executors.newSingleThreadExecutor();
			final Future<Position> task = executor.submit(ai);
			executor.shutdown();
			try {
				p = task.get(timeout, TimeUnit.SECONDS);
			} catch (Exception e) {
				p = ai.partial_result();
			}
			if (!executor.isTerminated())
				executor.shutdownNow();
		}
		// System.err.println(formatTimeInterval(System.currentTimeMillis() -
		// startTime));
		return new MNKCell(p.getRow(), p.getColumn());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	@Override
	public String playerName() {
		return "🅼🐵🅽🅺ey";
	}

	/**
	 * Formats a number of milliseconds converting it into seconds and milliseconds.
	 *
	 * @param milliseconds The number to convert and format.
	 * @return A formatted <code>String</code>.
	 * @author Gaia Clerici
	 * @version 1.0
	 * @since 1.0
	 */
	public static String formatTimeInterval(long milliseconds) {
		return String.format("⏱️ %2d\"%03d", milliseconds / S_TO_MS, milliseconds % S_TO_MS);
	}

	/** Artificial intelligence used by <code>MoNKey</code>. */
	private AI<Board, Position> ai = null;
	/** Number of rows. */
	private int m;
	/** Number of columns. */
	private int n;
	/** Timeout per initialization/move (in seconds). */
	private int timeout;
	/**
	 * Maximum number of cells of a configuration commonly considered small enough
	 * to be explored.
	 */
	final static private int BIGGAME = 100;
	/** Conversion factor from seconds to milliseconds. */
	final static private int S_TO_MS = 1000;

}
