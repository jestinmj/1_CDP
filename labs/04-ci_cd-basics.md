# CI/CD Basics

## Indice

- [GitLab CI/CD Basics (Mandatory)](#gitlab-cicd-basics)
- [GitLab CI/CD Advanced (Mandatory)](#gitlab-cicd-advanced)
- [GitLab rules for Conditional Pipelines (Mandatory)](#gitlab-rules-for-conditional-pipelines)
- [Continuous Deployment with GitLab (Mandatory)](#continuous-deployment-with-gitlab)
- [Pipeline as Code (PaC) with Jenkins (Optional)](#pipeline-as-code-pac-with-jenkins)
- [Continuous Integration with Jenkins (Optional)](#continuous-integration-with-jenkins)
- Continuous Deployment with Jenkins (Optional)
- Continuous Integration with GitHub Actions (Optional)
- Continuous Integration with CircleCI (Optional)
- Build a Docker Image in CircleCI (Optional)

## GitLab CI/CD Basics

https://en.wikipedia.org/wiki/YAML

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

https://gitlab-ce-xikcivak.lab.practical-devsecops.training/root/django-nv/pipelines
https://docs.gitlab.com/ee/ci/yaml/index.htm

```yml
# This is how a comment is added to a YAML file; please read them carefully.
 
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage
 
job1:
  stage: build  # this job belongs to the build stage.
  script:
    - echo "This is a build step."  # We are running an echo command, but it can be any command.
 
job2:
  stage: test
  script:
    - echo "This is a test step."
    - exit 1          # Non zero exit code, fails a job.
 
job3:          # integration stage
  stage: integration
  script:
    - echo "This is an integration step."
 
job4:
  stage: prod
  script:
    - echo "This is a deploy step."
```

### "Challenge - Create a simple CI/CD pipeline"

In this exercise, you will edit the **.gitlab-ci.yml** file to create a simple CI/CD pipeline.

1. Create five stages namely **build, test, integration, staging** and **prod** (deploy phase)

```yml
stages:
 - build
 - test
 - integration
 - staging
 - prod
```

2. Create jobs with job names as **build, test, integration, staging** and **prod** (You can make use of simple echo commands under the script tag) under the respective stages(build under build stage, test under test stage)

```yml
stages:
  - build
  - test
  - integration
  - staging
  - prod
 
build:
  stage: build
  script:
    - echo "Build under build stage."
 
test:
  stage: test
  script:
    - echo "Test under test stage."

integration:
  stage: integration
  script:
    - echo "Integrate under integration stage."

staging:
  stage: staging
  script:
    - echo "Staging under staging stage."

prod:
  stage: prod
  script:
    - echo "Production under prod stage."
```

3. If you are not comfortable with the syntax, explore the GitLab CI syntax at https://docs.gitlab.com/ee/ci/yaml/README.html#stages

Done

## GitLab CI/CD Advanced

### This is how a comment is added to a YAML file; please read them carefully.

```yaml
stages:   # Dictionary
 - build   # this is build stage
 - test    # this is test stage
 - integration # this is an integration stage
 - prod       # this is prod/production stage

build:       # this is job named build, it can be anything, job1, job2, etc.,
  stage: build    # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - echo "This is a build step."  # We are running an echo command, but it can be any command.

test:
  stage: test
  script:
    - echo "This is a test step."
    - exit 1         # Non zero exit code, fails a job.

integration:        # integration job under stage integration.
  stage: integration
  script:
    - echo "This is an integration step."

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
```

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

```yml
stages:   # Dictionary
 - build   # this is build stage
 - test    # this is test stage
 - integration # this is an integration stage
 - prod       # this is prod/production stage

job1:       # this is job named build, it can be anything, job1, job2, etc.,
  stage: build    # this job belongs to the build stage. Here both job name and stage name is the same i.e., build
  script:
    - echo "This is a build step"  # We are running an echo command, but it can be any command.

job2:
  stage: test
  script:
    - echo "This is a test step"
    # ************ Non zero exit code, fails a job. ************ #
    - exit 1

job3:        # integration job under stage integration.
  stage: integration
  script:
    - echo "This is an integration step."

job4:
  stage: prod
  script:
    - echo "This is a deploy step."
```

```yml
stages:   # Dictionary
 - build   # this is build stage
 - test    # this is test stage
 - integration # this is an integration stage
 - prod       # this is prod/production stage

job1:       # this is job named build, it can be anything, job1, job2, etc.,
  stage: build    # this job belongs to the build stage. Here both job name and stage name is the same i.e., build
  script:
    - echo "This is a build step"  # We are running an echo command, but it can be any command.

job2:
  stage: test
  script:
    - echo "This is a test step."
    - exit 1         # Non zero exit code, fails a job.
  allow_failure: true   #<--- allow the build to fail, but don't mark it as such


job3:        # integration job under stage integration.
  stage: integration
  script:
    - echo "This is an integration step"

job4:
  stage: prod
  script:
    - echo "This is a deploy step."
```

### Challenge: Fail a job and allow it to fail

In this exercise, you will edit the .gitlab-ci.yml file to use advanced CI/CD options.

1. Create four stages, namely build, test, integration, and deploy

```yml
stages:
  - build
  - test
  - integration
  - deploy
```

2. Create a file using the echo "this is an output" > output.txt command in the integration stage and upload it using the artifacts tag

```yml
stages:
  - build
  - test
  - integration
  - deploy

build:
  stage: build
  script:
    - echo "this is an output"

test:
  stage: test
  script:
    - echo "this is an output"

integration:
  stage: integration
  script:
    - echo "this is an output" > output.txt
  artifacts:
    paths: [output.txt]
    when: always

deploy:
  stage: deploy
  script:
    - echo "this is an output"
  when: manual
```

3. Include an artifact with when: always for every executed integration job, and trigger a failure in the integration job using exit 1

```yml
stages:
  - build
  - test
  - integration
  - deploy

build:
  stage: build
  script:
    - echo "this is an output"

test:
  stage: test
  script:
    - echo "this is an output"

integration:
  stage: integration
  script:
    - echo "this is an output" > output.txt
    - exit 1
  artifacts:
    paths: [output.txt]
    when: always

deploy:
  stage: deploy
  script:
    - echo "this is an output"
```

4. Allow the integration job to fail yet move on to the next stage

```yml
stages:
  - build
  - test
  - integration
  - deploy

build:
  stage: build
  script:
    - echo "this is an output"

test:
  stage: test
  script:
    - echo "this is an output"

integration:
  stage: integration
  script:
    - echo "this is an output" > output.txt
    - exit 1
  artifacts:
    paths: [output.txt]
    when: always
  allow_failure: true

deploy:
  stage: deploy
  script:
    - echo "this is an output"
```

5. Create a job requiring a person’s approval (a button, play button) before running this job

```yml
stages:
  - build
  - test
  - integration
  - deploy

build:
  stage: build
  script:
    - echo "this is an output"

test:
  stage: test
  script:
    - echo "this is an output"

integration:
  stage: integration
  script:
    - echo "this is an output" > output.txt
    - exit 1
  artifacts:
    paths: [output.txt]
  allow_failure: true

deploy:
  stage: deploy
  script:
    - echo "this is an output"
```

## GitLab rules for Conditional Pipelines

### This is how a comment is added to a YAML file; please read them carefully.


https://about.gitlab.com/blog/2020/05/06/gitlab-com-13-0-breaking-changes/

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage

build:            # this is job named build, it can be anything, job1, job2, etc.,
  stage: build    # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - echo "This is a build step."  # We are running an echo command, but it can be any command.

test:
  stage: test
  script:
    - echo "This is a test step."
    - exit 1     # Non zero exit code, fails a job.

integration:            # integration job under stage integration.
  stage: integration
  script:
    - echo "This is an integration step."

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
```

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage

build:              # this is job named build, it can be anything, job1, job2, etc.,
  stage: build      # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - echo "This is a build step."          # We are running an echo command, but it can be any command.
  rules:
    - if: '$CI_COMMIT_BRANCH == "master"'   # this job will get triggered when the branch name is __master__
```

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - staging      # this is staging stage
 - prod         # this is prod/production stage

job1:               # this is job named build, it can be anything, job1, job2, etc.,
  stage: build      # this job belongs to the build stage. Here both job name and stage name is the same i.e., build
  script:
    - echo "This is a build step"          # We are running an echo command, but it can be any command.
```

rules:if

```yml
job4:
  stage: test
  script:
    - echo "This is a test step."
  rules:
    - if: '$CI_PIPELINE_SOURCE == "merge_request_event"'
```

rules:changes

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage

build:              # this is job named build, it can be anything, job1, job2, etc.,
  stage: build      # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - echo "This is a build step."          # We are running an echo command, but it can be any command.
  rules:
    - changes:
      - Dockerfile
```

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage

build:              # this is job named build, it can be anything, job1, job2, etc.,
  stage: build      # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - echo "This is a build step."          # We are running an echo command, but it can be any command.
  rules:
    - if: '$CI_PIPELINE_SOURCE == "merge_request_event"'
      changes:
        - Dockerfile
```

rules:exists

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - prod         # this is prod/production stage

build:              # this is job named build, it can be anything, job1, job2, etc.,
  stage: build      # this job belongs to the build stage. Here both job name and stage name is the same, i.e., build
  script:
    - docker build -t $CI_REGISTRY/root/django-nv .
  rules:
    - exists:
      - Dockerfile
```

rules:allow_failure

```yml
stages:         # Dictionary
 - build        # this is build stage
 - test         # this is test stage
 - integration  # this is an integration stage
 - staging      # this is staging stage
 - prod         # this is prod/production stage

job4:
  stage: staging
  script:
    - echo "This is a deploy step to staging environment."
    - exit 1    # Non zero exit code, fails a job.
  rules:
    - if: '$CI_COMMIT_BRANCH == "master"'   # this job will get triggered when the branch name is __master__
      allow_failure: true

job5:
  stage: prod
  script:
    - echo "This is a deploy step to production environment."
  rules:
    - if: '$CI_COMMIT_TAG !~ "/^$/"'   # this job will trigger when you create a new tag
      when: manual
```

## Continuous Deployment with GitLab

https://docs.gitlab.com/ee/ci/variables/predefined_variables.html

```yml
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
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py check

test:
  stage: test
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py test taskManager

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
```

Name	Value
Gitlab URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

```yml
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
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py check

test:
  stage: test
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py test taskManager

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
  when: manual  # <-- this job will not be executed by GitLab automatically
```

```yml
release:
  stage: release
  script:
   - docker build -t $CI_REGISTRY/root/django-nv .   # Build the application into Docker image
   - docker push $CI_REGISTRY/root/django-nv         # Push the image into registry
```

Name	Value
Key	CI_REGISTRY_USER
Value	root 
Name	Value
Key	CI_REGISTRY_PASS
Value	pdso-training

```yml
release:
  stage: release
  before_script:
   - echo $CI_REGISTRY_PASS | docker login -u $CI_REGISTRY_USER --password-stdin $CI_REGISTRY
  script:
   - docker build -t $CI_REGISTRY/root/django-nv .   # Build the application into Docker image
   - docker push $CI_REGISTRY/root/django-nv         # Push the image into registry
```

We need to add the following variables (Go to → Project (django.nv) → settings → CI/CD → Variables → Expand), as we did before for the CI_REGISTRY_USER variable.



Name	Value
Key	PROD_USERNAME
Value	root 
Name	Value
Key	PROD_HOST
Value	prod-xIKciVAk
Name	Value
Key	PROD_SSH_PRIVKEY
Value	Copy the private key from the production machine using SSH

```yml
prod:
  stage: prod
  image: kroniak/ssh-client:3.6
  environment: production
  only:
      - master
  before_script:
   - mkdir -p ~/.ssh
   - echo "$PROD_SSH_PRIVKEY" > ~/.ssh/id_rsa
   - chmod 600 ~/.ssh/id_rsa
   - eval "$(ssh-agent -s)"
   - ssh-add ~/.ssh/id_rsa
   - ssh-keyscan -t rsa $PROD_HOST >> ~/.ssh/known_hosts
  script:
   - echo
   - |
      ssh root@$PROD_HOST << EOF
        docker login -u ${CI_REGISTRY_USER} -p ${CI_REGISTRY_PASS} ${CI_REGISTRY}
        docker rm -f django.nv
        docker pull ${CI_REGISTRY}/root/django-nv
        docker run -d --name django.nv -p 8000:8000 ${CI_REGISTRY}/root/django-nv
      EOF
```

```yml
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
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py check

test:
  stage: test
  image: python:3.6
  before_script:
   - pip3 install --upgrade virtualenv
  script:
   - virtualenv env
   - source env/bin/activate
   - pip install -r requirements.txt
   - python manage.py test taskManager

release:
  stage: release
  before_script:
   - echo $CI_REGISTRY_PASS | docker login -u $CI_REGISTRY_USER --password-stdin $CI_REGISTRY
  script:
   - docker build -t $CI_REGISTRY/root/django-nv .   # Build the application into Docker image
   - docker push $CI_REGISTRY/root/django-nv         # Push the image into registry

integration:
  stage: integration
  script:
    - echo "This is an integration step"
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  image: kroniak/ssh-client:3.6
  environment: production
  only:
      - master
  before_script:
   - mkdir -p ~/.ssh
   - echo "$PROD_SSH_PRIVKEY" > ~/.ssh/id_rsa
   - chmod 600 ~/.ssh/id_rsa
   - eval "$(ssh-agent -s)"
   - ssh-add ~/.ssh/id_rsa
   - ssh-keyscan -t rsa $PROD_HOST >> ~/.ssh/known_hosts
  script:
   - echo
   - |
      ssh root@$PROD_HOST << EOF
        docker login -u ${CI_REGISTRY_USER} -p ${CI_REGISTRY_PASS} ${CI_REGISTRY}
        docker rm -f django.nv
        docker pull ${CI_REGISTRY}/root/django-nv
        docker run -d --name django.nv -p 8000:8000 ${CI_REGISTRY}/root/django-nv
      EOF
```

## Pipeline as Code (PaC) with Jenkins

https://gitlab.practical-devsecops.training/pdso/jenkins/-/blob/master/tutorials/configure-jenkins-with-gitlab.md
https://www.jenkins.io/doc/book/pipeline-as-code/
https://www.jenkins.io/doc/book/pipeline/#declarative-versus-scripted-pipeline-syntax



https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.
Name	Value
Username	root
Password	pdso-training

```groovy
pipeline {
    agent any
    stages {
        stage('build'){         // similar to __stage__ in .gitlab-ci.yml
            steps{              // similar to __script__ in .gitlab-ci.yml
                echo "hello"
            }
        }
    }
}
```

```yml
pipeline {
    agent any

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
}

```

### Challenge

In this exercise, you will edit the Jenkinsfile file to create a simple CI/CD pipeline.

1. Create four stages, namely build, test, integration, prod and each of these stages doing a simple echo command with their stage names
2. In the integration stage, create a file named output.txt and save it as an artifact
3. If you are not comfortable with the syntax, explore the Jenkins Pipeline syntax at https://www.jenkins.io/doc/pipeline/tour/getting-started

## Continuous Integration with Jenkins

https://gitlab.practical-devsecops.training/pdso/jenkins/-/blob/master/tutorials/configure-jenkins-with-gitlab.md

```bash
git config --global user.email "student@pdevsecops.com"
git config --global user.name "student"
git clone http://root:pdso-training@gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git
cd django-nv
git mv .gitlab-ci.yml .gitlab-ci.yml.backup
cat > Jenkinsfile <<EOL
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
                echo "This is a deploy step"
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
EOL

git add Jenkinsfile
git commit -m "Create the .gitlab-ci.yml backup and add Jenkinsfile"
git push origin master
```

### Challenge

1. Understand the Jenkinsfile provided in the previous step and figure out why we have used agent in build, and test stages
2. Ensure build and test stages run sequentially
3. Add another stage called artifact and create a file using a simple echo command, then save this file as an artifact
