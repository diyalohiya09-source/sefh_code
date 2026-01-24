package edu.research.fingerprint;

import java.util.Arrays;

/**
 * Represents a multi-scale fingerprint for a point set.
 * The fingerprint is scale-invariant and characterizes the geometric structure
 * of the object across multiple scales.
 */
public class Fingerprint {
    private final double[] scales;
    private final double[] values;
    
    /**
     * Creates a fingerprint with the given scale values and fingerprint values.
     * @param scales Array of normalized scale values in [0, 1]
     * @param values Array of fingerprint values (fraction of pairs below each scale)
     */
    public Fingerprint(double[] scales, double[] values) {
        if (scales.length != values.length) {
            throw new IllegalArgumentException("Scales and values must have the same length");
        }
        this.scales = scales.clone();
        this.values = values.clone();
    }
    
    /**
     * Computes a multi-scale fingerprint for a point set.
     * @param pointSet The point set to fingerprint
     * @param scales Array of normalized scale values to evaluate
     * @return The computed fingerprint
     */
    public static Fingerprint compute(PointSet pointSet, double[] scales) {
        // Validate scales
        for (double scale : scales) {
            if (scale < 0.0 || scale > 1.0) {
                throw new IllegalArgumentException("Scales must be in [0, 1]");
            }
        }
        
        // Compute all pairwise distances
        double[] distances = pointSet.computePairwiseDistances();
        
        // Compute diameter for normalization
        double diameter = pointSet.computeDiameter();
        
        // Handle edge case of zero diameter
        if (diameter == 0.0) {
            // All points are the same, return all zeros except at scale 1.0
            double[] values = new double[scales.length];
            for (int i = 0; i < scales.length; i++) {
                values[i] = (scales[i] == 1.0) ? 1.0 : 0.0;
            }
            return new Fingerprint(scales, values);
        }
        
        // Normalize distances
        double[] normalizedDistances = new double[distances.length];
        for (int i = 0; i < distances.length; i++) {
            normalizedDistances[i] = distances[i] / diameter;
        }
        
        // Compute fingerprint values at each scale
        double[] values = new double[scales.length];
        for (int i = 0; i < scales.length; i++) {
            double scale = scales[i];
            int count = 0;
            for (double normDist : normalizedDistances) {
                if (normDist <= scale) {
                    count++;
                }
            }
            values[i] = (double) count / normalizedDistances.length;
        }
        
        return new Fingerprint(scales, values);
    }
    
    /**
     * Gets the scale values.
     */
    public double[] getScales() {
        return scales.clone();
    }
    
    /**
     * Gets the fingerprint values.
     */
    public double[] getValues() {
        return values.clone();
    }
    
    /**
     * Gets the number of scales in the fingerprint.
     */
    public int size() {
        return scales.length;
    }
    
    /**
     * Computes the L1 distance between this fingerprint and another.
     * @param other The other fingerprint
     * @return The L1 distance
     */
    public double l1Distance(Fingerprint other) {
        if (this.scales.length != other.scales.length) {
            throw new IllegalArgumentException("Fingerprints must have the same number of scales");
        }
        
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum += Math.abs(this.values[i] - other.values[i]);
        }
        return sum;
    }
    
    /**
     * Computes the L2 (Euclidean) distance between this fingerprint and another.
     * @param other The other fingerprint
     * @return The L2 distance
     */
    public double l2Distance(Fingerprint other) {
        if (this.scales.length != other.scales.length) {
            throw new IllegalArgumentException("Fingerprints must have the same number of scales");
        }
        
        double sumSquared = 0.0;
        for (int i = 0; i < values.length; i++) {
            double diff = this.values[i] - other.values[i];
            sumSquared += diff * diff;
        }
        return Math.sqrt(sumSquared);
    }
    
    /**
     * Computes the L-infinity distance between this fingerprint and another.
     * @param other The other fingerprint
     * @return The L-infinity distance
     */
    public double lInfDistance(Fingerprint other) {
        if (this.scales.length != other.scales.length) {
            throw new IllegalArgumentException("Fingerprints must have the same number of scales");
        }
        
        double maxDiff = 0.0;
        for (int i = 0; i < values.length; i++) {
            double diff = Math.abs(this.values[i] - other.values[i]);
            if (diff > maxDiff) {
                maxDiff = diff;
            }
        }
        return maxDiff;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fingerprint[\n");
        for (int i = 0; i < scales.length; i++) {
            sb.append(String.format("  scale=%.4f, value=%.4f\n", scales[i], values[i]));
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Returns a compact string representation suitable for CSV output.
     */
    public String toCompactString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(String.format("%.6f", values[i]));
        }
        return sb.toString();
    }
}
