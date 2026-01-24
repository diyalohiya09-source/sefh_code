package edu.research.fingerprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Factory for creating canonical geometric shapes and synthetic point sets.
 * Supports 2D, 3D, and higher-dimensional spaces.
 */
public class ShapeFactory {
    
    /**
     * Creates a regular polygon in 2D space.
     * @param numPoints Number of vertices
     * @param radius Radius of the circumscribed circle
     * @return A PointSet representing the polygon
     */
    public static PointSet createRegularPolygon(int numPoints, double radius) {
        if (numPoints < 3) {
            throw new IllegalArgumentException("Polygon must have at least 3 points");
        }
        
        List<double[]> points = new ArrayList<>();
        double angleStep = 2 * Math.PI / numPoints;
        
        for (int i = 0; i < numPoints; i++) {
            double angle = i * angleStep;
            double[] point = new double[] {
                radius * Math.cos(angle),
                radius * Math.sin(angle)
            };
            points.add(point);
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a square in 2D space.
     * @param sideLength Side length of the square
     * @return A PointSet representing the square
     */
    public static PointSet createSquare(double sideLength) {
        double half = sideLength / 2.0;
        List<double[]> points = new ArrayList<>();
        points.add(new double[] {-half, -half});
        points.add(new double[] {half, -half});
        points.add(new double[] {half, half});
        points.add(new double[] {-half, half});
        return new PointSet(points);
    }
    
    /**
     * Creates a cube in 3D space.
     * @param sideLength Side length of the cube
     * @return A PointSet representing the cube vertices
     */
    public static PointSet createCube(double sideLength) {
        double half = sideLength / 2.0;
        List<double[]> points = new ArrayList<>();
        
        // Add all 8 vertices of the cube
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    points.add(new double[] {
                        (i == 0 ? -half : half),
                        (j == 0 ? -half : half),
                        (k == 0 ? -half : half)
                    });
                }
            }
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a tetrahedron in 3D space.
     * @param sideLength Side length of the tetrahedron
     * @return A PointSet representing the tetrahedron vertices
     */
    public static PointSet createTetrahedron(double sideLength) {
        List<double[]> points = new ArrayList<>();
        
        // Regular tetrahedron vertices
        double a = sideLength / Math.sqrt(2.0);
        points.add(new double[] {a, 0, -a / Math.sqrt(2.0)});
        points.add(new double[] {-a, 0, -a / Math.sqrt(2.0)});
        points.add(new double[] {0, a, a / Math.sqrt(2.0)});
        points.add(new double[] {0, -a, a / Math.sqrt(2.0)});
        
        return new PointSet(points);
    }
    
    /**
     * Creates a sphere surface sampling in 3D space using Fibonacci sphere algorithm.
     * @param numPoints Number of points to sample
     * @param radius Radius of the sphere
     * @return A PointSet representing points on the sphere surface
     */
    public static PointSet createSphereSurface(int numPoints, double radius) {
        List<double[]> points = new ArrayList<>();
        double goldenRatio = (1.0 + Math.sqrt(5.0)) / 2.0;
        double angleIncrement = 2.0 * Math.PI * goldenRatio;
        
        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            double inclination = Math.acos(1.0 - 2.0 * t);
            double azimuth = angleIncrement * i;
            
            double x = radius * Math.sin(inclination) * Math.cos(azimuth);
            double y = radius * Math.sin(inclination) * Math.sin(azimuth);
            double z = radius * Math.cos(inclination);
            
            points.add(new double[] {x, y, z});
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a random point set in a unit hypercube of arbitrary dimension.
     * @param numPoints Number of points
     * @param dimension Dimension of the space
     * @param random Random number generator for reproducibility
     * @return A PointSet of random points
     */
    public static PointSet createRandomPointSet(int numPoints, int dimension, Random random) {
        if (numPoints < 1) {
            throw new IllegalArgumentException("Must have at least 1 point");
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Dimension must be at least 1");
        }
        
        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            double[] point = new double[dimension];
            for (int j = 0; j < dimension; j++) {
                point[j] = random.nextDouble();
            }
            points.add(point);
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a random point set in a unit hypersphere of arbitrary dimension.
     * @param numPoints Number of points
     * @param dimension Dimension of the space
     * @param radius Radius of the hypersphere
     * @param random Random number generator for reproducibility
     * @return A PointSet of random points inside the hypersphere
     */
    public static PointSet createRandomInSphere(int numPoints, int dimension, double radius, Random random) {
        if (numPoints < 1) {
            throw new IllegalArgumentException("Must have at least 1 point");
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Dimension must be at least 1");
        }
        
        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            double[] point = new double[dimension];
            
            // Generate random direction
            double normSquared = 0.0;
            for (int j = 0; j < dimension; j++) {
                point[j] = random.nextGaussian();
                normSquared += point[j] * point[j];
            }
            double norm = Math.sqrt(normSquared);
            
            // Generate random radius (uniform distribution in ball)
            double r = radius * Math.pow(random.nextDouble(), 1.0 / dimension);
            
            // Normalize and scale
            for (int j = 0; j < dimension; j++) {
                point[j] = point[j] / norm * r;
            }
            
            points.add(point);
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a line segment in arbitrary dimension.
     * @param numPoints Number of points on the line
     * @param dimension Dimension of the space
     * @param length Length of the line segment
     * @return A PointSet of collinear points
     */
    public static PointSet createLineSegment(int numPoints, int dimension, double length) {
        if (numPoints < 2) {
            throw new IllegalArgumentException("Line segment must have at least 2 points");
        }
        
        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            double[] point = new double[dimension];
            point[0] = t * length; // Points along the first axis
            // All other coordinates remain 0
            points.add(point);
        }
        
        return new PointSet(points);
    }
    
    /**
     * Creates a hypercube in arbitrary dimension.
     * @param dimension Dimension of the space
     * @param sideLength Side length of the hypercube
     * @return A PointSet representing all vertices of the hypercube
     */
    public static PointSet createHypercube(int dimension, double sideLength) {
        if (dimension < 1) {
            throw new IllegalArgumentException("Dimension must be at least 1");
        }
        
        int numVertices = (int) Math.pow(2, dimension);
        List<double[]> points = new ArrayList<>();
        double half = sideLength / 2.0;
        
        for (int i = 0; i < numVertices; i++) {
            double[] point = new double[dimension];
            for (int j = 0; j < dimension; j++) {
                // Check if j-th bit is set
                point[j] = ((i & (1 << j)) != 0) ? half : -half;
            }
            points.add(point);
        }
        
        return new PointSet(points);
    }
}
