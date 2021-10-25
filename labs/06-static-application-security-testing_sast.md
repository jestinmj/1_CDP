# Static Application Security Testing (SAST)

## Indice

- [Secrets Scanning with TruffleHog (Mandatory)](#secrets-scanning-with-trufflehog)
- [How to Embed the TruffleHog into GitLab (Mandatory)](#how-to-embed-the-trufflehog-into-gitlab)
- How to Embed TruffleHog into Jenkins (Optional)
- How to Embed TruffleHog into GitHub Actions (Optional)
- How to Embed TruffleHog into CircleCI (Optional)
- Secrets Scanning with detect-secrets (Optional)
- Secrets Scanning with Talisman (Optional)
- Static Analysis using Bandit (Mandatory)
- How to Embed Bandit into GitLab (Mandatory)
- How to Embed Bandit into Jenkins (Optional)
- How to Embed Bandit into GitHub Actions (Optional)
- How to Embed Bandit into CircleCI (Optional)
- How to Fix The Issues Reported by Bandit (Optional)
- Static Analysis using Gosec  (Optional)
- How to Embed Gosec into GitLab (Optional)
- Embed Gosec into Jenkins (Optional)
- False Positive Analysis (FPA) (Mandatory) (Optional)
- Static Analysis using Semgrep (Optional)
- How to Write Custom Rule in Semgrep (Optional)
- Hunting Vulnerability with Semgrep (Optional)
- How to Fix The Issues Reported by Semgrep (Optional)
- Static Analysis using Hadolint (Optional)
- Static Analysis using FindSecBugs (Optional)
- Static Analysis using njsscan (Optional)
- Code Quality Analysis with pylint (Optional)
- Static Analysis using Brakeman (Mandatory)
- Code Quality Analysis with SonarQube (Optional)
- How to Embed SonarQube Scan into GitHub Actions (Optional)

## Secrets Scanning with TruffleHog

### Use Trufflehog to find secrets like token, ssh keys, certs, etc.

In this scenario, you will learn how to install and run **truffleHog**. Trufflehog helps you in finding secrets committed in a git repository.

To achieve the above objectives, you will need to do the following:

1. Clone/download the source code
2. Install the secret scanning tool
3. Run the secret scanner on the code

### Download the source code

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
```

Let’s cd into the application so we can scan the app.

```bash
cd webapp
```

We are now in the webapp directory.

Let’s move to the **next step**.

### Install TruffleHog

> TruflleHog is a tool that searches through git repositories for secrets, digging deep into commit history and branches. This tool is useful in finding the secrets accidentally committed to the repo.
> 
> You can find more details about the project at https://github.com/dxa4481/truffleHog.

Let’s install the TruffleHog tool on the system to scan for the secrets in our code.

```bash
pip3 install trufflehog
Collecting trufflehog
  Downloading truffleHog-2.2.1-py2.py3-none-any.whl (13 kB)
Collecting GitPython==3.0.6
  Downloading GitPython-3.0.6-py3-none-any.whl (451 kB)
     |████████████████████████████████| 451 kB 47.8 MB/s 
Collecting truffleHogRegexes==0.0.7
  Downloading truffleHogRegexes-0.0.7-py2.py3-none-any.whl (2.5 kB)
Collecting gitdb2>=2.0.0
  Downloading gitdb2-4.0.2-py3-none-any.whl (1.1 kB)
Collecting gitdb>=4.0.1
  Downloading gitdb-4.0.5-py3-none-any.whl (63 kB)
     |████████████████████████████████| 63 kB 3.8 MB/s 
Collecting smmap<4,>=3.0.1
  Downloading smmap-3.0.5-py2.py3-none-any.whl (25 kB)
Installing collected packages: smmap, gitdb, gitdb2, truffleHogRegexes, GitPython, trufflehog
Successfully installed GitPython-3.0.6 gitdb-4.0.5 gitdb2-4.0.2 smmap-3.0.5 truffleHogRegexes-0.0.7 trufflehog-2.2.1
```

Let’s explore what options Trufflehog provides us.

```bash
trufflehog --help
usage: trufflehog [-h] [--json] [--regex] [--rules RULES]
                  [--entropy DO_ENTROPY] [--since_commit SINCE_COMMIT]
                  [--max_depth MAX_DEPTH] [--branch BRANCH]
                  [-i INCLUDE_PATHS_FILE] [-x EXCLUDE_PATHS_FILE]
                  [--repo_path REPO_PATH] [--cleanup]
                  git_url

Find secrets hidden in the depths of git.

positional arguments:
  git_url               URL for secret searching

optional arguments:
  -h, --help            show this help message and exit
  --json                Output in JSON
  --regex               Enable high signal regex checks
  --rules RULES         Ignore default regexes and source from json list file
  --entropy DO_ENTROPY  Enable entropy checks
  --since_commit SINCE_COMMIT
                        Only scan from a given commit hash
  --max_depth MAX_DEPTH
                        The max commit depth to go back when searching for
                        secrets
  --branch BRANCH       Name of the branch to be scanned
  -i INCLUDE_PATHS_FILE, --include_paths INCLUDE_PATHS_FILE
                        File with regular expressions (one per line), at least
                        one of which must match a Git object path in order for
                        it to be scanned; lines starting with "#" are treated
                        as comments and are ignored. If empty or not provided
                        (default), all Git object paths are included unless
                        otherwise excluded via the --exclude_paths option.
  -x EXCLUDE_PATHS_FILE, --exclude_paths EXCLUDE_PATHS_FILE
                        File with regular expressions (one per line), none of
                        which may match a Git object path in order for it to
                        be scanned; lines starting with "#" are treated as
                        comments and are ignored. If empty or not provided
                        (default), no Git object paths are excluded unless
                        effectively excluded via the --include_paths option.
  --repo_path REPO_PATH
                        Path to the cloned repo. If provided, git_url will not
                        be used
  --cleanup             Clean up all temporary result files
```

Let’s move to the next step.

### Run the Scanner

As we have learned in the **DevSecOps Gospel**, we should save the output in the machine-readable format (**CSV, JSON, XML**) so it can be parsed by the machines easily.

We are using the **tee** command here to show the output and store it in a file simultaneously.

```bash
trufflehog --json . | tee secret.json
```

- **-json**: tells that output should be in the JSON format

```bash
.......................SNIP..........................................................
low...\"
-#     username = password = ''
-#     if request.POST:
-#         username = request.POST.get('username')
-#         password = request.POST.get('password')
-
-#         user = authenticate(username=username, password=password)
-#         if user is not None:
-#             if user.is_active:
-#                 login(request, user)
-#                 state = \"You're successfully logged in!\"
-#             else:
-#                 state = \"Your account is not active, please contact the site admin.\"
-#         else:
-#             state = \"Your username and/or password were incorrect.\"
+def login(request):
+    state = \"Please log in below...\"
+    username = password = ''
+    if request.POST:
+        username = request.POST.get('username')
+        password = request.POST.get('password')
+
+        user = authenticate(username=username, password=password)
+        if user is not None:
+            if user.is_active:
+                login(request, user)
+                state = \"You're successfully logged in!\"
+            else:
+                state = \"Your account is not active, please contact the site admin.\"
+        else:
+            state = \"Your username and/or password were incorrect.\"
 
-#     return render_to_response('login.html',{'state':state, 'username': username})
+    return render_to_response('login.html',{'state':state, 'username': username})
 
 
 def proj_details(request, project_id):
@@ -114,7 +86,15 @@ def the_comments(request, task_id):
 \tresponse = \"You're looking at the comments of question %s.\"
 \treturn HttpResponse(response % task_id)
 
-def detail(request, task_id, project_id):
+def detail(request, task_id, foreign_key):
+\t#try:
+\tif request.method == 'POST':
+\t\tform = CommentForm(request.POST)
+\t\tif form.is_valid():
+\t\t\treturn HttpResponseRedirect('/thanks/')
+\t\telse:
+\t\t\tform = CommentForm()
+
 \ttask = Task.objects.get(pk = task_id)
 \t#except Task.DoesNotExist:
 \t#\traise Http404
", "reason": "High Entropy", "stringsFound": ["20821e4abaea95268880f020c9f6768288f3725a"]}
```

### "Challenge: Secrets Scanning with TruffleHog"

> No hubo challenge

## How to Embed the TruffleHog into GitLab

### Use TruffleHog tool to perform secrets scanning in CI/CD pipeline

In this scenario, you will learn how to embed Secrets scanning in CI/CD pipeline.

You will learn to use TruffleHog in CI/CD pipeline and how to allow job failure when the tool found several issues.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

> Remember!
> 
> Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
> 
> After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

### A simple CI/CD pipeline

Considering your DevOps team created a simple CI pipeline with the following contents.

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

We have two jobs in this pipeline, a **build** job and a **test** job. As a security engineer, I do not care what they are doing as part of these jobs. Why? Imagine having to learn every build/testing tool used by your DevOps team, it will be a nightmare. Instead, rely on the DevOps team for help.

Let’s log in to Gitlab using the following details.

### GitLab CI/CD Machine

Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

Next, we need to create a CI/CD pipeline by adding the above content to the **.gitlab-ci.yml** file. Click on the **Edit** button to start adding the content.

Save changes to the file using **Commit changes** button.

### Verify the pipeline run

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

### "Challenge: How to Embed the TruffleHog into GitLab"

Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

1. Read the [trufflehog documentation](https://github.com/dxa4481/truffleHog)
2. Embed git secrets scanning tool, Trufflehog in **build** stage with job name as **git-secrets**
3. Ensure the job is running in the **build** stage
4. You can either install truffleHog manually or use **hysnsec/trufflehog** docker container
5. Follow all the best practices while embedding Trufflehog in the CI/CD pipeline. Don’t forget the tool evaluation criteria
6. Can you figure out why Trufflehog didn’t give an output or file after scanning is done?

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the next step.

### Embed TruffleHog in CI/CD pipeline

As discussed in the **Secrets Scanning** exercise, we can embed TruffleHog in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

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

git-secrets:
  stage: build
  script:
    - docker pull hysnsec/trufflehog
    - docker run --user $(id -u):$(id -g) -v $(pwd):/src --rm hysnsec/trufflehog file:///src
```

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

> As you can see, Trufflehog didn’t give us any output as it did on the DevSecOps Box. Why?

Let’s try running the **git status** command after the Trufflehog command and see the job’s output.

```yml
git-secrets:
  stage: build
  script:
    - docker pull hysnsec/trufflehog
    - docker run --user $(id -u):$(id -g) -v $(pwd):/src --rm hysnsec/trufflehog file:///src
    - git status
```

Notice the last line of the above job, we have added **git status** command.

> HEAD detached at c4560d9
> nothing to commit, working tree clean

The output tells us that **HEAD is detached**; what does it mean? It means we’re not on a branch but checked out a specific commit in the history. Trufflehog needs us to be on a branch. Hence Trufflehog didn’t find any secrets, and we can fix it with the following jobs.

```yml
git-secrets:
  stage: build
  script:
    - apk add git
    - git checkout master
    - docker run -v $(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
  artifacts:
    paths: [trufflehog-output.json]
    when: always  # What is this for?
    expire_in: one week
```

The **git** function helps us in cloning the source code and then checking out the **master** branch.

Let’s move to the **next step**.

### Allow the job failure

> Remember!
>
> Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
> After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the **allow_failure** tag to not fail the build even though the tool found issues.

```yaml
git-secrets:
  stage: build
  script:
    - apk add git
    - git checkout master
    - docker run -v $(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
  artifacts:
    paths: [trufflehog-output.json]
    when: always  # What is this for?
    expire_in: one week
  allow_failure: true   #<--- allow the build to fail but don't mark it as such
```

> Why we’re adding apk add git?
>
> See the first line in .gitlab-ci.yml file, the image keyword is the name of the Docker image the Docker executor runs to perform the CI tasks, and we use docker:latest (Alpine-based) image as default.

After adding the allow_failure tag, the pipeline would look like the following.

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

git-secrets:
  stage: build
  script:
    - apk add git
    - git checkout master
    - docker run -v $(pwd):/src --rm hysnsec/trufflehog file:///src --json | tee trufflehog-output.json
  artifacts:
    paths: [trufflehog-output.json]
    when: always  # What is this for?
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

You will notice that the **git-secrets** job failed however it didn’t block others from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

## How to Embed TruffleHog into Jenkins 

## How to Embed TruffleHog into GitHub Actions

## How to Embed TruffleHog into CircleCI

## Secrets Scanning with detect-secrets

## Secrets Scanning with Talisman

## Static Analysis using Bandit

### Learn how to run static analysis scans on the code

In this scenario, you will learn how to install and run SAST Scans on a git repository.

You will need to download the code, install the SAST tool called **Bandit** and then finally run the SAST scan on the code.

### Download the source code

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv webapp
```

Let’s cd into the application so we can scan the app.

```bash
cd webapp
```

We are now in the webapp directory.

Let’s move to the **next step**.

### Install SAST Tool

> The Bandit is a tool designed to find common security issues in Python code.
>
> To do this Bandit, processes each file, builds an AST, and runs appropriate plugins against the AST nodes. Once Bandit has finished scanning all the files it generates a report.
>
> Bandit was originally developed within the OpenStack Security Project and later rehomed to PyCQA.
>
> You can find more details about the project at [https://github.com/PyCQA/bandit](https://github.com/PyCQA/bandit).

```bash
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
```

We have successfully installed Bandit scanner. Let’s explore the functionality it provides us.

```bash
bandit --help
usage: bandit [-h] [-r] [-a {file,vuln}] [-n CONTEXT_LINES] [-c CONFIG_FILE]
              [-p PROFILE] [-t TESTS] [-s SKIPS] [-l] [-i]
              [-f {csv,custom,html,json,screen,txt,xml,yaml}]
              [--msg-template MSG_TEMPLATE] [-o [OUTPUT_FILE]] [-v] [-d] [-q]
              [--ignore-nosec] [-x EXCLUDED_PATHS] [-b BASELINE]
              [--ini INI_PATH] [--exit-zero] [--version]
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
                        config file) (default:
                        .svn,CVS,.bzr,.hg,.git,__pycache__,.tox,.eggs,*.egg)
  -b BASELINE, --baseline BASELINE
                        path of a baseline report to compare against (only
                        JSON-formatted files are accepted)
  --ini INI_PATH        path to a .bandit file that supplies command line
                        arguments
  --exit-zero           exit with 0, even with results found
  --version             show program's version number and exit
```

Let’s move to the **next step**.

### Run the scanner

As we have learned in DevSecOps Gospel, we would like to store the content in a JSON file. We are using the **tee** command here to show the output and store it in a file simultaneously.

```bash
bandit -r . -f json | tee bandit-output.json
```

Bandit ran successfully, and it found three security issues.
1. One high severity issue
2. One medium severity issue
3. One low severity issue

In the next exercise, we will explore how to **Embed Bandit to the CI/CD pipeline**.

### "Challenge: Static Analysis using Bandit"

> No hubo challenge

## How to Embed Bandit into GitLab

### Use Bandit tool to do SAST in CI/CD pipeline

In this scenario, you will learn how to embed SAST in CI/CD pipeline.

You will learn to use Bandit in CI/CD pipeline and how to allow job failure when the tool found several issues.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

> Remember!
>
> 1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
> 2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

### A simple CI/CD pipeline

Considering your DevOps team created a simple CI pipeline with the following contents.

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

We have four jobs in this pipeline, a **build** job, a **test** job, a **integration** job and a **prod** job. We did integrate SCA/OAST beforehand, we can carry forward the same tactics in this exercise as well.

As a security engineer, I don’t need to care much about what the DevOps team is doing as part of these jobs. Why? imagine having to learn every build/testing tool used by your DevOps team, it will be a nightmare. Instead, rely on the DevOps team for help.

Let’s login into the GitLab using the following details and execute this pipeline.

### GitLab CI/CD Machine

Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the above CI script. Click on the **Edit** button to replace the content (use Control+A and Control+V).

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.


### "Challenge: How to Embed Bandit into GitLab"

Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

1. Read the [bandit documentation](https://github.com/PyCQA/bandit/tree/master/bandit)
2. Embed SAST backend tool, Bandit in **test** stage with job name as **sast**

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

bandit:
  stage: test
  image: python:3.6
  script:
    - pip3 install bandit
    - bandit -r . -f json | tee bandit-output.json
  artifacts:
    paths: [bandit-output.json]
    when: always
    expire_in: one week
  allow_failure: true

integration:
  stage: integration
  script:
    - echo "This is an integration step"
    - exit 1
  allow_failure: true

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual
```

3. Ensure the job is running under the **test** stage

4. After the above steps, move it to the build stage now

5. You can either install Bandit manually or use **hysnsec/bandit** docker container

6. Follow all the best practices while embedding Bandit in the CI/CD pipeline, and don’t forget the tool evaluation criteria

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed Bandit in CI/CD pipeline

As discussed in the **Static Analysis using Bandit** exercise, we can embed Bandit in our CI/CD pipeline. However, do remember you need to run the command manually before you embed this SAST tool in the pipeline.

---

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

sast:
  stage: build
  script:
    # Download bandit docker container
    - docker pull hysnsec/bandit    
    # Run docker container, please refer docker security course, if this doesn't make sense to you.
    - docker run --user $(id -u):$(id -g) -v $(pwd):/src --rm hysnsec/bandit -r /src -f json -o /src/bandit-output.json
  artifacts:
    paths: [bandit-output.json]
    when: always

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

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

You will notice that the **sast** job’s output is saved in **bandit-output.json** file.

Let’s move to the **next step**.

### Allow the job failure

> Remember!
>
> Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
> After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the **allow_failure** tag to not fail the build even though the tool found issues.

```yaml
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
```

After adding the allow_failure tag, the pipeline would look like the following.

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
    - retire --outputformat json --outputpath retirejs-report.json --severity high --exitwith 0
  artifacts:
    paths: [retirejs-report.json]
    when: always # What is this for?
    expire_in: one week

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

> Did you see we sneaked **oast-frontend** at the end?

You will notice that the **sast** job failed however it didn’t block others from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

### Notes

The above option mounts the current directory in the host (runner) to /src inside the container. This could also be **-v /home/ubuntu/code:/src** or **c:\users\code:/src** if you were using windows.

Instead of manually removing the container after the scan, we can use **–rm** option so docker can clean up after itself.

What is this **–user** option here, and why are we using it? Can you please try removing this option **–user $(id -u):$(id -g)** and see what would happen?

> Hint: [Running a Docker container as a non-root user](https://medium.com/redbubble/running-a-docker-container-as-a-non-root-user-7d2e00f8ee15.)

Let’s move to the **next step**.

### Extra Mile Exercise: Write a custom wrapper for the bandit tool to fail the build only when you found 3 high, 1 low, and 1 medium issue

#### Who should do this exercise?

1. This exercise is beyond the scope of the CDP course and is added to help folks who already know these concepts in and out
2. You know how to write small scripts in **Python** or **Ruby** or **Golang**
3. You consider yourself an expert or advanced user in SAST

#### Challenge

Recall techniques you have learned in the previous exercises.

1. Read the [bandit documentation](https://github.com/PyCQA/bandit/tree/master/bandit)
2. Write a wrapper to parse the bandit output
3. Make the output pretty (**Red = High Severity**, **Blue = Medium Severity**, **Orange = Low Severity**)
4. Once done, create a docker image with the help of a **Dockerfile**
5. Run the docker image in CI/CD pipeline to pretty-print the output

> Please do note, we will not provide solutions for this extra mile exercise.

## False Positive Analysis (FPA)

### We will learn how to do false positive analysis on reported issues by bandit tool

In this scenario, you will learn how to do False Positive Analysis (FPA) on the Bandit tool’s reported issues.

### Download the source code

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/dvpa-api webapp
```

Let’s cd into the application so we can scan the app.

```bash
cd webapp
```

We are now in the **webapp** directory.

Let’s move to the **next step**.

### Installing Bandit

Let’s install the bandit scanner on the system to perform static analysis.

```bash
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
```

We have successfully installed the bandit scanner. Let’s explore the functionality it provides us.

```bash
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
```

Let’s move to the **next step**.

### Run the scanner

Let’s scan our source code by executing the following command:

```bash
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
```

We got **13** issues in total (by severity), but we need to exclude the False Positives.

Let’s move to the **next step**.

### False Positive Analysis (FPA)

There are two ways to do the False Positive Analysis. Either by reading the source code or by exploiting the vulnerability. In this exercise, we only cover the first way.

As per Bandit’s scan results, we have hardcoded password strings, an insecure hash function issue, an insecure deserialization issue, and SQL injection vulnerability.

Let’s try to analyze if they are real issues or false positives. We will try to explore the following three issues among them.

```bash
--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: dvpa-api/flaskblog/blogapi/dashboard.py:99
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
98              cur = db.connection.cursor()
99              cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}")
100             db.connection.commit()

--------------------------------------------------
>> Issue: [B608:hardcoded_sql_expressions] Possible SQL injection vector through string-based query construction.
   Severity: Medium   Confidence: Medium
   Location: dvpa-api/flaskblog/blogapi/dashboard.py:132
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html
131             cur.execute(
132                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
133                 [body, slug, claim.get("id"), title])

--------------------------------------------------
>> Issue: [B506:yaml_load] Use of unsafe yaml load. Allows instantiation of arbitrary objects. Consider yaml.safe_load().
   Severity: Medium   Confidence: High
   Location: dvpa-api/flaskblog/blogapi/dashboard.py:242
   More Info: https://bandit.readthedocs.io/en/latest/plugins/b506_yaml_load.html
241                 elif export_format == "yaml":
242                     import_post_data = yaml.load(import_data)
243
```

### Analysis of the issues

Let’s explore the first issue now.

```bash
98              cur = db.connection.cursor()
99              cur.execute(f"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}")
100             db.connection.commit()
```

It looks like the above code is vulnerable to SQL Injection because **uid** is used in the query directly, so it’s not a False Positive.

Next, lets check out the second result.

```bash
131             cur.execute(
132                 f"INSERT INTO posts (`body`, `slug`, `author`, `title`) VALUES (%s, %s, %s, %s)",
133                 [body, slug, claim.get("id"), title])
```

The above code is definitely **False Positive** as we are using **Parameter Binding** to create the query.

And the last one is a known vulnerability in Python’s YAML library called YAML deserialization. We can search for this known security issue on the CVE website.

```bash
241                 elif export_format == "yaml":
242                     import_post_data = yaml.load(import_data)
243
```

For more details about this issue, please visit [CVE-2020-1747](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2020-1747).

As mentioned before, our code is vulnerable to [Deserialization Attacks](https://cheatsheetseries.owasp.org/cheatsheets/Deserialization_Cheat_Sheet.html) and we can mark this as **not a False Positive**.

Let’s move to the next step.

### "Challenge: False Positive Analysis (FPA)"

1. Analyse the results and mark relevant issues as **False Positive**
2. Use Bandit feature to perform False Positive as Code
3. Please ensure the issues marked as False positives do not show up in the next run

Here I only mark "test_id": "B608", "filename": "./flaskblog/blogapi/dashboard.py", "code": "99... as false positive

```bash
cat > .baseline.json <<EOL
{
  "errors": [],
  "generated_at": "2021-10-18T12:36:43Z",
  "metrics": {
    "./flaskblog/__init__.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 45,
      "nosec": 0
    },
    "./flaskblog/auth.py": {
      "CONFIDENCE.HIGH": 1.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 1.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 2.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 33,
      "nosec": 0
    },
    "./flaskblog/blogapi/__init__.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 10,
      "nosec": 0
    },
    "./flaskblog/blogapi/app.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 0,
      "nosec": 0
    },
    "./flaskblog/blogapi/dashboard.py": {
      "CONFIDENCE.HIGH": 2.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 3.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 5.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 177,
      "nosec": 0
    },
    "./flaskblog/blogapi/home.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 2.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 2.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 37,
      "nosec": 0
    },
    "./flaskblog/blogapi/user.py": {
      "CONFIDENCE.HIGH": 1.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 1.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 2.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 78,
      "nosec": 0
    },
    "./flaskblog/config.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 2.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 2.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 13,
      "nosec": 0
    },
    "./flaskblog/dashboard/__init__.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 2,
      "nosec": 0
    },
    "./flaskblog/dashboard/password_change.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 9,
      "nosec": 0
    },
    "./flaskblog/dashboard/post.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 79,
      "nosec": 0
    },
    "./flaskblog/dashboard/profile.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 8,
      "nosec": 0
    },
    "./flaskblog/dashboard/routes.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 13,
      "nosec": 0
    },
    "./flaskblog/db_util.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 17,
      "nosec": 0
    },
    "./flaskblog/decorator.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 17,
      "nosec": 0
    },
    "./flaskblog/manage.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 13,
      "nosec": 0
    },
    "./flaskblog/user.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 30,
      "nosec": 0
    },
    "./flaskblog/views.py": {
      "CONFIDENCE.HIGH": 0.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 0.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 0.0,
      "SEVERITY.MEDIUM": 0.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 22,
      "nosec": 0
    },
    "_totals": {
      "CONFIDENCE.HIGH": 4.0,
      "CONFIDENCE.LOW": 0.0,
      "CONFIDENCE.MEDIUM": 9.0,
      "CONFIDENCE.UNDEFINED": 0.0,
      "SEVERITY.HIGH": 0.0,
      "SEVERITY.LOW": 2.0,
      "SEVERITY.MEDIUM": 11.0,
      "SEVERITY.UNDEFINED": 0.0,
      "loc": 603,
      "nosec": 0
    }
  },
  "results": [
    {
      "code": "99         cur = db.connection.cursor()\n100         cur.execute(f\"UPDATE `users` SET `email` = '{email}', `full_name` = '{full_name}', `phone_number` = '{phone_number}', `dob` = '{dob}' WHERE `users`.`id` = {request.args.get('uid')}\")\n101         db.connection.commit()\n",
      "filename": "./flaskblog/blogapi/dashboard.py",
      "issue_confidence": "MEDIUM",
      "issue_severity": "MEDIUM",
      "issue_text": "Possible SQL injection vector through string-based query construction.",
      "line_number": 100,
      "line_range": [
        100
      ],
      "more_info": "https://bandit.readthedocs.io/en/latest/plugins/b608_hardcoded_sql_expressions.html",
      "test_id": "B608",
      "test_name": "hardcoded_sql_expressions"
    }
  ]
}
bandit -r . -f json --baseline .baseline.json| tee bandit-output.json 
```

> Please do not forget to share the answer with our staff via Slack Direct Message (DM).

> Hint
>Checkout **-b baseline.json** to mark issues as False Positives.
>
>You can consume the bandit’s output from earlier scan to mark issues as False Positives.
>
>If you are still unsure, please try passing the bandit’s output to -b and see what happens.

### Additional Resources

For more information about False Positives in AppSec, see the following references:

- https://dev.to/johspaeth/the-myth-of-false-positives-in-static-application-security-testing-146g
- https://www.netsparker.com/false-positives-in-application-security-whitepaper
- https://www.synopsys.com/blogs/software-security/avoiding-false-positives
- https://www.contrastsecurity.com/security-influencers/accuracy-in-appsec-critical-reduce-false-positives

## Static Analysis using Brakeman

### Learn how to run static analysis scans on Ruby on Rails code using Brakeman

In this scenario, you will learn how to run **Brakeman** scan on Rails code.

You will need to download the code, install the SAST tool called **Brakeman** and then finally run the SAST scan on the code.

### Download the source code

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to download the source code of the project from our git repository.

```bash
git clone https://github.com/OWASP/railsgoat.git webapp
```

Let’s cd into the application so we can scan the app.

```bash
cd webapp
```

We are now in the **webapp** directory.

Let’s move to the **next step**.

### Install Brakeman

> Brakeman is Static Analysis tool for Rails application to find vulnerabilities, Fast and Flexible tools with a very good report and fit to embed it in CI/CD pipeline.
>
> You can find more details about the project at https://brakemanscanner.org/.

Basically, our system doesn’t have Ruby installed, let’s update apt first.

```bash
apt update
```

Then, let’s install **Ruby** with the following command.

```bash
apt install ruby-full -y
```

Let’s install the Brakeman tool to perform static analysis.

```bash
gem install brakeman
Fetching: brakeman-4.9.1.gem (100%)
Successfully installed brakeman-4.9.1
Parsing documentation for brakeman-4.9.1
Installing ri documentation for brakeman-4.9.1
Done installing documentation for brakeman after 2 seconds
1 gem installed
```

We have successfully installed **Brakeman**, let’s explore the functionality it provides us.

```bash
brakeman -h
Usage: brakeman [options] rails/root/path
    -n, --no-threads                 Run checks sequentially
        --[no-]progress              Show progress reports
    -p, --path PATH                  Specify path to Rails application
    -q, --[no-]quiet                 Suppress informational messages
    -z, --[no-]exit-on-warn          Exit code is non-zero if warnings found (Default)
        --[no-]exit-on-error         Exit code is non-zero if errors raised (Default)
        --ensure-latest              Fail when Brakeman is outdated
        --ensure-ignore-notes        Fail when an ignored warnings does not include a note
    -3, --rails3                     Force Rails 3 mode
    -4, --rails4                     Force Rails 4 mode
    -5, --rails5                     Force Rails 5 mode
    -6, --rails6                     Force Rails 6 mode

