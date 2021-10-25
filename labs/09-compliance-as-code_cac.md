# Compliance as Code (CaC)

- [Compliance as Code (CaC) using Inspec (Mandatory)](#compliance-as-code-cac-using-inspec)
- [How to Embed Inspec into Jenkins (Optional)](#how-to-embed-inspec-into-jenkins)
- [How to Create Custom Inspec Profile (Mandatory)](#how-to-create-custom-inspec-profile)
- [Inspec Command Resources (Optional)](#inspec-command-resources)
- [Inspec File Resource (Optional)](#inspec-file-resource)
- [Inspec Custom Matchers (Optional)](#inspec-custom-matchers)
- [Create Inspec Profile for CIS Benchmark (Optional)](#create-inspec-profile-for-cis-benchmark)
- [Docker Compliance using Inspec (Optional)](#docker-compliance-using-inspec)

## Compliance as Code (CaC) using Inspec

### Learn how to achieve compliance automation using the Inspec tool

In this scenario, you will learn how to install the Inspec and test a server for compliance.

As part of this scenario, you will need to install the **Inspec** tool and then run the Compliance scan against the production(prod) server.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

> Remember!
>
>Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

### Install Inspec Tool

> Chef InSpec is an open-source framework for testing and auditing your applications and infrastructure. Chef InSpec works by comparing the actual state of your system with the desired state that you express in easy-to-read and easy-to-write Chef InSpec code. Chef InSpec detects violations and displays findings in the form of a report, but puts you in control of remediation.
>
> Source: [Inspec official website](https://www.inspec.io/docs)

Let’s install the Inspec on the system to learn Compliance as Code (CaC).

Download the **Inspec** debian package from the InSpec website.

```bash
wget https://packages.chef.io/files/stable/inspec/4.37.8/ubuntu/18.04/inspec_4.37.8-1_amd64.deb
```

Install the downloaded package.

```bash
dpkg -i inspec_4.37.8-1_amd64.deb
```

We have successfully installed the Inspec tool, let’s explore the functionality it provides us.

```bash
inspec --help
```

```
Commands:
  inspec archive PATH                                  # archive a profile to tar.gz (default) or zip
  inspec artifact SUBCOMMAND                           # Manage Chef InSpec Artifacts
  inspec automate SUBCOMMAND or compliance SUBCOMMAND  # Chef Automate commands
  inspec check PATH                                    # verify all tests at the specified PATH
  inspec clear_cache                                   # clears the InSpec cache. Useful for debugging.
  inspec detect                                        # detect the target OS
  inspec env                                           # Output shell-appropriate completion configuration
  inspec exec LOCATIONS                                # Run all tests at LOCATIONS.
  inspec habitat SUBCOMMAND                            # Manage Habitat with Chef InSpec
  inspec help [COMMAND]                                # Describe available commands or one specific command
  inspec init SUBCOMMAND                               # Generate InSpec code
  inspec json PATH                                     # read all tests in PATH and generate a JSON summary
  inspec plugin SUBCOMMAND                             # Manage Chef InSpec and Train plugins
  inspec shell                                         # open an interactive debugging shell
  inspec supermarket SUBCOMMAND ...                    # Supermarket commands
  inspec vendor PATH                                   # Download all dependencies and generate a lockfile in a `vendor` directory
  inspec version                                       # prints the version of this tool

Options:
  l, [--log-level=LOG_LEVEL]                         # Set the log level: info (default), debug, warn, error
      [--log-location=LOG_LOCATION]                  # Location to send diagnostic log messages to. (default: $stdout or Inspec::Log.error)
      [--diagnose], [--no-diagnose]                  # Show diagnostics (versions, configurations)
      [--color], [--no-color]                        # Use colors in output.
      [--interactive], [--no-interactive]            # Allow or disable user interaction
      [--disable-user-plugins]                       # Disable loading all plugins that the user installed.
      [--enable-telemetry], [--no-enable-telemetry]  # Allow or disable telemetry
      [--chef-license=CHEF_LICENSE]                  # Accept the license for this product and any contained products: accept, accept-no-persist, accept-silent


About Chef InSpec:
  Patents: chef.io/patents
```

Let’s move to the **next step**.

### Run the Inspec profile

Let’s try to check whether our servers follow the **linux-baseline** best practices. We will be using the Dev-Sec’s **linux-baseline** Inspec profile.

Before executing the profile, we need to run the below command:

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

This command prevents the **ssh** agent from prompting **YES or NO** question.

Let’s run the Inspec against the production server.

```bash
inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

- The first parameter tells the Inspec profile that we need to run against the server
- **-t** tells the target machine
- **-i** flag used to specify the ssh-key since we are using login in via ssh
- **–chef-license accept** tells that we are accepting license this commands prevent the **inspec** from prompting **YES or NO** question

```bash
+---------------------------------------------+
✔ 1 product license accepted.
+---------------------------------------------+
[2021-04-12T22:47:51+00:00] WARN: URL target https://github.com/dev-sec/linux-baseline transformed to https://github.com/dev-sec/linux-baseline/archive/master.tar.gz. Consider using the git fetcher

Profile: DevSec Linux Security Baseline (linux-baseline)
Version: 2.8.0
Target:  ssh://root@prod-xIKciVAk:22

  ✔  os-01: Trusted hosts login
     ✔  File /etc/hosts.equiv is expected not to exist
  ✔  os-02: Check owner and permissions for /etc/shadow
     ✔  File /etc/shadow is expected to exist
     ✔  File /etc/shadow is expected to be file
     ✔  File /etc/shadow is expected to be owned by "root"
     ✔  File /etc/shadow is expected not to be executable
     ✔  File /etc/shadow is expected not to be readable by other
     ✔  File /etc/shadow group is expected to eq "shadow"
     ✔  File /etc/shadow is expected to be writable by owner
     ✔  File /etc/shadow is expected to be readable by owner
     ✔  File /etc/shadow is expected to be readable by group


  ...[SNIP]...


  ↺  sysctl-31b: Secure Core Dumps - dump path
     ↺  Skipped control due to only_if condition.
  ↺  sysctl-32: kernel.randomize_va_space
     ↺  Skipped control due to only_if condition.
  ↺  sysctl-33: CPU No execution Flag or Kernel ExecShield
     ↺  Skipped control due to only_if condition.


Profile Summary: 16 successful controls, 2 control failures, 37 controls skipped
Test Summary: 60 successful, 7 failures, 37 skipped
```

You can see, the output does inform us about **16** successful controls and **2** control failures.

### Challenge - Using Ansible/Inspec to achieve compliance

This exercise is to do continuous compliance scanning for production Infrastructure(prod-xIKciVAk). We will be using the configuration management tools, i.e., Ansible and a dedicated utility like Inspec. Please embed these two tools into your DevOps pipeline.

Doing so will provide immediate feedback to the DevOps teams of any deviation from standard policies.

1. Use Ansible to achieve compliance as code (remember the changed=1 Ansible output?)
2. Use Docker Inspec to do continuous compliance scanning and save the output into JSON file
3. Embed Ansible hardening and Inspec as part of the prod stage with job names as ansible-hardening and inspec
4. Ensure you are using the following projects for this task
      - https://github.com/dev-sec/ansible-os-hardening
      - https://github.com/dev-sec/linux-baseline

You can use the following URL and credentials to log into Gitlab SCM/CI.

Name	Value
**Gitlab URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
**Username**	root
**Password**	pdso-training

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed Inspec in CI/CD pipeline

> Can we put it into CI? Yes, why not?

Let’s login into the GitLab and configure production machine https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd.

We can use the GitLab credentials provided below to login i.e.,

Name	Value
**Username**	root
**Password**	pdso-training

Click the **Expand** button under the **Variables** section, then click on the **Add Variable** button.

Add the following key/value pair in the form.

Name	Value
**Key**	DEPLOYMENT_SERVER
**Value**	prod-xIKciVAk

Name	Value
**Key**	DEPLOYMENT_SERVER_SSH_PRIVKEY
**Value**	Copy the private key from the **prod** machine using SSH. The SSH key is available at **/root/.ssh/id_rsa**

Finally, Click on the button **Add Variable**.

Next, please visit https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml.

Click on the **Edit** button and append the following code to the **.gitlab-ci.yml** file.

```yml
inspec:
  stage: prod
  only:
    - "master"
  environment: production
  before_script:
    - mkdir -p ~/.ssh
    - echo "$DEPLOYMENT_SERVER_SSH_PRIVKEY" | tr -d '\r' > ~/.ssh/id_rsa
    - chmod 600 ~/.ssh/id_rsa
    - eval "$(ssh-agent -s)"
    - ssh-add ~/.ssh/id_rsa
    - ssh-keyscan -t rsa $DEPLOYMENT_SERVER >> ~/.ssh/known_hosts
  script:
    - docker run --rm -v ~/.ssh:/root/.ssh -v $(pwd):/share hysnsec/inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@$DEPLOYMENT_SERVER -i ~/.ssh/id_rsa --chef-license accept --reporter json:inspec-output.json
  artifacts:
    paths: [inspec-output.json]
    when: always
```

> **Reference**: https://docs.chef.io/inspec/reporters.


Save changes to the file using the **Commit changes** button.

> Don’t forget to set **DEPLOYMENT_SERVER** variable under Settings (**Project Settings > CI/CD > Variables > Expand > Add Variable**), otherwise your build will fail.

We can see the results by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.

There you have it. We have run the Inspec locally and then embedded it into a CI/CD pipeline.

## How to Embed Inspec into Jenkins

### Use Inspec tool to achieve compliance automation in Jenkins CI/CD pipeline

In this scenario, you will learn how to embed Inspec in the Jenkins CI/CD pipeline.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab and Jenkins machine to start.

> Remember!
>
>Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

> Note
>
>We have already set up the Jenkins machine with several plugins to help you do the exercise.

### Create a new job

> The Jenkins system is already configured with GitLab. If you wish to know **how to configure Jenkins with GitLab**, you can check out this link.

We will create a new job in Jenkins by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.

You can use the following details to log into Jenkins.

Name	Value
**Username**	root
**Password**	pdso-training

Provide a name for your new item, e.g., django.nv, select the **Pipeline** option, and click on the **OK** button.

In the next screen, click on the **Build Triggers** tab, check the **Build when a change is pushed to GitLab**..... checkbox.

At the bottom right-hand side, just below the **Comment (regex) for triggering a build** form field, you will find the **Advanced…** button. Please click on it.

Then, click on the **Generate** button under **Secret token** to generate a token. We will use this token for GitLab’s Webhook Settings. This webhook setting will allow GitLab to let Jenkins know whenever a change is made to the repository.

Please visit the following GitLab URL to set up the Jenkins webhook.

Name	Value
**URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/hooks

Fill the form using the following details.

Name	Value
**URL**	https://jenkins-xIKciVAk.lab.practical-devsecops.training/project/django.nv
**Secret Token**	Paste the secret token we just generated above.

Click on the **Add webhook** button, and **go back to the Jenkins tab** to continue the setup process.

Click on the **Pipeline** tab, and select **Pipeline script from SCM** from **Definition** options. Once you do that, few more options would be made available to you.

Select **Git** as **SCM**, enter our **django.nv** repository http url.

Name	Value
**Repository URL**	http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git

Since we haven’t added the GitLab credentials to the Jenkins system, you will get an error.

```
Failed to connect to repository : Command "git ls-remote -h -- http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv HEAD" returned status code 128:
stdout:
stderr: remote: HTTP Basic: Access denied
fatal: Authentication failed for 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git/'
```

Let’s add the credentials by clicking on the **Add** button (the one with a key symbol). Select the **Jenkins** option and fill the pop-up form with the following details.

Name	Value
**Username**	root
**Password**	pdso-training
**ID**	gitlab-auth
**Description**	Leave it blank as it’s optional

Click on the **Add** button, and select our new credentials from the Credentials Dropdown.

The error we experienced before should be gone by now.

Finally, click the **Save** button.

Let’s move to the **next step**.

### A simple CI/CD pipeline

Considering your DevOps team created a simple **Jenkinsfile** with the following contents.

```groovy
pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py check
                """
            }
        }
        stage("test") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py test taskManager
                """
            }
        }
        stage("integration") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    echo "This is an integration step"
                    sh "exit 1"
                }
            }
        }
        stage("prod") {
            steps {
                input "Deploy to production?"
                echo "This is a deploy step."
            }
        }
    }
    post {
        failure {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        unstable {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        success {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'skipped')
        }
        always { 
            deleteDir()                     // clean up workspace
        }
    }
}
```

We do have four stages in this pipeline, **build**, **test**, **integration** and **prod**. Assuming you don’t understand Python or any programming language, we can safely consider the DevOps team is building and testing the code.

Let’s login into Gitlab using the following details.

Name	Value
**Gitlab URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
**Username**	root
**Password**	pdso-training

Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

Since we want to use Jenkins to execute the CI/CD jobs, we need to remove **.gitlab-ci.yml** from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the **Gitlab Runner** and the Jenkins systems.

>Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the **.gitlab-ci.yaml** file is missing.
>
>Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Let’s move to the **next step**.

### Embed Inspec in Jenkins

As discussed in the **automate compliance** exercise, we can embed the Inspec tool in our CI/CD pipeline. However, you need to test the command manually before you embed this **CaC** tool in the pipeline.

We need to add credentials into Jenkins, namely, SSH Private Key and SSH hostname of the machine we are securing, i.e., production machine. Since we don’t want the credentials to be hardcoded in the **Jenkinsfile**. Let’s add the creds to the Jenkins credential store by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/credentials/store/system/domain/_/.

Click on the **Add Credentials** link on the left sidebar, and select **SSH Username with private key** as **Kind**, then add the following credentials into it.

Name	Value
**ID**	ssh-prod
**Description**	Credentials to login into Production machine
**Username**	root
**Private Key**	Check **Enter directly**, click the **Add** button and copy the private key from Production machine, available at **/root/.ssh/id_rsa**
**Passphrase**	Leave it blank because we want this process to be automatic without any human intervention. If you desire more robust credential security mechanism, please use dedicated secret management systems like **Hashicorp Vault**.

Once done, click the **OK** button.

Add another credential to save SSH hostname, so it’s not hardcoded in the **Jenkinsfile**, then select **Secret text** as **Kind**, then add the following credentials into it.

Name	Value
**Secret**	prod-xIKciVAk
**ID**	prod-server

Next, go back to the GitLab tab and add **inspec** stage in the **Jenkinsfile**. Now our Jenkinsfile will look like the following.

```yml
pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py check
                """
            }
        }
        stage("test") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py test taskManager
                """
            }
        }
        stage("integration") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    echo "This is an integration step."
                    sh "exit 1"
                }
            }
        }
        stage("prod") {
            steps {
                input "Deploy to production?"
                echo "This is a deploy step."
            }
        }
        stage("Inspec"){
            agent {
                docker {
                    image 'hysnsec/inspec'
                    args '-u root'
                }
            }
            steps {
                sshagent(['ssh-prod']) {
                    sh "inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@prod-xIKciVAk --chef-license accept --reporter json:inspec-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'inspec-output.json', fingerprint: true
                }
            }
        }
    }
    post {
        failure {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        unstable {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        success {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'skipped')
        }
        always { 
            deleteDir()                     // clean up workspace
        }
    }
}
```

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Let’s move to the **next step**.

### Allow the stage failure

> Remember!
>
>Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the **catchError** function to “not fail the build” even though the tool found security issues.

> **Reference**: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps.

```groovy
        stage("Inspec"){
            agent {
                docker {
                    image 'hysnsec/inspec'
                    args '-u root'
                }
            }
            steps {
                sshagent(['ssh-prod']) {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                        sh "inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@prod-xIKciVAk --chef-license accept --reporter json:inspec-output.json"
                    }
                }
            }
            ...
