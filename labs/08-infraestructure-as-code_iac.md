# Infrastructure as Code (IaC)

## Indice

- [Hardening using Ansible (Mandatory)](#hardening-using-ansible)
- [Ansible Ad-hoc commands (Mandatory)](#ansible-ad-hoc-commands)
- [Ansible playbook basics (Optional)](#ansible-playbook-basics)
- [Ansible conditionals (Optional)](#ansible-conditionals)
- [Container deployment using Ansible (Optional)](#container-deployment-using-ansible)
- [Learn how to embed Ansible in the Jenkins CI/CD pipeline (Optional)](#learn-how-to-embed-ansible-in-the-jenkins-cicd-pipeline)
- [Harden machines in CI/CD pipelines (Mandatory)](#harden-machines-in-cicd-pipelines)
- [Use TFLint to find security issues in IaC (Optional)](#use-tflint-to-find-security-issues-in-iac)
- [Learn how to embed TFLint into GitHub Actions (Optional)](#learn-how-to-embed-tflint-into-github-actions)
- [Use Checkov to check misconfigurations in IaC (Optional)](#use-checkov-to-check-misconfigurations-in-iac)
- [Learn how to embed Checkov into CI/CD pipeline (Optional)](#learn-how-to-embed-checkov-into-cicd-pipeline)
- [Learn how to embed Checkov into Jenkins CI/CD pipeline (Optional)](#learn-how-to-embed-checkov-into-jenkins-cicd-pipeline)
- [Learn how to embed Checkov into CircleCI (Optional)](#learn-how-to-embed-checkov-into-circleci)
- [Secure IaC using Ansible Vault (Optional)](#secure-iac-using-ansible-vault)
- [Use Terrascan to find security issues in IaC (Optional)](#use-terrascan-to-find-security-issues-in-iac)
- [Learn how to embed Terrascan into GitHub Actions (Optional)](#learn-how-to-embed-terrascan-into-github-actions)
- [Learn how to embed Terrascan into CircleCI (Optional)](#learn-how-to-embed-terrascan-into-circleci)
- [Use tfsec to find security issues in IaC (Optional)](#use-tfsec-to-find-security-issues-in-iac)
- [Learn how to embed tfsec into GitHub Actions (Optional)](#learn-how-to-embed-tfsec-into-github-actions)
- [Learn how to embed tfsec into CircleCI (Optional)](#learn-how-to-embed-tfsec-into-circleci)
- [Use Snyk tool to find security issues in your IaC (Optional)](#use-snyk-tool-to-find-security-issues-in-your-iac)

## Hardening using Ansible

### Learn how to use Ansible to harden a production environment

In this scenario, you will learn how to install and run Ansible on a remote machine.

You will need to install the **Ansible** and then finally run the Ansible ad-hoc and playbook commands on the remote machine.

### Install Ansible and Ansible Lint

> Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
>
>Source: [Ansible official website](https://www.ansible.com/).

This exercise uses two machines, the DevSecOps Box with hostname as **devsecops-box-xIKciVAk**, and a production machine, **prod-xIKciVAk**.

We will do all the exercises locally first in DevSecOps-Box, so let’s start the activity.

First, we need to install the **ansible** and **ansible-lint** programs.

```bash
pip3 install ansible==2.10.4 ansible-lint==4.3.7
```

Let’s move to the **next step**.

### Create the inventory file

Let’s create the inventory or CMDB file for Ansible using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[prod]
prod-xIKciVAk

EOL
```

>Please ignore the machine id in **Command Output**, it should be the same as your machine ID.

Next, we will have to ensure the SSH’s **yes/no** prompt is not shown while running the ansible commands, so we will be using ssh-keyscan to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa prod-xIKciVAk >> ~/.ssh/known_hosts
```

```
# prod-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s do this for the rest of the systems in the lab as well.

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

```bash
# devsecops-box-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

>**Pro-tip**: Instead of running the **ssh-keyscan** command twice, we can achieve the same using the below command.

```bash
ssh-keyscan -t rsa prod-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```
Let’s move to the **next step**.

### Run the ansible commands

Let’s run the **ansible ad-hoc command** to check the production machine’s uptime, install the **ntp** service, and check the bash version of all systems.

Let’s use the shell module of ansible to run the **uptime** command on the production machine.

```bash
ansible -i inventory.ini prod -m shell -a "uptime"
```

```bash
prod-qopqfmfu | CHANGED | rc=0 >>
 12:06:35 up 18 days,  5:11,  1 user,  load average: 0.65, 0.80, 0.40
```

As you can see above, we got the uptime of the machine.

```bash
12:06:35 up 18 days,  5:11,  1 user,  load average: 0.65, 0.80, 0.40
```

Similarly, we can use **apt** ansible module to install the ntp service on the production machine.

```bash
ansible -i inventory.ini prod -m apt -a "name=ntp state=present"
```

```bash
prod-qopqfmfu | CHANGED => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "cache_update_time": 1599221362,
    "cache_updated": false,
    "changed": true,
    "stderr": "debconf: delaying package configuration, since apt-utils is not installed\n",
    "stderr_lines": [
        "debconf: delaying package configuration, since apt-utils is not installed"
    ],
    "stdout": "Reading package lists...\nBuilding dependency tree...\nReading state information...\nThe following additional packages will be installed:\n  libopts25 netbase sntp tzdata\nSuggested packages:\n  ntp-doc\nThe following NEW packages will be installed:\n  libopts25 netbase ntp sntp tzdata\n0 upgraded, 5 newly installed, 0 to remove and 5 not upgraded.\nNeed to get 987 kB of archives.\nAfter this operation, 5547 kB of additional disk space will be used.\nGet:1 http://archive.ubuntu.com/ubuntu bionic/main amd64 netbase all 5.4 [12.7 kB]\nGet:2 http://archive.ubuntu.com/ubuntu bionic-updates/main amd64 tzdata all 2020a-0ubuntu0.18.04 [190 kB]\nGet:3 http://archive.ubuntu.com/ubuntu bionic/universe amd64 libopts25 amd64 1:5.18.12-4 [58.2 kB]\nGet:4 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 ntp amd64 1:4.2.8p10+dfsg-5ubuntu7.2 [640 kB]\nGet:5 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 sntp amd64 1:4.2.8p10+dfsg-5ubuntu7.2 [86.5 kB]\nFetched 987 kB in 1s (1735 kB/s)\nSelecting previously unselected package netbase.\r\n(Reading database ... \r(Reading database ... 5%\r(Reading database ... 10%\r(Reading database ... 15%\r(Reading database ... 20%\r(Reading database ... 25%\r(Reading database ... 30%\r(Reading database ... 35%\r(Reading database ... 40%\r(Reading database ... 45%\r(Reading database ... 50%\r(Reading database ... 55%\r(Reading database ... 60%\r(Reading database ... 65%\r(Reading database ... 70%\r(Reading database ... 75%\r(Reading database ... 80%\r(Reading database ... 85%\r(Reading database ... 90%\r(Reading database ... 95%\r(Reading database ... 100%\r(Reading database ... 10881 files and directories currently installed.)\r\nPreparing to unpack .../archives/netbase_5.4_all.deb ...\r\nUnpacking netbase (5.4) ...\r\nSelecting previously unselected package tzdata.\r\nPreparing to unpack .../tzdata_2020a-0ubuntu0.18.04_all.deb ...\r\nUnpacking tzdata (2020a-0ubuntu0.18.04) ...\r\nSelecting previously unselected package libopts25:amd64.\r\nPreparing to unpack .../libopts25_1%3a5.18.12-4_amd64.deb ...\r\nUnpacking libopts25:amd64 (1:5.18.12-4) ...\r\nSelecting previously unselected package ntp.\r\nPreparing to unpack .../ntp_1%3a4.2.8p10+dfsg-5ubuntu7.2_amd64.deb ...\r\nUnpacking ntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...\r\nSelecting previously unselected package sntp.\r\nPreparing to unpack .../sntp_1%3a4.2.8p10+dfsg-5ubuntu7.2_amd64.deb ...\r\nUnpacking sntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...\r\nSetting up tzdata (2020a-0ubuntu0.18.04) ...\r\n\r\nCurrent default time zone: 'Etc/UTC'\r\nLocal time is now:      Fri Sep  4 12:09:31 UTC 2020.\r\nUniversal Time is now:  Fri Sep  4 12:09:31 UTC 2020.\r\nRun 'dpkg-reconfigure tzdata' if you wish to change it.\r\n\r\nSetting up libopts25:amd64 (1:5.18.12-4) ...\r\nSetting up netbase (5.4) ...\r\nSetting up sntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...\r\nSetting up ntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...\r\ninvoke-rc.d: could not determine current runlevel\r\ninvoke-rc.d: policy-rc.d denied execution of start.\r\nProcessing triggers for libc-bin (2.27-3ubuntu1.2) ...\r\n",

    ...[SNIP]...

        "Setting up libopts25:amd64 (1:5.18.12-4) ...",
        "Setting up netbase (5.4) ...",
        "Setting up sntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...",
        "Setting up ntp (1:4.2.8p10+dfsg-5ubuntu7.2) ...",
        "invoke-rc.d: could not determine current runlevel",
        "invoke-rc.d: policy-rc.d denied execution of start.",
        "Processing triggers for libc-bin (2.27-3ubuntu1.2) ..."
    ]
}
```

Instead of restricting the commands to the prod machine, let’s find the bash version installed on all the machines in the inventory file.

```bash
ansible -i inventory.ini all -m command -a "bash --version"
```

```bash
devsecops-box-qopqfmfu | CHANGED | rc=0 >>
GNU bash, version 4.4.20(1)-release (x86_64-pc-linux-gnu)
Copyright (C) 2016 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>

This is free software; you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
prod-qopqfmfu | CHANGED | rc=0 >>
GNU bash, version 4.4.20(1)-release (x86_64-pc-linux-gnu)
Copyright (C) 2016 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>

This is free software; you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
```

Nice, we can see the bash version of the prod and devsecops-box **GNU bash**, **version 4.4.20(1)-release (x86_64-pc-linux-gnu)**.

### Challenge: Ansible Ad-hoc commands

In this exercise, we will use Ansible ad-hoc command to find the uptime of our lab machines.

1. Use inventory file (**-i inventory.ini**) and the following command to find the uptime
2. Use the Ansible command module to find **uptime**

> How is this helpful in real-world situations? We can find uptime of all of your production machine fleet of 500 machines within 5 minutes.
>
>Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Run the Ansible playbook

Let’s create a playbook to run against the production environment.

```bash
cat > playbook.yml <<EOL
---
- name: Example playbook to install nginx
  hosts: prod
  remote_user: root
  become: yes
  gather_facts: no
  vars:
    state: present

  tasks:
  - name: ensure Nginx is at the latest version
    apt:
      name: nginx

EOL
```

Let’s run this playbook against the prod machine.

```bash
ansible-playbook -i inventory.ini playbook.yml
```

```bash
PLAY [Example playbook to install nginx] ******************************************

TASK [ensure Nginx is at the latest version] ***********************************************************************************
ok: [prod-qopqfmfu]

PLAY RECAP ************************************************************************
prod-qopqfmfu             : ok=1    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

Try running the above ansible command once again.

```bash
ansible-playbook -i inventory.ini playbook.yml
```

```bash
PLAY [Example playbook to install nginx] ******************************************

TASK [ensure Nginx is at the latest version] ***********************************************************************************
ok: [prod-qopqfmfu]

PLAY RECAP ************************************************************************
prod-qopqfmfu             : ok=1    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

> Did you notice the **changed=0**? What is its importance from a security perspective?

### Challenge: Create playbook with roles from ansible-galaxy

In this exercise, we will create a playbook using roles available on Ansible Galaxy and run it on the inventory group, devsecops

1. Create a new directory **exercise-7.1** and create a file in it called **playbook.yml**
2. Download **secfigo.terraform** role using **ansible-galaxy**
3. Execute the playbook to install the Terraform utility using **ansible-playbook** command

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Download roles from Ansible Galaxy

Ansible galaxy helps you in storing open source Ansible roles.

Let’s explore the options it provides us.

```bash
ansible-galaxy role --help
```

```bash
usage: ansible-galaxy role [-h] ROLE_ACTION ...

positional arguments:
  ROLE_ACTION
    init       Initialize new role with the base structure of a role.
    remove     Delete roles from roles_path.
    delete     Removes the role from Galaxy. It does not remove or alter the
               actual GitHub repository.
    list       Show the name and version of each role installed in the
               roles_path.
    search     Search the Galaxy database by tags, platforms, author and
               multiple keywords.
    import     Import a role
    setup      Manage the integration between Galaxy and the given source.
    login      Login to api.github.com server in order to use ansible-galaxy
               role sub command such as 'import', 'delete', 'publish', and
               'setup'
    info       View more details about a specific role.
    install    Install role(s) from file(s), URL(s) or Ansible Galaxy

optional arguments:
  -h, --help   show this help message and exit
```

We can search for desired roles using the search option.

```bash
ansible-galaxy search terraform
```

> Please **press q** to get out of the editor.

It’s a long list, but we will choose the **secfigo.terraform** role from the search results.

```bash
ansible-galaxy install secfigo.terraform
```

```bash
- downloading role 'terraform', owned by secfigo
- downloading role from https://github.com/secfigo/ansible-role-terraform/archive/1.0.1.tar.gz
- extracting secfigo.terraform to /root/.ansible/roles/secfigo.terraform
- secfigo.terraform (1.0.1) was installed successfully
```

> Notice, the role was installed under **/root/.ansible/roles/secfigo.terraform** directory?

Let’s create a directory and playbook to install the terraform on the production machine.

```bash
mkdir exercise-7.1 && cd exercise-7.1
mv ../inventory.ini .
```

```yml
cat > playbook.yml <<EOL
---
- name: Example playbook to install Terraform using ansible role.
  hosts: prod
  remote_user: root
  become: yes

  roles:
    - secfigo.terraform
EOL
```

Let’s run this playbook against the prod machine to install the Terraform utility.

```bash
ansible-playbook -i inventory.ini playbook.yml
```

```bash
PLAY [Example playbook to install Terraform using ansible role.] *************

TASK [Gathering Facts] *******************************************************
ok: [prod-qopqfmfu]

TASK [secfigo.terraform : Make sure unzip is installed] **********************
ok: [prod-qopqfmfu]

TASK [secfigo.terraform : Download and Install Terraform] ********************
changed: [prod-qopqfmfu]

PLAY RECAP *******************************************************************
prod-qopqfmfu              : ok=3    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

Let’s move to the **next step**.

### Harden the production environment

> Dev-Sec Project has lots of good examples on how to create Ansible roles and uses lots of best practices which we can use as a baseline in our roles.
>
>For example, https://github.com/dev-sec/ansible-os-hardening.

### Challenge: Using Ansible to Harden prod server

In this exercise, we will create a new playbook that uses **dev-sec.os-hardening** role available from Ansible galaxy to harden the prod environment.

1. Create a new directory hardening and create a file in it called **ansible-hardening.yml**
2. Download **dev-sec.os-hardening** role from **ansible-galaxy**
3. Execute the playbook to harden the Ubuntu production machine
4. Optionally put this hardening job in the CI pipeline (please refer to **Exercise 6.0** for some inspiration)

> **Trivia**: Which stage should this job run in? and against which machines?
>
>Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Ansible Ad-hoc commands

### Learn how to use Ansible Ad-hoc commands to perform tasks on remote machine

In this scenario, you will learn how to install Ansible and run Ad-hoc commands on a remote machine.

### Install Ansible

> Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
> Source: [Ansible official website](https://www.ansible.com/)

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to install the **Ansible** program.

```bash
pip3 install ansible==2.10.4
```

Let’s move to the **next step**.

### Inventory file

Inventory is a file to define a list of hosts that can be sorted as groups, it provides ability to store and manage some variables. The inventory can be created as INI or YAML, but the most common format is INI and might look like this:

```ini
[devsecops]
devsecops-box-xIKciVAk

[sandbox]
sandbox-xIKciVAk

[prod]
prod-xIKciVAk
```

The headings in the bracket are a group name that used to defines our hosts. So let’s create the inventory file for Ansible using the following command.

```bash
cat > inventory.ini <<EOL

[devsecops]
devsecops-box-xIKciVAk

[sandbox]
sandbox-xIKciVAk

[prod]
prod-xIKciVAk

EOL
```

To see which hosts in our inventory matches a supplied group name, let’s try the following command.

```bash
ansible -i inventory.ini prod --list-hosts
```

```bash
  hosts (1):
    prod-xIKciVAk
```

You can change the **prod** value to another group name like **sandbox** or **devsecops** to see if there is a host match. In case there is no host match, the output looks like below for a group named gitlab that does not exist in our inventory file.

```bash
ansible -i inventory.ini gitlab --list-hosts
```

```bash
[WARNING]: Could not match supplied host pattern, ignoring: gitlab
[WARNING]: No hosts matched, nothing to do
  hosts (0):
```

Let’s move to the **next step**.

### Ansible Configuration file

Ansible also can be customized by modifying the ansible configuration called **ansible.cfg**. You can see if there is any default configuration through the following command:

```bash
ansible --version
```

```bash
ansible 2.10.4
  config file = None
  configured module search path = ['/root/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/local/lib/python3.6/dist-packages/ansible
  executable location = /usr/local/bin/ansible
  python version = 3.6.9 (default, Oct  8 2020, 12:12:24) [GCC 8.4.0]
```

**config file** is None, if you’re using CentOS, you will find out there is a config file at **/etc/ansible/ansible.cfg** by default.

Because we don’t have an **ansible.cfg** in our machine, we can create it manually by doing:

```bash
mkdir /etc/ansible/
```

```bash
cat > /etc/ansible/ansible.cfg <<EOF
[defaults]
stdout_callback = yaml
deprecation_warnings = False
host_key_checking = False
retry_files_enabled = False
inventory = /inventory.ini
EOF
```

If you type **ansible –version** command once again, you will see **config file** value now reflects the config file we created above.

```bash
ansible 2.10.4
  config file = /etc/ansible/ansible.cfg
  configured module search path = ['/root/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = /usr/local/lib/python3.6/dist-packages/ansible
  executable location = /usr/local/bin/ansible
  python version = 3.6.9 (default, Oct  8 2020, 12:12:24) [GCC 8.4.0]
```

With the config file, you can define any settings like **inventory** default location or output formats when Ansible finishes a certain command or a playbook. If you want to see further details about the config, you can check out this [link](https://docs.ansible.com/ansible/latest/installation_guide/intro_configuration.html) and the **ansible.cfg** default config at [here](https://raw.githubusercontent.com/ansible/ansible/devel/examples/ansible.cfg).

> **Pro-tip**: Instead of using **/etc/ansible/ansible.cfg** file, you can create an **ansible.cfg** file in the directory which you create a playbook or run Ansible commands.

Let’s move to the **next step**.

### Ad-hoc commands

Ansible uses **ad-hoc** command to execute a single task on one or more remote hosts, and this way of executing a command is easy and fast, but it’s not reusable like the playbook.

For example, we want to use **ping** module through **ad-hoc** command. But before we do that, we will have to ensure the SSH’s **yes/no** prompt is not shown while running the ansible commands, so let’s use the **ssh-keyscan** to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk sandbox-xIKciVAk prod-xIKciVAk >> ~/.ssh/known_hosts
```

```bash
# devsecops-box-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
# sandbox-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
# prod-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

>Please ignore the machine id in **Command Output** above, in your case it would be the same as your machine id.

Then execute the **ansible** command.

```bash
ansible -i inventory.ini all -m ping
```

```bash
devsecops-box-qopqfmfu | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": false,
    "ping": "pong"
}
sandbox-qopqfmfu | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": false,
    "ping": "pong"
}
prod-qopqfmfu | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": false,
    "ping": "pong"
}
```

As you can see the command output above, all our hosts(line number 1, 9 and 15) are connected to Ansible. Next, use the **shell** module of Ansible to run the **hostname** command on all machines.

```bash
ansible -i inventory.ini all -m shell -a "hostname"
```

```bash
devsecops-box-qopqfmfu | CHANGED | rc=0 >>
devsecops-box-qopqfmfu
sandbox-qopqfmfu | CHANGED | rc=0 >>
sandbox-qopqfmfu
prod-qopqfmfu | CHANGED | rc=0 >>
prod-qopqfmfu
```

We can use another module to install a package inside the remote host.

```bash
ansible -i inventory.ini all -m apt -a "name=ntp"
```

```bash
...[SNIP]...

prod-qopqfmfu | CHANGED => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "cache_update_time": 1614261728,
    "cache_updated": false,
    "changed": true,
    "stderr": "debconf: delaying package configuration, since apt-utils is not installed\n",
    "stderr_lines": [
        "debconf: delaying package configuration, since apt-utils is not installed"
    ],
    "stdout": "Reading package lists...\nBuilding dependency tree...\nReading state information...\nThe following additional packages will be installed:\n  libopts25 sntp\nSuggested packages:\n  ntp-doc\nThe following NEW packages will be installed:\n  libopts25 ntp sntp\n0 upgraded, 3 newly installed, 0 to remove and 57 not upgraded.\nNeed to get 785 kB of archives.\nAfter this operation, 2393 kB of additional disk space will be used.\nGet:1 http://archive.ubuntu.com/ubuntu bionic/universe amd64 libopts25 amd64 1:5.18.12-4 [58.2 kB]\nGet:2 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 ntp amd64 1:4.2.8p10+dfsg-5ubuntu7.3 [640 kB]\nGet:3 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 sntp amd64 1:4.2.8p10+dfsg-5ubuntu7.3 [86.5 kB]\nFetched 785 kB in 1s (1226 kB/s)\nSelecting previously unselected package libopts25:amd64.\r\n(Reading database ... \r(Reading database ... 5%\r(Reading database ... 10%\r(Reading database ... 15%\r(Reading database ... 20%\r(Reading database ... 25%\r(Reading database ... 30%\r(Reading database ... 35%\r(Reading database ... 40%\r(Reading database ... 45%\r(Reading database ... 50%\r(Reading database ... 55%\r(Reading database ... 60%\r(Reading database ... 65%\r(Reading database ... 70%\r(Reading database ... 75%\r(Reading database ... 80%\r(Reading database ... 85%\r(Reading database ... 90%\r(Reading database ... 95%\r(Reading database ... 100%\r(Reading database ... 17274 files and directories currently installed.)\r\nPreparing to unpack .../libopts25_1%3a5.18.12-4_amd64.deb ...\r\nUnpacking libopts25:amd64 (1:5.18.12-4) ...\r\nSelecting previously unselected package ntp.\r\nPreparing to unpack .../ntp_1%3a4.2.8p10+dfsg-5ubuntu7.3_amd64.deb ...\r\nUnpacking ntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...\r\nSelecting previously unselected package sntp.\r\nPreparing to unpack .../sntp_1%3a4.2.8p10+dfsg-5ubuntu7.3_amd64.deb ...\r\nUnpacking sntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...\r\nSetting up libopts25:amd64 (1:5.18.12-4) ...\r\nSetting up sntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...\r\nSetting up ntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...\r\nCreated symlink /etc/systemd/system/network-pre.target.wants/ntp-systemd-netif.path -> /lib/systemd/system/ntp-systemd-netif.path.\r\nCreated symlink /etc/systemd/system/multi-user.target.wants/ntp.service -> /lib/systemd/system/ntp.service.\r\n/usr/sbin/policy-rc.d returned 101, not running 'start ntp-systemd-netif.path'\r\n/usr/sbin/policy-rc.d returned 101, not running 'start ntp-systemd-netif.path ntp-systemd-netif.service'\r\ninvoke-rc.d: policy-rc.d denied execution of start.\r\nProcessing triggers for systemd (237-3ubuntu10.42) ...\r\nProcessing triggers for libc-bin (2.27-3ubuntu1.2) ...\r\n",
    "stdout_lines": [
        "Reading package lists...",
        "Building dependency tree...",
        "Reading state information...",
        "The following additional packages will be installed:",
        "  libopts25 sntp",
        "Suggested packages:",
        "  ntp-doc",
        "The following NEW packages will be installed:",
        "  libopts25 ntp sntp",
        "0 upgraded, 3 newly installed, 0 to remove and 57 not upgraded.",
        "Need to get 785 kB of archives.",
        "After this operation, 2393 kB of additional disk space will be used.",
        "Get:1 http://archive.ubuntu.com/ubuntu bionic/universe amd64 libopts25 amd64 1:5.18.12-4 [58.2 kB]",
        "Get:2 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 ntp amd64 1:4.2.8p10+dfsg-5ubuntu7.3 [640 kB]",
        "Get:3 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 sntp amd64 1:4.2.8p10+dfsg-5ubuntu7.3 [86.5 kB]",
        "Fetched 785 kB in 1s (1226 kB/s)",
        "Selecting previously unselected package libopts25:amd64.",
        "(Reading database ... ",
        "(Reading database ... 5%",
        "(Reading database ... 10%",
        "(Reading database ... 15%",
        "(Reading database ... 20%",
        "(Reading database ... 25%",
        "(Reading database ... 30%",
        "(Reading database ... 35%",
        "(Reading database ... 40%",
        "(Reading database ... 45%",
        "(Reading database ... 50%",
        "(Reading database ... 55%",
        "(Reading database ... 60%",
        "(Reading database ... 65%",
        "(Reading database ... 70%",
        "(Reading database ... 75%",
        "(Reading database ... 80%",
        "(Reading database ... 85%",
        "(Reading database ... 90%",
        "(Reading database ... 95%",
        "(Reading database ... 100%",
        "(Reading database ... 17274 files and directories currently installed.)",
        "Preparing to unpack .../libopts25_1%3a5.18.12-4_amd64.deb ...",
        "Unpacking libopts25:amd64 (1:5.18.12-4) ...",
        "Selecting previously unselected package ntp.",
        "Preparing to unpack .../ntp_1%3a4.2.8p10+dfsg-5ubuntu7.3_amd64.deb ...",
        "Unpacking ntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...",
        "Selecting previously unselected package sntp.",
        "Preparing to unpack .../sntp_1%3a4.2.8p10+dfsg-5ubuntu7.3_amd64.deb ...",
        "Unpacking sntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...",
        "Setting up libopts25:amd64 (1:5.18.12-4) ...",
        "Setting up sntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...",
        "Setting up ntp (1:4.2.8p10+dfsg-5ubuntu7.3) ...",
        "Created symlink /etc/systemd/system/network-pre.target.wants/ntp-systemd-netif.path -> /lib/systemd/system/ntp-systemd-netif.path.",
        "Created symlink /etc/systemd/system/multi-user.target.wants/ntp.service -> /lib/systemd/system/ntp.service.",
        "/usr/sbin/policy-rc.d returned 101, not running 'start ntp-systemd-netif.path'",
        "/usr/sbin/policy-rc.d returned 101, not running 'start ntp-systemd-netif.path ntp-systemd-netif.service'",
        "invoke-rc.d: policy-rc.d denied execution of start.",
        "Processing triggers for systemd (237-3ubuntu10.42) ...",
        "Processing triggers for libc-bin (2.27-3ubuntu1.2) ..."
    ]
}
```

To find the available modules, you can check out this [link](https://docs.ansible.com/ansible/2.8/modules/list_of_all_modules.html) or use the local help command-line tool.

```bash
ansible-doc -l
```

```bash
...[SNIP]...

add_host                                                                       Add a host (and alternatively a group) to the ansible-playbook in-memory inventory
amazon.aws.aws_az_facts                                                        Gather information about availability zones in AWS
amazon.aws.aws_az_info                                                         Gather information about availability zones in AWS
amazon.aws.aws_caller_facts                                                    Get information about the user and account being used to make AWS calls
amazon.aws.aws_caller_info                                                     Get information about the user and account being used to make AWS calls
amazon.aws.aws_s3                                                              manage objects in S3

...[SNIP]...
```

Let’s move to the **next step**.

### Challenge

1. Find a module that can send a file from DevSecOps-Box to remote machines
2. Use the **ansible-doc** command to see help examples
3. Create a file called **notes** and add any string into it, then copy this file into all remote machines (sandbox and production) using **ansible ad-hoc command**
 
> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

> Hint: Please explore [copy module from ansible](https://www.middlewareinventory.com/blog/ansible-ad-hoc-commands/#Example_20_ansible_ad_hoc_command_to_copy_file_Local_to_remote).

## Ansible playbook basics

### Learn how to create a simple playbook that sets up an application

In this scenario, you will learn how to install and run Ansible on a remote machine.

You will need to install the **Ansible** tool and create a simple playbook.

> This exercise provisions two machines, namely **DevSecOps Box** and **Sandbox** When you click on the start the exercise button.


### Install Ansible

> Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
>
> Source: [Ansible official website](https://www.ansible.com/)

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to install the Ansible program.

```
pip3 install ansible==2.10.4
```

Let’s move to the **next step**.

### Create the inventory file

We will learn Ansible basics by performing our experiments on these two machines. Let’s create the inventory or CMDB file for Ansible using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[sandbox]
sandbox-xIKciVAk

EOL
```

As Ansible uses SSH as its RPC mechanism, we will have to ensure the SSH’s **yes/no** prompt doesn’t wait for our input indefinitely while running the Ansible commands. To overcome this, we will be using the **ssh-keyscan** command to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
# devsecops-box-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s do this for the rest of the systems in this lab as well.

```bash
ssh-keyscan -t rsa sandbox-xIKciVAk >> ~/.ssh/known_hosts
# sandbox-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

> **Pro-tip**: Instead of running the **ssh-keyscan** command twice, we can achieve the same using the below command.

```bash
ssh-keyscan -t rsa sandbox-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
# devsecops-box-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
# sandbox-qopqfmfu:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s move to the **next step**.

### Create the playbook

Playbooks are a collection of Ansible tasks, you can run a playbook against a local or a remote machine. They help us in configuring machine(s) according to the requirements. Ansible uses the **YAML** format to declare the configurations.

Playbooks follow a directory layout. A sample playbook directory structure is shown below:

```yaml
tasks/              # task files included from playbooks
    01-install.yml  # task file to install something
    02-config.yml   # task file to copy configuration to remote machine
    main.yml        # master task playbook
roles/
    common/         # this hierarchy represents a "role"
vars/
    main.yml        # vars file to save variables
main.yml            # master playbook
```

> For more details, you can check out [Sample Directory Layout of Ansible](https://docs.ansible.com/ansible/latest/user_guide/sample_setup.html#sample-directory-layout).

We will create a simple playbook that will execute tasks like installing, configuring Nginx, and setting up the Django application on the **sandbox** machine.

Let’s create a directory to store our playbook.

```bash
mkdir simple-playbook && cd simple-playbook
```

Create the tasks directory to store tasks.

```bash
mkdir tasks
```

Next, we will create a file called main.yml that stores the needed tasks.

```bash
cat > tasks/main.yml <<EOL
---
- name: Install nginx
  apt:
    name: "nginx"
    state: present

- name: Copy the configuration
  template:
    src: templates/default.j2
    dest: /etc/nginx/sites-enabled/default

- name: Start nginx service
  service:
    name: nginx
    state: started
    enabled: yes

- name: Clone django repository
  git:
    repo: https://gitlab.practical-devsecops.training/pdso/django.nv.git
    dest: /opt/django

- name: Install dependencies
  command: pip3 install -r requirements.txt
  args:
    chdir: /opt/django

- name: Database migration
  command: python3 manage.py migrate
  args:
    chdir: /opt/django

- name: Load data from the fixtures
  command: python3 manage.py loaddata fixtures/*
  args:
    chdir: /opt/django

- name: Run an application in the background
  shell: nohup python3 manage.py runserver 0.0.0.0:8000 &
  args:
    chdir: /opt/django
EOL
```

Then, we will create a templates directory to save our configuration file.

```bash
mkdir templates
```

We will create a **default.j2** template file. This file gets copied to the remote machine to adjust the template variables according to the requirements.

```bash
cat > templates/default.j2 <<EOL
{% raw %}
server {
    listen      80;
    server_name localhost;

    access_log  /var/log/nginx/django_access.log;
    error_log   /var/log/nginx/django_error.log;

    proxy_buffers 16 64k;
    proxy_buffer_size 128k;

    location / {
        proxy_pass  http://localhost:8000;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
        proxy_redirect off;

        proxy_set_header    Host \$host;
        proxy_set_header    X-Real-IP \$remote_addr;
        proxy_set_header    X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header    X-Forwarded-Proto http;
    }
}
{% endraw %}
EOL
```

Let’s create a **main.yml** playbook file that uses the main.yml under the tasks folder.

```bash
cat > main.yml <<EOL
---
- name: Simple playbook
  hosts: sandbox
  remote_user: root
  gather_facts: no

  tasks:
  - include: tasks/main.yml
EOL
```

So far, we have created two directories and three files. We can check out our current directory layout using the tree command.

```bash
tree
simple-playbook# tree
.
├── main.yml
├── tasks
│   └── main.yml
└── templates
    └── default.j2

2 directories, 3 files
```

Let’s move to the **next step**.

### Run the Ansible playbook

Let’s run our playbook using the following command.

```bash
ansible-playbook -i inventory.ini main.yml
WARNING]: Unable to parse /simple-playbook/inventory.ini as an inventory source
[WARNING]: No inventory was parsed, only implicit localhost is available
[WARNING]: provided hosts list is empty, only localhost is available. Note that
the implicit localhost does not match 'all'
[WARNING]: Could not match supplied host pattern, ignoring: sandbox

PLAY [Simple playbook] *********************************************************
skipping: no hosts matched

PLAY RECAP *********************************************************************
```

>Oops, why our playbook didn’t execute? Can you look at the output and figure out why?

It’s simple! Ansible couldn’t find the inventory.ini file. This file is needed to marry the host groups and individual machines in it. Let’s move the inventory.ini file we created before to our current directory.

```bash
mv ../inventory.ini .
```

Try running the above ansible command once again.

```bash
ansible-playbook -i inventory.ini main.yml
PLAY [Simple playbook] *********************************************************

TASK [Install nginx] ***********************************************************
[WARNING]: Updating cache and auto-installing missing dependency: python-apt
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host sandbox-qjllvwyu
should use /usr/bin/python3, but is using /usr/bin/python for backward
compatibility with prior Ansible releases. A future Ansible release will
default to using the discovered platform python for this host. See https://docs
.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for
more information. This feature will be removed in version 2.12. Deprecation
warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
changed: [sandbox-qjllvwyu]

TASK [Copy the configuration] **************************************************
changed: [sandbox-qjllvwyu]

TASK [Start nginx service] *****************************************************
changed: [sandbox-qjllvwyu]

TASK [Clone django repository] *************************************************
changed: [sandbox-qjllvwyu]

TASK [Install dependencies] ****************************************************
changed: [sandbox-qjllvwyu]

TASK [Database migration] ******************************************************
changed: [sandbox-qjllvwyu]

TASK [Load data from the fixtures] *********************************************
changed: [sandbox-qjllvwyu]

TASK [Run an application in the background] ************************************
changed: [sandbox-qjllvwyu]

PLAY RECAP *********************************************************************
sandbox-qjllvwyu           : ok=8    changed=8    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

Once the Ansible playbook finishes, you will notice that the output has **changed=8**.

> What does it mean? Why is it important? What can we do with it?

The **changed=8** specifics that ansible made 8 changes to the system. What would it mean if changed=0? It would mean there were no changes. Since we ran about 8 tasks, we can assume each task made a change to the system, and we got our changed=8 as the output.

Let’s verify if nginx was installed, started, and properly deployed. We will use SSH to log into the sandbox machine.

```bash
ssh root@sandbox-xIKciVAk
```

Check if nginx is installed or not.

```bash
which nginx
/usr/sbin/nginx
```

Looks good. Next, let’s list all listening ports to check **nginx (port 80)** and **django (port 8000)** are being used.

```bash
netstat -tlpn
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 0.0.0.0:80              0.0.0.0:*               LISTEN      1856/nginx: master
tcp        0      0 127.0.0.53:53           0.0.0.0:*               LISTEN      447/systemd-resolve
tcp        0      0 0.0.0.0:22              0.0.0.0:*               LISTEN      742/sshd
tcp        0      0 127.0.0.11:39355        0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:8000            0.0.0.0:*               LISTEN      1994/python3
```

Perfect, we see port 80 is being used by nginx and 8000 by Django application.

Lets exit from the sandbox machine and head over to the DevSecOps-Box.

```bash
exit
```

Now, let’s verify our Django application is indeed serving the requests using the curl command.

```bash
curl sandbox-xIKciVAk
<!DOCTYPE html>
<html>
    <head>
    <title>Task Manager</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Styles
    ================================================== -->

    <link rel="stylesheet" href="/static/taskManager/css/bootstrap.css"/>
    <link rel="stylesheet" href="/static/taskManager/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/taskManager/css/font-awesome.css"/>
    <link rel="stylesheet" href="/static/taskManager/css/style.css"/>

...[SNIP]...

                    <p>e: <a href="javascript:;">info@tm.com</a></p>
                </address>
            </div>

            <div class="col-lg-2 col-sm-2 col-lg-offset-1">
                <h1>Our Mission</h1>
                <p>Making life easier, one task at a time.</p>
            </div>

        </div>
    </div>
</footer>
<!--footer end-->

<!-- End Footer -->

<!-- Javascript
================================================== -->
    <!--<script src="/static/taskManager/js/bootstrap.min.js" ></script>
    <script src="http://code.jquery.com/jquery-latest.js"></script>-->

</body>
```

Good, it seems our automation did work.

Let’s move to the **next step.**

### Challenge

1. Do you think it made sense for us to use Jinja 2 extension file as our templating file? Please look at the file **templates/default.j2** and make a call
2. Can we still execute the playbook without **gather_facts** syntax in **main.yml**?
3. Try to execute the playbook once again, and explain why the output is different than before, especially **changed=** field?

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Ansible conditionals

### Learn how to use Ansible conditionals to make decisions in the playbook

In this scenario, you will learn how to install Ansible and use conditionals statements in the playbook.

### Install Ansible

> Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
> 
> Source: [Ansible official website](https://www.ansible.com/)

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to install the **Ansible** program.

```bash
pip3 install ansible==2.10.4
```

Let’s move to the **next step**.

### Create the inventory file

As we’ve learned in the **Ansible Ad-hoc Commands** exercise, we need to create an inventory file to save our remote hosts to be used by Ansible. Let’s create the file using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[sandbox]
sandbox-xIKciVAk

EOL
```

As Ansible uses SSH as its RPC mechanism, we will have to ensure the SSH’s yes/no prompt doesn’t wait for our input indefinitely while running the Ansible commands. To overcome this, we will be using the ssh-keyscan command to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa sandbox-xIKciVAk >> ~/.ssh/known_hosts
# sandbox-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s do this for the rest of the systems in this lab as well.

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
# devsecops-box-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

>**Pro-tip**: Instead of running the **ssh-keyscan** command twice, we can achieve the same using the below command.

```bash
ssh-keyscan -t rsa sandbox-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

Let’s move to the next step.

### when

In Ansible, we can use conditionals to run a single task or multiple tasks in a playbook. Before doing this exercise, you should be familiar with Ad-hoc commands and Ansible Playbook to easily understand conditionals.

Let’s start by creating a simple task to use **when** statement in the playbook.

```bash
cat > main.yml <<EOL
---
- name: Simple playbook
  hosts: all
  remote_user: root
  gather_facts: yes     # what does it means?

  tasks:
  - debug:
      msg: "This system uses Ubuntu-based distro"
    when: ansible_distribution == "Ubuntu"
EOL
```

In the above content, we just created one task to print the message in the terminal if the remote hosts are using Ubuntu-based distribution.

> How did you get ansible_distribution as conditions?
>
>We can use the setup module in ansible to discover variables about a system.
>
>execute this command to use setup module as ad-hoc commands:
>
> ```bash
> ansible -i inventory.ini all -m setup
> ```

Let’s run our playbook using the following command.

```bash
ansible-playbook -i inventory.ini main.yml
PLAY [Simple playbook] ****************************************************************************************************************************

TASK [Gathering Facts] ****************************************************************************************************************************
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host devsecops-box-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compatibility with prior Ansible releases. A future Ansible release will default to using the discovered platform python for this
host. See https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version 2.12. Deprecation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [devsecops-box-JnOHfNpe]
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host sandbox-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compatibility with prior Ansible releases. A future Ansible release will default to using the discovered platform python for this host.
See https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version 2.12. Deprecation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [sandbox-JnOHfNpe]

TASK [debug] ****************************************************************************************************************************
ok: [devsecops-box-JnOHfNpe] => {
    "msg": "This system uses Ubuntu-based distro"
}
ok: [sandbox-JnOHfNpe] => {
    "msg": "This system uses Ubuntu-based distro"
}

PLAY RECAP ****************************************************************************************************************************
devsecops-box-JnOHfNpe     : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
sandbox-JnOHfNpe           : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

As expected, we’ve got the output because the remote hosts are Ubuntu-based, and if the condition is incorrect, it will skip the task, which in our case will result in simply not printing the message output.

Let’s try to change the value of our when condition from **Ubuntu** to **CentOS**.

```bash
cat > main.yml <<EOL
---
- name: Simple playbook
  hosts: all
  remote_user: root
  gather_facts: yes     # what does it mean?

  tasks:
  - debug:
      msg: "This system is used CentOS-based distro"
    when: ansible_distribution == "CentOS"
EOL
```

```bash
ansible-playbook -i inventory.ini main.yml
PLAY [Simple playbook] ****************************************************************************************************************************
 
TASK [Gathering Facts] ****************************************************************************************************************************
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host devsecops-box-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compatibility with prior Ansible releases. A future Ansible release will default to using the discovered platform python for this
host. See https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version 2.12. Deprecation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [devsecops-box-JnOHfNpe]
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host sandbox-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compatibility with prior Ansible releases. A future Ansible release will default to using the discovered platform python for this host.
See https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version 2.12. Deprecation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [sandbox-JnOHfNpe]
 
TASK [debug] ****************************************************************************************************************************
skipping: [devsecops-box-JnOHfNpe]
skipping: [sandbox-JnOHfNpe]
 
PLAY RECAP ****************************************************************************************************************************
devsecops-box-JnOHfNpe     : ok=1    changed=0    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
sandbox-JnOHfNpe           : ok=1    changed=0    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
```

As you can see above, a task is skipped because we changed the value in the when condition from **Ubuntu** to **CentOS**. Apart from this, you can use the **when** statement on each Ansible module and re-use it as a variable for desired tasks.

Let’s explore how in the **next step**.

### register

**register** statement is used to save a task as a new variable that can be used for the desired tasks. To understand how it works, let’s replace the previous **main.yml** file by executing the following command.

```bash
cat > main.yml <<EOL
---
- name: Simple playbook
  hosts: all
  remote_user: root
  gather_facts: no     # what does it mean?

  tasks:
  - name: Show the content of /etc/os-release
    command: cat /etc/os-release
    register: os_release

  - debug:
      msg: "This system uses Ubuntu-based distro"
    when: os_release.stdout.find('Ubuntu') != -1
EOL
```

> If you curious why we use **-1**, check out this [link](https://docs.python.org/2/library/string.html#string.find).

The first task uses the **command** module to execute **cat /etc/os-release** command and if you execute **cat /etc/os-release** on the DevSecOps box, the output is as below.

```bash
NAME="Ubuntu"
VERSION="18.04.5 LTS (Bionic Beaver)"
ID=ubuntu
ID_LIKE=debian
PRETTY_NAME="Ubuntu 18.04.5 LTS"
VERSION_ID="18.04"
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
VERSION_CODENAME=bionic
UBUNTU_CODENAME=bionic
```

We will use the above output in the next task to find **Ubuntu** string.

```bash
when: os_release.stdout.find('Ubuntu') != -1
```

You can see two properties in the syntax above, there is **stdout** (output of the command) and **find** (search for particular strings). We use it in a conditional statement to print a message in the terminal when the string **Ubuntu** is found.

Then, run the playbook once again to review the expected output.

```bash
ansible-playbook -i inventory.ini main.yml
PLAY [Simple playbook] **************************************************************************************************************************************

TASK [Gathering Facts] ****************************************************************************************************************************
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host devsecops-box-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compat
ibility with prior Ansible releases. A future Ansible release will default to using the discovered platform python for this 
host. See https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version
 2.12. Deprecation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.ok: [devsecops-box-JnOHfNpe]
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host sandbox-JnOHfNpe should use /usr/bin/python3, but is using /usr/bin/python for backward compatibility w
ith prior Ansible releases. A future Ansible release will default to using the discovered platform python for this host. See 
https://docs.ansible.com/ansible/2.10/reference_appendices/interpreter_discovery.html for more information. This feature will be removed in version 2.12. Dep
recation warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [sandbox-JnOHfNpe]

TASK [Show the content of /etc/os-release] ******************************************************************************************************************

changed: [devsecops-box-JnOHfNpe]
changed: [sandbox-JnOHfNpe]

TASK [debug] ************************************************************************************************************************************************

ok: [devsecops-box-JnOHfNpe] => {
    "msg": "This system uses Ubuntu-based distro"
}
ok: [sandbox-JnOHfNpe] => {
    "msg": "This system uses Ubuntu-based distro"
}

PLAY RECAP **************************************************************************************************************************************************

devsecops-box-JnOHfNpe     : ok=3    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
sandbox-JnOHfNpe              : ok=3    changed=1    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0   
```

Let’s move to the **next step**.

### Challenge

1. Create a new playbook that contains tasks to check **nginx** package is installed or not
2. If **nginx** is installed, print **nginx** version to the terminal by using the **msg** module
3. Otherwise, let your playbook install the missing **nginx** using the **apt** module

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Container deployment using Ansible

### Learn how to use Ansible to deploy the container

In this scenario, you will learn how to deploy an app on a production machine using Ansible.

You will install the **Ansible** tool, run the Ansible ad-hoc command, and playbook against the remote machine.

### Install Ansible

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

>Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
>
>Website: [Ansible official site](https://www.ansible.com/).

First, we need to install the **ansible** programs.

```bash
pip3 install ansible==2.10.4
```

Let’s move to the **next step**.

### Create the inventory file

Let’s create the inventory or CMDB file for ansible using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[prod]
prod-xIKciVAk ansible_python_interpreter=/usr/bin/python3

EOL
```

> Did you see **ansible_python_interpreter=/usr/bin/python3?** figure out why its used

Next, we will have to ensure the SSH’s **yes/no** prompt is not shown while running the ansible commands, so we will be using **ssh-keyscan** to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa prod-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

Let’s move to the next step.

### Run the ansible commands

Let’s run the **ansible ad-hoc command** to check if the production machine has **docker** installed.

Let’s use the shell module of ansible to run the **docker version** command on the production machine.

```bash
ansible -i inventory.ini prod -m shell -a "docker version"
prod-l3oVRKu3 | CHANGED | rc=0 >>
Client: Docker Engine - Community
 Version:           19.03.13
 API version:       1.40
 Go version:        go1.13.15
 Git commit:        4484c46d9d
 Built:             Wed Sep 16 17:02:36 2020
 OS/Arch:           linux/amd64
 Experimental:      false
 
Server: Docker Engine - Community
 Engine:
  Version:          19.03.13
  API version:      1.40 (minimum version 1.12)
  Go version:       go1.13.15
  Git commit:       4484c46d9d
  Built:            Wed Sep 16 17:01:06 2020
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.3.7
  GitCommit:        8fba4e9a7d01810a393d5d25a3621dc101981175
 runc:
  Version:          1.0.0-rc10
  GitCommit:        dc9208a3303feef5b3839f4323d9beb36df0a9dd
 docker-init:
  Version:          0.18.0
  GitCommit:        fec3683
```

As you can see, we got the output of **docker version** from the production machine.

Let’s move to the **next step**.

### Build the image

Before we deploy the container, we need to build the docker image first and store it in the docker registry.

Let’s SSH into the production machine.

```bash
ssh root@prod-xIKciVAk
```

Next, clone django.nv from git repository.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git
```

Let’s cd into the django.nv directory so we can run the application.

```bash
cd django.nv
```

Next, let’s create the django.nv application’s docker image.

```bash
docker build -t registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1 .
```

> Please refer to [Docker Registry exercise](https://portal.practical-devsecops.training/courses/devsecops-professional/docker/docker-registry) if you are not comfortable working with Docker registry.

We’re tagging the image with the registry location and the version(**1.1**). In the above command, we are using the shorthand instead of running two different commands, i.e., docker build and docker tag.

```bash
Sending build context to Docker daemon  4.013MB
Step 1/8 : FROM python:2-alpine
 ---> 8579e446340f
Step 2/8 : COPY . /app
 ---> 1a0431368169
Step 3/8 : WORKDIR /app
 ---> Running in 2db05726c9bf
Removing intermediate container 2db05726c9bf
 ---> 524661e2344b
Step 4/8 : RUN apk add --no-cache gawk sed bash grep bc coreutils nano
 ---> Running in 3493bfd20b4a
fetch http://dl-cdn.alpinelinux.org/alpine/v3.11/main/x86_64/APKINDEX.tar.gz
fetch http://dl-cdn.alpinelinux.org/alpine/v3.11/community/x86_64/APKINDEX.tar.gz
(1/11) Installing bash (5.0.11-r1)
Executing bash-5.0.11-r1.post-install
(2/11) Installing bc (1.07.1-r1)
(3/11) Installing libacl (2.2.53-r0)

...[SNIP]...

  Applying taskManager.0036_auto_20150921_2024... OK
  Applying taskManager.0037_auto_20150921_2025... OK
  Applying taskManager.0038_auto_20150921_2027... OK
Installed 48 object(s) from 5 fixture(s)
Removing intermediate container ee3fdcbadd67
 ---> e4b2ad6cd5ac
Step 7/8 : EXPOSE 8000
 ---> Running in 35a18999bbfe
Removing intermediate container 35a18999bbfe
 ---> 3484099ad1f9
Step 8/8 : CMD ["/app/run_app_docker.sh"]
 ---> Running in 2faee746a0fe
Removing intermediate container 2faee746a0fe
 ---> 88efbaa9490b
Successfully built 88efbaa9490b
Successfully tagged registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1
```

After the build finishes, we can push the image into the registry using the following command.

```bash
docker push registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1
The push refers to repository [registry-xIKciVAk.lab.practical-devsecops.training/django.nv]
e362b2fe9a66: Preparing
324f6735cbd8: Preparing
fbca1c68a5ab: Preparing
a89e7ad73916: Preparing
879c0d8666e3: Preparing
20a7b70bdf2f: Preparing
3fc750b41be7: Preparing
beee9f30bc1f: Preparing
20a7b70bdf2f: Waiting
3fc750b41be7: Waiting
beee9f30bc1f: Waiting
no basic auth credentials
```

If you try to push this image, you will get an error **no basic auth credentials** as we are not authenticated to the registry. Let’s use the **docker login** command to login.

```bash
docker login --username root registry-xIKciVAk.lab.practical-devsecops.training
```

And type pdso-training as the password to login

```bash
Login Succeeded
WARNING! Your password will be stored unencrypted in /root/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store
```

If you try to rerun the previous command, you will see that the image push did complete.

Exit from the production machine.

```bash
exit
```

Let’s move to the next step.

### Run the Ansible playbook

Let’s create a playbook to deploy our newly created **django.nv** docker image to the production environment.

```bash
cat > playbook.yml <<EOL
---
- name: Deploy container using Ansible
  hosts: prod
  remote_user: root
  gather_facts: no

  vars:
    state: present

  tasks:
  - name: Ensure docker is installed
    stat:
      path: "/usr/bin/docker"
    register: docker_result

  - debug:
      msg: "Please install the docker"
    when: not docker_result.stat.exists

  - name: Pull new image
    docker_image:
      name: "registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1"
      source: pull

  - name: Stopped container
    docker_container:
      name: "django.nv"
      state: absent

  - name: Run a new container
    docker_container:
      name: "django.nv"
      image: "registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1"
      detach: yes
      ports:
        - 8000:8000
EOL
```

> Reference: https://docs.ansible.com/ansible/latest/scenario_guides/guide_docker.html.

Lets run this playbook against the prod machine.

```bash
ansible-playbook -i inventory.ini playbook.yml
```

```yml
PLAY [Deploy container using Ansible] ******************************************

TASK [Ensure docker is installed] **********************************************
ok: [prod-STUDENT_ID]

TASK [debug] *******************************************************************
skipping: [prod-STUDENT_ID]

TASK [Pull new image] **********************************************************
fatal: [prod-STUDENT_ID]: FAILED! => {"changed": false, "msg": "Failed to import the required Python library (Docker SDK for Python: docker (Python >= 2.7) or docker-py (Python 2.6)) on prod-STUDENT_ID's Python /usr/bin/python. Please read the module documentation and install it in the appropriate location. If the required library is installed, but Ansible is using the wrong Python interpreter, please consult the documentation on ansible_python_interpreter, for example via `pip install docker` or `pip install docker-py` (Python 2.6). The error was: No module named requests.exceptions"}

PLAY RECAP *********************************************************************
prod-STUDENT_ID              : ok=1    changed=0    unreachable=0    failed=1    skipped=1    rescued=0    ignored=0
```

The output shows us an error about Python module called **docker-py**, we need to install it in our production machine. SSH again into the production machine.

```bash
ssh root@prod-xIKciVAk
```

Then install the python library.

```bash
pip3 install docker-py
Collecting docker-py
  Downloading https://files.pythonhosted.org/packages/23/c7/1fd6d4d620809fe2f323869d719e2dd0086c939b67021303a9ec40f5a05b/docker_py-1.10.6-py2.py3-none-any.wh
l (50kB)
    100% |████████████████████████████████| 51kB 8.1MB/s 
Collecting docker-pycreds>=0.2.1 (from docker-py)
  Downloading https://files.pythonhosted.org/packages/f5/e8/f6bd1eee09314e7e6dee49cbe2c5e22314ccdb38db16c9fc72d2fa80d054/docker_pycreds-0.4.0-py2.py3-none-an
y.whl
Collecting six>=1.4.0 (from docker-py)
  Downloading https://files.pythonhosted.org/packages/ee/ff/48bde5c0f013094d729fe4b0316ba2a24774b3ff1c52d924a8a4cb04078a/six-1.15.0-py2.py3-none-any.whl
Collecting websocket-client>=0.32.0 (from docker-py)
  Downloading https://files.pythonhosted.org/packages/08/33/80e0d4f60e84a1ddd9a03f340be1065a2a363c47ce65c4bd3bae65ce9631/websocket_client-0.58.0-py2.py3-none
-any.whl (61kB)
    100% |████████████████████████████████| 61kB 8.5MB/s 
Collecting requests!=2.11.0,>=2.5.2 (from docker-py)
  Downloading https://files.pythonhosted.org/packages/29/c1/24814557f1d22c56d50280771a17307e6bf87b70727d975fd6b2ce6b014a/requests-2.25.1-py2.py3-none-any.whl
 (61kB)
    100% |████████████████████████████████| 61kB 11.0MB/s 
Collecting urllib3<1.27,>=1.21.1 (from requests!=2.11.0,>=2.5.2->docker-py)
  Downloading https://files.pythonhosted.org/packages/09/c6/d3e3abe5b4f4f16cf0dfc9240ab7ce10c2baa0e268989a4e3ec19e90c84e/urllib3-1.26.4-py2.py3-none-any.whl 
(153kB)
    100% |████████████████████████████████| 153kB 8.0MB/s 
Collecting idna<3,>=2.5 (from requests!=2.11.0,>=2.5.2->docker-py)
  Downloading https://files.pythonhosted.org/packages/a2/38/928ddce2273eaa564f6f50de919327bf3a00f091b5baba8dfa9460f3a8a8/idna-2.10-py2.py3-none-any.whl (58kB
)
    100% |████████████████████████████████| 61kB 6.3MB/s 
Collecting chardet<5,>=3.0.2 (from requests!=2.11.0,>=2.5.2->docker-py)
  Downloading https://files.pythonhosted.org/packages/19/c7/fa589626997dd07bd87d9269342ccb74b1720384a4d739a1872bd84fbe68/chardet-4.0.0-py2.py3-none-any.whl (
178kB)
    100% |████████████████████████████████| 184kB 5.8MB/s 
Collecting certifi>=2017.4.17 (from requests!=2.11.0,>=2.5.2->docker-py)
  Downloading https://files.pythonhosted.org/packages/5e/a0/5f06e1e1d463903cf0c0eebeb751791119ed7a4b3737fdc9a77f1cdfb51f/certifi-2020.12.5-py2.py3-none-any.w
hl (147kB)
    100% |████████████████████████████████| 153kB 8.0MB/s 
Installing collected packages: six, docker-pycreds, websocket-client, urllib3, idna, chardet, certifi, requests, docker-py
Successfully installed certifi-2020.12.5 chardet-4.0.0 docker-py-1.10.6 docker-pycreds-0.4.0 idna-2.10 requests-2.25.1 six-1.15.0 urllib3-1.26.4 websocket-cl
ient-0.58.0
```

```bash
exit
```

If we execute the previous ansible command, we will see the following output.

```bash
ansible-playbook -i inventory.ini playbook.yml
PLAY [Deploy container using Ansible] ******************************************

TASK [Ensure docker is installed] **********************************************
ok: [prod-STUDENT_ID]

TASK [debug] *******************************************************************
skipping: [prod-STUDENT_ID]

TASK [Pull new image] **********************************************************
ok: [prod-STUDENT_ID]

TASK [Stopped container] *******************************************************
[DEPRECATION WARNING]: The container_default_behavior option will change its
default value from "compatibility" to "no_defaults" in community.general 3.0.0.
 To remove this warning, please specify an explicit value for it now. This
feature will be removed from community.general in version 3.0.0. Deprecation
warnings can be disabled by setting deprecation_warnings=False in ansible.cfg.
ok: [prod-STUDENT_ID]

TASK [Run a new container] *****************************************************
changed: [prod-STUDENT_ID]

PLAY RECAP *********************************************************************
prod-STUDENT_ID              : ok=4    changed=1    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
```

You can verify the new image is deployed on the production using the **ansible ad-hoc command**.

```bash
ansible -i inventory.ini prod -m shell -a "docker ps"
prod-xIKciVAk | CHANGED | rc=0 >>
CONTAINER ID        IMAGE                                                                  COMMAND                  CREATED             STATUS
    PORTS                    NAMES
c40eb809a416        registry-xIKciVAk.lab.practical-devsecops.training/django.nv:1.1   "/app/run_app_docker…"   36 seconds ago      Up 35 seconds       0.0.0.0:8000->8000/tcp   django.nv
```

Awesome! we managed to deploy an app to another machine.

> Think, how this would look like in a CI/CD setup.


## Learn how to embed Ansible in the Jenkins CI/CD pipeline

### Use Ansible tool to achieve compliance automation in Jenkins CI/CD pipeline

In this scenario, you will learn how to embed Ansible in the Jenkins CI/CD pipeline.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab and Jenkins machine to start.

> Remember!
>
>1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working
>
>3. We have already set up the Jenkins machine with several plugins to help you do the exercise

### Create a new job

> The Jenkins system is already configured with GitLab. If you wish to know **how to configure Jenkins with GitLab**, you can check out this link.

We will create a new job in Jenkins by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob.

You can use the following details to log into Jenkins.

Name	Value
**Username**	root
**Password**	pdso-training

Provide a name for your new item, e.g., django.nv, select the **Pipeline** option, and click on the **OK** button.

In the next screen, click on the **Build Triggers** tab, check the **Build when a change is pushed to GitLab**..... checkbox.

At the bottom right-hand side, just below the **Comment (regex) for triggering a build** form field, you will find the **Advanced**... button. Please click on it.

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

```bash
Failed to connect to repository : Command "git ls-remote -h -- http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv HEAD" returned status code 128:
stdout:
stderr: remote: HTTP Basic: Access denied
fatal: Authentication failed for 'http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git/'
```

Let’s add the credentials by clicking on the Add button (the one with a key symbol). Select the Jenkins option and fill the pop-up form with the following details.

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
```

We do have four stages in this pipeline, **build**, **test**, **integration** and **prod**. Assuming you do not understand python or any programming language, we can safely consider the DevOps team is building and testing the code.

Let’s login into Gitlab using the following details.

Name	Value
**Gitlab URL**	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
**Username**	root
**Password**	pdso-training

Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

Since we want to use Jenkins to execute the CI/CD jobs, we need to remove **.gitlab-ci.yml** from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the **Gitlab Runner** and the **Jenkins** systems.

>Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the **.gitlab-ci.yaml** file is missing.
>
>Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Let’s move to the **next step**.

### Embed Ansible in Jenkins

> Remember!
>
>1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

We need to add credentials into Jenkins, namely, SSH Private Key and SSH hostname of the machine we are securing, i.e., production machine. Since we don’t want the credentials to be hardcoded in the **Jenkinsfile**. Let’s add the creds to the Jenkins credential store by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/credentials/store/system/domain/_/.

Click on the **Add Credentials** link on the left sidebar, and select **SSH Username with private key** as **Kind**, then add the following credentials into it.

Name	Value
**ID**	ssh-prod
**Description**	Credentials to login into Production machine
**Username**	root
**Private Key**	check **Enter directly**, click the **Add** button and copy the private key from Production machine, available at **/root/.ssh/id_rsa**
**Passphrase**	leave it blank because we want this process to be automatic without any human intervention. If you desire more robust credential security mechanism, please use dedicated secret management systems like **Hashicorp Vault**.

Once done, click the **OK** button.

Add another credential to save SSH hostname, so it’s not hardcoded in the **Jenkinsfile**, then select **Secret text** as **Kind**, then add the following credentials into it.

Name	Value
**Secret**	prod-xIKciVAk
**ID**	prod-server

Next, go back to the GitLab tab and append the following code in the **Jenkinsfile** after the prod stage.

```groovy
        stage("ansible-hardening"){
            agent {
                docker {
                    image 'willhallonline/ansible:2.9-ubuntu-18.04'
                    args '-u root'
                }
            }
            steps {
                sshagent(['ssh-prod']) {
                    withCredentials([string(credentialsId: 'prod-server', variable: 'DEPLOYMENT_SERVER')]) {
                        sh """
                        echo "[prod]\n$DEPLOYMENT_SERVER" >> inventory.ini
                        ansible-galaxy install dev-sec.os-hardening
                        ansible-playbook -i inventory.ini ansible-hardening.yml
                        """
                    }
                }
            }
        }
```

Save changes to the file using the **Commit changes** button.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

Oh, it failed? Why? We also got the following helpful message in the CI output.

>+ ansible-playbook -i inventory.ini ansible-hardening.yml
>ERROR! the playbook: ansible-hardening.yml could not be found
>script returned exit code 1

That makes sense. We didn’t upload the **ansible-hardening.yml** to the git repository.

Let’s copy the hardening script.

```yaml
---
- name: Playbook to harden the Ubuntu OS.
  hosts: prod
  remote_user: root
  become: yes

  roles:
    - dev-sec.os-hardening
```

Visit the add new file URL https://gitlab-ce-xIKciVAklab.practical-devsecops.training/root/django-nv/-/new/master/.

>If you are comfortable with the Git command line, please use git add, git commit, and git push commands.

**Paste the above ansible script** into the space provided.

Ensure you name the file as

```bash
ansible-hardening.yml
```

Save changes to the repo using the **Commit changes** button.

We can see the results by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.

There you have it. We ran ansible hardening locally first and then embedded it into a CI/CD pipeline.

## Harden machines in CI/CD pipelines

### Learn how to embed hardening in CI/CD pipelines

In this scenario, you will learn how to install, run and embed Ansible on a remote machine.

You will need to install the **Ansible tool** and then finally run the Ansible playbook against the remote machine using **CI/CD pipeline**.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

> Remember!
>
>1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

### Install Ansible

>Ansible uses simple English like language to automate configurations, settings, and deployments in traditional and cloud environments. It’s easy to learn and can be understood by even non-technical folks.
>
>Source: [Ansible official website](https://www.ansible.com/)

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to install the **Ansible** program.

```bash
pip3 install ansible==2.10.4
```

Let’s move to the **next step**.

### Create the inventory file

Let’s create the inventory or CMDB file for Ansible using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[gitservers]
gitlab-ce-xIKciVAk

[prod]
prod-xIKciVAk
EOL
```

Next, we will have to ensure the SSH’s **yes/no** prompt is not shown while running the ansible commands, so we will be using **ssh-keyscan** to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa prod-xIKciVAk >> ~/.ssh/known_hosts
```

```bash
# prod-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s do this for the rest of the system’s in the lab as well.

```bash
ssh-keyscan -t rsa gitlab-ce-xIKciVAk >> ~/.ssh/known_hosts
```

```bash
# gitlab-ce-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

```bash
# devsecops-box-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

> **Pro-tip**: Instead of running the **ssh-keyscan** command thrice, we can achieve the same result using the below command.

```bash
ssh-keyscan -t rsa prod-xIKciVAk gitlab-ce-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

Let’s move to the **next step**.

### Harden the production environment

> Dev-Sec Project has lots of good examples on how to create Ansible roles and uses lots of best practices which we can use as a baseline in our roles.
>
>For example, https://github.com/dev-sec/ansible-os-hardening.

We will choose the **dev-sec.os-hardening** role from the dev-sec project to harden our production environment.

```bash
ansible-galaxy install dev-sec.os-hardening
```

Let’s create a playbook to use this role against a remote machine.

```bash
cat > ansible-hardening.yml <<EOL
---
- name: Playbook to harden Ubuntu OS.
  hosts: prod
  remote_user: root
  become: yes

  roles:
    - dev-sec.os-hardening

EOL
```

Let’s run this playbook against the prod machine to harden it.

```bash
ansible-playbook -i inventory.ini ansible-hardening.yml
```

Once the playbook runs, we should see the output as shown below.

```bash
ansible-playbook -i inventory.ini ansible-hardening.yml

PLAY [Playbook to harden ubuntu OS.] *******************************************

TASK [Gathering Facts] *********************************************************
ok: [prod-sxfnfgse]

TASK [dev-sec.os-hardening : Set OS family dependent variables] ****************
ok: [prod-sxfnfgse]

TASK [dev-sec.os-hardening : Set OS dependent variables] ***********************

TASK [dev-sec.os-hardening : install auditd package | package-08] **************
changed: [prod-sxfnfgse]

TASK [dev-sec.os-hardening : configure auditd | package-08] ********************
changed: [prod-sxfnfgse]

TASK [dev-sec.os-hardening : find files with write-permissions for group] ******
ok: [prod-sxfnfgse] => (item=/usr/local/sbin)
ok: [prod-sxfnfgse] => (item=/usr/local/bin)
ok: [prod-sxfnfgse] => (item=/usr/sbin)
ok: [prod-sxfnfgse] => (item=/usr/bin)
ok: [prod-sxfnfgse] => (item=/sbin)
ok: [prod-sxfnfgse] => (item=/bin)

...[SNIP]...

PLAY RECAP *********************************************************************
prod-sxfnfgse              : ok=40   changed=19   unreachable=0    failed=0    skipped=27   rescued=0    ignored=0
```

As we can see, there were **19 changes (changed=19)** made to the production machine while hardening.

> **Pro-tip**: try re-running the above command and see what happens to **changed=19**.
>
>Can we put it into CI? Yes, why not?

Let’s do it in the **next step** of the exercise.

### Running ansible role as part of the CI pipeline.

Let’s login into the GitLab and configure production machine https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd.

We can use the GitLab credentials provided below to login i.e.,

Name	Value
**Username**	root
**Password**	pdso-training

Click on the **Expand** button under the **Variables** section, then click the **Add Variable** button.

Add the following key/value pair in the form.

Name	Value
**Key**	DEPLOYMENT_SERVER
**Value**	prod-xIKciVAk

Name	Value
**Key**	DEPLOYMENT_SERVER_SSH_PRIVKEY
**Value**	Copy the private key from the production machine using SSH. The SSH key is available at **/root/.ssh/id_rsa**. Please refer to Advanced Linux Exercises for a refresher on SSH Keys

```bash
# Ensure the lines -----BEGIN RSA PRIVATE KEY----- and -----END RSA PRIVATE KEY----- are also copied
more /root/.ssh/id_rsa
```

Finally, Click on the button **Add Variable**.

Next, please visit https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml.

Click on the **Edit** button and append the following code to the **.gitlab-ci.yml** file.

```yaml
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

> With the echo command, we are simply copying the contents of the private key variable stored in Gitlab CI into the id_rsa file under ~/.ssh inside the container.
> eval runs the command ssh-agent in the background and sends the key whenever SSH asks for a key in an automated fashion.

Save changes to the file using the **Commit changes** button.

> Don’t forget to set **DEPLOYMENT_SERVER** variable under Settings (Project Settings > CI/CD > Variables > Expand > Add Variable), otherwise your build will fail

We can see the results by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.

Oh, it failed? Why? We also got the following helpful message in the CI output.

```bash
$ ansible-playbook -i inventory.ini ansible-hardening.yml
 ERROR! the playbook: ansible-hardening.yml could not be found
 ERROR: Job failed: exit status 1
```

That makes sense. We didn’t upload the **ansible-hardening.yml** to the git repository yet.

Let’s copy the hardening script.

```yml
---
- name: Playbook to harden ubuntu OS.
  hosts: prod
  remote_user: root
  become: yes

  roles:
    - dev-sec.os-hardening
```

Visit the add new file URL https://gitlab-ce-xIKciVAklab.practical-devsecops.training/root/django-nv/-/new/master/

**Paste the above ansible script** into the space provided. Ensure you name the file as ansible-hardening.yml.

Save changes to the repo using the **Commit changes** button.

We can see the results by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.

There you have it. We ran ansible hardening locally first and then embedded it into a CI/CD pipeline.

> If you see **Permisison Denied, Enter passphrase for /root/.ssh/id_rsa** or any other SSH related issue, then you have to ensure a proper key is copied in the CI/CD variable **DEPLOYMENT_SERVER_SSH_PRIVKEY**.

## Use TFLint to find security issues in IaC

### Learn how to use TFLint

In this scenario, you will learn how to install an IaC static analysis tool called **TFLint** and run it on the Terraform (IaC) code.

You will need to download the sample code, install the TFLint tool, and then finally run static analysis scan on the Terraform resource.

### Install TFLint

> TFLint is a framework to find possible errors (like illegal instance types) for Major Cloud providers (AWS/Azure/GCP, warn about deprecated syntax, unused declarations and enforce best practices, naming conventions.
>
>You can find more details about the project at https://github.com/terraform-linters/tflint.

We will do all the exercises locally first in DevSecOps-Box, so lets start the exercise.

Let’s install **tflint** on the system to perform static analysis on your IaC.

```bash
curl https://raw.githubusercontent.com/terraform-linters/tflint/master/install_linux.sh | bash
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  2134  100  2134    0     0  59277      0 --:--:-- --:--:-- --:--:-- 60971
os=linux_amd64


====================================================
Looking up the latest version ...
Downloading TFLint v0.27.0
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   628  100   628    0     0   6097      0 --:--:-- --:--:-- --:--:--  6097
100  9.9M  100  9.9M    0     0  34.6M      0 --:--:-- --:--:-- --:--:-- 34.6M
Download was successfully


====================================================
Unpacking /tmp/tflint.zip ...
Archive:  /tmp/tflint.zip
  inflating: /tmp/tflint             
Installing /tmp/tflint to /usr/local/bin...
'/tmp/tflint' -> '/usr/local/bin/tflint'
tflint installed at /usr/local/bin/ successfully
Cleaning /tmp/tflint.zip and /tmp/tflint ...


====================================================
Current tflint version
TFLint version 0.27.0
```

We have successfully installed **tflint**.

Let’s move to the **next step**.

### Download vulnerable infrastructure

Let’s clone an example IaC (terraform) repository with the following command.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/terraform.git
```

Let’s cd into **terraform** directory.

```bash
cd terraform
```

We can use **-h** to check the various options provided by this tool.

```bash
tflint -h
Usage:
  tflint [OPTIONS] [FILE or DIR...]

Application Options:
  -v, --version                                           Print TFLint version
      --langserver                                        Start language server
  -f, --format=[default|json|checkstyle|junit|compact]    Output format (default: default)
  -c, --config=FILE                                       Config file name (default: .tflint.hcl)
      --ignore-module=SOURCE                              Ignore module sources
      --enable-rule=RULE_NAME                             Enable rules from the command line
      --disable-rule=RULE_NAME                            Disable rules from the command line
      --only=RULE_NAME                                    Enable only this rule, disabling all other defaults. Can be specified multiple times
      --enable-plugin=PLUGIN_NAME                         Enable plugins from the command line
      --var-file=FILE                                     Terraform variable file name
      --var='foo=bar'                                     Set a Terraform variable
      --module                                            Inspect modules
      --force                                             Return zero exit status even if issues found
      --no-color                                          Disable colorized output
      --loglevel=[trace|debug|info|warn|error]            Change the loglevel

Help Options:
  -h, --help                                              Show this help message
```

Lets move to the **next step**.

### Run the TFLint tool

**TFLint** performs static analysis on our Terraform resources. In this scenario, we will try to scan **aws** directory to find any possible errors, and review if we are following best practices in Terraform resources.

```bash
tflint aws
Failed to load configurations. 6 error(s) occurred:

Error: Invalid quoted type constraints

  on aws/consts.tf line 29, in variable "availability_zone":
  29:   type    = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Error: Invalid quoted type constraints

  on aws/consts.tf line 34, in variable "availability_zone2":
  34:   type    = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Error: Invalid quoted type constraints

  on aws/consts.tf line 40, in variable "ami":
  40:   type    = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Error: Invalid quoted type constraints

  on aws/consts.tf line 45, in variable "dbname":
  45:   type        = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Error: Invalid quoted type constraints

  on aws/consts.tf line 51, in variable "password":
  51:   type        = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Error: Invalid quoted type constraints

  on aws/consts.tf line 57, in variable "neptune-dbname":
  57:   type        = "string"

Terraform 0.11 and earlier required type constraints to be given in quotes, but that form is now deprecated and will be removed in a future version of Terraform. Remove the quotes around "string".

Warning: Quoted references are deprecated

  on aws/db-app.tf line 30, in resource "aws_db_instance" "default":
  30:     ignore_changes = ["password"]

In this context, references are expected literally rather than in quotes. Terraform 0.11 and earlier required quotes, but quoted references are now deprecated and will be removed in a future version of Terraform. Remove the quotes surrounding this reference to silence this warning.

Warning: Quoted references are deprecated

  on aws/eks.tf line 74, in resource "aws_eks_cluster" "eks_cluster":
  74:     "aws_iam_role_policy_attachment.policy_attachment-AmazonEKSClusterPolicy",

In this context, references are expected literally rather than in quotes. Terraform 0.11 and earlier required quotes, but quoted references are now deprecated and will be removed in a future version of Terraform. Remove the quotes surrounding this reference to silence this warning.

Warning: Quoted references are deprecated

  on aws/eks.tf line 75, in resource "aws_eks_cluster" "eks_cluster":
  75:     "aws_iam_role_policy_attachment.policy_attachment-AmazonEKSServicePolicy",

In this context, references are expected literally rather than in quotes. Terraform 0.11 and earlier required quotes, but quoted references are now deprecated and will be removed in a future version of Terraform. Remove the quotes surrounding this reference to silence this warning.
```

As you can see, **tflint** found several errors that we need to fix in our Terraform configurations/code. Fixing errors when writing terraform resources will prevent errors occurring during runtime when executing with the **terraform run** command.


## Learn how to embed TFLint into GitHub Actions

### Use TFLint tool to find security issues for IaC in GitHub Actions

In this scenario, you will learn how to embed TFLint in GitHub Actions.

> Note
>
>DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.

### A simple CI/CD pipeline

You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named **GitHub Actions** that debuted on **13 November 2019**. **GitHub Actions** is a CI/CD system that is built-in to GitHub with **free** and paid offerings.

Let’s get started!

### 1. Create a new repository

> If you haven’t registered for a GitHub account, please sign up for an account [here](https://github.com/join?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with **Public** or **Private** options, and please ignore **initialize this repository with** section for now.

Click the **Create repository** button.

### 2. Create a Personal Access Token (PAT)

Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting [August 2021](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/).

Let’s create PAT by visiting https://github.com/settings/tokens,then click **Generate new token** button and give your token a name e.g. terraform.

Select **repo** option to access repositories from the command line, and scroll down to generate a new token.

> The token will have a format like this **ghp_xxxxxxxxx**.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use the token whenever needed.

### 3. Initial git setup

To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

```bash
git config --global user.email "your_email@gmail.com"
git config --global user.name "your_username"
```

> You need to use your email and username, which are registered in GitHub.
>Please don’t use your company’s GitHub credentials or token to practice these exercises.

### 4. Download the repository

Let’s start by cloning **terraform** in DevSecOps Box.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/terraform.git
```

By cloning the above repository, we created a local copy of the remote repository.

Let’s **cd** into this repository to explore its content.

```bash
cd terraform
```

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

```bash
git remote rename origin old-origin
```

> In the command below, please change “username” with your GitHub username.

```bash
git remote add origin https://github.com/username/terraform.git
```

Let’s check the status of our git repository.

```bash
git status
On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
```

We are in the **master** branch and we need to create one more branch called **main** as a default branch.

```bash
git checkout -b main
```

> Why do we need a new branch? Because in this exercise we will use the **main** branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.
>Read more about [Renaming the default branch from master](https://github.com/github/renaming).

Then, let’s push the code to the GitHub repository.

```bash
git push -u origin main
```

And enter your GitHub credentials when prompted (please use Personal Access Token as a password), then the code will be pushed to the GitHub repository.

### 5. Add a workflow file to the repository

To use **GitHub Actions**, you need to create **.github/workflows** directory and create a new YAML file named main.yaml or any other desired name because each file in the **.github/workflows** directory which has a **.yaml** extension will define a workflow.

Let’s create a simple workflow by entering the following commands in DevSecOps Box.

```bash
mkdir -p .github/workflows
```

```bash
cat >.github/workflows/main.yaml<<EOF
name: Terraform                               # workflow name

on:
  push:                                       
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - run: echo "This is a build step"

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - run: echo "This is a test step"

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
      - run: echo "This is a deploy step"
EOF
```

> If you are not comfortable with the syntax, explore the **GitHub Actions** syntax athttps://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#about-yaml-syntax-for-workflows

Let’s add this file to the repository and commit the changes.

```bash
git add .github/workflows/main.yaml
git commit -m "Add github workflows"
```

### 6. Push the changes to the repository

Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the **git push** command.

```bash
git push origin master
Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
```

### 7. Verify the pipeline runs

Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our **terraform** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

> You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

Let’s move to the **next step**.

### Embed TFLint in GitHub Actions

As discussed in the **Terraform Linter using TFLint** exercise, we can embed TFLint in our CI/CD pipeline. We will
embedding TFLint in GitHub Actions through a TFLint action available from the GitHub marketplace, please visit the following link to see the details of **TFLint** action [here](https://github.com/marketplace/actions/setup-tflint).

> **action** is similar to a plugin or a template that is offered by the provider themselves.

Go back to the DevSecOps Box machine, and replace the content of the **build** job in **.github/workflows/main.yaml** file with the below content.

```yml
  tflint:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - uses: terraform-linters/setup-tflint@v1
        name: Setup TFLint
        with:
          tflint_version: latest

      - name: Run TFLint
        run: tflint -f json aws > tflint-output.json

      - uses: actions/upload-artifact@v2
        with:
          name: TFLint
          path: tflint-output.json
        if: always()                        # what is this for?
```

> To understand **if: always()** Please refer to [conditionals](https://docs.github.com/en/actions/reference/context-and-expression-syntax-for-github-actions#job-status-check-functions).

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our **terraform** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

## Use Checkov to check misconfigurations in IaC

### Learn how to use Checkov

In this scenario, you will learn how to install and run SAST Scans on your Infrastructure as Code like Terraform.

You will need to download the code, install the tool, run the scan on the code and evaluate your infrastructure’s security posture.

### Install Checkov

> Checkov is a static code analysis tool for infrastructure-as-code, support several cloud such as AWS, Azure and GCP also can detect environment variables on your cloud environment.
>
>You can find more details about the project at https://github.com/bridgecrewio/checkov

We will do all the exercises locally first in DevSecOps-Box, so lets start the exercise.

Let’s install **checkov** on the system to perform static analysis on your IaC.

```bash
pip3 install checkov
Collecting checkov
  Downloading checkov-1.0.669-py3-none-any.whl (396 kB)
     |████████████████████████████████| 396 kB 31.1 MB/s 
Collecting pyyaml==5.3.1
  Downloading PyYAML-5.3.1.tar.gz (269 kB)
     |████████████████████████████████| 269 kB 56.2 MB/s 
Collecting requests==2.22.0
  Downloading requests-2.22.0-py2.py3-none-any.whl (57 kB)
     |████████████████████████████████| 57 kB 15.6 MB/s 
Collecting dpath==1.5.0
  Downloading dpath-1.5.0.tar.gz (17 kB)
Collecting docopt==0.6.2
  Downloading docopt-0.6.2.tar.gz (25 kB)
Collecting tqdm==4.49.0
  Downloading tqdm-4.49.0-py2.py3-none-any.whl (69 kB)
     |████████████████████████████████| 69 kB 25.2 MB/s 
Collecting boto3==1.12.43
  Downloading boto3-1.12.43-py2.py3-none-any.whl (128 kB)
     |████████████████████████████████| 128 kB 98.9 MB/s 

...[SNIP]...

Successfully built docopt dpath junit-xml lark-parser pyyaml tabulate termcolor
Installing collected packages: six, urllib3, python-dateutil, jmespath, docutils, smmap, idna, chardet, certifi, botocore, s3transfer, requests, pyparsing, lark-parser, gitdb, update-checker, tqdm, termcolor, tabulate, semantic-version, pyyaml, packaging, junit-xml, GitPython, dpath, docopt, deep-merge, colorama, boto3, bc-python-hcl2, checkov
Successfully installed GitPython-3.1.7 bc-python-hcl2-0.3.15 boto3-1.12.43 botocore-1.15.49 certifi-2021.5.30 chardet-3.0.4 checkov-1.0.669 colorama-0.4.3 deep-merge-0.0.4 docopt-0.6.2 docutils-0.15.2 dpath-1.5.0 gitdb-4.0.5 idna-2.8 jmespath-0.10.0 junit-xml-1.8 lark-parser-0.7.8 packaging-20.4 pyparsing-2.4.7 python-dateutil-2.8.1 pyyaml-5.3.1 requests-2.22.0 s3transfer-0.3.7 semantic-version-2.8.5 six-1.15.0 smmap-3.0.5 tabulate-0.8.6 termcolor-1.1.0 tqdm-4.49.0 update-checker-0.18.0 urllib3-1.25.10
WARNING: Running pip as root will break packages and permissions. You should install packages reliably by using venv: https://pip.pypa.io/warnings/venv
WARNING: You are using pip version 21.1.2; however, version 21.1.3 is available.
You should consider upgrading via the '/usr/bin/python3 -m pip install --upgrade pip' command.
```

We have successfully installed checkov, let’s move to the **next step**.

### Download vulnerable infrastructure

Let’s clone an example IaC (terraform) repository with the following command.

```bash
git clone https://github.com/bridgecrewio/terragoat.git
```

Let’s move into terragoat directory.

```bash
cd terragoat
```

We can use -h to check various options provided by this tool.

```bash
checkov -h
```

It should give us the list of available arguments to be used but if you found an error like this.

```bash
Traceback (most recent call last):
  File "/usr/local/bin/checkov", line 2, in <module>
    from checkov.main import run
  File "/usr/local/lib/python3.6/dist-packages/checkov/main.py", line 16, in <module>
    from checkov.common.util.docs_generator import print_checks
  File "/usr/local/lib/python3.6/dist-packages/checkov/common/util/docs_generator.py", line 11, in <module>
    from checkov.serverless.registry import sls_registry
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/__init__.py", line 1, in <module>
    from checkov.serverless.checks.function import *
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/checks/__init__.py", line 3, in <module>
    from checkov.serverless.checks.function import *
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/checks/function/__init__.py", line 1, in <module>
    from checkov.serverless.checks.function.aws import *
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/checks/function/aws/AWSCredentials.py", line 3, in <module>
    from checkov.serverless.checks.function.base_function_check import BaseFunctionCheck
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/checks/function/base_function_check.py", line 5, in <module>
    from checkov.serverless.checks.function.registry import function_registry
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/checks/function/registry.py", line 1, in <module>
    from checkov.serverless.base_registry import ServerlessRegistry
  File "/usr/local/lib/python3.6/dist-packages/checkov/serverless/base_registry.py", line 2, in <module>
    from dataclasses import dataclass
ModuleNotFoundError: No module named 'dataclasses'
```

oh okay, its complaining about the lack of **dataclasses**, we need to install the module with the following command.

```bash
pip3 install dataclasses
Collecting dataclasses
  Downloading dataclasses-0.8-py3-none-any.whl (19 kB)
Installing collected packages: dataclasses
Successfully installed dataclasses-0.8
WARNING: Running pip as root will break packages and permissions. You should install packages reliably by using venv: https://pip.pypa.io/warnings/venv
WARNING: You are using pip version 21.1.2; however, version 21.1.3 is available.
You should consider upgrading via the '/usr/bin/python3 -m pip install --upgrade pip' command.
```

Let’s try the help command once again.

```bash
checkov -h
usage: checkov [-h] [-v] [-d DIRECTORY] [-f FILE]
               [--external-checks-dir EXTERNAL_CHECKS_DIR]
               [--external-checks-git EXTERNAL_CHECKS_GIT] [-l]
               [-o [{cli,json,junitxml,github_failed_only}]] [--no-guide]
               [--quiet]
               [--framework {cloudformation,terraform,terraform_plan,kubernetes,serverless,arm,all}]
               [-c CHECK] [--skip-check SKIP_CHECK] [-s]
               [--bc-api-key BC_API_KEY] [--repo-id REPO_ID] [-b BRANCH]
               [--download-external-modules DOWNLOAD_EXTERNAL_MODULES]
               [--external-modules-download-path EXTERNAL_MODULES_DOWNLOAD_PATH]
               [--evaluate-variables EVALUATE_VARIABLES]

Infrastructure as code static analysis

optional arguments:
  -h, --help            show this help message and exit
  -v, --version         version
  -d DIRECTORY, --directory DIRECTORY
                        IaC root directory (can not be used together with
                        --file).
  -f FILE, --file FILE  IaC file(can not be used together with --directory)
  --external-checks-dir EXTERNAL_CHECKS_DIR
                        Directory for custom checks to be loaded. Can be
                        repeated
  --external-checks-git EXTERNAL_CHECKS_GIT
                        Github url of external checks to be added. you can
                        specify a subdirectory after a double-slash //. cannot
                        be used together with --external-checks-dir
  -l, --list            List checks
  -o [{cli,json,junitxml,github_failed_only}], --output [{cli,json,junitxml,github_failed_only}]
                        Report output format
  --no-guide            do not fetch bridgecrew guide in checkov output report
  --quiet               in case of CLI output, display only failed checks
  --framework {cloudformation,terraform,terraform_plan,kubernetes,serverless,arm,all}
                        filter scan to run only on a specific infrastructure
                        code frameworks
  -c CHECK, --check CHECK
                        filter scan to run only on a specific check
                        identifier(allowlist), You can specify multiple checks
                        separated by comma delimiter
  --skip-check SKIP_CHECK
                        filter scan to run on all check but a specific check
                        identifier(denylist), You can specify multiple checks
                        separated by comma delimiter
  -s, --soft-fail       Runs checks but suppresses error code
  --bc-api-key BC_API_KEY
                        Bridgecrew API key
  --repo-id REPO_ID     Identity string of the repository, with form
                        <repo_owner>/<repo_name>
  -b BRANCH, --branch BRANCH
                        Selected branch of the persisted repository. Only has
                        effect when using the --bc-api-key flag
  --download-external-modules DOWNLOAD_EXTERNAL_MODULES
                        download external terraform modules from public git
                        repositories and terraform registry
  --external-modules-download-path EXTERNAL_MODULES_DOWNLOAD_PATH
                        set the path for the download external terraform
                        modules
  --evaluate-variables EVALUATE_VARIABLES
                        evaluate the values of variables and locals
```

Interesting! lots of options to play around. Lets move to the **next step**.

### Run Checkov tool

Checkov can perform the static scan on a directory or a file. In this scenario, we will try to scan **terraform/aws/s3.tf** to find misconfiguration related to AWS S3 buckets.

```bash
checkov -f terraform/aws/s3.tf
       _               _
   ___| |__   ___  ___| | _______   __
  / __| '_ \ / _ \/ __| |/ / _ \ \ / /
 | (__| | | |  __/ (__|   < (_) \ V /
  \___|_| |_|\___|\___|_|\_\___/ \_/

by bridgecrew.io | version: 1.0.513

terraform scan results:

Passed checks: 19, Failed checks: 16, Skipped checks: 0

Check: CKV_AWS_70: "Ensure S3 bucket does not allow an action with any Principal"
        PASSED for resource: aws_s3_bucket.data
        File: /s3.tf:1-13
        Guide: https://docs.bridgecrew.io/docs/bc_aws_s3_23

Check: CKV_AWS_57: "S3 Bucket has an ACL defined which allows public WRITE access."
        PASSED for resource: aws_s3_bucket.data
        File: /s3.tf:1-13
        Guide: https://docs.bridgecrew.io/docs/s3_2-acl-write-permissions-everyone

Check: CKV_AWS_70: "Ensure S3 bucket does not allow an action with any Principal"
        PASSED for resource: aws_s3_bucket.financials
        File: /s3.tf:25-37
        Guide: https://docs.bridgecrew.io/docs/bc_aws_s3_23

Check: CKV_AWS_57: "S3 Bucket has an ACL defined which allows public WRITE access."
        PASSED for resource: aws_s3_bucket.financials
        File: /s3.tf:25-37
        Guide: https://docs.bridgecrew.io/docs/s3_2-acl-write-permissions-everyone

Check: CKV_AWS_20: "S3 Bucket has an ACL defined which allows public READ access."
        PASSED for resource: aws_s3_bucket.financials
        File: /s3.tf:25-37
        Guide: https://docs.bridgecrew.io/docs/s3_1-acl-read-permissions-everyone
...
...
```

As you can see, **checkov** found security issues in our Terraform configurations/code.

If you are interested in scanning a directory, you can use the following command.

```bash
checkov -d terraform/aws
```

Lets move to the **next step**.

## Challenge

1. Scan the entire directory (**/terragoat/terraform**) and save the output into a **JSON** file
2. How many checks failed after running **checkov** tool?
3. Can you skip a specific check in the target directory? Any check of your choice, but skip at least five checks.

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Learn how to embed Checkov into CI/CD pipeline

### Use Checkov tool to perform SAST for IaC in CI/CD pipeline

In this scenario, you will learn how to embed **Checkov** in CI/CD pipeline.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

>Remember!
>
>1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

### A simple CI/CD pipeline

Create a new projects by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/projects/new#blank_project, give the project name **terraform** and click **Create project** button. Once done we need to push our source code into GitLab, let’s download the code using **git clone** in DevSecOps Box.

### Embed Checkov in CI/CD pipeline

As discussed in the **Secure IaC using Checkov** exercise, we can embed Checkov in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the pipeline.

```yaml
image: docker:latest

services:
  - docker:dind

stages:
  - validate
  - build
  - test
  - release
  - preprod
  - integration
  - prod

checkov:
  stage: validate
  script:
    - docker pull bridgecrew/checkov
    - docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json
  artifacts:
    paths: [checkov-output.json]
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
```

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/terraform/pipelines.

Click on the appropriate job name to see the output.

You will notice that the **checkov** job’s output is saved in **checkov-output.json** file.

Let’s move to the **next step**.

### Allow the job failure

> Remember!
>
>Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the **allow_failure** tag to not fail the build even though the tool found issues.

```yaml
checkov:
  stage: validate
  script:
    - docker pull bridgecrew/checkov
    - docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json
  artifacts:
    paths: [checkov-output.json]
    when: always
  allow_failure: true   #<--- allow the build to fail but don't mark it as such
```

After adding the **allow_failure** tag, the pipeline would look like the following.

```yml
image: docker:latest

services:
  - docker:dind

stages:
  - validate
  - build
  - test
  - release
  - preprod
  - integration
  - prod

checkov:
  stage: validate
  script:
    - docker pull bridgecrew/checkov
    - docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json
  artifacts:
    paths: [checkov-output.json]
    when: always
  allow_failure: true

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
```

You will notice that the **checkov** job failed however it didn’t block others from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/terraform/pipelines.

Click on the appropriate job name to see the output.


## Learn how to embed Checkov into Jenkins CI/CD pipeline

### Use Checkov tool to perform SAST in Jenkins CI/CD pipeline

In this scenario, you will learn how to embed Checkov in the Jenkins CI/CD pipeline.

>Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab and Jenkins machine to start.

>Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working
>
>3. We have already set up the Jenkins machine with several plugins to help you do the exercise

### Create a new job

> The Jenkins system is already configured with GitLab. If you wish to know **how to configure Jenkins with GitLab**, you can check out this [link](https://gitlab.practical-devsecops.training/pdso/jenkins/-/blob/master/tutorials/configure-jenkins-with-gitlab.md).

### Add the repository for Terraform

Create a new project by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/projects/new#blank_project, give it a name **golang** and click **Create project** button. Once done, we need to push our source code into GitLab, let’s download the code using **git clone** in DevSecOps Box.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/terraform.git terraform
cd terraform
```

Rename git url to the new one.

```bash
git remote rename origin old-origin
git remote add origin http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/terraform.git
```

Then, push the code into the repository.

```bash
git push -u origin --all
```

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
```

We do have four stages in this pipeline, **build**, **test**, **integration** and **prod**.

Let’s log in to Gitlab using the following details.

Name	Value
**Gitlab UR**L	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
**Username**	root

Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

Since we want to use Jenkins to execute the CI/CD jobs, we need to remove **.gitlab-ci.yml** from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the **Gitlab Runner** and the **Jenkins** systems.

> Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the **.gitlab-ci.yaml** file is missing.
>
>Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.

Click on the appropriate build history to see the output.

### Challenge

Recall techniques you have learned in the previous module (Secure SDLC and CI/CD).

1. Read the Checkov documentation [here](https://www.checkov.io/documentation.html)
2. Run the Checkov tool in a new stage called **checkov** and save the output as a **JSON** file
3. Follow all the best practices while embedding Checkov in the CI/CD pipeline

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed Checkov in Jenkins

As discussed in the **Secure IaC using Checkov** exercise, we can embed Checkov in our CI/CD pipeline. However, you need to test the command manually before you embed this SAST tool in the CI pipeline.

```groovy
pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("checkov") {
            steps {
                sh "docker run --rm -w /src -v \$(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json"
            }
            post {
                always {
                    archiveArtifacts artifacts: 'checkov-output.json', fingerprint: true
                }
            }
        }
        stage("test") {
            steps {
                echo "This is a test step"
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

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/terraform.

Click on the appropriate job name to see the output.

You will notice that the **checkov** stage’s output is saved in the **checkov-output.json** file.

Let’s move to the **next step**.

### Allow the stage failure

> Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the **catchError** function to “not fail the build” even though the tool found issues.

> **Reference**: https://www.jenkins.io/doc/pipeline/steps/workflow-basic-steps

```groovy
        stage("checkov") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {     // Allow the sast stage to fail
                    sh "docker run --rm -w /src -v \$(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'checkov-output.json', fingerprint: true
                }
            }
        }
```

After adding the **catchError** function, the pipeline would look like the following.

```groovy
pipeline {
    agent any

    options {
        gitLabConnection('gitlab')
    }

    stages {
        stage("checkov") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh "docker run --rm -w /src -v \$(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'checkov-output.json', fingerprint: true
                }
            }
        }
        stage("test") {
            steps {
                echo "This is a test step."
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
```

You will notice that the **checkov** job failed. However, it didn’t block other jobs from continuing further.

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/terraform.

Click on the appropriate job name to see the output.

## Learn how to embed Checkov into CircleCI

### Use Checkov tool to perform SAST for IaC in CircleCI

In this scenario, you will learn how to embed **Checkov** in **CircleCI**.

> Note
>
>DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.

### Initial Setup

You’ve learned about CI/CD systems such as GitLab, Jenkins, GitHub Actions and so on. Remember every CI/CD system has its own advantages, and limitations, we just need to find what is suitable for our needs.

Now, we will look into another CI/CD system called **CircleCI**, this system doesn’t have a built-in Git repository like GitLab or GitHub. But it can be integrated with GitHub or Bitbucket as the repository, so let’s get started!

### 1. Create a new repository

> If you haven’t registered for a GitHub account, please sign up for an account [here](https://github.com/join?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with **Public** or **Private** options, and please ignore **Initialize this repository with** section for now.

Click the **Create repository** button.

### 2. Create a Personal Access Token (PAT)

Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting [August 2021](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/).


Let’s create PAT by visiting https://github.com/settings/tokens, then click **Generate new token** button and give your token a name e.g. terraform.

Select **repo** option to access repositories from the command line and scroll down to generate a new token.

>The token will have a format like **ghp_xxxxxxxxx**.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use it whenever we needed.

### A simple CI/CD pipeline

You need to create **.circleci** directory and create a new YAML file named config.yml and add the following CI script.

```bash
mkdir -p .circleci
cat >.circleci/config.yml<<EOF
jobs:
  build:
    machine: true
    steps:
      - checkout
      - run: echo "This is a build step"

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
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
```

> If you are not comfortable with the syntax, explore the **CircleCI** syntax at https://circleci.com/docs/2.0/configuration-reference/

Let’s add this file to the repository and commit the changes.

```bash
git add .circleci/config.yml
git commit -m "Add CircleCI config"
```

Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the **git push** command.

```bash
git push origin main
Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
```

Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our [CircleCI account](https://app.circleci.com/). Click **Projects**, select **terraform** repository and select the appropriate pipeline to see the output.

Let’s move to the **next step**.

### Embed Terrascan in CircleCI

As discussed in the **Secure IaC using Checkov** exercise, we can embed Checkov in our GitHub Actions. However, you need to test the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and replace the content of the **build** job in **.circleci/config.yml** file.

```yml
  checkov:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json

      - store_artifacts:
          path: checkov-output.json
          destination: checkov-artifact
```

Please remember to replace the above job name to the **checkov** in **workflows** section as shown below:

```yml
workflows:
  version: 2
  terraform:
    jobs:
      - checkov
      - test:
          requires:
            - checkov
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
```

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our [CircleCI account](https://app.circleci.com/). Click **Projects**, select **terraform** repository and select the appropriate pipeline to see the output.

### Allow the job failure

We do not want to fail the builds/jobs/scan in **DevSecOps Maturity Levels 1 and 2**, as security tools spit a significant amount of false positives.

You can use the** when: always** syntax to not fail the build even though the tool found security issues.

```yml
  checkov:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json

      - store_artifacts:
          path: checkov-output.json
          destination: checkov-artifact
          when: always              # Even if the job fails, continue to the next stages
```

The final pipeline would look like the following:

```yml
jobs:
  build:
    machine: true
    steps:
      - checkout
      - run: echo "This is a build step"

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  checkov:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -w /src -v $(pwd):/src bridgecrew/checkov -d aws -o json | tee checkov-output.json

      - store_artifacts:
          path: checkov-output.json
          destination: checkov-artifact
          when: always              # Even if the job fails, continue to the next stages

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
    jobs:
      - checkov
      - test:
          requires:
            - checkov
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
```

Go ahead and add the above content to the **.circleci/config.yml** file to run the pipeline.

You will notice that the **checkov** job failed however it didn’t block others from continuing further.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our [CircleCI account](https://app.circleci.com/). Click **Projects**, select **terraform** repository and select the appropriate pipeline to see the output.


## Secure IaC using Ansible Vault

### Learn how to use Ansible Vault to do secure IaC

In this scenario, you will learn how to use Ansible to do secure IaC.

You will learn how to use **ansible-vault** to encrypt your secrets when provisioning the infrastructure.

### Install Ansible and Ansible Vault

> Ansible uses simple English like language to automate configurations, settings and deployments in traditional and cloud environments. Its easy to learn and can be understood by even non-technical folks.
>
>Source: [Ansible official website](https://www.ansible.com/)

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

First, we need to install the **ansible**, **ansible-vault** is installed by default when you install ansible.

```bash
pip3 install ansible==2.10.4
Collecting ansible
  Downloading ansible-2.10.3.tar.gz (28.0 MB)
     |████████████████████████████████| 28.0 MB 15.8 MB/s 
Collecting ansible-base<2.11,>=2.10.3
  Downloading ansible-base-2.10.3.tar.gz (5.8 MB)
     |████████████████████████████████| 5.8 MB 49.9 MB/s 
Collecting jinja2
  Downloading Jinja2-2.11.2-py2.py3-none-any.whl (125 kB)
     |████████████████████████████████| 125 kB 78.6 MB/s 
Collecting PyYAML
  Downloading PyYAML-5.3.1.tar.gz (269 kB)
     |████████████████████████████████| 269 kB 77.0 MB/s 
Collecting cryptography
  Downloading cryptography-3.2.1-cp35-abi3-manylinux2010_x86_64.whl (2.6 MB)
     |████████████████████████████████| 2.6 MB 65.6 MB/s 
Collecting packaging
  Downloading packaging-20.4-py2.py3-none-any.whl (37 kB)
Collecting MarkupSafe>=0.23
  Downloading MarkupSafe-1.1.1-cp36-cp36m-manylinux1_x86_64.whl (27 kB)
Collecting six>=1.4.1
  Downloading six-1.15.0-py2.py3-none-any.whl (10 kB)
Collecting cffi!=1.11.3,>=1.8
  Downloading cffi-1.14.3-cp36-cp36m-manylinux1_x86_64.whl (400 kB)
     |████████████████████████████████| 400 kB 78.2 MB/s 
Collecting pyparsing>=2.0.2
  Downloading pyparsing-2.4.7-py2.py3-none-any.whl (67 kB)
     |████████████████████████████████| 67 kB 19.5 MB/s 
Collecting pycparser
  Downloading pycparser-2.20-py2.py3-none-any.whl (112 kB)
     |████████████████████████████████| 112 kB 55.8 MB/s 
Building wheels for collected packages: ansible, ansible-base, PyYAML
  Building wheel for ansible (setup.py) ... done
  Created wheel for ansible: filename=ansible-2.10.3-py3-none-any.whl size=46091824 sha256=b537268ab1619e2f871470b76512832c18052daac132c76207bfc4271c5f6625
  Stored in directory: /root/.cache/pip/wheels/3b/56/b0/fb0837270bfbd10b239e354d5cbe3abc50a3a4d9a885adb1b7
  Building wheel for ansible-base (setup.py) ... done
  Created wheel for ansible-base: filename=ansible_base-2.10.3-py3-none-any.whl size=1862869 sha256=1cb096480f7ed8b27fa01d50a228b40243e108caf3d69a5b11d2cff402e1cabd
  Stored in directory: /root/.cache/pip/wheels/e0/36/72/733a42537220bc29763b228c889c153c6c6df89b07c918f221
  Building wheel for PyYAML (setup.py) ... done
  Created wheel for PyYAML: filename=PyYAML-5.3.1-cp36-cp36m-linux_x86_64.whl size=44619 sha256=daf5c616a5478ac7b510fe8b235fd7dd5289039101e6ce6c5ee93183b5081fcb
  Stored in directory: /root/.cache/pip/wheels/e5/9d/ad/2ee53cf262cba1ffd8afe1487eef788ea3f260b7e6232a80fc
Successfully built ansible ansible-base PyYAML
Installing collected packages: MarkupSafe, jinja2, PyYAML, six, pycparser, cffi, cryptography, pyparsing, packaging, ansible-base, ansible
Successfully installed MarkupSafe-1.1.1 PyYAML-5.3.1 ansible-2.10.3 ansible-base-2.10.3 cffi-1.14.3 cryptography-3.2.1 jinja2-2.11.2 packaging-20.4 pycparser-2.20 pyparsing-2.4.7 six-1.15.0
```

Let’s move to the **next step**.

### Create the inventory file

Let’s create the inventory or CMDB file for Ansible using the following command.

```bash
cat > inventory.ini <<EOL

# DevSecOps Studio Inventory
[devsecops]
devsecops-box-xIKciVAk

[prod]
prod-xIKciVAk

EOL
```

Next, we will have to ensure the SSH’s **yes/no** prompt is not shown while running the ansible commands so we will be using **ssh-keyscan** to capture the key signatures beforehand.

```bash
ssh-keyscan -t rsa prod-xIKciVAk >> ~/.ssh/known_hosts
# prod-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

Let’s do this for the rest of the systems in the lab as well.

```bash
ssh-keyscan -t rsa devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
# devsecops-box-xIKciVAk:22 SSH-2.0-OpenSSH_7.6p1 Ubuntu-4ubuntu0.3
```

> **Pro-tip**: Instead of running the **ssh-keyscan** command twice, we can achieve the same using the below command.

```bash
ssh-keyscan -t rsa prod-xIKciVAk devsecops-box-xIKciVAk >> ~/.ssh/known_hosts
```

Let’s move to the **next step**.

### Run the ansible commands

Let’s run the **ansible ad-hoc command** to check the production machine’s uptime. We can use the **uptime** command to find the uptime.

```bash
ansible -i inventory.ini prod -m shell -a "uptime"
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host prod-xIKciVAk should
use /usr/bin/python3, but is using /usr/bin/python for backward compatibility
with prior Ansible releases. A future Ansible release will default to using the
 discovered platform python for this host. See https://docs.ansible.com/ansible
/2.9/reference_appendices/interpreter_discovery.html for more information. This
 feature will be removed in version 2.12. Deprecation warnings can be disabled
by setting deprecation_warnings=False in ansible.cfg.
prod-xIKciVAk | CHANGED | rc=0 >>
 12:06:35 up 18 days,  5:11,  1 user,  load average: 0.65, 0.80, 0.40
```

As you can see, we got the uptime of the production machine.

```bash
__12:06:35 up 18 days,  5:11,  1 user,  load average: 0.65, 0.80, 0.40__
```

Similarly, we can use other ansible modules to install/remove a package, copy a file or any other task on the production machine.

For example, if we want to copy a file into the production machine.

```bash
echo "hello" > example
ansible -i inventory.ini prod -m copy -a "src=example dest=/opt/example"
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host prod-xIKciVAk should
use /usr/bin/python3, but is using /usr/bin/python for backward compatibility
with prior Ansible releases. A future Ansible release will default to using the
 discovered platform python for this host. See https://docs.ansible.com/ansible
/2.9/reference_appendices/interpreter_discovery.html for more information. This
 feature will be removed in version 2.12. Deprecation warnings can be disabled
by setting deprecation_warnings=False in ansible.cfg.
prod-xIKciVAk | CHANGED => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": true,
    "checksum": "f572d396fae9206628714fb2ce00f72e94f2258f",
    "dest": "/opt/example",
    "gid": 0,
    "group": "root",
    "md5sum": "b1946ac92492d2347c6235b4d2611184",
    "mode": "0644",
    "owner": "root",
    "size": 6,
    "src": "/root/.ansible/tmp/ansible-tmp-1600065789.959017-108-91955689798923/source",
    "state": "file",
    "uid": 0
}
```

Let’s check the file is copied to the production machine.

```bash
ssh root@prod-xIKciVAk
Welcome to Ubuntu 18.04.5 LTS (GNU/Linux 5.4.0-47-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

This system has been minimized by removing packages and content that are
not required on a system that users do not log into.

To restore this content, you can run the 'unminimize' command.
Last login: Mon Sep 14 06:43:11 2020 from 172.18.0.2
```

```bash
ls /opt/
root@prod-xIKciVAk:~# ls /opt/
example
```

As you can see, the file was successfully copied from DevSecOps Box to the production machine with the help of Ansible.

Let’s go back to the **DevSecOps-Box** machine to try out **Ansible Vault** functionality of Ansible.

```bash
exit
```

In the next step, you will learn how to send the file securely with **ansible-vault**.

Let’s move to the **next step**.

### Encrypted Data with Ansible Vault

Ansible provides the ability to store secrets securely using **Ansible Vault**. Ansible can even send this file securely over the network using secure mechanisms like SSH and AES. For example, securely send a file containing a password over the network.

```bash
echo "StrongP@ssw0rd" > /secret
```

However, this file is not encrypted, and storing it in the source code is insecure, and hence not recommended. We can use **ansible-vault** command to encrypt the contents of the file before storing it into the code repository.

```bash
ansible-vault encrypt /secret --ask-vault-pass
```

You can use the following password as your password or anything you desire but ensure you remember this password as its needed when running **ansible-playbook** command.

**Password**: C0mpl3xp@sswOrd

```bash
New Vault password: 
Confirm New Vault password: 
Encryption successful
```

The above command encrypts the file using the AES algorithm, we can verify it by using the cat command.

```bash
cat /secret
$ANSIBLE_VAULT;1.1;AES256
62663235633637356435616166313963366465653938333265633861613332666330613334653330
3132396436333139313763643737616437626630386430620a666634396461396232333963356530
35613163313338356364326662333134363036313735396662323061316565393861333265643531
6332343530303433310a616539613637636437373038636364366365626261363537626231633362
3665
```

No one but folks who know the ansible vault’s password can edit or view the file. Ansible provides an easy way to provide this password when the encrypted files need to be decrypted. This option/flag is called **–ask-vault-pass**.

```bash
ansible -i inventory.ini prod --ask-vault-pass -m copy -a "src=/secret dest=/opt/secret"
Vault password: 
[DEPRECATION WARNING]: Distribution Ubuntu 18.04 on host prod-mtwfyysn should 
use /usr/bin/python3, but is using /usr/bin/python for backward compatibility 
with prior Ansible releases. A future Ansible release will default to using the
 discovered platform python for this host. See https://docs.ansible.com/ansible
/2.9/reference_appendices/interpreter_discovery.html for more information. This
 feature will be removed in version 2.12. Deprecation warnings can be disabled 
by setting deprecation_warnings=False in ansible.cfg.
prod-xIKciVAk | CHANGED => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python"
    },
    "changed": true,
    "checksum": "94b4760c3173f5622f2b1d28c3904acb2f9cf0b0",
    "dest": "/opt/secret",
    "gid": 0,
    "group": "root",
    "md5sum": "08c5986c765add82ed7cc25c8916aeb6",
    "mode": "0644",
    "owner": "root",
    "size": 15,
    "src": "/root/.ansible/tmp/ansible-tmp-1600067451.7159142-68-115152514452715/source",
    "state": "file",
    "uid": 0
}
```

Remember the decryption password for this file is C0mpl3xp@sswOrd.

Let’s SSH into our production machine.

```bash
ssh root@prod-xIKciVAk "cat /opt/secret"
StrongP@ssw0rd
```

As you can see, the file was transferred to the remote machine after Ansible Vault decrypted the file.

## Use Terrascan to find security issues in IaC

### Learn how to use Terrascan

In this scenario, you will learn how to install an IaC static analysis tool called **Terrascan** and run it on the Terraform (IaC) code.

You will need to install the tool, download the code, run the scan on the code and evaluate your infrastructure’s security posture.

##Install Terrascan tool

> Terrascan allows us to detect compliance and security violations across Infrastructure as Code to mitigate risk before provisioning cloud-native infrastructure.
>
> You can find more details about the project at https://github.com/accurics/terrascan.

We will do all the exercises locally first in DevSecOps-Box, so let’s get started.

Let’s install **terrascan** on the system to perform static analysis on your IaC.

```bash
wget https://github.com/accurics/terrascan/releases/download/v1.8.0/terrascan_1.8.0_Linux_x86_64.tar.gz

tar -xvf terrascan_1.8.0_Linux_x86_64.tar.gz

CHANGELOG.md
LICENSE
README.md
terrascan
```

Let’s give terrascan executable permissions.

```bash
chmod +x terrascan
```

We are moving the terrascan binary to the local binary location so we can reference it by just using its name.

```bash
mv terrascan /usr/local/bin/
```

We have successfully installed **terrascan**, let’s move to the **next step**.

### Download vulnerable infrastructure

Let’s clone an example IaC (terraform) repository with the following command.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/terraform.git
```

Let’s move into terraform directory.

```bash
cd terraform
```

We can use -h to check the various options provided by this tool.

```bash
terrascan -h
Terrascan

Detect compliance and security violations across Infrastructure as Code to mitigate risk before provisioning cloud native infrastructure.
For more information, please visit https://docs.accurics.com

Usage:
  terrascan [command]

Available Commands:
  init        Initializes Terrascan and clones policies from the Terrascan GitHub repository.
  scan        Detect compliance and security violations across Infrastructure as Code.
  server      Run Terrascan as an API server
  version     Terrascan version

Flags:
  -c, --config-path string   config file path
  -l, --log-level string     log level (debug, info, warn, error, panic, fatal) (default "info")
  -x, --log-type string      log output type (console, json) (default "console")
  -o, --output string        output type (human, json, yaml, xml, junit-xml, sarif) (default "human")

Use "terrascan [command] --help" for more information about a command.
```

Let’s move to the **next step**.

### Run the Terrascan tool

Terrascan performs static scan on a directory. In this scenario, we will try to scan **aws/** directory to find security issues.

```bash
terrascan scan -t aws -d aws

Violation Details -

        Description    :        Ensure S3 buckets have access logging enabled.
        File           :        s3.tf
        Module Name    :        root
        Plan Root      :        ./
        Line           :        69
        Severity       :        MEDIUM
        -----------------------------------------------------------------------

        Description    :        Ensure S3 buckets have access logging enabled.
        File           :        s3.tf
        Module Name    :        root
        Plan Root      :        ./
        Line           :        1
        Severity       :        MEDIUM
        -----------------------------------------------------------------------

...[SNIP]...

Scan Summary -

        File/Folder         :   /terraform/aws
        IaC Type            :   all
        Scanned At          :   2021-07-04 06:40:18.004553725 +0000 UTC
        Policies Validated  :   143
        Violated Policies   :   43
        Low                 :   4
        Medium              :   20
        High                :   19
```

As you can see, **terrascan** found security issues in our Terraform configurations/code.

Lets move to the **next step**.

### Challenge


1. Read the terrascan documentation
2. Mark a **low** severity issue as **False Positive**
3. How would you embed this tool in CI pipeline?

**Note**: you can access your GitLab machine by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training and using the credentials below.

Name	Value
**Username**	root
**Password**	pdso-training

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

## Learn how to embed Terrascan into GitHub Actions

### Use Terrascan tool to perform SAST for IaC in GitHub Actions

In this scenario, you will learn how to embed **Terrascan** in **GitHub Actions**.

>Note
>
>DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.

### A simple CI/CD pipeline

> You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named **GitHub Actions** that debuted on **13 November 2019**. **GitHub Actions** is a CI/CD system that is built-in to GitHub with **free** and paid offerings.

Let’s get started!

### 1. Create a new repository

> If you haven’t registered for a GitHub account, please sign up for an account [here](https://github.com/join?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with **Public** or **Private** options, and please ignore **initialize this repository with** section for now.

Click the **Create repository** button.

### 2. Create a Personal Access Token (PAT)

Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting [August 2021](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/).

Let’s create PAT by visiting https://github.com/settings/tokens,then click **Generate new token** button and give your token a name e.g. terraform.

Select **repo** option to access repositories from the command line, and scroll down to generate a new token.

> The token will have a format like this **ghp_xxxxxxxxx**.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use the token whenever needed.

### 3. Initial git setup

To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

```bash
git config --global user.email "your_email@gmail.com"

git config --global user.name "your_username"
```

>You need to use your email and username, which are registered in GitHub.
> 
> Please don’t use your company’s GitHub credentials or token to practice these exercises.

### 4. Download the repository

Let’s start by cloning **terraform** in DevSecOps Box.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/terraform.git
```

By cloning the above repository, we created a local copy of the remote repository.

Let’s **cd** into this repository to explore its content.

```bash
cd terraform
```

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

```bash
git remote rename origin old-origin
```

In the command below, please change “username” with your GitHub username.

```bash
git remote add origin https://github.com/username/terraform.git
```

Let’s check the status of our git repository.

```bash
git status
On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
```

We are in the master branch and we need to create one more branch called main as a default branch.

```bash
git checkout -b main
```

> Why do we need a new branch? Because in this exercise we will use the **main** branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.
>
>Read more about [Renaming the default branch from master](https://github.com/github/renaming).

Then, let’s push the code to the GitHub repository.

```bash
git push -u origin main
```

And enter your GitHub credentials when prompted (please use **Personal Access Toke**n as a password), then the code will be pushed to the GitHub repository.

### 5. Add a workflow file to the repository

To use **GitHub Actions**, you need to create **.github/workflows** directory and create a new YAML file named main.yaml or any other desired name because each file in the **.github/workflows** directory which has a **.yaml** extension will define a workflow.

Let’s create a simple workflow by entering the following commands in DevSecOps Box.

```bash
mkdir -p .github/workflows

cat >.github/workflows/main.yaml<<EOF
name: Terraform                               # workflow name

on:
  push:                                       
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - run: echo "This is a build step"

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - run: echo "This is a test step"

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
      - run: echo "This is a deploy step"
EOF
```

> If you are not comfortable with the syntax, explore the **GitHub Actions** syntax athttps://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#about-yaml-syntax-for-workflows

Let’s add this file to the repository and commit the changes.

```bash
git add .github/workflows/main.yaml

git commit -m "Add github workflows"
```

### 6. Push the changes to the repository

Since git is a decentralized source code management system, all changes are made in your local git repository. You have to push these changes to the remote server for the committed changes to reflect on the remote git repository.

Let’s push the changes to the remote git repository using the **git push** command.

```bash
git push origin main

Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
```

### 7. Verify the pipeline runs

Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our **terraform** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

> You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

Let’s move to the **next step**.

### Embed Terrascan in GitHub Actions

As discussed in the **Secure IaC using Terrascan** exercise, we can embed Terrascan in our CI/CD pipeline. We will
embedding Terrascan in GitHub Actions through a Terrascan action available from the GitHub marketplace, please visit the following link to see the details of **Terrascan** action [here](https://github.com/marketplace/actions/terrascan-iac-scanner).

Go back to the DevSecOps Box machine, and replace the content of the **build** job in **.github/workflows/main.yaml** file with the below content.

```bash
  terrascan:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Run Terrascan
        id: terrascan
        uses: accurics/terrascan-action@v1
        with:
          iac_type: 'terraform'
          iac_version: 'v14'
          policy_type: 'aws'
          only_warn: true
          iac_dir: 'aws'
```

> **action** is similar to a plugin or a template that is offered by the provider themselves.

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our **terraform** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

## Learn how to embed Terrascan into CircleCI
Use Terrascan tool to perform SAST for IaC in CircleCI
In this scenario, you will learn how to embed Terrascan in CircleCI.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.
Initial Setup
You’ve learned about CI/CD systems such as GitLab, Jenkins, GitHub Actions and so on. Remember every CI/CD system has its own advantages, and limitations, we just need to find what is suitable for our needs.

Now, we will look into another CI/CD system called CircleCI, this system doesn’t have a built-in Git repository like GitLab or GitHub. But it can be integrated with GitHub or Bitbucket as the repository, so let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with Public or Private options, and please ignore Initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens, then click Generate new token button and give your token a name e.g. terraform.

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
Let’s start by cloning terraform in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/terraform.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd terraform

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/terraform.git

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

Next, you will see the repository lists which has a button called Set Up Project. Select terraform repository then click on that button to start using CircleCI as our CI/CD pipeline and you will get a pop-up with Select a config.yml file for terraform message, please ignore it for now because we will create the CircleCI YML file in our GitHub repository.

Let’s move to the next step.
A simple CI/CD pipeline
You need to create .circleci directory and create a new YAML file named config.yml and add the following CI script.

mkdir -p .circleci

cat >.circleci/config.yml<<EOF
jobs:
  build:
    machine: true
    steps:
      - checkout
      - run: echo "This is a build step"

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
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
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.

Let’s move to the next step.

Embed Terrascan in CircleCI
As discussed in the Secure IaC using Terrascan exercise, we can embed Terrascan in our GitHub Actions. However, you need to test the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and replace the content of the build job in .circleci/config.yml file.

  terrascan:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src accurics/terrascan scan -t aws -d /src/aws -o json | tee terrascan-output.json

      - store_artifacts:
          path: terrascan-output.json
          destination: terrascan-artifact
Please remember to replace the above job name to terrascan in workflows section as shown below:

workflows:
  version: 2
  terraform:
    jobs:
      - terrascan
      - test:
          requires:
            - terrascan
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.
Allow the job failure
We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the when: always syntax to not fail the build even though the tool found security issues.

  terrascan:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src accurics/terrascan scan -t aws -d /src/aws -o json | tee terrascan-output.json

      - store_artifacts:
          path: terrascan-output.json
          destination: terrascan-artifact
          when: always              # Even if the job fails, continue to the next stages
The final pipeline would look like the following:

jobs:
  terrascan:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src accurics/terrascan scan -t aws -d /src/aws -o json | tee terrascan-output.json

      - store_artifacts:
          path: terrascan-output.json
          destination: terrascan-artifact
          when: always              # Even if the job fails, continue to the next stages

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
    jobs:
      - terrascan
      - test:
          requires:
            - terrascan
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
Go ahead and add the above content to the .circleci/config.yml file to run the pipeline.

You will notice that the terrascan job failed however it didn’t block others from continuing further.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.
## Use tfsec to find security issues in IaC
Learn how to use tfsec
In this scenario, you will learn how to install an IaC static analysis tool called tfsec and run it on the Terraform (IaC) code.

You will need to install the tool, download the code, run the scan on the code and evaluate your infrastructure’s security posture.
Install tfsec
tfsec uses static analysis of your terraform templates to spot potential security issues.

You can find more details about the project at https://github.com/aquasecurity/tfsec.

We will do all the exercises locally first in DevSecOps-Box, so let’s start the exercise.

Let’s install tfsec on the system to perform static analysis on your IaC.

wget -O /usr/local/bin/tfsec https://github.com/aquasecurity/tfsec/releases/download/v0.55.0/tfsec-linux-amd64

chmod +x /usr/local/bin/tfsec

We have successfully installed tfsec.

Let’s move to the next step.
Download vulnerable infrastructure
Let’s clone an example IaC (terraform) repository with the following command.

git clone https://gitlab.practical-devsecops.training/pdso/terraform.git

Let’s move into terraform directory.

cd terraform

We can use -h to check the various options provided by this tool.

tfsec -h
tfsec is a simple tool to detect potential security vulnerabilities in your terraformed infrastructure.

Usage:
  tfsec [directory] [flags]

Flags:
  -p, --allow-checks-to-panic        Allow panics to propagate up from rule checking
      --concise-output               Reduce the amount of output and no statistics
      --config-file string           Config file to use during run
      --custom-check-dir string      Explicitly the custom checks dir location
      --detailed-exit-code           Produce more detailed exit status codes.
  -e, --exclude string               Provide comma-separated list of rule IDs to exclude from run.
      --exclude-downloaded-modules   Remove results for downloaded modules in .terraform folder
      --filter-results string        Filter results to return specific checks only (supports comma-delimited input).
      --force-all-dirs               Don't search for tf files, include everything below provided directory.
  -f, --format string                Select output format: default, json, csv, checkstyle, junit, sarif
      --gif                          Show a celebratory gif in the terminal if no problems are found (default formatter only)
  -h, --help                         help for tfsec
      --ignore-hcl-errors            Stop and report an error if an HCL parse error is encountered
      --ignore-info                  [DEPRECATED] Don't show info results in the output.
      --ignore-warnings              [DEPRECATED] Don't show warnings in the output.
      --include-ignored              Include ignored checks in the result output
      --include-passed               Include passed checks in the result output
      --no-color                     Disable colored output (American style!)
      --no-colour                    Disable coloured output
      --out string                   Set output file
      --run-statistics               View statistics table of current findings.
  -s, --soft-fail                    Runs checks but suppresses error code
      --tfvars-file strings          Path to .tfvars file, can be used multiple times and evaluated in order of specification
      --update                       Update to latest version
      --verbose                      Enable verbose logging
  -v, --version                      Show version information and exit
  -w, --workspace string             Specify a workspace for ignore limits

Lets move to the next step.
Run the tfsec tool
The tfsec perform the static scan on a directory. In this scenario, we will try to scan aws directory to find security issues.

tfsec aws

...[SNIP]...

Check 39

  [AWS001][WARNING] Resource 'aws_s3_bucket.data' has an ACL which allows public access.
  /terraform/aws/s3.tf:7


       4 |   # bucket does not have access logs
       5 |   # bucket does not have versioning
       6 |   bucket        = "${local.resource_prefix.value}-data"
       7 |   acl           = "public-read"
       8 |   force_destroy = true
       9 |   tags = {
      10 |     Name        = "${local.resource_prefix.value}-data"

  Impact:     The contents of the bucket can be accessed publicly
  Resolution: Apply a more restrictive bucket ACL

  See https://tfsec.dev/docs/aws/AWS001/ for more information.

Check 40

  [AWS017][ERROR] Resource 'aws_s3_bucket.financials' defines an unencrypted S3 bucket (missing server_side_encryption_configuration block).
  /terraform/aws/s3.tf:25-37


      22 |   }
      23 | }
      24 |
      25 | resource "aws_s3_bucket" "financials" {
      26 |   # bucket is not encrypted
      27 |   # bucket does not have access logs
      28 |   # bucket does not have versioning
      29 |   bucket        = "${local.resource_prefix.value}-financials"
      30 |   acl           = "private"
      31 |   force_destroy = true
      32 |   tags = {
      33 |     Name        = "${local.resource_prefix.value}-financials"
      34 |     Environment = local.resource_prefix.value
      35 |   }
      36 |
      37 | }
      38 |
      39 | resource "aws_s3_bucket" "operations" {
      40 |   # bucket is not encrypted

  Impact:     The bucket objects could be read if compromised
  Resolution: Configure bucket encryption

  See https://tfsec.dev/docs/aws/AWS017/ for more information.

Check 41

  [AWS018][ERROR] Resource 'aws_security_group.default' should include a description for auditing purposes.
  /terraform/aws/db-app.tf:80-88


      77 |   }
      78 | }
      79 |
      80 | resource "aws_security_group" "default" {
      81 |   name   = "${local.resource_prefix.value}-rds-sg"
      82 |   vpc_id = aws_vpc.web_vpc.id
      83 |
      84 |   tags = {
      85 |     Name        = "${local.resource_prefix.value}-rds-sg"
      86 |     Environment = local.resource_prefix.value
      87 |   }
      88 | }
      89 |
      90 | resource "aws_security_group_rule" "ingress" {
      91 |   type              = "ingress"

  Impact:     Descriptions provide context for the firewall rule reasons
  Resolution: Add descriptions for all security groups anf rules

  See https://tfsec.dev/docs/aws/AWS018/ for more information.

  times
  ------------------------------------------
  disk i/o             9.408914ms
  parsing HCL          61.891µs
  evaluating values    5.173796ms
  running checks       5.273408ms

  counts
  ------------------------------------------
  files loaded         13
  blocks               85
  evaluated blocks     85
  modules              0
  module blocks        0
  ignored checks       0

41 potential problems detected.
As you can see above, tfsec found 41 potential security issues in our Terraform configurations/code.

We can also use other formats like JSON, XML, CSV, etc to save the output in a machine-readable format.

tfsec aws -f json | tee tfsec-output.json

Lets move to the next step.
Challenge
Read the [tfsec documentation](https://github.com/aquasecurity/tfsec)
What’s different between terrascan and tfsec? You can compare it with the result or provided arguments
Think how would you embed this tool in CI pipeline?
Note: you can access your GitLab machine by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training and use the credentials below.

Name	Value
Username	root
Password	pdso-training
Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).
## Learn how to embed tfsec into GitHub Actions
Use tfsec tool to find security issues for IaC in GitHub Actions
In this scenario, you will learn how to embed tfsec in GitHub Actions.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.
A simple CI/CD pipeline
You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named GitHub Actions that debuted on 13 November 2019. GitHub Actions is a CI/CD system that is built-in to GitHub with free and paid offerings.

Let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with Public or Private options, and please ignore initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens,then click Generate new token button and give your token a name e.g. terraform.

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
Let’s start by cloning terraform in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/terraform.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd terraform

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/terraform.git

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
name: Terraform                               # workflow name

on:
  push:                                       
    branches:                                 # similar to "only" in GitLab
      - main

jobs:
  build:
    runs-on: ubuntu-latest                    # similar to "image" in GitLab
    steps:
      - run: echo "This is a build step"

  test:
    runs-on: ubuntu-latest
    needs: build
    steps:
      - run: echo "This is a test step"

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
      - run: echo "This is a deploy step"
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
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
7. Verify the pipeline runs
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our terraform repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.

You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

Let’s move to the next step.
Embed tfsec in GitHub Actions
As discussed in the Secure IaC using tfsec exercise, we can embed tfsec in our CI/CD pipeline. We will
embedding tfsec in GitHub Actions through a tfsec action available from the GitHub marketplace, please visit the following link to see the details of tfsec action here.

Go back to the DevSecOps Box machine, and replace the content of the build job in .github/workflows/main.yaml file with the below content.

  tfsec:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Terraform security scan
        uses: triat/terraform-security-scan@v2.2.1
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
action is similar to a plugin or a template that is offered by the provider themselves.

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our terraform repository, clicking the Actions tab, and selecting the appropriate workflow name to see the output.
## Learn how to embed tfsec into CircleCI
Use tfsec tool to find security issues for IaC in CircleCI
In this scenario, you will learn how to embed tfsec in CircleCI.

Note

DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials
Initial Setup
You’ve learned about CI/CD systems such as GitLab, Jenkins, GitHub Actions and so on. Remember every CI/CD system has its own advantages, and limitations, we just need to find what is suitable for our needs.

Now, we will look into another CI/CD system called CircleCI, this system doesn’t have a built-in Git repository like GitLab or GitHub. But it can be integrated with GitHub or Bitbucket as the repository, so let’s get started!

1. Create a new repository
If you haven’t registered for a GitHub account, please sign up for an account here

First, we need to create a repository in our GitHub account by visiting https://github.com/new.

Create a repository named terraform, you can also check the box with Public or Private options, and please ignore Initialize this repository with section for now.

Click the Create repository button.

2. Create a Personal Access Token (PAT)
Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting August 2021.

Let’s create PAT by visiting https://github.com/settings/tokens, then click Generate new token button and give your token a name e.g. terraform.

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
Let’s start by cloning terraform in DevSecOps Box.

git clone https://gitlab.practical-devsecops.training/pdso/terraform.git

By cloning the above repository, we created a local copy of the remote repository.

Let’s cd into this repository to explore its content.

cd terraform

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

git remote rename origin old-origin

In the command below, please change “username” with your GitHub username.

git remote add origin https://github.com/username/terraform.git

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

Next, you will see the repository lists which has a button called Set Up Project. Select terraform repository then click on that button to start using CircleCI as our CI/CD pipeline and you will get a pop-up with Select a config.yml file for terraform message, please ignore it for now because we will create the CircleCI YML file in our GitHub repository.

Let’s move to the next step.
A simple CI/CD pipeline
You need to create .circleci directory and create a new YAML file named config.yml and add the following CI script.

mkdir -p .circleci

cat >.circleci/config.yml<<EOF
jobs:
  build:
    machine: true
    steps:
      - checkout
      - run: echo "This is a build step"

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
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
To https://github.com/username/terraform.git
   df066a2..98e754f  main -> main
Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.

Let’s move to the next step
Embed tfsec in CircleCI
As discussed in the Secure IaC using tfsec exercise, we can embed tfsec in our GitHub Actions. However, you need to test the command manually before you embed this SAST tool in the pipeline.

Go back to the DevSecOps Box machine, and replace the content of the build job in .circleci/config.yml file.

  tfsec:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src tfsec/tfsec /src -f json | tee tfsec-output.json

      - store_artifacts:
          path: tfsec-output.json
          destination: tfsec-artifact
Please remember to replace the above job name to tfsec in workflows section as shown below:

workflows:
  version: 2
  terraform:
    jobs:
      - tfsec
      - test:
          requires:
            - tfsec
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.
Allow the job failure
We do not want to fail the builds/jobs/scan in DevSecOps Maturity Levels 1 and 2, as security tools spit a significant amount of false positives.

You can use the when: always syntax to not fail the build even though the tool found security issues.

  tfsec:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src tfsec/tfsec /src -f json | tee tfsec-output.json

      - store_artifacts:
          path: tfsec-output.json
          destination: tfsec-artifact
          when: always              # Even if the job fails, continue to the next stages
The final pipeline would look like the following:

jobs:
  tfsec:
    machine: true
    steps:
      - checkout

      - run: docker run --rm -v $(pwd):/src tfsec/tfsec /src -f json | tee tfsec-output.json

      - store_artifacts:
          path: tfsec-output.json
          destination: tfsec-artifact
          when: always              # Even if the job fails, continue to the next stages

  test:
    machine: true
    steps:
      - checkout
      - run: echo "This is a test step"

  integration:
    machine: true
    steps:
      - checkout
      - run:
          command: |
            echo "This is an integration step"
            exit 1

  prod:
    machine: true
    steps:
      - checkout
      - run: echo "This is a deploy step"

workflows:
  version: 2
  terraform:
    jobs:
      - tfsec
      - test:
          requires:
            - tfsec
      - integration:
          requires:
            - test
      - prod:
          type: approval
          requires:
            - integration
Go ahead and add the above content to the .circleci/config.yml file to run the pipeline.

You will notice that the tfsec job failed however it didn’t block others from continuing further.

As discussed, any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our CircleCI account. Click Projects, select terraform repository and select the appropriate pipeline to see the output.
## Use Snyk tool to find security issues in your IaC
Learn to find security issues in Terraform resources using Snyk
In this scenario, you will learn how to perform SAST for Infrastructure as Code (IaC) using the Snyk tool.
Download the source code
We will do all the exercises locally first in DevSecOps-Box, so let’s get started.

First, we need to download the source code of the project from our git repository.

git clone https://gitlab.practical-devsecops.training/pdso/terraform

Let’s cd into the application code so we can scan the app.

cd terraform

We are now in the terraform directory.

Let’s move to the next step.
Install Synk
CLI and build-time tool to find & fix known vulnerabilities in open-source dependencies, apart from Software Component Analysis (SCA), Snyk also support to perform SAST for Infrastructure as Code like Terraform.

Source: Snyk Github Page

Let’s download the Snyk.

wget -O /usr/local/bin/snyk https://github.com/snyk/snyk/releases/download/v1.573.0/snyk-linux

Give executable permission to the Snyk binary.

chmod +x /usr/local/bin/snyk

Let’s explore what options Snyk provides us.

snyk iac --help

NAME
       snyk-iac - Find security issues in your Infrastructure as Code files

SYNOPSIS
       snyk iac [COMMAND] [OPTIONS] PATH

DESCRIPTION
       Find security issues in your Infrastructure as Code files.

       For more information see IaC help page https://snyk.co/ucT6Q

COMMANDS
       test   Test for any known issue.

OPTIONS
       --detection-depth=DEPTH
              (only in test command)
              Indicate  the  maximum depth of sub-directories to search. DEPTH
              must be a number.

              Default: No Limit
              Example: --detection-depth=3
              Will limit search to provided directory (or current directory if
              no PATH provided) plus two levels of subdirectories.

       --severity-threshold=low|medium|high
              Only report vulnerabilities of provided level or higher.

       --json Prints results in JSON format.

       --json-file-output=OUTPUT_FILE_PATH
              (only  in test command) Save test output in JSON format directly
              to the specified file, regardless of whether or not you use  the
              --json  option. This is especially useful if you want to display
              the human-readable test output via stdout and at the  same  time
              save the JSON format output to a file.

       --sarif
              Return results in SARIF format.

       --sarif-file-output=OUTPUT_FILE_PATH
              (only in test command) Save test output in SARIF format directly
              to the OUTPUT_FILE_PATH file, regardless of whether or  not  you
              use the --sarif option. This is especially useful if you want to
              display the human-readable test output via  stdout  and  at  the
              same time save the SARIF format output to a file.

       --experimental
              (only  in  test  command) Enable an experimental feature to scan
              configuration files locally on your machine. This  feature  also
              gives you the ability to scan terraform plan JSON files.

   Flags available accross all commands
       --insecure
              Ignore unknown certificate authorities.

       -d     Output debug logs.

       --quiet, -q
              Silence all output.

       --version, -v
              Prints versions.

       [COMMAND] --help, --help [COMMAND], -h
              Prints  a  help  text. You may specify a COMMAND to get more de-
              tails.

EXAMPLES
       For more information see IaC help page https://snyk.co/ucT6Q

       Test kubernetes file
              $ snyk iac test /path/to/Kubernetes.yaml

       Test terraform file
              $ snyk iac test /path/to/terraform_file.tf

       Test terraform plan file
              $ snyk iac test /path/to/tf-plan.json --experimental

       Test matching files in a directory
              $ snyk iac test /path/to/directory

EXIT CODES
       Possible exit codes and their meaning:

       0: success, no vulns found
       1: action_needed, vulns found
       2: failure, try to re-run command
       3: failure, no supported projects detected

ENVIRONMENT
       You can set these environment variables to change CLI run settings.

       SNYK_TOKEN
              Snyk authorization token. Setting this envvar will override  the
              token that may be available in your snyk config settings.

              How to get your account token https://snyk.co/ucT6J
              How to use Service Accounts https://snyk.co/ucT6L


       SNYK_CFG_KEY
              Allows  you  to  override  any key that's also available as snyk
              config option.

              E.g. SNYK_CFG_ORG=myorg will override default org option in con-
              fig with "myorg".

       SNYK_REGISTRY_USERNAME
              Specify  a  username  to use when connecting to a container reg-
              istry. Note that using the --username flag  will  override  this
              value.  This  will  be  ignored in favour of local Docker binary
              credentials when Docker is present.

       SNYK_REGISTRY_PASSWORD
              Specify a password to use when connecting to  a  container  reg-
              istry.  Note  that  using the --password flag will override this
              value. This will be ignored in favour  of  local  Docker  binary
              credentials when Docker is present.

Connecting to Snyk API
       By default Snyk CLI will connect to https://snyk.io/api/v1.

       SNYK_API
              Sets  API  host  to use for Snyk requests. Useful for on-premise
              instances and configuring proxies. If set with http protocol CLI
              will  upgrade  the  requests  to  https. Unless SNYK_HTTP_PROTO-
              COL_UPGRADE is set to 0.

       SNYK_HTTP_PROTOCOL_UPGRADE=0
              If set to the value of 0, API requests aimed at http  URLs  will
              not  be upgraded to https. If not set, the default behavior will
              be to upgrade these requests from http to  https.  Useful  e.g.,
              for reverse proxies.

       HTTPS_PROXY and HTTP_PROXY
              Allows  you  to specify a proxy to use for https and http calls.
              The https in the HTTPS_PROXY means  that  requests  using  https
              protocol  will  use this proxy. The proxy itself doesn't need to
              use https.

NOTICES
   Snyk API usage policy
       The use of Snyk's API, whether through the use of the 'snyk' npm  pack-
       age   or   otherwise,   is   subject   to   the   terms   &  conditions
       https://snyk.co/ucT6N
Let’s move to the next step.
Run the Scanner
As we have learned in the DevSecOps Gospel, we should save the output in a machine-readable format. We are using –json argument to output the results in JSON format.

snyk iac test aws --json

FailedToGetIacOrgSettingsError: Failed to fetch IaC organization settings
    at /snapshot/snyk/dist/cli/commands/test/iac-local-execution/org-settings/get-iac-org-settings.js:26:31
    at requestWrapper (/snapshot/snyk/dist/lib/request/index.js:15:13)
    at processTicksAndRejections (internal/process/task_queues.js:97:5)
We are greeted with an error. Snyk is complaining about missing API Token as it’s a paid solution and wants you to register before it can run the scan.

If you haven’t registered before, you can go to Snyk website and click SIGN UP FOR FREE -> Select Google account options -> Complete sign up -> Select CLI and go to the account settings page https://app.snyk.io/account and copy the token.

Do not use your company’s Synk Credentials/Token to practice these exercises. Please sign up for a Free Account instead.

Once you have the token, you can authenticate to the Snyk service using the snyk auth command.

snyk auth YOUR_API_TOKEN_HERE

Your account has been authenticated. Snyk is now ready to be used.
Or you can also export the environment variable using the following command.

export SNYK_TOKEN=YOUR_TOKEN_HERE

Now that we are authenticated, we can now scan our Terraform code with the snyk iac test command.

snyk iac test aws --json > snyk-output.json

Note

aws is a target directory, you can change it to specific path of your Terraform resources.