# Implementation of Edmonds' Blossom Algorithm

## Overview
The *blossom algorithm* is an algorithm in graph theory for constructing maximum matchings on graphs. The algorithm was developed by *Jack Edmonds* in 1961, and published in 1965. Given a general graph G = (V, E), the algorithm finds a matching M such that each vertex in V is incident with at most one edge in M and |M| is maximized. The matching is constructed by iteratively improving an initial empty matching along augmenting paths in the graph. Unlike bipartite matching, the key new idea is that an odd-length cycle in the graph (blossom) is contracted to a single vertex, with the search continuing iteratively in the contracted graph.

## Our objective
Through this project, we aim to implement the blossom algorithm and visualize the steps leading to the maximum matching by using a graphical interface. The user creates the graph on screen by virtue of mouse inputs and obtains the maximum matching for the given input graph. The interface also allows the user to control the steps which show the implementation of the algorithm.

## Prerequisites
The environment in which the code is to be executed must have the **Java Development Kit (JDK)** installed.

## How to use the code

Compile the Java files with the following command in the terminal:

```bash
javac *.java