```

After adding the **catchError** function, the pipeline would look like the following.

```groovy
pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py check
                """
            }
        }
        stage("test") {
            agent {
                docker {
                    image 'python:3.6'
                    args '-u root'
                }
            }
            steps {
                sh """
                pip3 install --user virtualenv
                python3 -m virtualenv env
                . env/bin/activate
                pip3 install -r requirements.txt
                python3 manage.py test taskManager
                """
            }
        }
        stage("integration") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    echo "This is an integration step."
                    sh "exit 1"
                }
            }
        }
        stage("prod") {
            steps {
                input "Deploy to production?"
                echo "This is a deploy step."
            }
        }
        stage("Inspec"){
            agent {
                docker {
                    image 'hysnsec/inspec'
                    args '-u root'
                }
            }
            steps {
                sshagent(['ssh-prod']) {
                    sh "inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@prod-xIKciVAk --chef-license accept --reporter json:inspec-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'inspec-output.json', fingerprint: true
                }
            }
        }
    }
    post {
        failure {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        unstable {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'failed')
        }
        success {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: STAGE_NAME, state: 'skipped')
        }
        always { 
            deleteDir()                     // clean up workspace
        }
    }
}
```

You will notice that the **inspec** stage failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

## How to Create Custom Inspec Profile

### Create and Run custom Inspec profile

In this scenario, you will learn how to create a dummy Inspec profile and run it against a production machine.

You will need to install the Inspec tool, create the profile and then run the Compliance scan against the server.

### Install Inspec Tool

> Chef InSpec is an open-source framework for testing and auditing your applications and infrastructure. Chef InSpec works by comparing the actual state of your system with the desired state that you express in easy-to-read and easy-to-write Chef InSpec code. Chef InSpec detects violations and displays findings in the form of a report, but puts you in control of remediation.
>
>**Source**: [Inspec official website](https://www.inspec.io/docs)

Let’s install the Inspec on the system to learn Compliance as code.

Download the **Inspec** debian package from the InSpec website.

```bash
wget https://packages.chef.io/files/stable/inspec/4.37.8/ubuntu/18.04/inspec_4.37.8-1_amd64.deb
```

Install the downloaded package.

```bash
dpkg -i inspec_4.37.8-1_amd64.deb
```

We have successfully installed Inspec tool, let’s explore the functionality it provides us.

```bash
inspec --help
```

```bash
Commands:
  inspec archive PATH                                  # archive a profile to tar.gz (default) or zip
  inspec artifact SUBCOMMAND                           # Manage Chef InSpec Artifacts
  inspec automate SUBCOMMAND or compliance SUBCOMMAND  # Chef Automate commands
  inspec check PATH                                    # verify all tests at the specified PATH
  inspec clear_cache                                   # clears the InSpec cache. Useful for debugging.
  inspec detect                                        # detect the target OS
  inspec env                                           # Output shell-appropriate completion configuration
  inspec exec LOCATIONS                                # Run all tests at LOCATIONS.
  inspec habitat SUBCOMMAND                            # Manage Habitat with Chef InSpec
  inspec help [COMMAND]                                # Describe available commands or one specific command
  inspec init SUBCOMMAND                               # Generate InSpec code
  inspec json PATH                                     # read all tests in PATH and generate a JSON summary
  inspec plugin SUBCOMMAND                             # Manage Chef InSpec and Train plugins
  inspec shell                                         # open an interactive debugging shell
  inspec supermarket SUBCOMMAND ...                    # Supermarket commands
  inspec vendor PATH                                   # Download all dependencies and generate a lockfile in a `vendor` directory
  inspec version                                       # prints the version of this tool

Options:
  l, [--log-level=LOG_LEVEL]                         # Set the log level: info (default), debug, warn, error
      [--log-location=LOG_LOCATION]                  # Location to send diagnostic log messages to. (default: $stdout or Inspec::Log.error)
      [--diagnose], [--no-diagnose]                  # Show diagnostics (versions, configurations)
      [--color], [--no-color]                        # Use colors in output.
      [--interactive], [--no-interactive]            # Allow or disable user interaction
      [--disable-user-plugins]                       # Disable loading all plugins that the user installed.
      [--enable-telemetry], [--no-enable-telemetry]  # Allow or disable telemetry
      [--chef-license=CHEF_LICENSE]                  # Accept the license for this product and any contained products: accept, accept-no-persist, accept-silent


About Chef InSpec:
  Patents: chef.io/patents
```

Let’s move to the **next step**.

### Create the custom profile

Create a new folder and cd into that folder.

```bash
mkdir inspec-profile && cd inspec-profile
```

Create the Ubuntu profile.

```bash
inspec init profile ubuntu --chef-license accept
```

Run the following command to append the inspec task to the file at **ubuntu/controls/example.rb**. If you wish, you can edit the file using nano or any text editor.

```bash
cat >> ubuntu/controls/example.rb <<EOL
describe file('/etc/shadow') do
    it { should exist }
    it { should be_file }
    it { should be_owned_by 'root' }
  end
EOL
```

This code basically checks whether **shadow** file is owned by **root** or not.

To know more about Inspec, please visit the official website at [Inspec-tutorial](https://www.inspec.io/tutorials).

Let’s validate the profile to make sure there are no syntax errors.

```bash
inspec check ubuntu
```

```bash
Location :   ubuntu
Profile :    ubuntu
Controls :   3
Timestamp :  2020-05-25T22:53:52+00:00
Valid :      true

No errors or warnings
```

Now run the profile on the local-machine before executing on the server.

```bash
inspec exec ubuntu
```

```bash

Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  local://

  ✔  tmp-1.0: Create /tmp directory
     ✔  File /tmp is expected to be directory

  File /tmp
     ✔  is expected to be directory
  File /etc/shadow
     ✔  is expected to exist
     ✔  is expected to be file
     ✔  is expected to be owned by "root"

Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 5 successful, 0 failures, 0 skipped
```

Let’s move to the next step.

### Run the Inspec tool to test for compliance against a server

Let’s try to run the custom profile created by us against the server. Before executing the profile we need to execute the below command to avoid being prompted with Yes or No when connecting to a server via ssh.

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

This commands prevent the ssh agent from prompting **YES or NO** question

Let’s run inspec with the following options.

```bash
inspec exec ubuntu -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

Woah! there’s lots going on here. Lets explore these options one by one.

The flags/options used in the above commands are:

- **-t** specifies the target machine to run the Inspec profile against. Here we are using ssh as a remote login mechanism, but we can also use winrm(windows), container(docker) etc.,
- **-i** provides the path where the remote/local machine’s ssh key is stored.
- **–chef-license** option ensures that we are accepting license agreement automatically.

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  ssh://root@prod-jftfefdf:22

  ✔  tmp-1.0: Create /tmp directory
     ✔  File /tmp is expected to be directory

  File /tmp
     ✔  is expected to be directory
  File /etc/shadow
     ✔  is expected to exist
     ✔  is expected to be file
     ✔  is expected to be owned by "root"

Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 5 successful, 0 failures, 0 skipped
```

You can see, we have about **1** successful control check and **0** control failures.

Let’s move to the **next step**.

### Challenge - Create an InSpec profile for PCI/DSS

1. Use Inspec’s init command to create a new profile
2. Edit our newly created Inspec skeleton to add any **three** checks from [PCI/DSS requirements](https://linux-audit.com/linux-systems-guide-to-achieve-pci-dss-compliance-and-certification/).
3. Run the tests locally before setting it up in the CI pipeline
4. Commit it to the project’s repository using either the GitLab UI or Git commands

> Note
>
>You can access your GitLab machine by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training and use the credentials mentioned in the first step.

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Inspec Command Resources

### Create and Run custom Inspec profile with command resource

In this scenario, you will learn how to about the command resources and how they help you in checking the compliance requirements.

You will need to install the Inspec tool, create the profile and then run the Compliance scan against the server.

### Setup Inspec

You know the drill already,

### Install Inspec

```bash
wget https://packages.chef.io/files/stable/inspec/4.18.114/ubuntu/16.04/inspec_4.18.114-1_amd64.deb
```

Install the downloaded package.

```bash
dpkg -i inspec_4.18.114-1_amd64.deb
```

```
Selecting previously unselected package inspec.
(Reading database ... 11606 files and directories currently installed.)
Preparing to unpack inspec_4.18.114-1_amd64.deb ...
You're about to install InSpec!
Unpacking inspec (4.18.114-1) ...
Setting up inspec (4.18.114-1) ...
Thank you for installing InSpec!
```

### Create the profile

Create a new folder and cd into that folder.

```bash
mkdir cis-ubuntu
cd cis-ubuntu
```

### Test the barebone profile

```bash
inspec init profile ubuntu --chef-license accept
```

```bash
+---------------------------------------------+
✔ 1 product license accepted.
+---------------------------------------------+

 ─────────────────────────── InSpec Code Generator ─────────────────────────── 

Creating new profile at /cis-ubuntu/ubuntu
 • Creating directory controls
 • Creating file controls/example.rb
 • Creating file README.md
 • Creating file inspec.yml
```

Let’s move to the **next step** to learn about command resources.

### Understand the audit command

We will create a custom profile based on CIS Ubuntu 18.04 Benchmark, you can download the benchmark PDF from here and refer the page 83 **“Ensure sudo commands use pty”**.

Let’s add this control/security best practice into Inspec profile. But, before we create the profile we need to ensure that the command works in DevSecOps Box and understand its output.

### Execute the audit command.

Following is the command, we will be using to check if pty is being used.

```bash
grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*
```

Interesting! we didn’t see any output so we can assume that we didn’t find **use_pty** setting in our sudoers file. Lets add this security best practice to the file and see how the above command behaves.

Edit **/etc/sudoers** file using **nano** or any text editor and add the use_pty setting.

```bash
cat >> /etc/sudoers <<EOL
#
# This file MUST be edited with the 'visudo' command as root.
#
# Please consider adding local content in /etc/sudoers.d/ instead of
# directly modifying this file.
#
Defaults        env_reset
Defaults        mail_badpass
Defaults        secure_path="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/snap/bin"
Defaults        use_pty    # you can put it here
# Host alias specification

# User alias specification

# Cmnd alias specification

# User privilege specification
root    ALL=(ALL:ALL) ALL

# Members of the admin group may gain root privileges
%admin ALL=(ALL) ALL

# Allow members of group sudo to execute any command
%sudo   ALL=(ALL:ALL) ALL

# See sudoers(5) for more information on "#include" directives:

#includedir /etc/sudoers.d
EOL
```

Let’s run the audit command once again and see if it finds pty usage. In short, absense of this string is a control failure and we need to enforce its presence.

```bash
grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*
```

```bash
/etc/sudoers:Defaults        use_pty    # you can put it here
```

Let’s move to the **next step** to add this to our ubuntu profile.

### Add the command to the profile.

> Basically, we need to run a command against a system to check for compliance.

To do this, inspec provides us with a command resource/method.

Let’s have a look at [Inspec documentation for the command resource](https://docs.chef.io/inspec/resources/command/).

As mentioned above, we can use this resource to execute commands like the **grep** we ran in the last step. Once a command returns its output, we can use **stdout**(aka output) to compare it against the desired/expected value in our profile.

We will use the following command to replace the inspec task in the file, **ubuntu/controls/example.rb**. If you wish, you can edit the file manually, using **nano** or any text editor.

```bash
cat > ubuntu/controls/example.rb <<EOL
control 'ubuntu-1.3.2' do
   title 'Ensure sudo commands use pty'
   desc 'Attackers can run a malicious program using sudo, which would again fork a background process that remains even when the main program has finished executing.'
   describe command('grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*') do
      its('stdout') { should match /Defaults(\s*)use_pty/ }
   end
end
EOL
```

Ignore the **should match** text for now as we will be covering it shortly in the next lesson.

> Reference: [Regex match any whitespace](https://stackoverflow.com/questions/21974376/regex-match-any-whitespace).

Did you wonder, why are we using the regex? This is to ensure, we capture the exact match irrespective of space or tab as a seperator used between Defaults and use_pty strings.

```
/etc/sudoers:Defaults        use_pty    # you can put it here
```

The sudoer file can also just a space and it will be valid.

```
/etc/sudoers:Defaults use_pty    # you can put it here
```

If you haven’t fallen in love with regex yet, head over to [Rubular site](https://rubular.com/) to proclaim your love.

Jokes aside, let’s validate the profile to ensure there are no syntax errors.

```bash
inspec check ubuntu
```

```bash
Location :   ubuntu
Profile :    ubuntu
Controls :   1
Timestamp :  2020-11-25T07:35:04+00:00
Valid :      true

No errors or warnings
```

Now, run the profile on the local-machine before executing it on the remote server

```bash
inspec exec ubuntu
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  local://

  ✔  ubuntu-1.3.2: Ensure sudo commands use pty
     ✔  Command: `grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*` stdout is expected to match /Defau
lts(\s*)use_pty/


Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 1 successful, 0 failures, 0 skipped
```

You can see, we have about **1** successful control check because we just add the remediation into DevSecOps Box.

Let’s move to the **next step**.

### Run the Inspec Profile to test for compliance against a server

Let’s try to run the custom profile created by us against the server.

Before executing the profile, we need to execute the below command.

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

The above command prevents the ssh agent from prompting **YES or NO** question.

Let’s run inspec with the following options.

```bash
inspec exec ubuntu -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

```
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  ssh://root@prod-xIKciVAk:22

  ×  ubuntu-1.3.2: Ensure sudo commands use pty
     ×  Command: `grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*` stdout is expected to match /Defau
lts(\s*)use_pty/
     expected "" to match /Defaults(\s*)use_pty/
     Diff:
     @@ -1,2 +1,2 @@
     -/Defaults(\s*)use_pty/
     +""

Profile Summary: 0 successful controls, 1 control failure, 0 controls skipped
Test Summary: 0 successful, 1 failure, 0 skipped
```

The flags/options used in the above commands are:

- **-t** : tells the target machine to run the profile against.
- **-i** : provides the path where the remote machine’s ssh key is stored.
- **–chef-license** : accept ensures that we are accepting license agreement there by preventing the **inspec** from prompting **YES or NO** question.

You can see, we got **1** control failure i.e., the production machine doesn’t follow the CIS best practice. The DevOps/Security team can run Ansible hardening scripts to ensure the production machine is hardened and continously scan the production machine for compliance.

## Inspec File Resource

### Learn how to use file resource to achieve compliance

In this scenario, you will learn how to install the Inspec and learn to create a profile with file resource.

You will need to install the Inspec tool, create the profile and then run the Compliance scan against the server.

### Setup Inspec

You know the drill already,

### Install Inspec

```bash
wget https://packages.chef.io/files/stable/inspec/4.18.114/ubuntu/16.04/inspec_4.18.114-1_amd64.deb
```

Install the downloaded package.

```bash
dpkg -i inspec_4.18.114-1_amd64.deb
```

```bash
Selecting previously unselected package inspec.
(Reading database ... 11606 files and directories currently installed.)
Preparing to unpack inspec_4.18.114-1_amd64.deb ...
You're about to install InSpec!
Unpacking inspec (4.18.114-1) ...
Setting up inspec (4.18.114-1) ...
Thank you for installing InSpec!
```

### Create the profile

Create a new folder and cd into that folder.

```bash
mkdir cis-ubuntu
cd cis-ubuntu
```

### Test the barebone profile

```bash
inspec init profile ubuntu --chef-license accept
```

```bash
+---------------------------------------------+
✔ 1 product license accepted.
+---------------------------------------------+

 ─────────────────────────── InSpec Code Generator ─────────────────────────── 

Creating new profile at /cis-ubuntu/ubuntu
 • Creating directory controls
 • Creating file controls/example.rb
 • Creating file README.md
 • Creating file inspec.yml
```

Let’s move to the **next step** to learn about file resources.

### Run the audit command

Let’s take the same example we covered in command resource exercise i.e, **“Ensure sudo commands use pty”** and try to automate it using file resource instead of command resource.

We used the following command in command resource exercise for the auditing the machine.

```bash
grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*
```

Obviously, we got nothing as we haven’t hardened this machine.

Let’s create a proper sudoers file to see before and after states of the file.

```bash
cat >> /etc/sudoers <<EOL
#
# This file MUST be edited with the 'visudo' command as root.
#
# Please consider adding local content in /etc/sudoers.d/ instead of
# directly modifying this file.
#
Defaults        env_reset
Defaults        mail_badpass
Defaults        secure_path="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/snap/bin"
Defaults        use_pty    # you can put it here
# Host alias specification

# User alias specification

# Cmnd alias specification

# User privilege specification
root    ALL=(ALL:ALL) ALL

# Members of the admin group may gain root privileges
%admin ALL=(ALL) ALL

# Allow members of group sudo to execute any command
%sudo   ALL=(ALL:ALL) ALL

# See sudoers(5) for more information on "#include" directives:

#includedir /etc/sudoers.d
EOL
```

Let’s re-run the above command.

```bash
grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers /etc/sudoers.d/*
```

```bash
/etc/sudoers:Defaults        use_pty    # you can put it here
```

Great, we got our desired/expected value.

Let’s move to the **next step** to automate this check using file resource.

### Understand the file resource

> Use the **file** InSpec audit resource to test all system file types, including files, directories, symbolic links, named pipes, sockets, character devices, block devices, and doors.


Let’s have a look at [Inspec documentation of the file resource](https://docs.chef.io/inspec/resources/file/) and see what it has to offer us.

Besides using command resource, we can also use file resource to test the desired/expected state of the system.

> As they say, “more than one way to do it”.

Run the following command to replace the inspec task at **ubuntu/controls/example.rb**. If you wish, you can edit the file using **nano** or any text editor.

```bash
cat > ubuntu/controls/example.rb <<EOL
control 'ubuntu-1.3.2' do
   title 'Ensure sudo commands use pty'
   desc 'Attackers can run a malicious program using sudo, which would again fork a background process that remains even when the main program has finished executing.'
   describe file('/etc/sudoers') do
      its('content') { should match /Defaults(\s*)use_pty/ }
   end
end
EOL
```

Lets validate the profile to ensure there are no syntax errors.

```bash
inspec check ubuntu
```

```bash
Location :   ubuntu
Profile :    ubuntu
Controls :   1
Timestamp :  2020-11-25T07:35:04+00:00
Valid :      true

No errors or warnings
```

Now run the profile on the local machine i.e., devsecops-box.

```bash
inspec exec ubuntu
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  local://

  ✔  ubuntu-1.3.2: Ensure sudo commands use pty
     ✔  File /etc/sudoers content is expected to match /Defaults(\s*)use_pty/


Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 1 successful, 0 failures, 0 skipped
```

You can see, we have about **1** successful control check because we created that file with the right settings.

Let’s move to the **next step**.

### Run the Inspec tool to test for compliance against a server

Let’s try to run the custom profile created by us against the server

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

```bash
inspec exec ubuntu -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  ssh://root@prod-xIKciVAk:22
 
  ×  ubuntu-1.3.2: Ensure sudo commands use pty
     ×  File /etc/sudoers content is expected to match /Defaults(\s*)use_pty/
     expected "#\n# This file MUST be edited with the 'visudo' command as root.\n#\n# Please consider adding local ...\n# See sudoers(5) for more information on \"#include\" directives:\n\n#includedir /etc/sudoers.d\n" to match /Defaults(\s*)use_pty/
     Diff:
     @@ -1,2 +1,31 @@
     -/Defaults(\s*)use_pty/
     +#
     +# This file MUST be edited with the 'visudo' command as root.
     +#
     +# Please consider adding local content in /etc/sudoers.d/ instead of
     +# directly modifying this file.
     +#
     +# See the man page for details on how to write a sudoers file.
     +#
     +Defaults  env_reset
     +Defaults  mail_badpass
     +Defaults  secure_path="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/snap/bin"
     +
     +# Host alias specification
     +
     +# User alias specification
     +
     +# Cmnd alias specification
     +
     +# User privilege specification
     +root      ALL=(ALL:ALL) ALL
     +
     +# Members of the admin group may gain root privileges
     +%admin ALL=(ALL) ALL
     +
     +# Allow members of group sudo to execute any command
     +%sudo     ALL=(ALL:ALL) ALL
     +
     +# See sudoers(5) for more information on "#include" directives:
     +
     +#includedir /etc/sudoers.d
 
Profile Summary: 0 successful controls, 1 control failure, 0 controls skipped
Test Summary: 0 successful, 1 failure, 0 skipped
```

The flags/options used in the above commands are:

- **-t** : tells the target machine to run the profile against.
- **-i** : provides the path where the remote machine’s ssh key is stored.
- **–chef-license** : accept ensures that we are accepting license agreement there by preventing the inspec from prompting YES or NO question.

As you can see on **line number 43**, we have about **0** successful control check and **1** control failures. It failed as production machine is not hardened yet.

## Inspec Custom Matchers

### Compare/Match the desired state of the system with real state

In this scenario, you will learn about custom matchers that are essential to any Inspec Profile.

You will need to install the Inspec tool, create the profile and then run the Compliance scan against the server.

### Setup Inspec

You know the drill already,

### Install Inspec

```bash
wget https://packages.chef.io/files/stable/inspec/4.18.114/ubuntu/16.04/inspec_4.18.114-1_amd64.deb
```

Install the downloaded package.

```bash
dpkg -i inspec_4.18.114-1_amd64.deb
```

```bash
Selecting previously unselected package inspec.
(Reading database ... 11606 files and directories currently installed.)
Preparing to unpack inspec_4.18.114-1_amd64.deb ...
You're about to install InSpec!
Unpacking inspec (4.18.114-1) ...
Setting up inspec (4.18.114-1) ...
Thank you for installing InSpec!
```

### Create the profile

Create a new folder and cd into that folder.

```bash
mkdir cis-ubuntu
cd cis-ubuntu
```

### Test the barebone profile

```bash
inspec init profile ubuntu --chef-license accept
```

```bash
+---------------------------------------------+
✔ 1 product license accepted.
+---------------------------------------------+

 ─────────────────────────── InSpec Code Generator ─────────────────────────── 

Creating new profile at /cis-ubuntu/ubuntu
 • Creating directory controls
 • Creating file controls/example.rb
 • Creating file README.md
 • Creating file inspec.yml
```

Let’s move to the **next step** to learn about custom mathcers.

### Run the audit command

We will learn about custom matchers in Inspec, how we can use the correct resources for our control.

Let’s choose 1 control from CIS Ubuntu 18.04 Benchmark, you can download the PDF from here and refer the page no. **384 “Ensure permissions on /etc/ssh/sshd_config are configured “**.

To find the file permission, we can use the stat command.

```bash
stat /etc/ssh/sshd_config
```

```bash
 File: /etc/ssh/sshd_config
  Size: 285             Blocks: 8          IO Block: 4096   regular file
Device: 400014h/4194324d        Inode: 4671949     Links: 1
Access: (0644/-rw-r--r--)  Uid: (    0/    root)   Gid: (    0/    root)
Access: 2020-11-18 05:26:02.758704203 +0000
Modify: 2020-09-23 08:02:23.714054949 +0000
Change: 2020-11-18 05:26:01.858728725 +0000
 Birth: -
```

As we can see, we have **0644** as permission bits.

To ensure only root has read access to this crucial file (loose permissions on this file can lead to serious security attacks), we will change its permissions and ownership.

```bash
chown root:root /etc/ssh/sshd_config
chmod og-rwx /etc/ssh/sshd_config
```

> Reference: [Linux file permissions and attributes](https://wiki.archlinux.org/index.php/File_permissions_and_attributes).

We’ve learned about **command** and **file** resource in the previous exercises. What would be an appropriate resource to choose in this scenario?

It really demands on what you are trying to accomplish. Ideally you must explore both of them. In this example using **command** resource will make our tasks complicated so let’s use **file** resource. Let’s refresh our memory by reading [the file resource documentation](https://docs.chef.io/inspec/resources/file/#unixlinux-properties).

As part of the documentation, we notice there are a few interesting properties such as **uid**, **gid**.

Let’s move to the **next step** to put these properties to use.

### Edit the profile

Run the following command to replace the Inspec task at **ubuntu/controls/example.rb**.

```bash
cat > ubuntu/controls/example.rb <<EOL
control 'ubuntu-5.2.1' do
   title 'Ensure permissions on /etc/ssh/sshd_config are configured'
   desc 'The /etc/ssh/sshd_configfile contains configuration specifications for sshd. The command below checks whether the owner and group of the file is root.'
   describe file('/etc/ssh/sshd_config') do
     its('owner') { should eq 'root'}
     its('group') { should eq 'root'}
     its('mode') { should cmp '0600' }
   end
end
EOL
```

In the above task we are checking for three things.

1. Ensure the owner of the file is root.
2. Ensure this file belongs to the root group.
3. The mode/permissions are read-only for root.

Lets validate the profile to ensure there are no syntax errors.

```bash
inspec check ubuntu
```

```bash
Location :   ubuntu
Profile :    ubuntu
Controls :   1
Timestamp :  2020-11-25T07:35:04+00:00
Valid :      true

No errors or warnings
```

Now run the profile on the local-machine before executing it against the remote server

```bash
inspec exec ubuntu
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  local://

  ✔  ubuntu-5.2.1: Ensure permissions on /etc/ssh/sshd_config are configured
     ✔  File /etc/ssh/sshd_config owner is expected to eq "root"
     ✔  File /etc/ssh/sshd_config group is expected to eq "root"
     ✔  File /etc/ssh/sshd_config mode is expected to cmp == "0600"


Profile Summary: 1 successful control, 0 control failures, 0 controls skipped
Test Summary: 3 successful, 0 failures, 0 skipped
```

Perfect, we got **1** successful control as output i.e., our machine DevSecOps Box is following compliance requirement(s).

Let’s move to the **next step**.

### Run the Inspec profile against a remote server

Let’s try to run the custom profile created by us against the remote server

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

```bash
inspec exec ubuntu -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  ssh://root@prod-xIKciVAk:22

  ×  ubuntu-5.2.1: Ensure permissions on /etc/ssh/sshd_config are configured (1 failed)
     ✔  File /etc/ssh/sshd_config owner is expected to eq "root"
     ✔  File /etc/ssh/sshd_config group is expected to eq "root"
     ×  File /etc/ssh/sshd_config mode is expected to cmp == "0600"

     expected: 0600
          got: 0666

     (compared using `cmp` matcher)



Profile Summary: 0 successful controls, 1 control failure, 0 controls skipped
Test Summary: 2 successful, 1 failure, 0 skipped
```

The flags/options used in the above commands are:

- **-t** : tells the target machine to run the profile against.
- **-i** : provides the path where the remote machine’s ssh key is stored.
- **–chef-license** : accept ensures that we are accepting license agreement there by preventing the inspec from prompting YES or NO question.

As expected, our production machine is not hardened yet, so we have **1** control failure.

What do we need to do to fix it? thats right, fix the issue on production aka harden the production environment.

## Create Inspec Profile for CIS Benchmark

### Create and Run custom Inspec profile based on CIS Ubuntu Benchmark

In this scenario, you will learn how to create a custom Inspec profile to check for your organization’s policies as code. You will also learn how to test this profile against a server.

### Install Inspec Tool

>Chef InSpec is an open-source framework for testing and auditing your applications and infrastructure. Chef InSpec works by comparing the actual state of your system with the desired state that you express in easy-to-read and easy-to-write Chef InSpec code. Chef InSpec detects violations and displays findings in the form of a report, but puts you in control of remediation.
>
>Source [Inspec official website](https://www.inspec.io/docs/).

Let’s install the Inspec on the system.

Download the Inspec Debian package from the InSpec website

```bash
wget https://packages.chef.io/files/stable/inspec/4.18.114/ubuntu/16.04/inspec_4.18.114-1_amd64.deb
```

Install the downloaded package

```bash
dpkg -i inspec_4.18.114-1_amd64.deb
```

```bash
Selecting previously unselected package inspec.
(Reading database ... 11606 files and directories currently installed.)
Preparing to unpack inspec_4.18.114-1_amd64.deb ...
You're about to install InSpec!
Unpacking inspec (4.18.114-1) ...
Setting up inspec (4.18.114-1) ...
Thank you for installing InSpec!
```

We have successfully installed the Inspec tool, let’s explore the functionality it provides us.

```bash
inspec --help
```

```bash
Commands:
  inspec archive PATH                # archive a profile to tar.gz (default) or zip
  inspec artifact SUBCOMMAND         # Manage Chef InSpec Artifacts
  inspec check PATH                  # verify all tests at the specified PATH
  inspec compliance SUBCOMMAND       # Chef Compliance commands
  inspec detect                      # detect the target OS
  inspec env                         # Output shell-appropriate completion configuration
  inspec exec LOCATIONS              # Run all test files at the specified LOCATIONS. Loads the given profile(s) and fetches their dependen...
  inspec habitat SUBCOMMAND          # Manage Habitat with Chef InSpec
  inspec help [COMMAND]              # Describe available commands or one specific command
  inspec init SUBCOMMAND             # Generate InSpec code
  inspec json PATH                   # read all tests in PATH and generate a JSON summary
  inspec nothing                     # does nothing
  inspec plugin SUBCOMMAND           # Manage Chef InSpec and Train plugins
  inspec shell                       # open an interactive debugging shell
  inspec supermarket SUBCOMMAND ...  # Supermarket commands
  inspec vendor PATH                 # Download all dependencies and generate a lockfile in a `vendor` directory
  inspec version                     # prints the version of this tool

Options:
  l, [--log-level=LOG_LEVEL]                         # Set the log level: info (default), debug, warn, error
      [--log-location=LOG_LOCATION]                  # Location to send diagnostic log messages to. (default: $stdout or Inspec::Log.error)
      [--diagnose], [--no-diagnose]                  # Show diagnostics (versions, configurations)
      [--color], [--no-color]                        # Use colors in output.
      [--interactive], [--no-interactive]            # Allow or disable user interaction
      [--disable-core-plugins]                       # Disable loading all plugins that are shipped in the lib/plugins directory of InSpec. Useful in development.
      [--disable-user-plugins]                       # Disable loading all plugins that the user installed.
      [--enable-telemetry], [--no-enable-telemetry]  # Allow or disable telemetry
      [--chef-license=CHEF_LICENSE]                  # Accept the license for this product and any contained products: accept, accept-no-persist, accept-silent
```

Let’s move to the **next step**.

### Create the profile with CIS Benchmark

We will create a custom profile for Inspec using the commands and best practices mentioned in CIS Ubuntu 18.04 Benchmark; you can download the CIS Ubuntu 18.04 Benchmark PDF from [here](https://downloads.cisecurity.org/#/).

Please refer the page no. 80, **“Configure Sudo”** for more information. Let’s convert the audit steps mentioned in the CIS Benchmark into a custom Inspec profile.

In short, we need to do two things to create a profile.

1. Figure out the command/tool that needs to be run to ascertain the state of the system.
2. Add the above command to the Inspec profile.

Let’s go ahead and create the Inspec profile.

To store our Inspec profile, we will create a new folder and **cd** into the newly created folder.

```bash
mkdir cis-ubuntu && cd cis-ubuntu
```
Use the **inspec init** command to create an Ubuntu Inspec profile.

```bash
inspec init profile ubuntu --chef-license accept
```

Run the following command to append the inspec task to the **ubuntu/controls/configure_sudo.rb** file. If you wish, you can edit the file using nano or any text editor.

```bash
cat >> ubuntu/controls/configure_sudo.rb <<EOL
control 'ubuntu-1.3.1' do
   title 'Ensure sudo is installed'
   desc 'sudo allows a permitted user to execute a command as the superuser or another user, as specified by the security policy.'
   describe package('sudo') do
      it { should be_installed }
   end
end

control 'ubuntu-1.3.2' do
   title 'Ensure sudo commands use pty'
   desc 'Attackers can run a malicious program using sudo, which would again fork a background process that remains even when the main program has finished executing.'
   describe command('grep -Ei "^\s*Defaults\s+([^#]+,\s*)?use_pty(,\s+\S+\s*)*(\s+#.*)?$" /etc/sudoers').stdout do
      it { should include 'Defaults use_pty' }
   end
end

control 'ubuntu-1.3.3' do
   title 'Ensure sudo log file exists'
   desc 'Attackers can run a malicious program using sudo, which would again fork a background process that remains even when the main program has finished executing.'
   describe command('grep -Ei "^\s*Defaults\s+logfile=\S+" /etc/sudoers').stdout do
      it { should include 'Defaults logfile=' }
   end
end
EOL
```

Remove the **ubuntu/controls/example.rb** file, as we will not use it in this profile.

```bash
rm ubuntu/controls/example.rb
```

> Can you use the **command** resource to check if the package is installed or not in the system?

If you notice in the above Inspec profile script, you will see a couple of new terms like **package** and **command**. These terms are called resources; think of them as modules or plugins that help you achieve something in the system.

- [command resource](https://docs.chef.io/inspec/resources/command/)
- [package resource](https://docs.chef.io/inspec/resources/package/)

For now, let’s move forward. If you wish to explore these resources further, you can use the above links.

The above code checks if the **sudo** package is installed. We are also checking if the file **/etc/sudoers** contain some sane security defaults. The CIS benchmark PDF provides us the needed commands to audit the system’s state, and we are using these commands to convert them into Inspec checks.

We tried to keep everything simple in this exercise for the sake of simplicity, but you do need to implement most of these checks in your organization. If we have a few hundred ubuntu servers to patch, maintain or ensure compliance, do you think we can/should do these manually? No, we should create an Inspec profile to automate our compliance checks. This process is what most people call **Compliance as Code**.

Let’s validate the profile to ensure there are no syntax errors.

```bash
inspec check ubuntu
```

```bash
Location :   ubuntu
Profile :    ubuntu
Controls :   3
Timestamp :  2020-11-24T05:19:27+00:00
Valid :      true

No errors or warnings
```

Now run the profile on the local machine before executing it against a server. If we do not provide any server details, it runs on the local machine by default.

```bash
inspec exec ubuntu
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  local://

  ✔  ubuntu-1.3.1: Ensure sudo is installed
     ✔  System Package sudo is expected to be installed
  ×  ubuntu-1.3.2: Ensure sudo commands use pty
     ×  is expected to include "Defaults use_pty"
     expected "" to include "Defaults use_pty"
  ×  ubuntu-1.3.3: Ensure sudo log file exists
     ×  is expected to include "Defaults logfile="
     expected "" to include "Defaults logfile="


Profile Summary: 1 successful control, 2 control failures, 0 controls skipped
Test Summary: 1 successful, 2 failures, 0 skipped
```

Let’s move to the **next step**.

### Run the Inspec against a remote server

Let’s try to run the custom profile we just created against a remote server.

Before executing the profile, we need to implement the password-less SSH login mechanism between the local machine and the remote machine.

To configure passwordless SSH login, we need to implement the following two steps.
1. Get rid of the SSH key verification checks.
2. Copy your local SSH public key into the remote machine.

We have already taken care of step 2 for you, so you only have to deal with step 1. Obviously, we can achieve step 1 objectives in multiple ways. Here, we are disabling host key checks, but it’s a bad idea to do it in production. Please research why it’s a bad idea!

```bash
echo "StrictHostKeyChecking no" >> ~/.ssh/config
```

As mentioned before, the above command prevents the **ssh** agent from prompting **YES or NO** question.

Let’s run our newly created ubuntu Inspec profile against our production server (**prod-xIKciVAk**)

```bash
inspec exec ubuntu -t ssh://root@prod-xIKciVAk -i ~/.ssh/id_rsa --chef-license accept
```

```bash
Profile: InSpec Profile (ubuntu)
Version: 0.1.0
Target:  ssh://root@prod-jftfefdf:22

  ✔  ubuntu-1.3.1: Ensure sudo is installed
     ✔  System Package sudo is expected to be installed
  ×  ubuntu-1.3.2: Ensure sudo commands use pty
     ×  is expected to include "Defaults use_pty"
     expected "" to include "Defaults use_pty"
  ×  ubuntu-1.3.3: Ensure sudo log file exists
     ×  is expected to include "Defaults logfile="
     expected "" to include "Defaults logfile="

Profile Summary: 1 successful control, 2 control failures, 0 controls skipped
Test Summary: 1 successful, 2 failures, 0 skipped
```

Woah! Lot is going on here. Let’s explore these options one by one.

The flags/options used in the above commands are

- **-t** specifies the target machine to run the Inspec profile against. Here, we are using SSH as a remote login mechanism, but we can also use winrm (windows), container (docker), etc.,
- **-i** provides the path where the remote/local machine’s ssh key is stored.
- **–chef-license** option ensures that we are accepting the license agreement automatically.

We can see that we have about **1** successful control check and **2** control failures.

Let’s move to the **next step**.

### Challenge

1. Read the documentation about [OS Resources](https://docs.chef.io/inspec/resources/)
2. Understand the difference between **command** and **service** resource
3. Please do whatever it takes on the production machine to achieve the following Inspec output

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

> **Hint**:
> The CIS Ubuntu Benchmark document is your best friend.
>
> ```bash
> Profile: InSpec Profile (ubuntu)
> Version: 0.1.0
> Target:  ssh://root@prod-jftfefdf:22
> 
> ✔  ubuntu-1.3.1: Ensure sudo is installed
>    ✔  System Package sudo is expected to be installed
> ✔  ubuntu-1.3.2: Ensure sudo commands use pty
>    ✔  Defaults use_pty
>       is expected to include "Defaults use_pty"
> ✔  ubuntu-1.3.3: Ensure sudo log file exists
>    ✔  Defaults logfile="/var/log/sudo.log"
>       is expected to include "Defaults logfile="
> 
> 
> Profile Summary: 3 successful controls, 0 control failures, 0 controls skipped
> Test Summary: 3 successful, 0 failures, 0 skipped
> ```

## Docker Compliance using Inspec

### Learn how to implement CIS Docker Benchmark using the Inspec tool

In this scenario, you will learn how to implement CIS Docker Benchmark checks as an Inspec profile.

### Install Inspec Tool

> Chef InSpec is an open-source framework for testing and auditing your applications and infrastructure. Chef InSpec works by comparing the actual state of your system with the desired state that you express in easy-to-read and easy-to-write Chef InSpec code. Chef InSpec detects violations and displays findings in the form of a report, but puts you in control of remediation.
>
>Source: [Inspec official website](https://www.inspec.io/docs/).

Let’s install the Inspec on the system to learn Compliance as code.

Download the **Inspec** Debian package from the InSpec website.

```bash
wget https://packages.chef.io/files/stable/inspec/4.18.114/ubuntu/16.04/inspec_4.18.114-1_amd64.deb
```

Install the downloaded package

```bash
dpkg -i inspec_4.18.114-1_amd64.deb
```

```bash
Selecting previously unselected package inspec.
(Reading database ... 11606 files and directories currently installed.)
Preparing to unpack inspec_4.18.114-1_amd64.deb ...
You're about to install InSpec!
Unpacking inspec (4.18.114-1) ...
Setting up inspec (4.18.114-1) ...
Thank you for installing InSpec!
```

We have successfully installed the Inspec tool, let’s explore the functionality it provides us.

```bash
inspec --help
```

```bash

Commands:
  inspec archive PATH                # archive a profile to tar.gz (default) or zip
  inspec artifact SUBCOMMAND         # Manage Chef InSpec Artifacts
  inspec check PATH                  # verify all tests at the specified PATH
  inspec compliance SUBCOMMAND       # Chef Compliance commands
  inspec detect                      # detect the target OS
  inspec env                         # Output shell-appropriate completion configuration
  inspec exec LOCATIONS              # Run all test files at the specified LOCATIONS. Loads the given profile(s) and fetches their dependen...
  inspec habitat SUBCOMMAND          # Manage Habitat with Chef InSpec
  inspec help [COMMAND]              # Describe available commands or one specific command
  inspec init SUBCOMMAND             # Generate InSpec code
  inspec json PATH                   # read all tests in PATH and generate a JSON summary
  inspec nothing                     # does nothing
  inspec plugin SUBCOMMAND           # Manage Chef InSpec and Train plugins
  inspec shell                       # open an interactive debugging shell
  inspec supermarket SUBCOMMAND ...  # Supermarket commands
  inspec vendor PATH                 # Download all dependencies and generate a lockfile in a `vendor` directory
  inspec version                     # prints the version of this tool

Options:
  l, [--log-level=LOG_LEVEL]                         # Set the log level: info (default), debug, warn, error
      [--log-location=LOG_LOCATION]                  # Location to send diagnostic log messages to. (default: $stdout or Inspec::Log.error)
      [--diagnose], [--no-diagnose]                  # Show diagnostics (versions, configurations)
      [--color], [--no-color]                        # Use colors in output.
      [--interactive], [--no-interactive]            # Allow or disable user interaction
      [--disable-core-plugins]                       # Disable loading all plugins that are shipped in the lib/plugins directory of InSpec. Useful in development.
      [--disable-user-plugins]                       # Disable loading all plugins that the user installed.
      [--enable-telemetry], [--no-enable-telemetry]  # Allow or disable telemetry
      [--chef-license=CHEF_LICENSE]                  # Accept the license for this product and any contained products: accept, accept-no-persist, accept-silent
```

Let’s move to the **next step**.

### Run the Inspec profile

In addition to scanning a host, we can also use Inspec to inspect a running container or the Docker Daemon itself. We can define compliance controls in our organization and avoid running containers that do not satisfy the organization’s compliance baselines.

Lets try to check whether our servers follow the **CIS Docker Benchmark** best practices using the Dev-Sec’s **cis-docker-benchmark** Inspec profile.

Let’s run the profile against the DevSecOps Box(local machine).

```bash
inspec exec https://github.com/dev-sec/cis-docker-benchmark --chef-license accept
```

```bash
+---------------------------------------------+
✔ 1 product license accepted.
+---------------------------------------------+
[2021-02-14T04:47:07+00:00] WARN: URL target https://github.com/dev-sec/cis-docker-benchmark transformed to https://github.com/dev-sec/cis-docker-benchmark/archive/master.tar.gz. Consider using the git fetcher

Profile: CIS Docker Benchmark Profile (cis-docker-benchmark)
Version: 2.1.2
Target:  local://

  ×  docker-4.2: Use trusted base images for containers
     ×  Environment variable DOCKER_CONTENT_TRUST content is expected to eq "1"

     expected: "1"
          got: nil

     (compared using ==)

  ↺  docker-4.3: Do not install unnecessary packages in the container
     ↺  Do not install unnecessary packages in the container
  ↺  docker-4.4: Rebuild the images to include security patches
     ↺  Rebuild the images to include security patches
  ×  docker-4.5: Enable Content trust for Docker
     ×  Environment variable DOCKER_CONTENT_TRUST content is expected to eq "1"

     expected: "1"
          got: nil

     (compared using ==)

  ↺  docker-4.8: Remove setuid and setgid permissions in the images
     ↺  Use DevSec Linux Baseline in Container
  ↺  docker-4.10: Do not store secrets in Dockerfiles
     ↺  Manually verify that you have not used secrets in images

  ...[SNIP]...

  ×  host-1.7: Audit docker daemon (4 failed)
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
     ×  Service auditd is expected to be installed
     expected that `Service auditd` is installed
     ×  Service auditd is expected to be enabled
     expected that `Service auditd` is enabled
     ×  Service auditd is expected to be running
     expected that `Service auditd` is running
  ×  host-1.8: Audit Docker files and directories - /var/lib/docker
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
  ×  host-1.9: Audit Docker files and directories - /etc/docker
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
  ↺  host-1.10: Audit Docker files and directories - docker.service
     ↺  Cannot determine docker path
  ↺  host-1.11: Audit Docker files and directories - docker.socket
     ↺  Cannot determine docker socket
  ×  host-1.12: Audit Docker files and directories - /etc/default/docker
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
  ×  host-1.13: Audit Docker files and directories - /etc/docker/daemon.json
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
  ×  host-1.14: Audit Docker files and directories - /usr/bin/docker-containerd
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist
  ×  host-1.15: Audit Docker files and directories - /usr/bin/docker-runc
     ×  Auditd Rules
     Command `/sbin/auditctl` does not exist


Profile Summary: 15 successful controls, 25 control failures, 35 controls skipped
Test Summary: 79 successful, 80 failures, 36 skipped
```

You can see, the output does inform us about **15** successful controls and **25** control failures.

The above profile not only helps us in scanning for daemon related misconfigurations but also running containers. Let’s try to run a docker container and see this functionality in action!

```bash
docker run -d --name alpine -it alpine /bin/sh
```

We can see all the running containers on a machine using the **docker ps** command.

```bash
docker ps
```

```bash
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
871af75c081a        alpine              "/bin/sh"           1 minute ago      Up 1 minute                           alpine
```

We will now run the **linux-baseline** profile against this container.

```bash
inspec exec https://github.com/dev-sec/linux-baseline --chef-license accept -t docker://alpine
```

> You can also use container name or container id as URI target

```bash
[2021-02-14T05:51:53+00:00] WARN: URL target https://github.com/dev-sec/linux-baseline transformed to https://github.com/dev-sec/linux-baseline/archive/master.tar.gz. Consider using the git fetcher

Profile: DevSec Linux Security Baseline (linux-baseline)
Version: 2.6.4
Target:  docker://871af75c081a67c2ad05d1dacb1eb81960c4b064823cf58aa0ea11c254ff3a2f

  ✔  os-01: Trusted hosts login
     ✔  File /etc/hosts.equiv is expected not to exist
  ×  os-02: Check owner and permissions for /etc/shadow (1 failed)
     ✔  File /etc/shadow is expected to exist
     ✔  File /etc/shadow is expected to be file
     ✔  File /etc/shadow is expected to be owned by "root"
     ✔  File /etc/shadow is expected not to be executable
     ✔  File /etc/shadow is expected not to be readable by other
     ✔  File /etc/shadow group is expected to eq "shadow"
     ✔  File /etc/shadow is expected to be writable by owner
     ✔  File /etc/shadow is expected to be readable by owner
     ×  File /etc/shadow is expected not to be readable by group
     expected File /etc/shadow not to be readable by group

  ...[SNIP]...

  ↺  sysctl-31b: Secure Core Dumps - dump path
     ↺  Skipped control due to only_if condition.
  ↺  sysctl-32: kernel.randomize_va_space
     ↺  Skipped control due to only_if condition.
  ↺  sysctl-33: CPU No execution Flag or Kernel ExecShield
     ↺  Skipped control due to only_if condition.


Profile Summary: 15 successful controls, 2 control failures, 39 controls skipped
Test Summary: 39 successful, 8 failures, 40 skipped
```

Now that we have a container running, what would happen if we run the **cis-docker-benchmark** profile again on this machine?

```bash
inspec exec https://github.com/dev-sec/cis-docker-benchmark --chef-license accept
```

```bash
...[SNIP]...

Profile Summary: 29 successful controls, 36 control failures, 35 controls skipped
Test Summary: 100 successful, 94 failures, 36 skipped
```

The output changes from 15 successful controls to 29 and 25 control failures to 36.

This change is because of skipped container runtime checks as no containers were running when we first used this inspec profile. After we started a container, Inspec also included runtime checks as part of the scan.

We can verify this behaviour by visiting [https://github.com/dev-sec/cis-docker-benchmark](https://github.com/dev-sec/cis-docker-benchmark) and selecting the **container_runtime.rb** under the **controls** directory.