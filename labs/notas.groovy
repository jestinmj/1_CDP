How to Embed TruffleHog into Jenkins Optional
Use TruffleHog tool to perform secrets scanning in Jenkins CI/CD pipeline
In this scenario, you will learn how to embed Secrets Scanning in Jenkins CI/CD pipeline.

Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab and Jenkins machine to start.

Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We have already set up the Jenkins machine with several plugins to help you do the exercise

Create a new job
The Jenkins system is already configured with GitLab. If you wish to know how to configure Jenkins with GitLab, you can check out this link.

We will create a new job in Jenkins by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.

You can use the following details to log into Jenkins.

Name	Value
Username	root
Password	pdso-training
Provide a name for your new item, e.g., django.nv, select the Pipeline option, and click on the OK button.

In the next screen, click on the Build Triggers tab, check the Build when a change is pushed to GitLab..... checkbox.

At the bottom right-hand side, just below the Comment (regex) for triggering a build form field, you will find the Advanced… button. Please click on it.

Then, click on the Generate button under Secret token to generate a token. We will use this token for GitLab’s Webhook Settings. This webhook setting will allow GitLab to let Jenkins know whenever a change is made to the repository.

Please visit the following GitLab URL to set up the Jenkins webhook.

Name	Value
URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/hooks
Fill the form using the following details.

Name	Value
URL	https://jenkins-xIKciVAk.lab.practical-devsecops.training/project/django.nv
Secret Token	Paste the secret token we just generated above.
Click on the Add webhook button, and go back to the Jenkins tab to continue the setup process.

Click on the Pipeline tab, and select Pipeline script from SCM from Definition options. Once you do that, few more options would be made available to you.

Select Git as SCM, enter our django.nv repository http url.

Name	Value
Repository URL	http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git
Since we haven’t added the GitLab credentials to the Jenkins system, you will get an error.

Failed to connect to repository : Command "git ls-remote -h -- http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv HEAD" returned status code 128:
stdout:
stderr: remote: HTTP Basic: Access denied
fatal: Authentication failed for 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git/'
Let’s add the credentials by clicking on the Add button (the one with a key symbol). Select the Jenkins option and fill the pop-up form with the following details.

Name	Value
Username	root
Password	pdso-training
ID	gitlab-auth
Description	Leave it blank as it’s optional
Click on the Add button, and select our new credentials from the Credentials Dropdown.

The error we experienced before should be gone by now.

Finally, click the Save button.

Let’s move to the next step.
A simple CI/CD pipeline
Considering your DevOps team created a simple Jenkinsfile with the following contents.

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
    }
    post {
        failure {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'failed')
        }
        unstable {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'failed')
        }
        success {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'canceled')
        }
        always {
            deleteDir()                     // clean up workspace
        }
    }
}
We have two jobs in this pipeline, a build stage and a test stage. As a security engineer, I do not care what they are doing as part of these stages. Why? Imagine having to learn every build/testing tool used by your DevOps team. It will be a nightmare. Instead, rely on the DevOps team for help.

Let’s login into Gitlab using the following details.

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
Username	root
Password	pdso-training
Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the Commit changes button.

Verify the pipeline run
Since we want to use Jenkins to execute the CI/CD jobs, we need to remove .gitlab-ci.yml from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the Gitlab Runner and the Jenkins systems.

Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the .gitlab-ci.yaml file is missing.

Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Challenge
Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

Read the Trufflehog documentation
Embed Trufflehog as git-secrets stage and ensure the stage is running
You can either install the Trufflehog manually or use hysnsec/trufflehog docker container
Follow all the best practices while embedding Trufflehog in the CI/CD pipeline. Don’t forget the tool evaluation criteria
Can you figure out why Trufflehog didn’t give output or file after scanning is done?
Please try to do this exercise without looking at the solution on the next page.

Let’s move to the next step.
Embed TruffleHog in Jenkins
As discussed in the Secrets Scanning exercise, we can embed TruffleHog in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the pipeline.

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
        stage("git-secrets") {
            steps {
                sh "docker run -v \$(pwd):/src --rm hysnsec/trufflehog file:///src --json" 
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
As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

As you can, Trufflehog didn’t give us any output as we did on the DevSecOps Box. Why?

Let’s try running the git status command after the Trufflehog command and see the output.

HEAD detached at c948d9c
nothing to commit, working tree clean
The output tells us that HEAD is detached; what does it mean? It means we’re not on a branch but checked out a specific commit in the history. Trufflehog needs us to be on a branch. Hence Trufflehog didn’t find any secrets.

        stage("git-secrets") {
            steps {
                git credentialsId: 'gitlab-auth', url: 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git'
                sh "docker run -v \$(pwd):/src --rm hysnsec/trufflehog file:///src --json"
            }
        }
The git plugin fetches commits from one or more remote repositories and performs a checkout in the agent workspace.

The git function helps us in cloning the source code and then checking out the master branch.

Let’s move to the next step.
Allow the stage failure
Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the catchError function to “not fail the build” even though the tool found security issues.

Reference: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps.

        stage("git-secrets") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    git credentialsId: 'gitlab-auth', url: 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git'
                    sh "docker run -v \$(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json "
                }
            }
            ...
After adding the catchError function, the pipeline would look like the following.

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
        stage("git-secrets") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    git credentialsId: 'gitlab-auth', url: 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git'
                    sh "docker run -v \$(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trufflehog-output.json', fingerprint: true
                }
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
You will notice that the git-secrets stage failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.
How to Embed TruffleHog into GitHub Actions Optional
Use TruffleHog tool to perform SAST in GitHub Actions
In this scenario, you will learn how to embed SAST in GitHub Actions.

You will learn to use TruffleHog in GitHub Actions and how to allow job failure when the tool found several issues.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again. 
A simple CI/CD pipeline
You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named GitHub Actions that debuted on 13 November 2019. GitHub Actions is a CI/CD system that is built-in to GitHub with free and paid offerings.

Let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named django.nv, you can also check the box with Public or Private options, and please ignore Initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens,then click Generate new token button and give your token a name e.g. django.

Select repo option to access repositories from the command line, and scroll down to generate a new token.

The token will have a format like this ghp_xxxxxxxxx.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use the token whenever needed.

3. Initial git setup
To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

git config --global user.email "your_email@gmail.com"

git config --global user.name "your_username"

You need to use your email and username, which are registered in GitHub.

Please don’t use your company’s GitHub credentials or token to practice these exercises.

4. Download the repository
Let’s start by cloning django.nv in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd django.nv

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/django.nv.git

Let’s check the status of our git repository.

git status

On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
We are in the master branch and we need to create one more branch called main as a default branch.

git checkout -b main

Why do we need a new branch? Because in this exercise we will use the main branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.

Read more about Renaming the default branch from master.

Then, let’s push the code to the GitHub repository.

git push -u origin main

And enter your GitHub credentials when prompted (please use Personal Access Token as a password), then the code will be pushed to the GitHub repository.

5. Add a workflow file to the repository
To use GitHub Actions, you need to create .github/workflows directory and create a new YAML file named main.yaml or any other desired name because each file in the .github/workflows directory which has a .yaml extension will define a workflow.

Let’s create a simple workflow by entering the following commands in DevSecOps Box.

mkdir -p .github/workflows

cat >.github/workflows/main.yaml<<EOF
name: Django                                  # workflow name

on:
  push:                                       
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py check

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py test taskManager

  integration:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - run: echo "This is an integration step"
      - run: exit 1
        continue-on-error: true

  prod:
    runs-on: ubuntu-latest
    needs: integration
    steps:
      - run: echo "This is a deploy step."
EOF
If you are not comfortable with the syntax, explore the GitHub Actions syntax athttps://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#about-yaml-syntax-for-workflows

Let’s add this file to the repository and commit the changes.

git add .github/workflows/main.yaml

git commit -m "Add github workflows"

6. Push the changes to the repository
Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the git push command.

git push origin main

Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
7. Verify the pipeline runs
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.

You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

Let’s move to the next step.
Embed TruffleHog in GitHub Actions
As discussed in the Static Analysis using TruffleHog exercise, we can embed TruffleHog in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and copy the below content to the .github/workflows/main.yaml file under test job.

  secret_scanning:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json

      - uses: actions/upload-artifact@v2
        with:
          name: TruffleHog
          path: trufflehog-output.json
        if: always()                        # what is this for?
To understand if: always() Please refer to conditionals.

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.

Allow the job failure
We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the continue-on-error syntax to not fail the build even though the tool found security issues.

  secret_scanning:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json
        continue-on-error: true             # allow the build to fail, similar to "allow_failure: true" in GitLab

      - uses: actions/upload-artifact@v2
        with:
          name: TruffleHog
          path: trufflehog-output.json
        if: always()                        # what is this for?
After adding the continue-on-error syntax, the pipeline would look like the following:

name: Django                                  # workflow name

on:
  push:
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py check

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py test taskManager

  secret_scanning:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json
        continue-on-error: true             # allow the build to fail, similar to "allow_failure: true" in GitLab

      - uses: actions/upload-artifact@v2
        with:
          name: TruffleHog
          path: trufflehog-output.json
        if: always()                        # what is this for?

  integration:
    runs-on: ubuntu-latest
    needs: secret_scanning
    steps:
      - run: echo "This is an integration step"
      - run: exit 1
        continue-on-error: true

  prod:
    runs-on: ubuntu-latest
    needs: integration
    steps:
      - run: echo "This is a deploy step."
Go ahead and add the above content to the .github/workflows/main.yaml file to run the pipeline.

You will notice that the secret_scanning job has failed but didn’t block the other jobs from running.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.
How to Embed TruffleHog into CircleCI Optional
Use TruffleHog tool to perform SAST in CircleCI
In this scenario, you will learn how to embed SAST in CircleCI.

You will learn to use TruffleHog in CircleCI and how to allow job failure when the tool found several issues.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.
Initial Setup
You’ve learned about CI/CD systems such as GitLab, Jenkins, GitHub Actions and so on. Remember every CI/CD system has its own advantages, and limitations, we just need to find what is suitable for our needs.

Now, we will look into another CI/CD system called CircleCI, this system doesn’t have a built-in Git repository like GitLab or GitHub. But it can be integrated with GitHub or Bitbucket as the repository, so let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named django.nv, you can also check the box with Public or Private options, and please ignore Initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens, then click Generate new token button and give your token a name e.g. django.

Select repo option to access repositories from the command line and scroll down to generate a new token.

The token will have a format like ghp_xxxxxxxxx.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use it whenever we needed.

3. Initial git setup
To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

git config --global user.email "your_email@gmail.com"

git config --global user.name "your_username"

You need to use your email and username, which are registered in GitHub.

Please don’t use your company’s GitHub credentials or token to practice these exercises.

4. Download the repository
Let’s start by cloning django.nv in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd django.nv

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/django.nv.git

Let’s check the status of our git repository.

git status

On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
We are in the master branch and we need to create one more branch called main as a default branch.

git checkout -b main

Why we need to do this? Because in this exercise we will use the main branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.

Read more about Renaming the default branch from master.

Then, let’s push the code to the GitHub repository.

git push -u origin main

And enter your GitHub credentials when prompted (please use Personal Access Token as a password), then the code will be pushed to the GitHub repository.

5. Create an account in CircleCI
If you’ve already created the CircleCI account, you can ignore this step.

To use CircleCI, we need to create an account by signing up at https://circleci.com/signup, click the Signup with GitHub button and you will be redirected to the page which tells you to allow CircleCI to access your GitHub account, accept it by clicking the Authorize circleci button.

Next, you will see the repository lists which has a button called Set Up Project. Select django.nv repository then click on that button to start using CircleCI as our CI/CD pipeline and you will get a pop-up with Select a config.yml file for django.nv message, please ignore it for now because we will create the CircleCI YML file in our GitHub repository.

Let’s move to the next step.

A simple CI/CD pipeline
You need to create .circleci directory and create a new YAML file named config.yml and add the following CI script.

mkdir -p .circleci

cat >.circleci/config.yml<<EOF
jobs:
  build:
    docker:
      - image: python:3.6                   # similar to "image" in GitLab
    steps:
      - checkout
      - run: |                              # similar to "script" in GitLab
          pip install -r requirements.txt
          python manage.py check

  test:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: |
          pip install -r requirements.txt
          python manage.py test taskManager

  integration:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: echo "This is a deploy step."

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build 
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
EOF
If you are not comfortable with the syntax, explore the CircleCI syntax at https://circleci.com/docs/2.0/configuration-reference/

Let’s add this file to the repository and commit the changes.

git add .circleci/config.yml

git commit -m "Add CircleCI config"

Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the git push command.

git push origin main

Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.

Let’s move to the next step.
Embed TruffleHog in CircleCI
As discussed in the Static Analysis using TruffleHog exercise, we can embed TruffleHog in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and copy the below content to the .circleci/config.yml file under test job.

  secret_scanning:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json

      - store_artifacts:
          path: trufflehog-output.json
          destination: trufflehog-artifact
Please remember to add the above job name to the workflows section as shown below:

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build
      - secret_scanning:
          requires:
            - test
      - integration:
          requires:
            - secret_scanning
      - prod:
          type: approval
          requires:
            - integration
Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.
Allow the job failure
We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the when: always syntax to not fail the build even though the tool found security issues.

  secret_scanning:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json

      - store_artifacts:
          path: trufflehog-output.json
          destination: trufflehog-artifact
          when: always              # Even if the job fails, continue to the next stages
The final pipeline would look like the following:

jobs:
  build:
    docker:
      - image: python:3.6                   # similar to "image" in GitLab
    steps:
      - checkout
      - run: |                              # similar to "script" in GitLab
          pip install -r requirements.txt
          python manage.py check

  test:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: |
          pip install -r requirements.txt
          python manage.py test taskManager

  secret_scanning:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json > trufflehog-output.json

      - store_artifacts:
          path: trufflehog-output.json
          destination: trufflehog-artifact
          when: always              # Even if the job fails, continue to the next stages

  integration:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1
          when: always                    # Even if the job fails, continue to the next stages

  prod:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: echo "This is a deploy step."

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build
      - secret_scanning:
          requires:
            - test
      - integration:
          requires:
            - secret_scanning
      - prod:
          type: approval
          requires:
            - integration
Go ahead and add the above content to the .circleci/config.yml file to run the pipeline.

You will notice that the secret_scanning job has failed but didn’t block the other jobs from running.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.

Secrets Scanning with detect-secrets Optional
Use detect-secrets to find secrets like token, ssh keys etc.
In this scenario, you will learn how to install and run detect-secrets to Scans for Secret on a git repository.

You will need to download the code, install the Secret Scanning tool, and then finally run the Secret scan on the code.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory

Let’s move to the next step.
Install detect-secrets
detect-secrets is a tool that designed with the enterprise client in mind: providing a backwards compatible to preventing new secrets from entering the code base and etc.

You can find more details about the project at https://github.com/Yelp/detect-secrets.

Let’s install the detect-secrets tool on the system to scan for the secrets in our code.

pip3 install detect-secrets

Collecting detect-secrets
  Downloading detect_secrets-1.0.3-py2.py3-none-any.whl (101 kB)
     |████████████████████████████████| 101 kB 14.0 MB/s 
Requirement already satisfied: pyyaml in /usr/local/lib/python3.6/dist-packages (from detect-secrets) (5.3.1)
Collecting requests
  Downloading requests-2.25.1-py2.py3-none-any.whl (61 kB)
     |████████████████████████████████| 61 kB 16.7 MB/s 
Collecting certifi>=2017.4.17
  Downloading certifi-2020.12.5-py2.py3-none-any.whl (147 kB)
     |████████████████████████████████| 147 kB 35.8 MB/s 
Collecting chardet<5,>=3.0.2
  Downloading chardet-4.0.0-py2.py3-none-any.whl (178 kB)
     |████████████████████████████████| 178 kB 46.5 MB/s 
Collecting idna<3,>=2.5
  Downloading idna-2.10-py2.py3-none-any.whl (58 kB)
     |████████████████████████████████| 58 kB 14.4 MB/s 
Collecting urllib3<1.27,>=1.21.1
  Downloading urllib3-1.26.3-py2.py3-none-any.whl (137 kB)
     |████████████████████████████████| 137 kB 50.7 MB/s 
Installing collected packages: urllib3, idna, chardet, certifi, requests, detect-secrets
Successfully installed certifi-2020.12.5 chardet-4.0.0 detect-secrets-1.0.3 idna-2.10 requests-2.25.1 urllib3-1.26.3
Let’s explore what options detect-secrets provides us.

detect-secrets --help

usage: detect-secrets [-h] [-v] [--version] {scan,audit} ...

positional arguments:
  {scan,audit}

optional arguments:
  -h, --help     show this help message and exit
  -v, --verbose  Verbose mode.
  --version      Display version information.
Let’s move to the next step.

Run the Scanner
As we have learned in the DevSecOps Gospel, we should save the output in the machine-readable format. JSON, CSV, XML formats can be parsed by the machines easily.

Let’s run the detect-secrets command with the scan argument to find any secrets in our code.

detect-secrets scan

{
  "custom_plugin_paths": [],
  "exclude": {
    "files": null,
    "lines": null
  },
  "generated_at": "2020-11-05T10:15:53Z",
  "plugins_used": [
    {
      "name": "AWSKeyDetector"
    },
    {
      "name": "ArtifactoryDetector"
    },
    {
      "base64_limit": 4.5,
      "name": "Base64HighEntropyString"
    },
    {
      "name": "BasicAuthDetector"
    },
    {
      "name": "CloudantDetector"
    },
    {
      "hex_limit": 3,
      "name": "HexHighEntropyString"
    },
    {
      "name": "IbmCloudIamDetector"
    },
    {
      "name": "IbmCosHmacDetector"
    },
    {
      "name": "JwtTokenDetector"
    },
    {
      "keyword_exclude": null,
      "name": "KeywordDetector"
    },
    {
      "name": "MailchimpDetector"
    },
    {
      "name": "PrivateKeyDetector"
    },
    {
      "name": "SlackDetector"
    },
    {
      "name": "SoftlayerDetector"
    },
    {
      "name": "StripeDetector"
    },
    {
      "name": "TwilioKeyDetector"
    }
  ],
  "results": {
    "fixtures/users.json": [
      {
        "hashed_secret": "67769bb527c3288a11758fa2c352a459184e0387",
        "is_verified": false,
        "line_number": 9,
        "type": "Secret Keyword"
      },
      {
        "hashed_secret": "dddf4e86c1ad748ea11da36e95855daf2d94cc82",
        "is_verified": false,
        "line_number": 23,
        "type": "Secret Keyword"
      },
      {
        "hashed_secret": "0bc240d52d1d053b4af505f6b3a72de130b944ba",
        "is_verified": false,
        "line_number": 37,
        "type": "Secret Keyword"
      },
      {
        "hashed_secret": "7d1061f95347b8e9672be204f5d2b4628a454aab",
        "is_verified": false,
        "line_number": 51,
        "type": "Secret Keyword"
      },
      {
        "hashed_secret": "487ee8358599cd09964066d5c746b7394649b97c",
        "is_verified": false,
        "line_number": 65,
        "type": "Secret Keyword"
      }
    ],
    "taskManager/templates/taskManager/register.html": [
      {
        "hashed_secret": "4275be7cb54a0f1cb835d5f5ea7bda93b1803887",
        "is_verified": false,
        "line_number": 34,
        "type": "Secret Keyword"
      }
    ]
  },
  "version": "0.14.3",
  "word_list": {
    "file": null,
    "hash": null
  }
}
It seems detect-secrets‘s default output format is JSON format. Let’s store the output/results in a file.

