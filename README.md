## 我的社区

## 资料
[Spring 文档](https://spring.io/guides)

[Spring WEB 文档](https://spring.io/guides/gs/serving-web-content/)

## 工具

- IDEA
- Git
- MyBaties


## 这是一款论坛，基于SpringBoot
![image](https://github.com/1061302569/community/tree/main/src/main/resources/static/img/project.png)
``` sql
-- auto-generated definition
create table USER
(
  ID           INTEGER default NEXT VALUE FOR "PUBLIC"."SYSTEM_SEQUENCE_D10380C6_75D8_4B27_A6B1_97B4A8F7A441"
    primary key,
  ACCOUNT_ID   VARCHAR(100),
  NAME         VARCHAR(50),
  TOKEN        CHAR(36),
  GMT_CREATE   BIGINT,
  GMT_MODIFIED BIGINT
);


```


```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```