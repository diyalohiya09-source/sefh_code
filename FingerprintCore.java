import java.util.ArrayList;
import java.util.List;

/**
 * FingerprintCore: A class for modeling high-dimensional metric spaces.
 * 
 * This implementation supports N-dimensional points (2D, 3D, and beyond) and provides
 * methods to compute pairwise distances and diameter in N-dimensional Euclidean space.
 */
public class FingerprintCore {
    
    private List<double[]> points;
    private Integer dimension;
    
    /**
     * Initialize an empty FingerprintCore instance.
     */
    public FingerprintCore() {
        this.points = new ArrayList<>();
        this.dimension = null;
    }
    
    /**
     * Add an N-dimensional point to the collection.
     * 
     * @param point An array of coordinates representing an N-dimensional point.
     *              For example: {x, y} for 2D, {x, y, z} for 3D, etc.
     * @throws IllegalArgumentException If the point's dimension doesn't match existing points
     *                                  or if the point is empty or null.
     * 
     * Example:
     * <pre>
     * FingerprintCore core = new FingerprintCore();
     * core.addPoint(new double[]{1.0, 2.0});     // 2D point
     * core.addPoint(new double[]{3.0, 4.0});     // Another 2D point
     * core.addPoint(new double[]{5.0, 6.0, 7.0}); // Throws IllegalArgumentException - dimension mismatch
     * </pre>
     */
    public void addPoint(double[] point) {
        // Validate input
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }
        
        if (point.length == 0) {
            throw new IllegalArgumentException("Point must have at least one dimension");
        }
        
        // Check dimension consistency
        if (this.dimension == null) {
            // First point sets the dimension
            this.dimension = point.length;
        } else if (point.length != this.dimension) {
            throw new IllegalArgumentException(
                String.format("Point dimension %d does not match expected dimension %d",
                    point.length, this.dimension)
            );
        }
        
        // Create a copy of the point to avoid external modifications
        double[] pointCopy = new double[point.length];
        System.arraycopy(point, 0, pointCopy, 0, point.length);
        this.points.add(pointCopy);
    }
    
    /**
     * Compute pairwise Euclidean distances between all points in N-dimensional space.
     * 
     * The Euclidean distance between two N-dimensional points p and q is:
     * d(p, q) = sqrt(sum((p_i - q_i)^2 for i in 1..N))
     * 
     * @return A 2D array (matrix) where element [i][j] represents the distance
     *         between points[i] and points[j]. The matrix is symmetric with
     *         zeros on the diagonal.
     * 
     * Example:
     * <pre>
     * FingerprintCore core = new FingerprintCore();
     * core.addPoint(new double[]{0.0, 0.0});
     * core.addPoint(new double[]{3.0, 4.0});
     * double[][] distances = core.computePairwiseDistances();
     * // distances[0][1] == 5.0 (distance from point 0 to point 1)
     * </pre>
     */
    public double[][] computePairwiseDistances() {
        int n = this.points.size();
        double[][] distances = new double[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Compute N-dimensional Euclidean distance
                double dist = euclideanDistance(this.points.get(i), this.points.get(j));
                distances[i][j] = dist;
                distances[j][i] = dist;  // Symmetric matrix
            }
        }
        
        return distances;
    }
    
    /**
     * Compute the diameter of the point set in N-dimensional space.
     * 
     * The diameter is defined as the maximum pairwise distance between any
     * two points in the collection.
     * 
     * @return The maximum distance between any two points. Returns 0.0 if there
     *         are fewer than 2 points.
     * 
     * Example:
     * <pre>
     * FingerprintCore core = new FingerprintCore();
     * core.addPoint(new double[]{0.0, 0.0, 0.0});
     * core.addPoint(new double[]{1.0, 1.0, 1.0});
     * core.addPoint(new double[]{2.0, 2.0, 2.0});
     * double diameter = core.computeDiameter();
     * // diameter == 3.4641016151377544 (sqrt(12))
     * </pre>
     */
    public double computeDiameter() {
        if (this.points.size() < 2) {
            return 0.0;
        }
        
        double maxDistance = 0.0;
        int n = this.points.size();
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = euclideanDistance(this.points.get(i), this.points.get(j));
                maxDistance = Math.max(maxDistance, dist);
            }
        }
        
        return maxDistance;
    }
    
    /**
     * Compute the Euclidean distance between two N-dimensional points.
     * 
     * @param p1 First N-dimensional point
     * @param p2 Second N-dimensional point
     * @return The Euclidean distance between p1 and p2
     */
    private double euclideanDistance(double[] p1, double[] p2) {
        double sum = 0.0;
        for (int i = 0; i < p1.length; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
    
    /**
     * Get all points in the collection.
     * 
     * @return A list of all N-dimensional points (as copies to prevent external modification).
     */
    public List<double[]> getPoints() {
        List<double[]> pointsCopy = new ArrayList<>();
        for (double[] point : this.points) {
            double[] copy = new double[point.length];
            System.arraycopy(point, 0, copy, 0, point.length);
            pointsCopy.add(copy);
        }
        return pointsCopy;
    }
    
    /**
     * Get the dimension of points in this collection.
     * 
     * @return The dimension of points, or null if no points have been added.
     */
    public Integer getDimension() {
        return this.dimension;
    }
    
    /**
     * Clear all points from the collection.
     */
    public void clear() {
        this.points.clear();
        this.dimension = null;
    }
}