detect-secrets scan > secrets-output.json

We can also mark the issues/findings as false-positives in the output file using the audit command. You can accept an issue as a real issue with the letter y for yes and n for no.

Go ahead and mark the issues appropriately.

detect-secrets audit secrets-output.json

Secret:      1 of 6
Filename:    fixtures/users.json
Secret Type: Secret Keyword
----------
4:    "pk": 1,
5:    "fields": {
6:      "first_name" : "",
7:      "last_name" : "",
8:      "username" : "admin",
9:      "password": "md5$oAKvI66ce0Xq$a5c1836db3d6dedff5deca630a358d8b",
10:      "is_superuser" : true,
11:      "email" : "admin@tm.com",
12:      "is_staff" : true,
13:      "is_active" : true
14:    }
----------
Is this a valid secret? i.e. not a false-positive __(y)es, (n)o, (s)kip, (q)uit__:
Did you notice it’s much more efficient than Trufflehog?

Let’s move to the next step.
Challenge
Explore the documentation of detect-secrets tool
Reduce the false positives and embed this tool in the CI/CD pipeline
Remember to keep the DevSecOps Gospel best practices in mind while doing this exercise
Note: you can access your GitLab machine by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training and use the credentials mentioned in the first step.

Please do not forget to share the answer with our staff via Slack Direct Message (DM).

Secrets Scanning with Talisman Optional
A tool to detect and prevent secrets from getting checked in
In this scenario, you will learn how to install and run the talisman tool.

This tool will help you in avoiding DevOps teams accidentally commiting secrets to the git repositories.

To achieve the above objectives, you will need to do the following.

Install the git pre-commit and pre-push hook tool.
Clone/download the source code.
Configure the pre-commit/pre-push tool on a repo.
Test to ensure the utility works fine.
Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab machine to start.

Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working
Install Talisman
Talisman is a tool that installs a hook to your repository to ensure that potential secrets or sensitive information do not leave the developer’s workstation.
It validates the outgoing changeset for things that look suspicious - such as potential SSH keys, authorization tokens, private keys, etc.

Source: Talisman Repoistory.

This tool looks great, however it is far from perfect.

Here’s why.

Pre-commit/Pre-push hooks are configured only on a developer’s workstation.
If a developer has administrator access, he/she can easily disable these checks.
There’s no way to enforce it across the organization.
Please prefer to use secrets scanning tools as part of the CI/CD pipeline because of the above reasons. Embedding secret scanning tools provide the best of both worlds, i.e., enforcement and visibility.

Name	Value
Gitlab URL:	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/
Username:	root
Password:	pdso-training
We will do all the exercises locally in DevSecOps-Box, and before installing the tool, we need to understand what is a git hook.

Git hooks are scripts that run before or after git events like commit, push, rebase, etc., and are run locally. You can see a list of git hooks here.

There are two types of git hooks supported by Talisman.

pre-commit
pre-push
In this exercise, we will use pre-commit hook to prevent our developers from adding a sensitive file to our git repository.

You can install Talisman with the pre-commit hook, using the following command.

curl --silent https://raw.githubusercontent.com/thoughtworks/talisman/master/global_install_scripts/install.bash > /tmp/install_talisman.bash && /bin/bash /tmp/install_talisman.bash pre-commit

Or you can add pre-push as an argument to use the pre-push hook.

curl --silent https://raw.githubusercontent.com/thoughtworks/talisman/master/global_install_scripts/install.bash > /tmp/install_talisman.bash && /bin/bash /tmp/install_talisman.bash pre-push

Downloading talisman binary
talisman_linux_amd64: OK


Setting up talisman binary and helper script in /root/.talisman
Setting up TALISMAN_HOME in path


PLEASE CHOOSE WHERE YOU WISH TO SET TALISMAN_HOME VARIABLE AND talisman binary PATH (Enter option number):
1) Set TALISMAN_HOME in ~/.bashrc
2) Set TALISMAN_HOME in ~/.bash_profile
3) Set TALISMAN_HOME in ~/.profile
4) I will set it later
#? 1


Setting up interaction mode

DO YOU WANT TO BE PROMPTED WHEN ADDING EXCEPTIONS TO .talismanrc FILE?
Enter y to install in interactive mode. Press any key to continue without interactive mode (y/n):m
Setting up TALISMAN_HOME in /root/.bashrc
After the installation is complete, you will need to manually restart the terminal or source /root/.bashrc file
Press any key to continue ...
Setting up pre-commit hook in git template directory
No git template directory is configured. Let's add one.
(this will override any system git templates and modify your git config file)

Git template directory: (/root/.git-template)

Setting up template pre-commit hook
'/root/.git-template/hooks/pre-commit' -> '/root/.talisman/bin/talisman_hook_script'
Talisman template hook successfully installed.

Setting up talisman hook recursively in git repos
Please enter root directory to search for git repos (Default: /root):
        Searching /root for git repositories
We have successfully installed the Talisman tool globally in our DevSecOps Box, so if there is any new repository that we clone or init, it will use Talisman as git hooks.

Let’s move to the next step
Download the source code
Next, let’s clone our code from Gitlab.

git clone https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

To see Talisman in action, we will try to add a sensitive file to this repository.

Let’s copy SSH Private Key into the current directory.

cp ~/.ssh/id_rsa .

git add id_rsa

git commit -m "Add private key"

*** Please tell me who you are.

Run

  git config --global user.email "you@example.com"
  git config --global user.name "Your Name"

to set your account's default identity.
Omit --global to set the identity only in this repository.

fatal: unable to auto-detect email address (got 'root@devsecops-box-krqItici.(none)')
The above error appears because we haven’t added any git identity on the system. Let’s follow the provided suggestions to configure the git.

git config --global user.email "student@pdevsecops.com"

git config --global user.name "DevSecOps Student"

Once done, you can re-execute the git commit command to see Talisman preventing us from committing the Private SSH key.

Talisman Scan: 3 / 3 <--------------------------------------------------------------------------------------------------------------------> 100.00%

Talisman Report:
+--------+------------------------------------------------------------------+----------+
|  FILE  |                              ERRORS                              | SEVERITY |
+--------+------------------------------------------------------------------+----------+
| id_rsa | The file name "id_rsa" failed                                    | high     |
|        | checks against the pattern                                       |          |
|        | ^.+_rsa$                                                         |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | MIIEpAIBAAKCAQEAwNpZcFqfOVT0Q486JHr7kQO7HGD2zsG...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | OUM633CIM0UYSqRTaOIRAi+HowZ5W0/8ZKW3okMQTQNjjY7...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | dAimRGlgm1ycE/ucKXShiH9iSSeIrtkUiHWxvRssxSjdT0l...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | CDoeSXRM+EtFBkc45Mbyc7VCIyYcW2gUmFCLr8js/vUuGJY...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | 5tHjKd+qNCs0HhytWbhVWDwl46uigIrvwk6gaQXgVv1djrP...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | DrPZspBia9bxShKbL+zGcyoVgAy/yvdM3peI6QIDAQABAoI...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | xY+UYXxa7OH3yChUWClXATpiKHtbzo7CTpSBcbK1WOaIYQ2...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | fzBNpKpU5I3A6rjH2AHoId2iDAvuEBaJIUeN6ijhJeMQgxJ...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | TmLJDpMl9YdoCQ8rYJ6ccP5DKu7hF9fQA/8V5/OAkj2XKjQ...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | zw/P5DnRTzKQPG58u2TMb8cPKmi9MAJUAeFd1k2uBxBWXdI...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | iYsNQtDRiVLDu1+CJ5ddo8IEFBRJVk8xUD9vjAocplINgc9...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | wv2ZJG1RAoGBAP4tdWTj2ds7TS4yBoOGLHdXDIPHHBHV8g7...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | tHR5ykp7o2OqSH+sVDzK4tGy50dWZzw+m4lR3714C4BWx89...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | OPAykJtrKBYPJZ3Lan90HnVkU5MD4blmRrdHFrq6idYaDXC...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | VGw9C1iDPz1PcLOmV3uVpNJFLud0gSaTtUyouGJPbauvwcn...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | 700OvMDH/YUCE5YB0VtZ0y/55s/qGEp8LRzYFUUxHB38Yne...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | 2veoxdT4FO/U6Stg0aLXQxakyFj7AcDKl1WcCzh9AoGAEHu...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | zAlvHFSbM2Tmb6PbAhcKlHavCgVeLvPri+oh/jaKX/o4/V6...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | ndQbaFQSmt4F0yHIUIGsP0gjzFc8gen+cUU2L34BeXy9+b+...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | ro4DoVRbf/DskjGXUzWNblkCgYAMWBMxccu3y1eIiPTrpeW...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | KaPWM6APqjLRpeb+EGgCQQTtQpqFwnZa2lXqlospGdGu1dy...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | 8uGfLRjRWwnS+q+erFI1lVp0HT1Mpu+Fj8dK2p1SBKDw7c1...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | ySD5bQKBgQDJt2QT2oxFP/UYU1OQO1Vu38U82Yw5F7XCFE5...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | sCXOqlg8u/Hat/tsAI4WNjyjXSLLdCvF85d6Sx2/pbPMfNB...               |          |
+--------+------------------------------------------------------------------+----------+
| id_rsa | Expected file to not to contain                                  | high     |
|        | base64 encoded texts such as:                                    |          |
|        | 1PSx8Bw50vWJVZFlHSC0thG7psyrflbOoLBKdwfvBkrfSrp...               |          |
+--------+------------------------------------------------------------------+----------+
            | high     |ret pattern : BEGIN RSA PRIVATE KEY-----
 |          |IEpAIBAAKCAQEAwNpZcFqfOVT0Q486JHr7kQO7HGD2zsGC4iodBxc6bdU/S1zn
|        | OUM633CIM0UYSqRTaOIRAi+HowZ5W                                    |          |
                              |          |PEi2
 |          |imRGlgm1ycE/ucKXShiH9iSSeIrtkUiHWxvRssxSjdT0lGhB/z7PN2hu0mpj2k
 |          |oeSXRM+EtFBkc45Mbyc7VCIyYcW2gUmFCLr8js/vUuGJYu/FQp8yZDvVg8+bBx
 |          |HjKd+qNCs0HhytWbhVWDwl46uigIrvwk6gaQXgVv1djrPNh9eYYLoXSwTfo//c
 |          |PZspBia9bxShKbL+zGcyoVgAy/yvdM3peI6QIDAQABAoIBAQCubpCJFB6CT7nj
 |          |+UYXxa7OH3yChUWClXATpiKHtbzo7CTpSBcbK1WOaIYQ2YrcsXyaoSrQTkyr1H
 |          |BNpKpU5I3A6rjH2AHoId2iDAvuEBaJIUeN6ijhJeMQgxJU7LaRtIFKodU3T7/M
 |          |LJDpMl9YdoCQ8rYJ6ccP5DKu7hF9fQA/8V5/OAkj2XKjQg7APEYV+nFjYvcpl6
 |          |/P5DnRTzKQPG58u2TMb8cPKmi9MAJUAeFd1k2uBxBWXdIXHqDH7fHNFKlb5vaP
 |          |sNQtDRiVLDu1+CJ5ddo8IEFBRJVk8xUD9vjAocplINgc9rRIlIKQDs4tHxoceU
 |          |2ZJG1RAoGBAP4tdWTj2ds7TS4yBoOGLHdXDIPHHBHV8g7V39TBCMO4aRfB2Xf+
 |          |R5ykp7o2OqSH+sVDzK4tGy50dWZzw+m4lR3714C4BWx89OZJw/5RJ0j2OSLpIp
 |          |AykJtrKBYPJZ3Lan90HnVkU5MD4blmRrdHFrq6idYaDXCHgBvC1undAoGBAMI8
 |          |w9C1iDPz1PcLOmV3uVpNJFLud0gSaTtUyouGJPbauvwcnVIxgeXjRO3sPMHEvI
 |          |0OvMDH/YUCE5YB0VtZ0y/55s/qGEp8LRzYFUUxHB38YnekNW9PCUZ5Fmbfcv5a
 |          |eoxdT4FO/U6Stg0aLXQxakyFj7AcDKl1WcCzh9AoGAEHuuMz67cAYmeSpxVbIr
 |          |lvHFSbM2Tmb6PbAhcKlHavCgVeLvPri+oh/jaKX/o4/V6Vj+OwVdz+NpgZ1cRR
 |          |QbaFQSmt4F0yHIUIGsP0gjzFc8gen+cUU2L34BeXy9+b+pRl6nYwGAkfYce0Nw
 |          |4DoVRbf/DskjGXUzWNblkCgYAMWBMxccu3y1eIiPTrpeWnaAI6jsUFVqUik36R
 |          |PWM6APqjLRpeb+EGgCQQTtQpqFwnZa2lXqlospGdGu1dy9Rn8ibGpbyk/S5ANl
 |          |GfLRjRWwnS+q+erFI1lVp0HT1Mpu+Fj8dK2p1SBKDw7c1E4RNVbBGDfihFXVqy
 |          |D5bQKBgQDJt2QT2oxFP/UYU1OQO1Vu38U82Yw5F7XCFE5GaUWv2n+ID/4kg6IR
 |          |XOqlg8u/Hat/tsAI4WNjyjXSLLdCvF85d6Sx2/pbPMfNBttDr6Oj0+CikklQCi
         |          |WJVZFlHSC0thG7psyrflbOoLBKdwfvBkrfSrpfyNeQGQ==
|        | -----END RSA PRIVATE KEY                                         |          |
+--------+------------------------------------------------------------------+----------+

What if we want to commit both Private and Public SSH keys for testing purposes?

You can do so by storing the sensitive filenames in the __.talismanrc__ file under the project's root directory.

fileignoreconfig:
- filename: id_rsa
  checksum: 416122aefc170aef929dbbd1e01626e9066d36f7cfd83c00e38d38b5b03a7c69
As expected, Talisman found a sensitive file(SSH private key) and stopped the file from being committed in to the repo. You can review supported rules and other configurations here.

Let’s move to the next step.
Run the Scanner
You can also use Talisman as a command-line utility instead of using it as a git hook.

Let’s explore what options Talisman provides us.

talisman -h

bash: talisman: command not found
What? We just installed it.

Oh, that’s right, we haven’t sourced the changes to the current bash environment.

source ~/.bashrc

There you go!

Usage of /root/.talisman/bin/talisman_linux_amd64:
  -c, --checksum string          checksum calculator calculates checksum and suggests .talismanrc format
  -d, --debug                    enable debug mode (warning: very verbose)
  -g, --githook string           either pre-push or pre-commit (default "pre-push")
      --ignoreHistory            scanner scans all files on current head, will not scan through git commit history
  -i, --interactive              interactively update talismanrc (only makes sense with -g/--githook)
  -p, --pattern string           pattern (glob-like) of files to scan (ignores githooks)
  -r, --reportdirectory string   directory where the scan reports will be stored
  -s, --scan                     scanner scans the git commit history for potential secrets
  -w, --scanWithHtml             generate html report (**Make sure you have installed talisman_html_report to use this, as mentioned in Readme**)
  -v, --version                  show current version of talisman
pflag: help requested
Let’s run the scanner to find any secrets in our code.

talisman --scan


d8888b. db    db d8b   db d8b   db d888888b d8b   db  d888b    .d8888.  .o88b.  .d8b.  d8b   db
88  `8D 88    88 888o  88 888o  88   `88'   888o  88 88' Y8b   88'  YP d8P  Y8 d8' `8b 888o  88
88oobY' 88    88 88V8o 88 88V8o 88    88    88V8o 88 88        `8bo.   8P      88ooo88 88V8o 88
88`8b   88    88 88 V8o88 88 V8o88    88    88 V8o88 88  ooo     `Y8b. 8b      88~~~88 88 V8o88
88 `88. 88b  d88 88  V888 88  V888   .88.   88  V888 88. ~8~   db   8D Y8b  d8 88   88 88  V888 db db
88   YD ~Y8888P' VP   V8P VP   V8P Y888888P VP   V8P  Y888P    `8888Y'  `Y88P' YP   YP VP   V8P VP VP


Talisman Scan: 555 / 555 <-------------------------------------------------------------------------------------------------------------------> 100.00%

Please check 'talisman_reports/data' folder for the talisman scan report
By default, Talisman outputs the results in the JSON format. You can check out the result file at talisman_reports/data/report.json,

Let’s move to the next step.

Challenge
Explore the documentation of talisman
Reduce the False Positives and embed this tool in the CI/CD pipeline
Remember to keep the DevSecOps Gospel best practices in mind while doing this exercise
Note: you can access your GitLab machine by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training and use the credentials mentioned in the first step.

Please do not forget to share the answer with our staff via Slack Direct Message (DM).
How to Embed Bandit into Jenkins Optional
Use Bandit tool to do SAST in Jenkins CI/CD pipeline
In this scenario, you will learn how to embed Bandit in the Jenkins CI/CD pipeline.

Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab and Jenkins machine to start.

Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We have already set up the Jenkins machine with several plugins to help you do the exercise
Create a new job
The Jenkins system is already configured with GitLab. If you wish to know how to configure Jenkins with GitLab, you can check out this link.

We will create a new job in Jenkins by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.

You can use the following details to log into Jenkins.

Name	Value
Username	root
Password	pdso-training
Provide a name for your new item, e.g., django.nv, select the Pipeline option, and click on the OK button.

In the next screen, click on the Build Triggers tab, check the Build when a change is pushed to GitLab..... checkbox.

At the bottom right-hand side, just below the Comment (regex) for triggering a build form field, you will find the Advanced… button. Please click on it.

Then, click on the Generate button under Secret token to generate a token. We will use this token for GitLab’s Webhook Settings. This webhook setting will allow GitLab to let Jenkins know whenever a change is made to the repository.

Please visit the following GitLab URL to set up the Jenkins webhook.

Name	Value
URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/hooks
Fill the form using the following details.

Name	Value
URL	https://jenkins-xIKciVAk.lab.practical-devsecops.training/project/django.nv
Secret Token	Paste the secret token we just generated above.
Click on the Add webhook button, and go back to the Jenkins tab to continue the setup process.

Click on the Pipeline tab, and select Pipeline script from SCM from Definition options. Once you do that, few more options would be made available to you.

Select Git as SCM, enter our django.nv repository http url.

Name	Value
Repository URL	http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git
Since we haven’t added the GitLab credentials to the Jenkins system, you will get an error.

Failed to connect to repository : Command "git ls-remote -h -- http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv HEAD" returned status code 128:
stdout:
stderr: remote: HTTP Basic: Access denied
fatal: Authentication failed for 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git/'
Let’s add the credentials by clicking on the Add button (the one with a key symbol). Select the Jenkins option and fill the pop-up form with the following details.

Name	Value
Username	root
Password	pdso-training
ID	gitlab-auth
Description	Leave it blank as it’s optional
Click on the Add button, and select our new credentials from the Credentials Dropdown.

