package edu.research.fingerprint;

import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test suite for the ShapeFactory class.
 */
public class ShapeFactoryTest {
    
    private static final double EPSILON = 1e-6;
    
    @Test
    public void testCreateSquare() {
        PointSet square = ShapeFactory.createSquare(2.0);
        assertEquals(4, square.size());
        assertEquals(2, square.getDimension());
    }
    
    @Test
    public void testCreateRegularPolygon() {
        PointSet triangle = ShapeFactory.createRegularPolygon(3, 1.0);
        assertEquals(3, triangle.size());
        assertEquals(2, triangle.getDimension());
        
        PointSet pentagon = ShapeFactory.createRegularPolygon(5, 1.0);
        assertEquals(5, pentagon.size());
    }
    
    @Test
    public void testCreateCube() {
        PointSet cube = ShapeFactory.createCube(2.0);
        assertEquals(8, cube.size()); // 8 vertices
        assertEquals(3, cube.getDimension());
    }
    
    @Test
    public void testCreateTetrahedron() {
        PointSet tetrahedron = ShapeFactory.createTetrahedron(1.0);
        assertEquals(4, tetrahedron.size());
        assertEquals(3, tetrahedron.getDimension());
    }
    
    @Test
    public void testCreateSphereSurface() {
        PointSet sphere = ShapeFactory.createSphereSurface(100, 1.0);
        assertEquals(100, sphere.size());
        assertEquals(3, sphere.getDimension());
        
        // All points should be approximately on the surface (radius 1.0)
        for (int i = 0; i < sphere.size(); i++) {
            double[] point = sphere.getPoint(i);
            double distance = Math.sqrt(point[0]*point[0] + point[1]*point[1] + point[2]*point[2]);
            assertEquals(1.0, distance, 0.01); // Within 1% tolerance
        }
    }
    
    @Test
    public void testCreateRandomPointSet2D() {
        Random random = new Random(42);
        PointSet ps = ShapeFactory.createRandomPointSet(50, 2, random);
        assertEquals(50, ps.size());
        assertEquals(2, ps.getDimension());
    }
    
    @Test
    public void testCreateRandomPointSet5D() {
        Random random = new Random(42);
        PointSet ps = ShapeFactory.createRandomPointSet(30, 5, random);
        assertEquals(30, ps.size());
        assertEquals(5, ps.getDimension());
    }
    
    @Test
    public void testCreateRandomPointSet10D() {
        Random random = new Random(42);
        PointSet ps = ShapeFactory.createRandomPointSet(25, 10, random);
        assertEquals(25, ps.size());
        assertEquals(10, ps.getDimension());
    }
    
    @Test
    public void testCreateHypercube4D() {
        PointSet hypercube = ShapeFactory.createHypercube(4, 2.0);
        assertEquals(16, hypercube.size()); // 2^4 vertices
        assertEquals(4, hypercube.getDimension());
    }
    
    @Test
    public void testCreateHypercube5D() {
        PointSet hypercube = ShapeFactory.createHypercube(5, 1.0);
        assertEquals(32, hypercube.size()); // 2^5 vertices
        assertEquals(5, hypercube.getDimension());
    }
    
    @Test
    public void testCreateLineSegment() {
        PointSet line = ShapeFactory.createLineSegment(10, 3, 1.0);
        assertEquals(10, line.size());
        assertEquals(3, line.getDimension());
    }
    
    @Test
    public void testCreateLineSegment7D() {
        PointSet line = ShapeFactory.createLineSegment(15, 7, 2.0);
        assertEquals(15, line.size());
        assertEquals(7, line.getDimension());
    }
    
    @Test
    public void testCreateRandomInSphere() {
        Random random = new Random(42);
        PointSet ps = ShapeFactory.createRandomInSphere(100, 3, 1.0, random);
        assertEquals(100, ps.size());
        assertEquals(3, ps.getDimension());
        
        // All points should be within radius 1.0
        for (int i = 0; i < ps.size(); i++) {
            double[] point = ps.getPoint(i);
            double distance = Math.sqrt(point[0]*point[0] + point[1]*point[1] + point[2]*point[2]);
            assertTrue(distance <= 1.0 + EPSILON);
        }
    }
    
    @Test
    public void testCreateRandomInSphere5D() {
        Random random = new Random(42);
        PointSet ps = ShapeFactory.createRandomInSphere(50, 5, 2.0, random);
        assertEquals(50, ps.size());
        assertEquals(5, ps.getDimension());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPolygon() {
        ShapeFactory.createRegularPolygon(2, 1.0); // Too few vertices
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDimension() {
        Random random = new Random(42);
        ShapeFactory.createRandomPointSet(10, 0, random); // Invalid dimension
    }
}
