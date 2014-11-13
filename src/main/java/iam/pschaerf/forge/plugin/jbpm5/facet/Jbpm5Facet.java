package iam.pschaerf.forge.plugin.jbpm5.facet;

import iam.pschaerf.forge.plugin.jbpm5.dependencies.Jbpm5Bpmn2Version;
import iam.pschaerf.forge.plugin.jbpm5.dependencies.Jbpm5FlowBuilderVersion;
import iam.pschaerf.forge.plugin.jbpm5.dependencies.Jbpm5FlowVersion;

import java.util.Arrays;

import javax.inject.Inject;

import org.jboss.seam.forge.project.dependencies.Dependency;
import org.jboss.seam.forge.project.facets.BaseFacet;
import org.jboss.seam.forge.project.facets.DependencyFacet;
import org.jboss.seam.forge.shell.ShellPrompt;
import org.jboss.seam.forge.shell.plugins.Alias;
import org.jboss.seam.forge.shell.plugins.RequiresFacet;

@Alias("Jbpm5Facet")
@RequiresFacet({DependencyFacet.class})
public class Jbpm5Facet extends BaseFacet {
    @Inject
    private ShellPrompt shellPrompt;

    @Override
    public boolean install() {
        installDependencies();

        return true;
    }

    @Override
    public boolean isInstalled() {
    	if(isJbpm5FlowInstalled() && isJbpm5FlowBuilderInstalled() && isJbpm5FlowBuilderInstalled()) {
    		return true;
    	}

    	return false;
    }

    private boolean isJbpm5FlowInstalled() {
        DependencyFacet dependencyFacet = getProject().getFacet(DependencyFacet.class);

        for (Jbpm5FlowVersion version : Jbpm5FlowVersion.values()) {
            for (Dependency dependency : version.getDependencies()) {
                if (!dependencyFacet.hasDependency(dependency)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isJbpm5FlowBuilderInstalled() {
        DependencyFacet dependencyFacet = getProject().getFacet(DependencyFacet.class);

        for (Jbpm5FlowBuilderVersion version : Jbpm5FlowBuilderVersion.values()) {
            for (Dependency dependency : version.getDependencies()) {
                if (!dependencyFacet.hasDependency(dependency)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isJbpm5Bpmn2Installed() {
        DependencyFacet dependencyFacet = getProject().getFacet(DependencyFacet.class);

        for (Jbpm5Bpmn2Version version : Jbpm5Bpmn2Version.values()) {
            for (Dependency dependency : version.getDependencies()) {
                if (!dependencyFacet.hasDependency(dependency)) {
                    return false;
                }
            }
        }
        return true;
    }


    private void installDependencies() {
    	if(!isJbpm5FlowInstalled()) {
    		installJbpm5Flow();
    	}
    	if(!isJbpm5FlowBuilderInstalled()) {
    		installJbpm5FlowBuilder();
    	}
    	if(!isJbpm5Bpmn2Installed()) {
    		installJbpm5Bpmn2();
    	}
    }


    private void installJbpm5Bpmn2() {
        Jbpm5Bpmn2Version version = shellPrompt.promptChoiceTyped("Chose a version:",
                Arrays.asList(Jbpm5Bpmn2Version.values()));

        DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : Jbpm5Bpmn2Version.JBPM5_BPMN2_5_5_0_0.getDependencyManagement()) {
            dependencyFacet.addDependency(dependency);
        }

        dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : version.getDependencies()) {
            dependencyFacet.addDependency(dependency);
        }
    }

    private void installJbpm5Flow() {
        Jbpm5FlowVersion version = shellPrompt.promptChoiceTyped("Chose a version:",
                Arrays.asList(Jbpm5FlowVersion.values()));

        DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : Jbpm5FlowVersion.JBPM5_FLOW_5_0_0.getDependencyManagement()) {
            dependencyFacet.addDependency(dependency);
        }

        dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : version.getDependencies()) {
            dependencyFacet.addDependency(dependency);
        }
    }

    private void installJbpm5FlowBuilder() {
    	Jbpm5FlowBuilderVersion version = shellPrompt.promptChoiceTyped("Chose a version:",
                Arrays.asList(Jbpm5FlowBuilderVersion.values()));

        DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : Jbpm5FlowBuilderVersion.JBPM5_FLOW_BUILDER_5_0_0.getDependencyManagement()) {
            dependencyFacet.addDependency(dependency);
        }

        dependencyFacet = project.getFacet(DependencyFacet.class);
        for (Dependency dependency : version.getDependencies()) {
            dependencyFacet.addDependency(dependency);
        }
    }
}