The error we experienced before should be gone by now.

Finally, click the Save button.

Let’s move to the next step.

A simple CI/CD pipeline
Considering your DevOps team created a simple Jenkinsfile with the following contents.

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
We do have four stages in this pipeline, build, test, integration and prod. We did integrate SCA/OAST beforehand, so let’s carry it forward in this exercise as well.

As a security engineer, I do not care what they are doing as part of these stages. Why? Imagine having to learn every build/testing tool used by your DevOps team. It will be a nightmare. Instead, rely on the DevOps team for help.

Let’s login into GitLab using the following details.

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
Username	root
Password	pdso-training
Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the Commit changes button.

Verify the pipeline run
Since we want to use Jenkins to execute the CI/CD jobs, we need to remove .gitlab-ci.yml from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the Gitlab Runner and the Jenkins systems.

Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the .gitlab-ci.yaml file is missing.

Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Challenge
Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

Read the bandit documentation
Embed SAST backend tool, Bandit in sast stage
You can either install Bandit manually or use hysnsec/bandit docker container
Follow all the best practices while embedding Bandit in the CI/CD pipeline, and don’t forget the tool evaluation criteria
Please try to do this exercise without looking at the solution on the next page.

Let’s move to the next step.

 
 Terminal
Embed Bandit in Jenkins
As discussed in the Static Analysis using Bandit exercise, we can embed bandit in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the pipeline.

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
        stage("sast") {
            steps {
                sh "docker run -v \$(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json"
            }
            post {
                always {
                    archiveArtifacts artifacts: 'bandit-output.json', fingerprint: true
                }
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
As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Let’s move to the next step.

Allow the stage failure
Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the catchError function to “not fail the build” even though the tool found security issues.

Reference: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps.

        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    sh "docker run -v \$(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'bandit-output.json', fingerprint: true
                }
            }
        }
After adding the catchError function, the pipeline would look like the following.

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
        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh "docker run -v \$(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'bandit-output.json', fingerprint: true
                }
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
You will notice that the sast stage failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Notes
The above option mounts the current directory in the host (runner) to /src inside the container. This could also be -v /home/ubuntu/code:/src or c:\Users\username\code:/src if you were using windows.

Instead of manually removing the container after the scan, we can use the –rm option so the docker can clean up after itself.

How to Embed Bandit into GitHub Actions Optional
A simple CI/CD pipeline
You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named GitHub Actions that debuted on 13 November 2019. GitHub Actions is a CI/CD system that is built-in to GitHub with free and paid offerings.

Let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named django.nv, you can also check the box with Public or Private options, and please ignore Initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens,then click Generate new token button and give your token a name e.g. django.

Select repo option to access repositories from the command line, and scroll down to generate a new token.

The token will have a format like this ghp_xxxxxxxxx.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use the token whenever needed.

3. Initial git setup
To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

git config --global user.email "your_email@gmail.com"

git config --global user.name "your_username"

You need to use your email and username, which are registered in GitHub.

Please don’t use your company’s GitHub credentials or token to practice these exercises.

4. Download the repository
Let’s start by cloning django.nv in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd django.nv

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/django.nv.git

Let’s check the status of our git repository.

git status

On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
We are in the master branch and we need to create one more branch called main as a default branch.

git checkout -b main

Why do we need a new branch? Because in this exercise we will use the main branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.

Read more about Renaming the default branch from master.

Then, let’s push the code to the GitHub repository.

git push -u origin main

And enter your GitHub credentials when prompted (please use Personal Access Token as a password), then the code will be pushed to the GitHub repository.

5. Add a workflow file to the repository
To use GitHub Actions, you need to create .github/workflows directory and create a new YAML file named main.yaml or any other desired name because each file in the .github/workflows directory which has a .yaml extension will define a workflow.

Let’s create a simple workflow by entering the following commands in DevSecOps Box.

mkdir -p .github/workflows

cat >.github/workflows/main.yaml<<EOF
name: Django                                  # workflow name

on:
  push:                                       
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py check

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py test taskManager

  integration:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - run: echo "This is an integration step"
      - run: exit 1
        continue-on-error: true

  prod:
    runs-on: ubuntu-latest
    needs: integration
    steps:
      - run: echo "This is a deploy step."
EOF
If you are not comfortable with the syntax, explore the GitHub Actions syntax athttps://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#about-yaml-syntax-for-workflows

Let’s add this file to the repository and commit the changes.

git add .github/workflows/main.yaml

git commit -m "Add github workflows"

6. Push the changes to the repository
Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the git push command.

git push origin main

Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
7. Verify the pipeline runs
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.

You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

Let’s move to the next step.
Embed Bandit in GitHub Actions
As discussed in the Static Analysis using Bandit exercise, we can embed Bandit in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and copy the below content to the .github/workflows/main.yaml file under test job.

  sast:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json

      - uses: actions/upload-artifact@v2
        with:
          name: Bandit
          path: bandit-output.json
        if: always()                        # what is this for?
To understand if: always() Please refer to conditionals.

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.
Allow the job failure
We do not want to fail the builds in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives in their default configuration.

You can use the continue-on-error syntax to not fail the build even though the tool found security issues.

  sast:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json
        continue-on-error: true             # allow the build to fail, similar to "allow_failure: true" in GitLab

      - uses: actions/upload-artifact@v2
        with:
          name: Bandit
          path: bandit-output.json
        if: always()                        # what is this for?
After adding the continue-on-error syntax, the pipeline would look like the following:

name: Django                                  # workflow name

on:
  push:
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py check

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/checkout@v2

      - name: Setup python
        uses: actions/setup-python@v2
        with:
          python-version: '3.6'

      - run: |
          pip3 install --upgrade virtualenv
          virtualenv env
          source env/bin/activate
          pip install -r requirements.txt
          python manage.py test taskManager

  oast-frontend:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v2

      - uses: actions/setup-node@v2
        with:
          node-version: '10.x'

      - run: npm install

      - run: docker run --rm -v $(pwd):/src -w /src hysnsec/retire --outputformat json --outputpath retirejs-report.json --severity high

      - uses: actions/upload-artifact@v2
        with:
          name: RetireJS
          path: retirejs-report.json
        if: always()                        # what is this for?

  sast:
    runs-on: ubuntu-latest
    needs: oast-frontend
    steps:
      - uses: actions/checkout@v2

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json
        continue-on-error: true             # allow the build to fail, similar to allow_failure: true

      - uses: actions/upload-artifact@v2
        with:
          name: Bandit
          path: bandit-output.json
        if: always()                        # what is this for?

  integration:
    runs-on: ubuntu-latest
    needs: oast-frontend
    steps:
      - run: echo "This is an integration step"
      - run: exit 1
        continue-on-error: true

  prod:
    runs-on: ubuntu-latest
    needs: integration
    steps:
      - run: echo "This is a deploy step."
Did you see we sneaked oast-frontend at the end?

Go ahead and add the above content to the .github/workflows/main.yaml file to run the pipeline.

You will notice that the sast job has failed but didn’t block the other jobs from running.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our django.nv repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.
How to Embed Bandit into CircleCI Optional
Use Bandit tool to perform SAST in CircleCI
In this scenario, you will learn how to embed SAST in CircleCI.

You will learn to use Bandit in CircleCI and how to allow job failure when the tool found several issues.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.
A simple CI/CD pipeline
You need to create .circleci directory and create a new YAML file named config.yml and add the following CI script.

mkdir -p .circleci

cat >.circleci/config.yml<<EOF
jobs:
  build:
    docker:
      - image: python:3.6                   # similar to "image" in GitLab
    steps:
      - checkout
      - run: |                              # similar to "script" in GitLab
          pip install -r requirements.txt
          python manage.py check

  test:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: |
          pip install -r requirements.txt
          python manage.py test taskManager

  integration:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: echo "This is a deploy step."

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build 
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
EOF
If you are not comfortable with the syntax, explore the CircleCI syntax at https://circleci.com/docs/2.0/configuration-reference/

Let’s add this file to the repository and commit the changes.

git add .circleci/config.yml

git commit -m "Add CircleCI config"

Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the git push command.

git push origin main

Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.

Let’s move to the next step.
Embed Bandit in CircleCI
As discussed in the Static Analysis using Bandit exercise, we can embed Bandit in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and copy the below content to the .circleci/config.yml file under test job.

  sast:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json

      - store_artifacts:
          path: bandit-output.json
          destination: bandit-artifact
Please remember to add the above job name to the workflows section as shown below:

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build
      - sast:
          requires:
            - test
      - integration:
          requires:
            - sast
      - prod:
          type: approval
          requires:
            - integration
Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.
Allow the job failure
We do not want to fail the builds in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives in their default configuration.

You can use the when: always syntax to not fail the build even though the tool found security issues.

  sast:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json

      - store_artifacts:
          path: bandit-output.json
          destination: bandit-artifact
          when: always              # Even if the job fails, continue to the next stages
The final pipeline would look like the following:

jobs:
  build:
    docker:
      - image: python:3.6                   # similar to "image" in GitLab
    steps:
      - checkout
      - run: |                              # similar to "script" in GitLab
          pip install -r requirements.txt
          python manage.py check

  test:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: |
          pip install -r requirements.txt
          python manage.py test taskManager

  oast-frontend:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src -w /src hysnsec/retire --outputformat json --outputpath retirejs-output.json --severity high

      - store_artifacts:
          path: retirejs-output.json
          destination: retirejs-artifact
          when: always              # Even if the job fails, continue to the next stages

  sast:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json

      - store_artifacts:
          path: bandit-output.json
          destination: bandit-artifact
          when: always              # Even if the job fails, continue to the next stages

  integration:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1
          when: always                    # Even if the job fails, continue to the next stages

  prod:
    docker:
      - image: python:3.6
    steps:
      - checkout
      - run: echo "This is a deploy step."

workflows:
  version: 2
  django:
    jobs:
      - build
      - test:
          requires:
            - build
      - oast-frontend:
          requires:
            - test
      - sast:
          requires:
            - oast-frontend
      - integration:
          requires:
            - sast
      - prod:
          type: approval
          requires:
            - integration
Did you see we sneaked oast-frontend at the end?

Go ahead and add the above content to the .circleci/config.yml file to run the pipeline.

You will notice that the sast job has failed but didn’t block the other jobs from running.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select django.nv repository and select the appropriate pipeline to see the output.
How to Fix The Issues Reported by Bandit Optional
We will learn how to fix the issues reported by bandit tools
In this scenario, you will learn how to fix issues reported by the Bandit tool in python source code.

You will do the following in this activity.
1. Download the source code from the git repository.
2. Install the Bandit tool.
3. Run the SAST scan on the code.
4. Fix the issues found by Bandit.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/dvpa-api webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.
Installing Bandit
The Bandit is a tool designed to find common security issues in Python code.

To do this Bandit, processes each file, builds an AST, and runs appropriate plugins against the AST nodes. Once Bandit has finished scanning all the files, it generates a report.

Bandit was originally developed within the OpenStack Security Project and later rehomed to PyCQA.

You can find more details about the project at https://github.com/PyCQA/bandit.

Let’s install the bandit scanner on the system to perform static analysis.

pip3 install bandit

Collecting bandit
  Downloading https://files.pythonhosted.org/packages/5a/50/ff2f2c8f1f0ca1569f678eeb608c0f973b835985410985594fbee96be820/bandit-1.6.2-py2.py3-none-any.whl (122kB)
    100% |████████████████████████████████| 122kB 498kB/s
Collecting six>=1.10.0 (from bandit)
  Downloading https://files.pythonhosted.org/packages/65/eb/1f97cb97bfc2390a276969c6fae16075da282f5058082d4cb10c6c5c1dba/six-1.14.0-py2.py3-none-any.whl
Collecting GitPython>=1.0.1 (from bandit)
  Downloading https://files.pythonhosted.org/packages/7a/c9/6e1aec0893efe66f407d2e14a3daba73dbb39bbeeb72142b037c4a224d40/GitPython-2.1.15-py2.py3-none-any.whl (452kB)
    100% |████████████████████████████████| 460kB 1.9MB/s
Collecting stevedore>=1.20.0 (from bandit)
  Downloading https://files.pythonhosted.org/packages/e6/49/a35dd566626892d577e426dbe5ea424dd7fbe10645f2c1070dcba474eca9/stevedore-1.32.0-py2.py3-none-any.whl (43kB)
    100% |████████████████████████████████| 51kB 7.1MB/s
Collecting PyYAML>=3.13 (from bandit)
We have successfully installed the bandit scanner. Let’s explore the functionality it provides us.

bandit --help

usage: bandit [-h] [-r] [-a {file,vuln}] [-n CONTEXT_LINES] [-c CONFIG_FILE]
              [-p PROFILE] [-t TESTS] [-s SKIPS] [-l] [-i]
              [-f {csv,custom,html,json,screen,txt,xml,yaml}]
              [--msg-template MSG_TEMPLATE] [-o [OUTPUT_FILE]] [-v] [-d] [-q]
              [--ignore-nosec] [-x EXCLUDED_PATHS] [-b BASELINE]
              [--ini INI_PATH] [--version]
              [targets [targets ...]]

Bandit - a Python source code security analyzer

positional arguments:
  targets               source file(s) or directory(s) to be tested

optional arguments:
  -h, --help            show this help message and exit
  -r, --recursive       find and process files in subdirectories
  -a {file,vuln}, --aggregate {file,vuln}
                        aggregate output by vulnerability (default) or by
                        filename
  -n CONTEXT_LINES, --number CONTEXT_LINES
                        maximum number of code lines to output for each issue
  -c CONFIG_FILE, --configfile CONFIG_FILE
                        optional config file to use for selecting plugins and
                        overriding defaults
  -p PROFILE, --profile PROFILE
                        profile to use (defaults to executing all tests)
  -t TESTS, --tests TESTS
                        comma-separated list of test IDs to run
  -s SKIPS, --skip SKIPS
                        comma-separated list of test IDs to skip
  -l, --level           report only issues of a given severity level or higher
                        (-l for LOW, -ll for MEDIUM, -lll for HIGH)
  -i, --confidence      report only issues of a given confidence level or
                        higher (-i for LOW, -ii for MEDIUM, -iii for HIGH)
  -f {csv,custom,html,json,screen,txt,xml,yaml}, --format {csv,custom,html,json,screen,txt,xml,yaml}
                        specify output format
  --msg-template MSG_TEMPLATE
                        specify output message template (only usable with
                        --format custom), see CUSTOM FORMAT section for list
                        of available values
  -o [OUTPUT_FILE], --output [OUTPUT_FILE]
                        write report to filename
  -v, --verbose         output extra information like excluded and included
                        files
  -d, --debug           turn on debug mode
  -q, --quiet, --silent
                        only show output in the case of an error
  --ignore-nosec        do not skip lines with # nosec comments
  -x EXCLUDED_PATHS, --exclude EXCLUDED_PATHS
                        comma-separated list of paths (glob patterns
                        supported) to exclude from scan (note that these are
                        in addition to the excluded paths provided in the
                        config file)
  -b BASELINE, --baseline BASELINE
                        path of a baseline report to compare against (only
                        JSON-formatted files are accepted)
  --ini INI_PATH        path to a .bandit file that supplies command line
                        arguments
  --version             show program's version number and exit
Let’s move to the next step.
Run the scanner
Let’s scan our source code by executing the following command:

bandit -r .

[main]  INFO    profile include tests: None
[main]  INFO    profile exclude tests: None
[main]  INFO    cli include tests: None
[main]  INFO    cli exclude tests: None
[main]  INFO    running on Python 3.6.9
Run started:2021-02-18 04:17:07.695129

Test results:
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/auth.py:13
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
12          cur = db.connection.cursor()
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/auth.py:14
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
15          user = cur.fetchone()

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/dashboard.py:67
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
66
67              hashed_password = hashlib.md5(password1.encode()).hexdigest()
68

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:85
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
84              cur = db.connection.cursor()
85              cur.execute(f"SELECT * FROM users WHERE id={request.args.get('uid')}")
86              user = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:100
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
99              cur = db.connection.cursor()
100             cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}")
101             db.connection.commit()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:133
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
132             cur.execute(
133                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
134                 [body, slug, claim.get("id"), title])

