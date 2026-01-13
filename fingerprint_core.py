"""
FingerprintCore: A class for modeling high-dimensional metric spaces.

This implementation supports N-dimensional points (2D, 3D, and beyond) and provides
methods to compute pairwise distances and diameter in N-dimensional Euclidean space.
"""

import math
from typing import List, Tuple, Optional


class FingerprintCore:
    """
    A class for managing and analyzing N-dimensional points in metric space.
    
    Supports operations on points of arbitrary dimensionality including:
    - Adding points with dimension validation
    - Computing pairwise Euclidean distances
    - Computing the diameter (maximum distance) of the point set
    """
    
    def __init__(self):
        """Initialize an empty FingerprintCore instance."""
        self.points: List[Tuple[float, ...]] = []
        self.dimension: Optional[int] = None
    
    def addPoint(self, point: Tuple[float, ...]) -> None:
        """
        Add an N-dimensional point to the collection.
        
        Args:
            point: A tuple of coordinates representing an N-dimensional point.
                   For example: (x, y) for 2D, (x, y, z) for 3D, etc.
        
        Raises:
            ValueError: If the point's dimension doesn't match existing points.
            TypeError: If point is not a tuple or list.
        
        Examples:
            >>> core = FingerprintCore()
            >>> core.addPoint((1.0, 2.0))  # 2D point
            >>> core.addPoint((3.0, 4.0))  # Another 2D point
            >>> core.addPoint((5.0, 6.0, 7.0))  # Raises ValueError - dimension mismatch
        """
        # Validate input type
        if not isinstance(point, (tuple, list)):
            raise TypeError("Point must be a tuple or list of coordinates")
        
        # Convert to tuple for consistency
        point = tuple(float(coord) for coord in point)
        
        # Validate dimension
        if len(point) == 0:
            raise ValueError("Point must have at least one dimension")
        
        # Check dimension consistency
        if self.dimension is None:
            # First point sets the dimension
            self.dimension = len(point)
        elif len(point) != self.dimension:
            raise ValueError(
                f"Point dimension {len(point)} does not match "
                f"expected dimension {self.dimension}"
            )
        
        self.points.append(point)
    
    def computePairwiseDistances(self) -> List[List[float]]:
        """
        Compute pairwise Euclidean distances between all points in N-dimensional space.
        
        The Euclidean distance between two N-dimensional points p and q is:
        d(p, q) = sqrt(sum((p_i - q_i)^2 for i in 1..N))
        
        Returns:
            A 2D list (matrix) where element [i][j] represents the distance
            between points[i] and points[j]. The matrix is symmetric with
            zeros on the diagonal.
        
        Examples:
            >>> core = FingerprintCore()
            >>> core.addPoint((0.0, 0.0))
            >>> core.addPoint((3.0, 4.0))
            >>> distances = core.computePairwiseDistances()
            >>> distances[0][1]  # Distance from point 0 to point 1
            5.0
        """
        n = len(self.points)
        distances = [[0.0 for _ in range(n)] for _ in range(n)]
        
        for i in range(n):
            for j in range(i + 1, n):
                # Compute N-dimensional Euclidean distance
                dist = self._euclidean_distance(self.points[i], self.points[j])
                distances[i][j] = dist
                distances[j][i] = dist  # Symmetric matrix
        
        return distances
    
    def computeDiameter(self) -> float:
        """
        Compute the diameter of the point set in N-dimensional space.
        
        The diameter is defined as the maximum pairwise distance between any
        two points in the collection.
        
        Returns:
            The maximum distance between any two points. Returns 0.0 if there
            are fewer than 2 points.
        
        Examples:
            >>> core = FingerprintCore()
            >>> core.addPoint((0.0, 0.0, 0.0))
            >>> core.addPoint((1.0, 1.0, 1.0))
            >>> core.addPoint((2.0, 2.0, 2.0))
            >>> core.computeDiameter()
            3.4641016151377544  # sqrt(12)
        """
        if len(self.points) < 2:
            return 0.0
        
        max_distance = 0.0
        n = len(self.points)
        
        for i in range(n):
            for j in range(i + 1, n):
                dist = self._euclidean_distance(self.points[i], self.points[j])
                max_distance = max(max_distance, dist)
        
        return max_distance
    
    def _euclidean_distance(self, p1: Tuple[float, ...], p2: Tuple[float, ...]) -> float:
        """
        Compute the Euclidean distance between two N-dimensional points.
        
        Args:
            p1: First N-dimensional point
            p2: Second N-dimensional point
        
        Returns:
            The Euclidean distance between p1 and p2
        """
        return math.sqrt(sum((c1 - c2) ** 2 for c1, c2 in zip(p1, p2)))
    
    def getPoints(self) -> List[Tuple[float, ...]]:
        """
        Get all points in the collection.
        
        Returns:
            A list of all N-dimensional points.
        """
        return self.points.copy()
    
    def getDimension(self) -> Optional[int]:
        """
        Get the dimension of points in this collection.
        
        Returns:
            The dimension of points, or None if no points have been added.
        """
        return self.dimension
    
    def clear(self) -> None:
        """Clear all points from the collection."""
        self.points.clear()
        self.dimension = None
