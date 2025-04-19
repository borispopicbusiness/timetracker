package com.semiramide.timetracker.core.hierarchy.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.hierarchy.EmployeeHierarchyGraph;
import com.google.common.graph.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuavaMutableGraph implements EmployeeHierarchyGraph {
    private final MutableGraph<Employee> graph;

    public GuavaMutableGraph() {
        graph = GraphBuilder.directed().build();
    }

    private static List<Employee> getAncestors(
            Graph<Employee> graph, Employee node, List<Employee> ancestors) {
        for ( Employee parent : graph.predecessors(node) ) {
            if ( !ancestors.contains(parent) ) {
                ancestors.add(parent);
                getAncestors(graph, parent, ancestors);
            }
        }
        return ancestors;
    }

    @Override
    public void clear() {
        Set<Employee> nodes = new HashSet<>(graph.nodes());
        for ( Employee e : nodes ) {
            graph.removeNode(e);
        }
    }

    @Override
    public void addNode(Employee employee) {
        graph.addNode(employee);
    }

    @Override
    public void putEdge(Employee parent, Employee child) {
        graph.putEdge(parent, child);
    }

    @Override
    public void removeEdge(Employee parent, Employee child) {
        graph.removeEdge(parent, child);
    }

    @Override
    public boolean hasCycle() {
        return Graphs.hasCycle(graph);
    }

    @Override
    public boolean isChild(Employee parent, Employee child) {
        return reachableNodes(parent).contains(child);
    }

    @Override
    public boolean hasNode(Employee employee) {
        return graph.nodes().contains(employee);
    }

    @Override
    public boolean hasEdge(Employee parent, Employee child) {
        return graph.hasEdgeConnecting(parent, child);
    }

    @Override
    public Set<Employee> reachableNodes(Employee root) {
        return Graphs.reachableNodes(graph, root);
    }

    @Override
    public boolean isSameAs(EmployeeHierarchyGraph other) {
        return graph.nodes().equals(other.nodes()) && graph.edges().equals(other.edges());
    }

    @Override
    public List<Employee> directChildren(Employee employee) {
        return graph.successors(employee).stream().toList();
    }

    @Override
    public List<Employee> directParents(Employee employee) {
        return graph.predecessors(employee).stream().filter(x -> !x.equals(employee)).toList();
    }

    @Override
    public List<Employee> allParents(Employee employee) {
        List<Employee> ancestors = new ArrayList<>();
        return getAncestors(graph, employee, ancestors).stream()
                .filter(x -> !x.equals(employee))
                .toList();
    }

    @Override
    public void deleteNode(Employee employee) {
        graph.removeNode(employee);
    }

    @Override
    public Set<Employee> nodes() {
        return graph.nodes();
    }

    @Override
    public Set<EndpointPair<Employee>> edges() {
        return graph.edges();
    }

    @Override
    public void print() {
        for ( Employee employee : graph.nodes() ) {
            System.out.println(
                    ("\nSubordinates of employee "
                            + employee.getFirstName()
                            + " "
                            + employee.getLastName()
                            + " are: "
                            + "\n"
                            + reachableNodes(employee).stream()
                            .filter(x -> !x.equals(employee))
                            .map(x -> x.getFirstName() + " " + x.getLastName())
                            .toList()));
        }
    }
}