--------------------------------------------------
>> Issue: [B506:yaml_load] Use of unsafe yaml load. Allows instantiation of arbitrary objects. Consider yaml.safe_load().
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/dashboard.py:248
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b506_yaml_load.html
247                 elif export_format == "yaml":
248                     import_post_data = yaml.load(import_data)
249

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:26
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
25              cur = db.connection.cursor()
26              cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
27              post = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:49
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
48              cur = db.connection.cursor()
49              cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
50              search_posts = cur.fetchall() #Post.objects(title__icontains=query)

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/user.py:71
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
70
71              hashed_password = hashlib.md5(password.encode()).hexdigest()
72              cur = db.connection.cursor()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/user.py:74
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
73              cur.execute(
74                  f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")
75              db.connection.commit()

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'flaskblog_secret_key'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:4
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
3       MONGODB_SETTINGS = 'flaskblog_db'
4       SECRET_KEY = 'flaskblog_secret_key'
5
6       # Debugging & Reloader
7       debugger = True

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'secret'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:13
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
12      username = 'admin'
13      password = 'secret'
14
15      # Disqus Configuration
16      disqus_shortname = 'blogpythonlearning'  # please change this.

--------------------------------------------------

Code scanned:
        Total lines of code: 603
        Total lines skipped (#nosec): 0

Run metrics:
        Total issues (by severity):
                Undefined: 0.0
                Low: 2.0
                Medium: 11.0
                High: 0.0
        Total issues (by confidence):
                Undefined: 0.0
                Low: 0.0
                Medium: 9.0
                High: 4.0
Files skipped (0):
We got 13 issues in total (by confidence). Among these, there are two(2) hardcoded password strings, an insecure hash function issue, insecure deserialization, and 8 SQL Injections. We must fix all these issues in order to avoid security breaches in our organization.

Let’s move to the next step.
Fixing Insecure Deserialization
Python yaml library has a known vulnerability around YAML deserialization. We can search for this known security issue on the CVE website.

For more details, please visit CVE-2020-1747. As mentioned before, our code is vulnerable to Deserialization Attacks.

If you recall, one of the findings in the previous step was unsafe YAML load, and the following was the code snippet for the same.

--------------------------------------------------
>> Issue: [B506:yaml_load] Use of unsafe yaml load. Allows instantiation of arbitrary objects. Consider yaml.safe_load().
   Severity: Medium   Confidence: High
   Location: ./blogapi/dashboard.py:248
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b506_yaml_load.html
247                 elif export_format == "yaml":
248                     import_post_data = yaml.load(import_data)
Let’s verify this issue exists by opening up the flaskblog/blogapi/dashboard.py file using any text editor like vim or nano. Ensure the security issue exists at line 248 and the program uses the insecure yaml.load function. To fix this issue, we need to replace yaml.load with a safe alternative yaml.safe_load.

Let’s run the scanner once again.

bandit -r .

[main]  INFO    profile include tests: None
[main]  INFO    profile exclude tests: None
[main]  INFO    cli include tests: None
[main]  INFO    cli exclude tests: None
[main]  INFO    running on Python 3.6.9
Run started:2021-02-18 04:19:03.203271

Test results:
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/auth.py:13
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
12          cur = db.connection.cursor()
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/auth.py:14
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
15          user = cur.fetchone()

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/dashboard.py:67
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
66
67              hashed_password = hashlib.md5(password1.encode()).hexdigest()
68

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:85
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
84              cur = db.connection.cursor()
85              cur.execute(f"SELECT * FROM users WHERE id={request.args.get('uid')}")
86              user = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:100
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
99              cur = db.connection.cursor()
100             cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}")
101             db.connection.commit()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:133
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
132             cur.execute(
133                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
134                 [body, slug, claim.get("id"), title])

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:26
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
25              cur = db.connection.cursor()
26              cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
27              post = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:49
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
48              cur = db.connection.cursor()
49              cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
50              search_posts = cur.fetchall() #Post.objects(title__icontains=query)

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/user.py:71
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
70
71              hashed_password = hashlib.md5(password.encode()).hexdigest()
72              cur = db.connection.cursor()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/user.py:74
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
73              cur.execute(
74                  f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")
75              db.connection.commit()

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'flaskblog_secret_key'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:4
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
3       MONGODB_SETTINGS = 'flaskblog_db'
4       SECRET_KEY = 'flaskblog_secret_key'
5
6       # Debugging & Reloader
7       debugger = True

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'secret'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:13
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
12      username = 'admin'
13      password = 'secret'
14
15      # Disqus Configuration
16      disqus_shortname = 'blogpythonlearning'  # please change this.

--------------------------------------------------

Code scanned:
        Total lines of code: 603
        Total lines skipped (#nosec): 0

Run metrics:
        Total issues (by severity):
                Undefined: 0.0
                Low: 2.0
                Medium: 10.0
                High: 0.0
        Total issues (by confidence):
                Undefined: 0.0
                Low: 0.0
                Medium: 9.0
                High: 3.0
Files skipped (0):
As you can see, there is no yaml.load issue in the output. The total High issue count (by confidence) has decreased from 4 to 3.

Let’s move to the next step.
Fixing SQL Injection
Bandit also informed us that there are 7 possible SQL Injection issues in this application. We can fix these issues in various ways, but the best way to fix SQL Injection issues is Parameterized queries, also known as Parameter Binding.

[main]  INFO    profile include tests: None
[main]  INFO    profile exclude tests: None
[main]  INFO    cli include tests: None
[main]  INFO    cli exclude tests: None
[main]  INFO    running on Python 3.8.5
Run started:2021-01-24 08:39:43.777107

Test results:
--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./auth.py:14
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
15          user = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/dashboard.py:85
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
84              cur = db.connection.cursor()
85              cur.execute(f"SELECT * FROM users WHERE id={request.args.get('uid')}")
86              user = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/dashboard.py:100
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
99              cur = db.connection.cursor()
100             cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}")
101             db.connection.commit()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/dashboard.py:133
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
132             cur.execute(
133                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
134                 [body, slug, claim.get("id"), title])

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/home.py:26
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
25              cur = db.connection.cursor()
26              cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
27              post = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/home.py:49
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
48              cur = db.connection.cursor()
49              cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
50              search_posts = cur.fetchall() #Post.objects(title__icontains=query)

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./blogapi/user.py:74
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
73              cur.execute(
74                  f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")
75              db.connection.commit()

...[SNIP]...
First, we will try to fix the SQL Injection issue present in the flaskblog/auth.py file at line number 14. As you can see in the following code snippet, there is a SQL injection issue in the check_auth function.

def check_auth(username, password):
   """ This function is called to check if a username / password
       combination is valid.
   """
   cur = db.connection.cursor()
   hashsed_password = hashlib.md5(password.encode()).hexdigest()
   cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
   user = cur.fetchone()

   if user is None:
      return False

   session["is_logged_in"] = True
   session["id"] = user.get("id")
   session["email"] = user.get("email")
   session["full_name"] = user.get("full_name")

   return user
The cur.execute function call will execute the SQL query on the database. It takes the username and password as possible inputs to the SQL query.

You can also verify this behavior dynamically by reproducing SQL query errors.

You can learn more about SQL Injection here.

You can replace line number 14 with the following code. This code will fix the SQL Injection issue.

   cur.execute(f"SELECT * FROM users WHERE email=%s AND password=%s", [username, hashsed_password ])
Then, re-run the bandit scanner.

bandit -r .

[main]  INFO    profile include tests: None
[main]  INFO    profile exclude tests: None
[main]  INFO    cli include tests: None
[main]  INFO    cli exclude tests: None
[main]  INFO    running on Python 3.6.9
Run started:2021-02-18 04:22:42.234029

Test results:
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/auth.py:13
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
12          cur = db.connection.cursor()
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email=%s AND password=%s", [username, hashsed_password ])

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/auth.py:14
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
13          hashsed_password = hashlib.md5(password.encode()).hexdigest()
14          cur.execute(f"SELECT * FROM users WHERE email=%s AND password=%s", [username, hashsed_password ])
15          user = cur.fetchone()

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/dashboard.py:67
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
66
67              hashed_password = hashlib.md5(password1.encode()).hexdigest()
68

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:85
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
84              cur = db.connection.cursor()
85              cur.execute(f"SELECT * FROM users WHERE id={request.args.get('uid')}")
86              user = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:100
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
99              cur = db.connection.cursor()
100             cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE ` users`.`id` = {request.args.get('uid')}")
101             db.connection.commit()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/dashboard.py:133
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
132             cur.execute(
133                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
134                 [body, slug, claim.get("id"), title])

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:26
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
25              cur = db.connection.cursor()
26              cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
27              post = cur.fetchone()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/home.py:49
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
48              cur = db.connection.cursor()
49              cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
50              search_posts = cur.fetchall() #Post.objects(title__icontains=query)

--------------------------------------------------
>> Issue: [B303:blacklist] Use of insecure MD2, MD4, MD5, or SHA1 hash function.
   Severity: Medium   Confidence: High
   Location: ./flaskblog/blogapi/user.py:71
   More Info: https://bandit.readthedocs.io/en/latest/blacklists/blacklist_calls.html#b303-md5
70
71              hashed_password = hashlib.md5(password.encode()).hexdigest()
72              cur = db.connection.cursor()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: ./flaskblog/blogapi/user.py:74
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
73              cur.execute(
74                  f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")
75              db.connection.commit()

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'flaskblog_secret_key'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:4
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
3       MONGODB_SETTINGS = 'flaskblog_db'
4       SECRET_KEY = 'flaskblog_secret_key'
5
6       # Debugging & Reloader
7       debugger = True

--------------------------------------------------
>> Issue: [B105:hardcoded_password_string] Possible hardcoded password: 'secret'
   Severity: Low   Confidence: Medium
   Location: ./flaskblog/config.py:13
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b105_hardcoded_password_string.html
12      username = 'admin'
13      password = 'secret'
14
15      # Disqus Configuration
16      disqus_shortname = 'blogpythonlearning'  # please change this.

--------------------------------------------------

Code scanned:
        Total lines of code: 603
        Total lines skipped (#nosec): 0

Run metrics:
        Total issues (by severity):
                Undefined: 0.0
                Low: 2.0
                Medium: 10.0
                High: 0.0
        Total issues (by confidence):
                Undefined: 0.0
                Low: 0.0
                Medium: 9.0
                High: 3.0
Files skipped (0):
There’s no change in the output even after fixing this issue; hence this is a False Positive.

Let’s move to the next step.
Challenge
Please fix all SQL Injection issues, and use the bandit tool to cross-check the fixes
Mark the findings as False Positive if you think so
Please do not forget to share the answer with our staff via Slack Direct Message (DM).
Static Analysis using Gosec Optional
Learn how to run static analysis scans on Golang code using gosec
In this scenario, you will learn how to run gosec scan on Golang code.

You will need to download the code, install the SAST tool called gosec and then finally run the SAST scan on the code.

 Terminal
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/golang.git webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.
Install gosec
GoSec inspects source code for security problems by scanning the Go AST, also can be configured to only run a subset of rules, to exclude certain file paths, and produce reports in different formats

You can find more details about the project at https://github.com/securego/gosec

Our system doesn’t have Golang installed, so we need to install it with the following command.

curl -s https://dl.google.com/go/go1.15.1.linux-amd64.tar.gz | tar xvz -C /usr/local

We also need to configure GOROOT, GOPATH and PATH variables so golang can find binaries and third party libraries.

export GOROOT=/usr/local/go

export GOPATH=$HOME/go

export PATH=$GOPATH/bin:$GOROOT/bin:$PATH

Let’s install gosec on the system to perform static analysis.

curl -sfL https://raw.githubusercontent.com/securego/gosec/master/install.sh | sh -s v2.4.0

securego/gosec info checking GitHub for tag 'v2.4.0'
securego/gosec info found version: 2.4.0 for v2.4.0/linux/amd64
securego/gosec info installed ./bin/gosec
Running curl and piping it to a shell is a security issue. how could we improve it?

You guessed it right. Use native tools to install it. For e.g., go get.

go get github.com/securego/gosec/v2/cmd/gosec

go: downloading github.com/securego/gosec/v2 v2.4.0
go: downloading github.com/securego/gosec v0.0.0-20200401082031-e946c8c39989
go: found github.com/securego/gosec/v2/cmd/gosec in github.com/securego/gosec/v2 v2.4.0
go: downloading github.com/gookit/color v1.2.5
go: downloading github.com/nbutton23/zxcvbn-go v0.0.0-20180912185939-ae427f1e4c1d
go: downloading gopkg.in/yaml.v2 v2.3.0
go: downloading golang.org/x/tools v0.0.0-20200701041122-1837592efa10
go: downloading golang.org/x/xerrors v0.0.0-20191204190536-9bdfabe68543
go: downloading golang.org/x/mod v0.2.0
We have successfully installed gosec, let’s explore the functionality it provides us.

gosec --help


gosec - Golang security checker

gosec analyzes Go source code to look for common programming mistakes that
can lead to security problems.

VERSION: 2.4.0
GIT TAG: v2.4.0
BUILD DATE: 2020-07-24T07:54:54Z

USAGE:

        # Check a single package
        $ gosec $GOPATH/src/github.com/example/project

        # Check all packages under the current directory and save results in
        # json format.
        $ gosec -fmt=json -out=results.json ./...

        # Run a specific set of rules (by default all rules will be run):
        $ gosec -include=G101,G203,G401  ./...

        # Run all rules except the provided
        $ gosec -exclude=G101 $GOPATH/src/github.com/example/project/...


OPTIONS:

  -conf string
        Path to optional config file
  -confidence string
        Filter out the issues with a lower confidence than the given value. Valid options are: low, medium, high (default "low")
  -exclude string
        Comma separated list of rules IDs to exclude. (see rule list)
  -exclude-dir value
        Exclude folder from scan (can be specified multiple times)
  -fmt string
        Set output format. Valid options are: json, yaml, csv, junit-xml, html, sonarqube, golint or text (default "text")
  -include string
        Comma separated list of rules IDs to include. (see rule list)
  -log string
        Log messages to file rather than stderr
  -no-fail
        Do not fail the scanning, even if issues were found
  -nosec
        Ignores #nosec comments when set
  -nosec-tag string
        Set an alternative string for #nosec. Some examples: #dontanalyze, #falsepositive
  -out string
        Set output file for results
  -quiet
        Only show output when errors are found
  -severity string
        Filter out the issues with a lower severity than the given value. Valid options are: low, medium, high (default "low")
  -sort
        Sort issues by severity (default true)
  -tags string
        Comma separated list of build tags
  -tests
        Scan tests files
  -version
        Print version and quit with exit code 0


RULES:

        G101: Look for hardcoded credentials
        G102: Bind to all interfaces
        G103: Audit the use of unsafe block
        G104: Audit errors not checked
        G106: Audit the use of ssh.InsecureIgnoreHostKey function
        G107: Url provided to HTTP request as taint input
        G108: Profiling endpoint is automatically exposed
        G109: Converting strconv.Atoi result to int32/int16
        G110: Detect io.Copy instead of io.CopyN when decompression
        G201: SQL query construction using format string
        G202: SQL query construction using string concatenation
        G203: Use of unescaped data in HTML templates
        G204: Audit use of command execution
        G301: Poor file permissions used when creating a directory
        G302: Poor file permissions used when creation file or using chmod
        G303: Creating tempfile using a predictable path
        G304: File path provided as taint input
        G305: File path traversal when extracting zip archive
        G306: Poor file permissions used when writing to a file
        G307: Unsafe defer call of a method returning an error
        G401: Detect the usage of DES, RC4, MD5 or SHA1
        G402: Look for bad TLS connection settings
        G403: Ensure minimum RSA key length of 2048 bits
        G404: Insecure random number source (rand)
        G501: Import blocklist: crypto/md5
        G502: Import blocklist: crypto/des
        G503: Import blocklist: crypto/rc4
        G504: Import blocklist: net/http/cgi
        G505: Import blocklist: crypto/sha1
        G601: Implicit memory aliasing in RangeStmt
Let’s move to the next step.
Run the Scanner
We can perform static analysis on golang code using the following command.

gosec ./...

[gosec] 2020/09/23 10:59:00 Including rules: default
[gosec] 2020/09/23 10:59:00 Excluding rules: default
[gosec] 2020/09/23 10:59:00 Import directory: /webapp/vulnerability/idor
[gosec] 2020/09/23 10:59:01 Checking package: idor
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/vulnerability/idor/function.go
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/vulnerability/idor/idor.go
[gosec] 2020/09/23 10:59:01 Import directory: /webapp/vulnerability/xss
[gosec] 2020/09/23 10:59:01 Checking package: xss
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/vulnerability/xss/function.go
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/vulnerability/xss/xss.go
[gosec] 2020/09/23 10:59:01 Import directory: /webapp
[gosec] 2020/09/23 10:59:01 Checking package: main
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/app.go
[gosec] 2020/09/23 10:59:01 Import directory: /webapp/util
[gosec] 2020/09/23 10:59:01 Checking package: util
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/util/cookie.go
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/util/http.go
[gosec] 2020/09/23 10:59:01 Checking file: /webapp/util/template.go
[gosec] 2020/09/23 10:59:01 Import directory: /webapp/util/database
[gosec] 2020/09/23 10:59:02 Checking package: database
[gosec] 2020/09/23 10:59:02 Checking file: /webapp/util/database/database.go
[gosec] 2020/09/23 10:59:02 Import directory: /webapp/setup
[gosec] 2020/09/23 10:59:02 Checking package: setup
[gosec] 2020/09/23 10:59:02 Checking file: /webapp/setup/function.go
[gosec] 2020/09/23 10:59:02 Checking file: /webapp/setup/setup.go
[gosec] 2020/09/23 10:59:02 Import directory: /webapp/util/config
[gosec] 2020/09/23 10:59:02 Checking package: config
[gosec] 2020/09/23 10:59:02 Checking file: /webapp/util/config/config.go
[gosec] 2020/09/23 10:59:02 Import directory: /webapp/vulnerability/csa
[gosec] 2020/09/23 10:59:03 Checking package: csa
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/vulnerability/csa/csa.go
[gosec] 2020/09/23 10:59:03 Import directory: /webapp/vulnerability/xxe
[gosec] 2020/09/23 10:59:03 Checking package: xxe
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/vulnerability/xxe/xxe.go
[gosec] 2020/09/23 10:59:03 Import directory: /webapp/user/session
[gosec] 2020/09/23 10:59:03 Checking package: session
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/user/session/session.go
[gosec] 2020/09/23 10:59:03 Import directory: /webapp/util/middleware
[gosec] 2020/09/23 10:59:03 Checking package: middleware
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/util/middleware/middleware.go
[gosec] 2020/09/23 10:59:03 Import directory: /webapp/vulnerability/sqli
[gosec] 2020/09/23 10:59:03 Checking package: sqli
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/vulnerability/sqli/function.go
[gosec] 2020/09/23 10:59:03 Checking file: /webapp/vulnerability/sqli/sqli.go
[gosec] 2020/09/23 10:59:03 Import directory: /webapp/setting
[gosec] 2020/09/23 10:59:04 Checking package: setting
[gosec] 2020/09/23 10:59:04 Checking file: /webapp/setting/setting.go
[gosec] 2020/09/23 10:59:04 Import directory: /webapp/user
[gosec] 2020/09/23 10:59:04 Checking package: user
[gosec] 2020/09/23 10:59:04 Checking file: /webapp/user/user.go
Results:


[/webapp/util/template.go:45] - G203 (CWE-79): this method will not auto-escape HTML. Verify data is well formed. (Confidence: LOW, Severity: MEDIUM)
    44: func ToHTML(text string)template.HTML{
  > 45:         return template.HTML(text)
    46: }



[/webapp/user/user.go:160] - G401 (CWE-326): Use of weak cryptographic primitive (Confidence: HIGH, Severity: MEDIUM)
    159: func Md5Sum(text string) string {
  > 160:        hasher := md5.New()
    161:        hasher.Write([]byte(text))



[/webapp/user/user.go:8] - G501 (CWE-327): Blocklisted import crypto/md5: weak cryptographic primitive (Confidence: HIGH, Severity: MEDIUM)
    7:  "strconv"
  > 8:  "crypto/md5"
    9:  "database/sql"



[/webapp/vulnerability/sqli/function.go:37-40] - G201 (CWE-89): SQL string formatting (Confidence: HIGH, Severity: MEDIUM)
    36:
  > 37:         getProfileSql := fmt.Sprintf(`SELECT p.user_id, p.full_name, p.city, p.phone_number
  > 38:                                                                 FROM Profile as p,Users as u
  > 39:                                                                 where p.user_id = u.id
  > 40:                                                                 and u.id=%s`, uid) //here is the vulnerable query
    41:         rows, err := DB.Query(getProfileSql)


[...SNIP...]


[/webapp/vulnerability/idor/idor.go:42] - G104 (CWE-703): Errors unhandled. (Confidence: HIGH, Severity: LOW)
    41:         p := NewProfile()
  > 42:         p.GetData(sid)
    43:



[/webapp/user/user.go:161] - G104 (CWE-703): Errors unhandled. (Confidence: HIGH, Severity: LOW)
    160:        hasher := md5.New()
  > 161:        hasher.Write([]byte(text))
    162:        return hex.EncodeToString(hasher.Sum(nil))



Summary:
   Files: 20
   Lines: 1573
   Nosec: 0
  Issues: 23
As you can see, we found 23 issues and there were many false positives. We can reduce the bloat and false positives by using custom rules, proper configurations and by using arguments such as –severity high.

gosec -exclude=G104 ./...

The above command will reduce the bloat but does it also removes real issues?

Let’s move to the next step.
Challenge
Use appropriate gosec options to reduce false positives
Ensure you follow the DevSecOps Gospel and best practices
Think how you would embed this tool in CI pipeline? and under which build stage?
Please do not forget to share the answer with our staff via Slack Direct Message (DM).

How to Embed Gosec into GitLab Optional
Use Gosec tool to perform SAST in CI/CD pipeline
In this scenario, you will learn how to embed GoSec in the CI/CD pipeline.

You will learn to use gosec in CI/CD pipeline and how to allow job failure when the tool found several issues.

Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab machine to start.

Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

A simple CI/CD pipeline
Create a new project by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/projects/new#blank_project, give the project name golang and click Create project button. Once done we need to push our source code into GitLab, let’s download the code using git clone in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/golang.git golang

cd golang

Rename git url to the new one.

git remote rename origin old-origin

git remote add origin http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang.git

Then, push the code into the repository.

git push -u origin --all

And enter the GitLab credentials.

Name	Value
Username	root
Password	pdso-training
git push -u origin --tags

Next, considering your DevOps team created a simple CI pipeline with the following contents.

image: docker:latest

services:
  - docker:dind

stages:
  - build
  - test
  - release
  - preprod
  - integration
  - prod

build:
  stage: build
  script:
    - echo "This is a build step"

test:
  stage: test
  script:
    - echo "This is a test step"

integration:
  stage: integration
  script:
    - echo "This is an integration step"
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
We have four jobs in this pipeline, a build job, a test job, a integration job and a prod job.

As a security engineer, I do not care much about what the DevOps team is doing as part of these jobs. Why? Imagine having to learn every build/testing tool used by your DevOps team, it will be a nightmare. Instead, rely on the DevOps team for help.

Let’s login into the GitLab using the following details and execute this pipeline.

GitLab CI/CD Machine
Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training
Next, we need to create a CI/CD pipeline by replacing the .gitlab-ci.yml file content with the above CI script. Click on the Edit button to replace the content (use Control+A and Control+V).

Save changes to the file using the Commit changes button.

Verify the pipeline run
As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang/pipelines.

Click on the appropriate job name to see the output.

Challenge
Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

Read the Gosec documentation
Embed SAST backend tool, Gosec in test stage with job name as sast
Ensure the job is running under the test stage
After the above steps, move it to the build stage now
You can either install Gosec manually or use the Docker image
Follow all the best practices while embedding Gosec in the CI/CD pipeline. Don’t forget the tool evaluation criteria
Please try to do this exercise without looking at the solution on the next page.

Let’s move to the next step.

 
 Terminal
Embed Gosec in CI/CD pipeline
As discussed in the Static Analysis using Gosec exercise, we can embed Gosec in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

image: docker:latest

services:
  - docker:dind

stages:
  - build
  - test
  - release
  - preprod
  - integration
  - prod

build:
  stage: build
  script:
    - echo "This is a build step"

sast:
  stage: build
  image: golang:1.15-alpine
  before_script:
    - go get github.com/securego/gosec/v2/cmd/gosec
  script:
    - gosec -fmt json -out gosec-output.json ./... 
  artifacts:
    paths: [gosec-output.json]
    when: always

test:
  stage: test
  script:
    - echo "This is a test step"

integration:
  stage: integration
  script:
    - echo "This is an integration step"
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang/pipelines.

Click on the appropriate job name to see the output.

You will notice that the sast job’s output is saved in gosec-output.json file.

Let’s move to the next step.

Allow the job failure
Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the allow_failure tag to “not fail the build” even though the tool found issues.

sast:
  stage: build
  image: golang:1.15-alpine
  before_script:
    - go get github.com/securego/gosec/v2/cmd/gosec
  script:
    - gosec -fmt json -out gosec-output.json ./...
  artifacts:
    paths: [gosec-output.json]
    when: always
  allow_failure: true
After adding the allow_failure tag, the pipeline would look like the following.

image: docker:latest

services:
  - docker:dind

stages:
  - build
  - test
  - release
  - preprod
  - integration
  - prod

sast:
  stage: build
  image: golang:1.15-alpine
  before_script:
    - go get github.com/securego/gosec/v2/cmd/gosec
  script:
    - gosec -fmt json -out gosec-output.json ./...
  artifacts:
    paths: [gosec-output.json]
    when: always
  allow_failure: true

integration:
  stage: integration
  script:
    - echo "This is an integration step"
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
You will notice that the sast job failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang/pipelines.

Click on the appropriate job name to see the output.

 
 Terminal

Embed Gosec into Jenkins Optional
Use Gosec tool to do SAST in Jenkins CI/CD pipeline
In this scenario, you will learn how to embed GoSec in the Jenkins CI/CD pipeline.

Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab and Jenkins machine to start.

Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We have already set up the Jenkins machine with several plugins to help you do the exercise
Create a new job
The Jenkins system is already configured with Gitlab. If you wish to know how to configure Jenkins with Gitlab, you can check out this link.

You don’t need to create a new project because the following commands will create a new project and push the code.

Before executing the following commands, you need to wait until GitLab machine is up.

Add the repository for Golang
Using git clone, let’s download the golang code that needs to be tested for security to the DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/golang.git golang

cd golang

Let’s rename the git remote URL to our Gitlab instance.

git remote rename origin old-origin

git remote add origin http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang.git

Then, push the code into the repository.

git push -u origin --all

Name	Value
Username	root
Password	pdso-training
And enter the GitLab credentials.

Counting objects: 104, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (87/87), done.
Writing objects: 100% (104/104), 1.07 MiB | 9.29 MiB/s, done.
Total 104 (delta 7), reused 104 (delta 7)
remote: Resolving deltas: 100% (7/7), done.
remote:
remote:
remote: The private project root/golang was successfully created.
remote:
remote: To configure the remote, run:
remote:   git remote add origin http://gitlab-ce-JxIKciVAk.lab.practical-devsecops.training/root/golang.git
remote:
remote: To view the project, visit:
remote:   http://gitlab-ce-JxIKciVAk.lab.practical-devsecops.training/root/golang
remote:
remote:
remote:
To http://gitlab-ce-JxIKciVAk.lab.practical-devsecops.training/root/golang.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
Create a Job
We will create a new job in Jenkins by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.

You can use the following details to log into Jenkins.

Name	Value
Username	root
Password	pdso-training
Provide a name for your new item(pipeline), e.g., golang, select the Pipeline option, and click on the OK button.

In the next screen, click on the Build Triggers tab, check the Build when a change is pushed to GitLab..... checkbox.

At the bottom right-hand side, just below the Comment (regex) for triggering a build form field, you will find the Advanced… button. Please click on it.

Then, click on the Generate button under Secret token to generate a token. We will use this token for Gitlab’s Webhook Settings. This webhook setting will allow Gitlab to let Jenkins know whenever a change is made to the repository.

Please visit the following GitLab URL to set up the Jenkins webhook.

Name	Value
URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang/hooks
Fill the form using the following details.

Name	Value
Jenkins URL	https://jenkins-xIKciVAk.lab.practical-devsecops.training/project/django.nv
Secret Token	paste the secret token we just generated above.
Click on the Add webhook button, and go back to the Jenkins tab to continue the setup process.

Click on the Pipeline tab, and select Pipeline script from SCM from Definition options. Once you do that, few more options would be made available to you.

Select Git as SCM, enter our django.nv repository http url.

Name	Value
Repository URL	http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang.git
Since we haven’t added the GitLab credentials to the Jenkins system, you will get an error.

Failed to connect to repository : Command "git ls-remote -h -- http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang HEAD" returned status code 128:
stdout:
stderr: remote: HTTP Basic: Access denied
fatal: Authentication failed for 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/golang.git/'
Let’s add the credentials by clicking on the Add button (the one with a key symbol). Select the Jenkins option and fill the pop-up form with the following details.

Name	Value
Username	root
Password	pdso-training
ID	gitlab-auth
Description	Leave it blank as it’s optional
Click on the Add button, and select our new credentials from the Credentials Dropdown.

The error we experienced before should be gone by now.

Finally, click the Save button.

Let’s move to the next step.

 
 Terminal
A simple CI/CD pipeline
Considering your DevOps team created a simple Jenkinsfile with the following contents.

pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            steps {
                echo "This is a build step"
            }
        }
        stage("test") {
            steps {
                echo "This is a test step"
            }
        }
        stage("integration") {
            steps {
                // Allow the stage to fail
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
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'failed')
        }
        unstable {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'failed')
        }
        success {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'success')
        }
        aborted {
            updateGitlabCommitStatus(name: "${env.STAGE_NAME}", state: 'canceled')
        }
        always {
            deleteDir()                     // clean up workspace
        }
    }
}
We do have 4 stage in this pipeline, build, test, integration and prod. We did integrate SCA/OAST beforehand, so we can carry it forward in this exercise.

As a security engineer, I do not care what they are doing as part of these stages. Why? Imagine having to learn every build/testing tool used by your DevOps team. It will be a nightmare. Instead, rely on the DevOps team for help.

Let’s log in to Gitlab using the following details.

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
Username	root
Password	pdso-training
Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the Commit changes button.

Verify the pipeline run
Since we want to use Jenkins to execute the CI/CD jobs, we need to remove .gitlab-ci.yml from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the Gitlab Runner and the Jenkins systems.

Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the .gitlab-ci.yaml file is missing.

Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/golang.

Click on the appropriate build history to see the output.

Challenge
Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

Read the gosec documentation
Embed SAST backend tool, Gosec in the test stage
Ensure the job is running under the test stage
Follow all the best practices while embedding Gosec in the CI/CD pipeline. Don’t forget the tool evaluation criteria
Please try to do this exercise without looking at the solution on the next page.

Let’s move to the next step.

 
 Terminal

Embed Gosec in Jenkins
As discussed in the Static Analysis using Gosec exercise, we can embed the GoSec in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the pipeline.

pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            steps {
                echo "This is a build step"
            }
        }
        stage("sast") {
            steps {
                sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
        stage("integration") {
            steps {
                // Allow the stage to fail
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
As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/golang.

Click on the appropriate build history to see the output.

Let’s move to the next step.

 
 Terminal
Embed Gosec in Jenkins
As discussed in the Static Analysis using Gosec exercise, we can embed the GoSec in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the pipeline.

pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            steps {
                echo "This is a build step"
            }
        }
        stage("sast") {
            steps {
                sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
        stage("integration") {
            steps {
                // Allow the stage to fail
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
As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/golang.

Click on the appropriate build history to see the output.

Let’s move to the next step.

 
 Terminal


Allow the stage failure
Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the catchError function to “not fail the build” even though the tool found issues.

Reference: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps.

        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
After adding the catchError function, the pipeline would look like the following.

pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            steps {
                echo "This is a build step."
            }
        }
        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
        stage("integration") {
            steps {
                // Allow the stage to fail
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
You will notice that the sast stage failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/golang.

Click on the appropriate build history to see the output.

 
 Terminal
Allow the stage failure
Remember!

Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise

After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the catchError function to “not fail the build” even though the tool found issues.

Reference: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps.

        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
After adding the catchError function, the pipeline would look like the following.

pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("build") {
            steps {
                echo "This is a build step."
            }
        }
        stage("sast") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh "docker run --rm -w /src -v \$(pwd):/src securego/gosec -fmt json -out /src/gosec-output.json /src/..."
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'gosec-output.json', fingerprint: true
                }
            }
        }
        stage("integration") {
            steps {
                // Allow the stage to fail
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
You will notice that the sast stage failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/golang.

Click on the appropriate build history to see the output.

 
 Terminal

Static Analysis using Semgrep Optional
We will learn the basic usage of semgrep to perfom static analysis
In this scenario, you will learn basics of semgrep to perform static code analysis.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.

 
 Terminal

Install Semgrep
Semgrep is a fast, open-source, static analysis tool that excels at expressing code standards — without complicated queries — and surfacing bugs early in the development flow. Precise rules look like the code you’re searching; no more traversing abstract syntax trees or wrestling with regexes.

You can find more details about the project at https://github.com/returntocorp/semgrep.

Let’s install the Semgrep tool on the system to perform static analysis.

pip3 install semgrep

Collecting semgrep
  Downloading semgrep-0.41.1-cp36.cp37.cp38.py36.py37.py38-none-any.whl (52.4 MB)
     |████████████████████████████████| 52.4 MB 22.4 MB/s 
Requirement already satisfied: setuptools in /usr/local/lib/python3.6/dist-packages (from semgrep) (51.1.2)
Requirement already satisfied: packaging>=20.4 in /usr/local/lib/python3.6/dist-packages (from semgrep) (20.8)
Requirement already satisfied: colorama>=0.4.3 in /usr/local/lib/python3.6/dist-packages (from semgrep) (0.4.4)
Collecting junit-xml==1.9
  Downloading junit_xml-1.9-py2.py3-none-any.whl (7.1 kB)
Requirement already satisfied: six in /usr/local/lib/python3.6/dist-packages (from junit-xml==1.9->semgrep) (1.15.0)
Collecting ruamel.yaml==0.16.10
  Downloading ruamel.yaml-0.16.10-py2.py3-none-any.whl (111 kB)
     |████████████████████████████████| 111 kB 68.6 MB/s 
Requirement already satisfied: ruamel.yaml.clib>=0.1.2 in /usr/local/lib/python3.6/dist-packages (from ruamel.yaml==0.16.10->semgrep) (0.2.2)
Collecting attrs>=19.3.0
  Downloading attrs-20.3.0-py2.py3-none-any.whl (49 kB)
     |████████████████████████████████| 49 kB 16.8 MB/s 
Collecting jsonschema~=3.2.0
  Downloading jsonschema-3.2.0-py2.py3-none-any.whl (56 kB)
     |████████████████████████████████| 56 kB 10.5 MB/s 
Requirement already satisfied: importlib-metadata in /usr/local/lib/python3.6/dist-packages (from jsonschema~=3.2.0->semgrep) (3.4.0)
Requirement already satisfied: pyparsing>=2.0.2 in /usr/local/lib/python3.6/dist-packages (from packaging>=20.4->semgrep) (2.4.7)
Collecting pyrsistent>=0.14.0
  Downloading pyrsistent-0.17.3.tar.gz (106 kB)
     |████████████████████████████████| 106 kB 88.9 MB/s 
Collecting requests>=2.22.0
  Downloading requests-2.25.1-py2.py3-none-any.whl (61 kB)
     |████████████████████████████████| 61 kB 19.7 MB/s 
Collecting certifi>=2017.4.17
  Downloading certifi-2020.12.5-py2.py3-none-any.whl (147 kB)
     |████████████████████████████████| 147 kB 77.7 MB/s 
Collecting chardet<5,>=3.0.2
  Downloading chardet-4.0.0-py2.py3-none-any.whl (178 kB)
     |████████████████████████████████| 178 kB 91.5 MB/s 
Collecting idna<3,>=2.5
  Downloading idna-2.10-py2.py3-none-any.whl (58 kB)
     |████████████████████████████████| 58 kB 19.0 MB/s 
Collecting tqdm>=4.46.1
  Downloading tqdm-4.58.0-py2.py3-none-any.whl (73 kB)
     |████████████████████████████████| 73 kB 6.3 MB/s 
Collecting urllib3<1.27,>=1.21.1
  Downloading urllib3-1.26.3-py2.py3-none-any.whl (137 kB)
     |████████████████████████████████| 137 kB 93.6 MB/s 
Requirement already satisfied: typing-extensions>=3.6.4 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.7.4.3)
Requirement already satisfied: zipp>=0.5 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.4.0)
Building wheels for collected packages: pyrsistent
  Building wheel for pyrsistent (setup.py) ... done
  Created wheel for pyrsistent: filename=pyrsistent-0.17.3-cp36-cp36m-linux_x86_64.whl size=55876 sha256=ed03cf66d9f739ce29846be29bcb1e0dfb457c5150509abf27323da125440abd
  Stored in directory: /root/.cache/pip/wheels/34/13/19/294da8e11bce7e563afee51251b9fa878185e14f4b5caf00cb
Successfully built pyrsistent
Installing collected packages: urllib3, pyrsistent, idna, chardet, certifi, attrs, tqdm, ruamel.yaml, requests, junit-xml, jsonschema, semgrep
  Attempting uninstall: ruamel.yaml
    Found existing installation: ruamel.yaml 0.16.12
    Uninstalling ruamel.yaml-0.16.12:
      Successfully uninstalled ruamel.yaml-0.16.12
Successfully installed attrs-20.3.0 certifi-2020.12.5 chardet-4.0.0 idna-2.10 jsonschema-3.2.0 junit-xml-1.9 pyrsistent-0.17.3 requests-2.25.1 ruamel.yaml-0.16.10 semgrep-0.41.1 tqdm-4.58.0 urllib3-1.26.3
We have successfully installed semgrep, let’s explore the functionality it provides us.

semgrep --help

usage: semgrep [-h] [-g | -f CONFIG | -e PATTERN] [-l LANG] [--validate] [--strict] [--exclude EXCLUDE] [--include INCLUDE] [--no-git-ignore] [--skip-unknown-extensions] [--dangerously-allow-arbitrary-code-execution-from-rules]
               [-j JOBS] [--timeout TIMEOUT] [--max-memory MAX_MEMORY] [--timeout-threshold TIMEOUT_THRESHOLD] [-q] [--no-rewrite-rule-ids] [-o OUTPUT] [--json] [--debugging-json] [--sarif] [--test] [--test-ignore-todo] [--dump-ast]
               [--error] [-a] [--dryrun] [--disable-nosem] [-v] [--version] [--force-color] [--disable-version-check]
               [target [target ...]]

semgrep CLI. For more information about semgrep, go to https://semgrep.dev/

positional arguments:
  target                Search these files or directories. Defaults to entire current working directory. Implied argument if piping to semgrep.

optional arguments:
  -h, --help            show this help message and exit
  --exclude EXCLUDE     Skip any file or directory that matches this pattern; --exclude='*.py' will ignore the following: foo.py, src/foo.py, foo.py/bar.sh. --exclude='tests' will ignore tests/foo.py as well as a/b/tests/c/foo.py. Can
                        add multiple times. Overrides includes.
  --include INCLUDE     Scan only files or directories that match this pattern; --include='*.jsx' will scan the following: foo.jsx, src/foo.jsx, foo.jsx/bar.sh. --include='src' will scan src/foo.py as well as a/b/src/c/foo.py. Can add
                        multiple times.
  --no-git-ignore       Scan all files even those ignored by a projects gitignore(s)
  --skip-unknown-extensions
                        Scan only known file extensions, even if unrecognized ones are explicitly targeted.
  --test-ignore-todo    Ignore rules marked as '#todoruleid:' in test files.
  --version             Show the version and exit.
  --force-color         Always include ANSI color in the output, even if not writing to a TTY
  --disable-version-check
                        Disable checking for latest version.
config:
  -g, --generate-config
                        Generate starter configuration file, .semgrep.yml
  -f CONFIG, --config CONFIG
                        YAML configuration file, directory of YAML files
                        ending in .yml|.yaml, URL of a configuration file, or
                        semgrep registry entry name. See
                        https://semgrep.dev/docs/writing-rules/rule-syntax for
                        information on configuration file format.
  -e PATTERN, --pattern PATTERN
                        Code search pattern. See
                        https://semgrep.dev/docs/writing-rules/pattern-syntax
                        for information on pattern features.
  -l LANG, --lang LANG  Parse pattern and all files in specified language.
                        Must be used with -e/--pattern.
  --validate            Validate configuration file(s). No search is
                        performed.
  --strict              Only invoke semgrep if configuration files(s) are
                        valid.
  --dangerously-allow-arbitrary-code-execution-from-rules
                        WARNING: allow rules to run arbitrary code. ONLY
                        ENABLE IF YOU TRUST THE SOURCE OF ALL RULES IN YOUR
                        CONFIGURATION.
  -j JOBS, --jobs JOBS  Number of subprocesses to use to run checks in
                        parallel. Defaults to the number of CPUs on the
                        system.
  --timeout TIMEOUT     Maximum time to spend running a rule on a single file
                        in seconds. If set to 0 will not have time limit.
                        Defaults to 30 s.
  --max-memory MAX_MEMORY
                        Maximum memory to use running a rule on a single file
                        in MB. If set to 0 will not have memory limit.
                        Defaults to 0.
  --timeout-threshold TIMEOUT_THRESHOLD
                        Maximum number of rules that can timeout on a file
                        before the file is skipped. If set to 0 will not have
                        limit. Defaults to 0.
  --severity SEVERITY   Report findings only from rules matching the supplied
                        severity level. By default all applicable rules are
                        run.Can add multiple times. Each should be one of
                        INFO, WARNING, or ERROR.

output:
  -q, --quiet           Do not print any logging messages to stderr. Finding
                        output will still be sent to stdout. Exit code
                        provides success status.
  --no-rewrite-rule-ids
                        Do not rewrite rule ids when they appear in nested
                        sub-directories (by default, rule 'foo' in
                        test/rules.yaml will be renamed 'test.foo').
  -o OUTPUT, --output OUTPUT
                        Save search results to a file or post to URL. Default
                        is to print to stdout.
  --json                Output results in JSON format.
  --save-test-output-tar
                        Store json output as a tarball that will be uploaded
                        as a Github artifact.
  --debugging-json      Output JSON with extra debugging information
                        (experimental).
  --junit-xml           Output results in JUnit XML format.
  --sarif               Output results in SARIF format.
  --test                Run test suite.
  --dump-ast            Show AST of the input file or passed expression and
                        then exit (can use --json).
  --error               Exit 1 if there are findings. Useful for CI and
                        scripts.
  -a, --autofix         Apply the autofix patches. WARNING: data loss can
                        occur with this flag. Make sure your files are stored
                        in a version control system.
  --dryrun              Do autofixes, but don't write them to a file. This
                        will print the changes to the console. This lets you
                        see the changes before you commit to them. Only works
                        with the --autofix flag. Otherwise does nothing.
  --disable-nosem       Disable the effect of 'nosem'. This will report
                        findings on lines containing a 'nosem' comment at the
                        end.
  --max-lines-per-finding MAX_LINES_PER_FINDING
                        Maximum number of lines of code that will be shown for
                        each match before trimming (set to 0 for unlimited).
  --debug               Set the logging level to DEBUG

logging:
  -v, --verbose         Show more details about what rules are running, which
                        files failed to parse, etc.
Let’s move to the next step.

 
 Terminal

Basic usage of Semgrep
The tool has four categories of parameters.

Category	Description
positional arguments	The target of file or directory that we want to scan
optional arguments	Many optional arguments like include/exclude file/dir to scan
config	Configuration to scan the code
output	The result output
For example, we will scan the source code of webapp to see all the os.system calls in the program.

semgrep --lang python -e "os.system(...)" .

taskManager/misc.py
33:    os.system(
34:        "mv " +
35:        uploaded_file.temporary_file_path() +
36:        " " +
37:        "%s/%s" %
38:        (upload_dir_path,
39:         title))
ran 1 rules on 50 files: 1 findings
–lang is the parameter to set which programming language that we want to scan.
-e is the parameter to set the pattern for code search pattern, see the details here.
webapp is a target directory where the source code is located.

And also, we can set the output result into a JSON file.

semgrep --lang python -e "os.system(...)" . --json | jq

ran 1 rules on 51 files: 1 findings
{
  "results": [
    {
      "check_id": "-",
      "path": "taskManager/misc.py",
      "start": {
        "line": 33,
        "col": 5
      },
      "end": {
        "line": 39,
        "col": 17
      },
      "extra": {
        "message": "os.system(...)",
        "metavars": {},
        "metadata": {},
        "severity": "ERROR",
        "is_ignored": false,
        "lines": "    os.system(\n        \"mv \" +\n        uploaded_file.temporary_file_path() +\n        \" \" +\n        \"%s/%s\" %\n        (upload_dir
_path,\n         title))"
      }
    }
  ],
  "errors": []
}
We can specify a file or directory that we want to scan by providing –include argument.

semgrep --lang python -e "DEBUG =True" --include settings.py .

taskManager/settings.py
28:DEBUG = True
ran 1 rules on 1 files: 1 findings
The above command will perform a scan on settings.py file, to see if DEBUG=True is being used in the settings.py file.

Let’s move to the next step.

 
 Terminal
Challenge
Scan all declarations of variables in webapp source code
Next, scan all function calls that have request as an argument
Please do not forget to share the answer with our staff via Slack Direct Message (DM).

 
 Terminal

How to Write Custom Rule in Semgrep Optional
We will learn how to create a custom rule for Semgrep
In this scenario, you will learn how to write a custom rule for the Semgrep tool, a static analysis tool.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.

 
 Terminal
Install Semgrep
Semgrep is a fast, open-source, static analysis tool that excels at expressing code standards — without complicated queries — and surfacing bugs early in the development flow. Precise rules look like the code you’re searching; no more traversing abstract syntax trees or wrestling with regexes.

You can find more details about the project at https://github.com/returntocorp/semgrep.

Let’s install the semgrep tool on the system to perform static analysis.

pip3 install semgrep

Collecting semgrep
  Downloading semgrep-0.41.1-cp36.cp37.cp38.py36.py37.py38-none-any.whl (52.4 MB)
     |████████████████████████████████| 52.4 MB 22.4 MB/s 
Requirement already satisfied: setuptools in /usr/local/lib/python3.6/dist-packages (from semgrep) (51.1.2)
Requirement already satisfied: packaging>=20.4 in /usr/local/lib/python3.6/dist-packages (from semgrep) (20.8)
Requirement already satisfied: colorama>=0.4.3 in /usr/local/lib/python3.6/dist-packages (from semgrep) (0.4.4)
Collecting junit-xml==1.9
  Downloading junit_xml-1.9-py2.py3-none-any.whl (7.1 kB)
Requirement already satisfied: six in /usr/local/lib/python3.6/dist-packages (from junit-xml==1.9->semgrep) (1.15.0)
Collecting ruamel.yaml==0.16.10
  Downloading ruamel.yaml-0.16.10-py2.py3-none-any.whl (111 kB)
     |████████████████████████████████| 111 kB 68.6 MB/s 
Requirement already satisfied: ruamel.yaml.clib>=0.1.2 in /usr/local/lib/python3.6/dist-packages (from ruamel.yaml==0.16.10->semgrep) (0.2.2)
Collecting attrs>=19.3.0
  Downloading attrs-20.3.0-py2.py3-none-any.whl (49 kB)
     |████████████████████████████████| 49 kB 16.8 MB/s 
Collecting jsonschema~=3.2.0
  Downloading jsonschema-3.2.0-py2.py3-none-any.whl (56 kB)
     |████████████████████████████████| 56 kB 10.5 MB/s 
Requirement already satisfied: importlib-metadata in /usr/local/lib/python3.6/dist-packages (from jsonschema~=3.2.0->semgrep) (3.4.0)
Requirement already satisfied: pyparsing>=2.0.2 in /usr/local/lib/python3.6/dist-packages (from packaging>=20.4->semgrep) (2.4.7)
Collecting pyrsistent>=0.14.0
  Downloading pyrsistent-0.17.3.tar.gz (106 kB)
     |████████████████████████████████| 106 kB 88.9 MB/s 
Collecting requests>=2.22.0
  Downloading requests-2.25.1-py2.py3-none-any.whl (61 kB)
     |████████████████████████████████| 61 kB 19.7 MB/s 
Collecting certifi>=2017.4.17
  Downloading certifi-2020.12.5-py2.py3-none-any.whl (147 kB)
     |████████████████████████████████| 147 kB 77.7 MB/s 
Collecting chardet<5,>=3.0.2
  Downloading chardet-4.0.0-py2.py3-none-any.whl (178 kB)
     |████████████████████████████████| 178 kB 91.5 MB/s 
Collecting idna<3,>=2.5
  Downloading idna-2.10-py2.py3-none-any.whl (58 kB)
     |████████████████████████████████| 58 kB 19.0 MB/s 
Collecting tqdm>=4.46.1
  Downloading tqdm-4.58.0-py2.py3-none-any.whl (73 kB)
     |████████████████████████████████| 73 kB 6.3 MB/s 
Collecting urllib3<1.27,>=1.21.1
  Downloading urllib3-1.26.3-py2.py3-none-any.whl (137 kB)
     |████████████████████████████████| 137 kB 93.6 MB/s 
Requirement already satisfied: typing-extensions>=3.6.4 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.7.4.3)
Requirement already satisfied: zipp>=0.5 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.4.0)
Building wheels for collected packages: pyrsistent
  Building wheel for pyrsistent (setup.py) ... done
  Created wheel for pyrsistent: filename=pyrsistent-0.17.3-cp36-cp36m-linux_x86_64.whl size=55876 sha256=ed03cf66d9f739ce29846be29bcb1e0dfb457c5150509abf27323da125440abd
  Stored in directory: /root/.cache/pip/wheels/34/13/19/294da8e11bce7e563afee51251b9fa878185e14f4b5caf00cb
Successfully built pyrsistent
Installing collected packages: urllib3, pyrsistent, idna, chardet, certifi, attrs, tqdm, ruamel.yaml, requests, junit-xml, jsonschema, semgrep
  Attempting uninstall: ruamel.yaml
    Found existing installation: ruamel.yaml 0.16.12
    Uninstalling ruamel.yaml-0.16.12:
      Successfully uninstalled ruamel.yaml-0.16.12
Successfully installed attrs-20.3.0 certifi-2020.12.5 chardet-4.0.0 idna-2.10 jsonschema-3.2.0 junit-xml-1.9 pyrsistent-0.17.3 requests-2.25.1 ruamel.yaml-0.16.10 semgrep-0.41.1 tqdm-4.58.0 urllib3-1.26.3
We have successfully installed semgrep, let’s explore the functionality it provides us.

semgrep --help

usage: semgrep [-h] [-g | -f CONFIG | -e PATTERN] [-l LANG] [--validate] [--strict] [--exclude EXCLUDE] [--include INCLUDE] [--no-git-ignore] [--skip-unknown-extensions] [--dangerously-allow-arbitrary-code-execution-from-rules]
               [-j JOBS] [--timeout TIMEOUT] [--max-memory MAX_MEMORY] [--timeout-threshold TIMEOUT_THRESHOLD] [-q] [--no-rewrite-rule-ids] [-o OUTPUT] [--json] [--debugging-json] [--sarif] [--test] [--test-ignore-todo] [--dump-ast]
               [--error] [-a] [--dryrun] [--disable-nosem] [-v] [--version] [--force-color] [--disable-version-check]
               [target [target ...]]

semgrep CLI. For more information about semgrep, go to https://semgrep.dev/

positional arguments:
  target                Search these files or directories. Defaults to entire current working directory. Implied argument if piping to semgrep.

optional arguments:
  -h, --help            show this help message and exit
  --exclude EXCLUDE     Skip any file or directory that matches this pattern; --exclude='*.py' will ignore the following: foo.py, src/foo.py, foo.py/bar.sh. --exclude='tests' will ignore tests/foo.py as well as a/b/tests/c/foo.py. Can
                        add multiple times. Overrides includes.
  --include INCLUDE     Scan only files or directories that match this pattern; --include='*.jsx' will scan the following: foo.jsx, src/foo.jsx, foo.jsx/bar.sh. --include='src' will scan src/foo.py as well as a/b/src/c/foo.py. Can add
                        multiple times.
  --no-git-ignore       Scan all files even those ignored by a projects gitignore(s)
  --skip-unknown-extensions
                        Scan only known file extensions, even if unrecognized ones are explicitly targeted.
  --test-ignore-todo    Ignore rules marked as '#todoruleid:' in test files.
  --version             Show the version and exit.
  --force-color         Always include ANSI color in the output, even if not writing to a TTY
  --disable-version-check
                        Disable checking for latest version.
config:
  -g, --generate-config
                        Generate starter configuration file, .semgrep.yml
  -f CONFIG, --config CONFIG
                        YAML configuration file, directory of YAML files
                        ending in .yml|.yaml, URL of a configuration file, or
                        semgrep registry entry name. See
                        https://semgrep.dev/docs/writing-rules/rule-syntax for
                        information on configuration file format.
  -e PATTERN, --pattern PATTERN
                        Code search pattern. See
                        https://semgrep.dev/docs/writing-rules/pattern-syntax
                        for information on pattern features.
  -l LANG, --lang LANG  Parse pattern and all files in specified language.
                        Must be used with -e/--pattern.
  --validate            Validate configuration file(s). No search is
                        performed.
  --strict              Only invoke semgrep if configuration files(s) are
                        valid.
  --dangerously-allow-arbitrary-code-execution-from-rules
                        WARNING: allow rules to run arbitrary code. ONLY
                        ENABLE IF YOU TRUST THE SOURCE OF ALL RULES IN YOUR
                        CONFIGURATION.
  -j JOBS, --jobs JOBS  Number of subprocesses to use to run checks in
                        parallel. Defaults to the number of CPUs on the
                        system.
  --timeout TIMEOUT     Maximum time to spend running a rule on a single file
                        in seconds. If set to 0 will not have time limit.
                        Defaults to 30 s.
  --max-memory MAX_MEMORY
                        Maximum memory to use running a rule on a single file
                        in MB. If set to 0 will not have memory limit.
                        Defaults to 0.
  --timeout-threshold TIMEOUT_THRESHOLD
                        Maximum number of rules that can timeout on a file
                        before the file is skipped. If set to 0 will not have
                        limit. Defaults to 0.
  --severity SEVERITY   Report findings only from rules matching the supplied
                        severity level. By default all applicable rules are
                        run.Can add multiple times. Each should be one of
                        INFO, WARNING, or ERROR.

output:
  -q, --quiet           Do not print any logging messages to stderr. Finding
                        output will still be sent to stdout. Exit code
                        provides success status.
  --no-rewrite-rule-ids
                        Do not rewrite rule ids when they appear in nested
                        sub-directories (by default, rule 'foo' in
                        test/rules.yaml will be renamed 'test.foo').
  -o OUTPUT, --output OUTPUT
                        Save search results to a file or post to URL. Default
                        is to print to stdout.
  --json                Output results in JSON format.
  --save-test-output-tar
                        Store json output as a tarball that will be uploaded
                        as a Github artifact.
  --debugging-json      Output JSON with extra debugging information
                        (experimental).
  --junit-xml           Output results in JUnit XML format.
  --sarif               Output results in SARIF format.
  --test                Run test suite.
  --dump-ast            Show AST of the input file or passed expression and
                        then exit (can use --json).
  --error               Exit 1 if there are findings. Useful for CI and
                        scripts.
  -a, --autofix         Apply the autofix patches. WARNING: data loss can
                        occur with this flag. Make sure your files are stored
                        in a version control system.
  --dryrun              Do autofixes, but don't write them to a file. This
                        will print the changes to the console. This lets you
                        see the changes before you commit to them. Only works
                        with the --autofix flag. Otherwise does nothing.
  --disable-nosem       Disable the effect of 'nosem'. This will report
                        findings on lines containing a 'nosem' comment at the
                        end.
  --max-lines-per-finding MAX_LINES_PER_FINDING
                        Maximum number of lines of code that will be shown for
                        each match before trimming (set to 0 for unlimited).
  --debug               Set the logging level to DEBUG

logging:
  -v, --verbose         Show more details about what rules are running, which
                        files failed to parse, etc.
Let’s move to the next step.

 
 Terminal
Pattern syntax
Semgrep tool has several ways to specify the Pattern syntax, for e.g., Ellipsis operator (…). This operator is used to match a sequence of zero or more arguments, statements, or characters in the given code.

os.system(...)
The above pattern will match the following functions:

os.system("command")
os.system(command)
os.system("really_weird_command")
os.system(really_weird_command)
Metavariables can be used to track values across a specific code scope. This includes variables, functions, arguments, classes, object methods, imports, exceptions, and more especially when we don’t know the value or contents beforehand.

$X = $Y
semgrep --lang python -e '$X = $Y' .

The above command will match the following statements

taskManager/forms.py
27:    user_list = User.objects.order_by('date_joined')
--------------------------------------------------------------------------------
28:    user_tuple = []
--------------------------------------------------------------------------------
29:    counter = 1
--------------------------------------------------------------------------------
32:        counter = counter + 1
--------------------------------------------------------------------------------
41:    task_list = []
--------------------------------------------------------------------------------
42:    tasks = Task.objects.all()
--------------------------------------------------------------------------------
47:    task_tuple = []
--------------------------------------------------------------------------------
48:    counter = 1
--------------------------------------------------------------------------------
51:        counter = counter + 1
--------------------------------------------------------------------------------
60:    proj_list = Project.objects.all()
--------------------------------------------------------------------------------
61:    proj_tuple = []
--------------------------------------------------------------------------------
...[SNIP]...
Lets take another example.

$X.objects.get
The above pattern will match all code snippets that have suffix .object.get

semgrep --lang python -e '$X.objects.get' .

taskManager/views.py
42:    proj = Project.objects.get(pk=project_id)
--------------------------------------------------------------------------------
53:                user = User.objects.get(pk=userid)
--------------------------------------------------------------------------------
54:                task = Task.objects.get(pk=taskid)
--------------------------------------------------------------------------------
88:                user = User.objects.get(pk=userid)
--------------------------------------------------------------------------------
89:                project = Project.objects.get(pk=projectid)
--------------------------------------------------------------------------------
130:                    grp = Group.objects.get(name=accesslevel)
--------------------------------------------------------------------------------
133:                specified_user = User.objects.get(pk=post_data["userid"])
--------------------------------------------------------------------------------
174:        proj = Project.objects.get(pk=project_id)
--------------------------------------------------------------------------------
208:    file = File.objects.get(pk=file_id)
--------------------------------------------------------------------------------
222:    user = User.objects.get(pk=user_id)
--------------------------------------------------------------------------------
244:        proj = Project.objects.get(pk=project_id)
--------------------------------------------------------------------------------
As you can see the Semgrep is pretty powerful.

Let’s move to the next step.

 
 Terminal
Rule syntax
Semgrep have several ways to represent custom Rules. Let’s explore few of these in order to write semgrep rules.

Schema

The following fields are required while creating a rule in Semgrep.

Name	Value
id (string)	Unique, descriptive identifier, e.g., possible-command-injection
message (string)	Message highlighting why this rule fired and how to remediate the issue, e.g. Command injection attack
severity (string)	One of: WARNING, ERROR
languages (array)	Any of: go, java, javascript, python, typescript
pattern (string)	Find code matching this expression
patterns (array)	Logical AND of multiple patterns
pattern-either (array)	Logical OR of multiple patterns
pattern-regex (string)	Search files for Python re compatible expressions
The following fields can be used inside patterns or pattern-either fields to target specific parts of the code

Name	Value
metavariable-regex (map)	Search metavariables for Python re compatible expressions
pattern-not (string)	Logical NOT - remove findings matching this expression
pattern-inside (string)	Keep findings that lie inside this pattern
pattern-not-inside (string)	Keep findings that do not lie inside this pattern
pattern-where-python (string)	Remove findings matching this Python expression
If you wish to search for a possible Command Injection vulnerability in our source code, we can write the following semgrep rule.

cat > command_injection.yaml <<EOF
rules:
- id: Possible Command Injection
  patterns:
  - pattern: os.system(...)
  - pattern-not: os.system("...")
  message: Possible Command Injection
  languages:
  - python
  severity: WARNING
EOF
Let’s run a semgrep scan using the above rule.

semgrep --lang python -f command_injection.yaml .

running 1 rules...
taskManager/misc.py
severity:warning rule:Possible Command Injection: Possible Command Injection
33:    os.system(
34:        "mv " +
35:        uploaded_file.temporary_file_path() +
36:        " " +
37:        "%s/%s" %
38:        (upload_dir_path,
39:         title))
ran 1 rules on 50 files: 1 findings
pattern-not: os.system(“…”) specifies that semgrep should not search os.system function that uses harcoded commands.

We can also use pattern-inside to search a pattern inside code blocks e.g., inside an if statement, a function call, a class or anything that contains a code block.

cat > find_project_db_get_call.yaml <<EOF
rules:
- id: find-get-project-db-value
  patterns:
  - pattern: Project.objects.get(...)
  - pattern-inside: |
      def \$FUNC(request):
        ...
  message: Get project db value
  languages:
  - python
  severity: WARNING
EOF
semgrep --lang python -f find_project_db_get_call.yaml .

running 1 rules...
taskManager/views.py
severity:warning rule:find-get-project-db-value: Get project db value
89:                project = Project.objects.get(pk=projectid)
ran 1 rules on 50 files: 1 findings
Challenge
This is the example code from webapp, you must create a semgrep rule to find SQL Injection vulnerability.

...[SNIP]...

def upload(request, project_id):

    if request.method == 'POST':

        proj = Project.objects.get(pk=project_id)
        form = ProjectFileForm(request.POST, request.FILES)

        if form.is_valid():
            name = request.POST.get('name', False)
            upload_path = store_uploaded_file(name, request.FILES['file'])

            #A1 - Injection (SQLi)
            curs = connection.cursor()
            curs.execute(
                "insert into taskManager_file ('name','path','project_id') values ('%s','%s',%s)" %
                (name, upload_path, project_id))

            # file = File(
            #name = name,
            #path = upload_path,
            # project = proj)

            # file.save()

            return redirect('/taskManager/' + project_id +
                            '/', {'new_file_added': True})
        else:
            form = ProjectFileForm()
    else:
        form = ProjectFileForm()
    return render_to_response(
        'taskManager/upload.html', {'form': form}, RequestContext(request))

...[SNIP]...
Please do not forget to share the answer with our staff via Slack Direct Message (DM).

 
 Terminal

Hunting Vulnerability with Semgrep Optional
We will learn how to find the vulnerabilities with a custom rule in Semgrep
In this scenario, you will learn how to use Semgrep to perform static code analysis.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp

Let’s cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.
Installing Semgrep
Semgrep is a fast, open-source, static analysis tool that excels at expressing code standards — without complicated queries — and surfacing bugs early in the development flow. Precise rules look like the code you’re searching; no more traversing abstract syntax trees or wrestling with regexes.

You can find more details about the project at https://github.com/returntocorp/semgrep

Let’s install the semgrep tool on the system to perform static analysis.

pip3 install semgrep

Collecting semgrep
  Downloading semgrep-0.41.1-cp36.cp37.cp38.py36.py37.py38-none-any.whl (52.4 MB)
     |████████████████████████████████| 52.4 MB 22.4 MB/s 
Requirement already satisfied: setuptools in /usr/local/lib/python3.6/dist-packages (from semgrep) (51.1.2)
Requirement already satisfied: packaging>=20.4 in /usr/local/lib/python3.6/dist-packages (from semgrep) (20.8)
Requirement already satisfied: colorama>=0.4.3 in /usr/local/lib/python3.6/dist-packages (from semgrep) (0.4.4)
Collecting junit-xml==1.9
  Downloading junit_xml-1.9-py2.py3-none-any.whl (7.1 kB)
Requirement already satisfied: six in /usr/local/lib/python3.6/dist-packages (from junit-xml==1.9->semgrep) (1.15.0)
Collecting ruamel.yaml==0.16.10
  Downloading ruamel.yaml-0.16.10-py2.py3-none-any.whl (111 kB)
     |████████████████████████████████| 111 kB 68.6 MB/s 
Requirement already satisfied: ruamel.yaml.clib>=0.1.2 in /usr/local/lib/python3.6/dist-packages (from ruamel.yaml==0.16.10->semgrep) (0.2.2)
Collecting attrs>=19.3.0
  Downloading attrs-20.3.0-py2.py3-none-any.whl (49 kB)
     |████████████████████████████████| 49 kB 16.8 MB/s 
Collecting jsonschema~=3.2.0
  Downloading jsonschema-3.2.0-py2.py3-none-any.whl (56 kB)
     |████████████████████████████████| 56 kB 10.5 MB/s 
Requirement already satisfied: importlib-metadata in /usr/local/lib/python3.6/dist-packages (from jsonschema~=3.2.0->semgrep) (3.4.0)
Requirement already satisfied: pyparsing>=2.0.2 in /usr/local/lib/python3.6/dist-packages (from packaging>=20.4->semgrep) (2.4.7)
Collecting pyrsistent>=0.14.0
  Downloading pyrsistent-0.17.3.tar.gz (106 kB)
     |████████████████████████████████| 106 kB 88.9 MB/s 
Collecting requests>=2.22.0
  Downloading requests-2.25.1-py2.py3-none-any.whl (61 kB)
     |████████████████████████████████| 61 kB 19.7 MB/s 
Collecting certifi>=2017.4.17
  Downloading certifi-2020.12.5-py2.py3-none-any.whl (147 kB)
     |████████████████████████████████| 147 kB 77.7 MB/s 
Collecting chardet<5,>=3.0.2
  Downloading chardet-4.0.0-py2.py3-none-any.whl (178 kB)
     |████████████████████████████████| 178 kB 91.5 MB/s 
Collecting idna<3,>=2.5
  Downloading idna-2.10-py2.py3-none-any.whl (58 kB)
     |████████████████████████████████| 58 kB 19.0 MB/s 
Collecting tqdm>=4.46.1
  Downloading tqdm-4.58.0-py2.py3-none-any.whl (73 kB)
     |████████████████████████████████| 73 kB 6.3 MB/s 
Collecting urllib3<1.27,>=1.21.1
  Downloading urllib3-1.26.3-py2.py3-none-any.whl (137 kB)
     |████████████████████████████████| 137 kB 93.6 MB/s 
Requirement already satisfied: typing-extensions>=3.6.4 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.7.4.3)
Requirement already satisfied: zipp>=0.5 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.4.0)
Building wheels for collected packages: pyrsistent
  Building wheel for pyrsistent (setup.py) ... done
  Created wheel for pyrsistent: filename=pyrsistent-0.17.3-cp36-cp36m-linux_x86_64.whl size=55876 sha256=ed03cf66d9f739ce29846be29bcb1e0dfb457c5150509abf27323da125440abd
  Stored in directory: /root/.cache/pip/wheels/34/13/19/294da8e11bce7e563afee51251b9fa878185e14f4b5caf00cb
