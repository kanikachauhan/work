package pathFinder;

import map.Coordinate;
import map.PathMap;
import java.util.*;


public class DijkstraPathFinder implements PathFinder {
	// TODO: You might need to implement some attributes
	Coordinate cells[][] = null;
	int cellss[][] = null;
	List<Coordinate> originCells = null, destCells = null, waypointCells = null;
	PathMap map;
	Map<Coordinate, List<Coordinate>> neighbourMap = new LinkedHashMap<>();
	int srcx = -1,srcy = -1,destx=-1,desty=-1;
	int[][] distances =null;
	/**
	 * initialize map,cells, mapping origin and destination and terrain points.
	 * @param map
	 */
	public DijkstraPathFinder(PathMap map) {
		this.map = map;
		cells = map.cells;
		originCells = map.originCells;
		destCells = map.destCells;
		waypointCells = map.waypointCells;
		cellss = new int[map.sizeR][map.sizeC];
	} 
	/**
	 * create a matrix representing the grid of coordinates.
	 * creating a distance matrix 2d which will store the distance of each point.
	 * it can be either infinity or equal to cost of terrain depending upon the condition of the point.
	 * the points which are not passable are all skipped during the evaulation of the distance matrix in the code.
	 * @param sourcePoint
	 * @param destinationPoint
	 */
	private void initialize(Coordinate sourcePoint,Coordinate destinationPoint) {
		distances = new int[map.sizeR][map.sizeC];
		for (int i = 0; i < map.sizeR; i++) {
			for (int j = 0; j < map.sizeC; j++) {
				Coordinate cordinate = cells[i][j];
				if (!cordinate.getImpassable()) {
					cellss[i][j]=0;
				}else {
					cellss[i][j]=1;
				}
				if(cordinate.getTerrainCost()!=1) {
					distances[i][j] = cordinate.getTerrainCost();
				}else {
					distances[i][j] = Integer.MAX_VALUE;
				}
				
				if((cordinate.getColumn()==sourcePoint.getColumn()) && (cordinate.getRow()==sourcePoint.getRow())){
					srcx = i;
					srcy = j;
				}
				if((cordinate.getColumn()==destinationPoint.getColumn()) && (cordinate.getRow()==destinationPoint.getRow())){
					destx = i;
					desty = j;
				}
			}
		}
		cellss[srcx][srcy] = -1;
		cellss[destx][desty] = -1;
	}
	
	
	/**
	 * driver program to calculate the shortest path in a 2D array.
	 * cells which are marked 1 are regarded as obstacles and not used in evaluation of the shortest path.
	 * distance is evaluated from source node at each step and minimum is tracked.
	 * @param map
	 * @param start
	 * @param end
	 * @param path
	 * @return
	 */
	public int shortestPath(int[][] map, Coordinate start, Coordinate end, List<Coordinate> path) {
		int distance = 0;
		List<Coordinate> currentCells = Arrays.asList(start);
		while (distances[end.getRow()][end.getColumn()] == Integer.MAX_VALUE && !currentCells.isEmpty()) {
			List<Coordinate> nextCells = new ArrayList<>();
			for (Coordinate cell : currentCells) {
				if (distances[cell.getRow()][cell.getColumn()] == Integer.MAX_VALUE && map[cell.getRow()][cell.getColumn()]!=1) {
					distances[cell.getRow()][cell.getColumn()] = distance;
					addNeighbour(cell, nextCells, map.length, map[0].length);
				}
			}
			currentCells = nextCells;
			distance++;
		}
		if (distances[end.getRow()][end.getColumn()] < Integer.MAX_VALUE) {
			Coordinate cell = end;
			path.add(end);
			for (int d = distances[end.getRow()][end.getColumn()] - 1; d >= 0; d--) {
				cell = getNeighbour(cell, d, distances);
				path.add(cell);
			}
		}
		return distances[end.getRow()][end.getColumn()];
	}
	/**
	 * driver code to evaluate the neighbours of a coordinate.
	 * @param coordinate
	 * @param list
	 * @param maxRow
	 * @param maxCol
	 */
	private static void addNeighbour(Coordinate coordinate, List<Coordinate> list, int maxRow, int maxCol) {
		int[][] ds = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] d : ds) {
			int row = coordinate.getRow() + d[0];
			int col = coordinate.getColumn() + d[1];
			if (validateRowCol(row, col, maxRow, maxCol))
				list.add(new Coordinate(row, col));
		}
	}

	/**
	 * getting individual neighbour from the list of neighbours.
	 * @param coordinate
	 * @param distance
	 * @param distances
	 * @return
	 */
	private static Coordinate getNeighbour(Coordinate coordinate, int distance, int[][] distances) {
		int[][] ds = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] d : ds) {
			int row = coordinate.getRow() + d[0];
			int col = coordinate.getColumn() + d[1];
			if (validateRowCol(row, col, distances.length, distances[0].length) && distances[row][col] == distance)
				return new Coordinate(row, col);
		}
		return null;
	}
	
	private static boolean validateRowCol(int row, int col, int maxRow, int maxCol) {
		return row >= 0 && row < maxRow && col >= 0 && col < maxCol;
	}
	
	/**
	 * for every source and destination calculate shortest path and find the shortest path.
	 */
	@Override
	public List<Coordinate> findPath() {
		List<Coordinate> path = new LinkedList<Coordinate>();
		List<Coordinate> resultPath = null;
		int min = Integer.MAX_VALUE;
		for(int i=0;i<originCells.size();i++) {
			for(int j=0;j<destCells.size();j++) {
				initialize(originCells.get(i), destCells.get(j));
				int temp = shortestPath(cellss, originCells.get(i), destCells.get(j), path);
				if(temp<min) {
					min = temp;
					resultPath = path;
				}
				path = new LinkedList<Coordinate>();
			}
		}
		
		return resultPath;
	}

	@Override
	public int coordinatesExplored() {
		// TODO: Implement (optional)

		// placeholder
		return findPath().size();
	} // end of cellsExplored()

	
	
	
} // end of class DijsktraPathFinder
