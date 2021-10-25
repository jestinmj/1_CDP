# Software Component Analysis (SCA)

## Indice

- [Software Component Analysis using Safety (Mandatory)](#software-component-analysis-using-safety)
- [How to Embed Safety into GitLab (Mandatory)](#how-to-embed-safety-into-gitlab)
- How to Embed Safety into Jenkins (Optional)
- How to Embed Safety into GitHub Actions (Optional)
- How to Embed Safety into CircleCI  (Optional)
- [Software Component Analysis using RetireJS (Mandatory)](#software-component-analysis-using-retirejs)
- [How to Embed RetireJS into GitLab (Mandatory)](#how-to-embed-retirejs-into-gitlab)
- How to Embed RetireJS into Jenkins (Optional)
- How to Embed RetireJS into GitHub Actions (Optional)
- How to Embed RetireJS into CircleCI (Optional)
- Software Component Analysis using Dependency-Check (Optional)
- How to Embed Dependency-Check into GitLab (Optional)
- How to Embed Dependency-Check into CircleCI (Optional)
- How to Embed Dependency-Check into GitHub Actions (Optional)
- Software Component Analysis using Snyk (Optional)
- How to Embed Snyk into GitLab (Optional)
- How to Embed Snyk into GitHub Actions (Optional)
- How to Embed Snyk into CircleCI (Optional)
- Software Component Analysis using NPM Audit (Optional)
- Software Component Analysis using AuditJS (Optional)
- Software Component Analysis using bundler-audit (Optional)
- Software Component Analysis using chelsea (Optional)

## Software Component Analysis using Safety

https://pypi.org/project/safety/
https://github.com/pyupio/safety

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
ls -l
git -c http.sslVerify=false clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
cd webapp
pip3 install safety
safety check --help
safety check -r requirements.txt --json | tee safety_output.json
```

## How to Embed Safety into GitLab

```yaml
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
```

### "Challenge: How to Embed Safety into GitLab"

Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

1. Embed OAST scanning in the test stage using the safety tool

```yml
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

safety:
  stage: test
  script:
    - pip3 install safety
    - safety check --help
    - safety check -r requirements.txt --json | tee safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true
```

2. You can make use of hysnsec/safety docker image if you wish

```yml
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

safety:
  stage: test
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true
```

3. Understand the use of Docker’s -v (volume mount) flag/option

Answer: The flag -v (volume mount) mount the host directory over the specified path after the colon characters (:). This lets at "hysnsec/safety" container check the application.

4. Ensure you follow the DevSecOps Gospel and best practices while embedding the safety tool.

```yml
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

safety:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true
```

5. Rename test job name to oast.

```yml
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

oast:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true
```

---

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

oast-frontend:
  stage: test
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week
  allow_failure: true

oast:
  stage: test
  script:
    - docker pull hysnsec/safety  # We are going to pull the hysnsec/safety image to run the safety scanner
    # third party components are stored in requirements.txt for python, so we will scan this particular file with safety.
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > oast-results.json
  artifacts:
    paths: [oast-results.json]
    when: always # What does this do?

integration:
  stage: integration
  script:
    - echo "This is an integration step."
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
```

## Software Component Analysis using RetireJS

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
cd webapp
curl -sL https://deb.nodesource.com/setup_12.x | bash -
apt install nodejs -y
npm install -g retire
retire --help
cat package.json
retire --outputformat json --outputpath retire_output.json
npm install
retire --outputformat json --outputpath retire_output.json
cat retire_output.json | jq
```

### "Challenge: Software Component Analysis using RetireJS"

In this exercise, you will explore and use the advanced features of Retire

1. Please configure the tool such that it only throws non zero exit code when high severity issues are present in the results

```yml
retire --outputformat json --outputpath retire_output.json --severity high
HIGH_SEVERITY=$(cat retire_output.json | jq -c '.data[].results[].vulnerabilities[] |  select (.severity == "high")' | jq -sc '. | length')
if (( "${HIGH_SEVERITY}" > 1 ));then exit 1; else exit 0;fi
```

2. Mark a high severity issue as False Positives

```yml
cat > retireignore <<EOL
[
    { 
		"component": "dojo",
		"identifiers" : { "CVE": "CVE-2018-15494"},
		"justification" : "False Positive"
	}
]
EOL
retire --outputformat json --outputpath retire_output.json --severity high
```

## How to Embed RetireJS into GitLab

### Use RetireJS tool to perform OAST in CI/CD pipeline

In this scenario, you will learn how to embed an SCA tool in CI/CD pipeline.

You will learn to use **Retire.js** in CI/CD pipeline and allow the job to fail even when the tool found several issues.

> Once you click the **Start the Exercise** button, you will need to wait 2 minutes for the GitLab machine to start.

> Remember!
>
> 1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
> 2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

![Next]()

### A Simple CI/CD pipeline

Considering your DevOps team created a simple CI pipeline with the following contents. Please add the Retire.js scan to this pipeline.

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
```

Let’s log into GitLab using the following details and run the above pipeline.

### GitLab CI/CD Machine

Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the above CI script. Click on the **Edit** button to replace the content (use Control+A and Control+V).

Save changes to the file using the **Commit changes** button.

### Verify the pipeline runs

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xikcivak.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

### "Challenge: How to Embed RetireJS into GitLab"

Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

1. Explore the documentation of Retire.js tool

```bash
retire --help
```

2. Embed OAST frontend scanning in the test stage using the Retire.js tool

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

retirejs:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  before_script:
    - apt-get update
    - apt install jq -y
    - curl -sL https://deb.nodesource.com/setup_12.x | bash -
    - apt install nodejs -y
    - npm install -g retire
  script:
    - npm install
    - retire --outputformat json --outputpath retirejs-report.json
  artifacts:
    paths: [retirejs-report.json]
    when: always
    expire_in: one week
  allow_failure: true
```

3. Add another job named oast-frontend under the test stage

```yaml
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

safety:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true

oast-frontend:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  before_script:
    - apt-get update
    - apt install jq -y
    - curl -sL https://deb.nodesource.com/setup_12.x | bash -
    - apt install nodejs -y
    - npm install -g retire
  script:
    - npm install
    - retire --outputformat json --outputpath retirejs-report.json --severity high
  artifacts:
    paths: [retirejs-report.json]
    when: always
    expire_in: one week
  allow_failure: true
```

4. You can make use of any container image of your choice, including the node image

```yaml
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

safety:
  stage: test
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  script:
    - docker pull hysnsec/safety
    - docker run --rm -v $(pwd):/src hysnsec/safety check -r requirements.txt --json > safety_output.json
  artifacts:
    paths: [safety_output.json]
    when: always
  allow_failure: true

oast-frontend:
  stage: test
  image: node:alpine3.10
  rules:
    - if: '$CI_COMMIT_BRANCH == "main"'
      when: always
    - when: never
  before_script:
    - npm install -g retire
  script:
    - npm install
    - retire --outputformat json --outputpath retirejs-report.json
  artifacts:
    paths: [retirejs-report.json]
    when: always
    expire_in: one week
  allow_failure: true
```

### Embed RetireJS in CI/CD pipeline

As discussed in the **SCA using the Retire.js** exercise, we can embed the RetireJS tool in our CI/CD pipeline. However, do remember you need to run the command manually before you embed OAST in the pipeline.

> Do you wonder which stage this job should go into?

Maybe you want to scan the components before performing SAST scans.

```yaml
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

oast-frontend:
  stage: test
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week

integration:
  stage: integration
  script:
    - echo "This is an integration step."
    - exit 1
  allow_failure: true # Even if the job fails, continue to the next stages

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
```

Copy the above CI script and add it to the **.gitlab.ci.yml** file on Gitlab repo at [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml).

> Do not forget to click on the **“Commit Changes”** button to save the file.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

You will notice that the oast-frontend job stores the output to a file **retirejs-report.json**. This is done to ensure we can process the results further via APIs or vulnerability management systems like **Defect Dojo**.

In the **next step**, you will learn the need to not fail the builds.

### Allow the job failure

> Remember!
> 
> 1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise.
>
> 2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working.

You do not want to fail the builds in **DevSecOps Maturity Levels 1 and 2**. If a security tool fails a build upon security findings, you would want to allow it to fail and not block the pipeline as there would be false positives in the results.

You can use the allow_failure tag to not fail the build even though the tool found security issues.

```yaml
oast-frontend:
  stage: test
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week
  allow_failure: true  #<--- allow the build to fail but don't mark it as such
```

> Notice **allow_failure: true** at the end of the YAML file.

The final pipeline would look like the following.

```yaml
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

oast-frontend:
  stage: test
  image: node:alpine3.10
  script:
    - npm install
    - npm install -g retire # Install retirejs npm package.
    - retire --outputformat json --outputpath retirejs-report.json --severity high
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week
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
```

Go ahead and add it to the **.gitlab-ci.yml** file to run the pipeline.

You will notice that the **oast-frontend** job has failed but didn’t block the other jobs from running.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.