Successfully built pyrsistent
Installing collected packages: urllib3, pyrsistent, idna, chardet, certifi, attrs, tqdm, ruamel.yaml, requests, junit-xml, jsonschema, semgrep
  Attempting uninstall: ruamel.yaml
    Found existing installation: ruamel.yaml 0.16.12
    Uninstalling ruamel.yaml-0.16.12:
      Successfully uninstalled ruamel.yaml-0.16.12
Successfully installed attrs-20.3.0 certifi-2020.12.5 chardet-4.0.0 idna-2.10 jsonschema-3.2.0 junit-xml-1.9 pyrsistent-0.17.3 requests-2.25.1 ruamel.yaml-0.16.10 semgrep-0.41.1 tqdm-4.58.0 urllib3-1.26.3
We have successfully installed semgrep, let’s explore the functionality it provides us.

semgrep --help

usage: semgrep [-h] [-g | -f CONFIG | -e PATTERN] [-l LANG] [--validate] [--strict] [--exclude EXCLUDE] [--include INCLUDE] [--no-git-ignore] [--skip-unknown-extensions] [--dangerously-allow-arbitrary-code-execution-from-rules]
               [-j JOBS] [--timeout TIMEOUT] [--max-memory MAX_MEMORY] [--timeout-threshold TIMEOUT_THRESHOLD] [-q] [--no-rewrite-rule-ids] [-o OUTPUT] [--json] [--debugging-json] [--sarif] [--test] [--test-ignore-todo] [--dump-ast]
               [--error] [-a] [--dryrun] [--disable-nosem] [-v] [--version] [--force-color] [--disable-version-check]
               [target [target ...]]

