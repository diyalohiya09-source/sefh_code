package edu.research.fingerprint;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test suite for the PointSet class.
 */
public class PointSetTest {
    
    private static final double EPSILON = 1e-6;
    
    @Test
    public void testPointSetCreation2D() {
        List<double[]> points = new ArrayList<>();
        points.add(new double[] {0.0, 0.0});
        points.add(new double[] {1.0, 0.0});
        points.add(new double[] {0.0, 1.0});
        
        PointSet ps = new PointSet(points);
        assertEquals(3, ps.size());
        assertEquals(2, ps.getDimension());
    }
    
    @Test
    public void testPointSetCreation3D() {
        List<double[]> points = new ArrayList<>();
        points.add(new double[] {0.0, 0.0, 0.0});
        points.add(new double[] {1.0, 0.0, 0.0});
        points.add(new double[] {0.0, 1.0, 0.0});
        points.add(new double[] {0.0, 0.0, 1.0});
        
        PointSet ps = new PointSet(points);
        assertEquals(4, ps.size());
        assertEquals(3, ps.getDimension());
    }
    
    @Test
    public void testPointSetCreationHighDimensional() {
        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double[] point = new double[10];
            for (int j = 0; j < 10; j++) {
                point[j] = i + j;
            }
            points.add(point);
        }
        
        PointSet ps = new PointSet(points);
        assertEquals(10, ps.size());
        assertEquals(10, ps.getDimension());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentDimensions() {
        List<double[]> points = new ArrayList<>();
        points.add(new double[] {0.0, 0.0});
        points.add(new double[] {1.0, 0.0, 0.0}); // Different dimension
        new PointSet(points);
    }
    
    @Test
    public void testEuclideanDistance() {
        double[] p1 = {0.0, 0.0};
        double[] p2 = {3.0, 4.0};
        double dist = PointSet.euclideanDistance(p1, p2);
        assertEquals(5.0, dist, EPSILON);
    }
    
    @Test
    public void testEuclideanDistance3D() {
        double[] p1 = {0.0, 0.0, 0.0};
        double[] p2 = {1.0, 2.0, 2.0};
        double dist = PointSet.euclideanDistance(p1, p2);
        assertEquals(3.0, dist, EPSILON);
    }
    
    @Test
    public void testDiameter() {
        List<double[]> points = new ArrayList<>();
        points.add(new double[] {0.0, 0.0});
        points.add(new double[] {1.0, 0.0});
        points.add(new double[] {0.0, 1.0});
        
        PointSet ps = new PointSet(points);
        double diameter = ps.computeDiameter();
        assertEquals(Math.sqrt(2.0), diameter, EPSILON);
    }
    
    @Test
    public void testJitter() {
        Random random = new Random(42);
        PointSet original = ShapeFactory.createSquare(1.0);
        PointSet jittered = original.applyJitter(0.1, random);
        
        assertEquals(original.size(), jittered.size());
        assertEquals(original.getDimension(), jittered.getDimension());
        
        // Points should be different but close
        for (int i = 0; i < original.size(); i++) {
            double[] p1 = original.getPoint(i);
            double[] p2 = jittered.getPoint(i);
            double dist = PointSet.euclideanDistance(p1, p2);
            assertTrue(dist > 0); // Should be different
            assertTrue(dist < 1.0); // But not too far
        }
    }
    
    @Test
    public void testPartialObservability() {
        Random random = new Random(42);
        PointSet original = ShapeFactory.createCube(1.0);
        double removalFraction = 0.5;
        PointSet partial = original.simulatePartialObservability(removalFraction, random);
        
        int expectedSize = (int) Math.ceil(original.size() * (1.0 - removalFraction));
        assertEquals(expectedSize, partial.size());
        assertEquals(original.getDimension(), partial.getDimension());
    }
    
    @Test
    public void testAddOutliers() {
        Random random = new Random(42);
        PointSet original = ShapeFactory.createSquare(1.0);
        int numOutliers = 3;
        PointSet withOutliers = original.addOutliers(numOutliers, 2.0, random);
        
        assertEquals(original.size() + numOutliers, withOutliers.size());
        assertEquals(original.getDimension(), withOutliers.getDimension());
    }
    
    @Test
    public void testJitterIn3D() {
        Random random = new Random(42);
        PointSet cube = ShapeFactory.createCube(1.0);
        PointSet jittered = cube.applyJitter(0.05, random);
        
        assertEquals(cube.size(), jittered.size());
        assertEquals(3, jittered.getDimension());
    }
    
    @Test
    public void testPartialObservabilityIn5D() {
        Random random = new Random(42);
        PointSet random5D = ShapeFactory.createRandomPointSet(20, 5, random);
        PointSet partial = random5D.simulatePartialObservability(0.4, random);
        
        assertEquals(5, partial.getDimension());
        assertTrue(partial.size() < random5D.size());
        assertTrue(partial.size() >= 1); // At least one point
    }
}
