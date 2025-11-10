# Micro Headlines

## Introduction

Java web practice project.

Source: https://www.bilibili.com/video/BV1UN411x7xe

Course: 
- https://www.bilibili.com/video/BV1UN411x7xe
- https://www.bilibili.com/video/BV1Fi4y1S7ix

## Architecture

MVC architecture, front and back are separated.

```mermaid
%%{init: {'theme':'base', 'themeVariables': {'actorBkg':'#E8E8FF', 'actorBorder':'#9B9BD5', 'actorTextColor':'#000000', 'lineColor':'#7B7BA7', 'labelBoxBkgColor':'rgba(0,0,0,0)', 'labelBoxBorderColor':'rgba(0,0,0,0)'}}}%%
sequenceDiagram
    box rgb(245, 255, 245) View Layer
    participant V as Vue Frontend
    end
    
    box rgb(255, 250, 240) Controller Layer
    participant I as Interceptor
    participant C as Controller
    end
    
    box rgb(248, 245, 255) Model Layer
    participant S as Service
    participant D as DAO
    participant E as Entity
    end
    
    participant DB as MySQL Database
    
    V->>I: HTTP Request
    I->>C: Route Dispatch
    C->>S: Call Business Logic
    S->>D: Call Data Access
    D->>E: Object Mapping
    D->>DB: Execute SQL via MyBatis
    
    DB-->>D: Return Data
    D-->>S: Return Entity
    S-->>C: Return Business Data
    C-->>V: JSON Response
```

**Tech stack**: Vue.js, Spring, SpringMVC, MyBatis, MySQL

## How to run

### Dependencies

- [Node.js 22](https://nodejs.org/)
- [JDK 1.8](https://www.oracle.com/java)
- [MySQL 8](https://www.mysql.com/)
- [Tomcat 9](https://tomcat.apache.org/)
- [Maven 3.9](https://maven.apache.org/)

### Local

Front end:
```bash
cd front-end
npm install

# local run
npm run dev
```

Database:
```sql
create database micro_headlines;
use micro_headlines;
source database/micro_headlines.sql;
```

Back end:
- Open project in IDEA.
- Configure Tomcat and run the back-end module.

Optional:
```bash
# configure database connection
cd back-end/resources
vim jdbc.properties

# deploy
mv micro-headlines.war /path/to/tomcat/webapps/
cd /path/to/tomcat/bin
sh startup.sh
```

### Server

[Ubuntu](./docs/ubuntu.md)

## API
- [API Documentation](./docs/api.md)

## Thanks

Cursor and Claude Code for page beautification.
