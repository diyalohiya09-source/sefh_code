import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for FingerprintCore class.
 * 
 * Tests N-dimensional point support including 2D, 3D, and higher dimensions,
 * as well as validation and edge cases.
 */
public class FingerprintCoreTest {
    
    private FingerprintCore core;
    private static final double DELTA = 1e-10; // Tolerance for floating-point comparisons
    
    @Before
    public void setUp() {
        core = new FingerprintCore();
    }
    
    // Test initialization
    @Test
    public void testInitialization() {
        assertEquals(0, core.getPoints().size());
        assertNull(core.getDimension());
    }
    
    // Test 2D points (backward compatibility)
    @Test
    public void testAdd2DPoints() {
        core.addPoint(new double[]{1.0, 2.0});
        core.addPoint(new double[]{3.0, 4.0});
        
        assertEquals(2, core.getPoints().size());
        assertArrayEquals(new double[]{1.0, 2.0}, core.getPoints().get(0), DELTA);
        assertArrayEquals(new double[]{3.0, 4.0}, core.getPoints().get(1), DELTA);
        assertEquals(Integer.valueOf(2), core.getDimension());
    }
    
    @Test
    public void test2DPairwiseDistances() {
        core.addPoint(new double[]{0.0, 0.0});
        core.addPoint(new double[]{3.0, 4.0});
        core.addPoint(new double[]{6.0, 8.0});
        
        double[][] distances = core.computePairwiseDistances();
        
        // Distance from (0,0) to (3,4) should be 5
        assertEquals(5.0, distances[0][1], DELTA);
        assertEquals(5.0, distances[1][0], DELTA);
        
        // Distance from (0,0) to (6,8) should be 10
        assertEquals(10.0, distances[0][2], DELTA);
        
        // Distance from (3,4) to (6,8) should be 5
        assertEquals(5.0, distances[1][2], DELTA);
        
        // Diagonal should be 0
        assertEquals(0.0, distances[0][0], DELTA);
        assertEquals(0.0, distances[1][1], DELTA);
        assertEquals(0.0, distances[2][2], DELTA);
    }
    
    @Test
    public void test2DDiameter() {
        core.addPoint(new double[]{0.0, 0.0});
        core.addPoint(new double[]{3.0, 4.0});
        core.addPoint(new double[]{1.0, 1.0});
        
        double diameter = core.computeDiameter();
        // Maximum distance is from (0,0) to (3,4) = 5
        assertEquals(5.0, diameter, DELTA);
    }
    
