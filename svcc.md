footer: (c) HuaWei Technologies 2014
slidenumbers: true


# Spectral Clustering Design for MLLib

## Intro to Spectral Clustering


- Focuses on top features - feature extraction / dimensionality reduction
- Intro
	- Used in Mulivariate Statistics and clustering of data
	- Uses eigenvalues of Similarity Matrix to perform dimensionality 	reduction

___

	- Applications;
		- Image Compression - Segment image into homogeneous regions to aid compression
		- Medical diagnosis - Segmentation of MRI images to identify targeted regions
		- Mapping and measurement: Analyze satellite sensor data to identify areas of interest
	- Image segmentation: segmentation-based object categorization
	- Similarity matrix S: symmetric matrix
	- Normalized Cuts Algorithm for partitioning matrix 
	- $$G = ($$V, $$E, $$omega) = weighted graph
	- A,B 
		
    Uses Eigenvalue Decomposition into:
- Input/Output:
	- Upper Triangular orthonormal eigenvectors: each (i,j) entry shows the similarity/correlation between the ith and jth feature
	- Similarity Matrix: a diagonal matrix of eigenvalues showing the strength of auto-correlation / self-referencing on the ith feature/dimension
	- Lower  Triangular Matrix
	- 							

	
	 

