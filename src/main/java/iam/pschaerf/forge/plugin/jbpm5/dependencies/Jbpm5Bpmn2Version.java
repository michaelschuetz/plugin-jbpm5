package iam.pschaerf.forge.plugin.jbpm5.dependencies;

import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.dependencies.DependencyBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public enum Jbpm5Bpmn2Version {
    JBPM5_BPMN2_5_5_0_0("jbpm 5.0.0",
            Arrays.asList(DependencyBuilder.create("org.jbpm:jbpm-bpmn2:5.0.0")),
            Collections.EMPTY_LIST
    );

    private List<? extends Dependency> dependencies;
    private List<? extends Dependency> dependencyManagement;
    private String name;

    private Jbpm5Bpmn2Version(String name, List<? extends Dependency> dependencies, List<? extends Dependency> dependencyManagement) {
        this.name = name;
        this.dependencies = dependencies;
        this.dependencyManagement = dependencyManagement;
    }

    public List<? extends Dependency> getDependencies() {
        return dependencies;
    }

    public List<? extends Dependency> getDependencyManagement() {
        return dependencyManagement;
    }

    @Override
    public String toString() {
        return name;
    }
}
