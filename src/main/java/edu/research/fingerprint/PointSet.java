package edu.research.fingerprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a finite point set in N-dimensional Euclidean space.
 * Each object is an unordered collection of points, independent of coordinate labels.
 */
public class PointSet {
    private final List<double[]> points;
    private final int dimension;
    
    /**
     * Creates a point set from a list of points.
     * @param points List of points, where each point is a double array of coordinates
     * @throws IllegalArgumentException if points have inconsistent dimensions
     */
    public PointSet(List<double[]> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Point set cannot be null or empty");
        }
        
        this.dimension = points.get(0).length;
        
        // Validate all points have the same dimension
        for (double[] point : points) {
            if (point.length != dimension) {
                throw new IllegalArgumentException("All points must have the same dimension");
            }
        }
        
        this.points = new ArrayList<>(points);
    }
    
    /**
     * Gets the number of points in the set.
     */
    public int size() {
        return points.size();
    }
    
    /**
     * Gets the dimension of the space.
     */
    public int getDimension() {
        return dimension;
    }
    
    /**
     * Gets a copy of the point at the specified index.
     */
    public double[] getPoint(int index) {
        return points.get(index).clone();
    }
    
    /**
     * Gets all points as a list.
     */
    public List<double[]> getPoints() {
        List<double[]> result = new ArrayList<>();
        for (double[] point : points) {
            result.add(point.clone());
        }
        return result;
    }
    
    /**
     * Computes Euclidean distance between two points.
     */
    public static double euclideanDistance(double[] p1, double[] p2) {
        if (p1.length != p2.length) {
            throw new IllegalArgumentException("Points must have the same dimension");
        }
        
        double sumSquared = 0.0;
        for (int i = 0; i < p1.length; i++) {
            double diff = p1[i] - p2[i];
            sumSquared += diff * diff;
        }
        return Math.sqrt(sumSquared);
    }
    
    /**
     * Computes all pairwise distances in the point set.
     * @return Array of all pairwise distances
     */
    public double[] computePairwiseDistances() {
        int n = points.size();
        int numPairs = n * (n - 1) / 2;
        double[] distances = new double[numPairs];
        
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                distances[idx++] = euclideanDistance(points.get(i), points.get(j));
            }
        }
        
        return distances;
    }
    
    /**
     * Computes the diameter (maximum pairwise distance) of the point set.
     */
    public double computeDiameter() {
        double[] distances = computePairwiseDistances();
        double maxDist = 0.0;
        for (double d : distances) {
            if (d > maxDist) {
                maxDist = d;
            }
        }
        return maxDist;
    }
    
    /**
     * Applies Gaussian noise (jitter) to all points.
     * @param noiseMagnitude Standard deviation of the Gaussian noise
     * @param random Random number generator for reproducibility
     * @return A new PointSet with noise applied
     */
    public PointSet applyJitter(double noiseMagnitude, Random random) {
        List<double[]> noisyPoints = new ArrayList<>();
        
        for (double[] point : points) {
            double[] noisyPoint = new double[dimension];
            for (int i = 0; i < dimension; i++) {
                noisyPoint[i] = point[i] + random.nextGaussian() * noiseMagnitude;
            }
            noisyPoints.add(noisyPoint);
        }
        
        return new PointSet(noisyPoints);
    }
    
    /**
     * Simulates partial observability by randomly removing points.
     * @param removalFraction Fraction of points to remove (0.0 to 1.0)
     * @param random Random number generator for reproducibility
     * @return A new PointSet with some points removed
     */
    public PointSet simulatePartialObservability(double removalFraction, Random random) {
        if (removalFraction < 0.0 || removalFraction >= 1.0) {
            throw new IllegalArgumentException("Removal fraction must be in [0, 1)");
        }
        
        int numToKeep = (int) Math.ceil(points.size() * (1.0 - removalFraction));
        if (numToKeep < 1) {
            numToKeep = 1; // Keep at least one point
        }
        
        List<double[]> shuffled = new ArrayList<>(points);
        // Shuffle using Fisher-Yates algorithm
        for (int i = shuffled.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            double[] temp = shuffled.get(i);
            shuffled.set(i, shuffled.get(j));
            shuffled.set(j, temp);
        }
        
        return new PointSet(shuffled.subList(0, numToKeep));
    }
    
    /**
     * Adds outlier points to the point set.
     * @param numOutliers Number of outliers to add
     * @param outlierScale Scale factor for outlier positioning relative to diameter
     * @param random Random number generator for reproducibility
     * @return A new PointSet with outliers added
     */
    public PointSet addOutliers(int numOutliers, double outlierScale, Random random) {
        if (numOutliers < 0) {
            throw new IllegalArgumentException("Number of outliers must be non-negative");
        }
        
        List<double[]> augmentedPoints = new ArrayList<>(points);
        double diameter = computeDiameter();
        
        // Compute centroid
        double[] centroid = new double[dimension];
        for (double[] point : points) {
            for (int i = 0; i < dimension; i++) {
                centroid[i] += point[i];
            }
        }
        for (int i = 0; i < dimension; i++) {
            centroid[i] /= points.size();
        }
        
        // Generate outliers far from the centroid
        for (int i = 0; i < numOutliers; i++) {
            double[] outlier = new double[dimension];
            for (int j = 0; j < dimension; j++) {
                outlier[j] = centroid[j] + (random.nextDouble() * 2 - 1) * diameter * outlierScale;
            }
            augmentedPoints.add(outlier);
        }
        
        return new PointSet(augmentedPoints);
    }
    
    @Override
    public String toString() {
        return String.format("PointSet[dimension=%d, size=%d]", dimension, points.size());
    }
}