Scanning options:
    -A, --run-all-checks             Run all default and optional checks
    -a, --[no-]assume-routes         Assume all controller methods are actions (Default)
    -e, --escape-html                Escape HTML by default
        --faster                     Faster, but less accurate scan
        --ignore-model-output        Consider model attributes XSS-safe
        --ignore-protected           Consider models with attr_protected safe
        --[no-]index-libs            Add libraries to call index (Default)
        --interprocedural            Process method calls to known methods
        --no-branching               Disable flow sensitivity on conditionals
        --branch-limit LIMIT         Limit depth of values in branches (-1 for no limit)
        --parser-timeout SECONDS     Set parse timeout (Default: 10)
    -r, --report-direct              Only report direct use of untrusted data
    -s meth1,meth2,etc,              Set methods as safe for unescaped output in views
        --safe-methods
        --url-safe-methods method1,method2,etc
                                     Do not warn of XSS if the link_to href parameter is wrapped in a safe method
        --skip-files file1,path2,etc Skip processing of these files/directories. Directories are application relative and must end in "/"
        --only-files file1,path2,etc Process only these files/directories. Directories are application relative and must end in "/"
        --skip-libs                  Skip processing lib directory
        --add-libs-path path1,path2,etc
                                     An application relative lib directory (ex. app/mailers) to process
        --add-engines-path path1,path2,etc
                                     Include these engines in the scan
    -E, --enable Check1,Check2,etc   Enable the specified checks
    -t, --test Check1,Check2,etc     Only run the specified checks
    -x, --except Check1,Check2,etc   Skip the specified checks
        --add-checks-path path1,path2,etc
                                     A directory containing additional out-of-tree checks to run

