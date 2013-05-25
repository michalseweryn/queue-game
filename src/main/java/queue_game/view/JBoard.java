package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameState;
import queue_game.model.ProductType;
import queue_game.model.Store;

/**
 * @author michal
 * 
 *         User interface containing game board.
 */
public class JBoard extends JPanel implements ComponentListener {

	private static final long serialVersionUID = -2270325617374583365L;
	private Game game = null;
	private GameState gameState = null;

	private JLayeredPane layeredPane;
	// Must be Integers (otherwise doesn't work)
	static final Integer BOARD_LAYER = 0;
	static final Integer QUEUE_LAYER = 1;
	static final Integer STORE_LAYER = 1;
	static final Integer PAWN_LAYER0 = 2;
	static final Integer PRODUCT_LAYER = 35;

	private ArrayList<ArrayList<JPawn>> pawns = new ArrayList<ArrayList<JPawn>>(
			6);
	private ArrayList<JPawn> outdoorMarket = new ArrayList<JPawn>();
	private ArrayList<ArrayList<JProductSquare>> products = new ArrayList<ArrayList<JProductSquare>>(
			6);
	private ArrayList<JProductSquare> outdoorProducts = new ArrayList<JProductSquare>(
			6);
	private List<JQueue> queues = new ArrayList<JQueue>(6);
	private List<JStore> stores = new ArrayList<JStore>(6);
	private JPawn trader;
	// Board Image Data
	private static final double STONE_SIZE = 0.95;
	private static final int BOARD_WIDTH = 15;
	private static final int BOARD_HEIGHT = 14;
	private static final double TILE_SIDES_RATIO = 2;
	private static final String[] boardPattern = new String[] {
			"               ", "               ", "               ",
			"               ", "K  K  K  K  K  ", "O  O  O  O  O  ",
			"L  L  L  L  L  ", "E  E  E  E  E  ", "J  J  J  J  J  ",
			"K  K  K  K  K  ", "A  A  A  A  A  ", "               ",
			"     WYMIANA   ", "               ", };
	private BoardDrawer boardDrawer = null;

	private class BoardDrawer extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2134037715503827042L;

		@Override
		public Dimension getPreferredSize() {
			return getParent().getSize();
		}