semgrep CLI. For more information about semgrep, go to https://semgrep.dev/

positional arguments:
  target                Search these files or directories. Defaults to entire current working directory. Implied argument if piping to semgrep.

optional arguments:
  -h, --help            show this help message and exit
  --exclude EXCLUDE     Skip any file or directory that matches this pattern; --exclude='*.py' will ignore the following: foo.py, src/foo.py, foo.py/bar.sh. --exclude='tests' will ignore tests/foo.py as well as a/b/tests/c/foo.py. Can
                        add multiple times. Overrides includes.
  --include INCLUDE     Scan only files or directories that match this pattern; --include='*.jsx' will scan the following: foo.jsx, src/foo.jsx, foo.jsx/bar.sh. --include='src' will scan src/foo.py as well as a/b/src/c/foo.py. Can add
                        multiple times.
  --no-git-ignore       Scan all files even those ignored by a projects gitignore(s)
  --skip-unknown-extensions
                        Scan only known file extensions, even if unrecognized ones are explicitly targeted.
  --test-ignore-todo    Ignore rules marked as '#todoruleid:' in test files.
  --version             Show the version and exit.
  --force-color         Always include ANSI color in the output, even if not writing to a TTY
  --disable-version-check
                        Disable checking for latest version.
config:
  -g, --generate-config
                        Generate starter configuration file, .semgrep.yml
  -f CONFIG, --config CONFIG
                        YAML configuration file, directory of YAML files
                        ending in .yml|.yaml, URL of a configuration file, or
                        semgrep registry entry name. See
                        https://semgrep.dev/docs/writing-rules/rule-syntax for
                        information on configuration file format.
  -e PATTERN, --pattern PATTERN
                        Code search pattern. See
                        https://semgrep.dev/docs/writing-rules/pattern-syntax
                        for information on pattern features.
  -l LANG, --lang LANG  Parse pattern and all files in specified language.
                        Must be used with -e/--pattern.
  --validate            Validate configuration file(s). No search is
                        performed.
  --strict              Only invoke semgrep if configuration files(s) are
                        valid.
  --dangerously-allow-arbitrary-code-execution-from-rules
                        WARNING: allow rules to run arbitrary code. ONLY
                        ENABLE IF YOU TRUST THE SOURCE OF ALL RULES IN YOUR
                        CONFIGURATION.
  -j JOBS, --jobs JOBS  Number of subprocesses to use to run checks in
                        parallel. Defaults to the number of CPUs on the
                        system.
  --timeout TIMEOUT     Maximum time to spend running a rule on a single file
                        in seconds. If set to 0 will not have time limit.
                        Defaults to 30 s.
  --max-memory MAX_MEMORY
                        Maximum memory to use running a rule on a single file
                        in MB. If set to 0 will not have memory limit.
                        Defaults to 0.
  --timeout-threshold TIMEOUT_THRESHOLD
                        Maximum number of rules that can timeout on a file
                        before the file is skipped. If set to 0 will not have
                        limit. Defaults to 0.
  --severity SEVERITY   Report findings only from rules matching the supplied
                        severity level. By default all applicable rules are
                        run.Can add multiple times. Each should be one of
                        INFO, WARNING, or ERROR.

