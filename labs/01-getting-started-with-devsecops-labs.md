# Getting started with DevSecOps Lavs

## **Indice**

- [Welcome to Practical DevSecOps Labs (Mandatory)](#welcome-to-practical-devsecops-labs)
- [Understanging The Lab Setup (Mandatory)](#understanging-the-lab-setup)

## Welcome to Practical DevSecOps Labs

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
cd webapp
ls
```

Copy:

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
  script:
    - echo "This is a build step."

test:
  stage: test
  script:
    - echo "This is a test step."

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

> Username	root
> Password	pdso-training

```yaml
sast:
  stage: build
  script:
    - docker pull hysnsec/bandit  # Download bandit docker container
    - docker run --user $(id -u):$(id -g) -v $(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json
  artifacts:
    paths: [bandit-output.json]
    when: always
  allow_failure: true   #<--- allow the build to fail but don't mark it as such
```

> echo "I'm a command"


> https://github.com/PyCQA/bandit

## Understanging The Lab Setup

- System Name	Description
  - DevSecOps Box	This machine acts as a developer/security engineer machine and contains many DevSecOps tools in it
  - Gitlab	This machine contains a source code management system (SCM), CI/CD system and many other tools
  - Gitlab runner	This machine acts as a slave machine in master/slave systems architecture and executes commands given by Gitlab master
  - Production	This machine acts as the production machine
  - Vulnerability Management	This machine hosts vulnerability management software to manage the vulnerabilities found as part of the day to day security scans
  - Other machines	Other machines in the lab are introduced in the appropriate exercises like Docker Registry, SonarQube, Jenkins, etc.,

- Machines in the lab and their domain names
System Name	Domain names	Uptime of these machines
DevSecOps-Box	devsecops-box-XXXXX	Expires when Lab timer closes
Git server	gitlab-ce-XXXXX	2 hours
CI/CD	gitlab-ce-XXXXX	2 hours
Prod	prod-XXXXX	2 hours
Vulnerability Management	dojo-XXXXXX	2 hours

devsecops-box-xIKciVAk

ping -c 3 prod-xIKciVAk

ssh root@prod-xIKciVAk

exit

hostname

- Production Machine
  Name	Value
  Link	https://prod-xIKciVAk.lab.practical-devsecops.training
  Username	admin
  Password	admin

- Vulnerability Management Machine
Name	Value
Link	https://dojo-xIKciVAk.lab.practical-devsecops.training/
Username	root
Password	pdso-training