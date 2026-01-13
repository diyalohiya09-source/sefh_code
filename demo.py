"""
Demo script for FingerprintCore class.

Demonstrates N-dimensional point support with examples in 2D, 3D, and higher dimensions.
"""

from fingerprint_core import FingerprintCore
import math


def demo_2d_points():
    """Demonstrate 2D point operations."""
    print("=" * 60)
    print("2D Point Operations (Backward Compatibility)")
    print("=" * 60)
    
    core = FingerprintCore()
    
    # Add 2D points
    points_2d = [(0.0, 0.0), (3.0, 4.0), (6.0, 0.0)]
    for point in points_2d:
        core.addPoint(point)
        print(f"Added point: {point}")
    
    print(f"\nDimension: {core.getDimension()}D")
    print(f"Number of points: {len(core.getPoints())}")
    
    # Compute pairwise distances
    distances = core.computePairwiseDistances()
    print("\nPairwise distances:")
    for i, row in enumerate(distances):
        print(f"  From point {i}: {[f'{d:.2f}' for d in row]}")
    
    # Compute diameter
    diameter = core.computeDiameter()
    print(f"\nDiameter (maximum distance): {diameter:.2f}")
    print()


def demo_3d_points():
    """Demonstrate 3D point operations."""
    print("=" * 60)
    print("3D Point Operations")
    print("=" * 60)
    
    core = FingerprintCore()
    
    # Add 3D points (corners of a cube)
    points_3d = [
        (0.0, 0.0, 0.0),
        (1.0, 0.0, 0.0),
        (0.0, 1.0, 0.0),
        (1.0, 1.0, 1.0)
    ]
    
    for point in points_3d:
        core.addPoint(point)
        print(f"Added point: {point}")
    
    print(f"\nDimension: {core.getDimension()}D")
    print(f"Number of points: {len(core.getPoints())}")
    
    # Compute pairwise distances
    distances = core.computePairwiseDistances()
    print("\nPairwise distances:")
    for i, row in enumerate(distances):
        print(f"  From point {i}: {[f'{d:.2f}' for d in row]}")
    
    # Compute diameter
    diameter = core.computeDiameter()
    print(f"\nDiameter (maximum distance): {diameter:.2f}")
    print(f"Expected: {math.sqrt(3):.2f} (distance from (0,0,0) to (1,1,1))")
    print()


def demo_high_dimensional():
    """Demonstrate high-dimensional point operations."""
    print("=" * 60)
    print("High-Dimensional Point Operations (5D)")
    print("=" * 60)
    
    core = FingerprintCore()
    
    # Add 5D points
    points_5d = [
        (0.0, 0.0, 0.0, 0.0, 0.0),
        (1.0, 1.0, 1.0, 1.0, 1.0),
        (2.0, 2.0, 2.0, 2.0, 2.0)
    ]
    
    for point in points_5d:
        core.addPoint(point)
        print(f"Added point: {point}")
    
    print(f"\nDimension: {core.getDimension()}D")
    print(f"Number of points: {len(core.getPoints())}")
    
    # Compute pairwise distances
    distances = core.computePairwiseDistances()
    print("\nPairwise distances:")
    for i, row in enumerate(distances):
        print(f"  From point {i}: {[f'{d:.2f}' for d in row]}")
    
    # Compute diameter
    diameter = core.computeDiameter()
    print(f"\nDiameter (maximum distance): {diameter:.2f}")
    print(f"Expected: {math.sqrt(20):.2f} (distance from origin to (2,2,2,2,2))")
    print()


def demo_dimension_validation():
    """Demonstrate dimension validation."""
    print("=" * 60)
    print("Dimension Validation")
    print("=" * 60)
    
    core = FingerprintCore()
    
    # Add a 2D point
    core.addPoint((1.0, 2.0))
    print(f"Added 2D point: (1.0, 2.0)")
    print(f"Current dimension: {core.getDimension()}D")
    
    # Try to add a 3D point (should fail)
    try:
        core.addPoint((3.0, 4.0, 5.0))
        print("ERROR: Should have raised ValueError!")
    except ValueError as e:
        print(f"\n✓ Correctly rejected 3D point with error:")
        print(f"  {e}")
    
    # Add another 2D point (should succeed)
    core.addPoint((6.0, 7.0))
    print(f"\n✓ Successfully added another 2D point: (6.0, 7.0)")
    print(f"Total points: {len(core.getPoints())}")
    print()


def main():
    """Run all demonstrations."""
    print("\n" + "=" * 60)
    print("FingerprintCore - N-Dimensional Point Support Demo")
    print("=" * 60)
    print()
    
    demo_2d_points()
    demo_3d_points()
    demo_high_dimensional()
    demo_dimension_validation()
    
    print("=" * 60)
    print("All demonstrations completed successfully!")
    print("=" * 60)


if __name__ == '__main__':
    main()