		@Override
		public void paintComponent(Graphics g) {
			Random rand = new Random(0);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			double height = getParent().getSize().getHeight();
			double width = getParent().getSize().getWidth();
			g2d.fill(new Rectangle2D.Double(0, 0, width, height));
			double tileWidth = width / BOARD_WIDTH;
			double tileHeight = height / BOARD_HEIGHT;
			for (int i = 0; i < BOARD_WIDTH; i++)
				for (int j = 0; j < BOARD_HEIGHT; j++) {
					int k = 100 + rand.nextInt(10);
					double dx = 0;
					double dy = 0;
					Rectangle.Double rect = new Rectangle2D.Double(i
							* tileWidth + dx, j * tileHeight + dy, tileWidth
							* STONE_SIZE, tileHeight * STONE_SIZE);
					char ch = boardPattern[j].charAt(i);
					if (ch != ' ') {
						String letter = "" + ch;
						int l = 150 + rand.nextInt(5);
						g2d.setColor(new Color(l, l, l));
						g2d.fill(rect);
						Font font = new Font("Arial Black", Font.BOLD,
								(int) (1.5 * tileHeight));
						Rectangle2D bounds = getFontMetrics(font)
								.getStringBounds(letter, g2d);
						GlyphVector v = font.createGlyphVector(
								getFontMetrics(font).getFontRenderContext(), ""
										+ ch);
						AffineTransform aT = AffineTransform
								.getTranslateInstance(i * tileWidth
										+ (tileWidth - bounds.getWidth()) / 2,
										j * tileHeight + 0.75 * tileHeight);
						aT.concatenate(AffineTransform.getScaleInstance(1, 0.5));
						Shape shape = aT.createTransformedShape(v.getOutline());
						g2d.setFont(font);
						g2d.setColor(new Color(k, k, k));
						g2d.fill(shape);

					} else {
						g2d.setColor(new Color(k, k, k));
						g2d.fill(rect);
					}
				}
		}
	}

	public JQueue getQueue(ProductType type) {
		return queues.get(type.ordinal());
	}

	private double tileWidth;
	private double tileHeight;
	private LocalGameActionCreator creator;

	@Override
	public Dimension getPreferredSize() {
		int maxH = (int) getParent().getSize().getHeight() - 180;
		int maxW = (int) getParent().getSize().getWidth();
		if (maxH * BOARD_WIDTH * TILE_SIDES_RATIO < maxW * BOARD_HEIGHT)
			return new Dimension(
					(int) (maxH * BOARD_WIDTH * TILE_SIDES_RATIO / BOARD_HEIGHT),
					maxH);
		return new Dimension(maxW,
				(int) (maxW * BOARD_HEIGHT / (TILE_SIDES_RATIO * BOARD_WIDTH)));

	}

	public void setGame(Game game) {
		this.game = game;
		for (JQueue queue : queues)
			if (queue != null)
				queue.setGame(game);
		resetComponents(true);
	}

	public JBoard(GameState gameState, LocalGameActionCreator adapter) {
		super();
		setOpaque(true);
		this.gameState = gameState;
		this.creator = adapter;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		layeredPane = new JLayeredPane();
		// layeredPane.setPreferredSize(new Dimension(300, 310));
		layeredPane.setBorder(BorderFactory.createTitledBorder("JBoard"));
		for (int i = 0; i < 6; i++)
			queues.add(null);
		for (int i = 0; i < 5; i++)
			stores.add(null);
		for (int i = 0; i < 6; i++)
			pawns.add(new ArrayList<JPawn>());
		for (ProductType store : ProductType.values())
			products.add(new ArrayList<JProductSquare>());
		outdoorProducts = new ArrayList<JProductSquare>();
		BoardDrawer boardDrawer = new BoardDrawer();
		boardDrawer.setBounds(0, 0, 2000, 2000);
		layeredPane.add(boardDrawer, new Integer(0));
		add(layeredPane);
		addComponentListener(this);

	}

	public void update() {
		resetComponents(false);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);

	}

	Game getGame() {
		return game;
	}

	GameState getGameState() {
		return gameState;
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
	}

	public void componentResized(ComponentEvent arg0) {
		tileWidth = getSize().getWidth() / BOARD_WIDTH;
		tileHeight = getSize().getHeight() / BOARD_HEIGHT;
		resetComponents(true);

		layeredPane.repaint();
	}

	/**
	 * 
	 */
	private void resetComponents(boolean resetBounds) {
		if (boardDrawer == null) {
			BoardDrawer boardDrawer = new BoardDrawer();
			boardDrawer.setBounds(0, 0, 2000, 2000);
			layeredPane.add(boardDrawer, BOARD_LAYER);
		}
		int ind = 0;
		for (ProductType product : ProductType.values()) {
			JQueue queue = queues.get(ind);
			boolean newOne = false;
			if (queue == null) {
				newOne = true;
				queue = new JQueue(product, creator);
				queue.setGame(game);
			}

			queue.setBounds((int) (3 * tileWidth * ind),
					(int) (4 * tileHeight), (int) tileWidth,
					(int) (7 * tileHeight));
			if (newOne) {
				queues.set(ind, queue);
				layeredPane.add(queue, QUEUE_LAYER);
			}
			ind++;
		}
		ind = 0;
		for (ProductType product : ProductType.values()) {
			JStore store = stores.get(ind);
			boolean newOne = false;
			if (store == null) {
				newOne = true;
				store = new JStore(game, gameState, product, creator);
			}
			store.setBounds((int) (3 * tileWidth * ind), (int) (0),
					(int) (2.75 * tileWidth), (int) (4 * tileHeight));
			if (newOne) {
				stores.set(ind, store);
				layeredPane.add(store, STORE_LAYER);
				store.repaint();
			}
			ind++;
		}
		if (gameState.getCurrentGamePhase() == null)
			return;
		JQueue queue = queues.get(5);
		boolean newOne = false;
		if (queue == null) {
			queue = new JQueue(null, creator);
			newOne = true;
			queue.setGame(game);
		}
		queue.setBounds(new Rectangle((int) (5 * tileWidth),
				(int) (12 * tileHeight), (int) (7 * tileWidth),
				(int) tileHeight));
		queue.setPreferredSize(new Dimension((int) tileWidth,
				(int) (7 * tileHeight)));
		if (newOne) {
			queues.set(5, queue);
			layeredPane.add(queue, QUEUE_LAYER);
		}
		for (int i = 0; i < 5; i++) {
			List<Integer> ints = gameState.getStores()[i].getQueue();
			List<JPawn> pawnList = pawns.get(i);
			int isize = ints.size();
			int psize = pawnList.size();
			while (isize < psize) {
				layeredPane.remove(pawnList.get(--psize));
				pawnList.remove(psize);
			}
			double x = (3 * i + 0.25) * tileWidth;
			double y = (3 * tileHeight);
			double skip = 6 * tileHeight / (isize - 1);
			if (skip > tileHeight * 0.75)
				skip = tileHeight * 0.75;
			while (isize > psize) {
				JPawn pawn = new JPawn(game, ProductType.values()[i],
						ints.get(psize), 0, tileHeight * 2, creator, false);
				pawn.setBounds((int) (x), (int) (y + psize * skip),
						(int) (tileHeight), (int) (2 * tileHeight));
				pawnList.add(pawn);
				layeredPane.add(pawn, new Integer(PAWN_LAYER0 + psize++));
			}
			for (int j = 0; j < isize; j++) {
				JPawn pawn = pawnList.get(j);

				if (resetBounds)
					pawn.setBounds((int) (x), (int) (y + psize * skip),
							(int) (tileHeight), (int) (2 * tileHeight));
				boolean changed = false;
				if (pawn.getPlayerId() != ints.get(j)) {
					pawn.setPlayerId(ints.get(j));
					changed = true;
				}
				pawn.setPosition(j);
				pawn.setPlayerId(ints.get(j));
				if (changed)
					pawn.repaint();
				pawn.setLocation((int) x, (int) (y + j * skip));
			}

		}
		if(trader == null){
			trader = new JPawn(game, null, 0, 0, tileHeight * 2, null, true);
			trader.setBounds((int) (0.25 * tileWidth + (gameState.getDayNumber() % 5) * tileWidth), (int)(10.5 * tileHeight), (int)(tileWidth / 2), (int)(tileHeight * 2));
			layeredPane.add(trader, new Integer(PAWN_LAYER0 + 30));
			trader.repaint();
		}
		trader.setBounds((int) (0.25 * tileWidth + (gameState.getDayNumber() % 5) * tileWidth), (int)(10.5 * tileHeight), (int)(tileWidth / 2), (int)(tileHeight * 2));
		trader.setLocation((int) (0.25 * tileWidth + (gameState.getDayNumber() % 5) * tileWidth), (int)(10.5 * tileHeight));
		
		List<Integer> ints = gameState.getStore(null).getQueue();
		List<JPawn> pawnList = outdoorMarket;
		int isize = ints.size();
		int psize = pawnList.size();
		while (isize < psize) {
			layeredPane.remove(pawnList.get(--psize));
			pawnList.remove(psize);
		}
		double x = (5.25) * tileWidth;
		double y = (11 * tileHeight);
		double skip = 6 * tileWidth / (isize - 1);
		if (skip > tileWidth * 0.55)
			skip = tileWidth * 0.55;
		while (isize > psize) {
			JPawn pawn = new JPawn(game, null, ints.get(psize), 0,
					tileHeight * 2, creator, false);
			pawn.setBounds((int) (x + psize * skip), (int) (y),
					(int) (tileHeight), (int) (2 * tileHeight));
			pawnList.add(pawn);
			layeredPane.add(pawn, new Integer(PAWN_LAYER0 + psize++));
		}
		for (int j = 0; j < isize; j++) {
			JPawn pawn = pawnList.get(j);

			if (resetBounds)
				pawn.setBounds((int) (x + psize * skip), (int) (y),
						(int) (tileHeight), (int) (2 * tileHeight));
			boolean changed = false;
			if (pawn.getPlayerId() != ints.get(j)) {
				pawn.setPlayerId(ints.get(j));
				changed = true;
			}
			pawn.setPosition(j);
			pawn.setPlayerId(ints.get(j));
			if (changed)
				pawn.repaint();
			pawn.setLocation((int) (x + j * skip), (int) y);
		}

		ind = 0;
		for (int i = 0; i < 5; i++) {
			boolean rep = false;
			ArrayList<JProductSquare> list = products.get(i);
			int count = 0;
			double side = (1.5 * tileHeight);
			Store store = game.getGameState().getStores()[i];
			for (ProductType type : ProductType.values())
				if (store.getNumberOf(type) > 0)
					count++;
			int lsize = list.size();
			if (count != lsize)
				rep = true;
			while (count < lsize) {
				layeredPane.remove(list.get(--lsize));
				list.remove(lsize);
			}
			while (count > lsize) {
				JProductSquare square = new JProductSquare(game,
						ProductType.values()[0], 0, ProductType.values()[i],
						creator);
				square.setBounds(
						(int) (tileWidth * (3 * ind + 0.5) + (2 - lsize % 3)
								* side),
						(int) (tileHeight + side * (lsize / 3)), (int) side,
						(int) side);
				lsize++;
				list.add(square);
				layeredPane.add(square, PRODUCT_LAYER);
			}
			int pr = -1;
			for (int j = 0; j < count; j++) {
				while (store.getNumberOf(ProductType.values()[++pr]) == 0)
					;
				JProductSquare square = list.get(j);
				square.setBounds(
						(int) (tileWidth * (3 * ind + 0.5) + (2 - j % 3) * side),
						(int) (tileHeight + side * (j / 3)), (int) side,
						(int) side);
				if (square
						.setAmount(store.getNumberOf(ProductType.values()[pr])))
					rep = true;
				if (square.setType(ProductType.values()[pr]))
					rep = true;
				square.repaint();
				square.setLocation(
						(int) (tileWidth * (3 * ind + 0.5) + (2 - j % 3) * side),
						(int) (tileHeight + side * (j / 3)));
			}
			if (rep)
				stores.get(i).repaint();
			ind++;
		}
		boolean rep = false;
		ArrayList<JProductSquare> list = outdoorProducts;
		int count = 0;
		double side = (1.5 * tileHeight);
		Store store = game.getGameState().getStore(null);
		for (ProductType type : ProductType.values())
			if (store.getNumberOf(type) > 0) {
				count++;
			}
		int lsize = list.size();
		if (count != lsize)
			rep = true;
		while (count < lsize) {
			layeredPane.remove(list.get(--lsize));
			list.remove(lsize);
		}
		while (count > lsize) {
			JProductSquare square = new JProductSquare(game, null, 0, null,
					creator);
			square.setBounds((int) (3 * tileWidth * ind + lsize++ * side),
					(int) (tileHeight), (int) side, (int) side);
			list.add(square);
			layeredPane.add(square, PRODUCT_LAYER);
		}
		int pr = -1;
		for (int j = 0; j < count; j++) {
			while (store.getNumberOf(ProductType.values()[++pr]) == 0)
				;
			JProductSquare square = list.get(j);
			square.setBounds((int) (3 * tileWidth * ind + j * side),
					(int) (tileHeight), (int) side, (int) side);
			if (square.setAmount(store.getNumberOf(ProductType.values()[pr])))
				rep = true;
			if (square.setType(ProductType.values()[pr]))
				rep = true;
			square.repaint();
			square.setLocation((int) (0.125 * tileWidth + pr * tileWidth),
					(int) (12 * tileHeight));
		}
		repaint();

	}

	public void componentShown(ComponentEvent arg0) {
	}

}