Output options:
    -d, --debug                      Lots of output
    -f, --format TYPE                Specify output formats. Default is text
        --css-file CSSFile           Specify CSS to use for HTML output
    -i, --ignore-config IGNOREFILE   Use configuration to ignore warnings
    -I, --interactive-ignore         Interactively ignore warnings
    -l, --[no-]combine-locations     Combine warning locations (Default)
        --[no-]highlights            Highlight user input in report
        --[no-]color                 Use ANSI colors in report (Default)
    -m, --routes                     Report controller information
        --message-limit LENGTH       Limit message length in HTML report
        --[no-]pager                 Use pager for output to terminal (Default)
        --table-width WIDTH          Limit table width in text report
    -o, --output FILE                Specify files for output. Defaults to stdout. Multiple '-o's allowed
        --[no-]separate-models       Warn on each model without attr_accessible (Default)
        --[no-]summary               Only output summary of warnings
        --absolute-paths             Output absolute file paths in reports
        --github-repo USER/REPO[/PATH][@REF]
                                     Output links to GitHub in markdown and HTML reports using specified repo
        --text-fields field1,field2,etc.
                                     Specify fields for text report format
    -w, --confidence-level LEVEL     Set minimal confidence level (1 - 3)
        --compare FILE               Compare the results of a previous Brakeman scan (only JSON is supported)

