### Base Env
```bash
sudo apt update
sudo apt upgrade -y

# Git
sudo apt install git -y
```
Download project
```bash
cd /opt
git clone https://github.com/toaction/Micro-headlines.git
```

### Database
```bash
# MySQL install
sudo apt install mysql-server -y

# create database
sudo mysql -u root -p
create database micro_headlines;

# create user
create user 'micro_headlines'@'localhost' identified by '12345678';
grant all privileges on micro_headlines.* to 'micro_headlines'@'localhost';
flush privileges;
exit;
```
Import database
```bash
cd /opt/Micro-headlines
mysql -u micro_headlines -p micro_headlines < database/micro_headlines.sql;
```

### Back end
```bash
# Java
sudo apt install openjdk-8-jdk -y

# Tomcat
cd /tmp
sudo wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.69/bin/apache-tomcat-9.0.69.tar.gz
sudo tar -xzvf apache-tomcat-9.0.69.tar.gz -C /opt
sudo mv apache-tomcat-9.0.69 /opt/tomcat9
```

Use IDEA to package the project as a WAR file (`micro-headlines.war`), upload to `/opt/tomcat9/webapps/`

Start Tomcat
```bash
/opt/tomcat9/bin/startup.sh
```

### Front end
Node.js and npm install
```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash

\. "$HOME/.nvm/nvm.sh"

nvm install 22

node -v
npm -v
```
Package
```bash
cd /opt/Micro-headlines/front-end
npm install
npm run build
```

Nginx
```bash
sudo apt install nginx -y

sudo vim /etc/nginx/sites-available/default
```
```nginx
server {
    listen 80;
    server_name your_domain_name_or_your_server_ip;
    
    root /opt/Micro-headlines/front-end/dist;
    index index.html index.htm;
    
    # redirect to index.html
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # redirect base_url to backend
    location /api/ {
        proxy_pass http://localhost:8080/micro-headlines/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```
Enable Nginx
```bash
sudo ln -s /etc/nginx/sites-available/default /etc/nginx/sites-enabled/

# test
sudo nginx -t

# restart
sudo systemctl restart nginx
```

### Done
Access the website via `http://your_domain_name_or_your_server_ip`