package io.jenkins.plugins.opslevel.workflow;

import io.jenkins.plugins.opslevel.OpsLevelConfig;
import io.jenkins.plugins.opslevel.GlobalConfigUI;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.tasks.Notifier;
import hudson.tasks.BuildStepMonitor;
import hudson.Extension;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.model.AbstractProject;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostBuildAction extends Notifier {

    private static final Logger logger = LoggerFactory.getLogger(GlobalConfigUI.class.getName());

    public boolean run;
    public String webhookUrl;
    public String serviceAlias;
    public String environment;
    public String description;
    public String deployUrl;
    public String deployerId;
    public String deployerEmail;
    public String deployerName;

    @DataBoundConstructor
    public PostBuildAction(Boolean run, String webhookUrl, String serviceAlias, String environment,
                           String description, String deployUrl, String deployerId,
                           String deployerEmail, String deployerName) {
        super();
        this.run = run;
        this.webhookUrl = cleanupValue(webhookUrl);
        this.serviceAlias = cleanupValue(serviceAlias);
        this.environment = cleanupValue(environment);
        this.description = cleanupValue(description);
        this.deployUrl = cleanupValue(deployUrl);
        this.deployerId = cleanupValue(deployerId);
        this.deployerEmail = cleanupValue(deployerEmail);
        this.deployerName = cleanupValue(deployerName);
    }

    private String cleanupValue(String someValue) {
        if (someValue == null) {
            return "";
        }
        return someValue.trim();
    }

    public OpsLevelConfig generateOpsLevelConfig() {
        OpsLevelConfig config = new OpsLevelConfig();
        config.run = this.run;
        config.webhookUrl = this.webhookUrl;
        config.serviceAlias = this.serviceAlias;
        config.environment = this.environment;
        config.description = this.description;
        config.deployUrl = this.deployUrl;
        config.deployerId = this.deployerId;
        config.deployerEmail = this.deployerEmail;
        config.deployerName = this.deployerName;
        return config;
    }

    public boolean getRun() {
        return this.run;
    }

    public String getWebhookUrl() {
        return this.webhookUrl;
    }

    public String getServiceAlias() {
        return this.serviceAlias;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDeployUrl() {
        return this.deployUrl;
    }

    public String getDeployerId() {
        return this.deployerId;
    }

    public String getDeployerEmail() {
        return this.deployerEmail;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
    throws InterruptedException, IOException {
        // The job listener will notice this job. Nothing to do here.
        return true;
    }

    @Override
    public WebHookPublisherDescriptor getDescriptor() {
        return (WebHookPublisherDescriptor) super.getDescriptor();
    }
    @Extension
    public static class WebHookPublisherDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Modify OpsLevel notification for this build";
        }
    }
}