output:
  -q, --quiet           Do not print any logging messages to stderr. Finding
                        output will still be sent to stdout. Exit code
                        provides success status.
  --no-rewrite-rule-ids
                        Do not rewrite rule ids when they appear in nested
                        sub-directories (by default, rule 'foo' in
                        test/rules.yaml will be renamed 'test.foo').
  -o OUTPUT, --output OUTPUT
                        Save search results to a file or post to URL. Default
                        is to print to stdout.
  --json                Output results in JSON format.
  --save-test-output-tar
                        Store json output as a tarball that will be uploaded
                        as a Github artifact.
  --debugging-json      Output JSON with extra debugging information
                        (experimental).
  --junit-xml           Output results in JUnit XML format.
  --sarif               Output results in SARIF format.
  --test                Run test suite.
  --dump-ast            Show AST of the input file or passed expression and
                        then exit (can use --json).
  --error               Exit 1 if there are findings. Useful for CI and
                        scripts.
  -a, --autofix         Apply the autofix patches. WARNING: data loss can
                        occur with this flag. Make sure your files are stored
                        in a version control system.
  --dryrun              Do autofixes, but don't write them to a file. This
                        will print the changes to the console. This lets you
                        see the changes before you commit to them. Only works
                        with the --autofix flag. Otherwise does nothing.
  --disable-nosem       Disable the effect of 'nosem'. This will report
                        findings on lines containing a 'nosem' comment at the
                        end.
  --max-lines-per-finding MAX_LINES_PER_FINDING
                        Maximum number of lines of code that will be shown for
                        each match before trimming (set to 0 for unlimited).
  --debug               Set the logging level to DEBUG

logging:
  -v, --verbose         Show more details about what rules are running, which
                        files failed to parse, etc.
Let’s move to the next step.

 
 Terminal
Cross-Site Request Forgery (CSRF)
Django applications would be vulnerable to CSRF attack if we’re using csrf_exempt decorator or django.middleware.csrf.CsrfViewMiddleware middleware is not set in MIDDLEWARE_CLASSES, we can use Semgrep to find those things to mitigate the CSRF vulnerability.

cat > csrf_hunting.yaml <<EOF
rules:
- id: possible-csrf
  patterns:
  - pattern-inside: | 
      @csrf_exempt
      def \$FUNC(\$X):
          ...
  message: |
    Possible CSRF
  languages:
  - python
  severity: WARNING

- id: no-csrf-middleware
  patterns:
  - pattern: MIDDLEWARE_CLASSES=(...)
  - pattern-not: MIDDLEWARE_CLASSES=(...,'django.middleware.csrf.CsrfViewMiddleware',...)
  message: |
    No CSRF middleware
  languages:
  - python
  severity: WARNING
EOF
There are 2 rules in YAML file.

possible-csrf: Find all function definitions that use @csrf_exempt as decorator
no-csrf-middleware: Search the string MIDDLEWARE_CLASSES and see if doesn’t have ‘django.middleware.csrf.CsrfViewMiddleware’ on it
Run Semgrep with our rules against the source code.

semgrep --lang python -f csrf_hunting.yaml .

running 2 rules...
100%|████████████████████████████████████████████████████████████████████|2/2
taskManager/views.py
severity:warning rule:possible-csrf: Possible CSRF

739:@csrf_exempt
740:def reset_password(request):
741:
742:    if request.method == 'POST':
743:
744:        reset_token = request.POST.get('reset_token')
745:
746:        try:
747:            userprofile = UserProfile.objects.get(reset_token = reset_token)
748:            if timezone.now() > userprofile.reset_token_expiration:
-------- [hid 27 additional lines, adjust with --max-lines-per-finding] --------
779:@csrf_exempt
780:def forgot_password(request):
781:
782:    if request.method == 'POST':
783:        t_email = request.POST.get('email')
784:
785:        try:
786:            reset_user = User.objects.get(email=t_email)
787:
788:            # Generate secure random 6 digit number
-------- [hid 21 additional lines, adjust with --max-lines-per-finding] --------
813:@csrf_exempt
814:def change_password(request):
815:
816:    if request.method == 'POST':
817:        user = request.user
818:        old_password = request.POST.get('old_password')
819:        new_password = request.POST.get('new_password')
820:        confirm_password = request.POST.get('confirm_password')
821:
822:        if authenticate(username=user.username, password=old_password):
-------- [hid 12 additional lines, adjust with --max-lines-per-finding] --------
ran 2 rules on 51 files: 3 findings
As you can see, we found three functions where CSRF vulnerability can be exploited.

Let’s move to the next step.

 
 Terminal

Misconfigurations
Security misconfiguration is a class of vulnerability that occurs when the software is set up incorrectly, left insecure and can happen at any level of an application stack including the platform, web server, application server, database, framework, or any custom code.

Source: OWASP Top 10

Let’s create a rule to hunt misconfigurations in our application e.g., availability of DEBUG=True flag in settings.py file.

cat > debug_enable.yaml <<EOF 
rules:
- id: debug-enabled
  patterns:
  - pattern: DEBUG=True
  message: |
    Detected Django app with DEBUG=True. Do not deploy to production with this flag enabled
    as it will leak sensitive information.
  metadata:
    cwe: 'CWE-489: Active Debug Code'
    owasp: 'A6: Security Misconfiguration'
    references:
    - https://blog.scrt.ch/2018/08/24/remote-code-execution-on-a-facebook-server/
  severity: WARNING
  languages:
  - python
EOF
Run Semgrep with our rules in the source code.

semgrep --lang python -f debug_enable.yaml .

running 1 rules...
taskManager/settings.py
severity:warning rule:debug-enabled: Detected Django app with DEBUG=True. Do not deploy to production with this flag enabled
as it will leak sensitive information.

28:DEBUG = True
ran 1 rules on 51 files: 1 findings
We scanned about 51 files and found 1 security issue.

Let’s move to the next step.

 
 Terminal

Ported Security Tools Ruleset
Semgrep’s team converted the security rulesets from different tools like bandit, gosec, nodejsscan, findsecbugs, or eslint-plugin-security into Semgrep rules. Let use these rulesets to scan our application.

We will use the Bandit ruleset for semgrep in this section.

semgrep --config "p/bandit" .

using config from https://semgrep.dev/p/bandit. Visit https://semgrep.dev/registry to see all public rules.
downloading config...
running 60 rules...
100%|█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████|60/60
taskManager/misc.py
severity:warning rule:python.lang.security.audit.dangerous-system-call.dangerous-system-call: Found dynamic content used in a system call. This is dangerous if external data can reach this function call because it allows a malicious actor to execute commands. Use the 'subprocess' module instead, which is easier to use without accidentally exposing a command injection vulnerability.
33:    os.system(
34:        "mv " +
35:        uploaded_file.temporary_file_path() +
36:        " " +
37:        "%s/%s" %
38:        (upload_dir_path,
39:         title))
ran 60 rules on 50 files: 1 findings
Semgrep ran 60 rules from bandit rulesets and found 1 vulnerability, so we don’t rely on predefined rules.

Did it miss any vulnerabilities which bandit found? If so, why?

Let’s move to the next step.

 
 Terminal

Challenge
Write a semgrep rule to detect Insecure Redirect in the following code snippet
def logout_view(request):
    logout(request)
    return redirect(request.GET.get('redirect', '/taskManager/'))
Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

 
 Terminal