Configuration files:
    -c, --config-file FILE           Use specified configuration file
    -C, --create-config [FILE]       Output configuration file based on options
        --allow-check-paths-in-config
                                     Allow loading checks from configuration file (Unsafe)

    -k, --checks                     List all available vulnerability checks
        --optional-checks            List optional checks
    -v, --version                    Show Brakeman version
        --force-scan                 Scan application even if rails is not detected
    -h, --help                       Display this message
```

Let’s move to the **next step**.

### Run the Scanner

As we have learned in **DevSecOps Gospel**, we would like to store the content in a JSON file. We are using the **tee** command here to show the output and store it in a file simultaneously.

```bash
brakeman -f json | tee result.json
```

Brakeman ran successfully, and it found **17** security issues.

1. Six medium severity issues
2. Eleven high severity issues

It seems we have found quite a few issues; you can ignore issues with using the **brakeman.ignore** file.

> You will find fingerprints in the scan output.

```bash
nano brakeman.ignore
```

Please copy the below code snippet and store it in the **brakeman.ignore** file.

```json
{
    "ignored_warnings": [
        {
          "fingerprint": "febb21e45b226bb6bcdc23031091394a3ed80c76357f66b1f348844a7626f4df",
          "note": "ignore XSS"
        }
    ]
}
```

Let’s re-run the scanner.

```bash
brakeman -f json -i brakeman.ignore | tee result.json
```

You will see the issues are reduced because we ignored the **Cross-Site Scripting (XSS)** vulnerability.

### "Challenge: Static Analysis using Brakeman"

> No hubo challenge