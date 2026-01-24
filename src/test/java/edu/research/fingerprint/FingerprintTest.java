package edu.research.fingerprint;

import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test suite for the Fingerprint class.
 */
public class FingerprintTest {
    
    private static final double EPSILON = 1e-6;
    
    @Test
    public void testFingerprintComputation() {
        PointSet square = ShapeFactory.createSquare(1.0);
        double[] scales = {0.0, 0.5, 1.0};
        
        Fingerprint fp = Fingerprint.compute(square, scales);
        
        assertEquals(3, fp.size());
        assertArrayEquals(scales, fp.getScales(), EPSILON);
        
        // At scale 0, no pairs should have distance <= 0
        assertEquals(0.0, fp.getValues()[0], EPSILON);
        
        // At scale 1, all pairs should have normalized distance <= 1
        assertEquals(1.0, fp.getValues()[2], EPSILON);
    }
    
    @Test
    public void testScaleInvariance() {
        PointSet small = ShapeFactory.createSquare(1.0);
        PointSet large = ShapeFactory.createSquare(10.0);
        
        double[] scales = {0.0, 0.25, 0.5, 0.75, 1.0};
        
        Fingerprint fpSmall = Fingerprint.compute(small, scales);
        Fingerprint fpLarge = Fingerprint.compute(large, scales);
        
        // Fingerprints should be identical due to scale invariance
        assertArrayEquals(fpSmall.getValues(), fpLarge.getValues(), EPSILON);
    }
    
    @Test
    public void testL2Distance() {
        PointSet square = ShapeFactory.createSquare(1.0);
        PointSet triangle = ShapeFactory.createRegularPolygon(3, 1.0);
        
        double[] scales = {0.0, 0.25, 0.5, 0.75, 1.0};
        
        Fingerprint fpSquare = Fingerprint.compute(square, scales);
        Fingerprint fpTriangle = Fingerprint.compute(triangle, scales);
        
        double distance = fpSquare.l2Distance(fpTriangle);
        
        // Different shapes should have different fingerprints
        assertTrue(distance > 0);
    }
    
    @Test
    public void testStabilityUnderJitter() {
        Random random = new Random(42);
        PointSet original = ShapeFactory.createCube(1.0);
        PointSet jittered = original.applyJitter(0.05, random);
        
        double[] scales = new double[10];
        for (int i = 0; i < 10; i++) {
            scales[i] = (double) i / 9;
        }
        
        Fingerprint fpOriginal = Fingerprint.compute(original, scales);
        Fingerprint fpJittered = Fingerprint.compute(jittered, scales);
        
        double distance = fpOriginal.l2Distance(fpJittered);
        
        // Small jitter should result in small fingerprint change
        assertTrue(distance < 0.5);
    }
    
    @Test
    public void testFingerprintIn3D() {
        PointSet cube = ShapeFactory.createCube(1.0);
        double[] scales = {0.0, 0.5, 1.0};
        
        Fingerprint fp = Fingerprint.compute(cube, scales);
        
        assertEquals(3, fp.size());
        assertEquals(0.0, fp.getValues()[0], EPSILON);
        assertEquals(1.0, fp.getValues()[2], EPSILON);
    }
    
    @Test
    public void testFingerprintInHighDimensions() {
        Random random = new Random(42);
        
        // Test 5D
        PointSet random5D = ShapeFactory.createRandomPointSet(15, 5, random);
        double[] scales = {0.0, 0.5, 1.0};
        Fingerprint fp5D = Fingerprint.compute(random5D, scales);
        assertEquals(3, fp5D.size());
        
        // Test 10D
        PointSet random10D = ShapeFactory.createRandomPointSet(15, 10, random);
        Fingerprint fp10D = Fingerprint.compute(random10D, scales);
        assertEquals(3, fp10D.size());
    }
    
    @Test
    public void testL1Distance() {
        PointSet square = ShapeFactory.createSquare(1.0);
        PointSet triangle = ShapeFactory.createRegularPolygon(3, 1.0);
        
        // Use more scales to distinguish between shapes
        double[] scales = new double[20];
        for (int i = 0; i < 20; i++) {
            scales[i] = (double) i / 19;
        }
        
        Fingerprint fp1 = Fingerprint.compute(square, scales);
        Fingerprint fp2 = Fingerprint.compute(triangle, scales);
        
        double dist = fp1.l1Distance(fp2);
        assertTrue(dist > 0);
    }
    
    @Test
    public void testLInfDistance() {
        PointSet square = ShapeFactory.createSquare(1.0);
        PointSet triangle = ShapeFactory.createRegularPolygon(3, 1.0);
        
        // Use more scales to distinguish between shapes
        double[] scales = new double[20];
        for (int i = 0; i < 20; i++) {
            scales[i] = (double) i / 19;
        }
        
        Fingerprint fp1 = Fingerprint.compute(square, scales);
        Fingerprint fp2 = Fingerprint.compute(triangle, scales);
        
        double dist = fp1.lInfDistance(fp2);
        assertTrue(dist > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidScales() {
        PointSet square = ShapeFactory.createSquare(1.0);
        double[] scales = {-0.1, 0.5, 1.0}; // Invalid: negative scale
        Fingerprint.compute(square, scales);
    }
}
