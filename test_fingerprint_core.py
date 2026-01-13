"""
Unit tests for FingerprintCore class.

Tests N-dimensional point support including 2D, 3D, and higher dimensions,
as well as validation and edge cases.
"""

import unittest
import math
from fingerprint_core import FingerprintCore


class TestFingerprintCore(unittest.TestCase):
    """Test suite for FingerprintCore class."""
    
    def setUp(self):
        """Set up a fresh FingerprintCore instance for each test."""
        self.core = FingerprintCore()
    
    # Test initialization
    def test_initialization(self):
        """Test that FingerprintCore initializes with empty state."""
        self.assertEqual(len(self.core.getPoints()), 0)
        self.assertIsNone(self.core.getDimension())
    
    # Test 2D points (backward compatibility)
    def test_add_2d_points(self):
        """Test adding 2D points."""
        self.core.addPoint((1.0, 2.0))
        self.core.addPoint((3.0, 4.0))
        
        points = self.core.getPoints()
        self.assertEqual(len(points), 2)
        self.assertEqual(points[0], (1.0, 2.0))
        self.assertEqual(points[1], (3.0, 4.0))
        self.assertEqual(self.core.getDimension(), 2)
    
    def test_2d_pairwise_distances(self):
        """Test computing pairwise distances for 2D points."""
        self.core.addPoint((0.0, 0.0))
        self.core.addPoint((3.0, 4.0))
        self.core.addPoint((6.0, 8.0))
        
        distances = self.core.computePairwiseDistances()
        
        # Distance from (0,0) to (3,4) should be 5
        self.assertAlmostEqual(distances[0][1], 5.0)
        self.assertAlmostEqual(distances[1][0], 5.0)
        
        # Distance from (0,0) to (6,8) should be 10
        self.assertAlmostEqual(distances[0][2], 10.0)
        
        # Distance from (3,4) to (6,8) should be 5
        self.assertAlmostEqual(distances[1][2], 5.0)
        
        # Diagonal should be 0
        self.assertEqual(distances[0][0], 0.0)
        self.assertEqual(distances[1][1], 0.0)
        self.assertEqual(distances[2][2], 0.0)
    
    def test_2d_diameter(self):
        """Test computing diameter for 2D points."""
        self.core.addPoint((0.0, 0.0))
        self.core.addPoint((3.0, 4.0))
        self.core.addPoint((1.0, 1.0))
        
        diameter = self.core.computeDiameter()
        # Maximum distance is from (0,0) to (3,4) = 5
        self.assertAlmostEqual(diameter, 5.0)
    
    # Test 3D points
    def test_add_3d_points(self):
        """Test adding 3D points."""
        self.core.addPoint((1.0, 2.0, 3.0))
        self.core.addPoint((4.0, 5.0, 6.0))
        
        points = self.core.getPoints()
        self.assertEqual(len(points), 2)
        self.assertEqual(points[0], (1.0, 2.0, 3.0))
        self.assertEqual(points[1], (4.0, 5.0, 6.0))
        self.assertEqual(self.core.getDimension(), 3)
    
    def test_3d_pairwise_distances(self):
        """Test computing pairwise distances for 3D points."""
        self.core.addPoint((0.0, 0.0, 0.0))
        self.core.addPoint((1.0, 0.0, 0.0))
        self.core.addPoint((0.0, 1.0, 0.0))
        self.core.addPoint((0.0, 0.0, 1.0))
        
        distances = self.core.computePairwiseDistances()
        
        # Distance from origin to each axis point should be 1
        self.assertAlmostEqual(distances[0][1], 1.0)
        self.assertAlmostEqual(distances[0][2], 1.0)
        self.assertAlmostEqual(distances[0][3], 1.0)
        
        # Distance between axis points should be sqrt(2)
        self.assertAlmostEqual(distances[1][2], math.sqrt(2))
        self.assertAlmostEqual(distances[1][3], math.sqrt(2))
        self.assertAlmostEqual(distances[2][3], math.sqrt(2))
    
    def test_3d_diameter(self):
        """Test computing diameter for 3D points."""
        self.core.addPoint((0.0, 0.0, 0.0))
        self.core.addPoint((1.0, 1.0, 1.0))
        self.core.addPoint((2.0, 2.0, 2.0))
        
        diameter = self.core.computeDiameter()
        # Maximum distance is from (0,0,0) to (2,2,2) = sqrt(12)
        expected = math.sqrt(12)
        self.assertAlmostEqual(diameter, expected)
    
    # Test higher-dimensional points
    def test_add_4d_points(self):
        """Test adding 4D points."""
        self.core.addPoint((1.0, 2.0, 3.0, 4.0))
        self.core.addPoint((5.0, 6.0, 7.0, 8.0))
        
        self.assertEqual(self.core.getDimension(), 4)
        self.assertEqual(len(self.core.getPoints()), 2)
    
    def test_5d_pairwise_distances(self):
        """Test computing pairwise distances for 5D points."""
        self.core.addPoint((0.0, 0.0, 0.0, 0.0, 0.0))
        self.core.addPoint((1.0, 1.0, 1.0, 1.0, 1.0))
        
        distances = self.core.computePairwiseDistances()
        # Distance should be sqrt(5)
        expected = math.sqrt(5)
        self.assertAlmostEqual(distances[0][1], expected)
    
    def test_high_dimensional_diameter(self):
        """Test diameter computation in high dimensions."""
        # 10D points
        self.core.addPoint(tuple([0.0] * 10))
        self.core.addPoint(tuple([1.0] * 10))
        
        diameter = self.core.computeDiameter()
        # Distance is sqrt(10)
        expected = math.sqrt(10)
        self.assertAlmostEqual(diameter, expected)
    
    # Test dimension validation
    def test_dimension_consistency_validation(self):
        """Test that mixing different dimensions raises an error."""
        self.core.addPoint((1.0, 2.0))  # 2D
        
        with self.assertRaises(ValueError) as context:
            self.core.addPoint((3.0, 4.0, 5.0))  # 3D
        
        self.assertIn("dimension", str(context.exception).lower())
    
    def test_empty_point_rejected(self):
        """Test that empty points are rejected."""
        with self.assertRaises(ValueError):
            self.core.addPoint(())
    
    def test_invalid_type_rejected(self):
        """Test that invalid point types are rejected."""
        with self.assertRaises(TypeError):
            self.core.addPoint("not a point")
        
        with self.assertRaises(TypeError):
            self.core.addPoint(123)
    
    def test_list_input_accepted(self):
        """Test that lists are accepted and converted to tuples."""
        self.core.addPoint([1.0, 2.0, 3.0])
        points = self.core.getPoints()
        self.assertEqual(points[0], (1.0, 2.0, 3.0))
        self.assertIsInstance(points[0], tuple)
    
    # Test edge cases
    def test_diameter_with_no_points(self):
        """Test diameter computation with no points."""
        diameter = self.core.computeDiameter()
        self.assertEqual(diameter, 0.0)
    
    def test_diameter_with_single_point(self):
        """Test diameter computation with a single point."""
        self.core.addPoint((1.0, 2.0, 3.0))
        diameter = self.core.computeDiameter()
        self.assertEqual(diameter, 0.0)
    
    def test_pairwise_distances_empty(self):
        """Test pairwise distances with no points."""
        distances = self.core.computePairwiseDistances()
        self.assertEqual(distances, [])
    
    def test_pairwise_distances_single_point(self):
        """Test pairwise distances with a single point."""
        self.core.addPoint((1.0, 2.0))
        distances = self.core.computePairwiseDistances()
        self.assertEqual(len(distances), 1)
        self.assertEqual(len(distances[0]), 1)
        self.assertEqual(distances[0][0], 0.0)
    
    def test_clear_method(self):
        """Test clearing all points."""
        self.core.addPoint((1.0, 2.0))
        self.core.addPoint((3.0, 4.0))
        
        self.core.clear()
        
        self.assertEqual(len(self.core.getPoints()), 0)
        self.assertIsNone(self.core.getDimension())
    
    def test_clear_and_add_different_dimension(self):
        """Test that dimension can change after clearing."""
        self.core.addPoint((1.0, 2.0))  # 2D
        self.assertEqual(self.core.getDimension(), 2)
        
        self.core.clear()
        
        self.core.addPoint((1.0, 2.0, 3.0))  # 3D
        self.assertEqual(self.core.getDimension(), 3)
    
    # Test symmetry of distance matrix
    def test_distance_matrix_symmetry(self):
        """Test that the distance matrix is symmetric."""
        self.core.addPoint((1.0, 2.0, 3.0))
        self.core.addPoint((4.0, 5.0, 6.0))
        self.core.addPoint((7.0, 8.0, 9.0))
        
        distances = self.core.computePairwiseDistances()
        
        for i in range(len(distances)):
            for j in range(len(distances)):
                self.assertAlmostEqual(distances[i][j], distances[j][i])
    
    # Test with negative coordinates
    def test_negative_coordinates(self):
        """Test that negative coordinates work correctly."""
        self.core.addPoint((-1.0, -2.0))
        self.core.addPoint((1.0, 2.0))
        
        distances = self.core.computePairwiseDistances()
        # Distance from (-1,-2) to (1,2) is sqrt(4+16) = sqrt(20)
        expected = math.sqrt(20)
        self.assertAlmostEqual(distances[0][1], expected)
    
    # Test with zero distances
    def test_duplicate_points(self):
        """Test with duplicate points (zero distance)."""
        self.core.addPoint((1.0, 2.0, 3.0))
        self.core.addPoint((1.0, 2.0, 3.0))
        
        distances = self.core.computePairwiseDistances()
        self.assertAlmostEqual(distances[0][1], 0.0)
        
        diameter = self.core.computeDiameter()
        self.assertAlmostEqual(diameter, 0.0)


if __name__ == '__main__':
    unittest.main()
