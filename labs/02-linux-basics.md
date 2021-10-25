# Linux Basics

## Indice

- Basic of Running Commands (Mandatory)
- Basics of Files and Directories (Mandatory)
- Basics of Command Outputs (Mandatory)
- Basics of Files Permissions (Mandatory)
- Linux Exit Code (Mandatory)
- Git Basics (Mandatory)
- SSH Basics (Mandatory)

## dont remember

https://opensource.com/article/18/8/introduction-pipes-linux

## Basics of File Permissions

https://portal.practical-devsecops.training/courses/devsecops-professional/linux-basics/learn-the-linux-permissions/

john@devsecops-box-xIKciVAk:~$ cat /etc/shadow
pdevsecops

## Linux Exit Code

https://tldp.org/LDP/abs/html/exitcodes.html

> Developers of a security tool are liberal in choosing to return non-zero exit codes. That is, when a vulnerability is found, some security tools return an exit code of 13, some security tools return an exit code of 255, some security tools return an exit code of 1. In all cases, 13, 255, and 1 are non-zero exit codes indicating the presence of a vulnerability.

cat > myfaketool << EOL
#!/bin/bash
vulncount=\$((0 + \$RANDOM % 3)); #randomly fake vulnerability count
if [ \$vulncount -eq 0 ];
then
        echo "No Vulnerabilities";
        exit 0
else
        echo "Vulnerabilities found: \$vulncount";
        exit 99
fi
EOL

chmod +x myfaketool

./myfaketool

echo $?

## Git Basics

git config --global user.email "student@pdevsecops.com"
git config --global user.name "student"
git clone http://root:pdso-training@gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv.git
cd django-nv
ls -l
cat > myfile <<EOL
This is my file
EOL
echo "Practical DevSecOps" >> README.md
git status
git add myfile README.md
git status
git commit -m "Add myfile and update README.md"
git push

GitLab credentials: https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv
Name	Value
Username	root
Password	pdso-training

## SSH Basics

ssh -i ~/.ssh/id_rsa root@prod-xIKciVAk