How to Fix The Issues Reported by Semgrep Optional
We will learn how to fix the issues reported by Semgrep tools
In this scenario, you will learn how to fix issues reported by the semgrep tool in Django source code.

You will do the following in this activity.
1. Download the source code from the git repository.
2. Install the Semgrep tool.
3. Run the SAST scan on the code.
4. Fix the issues found by Semgrep.

 Terminal

Download the source code
We will do all the exercises locally first in DevSecOps-Box, so lets start the activity.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/dvpa webapp

Lets cd into the application so we can scan the app.

cd webapp

We are now in the webapp directory.

Let’s move to the next step.
Installing Semgrep
Semgrep is a fast, open-source, static analysis tool that excels at expressing code standards — without complicated queries — and surfacing bugs early in the development flow. Precise rules look like the code you’re searching; no more traversing abstract syntax trees or wrestling with regexes.

You can find more details about the project at https://github.com/returntocorp/semgrep

Let’s install the Semgrep tool on the system.

pip3 install semgrep

Collecting semgrep
  Downloading semgrep-0.41.1-cp36.cp37.cp38.py36.py37.py38-none-any.whl (52.4 MB)
     |████████████████████████████████| 52.4 MB 22.4 MB/s 
Requirement already satisfied: setuptools in /usr/local/lib/python3.6/dist-packages (from semgrep) (51.1.2)
Requirement already satisfied: packaging>=20.4 in /usr/local/lib/python3.6/dist-packages (from semgrep) (20.8)
Requirement already satisfied: colorama>=0.4.3 in /usr/local/lib/python3.6/dist-packages (from semgrep) (0.4.4)
Collecting junit-xml==1.9
  Downloading junit_xml-1.9-py2.py3-none-any.whl (7.1 kB)
Requirement already satisfied: six in /usr/local/lib/python3.6/dist-packages (from junit-xml==1.9->semgrep) (1.15.0)
Collecting ruamel.yaml==0.16.10
  Downloading ruamel.yaml-0.16.10-py2.py3-none-any.whl (111 kB)
     |████████████████████████████████| 111 kB 68.6 MB/s 
Requirement already satisfied: ruamel.yaml.clib>=0.1.2 in /usr/local/lib/python3.6/dist-packages (from ruamel.yaml==0.16.10->semgrep) (0.2.2)
Collecting attrs>=19.3.0
  Downloading attrs-20.3.0-py2.py3-none-any.whl (49 kB)
     |████████████████████████████████| 49 kB 16.8 MB/s 
Collecting jsonschema~=3.2.0
  Downloading jsonschema-3.2.0-py2.py3-none-any.whl (56 kB)
     |████████████████████████████████| 56 kB 10.5 MB/s 
Requirement already satisfied: importlib-metadata in /usr/local/lib/python3.6/dist-packages (from jsonschema~=3.2.0->semgrep) (3.4.0)
Requirement already satisfied: pyparsing>=2.0.2 in /usr/local/lib/python3.6/dist-packages (from packaging>=20.4->semgrep) (2.4.7)
Collecting pyrsistent>=0.14.0
  Downloading pyrsistent-0.17.3.tar.gz (106 kB)
     |████████████████████████████████| 106 kB 88.9 MB/s 
Collecting requests>=2.22.0
  Downloading requests-2.25.1-py2.py3-none-any.whl (61 kB)
     |████████████████████████████████| 61 kB 19.7 MB/s 
Collecting certifi>=2017.4.17
  Downloading certifi-2020.12.5-py2.py3-none-any.whl (147 kB)
     |████████████████████████████████| 147 kB 77.7 MB/s 
Collecting chardet<5,>=3.0.2
  Downloading chardet-4.0.0-py2.py3-none-any.whl (178 kB)
     |████████████████████████████████| 178 kB 91.5 MB/s 
Collecting idna<3,>=2.5
  Downloading idna-2.10-py2.py3-none-any.whl (58 kB)
     |████████████████████████████████| 58 kB 19.0 MB/s 
Collecting tqdm>=4.46.1
  Downloading tqdm-4.58.0-py2.py3-none-any.whl (73 kB)
     |████████████████████████████████| 73 kB 6.3 MB/s 
Collecting urllib3<1.27,>=1.21.1
  Downloading urllib3-1.26.3-py2.py3-none-any.whl (137 kB)
     |████████████████████████████████| 137 kB 93.6 MB/s 
Requirement already satisfied: typing-extensions>=3.6.4 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.7.4.3)
Requirement already satisfied: zipp>=0.5 in /usr/local/lib/python3.6/dist-packages (from importlib-metadata->jsonschema~=3.2.0->semgrep) (3.4.0)
Building wheels for collected packages: pyrsistent
  Building wheel for pyrsistent (setup.py) ... done
  Created wheel for pyrsistent: filename=pyrsistent-0.17.3-cp36-cp36m-linux_x86_64.whl size=55876 sha256=ed03cf66d9f739ce29846be29bcb1e0dfb457c5150509abf27323da125440abd
  Stored in directory: /root/.cache/pip/wheels/34/13/19/294da8e11bce7e563afee51251b9fa878185e14f4b5caf00cb
Successfully built pyrsistent
Installing collected packages: urllib3, pyrsistent, idna, chardet, certifi, attrs, tqdm, ruamel.yaml, requests, junit-xml, jsonschema, semgrep
  Attempting uninstall: ruamel.yaml
    Found existing installation: ruamel.yaml 0.16.12
    Uninstalling ruamel.yaml-0.16.12:
      Successfully uninstalled ruamel.yaml-0.16.12
Successfully installed attrs-20.3.0 certifi-2020.12.5 chardet-4.0.0 idna-2.10 jsonschema-3.2.0 junit-xml-1.9 pyrsistent-0.17.3 requests-2.25.1 ruamel.yaml-0.16.10 semgrep-0.41.1 tqdm-4.58.0 urllib3-1.26.3
We have successfully installed semgrep tool. Let’s explore the functionality it provides us.

semgrep --help

usage: semgrep [-h] [-g | -f CONFIG | -e PATTERN] [-l LANG] [--validate] [--strict] [--exclude EXCLUDE] [--include INCLUDE] [--no-git-ignore] [--skip-unknown-extensions] [--dangerously-allow-arbitrary-code-execution-from-rules]
               [-j JOBS] [--timeout TIMEOUT] [--max-memory MAX_MEMORY] [--timeout-threshold TIMEOUT_THRESHOLD] [-q] [--no-rewrite-rule-ids] [-o OUTPUT] [--json] [--debugging-json] [--sarif] [--test] [--test-ignore-todo] [--dump-ast]
               [--error] [-a] [--dryrun] [--disable-nosem] [-v] [--version] [--force-color] [--disable-version-check]
               [target [target ...]]

semgrep CLI. For more information about semgrep, go to https://semgrep.dev/

positional arguments:
  target                Search these files or directories. Defaults to entire current working directory. Implied argument if piping to semgrep.

optional arguments:
  -h, --help            show this help message and exit
  --exclude EXCLUDE     Skip any file or directory that matches this pattern; --exclude='*.py' will ignore the following: foo.py, src/foo.py, foo.py/bar.sh. --exclude='tests' will ignore tests/foo.py as well as a/b/tests/c/foo.py. Can
                        add multiple times. Overrides includes.
  --include INCLUDE     Scan only files or directories that match this pattern; --include='*.jsx' will scan the following: foo.jsx, src/foo.jsx, foo.jsx/bar.sh. --include='src' will scan src/foo.py as well as a/b/src/c/foo.py. Can add
                        multiple times.
  --no-git-ignore       Scan all files even those ignored by a projects gitignore(s)
  --skip-unknown-extensions
                        Scan only known file extensions, even if unrecognized ones are explicitly targeted.
  --test-ignore-todo    Ignore rules marked as '#todoruleid:' in test files.
  --version             Show the version and exit.
  --force-color         Always include ANSI color in the output, even if not writing to a TTY
  --disable-version-check
                        Disable checking for latest version.
config:
  -g, --generate-config
                        Generate starter configuration file, .semgrep.yml
  -f CONFIG, --config CONFIG
                        YAML configuration file, directory of YAML files
                        ending in .yml|.yaml, URL of a configuration file, or
                        semgrep registry entry name. See
                        https://semgrep.dev/docs/writing-rules/rule-syntax for
                        information on configuration file format.
  -e PATTERN, --pattern PATTERN
                        Code search pattern. See
                        https://semgrep.dev/docs/writing-rules/pattern-syntax
                        for information on pattern features.
  -l LANG, --lang LANG  Parse pattern and all files in specified language.
                        Must be used with -e/--pattern.
  --validate            Validate configuration file(s). No search is
                        performed.
  --strict              Only invoke semgrep if configuration files(s) are
                        valid.
  --dangerously-allow-arbitrary-code-execution-from-rules
                        WARNING: allow rules to run arbitrary code. ONLY
                        ENABLE IF YOU TRUST THE SOURCE OF ALL RULES IN YOUR
                        CONFIGURATION.
  -j JOBS, --jobs JOBS  Number of subprocesses to use to run checks in
                        parallel. Defaults to the number of CPUs on the
                        system.
  --timeout TIMEOUT     Maximum time to spend running a rule on a single file
                        in seconds. If set to 0 will not have time limit.
                        Defaults to 30 s.
  --max-memory MAX_MEMORY
                        Maximum memory to use running a rule on a single file
                        in MB. If set to 0 will not have memory limit.
                        Defaults to 0.
  --timeout-threshold TIMEOUT_THRESHOLD
                        Maximum number of rules that can timeout on a file
                        before the file is skipped. If set to 0 will not have
                        limit. Defaults to 0.
  --severity SEVERITY   Report findings only from rules matching the supplied
                        severity level. By default all applicable rules are
                        run.Can add multiple times. Each should be one of
                        INFO, WARNING, or ERROR.

output:
  -q, --quiet           Do not print any logging messages to stderr. Finding
                        output will still be sent to stdout. Exit code
                        provides success status.
  --no-rewrite-rule-ids
                        Do not rewrite rule ids when they appear in nested
                        sub-directories (by default, rule 'foo' in
                        test/rules.yaml will be renamed 'test.foo').
  -o OUTPUT, --output OUTPUT
                        Save search results to a file or post to URL. Default
                        is to print to stdout.
  --json                Output results in JSON format.
  --save-test-output-tar
                        Store json output as a tarball that will be uploaded
                        as a Github artifact.
  --debugging-json      Output JSON with extra debugging information
                        (experimental).
  --junit-xml           Output results in JUnit XML format.
  --sarif               Output results in SARIF format.
  --test                Run test suite.
  --dump-ast            Show AST of the input file or passed expression and
                        then exit (can use --json).
  --error               Exit 1 if there are findings. Useful for CI and
                        scripts.
  -a, --autofix         Apply the autofix patches. WARNING: data loss can
                        occur with this flag. Make sure your files are stored
                        in a version control system.
  --dryrun              Do autofixes, but don't write them to a file. This
                        will print the changes to the console. This lets you
                        see the changes before you commit to them. Only works
                        with the --autofix flag. Otherwise does nothing.
  --disable-nosem       Disable the effect of 'nosem'. This will report
                        findings on lines containing a 'nosem' comment at the
                        end.
  --max-lines-per-finding MAX_LINES_PER_FINDING
                        Maximum number of lines of code that will be shown for
                        each match before trimming (set to 0 for unlimited).
  --debug               Set the logging level to DEBUG

logging:
  -v, --verbose         Show more details about what rules are running, which
                        files failed to parse, etc.
Let’s move to the next step.

 
 Terminal


Run the scanner
Before we run semgrep, we need to create the rules file, semgrep_rules.yaml. These rules will help us find vulnerabilities in the code. You can use any text editor to create this file with the following content.

rules:
- id: avoid-pyyaml-load
  metadata:
    owasp: 'A8: Insecure Deserialization'
    cwe: 'CWE-502: Deserialization of Untrusted Data'
    references:
    - https://github.com/yaml/pyyaml/wiki/PyYAML-yaml.load(input)-Deprecation
    - https://nvd.nist.gov/vuln/detail/CVE-2017-18342
  languages:
  - python
  message: |
    Avoid using `load()`. `PyYAML.load` can create arbitrary Python
    objects. A malicious actor could exploit this to run arbitrary
    code. Use `safe_load()` instead.
  fix: yaml.safe_load($FOO)
  severity: ERROR
  patterns:
  - pattern-inside: |
      import yaml
      ...
      yaml.load($FOO)
  - pattern: yaml.load($FOO)

- id: possible-sqli
  metadata:
    owasp: 'A1: Injection'
    references:
    - https://owasp.org/www-community/attacks/SQL_Injection
  languages:
  - python
  message: |
    Possible SQL Injection
  severity: WARNING
  patterns:
  - pattern: $X.execute(...)
  - pattern-not: $X.execute(..., [...])
  - pattern-not: $X.execute("...")
In the above file, we have created rules to find Insecure Deserialization and SQL Injection attacks. Let’s run the Semgrep tool against the current working directory.

semgrep -f semgrep_rules.yaml .

running 2 rules...
100%|████████████████████████████████████████████████████████████████████████████████████████████████████████████|2/2
flaskblog/auth.py
severity:warning rule:possible-sqli: Possible SQL Injection

14:    cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")

flaskblog/dashboard/post.py
severity:error rule:avoid-pyyaml-load: Avoid using `load()`. `PyYAML.load` can create arbitrary Python
objects. A malicious actor could exploit this to run arbitrary
code. Use `safe_load()` instead.

204:                import_post_data = yaml.load(import_data)
--------------------------------------------------------------------------------
autofix: yaml.safe_load(import_data)
severity:warning rule:possible-sqli: Possible SQL Injection

60:            cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")

flaskblog/user.py
severity:warning rule:possible-sqli: Possible SQL Injection

57:        cur.execute(
58:            f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")

flaskblog/views.py
severity:warning rule:possible-sqli: Possible SQL Injection

37:        cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
--------------------------------------------------------------------------------
68:        cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
ran 2 rules on 15 files: 6 findings
We got 6 findings, one insecure deserialization, and five SQL Injections. We must fix all of these issues to keep this app secure.

Let’s move to the next step.

 
 Terminal
Fixing Insecure Deserialization
Python yaml library has a known vulnerability around YAML deserialization. We can search for this known security issue on the CVE website.

For more details, please visit CVE-2020-1747. As mentioned before, our code is vulnerable to Deserialization Attacks.

If you recall, one of the findings in the previous step was unsafe YAML load, and the following was the code snippet for the same.

flaskblog/dashboard/post.py
severity:error rule:avoid-pyyaml-load: Avoid using `load()`. `PyYAML.load` can create arbitrary Python
objects. A malicious actor could exploit this to run arbitrary
code. Use `safe_load()` instead.

204:                import_post_data = yaml.load(import_data)
--------------------------------------------------------------------------------
autofix: yaml.safe_load(import_data)
Lets verify this issue exists by opening up the flaskblog/dashboard/post.py file using any text editor like vim or nano. Ensure the security issue exists at line 204 and the program uses the insecure yaml.load function. To fix this issue, we need to replace yaml.load with a safe alternative yaml.safe_load.

Let’s run the semgrep scanner once again

semgrep -f semgrep_rules.yaml .

running 2 rules...
100%|████████████████████████████████████████████████████████████████████████████████████████████████████████████|2/2
flaskblog/auth.py
severity:warning rule:possible-sqli: Possible SQL Injection

14:    cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")

flaskblog/dashboard/post.py
severity:warning rule:possible-sqli: Possible SQL Injection

60:            cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")

flaskblog/user.py
severity:warning rule:possible-sqli: Possible SQL Injection

57:        cur.execute(
58:            f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")

flaskblog/views.py
severity:warning rule:possible-sqli: Possible SQL Injection

37:        cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
--------------------------------------------------------------------------------
68:        cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
ran 2 rules on 15 files: 5 findings
As you can see, there is no yaml.load issue in the output. The total issue count has decreased from 6 to 5.

Let’s move to the next step.

 
 Terminal

Fixing SQL Injection Issue
Apart from Insecure Deserializatio issue, there were five possible SQL Injection issues as well. We can fix these issues in various ways, but the best way to fix SQL Injection issues is Parameterized queries, also known as Parameter Binding.

running 2 rules...
100%|████████████████████████████████████████████████████████████████████████████████████████████████████████████|2/2
flaskblog/auth.py
severity:warning rule:possible-sqli: Possible SQL Injection

14:    cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
severity:warning rule:possible-sqli: Possible SQL Injection

60:            cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")

flaskblog/user.py
severity:warning rule:possible-sqli: Possible SQL Injection

57:        cur.execute(
58:            f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")

flaskblog/views.py
severity:warning rule:possible-sqli: Possible SQL Injection

37:        cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
--------------------------------------------------------------------------------
68:        cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
ran 2 rules on 15 files: 6 findings
First, we will try to fix the SQL Injection issue present in the flaskblog/auth.py file at line number 14. As you can see in the following code snippet, there is a SQL injection issue in the check_auth function.

def check_auth(username, password):
   """ This function is called to check if a username / password
       combination is valid.
   """
   cur = db.connection.cursor()
   hashsed_password = hashlib.md5(password.encode()).hexdigest()
   cur.execute(f"SELECT * FROM users WHERE email='{username}' AND password='{hashsed_password}'")
   user = cur.fetchone()

   if user is None:
      return False

   session["is_logged_in"] = True
   session["id"] = user.get("id")
   session["email"] = user.get("email")
   session["full_name"] = user.get("full_name")

   return user
The cur.execute function call will execute the SQL query on the database. It takes the username and password as possible inputs to the SQL query.

You can also verify this behavior dynamically by reproducing SQL query errors.

Learn more about SQL Injection here.

You can replace line number 14 with the following code. This code will fix the SQL Injection issue.

   cur.execute(f"SELECT * FROM users WHERE email=%s AND password=%s", [username, hashsed_password ])
Then, rerun the scanner

semgrep -f semgrep_rules.yaml .

running 2 rules...
100%|████████████████████████████████████████████████████████████████████████████████████████████████████████████|2/2
flaskblog/dashboard/post.py
severity:warning rule:possible-sqli: Possible SQL Injection

60:            cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")

flaskblog/user.py
severity:warning rule:possible-sqli: Possible SQL Injection

57:        cur.execute(
58:            f"INSERT INTO users (`email`, `full_name`, `password`) VALUES ('{email}', '{full_name}', '{hashed_password}')")

flaskblog/views.py
severity:warning rule:possible-sqli: Possible SQL Injection

37:        cur.execute(f"SELECT * FROM posts WHERE slug='{slug}'")
--------------------------------------------------------------------------------
68:        cur.execute(f"SELECT * FROM posts WHERE title LIKE '%{query}%'")
ran 2 rules on 15 files: 4 findings
As you can see(the last line), semgrep found only 4 issues. Great!

Challenge
Please fix all SQL Injection issues, and use the semgrep tool to cross-check vulnerability fix
Please do not forget to share the answer with our staff via Slack Direct Message (DM).

 
 Terminal

Static Analysis using Hadolint Optional
Static Analysis using FindSecBugs Optional
Static Analysis using njsscan Optional
Code Quality Analysis with pylint Optional
Code Quality Analysis with SonarQube Optional
How to Embed SonarQube Scan into GitHub Actions Optional