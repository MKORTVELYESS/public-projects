App Info:
Name: ${app.name}
Version: ${app.version}

Database:
Host: ${db.host}
Port: ${db.port}
User: ${db.credentials.user}

Regions:
<#list host.region?keys as code>
- ${code?upper_case}: ${host.region[code]}
</#list>