    // Test 3D points
    @Test
    public void testAdd3DPoints() {
        core.addPoint(new double[]{1.0, 2.0, 3.0});
        core.addPoint(new double[]{4.0, 5.0, 6.0});
        
        assertEquals(2, core.getPoints().size());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, core.getPoints().get(0), DELTA);
        assertArrayEquals(new double[]{4.0, 5.0, 6.0}, core.getPoints().get(1), DELTA);
        assertEquals(Integer.valueOf(3), core.getDimension());
    }
    
    @Test
    public void test3DPairwiseDistances() {
        core.addPoint(new double[]{0.0, 0.0, 0.0});
        core.addPoint(new double[]{1.0, 0.0, 0.0});
        core.addPoint(new double[]{0.0, 1.0, 0.0});
        core.addPoint(new double[]{0.0, 0.0, 1.0});
        
        double[][] distances = core.computePairwiseDistances();
        
        // Distance from origin to each axis point should be 1
        assertEquals(1.0, distances[0][1], DELTA);
        assertEquals(1.0, distances[0][2], DELTA);
        assertEquals(1.0, distances[0][3], DELTA);
        
        // Distance between axis points should be sqrt(2)
        assertEquals(Math.sqrt(2), distances[1][2], DELTA);
        assertEquals(Math.sqrt(2), distances[1][3], DELTA);
        assertEquals(Math.sqrt(2), distances[2][3], DELTA);
    }
    
    @Test
    public void test3DDiameter() {
        core.addPoint(new double[]{0.0, 0.0, 0.0});
        core.addPoint(new double[]{1.0, 1.0, 1.0});
        core.addPoint(new double[]{2.0, 2.0, 2.0});
        
        double diameter = core.computeDiameter();
        // Maximum distance is from (0,0,0) to (2,2,2) = sqrt(12)
        double expected = Math.sqrt(12);
        assertEquals(expected, diameter, DELTA);
    }
    
    // Test higher-dimensional points
    @Test
    public void testAdd4DPoints() {
        core.addPoint(new double[]{1.0, 2.0, 3.0, 4.0});
        core.addPoint(new double[]{5.0, 6.0, 7.0, 8.0});
        
        assertEquals(Integer.valueOf(4), core.getDimension());
        assertEquals(2, core.getPoints().size());
    }
    
    @Test
    public void test5DPairwiseDistances() {
        core.addPoint(new double[]{0.0, 0.0, 0.0, 0.0, 0.0});
        core.addPoint(new double[]{1.0, 1.0, 1.0, 1.0, 1.0});
        
        double[][] distances = core.computePairwiseDistances();
        // Distance should be sqrt(5)
        double expected = Math.sqrt(5);
        assertEquals(expected, distances[0][1], DELTA);
    }
    
    @Test
    public void testHighDimensionalDiameter() {
        // 10D points
        double[] point1 = new double[10];
        double[] point2 = new double[10];
        for (int i = 0; i < 10; i++) {
            point1[i] = 0.0;
            point2[i] = 1.0;
        }
        
        core.addPoint(point1);
        core.addPoint(point2);
        
        double diameter = core.computeDiameter();
        // Distance is sqrt(10)
        double expected = Math.sqrt(10);
        assertEquals(expected, diameter, DELTA);
    }
    
    // Test dimension validation
    @Test(expected = IllegalArgumentException.class)
    public void testDimensionConsistencyValidation() {
        core.addPoint(new double[]{1.0, 2.0}); // 2D
        core.addPoint(new double[]{3.0, 4.0, 5.0}); // 3D - should throw
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPointRejected() {
        core.addPoint(new double[]{});
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPointRejected() {
        core.addPoint(null);
    }
    
    // Test edge cases
    @Test
    public void testDiameterWithNoPoints() {
        double diameter = core.computeDiameter();
        assertEquals(0.0, diameter, DELTA);
    }
    
    @Test
    public void testDiameterWithSinglePoint() {
        core.addPoint(new double[]{1.0, 2.0, 3.0});
        double diameter = core.computeDiameter();
        assertEquals(0.0, diameter, DELTA);
    }
    
    @Test
    public void testPairwiseDistancesEmpty() {
        double[][] distances = core.computePairwiseDistances();
        assertEquals(0, distances.length);
    }
    
    @Test
    public void testPairwiseDistancesSinglePoint() {
        core.addPoint(new double[]{1.0, 2.0});
        double[][] distances = core.computePairwiseDistances();
        assertEquals(1, distances.length);
        assertEquals(1, distances[0].length);
        assertEquals(0.0, distances[0][0], DELTA);
    }
    
    @Test
    public void testClearMethod() {
        core.addPoint(new double[]{1.0, 2.0});
        core.addPoint(new double[]{3.0, 4.0});
        
        core.clear();
        
        assertEquals(0, core.getPoints().size());
        assertNull(core.getDimension());
    }
    
    @Test
    public void testClearAndAddDifferentDimension() {
        core.addPoint(new double[]{1.0, 2.0}); // 2D
        assertEquals(Integer.valueOf(2), core.getDimension());
        
        core.clear();
        
        core.addPoint(new double[]{1.0, 2.0, 3.0}); // 3D
        assertEquals(Integer.valueOf(3), core.getDimension());
    }
    
    // Test symmetry of distance matrix
    @Test
    public void testDistanceMatrixSymmetry() {
        core.addPoint(new double[]{1.0, 2.0, 3.0});
        core.addPoint(new double[]{4.0, 5.0, 6.0});
        core.addPoint(new double[]{7.0, 8.0, 9.0});
        
        double[][] distances = core.computePairwiseDistances();
        
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                assertEquals(distances[i][j], distances[j][i], DELTA);
            }
        }
    }
    
    // Test with negative coordinates
    @Test
    public void testNegativeCoordinates() {
        core.addPoint(new double[]{-1.0, -2.0});
        core.addPoint(new double[]{1.0, 2.0});
        
        double[][] distances = core.computePairwiseDistances();
        // Distance from (-1,-2) to (1,2) is sqrt(4+16) = sqrt(20)
        double expected = Math.sqrt(20);
        assertEquals(expected, distances[0][1], DELTA);
    }
    
    // Test with zero distances
    @Test
    public void testDuplicatePoints() {
        core.addPoint(new double[]{1.0, 2.0, 3.0});
        core.addPoint(new double[]{1.0, 2.0, 3.0});
        
        double[][] distances = core.computePairwiseDistances();
        assertEquals(0.0, distances[0][1], DELTA);
        
        double diameter = core.computeDiameter();
        assertEquals(0.0, diameter, DELTA);
    }
}
