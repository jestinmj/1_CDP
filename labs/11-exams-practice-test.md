# Exams Practice Test

## Indice

- [CDP Exam Practice (Optional)](#cdp-exam-practice)
- [CDP Exam Practice Test Solutions (Optional)](#cdp-exam-practice-test-solutions)

## CDP Exam Practice

### A Practice exam

Thank you for taking the DevSecOps Professional **Practice Exam**.

Please read and understand the information presented here carefully as it contains important information to attempt and pass the mock exam.

### No Guarantee

Passing this practice exam doesn’t guarantee or suggest that you are ready for the certification exam. This is added purely for testing your confidence before taking the exam.

### Best way to approach this exam

Here are a few pointers to keep in mind while taking this practice test

1. Do not refer your notes or videos while taking this practice exam
2. You can use google or the security tool’s documentation during the practice exam
3. Document everything as if its a real exam

### All rights reserved to Hysn Technologies Inc and its affiliates.

No part of this publication/exam may be reproduced, copied, transmitted in any form or by any means, electronic, mechanical, photocopying, recording or otherwise, without the prior written permission of Hysn Technologies Inc.

In short, you are not allowed to discuss and/or share the examination questions/answers with anyone without our permission.

### Exam Details

You have **2 hours to solve 2 challenges** in order to achieve 40 points to pass, out of a total score of 50. You need to satisfy all the requirements mentioned in a challenge to gain complete points.

Please take notes of enough details so anyone can reproduce the steps to solve these exam challenges.

Each challenge solution should at least include:

- Step by step instructions
- Files used (like **.gitlab-ci.yml**, roles, playbook.yml, etc.)
- Screenshots
- Output/Results (in a machine-readable format like **JSON**, **XML**).

> You do not need to share the exam solutions with our staff as you can self evaluate these in the next exercise.

### Lab architecture and Tools

The practice exam labs are similar to the course labs but we do introduce differences in the exam so please pay attention to the commands and the environment.

### Various machines in the lab

We have numerous machines in the practice exam lab. Each of these machines serve a purpose.

System Name	Description
**DevSecOps Box**	This machine acts as a developer/security engineer machine and contains many DevSecOps tools in it
**Gitlab**	This machine contains a source code management system (SCM), CI/CD system and many other tools
**Gitlab runner**	This machine acts as a slave machine in master/slave systems architecture and executes commands given by Gitlab master
**Production**	This machine acts as the production machine
**Vulnerability Management**	This machine hosts vulnerability management software to manage the vulnerabilities found as part of the day to day security scans

### Machines in the lab and their domain names

System Name	Domain names
**DevSecOps-Box**	devsecops-box-xIKciVAk
**Git server**	gitlab-ce-xIKciVAk
**CI/CD**	gitlab-ce-xIKciVAk
**Prod**	prod-xIKciVAk
**Vulnerability Management**	dojo-xIKciVAk

Click the **Next** button to move to the next step.

### Access Deployed Web Services and ports

You can access various machines web services and machines during the exam using the following details.

### GitLab CI/CD Machine

Name	Value
**Link**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
**Username**	root
**Password**	pdso-training

### Production Machine

Name	Value
**Link**	https://prod-xIKciVAk.lab.practical-devsecops.training
**Username**	admin
**Password**	admin

### Vulnerability Management Machine

Name	Value
**Link**	https://dojo-xIKciVAk.lab.practical-devsecops.training/
**Username**	root
**Password**	pdso-training

### Defect Dojo Upload Script

If you wish, you can download a simple Defect Dojo upload script using the following command.

```bash
curl https://gitlab.practical-devsecops.training/-/snippets/3/raw -o upload-results.py
```

### Challenges

You need to **solve two challenges in two hours**.

### Challenge 1 (25 points)

1. Implement SCA, SAST, DAST for the django.nv project
2. Please embed these tests in CI/CD pipeline
3. Ensure all the best practices covered in the course videos, labs and slack discussion are being implemented

### Challenge 2 (25 points)

1. Harden the production machine
2. Ensure it stays compliant with linux-baseline Inspec Profile
3. Embed these tests as part of CI/CD pipeline

>Good luck with the practice exam

## CDP Exam Practice Test Solutions

### The Practice exam solutions

In the practice exam, you were given two challenges worth 50 points.

### Challenge 1 (25 points)

1. Implement SCA, SAST, DAST for the django.nv project
2. Ensure all the best practices covered in the course videos, labs and slack discussion are being implemented
3. Please embed these tests in CI/CD pipeline

### Challenge 2 (25 points)

1. Harden the production machine
2. Ensure it stays compliant with linux-baseline Inspec Profile
3. Embed these tests as part of CI/CD pipeline

Let’s try to solve these challenges in the **next** page.

### Challenge 1 (25 points)

#### Task 1

> Implement SCA, SAST, DAST for the django.nv project. As per the best practices, we need to test DevSecOps tools locally before embedding them into CI/CD pipeline.

So lets go ahead and test them out.

##### **Software Component Analysis (SCA)**

For front-end:

```bash
npm install -g retire # Install retirejs npm package
```

```bash
retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
```

For backend:

```bash
docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > oast-results.json
```

##### **Static Application Security Testing (SAST)**

For secrets-scanning:

```bash
docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
```

For code analysis:

```bash
docker run --user $(id -u):$(id -g) --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json
```

##### **Dynamic Application Security Testing (DAST)**

SSL Scan

```bash
docker run --rm -v $(pwd):/tmp hysnsec/sslyze --regular prod-xIKciVAk.lab.practical-devsecops.training:443 --json_out /tmp/sslyze-output.json
```

Nmap

```bash
docker run --rm -v $(pwd):/tmp hysnsec/nmap prod-xIKciVAk -oX /tmp/nmap-output.xml
```

ZAP Baseline

```bash
docker run --user $(id -u):$(id -g) -w /zap -v $(pwd):/zap/wrk:rw --rm owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json
```

#### Task 2

> Please embed these tests in CI/CD pipeline

Considering your DevOps team created a simple CI pipeline with the following contents.

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

We will try to do these challenges step by step as mentioned in the courseware. SCA, SAST and then DAST.

##### **Software Component Analysis (SCA)**

```yml
# Software Component Analysis
sca-frontend:
  stage: build
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week

sca-backend:
  stage: build
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > oast-results.json
  artifacts:
    paths: [oast-results.json]
    when: always # What does this do?
  allow_failure: true #<--- allow the build to fail but don't mark it as such
```

##### **Static Application Security Testing (SAST)**

```yml
# Git Secrets Scanning
secrets-scanning:
  stage: build
  script:
    - apk add git
    - git checkout master
    - docker run --rm -v $(pwd):/src hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
  artifacts:
    paths: [trufflehog-output.json]
    when: always # What is this for?
    expire_in: one week
  allow_failure: true   #<--- allow the build to fail but don't mark it as such

# Static Application Security Testing
sast:
  stage: build
  script:
    - docker pull hysnsec/bandit  # Download bandit docker container
    # Run docker container, please refer docker security course, if this doesn't make sense to you.
    - docker run --user $(id -u):$(id -g) --rm -v $(pwd):/src hysnsec/bandit -r /src -f json -o /src/bandit-output.json
  artifacts:
    paths: [bandit-output.json]
    when: always
  allow_failure: true   #<--- allow the build to fail but don't mark it as such
```

##### **Dynamic Application Security Testing (DAST)**

```yml

# Dynamic Application Security Testing
nikto:
  stage: integration
  script:
    - docker pull hysnsec/nikto
    - docker run --rm -v $(pwd):/tmp hysnsec/nikto -h prod-xIKciVAk -o /tmp/nikto-output.xml
  artifacts:
    paths: [nikto-output.xml]
    when: always

sslscan:
  stage: integration
  script:
    - docker pull hysnsec/sslyze
    - docker run --rm -v $(pwd):/tmp hysnsec/sslyze --regular prod-xIKciVAk.lab.practical-devsecops.training:443 --json_out /tmp/sslyze-output.json
  artifacts:
    paths: [sslyze-output.json]
    when: always

nmap:
  stage: integration
  script:
    - docker pull hysnsec/nmap
    - docker run --rm -v $(pwd):/tmp hysnsec/nmap prod-xIKciVAk -oX /tmp/nmap-output.xml
  artifacts:
    paths: [nmap-output.xml]
    when: always

zap-baseline:
  stage: integration
  script:
    - docker pull owasp/zap2docker-stable:2.10.0
    - docker run --user $(id -u):$(id -g) -w /zap -v $(pwd):/zap/wrk:rw --rm owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json
  after_script:
    - docker rmi owasp/zap2docker-stable:2.10.0  # clean up the image to save the disk space
  artifacts:
    paths: [zap-output.json]
    when: always # What does this do?
  allow_failure: false
```

Let’s combine the SCA, SAST and DAST steps into a Gitlab CI script.

We will login into the GitLab using the following details and execute this pipeline.

Name	Value
**URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
**Username**	root
**Password**	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the below Gitlab CI script. Click on the **Edit** button to start replacing the content (use Control+A and Control+V).

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

# Software Component Analysis
sca-frontend:
  stage: build
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week

sca-backend:
  stage: build
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > oast-results.json
  artifacts:
    paths: [oast-results.json]
    when: always # What does this do?
  allow_failure: true #<--- allow the build to fail but don't mark it as such

# Git Secrets Scanning
secrets-scanning:
  stage: build
  script:
    - apk add git
    - git checkout master
    - docker run -v $(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
  artifacts:
    paths: [trufflehog-output.json]
    when: always # What is this for?
    expire_in: one week
  allow_failure: true   #<--- allow the build to fail but don't mark it as such

# Static Application Security Testing
sast:
  stage: build
  script:
    - docker pull hysnsec/bandit  # Download bandit docker container
    # Run docker container, please refer docker security course, if this doesn't make sense to you.
    - docker run --user $(id -u):$(id -g) -v $(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json
  artifacts:
    paths: [bandit-output.json]
    when: always
  allow_failure: true   #<--- allow the build to fail but don't mark it as such

# Dynamic Application Security Testing
nikto:
  stage: integration
  script:
    - docker pull hysnsec/nikto
    - docker run --rm -v $(pwd):/tmp hysnsec/nikto -h prod-xIKciVAk -o /tmp/nikto-output.xml
  artifacts:
    paths: [nikto-output.xml]
    when: always

sslscan:
  stage: integration
  script:
    - docker pull hysnsec/sslyze
    - docker run --rm -v $(pwd):/tmp hysnsec/sslyze --regular prod-xIKciVAk.lab.practical-devsecops.training:443 --json_out /tmp/sslyze-output.json
  artifacts:
    paths: [sslyze-output.json]
    when: always

nmap:
  stage: integration
  script:
    - docker pull hysnsec/nmap
    - docker run --rm -v $(pwd):/tmp hysnsec/nmap prod-xIKciVAk -oX /tmp/nmap-output.xml
  artifacts:
    paths: [nmap-output.xml]
    when: always

zap-baseline:
  stage: integration
  script:
    - docker pull owasp/zap2docker-stable:2.10.0
    - docker run --user $(id -u):$(id -g) --rm -v $(pwd):/zap/wrk:rw owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json
  after_script:
    - docker rmi owasp/zap2docker-stable:2.10.0  # clean up the image to save the disk space
  artifacts:
    paths: [zap-output.json]
    when: always # What does this do?
  allow_failure: false
```

Save changes to the file using the **Commit changes** button.

##### **Verify the pipeline run**

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

#### Task 3

> Ensure all the best practices covered in the course videos, labs and slack discussion are being implemented

We tried to implement all the best practices we have learned in the course

1. Tested the tools locally before embedding in the pipeline
2. Ensured the scans finish within 10 minutes
3. Ensured they each run in their own jobs
4. We saved the output in a file
5. We didn’t fail the builds

There’s more to the best practices, we leave it to you, to implement and suggest the remaining best practices.

Let’s move to the **next step** where we will solve the challenge 2.

### Challenge 2 (25 points)

#### Task 1

> Harden the production machine

As per the best practices, we need to test DevSecOps tools locally before embedding them into CI/CD pipeline.

So lets go ahead and test them out.

##### IaC

We need the inventory.ini first, so lets create one

```bash
cat > inventory.ini <<EOL
# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

EOL
```

Let’s download the role

```bash
ansible-galaxy install dev-sec.os-hardening
```

Next, let’s create a playbook

```bash
cat > ansible-hardening.yml <<EOL
---
- name: Playbook to harden Ubuntu OS.
  hosts: devsecops
  remote_user: root
  become: yes

  roles:
    - dev-sec.os-hardening

EOL
```

Let’s run the pipeline now.

```bash
ansible-playbook -i inventory.ini ansible-hardening.yml
```

#### Task 2

> Ensure it stays compliant with linux-baseline Inspec Profile

We can verify if the machine stays hardened using Inspec.

```bash
docker run --rm -v ~/.ssh:/root/.ssh -v $(pwd):/opt hysnsec/inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@$DEPLOYMENT_SERVER -i /root/.ssh/id_rsa --chef-license accept --reporter json:/opt/inspec-output.json
```

If there are any discrepancies in the results, we need to explore and fix them.

#### Task 3

> Embed these tests as part of CI/CD pipeline

##### **Infrastructure as Code (IaC)**

```bash
# Infrastructure as Code
# PLEASE ENSURE YOU HAVE SETUP THE ENVIRONMENT VARIABLES APPROPRIATELY
ansible-hardening:
  stage: prod
  image: willhallonline/ansible:2.9-ubuntu-18.04
  before_script:
    - mkdir -p ~/.ssh
    - echo "$DEPLOYMENT_SERVER_SSH_PRIVKEY" | tr -d '\r' > ~/.ssh/id_rsa
    - chmod 600 ~/.ssh/id_rsa
    - eval "$(ssh-agent -s)"
    - ssh-add ~/.ssh/id_rsa
    - ssh-keyscan -t rsa $DEPLOYMENT_SERVER >> ~/.ssh/known_hosts
  script:
    - echo -e "[prod]\n$DEPLOYMENT_SERVER" >> inventory.ini
    - ansible-galaxy install dev-sec.os-hardening
    - ansible-playbook -i inventory.ini ansible-hardening.yml
```

##### **Compliance as Code (CaC)**

```yml
# Compliance as Code
# PLEASE ENSURE YOU HAVE SETUP THE ENVIRONMENT VARIABLES APPROPRIATELY
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
    - docker run --rm -v ~/.ssh:/root/.ssh -v $(pwd):/share hysnsec/inspec exec https://github.com/dev-sec/linux-baseline -t ssh://root@$DEPLOYMENT_SERVER -i /root/.ssh/id_rsa --chef-license accept --reporter json:/share/inspec-output.json
  artifacts:
    paths: [inspec-output.json]
    when: always
```

We will login into the GitLab using the following details and execute this pipeline.

Name	Value
**URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
**Username**	root
**Password**	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the below Gitlab CI script. Click on the **Edit** button to start replacing the content (use Control+A and Control+V).

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

# Infrastructure as Code
# PLEASE ENSURE YOU HAVE SETUP THE ENVIRONMENT VARIABLES AND NEEDED FILES APPROPRIATELY
ansible-hardening:
  stage: prod
  image: willhallonline/ansible:2.9-ubuntu-18.04
  before_script:
    - mkdir -p ~/.ssh
    - echo "$DEPLOYMENT_SERVER_SSH_PRIVKEY" | tr -d '\r' > ~/.ssh/id_rsa
    - chmod 600 ~/.ssh/id_rsa
    - eval "$(ssh-agent -s)"
    - ssh-add ~/.ssh/id_rsa
    - ssh-keyscan -t rsa $DEPLOYMENT_SERVER >> ~/.ssh/known_hosts
  script:
    - echo -e "[prod]\n$DEPLOYMENT_SERVER" >> inventory.ini
    - ansible-galaxy install dev-sec.os-hardening
    - ansible-playbook -i inventory.ini ansible-hardening.yml

# Compliance as Code
# PLEASE ENSURE YOU HAVE SETUP THE ENVIRONMENT VARIABLES AND NEEDED FILES APPROPRIATELY
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

> Make sure you have added the necessary variables into your project (**Settings > CI/CD**) such as **$DOJO_HOST**, and **$DOJO_API_TOKEN**. Otherwise, your results are not uploaded to DefectDojo in the **sast-with-vm** job.

Save changes to the file using the **Commit changes** button.

##### Verify the pipeline run

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xikcivak.lab.practical-devsecops.training/root/django-nv/pipelines).

You can click on the appropriate job to see the results.