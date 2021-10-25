# Dynamic Application Security Testing (DAST)

- [Dynamic Analysis using Nikto (Mandatory)](#dynamic-analysis-using-nikto)
- [Dynamic Analysis using NMAP (Mandatory)](#dynamic-analysis-using-nmap)
- [Dynamic Analysis using SSLyze (Mandatory)](#dynamic-analysis-using-sslyze)
- [Embed Nikto, SSLyze, and Nmap into GitLab (Mandatory)](#embed-nikto,-sslyze,-and-nmap-into-gitlab)
- [Dynamic Analysis using ZAP (Mandatory)](#dynamic-analysis-using-zap)
- [How to Embed Zed Attack Proxy (ZAP) into GitLab (Mandatory)](#how-to-embed-zed-attack-proxy-zap-into-gitlab)
- [How to Embed Zed Attack Proxy (ZAP) into Jenkins (Optional)](#how-to-embed-zed-attack-proxy-zap-into-jenkins)
- [How to Embed Zed Attack Proxy (ZAP) into GitHub Actions (Optional)](#how-to-embed-zed-attack-proxy-zap-into-github-actions)
- [How to Embed Zed Attack Proxy (ZAP) into CircleCI (Optional)](#how-to-embed-zed-attack-proxy-zap-into-circleci)

## Dynamic Analysis using Nikto

### Learn how to use the Nikto tool to find security issues in an application

In this scenario, you will learn how to install DAST tool and run DAST Scans on the Application.

You will need to install the DAST tool and then run the DAST scan on our Application.

### Install Nikto

> Nikto is a web server assessment tool. It’s designed to find various default and insecure files, configurations, and programs on any type of web server.
>
> Nikto is built on LibWhisker2 (by RFP) and can run on any platform which has a Perl environment. It supports SSL, proxies, host authentication, attack encoding, and more.
>
> Source: [Nikto official](https://cirt.net/Nikto2).

Let’s install Nikto to perform Dynamic Analysis.

```bash
apt install -y libnet-ssleay-perl
```

> The above command is needed to support SSL scan in Nikto

```bash
git clone https://github.com/sullo/nikto
cd nikto/program
```

We have successfully installed the Nikto scanner. Let’s explore the functionality it provides us.

```
./nikto.pl -Help
Option host requires an argument

   Options:
       -ask+               Whether to ask about submitting updates
                               yes   Ask about each (default)
                               no    Don't ask, don't send
                               auto  Don't ask, just send
       -Cgidirs+           Scan these CGI dirs: "none", "all", or values like "/cgi/ /cgi-a/"
       -config+            Use this config file
       -Display+           Turn on/off display outputs:
                               1     Show redirects
                               2     Show cookies received
                               3     Show all 200/OK responses
                               4     Show URLs which require authentication
                               D     Debug output
                               E     Display all HTTP errors
                               P     Print progress to STDOUT
                               S     Scrub output of IPs and hostnames
                               V     Verbose output
       -dbcheck           Check database and other key files for syntax errors
       -evasion+          Encoding technique:
                               1     Random URI encoding (non-UTF8)
                               2     Directory self-reference (/./)
                               3     Premature URL ending
                               4     Prepend long random string
                               5     Fake parameter
                               6     TAB as request spacer
                               7     Change the case of the URL
                               8     Use Windows directory separator (\)
                               A     Use a carriage return (0x0d) as a request spacer
                               B     Use binary value 0x0b as a request spacer
       -Format+           Save file (-o) format:
                               csv   Comma-separated-value
                               json  JSON Format
                               htm   HTML Format
                               nbe   Nessus NBE format
                               sql   Generic SQL (see docs for schema)
                               txt   Plain text
                               xml   XML Format
                               (if not specified the format will be taken from the file extension passed to -output)
       -Help              This help information
       -host+             Target host/URL
       -404code           Ignore these HTTP codes as negative responses (always). Format is "302,301".
       -404string         Ignore this string in response body content as negative response (always). Can be a regular expression.
       -id+               Host authentication to use, format is id:pass or id:pass:realm
       -key+              Client certificate key file
       -list-plugins      List all available plugins, perform no testing
       -maxtime+          Maximum testing time per host (e.g., 1h, 60m, 3600s)
       -mutate+           Guess additional file names:
                               1     Test all files with all root directories
                               2     Guess for password file names
                               3     Enumerate user names via Apache (/~user type requests)
                               4     Enumerate user names via cgiwrap (/cgi-bin/cgiwrap/~user type requests)
                               5     Attempt to brute force sub-domain names, assume that the host name is the parent domain
                               6     Attempt to guess directory names from the supplied dictionary file
       -mutate-options    Provide information for mutates
       -nointeractive     Disables interactive features
       -nolookup          Disables DNS lookups
       -nossl             Disables the use of SSL
       -no404             Disables nikto attempting to guess a 404 page
       -Option            Over-ride an option in nikto.conf, can be issued multiple times
       -output+           Write output to this file ('.' for auto-name)
       -Pause+            Pause between tests (seconds)
       -Plugins+          List of plugins to run (default: ALL)
       -port+             Port to use (default 80)
       -RSAcert+          Client certificate file
       -root+             Prepend root value to all requests, format is /directory
       -Save              Save positive responses to this directory ('.' for auto-name)
       -ssl               Force ssl mode on port
       -Tuning+           Scan tuning:
                               1     Interesting File / Seen in logs
                               2     Misconfiguration / Default File
                               3     Information Disclosure
                               4     Injection (XSS/Script/HTML)
                               5     Remote File Retrieval - Inside Web Root
                               6     Denial of Service
                               7     Remote File Retrieval - Server Wide
                               8     Command Execution / Remote Shell
                               9     SQL Injection
                               0     File Upload
                               a     Authentication Bypass
                               b     Software Identification
                               c     Remote Source Inclusion
                               d     WebService
                               e     Administrative Console
                               x     Reverse Tuning Options (i.e., include all except specified)
       -timeout+          Timeout for requests (default 10 seconds)
       -Userdbs           Load only user databases, not the standard databases
                               all   Disable standard dbs and load only user dbs
                               tests Disable only db_tests and load udb_tests
       -useragent         Over-rides the default useragent
       -until             Run until the specified time or duration
       -update            Update databases and plugins from CIRT.net
       -url+              Target host/URL (alias of -host)
       -useproxy          Use the proxy defined in nikto.conf, or argument http://server:port
       -Version           Print plugin and database versions
       -vhost+            Virtual host (for Host header)
                + requires a value
```

Let’s move to the **next step**.

### Run the Scanner

As we have learned in the **DevSecOps Gospel**, we should save the output in the machine-readable format (**CSV**, **JSON**, **XML**) so it can be parsed by the machines easily.

Let’s run the Nikto with the following options.

```bash
./nikto.pl -output nikto_output.xml -h prod-xIKciVAk
```

- **-h**: flag used to set the target application which we want to scan
- **-output**: flag used to set the output file in which we want to store the result

```
- Nikto v2.1.6
---------------------------------------------------------------------------
+ Target IP:          x.x.x.x
+ Target Hostname:    prod-xIKciVAk.lab.practical-devsecops.training
+ Target Port:        80
+ Start Time:         2021-07-11 05:46:26 (GMT0)
---------------------------------------------------------------------------
+ Server: nginx/1.14.0 (Ubuntu)
+ The X-Content-Type-Options header is not set. This could allow the user agent to render the content of the site in a different fashion to the MIME type.
+ No CGI Directories found (use '-C all' to force check all possible dirs)
+ nginx/1.14.0 appears to be outdated (current is at least 1.18.0)
+ OSVDB-17113: /SilverStream: SilverStream allows directory listing
+ 8135 requests: 0 error(s) and 3 item(s) reported on remote host
+ End Time:           2021-07-11 05:47:14 (GMT0) (48 seconds)
---------------------------------------------------------------------------
+ 1 host(s) tested
```

Now, executing **cat** command on the output file will show the **nikto** result in **XML** format.

```
cat nikto_output.xml
<?xml version="1.0" ?>
<!DOCTYPE niktoscan SYSTEM "docs/nikto.dtd">
<niktoscan>
<niktoscan hoststest="0" options="-h prod-xIKciVAk.lab.practical-devsecops.training -output nikto_output.xml" version="2.1.6" scanstart="Sun Jul 11 05:47:55 
2021" scanend="Thu Jan  1 00:00:00 1970" scanelapsed=" seconds" nxmlversion="1.2">

<scandetails targetip="134.209.130.45" targethostname="prod-xIKciVAk.lab.practical-devsecops.training" targetport="80" targetbanner="nginx/1.14.0 (Ubuntu)" s
tarttime="2021-07-11 05:47:56" sitename="http://prod-xIKciVAk.lab.practical-devsecops.training:80/" siteip="http://134.209.130.45:80/" hostheader="prod-wciku
6wj.lab.practical-devsecops.training" errors="0" checks="6955">


<item id="999103" osvdbid="0" osvdblink="" method="GET">
<description><![CDATA[The X-Content-Type-Options header is not set. This could allow the user agent to render the content of the site in a different fashion 
to the MIME type.]]></description>
<uri><![CDATA[/]]></uri>
<namelink><![CDATA[http://prod-xIKciVAk.lab.practical-devsecops.training:80/]]></namelink>
<iplink><![CDATA[http://134.209.130.45:80/]]></iplink>
</item>

<item id="600575" osvdbid="0" osvdblink="" method="HEAD">
<description><![CDATA[nginx/1.14.0 appears to be outdated (current is at least 1.18.0)]]></description>
<uri><![CDATA[/]]></uri>
<namelink><![CDATA[http://prod-xIKciVAk.lab.practical-devsecops.training:80/]]></namelink>
<iplink><![CDATA[http://134.209.130.45:80/]]></iplink>
</item>

<item id="000398" osvdbid="17113" osvdblink="https://vulners.com/osvdb/OSVDB:17113" method="GET">
<description><![CDATA[/SilverStream: SilverStream allows directory listing]]></description>
<uri><![CDATA[/SilverStream]]></uri>
<namelink><![CDATA[http://prod-xIKciVAk.lab.practical-devsecops.training:80/SilverStream]]></namelink>
<iplink><![CDATA[http://134.209.130.45:80/SilverStream]]></iplink>
</item>

<statistics elapsed="55" itemsfound="3" itemstested="6955" endtime="2021-07-11 05:48:51" />
</scandetails>

</niktoscan>


</niktoscan>
```

Let’s move to the **next step**.

### "Challenge: Dynamic Analysis using Nikto"

1. Read the [Nikto documentation](https://github.com/sullo/nikto/wiki)
2. List the available plugins in **Nikto**

```bash
./nikto.pl -list-plugins |  grep "Plugin:"
```

3. Can you run a specific Nikto plugin to perform any scan? You can select any plugin of your choice from the list (e.g. **headers**, **dictionary**, etc)

```bash
./nikto.pl -output nikto_output.xml -h prod-xIKciVAk -Plugins headers
./nikto.pl -output nikto_output.xml -h prod-xIKciVAk -Plugins dictionary
```

4. Create a Nikto configuration file(nikto.conf) to satisfy the below criteria:
    - Remove the ports that would never be scanned

```
cat > nikto.conf <<EOL
SKIPPORTS=22 21 
EOL
./nikto.pl -output nikto_output.csv -h prod-xIKciVAk 
```

    - Remove the False Positives

```
# https://fossies.org/linux/nikto/program/docs/nikto_manual.html
cat > nikto.conf <<EOL
CLIOPTS=-output nikto_output.xml -h prod-xIKciVAk
SKIPIDS=999103 600575 000398
EOL
./nikto.pl
```
    - Save the output in CSV format

```
cat > nikto.conf <<EOL
CLIOPTS=-output nikto_output.csv -h prod-xIKciVAk -Format csv
EOL
./nikto.pl
```

> Please do not forget to share the answer (a screenshot and commands) with our staff via Slack Direct Message (DM).

> Hint: Explore the nikto config variables in this [link](https://github.com/sullo/nikto/wiki/Config-Variables)

## Dynamic Analysis using NMAP

### Learn how to perform port scanning on a server

In this scenario, you will learn how to install the DAST tool and run port scanning (DAST) on the web server in which we host our Web Application.

You will need to install the DAST tool and then run the DAST scan against the server.

### Install DAST Tool

> Nmap (“Network Mapper”) is a free and open source (license) utility for network discovery and security auditing. Many systems and network administrators also find it useful for tasks such as network inventory, managing service upgrade schedules, and monitoring host or service uptime.
> Source [Nmap official website](https://nmap.org/)

Let’s install **nmap** to perform **Dynamic Analysis**.

```bash
apt-get update && apt-get install nmap -y
Get:1 http://security.ubuntu.com/ubuntu bionic-security InRelease [88.7 kB]
Hit:2 https://download.docker.com/linux/ubuntu bionic InRelease                                                                    
Hit:3 http://archive.ubuntu.com/ubuntu bionic InRelease                                                                            
Get:4 http://security.ubuntu.com/ubuntu bionic-security/main amd64 Packages [2221 kB]
Get:5 http://security.ubuntu.com/ubuntu bionic-security/universe amd64 Packages [1418 kB]       
Get:6 http://archive.ubuntu.com/ubuntu bionic-updates InRelease [88.7 kB]                                 
Get:7 http://archive.ubuntu.com/ubuntu bionic-backports InRelease [74.6 kB]           
Get:8 http://archive.ubuntu.com/ubuntu bionic-updates/universe amd64 Packages [2188 kB]
Get:9 http://archive.ubuntu.com/ubuntu bionic-updates/main amd64 Packages [2658 kB]
Fetched 8737 kB in 1s (7297 kB/s)                         
Reading package lists... Done
Reading package lists... Done
Building dependency tree       
Reading state information... Done
The following additional packages will be installed:
  libblas3 liblinear3 liblua5.3-0 libpcap0.8
Suggested packages:
  liblinear-tools liblinear-dev ndiff
The following NEW packages will be installed:
  libblas3 liblinear3 liblua5.3-0 libpcap0.8 nmap
0 upgraded, 5 newly installed, 0 to remove and 7 not upgraded.
Need to get 5585 kB of archives.
After this operation, 25.3 MB of additional disk space will be used.
Get:1 http://archive.ubuntu.com/ubuntu bionic-updates/main amd64 libpcap0.8 amd64 1.8.1-6ubuntu1.18.04.2 [118 kB]
Get:2 http://archive.ubuntu.com/ubuntu bionic/main amd64 libblas3 amd64 3.7.1-4ubuntu1 [140 kB]
Get:3 http://archive.ubuntu.com/ubuntu bionic/main amd64 liblinear3 amd64 2.1.0+dfsg-2 [39.3 kB]
Get:4 http://archive.ubuntu.com/ubuntu bionic-updates/main amd64 liblua5.3-0 amd64 5.3.3-1ubuntu0.18.04.1 [115 kB]
Get:5 http://archive.ubuntu.com/ubuntu bionic/main amd64 nmap amd64 7.60-1ubuntu5 [5174 kB]
Fetched 5585 kB in 1s (6437 kB/s)
debconf: delaying package configuration, since apt-utils is not installed
Selecting previously unselected package libpcap0.8:amd64.
(Reading database ... 19683 files and directories currently installed.)
Preparing to unpack .../libpcap0.8_1.8.1-6ubuntu1.18.04.2_amd64.deb ...
Unpacking libpcap0.8:amd64 (1.8.1-6ubuntu1.18.04.2) ...
Selecting previously unselected package libblas3:amd64.
Preparing to unpack .../libblas3_3.7.1-4ubuntu1_amd64.deb ...
Unpacking libblas3:amd64 (3.7.1-4ubuntu1) ...
Selecting previously unselected package liblinear3:amd64.
Preparing to unpack .../liblinear3_2.1.0+dfsg-2_amd64.deb ...
Unpacking liblinear3:amd64 (2.1.0+dfsg-2) ...
Selecting previously unselected package liblua5.3-0:amd64.
Preparing to unpack .../liblua5.3-0_5.3.3-1ubuntu0.18.04.1_amd64.deb ...
Unpacking liblua5.3-0:amd64 (5.3.3-1ubuntu0.18.04.1) ...
Selecting previously unselected package nmap.
Preparing to unpack .../nmap_7.60-1ubuntu5_amd64.deb ...
Unpacking nmap (7.60-1ubuntu5) ...
Setting up libblas3:amd64 (3.7.1-4ubuntu1) ...
update-alternatives: using /usr/lib/x86_64-linux-gnu/blas/libblas.so.3 to provide /usr/lib/x86_64-linux-gnu/libblas.so.3 (libblas.so.3-x86_64-linux-gnu) in auto mode
Setting up liblinear3:amd64 (2.1.0+dfsg-2) ...
Setting up liblua5.3-0:amd64 (5.3.3-1ubuntu0.18.04.1) ...
Setting up libpcap0.8:amd64 (1.8.1-6ubuntu1.18.04.2) ...
Setting up nmap (7.60-1ubuntu5) ...
Processing triggers for libc-bin (2.27-3ubuntu1.4) ...
```

We have successfully installed **Nmap** scanner, let’s explore the functionality it provides us.

```bash
nmap -help
Nmap 7.60 ( https://nmap.org )
Usage: nmap [Scan Type(s)] [Options] {target specification}
TARGET SPECIFICATION:
  Can pass hostnames, IP addresses, networks, etc.
  Ex: scanme.nmap.org, microsoft.com/24, 192.168.0.1; 10.0.0-255.1-254
  -iL <inputfilename>: Input from list of hosts/networks
  -iR <num hosts>: Choose random targets
  --exclude <host1[,host2][,host3],...>: Exclude hosts/networks
  --excludefile <exclude_file>: Exclude list from file
HOST DISCOVERY:
  -sL: List Scan - simply list targets to scan
  -sn: Ping Scan - disable port scan
  -Pn: Treat all hosts as online -- skip host discovery
  -PS/PA/PU/PY[portlist]: TCP SYN/ACK, UDP or SCTP discovery to given ports
  -PE/PP/PM: ICMP echo, timestamp, and netmask request discovery probes
  -PO[protocol list]: IP Protocol Ping
  -n/-R: Never do DNS resolution/Always resolve [default: sometimes]
  --dns-servers <serv1[,serv2],...>: Specify custom DNS servers
  --system-dns: Use OS's DNS resolver
  --traceroute: Trace hop path to each host
SCAN TECHNIQUES:
  -sS/sT/sA/sW/sM: TCP SYN/Connect()/ACK/Window/Maimon scans
  -sU: UDP Scan
  -sN/sF/sX: TCP Null, FIN, and Xmas scans
  --scanflags <flags>: Customize TCP scan flags
  -sI <zombie host[:probeport]>: Idle scan
  -sY/sZ: SCTP INIT/COOKIE-ECHO scans
  -sO: IP protocol scan
  -b <FTP relay host>: FTP bounce scan
PORT SPECIFICATION AND SCAN ORDER:
  -p <port ranges>: Only scan specified ports
    Ex: -p22; -p1-65535; -p U:53,111,137,T:21-25,80,139,8080,S:9
  --exclude-ports <port ranges>: Exclude the specified ports from scanning
  -F: Fast mode - Scan fewer ports than the default scan
  -r: Scan ports consecutively - don't randomize
  --top-ports <number>: Scan <number> most common ports
  --port-ratio <ratio>: Scan ports more common than <ratio>
SERVICE/VERSION DETECTION:
  -sV: Probe open ports to determine service/version info
  --version-intensity <level>: Set from 0 (light) to 9 (try all probes)
  --version-light: Limit to most likely probes (intensity 2)
  --version-all: Try every single probe (intensity 9)
  --version-trace: Show detailed version scan activity (for debugging)
SCRIPT SCAN:
  -sC: equivalent to --script=default
  --script=<Lua scripts>: <Lua scripts> is a comma separated list of
           directories, script-files or script-categories
  --script-args=<n1=v1,[n2=v2,...]>: provide arguments to scripts
  --script-args-file=filename: provide NSE script args in a file
  --script-trace: Show all data sent and received
  --script-updatedb: Update the script database.
  --script-help=<Lua scripts>: Show help about scripts.
           <Lua scripts> is a comma-separated list of script-files or
           script-categories.
OS DETECTION:
  -O: Enable OS detection
  --osscan-limit: Limit OS detection to promising targets
  --osscan-guess: Guess OS more aggressively
TIMING AND PERFORMANCE:
  Options which take <time> are in seconds, or append 'ms' (milliseconds),
  's' (seconds), 'm' (minutes), or 'h' (hours) to the value (e.g. 30m).
  -T<0-5>: Set timing template (higher is faster)
  --min-hostgroup/max-hostgroup <size>: Parallel host scan group sizes
  --min-parallelism/max-parallelism <numprobes>: Probe parallelization
  --min-rtt-timeout/max-rtt-timeout/initial-rtt-timeout <time>: Specifies
      probe round trip time.
  --max-retries <tries>: Caps number of port scan probe retransmissions.
  --host-timeout <time>: Give up on target after this long
  --scan-delay/--max-scan-delay <time>: Adjust delay between probes
  --min-rate <number>: Send packets no slower than <number> per second
  --max-rate <number>: Send packets no faster than <number> per second
FIREWALL/IDS EVASION AND SPOOFING:
  -f; --mtu <val>: fragment packets (optionally w/given MTU)
  -D <decoy1,decoy2[,ME],...>: Cloak a scan with decoys
  -S <IP_Address>: Spoof source address
  -e <iface>: Use specified interface
  -g/--source-port <portnum>: Use given port number
  --proxies <url1,[url2],...>: Relay connections through HTTP/SOCKS4 proxies
  --data <hex string>: Append a custom payload to sent packets
  --data-string <string>: Append a custom ASCII string to sent packets
  --data-length <num>: Append random data to sent packets
  --ip-options <options>: Send packets with specified ip options
  --ttl <val>: Set IP time-to-live field
  --spoof-mac <mac address/prefix/vendor name>: Spoof your MAC address
  --badsum: Send packets with a bogus TCP/UDP/SCTP checksum
OUTPUT:
  -oN/-oX/-oS/-oG <file>: Output scan in normal, XML, s|<rIpt kIddi3,
     and Grepable format, respectively, to the given filename.
  -oA <basename>: Output in the three major formats at once
  -v: Increase verbosity level (use -vv or more for greater effect)
  -d: Increase debugging level (use -dd or more for greater effect)
  --reason: Display the reason a port is in a particular state
  --open: Only show open (or possibly open) ports
  --packet-trace: Show all packets sent and received
  --iflist: Print host interfaces and routes (for debugging)
  --append-output: Append to rather than clobber specified output files
  --resume <filename>: Resume an aborted scan
  --stylesheet <path/URL>: XSL stylesheet to transform XML output to HTML
  --webxml: Reference stylesheet from Nmap.Org for more portable XML
  --no-stylesheet: Prevent associating of XSL stylesheet w/XML output
MISC:
  -6: Enable IPv6 scanning
  -A: Enable OS detection, version detection, script scanning, and traceroute
  --datadir <dirname>: Specify custom Nmap data file location
  --send-eth/--send-ip: Send using raw ethernet frames or IP packets
  --privileged: Assume that the user is fully privileged
  --unprivileged: Assume the user lacks raw socket privileges
  -V: Print version number
  -h: Print this help summary page.
EXAMPLES:
  nmap -v -A scanme.nmap.org
  nmap -v -sn 192.168.0.0/16 10.0.0.0/8
  nmap -v -iR 10000 -Pn -p 80
SEE THE MAN PAGE (https://nmap.org/book/man.html) FOR MORE OPTIONS AND EXAMPLES
```

Let’s move to the **next step**.

### Run the Scanner

As we have learned in the **DevSecOps Gospel**, we should save the output in the machine-readable format so that it can be parsed by the computers easily.

Let’s run nmap with the following options.

```bash
nmap prod-xIKciVAk -oX nmap_out.xml
```

- **-oX**: flag used to tells the tool that the output should be saved in XML format

```bash
Starting Nmap 7.60 ( https://nmap.org ) at 2021-05-03 07:37 UTC
Nmap scan report for prod-xIKciVAk (x.x.x.x)
Host is up (0.000027s latency).
rDNS record for x.x.x.x: prod-xIKciVAk.xIKciVAk
Not shown: 997 closed ports
PORT     STATE SERVICE
22/tcp   open  ssh
80/tcp   open  http
8000/tcp open  http-alt

Nmap done: 1 IP address (1 host up) scanned in 1.59 seconds
```

We can check the scan output using the following command

```bash
cat nmap_out.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE nmaprun>
<?xml-stylesheet href="file:///usr/bin/../share/nmap/nmap.xsl" type="text/xsl"?>
<!-- Nmap 7.60 scan initiated Tue Mar 23 12:02:33 2021 as: nmap -oX nmap_out.xml prod-xIKciVAk -->
<nmaprun scanner="nmap" args="nmap -oX nmap_out.xml prod-xIKciVAk" start="1616500953" startstr="Tue Mar 23 12:02:33 2021" version="7.60" xmloutputversion="1.
04">
<scaninfo type="syn" protocol="tcp" numservices="1000" services="1,3-4,6-7,9,13,17,19-26,30,32-33,37,42-43,49,53,70,79-85,88-90,99-100,106,109-111,113,119,12
5,135,139,143-144,146,161,163,179,199,211-212,222,254-256,259,264,280,301,306,311,340,366,389,406-407,416-417,425,427,443-445,458,464-465,481,497,500,512-515
,524,541,543-545,548,554-555,563,587,593,616-617,625,631,636,646,648,666-668,683,687,691,700,705,711,714,720,722,726,749,765,777,783,787,800-801,808,843,873,
880,888,898,900-903,911-912,981,987,990,992-993,995,999-1002,1007,1009-1011,1021-1100,1102,1104-1108,1110-1114,1117,1119,1121-1124,1126,1130-1132,1137-1138,1
141,1145,1147-1149,1151-1152,1154,1163-1166,1169,1174-1175,1183,1185-1187,1192,1198-1199,1201,1213,1216-1218,1233-1234,1236,1244,1247-1248,1259,1271-1272,127
7,1287,1296,1300-1301,1309-1311,1322,1328,1334,1352,1417,1433-1434,1443,1455,1461,1494,1500-1501,1503,1521,1524,1533,1556,1580,1583,1594,1600,1641,1658,1666,
1687-1688,1700,1717-1721,1723,1755,1761,1782-1783,1801,1805,1812,1839-1840,1862-1864,1875,1900,1914,1935,1947,1971-1972,1974,1984,1998-2010,2013,2020-2022,20
30,2033-2035,2038,2040-2043,2045-2049,2065,2068,2099-2100,2103,2105-2107,2111,2119,2121,2126,2135,2144,2160-2161,2170,2179,2190-2191,2196,2200,2222,2251,2260
,2288,2301,2323,2366,2381-2383,2393-2394,2399,2401,2492,2500,2522,2525,2557,2601-2602,2604-2605,2607-2608,2638,2701-2702,2710,2717-2718,2725,2800,2809,2811,2
869,2875,2909-2910,2920,2967-2968,2998,3000-3001,3003,3005-3007,3011,3013,3017,3030-3031,3052,3071,3077,3128,3168,3211,3221,3260-3261,3268-3269,3283,3300-330
1,3306,3322-3325,3333,3351,3367,3369-3372,3389-3390,3404,3476,3493,3517,3527,3546,3551,3580,3659,3689-3690,3703,3737,3766,3784,3800-3801,3809,3814,3826-3828,
3851,3869,3871,3878,3880,3889,3905,3914,3918,3920,3945,3971,3986,3995,3998,4000-4006,4045,4111,4125-4126,4129,4224,4242,4279,4321,4343,4443-4446,4449,4550,45
67,4662,4848,4899-4900,4998,5000-5004,5009,5030,5033,5050-5051,5054,5060-5061,5080,5087,5100-5102,5120,5190,5200,5214,5221-5222,5225-5226,5269,5280,5298,5357
,5405,5414,5431-5432,5440,5500,5510,5544,5550,5555,5560,5566,5631,5633,5666,5678-5679,5718,5730,5800-5802,5810-5811,5815,5822,5825,5850,5859,5862,5877,5900-5
904,5906-5907,5910-5911,5915,5922,5925,5950,5952,5959-5963,5987-5989,5998-6007,6009,6025,6059,6100-6101,6106,6112,6123,6129,6156,6346,6389,6502,6510,6543,654
7,6565-6567,6580,6646,6666-6669,6689,6692,6699,6779,6788-6789,6792,6839,6881,6901,6969,7000-7002,7004,7007,7019,7025,7070,7100,7103,7106,7200-7201,7402,7435,
7443,7496,7512,7625,7627,7676,7741,7777-7778,7800,7911,7920-7921,7937-7938,7999-8002,8007-8011,8021-8022,8031,8042,8045,8080-8090,8093,8099-8100,8180-8181,81
92-8194,8200,8222,8254,8290-8292,8300,8333,8383,8400,8402,8443,8500,8600,8649,8651-8652,8654,8701,8800,8873,8888,8899,8994,9000-9003,9009-9011,9040,9050,9071
,9080-9081,9090-9091,9099-9103,9110-9111,9200,9207,9220,9290,9415,9418,9485,9500,9502-9503,9535,9575,9593-9595,9618,9666,9876-9878,9898,9900,9917,9929,9943-9
944,9968,9998-10004,10009-10010,10012,10024-10025,10082,10180,10215,10243,10566,10616-10617,10621,10626,10628-10629,10778,11110-11111,11967,12000,12174,12265
,12345,13456,13722,13782-13783,14000,14238,14441-14442,15000,15002-15004,15660,15742,16000-16001,16012,16016,16018,16080,16113,16992-16993,17877,17988,18040,
18101,18988,19101,19283,19315,19350,19780,19801,19842,20000,20005,20031,20221-20222,20828,21571,22939,23502,24444,24800,25734-25735,26214,27000,27352-27353,2
7355-27356,27715,28201,30000,30718,30951,31038,31337,32768-32785,33354,33899,34571-34573,35500,38292,40193,40911,41511,42510,44176,44442-44443,44501,45100,48
080,49152-49161,49163,49165,49167,49175-49176,49400,49999-50003,50006,50300,50389,50500,50636,50800,51103,51493,52673,52822,52848,52869,54045,54328,55055-550
56,55555,55600,56737-56738,57294,57797,58080,60020,60443,61532,61900,62078,63331,64623,64680,65000,65129,65389"/>
<verbose level="0"/>
<debugging level="0"/>
<host starttime="1616500953" endtime="1616500954"><status state="up" reason="arp-response" reason_ttl="0"/>
<address addr="172.17.0.5" addrtype="ipv4"/>
<address addr="02:42:AC:11:00:05" addrtype="mac"/>
<hostnames>
<hostname name="prod-xIKciVAk" type="user"/>
<hostname name="prod-xIKciVAk.lab" type="PTR"/>
</hostnames>
<ports><extraports state="closed" count="998">
<extrareasons reason="resets" count="998"/>
</extraports>
<port protocol="tcp" portid="22"><state state="open" reason="syn-ack" reason_ttl="64"/><service name="ssh" method="table" conf="3"/></port>
<port protocol="tcp" portid="8000"><state state="open" reason="syn-ack" reason_ttl="63"/><service name="http-alt" method="table" conf="3"/></port>
</ports>
<times srtt="25" rttvar="27" to="100000"/>
</host>
<runstats><finished time="1616500954" timestr="Tue Mar 23 12:02:34 2021" elapsed="1.64" summary="Nmap done at Tue Mar 23 12:02:34 2021; 1 IP address (1 host up) scanned in 1.64 seconds" exit="success"/><hosts up="1" down="0" total="1"/>
</runstats>
</nmaprun>
```

In the next exercise, we will put **nmap** into the CI/CD pipeline.



### "Challenge: Dynamic Analysis using NMAP"

> No hubo challenge

## Dynamic Analysis using SSLyze

### Learn how to run SSLyze to find SSL/TLS misconfigurations.

In this scenario, you will learn how to install SSLyze to scan a website for SSL misconfiguration and issues.

You will need to install the SSLyze tool and then run the tool scan for SSL misconfiguration.

### Install DAST Tool

> SSLyze is a fast and powerful SSL/TLS scanning library.
>
> It allows you to analyze the SSL/TLS configuration of a server by connecting to it, in order to detect various issues (bad certificate, weak cipher suites, Heartbleed, ROBOT, TLS 1.3 support, etc.).
>
> SSLyze can either be used as command line tool or as a Python library.
>
> Source [SSLyze Github](https://github.com/nabla-c0d3/sslyze)

Let’s install SSlyze to perform Dynamic analysis.

```bash
pip3 install sslyze
Collecting sslyze
  Downloading sslyze-2.1.4.tar.gz (1.1 MB)
     |████████████████████████████████| 1.1 MB 17.6 MB/s 
Requirement already satisfied: dataclasses in /usr/local/lib/python3.6/dist-packages (from sslyze) (0.8)
Collecting cryptography==2.5
  Downloading cryptography-2.5-cp34-abi3-manylinux1_x86_64.whl (2.4 MB)
     |████████████████████████████████| 2.4 MB 62.4 MB/s 
Requirement already satisfied: cffi!=1.11.3,>=1.8 in /usr/local/lib/python3.6/dist-packages (from cryptography==2.5->sslyze) (1.14.4)
Requirement already satisfied: six>=1.4.1 in /usr/local/lib/python3.6/dist-packages (from cryptography==2.5->sslyze) (1.15.0)
Collecting asn1crypto>=0.21.0
  Downloading asn1crypto-1.4.0-py2.py3-none-any.whl (104 kB)
     |████████████████████████████████| 104 kB 92.6 MB/s 
Requirement already satisfied: pycparser in /usr/local/lib/python3.6/dist-packages (from cffi!=1.11.3,>=1.8->cryptography==2.5->sslyze) (2.20)
Collecting nassl<2.3.0,>=2.2.0
  Downloading nassl-2.2.0-cp36-cp36m-manylinux1_x86_64.whl (3.1 MB)
     |████████████████████████████████| 3.1 MB 69.9 MB/s 
Collecting tls-parser<1.3.0,>=1.2.0
  Downloading tls_parser-1.2.2.tar.gz (7.6 kB)
Building wheels for collected packages: sslyze, tls-parser
  Building wheel for sslyze (setup.py) ... done
  Created wheel for sslyze: filename=sslyze-2.1.4-py3-none-any.whl size=1075103 sha256=3394dc304d79cefa35fa1141cfcb8e7a764327a0194a6b7c4458730dd59c88aa
  Stored in directory: /root/.cache/pip/wheels/20/4c/c3/1ded8047098a5d1813e77aa4087cc7662519dfe2aac2448d55
  Building wheel for tls-parser (setup.py) ... done
  Created wheel for tls-parser: filename=tls_parser-1.2.2-py3-none-any.whl size=11569 sha256=375d7fe4409ec599967b474f62b21710fe6dc381f798f4ecafd6d427fababb67
  Stored in directory: /root/.cache/pip/wheels/59/08/8b/cbbe1cf6b1edc19eb6cca712ec41d642740ce6b060e365bb7a
Successfully built sslyze tls-parser
Installing collected packages: asn1crypto, tls-parser, nassl, cryptography, sslyze
  Attempting uninstall: cryptography
    Found existing installation: cryptography 3.3.1
    Uninstalling cryptography-3.3.1:
      Successfully uninstalled cryptography-3.3.1
Successfully installed asn1crypto-1.4.0 cryptography-2.5 nassl-2.2.0 sslyze-2.1.4 tls-parser-1.2.2
```

We have successfully installed SSLyze scanner, let’s explore the functionality it provides us.

```bash
sslyze --help

Usage: sslyze [options] target1.com target2.com:443 target3.com:443{ip} etc...

Options:
  --version             show program's version number and exit
  -h, --help            show this help message and exit
  --regular             Regular HTTPS scan; shortcut for --sslv2 --sslv3
                        --tlsv1 --tlsv1_1 --tlsv1_2 --tlsv1_3 --reneg --resum
                        --certinfo --http_get --hide_rejected_ciphers
                        --compression --heartbleed --openssl_ccs --fallback
                        --robot

  Trust stores options:
    --update_trust_stores
                        Update the default trust stores used by SSLyze. The
                        latest stores will be downloaded from https://github.c
                        om/nabla-c0d3/trust_stores_observatory. This option is
                        meant to be used separately, and will silence any
                        other command line option supplied to SSLyze.

  Client certificate options:
    --cert=CERT         Client certificate chain filename. The certificates
                        must be in PEM format and must be sorted starting with
                        the subject's client certificate, followed by
                        intermediate CA certificates if applicable.
    --key=KEY           Client private key filename.
    --keyform=KEYFORM   Client private key format. DER or PEM (default).
    --pass=KEYPASS      Client private key passphrase.

  Input and output options:
    --xml_out=XML_FILE  Write the scan results as an XML document to the file
                        XML_FILE. If XML_FILE is set to "-", the XML output
                        will instead be printed to stdout. The corresponding
                        XML Schema Definition is available at
                        ./docs/xml_out.xsd
    --json_out=JSON_FILE
                        Write the scan results as a JSON document to the file
                        JSON_FILE. If JSON_FILE is set to "-", the JSON output
                        will instead be printed to stdout. The resulting JSON
                        file is a serialized version of the ScanResult objects
                        described in SSLyze's Python API: the nodes and
                        attributes will be the same. See https://nabla-c0d3.gi
                        thub.io/sslyze/documentation/available-scan-
                        commands.html for more details.
    --targets_in=TARGETS_IN
                        Read the list of targets to scan from the file
                        TARGETS_IN. It should contain one host:port per line.
    --quiet             Do not output anything to stdout; useful when using
                        --xml_out or --json_out.

  Connectivity options:
    --slow_connection   Greatly reduce the number of concurrent connections
                        initiated by SSLyze. This will make the scans slower
                        but more reliable if the connection between your host
                        and the server is slow, or if the server cannot handle
                        many concurrent connections. Enable this option if you
                        are getting a lot of timeouts or errors.
    --https_tunnel=HTTPS_TUNNEL
                        Tunnel all traffic to the target server(s) through an
                        HTTP CONNECT proxy. HTTP_TUNNEL should be the proxy's
                        URL: 'http://USER:PW@HOST:PORT/'. For proxies
                        requiring authentication, only Basic Authentication is
                        supported.
    --starttls=STARTTLS
                        Perform a StartTLS handshake when connecting to the
                        target server(s). StartTLS should be one of: smtp ,
                        xmpp , xmpp_server , pop3 , ftp , imap , ldap , rdp ,
                        postgres , auto. The 'auto' option will cause SSLyze
                        to deduce the protocol (ftp, imap, etc.) from the
                        supplied port number, for each target servers.
    --xmpp_to=XMPP_TO   Optional setting for STARTTLS XMPP. XMPP_TO should be
                        the hostname to be put in the 'to' attribute of the
                        XMPP stream. Default is the server's hostname.
    --sni=SNI           Use Server Name Indication to specify the hostname to
                        connect to.  Will only affect TLS 1.0+ connections.

  SessionRenegotiationPlugin:
    Test the server(s)' implementation of session renegotiation.

    --reneg             Test the server(s) for client-initiated renegotiation
                        and secure renegotiation support.

  HeartbleedPlugin:
    Test the server(s) for the OpenSSL Heartbleed vulnerability
    (CVE-2014-0160).

    --heartbleed        Test the server(s) for the OpenSSL Heartbleed
                        vulnerability.

  SessionResumptionPlugin:
    Analyze the server(s) SSL session resumption capabilities.

    --resum             Test the server(s) for session resumption support
                        using session IDs and TLS session tickets (RFC 5077).
    --resum_rate        Perform 100 session ID resumptions with the server(s),
                        in order to estimate the rate for successful
                        resumptions.

  OpenSslCcsInjectionPlugin:
    Test the server(s) for the OpenSSL CCS injection vulnerability
    (CVE-2014-0224).

    --openssl_ccs       Test the server(s) for the OpenSSL CCS injection
                        vulnerability (CVE-2014-0224).

  CertificateInfoPlugin:
    Retrieve and validate the server(s)' certificate chain.

    --certinfo          Verify the validity of the server(s) certificate(s)
                        against various trust stores (Mozilla, Apple, etc.),
                        and     check for OCSP stapling support.
    --ca_file=CA_FILE   Path to a local trust store file (with root
                        certificates in PEM format) to verify the validity of
                        the server(s) certificate's chain(s) against.

  EarlyDataPlugin:
    Test the server(s) for TLS 1.3 early data support.      This plugin
    will only work for HTTPS servers; other TLS servers (SMTP, POP3, etc.)
    are not supported.

    --early_data        Test the server(s) for TLS 1.3 early data support.

  RobotPlugin:
    Test the server(s) for the Return Of Bleichenbacher's Oracle Threat
    vulnerability.

    --robot             Test the server(s) for the Return Of Bleichenbacher's
                        Oracle Threat vulnerability.

  HttpHeadersPlugin:
    Test the server(s) for the presence of security-related HTTP headers.

    --http_headers      Check for the HTTP Strict Transport Security (HSTS)
                        and HTTP Public Key Pinning (HPKP) HTTP headers within
                        the     response sent back by the server(s). Also
                        compute the HPKP pins for the server(s)' current
                        certificate chain.

  FallbackScsvPlugin:
    Test the server(s) for support of the TLS_FALLBACK_SCSV cipher suite
    which prevents downgrade attacks.

    --fallback          Test the server(s) for support of the
                        TLS_FALLBACK_SCSV cipher suite which prevents
                        downgrade attacks.

  OpenSslCipherSuitesPlugin:
    Scan the server(s) for supported OpenSSL cipher suites.

    --sslv2             List the SSL 2.0 OpenSSL cipher suites supported by
                        the server(s).
    --sslv3             List the SSL 3.0 OpenSSL cipher suites supported by
                        the server(s).
    --tlsv1             List the TLS 1.0 OpenSSL cipher suites supported by
                        the server(s).
    --tlsv1_1           List the TLS 1.1 OpenSSL cipher suites supported by
                        the server(s).
    --tlsv1_2           List the TLS 1.2 OpenSSL cipher suites supported by
                        the server(s).
    --tlsv1_3           List the TLS 1.3 OpenSSL cipher suites supported by
                        the server(s).
    --http_get          Option - For each cipher suite, sends an HTTP GET
                        request after completing the SSL handshake and returns
                        the HTTP status code.
    --hide_rejected_ciphers
                        Option - Hides the (usually long) list of cipher
                        suites that were rejected by the server(s).

  CompressionPlugin:
    Test the server(s) for Zlib compression support.

    --compression       Test the server(s) for Zlib compression support.
```

Let’s move to the **next step**.

### Run the Scanner

As we have learned in the **DevSecOps Gospel** we should save the output in the machine-readable format so that It can be parsed by the computers easily.

Let’s run sslyze with the following options.


```bash
sslyze --regular --json_out sslyze-output.json prod-xIKciVAk.lab.practical-devsecops.training:443
```

Note, we are using the target as [prod-xIKciVAk.lab.practical-devsecops.training:443](https://prod-xikcivak.lab.practical-devsecops.training/)

- **-regular** flag used to tells tool to perform Regular HTTPS scan
- **-json_out** flag is used to store the output in a json format.

```bash
 AVAILABLE PLUGINS
 -----------------

  EarlyDataPlugin
  SessionRenegotiationPlugin
  SessionResumptionPlugin
  HttpHeadersPlugin
  OpenSslCipherSuitesPlugin
  CertificateInfoPlugin
  CompressionPlugin
  FallbackScsvPlugin
  OpenSslCcsInjectionPlugin
  HeartbleedPlugin
  RobotPlugin


 CHECKING HOST(S) AVAILABILITY
 -----------------------------

   prod-xIKciVAk.lab.practical-devsecops.training:443                       => x.x.x.x.x 


 SCAN RESULTS FOR PROD-xIKciVAk.xIKciVAk.lab.practical-devsecops.training:443 - x.x.x.x.x
 ------------------------------------------------------------------------------------

 * Session Renegotiation:
       Client-initiated Renegotiation:    OK - Rejected
       Secure Renegotiation:              OK - Supported

 * TLSV1_1 Cipher Suites:
      Server rejected all cipher suites.

 * TLSV1_3 Cipher Suites:
       Forward Secrecy                    OK - Supported
       RC4                                OK - Not Supported

     Preferred:
        TLS_AES_256_GCM_SHA384                                           256 bits      HTTP 200 OK                                                 
     Accepted:
        TLS_CHACHA20_POLY1305_SHA256                                     256 bits      HTTP 200 OK                                                 
        TLS_AES_256_GCM_SHA384                                           256 bits      HTTP 200 OK                                                 
        TLS_AES_128_GCM_SHA256                                           128 bits      HTTP 200 OK                                                 

 * TLS 1.2 Session Resumption Support:
      With Session IDs:                  OK - Supported (5 successful, 0 failed, 0 errors, 5 total attempts).
      With TLS Tickets:                  OK - Supported

 * ROBOT Attack:
                                          OK - Not vulnerable

 * Deflate Compression:
                                          OK - Compression disabled

 * TLSV1 Cipher Suites:
      Server rejected all cipher suites.

 * Downgrade Attacks:
       TLS_FALLBACK_SCSV:                 OK - Supported

 * OpenSSL CCS Injection:
                                          OK - Not vulnerable to OpenSSL CCS injection

 * OpenSSL Heartbleed:
                                          OK - Not vulnerable to Heartbleed

 * SSLV2 Cipher Suites:
      Server rejected all cipher suites.

 * TLSV1_2 Cipher Suites:
       Forward Secrecy                    OK - Supported
       RC4                                OK - Not Supported

     Preferred:
        TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384                            256 bits      HTTP 200 OK                                                 
     Accepted:
        TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256                      256 bits      HTTP 200 OK                                                 
        TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384                            256 bits      HTTP 200 OK                                                 
        TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384                            256 bits      HTTP 200 OK                                                 
        TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256                            128 bits      HTTP 200 OK                                                 
        TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256                            128 bits      HTTP 200 OK                                                 

 * SSLV3 Cipher Suites:
      Server rejected all cipher suites.

 * Certificate Information:
     Content
       SHA1 Fingerprint:                  89d7a9d2bc1a482e0e74cb5a54b2d972733d2e1b
       Common Name:                       *.lab.practical-devsecops.training
       Issuer:                            R3
       Serial Number:                     429575392189153564980344116108018657284994
       Not Before:                        2021-02-11 07:27:08
       Not After:                         2021-05-12 07:27:08
       Signature Algorithm:               sha256
       Public Key Algorithm:              RSA
       Key Size:                          2048
       Exponent:                          65537 (0x10001)
       DNS Subject Alternative Names:     ['*.lab.practical-devsecops.training']

     Trust
       Hostname Validation:               OK - Certificate matches prod-xIKciVAk.lab.practical-devsecops.training
       Android CA Store (9.0.0_r9):       OK - Certificate is trusted
       Apple CA Store (iOS 12, macOS 10.14, watchOS 5, and tvOS 12):OK - Certificate is trusted
       Java CA Store (jdk-12.0.1):        OK - Certificate is trusted
       Mozilla CA Store (2019-03-14):     OK - Certificate is trusted
       Windows CA Store (2019-05-27):     OK - Certificate is trusted
       Symantec 2018 Deprecation:         WARNING: Certificate distrusted by Google and Mozilla on September 2018
       Received Chain:                    *.lab.practical-devsecops.training --> R3
       Verified Chain:                    *.lab.practical-devsecops.training --> R3 --> DST Root CA X3
       Received Chain Contains Anchor:    OK - Anchor certificate not sent
       Received Chain Order:              OK - Order is valid
       Verified Chain contains SHA1:      OK - No SHA1-signed certificate in the verified certificate chain

     Extensions
       OCSP Must-Staple:                  NOT SUPPORTED - Extension not found
       Certificate Transparency:          WARNING - Only 2 SCTs included but Google recommends 3 or more

     OCSP Stapling
                                          NOT SUPPORTED - Server did not send back an OCSP response


 SCAN COMPLETED IN 0.38 S
 ------------------------
```

We can check the scan output using the following command.

```bash
cat sslyze-output.json
...[SNIP]...
                    "errored_cipher_list": [],
                    "preferred_cipher": {
                        "is_anonymous": false,
                        "key_size": 256,
                        "openssl_name": "TLS_AES_256_GCM_SHA384",
                        "post_handshake_response": "HTTP 200 OK",
                        "ssl_version": "TLSV1_3"
                    },
                    "rejected_cipher_list": [
                        {
                            "handshake_error_message": "TLS / Alert: handshake failure",
                            "is_anonymous": false,
                            "openssl_name": "TLS_AES_128_CCM_SHA256",
                            "ssl_version": "TLSV1_3"
                        },
                        {
                            "handshake_error_message": "TLS / Alert: handshake failure",
                            "is_anonymous": false,
                            "openssl_name": "TLS_AES_128_CCM_8_SHA256",
                            "ssl_version": "TLSV1_3"
                        }
                    ]
                }
            },
            "server_info": {
                "client_auth_credentials": null,
                "client_auth_requirement": "DISABLED",
                "highest_ssl_version_supported": 6,
                "hostname": "prod-xIKciVAk.lab.practical-devsecops.training",
                "http_tunneling_settings": null,
                "ip_address": "x.x.x.x",
                "openssl_cipher_string_supported": "TLS_AES_256_GCM_SHA384",
                "port": 443,
                "tls_server_name_indication": "prod-xIKciVAk.lab.practical-devsecops.training",
                "tls_wrapped_protocol": "HTTPS",
                "xmpp_to_hostname": null
            }
        }
    ],
    "invalid_targets": [],
    "sslyze_url": "https://github.com/nabla-c0d3/sslyze",
    "sslyze_version": "2.1.4",
    "total_scan_time": "0.3779592514038086"
```

In the next exercise, we will integrate sslyze into the CI/CD pipeline.

### "Challenge: Dynamic Analysis using SSLyze"

> No hubo challenge

## Embed Nikto, SSLyze, and Nmap into GitLab

### Use Nikto, SSLyze, and Nmap to do DAST in CI/CD pipeline

In this scenario, you will learn how to embed DAST scan in a CI/CD pipeline.

You will learn to incorporate **DAST tools** in CI/CD pipeline and handle job failures in Maturity Levels 1 and 2.

> Once you click the Start the Exercise button, you will need to wait 2 minutes for the GitLab machine to start.

> Remember!
>
> 1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
> 2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

### A simple CI/CD pipeline

Imagine your boss asked you to embed DAST tools in a project’s CI pipeline. You have access to the source code, and you saw they were using the following **.gitlab-ci.yml** script.

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

You saw that the CI Script has two jobs, the **build** job and the **test** job. Assuming you do not understand python or any programming language, we can safely consider the DevOps team is building and testing the code.

Let’s log into GitLab using the following details and execute this pipeline once again.

### GitLab CI/CD Machine

Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the above CI script. Click on the **Edit** button to replace the content (use Control+A and Control+V).

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xikcivak.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

### Challenge: Embed Nikto, SSLyze, and Nmap into GitLab

Recall the techniques you have learned in the previous module (CI/CD, OAST, and SAST).

1. In this exercise, we will explore how to add DAST tools, **nikto**, **nmap**, and **sslyze** in the CI/CD pipeline against the production machine [https://prod-xIKciVAk.lab.practical-devsecops.training](https://prod-xIKciVAk.lab.practical-devsecops.training)
2. Visit the respective tool’s documentation page to see if you can create an appropriate command to run and test the tool locally first, before integrating in to the CI/CD pipeline
3. Please follow the DevSecOps Gospel and other best practices while trying to embed the above scanners

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed DAST in CI/CD pipeline

As discussed in the **Dynamic Analysis** exercises, we can integrate DAST tools like **Nikto**, **SSLyze** and **Nmap** in our CI/CD pipelines.

> What would you do if the tools are not installed? You can either install it yourself, provided you have permission to do so, or ask your operations team to help you out.

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

nikto:
  stage: integration
  script:
    - docker pull hysnsec/nikto
    - docker run --rm -v $(pwd):/tmp hysnsec/nikto -h http://prod-xIKciVAk.lab.practical-devsecops.training -o /tmp/nikto-output.xml
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

prod:
  stage: prod
  script:
    - echo "This is a deploy step."
  when: manual # Continuous Delivery
```

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xikcivak.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

In the **next step**, you will learn why you should not fail the builds.

### Allow the job failure

> Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

You do not want to fail the builds in **DevSecOps Maturity Levels 1 and 2**. If a security tool fails a job, it won’t allow other DevOps jobs like release/deploy to run hence causing great distress to DevOps Team. Moreover, the security tools suffer from false positives. Failing a build on inaccurate data is a sure recipe for disaster.

> **Pro-tip**: Don’t block builds unless you are sure about the result’s quality.

You can use the **allow_failure** tag to not fail the build even though the tool found issues.

```yaml
sslscan:
  stage: integration
  script:
    - docker pull hysnsec/sslyze
    - docker run --rm -v $(pwd):/tmp hysnsec/sslyze --regular prod-xIKciVAk.lab.practical-devsecops.training:443 --json_out /tmp/sslyze-output.json
  artifacts:
    paths: [sslyze-output.json]
    when: always
  allow_failure: true   #<--- allow the build to fail but don't mark it as such
```



> Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

You do not want to fail the builds in **DevSecOps Maturity Levels 1 and 2**. If a security tool fails a job, it won’t allow other DevOps jobs like release/deploy to run hence causing great distress to DevOps Team. Moreover, the security tools suffer from false positives. Failing a build on inaccurate data is a sure recipe for disaster.

---

### Extra Mile Challenge: Create a docker image for SSLyze and run it in the pipeline

#### Who should do this exercise?

1. This exercise is beyond the scope of the CDP course and is added to help folks who already know these concepts in and out
2. You know how to write small scripts in Python/Ruby/Golang
3. You are comfortable writing **Dockerfiles**
4. You consider yourself an expert or advanced user in DAST

#### Challenge

Recall techniques you have learned in the docker security course.

1. Read the [SSLyze documentation](https://github.com/nabla-c0d3/sslyze)
2. Create the docker image with the help of a Dockerfile and use alpine as the base image
3. Run the docker image in CI/CD pipeline against **django.nv** web application

> Please do note, we will not provide solutions for this extra mile exercise.

## Dynamic Analysis using ZAP

### Learn how to run ZAP to find security issues

In this scenario, you will learn how to use ZAP to scan a website for security issues.

### OWASP Zed Attack Proxy tool

ZAP is an open-source web application security scanner to perform security testing (Dynamic Testing) on web applications. OWASP ZAP is the flagship OWASP project used extensively by penetration testers. ZAP can also run in a daemon mode for hands-off scans for CI/CD pipeline. ZAP provides extensive API (SDK) and a REST API to help users create custom scripts.

Source: [OWASP ZAP](https://www.zaproxy.org/getting-started/)

In this exercise, we will use **ZAP Baseline Scan** to find security issues passively.

First, we need to pull ZAP’s stable docker image.

```bash
docker pull owasp/zap2docker-stable
# docker pull owasp/zap2docker-stable:2.10.0
```

Let’s see the usage of ZAP Docker for Baseline Scan.

```bash
docker run --rm owasp/zap2docker-stable zap-baseline.py --help
# docker run --rm owasp/zap2docker-stable:2.10.0 zap-baseline.py --help
```

```
Usage: zap-baseline.py -t <target> [options]
    -t target         target URL including the protocol, e.g. https://www.example.com
Options:
    -h                print this help message
    -c config_file    config file to use to INFO, IGNORE or FAIL warnings
    -u config_url     URL of config file to use to INFO, IGNORE or FAIL warnings
    -g gen_file       generate default config file (all rules set to WARN)
    -m mins           the number of minutes to spider for (default 1)
    -r report_html    file to write the full ZAP HTML report
    -w report_md      file to write the full ZAP Wiki (Markdown) report
    -x report_xml     file to write the full ZAP XML report
    -J report_json    file to write the full ZAP JSON document
    -a                include the alpha passive scan rules as well
    -d                show debug messages
    -P                specify listen port
    -D                delay in seconds to wait for passive scanning
    -i                default rules not in the config file to INFO
    -I                do not return failure on warning
    -j                use the Ajax spider in addition to the traditional one
    -l level          minimum level to show: PASS, IGNORE, INFO, WARN or FAIL, use with -s to hide example URLs
    -n context_file   context file which will be loaded prior to spidering the target
    -p progress_file  progress file which specifies issues that are being addressed
    -s                short output format - dont show PASSes or example URLs
    -T                max time in minutes to wait for ZAP to start and the passive scan to run
    -U user           username to use for authenticated scans - must be defined in the given context file
    -z zap_options    ZAP command line options e.g. -z "-config aaa=bbb -config ccc=ddd"
    --hook            path to python file that define your custom hooks

For more details see https://www.zaproxy.org/docs/docker/baseline-scan/
```

Let’s move to the **next step**.

### Run the Scanner

As we have learned in the **DevSecOps Gospel**, we should save the output in the machine-readable format like **JSON**, **CSV** or **XML**.

Let’s run **zap-baseline.py** with basic options.

```bash
docker run --rm owasp/zap2docker-stable zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training
# docker run --rm owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training
```

```
2021-03-23 12:07:00,509 Could not find custom hooks file at /home/zap/.zap_hooks.py
Mar 23, 2021 12:07:04 PM java.util.prefs.FileSystemPreferences$1 run
INFO: Created user preferences directory.
Total of 5 URLs
PASS: Vulnerable JS Library [10003]
PASS: Cookie No HttpOnly Flag [10010]
PASS: Cookie Without Secure Flag [10011]
PASS: Content-Type Header Missing [10019]
PASS: X-Frame-Options Header [10020]
PASS: Information Disclosure - Debug Error Messages [10023]
PASS: Information Disclosure - Sensitive Information in URL [10024]
PASS: Information Disclosure - Sensitive Information in HTTP Referrer Header [10025]
PASS: HTTP Parameter Override [10026]
PASS: Information Disclosure - Suspicious Comments [10027]
PASS: Open Redirect [10028]
PASS: Cookie Poisoning [10029]
PASS: User Controllable Charset [10030]
PASS: User Controllable HTML Element Attribute (Potential XSS) [10031]
PASS: Viewstate [10032]
PASS: Directory Browsing [10033]
PASS: Heartbleed OpenSSL Vulnerability (Indicative) [10034]
PASS: Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s) [10037]
PASS: X-Backend-Server Header Information Leak [10039]
PASS: HTTP to HTTPS Insecure Transition in Form Post [10041]
PASS: HTTPS to HTTP Insecure Transition in Form Post [10042]
PASS: User Controllable JavaScript Event (XSS) [10043]
PASS: Big Redirect Detected (Potential Sensitive Information Leak) [10044]
PASS: Retrieved from Cache [10050]
PASS: X-ChromeLogger-Data (XCOLD) Header Information Leak [10052]
PASS: Cookie Without SameSite Attribute [10054]
PASS: CSP [10055]
PASS: X-Debug-Token Information Leak [10056]
PASS: Username Hash Found [10057]
PASS: X-AspNet-Version Response Header [10061]
PASS: PII Disclosure [10062]
PASS: Timestamp Disclosure [10096]
PASS: Hash Disclosure [10097]
PASS: Cross-Domain Misconfiguration [10098]
PASS: Weak Authentication Method [10105]
PASS: Reverse Tabnabbing [10108]
PASS: Modern Web Application [10109]
PASS: Absence of Anti-CSRF Tokens [10202]
PASS: Private IP Disclosure [2]
PASS: Session ID in URL Rewrite [3]
PASS: Script Passive Scan Rules [50001]
PASS: Insecure JSF ViewState [90001]
PASS: Charset Mismatch [90011]
PASS: Application Error Disclosure [90022]
PASS: WSDL File Detection [90030]
PASS: Loosely Scoped Cookie [90033]
WARN-NEW: Cookie No HttpOnly Flag [10010] x 4
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
WARN-NEW: Cookie Without Secure Flag [10011] x 4
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
WARN-NEW: Incomplete or No Cache-control and Pragma HTTP Header Set [10015] x 26
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/bootstrap.css (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/font-awesome.css (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/bootstrap.min.css (200 OK)
WARN-NEW: Cross-Domain JavaScript Source File Inclusion [10017] x 18
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/injection/ (200 OK)
WARN-NEW: X-Content-Type-Options Header Missing [10021] x 38
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/bootstrap.css (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/font-awesome.css (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/css/bootstrap.min.css (200 OK)
WARN-NEW: Strict-Transport-Security Header Not Set [10035] x 44
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/robots.txt (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/sitemap.xml (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register (301 MOVED PERMANENTLY)
WARN-NEW: Server Leaks Version Information via "Server" HTTP Response Header Field [10036] x 44
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/robots.txt (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/sitemap.xml (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register (301 MOVED PERMANENTLY)
WARN-NEW: Content Security Policy (CSP) Header Not Set [10038] x 21
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/robots.txt (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/sitemap.xml (404 NOT FOUND)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/static/taskManager/js/bootstrap.min.js (404 NOT FOUND)
WARN-NEW: Secure Pages Include Mixed Content (Including Scripts) [10040] x 18
    https://prod-xIKciVAk.lab.practical-devsecops.training/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/tutorials/injection/ (200 OK)
WARN-NEW: Cookie Without SameSite Attribute [10054] x 4
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/login/ (200 OK)
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/register/ (200 OK)
WARN-NEW: Absence of Anti-CSRF Tokens [10202] x 1
    https://prod-xIKciVAk.lab.practical-devsecops.training/taskManager/forgot_password/ (200 OK)
FAIL-NEW: 0 FAIL-INPROG: 0  WARN-NEW: 11    WARN-INPROG: 0  INFO: 0 IGNORE: 0   PASS: 40
```

If you want the output in JSON format, you can use the **-J** option.

```bash
docker run --user $(id -u):$(id -g) -w /zap -v $(pwd):/zap/wrk:rw --rm owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json
# docker run --user $(id -u):$(id -g) -w /zap -v $(pwd):/zap/wrk:rw --rm owasp/zap2docker-stable zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json
```

The **zap-output.json** file is in the current directory. You can verify the existence of the output file with **ls -l** command.

In the next exercise, we will integrate **ZAP Baseline Scan** into the CI/CD pipeline.

### "Challenge: Dynamic Analysis using ZAP"

> No hubo challenge

## How to Embed Zed Attack Proxy (ZAP) into GitLab

### Use ZAP tool to do DAST in CI/CD pipeline

In this scenario, you will learn how to embed DAST in CI/CD pipeline.

You will learn to use **ZAP Baseline Scan** in CI/CD pipeline using all the best practices mentioned in the **Practical DevSecOps Gospel**.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab machine to start.

> Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

### A simple CI/CD pipeline

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
```

We have two jobs in the above pipeline, the **build** job and the **test** job. As a security engineer, I do not care what they are doing as part of these jobs. Why? Imagine having to learn every build/testing tool used by your DevOps team. It will be a nightmare! Instead, rely on the DevOps team for help.

Let’s log into GitLab using the following details and execute this pipeline.

### GitLab CI/CD Machine

Name	Value
Link	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/blob/master/.gitlab-ci.yml
Username	root
Password	pdso-training

Next, we need to create a CI/CD pipeline by replacing the **.gitlab-ci.yml** file content with the above CI script. Click on the **Edit** button to replace the content.

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

As soon as a change is made to the repository, the pipeline starts executing the jobs.

We can see the results of this pipeline by visiting [https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines](https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines).

Click on the appropriate job name to see the output.

### Challenge

We will use the **Zed Attack Proxy** (ZAP) to scan applications for security issues and then embed it into CI/CD using **ZAP Baseline Scan** docker image.

1. Explore ZAP Baseline script/tool details here
2. Use [https://prod-xIKciVAk.lab.practical-devsecops.training](https://prod-xIKciVAk.lab.practical-devsecops.training) as the endpoint for ZAP Scanning
3. Embed ZAP scanning in **integration** stage and save the output as **JSON** file
4. Remember to follow all best practices while adding the baseline scan to CI/CD pipeline
5. Once you’re done, please do not forget to share the answer with our staff via Slack Direct Message(DM).

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed ZAP in CI/CD pipeline

> Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

As discussed in the Dynamic Analysis using ZAP exercises, we can put ZAP in our CI/CD pipeline. We did ensure the zap command runs fine in **DevSecOps-Box**, we need to embed this into CI/CD pipeline now using the same command.

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

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/pipelines.

Click on the appropriate job name to see the output.

## How to Embed Zed Attack Proxy (ZAP) into Jenkins

### Use ZAP tool to do DAST in Jenkins CI/CD pipeline

In this scenario, you will learn how to embed DAST in the Jenkins CI/CD pipeline.

You will learn to use **ZAP Baseline Scan** in CI/CD pipeline using all the best practices mentioned in the **Practical DevSecOps Gospel**.

> Once you click the **Start the Exercise** button, you will need to **wait 2 minutes** for the GitLab and Jenkins machine to start.

> Remember!
>
>1. Except for DevSecOps-Box, every other machine **closes after two hours**, even if you are in the middle of the exercise
>
>2. After two hours, in case of a **404**, you need to refresh the exercise page and click on **Start the Exercise** button to continue working
>
>3. We have already set up the Jenkins machine with several plugins to help you do the exercise

### Create a new job

> The Jenkins system is already configured with GitLab. If you wish to know** how to configure Jenkins with GitLab**, you can check out this [link](https://gitlab.practical-devsecops.training/pdso/jenkins/-/blob/master/tutorials/configure-jenkins-with-gitlab.md).

We will create a new job in Jenkins by visiting [https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob](https://jenkins-xIKciVAk.lab.practical-devsecops.training/newJob).

You can use the following details to log into Jenkins.

Name	Value
Username	root
Password	pdso-training

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

As we can see in the above CI Script, we have has two stages, namely **build** and **test**. Assuming you do not understand python or any programming language, we can safely consider the DevOps team is building and testing the code.

Let’s log in to Gitlab using the following details.

Name	Value
**Gitlab** URL	https://gitlab-ce-xIKciVAk.lab.practical-devsecops.training
**Username**	root
**Password**	pdso-training

Add a new file to the repository by clicking on the +(plus) button and give it a name as Jenkinsfile, then add the above script into the file.

Save changes to the file using the **Commit changes** button.

### Verify the pipeline run

Since we want to use Jenkins to execute the CI/CD jobs, we need to remove **.gitlab-ci.yml** from the git repository. Doing so will prevent Gitlab from running the CI jobs on both the **Gitlab Runner** and the **Jenkins** systems.

> Don’t forget to disable Auto DevOps in Gitlab as it will execute the job when any changes are pushed to the repository even though the .gitlab-ci.yaml file is missing.
>
>Visit http://gitlab-ce-xIKciVAk.lab.practical-devsecops.training/root/django-nv/-/settings/ci_cd to disable it.

>We can see the results of this pipeline by visiting https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv.
>
>Click on the appropriate build history to see the output.

### Challenge

We will use the **Zed Attack Proxy** (ZAP) to scan applications for security issues and then embed it into CI/CD using **ZAP Baseline Scan** docker image.

1. Explore ZAP Baseline script/tool details here
2. Use https://prod-xIKciVAk.lab.practical-devsecops.training as the endpoint for ZAP Scanning
3. Embed ZAP scanning as **zap-baseline** stage and save the output as **JSON** file
4. Remember to follow all best practices while adding the baseline scan to CI/CD pipeline

Once done, please do not forget to share the pipeline script with our staff.

> Please try to do this exercise without looking at the solution on the next page.

Let’s move to the **next step**.

### Embed ZAP in Jenkins

>Remember!
>
>1. Except for DevSecOps-Box, every other machine closes after two hours, even if you are in the middle of the exercise
>
>2. After two hours, in case of a 404, you need to refresh the exercise page and click on Start the Exercise button to continue working

As discussed in the **Dynamic Analysis using ZAP** exercises, we can put ZAP in our CI/CD pipeline. We did ensure that the ZAP command runs fine in **DevSecOps-Box**, now we need to embed zap into CI/CD pipeline.

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
                    sh "docker run -u root -v \$(pwd):/zap/wrk:rw --rm -t owasp/zap2docker-stable:2.10.0 zap-baseline.py -t https://prod-xIKciVAk.lab.practical-devsecops.training -J zap-output.json"
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'zap-output.json', fingerprint: true
                }
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

As discussed, any change to the repo kick starts the pipeline.

We can see the results of this pipeline by visiting [https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv](https://jenkins-xIKciVAk.lab.practical-devsecops.training/job/django.nv).

Click on the appropriate build history to see the output.

## How to Embed Zed Attack Proxy (ZAP) into GitHub Actions

### Use ZAP tool to perform DAST in GitHub Actions

In this scenario, you will learn how to embed DAST in **GitHub Actions**.

You will learn to use **ZAP Baseline Scan** in CI/CD pipeline using all the best practices mentioned in the **Practical DevSecOps Gospel**.

> Note
>
> DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.

### A simple CI/CD pipeline

You’ve learned about CI/CD systems using GitLab and Jenkins. Both are good systems, but they also have different features, and use cases. We will look into another CI/CD system named **GitHub Actions** that debuted on **13 November 2019**. **GitHub Actions** is a CI/CD system that is built-in to GitHub with **free** and paid offerings.

Let’s get started!

### 1. Create a new repository

> If you haven’t registered for a GitHub account, please sign up for an account [here](https://github.com/join?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)

First, we need to create a repository in our GitHub account by visiting [https://github.com/new](https://github.com/new).

Create a repository named django.nv, you can also check the box with **Public** or **Private** options, and please ignore **Initialize this repository with** section for now.

Click the **Create repository** button.

### 2. Create a Personal Access Token (PAT)

Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting [August 2021](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/).

Let’s create PAT by visiting [https://github.com/settings/tokens](https://github.com/settings/tokens),then click **Generate new token** button and give your token a name e.g. django.

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
>**Please don’t use your company’s GitHub credentials or token to practice these exercises.**

### 4. Download the repository

Let’s start by cloning **django.nv** in DevSecOps Box.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git
```

By cloning the above repository, we created a local copy of the remote repository.

Let’s **cd** into this repository to explore its content.

```bash
cd django.nv
```

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

```bash
git remote rename origin old-origin
```

> In the command below, please change “username” with your GitHub username.

```bash
git remote add origin https://github.com/username/django.nv.git
```

Let’s check the status of our git repository.

```bash
git status
```

```
On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
```

We are in the **master** branch and we need to create one more branch called **main** as a default branch.

```bash
git checkout -b main
```

> Why do we need a new branch? Because in this exercise we will use the main branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.
>
> Read more about Renaming the default branch from master.

Then, let’s push the code to the GitHub repository.

```bash
git push -u origin main
```

And enter your GitHub credentials when prompted (please use **Personal Access Token** as a password), then the code will be pushed to the GitHub repository.

### 5. Add a workflow file to the repository

To use **GitHub Actions**, you need to create **.github/workflows** directory and create a new YAML file named main.yaml or any other desired name because each file in the **.github/workflows** directory which has a **.yaml** extension will define a workflow.

Let’s create a simple workflow by entering the following commands in DevSecOps Box.

```bash
mkdir -p .github/workflows
```

```bash
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
```

```
Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
```

### 7. Verify the pipeline runs

Any change to the repo, will kick start the pipeline.

We can see the result of the pipeline by visiting our **django.nv** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

You will notice that the **integration** has **exit 1** and hence failed the job, but other jobs are still running. Why?

> You can find more details at https://docs.github.com/en/actions/learn-github-actions/introduction-to-github-actions#jobs.

> Let’s move to the **next step**.

### Embed ZAP in GitHub Actions

As discussed in the Dynamic Analysis using ZAP exercises, we can integrate ZAP in our CI/CD pipeline. We did ensure that the ZAP command runs fine in **DevSecOps-Box**, now we need to embed zap into CI/CD pipeline. In GitHub Actions, there are two ways to integrate ZAP. ZAP can be integrated in GitHub actions using **docker run**, or using the ZAP scan actions available from the **GitHub Marketplace**. **GitHub Marketplace** offers two actions to integrate ZAP, namely the **Baseline Scan** and the **Full Scan** actions.

We will explore both the different ways of embedding ZAP in GitHub Actions, that is through a ZAP docker image, and through a ZAP action available from the GitHub marketplace.

### Add secret

We will set up the necessary secrets, go back to **django.nv** repository and click the **Settings** tab.

Click the **Secrets** option, then select **New repository secret** and add the following credentials into it.

Name	Value
**Name**	PROD_URL
**Value**	https://prod-xIKciVAk.lab.practical-devsecops.training

Once done, click the **Add secret** button.

### Embed ZAP using the docker run command

Go back to the DevSecOps Box machine, and replace the **integration** job under **.github/workflows/main.yaml** with the following content:

```yml
  zap_baseline:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - run: |
           docker pull owasp/zap2docker-stable:2.10.0
           docker run --user root --rm -v $(pwd):/zap/wrk:rw -w /zap owasp/zap2docker-stable:2.10.0 zap-baseline.py -t ${{ secrets.PROD_URL }} -J zap-output.json

      - uses: actions/upload-artifact@v2
        with:
          name: ZAP Scan
          path: zap-output.json
        if: always()        # what is this for?
```

> To understand **if: always()** Please refer to conditionals.

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our **django.nv** repository, clicking the **Actions** tab, and selecting the appropriate workflow name to see the output.

### Embed ZAP using an action from GitHub Marketplace

Please visit the following link to see the details of **ZAP Baseline Scan** action here.

You can replace the content of previous workflow file with the below content to understand the difference between embedding ZAP through **docker run** directly and embedding ZAP through GitHub action from the GitHub Marketplace.

> **action** is similar to a plugin or a template that is offered by the provider themselves.

```yml
  zap_baseline:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - name: Checkout
        uses: actions/checkout@v2
        with:
          ref: master

      - name: ZAP Scan
        uses: zaproxy/action-baseline@v0.4.0
        with:
          token: ${{ secrets.GITHUB_TOKEN }}
          docker_name: 'owasp/zap2docker-stable:2.10.0'
          target: ${{ secrets.PROD_URL }}
```

After replacing the workflow content, commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

Once the pipeline is complete, you can click the **Issues** tab in your repository, and you will notice that there is **1** issue open. The open issue is a result of embedding ZAP through the action **zaproxy/action-baseline@v0.4.0**, and the issue is created automatically by **github-actions** bot.

## How to Embed Zed Attack Proxy (ZAP) into CircleCI

### Use ZAP tool to perform DAST in CircleCI

In this scenario, you will learn how to embed DAST in **CircleCI**.

You will learn to use **ZAP Baseline Scan** in CI/CD pipeline using all the best practices mentioned in the **Practical DevSecOps Gospel**.

> Note
>
> DevSecOps Box is an immutable machine, and hence it does not save the previous state after you close or reload the browser. The credentials that were entered or saved previously need to be provided again.

### Initial Setup

You’ve learned about CI/CD systems such as GitLab, Jenkins, GitHub Actions and so on. Remember every CI/CD system has its own advantages, and limitations, we just need to find what is suitable for our needs.

Now, we will look into another CI/CD system called CircleCI, this system doesn’t have a built-in Git repository like GitLab or GitHub. But it can be integrated with GitHub or Bitbucket as the repository, so let’s get started!

### 1. Create a new repository

> If you haven’t registered for a GitHub account, please sign up for an account [here](https://github.com/join?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home)

First, we need to create a repository in our GitHub account by visiting [https://github.com/new](https://github.com/new).

Create a repository named django.nv, you can also check the box with **Public** or **Private** options, and please ignore **Initialize this repository with** section for now.

Click the **Create repository** button.

### 2. Create a Personal Access Token (PAT)

Next, we will create and use PAT for git authentication in DevSecOps Box because GitHub will not support account passwords starting [August 2021](https://github.blog/2020-12-15-token-authentication-requirements-for-git-operations/).

Let’s create PAT by visiting [https://github.com/settings/tokens](https://github.com/settings/tokens), then click **Generate new token** button and give your token a name e.g. django.

Select **repo** option to access repositories from the command line and scroll down to generate a new token.

> The token will have a format like **ghp_xxxxxxxxx**.

Once you have the token, please copy and save it as a file in DevSecOps Box, so we can use it whenever we needed.

### 3. Initial git setup

To work with git repositories via Command Line Interface (CLI), aka terminal/command prompt, we need to set up a user and an email. We can use git config command to configure git user and email.

```bash
git config --global user.email "your_email@gmail.com"
git config --global user.name "your_username"
```

>You need to use your email and username, which are registered in GitHub.
>
>Please don’t use your company’s GitHub credentials or token to practice these exercises.

### 4. Download the repository

Let’s start by cloning **django.nv** in DevSecOps Box.

```bash
git clone https://gitlab.practical-devsecops.training/pdso/django.nv.git
```

By cloning the above repository, we created a local copy of the remote repository.

Let’s **cd** into this repository to explore its content.

```bash
cd django.nv
```

Since this repository was cloned from Gitlab, the remote URL of this Git repository is going to point to the GitLab URL. Let’s rename the repository’s Git URL to point to GitHub, enabling us to push our code to GitHub.

```bash
git remote rename origin old-origin
```

> In the command below, please change “username” with your GitHub username.

```bash
git remote add origin https://github.com/username/django.nv.git
```

Let’s check the status of our git repository.

```bash
git status
```

```bash
On branch master
Your branch is up to date with 'old-origin/master'.

nothing to commit, working tree clean
```

We are in the **master** branch and we need to create one more branch called **main** as a default branch.

```bash
git checkout -b main
```

> Why we need to do this? Because in this exercise we will use the main branch as a control to run the pipeline in every commit. If you don’t do this, you will not be able to see any pipeline in your repository.
>
>Read more about [Renaming the default branch from master](https://github.com/github/renaming).

Then, let’s push the code to the GitHub repository.

```bash
git push -u origin main
```

And enter your GitHub credentials when prompted (please use Personal Access Token as a password), then the code will be pushed to the GitHub repository.

### 5. Create an account in CircleCI

> If you’ve already created the CircleCI account, you can ignore this step.

To use **CircleCI**, we need to create an account by signing up at [https://circleci.com/signup](https://circleci.com/signup), click the **Signup with GitHub** button and you will be redirected to the page which tells you to allow **CircleCI** to access your GitHub account, accept it by clicking the **Authorize circleci** button.

Next, you will see the repository lists which has a button called **Set Up Project**. Select **django.nv** repository then click on that button to start using **CircleCI** as our CI/CD pipeline and you will get a pop-up with **Select a config.yml file for django.nv** message, please ignore it for now because we will create the CircleCI YML file in our GitHub repository.

Let’s move to the **next step**.

### A simple CI/CD pipeline

You need to create **.circleci** directory and create a new YAML file named config.yml and add the following CI script.

```bash
mkdir -p .circleci
```

```bash
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
```

```bash
Counting objects: 5, done.
Delta compression using up to 16 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (5/5), 577 bytes | 577.00 KiB/s, done.
Total 5 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/username/django.nv.git
   df066a2..98e754f  main -> main
```

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our [CircleCI account](https://app.circleci.com/). Click **Projects**, select **django.nv** repository and select the appropriate pipeline to see the output.

Let’s move to the **next step**.

### Embed ZAP in CircleCI

As discussed in the **Dynamic Analysis using ZAP** exercises, we can integrate ZAP in our CI/CD pipeline. In the previous exercises, we did ensure that the ZAP command runs fine in **DevSecOps-Box**, now we need to embed zap into CI/CD pipeline.

### Add secret

We will set up the necessary secrets by visiting https://app.circleci.com/pipelines/github/username/django.nv, then click **Project Settings** on the top right and select **Environment Variables** to add the following variables in the form of key value pairs.

Name	Value
**Key**	PROD_URL
**Value**	https://prod-xIKciVAk.lab.practical-devsecops.training

Go back to the DevSecOps Box machine, and replace the **integration** job under **.circleci/config.yml** with the following content:

```yml
  zap_baseline:
    machine: true
    steps:
      - checkout

      - run: |
          docker pull owasp/zap2docker-stable:2.10.0
          docker run --user root --rm -v $(pwd):/zap/wrk:rw -w /zap owasp/zap2docker-stable:2.10.0 zap-baseline.py -t ${PROD_URL} -J zap-output.json

      - store_artifacts:
          path: zap-output.json
          destination: zap-artifact
```

Commit, and push the changes to GitHub.

Any change to the repo will kick start the pipeline.

We can see the result of the pipeline by visiting our [CircleCI account](https://app.circleci.com/). Click **Projects**, select **django.nv** repository and select the appropriate pipeline to see the output.